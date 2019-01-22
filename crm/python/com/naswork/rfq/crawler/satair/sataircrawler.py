import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])
import traceback

from com.naswork.rfq.common.utils import Logging,  PIDUtils 
from com.naswork.rfq.common.crawlercommon import CrawlerConstants, CrawlerManager, CrawlerBase
from bs4 import BeautifulSoup

COMPANY_URL = 'http://www.satair.com/products/product-list?page=%d'
PARTLIST_URL = 'http://www.satair.com/products/product-list?supp=%s&page=%d'
#http://www.satair.com/products/product-list?supp=73030&page=2
PART_URL = 'http://www.satair.com/products/product-list/%s'
#http://www.satair.com/products/product-list/0000052370
LOGGER_NAME_CRAWL = 'sat'
PAGE_TYPE_COMPANY_LIST = 0
PAGE_TYPE_PART_LIST = 1
PAGE_TYPE_PART_DETAIL = 2

PAGE_STATUS_INIT = 0
PAGE_STATUS_PARSING = 1
PAGE_STATUS_DONE = 2
PAGE_STATUS_FAILURE= -1

class SatAirCrawlerManager(CrawlerManager):
    def __init__(self, json_config_file):
        '''
        Constructor
        '''
        self.__startPageNum = None
        self.__endPageNum = None
        super(SatAirCrawlerManager, self).__init__(json_config_file, 0.001, None)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)

    def _getNonCommonConfig(self, config):
        self.__startPageNum = config['startPageNum']
        self.__endPageNum = config['endPageNum']

    def _initTask(self):
        if self.__startPageNum == 0:
            self.__fetchNewTask()
            if len(self._taskList)==0:
                self.__fetchFailureTask()        
        else:
            page = self.__startPageNum
            sql = 'insert into c_page (page_type, url, status) values '
            values = list()
            while page<=self.__endPageNum:
                url = COMPANY_URL%page
                self._taskList.append((url, PAGE_TYPE_COMPANY_LIST))
                page+=1
                values.append('(%d, "%s", %d)'%(PAGE_TYPE_COMPANY_LIST, url,0))
            sql = sql + ','.join(values)
            self.dbProxy.execute(sql)
            self.dbProxy.commit()

    def _generateTask(self, task, checkTaskList=True):
        if super(SatAirCrawlerManager, self)._generateTask(task,checkTaskList) is False:
            return False
        item = self._taskList.pop()
        task[CrawlerConstants.PARA_CLASS] = SatAirCrawler
        task[SatAirCrawler.PARA_URL] = item[0]
        task[SatAirCrawler.PARA_PAGE_TYPE] = item[1]
        return True
    def __fetchFailureTask(self):
        sql = 'select url, page_type from c_page where status=-1 and parse_count<=3 order by page_type asc,parse_count asc limit %d' % self._numThread
        self.dbProxy.execute(sql)
        urlList = list()
        for result in self.dbProxy.cur.fetchall():
            urlList.append('"'+result[0]+'"')
            self._taskList.append((result[0], result[1]))
        
        if len(urlList)>0:
            sql = 'update c_page set status=%d where url in (%s)' % (PAGE_STATUS_PARSING, ','.join(urlList))
            self.dbProxy.execute(sql)
            self.dbProxy.commit()
        
    def __fetchNewTask(self):
        sql = 'select url, page_type from c_page where status=0 order by page_type asc limit %d' % self._numThread
        self.dbProxy.execute(sql)
        urlList = list()
        for result in self.dbProxy.cur.fetchall():
            urlList.append('"'+result[0]+'"')
            self._taskList.append((result[0], result[1]))
        
        if len(urlList)>0:
            sql = 'update c_page set status=%d where url in (%s)' % (PAGE_STATUS_PARSING, ','.join(urlList))
            self.dbProxy.execute(sql)
            self.dbProxy.commit()
            
    def _handleNotifyDone(self, request):
        if len(self._taskList)==0:
            self.__fetchNewTask()
            if len(self._taskList)==0:
                self.__fetchFailureTask()        
            self.logger.debug('Fetch tasks:%d', len(self._taskList))
        super(SatAirCrawlerManager, self)._handleNotifyDone(request)

class SatAirCrawler(CrawlerBase):
    PARA_URL = 'url'
    PARA_PAGE_TYPE = 'pageType'
    def __init__(self, controller, dbProxy, request):
        super(SatAirCrawler, self).__init__(controller, dbProxy, request)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)
        self.__url = request[SatAirCrawler.PARA_URL]
        self.__pageType = request[SatAirCrawler.PARA_PAGE_TYPE]

    def run(self):
        super(SatAirCrawler, self).run()
        self.logger.info('Begin to Crawl for %s', self.__url)
        content = self._fetchContent(self.__url, record=False)
        try:
            if self.__pageType == PAGE_TYPE_COMPANY_LIST:
                companyHrefList = parseCompanyList(content)
                self.__insertUrl(companyHrefList, PAGE_TYPE_PART_LIST)                
            elif self.__pageType == PAGE_TYPE_PART_LIST:
                (partList,companyHrefList) = parsePartList(content, self.__url)
                self.__insertUrl(partList, PAGE_TYPE_PART_DETAIL)
                self.__insertUrl(companyHrefList, PAGE_TYPE_PART_LIST)
            elif self.__pageType == PAGE_TYPE_PART_DETAIL:
                infoList = parseDetailInfo(content)
                self.__insertDetailInfo(infoList)
            self.__updateSelfStatus(PAGE_STATUS_DONE)
            self._reportDone(CrawlerConstants.VAL_STATUS_FINISH)
            self.logger.info('Finish Crawl for %s', self.__url)
        except Exception, e:
            traceInfo = traceback.format_exc()
            self.logger.warn('Fail to parse %s:%s', self.__url, traceInfo)
            self.__updateSelfStatus(PAGE_STATUS_FAILURE)
            self._reportDone(CrawlerConstants.VAL_STATUS_FAILURE)
    
    def __insertDetailInfo(self, infoList):
        if infoList is None or len(infoList)==0:
            return
        sql = 'insert into r_part_info (cage_id,cage_name,part_num,part_name,pn_cage) values '
        sql += ','.join(
                        map(lambda info:
                            '("%s","%s","%s","%s","%s")'%(
                                                          info.cage_code,
                                                          info.manufacturer,
                                                          info.pn,
                                                          info.description,
                                                          info.pn_cage_code
                                                          ), 
                            infoList)
                        )
        if self.dbProxy.execute(sql)<0:
            raise Exception('Fail to execute sql')
        self.dbProxy.commit()
        
    def __updateSelfStatus(self, status):
        sql = 'update c_page set status=%d, parse_count=parse_count+1 where url="%s"'%(status, self.__url)
        if self.dbProxy.execute(sql)<0:
            raise Exception('Fail to execute sql')
        self.dbProxy.commit()    
    
    def __insertUrl(self, urlList, pageType):
        if urlList is None or len(urlList)==0:
            return
        if pageType == PAGE_TYPE_PART_DETAIL:
            sql = 'select url from c_page where url in (%s)' % (','.join(map(lambda x: '"'+x+'"' , urlList)))
            self.dbProxy.execute(sql)
            for result in self.dbProxy.cur.fetchall():
                try:
                    urlList.remove(result[0])
                except:
                    pass
        if urlList is None or len(urlList)==0:
            return
        sql = 'insert into c_page (page_type, url, status) values '
        values = list()
        for url in urlList:
            values.append('(%d, "%s", %d)'%(pageType, url,0))
        sql = sql + ','.join(values)
        if self.dbProxy.execute(sql)<0:
            raise Exception('Fail to execute sql')
        self.dbProxy.commit()

def parseCompanyList(content):
    soup = BeautifulSoup(content)
    divs = soup.findAll('div',{'class':'part-list'})
    if len(divs)==0:
        raise Exception('No divs fond for company list')
    ahrefs = divs[0].findAll('a')
    companyHrefList = list()
    for ahref in ahrefs:
        href = ahref['href']
        cageList = href[href.rfind('=')+1:].split(',')
        for cageId in cageList:
            companyHrefList.append('http://www.satair.com/products/product-list?supp='+cageId)
    return companyHrefList
    
def parsePartList(content, url):
    soup = BeautifulSoup(content)
    divs = soup.findAll('div',{'class':'part-list'})
    if len(divs)==0:
        raise Exception('No divs fond for part list')
    rowDivs = divs[0].findAll('div',{'class':'row'})
    if len(rowDivs)==0:
        raise Exception('No row divs fond for part list')
    ahrefs = rowDivs[0].findAll('a')#120 per page
    partHrefList = list()
    for ahref in ahrefs:
        partHrefList.append('http://www.satair.com'+ahref['href'])
    companyHrefList = list()
    if url.find('page')<0:
        lis = soup.findAll('label',{'class':'ListPages'})
        if len(lis)==0:
            raise Exception('Fail to fetch page info')
        totalPage = int(lis[0].text.split('/')[1].strip())
        for page in range(2, totalPage+1):
            companyHrefList.append(url+'&page=%d'%page)
    return (partHrefList, companyHrefList)

def parseDetailInfo(content):
    soup = BeautifulSoup(content)
    divs = soup.findAll('div',{'class':'information-list'})
    if len(divs)==0:
        raise Exception('No divs fond for detail info')
    
    tables = divs[0].findAll('table')
    infoList = list()
    for table in tables:
        trs = table.findAll('tr')
        manufacturer = getRowValue(trs[0])
        pn = getRowValue(trs[1])
        cage_code = getRowValue(trs[2])
        pn_cage_code = getRowValue(trs[3])
        description = getRowValue(trs[4])
        infoList.append(DetailInfo(manufacturer, pn, cage_code, pn_cage_code, description))
    
    return infoList
    
def getRowValue(tr):
    tds = tr.findAll('td')
    return tds[1].text.strip()

class DetailInfo(object):
    def __init__(self, manufacturer, pn, cage_code, pn_cage_code, description):
        self.manufacturer = manufacturer
        self.pn = pn
        self.cage_code = cage_code
        self.pn_cage_code = pn_cage_code
        self.description = description

if __name__ == '__main__':
    '''
    if PIDUtils.isPidFileExist(LOGGER_NAME_CRAWL):
        print 'Previous process is on-going, please stop it firstly'
        sys.exit(1)
    '''
    import platform
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CRAWL, pid)
    if 'window' in platform.system().lower():
        Logging.initLogger(os.path.join('conf','crawler.logging.win.cfg'))
    else:
        Logging.initLogger(os.path.join('conf','crawler.logging.cfg'))
    ins = SatAirCrawlerManager(os.path.join('conf',LOGGER_NAME_CRAWL+'.cfg'))
    #ins = SatAirCrawlerManager('F:\\program\\crm\\crawler\\src\\python\\conf\\nfc.cfg')
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CRAWL, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                