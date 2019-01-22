'''
Created on Aug 29, 2016

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils
import time

LOGGER_NAME_CLENER = 'csa' 
class SATCleaner(CleanerCommon):
    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(SATCleaner, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)

    def _handleNextBatch(self):
        sql = '''
        select tid,cage_id, cage_name, part_num, part_name 
        from r_part_info
        where
            clean_flag=0 and tid>%d
            and part_num<>'' 
        order by tid asc limit %d
        ''' % (self.currentNo, self.batchSize)

        self.srcDbProxy.execute(sql)
        results = self.srcDbProxy.cur.fetchall()
        if len(results)==0:
            self.processFinish = True
            return
        finishFlag = False
        if len(results)<self.batchSize:
            finishFlag = True

        resultBatch = dict()
        cageNameDict = dict()
        keySet = set()
        tidSet = set()
        spnDict = dict()
        
        for result in results:
            tid = result[0]
            tidSet.add(tid)
            cageId = result[1]
            partNum = result[3].upper()
            
            key = cageId+partNum
            if key in keySet:
                continue
            keySet.add(key)
            cageName = result[2].upper()
            partName = result[4].upper()
            
            item = Item(tid, cageId, cageName, partNum, partName)
            resultBatch[key] = item
            if cageId not in cageNameDict:
                cageNameDict[cageId] = list()
            
        self.currentNo = max(tidSet)
        
        self.__checkPartExistence(resultBatch)
        
        self.__checkMSNExistence(cageNameDict)

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
            if item.isPartExists==True:
                continue
            #generate msn
            if item.cageCode is None or item.cageCode=='':
                item.cageCode = cageCodeList[cageCodeListIndex]
                cageCodeListIndex+=1
                item.msn = self.generateMsn(item.cageCode, True)
            else:
                item.msn = self.generateMsn(item.cageCode, False)
            #generate spn
            item.spn = self.generateSPN(item.partNum)
            if item.msn+'-'+item.spn in spnDict:
                spnDict[item.msn+'-'+item.spn]['count']+=1
            else:
                spnDict[item.msn+'-'+item.spn]={'count':1,'startSeq':0}


        if len(spnDict)>0:
            self.generatePSNSeq(spnDict)

        for key in resultBatch:
            item = resultBatch[key]
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.isPartExists == True:
                continue
            seq = spnDict[item.msn+'-'+item.spn]['startSeq']
            spnDict[item.msn+'-'+item.spn]['startSeq'] += 1
            item.psn = '%04d'%(seq)+'-'+item.spn
            item.bsn = item.msn+'-'+item.psn

        self.__insertOrUpdatePartTable(resultBatch.values())

        self.__insertOrUpdateManTable(resultBatch.values(), cageNameDict)

        self.__insertRPartTable(resultBatch.values())

        self.__updateOriginal(resultBatch.values())
        
        self.dstDbProxy.commit()
        self.srcDbProxy.commit()
        
        if finishFlag:
            self.processFinish = True
        
    def __checkPartExistence(self, resultDict):
        sql = 'select bsn, SHORT_PART_NUM, cage_code, part_num, part_name from T_PART where '
        whereList = list()
        for item in resultDict.values():
            whereList.append(' (cage_code="%s" and part_num="%s") ' %(item.cageCode, item.partNum.replace('"','\\"')))
        self.dstDbProxy.execute(sql+' or '.join(whereList))
        results = self.dstDbProxy.cur.fetchall()
        for result in results:
            key = result[2]+result[3]
            resultDict[key].isPartExists = True
            resultDict[key].origPartName = map(lambda x: x.strip().upper(), result[4].split(','))
            resultDict[key].bsn = result[0]
            resultDict[key].spn = result[1]
            resultDict[key].msn = self.generateMsn(result[2], False)

    def __checkMSNExistence(self, cageNameDict):
        sql = 'select cage_code, MAN_NAME from T_MANUFACTORY where '
        whereList = list()
        for cageCode in cageNameDict.keys():
            whereList.append(' (cage_code="%s") ' %(cageCode))
        self.dstDbProxy.execute(sql+' or '.join(whereList))
        results = self.dstDbProxy.cur.fetchall()
        for result in results:
            key = result[0]
            cageNameDict[key] = map(lambda x: x.strip().upper(), result[1].split(','))

    def __insertOrUpdatePartTable(self, itemList):
        updatedItems = filter(lambda item: item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN and item.isPartExists==True, itemList)
        newItems = filter(lambda item: item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN and item.isPartExists==False, itemList)
        #check if BSN exists
        bsnList = map(lambda item: '"'+item.bsn+'"', newItems)
        if len(bsnList) >0:
            sql = 'SELECT BSN,MSN_FLAG from t_part where BSN IN (%s)' % ','.join(bsnList)
            self.dstDbProxy.execute(sql)        
            existentBsnDict = dict(self.dstDbProxy.cur.fetchall())
        else:
            existentBsnDict = dict()
        insertValues = list()
        insertSql = '''INSERT INTO T_PART
        (BSN, MSN, PART_NUM, SHORT_PART_NUM,PART_NAME,CAGE_CODE,SRC_SAT) VALUES 
        '''
        updatedFlagItems = set()
        for item in newItems:
            if item.bsn in existentBsnDict:
                if existentBsnDict[item.bsn]=='0':
                    updatedFlagItems.add('"'+item.bsn+'"')
                continue
            existentBsnDict[item.bsn]='0'
            insertValues.append(
                              '("%s","%s","%s","%s","%s","%s","%d")'%(
                                    item.bsn,
                                    item.msn,
                                    item.partNum.replace('"','\\"'),
                                    item.spn,
                                    item.partName.replace('"','\\"'),
                                    item.cageCode,
                                    item.tid
                                )
                              )
        if len(insertValues)>0:
            insertSql = insertSql + ','.join(insertValues)
            self.dstDbProxy.execute(insertSql)
            #self.logger.debug('insert new part:%s', insertSql)
            
        
        if len(updatedFlagItems)>0:
            updateSql = 'UPDATE T_PART SET msn_flag="1" where bsn in (%s)'
            self.dstDbProxy.execute(updateSql+','.join(updatedFlagItems))
            #self.logger.debug('update flag for bsn:%s', updatedFlagItems)
        
        if len(updatedItems) > 0:
            for item in updatedItems:
                updatedNames = list()
                updatedNames.extend(item.origPartName)
                partNames = map(lambda x: x.upper(), item.partName.split(','))
                for name in partNames:
                    if name not in updatedNames:
                        updatedNames.append(name)
                if len(updatedNames)> len(item.origPartName):
                    updateSql = 'UPDATE T_PART SET PART_NAME = "%s", SRC_SAT=%d where bsn="%s"' % (','.join(map(lambda n: n.replace('"','\\"')  ,updatedNames)), item.tid, item.bsn)
                    self.dstDbProxy.execute(updateSql)
                    #self.logger.debug('Update for part: %s, len of names:%d', item.bsn, len(','.join(updatedNames)))

    def __insertOrUpdateManTable(self, itemList, cageNameDict):
        #insert
        insertSql = 'INSERT INTO T_MANUFACTORY (MSN, CAGE_CODE, MAN_NAME, SRC_SAT) values '
        updateSql = 'UPDATE T_MANUFACTORY SET MAN_NAME="%s", SRC_SAT="%d" where MSN="%s"'
        insertValues = list()
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.cageCode in cageNameDict:
                msnNames = map(lambda x: x.upper(), item.cageName.split(','))
                origLength = len(cageNameDict[item.cageCode])
                for name in msnNames:
                    if name not in cageNameDict[item.cageCode]:
                        cageNameDict[item.cageCode].append(name)
                if len(cageNameDict[item.cageCode])> origLength:
                    #self.logger.debug('Length for %s:%d', item.cageCode, len(','.join(cageNameDict[item.cageCode])))
                    sql = updateSql % (','.join(map(lambda n: n.replace('"','\\"'),cageNameDict[item.cageCode])), item.tid, item.msn)
                    self.dstDbProxy.execute(sql)
                    #self.logger.debug('update cage code:%s', item.cageCode)
            else:
                insertValues.append('("%s","%s","%s","%d")'%(
                                    item.msn,
                                    item.cageCode,
                                    item.cageName.replace('"','\\"'),
                                    item.tid
                                )
                            )
                
        if len(insertValues)>0:
            self.dstDbProxy.execute(insertSql+','.join(insertValues))
            #self.logger.debug('Insert msn: %s', insertSql+','.join(insertValues))     

    def __insertRPartTable(self, itemList):
        sql = 'SELECT BSN,MSN FROM R_PART_MSN where '
        whereList = list()
        for item in itemList:
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
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
            if item.cleanFlag != CleanerCommon.CLEAN_FLAG_CLEAN:
                continue
            if item.bsn+'_'+item.msn in existentItems:
                continue
            existentItems.add(item.bsn+'_'+item.msn)
            values.append('("%s","%s")' %(item.bsn, item.msn))
        if len(values)>0:
            insertSql += ','.join(values)
            self.dstDbProxy.execute(insertSql)
            #self.logger.debug('insert r_part_msn:%s ', insertSql)

    def __updateOriginal(self, itemList):
        cleanItemList = map(lambda item: str(item.tid), filter(lambda item: item.cleanFlag==CleanerCommon.CLEAN_FLAG_CLEAN, itemList))
        duplicateItemList = map(lambda item: str(item.tid), filter(lambda item: item.cleanFlag == CleanerCommon.CLEAN_FLAG_DUPLICATE, itemList))
        
        updateSql = 'UPDATE r_part_info set clean_flag=%d where tid in (%s)'
        if len(cleanItemList) >0:
            self.srcDbProxy.execute(updateSql%(CleanerCommon.CLEAN_FLAG_CLEAN, ','.join(cleanItemList)))
            #self.logger.debug('Update original clean_flag=1 for tid:%s', ','.join(cleanItemList))
        if len(duplicateItemList) >0:
            self.srcDbProxy.execute(updateSql%(CleanerCommon.CLEAN_FLAG_DUPLICATE, ','.join(duplicateItemList)))
            #self.logger.debug('Update original clean_flag=2 for tid:%s', ','.join(duplicateItemList))
                    
class Item(object):
    def __init__(self, tid, cageCode, cageName, partNum, partName):
        self.tid = tid
        self.cageCode = cageCode
        self.partNum = partNum
        self.partName = partName
        self.cageName = cageName
        self.spn = None
        self.msn = None
        self.psn = None
        self.bsn = None
        self.origPartName = None
        self.origCageName = None
        self.cleanFlag = CleanerCommon.CLEAN_FLAG_CLEAN
        self.isPartExists = False
        self.isMsnExists = False

if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CLENER, pid)
    Logging.initLogger('conf/crawler.logging.cfg')
    ins = SATCleaner(os.path.join('conf',LOGGER_NAME_CLENER+'.cfg'))
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CLENER, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                