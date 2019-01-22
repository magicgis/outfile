'''
Created on Aug 22, 2016

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils
import time

LOGGER_NAME_CLENER = 'cns' 
class NSNCleaner(CleanerCommon):
    '''
    classdocs
    '''


    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(NSNCleaner, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)
    
    def _handleNextBatch(self):
        sql = '''
        select r_cage_nsn_part.tid as tid, 
                r_cage_nsn_part.cage_id as cage_id, 
                r_cage_nsn_part.part_num as part_num, 
                r_cage_nsn_part.nsn_name as part_name,
                r_cage_nsn_part.nsn_id as nsn_id,
                r_nsn_replace.replacedby_nsn_id as replacedby_nsn_id,
                t_cage_info.cage_name as cage_name  
        from r_cage_nsn_part 
        join t_cage_info on r_cage_nsn_part.cage_id=t_cage_info.cage_id
        left join r_nsn_replace on 
            r_cage_nsn_part.nsn_id = r_nsn_replace.nsn_id and
            r_cage_nsn_part.part_num = r_nsn_replace.part_num and
            r_cage_nsn_part.cage_id = r_nsn_replace.cage_id
        where
            r_cage_nsn_part.clean_flag=0 and r_cage_nsn_part.tid>%d
            and r_cage_nsn_part.part_num<>'' 
        order by r_cage_nsn_part.tid asc limit %d
        
        '''% (self.currentNo, self.batchSize)
        
        #stime = time.time()
        self.srcDbProxy.execute(sql)
        results = self.srcDbProxy.cur.fetchall()
        #self.logger.debug('%f seconds for query src', time.time()-stime)

        if len(results)==0:
            self.processFinish = True
            return
        finishFlag = False
        if len(results)<self.batchSize:
            finishFlag = True
        
        resultBatch = dict()
        keySet = set()
        tidSet = set()
        spnDict = dict()
        for result in results:
            tid = result[0]
            tidSet.add(tid)
            cageCode = result[1].upper()
            partNum = result[2].upper()
            partName = result[3]
            nsnId = result[4]
            key = cageCode+nsnId+partNum
            if key in keySet:
                continue
            keySet.add(key)
            replacedNsn = result[5]
            cageName = result[6]
            item = Item(tid, cageCode, nsnId, partNum, partName, replacedNsn, cageName)
            resultBatch[key] = item
        
        self.currentNo = max(tidSet)
        
        #check to see if the dst db already has the items and set the flag to 0 for those items
        #stime = time.time()        
        self.__checkExistence(resultBatch)
        #self.logger.debug('%f seconds for __checkExistence', time.time()-stime)
        nullCageCodeCount = len(filter(lambda item: item.cleanFlag == CleanerCommon.CLEAN_FLAG_CLEAN and (item.cageCode is None or item.cageCode == ''), 
                                   resultBatch.values()))
        #generate the self-defined cage code
        if nullCageCodeCount>0:
            cageCodeList = self.generateCageCodeList(nullCageCodeCount)
        cageCodeListIndex = 0

        #calculate the spn
        for key in resultBatch:
            item = resultBatch[key]
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.cageCode is None or item.cageCode=='':
                item.cageCode = cageCodeList[cageCodeListIndex]
                cageCodeListIndex+=1
                item.msn = self.generateMsn(item.cageCode, True)
            else:
                item.msn = self.generateMsn(item.cageCode, False)
            
            item.spn = self.generateSPN(item.partNum)
            if item.msn+'-'+item.spn in spnDict:
                spnDict[item.msn+'-'+item.spn]['count']+=1
            else:
                spnDict[item.msn+'-'+item.spn]={'count':1,'startSeq':0}
        
        #self.logger.debug('cageCodeListIndex:%d', cageCodeListIndex)
        #generate the psn seq
        if len(spnDict)>0:
            #stime = time.time()
            self.generatePSNSeq(spnDict)
            #self.logger.debug('%f seconds for generatePSNSeq', time.time()-stime)
        
        #generate the msn, psn & bsn
        for key in resultBatch:
            item = resultBatch[key]
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            seq = spnDict[item.msn+'-'+item.spn]['startSeq']
            spnDict[item.msn+'-'+item.spn]['startSeq'] += 1
            item.psn = '%04d'%(seq)+'-'+item.spn
            item.bsn = item.msn+'-'+item.psn
        
        #stime = time.time()
        self.__insertOrUpdatePartTable(resultBatch.values())
        #self.logger.debug('%f seconds for __insertOrUpdatePartTable', time.time()-stime)
        #self.logger.debug('after __insertOrUpdatePartTable')
        #stime = time.time()
        self.__insertOrUpdateManTable(resultBatch.values())
        #self.logger.debug('%f seconds for __insertOrUpdateManTable', time.time()-stime)
        #self.logger.debug('after __insertOrUpdateManTable')
        #stime = time.time()
        self.__insertRPartTable(resultBatch.values())
        #self.logger.debug('%f seconds for __insertRPartTable', time.time()-stime)
        #self.logger.debug('after __insertRPartTable')
        #stime = time.time()
        self.__insertRNsnTable(resultBatch.values())
        #self.logger.debug('%f seconds for __insertRNsnTable', time.time()-stime)
        #self.logger.debug('after __insertRNsnTable')
        #stime = time.time()
        self.__updateOriginal(resultBatch.values())
        #self.logger.debug('%f seconds for __updateOriginal', time.time()-stime)
        #self.logger.debug('after __updateOriginal')
        self.dstDbProxy.commit()
        self.srcDbProxy.commit()
        if finishFlag:
            self.processFinish = True
            
    def __checkExistence(self, resultDict):
        sql = 'select cage_code, nsn, part_num from t_part where '
        whereList = list()
        for item in resultDict.values():
            whereList.append(' (cage_code="%s" and nsn="%s" and part_num="%s") ' %(item.cageCode, item.nsnId, item.partNum.replace('"','\\"')))
        
        self.dstDbProxy.execute(sql+' or '.join(whereList))
        results = self.dstDbProxy.cur.fetchall()
        for result in results:
            key = result[0]+result[1]+result[2]
            resultDict[key].cleanFlag = CleanerCommon.CLEAN_FLAG_DUPLICATE
        
    def __insertOrUpdatePartTable(self, itemList):
        #check if BSN exists
        bsnList = map(lambda item: '"'+item.bsn+'"', filter(lambda item: item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList))
        if len(bsnList) >0:
            sql = 'SELECT BSN,MSN_FLAG from t_part where BSN IN (%s)' % ','.join(bsnList)
            self.dstDbProxy.execute(sql)        
            existentBsnDict = dict(self.dstDbProxy.cur.fetchall())
        else:
            existentBsnDict = dict()
        insertValues = list()
        insertSql = '''INSERT INTO T_PART
        (BSN, MSN, PART_NUM, SHORT_PART_NUM,PART_NAME,CAGE_CODE,NSN,REPLACED_NSN,SRC_NSN) VALUES 
        '''
        updateItems = set()
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.bsn in existentBsnDict:
                if existentBsnDict[item.bsn]=='0':
                    updateItems.add('"'+item.bsn+'"')
                continue
            existentBsnDict[item.bsn]='0'
            insertValues.append(
                              '("%s","%s","%s","%s","%s","%s","%s",%s,"%d")'%(
                                    item.bsn,
                                    item.msn,
                                    item.partNum.replace('"','\\"'),
                                    item.spn,
                                    item.partName.replace('"','\\"'),
                                    item.cageCode,
                                    item.nsnId,
                                    self.genColValueStr(item.replacedNsn),
                                    item.tid
                                )
                              )
        if len(insertValues)>0:
            insertSql = insertSql + ','.join(insertValues)
            self.dstDbProxy.execute(insertSql)
        
        if len(updateItems)>0:
            updateSql = 'UPDATE T_PART SET msn_flag="1" where bsn in (%s)'
            self.dstDbProxy.execute(updateSql+','.join(updateItems))

    
    def __insertRPartTable(self, itemList):
        sql = 'SELECT BSN,MSN FROM R_PART_MSN where '
        whereList = list()
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            whereList.append(' (BSN="%s" and MSN="%s") '% (item.bsn, item.msn))
        if len(whereList)>0:
            self.dstDbProxy.execute(sql+' or '.join(whereList))
            existentItems = map(lambda result: result[0]+'_'+result[1],self.dstDbProxy.cur.fetchall())
        else:
            existentItems = list()
        insertSql = 'INSERT INTO R_PART_MSN (BSN, MSN) VALUES '
        values = list()
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.bsn+'_'+item.msn in existentItems:
                continue
            values.append('("%s","%s")' %(item.bsn, item.msn))
        if len(values)>0:
            insertSql += ','.join(values)
            self.dstDbProxy.execute(insertSql)

    def __insertRNsnTable(self, itemList):
        sql = 'SELECT BSN, NSN FROM R_PART_NSN where '
        whereList = list()
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            whereList.append(' (BSN="%s" and NSN="%s") ')
        if len(whereList)>0:
            self.dstDbProxy.execute(sql+' or '.join(whereList))
            existentItems = map(lambda result: result[0]+'_'+result[1],self.dstDbProxy.cur.fetchall())
        else:
            existentItems = list()
            
        insertSql = 'INSERT INTO R_PART_NSN (BSN, NSN, REPLACED_NSN) VALUES '
        values = list()
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.bsn+'_'+item.msn in existentItems:
                continue
            existentItems.append(item.bsn+'_'+item.msn)
            values.append('("%s","%s",%s)' %(item.bsn, item.nsnId, self.genColValueStr(item.replacedNsn)))
        if len(values)>0:
            insertSql += ','.join(values)
            self.dstDbProxy.execute(insertSql)
        
        
    def __insertOrUpdateManTable(self, itemList):
        #check existence
        sql = 'SELECT MSN from T_MANUFACTORY where MSN in (%s)'
        values = map(lambda item: '"'+item.msn+'"', 
                     filter(
                            lambda item: item.cleanFlag == CleanerCommon.CLEAN_FLAG_CLEAN, 
                            itemList))
        if len(values) >0:
            self.dstDbProxy.execute(sql%','.join(set(values)))
            existentMsnSet = set(map(lambda result: result[0],self.dstDbProxy.cur.fetchall()))
        else:
            existentMsnSet = set()
        
        #insert
        insertSql = 'INSERT INTO T_MANUFACTORY (MSN, CAGE_CODE, MAN_NAME, SRC_NSN) values '
        insertValues = list()
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.msn in existentMsnSet:
                continue
            existentMsnSet.add(item.msn)
            insertValues.append('("%s","%s","%s","%d")'%(
                                    item.msn,
                                    item.cageCode,
                                    item.cageName.replace('"','\\"'),
                                    item.tid
                                )
                            )
        if len(insertValues)>0:
            self.dstDbProxy.execute(insertSql+','.join(insertValues))     
    
    def __updateOriginal(self, itemList):
        cleanItemList = map(lambda item: str(item.tid), filter(lambda item: item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList))
        duplicateItemList = map(lambda item: str(item.tid), filter(lambda item: item.cleanFlag == CleanerCommon.CLEAN_FLAG_DUPLICATE, itemList))
        
        updateSql = 'UPDATE r_cage_nsn_part set clean_flag=%d where tid in (%s)'
        if len(cleanItemList) >0:
            self.srcDbProxy.execute(updateSql%(CleanerCommon.CLEAN_FLAG_CLEAN, ','.join(cleanItemList)))
        if len(duplicateItemList) >0:
            self.srcDbProxy.execute(updateSql%(CleanerCommon.CLEAN_FLAG_DUPLICATE, ','.join(duplicateItemList)))
        
    
class Item(object):
    def __init__(self, tid, cageCode, nsnId, partNum, partName, replacedNsn, cageName):
        self.tid = tid
        self.cageCode = cageCode
        self.nsnId = nsnId
        self.partNum = partNum
        self.partName = partName
        self.replacedNsn = replacedNsn
        self.cageName = cageName
        self.spn = None
        self.msn = None
        self.psn = None
        self.bsn = None
        self.cleanFlag = CleanerCommon.CLEAN_FLAG_CLEAN

if __name__ == '__main__':
    '''
    if PIDUtils.isPidFileExist(LOGGER_NAME_CRAWL):
        print 'Previous process is on-going, please stop it firstly'
        sys.exit(1)
    '''
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CLENER, pid)
    Logging.initLogger('conf/crawler.logging.cfg')
    ins = NSNCleaner(os.path.join('conf',LOGGER_NAME_CLENER+'.cfg'))
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CLENER, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                    