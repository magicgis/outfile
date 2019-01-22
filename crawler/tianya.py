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


class TianYaCrawler(object):
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
        self.site = 'bbs.tianya.cn'
        self.url = 'http://search.tianya.cn/bbs?&s=4&f=0'
        self.luntanUrl_list = list()
        self.session = SessionCrawler(sleepRange=[3, 5])
        self.channel = channel

        self.count_page = 0
        self.pageList = []
        self.articleList = list()  # 全局去重复文章变量


    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        @param keywordList: 关键字数组
        @param startTime: 搜索时间范围起始
        @param endTime: 搜索时间范围结束
        '''

        page = 1
        articleList = list()
        hasnext = True

        while hasnext:
            startTime = endTime - datetime.timedelta(days=self.channel.search_ranges)
            (urllist, hasnext) = self.__searchByPage(keywordList, startTime, endTime, page)
            self.logger.error(len(urllist))
            # if len(urllist) == 0:
            #     self.logger.warn(u'搜索关键词结果为空：：%s',keywordList)
            #     break
            self.__parseSearchPage(urllist) # 根据url找相关帖子
            page += 1

        return self.articleList

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        html = self.session.download(article.url, encoding='utf-8', data=None, isJson=False, timeout=10, retry=3,
                                     addr=True)
        soup = BeautifulSoup(html['html'], 'lxml')

        try:
            Treply = soup.find('div', attrs={'class': "atl-info"}).find_all('span')[3].text.strip()  # 总回复数
            Treply = re.sub(u'[\u4e00-\u9fa5]+：', '', Treply)
            Treply = int(Treply)
            Tclick = soup.find('div', attrs={'class': "atl-info"}).find_all('span')[2].text.strip()  # 总回复数
            Tclick = re.sub(u'[\u4e00-\u9fa5]+：', '', Tclick)
            Tclick = int(Tclick)
            article.statistics.reply_count = Treply
            article.statistics.reply_count = Tclick
        except:
            self.logger.debug(u'无权限访问的网址:%s',article.url)


    #先通过论坛搜索栏查询url列表
    def __searchByPage(self, keywordList, startTime, endTime, page):

        data = {
            'q': keywordList,
            's': '4',
            'pn': page,
            'f':'0'
        }
        html = self.session.download(self.url, encoding='utf-8', data=data, isJson=False, timeout=10, retry=3, addr=True)

        soup = BeautifulSoup(html['html'], 'html.parser')
        urls = soup.find('div', attrs={'class': "searchListOne"}).find_all('li')[:-1] # # if i.attrs['id'] == 'search_msg':存在这个li
        self.logger.warn(len(urls))

        if self.count_page == 0:   #只进入一次（计算分页数）
            self.count_page = 1
            pages = soup.find('div', attrs={'class': "long-pages"}).find_all('a')
            for p in pages:
                pa = p.text.strip()
                self.pageList.append(pa)

        if str(page + 1) in self.pageList:
            hasnext = True
        else:
            hasnext = False

        for i in urls:

            urlTime = i.find('p', attrs={'class': "source"}).find('span').text.strip()

            urlTime = time.strptime(urlTime, "%Y-%m-%d %H:%M:%S")
            self.logger.error(urlTime)
            Y, M, D, H, S= urlTime[0:5]
            urlTime2 = datetime.datetime(Y, M, D, H,S)
            self.logger.error(urlTime2)

            if urlTime2 >= startTime and urlTime2 <= endTime:
                self.logger.error(u"时间比较")
                try:
                    url = i.find('h3').find('a').attrs['href']
                    if url not in self.luntanUrl_list:
                        self.luntanUrl_list.append(url)
                except:
                    continue
            else:
                hasnext = False
                break

        return (self.luntanUrl_list,hasnext)

    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        html = self.session.download(url, encoding='utf-8', data=None, isJson=False, timeout=10, retry=3, addr=True)
        article_url = html['url']
        self.logger.debug(article_url)
        soup = BeautifulSoup(html['html'], 'html.parser')

        main = soup.find('div', attrs={'id': "bd"})
        main1 = soup.find('div', attrs={'class': "wd-question"})#论坛提问帖子http://bbs.tianya.cn/post-730-5795-1-1.shtml
        article = None
        if main:
            Ttitle = main.find('h1').find('span').text
            Ttime = main.find('div', attrs={'class': "atl-info"}).find_all('span')[1].text.strip()
            Ttime = re.sub(u'[\u4e00-\u9fa5]+：', '', Ttime)
            Tid = main.find('div', attrs={'class': "atl-info"}).find_all('span')[0].find('a').attrs['uid'].strip()
            Tauthor = main.find('div', attrs={'class': "atl-info"}).find_all('span')[0].find('a').attrs[
                'uname'].strip()
            Tclick = main.find('div', attrs={'class': "atl-info"}).find_all('span')[2].text.strip()
            Tclick = re.sub(u'[\u4e00-\u9fa5]+：', '', Tclick)
            Tclick = int(Tclick)
            Treply = main.find('div', attrs={'class': "atl-info"}).find_all('span')[3].text.strip()
            Treply = re.sub(u'[\u4e00-\u9fa5]+：', '', Treply)
            Treply = int(Treply)
            Tlike = main.find('a',attrs={'class':"zantop"}).attrs['_count']
            Tcontent = main.find('div', attrs={'class': "bbs-content clearfix"}).text.strip()

            article = Article(Tid, self.channel.channel_id,
                              Ttitle, Tcontent, Ttime, url=article_url,
                              author_id=None, author_name=Tauthor)

            article.statistics.reply_count = Treply
            article.statistics.click_count = Tclick
            article.statistics.like_count = Tlike

        elif main1:
            Ttitle = main1.find('h1').find('span').text
            Ttime = main1.find('div').attrs['js_replytime']
            Ttime = re.findall(r'(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})', Ttime)[0]
            Tid = main1.find('div').attrs['_host']
            Tauthor = main1.find('div', attrs={'class': "q-info"}).find('a').text
            Tclick = main1.find('div').attrs['js_clickcount']
            Treply = main1.find('div').attrs['js_powerreply']
            Tcontent = main1.find('div', attrs={'class': "q-content atl-item"})
            if Tcontent:
                Tcontent = Tcontent.find('div', attrs={'class': "text"}).text.strip()
            else:
                Tcontent = ''

            article = Article(Tid, self.channel.channel_id,
                              Ttitle, Tcontent, Ttime, url=article_url,
                              author_id=None, author_name=Tauthor)

            article.statistics.reply_count = Treply
            article.statistics.click_count = Tclick
        return article
    
    #通过找到的url列表一个个访问文章
    def __parseSearchPage(self, urllist):

        self.logger.info(u'这里在访问收集到的url，请稍等')

        for url in urllist:
                article = self.crawlArticle(url)

                if article not in self.articleList and article is not None:
                    self.articleList.append(article)


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
        html = self.session.download(article.url, encoding='utf-8', data=None, isJson=False, timeout=10, retry=3)
        article_url = article.url
        soup = BeautifulSoup(html, 'html.parser')
        comments = soup.find_all(lambda tag: tag.name == 'div' and tag.get('class') == ['atl-item'])
        for i in comments:
            cid = i.attrs['replyid']
            user_id = i.attrs['_hostid']
            user_name = i.attrs['_host']
            # user_head = i.find('div', attrs={'class': "atl-info"}).find('a').attrs['href'] #楼主name
            cpublish_datetime = i.attrs['js_restime']
            reply_userid = ''  # 评论父id
            like_count = i.find('a', attrs={'class': "zan"}).attrs['_count']

            reply_count = i.find('div', attrs={'class': "atl-reply"}).find('a', attrs={'title': "插入评论"}).text.strip()
            reply_count = re.findall(r'\d+', reply_count)
            if reply_count:
                reply_count = reply_count[0]
            else:
                reply_count = 0

            content = i.find('div', attrs={'class': "bbs-content"}).text.strip()
            location_coutry = 'CN'
            commentList.append(Comment(article.tid, self.channel.channel_id, cid,
                                       add_datetime, cpublish_datetime,
                                       None, location_coutry, None, None,
                                       user_id, user_name, content, reply_userid,
                                       None, like_count, reply_count, dislike_count=None
                                       ))

        return (commentList, False)