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
VENDOR_PREFIX = "V"
DUMMY_CODE = 'DUMMY CODE'
POS_VENDOR_CODE = 6
POS_INFO_START = 15
LOGGER_NAME = 'avp'
class AirBusVendorParser(AirBusParser):
    '''
    CREATE TABLE VENDOR(
        VENDOR_CODE CHAR(6) NOT NULL,
        CAGE_CODE CHAR(5) NOT NULL,
        CAGE_NAME VARCHAR(50) NOT NULL,
        ADDRESS VARCHAR(200) NOT NULL,
        DUMMY tinyint NOT NULL default 0,
        PRIMARY KEY (VENDOR_CODE)
    )ENGINE=MyISAM DEFAULT CHARSET=utf8;
    '''

    
    def __init__(self, conf, logger):
        '''
        Constructor
        '''
        super(AirBusVendorParser, self).__init__(conf, logger)
    
    def parse(self, fileName):
        f = open(fileName)
        fl = list(f)
        f.close()

        currentVendor = None
        vendorList = list()
        for line in fl:
            line = line.strip('\n')
            if line.startswith(VENDOR_PREFIX):
                if currentVendor is not None:
                    vendorList.append('("%s", "%s", "%s", "%s", %d)' % (
                                                                  currentVendor.vendorCode,
                                                                  currentVendor.vendorCode[1:],
                                                                  currentVendor.vendorName,
                                                                  currentVendor.address,
                                                                  currentVendor.isDummy))
                vendorCode = line[:POS_VENDOR_CODE]
                vendorName = line[POS_VENDOR_CODE:].strip()
                currentVendor = Vendor(vendorCode, vendorName)
            else:
                if line.find(DUMMY_CODE)>=0:
                    currentVendor.isDummy = 1
                else:
                    if line[:POS_INFO_START].strip()=='' and line[POS_INFO_START]!='':
                        currentVendor.address+=line.strip().strip('.')
        
        if currentVendor is not None:
            vendorList.append('("%s", "%s", "%s", "%s", %d)' % (
                                                          currentVendor.vendorCode,
                                                          currentVendor.vendorCode[1:],
                                                          currentVendor.vendorName,
                                                          currentVendor.address,
                                                          currentVendor.isDummy))
        sql = 'insert into vendor (vendor_code, cage_code, cage_name, address, dummy) values ' + ','.join(vendorList)
        
        self.dbProxy.execute(sql)
        self.dbProxy.commit()
        
class Vendor(object):
    def __init__(self, vendorCode, vendorName):
        self.vendorCode = vendorCode
        self.vendorName = vendorName
        self.address = ''
        self.isDummy = 0

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
    parser = AirBusVendorParser(conf, Logging.getLogger(LOGGER_NAME))
    fileName = 'F:\\tmp\\vendor.txt'
    parser.parse(fileName)