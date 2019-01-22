'''
Created on 19 Nov 2016

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])
import traceback

from com.naswork.rfq.common.utils import Logging 
from com.naswork.rfq.common.crawlercommon import CrawlerConstants
from com.naswork.rfq.crawler.pdf.airbus import AirBusParser

START_INDEX_FIG_NUM = 4
START_INDEX_FIG_ITEM = 4
START_INDEX_PART_NUM = 9
START_INDEX_DETAIL = 25
START_INDEX_USAGEFROMTO = 58
START_INDEX_UNIT = 67
START_INDEX_VERSION = 70

CAGE_CODE_LENGTH = 7

KW_DELETE_FLAG = 'D'
KW_VACRT = 'VACRT'
KW_VACRT_P = '(VACRT)'

KW_NP = 'NP'
KW_NP_P = '(NP)'

KW_LM = '* LM *'

KW_NO_MAINTENANCE_PART = 'NO MAINTENANCE PART'

KW_OPT_TO = 'OPT TO'
KW_ALT_FROM = 'ALT FROM'
KW_RPLDBY = 'RPLD BY'
KW_IW = 'I/W'
KW_REIDENT = 'RE-IDENT'
KW_REIDENT_TO = 'RE-IDENT TO'
KW_BUY_PN = 'BUY PN'

LOGGER_NAME = 'app'

DEBUG = False

class AirBusPartParser(AirBusParser):
    '''
    classdocs
    
    CREATE TABLE PART(
        CHSESU VARCHAR(15) NOT NULL,
        PART_NO VARCHAR(32) NOT NULL,
        FIG_NO VARCHAR(10) NOT NULL,
        PARENT_PART_NO VARCHAR(32),
        PART_NAME VARCHAR(100) NOT NULL,
        VENDOR_CODE CHAR(6),
        USAGE_RANGE VARCHAR(100), /*A list of range separated by ,*/
        UNIT_PER_ASSY SMALLINT DEFAULT 0,
        UNIT_TYPE VARCHAR(10) DEFAULT 'NORMAL',
        NHA_NO VARCHAR(15),
        DET_NO VARCHAR(15),
        POST_TYPE VARCHAR(10),
        POST_NO VARCHAR(15),
        NP TINYINT DEFAULT 0, /*whether this part is "not produced" or not*/
        LM TINYINT DEFAULT 0, /*whether this part is "local manufactured" or not */
        EQUIPMENT_POSITION VARCHAR(20),
        SPARE_PART_NO VARCHAR(32),
        OVRLGTH VARCHAR(20),
        DIMENSION VARCHAR(32),
        NO_MAINTENANCE TINYINT DEFAULT 0,
        PRIMARY KEY(CHSESU, FIG_NO, PART_NO)
    )ENGINE=MyISAM DEFAULT CHARSET=utf8;
    
    CREATE TABLE PART_ATTACH(
        PART_NO VARCHAR(32) NOT NULL,
        ATTACH_PART_NO VARCHAR(32) NOT NULL
    )
    
    CREATE TABLE PART_STORAGE(
        PART_NO VARCHAR(20) NOT NULL,
        STORAGE_PART_NO VARCHAR(32) NOT NULL
    )
    
    CREATE TABLE REPLACEMENT(
        PART_NO VARCHAR(32) NOT NULL,
        REPLACE_PART_NO VARCHAR(32) NOT NULL,
        REPLACE_TYPE ENUM ("ALT", "OPT", "RPLD", "IW", "REIDENT")
    )
    '''


    def __init__(self, conf, logger):
        '''
        Constructor
        '''
        super(AirBusPartParser, self).__init__(conf, logger)
        self.lastL0Part = None
        self.lastL1Part = None
        self.lastL2Part = None
        self.lastFigNum = None
        self.lastLeftBlank = 0
        self.chsesu = ''

    def __fetchChsesu(self, fileName):
        lastSepIndex = fileName.rfind(os.path.sep)
        lastDotIndex = fileName.rfind('.')
        self.chsesu = fileName[lastSepIndex+1:lastDotIndex]
        
    def parse(self, fileName):
        self.__fetchChsesu(fileName)
        f = open(fileName)        
        fl = list(f)
        f.close()
        lastPart = None
        partList = list()
        index = 0
        self.logger.debug('Row number of file %s is: %d', fileName, len(fl))
        while index < len(fl):
            line = fl[index]
            line = line.strip('\n').strip('\r')
            rowContent = self.__parseRowContent(line)
            if rowContent.partNum!= '':
                duplicate = False 
                part = Part(rowContent.partNum)
                
                if rowContent.version == KW_DELETE_FLAG:
                    part.isDelete = 1
                
                self.__checkFigNum(rowContent, part)
                if lastPart is not None and lastPart.partNo == part.partNo and lastPart.figNo == part.figNo:
                    part = lastPart
                    duplicate = True
                else:
                    self.__checkPartLevel(rowContent.detail, part)
                    detail = rowContent.detail.strip('.')
                    #check special cage code
                    detail = self.__checkSpecialCageCode(detail, part)
                    #check normal cage code
                    detail = self.__checkNormalCageCode(detail, part)
                    #check maintenance
                    detail = self.__checkMaintenance(detail, part)
                    
                    if part.noMaintenance == 0:
                        part.partName = detail.strip()
                
                #check the unit
                if rowContent.unit.isdigit() == True:
                    part.unitPerAsset = int(rowContent.unit)
                else:
                    part.unitType = rowContent.unit
                
                #check usage from to
                self.__checkUsageFromTo(rowContent, part)
                if duplicate is False:
                    partList.append(part)
                    lastPart = part
                self.lastLeftBlank = rowContent.leftBlankForDetail
            else:
                #check usage from to
                self.__checkUsageFromTo(rowContent, lastPart)
                #check other info
                detail = rowContent.detail.strip()
                if detail=='':
                    index+=1
                    continue
                #check if row name as well
                if lastPart.partName!='' and self.lastLeftBlank == rowContent.leftBlankForDetail:
                    if lastPart.partName.find(detail)<0:
                        lastPart.partName += detail
                    index+=1
                    continue
                
                #check if for part name
                if self.__checkPartLevel(detail, lastPart):
                    detail = rowContent.detail.strip('.')
                    #check special cage code
                    detail = self.__checkSpecialCageCode(detail, lastPart)
                    #check normal cage code
                    detail = self.__checkNormalCageCode(detail, lastPart)
                    #check maintenance
                    detail = self.__checkMaintenance(detail, lastPart)
                    
                    if lastPart.noMaintenance == 0:
                        lastPart.partName = detail.strip()
                    
                    #check the unit
                    if rowContent.unit.isdigit() == True:
                        lastPart.unitPerAsset = int(rowContent.unit)
                    else:
                        lastPart.unitType = rowContent.unit
                    index+=1
                    
                    continue
                #check spare part
                self.__checkBuyPN(detail, lastPart)
                #check replacement
                if detail.find(KW_OPT_TO)>=0:
                    info = detail.replace(KW_OPT_TO, '').strip()
                    lastPart.replaceDict['OPT'].append(info.split(' ')[0])
                elif detail.find(KW_ALT_FROM) >= 0:
                    info = detail.replace(KW_ALT_FROM, '').strip()
                    lastPart.replaceDict['ALT'].append(info.split(' ')[0])
                elif detail.find(KW_RPLDBY) >=0:
                    items = detail.split(KW_RPLDBY)
                    info = items[1].strip()
                    if info == '':
                        #expand to next row
                        index+=1
                        nextRow = self.__parseRowContent(fl[index])
                        rpld = nextRow.detail.strip().split(' ')[0]
                        lastPart.replaceDict['RPLD'].append(rpld)
                    else:
                        lastPart.replaceDict['RPLD'].append(info)
                elif detail.find(KW_IW)>=0:
                    items = detail.split(KW_IW)
                    info = items[1].strip()
                    if info == '':
                        #expand to next row
                        index+=1
                        nextRow = self.__parseRowContent(fl[index])
                        iw = nextRow.detail.strip().split(' ')[0]
                        lastPart.replaceDict['IW'].append(iw)
                    else:
                        lastPart.replaceDict['IW'].append(info)
                elif detail.find(KW_REIDENT_TO)>=0:
                    items = detail.split(KW_REIDENT_TO)
                    info = items[1].strip()
                    if info == '':
                        #expand to next row
                        index+=1
                        nextRow = self.__parseRowContent(fl[index])
                        reident = nextRow.detail.strip().split(' ')[0]
                        lastPart.replaceDict['REIDENT'].append(reident)
                    else:
                        lastPart.replaceDict['REIDENT'].append(info)
                elif detail.find(KW_REIDENT)>=0:
                    items = detail.split(KW_REIDENT)
                    info = items[1].strip()
                    if info == '':
                        #expand to next row
                        index+=1
                        nextRow = self.__parseRowContent(fl[index])
                        reident = nextRow.detail.replace('TO','').strip().split(' ')[0]
                        lastPart.replaceDict['REIDENT'].append(reident)
                    else:
                        lastPart.replaceDict['REIDENT'].append(info)
                        
                #ignore other dummy data    
                '''
                while index+1 < len(fl):
                    
                    nextRow = self.__parseRowContent(fl[index+1])
                    if nextRow.partNum!='':
                    #nextRow.leftBlankForDetail <= rowContent.leftBlankForDetail:
                        break
                    index+=1
                '''
            index+=1
        self.__writeDb(partList)
    
    def __checkFigNum(self, rowContent, part):
        if rowContent.figNum!='' and rowContent.figNum!='-':
            self.lastFigNum = rowContent.figNum        
        part.figNo = self.lastFigNum + rowContent.figItem
        
    def __checkPartLevel(self, detail, part):
        if DEBUG:
            self.logger.debug('l0:%s', self.lastL0Part)
            self.logger.debug('detail:%s', detail)
        if part.partName!='':
            return False
        if detail.startswith('.'):
            if detail.startswith('...'):
                part.parentPartNo = self.lastL2Part.partNo
            elif detail.startswith('..'):
                part.parentPartNo = self.lastL1Part.partNo
                self.lastL2Part = part
            elif detail.startswith('.'):
                part.parentPartNo = self.lastL0Part.partNo
                self.lastL1Part = part
        else:
            self.lastL0Part = part
                
        return True

    def __checkUsageFromTo(self, rowContent, part):
        #check usage from to
        if rowContent.usageFromTo != '':
            part.usageFromTo.append(rowContent.usageFromTo)
        
    def __checkSpecialCageCode(self, content, part):
        if content.find(KW_VACRT_P) >= 0:
            part.cageCode = KW_VACRT
            content = content.replace(KW_VACRT_P,'')
        elif content.find(KW_VACRT) >= 0:
            part.cageCode = KW_VACRT
            content = content.replace(KW_VACRT,'')
        return content.strip()
    
    def __checkNormalCageCode(self, detail, part):
        if len(detail)<CAGE_CODE_LENGTH:
            return detail
        cageCode = detail[(len(detail)-CAGE_CODE_LENGTH):]
        if cageCode.startswith(' V'):
            part.cageCode = cageCode.strip()
            detail = detail.replace(part.cageCode, '').strip()        
        return detail
    
    def __checkMaintenance(self, detail, part):
        if detail.find(KW_NO_MAINTENANCE_PART)<0:                        
            #check np
            if detail.find(KW_NP_P)>=0:
                part.np = 1
                detail = detail.replace(KW_NP_P,'')
            elif detail.find(KW_NP)>=0:
                part.np = 1
                detail = detail.replace(KW_NP,'')
            
            #check lm
            if detail.find(KW_LM) >= 0:
                part.lm = 1
                detail.replace(KW_LM, '')
        else:
            part.noMaintenance = 1
        return detail
    
    def __checkBuyPN(self, detail, part):
        if detail.find(KW_BUY_PN)>=0:
            detail = detail.replace(KW_BUY_PN, '').strip()
            part.sparePart = detail
        return detail
            
    def __parseRowContent(self, line):
        rowContent = RowContent()
        rowContent.figNum = line[:START_INDEX_FIG_NUM].replace(' ','')
        rowContent.figItem = line[START_INDEX_FIG_ITEM:START_INDEX_PART_NUM].replace(' ','')
        rowContent.partNum = line[START_INDEX_PART_NUM: START_INDEX_DETAIL].strip()
        detail = line[START_INDEX_DETAIL: START_INDEX_USAGEFROMTO]
        if detail.strip() == '':
            rowContent.leftBlankForDetail = 0
        else:
            rowContent.leftBlankForDetail = len(detail) - len(detail.lstrip())
        rowContent.detail = detail.strip()
        rowContent.usageFromTo = line[START_INDEX_USAGEFROMTO: START_INDEX_UNIT].strip()
        rowContent.verion = ''
        if len(line)> START_INDEX_VERSION:
            rowContent.unit = line[START_INDEX_UNIT: START_INDEX_VERSION].strip()
            rowContent.version = line[START_INDEX_VERSION:].strip()
        else:
            rowContent.unit = line[START_INDEX_UNIT:].strip()
        return rowContent
    
    def __writeDb(self, partList):
        sql = '''
        INSERT INTO PART (CHSESU, FIG_NO, PART_NO, PARENT_PART_NO, PART_NAME,
            VENDOR_CODE,USAGE_RANGE,UNIT_PER_ASSY,UNIT_TYPE,NP,LM,NO_MAINTENANCE,
            SPARE_PART_NO) VALUES 
        '''
        replaceList = list()
        partValues = list()
        self.logger.debug('Total %d part', len(partList))
        for part in partList:
            partValues.append('("%s","%s","%s","%s","%s","%s","%s",%d,"%s",%d,%d,%d,"%s")' %(
                                         self.chsesu,
                                         part.figNo,
                                         part.partNo,
                                         part.parentPartNo,
                                         part.partName,
                                         part.cageCode,
                                         ','.join(part.usageFromTo),
                                         part.unitPerAsset,
                                         part.unitType,
                                         part.np,
                                         part.lm,
                                         part.noMaintenance,
                                         part.sparePart
                                         ))
            for key in part.replaceDict:
                for item in part.replaceDict[key]:
                    replaceList.append('("%s","%s","%s")'%(
                                                           part.partNo,
                                                           item,
                                                           key))
        
        #self.logger.debug(partValues)
        
        if len(partValues)>0:
            self.dbProxy.execute(sql+','.join(partValues))
        if len(replaceList)>0:
            sql = 'INSERT INTO REPLACEMENT (PART_NO, REPLACE_PART_NO,REPLACE_TYPE) VALUES '+ ','.join(replaceList)
            self.dbProxy.execute(sql)
        self.dbProxy.commit()
        
    
class RowContent(object):
    def __init__(self):
        self.figNum = ''
        self.figItem = ''
        self.partNum = ''
        self.detail = ''
        self.leftBlankForDetail = 0
        self.usageFromTo = ''
        self.unit = ''
        self.version = ''
    
    def __str__(self, *args, **kwargs):
        return 'figNum:%s, figItem:%s, partNum:%s, detail:%s, leftBlank:%d, usageFromto:%s, unit:%s, version:%s' % (
                    self.figNum, self.figItem, self.partNum, self.detail, self.leftBlankForDetail, self.usageFromTo,
                    self.unit, self.version)    
        
class Part(object):
    def __init__(self, partNo):
        self.partNo = partNo
        self.figNo = ''
        self.partName = ''                
        self.parentPartNo = ''
        self.cageCode = ''
        self.noMaintenance = 0
        self.sparePart = ''
        self.np = 0
        self.unitPerAsset = 0
        self.unitType = 'NORMAL'
        self.usageFromTo = list()
        self.isDelete = 0
        self.lm = 0
        self.replaceDict = {'OPT':list(), 'RPLD':list(), 'IW':list(), 'ALT':list(), 'REIDENT':list()}

if __name__ == '__main__':
    import platform
    if 'window' in platform.system().lower():
        Logging.initLogger(os.path.join('conf','crawler.logging.win.cfg'))
    else:
        Logging.initLogger(os.path.join('conf','crawler.logging.cfg'))
    conf = {
                CrawlerConstants.CONFIG_FILE_DBHOST: 'localhost',
                CrawlerConstants.CONFIG_FILE_DBPORT: 3306,
                CrawlerConstants.CONFIG_FILE_DBUSER: 'test',
                CrawlerConstants.CONFIG_FILE_DBPASS: 'test',
                CrawlerConstants.CONFIG_FILE_DBNAME: 'airbus'
            
            }
    
    if DEBUG is False:
        folder = 'F:\\tmp\\part'
        for root, dirs, files in os.walk(folder, topdown=False):
            for filename in files:
                if filename.endswith('.txt'):
                    fullpath = os.path.join(root,filename)    
                    parser = AirBusPartParser(conf, Logging.getLogger(LOGGER_NAME))
                    parser.parse(fullpath)
                    parser.clean()
    else:
        fileName = 'F:\\tmp\\part\\0010\\27-54-49.txt'
        #fileName = 'F:\\tmp\\part\\test.txt'
        parser = AirBusPartParser(conf, Logging.getLogger(LOGGER_NAME))
        parser.parse(fileName)
        parser.clean()        
