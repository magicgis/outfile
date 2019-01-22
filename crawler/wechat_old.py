# -*- coding:utf-8 -*-
'''
Created on 15 Oct 2017

@author: eyaomai
'''
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
from com.naswork.sentiment.common.constant import Constants
from com.naswork.sentiment.common.utils import Configuration, MySqlProxy
import re, json, os
from bs4 import BeautifulSoup
import datetime, time, requests, traceback
import urllib
from lxml import etree
import urllib2

PROXY_IP_URL_AGENT = 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.22 Safari/537.36 SE 2.X MetaSr 1.0'
PROXY_IP_URL_HEADER = {'User-Agent': PROXY_IP_URL_AGENT}
PROXY_IP_URL = 'http://www.xicidaili.com/nn/'
AGENT = 'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0'
COOKIE = 'cuid=5308142514; tvfe_boss_uuid=a42cc0448522b64f; pgv_pvi=4518479872; RK=+COOf7V7M4; ptui_loginuin=965922280@qq.com; pac_uid=1_965922280; pgv_pvid=1997372141; o_cookie=965922280; ptisp=cm; ptcz=3adaf538c33388658694049a816f8bbabfd3f91f9f029718ada815bc5fd18865; uin=o0965922280; skey=@WMTRDuU3R; pt2gguin=o0965922280'
COOKIE2 = 'cuid=5308142514; tvfe_boss_uuid=a42cc0448522b64f; pgv_pvi=4518479872; RK=+COOf7V7M4; ptui_loginuin=965922280@qq.com; pac_uid=1_965922280; pgv_pvid=1997372141; o_cookie=965922280; ptisp=cm; ptcz=3adaf538c33388658694049a816f8bbabfd3f91f9f029718ada815bc5fd18865; uin=o0965922280; skey=@WMTRDuU3R; pt2gguin=o0965922280'
COOKIE3 = 'cuid=5308142514; tvfe_boss_uuid=a42cc0448522b64f; pgv_pvi=4518479872; RK=+COOf7V7M4; ptui_loginuin=965922280@qq.com; pac_uid=1_965922280; pgv_pvid=1997372141; o_cookie=965922280; ptisp=cm; ptcz=3adaf538c33388658694049a816f8bbabfd3f91f9f029718ada815bc5fd18865; uin=o0965922280; skey=@WMTRDuU3R; pt2gguin=o0965922280'
COOKIE4 = 'cuid=5308142514; tvfe_boss_uuid=a42cc0448522b64f; pgv_pvi=4518479872; RK=+COOf7V7M4; ptui_loginuin=965922280@qq.com; pac_uid=1_965922280; pgv_pvid=1997372141; o_cookie=965922280; ptisp=cm; ptcz=3adaf538c33388658694049a816f8bbabfd3f91f9f029718ada815bc5fd18865; uin=o0965922280; skey=@WMTRDuU3R; pt2gguin=o0965922280'
SOGOU_URL = 'http://weixin.sogou.com/weixin'
SOUGO_WECHARTPUBLIC_URL_INIT = 'http://weixin.sogou.com/weixin?type=1&s_from=input&query=%s&ie=utf8&_sug_=y&_sug_type_=&w=01019900&sut=1037118&sst0=1512481190038'
HEADERS_SOGOU = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
            'Connection': 'keep-alive',
            'Host': 'weixin.sogou.com',
            'Referer': 'http://weixin.sogou.com/weixin?type=1&s_from=input&query=%E4%B8%AD%E5%B1%B1%E5%A4%A7%E5%AD%A6%E5%9B%A2%E5%A7%94&ie=utf8&_sug_=n&_sug_type_=',
            'Upgrade-Insecure-Requests': '1',
            'Cookie':COOKIE,
            'User-Agent': AGENT
        }
SOUGO_WEIXIN_URL_INIT = 'http://weixin.sogou.com/weixin?type=2&query=%s&ie=utf8&s_from=input&_sug_=n&_sug_type_=1&w=01015002&oq=&ri=9&sourceid=sugg&sut=0&sst0=%d&lkt=0#2C0#2C0&p=40040108'
# 爬取一天之内的微信内容
SOUGO_WEIXIN_URL_SUB_PAGE = 'http://weixin.sogou.com/weixin?type=2&ie=utf8&query=%s&tsn=1&ft=&et=&interation=&wxid=&usip='

HEADERS_ARTICLE = {
            "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding":"gzip, deflate, br",
            "Accept-Language":"zh-CN,zh;q=0.9",
            "Cache-Control":"max-age=0",
            "Connection":"keep-alive",
            "Host":"mp.weixin.qq.com",
            "Upgrade-Insecure-Requests":'1',
            "Cookie":COOKIE2,
            "User-Agent":AGENT
        }
HEADERS_PUBLIC = {
            'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding':'gzip, deflate, br',
            'Accept-Language':'zh-CN,zh;q=0.9',
            'Cache-Control':'max-age=0',
            'Connection':'keep-alive',
            'Host':'mp.weixin.qq.com',
            'Cookie': COOKIE
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

    
    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        @param keywordList: 关键字数组
        @param endTime: 搜索时间范围结束
        '''
        startTime = endTime - datetime.timedelta(days=self.channel.search_ranges)
        startTimeStr = startTime.strftime('%Y-%m-%d')
        endTimeStr = endTime.strftime('%Y-%m-%d')
        self.logger.debug('startTime:%s', startTimeStr)
        self.logger.debug('endTime:%s', endTimeStr)
        urlList = list()
        publicList = self.getPublic()
        articleList = self.searchPublic(publicList, endTime)
        for keyword in keywordList:
            # 忽略第一次，第一次不带时间范围
            pageUrl = (SOUGO_WEIXIN_URL_INIT % (urllib.quote(keyword.encode('utf-8')),int(time.time()*1000))).replace('#','%')
            self.logger.debug('pageUrl:%s', pageUrl)
            self.session.randomSleep()
            response = self.session.get(pageUrl, textRspOnly=False, headers=HEADERS_SOGOU)
            lastPageUrl = pageUrl
            pageUrl = SOUGO_WEIXIN_URL_SUB_PAGE % (urllib.quote(keyword.encode('utf-8')), startTimeStr, endTimeStr)
            pageUrl = SOUGO_WEIXIN_URL_SUB_PAGE % (urllib.quote(keyword.encode('utf-8')))
            self.logger.debug('pageUrl:%s', pageUrl)
            while True:
                # 用上一次url作为这次的referer
                headers = HEADERS_SOGOU.copy()
                headers['Referer'] = lastPageUrl
                self.session.randomSleep()
                response = self.session.get(pageUrl, textRspOnly=False, headers=headers)
                soup = BeautifulSoup(response.text, 'lxml')
                main = soup.find('ul',{'class': "news-list"})
                if main is None:
                    self.logger.error('Fail to parse:%s', response.text)
                    return []
                li_list = main.findAll('li')
                #li_list有可能为空，但还可以翻页
                for li in li_list:
                    a_list = li.findAll('a')
                    for a in a_list:
                        if a['uigs'].startswith('article_title'):
                            #self.logger.debug('Article title:%s',a.text)
                            urlList.append((a['href'],pageUrl, a.text))
                            break
                pageBarList = soup.findAll('div',{'id':'pagebar_container'})
                if len(pageBarList) == 0:
                    #没有翻页，直接退出
                    break
                pageBar = pageBarList[0]
                aList = pageBar.findAll('a')
                foundNextPage = False
                for a in aList:
                    if a['uigs']=='page_next':
                        foundNextPage = True
                        lastPageUrl = pageUrl
                        pageUrl = SOGOU_URL+a['href']
                        self.logger.debug('Found next page:%s', a.text)
                        break
                if foundNextPage is False:
                    break
        for item in urlList:
            article = self.crawlArticle(item[0], referer=item[1], title=item[2], flag=0)
            if article is not None:
                if article not in articleList:
                    #同一文章可能会在搜索结果出现多次，在baidu的结果url是不重复，但是实际可能指向同一文章，需要去重
                    articleList.append(article)
        return articleList

    def searchPublic(self, keywordList, endTime):
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
            li_list = main.findAll('li')

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

    
    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        pass
    
    def crawlArticle(self, url, **kwargs):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        #TBD, 转发情况目前先不考虑
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
            if(kwargs['flag'] == 0):
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
            try:
                publish_datetime = main.find('em',{'id':"post-date"}).text.strip()+' 00:00:00'
                publish_datetime = datetime.datetime.strptime(publish_datetime, '%Y-%m-%d %H:%M:%S')
            except:
                self.logger.warn('Fail to parse publish_datetime, use current time as time')
                publish_datetime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            article = Article(mid, self.channel.channel_id, title, content,publish_datetime=publish_datetime,
                            url=url, author_id=author_id, author_name=author_name, meta_info='{refer="%s"}'%referer)
            self.logger.debug('Successfully parse article:%s', title)


            return article
        except:
            self.logger.error('Fail to get article for %s: %s due to %s', url, searchTitle, traceback.format_exc())
            return None
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

    def  getProxyPageList(self, url):
        url_list = []
        for i in range(1, 100):
            url_new = url + str(i)
            url_list.append(url_new)

        return url_list

    def getProxyIpList(self, url):
        response = self.session.get(url, textRspOnly=False, headers=PROXY_IP_URL_HEADER, timeout=2)
        host_list = etree.HTML(response.text).xpath('//table[contains(@id,"ip_list")]/tr/td[2]/text()')
        port_list = etree.HTML(response.text).xpath('//table[contains(@id,"ip_list")]/tr/td[3]/text()')
        ip_list = list()
        for i in range(0, len(host_list)):
            ip = host_list[i] + r':' + port_list[i]
            ip_list.append(ip)
        return ip_list

    def verifyIp(self, ip):
        proxy = {'http':'http://' + ip}
        proxy_handler = urllib2.ProxyHandler(proxy)
        opener = urllib2.build_opener(proxy_handler)
        urllib2.install_opener(opener)

        test_url = 'http://www.baidu.com'
        req = urllib2.Request(url=test_url, headers=PROXY_IP_URL_HEADER)
        try:
            res = urllib2.urlopen(req)
            content = res.read()
            if content:
                self.logger.debug(r'https://'+ ip + ' is OK')
            else:
                self.logger.debug(r'https://'+ ip + ' is BAD')
                ip = ""
            return ip
        except urllib2.URLError as e:
            self.logger.debug(r'https://'+ ip + ' ' + str(e.reason))
            return ""
        except:
            self.logger.debug(r'https://'+ ip + ' Other Error')
            return ""

    def getVaildIp(self):
        page_url_list = self.getProxyPageList(PROXY_IP_URL)
        ip_list = list()
        for page_url in page_url_list:
            page_ip_list = self.getProxyIpList(page_url)
            for ip in page_ip_list:
                ip = self.verifyIp(ip)
                if ip != "":
                    ip_list.append(ip)
        return ip_list

    def isCrawlerPublic(self, url):
        data_script_list = list()
        page_url_list = self.getProxyPageList(PROXY_IP_URL)
        flag = 0
        for page_url in page_url_list:
            page_ip_list = self.getProxyIpList(page_url)
            flag = 0
            for ip in page_ip_list:
                flag = 0
                ip = self.verifyIp(ip)
                if ip != "":
                    # self.session_public.randomSleep()
                    proxy = {
                        'http': r'http://' + ip
                    }
                    try:
                        response = self.session_public.get(url, textRspOnly=True, headers=HEADERS_ARTICLE,
                                                           proxies=proxy)
                        soup = BeautifulSoup(response.text, 'lxml')
                        self.logger.debug(response.text)
                        script_list = soup.findAll('script')
                        if len(script_list) != 0:
                            flag = 0
                            for li in script_list:
                                li_str = str(li.text)
                                sub_str1 = "msgList = "
                                if li_str.find(sub_str1) != -1:
                                    data_script_list.append(li)
                                    flag = 1
                                    break
                            if flag == 1:
                                break
                    except:
                        self.logger.debug('The ip can not be used to crawler public')
            if flag == 1:
                break
        if (flag == 0):
            self.logger.debug('The ip can not be used to crawler public')

        return data_script_list