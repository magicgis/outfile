# -*- coding:utf-8   -*-
'''
Created on 2 Nov 2017

@author: zemianzhang
'''

from com.naswork.sentiment.crawler.baidu import BaiduCrawler
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
import re, json, traceback
from bs4 import BeautifulSoup
import datetime, time
class XinHuaNewsCrawler(object):
    '''
    classdocs
    '''

    def __init__(self, channel, logger=None):
        '''
        Constructor
        :param channel:媒体
        :param logger:日志
        :return:
        '''
        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger

        self.baiduCrawler = BaiduCrawler(self.logger)
        self.session = self.baiduCrawler.session
        self.channel = channel

    def searchArticle(self, keywordList, endTime):
        '''
        根据关键字数组，开始时间和结束时间范围搜索文章
        :param keywordList:关键字数组
        :param endTime:搜索时间范围结束
        :return:返回文章列表
        '''
        startTime = endTime - datetime.timedelta(days=1)
        startTimeIntSecond = time.mktime(startTime.timetuple())
        endTimeIntSecond = time.mktime(endTime.timetuple())
        urls = self.baiduCrawler.search(self.channel.url, keywordList, startTimeIntSecond, endTimeIntSecond)
        articleList = list()
        for url in urls:
            self.logger.debug(url)
            article = self.crawlArticle(url)
            if article is not None and article not in articleList:
                #同一文章可能会在搜索中出现多次结果，在baidu的结果url是不重复，但是实际可能指向同一文章，需要去重
                articleList.append(article)
        return articleList

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        :param article:文章
        :return:无需返回参数，统计信息写入article实例
        '''
        pass

    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        :param url:
        :return:返回一个Article实例
        '''
        cookies = None
        html = self.session.download(url=url, encoding='utf-8', data=None, timeout=10, retry=3, addr=True, cookies=cookies)
        if html:
            article_url = html['url']
            article_url = re.findall(r'.*?\.html|.*?\.htm|.*?\.shtml|.*?\.shtm', article_url)[0]
            article_id = re.findall(r'c_\d+', article_url)[0]
            article_id = article_id[2:]
            soup = BeautifulSoup(html['html'], "html.parser")
            main1 = soup.find('div', attrs={'class':"widthMain main"})
            main2 = soup.find('div', attrs={'class':"main pagewidth"})
            main3 = soup.find('body', attrs={'class':"streamline-page"})
            main4 = soup.find('div', attrs={'class':"h-title"})
            main5 = soup.find('div', attrs={'id':"article"})
            main6 = soup.find('div', attrs={'id':"Title"})
            main7 = soup.find('div', attrs={'class':"article"})

            if main1 is not None:
                self.logger.debug("main1")
                Ttitle, Ttime, Tauthor, Tcontent = self.crawlMain1(main1)
                if (Ttitle != None and Ttime != None and Tcontent != None):
                    article = Article(tid=article_id, channel_id=self.channel.channel_id, title=Ttitle,
                                      author_name=Tauthor, publish_datetime=Ttime, content=Tcontent,
                                      url=article_url)
                    return article

            if main2 is not None:
                self.logger.debug("main2")
                Ttitle, Ttime, Tauthor, Tcontent = self.crawlMain2(main2)
                if (Ttitle != None and Ttime != None and Tcontent != None):
                    article = Article(tid=article_id, channel_id=self.channel.channel_id, title=Ttitle,
                                    author_name=Tauthor, publish_datetime=Ttime, content=Tcontent,
                                    url=article_url)
                    return article

            if main3 is not None:
                self.logger.debug("main3")
                Ttitle, Ttime, Tauthor, Tcontent = self.crawlMain3(main3)
                if (Ttitle != None and Ttime != None and Tcontent != None):
                    article = Article(tid=article_id, channel_id=self.channel.channel_id, title=Ttitle,
                                    author_name=Tauthor, publish_datetime=Ttime, content=Tcontent,
                                    url=article_url)
                    return article

            if main3 is None and main4 is not None:
                self.logger.debug("main4")
                Ttitle, Ttime, Tauthor, Tcontent = self.crawlMain4(soup)
                if (Ttitle != None and Ttime != None and Tcontent != None):
                    article = Article(tid=article_id, channel_id=self.channel.channel_id, title=Ttitle,
                                    author_name=Tauthor, publish_datetime=Ttime, content=Tcontent,
                                    url=article_url)
                    return article

            if main5 is not None:
                self.logger.debug("main5")
                Ttitle, Ttime, Tauthor, Tcontent = self.crawlMain5(main5)
                if (Ttitle != None and Ttime != None and Tcontent != None):
                    article = Article(tid=article_id, channel_id=self.channel.channel_id, title=Ttitle,
                                    author_name=Tauthor, publish_datetime=Ttime, content=Tcontent,
                                    url=article_url)
                    return article

            if main6 is not None:
                self.logger.debug("main6")
                Ttitle, Ttime, Tauthor, Tcontent = self.crawlMain6(soup)
                if (Ttitle != None and Ttime != None and Tcontent != None):
                    article = Article(tid=article_id, channel_id=self.channel.channel_id, title=Ttitle,
                                    author_name=Tauthor, publish_datetime=Ttime, content=Tcontent,
                                    url=article_url)
                    return article

            if main7 is not None:
                self.logger.debug("main7")
                Ttitle, Ttime, Tauthor, Tcontent = self.crawlMain7(soup)
                if (Ttitle != None and Ttime != None and Tcontent != None):
                    article = Article(tid=article_id, channel_id=self.channel.channel_id, title=Ttitle,
                                    author_name=Tauthor, publish_datetime=Ttime, content=Tcontent,
                                    url=article_url)
                    return article

            if(main1 is None and main2 is None and main3 is None and main4 is None and main5 is None and main6 is None and main7 is None):
                self.logger.error(u"存在另外一种html格式：：%s",url)
                return


    def refreshSearch(self):
        '''
        重置搜索
        :return:
        '''
        pass

    def refreshCommentCrawler(self):
        '''
        重置评论爬取
        :return:
        '''
        pass

    def crawlComment(self, article):
        '''
        根据文章，爬取文章的评论，返回评论列表
        :param article:文章
        :return:(commentList, hasnext)二元组，commentList是指评论数组（每个元素是Comment实例），hasnext表示是否还有要爬取
        '''
        return ([],False)

    def crawlMain1(self, main1):
        # 获取标题
        Ttitle = main1.find('h1')
        if Ttitle is None:
            self.logger.error(u'[XinhuaNews]' + u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        else:
            Ttitle = Ttitle.text.strip()

        # 获取发布时间
        Tinfo = main1.find('div', attrs={'class': "info"})
        if Tinfo is not None:
            Ttime = Tinfo.find('span', attrs={'class': "h-time"})
            Ttime = Ttime.text.strip()
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None

        if len(Ttime) == 16:
            Ttime = Ttime + ':00'

        # 获取发布作者
        Tauthor = Tinfo.find('em', attrs={'id': "source"})
        if Tauthor is not None:
            Tauthor = Tauthor.text.strip()
        else:
            Tauthor = None

        #获取发布内容
        Tcontent = main1.find('div', attrs={'id':"content"})

        if Tcontent is not  None:
            Tcontent = Tcontent.text.strip()
            Tcontent = re.sub(r'\n\t','',Tcontent)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        return Ttitle, Ttime, Tauthor, Tcontent

    def crawlMain2(self, main2):
        # 获取标题
        error = ""
        Ttitle = main2.find('h1')
        if Ttitle is None:
            self.logger.error(u'[XinhuaNews]' + u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        else:
            Ttitle = Ttitle.text.strip()

        # 获取发布时间
        Tinfo = main2.find('div', attrs={'class': "info"})
        if Tinfo is not None:
            Ttime = Tinfo.find('span', attrs={'id': "pubtime"})
            Ttime = Ttime.text.strip()
            if (Ttime == ""):
                Ttime = Tinfo.text.strip()
                Ttime = re.findall(u'\d{4}年\d{2}月\d{2}日.\d{2}:\d{2}:\d{2}', Ttime)[0]
            timeArray = time.strptime(Ttime, u"%Y年%m月%d日 %H:%M:%S")
            Ttime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None

        if len(Ttime) == 16:
            Ttime = Ttime + ':00'

        # 获取发布作者
        Tauthor = Tinfo.find('em', attrs={'id': "source"})
        if Tauthor is not None:
            Tauthor = Tauthor.text.strip()
        else:
            Tauthor = None

        # 获取发布内容
        Tcontent = main2.find('div', attrs={'id': "content"})
        if Tcontent is not None:
            Tcontent = Tcontent.text.strip()
            Tcontent = re.sub(r'\n\t', '', Tcontent)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        return Ttitle, Ttime, Tauthor, Tcontent

    def crawlMain3(self, main3):
        # 获取标题
        Ttitle = main3.find('div', attrs={'class':"h-title"})
        if Ttitle is None:
            self.logger.error(u'[XinhuaNews]' + u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        else:
            Ttitle = Ttitle.text.strip()

        # 获取发布时间
        Tinfo = main3.find('div', attrs={'class': "h-info"})
        if Tinfo is not None:
            Ttime = Tinfo.find('span', attrs={'class': "h-time"})
            Ttime = Ttime.text.strip()
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None

        if len(Ttime) == 16:
            Ttime = Ttime + ':00'

        # 获取发布作者
        Tauthor = Tinfo.find_all('span')[1]
        if Tauthor is not None:
            Tauthor = Tauthor.text.strip()
        else:
            Tauthor = None

        # 获取发布内容
        Tcontent = main3.find('div', attrs={'class': "h-title"})
        if Tcontent is not None:
            Tcontent = Tcontent.text.strip()
            Tcontent = re.sub(r'\n\t', '', Tcontent)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        return Ttitle, Ttime, Tauthor, Tcontent

    def crawlMain4(self, main4):
        # 获取标题
        Ttitle = main4.find('div', attrs={'class': "h-title"})
        if Ttitle is None:
            self.logger.error(u'[XinhuaNews]' + u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        else:
            Ttitle = Ttitle.text.strip()

        # 获取发布时间
        Tinfo = main4.find('div', attrs={'class': "h-info"})
        if Tinfo is not None:
            Ttime = Tinfo.find('span', attrs={'class': "h-time"})
            Ttime = Ttime.text.strip()
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None

        if len(Ttime) == 16:
            Ttime = Ttime + ':00'

        # 获取发布作者
        Tauthor = Tinfo.find_all('span')[1]
        if Tauthor is not None:
            Tauthor = Tauthor.text.strip()
        else:
            Tauthor = None

        # 获取发布内容
        Tcontent = main4.find('div', attrs={'id': "p-detail"})
        if Tcontent is not None:
            Tcontent = Tcontent.text.strip()
            Tcontent = re.sub(r'\n\t', '', Tcontent)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        return Ttitle, Ttime, Tauthor, Tcontent

    def crawlMain5(self, main5):
        # 获取标题
        Ttitle = main5.find('h1')
        if Ttitle is None:
            self.logger.error(u'[XinhuaNews]' + u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        else:
            Ttitle = Ttitle.text.strip()

        # 获取发布时间
        Tinfo = main5.find('div', attrs={'class': "source"})
        Ttime = Tinfo.find('span', attrs={'class': "time"})
        if Ttime is not None:
            Ttime = Ttime.text.strip()
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None

        if len(Ttime) == 16:
            Ttime = Ttime + ':00'

        # 获取发布作者
        Tauthor = Tinfo.find('em', attrs={'id':"source"})
        if Tauthor is not None:
            Tauthor = Tauthor.text.strip()
        else:
            Tauthor = None

        # 获取发布内容
        Tcontent = main5.find('div', attrs={'class': "article"})
        if Tcontent is not None:
            Tcontent = Tcontent.text.strip()
            Tcontent = re.sub(r'\n\t', '', Tcontent)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        return Ttitle, Ttime, Tauthor, Tcontent

    def crawlMain6(self, main6):
        # 获取标题
        Ttitle_div = main6.find('div', attrs={'id':"Title"})
        if Ttitle_div is None:
            self.logger.error(u'[XinhuaNews]' + u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        else:
            Ttitle = Ttitle_div.text.strip()

        # 获取发布时间
        title_parents = Ttitle_div.find_parents()
        Tinfo = title_parents[2].find_all('td')[1]
        Ttime = Tinfo.text.strip()
        try:
            Ttime = re.findall(u'\d{4}年\d{2}月\d{2}日.\d{2}:\d{2}:\d{2}', Ttime)[0]
        except:
            self.logger.error(traceback.format_exc())
            Ttime = ""
        if Ttime == "":
            self.logger.error(u'[XinhuaNews]' + u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        else:
            timeArray = time.strptime(Ttime, u"%Y年%m月%d日 %H:%M:%S")
            Ttime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)

        Tauthor = Tinfo.find('font')
        if Tauthor is not None:
            Tauthor = Tauthor.text.strip()
        else:
            Tauthor = None

        # 获取发布内容
        Tcontent = main6.find('div', attrs={'id': "Content"})
        if Tcontent is not None:
            Tcontent = Tcontent.text.strip()
            Tcontent = re.sub(r'\n\t', '', Tcontent)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        return Ttitle, Ttime, Tauthor, Tcontent

    def crawlMain7(self, main7):
        # 获取标题
        error = ""
        Ttitle = main7.find('h1')
        if Ttitle is None:
            self.logger.error(u'[XinhuaNews]' + u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        else:
            Ttitle = Ttitle.text.strip()

        # 获取发布时间
        Ttime = main7.find('span', attrs={'class': "time"})
        if Ttime is not None:
            Ttime = Ttime.text.strip()
            timeArray = time.strptime(Ttime, u"%Y年%m月%d日 %H:%M:%S")
            Ttime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None

        if len(Ttime) == 16:
            Ttime = Ttime + ':00'

        # 获取发布作者
        Tauthor = main7.find('em', attrs={'id': "source"})
        if Tauthor is not None:
            Tauthor = Tauthor.text.strip()
        else:
            Tauthor = None

        # 获取发布内容
        Tcontent = main7.find('div', attrs={'class': "article"})
        if Tcontent is not None:
            Tcontent = Tcontent.text.strip()
            Tcontent = re.sub(r'\n\t', '', Tcontent)
        else:
            self.logger.error(u'[XinhuaNews]' + u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
            return None, None, None, None
        return Ttitle, Ttime, Tauthor, Tcontent










