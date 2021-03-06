# -*- coding:utf-8 -*-
'''
Created on 5 Oct 2017

@author: eyaomai
'''
from com.naswork.sentiment.crawler.baidu import BaiduCrawler
from com.naswork.sentiment.objectmodel.article import Article
from com.naswork.sentiment.objectmodel.comment import Comment
from com.naswork.sentiment.common.utils import Logging
import re, json
from bs4 import BeautifulSoup
import datetime, time
import traceback


class TencentNewsCrawler(object):
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
        self.lastCommentId = None
    
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
                # 同一文章可能会在搜索结果出现多次，在baidu的结果url是不重复，但是实际可能指向同一文章，需要去重
                articleList.append(article)
        return articleList
    
    def crawlStatistics(self, article):
        meta_info = article.meta_info
        if meta_info is None:
            return
        jo = json.loads(meta_info)
        if "commentid" not in jo:
            return
        commentid = jo["commentid"]
        re_url = 'http://coral.qq.com/article/batchcommentnum'
        data1 = {'targetid': commentid}
        html1 = json.loads(self.session.download(re_url, encoding='utf-8', data=data1, timeout=10, retry=3))
        article.statistics.reply_count = int(html1['data'][0]['commentnum'])
    
    def crawlArticle(self, url):
        html = self.session.download(url, encoding='gbk', data=None, timeout=10, retry=3, addr=True)
        if html:
            article_url = html['url']
            if article_url.find(self.channel.url)<0:
                self.logger.warn('Unrelated url found:%s', url)
                return None
            article_url = re.findall(r'.*?\.html|.*?\.htm|.*?\.shtml|.*?\.shtm', article_url)[0]
            self.logger.debug('[TencentNews]'+article_url)
            soup = BeautifulSoup(html['html'], 'html.parser')
            main = soup.find('div', attrs={'id': "Main-Article-QQ"})
            main1 = soup.find('div', attrs={'id': "Main-P-QQ"})
            if main is not None:
                Ttitle = main.find('h1').text.strip()  #标题
                Ttime = main.find('span', attrs={'class':"article-time"})  #发布时间
                Ttime1 = main.find('span', attrs={'class': "a_time"})
                Ttime2 = main.find('span', attrs={'class': "pubTime"})
                if Ttime is not None:
                    Ttime = Ttime.text.strip()
                elif Ttime1 is not None:
                    Ttime1 = Ttime1.text.strip()
                    Ttime = Ttime1
                elif Ttime2 is not None:
                    Ttime2 = Ttime2.text.strip()
                    Ttime = Ttime2
                else:
                    Ttime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                if len(Ttime)==16:
                    Ttime=Ttime+':00'
                    
                Tauthor = main.find('span', attrs={'class':"a_source"})
                Tauthor1 = main.find('span', attrs={'class': "color-a-1"})
                if Tauthor is not None:
                    #Tauthor = Tauthor.find('a').text.strip()
                    Tauthor = Tauthor.text.strip()
                elif Tauthor1 is not None:
                    #Tauthor1 = Tauthor1.find('a').text.strip()
                    Tauthor1 = Tauthor1.text.strip()
                    Tauthor = Tauthor1
                else:
                    Tauthor = None
                Tcontent = main.find('div', attrs={'id': "Cnt-Main-Article-QQ"})
                if Tcontent is not None:
                    Tcontent = Tcontent.text.strip()
                    Tcontent = re.sub(r'\n|\t', '', Tcontent)
                else:
                    Tcontent = None
                articleid = re.findall(r'id:\'(\d+)\',', html['html'])[0]
                try:
                    commentid = re.findall(r'cmt_id = (\d+);', html['html'])[0]
                    meta_info = '{"commentid":"%s"}'%commentid
                except:
                    commentid = None 
                    meta_info = None
                article = Article(articleid, self.channel.channel_id, Ttitle, Tcontent, Ttime, article_url, None,Tauthor, meta_info=meta_info)
                if commentid is not None:
                    try:                    
                        re_url = 'http://coral.qq.com/article/'+ commentid +'/commentnum'
                        html1 = json.loads(self.session.download(re_url, encoding='utf-8', data=None, timeout=10, retry=3))
                        Treply = int(html1['data']['commentnum'])
                    except Exception:
                        traceInfo = traceback.format_exc()
                        self.logger.error('Faile to parse comment for %s (cid=%s):%s', articleid, commentid, traceInfo)
                        Treply = None
                    article.statistics.reply_count = Treply
                return article
            elif main1 is not None:
                Ttitle = soup.find('meta', attrs={'name':"Description"}).attrs['content']  # 标题
                Ttime = re.findall(r"pubtime\D+(\d{4})\D(\d{2})\D(\d{2})\D(\d{2}:\d{2})\',", html['html'])
                if Ttime is not None:
                    Ttime = Ttime[0]
                    Ttime = Ttime[0] + '-' + Ttime[1] + '-' + Ttime[2] + ' ' + Ttime[3]
                else:
                    Ttime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                if len(Ttime)==16:
                    Ttime=Ttime+':00'
                Tauthor = re.findall(r'para = {\s+name: \"(.*)\",', html['html'])
                if Tauthor is not None:
                    Tauthor = Tauthor[0]
                else:
                    Tauthor = None
                con_url = re.sub(r'\.htm\?.*', '.hdBigPic.js', article_url)
                con_html = self.session.download(con_url, encoding='gbk', data=None,timeout=10, retry=3)
                con_list = re.findall(r'<p>(.*?)</p>', con_html)
                if con_list is not None:
                    TT = []
                    for i in con_list:
                        if i.strip() not in TT:
                            TT.append(i)
                    Tcontent = ''.join(TT)
                else:
                    Tcontent = None
                articleid = re.findall(r'id:\'(\d+)\',', html['html'])[0]
                try:
                    commentid = re.findall(r'aid\D+(\d+)\",', html['html'])[0]
                    meta_info = '{"commentid":"%s"}'%commentid
                except:
                    commentid = None 
                    meta_info = None
                article = Article(articleid, self.channel.channel_id, Ttitle, Tcontent, Ttime, article_url, None,Tauthor, meta_info=meta_info)
                try:
                    if commentid is not None:
                        re_url = 'http://coral.qq.com/article/batchcommentnum'
                        data1 = {'targetid': articleid}
                        html1 = json.loads(self.session.download(re_url, encoding='utf-8', data=data1, timeout=10, retry=3))
                        Treply = int(html1['data'][0]['commentnum'])
                    else:
                        Treply = None
                except:
                    Treply = None
                article.statistics.reply_count = Treply
                return article
        return None
    
    def refreshSearch(self):
        pass
    
    def refreshCommentCrawler(self):
        self.lastCommentId = None
        
    def crawlComment(self, article):
        # 获取文章评论
        meta_info = article.meta_info
        if meta_info is None:
            return (list(), False)
        jo = json.loads(meta_info)
        if "commentid" not in jo:
            return (list(), False)
        commentid = jo["commentid"]
        cookies = 'pac_uid=0_58ec8106620c1; gj_mpvid=80515918; ad_play_index=97; dsp_cookiemapping0=1492586667155; pgv_info=ssid=s9259450720; ts_last=news.qq.com/a/20170415/002007.htm; ts_refer=www.baidu.com/link; pgv_pvid=1281052383; ts_uid=1143064466; ptag=www_baidu_com|'
        re_url = 'http://coral.qq.com/article/' + commentid + '/comment'
        commentList = list()
        add_datetime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        data1 = {
            'commentid': self.lastCommentId if self.lastCommentId is not None else '0',
            'reqnum': '50'
        }
        html = self.session.download(re_url, encoding='utf-8', cookies=cookies, data=data1, timeout=10, retry=3)
        jo = json.loads(html)
        if jo['errCode'] != 0:
            return ([], False)
        if jo['data']['retnum'] == 0:
            return ([], False)
        self.lastCommentId = jo['data']['last']
        for i in jo['data']['commentid']:
            cid = i['id']
            user_id = i['userinfo']['userid']
            user_name = i['userinfo']['nick']
            user_ip = ''
            location = i['userinfo']['region'].replace(u'市','').replace(u'自治','').replace(u'新区','').replace(u'区','').replace(u'洲','')
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
            #user_head = i['userinfo']['head']

            publish_datetime = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(i['time']))
            reply_userid = str(i['replyuserid'])
            like_count = i['up']
            reply_count = i['rep']
            content = i['content']
            # print cid, user_id, user_name, user_ip, ip_address, user_head, publish_datetime, reply_userid
            # print like_count,unlike_count,read_count,reply_count,source_url
            commentList.append(Comment(article.tid, self.channel.channel_id, cid,
                                       add_datetime,publish_datetime, 
                                       user_ip, location_country, location_region, location_city,
                                       user_id, user_name, content, reply_userid,
                                       None, like_count, reply_count, None
                                       ))
        return (commentList, jo['data']['hasnext'])                