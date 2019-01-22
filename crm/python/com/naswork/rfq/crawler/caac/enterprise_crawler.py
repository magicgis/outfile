'''
Created on May 30, 2016
@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])
import traceback

from com.naswork.rfq.common.utils import Logging,  PIDUtils 
from com.naswork.rfq.common.crawlercommon import CrawlerConstants, CrawlerManager, CrawlerBase
import time
import json
LOGGER_NAME_CRAWL = 'cel'
URL = 'http://fsop.caac.gov.cn/g145/CARS/WebSiteQueryServlet?method=enterpriseQuery&sEcho=%d&iColumns=3&sColumns=&iDisplayStart=%d&iDisplayLength=%d&mDataProp_0=enterpriseName&mDataProp_1=licenceCode&mDataProp_2=2&continent=%s&country=%s&orgId=%s&enterpriseName=&_=%d'
PAGE_SIZE = 20

HEARACHY = {
            '1000':['411000','10112','10115','10120','10135','10161','10176','10194','10207'],
            '1001':['10050','10057','10079','10089','10094','10097','10105','10111','10113','10149','10155','10162','10181','10188','10189','10198','10203'],
            '1002':['10039','10070','10140'],
            '1003':['10063'],
            '1004':['10049','10150']
            }

ORGID=['402881fa2c3e722b012c3e7b56a00005',
       '402881fa2c954d54012c954d54530000',
       '402881fb2c965e73012c965e73e20000',
       '402881fb2c965e73012c965ee4200001',
       '402881fb2c965e73012c965f5ac70002',
       '402881fb2c965e73012c965fc8940003',
       '402881fb2c965e73012c966030570004',
       '402881fb2c965e73012c9660d9b60005']

class EnterpriseListCrawlerManager(CrawlerManager):
    def __init__(self, json_config_file):
        '''
        Crawl for enterprise:
CREATE TABLE `enterprise` (
  `ENTERPRISE_ID` varchar(63) NOT NULL DEFAULT '',
  `ENTERPRISE_NAME` varchar(255) DEFAULT NULL,
  `licence_id` varchar(63) DEFAULT NULL,
  `COUNTRY_CODE` varchar(10) DEFAULT NULL,
  `ORGID` varchar(63) DEFAULT NULL,
  `certificate_no` varchar(63) DEFAULT NULL,
  `EXPIRED_DATE` date DEFAULT NULL,
  `address` varchar(1023) DEFAULT NULL,
  `scan_copy_link` varchar(1023) DEFAULT NULL,
  PRIMARY KEY (`ENTERPRISE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8         
        
        '''
        super(EnterpriseListCrawlerManager, self).__init__(json_config_file, 0.1, None)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)

    def _initTask(self):
        '''
        for orgId in ORGID:
            self._taskList.append(('1000','10001',orgId))
        '''
        for continent, countries in HEARACHY.iteritems():
            for country in countries:
                self._taskList.append((continent, country, ''))
        
    def _generateTask(self, task, checkTaskList=True):
        if super(EnterpriseListCrawlerManager, self)._generateTask(task,checkTaskList) is False:
            return False
        
        item = self._taskList.pop()
        if item is not None:
            task[CrawlerConstants.PARA_CLASS] = EnterpriseListCrawler
            task[EnterpriseListCrawler.PARA_CONTINENT_CODE] = item[0]
            task[EnterpriseListCrawler.PARA_COUNTRY_CODE] = item[1]
            task[EnterpriseListCrawler.PARA_ORG_ID] = item[2]
            return True
        else:
            return False
        
class EnterpriseListCrawler(CrawlerBase):
    PARA_CONTINENT_CODE = 'continent_code'
    PARA_COUNTRY_CODE = 'country_code'
    PARA_ORG_ID = 'org_id'
    #sEcho,start, length, continent, country, org, time
    def __init__(self, controller, dbProxy, request):
        super(EnterpriseListCrawler, self).__init__(controller, dbProxy, request)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)
        self.__continentCode = request[EnterpriseListCrawler.PARA_CONTINENT_CODE]
        self.__countryCode = request[EnterpriseListCrawler.PARA_COUNTRY_CODE]
        self.__orgId = request[EnterpriseListCrawler.PARA_ORG_ID]

    def run(self):
        super(EnterpriseListCrawler, self).run()
        self.__parse()
        self._reportDone(CrawlerConstants.VAL_STATUS_FINISH )
    
    def __parse(self):
        page = 1
        timePara = int(time.time()*1000)
        
        while True:
            startNum = (page-1) * PAGE_SIZE
            url = URL % (page, startNum, PAGE_SIZE, self.__continentCode, self.__countryCode, self.__orgId, timePara)
            self.logger.debug('Crawl url:%s', url)
            content = self._fetchContent(url)
            jo = json.loads(content)
            totalItems = int(jo["iTotalDisplayRecords"])
            self.__writeDb(jo["aaData"])
            if page*PAGE_SIZE >= totalItems:
                break  
            timePara += 1
            page+=1
            self.controller.randomSleep()
    
    def __writeDb(self, items):
        sql = 'INSERT INTO ENTERPRISE (enterprise_id, enterprise_name, licence_id, country_code, orgid, certificate_no, expired_date) values '
        values = list()
        for item in items:
            if item['validity']=='':
                valueStr = '("%s", "%s", "%s", "%s", "%s","%s",NULL)' % (item['enterpriseId'],
                                                          item['enterpriseName'],
                                                          item['licenceId'],
                                                          self.__countryCode,
                                                          self.__orgId,
                                                          item['licenceCode']
                                                          )
            else:
                valueStr = '("%s", "%s", "%s", "%s", "%s","%s","%s")' % (item['enterpriseId'],
                                                          item['enterpriseName'],
                                                          item['licenceId'],
                                                          self.__countryCode,
                                                          self.__orgId,
                                                          item['licenceCode'],
                                                          item['validity'])
            values.append(valueStr)
        if len(values)>0:
            self.dbProxy.execute(sql + ','.join(values))
            self.dbProxy.commit()

if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CRAWL, pid)
    Logging.initLogger('conf/crawler.logging.cfg')
    ins = EnterpriseListCrawlerManager('conf/'+LOGGER_NAME_CRAWL+'.cfg')
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CRAWL, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                