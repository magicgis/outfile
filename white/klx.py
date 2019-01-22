#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Author: white<white@naswork.com>
# @DateTime: 2018/10/27 10:00
# @Descrition: klx.py

import falcon
import requests
import urllib
import json
import md5
import traceback
from wsgiref import simple_server
from com.naswork.rfq.common.restsimpleserver import RestRequestHandler
import cookielib
from bs4 import BeautifulSoup

session = requests.session()
headers={
    'Host':'www.shopklx.com',
    'Origin':'https://www.shopklx.com',
    'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36',
    'Referer':'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp'
}

login_headers = {
    'Host':'www.shopklx.com',
    'Origin':'https://www.shopklx.com',
    'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36',
    'Referer':'https://www.shopklx.com/index.jsp?DPSLogout=true'
}

ROUTES=['/crawKlx']
class Klx(object):

    def __init__(self,falconApi):
        logger = Logger().getLogger()
        logger.info("爬虫开启")
        for route in ROUTES:
            falconApi.add_route(route,self)
    # get请求
    def on_get(self,req,resp):
        logger = Logger().getLogger()
        logger.info("get请求")
        resp.status = falcon.HTTP_200
    # post请求
    def on_post(self,req,resp):
        logger = Logger().getLogger()
        logger.info("post请求")
        try:
            resp.status = falcon.HTTP_200
            partNumber = req.params['partNumber']
            logger.info("开始寻找"+partNumber)
            self.getData(partNumber)
        except Exception,ex:
            logger.error(str(traceback.format_exc()))
            logger.error(str(Exception)+":"+str(ex))

    def login(self):
        logger = Logger().getLogger()
        logger.info('login start')
        url = 'https://www.shopklx.com/en/ajax/login.jsp?v=1540777873525'
        postData={
            'userName':'BETTERAIRGROUP.PBETTERAIR',
            'password':'GZBA0919',
            'v':'1540777873525'
        }
        result = requests.post(url,params=postData,headers=login_headers,allow_redirects=False,verify=False)
        self.save_cookies_lwp(result.cookies)

    def getData(self, partNumber):
        logger = Logger().getLogger()
        url = 'https://www.shopklx.com/index.jsp?_DARGS=/'

        Res1 = self.crawlDefault()

        soup = BeautifulSoup(Res1,features="html.parser")

        stt = soup.find(attrs={"name": "_dynSessConf"})

        ran = stt['value']

        postData = {
            '_dyncharset': 'UTF-8',
            '_dynSessConf': ran,
            '/com/klx/search/MultiPartSearchFormHandler.activeTab': 'box',
            '_D:/com/klx/search/MultiPartSearchFormHandler.activeTab': ' ',
            '/com/klx/search/MultiPartSearchFormHandler.lazySearch': 'false',
            '_D:/com/klx/search/MultiPartSearchFormHandler.lazySearch': ' ',
            '_D:bulk-part': ' ',
            'bulk-part': partNumber,
            'files[]': '',
            '/com/klx/search/MultiPartSearchFormHandler.search': 'Search',
            '_D:/com/klx/search/MultiPartSearchFormHandler.search': ' ',
            '_DARGS': '/'
        }
        # postData = {
        #     'bulk-part':partNumber,
        # }
        result = session.get(url,data = postData,headers=headers,verify=False)
        print  result.status_code
        print  result.text
        # if result.status_code != 200:
        #     print result.status_code
        #     # logger.info("打印请求响应状态码%d" % result.status_code)
        #     self.login()
        #     return self.getData(partNumber)
        # else:
        #     print result.status_code
        #     print result.text

    def save_cookies_lwp(self,cookiejar):
        """
        保存cookies到本地
        """
        filename = 'klxCookies'
        lwp_cookiejar = cookielib.LWPCookieJar()
        for c in cookiejar:
            args = dict(vars(c).items())
            args['rest'] = args['_rest']
            del args['_rest']
            c = cookielib.Cookie(**args)
            lwp_cookiejar.set_cookie(c)
        lwp_cookiejar.save(filename, ignore_discard=True)

    def load_cookies_lwp(self):
        """
        读取本地cookies
        """
        filename = 'klxCookies'
        lwp_cookiejar = cookielib.LWPCookieJar()
        lwp_cookiejar.load(filename, ignore_discard=True)
        return lwp_cookiejar

    def crawlDefault(self):
        AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3047.4 Safari/537.36"
        HEADERS = {
            "User-Agent": AGENT,
            "Host": "www.shopklx.com",
            "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
            "Accept-Encoding": "gzip, deflate, sdch, br",
            "Accept-Language": "zh-CN,zh;q=0.8",
            "Cache-Control": "max-age=0",
            "Connection": "keep-alive",
            "Upgrade-Insecure-Requests": "1",
            "Referer": "https://www.aviall.com/aviallstorefront/"
        }
        try:
            url = 'https://www.shopklx.com/'
            result = session.get(url, headers=HEADERS, verify=False, timeout=90)
            return result.text
        except Exception, e:
            'do nothing'

class Logger(object):

    def getLogger(self):
        import logging
        import os
        import inspect

        logger = logging.getLogger('[klxFalcon]')

        this_file = inspect.getfile(inspect.currentframe())
        dirpath = os.path.abspath(os.path.dirname(this_file))
        dirpath = dirpath+"\\log"
        handler = logging.FileHandler(os.path.join(dirpath,"white_klx.log"))

        logger.addHandler(handler)
        logger.setLevel(logging.INFO)
        return logger

if __name__ == '__main__':
    app = falcon.API()
    Klx(app).getData("ABS0336-09")
    # try:
    #     app = falcon.API()
    #     Klx(app)
    #     httpd = simple_server.make_server('127.0.0.1',8088,app,handler_class=RestRequestHandler)
    #     httpd.serve_forever()
    # except Exception,ex:
    #     logger = Logger().getLogger()
    #     logger.error(str(traceback.format_exc()))
    #     logger.error(str(Exception)+":"+str(ex))









