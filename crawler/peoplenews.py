# -*- coding:utf-8 -*-
from com.naswork.sentiment.crawler.baidu import BaiduCrawler
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
import re, json
from bs4 import BeautifulSoup
import datetime, time,random

class PeopleNewsCrawler(object):
    '''
    classdocs
    '''

    def __init__(self, channel, logger=None):
        '''
        Constructor
        '''
        self.site = 'people.com.cn'  # 搜索站点

        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger
        self.baiduCrawler = BaiduCrawler(self.logger)
        self.session = self.baiduCrawler.session
        self.channel = channel
    
    def searchArticle(self, keywordList,  endTime):
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
        count = 0
        for url in urls:
            article = self.crawlArticle(url)
            #self.logger.debug(article)

            #同一文章可能会在搜索结果出现多次，在baidu的结果url是不重复，但是实际可能指向同一文章，需要去重
            if article is not None and article not in articleList:
                #count = count +1
                #self.logger.debug(u'文章数量%d',count)
                articleList.append(article)
        return articleList

    def crawlStatistics(self, article):
        '''
        爬取统计信息
        @return: 无需返回参数，统计信息写入article实例
        '''
        pass
    
    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        @return: 返回一个Article实例
        '''
        cookies = '_HY_lvt_c5928ed57343443aa175434a09ea2804=1492582419784; _HY_CTK_c5928ed57343443aa175434a09ea2804=0acc9a79b115c4ca1931c41e303bec28; BAIDU_SSP_lcr=https://www.baidu.com/link?url=NszYD2w_HgkPWqrzDQ3WKApYldw_9MpUVun9r-R09M7r0dh09MUwTHzG087WaJrhBwMCY-7pDfds4xjtWArRf2xh01DHOWWWd9DpBnHwZ03&wd=&eqid=84d5edfe0003dbdd0000000658f6c280; ALLYESID4=0DB901C6E627D980; sso_c=0; wdcid=5838509dcecc0a53; _people_ip_new_code=510000; UM_distinctid=15b802cdbd364d-02e7f218c26ae6-4e45042e-100200-15b802cdbd49ce; wdses=62d3f0f698d07532; sfr=1; CNZZDATA1260954200=1457761991-1492499618-null%7C1492580619; CNZZDATA1260954203=33096124-1492503288-null%7C1492578888; CNZZDATA1256327855=1768205365-1492503342-null%7C1492578947; wdlast=1492582420'
        html = self.session.download(url,data=None,isJson=False, timeout=10, retry=3, addr=True, cookies=cookies)

        soup = BeautifulSoup(html['html'], 'html.parser')  # 'html.parser' 解析器
        try:
            meta = soup.find('meta').attrs['content']
        except:
            self.logger.warn(u'找不到meta里的content')
            return
        # self.logger.error('%s',meta)

        if "GB2312" in meta:
            encoding1 = 'GB2312'
        elif "UTF-8" in meta:
            encoding1 = 'UTF-8'
        elif "utf-8" in meta:
            encoding1 = 'utf-8'
        else:
            encoding1 = 'gbk'

        html = self.session.download(url,encoding=encoding1, data=None, isJson=False, timeout=10, retry=3, addr=True, cookies=cookies)
        # 不同网页编码格式让其重新下载一遍
        soup = BeautifulSoup(html['html'], 'html.parser')  # 'html.parser' 解析器
        main = soup.find('body')

        if html:
            article_url = html['url']
            # self.logger.debug(article_url)
            if article_url.find(self.channel.url)<0:
                self.logger.warn('Unrelated url found:%s',url)
                return None

            # if '.html' not in article_url:
            #     self.logger.error(u'非文章类型网址：%s ',article_url)
            #     return

            try:
                article_url = re.findall(r'.*?\.html|.*?\.htm|.*?\.shtml|.*?\.shtm', article_url)[0]
            except:
                self.logger.error(u'网址后缀不符合：%s ', article_url)
                return

            self.logger.debug('[peoplenews]' + article_url)
            articleid = article_url
            articleid = articleid[articleid.find('cn/') + 3:-5]  # 由于数据库字段长度有限，所以截取部分作为ID
            self.logger.warn(u'地址 %s 以url地址部分字段作为tid', article_url)

            if 'bbs1' not in article_url:

                main1 = soup.find('div',attrs={'class':"i_cont"}) #http://health.people.com.cn/n1/2017/1011/c14739-29579836.html
                main2 = soup.find('div', attrs={'class': "text_c"})  #http://rencai.people.com.cn/n/2014/0721/c244800-25311391.html

                if (main1 is None) and (main2 is None):
                    self.logger.debug(u'走main')
                    try:
                        Ttitle = main.find('h1').text.strip() # 标题
                    except:
                        self.logger.error(u'Ttitle存在走了main部分却不满足其他格式的的url：：%s', article_url)
                        return

                    if Ttitle is None:
                        self.logger.error(u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                        return
                    else:
                        Ttitle_crawl = soup.find('div', attrs={'class': "box01"})#对应一种格式
                        if Ttitle_crawl is None:
                            self.logger.error(u'Ttitle_crawl存在走了main部分却不满足其他格式的的url：：%s',article_url)
                            return
                        try:
                            Tpublishtime = Ttitle_crawl.find('div',attrs={'class':"fl"}).text.strip()
                        except:
                            self.logger.error(u'main中发布时间不匹配')
                            return

                        if Tpublishtime is None:
                            self.logger.error(u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                            return
                        else:
                            # self.logger.error(Tpublishtime)
                            Tpublishtime = re.findall(r'(\d{4})\D(\d{2})\D(\d{2})\D(\d{2}:\d{2}(:\d{2})?)',
                                                      Tpublishtime)[0]

                            # Tpublishtime = Tpublishtime[:18]
                            if len(Tpublishtime[4]) > 1:
                                Tpublishtime = Tpublishtime[0] + '-' + Tpublishtime[1] + '-' + Tpublishtime[2] + ' ' + \
                                               Tpublishtime[3] + Tpublishtime[4]
                            else:
                                Tpublishtime = Tpublishtime[0] + '-' + Tpublishtime[1] + '-' + Tpublishtime[2] + ' ' + \
                                               Tpublishtime[3] + ':00'

                            # Tpublishtime = Tpublishtime.replace(u'年', '-').replace(u'月', '-').replace(u'日', '')


                            Tauthor = Ttitle_crawl.find('a', attrs={'target': "_blank"})
                            if Tauthor is not None:
                                Tauthor = Tauthor.text.strip()
                            else:
                                Tauthor = 'None'
                            Tcontent = soup.find('div', attrs={'class': "box_con"})
                            if Tcontent is not None:
                                Tcontent = Tcontent.text.strip()
                                Tcontent = re.sub(r'\n|\t', '', Tcontent)
                            else:
                                self.logger.error(u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                                return

                            Treply = None #这种格式下没有这些统计可以获取
                            meta_info = None

                            article = Article(articleid, self.channel.channel_id, Ttitle, Tcontent, Tpublishtime,
                                              article_url, None, Tauthor, meta_info=meta_info)
                            article.statistics.reply_count = Treply
                            #self.logger.info(article)
                            return article

                elif (main1 is not None):
                    self.logger.debug(u'走main1')
                    Ttitle = main1.find('h2')  # 标题

                    if Ttitle is None:
                        self.logger.error(u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                        return
                    else:
                        Ttitle = Ttitle.text.strip()

                    try:
                        Tpublishtime = main1.find('div', attrs={'class': "artOri"}).text.strip()
                    except:
                        self.logger.error(u'main1中发布时间不匹配')
                        return

                    if Tpublishtime is None:
                        self.logger.error(u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                        return
                    else:
                        Tpublishtime = re.findall(r'(\d{4})\D(\d{2})\D(\d{2})\D(\d{2}:\d{2}(:\d{2})?)',
                                                  Tpublishtime)[0]
                        # self.logger.error(Tpublishtime)
                        # Tpublishtime = Tpublishtime[:18]
                        if len(Tpublishtime[4]) > 1:
                            Tpublishtime = Tpublishtime[0] + '-' + Tpublishtime[1] + '-' + Tpublishtime[2] + ' ' + \
                                           Tpublishtime[3] + Tpublishtime[4]
                        else:
                            Tpublishtime = Tpublishtime[0] + '-' + Tpublishtime[1] + '-' + Tpublishtime[2] + ' ' + \
                                           Tpublishtime[3] + ':00'


                        Tauthor = main1.find('div', attrs={'class': "artOri"}).find('a', attrs={'target': "_blank"})
                        # self.logger.debug(u"作者:%s",Tauthor)
                        if Tauthor is not None:
                            Tauthor = Tauthor.text.strip()
                        else:
                            Tauthor = 'None'

                        Tcontent = main1.find('div', attrs={'class': "artDet"})
                        if Tcontent is not None:
                            Tcontent = Tcontent.text.strip()
                            Tcontent = re.sub(r'\n|\t', '', Tcontent)
                        else:
                            self.logger.error(u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                            return

                        Treply = None  # 这种格式下没有这些统计可以获取
                        meta_info = None

                        article = Article(articleid, self.channel.channel_id, Ttitle, Tcontent, Tpublishtime,
                                          article_url, None, Tauthor, meta_info=meta_info)
                        article.statistics.reply_count = Treply
                        self.logger.info(article)
                        return article

                elif (main2 is not None):
                    self.logger.debug(u'走main2')
                    Ttitle = main2.find('h2',attrs={'class':"one"})  # 标题

                    if Ttitle is None:
                        self.logger.error(u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                        return
                    else:
                        Ttitle = Ttitle.text.strip()
                    try:
                        Tpublishtime = main2.find('span', attrs={'id': "p_publishtime"}).text.strip()
                    except:
                        self.logger.error(u'main2中发布时间不匹配')
                        return

                    if Tpublishtime is None:
                        self.logger.error(u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                        return
                    else:
                        Tpublishtime = re.findall(r'(\d{4})\D(\d{2})\D(\d{2})\D(\d{2}:\d{2}(:\d{2})?)',
                                                  Tpublishtime)[0]
                        # self.logger.error(Tpublishtime)
                        # Tpublishtime = Tpublishtime[:18]
                        if len(Tpublishtime[4]) > 1:
                            Tpublishtime = Tpublishtime[0] + '-' + Tpublishtime[1] + '-' + Tpublishtime[2] + ' ' + \
                                           Tpublishtime[3] + Tpublishtime[4]
                        else:
                            Tpublishtime = Tpublishtime[0] + '-' + Tpublishtime[1] + '-' + Tpublishtime[2] + ' ' + \
                                           Tpublishtime[3] + ':00'


                        Tauthor = main2.find('span', attrs={'id': "p_origin"}).find('a', attrs={'target': "_blank"})
                        # self.logger.debug(u"作者:%s",Tauthor)
                        if Tauthor is not None:
                            Tauthor = Tauthor.text.strip()
                        else:
                            Tauthor = 'None'

                        Tcontent = main2.find('div', attrs={'class': "show_text"})
                        if Tcontent is not None:
                            Tcontent = Tcontent.text.strip()
                            Tcontent = re.sub(r'\n|\t', '', Tcontent)
                        else:
                            self.logger.error(u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
                            return

                        Treply = None  # 这种格式下没有这些统计可以获取
                        meta_info = None

                        article = Article(articleid, self.channel.channel_id, Ttitle, Tcontent, Tpublishtime,
                                          article_url, None, Tauthor, meta_info=meta_info)
                        article.statistics.reply_count = Treply
                        self.logger.info(article)
                        return article

                else:
                    self.logger.warn(u'存在另外一种html格式 %s',article_url)

            # elif 'bbs1' in article_url: #bbs1的格式
            #     self.logger.debug(u'走bbs1')
            #     if main is not None:
            #         main_crawl = main.find('div',attrs={'class':"navBar"})
            #         Ttitle = main_crawl.find('h2').text
            #
            #         if Ttitle is None:
            #             self.logger.error(u'缺少标题，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s', article_url)
            #             return
            #         else:
            #             statice_crawl = soup.find('p', attrs={'class': "replayInfo"})
            #             Tpublishtime = statice_crawl.find('span',attrs={'class':"float_l mT10"}).text.strip()
            #             Tpublishtime = Tpublishtime[-19:]
            #             if Tpublishtime is None:
            #                 self.logger.error(u'缺少文章发布时间，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
            #                 return
            #             else:
            #                 Tpublishtime = Tpublishtime.replace(u'年', '-').replace(u'月', '-').replace(u'日', '')
            #
            #                 Tauthor = None
            #
            #                 Tcontent_crawl = soup.find('article')
            #                 Tcontent_crawl1 = Tcontent_crawl.find('div').attrs['content_path']
            #                 Tcontent_html = self.session.download(Tcontent_crawl1,encoding='utf-8', data=None, isJson=False, timeout=10, retry=3)
            #                 soup1 = BeautifulSoup(Tcontent_html, 'html.parser')
            #                 Tcontent = soup1.text.strip()
            #                 if Tcontent is not None:
            #                     Tcontent = re.sub(r'\n|\t', '', Tcontent)
            #                 else:
            #                     self.logger.error(u'缺少文章内容，无法构成文章，可能已被修改格式，本文停止爬取::该网站为 %s ', article_url)
            #                     return
            #
            #                 Tread = statice_crawl.find('span',attrs={'class':"readNum"}).text.strip()
            #                 Treply = statice_crawl.find('span', attrs={'class': "replayNum"}).text.strip()
            #                 Tlike = statice_crawl.find('span',attrs={'class':"float_l supportBtn"}).attrs['overscore']
            #
            #                 meta_info = None ##这里保存class= replyInfo
            #
            #                 article = Article(articleid, self.channel.channel_id, Ttitle, Tcontent, Tpublishtime,
            #                                   article_url, None, Tauthor, meta_info=meta_info)
            #                 article.statistics.reply_count = Treply
            #                 article.statistics.read_count = Tread
            #                 article.statistics.like_count = Tlike
            #
            #                 # self.logger.info(article)
            #                 return article



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

        cookies = '_HY_lvt_c5928ed57343443aa175434a09ea2804=1492582419784; _HY_CTK_c5928ed57343443aa175434a09ea2804=0acc9a79b115c4ca1931c41e303bec28; BAIDU_SSP_lcr=https://www.baidu.com/link?url=NszYD2w_HgkPWqrzDQ3WKApYldw_9MpUVun9r-R09M7r0dh09MUwTHzG087WaJrhBwMCY-7pDfds4xjtWArRf2xh01DHOWWWd9DpBnHwZ03&wd=&eqid=84d5edfe0003dbdd0000000658f6c280; ALLYESID4=0DB901C6E627D980; sso_c=0; wdcid=5838509dcecc0a53; _people_ip_new_code=510000; UM_distinctid=15b802cdbd364d-02e7f218c26ae6-4e45042e-100200-15b802cdbd49ce; wdses=62d3f0f698d07532; sfr=1; CNZZDATA1260954200=1457761991-1492499618-null%7C1492580619; CNZZDATA1260954203=33096124-1492503288-null%7C1492578888; CNZZDATA1256327855=1768205365-1492503342-null%7C1492578947; wdlast=1492582420'
        html = self.session.download(article.url, encoding='gbk', data=None, isJson=False, timeout=10, retry=3, addr=True, cookies=cookies)
        article_url = article.url

        soup = BeautifulSoup(html['html'], 'html.parser')
        try:
            sid = soup.find('meta', attrs={'name': "contentid"}).attrs['content']
        except:
            return (list(), False)
        sid = re.sub(r'\D', '', sid)
        bbs = 'http://bbs1.people.com.cn/postLink.do?nid=' + sid
        # bbs = soup.find('div', attrs={'class': "message"})
        # if bbs:
        # bbs = bbs.find('a')
        # if bbs:
        # bbs = bbs.attrs['href']
        # else:
        # bbs = 'http://bbs1.people.com.cn/postLink.do?nid='
        # print bbs
        # else:
        # return None

        commentList = list()
        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        html1 = self.session.download(bbs, encoding='gbk', data=None, isJson=False, timeout=10, retry=3)
        soup1 = BeautifulSoup(html1, 'html.parser')
        id = soup1.find('meta', attrs={'name': "contentid"})
        if id:
            id = id.attrs['content']
            id = re.sub(r'\D', '', id)
            re_url = 'http://bbs1.people.com.cn/api/postApi.do'
            page = 1
            while page < 30:
                data1 = {'action': 'postDetailByParentId', 'replayPostId': id, 'pageNo': page}
                html2 = self.session.download(re_url, encoding='utf-8', data=data1, isJson=False, timeout=10,retry=3)
                html2 = re.sub(r'\\\\\\', '', html2)
                html2 = re.sub(r'"\[\\"', '[', html2)
                html2 = re.sub(r'\\"\]"', ']', html2)
                html2 = re.sub(r'\\",\\"', ',', html2)
                html2 = json.loads(html2)
                totalCount = html2['totalCount']
                if totalCount == 0:
                    break
                replayPosts = html2['replayPosts']
                if replayPosts:
                    for i in replayPosts:
                        cid = i['id']
                        user_id = i['userId']
                        user_name = i['userNick']
                        user_ip = i['userIP']
                        # ip_address = get_ip_address(str(user_ip))
                        # ip_address = ''
                        user_head = ''
                        publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S',
                                                         time.localtime(float(i['createTime']) / 1000))
                        reply_userid = i['parentId']
                        like_count = i['vote_yes']
                        unlike_count = i['vote_no']
                        read_count = i['readCount']
                        reply_count = i['replyCount']
                        source_url = article_url
                        content = i['contentText']
                        heat = 0
                        location_coutry = 'CN'
                        # print cid, user_id, user_name, user_ip, ip_address, user_head, publish_datetime, reply_userid
                        # print like_count,unlike_count,read_count,reply_count,source_url
                        commentList.append(Comment(article.tid, self.channel.channel_id, cid,
                                                   add_datetime, publish_datetime,
                                                   user_ip, location_coutry, None, None,  ###这里的ip_address还未实现
                                                   user_id, user_name, content, reply_userid,
                                                   None, like_count, reply_count, dislike_count=None
                                                   ))
                    pageCount = html2['pageCount']  # 评论总页数
                    if pageCount == page:
                        break
                    page = page + 1  # 评论页数+1
                else:
                    break
        return (commentList, False)


def get_ip_address(self,ip):
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

