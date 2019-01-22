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
import datetime, time
import random
import math


class IFengNewsCrawler(object):
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
            # self.crawlStatistics(article)
            if article is not None and article not in articleList:
                # 同一文章可能会在搜索结果出现多次，在baidu的结果url是不重复，但是实际可能指向同一文章，需要去重
                articleList.append(article)
        return articleList

    def crawlStatistics(self, article): #下次直接获得要统计的变量而不用爬整个网页
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        meta_info = article.meta_info #凤凰网的meta_info我保存的是doc_url值,因为有几种格式，要么是subxxxx_0 要么是文章url
        # print len(meta_info)
        data1 = {
            'callback': 'newCommentListCallBack',
            'doc_url':meta_info,
            'job': '1',
            'callback': 'newCommentListCallBack'
        }
        re_url = 'http://comment.ifeng.com/get.php'
        html1 = self.session.download(re_url, encoding='gbk', data=data1, timeout=10, retry=3, addr=False, isJson=True)

        article.statistics.reply_count = html1['count']  #如果还需要其他统计数可以继续添加

    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        html = self.session.download(url, encoding='utf-8', data=None, timeout=10, retry=3, addr=True)

        if html:
            article_url = html['url']
            if article_url.find(self.channel.url)<0:
                self.logger.warn('Unrelated url found:%s',url)
                return None
            article_url = re.findall(r'.*?\.html|.*?\.htm|.*?\.shtml|.*?\.shtm', article_url)[0]
            self.logger.debug('[iFengnews]' + article_url)
            soup = BeautifulSoup(html['html'], 'lxml')  # 'html.parser' 解析器
            main = soup.find('div', attrs={'class': "main"})
            main1 = soup.find('div', attrs={'class': "yc_main"})

            if main is not None:
                self.logger.debug(u'走第一种格式')
                Ttitle = main.find('h1').text.strip()  # 标题
                if Ttitle is None:
                    self.logger.error(u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ',url)
                    return
                else:
                    Tpublishtime = main.find('span', attrs={'class': "ss01"}).text.strip()
                    if Tpublishtime is None:
                        self.logger.error(u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ',url)
                        return
                    else:
                        Tpublishtime = Tpublishtime.replace(u'年', '-').replace(u'月', '-').replace(u'日', '')

                        Tauthor = main.find('a', attrs={'target': "_blank"})
                        if Tauthor is not None:
                            Tauthor = Tauthor.text.strip()
                        else:
                            Tauthor = 'None'
                        Tcontent = main.find('div', attrs={'id': "main_content"})
                        # print Tcontent
                        # Tcontent = Tcontent.find('p')
                        if Tcontent is not None:
                            Tcontent = Tcontent.text.strip()
                            Tcontent = re.sub(r'\n|\t', '', Tcontent)
                        else:
                            self.logger.error(u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ',url)
                            return

                        doc_url = re.findall(r'"commentUrl":"(.*)",', html['html'])
                        if doc_url:
                            doc_url = doc_url[0]
                        else:
                            doc_url = url

                        data1 = {
                            'callback': 'newCommentListCallBack',
                            # 'doc_url': 'http://gd.ifeng.com/a/20171010/6053241_0.shtml',
                            'doc_url': doc_url,
                            'job': '1',
                            'callback': 'newCommentListCallBack'
                        }
                        re_url = 'http://comment.ifeng.com/get.php'
                        html1 = self.session.download(re_url, encoding='gbk', data=data1, timeout=10, retry=3,
                                                      addr=False, isJson=True)
                        # html1 = json.loads( html1[html1.find('=') + 1:html1.rfind(';c')] )

                        Treply = html1['count']
                        if len(html1['comments']) is not 0:
                            articleid = html1['comments'][0]['article_id']
                        else:
                            articleid = article_url
                            articleid = articleid[articleid.find('a/') + 2:-6]  # 由于数据库字段长度有限，所以截取部分作为ID
                            self.logger.warn(u'地址 %s 没有评论因此以url地址部分字段作为tid',article_url)

                        meta_info = doc_url

                        article = Article(articleid, self.channel.channel_id, Ttitle, Tcontent, Tpublishtime, article_url,None, Tauthor,meta_info=meta_info)
                        article.statistics.reply_count = Treply

                        self.logger.info(article)
                        return article

            ##对第二种格式的爬取
            if main1 is not None:
                self.logger.debug(u'走第二种格式')
                Ttitle = main1.find('h1').text.strip()
                if Ttitle is None:
                    self.logger.error(u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ',url)
                    return
                else:
                    Tpublishtime = main1.find('span').text.strip()
                    if Tpublishtime is None:
                        self.logger.error(u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ',url)
                        return

                # return Tpublishtime
                Tauthor = main1.find('a', attrs={'target': "_blank"})
                if Tauthor is not None:
                    Tauthor = Tauthor.text.strip()
                else:
                    Tauthor = 'None'
                Tcontent = main1.find('div', attrs={'class': "yc_con_txt"})

                if Tcontent is not None:
                    Tcontent = Tcontent.text.strip()
                    Tcontent = re.sub(r'\n|\t', '', Tcontent)
                else:
                    self.logger.warn(u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ',url)
                    return

                doc_url = re.findall(r'"commentUrl":"(.*)",', html['html'])
                if doc_url:
                    doc_url = doc_url[0]
                else:
                    doc_url = url

                data1 = {
                    'callback': 'newCommentListCallBack',
                    # 'doc_url': 'http://gd.ifeng.com/a/20171010/6053241_0.shtml',
                    'doc_url': doc_url,
                    'job': '1',
                    'callback': 'newCommentListCallBack'
                }
                re_url = 'http://comment.ifeng.com/get.php'
                html1 = self.session.download(re_url, encoding='gbk', data=data1, timeout=10, retry=3, addr=False,
                                              isJson=True)
                # html1 = json.loads( html1[html1.find('=') + 1:html1.rfind(';c')] )
                try:
                    Treply = html1['count']
                except:
                    Treply = None

                if len(html1['comments']) is not 0:
                    articleid = html1['comments'][0]['article_id']
                else:
                    articleid = url.strip()
                    articleid = articleid[articleid.find('a/') + 2:-6]  # 由于数据库字段长度有限，所以截取部分作为ID
                    self.logger.warn(u'地址 %s 没有评论因此以url地址部分字段作为tid', article_url)

                meta_info = doc_url
                article = Article(articleid, self.channel.channel_id, Ttitle, Tcontent, Tpublishtime, article_url, None,
                                  Tauthor, meta_info=meta_info)
                article.statistics.reply_count = Treply
                self.logger.info(article)
                return article

            if (main is None) and (main1 is None):
                self.logger.warn(u"存在另外一种html格式：：%s",url)
                return

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

        html = self.session.download(article.url, encoding='utf-8', data=None, timeout=10, retry=3, addr=True,
                                     isJson=False)
        # meta_info = article.meta_info
        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        # add_datetime = time.mktime(time.strptime('','%Y-%m-%d'))
        commentList = list()
        page = 1
        while page < 30:
            doc_url = re.findall(r'"commentUrl":"(.*)",', html['html'])
            if doc_url:
                doc_url = doc_url[0]
            else:
                doc_url = article.url

            data1 = {
                'callback': 'newCommentListCallBack',
                'orderby': '',
                'docUrl': doc_url,
                'job': '1',
                'p': page,
                'callback': 'newCommentListCallBack'
            }
            re_url = 'http://comment.ifeng.com/get.php'
            html1 = self.session.download(re_url, encoding='gbk', data=data1, timeout=10, retry=3, isJson=True)
            totalcount = html1['count']  # 评论总数
            if totalcount == 0:
                break
            comments = html1['comments']
            if comments:
                for comment in comments:
                    cid = comment['comment_id']
                    user_id = comment['user_id']
                    user_name = comment['uname']
                    user_ip = comment['client_ip']
                    # ip_address = get_ip_address(self, str(user_ip))  # 并没有获取到值
                    # if ip_address is '':
                    try:
                        ip_address = comment['ip_from']
                    except:
                        ip_address=None
                    # ip_address = comment['ip_from']
                    user_head = comment['user_url']
                    publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(float(comment['create_time'])))
                    reply_userid = comment['parent']  # 评论的回复
                    if reply_userid:
                        reply_userid = comment['parent'][0]['user_id']
                    else:
                        reply_userid = ''
                    like_count = comment['uptimes']
                    unlike_count = None
                    read_count = None
                    reply_count = None
                    source_url = article.url
                    content = comment['comment_contents']
                    heat = 0
                    location_coutry = 'CN'

                    if ip_address is None:
                        commentList.append(Comment(article.tid, self.channel.channel_id, cid,
                                               add_datetime, publish_datetime,
                                               user_ip,None,None,None,   ###这里的ip_address还未实现
                                               user_id, user_name, content, reply_userid,
                                               None, like_count, reply_count, dislike_count=None
                                               ))
                    else:
                        try:
                            location_region = ip_address[:ip_address.find(u'省') + 1]
                            location_city = ip_address[ip_address.find(u'省') + 1:]
                        except:
                            location_region=None
                            location_city =None
                        commentList.append(Comment(article.tid, self.channel.channel_id, cid,
                                                   add_datetime, publish_datetime,
                                                   user_ip, location_coutry,location_region, location_city,
                                                   user_id, user_name, content, reply_userid,
                                                   None, like_count, reply_count, dislike_count=None
                                                   ))

                page = page + 1
                totalpage = math.ceil(totalcount / 20.0)  # 计算评论总页数，向上取整
                if totalpage < page:
                    break
            else:
                break
        return (commentList, False)  #测试的时候 article[0][222].content  可以取出第222条的评论内容


def get_ip_address(self, ip):
    url1 = 'http://ip.taobao.com/service/getIpInfo.php?ip='
    url2 = 'http://ip.taobao.com/service/getIpInfo2.php?ip='
    url = random.choice([url1, url2])
    if '*' in ip:
        ip = ip.replace('*', '0')
    u = url + ip
    cookies = 'thw=cn; PHPSESSID=r1308s6fujg00i80sp7j7er9r4'
    try:
        html = self.session.download(u, retry=10, cookies=cookies, timeout=0.5, isJson=True)
        main = html['data']
        country = main['country']
        region = main['region'].replace('省', '').replace('市', '').replace('自治', '').replace('区', '').replace('洲',
                                                                                                             '')
        city = main['city'].replace('省', '').replace('市', '').replace('自治', '').replace('区', '').replace('洲', '')
        address = [country, region, city]
        # print ';'.join(address)
        # address = ';'.join(address)
        return address
    except:
        return ''

