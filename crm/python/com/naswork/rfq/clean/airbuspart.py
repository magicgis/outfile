# -*- coding: utf-8 -*-
'''
Created on 3 Mar 2017

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])
from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils

LOGGER_NAME_CLENER = 'abp' 
class AirBusPart(CleanerCommon):
    '''
    classdocs
    '''


    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(AirBusPart, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)

    def __composeUpdateSql(self, bsn, existingNames, partName, partType):
        existingNameList = existingNames.split(' ')
        while True:
            try:
                existingNameList.remove(' ')
            except:
                break
        newNameList = partName.split(' ')
        while True:
            try:
                newNameList.remove(' ')
            except:
                break
        for name in newNameList:
            if name in existingNameList:
                continue
            existingNameList.append(name)
        updatedName = ' '.join(existingNameList)
        if partType==0:
            sql = '''update t_part set part_name="%s" where bsn="%s"                
            '''%(updatedName.replace('"','\\"'), bsn)
        else:
            sql = '''update t_part set part_name="%s",part_type=%d where bsn="%s"                
            '''%(updatedName.replace('"','\\"'),
                 partType, 
                 bsn)
        return sql
        
    def _handleNextBatch(self):
        sql = '''
            select part_num, part_name, cage_code, part_type,tid,ata_code from part_clean             
        where 
            clean_flag=0 and tid>%d
             
        order by tid asc limit %d
        ''' %(self.currentNo, self.batchSize)
        self.srcDbProxy.execute(sql)
        resultList = self.srcDbProxy.cur.fetchall()
        if len(resultList)==0:
            self.processFinish = True
            return
        finishFlag = False
        if len(resultList)<self.batchSize:
            finishFlag = True
        insertCount = 0
        updateCount = 0
        exitingCount = 0
        tidSet = set()
        for result in resultList:
            
            partNum = result[0]
            partName = result[1]
            cageCodePrefix = result[2][0]
            cageCode = result[2][1:]
            partType = result[3]
            tid = result[4]
            tidSet.add('"'+str(tid)+'"')
            self.currentNo = max(self.currentNo, tid)
            ataCode = result[5]
            try:
                ataCode = int(ataCode)
            except:
                pass
            newNameList = partName.split(' ')
            newNameSet = set(newNameList)
            if ' ' in newNameSet:
                newNameSet.remove(' ')

                '''
获取cagecode一致，partnum或者shortpartnum一致的记录
如果存在这样的记录（可以多条），检查（名字优先）；
1）如果存在名字一致的记录，无需更新（对于非零partype，更新parttype），sameRecord
2）名字按照空格划分后，相同数量最多的，取partnum一致的（如果没有，那么取第一条），更新名字，similarName
3）partnum一致的，更新名字，samePartNum
4）partnum不一样，名字没有重叠，新增一条记录

找不到的，也是新增一条记录        
        '''
            spn = self.generateSPN(partNum)
            sql = '''
            select bsn, part_num, part_name, part_type 
            from t_part
            where (part_num="%s" or short_part_num="%s")and cage_code="%s" 
            ''' % (partNum, spn, cageCode)
            count = self.dstDbProxy.execute(sql)
            sameRecord = None
            similarNameWithSamePartNumRecord = None
            maxMatchedCountForSamePartNum = 0
            maxMatchedCountForNoSamePartNum = 0
            similarNameWithoutSamePartNumRecord = None
            samePartNumOnlyRecord = None
            if count > 0:
                existingRecordList = self.dstDbProxy.cur.fetchall()
                for existingRecord in existingRecordList:
                    #1.check if there is same
                    existingPartNum = existingRecord[1]
                    existingPartName = existingRecord[2]
                    if partName==existingPartName:
                        sameRecord = existingRecord
                        break
                    #2.check names
                    existingNameSet = set(existingPartName.split(' '))
                    if ' ' in existingNameSet:
                        existingNameSet.remove(' ')
                    matchedCount = len(newNameSet.intersection(existingNameSet))
                    if partNum!=existingPartNum:
                        if matchedCount > maxMatchedCountForNoSamePartNum:
                            maxMatchedCountForNoSamePartNum = matchedCount
                            similarNameWithoutSamePartNumRecord = existingRecord
                    else:
                        if matchedCount > maxMatchedCountForSamePartNum:
                            maxMatchedCountForSamePartNum = matchedCount
                            similarNameWithSamePartNumRecord = existingRecord
                    
                    if similarNameWithoutSamePartNumRecord is None and\
                            similarNameWithSamePartNumRecord is None and\
                            partNum == existingPartNum:
                        samePartNumOnlyRecord = existingRecord
            #compose sql based on different condition
            sql = None
            if sameRecord is not None:
                if partType!=0:
                    sql = 'update t_part set part_type=%d where bsn="%s"' % (partType, sameRecord[0])
                    updateCount+=1
                else:
                    exitingCount+=1
            elif similarNameWithSamePartNumRecord is not None:
                sql = self.__composeUpdateSql(similarNameWithSamePartNumRecord[0],
                                                similarNameWithSamePartNumRecord[2], 
                                                partName, partType)
                updateCount+=1
            elif similarNameWithoutSamePartNumRecord is not None:
                sql = self.__composeUpdateSql(similarNameWithoutSamePartNumRecord[0],
                                                similarNameWithoutSamePartNumRecord[2], 
                                                partName, partType)
                updateCount+=1
            elif samePartNumOnlyRecord is not None:
                sql = self.__composeUpdateSql(samePartNumOnlyRecord[0],
                                                samePartNumOnlyRecord[2], 
                                                partName, partType)
                updateCount+=1
            else:
                qsql = 'select max(bsn) from t_part where short_part_num="%s" and cage_code="%s"' % (spn, cageCode)
                self.dstDbProxy.execute(qsql)
                maxBsn = self.dstDbProxy.cur.fetchone()
                if maxBsn is not None and maxBsn[0] is not None:
                    maxPsnSeq = int(maxBsn[0][8:12])
                    psn = '%04d'%(maxPsnSeq+1)+'-'+spn
                else:                                
                    psn = '0000-'+spn
                msn = cageCodePrefix+'-'+cageCode                
                bsn = msn+'-'+psn
                sql = '''INSERT INTO T_PART
        (BSN, MSN, PART_NUM, SHORT_PART_NUM,        
        PART_NAME,CAGE_CODE,ATA_CHAPTER_SECTION
        ) 
        VALUES ("%s", "%s", "%s","%s", "%s", "%s", %d)
        '''%(bsn, msn, partNum, spn, partName.replace('"','\\"'), cageCode, ataCode)
                insertCount+=1
            
            #execute the sql if necessary
            if sql is not None:
                self.dstDbProxy.execute(sql)
                
        sql = 'update part_clean set clean_flag=1 where tid in (%s)' % ','.join(tidSet)
        self.srcDbProxy.execute(sql)
        self.dstDbProxy.commit()
        self.srcDbProxy.commit()
        #self.processFinish = True
        if finishFlag:
            self.processFinish = True
        self.logger.info('Totally processed %d. Insert:%d, Update:%d, Existing:%d', 
                         self.currentNo,
                         insertCount,
                         updateCount,
                         exitingCount)

if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CLENER, pid)
    Logging.initLogger(os.path.join('conf','crawler.logging.win.cfg'))
    ins = AirBusPart(os.path.join('conf',LOGGER_NAME_CLENER+'.cfg'))
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CLENER, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                                        