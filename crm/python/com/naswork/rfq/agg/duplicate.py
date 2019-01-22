'''
Created on 23 Jan 2017

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils

LOGGER_NAME_CLENER = 'dpa'
class DuplicateAgg(CleanerCommon):
    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(DuplicateAgg, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)
    
    def _handleNextBatch(self):
        sql='''
        select short_part_num, cage_code, tid ,c from t_part_duplicate where c>1
        and tid>%d and clean_flag=0 and c>1 and short_part_num<>""
        ORDER BY TID ASC
        limit %d
        '''% (self.currentNo, self.batchSize)
        self.srcDbProxy.execute(sql)
        resultList = self.srcDbProxy.cur.fetchall()
        #self.processFinish = True

        if len(resultList)==0:
            self.processFinish = True
            return
        if len(resultList)<self.batchSize:
            self.processFinish = True

        allNamesDict = dict()
        for result in resultList:
            shortPartNum= result[0]
            cageCode = result[1]
            tid = result[2]
            c = result[3]
            self.currentNo = tid
            #query all fields for potential 
            sql = '''
            select
                bsn, msn, msn_flag, part_num, part_name, short_part_num,
                 cage_code, nsn, replaced_nsn, weight, dimentions, country_of_origin,
                 eccn, schedule_b_code, shelf_life, ata_chapter_section, category_no,
                 usml, hazmat_code, img_path, part_type, is_blacklist 
            from t_part 
            where short_part_num="%s" and cage_code="%s"
            '''%(shortPartNum, cageCode)

            self.srcDbProxy.execute(sql)
            partNameDict = dict()
            allNamesDict.clear()
            duplicatePartNameSet = set()
            partResultList = self.srcDbProxy.cur.fetchall()
            self.logger.debug('Count is %d & num is %d for (%s, %s)', c, len(partResultList), shortPartNum, cageCode)
            for partResult in partResultList:
                nameSet = set(partResult[4].replace(' ',',').split(','))
                if '' in nameSet:
                    nameSet.remove('')
                if ',' in nameSet:
                    nameSet.remove('')
                partNameDict[partResult[0]] = nameSet
                for item in partNameDict[partResult[0]]:
                    if item not in allNamesDict:
                        allNamesDict[item] = 0
                    allNamesDict[item]+=1
            
            duplicateBsnSet = set()
            for bsn in partNameDict:
                duplicateFlag = False
                for item in partNameDict[bsn]:
                    if allNamesDict[item] > 1:
                        duplicateFlag = True
                        break
                if duplicateFlag:
                    duplicateBsnSet.add(bsn)
                    duplicatePartNameSet.update(partNameDict[bsn])
            
            #generate the part list
            duplicatePartList, unduplicatePartList = self.__generatePartListFromSqlResult(partResultList, duplicateBsnSet)
            self.logger.debug('Totally %d duplicate & %d unduplicate for (%s,%s)', len(duplicatePartList), len(unduplicatePartList), shortPartNum, cageCode)
            #insert into the part table            
            msn = '0-'+cageCode
            psn = '0000-'+ shortPartNum
            rbsn = msn + '-' + psn
            
            standardPart = Part(rbsn, msn, 0, shortPartNum, ','.join(duplicatePartNameSet), shortPartNum,
                            cageCode, None, None, None, None,None,
                            None, None, 0, 0, 0,
                            None, None, None, 200, 0, None, True)
            startSeq = 1
            if len(duplicatePartList) > 0:
                self.__insertDuplicateToPart2(standardPart, duplicatePartList)
            else:
                startSeq = 0
            if len(unduplicatePartList)>0:
                self.__insertUnDuplicateToPart2(unduplicatePartList, startSeq)
            
            #update existing table
            if len(duplicatePartList) > 0:
                self.__updateOriginalForDuplicate(standardPart.bsn, duplicatePartList)
            if len(unduplicatePartList) > 0:
                self.__updateOriginalForUnDuplicate(unduplicatePartList)
            
            #update the clean flag
            self.__updateFlag(tid)
            
            self.dstDbProxy.commit()
            self.srcDbProxy.commit()

    def __updateFlag(self, tid):
        sql = 'UPDATE T_PART_DUPLICATE SET CLEAN_FLAG=1 where tid=%d' % tid
        self.srcDbProxy.execute(sql)
    def __updateOriginalForDuplicate(self, rbsn, partList):
        sql = 'update T_PART set rbsn="%s" where BSN in (%s)' % (
                         rbsn,
                        ','.join(map(lambda x: '"'+x.bsn+'"', partList)))
        self.srcDbProxy.execute(sql)

    def __updateOriginalForUnDuplicate(self, partList):
        for part in partList:
            sql = 'update T_PART set rbsn="%s" where BSN = "%s"' % (
                         part.rbsn,
                         part.bsn)
            self.srcDbProxy.execute(sql)
    def __insertUnDuplicateToPart2(self, partList, startSeq):
        sql = '''INSERT INTO T_PART2 (
                    bsn, msn, part_num, part_name, short_part_num,
                     cage_code, nsn, replaced_nsn, weight, dimentions, country_of_origin,
                     eccn, schedule_b_code, shelf_life, ata_chapter_section, category_no,
                     usml, hazmat_code, img_path, is_blacklist 
            ) values 
              
        '''
        valueList = list()
        seq = startSeq
        for part in partList:
            psn = '%04d'%(seq)+'-'+part.shortPartNum
            rbsn = part.msn + '-' + psn
            valueList.append(
                             '''
                ("%s","%s", "%s","%s","%s",
                "%s",%s, %s, %s, %s, %s, 
                %s, %s, %s,%s,%s, 
                %s, %s,%s, %d
                )
                ''' % (rbsn, part.msn, part.partNum, part.partName.replace('"','\\"'), part.shortPartNum,
               part.cageCode, self.genColValueStr(part.nsn), self.genColValueStr(part.replacedNsn), 
               self.genColValueStr(part.weight), self.genColValueStr(part.dimentions), 
               self.genColValueStr(part.countryOfOrigin),self.genColValueStr(part.eccn), 
               self.genColValueStr(part.scheduleBCode), self.genColValueNum(part.shelfLife),
               self.genColValueNum(part.ataChapterSection), self.genColValueNum(part.categoryNo),
               self.genColValueStr(part.usml), self.genColValueStr(part.hazmatCode),
               self.genColValueStr(part.imgPath), part.isBlackList)
            )
            part.rbsn = rbsn
            seq += 1
        if len(valueList) > 0:
            self.dstDbProxy.execute(sql+','.join(valueList))
            
    def __insertDuplicateToPart2(self, standardPart, partList):
        for part in partList:
            if part.nsn!=None and part.nsn.strip()!='':
                standardPart.nsn = part.nsn.strip()
            if part.replacedNsn!=None and part.replacedNsn.strip()!='':
                standardPart.replacedNsn = part.replacedNsn.strip()
            if part.weight!=None and part.weight.strip()!='':
                standardPart.weight = part.weight.strip()
            if part.dimentions!=None and part.dimentions.strip()!='':
                standardPart.dimentions = part.dimentions.strip()
            if part.countryOfOrigin!=None and part.countryOfOrigin.strip()!='':
                standardPart.countryOfOrigin = part.countryOfOrigin.strip()
            if part.eccn!=None and part.eccn.strip()!='':
                standardPart.eccn = part.eccn
            if part.scheduleBCode!=None and part.scheduleBCode.strip()!='':
                standardPart.scheduleBCode = part.scheduleBCode.strip()
            if part.shelfLife!=None and part.shelfLife!=0:
                standardPart.shelfLife = part.shelfLife
            if part.ataChapterSection!=None and part.ataChapterSection!=0:
                standardPart.ataChapterSection = part.ataChapterSection
            if part.categoryNo!=None and part.categoryNo!=0:
                standardPart.categoryNo=part.categoryNo
            if part.usml!=None and part.usml.strip()!='':
                standardPart.usml = part.usml.strip()
            if part.hazmatCode!=None and part.hazmatCode.strip()!='':
                standardPart.hazmatCode = part.hazmatCode.strip()
            if part.imgPath!=None and part.imgPath.strip()!='':
                standardPart.imgPath = part.imgPath.strip()
            if part.isBlackList!=None and part.isBlackList!=0:
                standardPart.isBlackList = part.isBlackList
            part.rbsn = standardPart.bsn
        sql = '''INSERT INTO T_PART2 (
                    bsn, msn, part_num, part_name, short_part_num,
                     cage_code, nsn, replaced_nsn, weight, dimentions, country_of_origin,
                     eccn, schedule_b_code, shelf_life, ata_chapter_section, category_no,
                     usml, hazmat_code, img_path, is_blacklist 
            ) values 
                ("%s","%s", "%s","%s","%s",
                "%s",%s, %s, %s, %s, %s, 
                %s, %s, %s,%s,%s, 
                %s, %s,%s, %d
                )
            ''' % (standardPart.bsn, standardPart.msn, standardPart.partNum, standardPart.partName.replace('"','\\"'), standardPart.shortPartNum,
               standardPart.cageCode, self.genColValueStr(standardPart.nsn), self.genColValueStr(standardPart.replacedNsn), 
               self.genColValueStr(standardPart.weight), self.genColValueStr(standardPart.dimentions), 
               self.genColValueStr(standardPart.countryOfOrigin),self.genColValueStr(standardPart.eccn), 
               self.genColValueStr(standardPart.scheduleBCode), self.genColValueNum(standardPart.shelfLife),
               self.genColValueNum(standardPart.ataChapterSection), self.genColValueNum(standardPart.categoryNo),
               self.genColValueStr(standardPart.usml), self.genColValueStr(standardPart.hazmatCode),
               self.genColValueStr(standardPart.imgPath), standardPart.isBlackList)
        self.dstDbProxy.execute(sql)  
            
    def __generatePartListFromSqlResult(self, partResultList, duplicateBsnSet):
        duplicatePartList = list()
        unduplicatePartList = list()
        for partResult in partResultList:
            if partResult[0] in duplicateBsnSet:
                isDuplicate = True
            else:
                isDuplicate = False
            part = Part(partResult[0], partResult[1], partResult[2], partResult[3],
                        partResult[4], partResult[5], partResult[6], partResult[7],
                        partResult[8], partResult[9], partResult[10], partResult[11],
                        partResult[12], partResult[13], partResult[14], partResult[15],
                        partResult[16], partResult[17], partResult[18], partResult[19],
                        partResult[20], partResult[21], None, isDuplicate)
            if isDuplicate:
                duplicatePartList.append(part)
            else:
                unduplicatePartList.append(part)
        
        return duplicatePartList, unduplicatePartList

class Part(object):
    def __init__(self, bsn, msn, msnFlag, partNum, partName, shortPartNum,
                 cageCode, nsn, replacedNsn, weight, dimentions, countryOfOrigin,
                 eccn, scheduleBCode, shelfLife, ataChapterSection, categoryNo,
                 usml, hazmatCode, imgPath, partType, isBlackList, rbsn, isDuplicate):
        self.bsn = bsn
        self.msn = msn
        self.msnFlg = msnFlag
        self.partNum = partNum
        self.partName = partName
        self.shortPartNum = shortPartNum
        self.cageCode = cageCode
        self.nsn = nsn
        self.replacedNsn = replacedNsn
        self.weight = weight
        self.dimentions = dimentions
        self.countryOfOrigin = countryOfOrigin
        self.eccn = eccn
        self.scheduleBCode = scheduleBCode
        self.shelfLife = shelfLife
        self.ataChapterSection = ataChapterSection
        self.categoryNo = categoryNo
        self.usml = usml
        self.hazmatCode = hazmatCode
        self.imgPath = imgPath
        self.partType = partType
        self.isBlackList = isBlackList
        self.rbsn = rbsn
        self.isDuplicate = isDuplicate

if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CLENER, pid)
    Logging.initLogger(os.path.join('conf','crawler.logging.win.cfg'))
    ins = DuplicateAgg(os.path.join('conf',LOGGER_NAME_CLENER+'.cfg'))
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CLENER, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                                                                    