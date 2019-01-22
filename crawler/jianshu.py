# _*_ coding:utf-8 _*_
'''
   Created on 27 July 2018

   @author: Jeremy
'''

from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
import re
import datetime, time
import requests
try:
    import cookielib
except:
    import http.cookiejar as cookielib
from bs4 import BeautifulSoup
import json
import urllib

class JianShuCrawler(object):

    def __init__(self,channel,logger=None):
        '''

        :param channel:
        :param logger:
        '''
        if logger is None:
            self.logger=Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger=logger
        self.channel=channel  #获取媒体实体
        # 设置请求头和代理
        self.headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate, br',
            'Accept-Language': 'zh-CN,zh;q=0.9',
            'Host': 'www.jianshu.com',
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36',
        }
        self.headers_1 = {
            'Host': 'www.jianshu.com',
            'Connection': 'keep-alive',
            'Connection-Length': '0',
            'Accept': 'application/json',
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36',
            'Accept-Encoding': 'gzip,deflate,br',
            'Accept-Language': 'zh-CN,zh;q=0.9'}
        #用户信息
        self.usename = '13432881156'
        self.password = 'liang359247623'
        #用cookie进行登录
        self.session=SessionCrawler()
        self.session.cookies = cookielib.LWPCookieJar(filename='cookie')#读取cookie
        try:
            self.session.cookies.load(ignore_discard=True)
        except:
            print('未能加载cookie')
        if self.islogin():
            print('已经登录简书')
        else:
            self.login(self.usename, self.password)

    def searchArticle(self,keywordList,endTime):
        '''
            根据关键字数组，开始时间和结束时间范围搜索文章
            :param keywordList: 关键字数组
            :param endTime: 搜索时间范围结束
            :return:
        '''
        if self.islogin():
            page = 0  # 页数
            articleList = list()
            hasnext = True  # 开始爬虫
            while hasnext:
                page += 1
                if page == 5:
                    break
                (articleListInPage, hasnext) = self._searchByPage(keywordList,endTime, page)
                is_article=list()
                for article in articleListInPage:
                    if article not in articleList:
                        is_article.append(article)
                articleList.extend(is_article)
                print(len(articleList))

            self.logger.debug('总共抓取文章有:%s' % len(articleList))
            return articleList
        else:
            self.logger.error(u'未登录简书，请确认登录')
            return None

    def _searchByPage(self, keywordList, endTime, page):
        hasnext = True
        articleList = list()
        page = str(page)  # 在简书内部搜索框搜索
        search_url_1 = "https://www.jianshu.com/search/do?q="
        search_url_2 = "&type=note&page=" + page + "&order_by=published_at&time_range=a_day"
        # q：是关键词搜索，page：页数,order_by是排序，time_range是按天排序
        query = urllib.quote(' '.join(keywordList).encode('utf-8'))
        search_url = search_url_1 + str(query) + search_url_2  # 抓取页面的网址
        csrf = self.getCsrf(keywordList, page)
        post_data = {'X-CSRF-Token': csrf}
        # 获取页面信息
        url_page = self.session.post(search_url, headers=self.headers_1, data=post_data,textRspOnly=False)
        if url_page.status_code == 200:
            self.logger.debug(u'已经获取中大新闻页面')
        else:
            self.logger.debug(u'中大新闻获取完毕')
            return (articleList,False) #已经爬取到新闻界面的最后一页

        # 以下对中大新闻页面进行解析
        articleJson = None
        try:
            articleJson = json.loads(url_page.text)
        except Exception, e:
            self.logger.error(e)

        if articleJson is None:
            self.logger.error('articleJson is None')
            return

        allInfo = articleJson['entries']  # allinfo代表字典0:{}\1:{}.....
        for info in allInfo:
            pre_publishedTime = info["first_shared_at"]
            publishedTime_1 = ''.join(re.findall('[^A-Za-z]', pre_publishedTime[0:-5]))  # 文章发表时间
            publishedTime = publishedTime_1[0:10] + ' ' + publishedTime_1[10:18]
            #print(publishedTime)
            urlTime = time.strptime(publishedTime, '%Y-%m-%d %H:%M:%S')
            Y, M, D, H = urlTime[0:4]
            urlTime2 = datetime.datetime(Y, M, D, H)
            # 转化成时间戳来比较float
            urlTime2 = time.mktime(urlTime2.timetuple())  # 文章发表时间的时间戳
            startTime = endTime - datetime.timedelta(days=self.channel.search_ranges)
            startTimeIntSecond = time.mktime(startTime.timetuple())  # 开始爬取的时间戳
            endTimeIntSecond = time.mktime(endTime.timetuple())  # 结束爬取的时间错
            if urlTime2 >= startTimeIntSecond and urlTime2 <= endTimeIntSecond:
               # 获取文章链接，爬取文章内容
               pre_article_url=info['slug']
               articleUrl = "https://www.jianshu.com/p/" + pre_article_url  # 文章的链接
               (content,authorName,title)=self.crawlArticle(articleUrl)#爬取文章内容
               #print(title)
               if content is None:  # 话题已被删除或则其他格式
                   print("没有爬到文章")
                   continue
               authorId = info["user"]["id"]    #作者id
               likeCount = info["likes_count"]  # 点赞数
               readCount = info["views_count"]  # 文章阅读数
               replyCount = info["public_comments_count"]  # 文章评论数
               tid = info["id"]  # 文章的id
               article=Article(tid,self.channel.channel_id,title,content,publishedTime,articleUrl,authorId,authorName)
               #self.crawlComment(article)
               article.statistics.reply_count = replyCount
               article.statistics.like_count = likeCount
               article.statistics.read_count = readCount
               if (article is not None) and (article not in articleList):
                   articleList.append(article)
            else:
                print('结束爬虫')
                hasnext = False
                break
        return (articleList,hasnext)

    def crawlArticle(self,url):
        html = self.session.get(url, headers=self.headers, verify=False)  # 获取文章的页面信息
        if html:
            print('文章页面获取成功')
            soup = BeautifulSoup(html,'html.parser')
            div = soup.find('div', attrs={'class': 'article'})
            title=div.find('h1',attrs={'class':'title'}).text.strip()#文章标题
            div_info=div.find('div',attrs={'class':'info'})
            span = div_info.find('span', attrs={'class': 'name'})
            authorName = span.find('a').text.strip()#文章的作者
            content = div.find('div', attrs={'class': 'show-content-free'}).text.strip()#文章的内容‘
            return (content, authorName, title)
        else:
            self.logger.error(u'该话题已被删除或存在其他格式')
            return (None,None,None)

    def refreshSearch(self):
        '''
        重置搜索
        '''
        pass

    def crawlComment(self, article):
        '''

        :param article:文章的实例
        :return:返回（commentList,hasnext）commentList是文章的评论，hasnext代表是否继续爬虫
        '''
        html=self.session.get(article.url,headers=self.headers,verify=False)
        soup=BeautifulSoup(html,'html.parser')
        commentList=list()
        add_time=datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        try:

            div=soup.find('div',attrs={'class':'normal-comment-list'})
            divs=div.findAll('div',attrs={'class':'comment'})
        except:
            self.logger.error(u'该页面已经被删除:%s',article.url)
            #print('该文章没有评论')
            return (commentList,False)
        for c in divs:
            pre_cid=c.attrs['id']
            cid=re.sub('\D','',pre_cid)#评论的id
            user_id=None
            name_div=c.find('div',attrs={'class':'info'})
            user_name=name_div.find('a').text #评论者的名字
            pre_time=name_div.find('div',attrs={'class':'meta'})
            publish_time=pre_time.find('span').text.strip()#评论的时间
            publish_time=publish_time[-16:]
            pre_content=c.find('div',attrs={'class':'comment-wrap'})
            content=pre_content.find('p').text
            reply_user_wrap=c.find('div',attrs={'class':'sub-comment-list'})
            if reply_user_wrap:
                pre_reply_user_id=reply_user_wrap.find('div',attrs={'class':'sub-comment'})
                reply_user_id=re.sub('\D','',pre_reply_user_id)
            else:
                reply_user_id=' '
            like_count = None
            reply_count = None
            location_coutry = 'CN'
            commentList.append(Comment(article.tid,self.channel.channel_id,
                                       cid,add_time,publish_time,None,
                                       location_coutry,None,None,user_id,
                                       user_name,content,reply_user_id,None,like_count,
                                       reply_count,dislike_count=None))
        return (commentList,False)  #测试的时候 article[0][222].content  可以取出第222条的评论内容

    def refreshCommentCrawler(self):
        '''
        重置评论爬取
        '''
        self.lastCommentId = None


    def getCsrf(self,keywordList,page):
        '''
        搜索中山大学时候，需要csrf
        :return:
        '''
        url_1 = 'https://www.jianshu.com/search?q='
        url_2='&page'+page+'&type=note'
        query = urllib.quote(' '.join(keywordList).encode('utf-8'))
        url=url_1+str(query)+url_2
        html = self.session.get(url, headers=self.headers, verify=False)
        soup = BeautifulSoup(html, 'html.parser')
        csrf = soup.find('meta', attrs={'name': 'csrf-token'}).attrs['content']
        return csrf

    def getAuthenticity(self):
        # 登录的时候需要authenticity_token
        url = 'https://www.jianshu.com/sign_in'
        html = self.session.get(url, headers=self.headers, verify=False)
        soup = BeautifulSoup(html,'html.parser')
        Aut = soup.find('input', attrs={'name': 'authenticity_token'}).attrs['value']
        return Aut

    def login(self, usename, password):
        use_name = usename
        pass_word = password
        aut =self.getAuthenticity()
        post_data = {
            'authenticity_token': aut,
            'session[email_or_mobile_number]': use_name,
            'session[password]': pass_word,
            'session[oversea]': 'false',
            'session[remember_me]': 'true',
        }
        post_url = 'https://www.jianshu.com/sessions'
        response = self.session.post(post_url, data=post_data, textRspOnly=False,headers=self.headers, verify=False)
        code = response.status_code
        if code == 201 or 200:
            self.session.cookies.save()
            return True
        else:
            return False

    def islogin(self):
        # 进入个人中心，看是否登录成功
        url = 'https://www.jianshu.com/u/663a3e4587dd'
        response = self.session.get(url, headers=self.headers, textRspOnly=False,verify=False)
        code = response.status_code
        if code == 200:
            return True
        else:
            return False