#-*- coding:utf-8-*-
'''
Created on 19 Nov 2017
@author: ZhangZemian
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

from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.objectmodel.comment import Comment

from PIL import Image
from bs4 import BeautifulSoup
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
        self.username = '13064596945'
        self.password = 'Desktop4you'

        #构造request headers
        agent = 'Mozilla/5.0 (Windows NT 5.1; rv:33.0) Gecko/20100101 Firefox/33.0'
        self.headers = {
            'Host':'www.zhihu.com',
            'Referer':'https://www.zhihu.com',
            'User-Agent':agent
        }

        #使用cookies登陆信息
        self.session = requests.session()
        self.session.cookies = cookielib.LWPCookieJar(filename='cookies')
        try:
            self.session.cookies.load(ignore_discard=True)
        except:
            self.logger.error(u"Cookie未能加载")

        if self.isLogin():
            self.logger.info(u'已经登录知乎')
        else:
            self.login(self.username, self.password)
        self.sleepRange = [0,1]
        self.lastCrawlTime = 0

    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        :param keywordList: 关键字数组
        :param endTime: 搜索时间范围结束
        :return:
        '''
        if self.isLogin():
            url = 'https://www.zhihu.com/r/search?q=%s&correction=1&type=content&range=%dd&offset=%d'
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

            #获取标题
            try:  #针对404页面
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



    def getXsrf(self):
        '''
        获取登陆时需要的xsrf
        :return:
        '''
        index_page = self.session.get('https://' + self.channel.url, headers=self.headers)
        html = index_page.text
        pattern = r'name="_xsrf" value="(.*?)"'
        _xsrf = re.findall(pattern, html)
        return _xsrf[0]

    def getCaptcha(self):
        '''
        获取登录时的验证码
        :return:
        '''
        timenow = str(int(time.time()*1000))
        captcha_url = 'https://www.zhihu.com/captcha.gif?r='+timenow+"&type=login"
        captcha = self.session.get(captcha_url, headers=self.headers)
        with open('captcha.jpg', 'wb') as f:
            f.write(captcha.content)
            f.close()
        # 用pillow 的 Image 显示验证码
        # 如果没有安装 pillow 到源代码所在的目录去找到验证码然后手动输入
        try:
            im = Image.open('captcha.jpg')
            im.show()
            im.close()
        except:
            self.logger.error(u'请到%s目录找到captcha.jpg手动输入登录验证码'%os.path.abspath('captcha.jpg'))
        captcha_cotent = raw_input('请输入验证码\n>')
        return str(captcha_cotent)


    def isLogin(self):
        '''
        通过查看用户个人信息来判断是否已经登陆
        :return:
        '''
        url = 'https://www.zhihu.com/settings/profile'
        login_code = self.session.get(url, headers=self.headers, allow_redirects=False).status_code
        if login_code == 200:
            return True
        else:
            return False


    def login(self, username, password):
        '''
        模拟知乎登录
        :param username:
        :param password:
        :return:
        '''
        #通过输入的用户名判断是否手机号
        postdata = {}
        post_url = ''
        if re.match(r'^1\d{10}$', username):
            self.logger.info(u'手机号码登录知乎')
            post_url = 'https://www.zhihu.com/login/phone_num'
            postdata = {
                '_xsrf':self.getXsrf(),
                'password':password,
                'remember_me':'true',
                'phone_num':username,
            }
        elif '@' in username:
            self.logger.info(u'邮箱登录知乎')
            post_url = 'https://www.zhihu.com/login/email'
            postdata = {
                '_xsrf': self.getXsrf(),
                'password': password,
                'remember_me': 'true',
                'email': username,
            }
        else:
            self.logger.error(u'账号输入格式出错，请重新登录')

        try:
            #不需要验证码直接登录
            login_page = self.session.post(post_url, data=postdata, headers=self.headers)
            login_code = login_page.text
            self.logger.info(u'非验证码登录')
            self.logger.info(u'知乎登录状态：' + login_page.status_code)
        except:
            postdata['captcha'] = self.getCaptcha()
            login_page = self.session.post(post_url, data=postdata, headers=self.headers)
            self.logger.info(u'验证码登录')
            self.logger.info(u'知乎登录状态：' + str(login_page.status_code))
        if self.isLogin():
            self.logger.info(u'成功登录知乎')
            self.session.cookies.save(ignore_discard=True, ignore_expires=True)
        else:
            self.logger.info(u'登录知乎失败')
            return 0

    def randomSleep(self):
        if time.time() - self.lastCrawlTime < self.sleepRange[0]:
            sleepTime =self.sleepRange[0] + (self.sleepRange[1]-self.sleepRange[0])*random.random()
            time.sleep(sleepTime)