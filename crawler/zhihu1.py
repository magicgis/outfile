# -*- coding:utf-8 -*-
'''
Created on 13 Oct 2017

@author: eyaomai
'''
from com.naswork.sentiment.crawler.baidu import BaiduCrawler
from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
import re, json
from bs4 import BeautifulSoup
import datetime, time
import traceback
CRAWL_ARTICLE_HEADERS = {
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"en,zh;q=0.8,zh-CN;q=0.6",
"Connection":"keep-alive",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"}
CRAWL_COMMENT_HEADERS = {
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"authorization":"oauth c3cef7c66a1843f8b3a9e6a1e3160e20",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"en,zh;q=0.8,zh-CN;q=0.6",
"Connection":"keep-alive",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"}
COMMENT_URL_TEMPLATE = 'https://www.zhihu.com/api/v4/questions/%s/answers?sort_by=default&include=data#5B#2A#5D.is_normal#2Cadmin_closed_comment#2Creward_info#2Cis_collapsed#2Cannotation_action#2Cannotation_detail#2Ccollapse_reason#2Cis_sticky#2Ccollapsed_by#2Csuggest_edit#2Ccomment_count#2Ccan_comment#2Ccontent#2Ceditable_content#2Cvoteup_count#2Creshipment_settings#2Ccomment_permission#2Ccreated_time#2Cupdated_time#2Creview_info#2Cquestion#2Cexcerpt#2Crelationship.is_authorized#2Cis_author#2Cvoting#2Cis_thanked#2Cis_nothelp#2Cupvoted_followees#3Bdata#5B#2A#5D.mark_infos#5B#2A#5D.url#3Bdata#5B#2A#5D.author.follower_count#2Cbadge#5B#3F#28type#3Dbest_answerer#29#5D.topics&limit=%d&offset=%d'
COMMENT_PAGE_SIZE = 20

class ZhihuCrawler(object):
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
        
        self.baiduCrawler = BaiduCrawler(self.logger)
        self.session = SessionCrawler()
        self.channel = channel
        self.nextCommentUrl = None
    
    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        @param keywordList: 关键字数组
        @param endTime: 搜索时间范围结束
        '''
        startTime = endTime - datetime.timedelta(days=self.channel.search_ranges)
        startTimeIntSecond = time.mktime(startTime.timetuple())
        endTimeIntSecond = time.mktime(endTime.timetuple())
        urls = self.baiduCrawler.search(self.channel.url, keywordList, startTimeIntSecond, endTimeIntSecond)
        articleList = list()
        for baiduUrl in urls:
            url = self.__fetchRealUrlFromBaiduUrl(baiduUrl)
            article = self.crawlArticle(url)
            if article is not None and article not in articleList:
                #同一文章可能会在搜索结果出现多次，在baidu的结果url是不重复，但是实际可能指向同一文章，需要去重
                articleList.append(article)
        return articleList
    
    def __fetchRealUrlFromBaiduUrl(self, baiduUrl):
        '''
        '''
        response = self.session.session.get(baiduUrl, allow_redirects=False)
        if response.status_code == 302:
            return response.headers['Location']
        
    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        #判断url格式，因为从百度查询所得不一定是question，目前只爬question
        if url.find('question')<0:
            self.logger.warn('Question supported only:%s', url)
            return None
        article_id = re.findall(r'question/(\d+)', url)[0]
        self.session.randomSleep()
        response = self.session.get(url, headers=CRAWL_ARTICLE_HEADERS)
        soup = BeautifulSoup(response)
        main = soup.find('div', attrs={'id': "data"}).attrs['data-state']
        articleJson = json.loads(main)
        questionJson = articleJson['entities']['questions'][article_id]
        title = questionJson['title']
        contentSoup = BeautifulSoup(questionJson['editableDetail'])
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

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        articleCopy = self.crawlArticle(article.url)
        article.statistics.reply_count = articleCopy.statistics.reply_count
        article.statistics.read_count = articleCopy.statistics.read_count
        article.statistics.collect_count = articleCopy.statistics.collect_count

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
        根据文章，爬取文章的评论，返回评论列表
        @return: (commentList, hasnext)二元组，commentList是指评论数组（每个元素是Comment实例），hasnext表示是否还有要爬取
        '''
        if self.nextCommentUrl is None:
            curl = COMMENT_URL_TEMPLATE % (article.tid, COMMENT_PAGE_SIZE, 0)
            curl = curl.replace('#','%')
        else:
            curl = self.nextCommentUrl
        self.session.randomSleep()
        result = self.session.get(curl, headers = CRAWL_COMMENT_HEADERS)
        jo = json.loads(result)
        paging = jo['paging']
        hasnext = not paging['is_end']
        self.nextCommentUrl = paging['next']
        dataList = jo['data']
        add_datetime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')        
        commentList = list()
        for data in dataList:
            #self.logger.debug('[Zhihu]Comment data keys:%s', data.keys())
            #self.logger.debug('[ZHIHU]Comment url for %s:%s', article.title, data['url'])
            publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(data['updated_time']))
            comment = Comment(article.tid, article.channel_id, data['id'],
                 add_datetime, publish_datetime,
                 ip_address=None, location_country=None, location_region=None, location_city=None, 
                 author_id = data['author']['id'], author_name = data['author']['name'], content= data['content'], reply_author_id=None, 
                 read_count=None, like_count=data['voteup_count'], reply_count=data['comment_count'], dislike_count=None)
            commentList.append(comment)
        return (commentList, hasnext)