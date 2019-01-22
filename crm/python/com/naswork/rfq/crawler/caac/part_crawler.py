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
from bs4 import BeautifulSoup
LOGGER_NAME_CRAWL = 'cpc'
ENTERPRISE_URL = 'http://fsop.caac.gov.cn/g145/CARS/WebSiteQueryServlet?method=loadEnterpriseDetail&enterpriseId=%s'
PART_URL = 'http://fsop.caac.gov.cn/g145/CARS/WebSiteQueryServlet?method=aircraftQuery&sEcho=%d&iColumns=9&sColumns=&iDisplayStart=%d&iDisplayLength=20&mDataProp_0=categoryNo&mDataProp_1=partsNumber&mDataProp_2=partsName&mDataProp_3=ataChaptersection&mDataProp_4=manufacturers&mDataProp_5=5&mDataProp_6=fileToAccord&mDataProp_7=mainDevices&mDataProp_8=remark&licenceId=%s&_=%d'
PAGE_SIZE = 20
import MySQLdb

class PartCrawlerManager(CrawlerManager):
    def __init__(self, json_config_file):
        '''
        CREATE TABLE `part` (
  `enterprise_id` varchar(63) DEFAULT NULL,
  `licence_id` varchar(63) DEFAULT NULL,
  `aircraft_part_id` varchar(63) DEFAULT NULL,
  `ata_chapter_section` varchar(63) DEFAULT NULL,
  `category_no` varchar(63) DEFAULT NULL,
  `parts_number` varchar(63) DEFAULT NULL,
  `parts_name` varchar(255) DEFAULT NULL,
  `manufacturers` varchar(63) DEFAULT NULL,
  `inspection` char(1) DEFAULT '0',
  `repair` char(1) DEFAULT '0',
  `modification` char(1) DEFAULT '0',
  `overhaul` char(1) DEFAULT '0',
  `file_to_accord` varchar(255) DEFAULT NULL,
  `main_devices` varchar(255) DEFAULT NULL,
  `remark` text
) ENGINE=MyISAM DEFAULT CHARSET=utf8
        Constructor
        '''
        self.__enterpriseOnly = False
        super(PartCrawlerManager, self).__init__(json_config_file, 0.1, None)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)
        

    def _initTask(self):
        
        
        #this is for overall parsing
        sql = 'select enterprise_id, licence_id from enterprise'
        self.dbProxy.execute(sql)
        results = self.dbProxy.cur.fetchall()
        for result in results:
            self._taskList.append((result[0], result[1]))
        '''    
        
        #this is for enterprise only
        self.__enterpriseOnly = True
        sql = 'select enterprise_id, licence_id from enterprise where address is null'
        self.dbProxy.execute(sql)
        results = self.dbProxy.cur.fetchall()
        for result in results:
            self._taskList.append((result[0], result[1]))
        
        s
        #following is for error handling for those which could not be queried
        self._taskList.append(('757c619e-14588ce3dbf-a8c051a458585a91f6bbafef906370ff','4b51c9b5-151480b00df-a8c051a458585a91f6bbafef906370ff',960))
        self._taskList.append(('757c619e-14588ce37b7-a8c051a458585a91f6bbafef906370ff','455f5d5d-150a1a13014-a8c051a458585a91f6bbafef906370ff',440))
        self._taskList.append(('15340051-144f1ebca7d-a8c051a458585a91f6bbafef906370ff','169d0dac-15543af79f0-a8c051a458585a91f6bbafef906370ff',660))
        self._taskList.append(('2bc6a19f-14a09a6136b-b0d06a47433585a6fab6f04c0fb0f91b','ea2b3e1-154580a4f39-a8c051a458585a91f6bbafef906370ff',1720))
        self._taskList.append(('2bc6a19f-14a09a6136b-b0d06a47433585a6fab6f04c0fb0f91b','ea2b3e1-154580a4f39-a8c051a458585a91f6bbafef906370ff',20860))
        self._taskList.append(('245d0782-1456e4e19df-a8c051a458585a91f6bbafef906370ff','15fdfa64-154086f6e25-a8c051a458585a91f6bbafef906370ff',3820))
        self._taskList.append(('15340051-144f1ec994e-a8c051a458585a91f6bbafef906370ff','36e08cbd-1533b5806b8-b0d06a47433585a6fab6f04c0fb0f91b',600))
        self._taskList.append(('757c619e-14588ce3aa4-a8c051a458585a91f6bbafef906370ff','169d0dac-1552ebd2a7c-a8c051a458585a91f6bbafef906370ff',1620))
        '''
        
    def _generateTask(self, task, checkTaskList=True):
        if super(PartCrawlerManager, self)._generateTask(task,checkTaskList) is False:
            return False
        
        item = self._taskList.pop()
        if item is not None:
            task[CrawlerConstants.PARA_CLASS] = PartCrawler
            task[PartCrawler.PARA_ENTERPRISE_ID] = item[0]
            task[PartCrawler.PARA_LICENCE_ID] = item[1]
            task[PartCrawler.PARA_ENTERPRISE_ONLY] = self.__enterpriseOnly
            if len(item)==3:
                task[PartCrawler.PARA_SPECIFIC_STARTNO] = item[2]
            return True
        else:
            return False
            
class PartCrawler(CrawlerBase):
    PARA_ENTERPRISE_ID = 'enterpriseid'
    PARA_LICENCE_ID = 'licenceid'
    PARA_SPECIFIC_STARTNO = 'specificStartNo'
    PARA_ENTERPRISE_ONLY = 'enterpriseOnly'
    #sEcho,start, length, continent, country, org, time
    def __init__(self, controller, dbProxy, request):
        super(PartCrawler, self).__init__(controller, dbProxy, request)
        self.logger = Logging.getLogger(LOGGER_NAME_CRAWL)
        self.__enterpriseId = request[PartCrawler.PARA_ENTERPRISE_ID]
        self.__licenceId = request[PartCrawler.PARA_LICENCE_ID]
        self.__startNo = None
        self.__enterpriseOnly = request[PartCrawler.PARA_ENTERPRISE_ONLY]
        if PartCrawler.PARA_SPECIFIC_STARTNO in request:
            self.__startNo = request[PartCrawler.PARA_SPECIFIC_STARTNO]
        
    def run(self):
        super(PartCrawler, self).run()
        if self.__enterpriseOnly:
            self.__parseEnterprise()
        else:
            if self.__startNo is None:
                self.__parseEnterprise()
                self.__parsePart()
            else:
                self.__parseSpecificPage()
        self._reportDone(CrawlerConstants.VAL_STATUS_FINISH )
    
    def __parseEnterprise(self):
        url = ENTERPRISE_URL % self.__enterpriseId
        self.logger.debug('Crawl %s', url)
        content = self._fetchContent(url)
        soup = BeautifulSoup(content)
        forms = soup.findAll('form')
        if len(forms)!=1:
            self.logger.warn('Detail data for %s is not expected. Form length %d not expected', self.__enterpriseId, len(forms))
            return
        divs = forms[0].findAll('div',{'class','am-g'})
        if len(divs)!=6:
            self.logger.warn('Detail data for %s is not expected. Div length %d not expected', self.__enterpriseId, len(divs))
            return
        
        address = divs[2].findAll('div')[1].text.strip().replace('\t','').replace('\n','').replace('\r','')
        ahrefs = divs[4].findAll('div')[1].findAll('a')
        scan_copy_link = None
        if len(ahrefs)>0:
            scan_copy_link = ahrefs[0]['href']
        limited_rating = str(divs[5].findAll('div')[1])
        limited_rating = MySQLdb.escape_string(limited_rating)
        try:
            #sql = 'UPDATE ENTERPRISE set limited_rating="'+limited_rating+'"'
            sql = 'UPDATE ENTERPRISE set '
            try:
                sql += 'address="'+MySQLdb.escape_string(address)+'"'
            except Exception, e:
                sql += 'address="'+address+'"'
            if scan_copy_link is not None:
                sql+= ', scan_copy_link="'+MySQLdb.escape_string(scan_copy_link)+'"'
            sql+='  where enterprise_id="'+self.__enterpriseId+'"'
            self.dbProxy.execute(sql)
            self.dbProxy.commit()
        except Exception, e:
            traceInfo = traceback.format_exc()
            self.logger.error('Fail to update ENTERPRISE info for %s: %s', self.__enterpriseId, traceInfo)
            
    def __parseSpecificPage(self):
        page = 1
        timePara = int(time.time()*1000)
        url = PART_URL % (page, self.__startNo, self.__licenceId, timePara)
        self.logger.debug('Crawl specific page for url:%s', url)
        try:
            content = self._fetchContent(url)
            jo = json.loads(content)
            self.__writeDb(jo["aaData"])
        except Exception, e:
            traceInfo = traceback.format_exc()
            self.logger.error('Fail to parse part info for %s: %s', url, traceInfo)
        
    def __parsePart(self):
        page = 1
        timePara = int(time.time()*1000)
        totalItems = 0
        while True:
            startNum = (page-1) * PAGE_SIZE
            url = PART_URL % (page, startNum, self.__licenceId, timePara)
            self.logger.debug('Crawl url:%s', url)
            try:
                content = self._fetchContent(url)
                jo = json.loads(content)
                if totalItems == 0:
                    totalItems = int(jo["iTotalRecords"])
                self.__writeDb(jo["aaData"])
            except Exception, e:
                traceInfo = traceback.format_exc()
                self.logger.error('Fail to parse part info for %s: %s', url, traceInfo)
            if page*PAGE_SIZE >= totalItems:
                break  
            timePara += 1
            page+=1
            self.controller.randomSleep()
    
    def __writeDb(self, items):
        #pass
        
        sql = 'INSERT INTO PART (enterprise_id, licence_id, aircraft_part_id, ata_chapter_section,'+\
                'category_no,parts_number,parts_name,manufacturers,inspection,repair,'+\
                'modification,overhaul,file_to_accord,main_devices,remark) values '
        values = list()        
        for item in items:
            self.totalNum+=1
            valuestr = '("%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s","%s")'

            ataChaptersection = ''            
            if 'ataChaptersection' in item:
                if item['ataChaptersection'] is not None and item['ataChaptersection'].lower()!='none':
                    ataChaptersection = item['ataChaptersection']
            
            categoryNo = ''
            if 'categoryNo' in item:
                if item['categoryNo'] is not None and item['categoryNo'].lower()!='none':
                    categoryNo = item['categoryNo']

            partsNumber = ''
            if 'partsNumber' in item:
                if item['partsNumber'] is not None and item['partsNumber'].lower()!='none':
                    partsNumber = item['partsNumber']
            
            partsName = ''
            if 'partsName' in item:
                if item['partsName'] is not None and item['partsName'].lower()!='none':
                    try:
                        partsName = MySQLdb.escape_string(item['partsName'])
                    except Exception, e:
                        partsName = item['partsName']
                
            manufacturers = ''
            if 'manufacturers' in item:
                if item['manufacturers'] is not None and item['manufacturers'].lower()!='none':
                    try:
                        manufacturers = MySQLdb.escape_string(item['manufacturers'])
                    except Exception,e:
                        manufacturers = item['manufacturers']
            
            inspection = '0'
            if 'inspection' in item:
                if item['inspection']=="1":
                    inspection = "1"

            repair = '0'
            if 'repair' in item:
                if item['repair']=="1":
                    repair = "1"

            modification = '0'
            if 'modification' in item:
                if item['modification']=="1":
                    modification = "1"

            overhaul = '0'
            if 'overhaul' in item:
                if item['overhaul']=="1":
                    overhaul = "1"

            fileToAccord = ''
            if 'fileToAccord' in item:
                if item['fileToAccord'] is not None and item['fileToAccord'].lower()!='none':
                    try:
                        fileToAccord = MySQLdb.escape_string(item['fileToAccord'])
                    except Exception,e:
                        fileToAccord = item['fileToAccord']

            mainDevices = ''
            if 'mainDevices' in item:
                if item['mainDevices'] is not None and item['mainDevices'].lower()!='none':
                    try:                        
                        mainDevices = MySQLdb.escape_string(item['mainDevices'])
                    except Exception, e:
                        mainDevices = item['mainDevices']

            remark = ''
            if 'remark' in item:
                if item['remark'] is not None and item['remark'].lower()!='none':
                    try:
                        remark = MySQLdb.escape_string(item['remark'])
                    except Exception, e:
                        remark = item['remark']
                    
            valuestr = valuestr % (
                                   self.__enterpriseId,
                                   self.__licenceId,
                                   item['aircraftPartId'],
                                   ataChaptersection,
                                   categoryNo,
                                   partsNumber,
                                   partsName,
                                   manufacturers,
                                   inspection,
                                   repair,
                                   modification,
                                   overhaul,
                                   fileToAccord,
                                   mainDevices,
                                   remark
                                   )
            values.append(valuestr)
            
        if len(values)>0:
            self.dbProxy.execute(sql + ','.join(values))
            self.dbProxy.commit()
        
   
if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CRAWL, pid)
    Logging.initLogger('conf/crawler.logging.cfg')
    ins = PartCrawlerManager('conf/'+LOGGER_NAME_CRAWL+'.cfg')
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CRAWL, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                            