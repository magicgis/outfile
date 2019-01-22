'''
Created on 14 Dec 2016

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils

LOGGER_NAME_CLENER = 'abl'

PART_TYPE_STANDARD = 83
PART_TYPE_IP = 41
PART_TYPE_UNKNOWN = 0

class AirBusRemoteClean(CleanerCommon):
    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(AirBusRemoteClean, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)

    def _handleNextBatch(self):
        sql = '''
            SELECT TID, PART_NUM, PART_NAME, CAGE_CODE, ATA_CODE, PART_TYPE
            FROM PART_CLEAN
        where 
            clean_flag=0 and tid>%d
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
        for result in results:
            tid = result[0]
            self.currentNo = max(tid, self.currentNo)
            partNum = result[1]
            partName = result[2]
            fullCageCode = result[3]
            ataCode = result[4]
            partType = result[5]
            spn = self.generateSPN(partNum)
            cageCode = fullCageCode[1:]
            msn = fullCageCode[0]+'-'+cageCode
            item = Item(tid, partNum, partName, cageCode, spn ,msn, ataCode, partType)
            itemList.append(item)
        
        self.__checkAndSetPN_BSN(itemList)
        
        self.__insertOrUpdatePartTable(itemList)
        
        self.__insertOrUpdateAta(itemList)
        
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
        
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.isPartExists is False:
                seq = spnDict[item.msn+'-'+item.spn]['startSeq']
                spnDict[item.msn+'-'+item.spn]['startSeq'] += 1
                item.psn = '%04d'%(seq)+'-'+item.spn
                item.bsn = item.msn+'-'+item.psn

    def __insertOrUpdatePartTable(self, itemList):
        updatedList = filter(lambda item: item.isPartExists is True and item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList)
        insertedList = filter(lambda item: item.isPartExists is False and item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList)
        
        insertSql = '''INSERT INTO T_PART
        (BSN, MSN, PART_NUM, SHORT_PART_NUM,PART_NAME,CAGE_CODE,        
        ATA_CHAPTER_SECTION,SRC_AIRBUS
        ) 
        VALUES 
        '''
        insertValues = list()
        for item in insertedList:
            insertValues.append(
                                '''("%s","%s","%s","%s","%s","%s",                                
                                %s, "%d"
                                )''' %(
                                    item.bsn, item.msn, item.partNum.replace('"','\\"'), item.spn,
                                    item.partName.replace('"','\\"'), item.cageCode,
                                    self.genColValueNum(item.ataCode), item.tid
                                    )
                                )
        if len(insertValues)>0:
            insertSql = insertSql + ','.join(insertValues)
            self.dstDbProxy.execute(insertSql)
        
        for item in updatedList:
            updateSql = '''UPDATE T_PART SET 
                        part_name = "%s", src_airbus = "%d"
            '''%(item.partName, item.tid)
            updateSql += ' where BSN = "%s" ' % (item.bsn)                
            self.dstDbProxy.execute(updateSql)

    def __insertOrUpdateAta(self, itemList):
        insertSql = 'INSERT INTO R_PART_ATA (BSN, ATA_CODE) VALUES '
        values = list()
        for item in itemList:
            values.append('("%s","%s")' %(item.bsn, item.ataCode))
        if len(values)>0:
            insertSql += ','.join(values)
            self.dstDbProxy.execute(insertSql)

    def __updateOriginal(self, itemList):
        cleanItemList = map(lambda item: str(item.tid), filter(lambda item: item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList))
        unMatchItemList = map(lambda item: str(item.tid), filter(lambda item: item.cleanFlag == CleanerCommon.CLEAN_FLAG_UNMATCH_PART_NAME, itemList))
        
        updateSql = 'UPDATE part_clean set clean_flag=%d where tid in (%s)'
        if len(cleanItemList) >0:
            self.srcDbProxy.execute(updateSql%(CleanerCommon.CLEAN_FLAG_CLEAN, ','.join(cleanItemList)))
            #self.logger.debug('Update original clean_flag=1 for tid:%s', ','.join(cleanItemList))
        if len(unMatchItemList) >0:
            self.srcDbProxy.execute(updateSql%(CleanerCommon.CLEAN_FLAG_UNMATCH_PART_NAME, ','.join(unMatchItemList)))
            #self.logger.debug('Update original clean_flag=2 for tid:%s', ','.join(duplicateItemList))
        
class Item(object):
    def __init__(self, tid,partNum,partName,cageCode,spn, msn, ataCode, partType):
        self.tid = tid
        self.partNum = partNum
        self.partName = partName
        self.cageCode = cageCode
        self.spn = spn
        self.msn = msn
        self.psn = None
        self.bsn = None
        self.ataCode = ataCode
        self.partType = partType
        self.isPartExists = False
        self.cleanFlag = CleanerCommon.CLEAN_FLAG_CLEAN
            