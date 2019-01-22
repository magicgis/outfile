'''
Created on May 30, 2016
Crawl a specific cage from url: https://www.nsncenter.com/CAGE/<cage_num>
where cage_num is a string with length of 5 composed of digit & alphabet only.

We fetch the cage base information and the associated part list info
@author: eyaomai
'''
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind('com/naswork')])
import traceback
import os
from com.naswork.rfq.common.utils import Logging,  PIDUtils 
from com.naswork.rfq.common.crawlercommon import CrawlerConstants, CrawlerManager, CrawlerBase
from bs4 import BeautifulSoup

LOGGER_NAME_CRAWL = 'ncg'

class NSNCageCrawlerManager(CrawlerManager):
    
    CAGE_CHAR_SEQ = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
    CAGE_CHAR_LENGTH = len(CAGE_CHAR_SEQ) 
    def __init__(self, json_config_file):
        '''
        Constructor
        '''
        self.__cageNumIndex = [-1,0,0,0,0]
        self.__noMore = False
        self.__cageNumList = []
        super(NSNCageCrawlerManager, self).__init__(json_config_file, 0.1, None)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)

    def __generateNextCageNum(self):
        if self.__noMore:
            return None
        digitIndex = 0
        while digitIndex<5:
            self.__cageNumIndex[digitIndex]+=1
            if self.__cageNumIndex[digitIndex] == NSNCageCrawlerManager.CAGE_CHAR_LENGTH:
                self.__cageNumIndex[digitIndex] = 0
                digitIndex+=1
            else:
                break
        if digitIndex==5:
            self.__noMore = True
            return None
        else:
            return NSNCageCrawlerManager.CAGE_CHAR_SEQ[self.__cageNumIndex[4]]+\
                NSNCageCrawlerManager.CAGE_CHAR_SEQ[self.__cageNumIndex[3]]+\
                NSNCageCrawlerManager.CAGE_CHAR_SEQ[self.__cageNumIndex[2]]+\
                NSNCageCrawlerManager.CAGE_CHAR_SEQ[self.__cageNumIndex[1]]+\
                NSNCageCrawlerManager.CAGE_CHAR_SEQ[self.__cageNumIndex[0]]
                 
    def _generateTask(self, task, checkTaskList=True):
        if super(NSNCageCrawlerManager, self)._generateTask(task,checkTaskList) is False:
            return False
        if self.__cageNumList is None or len(self.__cageNumList)==0:
            nextCageNum = self.__generateNextCageNum()
        else:
            nextCageNum = self.__generateCageNumFromConf()        
        if nextCageNum is None:
            return False
        else:
            task[CrawlerConstants.PARA_CLASS] = NSNCageCrawler
            task[NSNCageCrawler.PARA_CAGE_NUM] = nextCageNum
            return True

    def __generateCageNumFromConf(self):        
        return self._taskList.pop()        
    
    def _getNonCommonConfig(self, config):
        self.__cageNumList = config['cageNumList']
        self.logger.debug('Read cageNumList:%s', self.__cageNumList)
        
    def _initTask(self):
        self.logger.debug('Init, cageNumList:%s', self.__cageNumList)
        if self.__cageNumList!=None and len(self.__cageNumList)>0:
            self._taskList.extend(self.__cageNumList)
        else:
            self._taskList.append(0)
        
class NSNCageCrawler(CrawlerBase):
    URL = 'https://www.nsncenter.com/CAGE/%s?PageNumber=%d'
    PARA_CAGE_NUM = 'cage_num'
    def __init__(self, controller, dbProxy, request):
        super(NSNCageCrawler, self).__init__(controller, dbProxy, request)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)
        self.__cageNum = request[NSNCageCrawler.PARA_CAGE_NUM]

    def run(self):
        super(NSNCageCrawler, self).run()
        self.logger.info('Begin to Crawl for %s', self.__cageNum)
        page = 1
        while True:
            status =self.__parse(page) 
            if status!=CrawlerConstants.VAL_STATUS_MORE:
                break
            page+=1
        
        self._reportDone(CrawlerConstants.VAL_STATUS_FINISH )
        self.logger.info('Finish Crawl for %s', self.__cageNum)
    
    def __testFindPanel(self, content):
        rindex=-1
        while True:
            rindex = content[:rindex].rfind('panel panel-primary')
            self.logger.debug('rindex:%d', rindex)
            if rindex<0:
                break
    def __parse(self, page):
        url = NSNCageCrawler.URL % (self.__cageNum, page)
        self.logger.debug('Crawl %s', url)
        content = self._fetchContent(url)
        soup = BeautifulSoup(content)
        panels = soup.findAll('div',{'class':'panel panel-primary'})
        '''
        pi=0
        for panel in panels:
            self.logger.debug('Panel %d:%s',pi, panel)
            pi+=1
        '''
        if len(panels)!=3 and len(panels)!=4:
            self.logger.warn('Panels length is not expected:%d', len(panels))
            return CrawlerConstants.VAL_STATUS_FINISH
        else:
            self.logger.debug('Panel length:%d', len(panels))
        for panel in panels:
            if page==1:
                if 'Address For CAGE' in panel.text:
                    self.__parseAddress(panel)
                elif 'Additional Data For CAGE' in panel.text:
                    self.__parseAdditionalInfo(panel)
            if 'National Stock Numbers Related To CAGE' in panel.text:
                self.__parsePartList(panel)
        if page == 1:
            self.__parseTitle(soup)

        nextPageDisabled = soup.findAll('li',{'class':'next disabled'})
        if len(nextPageDisabled)>0:
            return CrawlerConstants.VAL_STATUS_FINISH 
        else:
            nextPage = soup.findAll('li',{'class':'next'})
            if len(nextPage)>0:
                return CrawlerConstants.VAL_STATUS_MORE
            else:
                return CrawlerConstants.VAL_STATUS_FINISH
        
    def __parseTitle(self, soup):
        titles= soup.findAll('title')
        titleFull = titles[0].text
        lindex = titleFull.find('-')
        rindex = titleFull.rfind('-')
        title = titleFull[lindex+1:rindex].strip()
        self.logger.debug('Title is:%s', title)
        return title

    def __parseAddress(self, panel):
        self.logger.debug('parse address')
    
    def __parseAdditionalInfo(self, panel):
        tables = panel.findAll('table')
        if len(tables)==0:
            self.logger.warn('No table found when parsing additional info for cage:%s', self.__cageNum)
            return
        trs = tables[0].findAll('tr')
        addInfo = dict()
        for tr in trs:
            tds = tr.findAll('td')
            key = tds[0].text.strip().strip(':').replace(' ', '_').lower()
            value = tds[1].text
            addInfo[key] = value
        self.logger.debug('AddtionalInfo:%s', addInfo)
        return addInfo
        
    def __parsePartList(self, panel):
        tables = panel.findAll('table')
        if len(tables)==0:
            self.logger.warn('No table found when parsing part list info for cage:%s', self.__cageNum)
            return
        
        trs = tables[0].findAll('tr')
        #the first tr is for header, so from the second line on
        #and the subsequent trs are for items
        #for each item, it consumes 3 trs, one for valid info row, one for quotation row and one for blank row
        i=1
        while i<len(trs):
            tr = trs[i]
            i+=4
            tds = tr.findAll('td')
            if len(tds)<6:
                self.logger.warn('Invalid row found:%s', tr)
                continue
            infoList = tds[5].text.strip().split('\r\n')
            infoDict = self.__parseInfoList(infoList, tds[3].text.strip())
            infoDict['nsnNum'] = tds[3].text.strip()
            infoDict['partNum'] = tds[4].text.strip()
            self.logger.debug('Info under %d row:%s', i, infoDict)
            self.totalNum+=1
            
    
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
                resultDict['nsnName'] = info_str
                
        
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
    Logging.initLogger('conf/crawler.logging.cfg')
    #Logging.initLogger('F:\\program\\crm\\crawler\\src\\python\\conf\\crawler.logging.cfg')
    ins = NSNCageCrawlerManager('conf/'+LOGGER_NAME_CRAWL+'.cfg')
    #ins = NSNCageCrawlerManager('F:\\program\\crm\\crawler\\src\\python\\conf\\nsn.cfg')
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CRAWL, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                    