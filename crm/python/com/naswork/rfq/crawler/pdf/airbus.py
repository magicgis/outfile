'''
Created on 19 Nov 2016

@author: eyaomai
'''
from com.naswork.rfq.common.utils import MySqlProxy
from com.naswork.rfq.common.crawlercommon import CrawlerConstants
class AirBusParser(object):
    '''
    classdocs
    '''


    def __init__(self, conf, logger):
        '''
        Constructor
        '''
        self.logger = logger
        self.__config = conf
        self._dbHost = self.__config[CrawlerConstants.CONFIG_FILE_DBHOST]
        self._dbPort = self.__config[CrawlerConstants.CONFIG_FILE_DBPORT]
        self._dbUser = self.__config[CrawlerConstants.CONFIG_FILE_DBUSER]
        self._dbPasswd = self.__config[CrawlerConstants.CONFIG_FILE_DBPASS]
        self._dbName = self.__config[CrawlerConstants.CONFIG_FILE_DBNAME]
        self.dbProxy = MySqlProxy(self._dbHost, self._dbPort, self._dbUser, self._dbPasswd, self._dbName, logger=self.logger)
    
    def clean(self):
        self.dbProxy.close()
    