'''
Created on 11 Apr 2017

@author: eyaomai
'''
import requests
import time
import random
import traceback
from com.naswork.sentiment.common.utils import Logging


class SessionCrawler(object):
    '''
    classdocs
    '''

    def __init__(self, session=None, sleepRange=[1,2], logger=None):
        '''
        Constructor
        '''
        if session is None:
            self.session = requests.session()
        else:
            self.session = session
        self.logger = logger
        if self.logger is None:
            self.logger = Logging.LOGGER
        self.sleepRange = sleepRange
        self.lastCrawlTime = 0
    
    def get(self, url, textRspOnly=True, **kwargs):
        result = self.session.get(url, **kwargs)
        self.lastCrawlTime = time.time()
        if textRspOnly:
            return result.text
        else:            
            return result
    
    def post(self, url, data=None, json=None, textRspOnly=True, **kwargs):
        result = self.session.post(url, data, json, **kwargs)
        self.lastCrawlTime = time.time()
        if textRspOnly:
            return result.text
        else:            
            return result

    def randomSleep(self):
        if time.time() - self.lastCrawlTime < self.sleepRange[0]:
            sleepTime =self.sleepRange[0] + (self.sleepRange[1]-self.sleepRange[0])*random.random()            
            time.sleep(sleepTime)

    def download(self, url, encoding=False, data=None, cookies='', timeout=10, retry=3, addr=False, isJson=False):
        try:
            #s = requests.session()
            user_agent = [
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.2595.400 QQBrowser/9.6.10872.400",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14",
                "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0",
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;  Trident/5.0)",
                ]
            randdom_header = random.choice(user_agent)
            headers = {
                'Cookie': cookies,
                'User-Agent': randdom_header,
            }
            self.randomSleep()
            r = self.session.get(url, params=data, timeout=timeout, headers=headers)
            if encoding:
                r.encoding = encoding
            if addr:
                if isJson:
                    return {'url':r.url, 'html':r.json()}
                else:
                    return {'url':r.url, 'html':r.text}
            else:
                if isJson:
                    return r.json()
                else:
                    return r.text
            '''
            if addr:
                    return {'url':r.url, 'html':r.text}
            else:
                    #return r.text
                    return r.json()
            '''
        except Exception, e:
            if retry < 1:
                self.logger.error('Fail to search baidu:'+ traceback.format_exc())
                return None
            else:
                return self.download(url=url, encoding=encoding, data=data,timeout=timeout, retry=retry-1, addr=addr, cookies=cookies)
    