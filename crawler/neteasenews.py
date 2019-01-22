# -*- coding:utf-8 -*-
'''
Created on 8 Oct 2017

@author: eyaomai
'''

from com.naswork.sentiment.crawler.baidu import BaiduCrawler
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
import re, json, traceback
from bs4 import BeautifulSoup
import datetime, time
class NeteaseNewsCrawler(object):
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
        self.session = self.baiduCrawler.session
        self.channel = channel
    
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
        for url in urls:
            article = self.crawlArticle(url)
            if article is not None and article not in articleList:
                #同一文章可能会在搜索结果出现多次，在baidu的结果url是不重复，但是实际可能指向同一文章，需要去重
                articleList.append(article)
        return articleList

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        cookies = 'Province=020; City=020; usertrack=c+5+hljsm+B+cg5MA7YDAg==; vjuids=-7517fab.15b5c40e631.0.a042d54907b81; _ntes_nnid=5e90ea8f4ef321150e3b5d43f68870c8,1491901408828; _ntes_nuid=5e90ea8f4ef321150e3b5d43f68870c8; UM_distinctid=15b5c41b7836eb-0fd2f7e510ef22-4e45042e-100200-15b5c41b78461b; __gads=ID=18c804c9f3ead780:T=1491901995:S=ALNI_MYWNxLkcHVgXyExP9eeFcD-mj7SiQ; afpCT=1; CNZZDATA1256734798=337963631-1491900970-http%253A%252F%252Fnews.163.com%252F%7C1492767097; CNZZDATA1256336326=1559830613-1491900088-http%253A%252F%252Fnews.163.com%252F%7C1492765460; vjlast=1491901409.1492754596.11; ne_analysis_trace_id=1492768109053; vinfo_n_f_l_n3=09c375e3d4394d15.1.13.1491901408836.1492766182939.1492768266676; s_n_f_l_n3=09c375e3d4394d151492768109056'
        try:
            self.logger.info("[crawlStatistics]" + article.tid)
            if len(article.tid) != 16:
                articleid = article.tid[3:len(article.tid) - 2]
            else:
                articleid = article.tid
            re_url = 'http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/' + articleid
            html1 = self.session.download(url=re_url, encoding='utf-8', data=None, isJson=True, timeout=10, retry=3)
            article.statistics.reply_count = html1["tcount"]
        except:
            self.logger.error('[SinaStatistics]url:' + article.url + ', tid:' + article.tid + ', %s' + traceback.format_exc())
            return

    
    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        cookies = 'Province=020; City=020; usertrack=c+5+hljsm+B+cg5MA7YDAg==; vjuids=-7517fab.15b5c40e631.0.a042d54907b81; _ntes_nnid=5e90ea8f4ef321150e3b5d43f68870c8,1491901408828; _ntes_nuid=5e90ea8f4ef321150e3b5d43f68870c8; UM_distinctid=15b5c41b7836eb-0fd2f7e510ef22-4e45042e-100200-15b5c41b78461b; __gads=ID=18c804c9f3ead780:T=1491901995:S=ALNI_MYWNxLkcHVgXyExP9eeFcD-mj7SiQ; afpCT=1; CNZZDATA1256734798=337963631-1491900970-http%253A%252F%252Fnews.163.com%252F%7C1492767097; CNZZDATA1256336326=1559830613-1491900088-http%253A%252F%252Fnews.163.com%252F%7C1492765460; vjlast=1491901409.1492754596.11; ne_analysis_trace_id=1492768109053; vinfo_n_f_l_n3=09c375e3d4394d15.1.13.1491901408836.1492766182939.1492768266676; s_n_f_l_n3=09c375e3d4394d151492768109056'
        html = self.session.download(url, encoding='gbk', data=None, timeout=10, retry=3, addr=True, cookies=cookies)
        if html:
            article_url = html['url']
            article_url = re.findall(r'.*?\.html|.*?\.htm|.*?\.shtml|.*?\.shtm', article_url)[0]
            self.logger.info(article_url)
            soup = BeautifulSoup(html['html'], 'html.parser')
            main = soup.find('div', attrs={'class': "post_content_main"})
            main1 = soup.find('div', attrs={'class': "ep-content-main"})

            #第一种网页格式
            if main is not None:

                #获取标题
                Ttitle = main.find('h1')
                if Ttitle is None:
                    self.logger.error('[NeteaseNews]' + '缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                else:
                    Ttitle = Ttitle.text.strip()

                #获取发布时间
                Ttime = main.find('div', attrs={'class': "post_time_source"})
                if Ttime is not None:
                    Ttime = Ttime.text.strip()
                    Ttime = re.findall(r'\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}', Ttime)[0]
                else:
                    self.logger.error('[NeteaseNews]' + '缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
                    return

                if len(Ttime) == 16:
                    Ttime = Ttime + ':00'

                #获取发布作者
                Tauthor = main.find('div', attrs={'class': "post_time_source"})
                if Tauthor is not None:
                    Tauthor = Tauthor.find('a')
                    if Tauthor is not None:
                        Tauthor = Tauthor.text.strip()
                    else:
                        Tauthor = None

                #获取发布内容
                Tcontent = main.find('div', attrs={'class': "post_text"})
                if Tcontent is not None:
                    Tcontent = Tcontent.text.strip()
                    Tcontent = re.sub(r'\n|\t', '', Tcontent)
                    dr = re.compile(r'<[^>]+>', re.S)
                    Tcontent = dr.sub('', Tcontent)
                else:
                    self.logger.error('[NeteaseNews]' + '缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
                    return

                    # 获取评论数
                articleid = ""
                try:
                    articleid = re.findall(r'"docId" : "(.*)",', html['html'])[0]
                    re_url = 'http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/' + articleid
                    html1 = self.session.download(url=re_url, encoding='utf-8', data=None, isJson=True, timeout=10, retry=3)
                    Treply = html1["tcount"]
                except:
                    Treply = None
                    self.logger.error('[NeteaseComment]url:' + article_url + ', tid:' + articleid + ', %s' + traceback.format_exc())
                finally:
                    article = Article(tid=articleid, channel_id=self.channel.channel_id, title=Ttitle,
                                      content=Tcontent, publish_datetime=Ttime, url=article_url,
                                      author_name=Tauthor)
                    article.statistics.reply_count = Treply
                    return article

            #第二种网页格式
            elif main1 is not None:

                #标题
                Ttitle = main1.find('h1')
                if Ttitle is None:
                    self.logger.error('[NeteaseNews]' + '缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                else:
                    Ttitle = Ttitle.text.strip()

                #发布的时间
                Ttime = main1.find('div', attrs={'class': "ep-time-source cDGray"})
                Ttime1 = main1.find('div',  attrs={'class': "ep-info cDGray"})
                if Ttime is not None:
                    Ttime = Ttime.text.strip();
                    Ttime = re.findall(r'\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}')[0]
                elif Ttime1 is not None:
                    Ttime = Ttime1.text.strip()
                    Ttime = re.findall(r'\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}')[0]
                else:
                    self.logger.error('[NeteaseNews]' + '缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                if len(Ttime) == 16:
                    Ttime = Ttime + ':00'

                #获取作者信息
                Tauthor = main1.find('div', attrs={'class': "ep-time-soure cDGray"})
                Tauthor1 = main1.find('div', attrs={'class': "ep-source cDGray"})
                if Tauthor is not None:
                    Tauthor = Tauthor.find('a')
                    if Tauthor is not None:
                        Tauthor = Tauthor.text.strip()
                    else:
                        Tauthor = None
                elif Tauthor1 is not None:
                    Tauthor = Tauthor1.find('span')
                    if Tauthor is not None:
                        Tauthor = Tauthor.text.strip()
                        print Tauthor
                        Tauthor = re.findall(r'来源：(.*)"', Tauthor)[0]
                    else:
                        Tauthor = None
                else:
                    Tauthor = None

                #获取内容
                Tcontent = main1.find('div', attrs={'id': "endText"})
                if Tcontent is not None:
                    Tcontent = Tcontent.text.strip()
                    Tcontent = re.sub(r'\n|\t', '', Tcontent)
                    dr = re.compile(r'<[^>]+>', re.S)
                    Tcontent = dr.sub('', Tcontent)
                else:
                    self.logger.error('[SinaNews]' + '缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
                    return

                #获取评论数
                try:
                    articleid = re.findall(r'"docId" : "(.*)",', html['html'])[0]
                    re_url = 'http://sdk.comment.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/' + articleid
                    html1 = json.load(self.session.download(re_url, encoding='utf-8', data=None, isJson=True, timeout=10, retry=3))
                    Treply = html1['tcount']
                except:
                    Treply = None
                    self.logger.error('[NeteaseComment]url:' + article_url + ', tid:' + articleid + ', %s' + traceback.format_exc())
                finally:
                    article = Article(tid=articleid, channel_id=self.channel.channel_id, title=Ttitle, content=Tcontent, publish_datetime=Ttime, url=article_url, author_name=Tauthor)
                    self.logger.debug("[crawlArticle]" + article.tid)
                    article.statistics.reply_count = Treply
                    return article

    def refreshSearch(self):
        '''
        重置搜索
        '''
        pass
    
    def refreshCommentCrawler(self):
        '''
        重置评论爬取
        '''
        pass
        
    def crawlComment(self, article):
        '''
        根据文章，爬取文章的评论，返回评论列表
        @return: (commentList, hasnext)二元组，commentList是指评论数组（每个元素是Comment实例），hasnext表示是否还有要爬取
        '''
        cookies = 'Province=020; City=020; usertrack=c+5+hljsm+B+cg5MA7YDAg==; vjuids=-7517fab.15b5c40e631.0.a042d54907b81; _ntes_nnid=5e90ea8f4ef321150e3b5d43f68870c8,1491901408828; _ntes_nuid=5e90ea8f4ef321150e3b5d43f68870c8; UM_distinctid=15b5c41b7836eb-0fd2f7e510ef22-4e45042e-100200-15b5c41b78461b; __gads=ID=18c804c9f3ead780:T=1491901995:S=ALNI_MYWNxLkcHVgXyExP9eeFcD-mj7SiQ; afpCT=1; CNZZDATA1256734798=337963631-1491900970-http%253A%252F%252Fnews.163.com%252F%7C1492767097; CNZZDATA1256336326=1559830613-1491900088-http%253A%252F%252Fnews.163.com%252F%7C1492765460; vjlast=1491901409.1492754596.11; ne_analysis_trace_id=1492768109053; vinfo_n_f_l_n3=09c375e3d4394d15.1.13.1491901408836.1492766182939.1492768266676; s_n_f_l_n3=09c375e3d4394d151492768109056'
        if len(article.tid) != 16:
            articleid = article.tid[3:len(article.tid) - 2]
        else:
            articleid = article.tid
        re_url = 'http://comment.news.163.com/api/v1/products/a2869674571f77b5a0867c3d71db5856/threads/' + articleid+ '/comments/newList'
        commentList = list()
        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        page = 0
        while page < 750:
            data1 = {
                'offset': page,
                'limit': 30,
                'showLevelThreshold': 72,
                'headLimit': 1,
                'tailLimit': 2,
                'ibc': 'newspc'
            }
            try:
                html1 = self.session.download(url=re_url, encoding='utf-8', cookies=cookies, data=data1, timeout=10, retry=3)
                html1 = json.loads(html1)
                totalcount = html1["newListSize"]

                if totalcount == 0:
                    break
                for i in html1['comments'].itervalues():
                    cid = i['commentId']
                    user_id = i['user']['userId']
                    if user_id == 0:
                        user_name = ''
                    else:
                        user_name = i['user']['nickname']
                    user_ip = ''
                    location = i['user']['location'].replace(u'市', ':').replace(u'自治', ':').replace(u'新区', ':').replace(u'区',':').replace(u'洲', ':')
                    location_list = location.split(':')

                    location_country = location_list[0]
                    if len(location_list) > 1:
                        location_region = location_list[1]
                    else:
                        location_region = ''
                    if len(location_list) > 2:
                        location_city = location_list[2]
                    else:
                        location_city = ''
                    publish_datetime = i['createTime']
                    like_count = i['vote']
                    unlike_count = i['against']
                    content = i['content']
                    dr = re.compile(r'<[^>]+>', re.S)
                    content = dr.sub('', i['content'])
                    commentList.append(Comment(articleid, self.channel.channel_id, cid,
                                               add_datetime, publish_datetime,
                                               user_ip, location_country, location_region, location_city,
                                               user_id, user_name, content, None,
                                               None, like_count, None, unlike_count
                                               ))
                # print page, totalcount
                if page > int(totalcount):
                    break
                page = page + 30
            except:
                self.logger.error('[NeteaseComment]url:' + article.url + ', tid:' + article.tid + ', %s' + traceback.format_exc())
                return
            finally:
                return (commentList, False)