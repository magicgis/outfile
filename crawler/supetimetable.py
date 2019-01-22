# -*- coding:utf-8 -*-
'''
Created on 8 Oct 2017

@author: eyaomai
'''

from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
import re, json
from bs4 import BeautifulSoup
import datetime, time
import simplejson
import requests


class SuperTimeTableCrawler(object):
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

        self.s = requests.Session()
        self.channel = channel
        self.headers = {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            'User-Agent': 'libcurl-agent/1.0',
            'Host': '120.55.151.61',
            'Connection': 'Keep-Alive',
            'Accept-Encoding': 'gzip',
            # 'Content-Length': '215',
        }
        self.req_data = 'timestamp=0&preMoodTimestap=0&phoneBrand=HONOR&platform=1&phoneVersion=24&channel=huaweiMarket&phoneModel=BLN-AL10&type=1&versionNumber=9.1.1&'
        self.articleList = list()
        self.startTime=0

    def __login(self):
        loginurl = 'http://120.55.151.61/V2/StudentSkip/loginCheckV4.action'
        # logindata = 'platform=1&password=3F8A553710091B1898ADDA5B8A2189DB&phoneBrand=HONOR&phoneVersion=24&account=4327829BB5EBB188F7DB0C7636CFB5FF&versionNumber=9.1.1&phoneModel=BLN-AL10&deviceCode=862119036064819&channel=huaweiMarket&'
        logindata='platform=1&password=0478007775799A64514E07C53C6A270A&phoneBrand=HONOR&phoneVersion=24&account=D12FD8F90E92A933E912B823C7FB5814&versionNumber=9.1.1&phoneModel=BLN-AL10&deviceCode=862119036064819&channel=huaweiMarket&'
        data = self.s.post(url=loginurl, data=logindata, headers=self.headers, stream=True, verify=False)

    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        @param keywordList: 关键字数组
        @param endTime: 搜索时间范围结束
        '''
        self.startTime = endTime - datetime.timedelta(days=self.channel.search_ranges)

        # self.startTime = int(time.mktime(self.startTime.timetuple()))
        # endTimeIntSecond = time.mktime(endTime.timetuple())

        self.__login()

        hasnext = True
        while hasnext:
            (res_data, hasnext) = self.crawlArticle(self.req_data)
            self.req_data=res_data
        return self.articleList

    def crawlArticle(self,req_data):

        hasnext=False

        req_url = 'http://120.55.151.61/Treehole/V4/Cave/getList.action'
        data_r = self.s.post(url=req_url, data=req_data, headers=self.headers)
        data_r = data_r.text

        true = True
        false = False

        data_j = eval(data_r)
        data_js = json.dumps(data_j)
        data_dict = simplejson.loads(data_js)

        data = data_dict['data']
        timestamp=data['timestampLong']
        timestampLong = data['timestampLong']/1000
        timestampLong_struct = datetime.datetime.fromtimestamp(timestampLong)
        timestampLong_str = timestampLong_struct.strftime('%Y-%m-%d %H:%M:%S')
        messageBO = data['messageBOs']

        for each in messageBO:  # 20条记录保存在一组里
            if 'studentBO' in each:
                content = each.get('content', False)
                if content :
                    publish_timeStamp = each['issueTime'] / 1000
                    datetime_struct = datetime.datetime.fromtimestamp(publish_timeStamp)
                    publish_time = datetime_struct.strftime('%Y-%m-%d %H:%M:%S')
                    if datetime.datetime.strptime(publish_time,'%Y-%m-%d %H:%M:%S')<self.startTime:
                        hasnext=False
                        break

                    schoolName=each['schoolName']
                    readCount=each['readCount']
                    relayedCount = each['relayedCount']
                    reply_count=each['comments']
                    content = each['content']
                    if len(content)>8:
                        title=content[:8]+'...'
                    elif len(content)<=0:
                        title = u'内容为图片'
                    else:
                        title = content

                    studentBO = each['studentBO']
                    studentBO_id=studentBO['studentId']
                    studentBO_name = studentBO['nickName']
                    likeCount = each['likeCount']
                    article_id = each['messageId']
                    publish_method=each['source']
                    publish_method = publish_method[publish_method.find('_')+1:]

                    data2 = 'platform=1&phoneBrand=HONOR&phoneVersion=24&versionNumber=9.1.1&phoneModel=BLN-AL10&sharePlatform=4&channel=huaweiMarket&plateId=1&messageId='+str(article_id)+'&'
                    article_urlPOST = 'http://120.55.151.61/Treehole/V4/Message/preShare.action'
                    data_urlPOST = self.s.post(url=article_urlPOST, data=data2, headers=self.headers)

                    data3 = data_urlPOST.text
                    data_j3 = eval(data3)
                    data_js3 = json.dumps(data_j3)
                    data_dict3 = simplejson.loads(data_js3)

                    data4 = data_dict3['data']

                    article_url =data4['shareUrl']

                    article = Article(article_id, self.channel.channel_id, title, content, publish_time, article_url,
                                      author_id=studentBO_id, author_name=studentBO_name,publish_method=publish_method, entity=schoolName)
                    article.statistics.reply_count = reply_count
                    article.statistics.like_count = likeCount
                    article.statistics.read_count = readCount
                    article.statistics.forward_count = relayedCount
                    # print article.__str__()

                    if article is not None and article not in self.articleList:
                        self.articleList.append(article)

        # print datetime.datetime.strptime(timestampLong_str,'%Y-%m-%d %H:%M:%S')
        # print '1111111'
        if datetime.datetime.strptime(timestampLong_str,'%Y-%m-%d %H:%M:%S') < self.startTime:
            hasnext=False
            return ('',hasnext)
        else:
            hasnext = True
            res_data = 'timestamp=' + str(
                timestamp) + '&preMoodTimestap=' + str(
                timestamp) + '&platform=1&phoneBrand=HONOR&phoneVersion=24&versionNumber=9.1.1&phoneModel=BLN-AL10&type=1&channel=huaweiMarket&'
            return (res_data,hasnext)


    def crawlStatistics(self, article): #下次直接获得要统计的变量而不用爬整个网页
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        into_url = 'http://120.55.151.61/Treehole/V4/Message/getDetail.action'
        data = 'platform=1&phoneBrand=HONOR&phoneVersion=24&versionNumber=9.2.0&phoneModel=BLN-AL10&channel=huaweiMarket&plateId=1&messageId=' + str(
            article.tid) + '&'
        data_detal = self.s.post(url=into_url, data=data, headers=self.headers)
        data0 = data_detal.text
        true = True
        false = False
        data_j0 = eval(data0)
        data_js0 = json.dumps(data_j0)
        data_dict0 = simplejson.loads(data_js0)
        try:
            comment_list = data_dict0['data']['commentListBO']['commentBOs']
            article.statistics.reply_count=len(comment_list)
        except:
            self.logger.warning(u"can't find commentBOs")


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
        commentList=list()
        comment_url='http://120.55.151.61/Treehole/V4/Message/getDetail.action'
        data='platform=1&phoneBrand=HONOR&phoneVersion=24&versionNumber=9.2.0&phoneModel=BLN-AL10&channel=huaweiMarket&plateId=1&messageId='+str(article.tid)+'&'
        data_comment=self.s.post(url=comment_url,data=data,headers=self.headers)
        data0 = data_comment.text
        true = True
        false = False
        data_j0 = eval(data0)
        data_js0 = json.dumps(data_j0)
        data_dict0 = simplejson.loads(data_js0)
        try:
            comment_list = data_dict0['data']['commentListBO']['commentBOs']
        except:
            return (commentList, False)
        if len(comment_list)==0:
            return (commentList, False)

        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        for comment in comment_list:
            cid = comment['commentId']
            commentTimeStamp = comment['commentTime']/1000
            datetime_struct = datetime.datetime.fromtimestamp(commentTimeStamp)
            commentTime = datetime_struct.strftime('%Y-%m-%d %H:%M:%S')
            commentContent=comment['content']
            user_id = comment['student']['id']
            user_name = comment['student']['nickName']

            location_coutry = 'CN'

            commentList.append(Comment(article.tid, self.channel.channel_id, cid,
                                       add_datetime, commentTime,
                                       None, location_coutry, None, None,  ###这里的ip_address还未实现
                                       user_id, user_name, commentContent, None,
                                       None, None, None, dislike_count=None
                                       ))

        return (commentList,False)
