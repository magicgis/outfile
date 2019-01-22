# -*- coding:utf-8 -*-
'''
Created on 8 Oct 2017

@author: eyaomai
'''

from com.naswork.sentiment.crawler.sessioncrawler import SessionCrawler
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
import re, json
from bs4 import BeautifulSoup
import datetime, time


class BaiduTieBaCrawler(object):
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
        self.site = 'tieba.baidu.com'  # 搜索站点
        self.url = 'http://tieba.baidu.com/f/search/res'
        self.tiebaUrl_list = list()
        self.session = SessionCrawler(sleepRange=[3, 5])
        self.channel = channel

        self.count_page = 0 #优化时间，使进入一次便可以
        self.pageList = []  #保存__searchByPage 里搜索到的url页数
        self.articleList = list()  #全局去重复文章变量
    
    def searchArticle(self, keywordList,  endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        @param keywordList: 关键字数组
        @param startTime: 搜索时间范围起始
        @param endTime: 搜索时间范围结束
        '''

        page = 1

        hasnext = True
        # self.logger.info(u'这里等待1')
        startTime = endTime - datetime.timedelta(days=self.channel.search_ranges)
        while hasnext:
            (urllist, hasnext) = self.__searchByPage(keywordList, startTime, endTime, page)
            self.__parseSearchPage(urllist) # 根据url找相关帖子

            page += 1

        return self.articleList

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        html = self.session.download(article.url, encoding='utf-8', data=None, isJson=False, timeout=10, retry=3, addr=True)
        soup = BeautifulSoup(html['html'], 'lxml')
        try:
            Treply = soup.find('li', attrs={'class': "l_reply_num"}).find('span').text.strip()  # 总回复数
            article.statistics.reply_count = Treply
        except:
            return


    #先通过贴吧搜索栏查询url列表
    def __searchByPage(self, keywordList, startTime, endTime, page):
        # self.logger.info(u'这里等待2')

        data = {
            'ie': 'utf-8',
            'kw': '',  # 贴吧名称
            'qw': keywordList,  # 关键字
            # 'rn': '60',  # 显示条数
            'un': '',  # 用户名
            'only_thread': '1',
            'sm': '1',  # 按时间倒序
            # 'timescope': 'custom:%s:%s' % (startTime.strftime("%Y-%m-%d-%H"), (endTime+datetime.timedelta(hours=1)).strftime("%Y-%m-%d-%H")),  # 时间
            'sd': '',
            'ed': '',
            'pn': page  # 页数
        }
        html = self.session.download(self.url, encoding='utf-8', data=data, isJson=False, timeout=10, retry=3, addr=True)

        soup = BeautifulSoup(html['html'], 'html.parser')
        main = soup.find('div', attrs={'class': "s_post_list"})
        try:
            urls = main.find_all('div', attrs={'class': "s_post"})   #在贴吧搜索中获取url  list
        except:
            self.logger.warn(u'找不到url')
            return
        self.logger.debug(u'共找到%d条url',len(urls))

        if self.count_page == 0:   #只进入一次（计算分页数）
            self.count_page = 1
            pages = soup.find('div', attrs={'class': "pager pager-search"}).find_all('a')
            for p in pages:
                pa = p.text.strip()
                self.pageList.append(pa)

        if str(page + 1) in self.pageList:
            hasnext = True
        else:
            hasnext = False

        for i in urls:

            urlTime = i.find('font',attrs={'class':"p_green p_date"}).text.strip()
            urlTime = time.strptime(urlTime,"%Y-%m-%d %H:%M")
            Y,M,D,H = urlTime[0:4]
            urlTime2 = datetime.datetime(Y,M,D,H)

            if urlTime2 >= startTime and urlTime2 <= endTime:
                # self.logger.error(u"时间比较")
                try:
                    url = i.find('span', attrs={'class': "p_title"}).find('a').attrs['data-tid']
                    url = 'http://tieba.baidu.com/p/' + url
                    if url not in self.tiebaUrl_list:
                        self.tiebaUrl_list.append(url)
                except:
                    continue
            else:
                hasnext = False
                break

        return (self.tiebaUrl_list,hasnext)

    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        article = None
        html = self.session.download(url, encoding='utf-8', data=None, isJson=False, timeout=10, retry=3, addr=True)
        article_url = html['url']

        # if article_url.find(self.channel.url)<0:
        #     self.logger.warn('Unrelated url found:%s',url)
        #     continue

        # self.logger.debug(article_url)
        soup = BeautifulSoup(html['html'], 'html.parser')


        noResultDivList = soup.findAll('div', {'class': 'pl_noresult'})
        if len(noResultDivList) > 0:
            hasnext = False
            self.logger.info('No result')
            return article

        main = soup.find('div', attrs={'class': "left_section"})

        if main:
            Ttitle = main.find('div', attrs={'id': "j_core_title_wrap"}).find('h1')
            Ttitle1 = main.find('div', attrs={'id': "j_core_title_wrap"}).find('h3')
            if Ttitle:
                Ttitle = Ttitle.text.strip()
            elif Ttitle1:
                Ttitle = Ttitle1.text.strip()
            else:
                Ttitle = ''

            # self.logger.debug(u'标题%s',Ttitle)

            data_field = main.find('div', attrs={'id': "j_p_postlist"}).find('div').attrs['data-field'].strip()
            data_field = json.loads(data_field)
            publish_datetime = data_field['content']
            if 'date' in publish_datetime.keys():
                publish_datetime = publish_datetime['date']
            else:
                publish_datetime = main.find('div', attrs={'id': "j_p_postlist"}).find('div').find_all('span', attrs={
                    'class': "tail-info"})[-1].text.strip()

            publish_datetime = re.findall(r'(\d{4})\D(\d{2})\D(\d{2})\D(\d{2}:\d{2}(:\d{2})?)',
                                          publish_datetime)[0]

            if len(publish_datetime[4]) > 1:
                publish_datetime = publish_datetime[0] + '-' + publish_datetime[1] + '-' + publish_datetime[
                    2] + ' ' + publish_datetime[3] + publish_datetime[4]
            else:
                publish_datetime = publish_datetime[0] + '-' + publish_datetime[1] + '-' + publish_datetime[
                    2] + ' ' + publish_datetime[3] + ':00'

            Tid = data_field['author']['user_id']
            Tauthor = data_field['author']['user_name']
            Treply = soup.find('li', attrs={'class': "l_reply_num"}).find('span').text.strip()#总回复数
            Tcontent = main.find('div', attrs={'id': "j_p_postlist"}).find('div').find('cc').text.strip()

            article = Article(Tid, self.channel.channel_id,
                              Ttitle, Tcontent, publish_datetime, url=article_url,
                              author_id=None, author_name=Tauthor,meta_info=None)

            article.statistics.reply_count = Treply

        else:
            self.logger.warn(u'很抱歉，该贴已被删除。%s', article_url)
        
        return article

    #通过找到的url列表一个个访问文章
    def __parseSearchPage(self, urllist):
        '''
        @return: (articleList,hasnext)
        '''

        self.logger.info(u'这里在访问收集到的url，请稍等')
        for url in urllist:
            article = self.crawlArticle(url)
            if article not in self.articleList and article is not None:
                self.articleList.append(article)
        # return articleList2


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
        commentList = list()
        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        page = 1
        while page <= 30:
            data = {'pn': page}
            html = self.session.download(article.url, encoding='utf-8', data=data, isJson=False, timeout=10, retry=3, addr=True)
            article_url = article.url
            # print article_url
            soup = BeautifulSoup(html['html'])

            try:
                main = soup.find('div', attrs={'class': "left_section"})
                main = main.find('div', attrs={'id':"j_p_postlist"})
            except:
                self.logger.warn(u'很抱歉，该贴已被删除。%s',article_url)
                return (commentList, False)

            sectionsite = main.find_all('div',attrs={'class':"l_post"})
            # self.logger.error(len(sectionsite))

            index = 0

            if main:

                com_all = main.find_all('div', attrs={'data-field': True})

                for i in sectionsite[2:]:
                    # self.logger.warn(i)

                    index= index+1

                    if com_all[index].attrs['data-field']:

                        try:
                            data_field = i.attrs['data-field'].strip()
                        except:
                            self.logger.error(u'存在未找到的data-field')
                            self.logger.error(article_url)
                            continue

                        data_field = json.loads(data_field)
                        if 'content' in data_field.keys():
                            # self.logger.warn(u'这里真的会不糊出错2')
                            cid = data_field['content']['post_id']
                            user_id = data_field['author']['user_id']
                            user_name = data_field['author']['user_name']
                            # user_ip = ''
                            # ip_address = ''
                            # user_head = ''
                            if 'date' in data_field['content'].keys():
                                # self.logger.warn(u'这里没有出错%s', article_url)
                                cpublish_datetime = data_field['content']['date']
                            else:
                                # self.logger.warn(u'这里出错%s',article_url)
                                cpublish_datetime = i.findAll('span')
                                cpublish_datetime = cpublish_datetime[-1].text.strip()
                                if u'广告' in cpublish_datetime:
                                    continue

                            cpublish_datetime = re.findall(r'(\d{4})\D(\d{2})\D(\d{2})\D(\d{2}:\d{2}(:\d{2})?)',
                                                               cpublish_datetime)[0]

                            if len(cpublish_datetime[4]) > 1:
                                cpublish_datetime = cpublish_datetime[0] + '-' + cpublish_datetime[1] + '-' + \
                                                    cpublish_datetime[2] + ' ' + cpublish_datetime[3] + cpublish_datetime[4]
                            else:
                                cpublish_datetime = cpublish_datetime[0] + '-' + cpublish_datetime[1] + '-' + \
                                                   cpublish_datetime[2] + ' ' + cpublish_datetime[3] + ':00'
                            # reply_userid = ''
                            # like_count =
                            # unlike_count = -1
                            # read_count = -1
                            reply_count = data_field['content']['comment_num']
                            source_url = article_url
                            content = i.find('cc').text.strip()
                            location_coutry = 'CN'
                            # channeltype = 'tieba'
                            # channel = self.site
                            # heat = 0

                            commentList.append(Comment(article.tid, self.channel.channel_id, cid,
                                                       add_datetime, cpublish_datetime,
                                                       None, location_coutry, None, None,
                                                       user_id, user_name, content, None,
                                                       None, None, reply_count, dislike_count=None
                                                       ))

            # 翻页控制
            pages = soup.find('li', attrs={'class': "l_pager"}).find_all('a')
            pageList = []
            for p in pages:
                pa = p.text.strip()
                pageList.append(pa)
            if str(page + 1) in pageList:
                page += 1
            else:
                break
        return (commentList, False)