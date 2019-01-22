# -*- coding:utf-8 -*-
'''
Created on 28 Nov 2017

@author: eyaomai
'''

from com.naswork.sentiment.crawler.crawlerfactory import CrawlerFactory
from com.naswork.sentiment.common.constant import Constants
from com.naswork.sentiment.common.globalvar import GlobalVariable
from com.naswork.sentiment.common.conf import SAConfiguration
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.analysis.heatanalysis_back import HeatAnalytics
from com.naswork.sentiment.analysis.identificationanalysis import EntityIdentificationAnalytics, EventIdentificationAnalytics
from com.naswork.sentiment.analysis.warninganalysis import ArticleWarningAnalytics
from com.naswork.sentiment.common.utils import Logging
import json
import time, datetime
import copy

def encodeText(content):
    return content.replace('"','\\"').replace("%", "\%")

class SAEventCrawler(object):
    '''
    只负责按照event来爬取新文章
    '''
    def __init__(self, channel, dbProxy, logger=None):
        '''
        Constructor
        '''
        self.channel = channel
        self.dbProxy = dbProxy
        self.loggingPrefix = '[%s]' % self.channel.channel_name
        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger

    def __crawl(self, crawler, entityId, event):
        #获取事件关键字
        keywordList = event.external_keyword.split('|')
        #获取搜索范围
        saConf = GlobalVariable.getSAConfDict()['']
        endDateTime = datetime.datetime.now()
        #startDateTime = endDateTime - datetime.timedelta(days=crawlerSearchDays)
        self.logger.info('%s Params:\n\tEnd:%s\n\tRanges:%d\n\tKeywords:%s', 
                         self.loggingPrefix,
                         endDateTime.strftime('%Y-%m-%d %H:%M:%S'),
                         self.channel.search_ranges,
                         ','.join(keywordList))
        #爬取文章
        articleList = crawler.searchArticle(keywordList, endDateTime)#这里不仅获取新的文章 还会从数据库里找记录来获取未消亡的数据
        self.logger.info('%sTotally %d article crawled', self.loggingPrefix, len(articleList) )
        #分析文章热度
        ha = HeatAnalytics(self.dbProxy)
        for article in articleList:
            article.statistics.heat = ha.analysis(article, Constants.OBJECT_TYPE_ARTICLE)
        #写入全局文章表
        self.__updateToArticleTable(articleList, Constants.TABLE_SA_ARTICLE, hasMetaInfo=True)
        #写入全局文章历史表
        self.__updateToArticleHistoryTable(articleList, Constants.TABLE_SA_ARTICLE_HISTORY)
        self.dbProxy.commit()
        
        #写入实体文章表
        filteredArticle = self.__filterRemovedArticle(articleList, entityId)        
        if len(filteredArticle) > 0:
            #拆分新旧文章，进行敏感词预警分析
            (existingArticleList, newArticleList) = self.__seperateNewOldArticles(filteredArticle, entityId)
            if len(newArticleList) > 0:
                self.logger.info('%sSensitive words analysis for %d article for %s', self.loggingPrefix, len(newArticleList), entityId)
                ana = ArticleWarningAnalytics(self.dbProxy, entityId)
                ana.analysis(articleList=newArticleList, commit=False)

            self.__updateToArticleTable(filteredArticle,
                                    Constants.TABLE_SA_ARTICLE + Constants.TABLE_NAME_DELIMITER + entityId)
            self.__updateToArticleHistoryTable(filteredArticle,
                                    Constants.TABLE_SA_ARTICLE_HISTORY + Constants.TABLE_NAME_DELIMITER + entityId)
        
        #写入实体事件文章表
        filteredEventArticle = self.__filterRemovedArticle(articleList,
                                    entityId, event.event_id)
        if len(filteredEventArticle) > 0:
            self.__updateToArticleTable(filteredEventArticle,
                                        Constants.TABLE_SA_EVENT_ARTICLE + Constants.TABLE_NAME_DELIMITER + entityId,
                                        event.event_id)
            self.__updateToArticleHistoryTable(filteredEventArticle,
                                        Constants.TABLE_SA_EVENT_ARTICLE_HISTORY + Constants.TABLE_NAME_DELIMITER + entityId,
                                        event.event_id)
        self.dbProxy.commit()
    def __seperateNewOldArticles(self, articleList, entityId=None):
        '''
        查询全局文章表，区分新文章和旧文章
        '''
        if len(articleList)==0:
            return ([],[])
        if entityId is None:
            selectSql = 'select tid, channel_id from %s where ' % Constants.TABLE_SA_ARTICLE
        else:
            selectSql = 'select tid, channel_id from %s where ' % (Constants.TABLE_SA_ARTICLE + Constants.TABLE_NAME_DELIMITER + entityId)
        whereClauseList = map(lambda article: '(tid="%s" and channel_id=%d)'%( article.tid, article.channel_id), articleList)
        self.dbProxy.execute(selectSql + ' or '.join(whereClauseList))
        resultList = map(lambda x: Article(x[0], x[1]), self.dbProxy.fetchall())

        existingArticleList = filter(lambda x: x in resultList, articleList)
        newArticleList = filter(lambda x: x not in resultList, articleList)
        return (existingArticleList, newArticleList)

    def __filterRemovedArticle(self,articleList, entityId,eventId=None):
        '''
        与remove表格对比，进行文章过滤
        返回不存在remove表中的文章list
        '''
        if len(articleList)==0:
            return []
        if eventId is not None:
            tableName = Constants.TABLE_SA_EVENT_ARTICLE_REMOVE + Constants.TABLE_NAME_DELIMITER + entityId
            eventCondition = ' event_id=%d and ' % eventId
        else:
            tableName = Constants.TABLE_SA_ARTICLE_REMOVE + Constants.TABLE_NAME_DELIMITER + entityId
            eventCondition = ''
        # 在remove表里查找文章
        selectSql = '''
            SELECT TID, CHANNEL_ID FROM %s where %s (%s)
        '''
        whereClauseList = map(lambda article: '(TID="%s" and CHANNEL_ID=%d)' % (article.tid, article.channel_id),
                              articleList)

        self.dbProxy.execute(selectSql % (tableName, eventCondition, ' or '.join(whereClauseList)))
        resultList = self.dbProxy.fetchall()  # 查询返回结果集
        removedArticleList = map(lambda x: Article(x[0], x[1]), resultList)
        filteredArticle = filter(lambda x: x not in removedArticleList, articleList)
        #self.logger.debug('originalList:%s', map(lambda article: article.tid, articleList))
        #self.logger.debug('removedArticleList:%s', map(lambda article: article.tid, removedArticleList))
        #self.logger.debug('filteredArticle:%s', map(lambda article: article.tid, filteredArticle))
        return filteredArticle

    def __updateToArticleTable(self, articleList, tableName, eventId=None, hasMetaInfo=False):
        '''
        更新到文章表
        @param tableName: 全局文章表、实体文章表或者实体事件文章表
        @param eventId: 如果更新到实体事件文章表，则需要提供事件id，否则为None
        '''
        n = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        if eventId is None:
            eventIdFieldName = ''
            eventIdFieldValue = ''
        else:
            eventIdFieldName = 'EVENT_ID,'
            eventIdFieldValue = str(eventId)+','
        if hasMetaInfo:
            metaInfoFieldName = ',META_INFO'
            metaInfoOnUpdate = ',META_INFO=VALUES(META_INFO)'
        else:
            metaInfoFieldName = ''
            metaInfoOnUpdate = ''

        insertSql = '''
        INSERT INTO %s (TID, %s CHANNEL_ID, URL, ADD_DATETIME, PUBLISH_DATETIME, PUBLISH_METHOD,
            TITLE, AUTHOR_ID, AUTHOR_NAME, DIGEST, CONTENT, READ_COUNT,LIKE_COUNT, REPLY_COUNT,
            FORWARD_COUNT, COLLECT_COUNT, HEAT, UPDATE_DATETIME %s)
        VALUES %s 
        ON DUPLICATE KEY UPDATE READ_COUNT=VALUES(READ_COUNT), LIKE_COUNT=VALUES(LIKE_COUNT), 
        REPLY_COUNT = VALUES(REPLY_COUNT), FORWARD_COUNT=VALUES(FORWARD_COUNT), 
        COLLECT_COUNT = VALUES(COLLECT_COUNT), HEAT = VALUES(HEAT), UPDATE_DATETIME=VALUES(UPDATE_DATETIME)
        %s
        '''
        valueList = list()
        for article in articleList:
            statistics = article.statistics
            if hasMetaInfo:
                metaInfoFieldValue = ',"'+encodeText(article.meta_info)+'"' if article.meta_info is not None else ','+Constants.DEFAULT_STR
            else:
                metaInfoFieldValue = ''
            valueList.append('("%s", %s %d, "%s", "%s", "%s", "%s", "%s", "%s", "%s", "%s","%s", %s, %s, %s, %s, %s, %s, "%s" %s)' % (
                        article.tid,
                        eventIdFieldValue,
                        article.channel_id,
                        article.url,
                        n,
                        article.publish_datetime if article.publish_datetime is not None else Constants.DEFAULT_PUBLISH_DATETIME,
                        article.publish_method if article.publish_method is not None else Constants.DEFAULT_PUBLISH_METHOD,
                        encodeText(article.title),
                        article.author_id if article.author_id is not None else Constants.DEFAULT_AUTHOR_ID,
                        article.author_name if article.author_name is not None else Constants.DEFAULT_AUTHOR_NAME,
                        encodeText(article.digest) if article.digest is not None else Constants.DEFAULT_DIGEST,
                        encodeText(article.content),
                        statistics.read_count if statistics.read_count is not None else Constants.DEFAULT_NUM,
                        statistics.like_count if statistics.like_count is not None else Constants.DEFAULT_NUM,
                        statistics.reply_count if statistics.reply_count is not None else Constants.DEFAULT_NUM,
                        statistics.forward_count if statistics.forward_count is not None else Constants.DEFAULT_NUM,
                        statistics.collect_count if statistics.collect_count is not None else Constants.DEFAULT_NUM,
                        statistics.heat if statistics.heat is not None else Constants.DEFAULT_NUM,
                        n,
                        metaInfoFieldValue
                        ))
        if len(valueList)>0:
            self.dbProxy.execute(insertSql % (tableName, eventIdFieldName, metaInfoFieldName, ','.join(valueList), metaInfoOnUpdate))

    def __updateToArticleHistoryTable(self, articleList, tableName, eventId=None):
        '''
        更新到文章历史表
        @param tableName: 当前文章表：全局文章表、实体文章表或者实体事件文章表
        @param eventId: 如果更新到实体事件文章表，则需要提供事件id，否则为None
        '''
        # self.logger.error('history')
        n = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        if eventId is None:
            eventIdFieldName = ''
            eventIdFieldValue = ''
        else:
            eventIdFieldName = 'EVENT_ID,'
            eventIdFieldValue = str(eventId)+','
        insertSql = '''
        INSERT INTO %s (TID, %s CHANNEL_ID,
            READ_COUNT,LIKE_COUNT, REPLY_COUNT,
            FORWARD_COUNT, COLLECT_COUNT, HEAT, ADD_DATETIME)
        VALUES %s 
        '''
        valueList = list()
        for article in articleList:
            statistics = article.statistics
            valueList.append('("%s", %s %d, %s, %s, %s, %s, %s, %s, "%s")' % (
                        article.tid,
                        eventIdFieldValue,
                        article.channel_id,
                        statistics.read_count if statistics.read_count is not None else Constants.DEFAULT_NUM,
                        statistics.like_count if statistics.like_count is not None else Constants.DEFAULT_NUM,
                        statistics.reply_count if statistics.reply_count is not None else Constants.DEFAULT_NUM,
                        statistics.forward_count if statistics.forward_count is not None else Constants.DEFAULT_NUM,
                        statistics.collect_count if statistics.collect_count is not None else Constants.DEFAULT_NUM,
                        statistics.heat if statistics.heat is not None else Constants.DEFAULT_NUM,
                        n
                        ))
        if len(valueList)>0:
            self.dbProxy.execute(insertSql % (tableName, eventIdFieldName, ','.join(valueList)))

    def crawlArticle(self, entityId=None):
        #根据媒体获取爬虫实例
        crawler = CrawlerFactory.getCrawler(self.channel, self.logger)
        if crawler is None:
            self.logger.error('%sCralwer not yet implemented', self.loggingPrefix)
            return
        
        self.logger.info('%sBegin to crawl', self.loggingPrefix)
        #获取所有事件
        entityEventDict = GlobalVariable.getEventMgmt().entityEventDict
        for entity_id in entityEventDict:
            if entityId is not None:
                if entity_id == entityId:
                    eventDict = entityEventDict[entityId]
                    for eventId in eventDict:
                        if eventDict[eventId].external_crawlable == True:
                            self.__crawl(crawler, entityId, eventDict[eventId])
        
    