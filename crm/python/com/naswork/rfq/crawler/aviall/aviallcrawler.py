'''
Created on Aug 11, 2016

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])
import traceback

from com.naswork.rfq.common.utils import Logging,  PIDUtils 
from com.naswork.rfq.common.crawlercommon import CrawlerConstants, CrawlerManager, CrawlerBase
from bs4 import BeautifulSoup
import MySQLdb

LOGGER_NAME_CRAWL = 'avi'

PAGE_TYPE_COMPANY_LIST = 0
PAGE_TYPE_PART_LIST = 1
PAGE_TYPE_PART_DETAIL = 2
PAGE_TYPE_PART_DETAIL_IMG = 3

PAGE_STATUS_INIT = 0
PAGE_STATUS_PARSING = 1
PAGE_STATUS_DONE = 2
PAGE_STATUS_FAILURE= -1

class AviAllCrawlerManager(CrawlerManager):
    def __init__(self, json_config_file):
        '''
        Constructor
        '''
        self.__imgOnly = False
        self.__imgSavePath = ''
        super(AviAllCrawlerManager, self).__init__(json_config_file, 0.001, None)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)

    def _getNonCommonConfig(self, config):
        imgOnly = config['imgOnly']
        if imgOnly.lower() == 'true':
            self.__imgOnly = True            
        self.__imgSavePath = config['imgSavePath']
        
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
            if self.__imgOnly:
                self.__fetchImgTask()
            else:
                self.__fetchNewTask()
            if len(self._taskList)==0:
                self.__fetchFailureTask()        
            self.logger.debug('Fetch tasks:%d', len(self._taskList))
        super(AviAllCrawlerManager, self)._handleNotifyDone(request)

    def _generateTask(self, task, checkTaskList=True):
        if super(AviAllCrawlerManager, self)._generateTask(task,checkTaskList) is False:
            return False
        item = self._taskList.pop()
        task[CrawlerConstants.PARA_CLASS] = AviAllCrawler
        task[AviAllCrawler.PARA_URL] = item[0]
        task[AviAllCrawler.PARA_PAGE_TYPE] = item[1]
        task[AviAllCrawler.PARA_IMG_ONLY] = self.__imgOnly
        task[AviAllCrawler.PARA_IMG_SAVE_PATH] = self.__imgSavePath
        return True

    def _initTask(self):
        if self.__imgOnly:
            self.__fetchImgTask()
        else:
            self.__fetchNewTask()
        if len(self._taskList)==0:
            self.__fetchFailureTask()        
    
    def __fetchImgTask(self):
        sql = 'select url from c_page where page_type=2 and status=0 order by parse_count asc limit %d' % self._numThread
        self.dbProxy.execute(sql)
        urlList = list()
        for result in self.dbProxy.cur.fetchall():
            urlList.append('"'+result[0]+'"')
            self._taskList.append((result[0], 2))
        
        if len(urlList)>0:
            sql = 'update c_page set status=%d where url in (%s)' % (PAGE_STATUS_PARSING, ','.join(urlList))
            self.dbProxy.execute(sql)
            self.dbProxy.commit()
        
    
class AviAllCrawler(CrawlerBase):
    PARA_URL = 'url'
    PARA_PAGE_TYPE = 'pageType'
    PARA_IMG_ONLY = 'imgOnly'
    PARA_IMG_SAVE_PATH = 'imgSavePath'
    def __init__(self, controller, dbProxy, request):
        super(AviAllCrawler, self).__init__(controller, dbProxy, request)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)
        self.__url = request[AviAllCrawler.PARA_URL]
        self.__pageType = request[AviAllCrawler.PARA_PAGE_TYPE]
        if AviAllCrawler.PARA_IMG_ONLY not in request:
            self.__imgOnly = False
        else:
            self.__imgOnly = request[AviAllCrawler.PARA_IMG_ONLY]
        
        self.__imgSavePath = request[AviAllCrawler.PARA_IMG_SAVE_PATH]
        
    def run(self):
        super(AviAllCrawler, self).run()
        self.logger.info('Begin to Crawl for %s', self.__url)
        content = self._fetchContent(self.__url, record=False)
        try:
            if self.__pageType == PAGE_TYPE_PART_LIST:
                (partList,companyHrefList) = parsePartList(content, self.__url)
                self.__insertUrl(partList, PAGE_TYPE_PART_DETAIL)
                self.__insertUrl(companyHrefList, PAGE_TYPE_PART_LIST)
            elif self.__pageType == PAGE_TYPE_PART_DETAIL:
                if self.__imgOnly:
                    imgUrl = parseImgUrl(content)                    
                    if imgUrl is not None:
                        self.__downloadImg(imgUrl)                        
                else:
                    detailInfo = parseDetailInfo(content)
                    detailInfo.sourceUrl = self.__url
                    self.__insertDetailInfo(detailInfo)
            self.__updateSelfStatus(PAGE_STATUS_DONE)
            self._reportDone(CrawlerConstants.VAL_STATUS_FINISH)
            self.logger.info('Finish Crawl for %s', self.__url)
        except Exception, e:
            traceInfo = traceback.format_exc()
            self.logger.warn('Fail to parse %s:%s', self.__url, traceInfo)
            self.__updateSelfStatus(PAGE_STATUS_FAILURE)
            self._reportDone(CrawlerConstants.VAL_STATUS_FAILURE)

    def __insertDetailInfo(self, detailInfo):
        sql = '''insert into r_part_info (catalog,part_num,description,
                    manufacturer,nsn,usml,hazmat_code,
                    shelf_life,cage_code,ata_code,
                    weight,dimensions,country_of_origin,
                    eccn,harmonize_code,schedule_b_code, source_url) values 
                    ("%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s")
            '''%(
                    detailInfo.catalog, detailInfo.pn, detailInfo.description,
                    detailInfo.manufacturer, detailInfo.nsn, detailInfo.usml,
                    detailInfo.hazmatCode, detailInfo.shelfLife, detailInfo.cageCode,
                    detailInfo.ataCode, detailInfo.weight, detailInfo.dimensions,
                    detailInfo.countryOfOrigin, detailInfo.eccn, detailInfo.harmonizeCode,
                    detailInfo.scheduleBCode, detailInfo.sourceUrl
                 )
        if self.dbProxy.execute(sql)<0:
            raise Exception('Fail to execute sql')
        self.dbProxy.commit()
        
    def __updateSelfStatus(self, status):
        sql = 'update c_page set status=%d, parse_count=parse_count+1 where url="%s"'%(status, self.__url)
        if self.dbProxy.execute(sql)<0:
            raise Exception('Fail to execute sql:'+sql)
        self.dbProxy.commit()    
    
    def __downloadImg(self, imgUrl):
        content = self._fetchRawContent(imgUrl)
        keyword = 'product-images/'
        index = imgUrl.rfind(keyword)
        fileName = imgUrl[index+len(keyword):].replace('/','_').lower()
        fullFilePath = os.path.join(self.__imgSavePath,fileName)
        f = open(fullFilePath, 'w')
        f.write(content)
        f.flush()
        f.close()
        sql = 'update r_part_info set img_url="%s", img_path="%s" where source_url="%s"' %(
                imgUrl, fullFilePath, self.__url)
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
            
def parsePartList(content, url):
    soup = BeautifulSoup(content)
    tables = soup.findAll('table',{'id':'page_0'})
    if len(tables)<2:
        raise Exception('No tables found for part list')
    ahrefs = tables[1].findAll('a',{'id':'partDetail'})
    if len(ahrefs)==0:
        raise Exception('No ahrefs found for part list')

    partHrefList = map(lambda ahref: 'https://www.aviall.com'+ahref['href'],
                       ahrefs)
    companyHrefList = list()
    pageDivs = soup.findAll('div',{'id':'pagenum_nav'})
    if len(pageDivs)>0:
        ahrefs = pageDivs[0].findAll('a')
        for ahref in ahrefs:
            text = ahref.text.strip().lower()
            if text=='next page':
                companyHrefList.append('https://www.aviall.com'+ahref['href'])
    return (partHrefList, companyHrefList)

def parseImgUrl(content):
    soup = BeautifulSoup(content)
    divs = soup.findAll('div',{'id':'fullsizeImage'})
    if len(divs)==0:
        raise Exception('No img divs found for part detail')
    imgs = divs[0].findAll('img')
    if len(imgs)==0:
        raise Exception('No img found for part detail')
    url = imgs[0]['src']
    if url == '/':
        return None
    else:
        return 'https://www.aviall.com'+url

def parseDetailInfo(content):
    soup = BeautifulSoup(content)
    contentDivs = soup.findAll('div',{'id':'content'})
    if len(contentDivs)==0:
        raise Exception('No content divs found for part detail')
    trs = contentDivs[0].findAll('tr')
    if len(trs)==0:
        raise Exception('No trs found for part detail')
    detailInfo = DetailInfo()
    detailInfo.catalog = trs[0].findAll('td')[1].findAll('a')[0].text.strip()
    detailInfo.pn = _fetchInfoText(trs, 1)
    detailInfo.description = _fetchInfoText(trs, 2)
    detailInfo.manufacturer = _fetchInfoText(trs, 3)
    detailInfo.nsn = _fetchInfoText(trs, 5)
    detailInfo.usml = _fetchInfoText(trs, 5, 3)
    detailInfo.hazmatCode = _fetchInfoText(trs, 6)
    detailInfo.shelfLife = _fetchInfoText(trs,6,3)
    detailInfo.cageCode = _fetchInfoText(trs,7)
    detailInfo.ataCode = _fetchInfoText(trs,7,3)
    detailInfo.weight = _fetchInfoText(trs,8)
    detailInfo.dimensions = _fetchInfoText(trs,8,3)
    detailInfo.countryOfOrigin = _fetchInfoText(trs,9)
    detailInfo.eccn = _fetchInfoText(trs,9,3)
    detailInfo.harmonizeCode = _fetchInfoText(trs,10)
    detailInfo.scheduleBCode = _fetchInfoText(trs,10,3)
    return detailInfo

def _fetchInfoText(trs, trIndex, tdIndex=1):
    td = trs[trIndex].findAll('td')[tdIndex]
    ret = MySQLdb.escape_string(td.text.strip())
    if ret.lower() == 'none':
        ret = ''
    if ret.lower() == 'n/a':
        ret = ''
    return ret

class DetailInfo(object):
    def __init__(self):
        self.catalog =None
        self.pn = None
        self.description = None
        self.manufacturer = None
        self.nsn = None
        self.usml = None
        self.hazmatCode = None
        self.shelfLife = None
        self.cageCode = None
        self.ataCode = None
        self.weight = None
        self.dimensions = None
        self.countryOfOrigin = None
        self.eccn = None
        self.harmonizeCode = None
        self.scheduleBCode = None
        self.sourceUrl = None

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
    ins = AviAllCrawlerManager(os.path.join('conf',LOGGER_NAME_CRAWL+'.cfg'))
    #ins = SatAirCrawlerManager('F:\\program\\crm\\crawler\\src\\python\\conf\\nfc.cfg')
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CRAWL, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                   
        
