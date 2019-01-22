# -*- coding:utf-8 -*-
'''
Created on 15 Oct 2017

@author: eyaomai
'''
import re, json, os
from bs4 import BeautifulSoup
import datetime, time, traceback
import urllib, urllib2
import random


from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
from com.naswork.sentiment.common.constant import Constants
from com.naswork.sentiment.common.utils import Configuration, MySqlProxy

from CrawlerMonitor import SendEmail,InsertDB
from get_ip import getIp


PROXY_IP_URL_AGENT = 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.22 Safari/537.36 SE 2.X MetaSr 1.0'
PROXY_IP_URL_HEADER = {'User-Agent': PROXY_IP_URL_AGENT}
PROXY_IP_URL = 'http://www.xicidaili.com/nn/'
AGENTS = [
    'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0',
    'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36',
    'Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50',
    'Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)',
    'Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1'
]
AGENT = random.choice(AGENTS)
COOKIE = 'cuid=5308142514; tvfe_boss_uuid=a42cc0448522b64f; pgv_pvi=4518479872; RK=+COOf7V7M4; ptui_loginuin=965922280@qq.com; pac_uid=1_965922280; pgv_pvid=1997372141; o_cookie=965922280; ptisp=cm; ptcz=3adaf538c33388658694049a816f8bbabfd3f91f9f029718ada815bc5fd18865; uin=o0965922280; skey=@WMTRDuU3R; pt2gguin=o0965922280'
COOKIE2 = 'cuid=5308142514; tvfe_boss_uuid=a42cc0448522b64f; pgv_pvi=4518479872; RK=+COOf7V7M4; ptui_loginuin=965922280@qq.com; pac_uid=1_965922280; pgv_pvid=1997372141; o_cookie=965922280; ptisp=cm; ptcz=3adaf538c33388658694049a816f8bbabfd3f91f9f029718ada815bc5fd18865; uin=o0965922280; skey=@WMTRDuU3R; pt2gguin=o0965922280'
COOKIE3 = 'cuid=5308142514; tvfe_boss_uuid=a42cc0448522b64f; pgv_pvi=4518479872; RK=+COOf7V7M4; ptui_loginuin=965922280@qq.com; pac_uid=1_965922280; pgv_pvid=1997372141; o_cookie=965922280; ptisp=cm; ptcz=3adaf538c33388658694049a816f8bbabfd3f91f9f029718ada815bc5fd18865; uin=o0965922280; skey=@WMTRDuU3R; pt2gguin=o0965922280'
COOKIE4 = 'cuid=5308142514; tvfe_boss_uuid=a42cc0448522b64f; pgv_pvi=4518479872; RK=+COOf7V7M4; ptui_loginuin=965922280@qq.com; pac_uid=1_965922280; pgv_pvid=1997372141; o_cookie=965922280; ptisp=cm; ptcz=3adaf538c33388658694049a816f8bbabfd3f91f9f029718ada815bc5fd18865; uin=o0965922280; skey=@WMTRDuU3R; pt2gguin=o0965922280'

SOGOU_URL = 'http://weixin.sogou.com/weixin'
# SOUGO_WECHARTPUBLIC_URL_INIT = 'http://weixin.sogou.com/weixin?type=1&s_from=input&query=%s&ie=utf8&_sug_=y&_sug_type_=&w=01019900&sut=1037118&sst0=1512481190038'
SOUGO_WECHARTPUBLIC_URL_INIT = 'http://weixin.sogou.com/weixin?type=1&s_from=input&query=%s&ie=utf8&_sug_=y&_sug_type_='
HEADERS_SOGOU = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Host': 'weixin.sogou.com',
            'Referer': 'http://weixin.sogou.com/weixin?type=1&s_from=input&query=%E4%B8%AD%E5%B1%B1%E5%A4%A7%E5%AD%A6%E5%9B%A2%E5%A7%94&ie=utf8&_sug_=n&_sug_type_=',
            'Upgrade-Insecure-Requests': '1',
            'Cookie': COOKIE,
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36'
        }

SOUGO_WEIXIN_URL_INIT = 'http://weixin.sogou.com/weixin?type=2&query=%s&ie=utf8&s_from=input&_sug_=n&_sug_type_=1&w=01015002&oq=&ri=9&sourceid=sugg&sut=0&sst0=%d&lkt=0#2C0#2C0&p=40040108'
# 爬取一天之内的微信内容
SOUGO_WEIXIN_URL_SUB_PAGE = 'http://weixin.sogou.com/weixin?type=2&ie=utf8&query=%s&tsn=5&ft=%s&et=%s&interation=&wxid=&usip='

HEADERS_ARTICLE = {
            "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding":"gzip, deflate, br",
            "Accept-Language":"zh-CN,zh;q=0.9",
            "Cache-Control":"max-age=0",
            "Connection":"keep-alive",
            "Host":"mp.weixin.qq.com",
            "Upgrade-Insecure-Requests":'1',
            "Cookie":COOKIE,
            "User-Agent":AGENT
        }
HEADERS_PUBLIC = {
            'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding':'gzip, deflate, br',
            'Accept-Language':'zh-CN,zh;q=0.9',
            'Cache-Control':'max-age=0',
            'Connection':'keep-alive',
            'Host':'mp.weixin.qq.com',
            'Cookie': COOKIE2
        }
HEADER_PUBLIC_ARTICLE = {
            "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding":"gzip, deflate, br",
            "Accept-Language":"zh-CN,zh;q=0.9",
            "Cache-Control":"max-age=0",
            'Cookie': COOKIE,
            "Connection":"keep-alive",
            "Host":"mp.weixin.qq.com",
            "Upgrade-Insecure-Requests":'1',
            "User-Agent":AGENT
}

CONF_FILE_NAME = 'sa.cfg'
CONF_FILE_DBCONF = 'dbConf'
CONF_FILE_DBNAME = 'dbName'
CONF_FILE_DBUSER = 'dbUser'
CONF_FILE_DBPASS = 'dbPasswd'
CONF_FILE_DBHOST = 'dbHost'
CONF_FILE_ITRADE_SERVER_IP = 'ip'
CONF_FILE_ITRADE_SERVER_PORT = 'port'


class WechatCrawler(object):
    '''
    classdocs
    '''

    def __init__(self, channel, logger=None):
        '''
        Constructor
        '''
        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger

        self.session = SessionCrawler(logger=self.logger)
        self.session_public = SessionCrawler(logger=self.logger)
        self.session_public_article = SessionCrawler(logger=self.logger)
        self.channel = channel
        self.entityId = 'SYSU'

        self.ip_list = None
        self.proxies=None
        self.monitor_title = '微信爬虫监控'
        self.email = SendEmail()
        self.db = InsertDB()

    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，爬取一天内的文章
        @param keywordList: 关键字数组
        @:param endTime: 搜索结束时间
        '''
        run_msg = '微信爬虫开始运行'
        self.db.Insert(self.channel.channel_id,self.entityId,run_msg)
        startTime = endTime - datetime.timedelta(days=self.channel.search_ranges)
        startTimeStr = startTime.strftime('%Y-%m-%d')
        endTimeStr = endTime.strftime('%Y-%m-%d')

        # startTime = endTime - datetime.timedelta(days=1)
        # startTimeStr = startTime.strftime('%Y-%m-%d')
        # endTimeStr=startTime.strftime('%Y-%m-%d')

        self.logger.debug('startTime:%s', startTimeStr)
        self.logger.debug('endTime:%s', endTimeStr)

        # 随机选取一个代理
        # proxy_crawler = ProxyCrawler()
        # proxies = proxy_crawler.get_random_proxy()

        # publicList = self.getPublic()
        # articleList = self.searchPublic(publicList)
        articleList = list()
        urlList = list()

        for keyword in keywordList:
            # 忽略第一次，第一次不带时间范围
            pageUrl = (SOUGO_WEIXIN_URL_INIT % (urllib.quote(keyword.encode('utf-8')),int(time.time()*1000))).replace('#','%')
            self.logger.debug('pageUrl:%s', pageUrl)
            self.session.randomSleep()
            lastPageUrl = pageUrl
            # 爬取微信一天时间的内容
            pageUrl = SOUGO_WEIXIN_URL_SUB_PAGE % (urllib.quote(keyword.encode('utf-8')), startTimeStr, endTimeStr)
            self.logger.debug('pageUrl:%s', pageUrl)
            # 得到ip队列
            self.ip_list = getIp()
            ip = self.ip_list.dequeue()
            self.proxies = {"http": "http://" + ip}

            while True:
                # proxies = {"http": "http://" + ip}
                headers = HEADERS_SOGOU.copy()
                headers['Referer'] = lastPageUrl
                try:
                    response = self.session.get(pageUrl, allow_redirects=False, headers=headers, proxies=self.proxies)
                    soup = BeautifulSoup(response, 'lxml')
                    main = soup.find('ul', {'class': "news-list"})
                    while True:
                        if main is None:
                            # self.logger.error('Fail to parse: ip被封，更新ip')
                            content = 'ip被封，更新ip'
                            # self.email.send(self.monitor_title, content)
                            self.db.Insert(self.channel.channel_id, self.entityId, content)
                            temp = self.ip_list.dequeue()
                            if self.ip_list.isempty():
                                self.ip_list = getIp()
                            self.proxies = {"http": "http://" + temp}
                            # while True:
                            #     try:
                            response = self.session.get(pageUrl, allow_redirects=False, headers=headers,
                                                        proxies=self.proxies)
                            soup = BeautifulSoup(response, 'lxml')
                            main = soup.find('ul', {'class': "news-list"})
                                #     break
                                # except:
                                #     ip_unuseful_content = '此ip是不合格的ip,更新ip'
                                #     # self.email.send(self.monitor_title, ip_unuseful_content)
                                #     self.db.Insert(self.channel.channel_id,self.entityId,ip_unuseful_content)
                                #     tmp = self.ip_list.dequeue()
                                #     if self.ip_list.isempty():
                                #         self.ip_list = getIp()
                                #     self.proxies = {"http": "http://" + tmp}
                        else:
                            break

                    li_list = main.findAll('li')
                    # li_list有可能为空，但还可以翻页
                    for li in li_list:
                        a_list = li.findAll('a')
                        try:
                            publish_datetime = li.select_one('.s-p').get('t')
                            publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(int(publish_datetime)))
                        except Exception as e:
                            self.logger.debug('Publish_datetime crawl failed, use now time')
                            publish_datetime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                        print publish_datetime

                        for a in a_list:
                            if a['uigs'].startswith('article_title'):
                                # self.logger.debug('Article title:%s',a.text)
                                urlList.append((a['href'], pageUrl, a.text, publish_datetime))
                                break
                    pageBarList = soup.findAll('div', {'id': 'pagebar_container'})
                    if len(pageBarList) == 0:
                        # 没有翻页，直接退出
                        break
                    pageBar = pageBarList[0]
                    aList = pageBar.findAll('a')
                    foundNextPage = False
                    for a in aList:
                        if a['uigs'] == 'page_next':
                            foundNextPage = True
                            lastPageUrl = pageUrl
                            pageUrl = SOGOU_URL + a['href']
                            self.logger.debug('Found next page:%s', a.text)
                            break
                    if foundNextPage is False:
                        break
                except:
                    ip_unuseful_content = '此ip是不合格的ip,更新ip'
                    # self.email.send(self.monitor_title,ip_unuseful_content)
                    self.db.Insert(self.channel.channel_id,self.entityId,ip_unuseful_content)
                    tmp = self.ip_list.dequeue()
                    if self.ip_list.isempty():
                        self.ip_list = getIp()
                    self.proxies = {"http": "http://" + tmp}
        for item in urlList:
            article = self.crawlArticle(item[0], referer=item[1], title=item[2], publish_datetime=item[3], flag=0)

            if article is not None:
                if article not in articleList:
                    # 同一文章可能会在搜索结果出现多次，在baidu的结果url是不重复，但是实际可能指向同一文章，需要去重
                    articleList.append(article)
        if articleList is None:
            monitor_content = '微信没有数据，或者微信爬虫挂了'
            self.email.send(self.monitor_title,monitor_content)
            self.db.Insert(self.channel.channel_id,self.entityId,monitor_content)
        end_msg = '微信爬虫结束'
        self.db.Insert(self.channel.channel_id,self.entityId,end_msg)
        return articleList

    def searchPublic(self, keywordList):
        '''
        根据关键字数组，开始时间和结束时间范围搜索公众号
        :param keywordList:
        :param endTime:
        :return:
        '''
        articleList = list()

        for keyword in keywordList:
            self.logger.debug(keyword)
            pageUrl = SOUGO_WECHARTPUBLIC_URL_INIT % (keyword[0])
            self.logger.info('pageUrl:%s', pageUrl)
            self.session.randomSleep()
            response = self.session.get(pageUrl, textRspOnly=False, headers=HEADERS_SOGOU)
            soup = BeautifulSoup(response.text, 'lxml')
            main = soup.find('ul', {'class': "news-list2"})

            if main is None:
                self.logger.error('Fail to parse:%s', response.text)
            try:
                li_list = main.findAll('li')
            except Exception, e:
                print e
                continue

            for li in li_list:
                a_title = li.find('p', {'class':"tit"})
                if a_title is not None:
                    title = str(a_title.text.strip())
                    if title == keyword[0]:
                        self.logger.debug(title)
                        a_href = a_title.find('a')['href']
                        sub_articleList = self.crawlWetchartpublic(a_href)
                        for article in sub_articleList:
                            articleList.append(article)
        return articleList

    def crawlWetchartpublic(self, url):
        '''
        按公众号爬取文章
        :param url:
        :return:
        '''
        self.logger.debug(url)
        self.session_public.randomSleep()
        response = self.session_public.get(url, textRspOnly=False, headers=HEADERS_ARTICLE)
        soup = BeautifulSoup(response.text, 'lxml')
        self.logger.debug(soup)
        script_list = soup.findAll('script')
        # if len(script_list) == 0:
        # script_list = self.isCrawlerPublic(url)

        articleList = list()
        for li in script_list:
            li_str = str(li.text)
            sub_str1 = "msgList = "
            sub_str2 = '}]};'
            if li_str.find(sub_str1) != -1:
                index1 = li_str.find(sub_str1)
                index2 = li_str.find(sub_str2)
                main = str(li.text)[index1 + len(sub_str1):index2 + 3]
                articleJson = json.loads(main)
                articlelistJson = articleJson['list']
                for item in articlelistJson:
                    mes_info = item['app_msg_ext_info']
                    url = 'https://mp.weixin.qq.com' + mes_info['content_url']
                    url = url.replace('amp;', '')
                    self.logger.debug('article_url:' + url)
                    article = self.crawlArticle(url, flag=1)
                    articleList.append(article)
                    multi_item_list = mes_info['multi_app_msg_item_list']
                    for multi_item in multi_item_list:
                        multi_url = 'https://mp.weixin.qq.com' + multi_item['content_url']
                        multi_url = multi_url.replace('amp;', '')
                        self.logger.debug('article_url:' + multi_url)
                        article = self.crawlArticle(multi_url, flag=1)
                        if article is not None:
                            articleList.append(article)
        return articleList

    def crawlArticle(self, url, **kwargs):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        # TBD, 转发情况目前先不考虑
        searchTitle = ''
        referer = None
        if (kwargs['flag'] == 0):
            if 'referer' not in kwargs:
                return None
            if 'title' in kwargs:
                searchTitle = kwargs['title']
            else:
                searchTitle = ''
        try:
            self.session_public_article.randomSleep()
            if kwargs['flag'] == 0:
                referer = kwargs['referer']
                headers = HEADERS_ARTICLE.copy()
                headers['Referer'] = referer
                response = self.session_public_article.get(url, textRspOnly=False, headers=HEADER_PUBLIC_ARTICLE)
            else:
                response = self.session_public_article.get(url, textRspOnly=False, headers=HEADER_PUBLIC_ARTICLE)
            mid = re.findall(r'var mid = .*"(\d+)";',response.text)[0] + '-' + re.findall(r'var idx = .*"(\d+)";',response.text)[0]
            soup = BeautifulSoup(response.text, 'lxml')
            main = soup.find('div',{'id':"img-content"})
            title = main.find('h2').text.strip()
            content = main.find('div',{'id':"js_content"}).text.strip()
            profile = main.find('div',{'class':"profile_inner"})
            author_id = profile.find('span').text.strip()
            author_name = profile.find('strong').text.strip()
            publish_datetime = kwargs['publish_datetime']

            article = Article(mid, self.channel.channel_id, title, content,publish_datetime=publish_datetime,
                            url=url, author_id=author_id, author_name=author_name, meta_info='{refer="%s"}'%referer)
            self.logger.debug('Successfully parse article:%s', title)
            return article
        except:
            self.logger.error('Fail to get article for %s: %s due to %s', url, searchTitle, traceback.format_exc())
            return None

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
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
        根据文章，爬取文章的评论，返回评论列表
        @return: (commentList, hasnext)二元组，commentList是指评论数组（每个元素是Comment实例），hasnext表示是否还有要爬取
        '''
        return (list(), False)

    def getPublic(self):
        self.conf = self.__readDbConf()
        publictablename = Constants.TABLE_SA_WETCHARTPUBLIC + Constants.TABLE_NAME_DELIMITER + self.entityId
        sql = '''
                SELECT public_name FROM %s
              ''' % (publictablename)
        dbProxy = MySqlProxy(self.conf[CONF_FILE_DBCONF][CONF_FILE_DBHOST],
                                3306,
                             self.conf[CONF_FILE_DBCONF][CONF_FILE_DBUSER],
                             self.conf[CONF_FILE_DBCONF][CONF_FILE_DBPASS],
                             self.conf[CONF_FILE_DBCONF][CONF_FILE_DBNAME])
        # dbProxy = MySqlProxy('localhost', 3306, 'root', 'zzm15331411', 'sentiment_re')
        # dbProxy = MySqlProxy('112.124.47.197', 3306, 'test', 'test', 'sa2')
        dbProxy.execute(sql)
        resultList = dbProxy.fetchall()
        # resultList = [(u'今日中大',),]
        return resultList

    def __readDbConf(self):
        fileName = os.path.join('conf', CONF_FILE_NAME)
        c = Configuration(fileName)
        return c.readConfig()
