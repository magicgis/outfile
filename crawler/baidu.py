# -*- coding:utf-8 -*-
'''
Created on 5 Oct 2017

@author: eyaomai
'''
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
from com.naswork.sentiment.common.utils import Logging
from bs4 import BeautifulSoup
URL ='https://www.baidu.com/s?ft=&tn=baiduadv'  #百度搜索
MAXPAGE = 30
class BaiduCrawler(object):
    '''
    classdocs
    '''

    def __init__(self, logger=None):
        '''
        Constructor
        '''
        self.session = SessionCrawler()
        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger
    
    def search(self, site, keywordList, startTimeIntSecond, endTimeIntSecond):
        keywordStr = ' | '.join(keywordList) 
        qstr = 'site:'+'('+site+') ('+keywordStr+')'  # 20180712.Jondar 改
        intime = 'stf=%d,%d|stftype=2' % (startTimeIntSecond, endTimeIntSecond)
        page = 1
        urlList = list()
        while page < MAXPAGE:
            #计算分页数
            pageParam = str(page-1)+'0'
            data = {
              'gpc': intime,
              # 'si':'('+site+')',
              'si': site,
              'bs': qstr,
              'wd': qstr,
              'oq': qstr,
              'pn': pageParam
            }            
            response = self.session.download(URL, encoding='utf-8', data=data, addr=True)
            soup = BeautifulSoup(response['html'], 'html.parser')
            #查找页面的url
            divContent = soup.find('div', attrs={'id': "content_left"})
            if divContent is None:
                break
            urlDivList = divContent.find_all('div', attrs={'class': "result c-container "})
            for urlDiv in urlDivList:
                try:
                    url = urlDiv.find('h3').find('a').attrs['href']
                    # tasks.put_nowait(url)
                    urlList.append(url)
                except:
                    continue
            #翻页控制
            pageHrefList = soup.find('div', attrs={'id': "page"}).find_all('a')
            #pageList = map(lambda x: int(str(x.text.strip())), pageHrefList)
            pageList = []
            for p in pageHrefList:
                pa = p.text.strip()
                pageList.append(pa)
            if str(page+1) in pageList:
                page += 1
            else:
                break
        return urlList