'''
Created on May 30, 2016
Crawl a specific cage from url: https://www.nsncenter.com/CAGE/<cage_num>
where cage_num is a string with length of 5 composed of digit & alphabet only.

We fetch the cage base information and the associated part list info
@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])
import traceback

from com.naswork.rfq.common.utils import Logging,  PIDUtils 
from com.naswork.rfq.common.crawlercommon import CrawlerConstants, CrawlerManager, CrawlerBase
from bs4 import BeautifulSoup

LOGGER_NAME_CRAWL = 'nfc'
CAGE_CHAR_SEQ = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
CAGE_CHAR_LENGTH = len(CAGE_CHAR_SEQ) 

class NSNCageFileCrawlerManager(CrawlerManager):
    
    def __init__(self, json_config_file):
        '''
        Constructor
        '''
        self.__cageNumIndex = [0,0,0,0,0]
        self.__noMore = False
        self.__startCageNum = None
        self.__endCageNum = None
        self.__parentPath = None
        super(NSNCageFileCrawlerManager, self).__init__(json_config_file, 0.1, None)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)

    def __genPath(self):
        l1 = CAGE_CHAR_SEQ[self.__cageNumIndex[4]]
        l2 = l1 + CAGE_CHAR_SEQ[self.__cageNumIndex[3]]
        l3 = l2 + CAGE_CHAR_SEQ[self.__cageNumIndex[2]]
        l4 = l3 + CAGE_CHAR_SEQ[self.__cageNumIndex[1]] + CAGE_CHAR_SEQ[self.__cageNumIndex[0]]
        path = os.path.join(l1,
                            os.path.join(l2,
                                         os.path.join(l3,l4)))
        return path
        
    def __generateNextCageFilePath(self):
        if self.__noMore:
            return None
        digitIndex = 0
        path = None
        cageNum = None
        while digitIndex<5:
            self.__cageNumIndex[digitIndex]+=1
            if self.__cageNumIndex[digitIndex] == CAGE_CHAR_LENGTH:
                self.__cageNumIndex[digitIndex] = 0
                digitIndex+=1
            else:
                cageNum = CAGE_CHAR_SEQ[self.__cageNumIndex[4]]+\
                            CAGE_CHAR_SEQ[self.__cageNumIndex[3]]+\
                            CAGE_CHAR_SEQ[self.__cageNumIndex[2]]+\
                            CAGE_CHAR_SEQ[self.__cageNumIndex[1]]+\
                            CAGE_CHAR_SEQ[self.__cageNumIndex[0]]
                if self.__endCageNum is not None and cageNum > self.__endCageNum:
                    self.__noMore = True
                    return None 
                
                path = os.path.join(self.__parentPath, self.__genPath())
                if os.path.exists(path):                    
                    break

        if digitIndex==5:
            self.__noMore = True
            return None
        else:
            return (cageNum, path)
        
    def _generateTask(self, task, checkTaskList=True):
        if super(NSNCageFileCrawlerManager, self)._generateTask(task,checkTaskList) is False:
            return False
        result = self.__generateNextCageFilePath()
        if result is None:
            return False
        else:
            task[CrawlerConstants.PARA_CLASS] = NSNCageFileCrawler
            task[NSNCageFileCrawler.PARA_CAGE_FILE_PATH] = result[1]
            task[NSNCageFileCrawler.PARA_CAGE_NUM] = result[0]
            return True

    def _getNonCommonConfig(self, config):
        self.__startCageNum = config['startCageNum']
        self.__endCageNum = config['endCageNum']
        self.__parentPath = config['parentPath']
        
    def _initTask(self):
        self._taskList.append(0)
        self.__setStartCageIndex()
    
    def __setStartCageIndex(self):
        if self.__startCageNum is None:
            return
        
        for i in range(len(self.__cageNumIndex)):
            rindex = CAGE_CHAR_SEQ.index(self.__startCageNum[4-i])            
            self.__cageNumIndex[i] = rindex
        
        self.__cageNumIndex[0]-=1
        
class NSNCageFileCrawler(CrawlerBase):
    PARA_CAGE_FILE_PATH = 'cage_num_file_path'
    PARA_CAGE_NUM = 'cage_num'
    def __init__(self, controller, dbProxy, request):
        super(NSNCageFileCrawler, self).__init__(controller, dbProxy, request)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)
        self.__cageNumFilePath = request[NSNCageFileCrawler.PARA_CAGE_FILE_PATH]
        self.__cageNum = request[NSNCageFileCrawler.PARA_CAGE_NUM]

    def run(self):
        super(NSNCageFileCrawler, self).run()
        self.logger.info('Begin to Crawl for %s', self.__cageNumFilePath)
        fileNameList = os.listdir(self.__cageNumFilePath)
        isFirstPage = True
        for fileName in fileNameList:
            if fileName.endswith('html'):
                self.__parse(os.path.join(self.__cageNumFilePath, fileName), isFirstPage)
                isFirstPage = False 
        
        self._reportDone(CrawlerConstants.VAL_STATUS_FINISH )
        self.logger.info('Finish Crawl for %s', self.__cageNumFilePath)
    
    def __testFindPanel(self, content):
        rindex=-1
        while True:
            rindex = content[:rindex].rfind('panel panel-primary')
            if rindex<0:
                break
            
    def __parse(self, filename, isFirstPage):  
        try:
                      
            self.logger.debug('Crawl %s', filename)
            fileObj = open(filename)
            try:
                content = fileObj.read()
            finally:
                fileObj.close()
            soup = BeautifulSoup(content)
            panels = soup.findAll('div',{'class':'panel panel-primary'})
            title = None
            addInfo = dict()
    
            for panel in panels:
                if isFirstPage:
                    if 'Address For CAGE' in panel.text:
                        self.__parseAddress(panel)
                    elif 'Additional Data For CAGE' in panel.text:
                        addInfo.update(self.__parseAdditionalInfo(panel))
                    elif 'Former Names For CAGE' in panel.text:
                        addInfo.update(self.__parseFormerNames(panel))
                        
                if 'National Stock Numbers Related To CAGE' in panel.text:
                    self.__parsePartList(panel)
            if isFirstPage:
                title = self.__parseTitle(soup)
                self.__insertCageInfo(title, addInfo)
        except Exception, e:
            self.logger.error('Fail to parse:%s',filename)
        return CrawlerConstants.VAL_STATUS_FINISH 
    
    def __insertCageInfo(self, title, addInfo):
        sql = 'insert into t_cage_info (cage_id, cage_name, addtional_info) values ("%s","%s","%s")' % (self.__cageNum, title, str(addInfo))
        self.dbProxy.execute(sql)
        self.dbProxy.commit()
    
    def __insertCageNsnPartInfo(self, infoDictList):
        values = list()
        refList = list()
        replaceList = list()
        for infoDict in infoDictList:
            if 'nsn_name' not in infoDict:
                infoDict['nsn_name']=''
            valueStr = '("%s","%s","%s","%s","%s","%s")'%(
                      self.__cageNum,
                      infoDict['fsg_id'],
                      infoDict['fsc_id'],
                      infoDict['nsn_id'],
                      infoDict['nsn_name'],
                      infoDict['part_num']
                      )
            if 'reference' in infoDict:
                refList.append('("%s","%s","%s","%s")'% (
                            self.__cageNum,
                            infoDict['nsn_id'],
                            infoDict['part_num'],
                            infoDict['reference']
                            ))
            if 'replaced' in infoDict:
                replaceList.append('("%s","%s","%s","%s")' % (
                            self.__cageNum,
                            infoDict['nsn_id'],
                            infoDict['part_num'],
                            infoDict['replaced']
                            ))
                
            values.append(valueStr)
        #insert to main
        sql = 'insert into r_cage_nsn_part (cage_id, fsg_id, fsc_id, nsn_id, nsn_name, part_num) values '
        sql = sql + ','.join(values)            
        self.dbProxy.execute(sql)
        
        #insert into ref
        if len(refList)>0:
            sql = 'insert into r_nsn_reference (cage_id, nsn_id, part_num, reference_nsn_id) values ' + ','.join(refList)
            self.dbProxy.execute(sql)
        
        #insert into replace
        if len(replaceList)>0:
            sql = 'insert into r_nsn_replace (cage_id, nsn_id, part_num, replacedby_nsn_id) values ' + ','.join(replaceList)
            self.dbProxy.execute(sql)
            
        self.dbProxy.commit()
    
    def __parseFormerNames(self, panel):
        tables = panel.findAll('table')
        if len(tables) == 0:
            return {}
        else:
            return {'former_name': tables[0].text.strip()}
    
    def __parseTitle(self, soup):
        titles= soup.findAll('title')
        titleFull = titles[0].text
        lindex = titleFull.find('-')
        rindex = titleFull.rfind('-')
        title = titleFull[lindex+1:rindex].strip()
        return title

    def __parseAddress(self, panel):
        #self.logger.debug('parse address')
        pass
    
    def __parseAdditionalInfo(self, panel):
        tables = panel.findAll('table')
        if len(tables)==0:
            self.logger.warn('No table found when parsing additional info for cage:%s', self.__cageNumFilePath)
            return {}
        trs = tables[0].findAll('tr')
        addInfo = dict()
        for tr in trs:
            tds = tr.findAll('td')
            key = tds[0].text.strip().strip(':').replace(' ', '_').lower()
            value = tds[1].text
            addInfo[key] = value
        return addInfo
        
    def __parsePartList(self, panel):
        tables = panel.findAll('table')
        if len(tables)==0:
            self.logger.warn('No table found when parsing part list info for cage:%s', self.__cageNumFilePath)
            return
        
        trs = tables[0].findAll('tr')
        #the first tr is for header, so from the second line on
        #and the subsequent trs are for items
        #for each item, it consumes 3 trs, one for valid info row, one for quotation row and one for blank row
        i=1
        infoDictList = list()
        while i<len(trs):
            tr = trs[i]
            i+=4
            tds = tr.findAll('td')
            if len(tds)<6:
                self.logger.warn('Invalid row found:%s', tr)
                continue
            infoList = tds[5].text.strip().split('\n')
            infoDict = self.__parseInfoList(infoList, tds[3].text.strip())
            infoDict['fsg_id'] = tds[1].text.strip()
            infoDict['fsc_id'] = tds[2].text.strip()
            infoDict['nsn_id'] = tds[3].text.strip()
            infoDict['part_num'] = tds[4].text.strip()
            infoDictList.append(infoDict)
            self.totalNum+=1
        
        if len(infoDictList)>0:
            self.__insertCageNsnPartInfo(infoDictList)
    
    def __parseInfoList(self, infoList, nsnNum):
        resultDict = dict()
        if len(infoList)>0:
            for info in infoList:
                info_str = info.strip().strip('\r').strip('\n').strip('\r').strip()
                if info_str == '':
                    continue
                index = info_str.find('Replaced by')
                if index>=0:
                    resultDict['replaced'] = info_str[index+11:].strip()
                    continue
                index = info_str.find('Alternate References:')
                if index>=0:
                    reference = self.__parseReference(info_str[index+21:].strip().split(','), nsnNum)
                    if reference is not None and len(reference)>0:
                        resultDict['reference'] = reference
                    continue                      
                if 'CANCELLED' not in info_str:
                    resultDict['nsn_name'] = info_str
                
        
        return resultDict
    
    def __parseReference(self, referenceStrList, nsnNum):
        referenceList = list()
        previousFullWithoutHyphen = None
        for ref in referenceStrList:
            ref = ref.strip()            
            if len(ref)==13:
                previousFullWithoutHyphen = ref
            else:
                ref = previousFullWithoutHyphen[:13-len(ref)]+ref
            ref = ref[:4]+'-'+ref[4:6]+'-'+ref[6:9]+'-'+ref[9:]                
            if ref==nsnNum:
                continue
            referenceList.append(ref)
            
        return referenceList
             

if __name__ == '__main__':
    '''
    if PIDUtils.isPidFileExist(LOGGER_NAME_CRAWL):
        print 'Previous process is on-going, please stop it firstly'
        sys.exit(1)
    '''
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CRAWL, pid)
    #Logging.initLogger('conf/crawler.logging.cfg')
    Logging.initLogger('F:\\program\\crm\\crawler\\src\\python\\conf\\crawler.logging.win.cfg')
    #ins = NSNCageFileCrawlerManager('conf/'+LOGGER_NAME_CRAWL+'.cfg')
    ins = NSNCageFileCrawlerManager('F:\\program\\crm\\crawler\\src\\python\\conf\\nfc.cfg')
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CRAWL, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                    