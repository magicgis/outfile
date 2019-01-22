# -*- coding:utf-8 -*-
'''
Created on 10 Oct 2017

@author: eyaomai
'''
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
from com.naswork.sentiment.common.constant import Constants
import re, json, urllib, base64, requests, traceback
from bs4 import BeautifulSoup
import datetime, time, traceback, binascii
import rsa
import cookielib, random
from lxml import etree
from get_ip import getIp

from CrawlerMonitor import SendEmail,InsertDB


AGENTS = [
    'Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0',
    'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36',
    'Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50',
    'Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)',
    'Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1'
]


class WeiboCrawler(object):
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

        self.channel = channel
        self.entityId = 'SYSU'
        self.user_name = None
        self.pass_word = None

        self.email = SendEmail()
        self.db = InsertDB()
        self.monitor_title = 'Weibo crawler monitor'
        self.HTTP = 'https:'

        self.session = SessionCrawler(sleepRange=[3, 8])

    def change_cookie(self, user_name=None):
        '''
        随机获取一个cookie
        '''
        # usename_list = [
        #             '18814095644','13432881156','yourhoneybee@163.com','15018377821','942364283@qq.com',
        #             '15767199023','13427287354','13532011721','13640792755','13794342903',
        #             '17825769929','18211493432','17827278983','13922771190','15999916968',
        #             '15119820746','15986585396','13430915912','15012471375','17880567972',
        #             '17876156948','15915132451','13543985544','13717382951','13640695490',
        #             '15711707673','13680181412','13414759320','17820956139','18476072534',
        #             '17806699214','13418852766','17827181603','15919354070','15088137907'
        #                ]
        user_name_list = [
            '13432881156', 'yourhoneybee@163.com', '15018377821',
            '942364283@qq.com', '15767199023', '18814095644'
        ]
        if user_name is None:
            user_name = random.choice(user_name_list)
        else:
            user_name = user_name

        self.user_name = user_name

    def get_username_password(self, user_name=None):
        '''
        随机赋值用户和密码
        :return:
        '''
        # user_name_password1 = '18814095644:ljda.18814095644'
        # user_name_password2 = '13432881156:liang452035397'
        # user_name_password3 = 'yourhoneybee@163.com:810214bee810214'
        # user_name_password4 = '15018377821:zzm15331411'
        # user_name_password5 = '15767199023:j980216'
        # user_name_password6 = '942364283@qq.com:uwinvip'

        user_dict = {
            '18814095644': 'ljda.18814095644', '13432881156': 'liang452035397',
            'yourhoneybee@163.com': '810214bee810214', '15018377821': 'zzm15331411',
            '15767199023': 'j980216', '942364283@qq.com': 'uwinvip'
        }

        user_name_list = [
            '13432881156', 'yourhoneybee@163.com', '15018377821',
            '942364283@qq.com', '15767199023', '18814095644'
        ]
        # user_list = [user_name_password1, user_name_password2,
        #              user_name_password3, user_name_password4,
        #              user_name_password5, user_name_password6]

        # 判断是否存在user_name
        if user_name is None:
            user_name = random.choice(user_name_list)
        else:
            user_name = user_name

        user_password = user_dict[user_name]

        self.user_name = user_name
        self.pass_word = user_password

    def searchArticle(self, keyword_list, end_time):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        @param keywordList: 关键字数组
        @param endTime: 搜索时间范围结束
        '''
        # 手动更新cookie
        # self.__login()

        run_msg = '微博爬虫开始运行'
        self.db.Insert(self.channel.channel_id, self.entityId, run_msg)

        # 搜索时间
        start_time = end_time - datetime.timedelta(hours=2.5)

        page = 1
        count_page = 0
        article_list = list()
        page_url_list = self.__search_page_url(keyword_list, start_time, end_time, page)

        # 如果没获取到数据，则通过这个user_name进行登录获取新的cookie
        if len(page_url_list) == 0:
            self.__login(self.user_name)
            page_url_list = self.__search_page_url(keyword_list, start_time, end_time, page)

        for page_url in page_url_list:
            time.sleep(1.5)
            count_page += 1
            self.logger.debug('Start to crawl: %s' % page_url)
            article_list_page = self.__parse_page_url(page_url, count_page)
            article_list.extend(article_list_page)

        if article_list is None:
            article_msg = '微博没有爬取到数据'
            self.email.send(self.monitor_title, article_msg)
            self.db.Insert(self.channel.channel_id, self.entityId, article_msg)

        return article_list

    def __search_page_url(self, keywordList, startTime, endTime, page):
        '''
        :param keywordList: 关键词
        :param startTime: 搜索开始时间
        :param endTime: 搜索结束时间
        :param page: 页码
        :return: 返回微博内容列表的链接
        '''
        query = urllib.quote(' '.join(keywordList).encode('utf-8'))
        params = {
            'typeall': '1',
            'suball': '1',  # 包含全部
            'timescope': 'custom:%s:%s' % (startTime.strftime("%Y-%m-%d-%H"), (endTime.strftime("%Y-%m-%d-%H"))),  # 时间
            # 微博搜索的时间范围格式不同 不能写%Y-%m-%d-%H
            'Refer': 'g',
            'page': page
        }
        user_agent = random.choice(AGENTS)
        headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Host': 's.weibo.com',
            'Referer': 'http://s.weibo.com/',
            'User-Agent': user_agent
        }

        index_url = 'http://s.weibo.com/weibo/' + query  # 搜索主页+

        # 随机选取cookie
        self.change_cookie()
        self.logger.debug('Use Cookie %s' % self.user_name)
        # 若加载的cookie没有，则重新登录
        try:
            cookies = self.__load_cookies_from_lwp(self.user_name)
            html = self.session.get(index_url, params=params, headers=headers, cookies=cookies)  # 加载本地cookies
            # print(html)
            response = etree.HTML(html)
            # 获取总共有多少页
            page_urls = response.xpath('//div[@class="m-page"]//span[@class="list"]//li/a/@href')
            page_url_list = list()
            for page in page_urls:
                url = self.HTTP + page
                page_url_list.append(url)
                print(url)
            return page_url_list

        except Exception as e:
            loginFlag = self.__login(self.user_name)
            self.logger.debug('Use username: %s' % self.user_name)
            if loginFlag is False:
                self.logger.error('Fail to login')
                login_msg = 'Weibo login field! Username is %s' % self.user_name
                self.email.send(self.monitor_title, login_msg)
                self.db.Insert(self.channel.channel_id, self.entityId, login_msg)
                return

            cookies = self.__load_cookies_from_lwp(self.user_name)
            self.logger.debug('Get a new Cookie: %s' % cookies)
            try:
                html = self.session.get(index_url, params=params, headers=headers, cookies=cookies)  # 加载本地cookies
                response = etree.HTML(html)
                # 获取总共有多少页
                page_urls = response.xpath('//div[@class="m-page"]//span[@class="list"]//li/a/@href')
                page_url_list = list()
                for page in page_urls:
                    url = self.HTTP + page
                    page_url_list.append(url)
                return page_url_list
            except Exception as e:
                self.logger.warning('Crawler failed: %s' % e)
                msg = 'No return data, because of %s' % e
                self.email.send(self.monitor_title, msg)
                self.db.Insert(self.channel.channel_id, self.entityId, msg)

    def __parse_page_url(self, page_url, count_page=0):
        '''
        获取每一页的微博信息
        @return: article_list
        '''
        user_agent = random.choice(AGENTS)
        headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Host': 's.weibo.com',
            'Referer': 'http://s.weibo.com/',
            'User-Agent': user_agent
        }

        self.change_cookie()
        cookies = self.__load_cookies_from_lwp(self.user_name)
        response = self.session.get(url=page_url, headers=headers, cookies=cookies)

        # 若此cookie无效，则重新登录获取新的cookie
        if re.match(r'File not .*', response) is not None:
            self.__login(self.user_name)
            cookies = self.__load_cookies_from_lwp(self.user_name)
            response = self.session.get(url=page_url, headers=headers, cookies=cookies)

        html = etree.HTML(response)
        # 内容list
        card_wrap_list = html.xpath('//div[@class="m-wrap"]/div[@id="pl_feedlist_index"]//div[@class="card-wrap"]')
        article_list = list()
        for card_wrap in card_wrap_list:
            try:
                mid = card_wrap.xpath('./@mid')[0]
            except Exception as e:
                continue
            info = card_wrap.xpath('.//div[@class="content"]')[0]
            author_name = info.xpath('.//a[@class="name"]/text()')[0]
            author_id = re.search(r'/(\d+).?refer_flag', info.xpath('.//a[@class="name"]/@href')[0]).group(1)

            txt = info.xpath('.//p[@class="txt"]')[0]
            content = txt.xpath('string(.)').strip()
            title = content[:30].replace("'", "''").replace("%", "\%").replace(":", "\:")+' '

            article_info = info.xpath('.//p[@class="from"]/a')[0]
            article_url = self.HTTP + article_info.xpath('./@href')[0]

            statistics = card_wrap.xpath('.//div[@class="card-act"]//li/a/text()')

            collect_count = re.search(r'(\d+)', statistics[0])
            forward_count = re.search(r'(\d+)', statistics[1])
            reply_count = re.search(r'(\d+)', statistics[2])
            like_count = card_wrap.xpath('.//div[@class="card-act"]//li/a/em/text()')

            if collect_count:
                collect_count = collect_count.group(1).strip()
            else:
                collect_count = 0
            if forward_count:
                forward_count = forward_count.group(1).strip()
            else:
                forward_count = 0
            if reply_count:
                reply_count = reply_count.group(1).strip()
            else:
                reply_count = 0
            if len(like_count):
                like_count = like_count[0]
            else:
                like_count = 0

            publish_datetime = self.__parse_time(article_url, cookies)

            article = Article(mid, self.channel.channel_id, title, content, url=article_url,
                              publish_datetime=publish_datetime, author_id=author_id,  author_name=author_name)

            article.statistics.collect_count = collect_count
            article.statistics.forward_count = forward_count
            article.statistics.reply_count = reply_count
            article.statistics.like_count = like_count

            if article not in article_list:
                article_list.append(article)

        self.logger.debug('%d page crawl article is %s' % (count_page, len(article_list)))
        return article_list

    def __parse_time(self, article_url, cookies):
        time.sleep(1.5)
        url = '%s&type=comment' % article_url
        user_agent = random.choice(AGENTS)
        headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'User-Agent': user_agent
        }

        response = self.session.get(url, headers=headers, cookies=cookies)
        try:
            date_str = re.search(r'date=\\"(\d+)\\"', response).group(1)
            localtime = time.localtime(int(date_str[0:10]))
            publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S', localtime)
        except Exception as e:
            publish_datetime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')

        return publish_datetime

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        # return
        try:
            (data, check) = self.__fetchSingleArticle(article)

            if check == '0':
                soup = BeautifulSoup(data,'lxml')
                ulList = soup.findAll('ul',{'class':'WB_row_line WB_row_r4 clearfix S_line2'})
                li_list = ulList[0].findAll('li')
                self.__parseStatistics(article, li_list)
            elif check=='1':
                self.logger.warning(u'要访问的网页404了:%s',article.url)
                return
            else:
                self.logger.warning(u'抱歉，你访问的页面地址有误，或者该页面不存在：%s', article.url)
                return
        except:
            self.logger.error('Fail to fetch statistics for:%s, %s', article.url, traceback.format_exc())
            return

    def __parseStatistics(self, article, soup_li_list):
        # 新版
        collect_count = soup_li_list[0].find('span').text
        collect_count = re.findall(r'\d+', collect_count)
        if len(collect_count) > 0:
            collect_count = int(collect_count[0])
        else:
            collect_count = 0
        forward_count = soup_li_list[1].find('span').text

        forward_count = re.findall(r'\d+', forward_count)
        if len(forward_count) > 0:
            forward_count = int(forward_count[0])
        else:
            forward_count = 0

        reply_count = soup_li_list[2].find('span').text
        reply_count = re.findall(r'\d+', reply_count)
        if len(reply_count) > 0:
            reply_count = int(reply_count[0])
        else:
            reply_count = 0

        like_count = soup_li_list[3].find('span').text
        like_count = re.findall(r'\d+', like_count)
        if len(like_count) > 0:
            like_count = int(like_count[0])
        else:
            like_count = 0
        article.statistics.reply_count = reply_count
        article.statistics.like_count = like_count
        article.statistics.collect_count = collect_count
        article.statistics.forward_count = forward_count

    def __fetchSingleArticle(self, article):
        '''
        根据文章url获取文章
        '''
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
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Cache-Control': 'max-age=0',
            'Connection': 'keep-alive',
            'Host': 'weibo.com',
            'Upgrade-Insecure-Requests': '1',
            'User-Agent': '%s' % randdom_header
        }

        data2={
            'type':'comment'
        }
        print article.url
        time.sleep(2)
        self.change_cookie()
        cookies = self.__load_cookies_from_lwp(self.user_name)
        html = self.session.get(article.url, headers=headers, params=data2, cookies=cookies)
        # 若cookie无效，则重新登录获取
        if re.match(r'File not .*', html) is not None:
            self.__login(self.user_name)
            cookies = self.__load_cookies_from_lwp(self.user_name)
            html = self.session.get(article.url, headers=headers, params=data2, cookies=cookies)

        if html.find('<div class="page_error">') == -1:
            lindex = html.find('<script>FM.view({"ns":"pl.content.weiboDetail.index"')
            rindex = html[lindex:].find('</script>')
            rindex = lindex + rindex - 1
            lindex = lindex + len('<script>FM.view(')
            # self.logger.debug(html[lindex:rindex])
            try:
                jo = json.loads(html[lindex:rindex])
                data = jo['html']  # 实时微博页面
                return (data, '0')
            except:
                return ({}, '1')
        else:
            return ({}, '2')

    def __get_username(self, user_name):
        """
        get legal username
        """
        username_quote = urllib.quote(user_name)
        username_base64 = base64.b64encode(username_quote.encode("utf-8"))
        return username_base64.decode("utf-8")

    def __get_password(self, servertime, nonce, pubkey):
        """
        get legal password
        """
        string = (str(servertime) + "\t" + str(nonce) + "\n" + str(self.pass_word)).encode("utf-8")
        public_key = rsa.PublicKey(int(pubkey, 16), int("10001", 16))
        password = rsa.encrypt(string, public_key)
        password = binascii.b2a_hex(password)
        return password.decode()

    def __get_json_data(self, su_value):
        """
        get the value of "servertime", "nonce", "pubkey", "rsakv" and "showpin", etc
        """
        params = {
            "entry": "weibo",
            "callback": "sinaSSOController.preloginCallBack",
            "rsakt": "mod",
            "checkpin": "1",
            "client": "ssologin.js(v1.4.18)",
            "su": su_value,
            "_": int(time.time()*1000),
        }
        try:
            response = self.session.get("http://login.sina.com.cn/sso/prelogin.php", params=params)
            json_data = json.loads(re.search(r"\((?P<data>.*)\)", response).group("data"))
        except Exception:
            json_data = {}
            self.logger.error("WeiBoLogin get_json_data error: %s", traceback.format_exc())

        self.logger.debug("WeiBoLogin get_json_data: %s", json_data)
        return json_data

    def __login(self, user_name=None):

        self.user_uniqueid = None
        self.user_nick = None

        # 根据传入的user_name获取用户名和密码
        self.get_username_password(user_name)

        # get json data
        s_user_name = self.__get_username(self.user_name)

        # user_name
        self.logger.debug('login weibo use %s' % self.user_name)

        json_data = self.__get_json_data(su_value=s_user_name)
        if not json_data:
            return False
        s_pass_word = self.__get_password(json_data["servertime"], json_data["nonce"], json_data["pubkey"])

        # make post_data
        post_data = {
            "entry": "weibo",
            "gateway": "1",
            "from": "",
            "savestate": "7",
            "userticket": "1",
            "vsnf": "1",
            "service": "miniblog",
            "encoding": "UTF-8",
            "pwencode": "rsa2",
            "sr": "1280*800",
            "prelt": "529",
            "url": "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack",
            "rsakv": json_data["rsakv"],
            "servertime": json_data["servertime"],
            "nonce": json_data["nonce"],
            "su": s_user_name,
            "sp": s_pass_word,
            "returntype": "TEXT",
        }

        # get captcha code
        if json_data["showpin"] == 1:

            # captcha_msg = '微博爬虫进入验证码页面，虫子已被反爬'
            # self.email.send(self.monitor_title,captcha_msg)
            # self.db.Insert(self.channel.channel_id,self.entityId,captcha_msg)

            url = "http://login.sina.com.cn/cgi/pin.php?r=%d&s=0&p=%s" % (int(time.time()), json_data["pcid"])
            print(url)
            with open("captcha.jpg", "wb") as file_out:
                file_out.write(self.session.get(url, textRspOnly=False).content)

            print self.session.get(url, textRspOnly=False).content
            code = raw_input("请输入验证码:")
            post_data["pcid"] = json_data["pcid"]
            post_data["door"] = code

        # login weibo.com
        login_url_1 = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.18)&_=%d" % int(time.time())
        json_data_1 = self.session.post(login_url_1, data=post_data, textRspOnly=False).json()
        if json_data_1["retcode"] == "0":
            params = {
                "callback": "sinaSSOController.callbackLoginStatus",
                "client": "ssologin.js(v1.4.18)",
                "ticket": json_data_1["ticket"],
                "ssosavestate": int(time.time()),
                "_": int(time.time()*1000),
            }
            response = self.session.get("https://passport.weibo.com/wbsso/login", textRspOnly=False, params=params)
            json_data_2 = json.loads(re.search(r"\((?P<result>.*)\)", response.text).group("result"))
            if json_data_2["result"] is True:
                self.user_uniqueid = json_data_2["userinfo"]["uniqueid"]
                self.user_nick = json_data_2["userinfo"]["displayname"]
                self.logger.info("WeiBoLogin succeed!")
                self.__save_cookies_lwp(response.cookies, self.user_name)  # 保存cookies到本地
            else:
                self.logger.warning("WeiBoLogin failed: %s", json_data_2)
        else:
            self.logger.warning("WeiBoLogin failed: %s", json_data_1)
        return True if self.user_uniqueid and self.user_nick else False

    def __save_cookies_lwp(self, cookiejar, usename):
        """
        保存cookies到本地
        """
        filename = 'sina_cookie_pool/sinaweibocookies_%s' % usename
        lwp_cookiejar = cookielib.LWPCookieJar()
        for c in cookiejar:
            args = dict(vars(c).items())
            args['rest'] = args['_rest']
            del args['_rest']
            c = cookielib.Cookie(**args)
            lwp_cookiejar.set_cookie(c)
        lwp_cookiejar.save(filename, ignore_discard=True)

    def __load_cookies_from_lwp(self, usename):
        """
        读取本地cookies
        """
        lwp_cookiejar = None
        try:
            filename = 'sina_cookie_pool/sinaweibocookies_%s' % usename
            self.logger.debug('Use cookie filename is %s' % usename)
            lwp_cookiejar = cookielib.LWPCookieJar()
            lwp_cookiejar.load(filename, ignore_discard=True)
        except:
            self.__login()
            filename = 'sina_cookie_pool/sinaweibocookies_%s' % '942364283@qq.com'
            self.logger.debug('cookies_filename: %s' % filename)
            lwp_cookiejar = cookielib.LWPCookieJar()
            lwp_cookiejar.load(filename, ignore_discard=True)
        return lwp_cookiejar

    def refreshSearch(self):
        '''
        重置搜索
        '''
        pass

    def refreshCommentCrawler(self):
        '''
        重置评论爬取
        '''
        self.lastCommentId = None

    def crawlComment(self, article):
        '''
        根据文章，爬取文章的评论，返回评论列表
        @return: (commentList, hasnext)二元组，commentList是指评论数组（每个元素是Comment实例），hasnext表示是否还有要爬取
        '''
        # return (list(), False)
        commentList = list()

        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        headers = {
            'Accept': '* / *',
            'Accept-Encoding': 'gzip, deflate, sdch, br',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Cache-Control': 'max-age=0',
            'Connection': 'keep-alive',
            'Content - Type':'application / x - www - form - urlencoded',
            'Host': 'weibo.com',
            'Referer': '%s' % article.url[:article.url.find('?')+1],
            'refer_flag' : '1001030103_ & type = comment',
            'Upgrade-Insecure-Requests': '1',
            'User-Agent':'Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0'
        }
        data1 = {
            'ajwvr': '6',
            'id': '%s' % article.tid,
            'from': 'singleWeiBo'
        }

        re_url = 'https://weibo.com/aj/v6/comment/big'

        html = self.session.get(re_url,params=data1, headers=headers, cookies=self.__load_cookies_from_lwp(self.user_name))

        jo = json.loads(html)
        data = jo['data']['html']
        # self.logger.error( data)
        soup = BeautifulSoup(data, 'lxml')
        if soup.find('div', {'class': "WB_empty"}) is None:

            commentbox = soup.find('div', {'class': "list_box"})
            root_commentlist = commentbox.find('div', {'node-type': "comment_list"})
            root_commentlist2=root_commentlist.find_all('div', {'node-type': "root_comment"})
            count_comment = 1
            self.logger.debug('root_commentlist:%d   %s', len(root_commentlist2), article.url)
            for root_comment in root_commentlist2:

                if count_comment > 20:
                    break  # 爬取前20条评论
                self.logger.error('count_comment:%d', count_comment)
                comment_id = root_comment.attrs['comment_id']  # 一级评论id
                list_con = root_comment.find('div', {'class': "list_con"})
                firstcomentwrap = list_con.find('div', {'class': "WB_text"})
                firstcoment = firstcomentwrap.text.strip()  # 一级评论内容

                useridwrap = firstcomentwrap.find('a')
                user_id = useridwrap.attrs['usercard']  # 一级评论者id
                user_id = re.findall(r'id=(\d+)', user_id)[0]

                user_name = useridwrap.test  # 一级评论者name
                if user_name is None:
                    user_name = ' '
                publish_timediv = list_con.find('div', {'class': "WB_func clearfix"})
                try:
                    publish_time = self.parseDateTime(publish_timediv.findAll('div')[1].text)  # 一级评论发布时间
                except:
                    continue
                # self.logger.error(publish_time)
                # if publish_time < datetime.datetime.now()-datetime.timedelta(hours=48):
                #     break

                like_count_div = publish_timediv.findAll('div')[0]
                try:
                    like_count_li = like_count_div.findAll('li')[3]
                    like_count = like_count_li.findAll('em')[1].text
                except:
                    like_count = 0
                if u'赞' == like_count:
                    like_count = 0

                commentList.append(Comment(article.tid, self.channel.channel_id, comment_id,
                                           add_datetime, publish_time,
                                           None, None, None, None,
                                           user_id, user_name, firstcoment, None,
                                           None, like_count, None, dislike_count=None
                                           ))
                count_comment += 1  # 评论数计数

        return (commentList, False)

    def parseDateTime(self, datetimeStr):
        if datetimeStr.find(u'\u79d2\u524d') > 0:
            secondsDelta = float(datetimeStr.replace(u'\u79d2\u524d', ''))
            return datetime.datetime.now() - datetime.timedelta(seconds=secondsDelta)
        if datetimeStr.find(u'\u5206\u949f\u524d') > 0:
            secondsDelta = float(datetimeStr.replace(u'\u5206\u949f\u524d', '')) * 60
            return datetime.datetime.now()-datetime.timedelta(seconds=secondsDelta)
        if datetimeStr.find(u'\u4eca\u5929') >= 0:
            datetimeStr = datetime.datetime.today().strftime('%Y-%m-%d') + datetimeStr.replace(u'\u4eca\u5929', '')
            return datetime.datetime.strptime(datetimeStr, '%Y-%m-%d %H:%M')

        if datetimeStr.find(u'\u6708') >= 0:
            datetimeStr = str(datetime.datetime.today().year) + '-' + \
                        datetimeStr.replace(u'\u6708', '-').replace(u'\u65e5','')
            return datetime.datetime.strptime(datetimeStr, '%Y-%m-%d %H:%M')
        return datetime.datetime.strptime(datetimeStr, '%Y-%m-%d %H:%M')


