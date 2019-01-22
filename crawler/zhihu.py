# _*_ coding:utf-8 _*_
'''
Created on 6 July 2018
@author: Jondar
'''
CRAWL_ARTICLE_HEADERS = {
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
#"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.9",
"Connection":"keep-alive",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36",
}
CRAWL_COMMENT_HEADERS = {
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"authorization":"oauth c3cef7c66a1843f8b3a9e6a1e3160e20",
#"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.9",
"Connection":"keep-alive",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"}
COMMENT_URL_TEMPLATE = 'https://www.zhihu.com/api/v4/questions/%s/answers?sort_by=default&include=data#5B#2A#5D.is_normal#2Cadmin_closed_comment#2Creward_info#2Cis_collapsed#2Cannotation_action#2Cannotation_detail#2Ccollapse_reason#2Cis_sticky#2Ccollapsed_by#2Csuggest_edit#2Ccomment_count#2Ccan_comment#2Ccontent#2Ceditable_content#2Cvoteup_count#2Creshipment_settings#2Ccomment_permission#2Ccreated_time#2Cupdated_time#2Creview_info#2Cquestion#2Cexcerpt#2Crelationship.is_authorized#2Cis_author#2Cvoting#2Cis_thanked#2Cis_nothelp#2Cupvoted_followees#3Bdata#5B#2A#5D.mark_infos#5B#2A#5D.url#3Bdata#5B#2A#5D.author.follower_count#2Cbadge#5B#3F#28type#3Dbest_answerer#29#5D.topics&limit=%d&offset=%d'
COMMENT_PAGE_SIZE = 20

import hmac
import base64


from hashlib import sha1
from PIL import Image
from bs4 import BeautifulSoup

from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.objectmodel.comment import Comment
try:
    import cookielib
except:
    import http.cookiejar as cookielib
import requests, re, time, os, json, datetime,sys, traceback, random
reload(sys)
sys.setdefaultencoding("utf-8")


class ZhihuCrawler(object):
    '''
    classdocs
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
        self.channel = channel
        self.nextCommentUrl = None
        self.user_name_password = self.get_username_password()
        self.user_name = self.user_name_password[0]
        self.pass_word = self.user_name_password[1]

        # 构造headers
        self.agent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
        self.headers = {
            "HOST": "www.zhihu.com",
            "referer": "https://www.zhihu.com",
            "User-Agent": self.agent,
            "Connection": "keep-alive"
        }

        # 利用session保持连接
        self.session = requests.session()
        self.session.cookies = cookielib.LWPCookieJar(filename='cookies.txt')
        try:
            self.session.cookies.load(ignore_discard=True, ignore_expires=True)
        except:
            self.logger.error('cookies 未能加载')

        if self.isLogin():
            self.logger.info('已经登录知乎')
        else:
            print '未登录知乎'
            self.login(self.user_name, self.pass_word)

        self.sleepRange = [0, 1]
        self.lastCrawlTime = 0

    def get_username_password(self):
        '''
        随机获取用户和密码
        :return:
        '''
        user_name_password1 = '18814095644:031935.qq'
        user_name_password2 = '13064596945:Desktop4you'
        user_name_password3 = '13432881156:liang123456789'
        user_name_password4 = '15902057182:uwinvip168'

        user_list = [user_name_password1, user_name_password2, user_name_password3, user_name_password4]

        user_choice = random.choice(user_list)
        user_name_password = user_choice.split(':')
        return user_name_password

    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        :param keywordList: 关键字数组
        :param endTime: 搜索时间范围结束
        :return:
        '''
        if self.isLogin():
            url = 'https://www.zhihu.com/r/search?q=%s&correction=1&type=content&range=%d&offset=%d'
            articleList = list()
            for keyword in keywordList:
                offset = 0
                while True:
                    keyword_url = url % (keyword, self.channel.search_ranges, offset)
                    self.logger.info(r'搜索关键词的url：' + keyword_url)
                    response = self.session.get(keyword_url, headers=self.headers)
                    articleListJson = json.loads(response.text)
                    if (articleListJson['paging']['next'] == ""):
                        break
                    searchhtml = articleListJson['htmls']
                    offset += 10
                    for articlehtml in searchhtml:
                        soup = BeautifulSoup(articlehtml, 'html.parser')
                        title = soup.find('div', attrs={'class': "title"})
                        href = title.find('a', attrs={'class':"js-title-link"})
                        if(href['href'].find('question') == -1):
                            article_url = href['href']
                        else:
                            article_url = 'https://'+ self.channel.url + href['href']
                        self.logger.info(u'文章的url：' + article_url)
                        article = self.crawlArticle(article_url)
                        if (article is not None and article not in articleList):
                            articleList.append(article)
            return articleList
        else:
            self.logger.error(u'未登录知乎，请确认登录')
            return None

    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        :return:返回一个article实例
        '''
        article_id = re.findall(r'\d+', url)[0]

        html = self.session.get(url=url, headers=CRAWL_ARTICLE_HEADERS)

        soup = BeautifulSoup(html.text,'lxml')

        if (url.find('zhuanlan') != -1):
            main = soup.find('textarea', attrs={'id': "preloadedState"})
            try:
                if main is None:
                    return main
                sub = re.findall(r'new Date\(\".*\"\)', main.text)[0]
                sub = re.findall(r'".*"', sub)[0]
                maintext = re.subn(r'new Date\(\".*\"\)', sub, main.text)[0]
                try:
                    articleJson = json.loads(maintext)["database"]["Post"][article_id]
                except:
                    return None
                #获取标题
                title = articleJson["title"]
                self.logger.info(title)
                authorName = articleJson["author"]
                contentSoup = BeautifulSoup(articleJson["content"])
                content = contentSoup.text
                commentCount = articleJson["commentCount"]
                collapsedCount = articleJson["collapsedCount"]
                likeCount = articleJson["likeCount"]
                publishedTime = articleJson["publishedTime"][0:18]
                timeArray = time.strptime(publishedTime, "%Y-%m-%dT%H:%M:%S")
                publishedTime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
                article = Article(article_id, self.channel.channel_id, title, content,
                                publishedTime, url, None, authorName)
                article.statistics.reply_count = commentCount
                article.statistics.like_count = likeCount
                article.statistics.collect_count = collapsedCount
                self.session.close()
                return article
            except:
                return None
        if (url.find('question') != -1):
            # 获取标题
            try:  # 针对404页面
                main = soup.find('div', attrs={'id': "data"}).attrs['data-state']
                articleJson = json.loads(main)
                questionJson = articleJson['entities']['questions'][article_id]
                title = questionJson['title']
                self.logger.info(title)
                contentSoup = BeautifulSoup(questionJson['editableDetail'],'lxml')
                content = contentSoup.text
                author_id = questionJson['author']['id']
                author_name = questionJson['author']['name']
                createTimeInFloat = questionJson['created']
                publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(createTimeInFloat))
                reply_count = questionJson['commentCount']
                read_count = questionJson['visitCount']
                collect_count = questionJson['followerCount']
                article = Article(article_id, self.channel.channel_id, title, content,
                                  publish_datetime, url, author_id, author_name)
                article.statistics.reply_count = reply_count
                article.statistics.read_count = read_count
                article.statistics.collect_count = collect_count
                return article
            except:
                return None

    def crawlStatistics(self, article):
        '''
        爬去统计信息
        :param article:
        :return: 无需返回参数，统计信息写入article实例
        '''
        try:
            articleCopy = self.crawlArticle(article.url)
            article.statistics.reply_count = articleCopy.statistics.reply_count
            article.statistics.read_count = articleCopy.statistics.read_count
            article.statistics.collect_count = articleCopy.statistics.collect_count
        except:
            return

    def refreshSearch(self):
        '''
        重置搜索
        '''
        pass

    def refreshCommentCrawler(self):
        '''
        重置评论爬取
        '''
        self.nextCommentUrl = None


    def crawlComment(self, article):
        '''
        根据文章，爬去文章的评论，返回评论列表
        :param article:
        :return:(commentList, hasnext)二元组，commentList是指评论数组（每个元素是Comment实例），hasnext表示是否还有要爬取
        '''
        try:
            commentList = list()
            count = 0
            if(article.url.find('question') != -1):
                if self.nextCommentUrl is None:
                    curl = COMMENT_URL_TEMPLATE % (article.tid, COMMENT_PAGE_SIZE, 0)
                    curl = curl.replace('#', '%')
                else:
                    curl = self.nextCommentUrl
                self.lastCrawlTime = time.time()
                self.randomSleep()
                result = self.session.get(curl, headers=CRAWL_COMMENT_HEADERS)
                jo = json.loads(result.text)
                paging = jo['paging']
                hasnext = not paging['is_end']
                self.nextCommentUrl = paging['next']
                dataList = jo['data']
                add_datetime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                for data in dataList:
                    publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(data['updated_time']))
                    dr = re.compile(r'<[^>]+>', re.S)
                    content = r"'%s'"%(str(dr.sub('', data['content'])))
                    comment = Comment(article.tid, article.channel_id, data['id'],
                                      add_datetime, publish_datetime,
                                      ip_address=None, location_country=None, location_region=None, location_city=None,
                                      author_id=data['author']['id'], author_name=data['author']['name'],
                                      content=content, reply_author_id=None,
                                      read_count=None, like_count=data['voteup_count'], reply_count=data['comment_count'],
                                      dislike_count=None)
                    count += 1
                    print count
                    commentList.append(comment)
                    if count > 500:
                        print count
                        hasnext = False
                        break
                return (commentList, hasnext)


            if (article.url.find('zhuanlan') != -1):
                offset = 0

                comment_url = 'https://zhuanlan.zhihu.com/api/posts/%s/comments?limit=10&offset=%d' % (article.tid, offset)
                self.lastCrawlTime = time.time()
                self.randomSleep()
                response = self.session.get(comment_url, headers=CRAWL_COMMENT_HEADERS)
                if (response.text == '[]' or count > 500):
                    return (list(), False)
                dataList = json.loads(response.text)
                add_datetime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                for data in dataList:
                    publish_datetime = str(data['createdTime'])[0:19].replace('T',' ')
                    content = r"'%s'" % str(data['content'])
                    comment = Comment(article.tid, article.channel_id, data['id'],
                                      add_datetime, publish_datetime,
                                      ip_address=None, location_country=None, location_region=None, location_city=None,
                                      author_id=data['author']['uid'], author_name=data['author']['name'],
                                      content=content, reply_author_id=None,
                                      read_count=None, like_count=data['likesCount'], reply_count=None,
                                      dislike_count=data['dislikesCount'])
                    commentList.append(comment)
                    print count
                    count += 1
                    if count > 500:
                        print count
                        break
                offset += 10
                return (commentList, False)
        except:
            self.logger.debug(traceback.format_exc())
            return (list(), False)    #不要返回None  会报nonetype has no method of len()错误  (sacrawler.py里)

    def isLogin(self):
        '''
        通过个人中心页面返回状态码来判断是否登录
        通过allow_redirects 设置为不获取重定向后的页面
        :return:
        '''
        url = 'https://www.zhihu.com/settings/account'
        login_code = self.session.get(url, headers=self.headers, allow_redirects=False).status_code
        if login_code == 200:
            return True
        else:
            return False

    def getXsrfDc0(self):
        '''
        获取xsrf code 和 d_c0
        在请求登录页面的时候页面会将xsrf code 和 d_c0加入到cookie中返回给客户端
        :return:
        '''
        response = self.session.get('https://www.zhihu.com/signin', headers=self.headers)
        return response.cookies['_xsrf'], response.cookies['d_c0']

    def getSignaturn(self, time_str):
        '''
        生成signature, 利用hmac加密
        根据分析之后的js, 可发现里面有一段是进行hmac加密的
        分析执行加密的js代码, 可得出加密的字段，然后进行解码
        :param time_str:
        :return:
        '''
        h = hmac.new(key='d1b964811afb40118a12068ff74a12f4'.encode('utf-8'), digestmod=sha1)
        grant_type = 'password'
        client_id = 'c3cef7c66a1843f8b3a9e6a1e3160e20'
        source = 'com.zhihu.web'
        now = time_str
        h.update((grant_type + client_id + source + now).encode('utf-8'))
        return h.hexdigest()

    def getIdentifyingCode(self):
        '''
        判断页面是否需要填写验证码，如果需要则弹出验证码，进行手动填写
        请求验证码的url后的参数lang=en，意思是取得英文验证码
        :param headers:
        :return:
        '''
        response = self.session.get('https://www.zhihu.com/api/v3/oauth/captcha?leng=en', headers=self.headers)
        # 检查是否存在验证码
        r = re.findall('"show_captcha":(\w+)', response.text)
        if r[0] == 'false':
            return ''
        else:
            response = self.session.put('https://www.zhihu.com/api/v3/oauth/captcha?lang=en', headers=self.headers)
            show_captcha = json.load(response.text)['img_base64']
            with open('zhihu_captcha.jpg', 'wb') as f:
                f.write(base64.b64decode(show_captcha))
            im = Image.open('zhihucaptcha.jpg')
            im.show()
            im.close()
            captcha = input('输入验证码：')
            self.session.post('https://www.zhihu.com/api/v3/oauth/captcha?leng=en', headers=self.headers,
                              data={'input_text': captcha})
            return captcha

    def login(self, username, password):
        '''
        登录
        :param username: 用户名
        :param password: 密码
        :return:
        '''
        post_url = 'https://www.zhihu.com/api/v3/oauth/sign_in'
        XXsrftoken, XUDID = self.getXsrfDc0()
        self.headers.update({
            'authorization': 'oauth c3cef7c66a1843f8b3a9e6a1e3160e20',
            'X-Xsrftoken': XXsrftoken,
        })
        time_str = str(int((time.time() * 1000)))
        # 直接写在引号内的值为固定值
        post_data = {
            'client_id': 'c3cef7c66a1843f8b3a9e6a1e3160e20',
            'grant_type': 'password',
            'timestamp': time_str,
            'source': 'com.zhihu.web',
            'password': password,
            'username': username,
            'lang': 'en',
            'ref_source': 'homepage',
            'utm_source': '',
            'signature': self.getSignaturn(time_str),
            'captcha': self.getIdentifyingCode()
        }

        response = self.session.post(post_url, data=post_data, headers=self.headers, cookies=self.session.cookies)
        if response.status_code == 201:
            # 保存cookie，下次直接读取保存的cookie，不用再次登录
            self.session.cookies.save()
            return True
        else:
            return False

    def randomSleep(self):
        if time.time() - self.lastCrawlTime < self.sleepRange[0]:
            sleepTime =self.sleepRange[0] + (self.sleepRange[1]-self.sleepRange[0])*random.random()
            time.sleep(sleepTime)