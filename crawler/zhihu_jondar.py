#-*- coding:utf-8 -*-
'''
Created on 21 Jun 2018
@Author: Jondar
'''

CRAWL_ARTICLE_HEADERS = {
    "Host": "www.zhihu.com",
    "Connection": "keep-alive",
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    # "Accept-Encoding": "gzip, deflate, br",
    "Accept-Language": "zh-CN,zh;q=0.9"
}
CRAWL_COMMENT_HEADERS = {
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"authorization":"oauth c3cef7c66a1843f8b3a9e6a1e3160e20",
#"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.9",
"Connection":"keep-alive",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"}
COMMENT_URL_TEMPLATE = 'https://www.zhihu.com/api/v4/questions/%s/answers?sort_by=default&include=data#5B#2A#5D.is_normal#2Cadmin_closed_comment#2Creward_info#2Cis_collapsed#2Cannotation_action#2Cannotation_detail#2Ccollapse_reason#2Cis_sticky#2Ccollapsed_by#2Csuggest_edit#2Ccomment_count#2Ccan_comment#2Ccontent#2Ceditable_content#2Cvoteup_count#2Creshipment_settings#2Ccomment_permission#2Ccreated_time#2Cupdated_time#2Creview_info#2Cquestion#2Cexcerpt#2Crelationship.is_authorized#2Cis_author#2Cvoting#2Cis_thanked#2Cis_nothelp#2Cupvoted_followees#3Bdata#5B#2A#5D.mark_infos#5B#2A#5D.url#3Bdata#5B#2A#5D.author.follower_count#2Cbadge#5B#3F#28type#3Dbest_answerer#29#5D.topics&limit=%d&offset=%d'
COMMENT_PAGE_SIZE = 20

import requests
import re
import time
import json
import sys
import hmac
import base64
import urllib

from hashlib import sha1
from lxml import etree
from PIL import Image
from com.naswork.sentiment.common.utils import Logging


try:
    import cookielib
except:
    import http.cookiejar as cookielib

reload(sys)
sys.setdefaultencoding('utf8')


class ZhihuCrawler(object):
    '''
    知乎爬虫
    '''
    def __init__(self, channel, logger=None):
        '''
        构造函数
        :param channel:
        :param logger:
        '''
        if logger is None:
            self.logger = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
        else:
            self.logger = logger
        self.channel = channel
        self.nextCommentUrl = None
        self.username = '13064596945'
        self.password = 'Desktop4you'

        # 伪造header
        self.agent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
        self.headers = {
            "HOST": "www.zhihu.com",
            "referer": "https://www.zhihu.com",
            "User-Agent": self.agent,
            "Connection": "keep-alive"
        }

        # 利用session保持连接
        self.session = requests.session()
        self.session.cookies = cookielib.LWPCookieJar(filename='cookies.txt')
        try:
            self.session.cookies.load(ignore_discard=True, ignore_expires=True)
        except:
            self.logger.error('cookies 未能加载')

        if self.isLogin():
            print '已经登录知乎1'
            self.logger.info('已经登录知乎')
        else:
            print '未登录知乎'
            self.login(self.username, self.password)

        self.sleepRange = [0, 1]
        self.lastCrawlTime = 0

    def searchArticle(self, keywordList, endTime=None):
        '''
        根据关键字，开始时间和结束时间搜索文章
        :param keywordList:
        :return:
        '''
        if self.isLogin():
            search_url = 'https://www.zhihu.com/api/v4/search_v3?t=general&q=%s&correction=1&offset=%s&limit=10&time_zone=a_day&search_hash_id=%s'
            articleList = list()
            # for keyword in keywordList:
            keyword = u'中山大学'
            offset = 0
            search_hash_id = 'befed82fe1ea36ce6b72fdf8a79ad144'
            while True:
                keyword_url = search_url % (keyword, offset, search_hash_id)
                self.logger.info(r'搜索关键词的url：' + keyword_url)
                response = self.session.get(keyword_url, headers=self.headers)
                articleListJson = json.loads(response.text)
                is_end = articleListJson['paging']['is_end']
                if not is_end:
                    break
                data = articleListJson['data']
                for i in xrange(len(data)):
                    print i
                    try:
                        # 有些数据是在data_list中的
                        item = data[i]['data_list']
                    except :
                        # 判断文章类型
                        item = data[i]
                        article_type = item['object']['type']
                        if article_type == 'article':
                            item = data[i]
                            print '文章类型:', item['object']['type']
                            print '文章ID:', item['object']['id']
                            print '作者：', item['object']['author']['name']
                            print '作者ID：', item['object']['author']['id']
                            print '文章title：', item['object']['title'].replace('<em>', '').replace('</em>', '')
                            publish_datetime = time.localtime(item['object']['created_time'])
                            update_datetime = time.localtime(item['object']['updated_time'])
                            print '发表时间', time.strftime('%Y-%m-%d %H:%M:%S', publish_datetime)
                            print '更新时间', time.strftime('%Y-%m-%d %H:%M:%S', update_datetime)
                            print 'read_count阅读数：', 0
                            print 'reply_count回复数：', item['object']['comment_count']
                            print 'like_count点赞数：', item['object']['voteup_count']
                            content = item['object']['content']
                            dr = re.compile(r'<[^>]+>', re.S)
                            print '文章内容：', dr.sub('', content)

                            print 'collect_count收藏数：', 0
                            print 'forward_count转发数：', 0
                            article_url = 'https://zhuanlan.zhihu.com/p/%s' % item['object']['id']
                            print '文章url：', article_url

                        else:
                            item = data[i]
                            print '文章类型:', item['object']['type']
                            print '标题:', item['object']['question']['name'].replace('<em>', '').replace('</em>', '')
                            print '文章id:', item['object']['question']['id']
                            print '点赞数like_count:', 0
                            print '转发数forward_count：', 0
                            publish_datetime = time.localtime(item['object']['created_time'])
                            update_datetime = time.localtime(item['object']['updated_time'])
                            print '发表时间', time.strftime('%Y-%m-%d %H:%M:%S', publish_datetime)
                            print '更新时间', time.strftime('%Y-%m-%d %H:%M:%S', update_datetime)

                            article_url = 'https://www.zhihu.com/question/%s' % (item['object']['question']['id'])
                            self.crawlArticle(article_url)
                offset += 10

    def crawlArticle(self, url):
        '''
        根据url爬取文章内容和统计信息
        :return:返回一个article实例
        '''
        article_id = re.findall(r'\d+', url)[0]
        response = self.session.get(url=url, headers=CRAWL_ARTICLE_HEADERS)

        html = etree.HTML(response.text)
        results = html.xpath("//div[@class='QuestionHeader-side']//div[@class='NumberBoard-itemInner']//strong/@title")
        print '收藏数collect_count：', results[0]
        print '阅读数read_count:', results[1]
        reply_count = html.xpath("//div[@class='Question-main']//div[@id='QuestionAnswers-answers']//div[@class='List-header']//span[1]/text()")
        print '回复数reply_count：', reply_count[0]
        logurl = 'https://www.zhihu.com/question/%s/log' % article_id
        print logurl
        authorInfo = self.crawlArticleLog(logurl)
        # author = authorInfo[0]
        # authorId = authorInfo[1]
        # print '作者名：', author
        # print '作者ID：', authorId

    def crawlArticleLog(self, url):
        '''
        获取question发表的作者
        :param url:
        :return:
        '''
        response = self.session.get(url=url, headers=CRAWL_ARTICLE_HEADERS)

        html = etree.HTML(response.text)

        author_list = html.xpath("//div[@class='zu-main-content']//div[@id='zh-question-log-list-wrap']/div[@class='zm-item']")

        author = author_list[-1].xpath(".//a/text()")[0].encode('utf-8')

        print '作者名称：', author

        if author == u'匿名用户':
           authorId = 0
        else:
            data = dict()
            nickname = author_list[-1].xpath(".//a/@href")
            nickname = nickname[0].split('/')[-1]
            data['params'] = '{"url_token":"%s"}' % nickname
            url_value = urllib.urlencode(data)

            profileUrl = "https://www.zhihu.com/node/MemberProfileCardV2?" + url_value
            profileResponse = self.session.get(url=profileUrl, headers=CRAWL_ARTICLE_HEADERS)

            profileHtml = etree.HTML(profileResponse.text)
            authorId = profileHtml.xpath("//div[@class='zh-profile-card member']//div[2]//div[@class='operation']/button/@data-id")
            print '作者ID：', authorId



        # return (author[0].strip())

    def isLogin(self):
        '''
        通过个人中心页面返回状态码来判断是否登录
        通过allow_redirects 设置为不获取重定向后的页面
        :return:
        '''
        response = self.session.get('https://www.zhihu.com/inbox', headers=self.headers, allow_redirects=False)
        if response.status_code == 200:
            return True
        else:
            return False

    def getXsrfDc0(self):
        '''
        获取xsrf code 和 d_c0
        在请求登录页面的时候页面会将xsrf code 和 d_c0加入到cookie中返回给客户端
        :return:
        '''
        response = self.session.get('https://www.zhihu.com/signin', headers=self.headers)
        return response.cookies['_xsrf'], response.cookies['d_c0']

    def getSignaturn(self, time_str):
        '''
        生成signature, 利用hmac加密
        根据分析之后的js, 可发现里面有一段是进行hmac加密的
        分析执行加密的js代码, 可得出加密的字段，然后进行解码
        :param time_str:
        :return:
        '''
        h = hmac.new(key='d1b964811afb40118a12068ff74a12f4'.encode('utf-8'), digestmod=sha1)
        grant_type = 'password'
        client_id = 'c3cef7c66a1843f8b3a9e6a1e3160e20'
        source = 'com.zhihu.web'
        now = time_str
        h.update((grant_type + client_id + source + now).encode('utf-8'))
        return h.hexdigest()

    def getIdentifyingCode(self):
        '''
        判断页面是否需要填写验证码，如果需要则弹出验证码，进行手动填写
        请求验证码的url后的参数lang=en，意思是取得英文验证码
        :param headers:
        :return:
        '''
        response = self.session.get('https://www.zhihu.com/api/v3/oauth/captcha?leng=en', headers=self.headers)
        # 检查是否存在验证码
        r = re.findall('"show_captcha":(\w+)', response.text)
        if r[0] == 'false':
            return ''
        else:
            response = self.session.put('https://www.zhihu.com/api/v3/oauth/captcha?lang=en', headers=self.headers)
            show_captcha = json.load(response.text)['img_base64']
            with open('zhihu_captcha.jpg', 'wb') as f:
                f.write(base64.b64decode(show_captcha))
            im = Image.open('zhihucaptcha.jpg')
            im.show()
            im.close()
            captcha = input('输入验证码：')
            self.session.post('https://www.zhihu.com/api/v3/oauth/captcha?leng=en', headers=self.headers,
                              data={'input_text': captcha})
            return captcha

    def login(self, username, password):
        '''
        登录
        :param username: 用户名
        :param password: 密码
        :return:
        '''
        post_url = 'https://www.zhihu.com/api/v3/oauth/sign_in'
        XXsrftoken, XUDID = self.getXsrfDc0()
        self.headers.update({
            'authorization': 'oauth c3cef7c66a1843f8b3a9e6a1e3160e20',
            'X-Xsrftoken': XXsrftoken,
        })
        time_str = str(int((time.time() * 1000)))
        # 直接写在引号内的值为固定值
        post_data = {
            'client_id': 'c3cef7c66a1843f8b3a9e6a1e3160e20',
            'grant_type': 'password',
            'timestamp': time_str,
            'source': 'com.zhihu.web',
            'password': password,
            'username': username,
            'lang': 'en',
            'ref_source': 'homepage',
            'utm_source': '',
            'signature': self.getSignaturn(time_str),
            'captcha': self.getIdentifyingCode()
        }

        response = self.session.post(post_url, data=post_data, headers=self.headers, cookies=self.session.cookies)
        if response.status_code == 201:
            # 保存cookie，下次直接读取保存的cookie，不用再次登录
            self.session.cookies.save()
            return True
        else:
            return False


if __name__ == '__main__':
    zhihu = ZhihuCrawler(8)
    zhihu.searchArticle(['中山大学'])
