# -*- coding:utf-8 -*-
'''
Created on 8 Oct 2017

@author: eyaomai
'''

from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
import re,urllib,json
from bs4 import BeautifulSoup
import datetime, time
import jieba


class SinanewsBlogCrawler(object):
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
        self.articleList = list()
        self.headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Cache-Control': 'max-age=0',
            'Connection': 'keep-alive',
            'Host': 'search.sina.com.cn',
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

        hasnext = True
        while hasnext:
            hasnext = self.__searchByPage(keywordList, startTime, endTime, page)
            page += 1
            self.logger.error(u'articlelength:%d',len(self.articleList))
        return self.articleList


    def __searchByPage(self,keywordList,startTime,endTime,page):
        # 在豆瓣内部搜索框搜索
        page = str(page) #url接收的是str格式
        query = urllib.quote(' '.join(keywordList).encode('utf-8'))
        params = {
            'c': 'blog',
            'range': 'article',  # 包含全部
            'by':'all',
            'sort':'time',
            'col':'',
            'source':'',
            'from':'',
            'country':'',
            'size':'',
            'time':'',
            'a':'',
            'isown':'false',
            'page':page,
            'dpc':'',
            'q':query
        }
        search_url = "http://search.sina.com.cn/"

        url_page = self.session.get(search_url,params=params,headers=self.headers)
        soup = BeautifulSoup(url_page, "lxml")
        # self.logger.debug(soup)
        main_wrap = soup.find('div', attrs={'class': "result-boxes"})

        if main_wrap is None: #为了防止因网络问题而导致的查找不到
            self.logger.debug(u'第一次查找没有结果再找一遍中')
            url_page = self.session.get(search_url, params=params, headers=self.headers)
            soup = BeautifulSoup(url_page, "lxml")
            main_wrap = soup.find('div', attrs={'class': "result-boxes"})
            if main_wrap is None:  #再找一遍如果还是没有才可以算没有结果
                self.logger.debug(u'第二次查找没有结果，该关键词没有结果')
                return (list(), False)

        main_article_list = main_wrap.findAll('div',attrs={'class':"box-result clearfix"})

        hasnext = True
        if main_article_list is not None:
            print '1'
            for title in main_article_list:
                print '2'
                article_publishtime_wrap = title.findAll('p')[1]
                article_publishtime = article_publishtime_wrap.find('span', attrs={'class': "fgray_time"}).text.strip()
                self.logger.error(article_publishtime)
                urlTime = time.strptime(article_publishtime, "%Y-%m-%d %H:%M:%S")

                Y, M, D, H = urlTime[0:4]
                urlTime2 = datetime.datetime(Y, M, D, H)
                urlTime2 = time.mktime(urlTime2.timetuple())
                #转换成时间戳来比较 float类型
                startTime = endTime - datetime.timedelta(days=5)
                startTimeIntSecond = time.mktime(startTime.timetuple())
                endTimeIntSecond = time.mktime(endTime.timetuple())

                #如果符合时间范围就爬取
                if urlTime2 >= startTimeIntSecond and urlTime2 <= endTimeIntSecond:
                    print '3'
                    title_wrap = title.find('h2')
                    article_url_parent = title_wrap.find('a').attrs['href']
                    article_url_index = article_url_parent.rfind('/')
                    article_url_child = article_url_parent[article_url_index+1:]
                    article_url = 'http://blog.sina.com.cn/s/blog_'+article_url_child+'.html'
                    Ttitle = title_wrap.find('a').text.strip()
                    self.logger.debug(article_url)
                    meta_info = article_url_child
                    (content,reply_count,like_count,read_count,collect_count,forward_count) = self.crawlArticle(article_url,meta_info)
                    self.logger.debug(like_count)

                    if content is None:  # 话题已被删除或则其他格式
                        print '756236'
                        continue
                    tid = 't_'+article_url_child
                    author_name = article_publishtime_wrap.find('a',attrs={'class':"rib-author"}).text
                    article = Article(tid,self.channel.channel_id,Ttitle, content, article_publishtime, article_url, None,
                                      author_name,meta_info=meta_info)

                    # self.crawlComment(article)

                    article.statistics.reply_count = reply_count
                    article.statistics.like_count = like_count
                    article.statistics.read_count = read_count
                    article.statistics.collect_count = collect_count
                    article.statistics.forward_count = forward_count
                    # self.logger.debug(article)
                    if article not in self.articleList:
                        self.articleList.append(article)

                else:
                    print '78956'
                    print len(self.articleList)
                    hasnext=False
                    break


            return hasnext


    def crawlStatistics(self, article): #下次直接获得要统计的变量而不用爬整个网页
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        headers2 = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Cache-Control': 'max-age=0',
            'Connection': 'keep-alive',
            'Host': 'comet.blog.sina.com.cn',
            'Upgrade-Insecure-Requests': '1'
        }
        try:
            # 统计数据的请求
            parmas = {
                'maintype': 'num',
                'aids': article.meta_info[-6:],
                'uid': article.meta_info[:-8]
            }
            data_url = 'http://comet.blog.sina.com.cn/api'
            data = self.session.get(data_url, params=parmas, headers=headers2)
            data2 = json.loads(data[data.find(':') + 1:-3])
            like_count = data2['d']
            read_count = data2['r']
            reply_count = data2['c']
            collect_count = data2['f']
            forward_count = data2['z']
            article.statistics.reply_count = reply_count
            article.statistics.like_count = like_count
            article.statistics.read_count = read_count
            article.statistics.collect_count = collect_count
            article.statistics.forward_count = forward_count
        except:
            self.logger.error(u'该话题已被删除或有其他格式')

    def crawlArticle(self, url,meta_info):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Cache-Control': 'max-age=0',
            'Connection': 'keep-alive',
            'Host': 'blog.sina.com.cn',
            'Upgrade-Insecure-Requests': '1'
        }
        headers2 = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.8',
            'Cache-Control': 'max-age=0',
            'Connection': 'keep-alive',
            'Host': 'comet.blog.sina.com.cn',
            'Upgrade-Insecure-Requests': '1'
        }

        html = self.session.get(url,headers=headers)

        if html:
            print '111'
            soup = BeautifulSoup(html, 'lxml')  # 'html.parser' 解析器
            main = soup.find('div', attrs={'id': "articlebody"})
            if main is not None:
                print '222'
                Tcontent = main.find('div', attrs={'id': "sina_keyword_ad_area2"}).text.strip()
                #统计数据的请求
                parmas={
                    'maintype':'num',
                    'aids':meta_info[-6:],
                    'uid':meta_info[:-8]
                }
                data_url = 'http://comet.blog.sina.com.cn/api'
                data = self.session.get(data_url, params=parmas, headers=headers2)
                data2 = json.loads(data[data.find(':')+1:-3])
                like_count = data2['d']
                read_count = data2['r']
                reply_count = data2['c']
                collect_count = data2['f']
                forward_count = data2['z']
                return (Tcontent,reply_count,like_count,read_count,collect_count,forward_count)
            else:
                self.logger.error(u'该话题已被删除或存在其他格式')
                return (None,None,None,None,None,None)

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
        comment_url= 'http://comment5.news.sina.com.cn/page/info?channel=blog&newsid='
        comment_url =comment_url+article.meta_info
        self.logger.error(comment_url)
        html = self.session.get(comment_url)
        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        commentList = list()
        main_comment = json.loads(html)['result']['cmntlist']
        print '8989'
        if len(main_comment) == 0:
            print '12212'
            return (commentList, False)

        for comment in main_comment:
            cid = comment['mid']
            publish_datetime = comment['time']
            user_id = comment['uid']
            user_name = comment['nick']
            content = comment['content']
            location = ','.join(jieba.cut(comment['area']))
            location_region = location[:location.find(',')]
            location_city = location[location.find(',')+1:]
            print location_city
            location_coutry = 'CN'

            commentList.append(Comment(article.tid,self.channel.channel_id, cid,
                                   add_datetime, publish_datetime,
                                    None,location_coutry,location_region,location_city,
                                   user_id, user_name, content, None,
                                   None, None, None, dislike_count=None
                                   ))

        return (commentList, False)  #测试的时候 article[0][222].content  可以取出第222条的评论内容