# -*- coding:utf-8 -*-
'''
Created on 8 Oct 2017

@author: eyaomai
'''

from com.naswork.sentiment.crawler.baidu import BaiduCrawler
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
import re, json
from bs4 import BeautifulSoup
import math
import datetime, time, traceback
class SinaNewsCrawler(object):
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
        cookies = 'U_TRS1=000000fa.9fe376b4.58573ebc.bde2f2c3; UOR=,vip.stock.finance.sina.com.cn,; vjuids=3923fcfb8.15914cd122a.0.e347599b65a6; SINAGLOBAL=183.63.92.250_1482112700.861930; SUB=_2AkMvC7H0f8NhqwJRmP4WzWzrb4xwzgnEieLBAH7sJRMyHRl-yD83qlNetRBAqqE4nv4pjjxQaUfLZo_Os-Bxsw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WFZzJ6nbHTRfVEqOXp-S.5z; SGUID=1482721389362_efec0e8d; vjlast=1488765553.1489054965.10; bdshare_firstime=1492414283526; _ct_uid=58f46f61.537a7929; lxlrtst=1492423120_o; rotatecount=2; Apache=59.42.29.149_1492670298.869113; ULV=1492670299361:18:6:6:59.42.29.149_1492670298.869113:1492670298484; afpCT=1; CNZZDATA1252916811=1442218969-1492654141-http%253A%252F%252Fnews.sina.com.cn%252F%7C1492664941; UM_distinctid=15b8a154522e79-0a3f79bddc9d05-4e45042e-100200-15b8a154523a49; CNZZDATA5399792=cnzz_eid%3D349789736-1492650802-http%253A%252F%252Fnews.sina.com.cn%252F%26ntime%3D1492667002; U_TRS2=00000095.1c285e96.58f85761.e07aa962; lxlrttp=1492423120'
        html = self.session.download(article.url, encoding='utf-8', data=None, timeout=10, retry=3, addr=True, cookies=cookies)
        re_url = 'http://comment5.news.sina.com.cn/page/info'
        channel = re.findall(r"channel: '(.*)',", html['html'])[0]
        newsid = re.findall(r"newsid: '(.*)',", html['html'])[0]
        data = {
            'format': 'js',
            'channel': channel,
            'newsid': newsid,
            'group': '',
            'compress': '1',
            'ie': 'gbk',
            'oe': 'gbk',
            'page': '1',
            'page_size': '20'
        }
        try:
            html1 = self.session.download(re_url, encoding='utf-8', data=data, isJson=False, timeout=10, retry=3, addr=False)
            html1 = re.sub(r'(.*=)\{', '{', html1)
            html1 = json.loads(html1)
            article.statistics.reply_count = html1['result']['count']['show']
        except:
            self.logger.error('[SinaStatistics]url:' + article.url + ', tid:' + article.tid + '%s' + traceback.format_exc())
            return
    
    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        cookies = 'U_TRS1=000000fa.9fe376b4.58573ebc.bde2f2c3; UOR=,vip.stock.finance.sina.com.cn,; vjuids=3923fcfb8.15914cd122a.0.e347599b65a6; SINAGLOBAL=183.63.92.250_1482112700.861930; SUB=_2AkMvC7H0f8NhqwJRmP4WzWzrb4xwzgnEieLBAH7sJRMyHRl-yD83qlNetRBAqqE4nv4pjjxQaUfLZo_Os-Bxsw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WFZzJ6nbHTRfVEqOXp-S.5z; SGUID=1482721389362_efec0e8d; vjlast=1488765553.1489054965.10; bdshare_firstime=1492414283526; _ct_uid=58f46f61.537a7929; lxlrtst=1492423120_o; rotatecount=2; Apache=59.42.29.149_1492670298.869113; ULV=1492670299361:18:6:6:59.42.29.149_1492670298.869113:1492670298484; afpCT=1; CNZZDATA1252916811=1442218969-1492654141-http%253A%252F%252Fnews.sina.com.cn%252F%7C1492664941; UM_distinctid=15b8a154522e79-0a3f79bddc9d05-4e45042e-100200-15b8a154523a49; CNZZDATA5399792=cnzz_eid%3D349789736-1492650802-http%253A%252F%252Fnews.sina.com.cn%252F%26ntime%3D1492667002; U_TRS2=00000095.1c285e96.58f85761.e07aa962; lxlrttp=1492423120'
        html = self.session.download(url, encoding='utf-8', data=None, timeout=10, retry=3, addr=True, cookies=cookies)
        if html:
            article_url = html['url']
            article_url = re.findall(r'.*?\.html|.*?\.htm|.*?\.shtml|.*?\.shtm', article_url)[0]
            self.logger.info('[SinaNews]'+article_url)
            #获取发布时间
            date = re.findall(r'/(\d{4}-\d{2}-\d{2})/', article_url)
            if len(date) == 0:
                return None
            # if date[0] < '2015-07-01':
            #     html = self.session.download(url, encoding='gbk', data=None, timeout=10, retry=3, addr=True)

            soup = BeautifulSoup(html['html'], 'lxml')
            main = soup.find('div', attrs={'class': "wrap-inner"})
            main1 = soup.find('div', attrs={'class': "Main clearfix"})
            main2 = soup.find('div', attrs ={'class': "main-content w1240"})

            #第一种网页格式
            if main is not None:
                self.logger.debug('走第一种格式')
                #获取标题
                Ttitle = main.find('h1', attrs={'id': "artibodyTitle"})
                if Ttitle is None:
                    self.logger.error('[SinaNews]' + '缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                else:
                    Ttitle = Ttitle.text.strip()

                #获取发布时间
                Ttime = main.find('span', attrs={'class': 'time-source'})
                if Ttime is not None:
                    Ttime = Ttime.text.strip()
                    Ttime = re.findall(r'(\d{4})\D(\d{2})\D(\d{2})\D(\d{2}:\d{2}).*', Ttime)[0]
                    Ttime = Ttime[0] + '-' + Ttime[1] + '-' + Ttime[2] + ' ' + Ttime[3]
                else:
                    self.logger.error('[SinaNews]' + '缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                if len(Ttime) == 16:
                    Ttime = Ttime + ':00'

                #获取作者信息
                Tauthor = soup.find('span', attrs={'class': "time-source"})
                if Tauthor is not None:
                    Tauthor = Tauthor.find('a')
                    if Tauthor is not None:
                        Tauthor = Tauthor.text.strip()
                    else:
                        Tauthor = None
                else:
                    Tauthor = None

                #获取内容
                Tcontent = main.find('div', attrs={'id': "artibody"})
                if Tcontent is not None:
                    Tcontent = Tcontent.text.strip()
                    Tcontent = re.sub(r'\n|\t', '', Tcontent)
                else:
                    self.logger.error('[SinaNews]' + '缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
                    return

                #获取评论数
                try:
                    channel = re.findall(r"channel: '(.*)',", html['html'])[0]
                    newsid = re.findall(r"newsid: '(.*)',", html['html'])[0]
                    data = {
                        'format': 'js',
                        'channel': channel,
                        'newsid': newsid,
                        'group': '',
                        'compress': '1',
                        'ie': 'gbk',
                        'oe': 'gbk',
                        'page': '1',
                        'page_size': '20'
                    }
                    re_url = 'http://comment5.news.sina.com.cn/page/info'
                    html1 = self.session.download(re_url, encoding='utf-8', data=data, isJson=False, timeout=10, retry=3, addr=False)
                    html1 = re.sub(r'(.*=)\{', '{', html1)
                    html1 = json.loads(html1)
                    totalcount = html1['result']['count']['show']
                    Treply = totalcount
                except:
                    Treply = None

                # 获取文章的id
                articleid = re.findall(r'([a-z]{8}\d{7})', article_url)[0]

                article = Article(tid=articleid, channel_id=self.channel.channel_id, title=Ttitle, publish_datetime=Ttime, content=Tcontent, url=article_url, author_name=Tauthor)
                article.statistics.reply_count = Treply
                return article

            #第二种网页格式
            elif main1 is not None:
                self.logger.debug('走第二种格式')
                #获取标题
                Ttitle = main1.find('h1', attrs={'id': "artibodyTitle"})
                if Ttitle is None:
                    self.logger.error('[SinaNews]' + '缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                else:
                    Ttitle = Ttitle.text.strip()

                #获取时间
                Ttime = main1.find('span', attrs={'id': "pub_date"})
                if Ttime is not None:
                    Ttime = Ttime.text.strip()
                    Ttime = re.findall(r'(\d{4})\D(\d{2})\D(\d{2})\D(\d{2}:\d{2})', Ttime)[0]
                    Ttime = Ttime[0] + '-' + Ttime[1] + '-' + Ttime[2] + ' ' + Ttime[3]
                else:
                    self.logger.error('[SinaNews]' + '缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                if len(Ttime) == 16:
                    Ttime = Ttime + ':00'

                #获取发布者
                Tauthor = main1.find('span', attrs={'id': "media_name"})
                if Tauthor is not None:
                    Tauthor = Tauthor.find('a').text.strip()
                else:
                    Tauthor = None

                #获取内容
                Tcontent = main1.find('div', attrs={'id': "artibody"})
                if Tcontent is not None:
                    Tcontent = Tcontent.text.strip()
                    Tcontent = re.sub(r'\n|\t', '', Tcontent)
                else:
                    self.logger.error('[SinaNews]' + '缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
                    return

                try:
                    channel = re.findall(r"channel: '(.*)',", html['html'])[0]
                    newsid = re.findall(r"newsid: '(.*)',", html['html'])[0]
                    data = {
                        'format': 'js',
                        'channel': channel,
                        'newsid': newsid,
                        'group': '',
                        'compress': '1',
                        'ie': 'gbk',
                        'oe': 'gbk',
                        'page': '1',
                        'page_size': '20'
                    }
                    re_url = 'http://comment5.news.sina.com.cn/page/info'
                    html1 = self.session.download(re_url, encoding='utf-8', data=data, isJson=False, timeout=10, retry=3, addr=False)
                    html1 = re.sub(r'(.*=)\{', '{', html1)
                    html1 = json.loads(html1)
                    totalcount = html1['result']['count']['show']
                    Treply = totalcount
                except:
                    Treply = None

                # 获取文章的id
                articleid = re.findall(r'([a-z]{8}\d{7})', article_url)[0]

                article = Article(tid=articleid, channel_id=self.channel.channel_id,title=Ttitle, content=Tcontent, publish_datetime=Ttime, url=article_url, author_name=Tauthor)
                article.statistics.reply_count = Treply
                return article

            #第三种网页格式
            elif main2 is not None:
                self.logger.debug(u'第三种格式')
                #获取标题
                Ttitle = main2.find('div', attrs={'class': "second-title"})

                if Ttitle is None:
                    self.logger.error('[SinaNews]' + '缺少标题，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                else:
                    Ttitle = Ttitle.text.strip()

                # 获取时间
                Ttime = main2.find('span', attrs={'class': "date"})
                if Ttime is not None:
                    Ttime = Ttime.text.strip()
                    Ttime = re.findall(r'(\d{4})\D(\d{2})\D(\d{2})\D ' '(\d{2}:\d{2}).*', Ttime)[0]
                    Ttime = Ttime[0] + '-' + Ttime[1] + '-' + Ttime[2] + ' ' + Ttime[3]
                else:
                    self.logger.error('[SinaNews]' + '缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取')
                    return
                if len(Ttime) == 16:
                    Ttime = Ttime + ':00'

                # 获取发布者
                Tauthor = main2.find('a', attrs={'class': "source"})
                if Tauthor is not None:
                    Tauthor = Tauthor.text.strip()
                else:
                    Tauthor = None

                # 获取内容
                Tcontent = main2.find('div', attrs={'id': "article"})
                if Tcontent is not None:
                    Tcontent = Tcontent.text.strip()
                    Tcontent = re.sub(r'\n|\t', '', Tcontent)
                else:
                    self.logger.error('[SinaNews]' + '缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取')
                    return

                # 获取评论数
                try:
                    channel = re.findall(r"channel: '(.*)',", html['html'])[0]
                    newsid = re.findall(r"newsid: '(.*)',", html['html'])[0]
                    data = {
                        'format': 'js',
                        'channel': channel,
                        'newsid': newsid,
                        'group': '',
                        'compress': '1',
                        'ie': 'gbk',
                        'oe': 'gbk',
                        'page': '1',
                        'page_size': '20'
                    }
                    re_url = 'http://comment5.news.sina.com.cn/page/info'
                    html1 = self.session.download(re_url, encoding='utf-8', data=data, isJson=False, timeout=10, retry=3, addr=False)
                    html1 = re.sub(r'(.*=)\{', '{', html1)
                    html1 = json.loads(html1)
                    totalcount = html1['result']['count']['show']
                    Treply = totalcount
                except:
                    Treply = None

                # 获取文章的id
                articleid = re.findall(r'([a-z]{8}\d{7})', article_url)[0]

                article = Article(tid=articleid, channel_id=self.channel.channel_id, title=Ttitle, publish_datetime=Ttime, content=Tcontent, url=article_url, author_name=Tauthor)
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
        html = self.session.download(article.url, encoding='utf-8', data=False, timeout=10, retry=3, addr=True)
        channel = re.findall(r"channel: '(.*)',", html['html'])[0]
        newsid = re.findall(r"newsid: '(.*)',", html['html'])[0]
        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        commentList = list()
        page = 1
        self.logger.info(article.url)
        try:
            while page < 30:
                data ={
                    'channel': channel,
                    'newsid': newsid,
                    'group': '',
                    'compress': '1',
                    'ie': 'gbk',
                    'oe': 'gbk',
                    'page': page,
                    'page_size': '20'
                }
                re_url = 'http://comment5.news.sina.com.cn/page/info'
                html1 = self.session.download(url=re_url, encoding='utf-8', data=data, timeout=10, retry=3, addr=True)
                html1 = html1["html"]
                html1 = re.sub(r'(.*=)\{', '{', html1)
                html1 = json.loads(html1)
                totalcount = html1['result']['count']['show']
                if totalcount == 0:
                    break
                cmntlist = html1["result"]["cmntlist"]
                for i in cmntlist:
                    cid = i["mid"]
                    user_id = i["uid"]
                    user_name = i["nick"]
                    user_ip = i["ip"]
                    publish_datetime = i["time"]
                    like_count = i["agree"]
                    content = i["content"]
                    commentList.append(Comment(article.tid, self.channel.channel_id, cid,add_datetime, publish_datetime, user_ip, None, None, None,user_id, user_name,content,None, None, like_count, None, None))

                totalpage = math.ceil(totalcount / 20.0)

                if totalpage < page:
                    break
                page = page + 1
        except:
            self.logger.error(self.logger.error('Fail to parse comment:%s'+traceback.format_exc()))
        finally:
            return (commentList, False)
