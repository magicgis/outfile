# -*- coding:utf-8 -*-
'''
Created on 10 Oct 2017

@author: eyaomai
'''
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
import re, json, urllib, base64, requests, traceback
from bs4 import BeautifulSoup
import datetime, time, traceback,binascii
import rsa
import cookielib,random


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
        # self.user_name = "yourhoneybee@163.com"
        # self.session = SessionCrawler(sleepRange=[3,8])
        # self.pass_word = "810214bee810214"
        # self.user_name = "15018377821"
        # self.pass_word = "zzm15331411"
        self.user_name_password = self.get_username_password()
        self.user_name = self.user_name_password[0]
        self.pass_word = self.user_name_password[1]
        self.logger.info('username: %s' % self.user_name)

        self.session = SessionCrawler(sleepRange=[3, 8])

    def get_username_password(self):
        '''
        随机获取用户和密码
        :return:
        '''
        user_name_password1 = '18814095644:ljda.18814095644'
        user_name_password2 = '13432881156:liang452035397'
        user_name_password3 = 'yourhoneybee@163.com:810214bee810214'
        user_name_password4 = '15018377821:zzm15331411'
        user_name_password5 = '942364283@qq.com:uwinvip'
        user_name_password6 = '15767199023:j980216'

        user_list = [user_name_password1, user_name_password2,
                     user_name_password3, user_name_password4,
                     user_name_password5, user_name_password6]

        user_choice = random.choice(user_list)
        user_name_password = user_choice.split(':')
        return user_name_password

    def searchArticle(self, keywordList,endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        @param keywordList: 关键字数组
        @param endTime: 搜索时间范围结束
        '''

        # time_now = time.time()
        # date = time.strftime('%Y-%m-%d', time.localtime(time.time()))
        # com_time_low = time.mktime(time.strptime(date + ' 07:00:00', '%Y-%m-%d %H:%M:%S'))
        # com_time_hig = time.mktime(time.strptime(date + ' 07:59:58', '%Y-%m-%d %H:%M:%S'))
        # if time_now > com_time_low and time_now < com_time_hig:
        # loginFlag = self.__login()
        # if loginFlag is False:
        #     self.logger.error('Fail to logon')
        #     return

        startTime = endTime - datetime.timedelta(hours=2)
        # startTime=datetime.datetime(2017,11,20,23)
        page = 1
        articleList = list()
        hasnext = True
        while hasnext:
            data = self.__searchByPage(keywordList, startTime,endTime, page)
            (articleListInPage,hasnext) = self.__parseSearchPage(data)

            articleList.extend(articleListInPage)
            page+=1
        return articleList

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        # return
        try:
            (data,check) = self.__fetchSingleArticle(article)

            if check=='0':
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

    def __save_cookies_lwp(self, cookiejar):
        """
        保存cookies到本地
        """
        filename = 'sinaweibocookies'
        lwp_cookiejar = cookielib.LWPCookieJar()
        for c in cookiejar:
            args = dict(vars(c).items())
            args['rest'] = args['_rest']
            del args['_rest']
            c = cookielib.Cookie(**args)
            lwp_cookiejar.set_cookie(c)
        lwp_cookiejar.save(filename, ignore_discard=True)

    def __load_cookies_from_lwp(self):
        """
        读取本地cookies
        """
        filename = 'sinaweibocookies'
        lwp_cookiejar = cookielib.LWPCookieJar()
        lwp_cookiejar.load(filename, ignore_discard=True)
        # self.logger.debug(lwp_cookiejar)
        return lwp_cookiejar

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

    def __parseSearchPage(self, data):
        '''
        @return: (articleList,hasnext)
        '''
        articleList = list()
        hasnext = False
        soup = BeautifulSoup(data,"lxml")
        # self.logger.info(soup)
        #check if no result
        noResultDivList = soup.findAll('div',{'class':'pl_noresult'})
        if len(noResultDivList)>0:
            hasnext = False
            self.logger.info('No result')
            return (articleList, hasnext)

        #find page bar to check if more

        pageDivList = soup.findAll('div', {'class': 'W_pages'})
        if len(pageDivList)>0:
            pageDiv = pageDivList[0]
            if len(pageDiv.findAll('a',{'class':'page next S_txt1 S_line1'}))>0:
                hasnext = True
        if hasnext is False:
            self.logger.info('The last page')

        root_1 = soup.findAll('div',{"action-type":"feed_list_item"})
        # self.logger.debug(root_1)
        for r in root_1:
                root_2 = r.find('div', {'class': "content clearfix"})
                mid = r.attrs['mid']
                article_url = root_2.find('div', {'class': "feed_from W_textb"}).findNext('a').attrs['href']
                self.logger.debug('1  %s',article_url)
                if not article_url.startswith('http:'):
                    article_url = 'http:'+article_url
                # self.logger.debug(article_url)
                root_content = root_2.find('p', {'class': "comment_txt"})

                long_content = root_content.find('a', {'action-type': "fl_unfold"})
                try:
                    link_content = root_content.find('a').attrs['href']
                    link_content='  原文链接： '+link_content
                except:
                    link_content=''
                if long_content:
                    content_url = 'http://s.weibo.com/ajax/direct/morethan140?' + long_content.attrs['action-data']
                    self.session.randomSleep()
                    response = self.session.get(content_url, textRspOnly=False)
                    try:
                        content_html = response.json()['data']['html']
                        content = BeautifulSoup(content_html, 'html.parser').text.strip().replace("'", "''").replace("%", "\%").replace(":", "\:")
                    except Exception, e:
                        self.logger.debug('Exception: %s' % e)
                        continue
                else:
                    content = root_content.text.strip().replace("'", "''").replace("%", "\%").replace(":", "\:")
                    content=content + link_content
                    # self.logger.error(content)

                title = content[:30].replace("'", "''").replace("%", "\%").replace(":", "\:")+' '
                author_id = r.attrs['tbinfo']
                author_id = re.findall(r'ouid=(\d+)', author_id)[0]
                author_name = root_2.find('a').attrs['nick-name']
                publish_datetime = root_2.find('a', {'class': "W_textb"}).attrs['date']

                try:
                    publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(float(publish_datetime) / 1000))
                except:
                    continue
                article = Article(mid, self.channel.channel_id,
                                  title, content, publish_datetime, url=article_url,
                                  author_id=author_id,  author_name=author_name)

                #fetch statistics
                root_3 = r.find('div', {'class': "feed_action clearfix"})
                soup_li_list = root_3.findAll('li')
                self.__parseStatistics(article, soup_li_list)
                # print mid, article_url, add_datetime, channeltype, channel, title, content, author_id, author_name, \
                #     publish_datetime, reply_count, read_count, like_count, collect_count, forward_count
                if article not in articleList:
                    articleList.append(article)
        return (articleList,hasnext)

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
        html = self.session.get(article.url,headers=headers,params=data2, cookies=self.__load_cookies_from_lwp())  # 加载本地cookies

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

    def __searchByPage(self, keywordList, startTime, endTime, page):
        query = urllib.quote(' '.join(keywordList).encode('utf-8'))
        params = {
            'typeall': '1',
            'suball': '1',  # 包含全部
            'timescope': 'custom:%s:%s' % (startTime.strftime("%Y-%m-%d"), (endTime.strftime("%Y-%m-%d"))),  # 时间
            #微博搜索的时间范围格式不同 不能写%Y-%m-%d-%H
            'Refer': 'g',
            'page': page
        }
        headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Cache-Control': 'max-age=0',
            'Connection': 'keep-alive',
            'Host': 's.weibo.com',
            'Referer': 'http://s.weibo.com/',
            'Upgrade-Insecure-Requests': '1'
        }
        index_url = 'http://s.weibo.com/weibo/'+ query  # 搜索主页+查询词
        try:
            html = self.session.get(index_url, params=params, headers=headers,
                                    cookies=self.__load_cookies_from_lwp())  # 加载本地cookies
            lindex = html.find('<script>STK && STK.pageletM && STK.pageletM.view({"pid":"pl_weibo_direct"')
            rindex = html[lindex:].find('</script>')
            rindex = lindex + rindex - 1
            lindex = lindex + len('<script>STK && STK.pageletM && STK.pageletM.view(')
            jo = json.loads(html[lindex:rindex])
            data = jo['html']  # 实时微博页
            return data
        except:
            loginFlag = self.__login()
            if loginFlag is False:
                self.logger.error('Fail to logon')
                return
            html = self.session.get(index_url, params=params, headers=headers,
                                    cookies=self.__load_cookies_from_lwp())  # 加载本地cookies
            lindex = html.find('<script>STK && STK.pageletM && STK.pageletM.view({"pid":"pl_weibo_direct"')
            rindex = html[lindex:].find('</script>')
            rindex = lindex + rindex - 1
            lindex = lindex + len('<script>STK && STK.pageletM && STK.pageletM.view(')
            jo = json.loads(html[lindex:rindex])
            data = jo['html']  # 实时微博页
            return data

    def __get_username(self, user_name):
        """
        get legal username
        """
        username_quote = urllib.quote(user_name)
        username_base64 = base64.b64encode(username_quote.encode("utf-8"))
        return username_base64.decode("utf-8")

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

    def __get_password(self, servertime, nonce, pubkey):
        """
        get legal password
        """
        string = (str(servertime) + "\t" + str(nonce) + "\n" + str(self.pass_word)).encode("utf-8")
        public_key = rsa.PublicKey(int(pubkey, 16), int("10001", 16))
        password = rsa.encrypt(string, public_key)
        password = binascii.b2a_hex(password)
        return password.decode()

    def __login(self):

        self.user_uniqueid = None
        self.user_nick = None

        # get json data
        s_user_name = self.__get_username(self.user_name)
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

            url = "http://login.sina.com.cn/cgi/pin.php?r=%d&s=0&p=%s" % (int(time.time()), json_data["pcid"])
            with open("captcha.jpg", "wb") as file_out:
                file_out.write(self.session.get(url, textRspOnly=False).content)

            print self.session.get(url, textRspOnly=False).content
            #sendCode().sendMail("627818082@qq.com", u'微博登录验证码', u'验证码')
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
                self.logger.info("WeiBoLogin succeed: %s", json_data_2)
                self.__save_cookies_lwp(response.cookies)  # 保存cookies到本地
            else:
                self.logger.warning("WeiBoLogin failed: %s", json_data_2)
        else:
            self.logger.warning("WeiBoLogin failed: %s", json_data_1)
        return True if self.user_uniqueid and self.user_nick else False

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

        html = self.session.get(re_url,params=data1, headers=headers, cookies=self.__load_cookies_from_lwp())

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
        if datetimeStr.find(u'\u79d2\u524d')>0:
            secondsDelta = float(datetimeStr.replace(u'\u79d2\u524d',''))
            return datetime.datetime.now()-datetime.timedelta(seconds=secondsDelta)
        if datetimeStr.find(u'\u5206\u949f\u524d')>0:
            secondsDelta = float(datetimeStr.replace(u'\u5206\u949f\u524d',''))*60
            return datetime.datetime.now()-datetime.timedelta(seconds=secondsDelta)
        if datetimeStr.find(u'\u4eca\u5929')>=0:
            datetimeStr=datetime.datetime.today().strftime('%Y-%m-%d')+datetimeStr.replace(u'\u4eca\u5929','')
            return datetime.datetime.strptime(datetimeStr, '%Y-%m-%d %H:%M')
        #
        # if datetimeStr.find(u'\u7b2c')>=0:
        #     datetimeStr = datetime.datetime.today().strftime('%Y-%m-%d')
        #     return datetime.datetime.strptime(datetimeStr, '%Y-%m-%d %H:%M')

        if datetimeStr.find(u'\u6708')>=0:
            datetimeStr=str(datetime.datetime.today().year)+'-'+datetimeStr.replace(u'\u6708','-').replace(u'\u65e5','')
            return datetime.datetime.strptime(datetimeStr, '%Y-%m-%d %H:%M')
        return datetime.datetime.strptime(datetimeStr, '%Y-%m-%d %H:%M')