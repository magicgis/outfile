# -*- coding:utf-8 -*-
'''
Created on 5 Oct 2017

@author: eyaomai
'''
from com.naswork.sentiment.crawler.crawlerfactory import CrawlerFactory
from com.naswork.sentiment.common.constant import Constants
from com.naswork.sentiment.common.globalvar import GlobalVariable
from com.naswork.sentiment.common.conf import SAConfiguration
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.analysis.heatanalysis import HeatAnalytics
from com.naswork.sentiment.analysis.identificationanalysis import EntityIdentificationAnalytics, EventIdentificationAnalytics
from com.naswork.sentiment.analysis.warninganalysis import ArticleWarningAnalytics
from com.naswork.sentiment.analysis.emotionanalysis import EmotionAnalysis
from com.naswork.sentiment.common.utils import Logging
from com.naswork.sentiment.analysis.eventidentificationanalysis import SingleEventBackTrack

import json
import time, datetime
import copy
import re,random,math


def encodeText(content): #?
    return content.replace('"','\\"').replace("%", "\%")
    #return content.replace("'", "''").replace("%", "\%").replace(":", "\:")


class SACrawler(object):
    '''
    classdocs
    '''

    def __init__(self, channel, dbProxy, logger=None):
        '''
        Constructor
        '''
        self.keywordList2 = list()   # 用来过滤不包含关键词的文章
        self.nonekyewordList = list()  # 用来过滤含有反关键词的文章
        self.channel = channel
        self.dbProxy = dbProxy
        self.loggingPrefix = '[%s]' % self.channel.channel_name
        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger
    
    def crawlArticle(self, entityId= None):
        # 根据媒体获取爬虫实例
        crawler = CrawlerFactory.getCrawler(self.channel, self.logger)
        if crawler is None:
            self.logger.error('%sCralwer not yet implemented', self.loggingPrefix)
            return
        
        self.logger.info('%sBegin to crawl', self.loggingPrefix)
        # 获取所有待爬取实体的关键字
        entityDict = GlobalVariable.getEntityMgmt().entityDict
        keywordList = list()
        for entity in entityDict.values():
            if entityId is not None:
                if entity.entity_id == entityId:
                    keywordList.extend(entity.keyword_list.split('|'))
                    self.keywordList2.extend(entity.internal_keyword_list.split('|'))
                    self.nonekyewordList.extend(entity.internal_none_keyword_list.split('|'))
                    break
            else:
                keywordList.extend(entity.keyword_list.split('|'))
                self.keywordList2.extend(entity.internal_keyword_list.split('|'))
                self.nonekyewordList.extend(entity.internal_none_keyword_list.split('|'))

        if len(keywordList) == 0:
            self.logger.error('No keyword')
            return
        # 获取搜索范围
        saConf = GlobalVariable.getSAConfDict()['']
        endDateTime = datetime.datetime.now()

        self.logger.info('%s Params:\n\tEnd:%s\n\tRanges:%d\n\tKeywords:%s', 
                         self.loggingPrefix,
                         endDateTime.strftime('%Y-%m-%d %H:%M:%S'),
                         self.channel.search_ranges,
                         ','.join(keywordList))
        # 爬取文章
        crawler.entityId = entityId
        # 这里不仅获取新的文章 还会从数据库里找记录来获取未消亡的数据
        articleList = crawler.searchArticle(keywordList, endDateTime)
        # articleList=list(set(articleList))
        # 所以无法判断空然后停止所有
        self.logger.info('%sTotally %d article crawled', self.loggingPrefix, len(articleList) )
        # 分析文章热度
        ha = HeatAnalytics(self.dbProxy)
        ea = EmotionAnalysis(self.dbProxy)
        for article in articleList:
            article.classified_nature = ea.analysisSingleArticle(article.content)  # article_nature 返回数值~
            article.statistics.heat = ha.analysis(article, Constants.OBJECT_TYPE_ARTICLE)  # 返回热度算法值

        # 写入全局文章表
        self.__updateToArticleTable(articleList, Constants.TABLE_SA_ARTICLE, hasMetaInfo=True)
        # 写入全局文章历史表
        self.__updateToArticleHistoryTable(articleList, Constants.TABLE_SA_ARTICLE_HISTORY)
        self.dbProxy.commit()
        # 分析实体和事件并写入数据库
        self.__identifyArticle(articleList,entityId)
        self.dbProxy.commit()

        # 获取最近一批文章
        oldArticleList = self.__fetchOldArticleList(articleList)
        self.logger.info('%s Totally %d old article found', self.loggingPrefix, len(oldArticleList))
        # 重新获取统计信息
        statisticsArticleList = list()
        for article in oldArticleList:
            if crawler.crawlStatistics(article) is not False:                
                article.statistics.heat = ha.analysis(article, Constants.OBJECT_TYPE_ARTICLE)
                statisticsArticleList.append(article)
        # 更新旧文章到相关文章表
        self.__updateOldArticleToArticleTable(statisticsArticleList, Constants.TABLE_SA_ARTICLE)
        self.__updateOldArticleToArticleHistoryTable(statisticsArticleList, 
                                                     Constants.TABLE_SA_ARTICLE,
                                                     Constants.TABLE_SA_ARTICLE_HISTORY)
        for entityId in entityDict.keys(): 
            self.__updateOldArticleToArticleTable(oldArticleList, Constants.TABLE_SA_ARTICLE + Constants.TABLE_NAME_DELIMITER + entityId)
            self.__updateOldArticleToArticleTable(oldArticleList, Constants.TABLE_SA_EVENT_ARTICLE + Constants.TABLE_NAME_DELIMITER + entityId, True)
            self.__updateOldArticleToArticleHistoryTable(oldArticleList,
                                                         Constants.TABLE_SA_ARTICLE + Constants.TABLE_NAME_DELIMITER + entityId, 
                                                         Constants.TABLE_SA_ARTICLE_HISTORY + Constants.TABLE_NAME_DELIMITER + entityId)
            self.__updateOldArticleToArticleHistoryTable(oldArticleList,
                                                         Constants.TABLE_SA_EVENT_ARTICLE + Constants.TABLE_NAME_DELIMITER + entityId, 
                                                         Constants.TABLE_SA_EVENT_ARTICLE_HISTORY + Constants.TABLE_NAME_DELIMITER + entityId, True)
        # 获取所有文章，重新爬取评论
        allArticleList = self.__fetchOldArticleList(list())
        self.logger.info('%sStart to Crawl Comment for %d articles', self.loggingPrefix, len(allArticleList))
        for article in allArticleList:
            hasnext = True
            crawler.refreshCommentCrawler()
            commentNum = 0
            while hasnext:
                try:
                    (commentList, hasnext) = crawler.crawlComment(article)
                    commentNum+=len(commentList)
                    self.__updateToCommentTable(commentList)
                except Exception, e:
                    self.logger.debug(e)
            self.logger.info('%sTotally %d comments for article (%s):%s', 
                             self.loggingPrefix, commentNum, article.tid, article.title)

        # 提交数据
        self.dbProxy.commit()
    
    def __updateToCommentTable(self, commentList):
        insertSql = '''
        INSERT INTO %s (TID, CHANNEL_ID, CID,ADD_DATETIME, PUBLISH_DATETIME,
                        IP_ADDRESS, LOCATION_COUNTRY, LOCATION_REGION, LOCATION_CITY, 
                        AUTHOR_ID, AUTHOR_NAME, CONTENT, REPLY_AUTHOR_ID, 
                        READ_COUNT, LIKE_COUNT, REPLY_COUNT, DISLIKE_COUNT) VALUES %s
        ON DUPLICATE KEY UPDATE READ_COUNT=VALUES(READ_COUNT), LIKE_COUNT=VALUES(LIKE_COUNT),
                        REPLY_COUNT=VALUES(REPLY_COUNT), DISLIKE_COUNT=VALUES(DISLIKE_COUNT)
        '''
        valueList = list()
        for comment in commentList:
            valueList.append('("%s", %d, "%s","%s", "%s","%s", "%s", "%s", "%s","%s", "%s", "%s", "%s", %s, %s, %s, %s)\n' % (
                                    comment.tid, 
                                    self.channel.channel_id, 
                                    comment.cid, 
                                    comment.add_datetime, 
                                    comment.publish_datetime,
                                    comment.ip_address, 
                                    comment.location_country, 
                                    comment.location_region, 
                                    comment.location_city,
                                    comment.author_id, 
                                    encodeText(comment.author_name), 
                                    encodeText(comment.content), 
                                    comment.reply_author_id,
                                    comment.read_count if comment.read_count is not None else Constants.DEFAULT_NUM,
                                    comment.like_count if comment.like_count is not None else Constants.DEFAULT_NUM,
                                    comment.reply_count if comment.reply_count is not None else Constants.DEFAULT_NUM,
                                    comment.dislike_count if comment.dislike_count is not None else Constants.DEFAULT_NUM
                            ))
        if len(valueList) > 0:            
            self.dbProxy.execute(insertSql % (Constants.TABLE_SA_COMMENT, ','.join(valueList)))
            self.dbProxy.commit()

    #  对应获取旧文章来进行分析
    def __fetchOldArticleList(self, articleList, articleCount=100):
        '''
        从全局文章表，获取尚未消亡的文章id，而且这些文章并不在本次爬虫爬回来的记录里
        '''
        #用来查询总页数
        selectSql_count = 'SELECT COUNT(*) FROM %s where extinct="N" and channel_id=%d '
        sql2 = selectSql_count % (Constants.TABLE_SA_ARTICLE,
                           self.channel.channel_id)
        #获取旧文章的sql
        selectSql = 'SELECT TID,title, publish_datetime,url, meta_info FROM %s where extinct="N" and channel_id=%d '
        sql = selectSql % (Constants.TABLE_SA_ARTICLE,
                           self.channel.channel_id)

        if len(articleList) > 0:
            whereClauseList = map(lambda article: ' tid<>"%s" '%(article.tid), articleList)
            sql += ' and (%s)' % (' and '.join(whereClauseList))
            sql2 += ' and (%s)' % (' and '.join(whereClauseList))

        sql2 += ' order by add_datetime desc'

        self.dbProxy.execute(sql2)
        resultList2 = self.dbProxy.fetchall()
        # print '12456789sssssssssssssssssss'
        # print resultList2 #((53,),)
        resultList2 = re.findall(r'\d+', str(resultList2)) #返回一个list
        # print resultList2[0]
        if int(resultList2[0]) > int(articleCount):
            randpage = random.randint(0,int(math.ceil(float(resultList2[0])/articleCount)))
        else:
            randpage = 0   #用来随机取数据库页数

        sql += ' order by add_datetime desc limit %d,%d' % (randpage, articleCount)
        self.dbProxy.execute(sql)
        resultList = self.dbProxy.fetchall()

        return map(lambda item: Article(item[0], self.channel.channel_id, 
                                        title=item[1], publish_datetime=item[2], url= item[3], meta_info=item[4]), resultList)
            
    def __identifyArticle(self, articleList, predefinedEntityId = None):
        '''
        标识文章所属实体以及使实体内的事件。一篇文章可以属于多个实体，也可以属于同一个实体的多个事件
        '''
        entityIA = EntityIdentificationAnalytics(self.dbProxy, '')
        allEntityIdList = GlobalVariable.getEntityMgmt().entityDict.keys()
        eventIADict = dict()
        for entityId in allEntityIdList: 
            eventIADict[entityId] = EventIdentificationAnalytics(self.dbProxy, entityId)
        
        entityEventArticleDict = dict()
        for article in articleList:
            # 定位实体
            # entityIdList = entityIA.analysis(article)
            if predefinedEntityId is not None:
                entityIdList = [predefinedEntityId]
            else:
                entityIdList = entityIA.analysis(article)
            if len(entityIdList) > 0:
                for entityId in entityIdList:
                    if entityId not in entityEventArticleDict:
                        entityEventArticleDict[entityId] = {'articleList':list(), 'eventArticleDict':dict()}
                    entityEventArticleDict[entityId]['articleList'].append(article)
                    # 根据实体，定位事件
                    eventIdList = eventIADict[entityId].analysis(article)
                    self.logger.debug('%sArticle %s belongs to entity(%s) and eventlist:%s', self.loggingPrefix, article.tid, entityId, eventIdList )
                    if len(eventIdList) > 0:
                        eventArticleDict = entityEventArticleDict[entityId]['eventArticleDict']
                        for eventId in eventIdList:
                            if eventId not in eventArticleDict:
                                eventArticleDict[eventId] = list()
                            eventArticleDict[eventId].append(article)
        
        #更新数据库
        for entityId in entityEventArticleDict:

            filteredArticle = self.__filterRemovedArticle(entityEventArticleDict[entityId]['articleList'], entityId)
            #filteredArticle = entityEventArticleDict[entityId]['articleList']
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

            for eventId in entityEventArticleDict[entityId]['eventArticleDict']:

                # filteredEventArticle = entityEventArticleDict[entityId]['eventArticleDict'][eventId]
                filteredEventArticle = self.__filterRemovedArticle(entityEventArticleDict[entityId]['eventArticleDict'][eventId],
                                            entityId, eventId)

                # 将符合某事件的文章插入到事件文章表
                if len(filteredEventArticle) > 0:
                    self.__updateToArticleTable(filteredEventArticle,
                                                Constants.TABLE_SA_EVENT_ARTICLE + Constants.TABLE_NAME_DELIMITER + entityId,
                                                eventId)
                    self.__updateToArticleHistoryTable(filteredEventArticle,
                                                Constants.TABLE_SA_EVENT_ARTICLE_HISTORY + Constants.TABLE_NAME_DELIMITER + entityId,
                                                eventId)

                    # 插入文章表后进行分析
                    back_track_analyse = SingleEventBackTrack(self.dbProxy, entityId, eventId)
                    back_track_analyse.analysis(False)

    def __fetchEventTime(self, entity_id, event_id):
        '''
        获取事件回溯的起始时间和结束时间
        :param event:
        :return:
        '''
        selectSql = '''
            SELECT start_datetime, end_datetime from %s where event_id=%d 
        '''
        tableName = Constants.TABLE_SA_EVENT + Constants.TABLE_NAME_DELIMITER + entity_id

        sql = selectSql % (tableName, event_id)
        self.dbProxy.execute(sql)
        result = self.dbProxy.fetchall()

        return (result[0][0],
                result[0][1])

    def __filterRemovedArticle(self, articleList, entityId, eventId=None):
        '''
        与remove表格对比，进行文章过滤
        返回不存在remove表中的文章list
        '''
        if len(articleList)==0:
            return []
        if eventId is not None:
            tableName = Constants.TABLE_SA_EVENT_ARTICLE_REMOVE + Constants.TABLE_NAME_DELIMITER + entityId
            eventCondition = ' event_id=%d and ' % eventId

            start_datetime, end_datetime = self.__fetchEventTime(entityId, eventId)

            # 过滤掉不在该事件开始时间和结束之间内的文章
            article_new_list = list()
            for article in articleList:
                if (str(article.publish_datetime) > str(start_datetime)) and (str(article.publish_datetime) < str(end_datetime)):
                    article_new_list.append(article)

            articleList = article_new_list

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

        return filteredArticle

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
    
    def __updateToArticleTable(self, articleList, tableName, eventId=None, hasMetaInfo=False):
        '''
        更新到文章表
        @param tableName: 全局文章表、实体文章表或者实体事件文章表
        @param eventId: 如果更新到实体事件文章表，则需要提供事件id，否则为None
        '''
        n = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        period = datetime.datetime.now().strftime('%Y%m%d%H')
        valueList = list()

        if hasMetaInfo:
            metaInfoFieldName = ',META_INFO'
            metaInfoOnUpdate = ',META_INFO=VALUES(META_INFO)'
        else:
            metaInfoFieldName = ''
            metaInfoOnUpdate = ''

        # 文章是否写入事件
        if eventId is None:
            eventIdFieldName = ''
            eventIdFieldValue = ''
            insertSql = '''
                INSERT INTO %s (TID, %s CHANNEL_ID, URL, ADD_DATETIME, PUBLISH_DATETIME, PUBLISH_METHOD,
                    TITLE, AUTHOR_ID, AUTHOR_NAME, DIGEST, CONTENT, READ_COUNT,LIKE_COUNT, REPLY_COUNT,
                    FORWARD_COUNT, COLLECT_COUNT, CLASSIFIED_NATURE, HEAT, UPDATE_DATETIME %s)
                VALUES %s 
                ON DUPLICATE KEY UPDATE READ_COUNT=VALUES(READ_COUNT), LIKE_COUNT=VALUES(LIKE_COUNT), 
                REPLY_COUNT = VALUES(REPLY_COUNT), FORWARD_COUNT=VALUES(FORWARD_COUNT), 
                COLLECT_COUNT = VALUES(COLLECT_COUNT), HEAT = VALUES(HEAT), UPDATE_DATETIME=VALUES(UPDATE_DATETIME), 
                CLASSIFIED_NATURE=VALUES(CLASSIFIED_NATURE)
                %s
            '''
            for article in articleList:
                temp_1 = 0
                temp_2 = 0
                # 先判断是否符合 internal_keyword_list 里的值
                for keyword in self.keywordList2:
                    if keyword in article.title or keyword in article.content:
                        temp_1 = 1  # internal_keyword_list的关键词存在
                        break

                if temp_1 == 0:  # 如果不符合internal_keyword_list关键词就不写入数据库
                    self.logger.info('continue')
                    continue

                for nonekeyword in self.nonekyewordList:
                    if nonekeyword in article.title or nonekeyword in article.content:
                        self.logger.debug('\tNone Keyword is <%s>' % nonekeyword)
                        self.logger.debug('\tNone Keyword article url is %s' % article.url)
                        temp_2 = 1  # internal_none_keyword_list的反关键词存在
                        break

                if temp_2 == 1:  # 如果符合internal_none_keyword_list关键词就不写入数据库
                    continue
                statistics = article.statistics
                if hasMetaInfo:
                    metaInfoFieldValue = ',"' + encodeText(
                        article.meta_info) + '"' if article.meta_info is not None else ',' + Constants.DEFAULT_STR
                else:
                    metaInfoFieldValue = ''

                value_str = '''("%s", %s %d, "%s", "%s", "%s", "%s", "%s", "%s", "%s", "%s",
                "%s", %s, %s, %s, %s, %s, %d, %s, "%s" %s)'''
                valueList.append(value_str % (
                        article.tid, eventIdFieldValue, article.channel_id, article.url, n,
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
                        article.classified_nature,
                        statistics.heat if statistics.heat is not None else Constants.DEFAULT_NUM,
                        n,
                        metaInfoFieldValue
                    ))
        else:
            eventIdFieldName = 'EVENT_ID,'
            eventIdFieldValue = str(eventId)+','

            insertSql = '''
                INSERT INTO %s (TID, %s CHANNEL_ID, URL, ADD_DATETIME, PERIOD, PUBLISH_DATETIME, PUBLISH_METHOD,
                    TITLE, AUTHOR_ID, AUTHOR_NAME, DIGEST, CONTENT, READ_COUNT,LIKE_COUNT, REPLY_COUNT,
                    FORWARD_COUNT, COLLECT_COUNT, CLASSIFIED_NATURE, HEAT, UPDATE_DATETIME %s)
                VALUES %s 
                ON DUPLICATE KEY UPDATE READ_COUNT=VALUES(READ_COUNT), LIKE_COUNT=VALUES(LIKE_COUNT), 
                REPLY_COUNT=VALUES(REPLY_COUNT), FORWARD_COUNT=VALUES(FORWARD_COUNT), 
                COLLECT_COUNT=VALUES(COLLECT_COUNT), HEAT=VALUES(HEAT), UPDATE_DATETIME=VALUES(UPDATE_DATETIME), 
                CLASSIFIED_NATURE=VALUES(CLASSIFIED_NATURE)
                %s
            '''
            for article in articleList:
                temp_1 = 0
                temp_2 = 0
                # 先判断是否符合 internal_keyword_list 里的值
                for keyword in self.keywordList2:
                    if keyword in article.title or keyword in article.content:
                        temp_1 = 1  # internal_keyword_list的关键词存在
                        break

                if temp_1 == 0:  # 如果不符合internal_keyword_list关键词就不写入数据库
                    self.logger.info('continue')
                    continue

                for nonekeyword in self.nonekyewordList:
                    if nonekeyword in article.title or nonekeyword in article.content:
                        self.logger.debug('\tNone Keyword is <%s>' % nonekeyword)
                        self.logger.debug('\tNone Keyword article url is %s' % article.url)
                        temp_2 = 1  # internal_none_keyword_list的反关键词存在
                        break

                if temp_2 == 1:  # 如果符合internal_none_keyword_list关键词就不写入数据库
                    continue
                statistics = article.statistics
                if hasMetaInfo:
                    metaInfoFieldValue = ',"'+encodeText(article.meta_info)+'"' if article.meta_info is not None else ','+Constants.DEFAULT_STR
                else:
                    metaInfoFieldValue = ''

                value_str = '''("%s", %s %d, "%s", "%s", %d, "%s", "%s", "%s", "%s", "%s", "%s","%s",
                 %s, %s, %s, %s, %s, %d, %s, "%s" %s)'''
                valueList.append(value_str % (
                            article.tid, eventIdFieldValue, article.channel_id, article.url, n, int(period),
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
                            article.classified_nature,
                            statistics.heat if statistics.heat is not None else Constants.DEFAULT_NUM,
                            n, metaInfoFieldValue
                            ))
        if len(valueList) > 0:
            tmp = insertSql % (tableName, eventIdFieldName, metaInfoFieldName, ','.join(valueList), metaInfoOnUpdate)
            self.dbProxy.execute(tmp)
            self.dbProxy.commit()

    def __updateToArticleHistoryTable(self, articleList, tableName, eventId=None):
        '''
        更新到文章历史表
        @param tableName: 当前文章表：全局文章表、实体文章表或者实体事件文章表
        @param eventId: 如果更新到实体事件文章表，则需要提供事件id，否则为None
        '''
        n = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        period = int(datetime.datetime.now().strftime('%Y%m%d%H'))
        valueList = list()
        if eventId is None:
            eventIdFieldName = ''
            eventIdFieldValue = ''
            insertSql = '''
                INSERT INTO %s (TID, %s CHANNEL_ID,
                    READ_COUNT,LIKE_COUNT, REPLY_COUNT,
                    FORWARD_COUNT, COLLECT_COUNT, HEAT, ADD_DATETIME)
                VALUES %s 
                ON DUPLICATE KEY UPDATE READ_COUNT = VALUES(READ_COUNT),LIKE_COUNT = VALUES(LIKE_COUNT),
                REPLY_COUNT = VALUES(REPLY_COUNT),FORWARD_COUNT = VALUES(FORWARD_COUNT),COLLECT_COUNT = VALUES(COLLECT_COUNT),
                HEAT = VALUES(HEAT), ADD_DATETIME = VALUES(ADD_DATETIME)
            '''
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
        else:
            eventIdFieldName = 'EVENT_ID,'
            eventIdFieldValue = str(eventId)+','

            insertSql = '''
                INSERT INTO %s (TID, %s CHANNEL_ID,
                    READ_COUNT,LIKE_COUNT, REPLY_COUNT,
                    FORWARD_COUNT, COLLECT_COUNT, HEAT, ADD_DATETIME, PERIOD)
                VALUES %s 
                ON DUPLICATE KEY UPDATE READ_COUNT = VALUES(READ_COUNT),LIKE_COUNT = VALUES(LIKE_COUNT),
                REPLY_COUNT = VALUES(REPLY_COUNT),FORWARD_COUNT = VALUES(FORWARD_COUNT),COLLECT_COUNT = VALUES(COLLECT_COUNT),
                HEAT = VALUES(HEAT), ADD_DATETIME = VALUES(ADD_DATETIME), PERIOD = VALUES(PERIOD)
            '''
            for article in articleList:
                statistics = article.statistics
                valueList.append('("%s", %s %d, %s, %s, %s, %s, %s, %s, "%s", %d)' % (
                            article.tid,
                            eventIdFieldValue,
                            article.channel_id,
                            statistics.read_count if statistics.read_count is not None else Constants.DEFAULT_NUM,
                            statistics.like_count if statistics.like_count is not None else Constants.DEFAULT_NUM,
                            statistics.reply_count if statistics.reply_count is not None else Constants.DEFAULT_NUM,
                            statistics.forward_count if statistics.forward_count is not None else Constants.DEFAULT_NUM,
                            statistics.collect_count if statistics.collect_count is not None else Constants.DEFAULT_NUM,
                            statistics.heat if statistics.heat is not None else Constants.DEFAULT_NUM,
                            n,
                            period
                            ))
        if len(valueList) > 0:
            self.dbProxy.execute(insertSql % (tableName, eventIdFieldName, ','.join(valueList)))
            self.dbProxy.commit()

    def __updateOldArticleToArticleTable(self, articleList, tableName, isEventTable=False):
        '''
        更新旧文章到文章表
        @param tableName: 全局文章表、实体文章表或者实体事件文章表
        @param eventId: 如果更新到实体事件文章表，则需要提供事件id，否则为None
        '''
        if len(articleList) > 0:
            if isEventTable is False:
                eventIdFieldName = ''
            else:
                eventIdFieldName = ',EVENT_ID'
            #找寻老文章
            selectSql = '''
            SELECT TID, CHANNEL_ID %s FROM %s where %s
            '''
            whereClauseList = map(lambda article: '(TID="%s" and CHANNEL_ID=%d)'%(article.tid, article.channel_id), 
                                  articleList)
            self.dbProxy.execute(selectSql % (eventIdFieldName, tableName, ' or '.join(whereClauseList)))
            resultList = self.dbProxy.fetchall()
            if isEventTable:
                existingArticleList = map(lambda item: Article(item[0], item[1], eventId=item[2]), resultList)
            else:
                existingArticleList = map(lambda item: Article(item[0], item[1]), resultList)
            toBeUpdateArticleList = list()
            for item in existingArticleList:
                index = articleList.index(item)
                obj = copy.copy(articleList[index])
                obj.eventId = item.eventId
                toBeUpdateArticleList.append(obj)                    
            if len(toBeUpdateArticleList) > 0:
                n = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                if isEventTable is False:
                    eventIdFieldName = ''
                else:
                    eventIdFieldName = 'EVENT_ID,'
                insertSql = '''
                INSERT INTO %s (TID, %s CHANNEL_ID,
                    READ_COUNT,LIKE_COUNT, REPLY_COUNT,
                    FORWARD_COUNT, COLLECT_COUNT, HEAT, UPDATE_DATETIME)
                VALUES %s 
                ON DUPLICATE KEY UPDATE READ_COUNT=VALUES(READ_COUNT), LIKE_COUNT=VALUES(LIKE_COUNT), 
                REPLY_COUNT = VALUES(REPLY_COUNT), FORWARD_COUNT=VALUES(FORWARD_COUNT), 
                COLLECT_COUNT = VALUES(COLLECT_COUNT), HEAT = VALUES(HEAT), UPDATE_DATETIME=VALUES(UPDATE_DATETIME)
                '''
                valueList = list()
                for article in toBeUpdateArticleList:
                    statistics = article.statistics
                    if isEventTable is False:
                        eventIdFieldValue = ''
                    else:
                        eventIdFieldValue = str(article.eventId)+','
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
                    self.dbProxy.commit()

    def __updateOldArticleToArticleHistoryTable(self, articleList, currentTableName, historyTableName, isEventTable=False):
        '''
        更新到文章历史表
        @param currentTableName: 当前文章表：全局文章表、实体文章表或者实体事件文章表
        @param historyTableName: 历史文章表：全局文章表、实体文章表或者实体事件文章表
        @param eventId: 如果更新到实体事件文章表，则需要提供事件id，否则为None
        '''
        if len(articleList) > 0:
            if isEventTable is False:
                eventIdFieldName = ''
            else:
                eventIdFieldName = ',EVENT_ID'
            #找寻老文章
            selectSql = '''
            SELECT TID, CHANNEL_ID %s FROM %s where %s
            '''
            whereClauseList = map(lambda article: '(TID="%s" and CHANNEL_ID=%d)'%(article.tid, article.channel_id), 
                                  articleList)
            self.dbProxy.execute(selectSql % (eventIdFieldName, currentTableName, ' or '.join(whereClauseList)))
            resultList = self.dbProxy.fetchall()
            if isEventTable:
                existingArticleList = map(lambda item: Article(item[0], item[1], eventId=item[2]), resultList)
            else:
                existingArticleList = map(lambda item: Article(item[0], item[1]), resultList)
            toBeUpdateArticleList = list()
            for item in existingArticleList:
                index = articleList.index(item)
                obj = copy.copy(articleList[index])
                obj.eventId = item.eventId
                toBeUpdateArticleList.append(obj)                    
            if len(toBeUpdateArticleList) > 0:
                n = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                if isEventTable is False:
                    eventIdFieldName = ''
                else:
                    eventIdFieldName = 'EVENT_ID,'
                insertSql = '''
                INSERT INTO %s (TID, %s CHANNEL_ID,
                    READ_COUNT,LIKE_COUNT, REPLY_COUNT,
                    FORWARD_COUNT, COLLECT_COUNT, HEAT, ADD_DATETIME)
                VALUES %s 
                '''
                valueList = list()
                for article in toBeUpdateArticleList:
                    statistics = article.statistics
                    if isEventTable is False:
                        eventIdFieldValue = ''
                    else:
                        eventIdFieldValue = str(article.eventId)+','
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
                    self.dbProxy.execute(insertSql % (historyTableName, eventIdFieldName, ','.join(valueList)))
                    self.dbProxy.commit()
