# -*- coding:utf-8 -*-
'''
Created on 8 Oct 2017

@author: eyaomai
'''

from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
import re,urllib
from bs4 import BeautifulSoup
import datetime, time


class DouBanCrawler(object):
    '''
    classdocs
    '''

    def __init__(self,channel,logger=None):
        '''
        Constructor
        '''

        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger
        self.session = SessionCrawler(sleepRange=[3, 8])
        self.channel = channel
        self.headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Cache-Control': 'max-age=0',
            'Connection': 'keep-alive',
            'Host': 'www.douban.com',
            'Upgrade-Insecure-Requests': '1'
        }

    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        @param keywordList: 关键字数组
        @param endTime: 搜索时间范围结束
        '''
        startTime = endTime - datetime.timedelta(hours=1)
        # startTime=datetime.datetime(2017,11,20,23)
        page = 0
        articleList = list()
        hasnext = True
        while hasnext:
            (articleListInPage, hasnext) = self.__searchByPage(keywordList, startTime, endTime, page)
            articleList.extend(articleListInPage)
            page += 50
        return articleList

    def __searchByPage(self,keywordList,startTime,endTime,page):
        # 在豆瓣内部搜索框搜索
        page = str(page) #url接收的是str格式
        search_url = "https://www.douban.com/group/search?start="+page+"&cat=1013&sort=time&q="
        #cat:按话题搜索  sort：按最新发布时间分类  q:搜索关键词
        query = urllib.quote(' '.join(keywordList).encode('utf-8'))
        search_url = search_url+str(query)

        url_page = self.session.get(search_url,headers=self.headers)
        soup = BeautifulSoup(url_page, "lxml")
        # self.logger.debug(soup)
        main_wrap = soup.find('div', attrs={'class': "article"})
        main_article_list = main_wrap.find('div',attrs={'class':"topics"})
        articleList = list()
        hasnext = True
        if main_article_list is not None:
            title_list = main_article_list.findAll('tr', {'class': 'pl'})
            for title in title_list:
                article_publishtime = title.find('td', attrs={'class': "td-time"}).attrs['title']

                urlTime = time.strptime(article_publishtime, "%Y-%m-%d %H:%M:%S")

                Y, M, D, H = urlTime[0:4]
                urlTime2 = datetime.datetime(Y, M, D, H)
                urlTime2 = time.mktime(urlTime2.timetuple())
                #转换成时间戳来比较 float类型
                startTime = endTime - datetime.timedelta(days=2)
                startTimeIntSecond = time.mktime(startTime.timetuple())
                endTimeIntSecond = time.mktime(endTime.timetuple())

                #如果符合时间范围就爬取
                if urlTime2 >= startTimeIntSecond and urlTime2 <= endTimeIntSecond:
                    article_url = title.find('a').attrs['href']
                    self.logger.debug(article_url)
                    (content, author_name, tid,like_count) = self.crawlArticle(article_url)
                    if content is None:  # 话题已被删除或则其他格式
                        continue

                    article_title = title.find('td', attrs={'class': "td-subject"}).text
                    article = Article(tid,  self.channel.channel_id, article_title, content, article_publishtime, article_url, None,
                                      author_name)
                    self.crawlComment(article)
                    reply_count = title.find('td', attrs={'class': "td-reply"}).text.strip()
                    reply_count = re.sub(u'回应', '', reply_count)  # 回复数量去除中文保留数字
                    article.statistics.reply_count = reply_count
                    article.statistics.like_count = like_count
                    if article not in articleList:
                        articleList.append(article)

                else:
                    print len(articleList)
                    hasnext=False
                    break

            return (articleList, hasnext)


    def crawlStatistics(self, article): #下次直接获得要统计的变量而不用爬整个网页
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        try:
            article_main = self.session.get(article.url, headers=self.headers)
            soup = BeautifulSoup(article_main, 'lxml')
            comment_list = soup.find('ul',attrs={'id':"comments"}) #如果还需要其他统计数可以继续添加
            comment_list_li = comment_list.findAll('li')
            article.statistics.reply_count = len(comment_list_li)
            #like_count页面
            url = article.url + '?type=like'
            article_main = self.session.get(url, headers=self.headers)
            soup = BeautifulSoup(article_main, 'lxml')
            main = soup.find('div', attrs={'class': "article"})
            lik_count_wrap = main.find('div', attrs={'class': "list topic-fav-list"})
            lik_count = lik_count_wrap.findAll('li')
            article.statistics.like_count = len(lik_count)
        except:
            self.logger.error(u'该话题已被删除或有其他格式')

    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        url = url+'?type=like'
        html = self.session.get(url,headers=self.headers)

        if html:
            print '111'
            soup = BeautifulSoup(html, 'lxml')  # 'html.parser' 解析器
            main = soup.find('div', attrs={'class': "article"})
            if main is not None:
                print '222'
                Tauthor = main.find('span', attrs={'class': "from"})
                if Tauthor is not None:
                    Tauthor = Tauthor.find('a').text.strip()
                else:
                    Tauthor = 'None'
                Tcontent = main.find('div', attrs={'class': "topic-content"}).text.strip()
                Tid_wrap = main.find('div', attrs={'class': "sns-bar"})
                Tid = Tid_wrap.find('a').attrs['data-tid']
                try:
                    lik_count_wrap = main.find('div',attrs={'class':"list topic-fav-list"})
                    lik_count = lik_count_wrap.findAll('li')
                    lik_count=len(lik_count)
                except:
                    lik_count=0
                return (Tcontent,Tauthor,Tid,lik_count)
            else:
                self.logger.error(u'该话题已被删除或存在其他格式')
                return (None,None,None,None)

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
        #self.logger.debug('Article:%s', article)

        html = self.session.get(article.url, headers=self.headers)
        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        commentList = list()
        main_comment = BeautifulSoup(html,'lxml')
        try:
            commentList_html = main_comment.find('ul',attrs={'id':"comments"})
            commentList_html_li = commentList_html.findAll('li')
        except:
            self.logger.error(u'该页面已被删除或则存在其他格式：%s',article.url)
            return (commentList, False)

        for comment in commentList_html_li:
            cid = comment.attrs['data-cid']
            comment_main_wrap = comment.find('div',attrs={'class':"reply-doc content"})
            user_id = comment_main_wrap.find('div',attrs={'class':"operation_div"}).attrs['id']
            user_info_wrap = comment_main_wrap.find('div',attrs={'class':"bg-img-green"})
            user_name = user_info_wrap.find('a').text.strip()
            publish_datetime = user_info_wrap.find('span').text.strip()
            content = comment_main_wrap.find('p').text.strip()
            reply_user_wrap = comment_main_wrap.find('div',attrs={'class':"reply-quote"})
            if reply_user_wrap:
                reply_userid_wrap=reply_user_wrap.find('span',attrs={'class':"pubdate"})
                reply_userid = reply_userid_wrap.find('a').attrs['href']
                reply_userid = re.sub(r'\D', "", reply_userid)
            else:
                reply_userid = ''
            # like_count_wrap = comment_main_wrap.find('div',attrs={'class':"operation_div"})
            # like_count = like_count_wrap.findAll('a')[1].text
            # like_count = re.sub(r'\D', "", like_count) #点赞数难获取不是页面自带的
            like_count = None
            reply_count = None
            location_coutry = 'CN'

            commentList.append(Comment(article.tid,  self.channel.channel_id, cid,
                                   add_datetime, publish_datetime,
                                    None,location_coutry,None,None,
                                   user_id, user_name, content, reply_userid,
                                   None, like_count, reply_count, dislike_count=None
                                   ))

        return (commentList, False)  #测试的时候 article[0][222].content  可以取出第222条的评论内容