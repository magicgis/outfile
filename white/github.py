#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Author: white<white@naswork.com>
# @DateTime: 2018/10/29 14:02
# @Descrition: github.py


from wsgiref import simple_server

import falcon
import requests
import json
import md5
from bs4 import BeautifulSoup
import datetime
import traceback

from restsimpleserver import RestRequestHandler

ROUTES=['/github']
class Github(object):

    def __init__(self,falconApi):
        logger = self.getLogger()
        logger.info("爬虫开始")
        for route in ROUTES:
            falconApi.add_route(route,self)

    def on_post(self,req,resp):
        self.getLogger().info("处理post请求开始")
        resp.status = falcon.HTTP_200

    def on_get(self,req,resp):
        self.getLogger().info("处理get请求开始")
        try:
            resp.status = falcon.HTTP_200
            account = req.params['account']
            self.getLogger().info("开始寻找账号为"+account+"的github")
            result = self.get_data(account)
            resp.body= json.dumps('{"status":"200","msg":"请求成功","data":'+str(result)+'}')
        except Exception, ex:
            self.getLogger().error(str(traceback.format_exc()))
            self.getLogger().error(str(Exception) + ":" + str(ex))

    def get_data(self,account):
        url = "https://github.com/"+account

        headers={
            'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36',
            'Host':'github.com',
        }
        postData = {
            'tab':'repositories'
        }
        self.getLogger().info("获取数据开始")
        result = requests.session().get(url,data=postData,headers=headers,verify=False)
        data = []
        if result.status_code == 200:
            soup = BeautifulSoup(result.text,'html.parser')
            for div in soup.find_all('div',class_='d-inline-block mb-1'):
                # print "https://github.com"+div.find('a').get("href")
                data.append("https://github.com"+div.find('a').get("href"))
        else:
            self.getLogger("获取失败时的status_code"+str(result.status_code))

        print str(data).replace('u\'','\'').decode("unicode-escape")
        return str(data).replace('u\'','\'').decode("unicode-escape")

    def getLogger(self):
        import logging
        import os
        import inspect

        logger = logging.getLogger('[klxFalcon]')

        this_file = inspect.getfile(inspect.currentframe())
        dirpath = os.path.abspath(os.path.dirname(this_file))
        dirpath = dirpath + "\\log"
        handler = logging.FileHandler(os.path.join(dirpath, "github.log"))

        logger.addHandler(handler)
        logger.setLevel(logging.INFO)
        return logger

if __name__ == '__main__':
    try:
        app = falcon.API()
        Github(app)
        httpd = simple_server.make_server('127.0.0.1',8088,app,handler_class=RestRequestHandler)
        httpd.serve_forever()
    except Exception,ex:
        print ex
        # logger = Github().getLogger()
        # logger.error(str(traceback.format_exc()))
        # logger.error(str(Exception)+":"+str(ex))



