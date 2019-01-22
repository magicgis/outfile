'''
Created on May 30, 2016
Crawl the cage catalog for all cages
The url is https://www.nsncenter.com/CAGE/Catalog?CAGEPrefix=<prefix>&PageNumber=<page_num>
The prefix range from 0-9 and A-Z. For each prefix, we have page_num range from 1 until no page
info is found (when the specified page number is invalid, the page result returned will
be of invalid format)

THIS IS NO LONGER NEEDED AS WE CAN LOOP THROUGH CAGE NUM.
@author: eyaomai
'''
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind('com/naswork')])
import traceback
import os
from com.naswork.rfq.common.utils import Logging,  PIDUtils 
from com.naswork.rfq.common.crawlercommon import CrawlerConstants, CrawlerManager, CrawlerBase
from bs4 import BeautifulSoup

LOGGER_NAME_CRAWL = 'nct'

class NSNCageCatalogCrawlerManager(CrawlerManager):
    def __init__(self, json_config_file):
        '''
        Constructor
        '''
        self.__cagePrefixList = []
        super(NSNCageCatalogCrawlerManager, self).__init__(json_config_file, 0.1, None)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)

    def _initTask(self):
        self._taskList.extend(self.__cagePrefixList)

    def _getNonCommonConfig(self, config):
        self.__cagePrefixList = config['cagePrefixList']

    def _generateTask(self, task, checkTaskList=True):
        if super(NSNCageCatalogCrawlerManager, self)._generateTask(task,checkTaskList) is False:
            return False
        cagePrefix = self._taskList.pop()
        task[CrawlerConstants.PARA_CLASS] = NSNCageCatalogCrawler
        task[NSNCageCatalogCrawler.PARA_CAGE_PREFIX] = cagePrefix
        return True
        
class NSNCageCatalogCrawler(CrawlerBase):
    URL = 'https://www.nsncenter.com/CAGE/Catalog?CAGEPrefix=%s&PageNumber=%d'
    PARA_CAGE_PREFIX = 'cage_prefix'
    def __init__(self, controller, dbProxy, request):
        super(NSNCageCatalogCrawler, self).__init__(controller, dbProxy, request)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)
        self.__cagePrefix = request[NSNCageCatalogCrawler.PARA_CAGE_PREFIX]

    def run(self):
        super(NSNCageCatalogCrawler, self).run()
        self.logger.info('Begin to Crawl for %s', self.__cagePrefix)
        page = 1
        while True:
            status =self.__parse(page) 
            if status!=CrawlerConstants.VAL_STATUS_MORE:
                break
            page+=1
        
        self._reportDone(CrawlerConstants.VAL_STATUS_FINISH )
        self.logger.info('Finish Crawl for %s', self.__cagePrefix)
    
    def __parse(self, page):
        url = NSNCageCatalogCrawler.URL % (self.__cagePrefix, page)
        self.logger.debug('Crawl %s', url)
        content = self._fetchContent(url)
        soup = BeautifulSoup(content)
        panels = soup.findAll('div',{'class':'panel panel-primary'})
        if len(panels)==0:
            self.logger.warn('No panel found for %s, %d', self.__cagePrefix, page)
            return CrawlerConstants.VAL_STATUS_FINISH
        trs = panels[0].findAll('tr')
        if len(trs) < 2:
            return CrawlerConstants.VAL_STATUS_FINISH
        for tr in trs[1:]:
            tds = tr.findAll('td')
            cageNum = tds[1].text.strip()
            cageName = tds[2].text.strip()
            self.logger.debug('CageNum:%s, CageName:%s', cageNum, cageName)
            self.totalNum+=1
        
        nextPageDisabled = soup.findAll('li',{'class':'next disabled'})
        if len(nextPageDisabled)>0:
            return CrawlerConstants.VAL_STATUS_FINISH 
        else:
            nextPage = soup.findAll('li',{'class':'next'})
            if len(nextPage)>0:
                return CrawlerConstants.VAL_STATUS_MORE
            else:
                return CrawlerConstants.VAL_STATUS_FINISH

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
    ins = NSNCageCatalogCrawlerManager('conf/'+LOGGER_NAME_CRAWL+'.cfg')
    #ins = NSNCageCrawlerManager('F:\\program\\crm\\crawler\\src\\python\\conf\\nsn.cfg')
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CRAWL, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                            