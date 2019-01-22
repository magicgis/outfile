#-*- coding:utf-8-*-
'''
Created on 25 Dec 2017
@author: ZhangZemian
'''

from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
from bs4 import BeautifulSoup

import json

SOUGO_WECHARTPUBLIC_URL_INIT = 'http://weixin.sogou.com/weixin?type=1&s_from=input&query=%s&ie=utf8&_sug_=y&_sug_type_=&w=01019900&sut=1037118&sst0=1512481190038'
AGENT = 'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36'
HEADERS_SOGOU = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
            'Connection': 'keep-alive',
            'Host': 'weixin.sogou.com',
            'Referer': 'http://weixin.sogou.com/weixin',
            'Upgrade-Insecure-Requests': '1',
            'User-Agent': AGENT
        }
HEADERS_SOGOU_PUBLIC = {
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.9",
"Cache-Control":"max-age=0",
"Connection":"keep-alive",
"Host":"mp.weixin.qq.com",
"Upgrade-Insecure-Requests":'1',
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
}

class WetchartpublicCrawler(object):
    '''
    calssdocs
    '''


    def __init__(self, channel, logger=None):
        '''
        构造函数
        :param channel:
        :param logger:
        '''
        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger

        self.session = SessionCrawler(logger=self.logger)
        self.channel = channel



    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        :param keywordList: 关键字数组
        :param endTime: 搜索时间范围结束
        :return:
        '''
        for keyword in keywordList:
            pageUrl = SOUGO_WECHARTPUBLIC_URL_INIT % (keyword)
            self.logger.debug('pageUrl:%s', pageUrl)
            response = self.session.get(pageUrl, textRspOnly=False, headers=HEADERS_SOGOU)
            soup = BeautifulSoup(response.text)
            main = soup.find('ul', {'class': "news-list2"})
            if main is None:
                self.logger.error('Fail to parse:%s', response.text)
            li_list = main.findAll('li')

            for li in li_list:
                a_title = li.find('p', {'class':"tit"})
                if a_title is not None:
                    title = str(a_title.text.strip())
                    if title == keyword:
                        a_href = a_title.find('a')['href']
                        self.logger.debug(a_href)
                        self.crawlWetchartpublic(a_href)


    def crawlWetchartpublic(self, url):
        response = self.session.get(url, textRspOnly=False, headers=HEADERS_SOGOU_PUBLIC)
        soup = BeautifulSoup(response.text)
        script_list = soup.findAll('script')
        for li in script_list:
            li_str = str(li.text)
            sub_str1 = "msgList = "
            sub_str2 = '}]};'
            if li_str.find(sub_str1) != -1:
                index1 = li_str.find(sub_str1)
                index2 = li_str.find(sub_str2)
                main = str(li.text)[index1 + len(sub_str1):index2 + 3]
                articleJson = json.loads(main)
                articlelist = articleJson['list']
                for item in articlelist:
                    mes_info = item['app_msg_ext_info']
                    url = 'https://mp.weixin.qq.com' + mes_info['content_url']
                    url = url.replace('amp;', '')
                    self.crawlArticle(url)
                    multi_item_list = mes_info['multi_app_msg_item_list']
                    for multi_item in multi_item_list:
                        multi_url = 'https://mp.weixin.qq.com' + multi_item['content_url']
                        multi_url = multi_url.replace('amp;', '')
                        self.crawlArticle(multi_url)

    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        :return:返回一个article实例
        '''
        self.session.randomSleep()
        response = self.session.get(url, textRspOnly=False)



    def crawlStatistics(self, article):
        '''
        爬去统计信息
        :param article:
        :return: 无需返回参数，统计信息写入article实例
        '''
        pass

    def refreshSearch(self):
        '''
        重置搜索
        '''
        pass

    def refreshCommentCrawler(self):
        '''
        重置评论爬取
        '''
        pass

    def crawlComment(self, article):
        '''
        根据文章，爬去文章的评论，返回评论列表
        :param article:
        :return:(commentList, hasnext)二元组，commentList是指评论数组（每个元素是Comment实例），hasnext表示是否还有要爬取
        '''
        pass