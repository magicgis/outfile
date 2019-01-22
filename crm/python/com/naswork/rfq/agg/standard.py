'''
Created on 18 Dec 2016

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils

#PREFIX_LIST = ["ABD","ABS","AGS","AMS","ASN","BAQ","BAS","BHM","BHQ","BJQ","BNAE","BSSP","CSP","DHS","DAN","DIN","DMZ","DTD","FON","GAQ","GBQ","HAN","ISO","LHQ","MBBN","MIL","MS","NAS","NFE","NFL","NSA","NSE","PAN","TAN","VFN"]
PREFIX_LIST = ["AMS","ASN","BAQ","BAS","BHM","BHQ","BJQ","BNAE","BSSP","CSP","DHS","DAN","DIN","DMZ","DTD","FON","GAQ","GBQ","HAN","ISO","LHQ","MBBN","MIL","MS","NAS","NFE","NFL","NSA","NSE","PAN","TAN","VFN"]
LOGGER_NAME_CLENER = 'sta'
PART_TYPE_STANDARD = 83
CAGE_CODE_STANDARD = '00083'
MSN_STANDARD = '1-'+CAGE_CODE_STANDARD

class StandardPartAgg(CleanerCommon):
    '''
    classdocs
    '''


    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(StandardPartAgg, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)
        self.prefixIndex = 0
    
    def __generarteBSN(self, partNum, partName):
        spn = self.generateSPN(partNum)        
        sql = 'select bsn from t_part_standard where part_num="%s" and part_name="%s"'
        existFlag = False
        if self.dstDbProxy.execute(sql)>0:
            result = self.dstDbProxy.cur.fetchone()
            bsn = result[0]
            existFlag = True
            return (bsn, existFlag)
        
        sql = 'select spn,seq from s_sta_spn where spn ="%s"' % spn
        seq = -1
        if self.dstDbProxy.execute(sql)>0:
            result = self.dstDbProxy.cur.fetchone()
            seq = result[1]
        seq+=1
        sql = 'delete from s_sta_spn where spn = "%s"' % spn
        self.dstDbProxy.execute(sql)
        sql = 'insert into s_sta_spn (spn,seq) values ("%s",%d)' % (spn, seq)
        self.dstDbProxy.execute(sql)
        psn = '%04d'%(seq)+'-'+spn
        bsn = MSN_STANDARD + '-'+ psn
        return (bsn, existFlag)
    
    def _handleNextBatch(self):
        sql = '''
        select * from 
(select part_num, part_name, count(*) as c from t_part 
where part_num like "%s%%" 
group by part_num, part_name) t
where c>1
order by c desc
        '''% (PREFIX_LIST[self.prefixIndex])
        self.srcDbProxy.execute(sql)
        resultList = self.srcDbProxy.cur.fetchall()
        self.logger.debug('[%s]Totally %d suspected partNum/partName pair', PREFIX_LIST[self.prefixIndex], len(resultList))
        if len(resultList) == 0:
            self.prefixIndex += 1
            if self.prefixIndex == len(PREFIX_LIST):
                self.processFinish = True
                return
                
        for result in resultList:
            partNum = result[0]
            partName = result[1]
            sql = '''
            select distinct(cage_code) from t_part  
            where part_num="%s" and part_name="%s"
            ''' % (partNum, partName)
            self.srcDbProxy.execute(sql)
            cageCodeResultList = self.srcDbProxy.cur.fetchall()
            self.logger.debug('[%s][%s][%s]Totally %d suspected cage code', 
                              PREFIX_LIST[self.prefixIndex], partNum, partName, len(cageCodeResultList))
            if len(cageCodeResultList) == 1:
                self.logger.warn('[%s][%s]No duplicate cage_code', partNum, partName)
                continue
            spn = self.generateSPN(partNum)
            rbsn, existFlag = self.__generarteBSN(partNum, partName)
            standardPart = Part(rbsn, MSN_STANDARD, 0, partNum, partName, spn,
                            CAGE_CODE_STANDARD, None, None, None, None,None,
                            None, None, 0, 0, 0,
                            None, None, None, PART_TYPE_STANDARD, 0, None)
            
            #only query those which have not yet been queried (part_type=200)
            sql = '''
            select
                bsn, msn, msn_flag, part_num, part_name, short_part_num,
                 cage_code, nsn, replaced_nsn, weight, dimentions, country_of_origin,
                 eccn, schedule_b_code, shelf_life, ata_chapter_section, category_no,
                 usml, hazmat_code, img_path, part_type, is_blacklist 
            from t_part 
            where part_num="%s" and part_name="%s" and part_type = 200
            '''%(partNum, partName)
            self.srcDbProxy.execute(sql)
            partResultList = self.srcDbProxy.cur.fetchall()
            self.logger.debug('[%s][%s][%s]Totally %d suspected records', 
                          PREFIX_LIST[self.prefixIndex], partNum, 
                          partName, len(partResultList))
            partList = self.__generatePartListFromSqlResult(partResultList)
            self.__insertOrUpdateStandardPart(standardPart, existFlag, partList)
            self.__updateOriginal(rbsn, partList)
            self.dstDbProxy.commit()
            self.srcDbProxy.commit()
        self.prefixIndex += 1
        if self.prefixIndex == len(PREFIX_LIST):
            self.processFinish = True
                
    def __generatePartListFromSqlResult(self, partResultList):
        partList = list()
        for partResult in partResultList:
            
            part = Part(partResult[0], partResult[1], partResult[2], partResult[3],
                        partResult[4], partResult[5], partResult[6], partResult[7],
                        partResult[8], partResult[9], partResult[10], partResult[11],
                        partResult[12], partResult[13], partResult[14], partResult[15],
                        partResult[16], partResult[17], partResult[18], partResult[19],
                        partResult[20], partResult[21], None)
            partList.append(part)
        
        return partList
    
    def __updateOriginal(self, rbsn, partList):
        sql = 'update T_PART set part_type=%d,rbsn="%s" where BSN in (%s)' % (
                        PART_TYPE_STANDARD, rbsn,
                        ','.join(map(lambda x: '"'+x.bsn+'"', partList)))
        self.srcDbProxy.execute(sql)
    
    def __insertOrUpdateStandardPart(self, standardPart, existFlag, partList):
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
        if not existFlag:
            sql = '''INSERT INTO T_PART_STANDARD (
                    bsn, part_num, part_name, short_part_num,
                     nsn, replaced_nsn, weight, dimentions, country_of_origin,
                     eccn, schedule_b_code, shelf_life, ata_chapter_section, category_no,
                     usml, hazmat_code, img_path, is_blacklist 
            ) values 
                ("%s","%s","%s","%s",
                %s, %s, %s, %s, %s, 
                %s, %s, %s,%s,%s, 
                %s, %s,%s, %d
                )
            ''' % (standardPart.bsn, standardPart.partNum, standardPart.partName, standardPart.shortPartNum,
               self.genColValueStr(standardPart.nsn), self.genColValueStr(standardPart.replacedNsn), 
               self.genColValueStr(standardPart.weight), self.genColValueStr(standardPart.dimentions), 
               self.genColValueStr(standardPart.countryOfOrigin),self.genColValueStr(standardPart.eccn), 
               self.genColValueStr(standardPart.scheduleBCode), self.genColValueNum(standardPart.shelfLife),
               self.genColValueNum(standardPart.ataChapterSection), self.genColValueNum(standardPart.categoryNo),
               self.genColValueStr(standardPart.usml), self.genColValueStr(standardPart.hazmatCode),
               self.genColValueStr(standardPart.imgPath), standardPart.isBlackList)
            self.dstDbProxy.execute(sql)  
class Part(object):
    def __init__(self, bsn, msn, msnFlag, partNum, partName, shortPartNum,
                 cageCode, nsn, replacedNsn, weight, dimentions, countryOfOrigin,
                 eccn, scheduleBCode, shelfLife, ataChapterSection, categoryNo,
                 usml, hazmatCode, imgPath, partType, isBlackList, rbsn):
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
if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CLENER, pid)
    Logging.initLogger(os.path.join('conf','crawler.logging.win.cfg'))
    ins = StandardPartAgg(os.path.join('conf',LOGGER_NAME_CLENER+'.cfg'))
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CLENER, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                                                