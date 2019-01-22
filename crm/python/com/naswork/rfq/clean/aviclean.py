'''
@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils

LOGGER_NAME_CLENER = 'cav'
class AVICleaner(CleanerCommon):
    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(AVICleaner, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)

    def _handleNextBatch(self):
        sql = '''
        select tid,catalog, part_num, description, cage_code,manufacturer,nsn,usml,hazmat_code,shelf_life,ata_code,
            weight,dimensions,country_of_origin,eccn,harmonize_code,schedule_b_code,img_path
        from r_part_info
        where 
            clean_flag=0 and tid>%d
            and cage_code<>'' 
        order by tid asc limit %d
        '''% (self.currentNo, self.batchSize)
        self.srcDbProxy.execute(sql)
        results = self.srcDbProxy.cur.fetchall()
        if len(results)==0:
            self.processFinish = True
            return
        finishFlag = False
        if len(results)<self.batchSize:
            finishFlag = True
        
        itemList = list()
        tidSet = set()
        for result in results:
            tid = result[0]
            tidSet.add(tid)
            item = Item(result[0], result[1].upper(), result[2].upper(), result[3].upper(),result[4].upper(), 
                        result[5].upper(), result[6], result[7].upper(),result[8].upper(), result[9], 
                        result[10].upper(), result[11].upper(),result[12].upper(), result[13].upper(),
                        result[14].upper(),result[15].upper(), result[16].upper(), result[17], 
                        self.generateSPN(result[2].upper()), self.generateMsn(result[4].upper(), False))
            itemList.append(item)
        
        self.currentNo = max(tidSet)
        
        self.__checkAndSetPN_BSN(itemList)

        self.__insertOrUpdatePartTable(itemList)
        
        self.__insertOrUpdateManTable(itemList)
        
        self.__insertRPartTable(itemList)
        
        self.__insertRNsnTable(itemList)
        
        self.__updateOriginal(itemList)
        
        self.dstDbProxy.commit()
        self.srcDbProxy.commit()
        if finishFlag:
            self.processFinish = True
                
    def __checkAndSetPN_BSN(self, itemList):
        sql = 'select bsn, cage_code, part_num, part_name from T_PART where '
        whereList = list()
        for item in itemList:
            whereList.append(' (cage_code="%s" and part_num="%s") ' %(item.cageCode, item.partNum.replace('"','\\"')))
        self.dstDbProxy.execute(sql+' or '.join(whereList))
        results = self.dstDbProxy.cur.fetchall()
        resultDict = dict()
        for result in results:
            key = result[1]+'-'+result[2]
            if key not in resultDict:
                resultDict[key] = list()
            resultDict[key].append((result[0], result[3]))
        
        spnDict = dict()
        for item in itemList:
            key = item.cageCode+'-'+item.partNum
            if key in resultDict:
                #if the cagecode/partnum exists, find out the "best matched" one
                # the best matched one is that it contains the most number of same name
                names = set(filter(lambda name: name!='',map(lambda name: name.strip(),item.partName.replace(':',',').split(','))))
                maxMatchLength = 0
                matchedBsn = None
                matchedNames = None
                for (bsn, partName) in resultDict[key]:
                    existingNames = set(filter(lambda name: name!='',map(lambda name: name.strip(), partName.split(','))))
                    matchLength = len(names.intersection(existingNames))
                    if matchLength>maxMatchLength:
                        matchedBsn = bsn
                        maxMatchLength = matchLength
                        matchedNames = existingNames
                if matchedBsn is not None:
                    item.isPartExists = True
                    item.bsn = matchedBsn
                    #self.logger.debug('Match for %s, orig:%s, avi:%s',item.bsn, matchedNames, names)
                    names.update(matchedNames)
                    item.partName = ','.join(names)
                else:
                    item.cleanFlag = CleanerCommon.CLEAN_FLAG_UNMATCH_PART_NAME
                    #self.logger.debug('no match for %s-%s', item.cageCode, item.partNum)
                    
            if item.isPartExists is False:
                if item.msn+'-'+item.spn in spnDict:
                    spnDict[item.msn+'-'+item.spn]['count']+=1
                else:
                    spnDict[item.msn+'-'+item.spn]={'count':1,'startSeq':0}
                    
        if len(spnDict)>0:
            self.generatePSNSeq(spnDict)
        
        #check nsn
        existingList = filter(lambda item: item.isPartExists is True, itemList)
        sql = 'SELECT BSN,NSN FROM R_PART_NSN WHERE '
        whereList = list()
        for item in existingList:
            whereList.append(' (BSN="%s" and NSN="%s" ) ' %(item.bsn, item.nsn))
        if len(whereList) > 0:
            self.dstDbProxy.execute(sql+' or '.join(whereList))
            existingNsnBsn = set(map(lambda result: result[1]+'-'+result[0],self.dstDbProxy.cur.fetchall()))
        else:
            existingNsnBsn = set()
            
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.isPartExists is False:
                seq = spnDict[item.msn+'-'+item.spn]['startSeq']
                spnDict[item.msn+'-'+item.spn]['startSeq'] += 1
                item.psn = '%04d'%(seq)+'-'+item.spn
                item.bsn = item.msn+'-'+item.psn
            else:
                if item.nsn+'-'+item.bsn in existingNsnBsn:
                    item.isNsnExists = True
        
    def __insertOrUpdatePartTable(self, itemList):
        updatedList = filter(lambda item: item.isPartExists is True and item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList)
        insertedList = filter(lambda item: item.isPartExists is False and item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList)
        
        insertSql = '''INSERT INTO T_PART
        (BSN, MSN, PART_NUM, SHORT_PART_NUM,PART_NAME,CAGE_CODE,
        NSN,WEIGHT, DIMENTIONS,COUNTRY_OF_ORIGIN,ECCN,SCHEDULE_B_CODE,SHELF_LIFE,
        ATA_CHAPTER_SECTION,USML,HAZMAT_CODE,IMG_PATH, SRC_AVI
        ) 
        VALUES 
        '''
        insertValues = list()
        for item in insertedList:
            insertValues.append(
                                '''("%s","%s","%s","%s","%s","%s",
                                %s, %s, %s, %s, %s, %s, %s,
                                %s,%s,%s,%s, "%d"
                                )''' %(
                                    item.bsn, item.msn, item.partNum.replace('"','\\"'), item.spn,
                                    item.partName.replace('"','\\"'), item.cageCode,
                                    self.genColValueStr(item.nsn), self.genColValueStr(item.weight), 
                                    self.genColValueStr(item.dimensions), self.genColValueStr(item.countryOfOrigin),
                                    self.genColValueStr(item.eccn), self.genColValueStr(item.scheduleBCode),
                                    self.genColValueNum(item.shelfLife), self.genColValueNum(item.ataCode), self.genColValueStr(item.usml), 
                                    self.genColValueStr(item.hazmatCode), self.__extractImgPath(item.imgPath), item.tid
                                    )
                                )
        if len(insertValues)>0:
            insertSql = insertSql + ','.join(insertValues)
            self.dstDbProxy.execute(insertSql)
        
        for item in updatedList:
            updateSql = '''UPDATE T_PART SET WEIGHT=%s, DIMENTIONS=%s,COUNTRY_OF_ORIGIN=%s,
                        ECCN=%s,SCHEDULE_B_CODE=%s,SHELF_LIFE=%s,ATA_CHAPTER_SECTION=%s,USML=%s,
                        HAZMAT_CODE=%s,IMG_PATH=%s,SRC_AVI="%d"
            '''%(self.genColValueStr(item.weight),self.genColValueStr(item.dimensions),
                 self.genColValueStr(item.countryOfOrigin),self.genColValueStr(item.eccn),
                 self.genColValueStr(item.scheduleBCode),self.genColValueNum(item.shelfLife), self.genColValueNum(item.ataCode), 
                 self.genColValueStr(item.usml),self.genColValueStr(item.hazmatCode),
                 self.__extractImgPath(item.imgPath), item.tid)
            updateSql+=",NSN=CASE WHEN NSN IS NULL OR NSN='' THEN %s ELSE NSN END " %(self.genColValueStr(item.nsn))
            updateSql += ' where BSN = "%s" ' % (item.bsn)                
            self.dstDbProxy.execute(updateSql)
    
    
    def __insertOrUpdateManTable(self, itemList):
        cageNameDict = dict()
        #check existence firstly
        sql = 'select cage_code, MAN_NAME from T_MANUFACTORY where '
        whereSet = set()
        for item in itemList:
            if item.cleanFlag!=CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            whereSet.add(' (cage_code="%s") ' %(item.cageCode))
        self.dstDbProxy.execute(sql+' or '.join(whereSet))
        results = self.dstDbProxy.cur.fetchall()
        for result in results:
            key = result[0]
            cageNameDict[key] = set(filter(lambda name: name!='',map(lambda x: x.strip().upper(), result[1].split(','))))
        
        insertSql = 'INSERT INTO T_MANUFACTORY (MSN, CAGE_CODE, MAN_NAME, SRC_AVI) values '
        updateSql = 'UPDATE T_MANUFACTORY SET MAN_NAME="%s", SRC_AVI="%d" where MSN="%s"'
        insertValues = list()
        updateSqlList = list()
        for item in itemList:
            if item.cleanFlag!=CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.cageCode in cageNameDict:
                msnNames = set(filter(lambda name: name!='',map(lambda x: x.strip(), item.cageName.split(','))))
                origLength = len(cageNameDict[item.cageCode])
                for name in msnNames:
                    if name not in cageNameDict[item.cageCode]:
                        cageNameDict[item.cageCode].add(name)
                if len(cageNameDict[item.cageCode])> origLength:
                    #self.logger.debug('Length for %s:%d', item.cageCode, len(','.join(cageNameDict[item.cageCode])))
                    sql = updateSql % (','.join(map(lambda n: n.replace('"','\\"'),cageNameDict[item.cageCode])), item.tid, item.msn)
                    updateSqlList.append(sql)
                    #self.logger.debug('update cage code:%s', item.cageCode)
            else:
                insertValues.append('("%s","%s","%s","%d")'%(
                                    item.msn,
                                    item.cageCode,
                                    item.cageName.replace('"','\\"'),
                                    item.tid
                                )
                            )
                cageNameDict[item.cageCode] = set(filter(lambda name: name!='',map(lambda x: x.strip(), item.cageName.split(','))))
                
        if len(insertValues)>0:
            self.dstDbProxy.execute(insertSql+','.join(insertValues))
        for updateSql in updateSqlList:
            self.dstDbProxy.execute(updateSql)
    def __insertRPartTable(self, itemList):
        sql = 'SELECT BSN,MSN FROM R_PART_MSN where '
        whereList = list()
        for item in itemList:
            if item.cleanFlag!=CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            whereList.append(' (BSN="%s" and MSN="%s") ' % (item.bsn, item.msn))
        if len(whereList)>0:
            self.dstDbProxy.execute(sql+' or '.join(whereList))
            existentItems = set(map(lambda result: result[0]+'_'+result[1],self.dstDbProxy.cur.fetchall()))
        else:
            existentItems = set()
        insertSql = 'INSERT INTO R_PART_MSN (BSN, MSN) VALUES '
        values = list()
        for item in itemList:
            if item.cleanFlag!=CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.bsn+'_'+item.msn in existentItems:
                continue
            existentItems.add(item.bsn+'_'+item.msn)
            values.append('("%s","%s")' %(item.bsn, item.msn))
        if len(values)>0:
            insertSql += ','.join(values)
            self.dstDbProxy.execute(insertSql)
    
    def __insertRNsnTable(self, itemList):
        insertList = filter(lambda item: item.isPartExists is False or item.isNsnExists is False, itemList)
        insertSql = 'INSERT INTO R_PART_NSN (BSN, NSN) VALUES '
        values = list()
        for item in insertList:
            if item.cleanFlag!=CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.nsn is None or item.nsn=='':
                continue
            values.append('("%s","%s")' %(item.bsn, item.nsn))
        if len(values)>0:
            insertSql += ','.join(set(values))
            self.dstDbProxy.execute(insertSql)
    
    def __extractImgPath(self, path):
        if path is None or path=='':
            return 'null'
        rindex = path.rfind('/')
        if rindex>=0:
            return '"'+path[rindex+1:]+'"'
        else:
            return '"'+path+'"'

    def __updateOriginal(self, itemList):
        cleanItemList = map(lambda item: str(item.tid), filter(lambda item: item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList))
        unMatchItemList = map(lambda item: str(item.tid), filter(lambda item: item.cleanFlag == CleanerCommon.CLEAN_FLAG_UNMATCH_PART_NAME, itemList))
        
        updateSql = 'UPDATE r_part_info set clean_flag=%d where tid in (%s)'
        if len(cleanItemList) >0:
            self.srcDbProxy.execute(updateSql%(CleanerCommon.CLEAN_FLAG_CLEAN, ','.join(cleanItemList)))
            #self.logger.debug('Update original clean_flag=1 for tid:%s', ','.join(cleanItemList))
        if len(unMatchItemList) >0:
            self.srcDbProxy.execute(updateSql%(CleanerCommon.CLEAN_FLAG_UNMATCH_PART_NAME, ','.join(unMatchItemList)))
            #self.logger.debug('Update original clean_flag=2 for tid:%s', ','.join(duplicateItemList))

class Item(object):
    def __init__(self, tid,catalog,partNum,description,cageCode,manufacturer,nsn,usml,hazmatCode,shelfLife,ataCode,
                 weight,dimensions,countryOfOrigin,eccn,harmonizeCode,scheduleBCode,imgPath, spn, msn):
        self.tid = tid
        self.catalog =catalog
        self.partNum = partNum
        self.partName = description
        self.cageCode = cageCode
        self.cageName = manufacturer
        self.nsn = nsn
        self.usml = usml
        self.hazmatCode = hazmatCode
        self.shelfLife = shelfLife
        self.ataCode = ataCode
        self.weight = weight
        self.dimensions = dimensions
        self.countryOfOrigin = countryOfOrigin
        self.eccn = eccn
        self.harmonizeCode = harmonizeCode
        self.scheduleBCode = scheduleBCode
        self.imgPath = imgPath
        self.spn = spn
        self.msn = msn
        self.psn = None
        self.bsn = None
        self.isPartExists = False
        self.isNsnExists = False
        self.cleanFlag = CleanerCommon.CLEAN_FLAG_CLEAN

if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CLENER, pid)
    Logging.initLogger('conf/crawler.logging.cfg')
    ins = AVICleaner(os.path.join('conf',LOGGER_NAME_CLENER+'.cfg'))
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CLENER, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                        