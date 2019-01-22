'''
Created on 26 may 2017

@author: tanoy
'''
import urllib2
import cookielib
from poster.encode import multipart_encode,MultipartParam
from poster.streaminghttp import register_openers
from bs4 import BeautifulSoup
#from com.naswork.rfq.common.utils import Logging,  PIDUtils 
#from com.naswork.rfq.common.crawlercommon import CrawlerConstants, CrawlerManager, CrawlerBase
import traceback
import time
import MySQLdb
import math
import ssl
import requests
import json
LOGGER_NAME_CRAWL = 'hscode'
opener = register_openers()
AGENT = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36'

class RedirctHandler(urllib2.HTTPRedirectHandler):  
  """docstring for RedirctHandler"""  
  def http_error_301(self, req, fp, code, msg, headers):  
    print code, msg, headers  
  def http_error_302(self, req, fp, code, msg, headers):  
    print code, msg, headers

cookie = cookielib.CookieJar()
handler=urllib2.HTTPCookieProcessor(cookie)
opener.add_handler(handler)
#opener.add_handler(RedirctHandler)
defaultHEADERS = {
    'User-Agent': AGENT,
    'Host': "www.hscode.net",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1"
}

searchHEADERS = {
    'User-Agent': AGENT,
    'Host': "www.hscode.net",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1",
    #"Referer":"http://www.hscode.net/Goods/GoodsList/?key=4804410000"
}

afterSearchHEADERS = {
    'User-Agent': AGENT,
    'Host': "www.hscode.net",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1",
    "Referer":"http://www.hscode.net/"
}

afterSearchDataHEADERS = {
    'User-Agent': AGENT,
    'Host': "www.hscode.net",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1",
    #"Referer":"http://www.hscode.net/goods/hscodelist/?key=4804410000",
    "X-Requested-With":"XMLHttpRequest"
}

session = requests.session()

def crawlDefault():
    url = 'http://www.hscode.net/'
    result = session.get(url, headers=defaultHEADERS, verify=False)
    return result.text

def search(hsCode):
    url = 'http://www.hscode.net/goods/hscodelist/?key='+str(hsCode)
    searchHEADERS["Referer"] = "http://www.hscode.net/Goods/GoodsList/?key="+str(hsCode)
    result = session.get(url, headers=searchHEADERS, verify=False)
    return result.text

def afterSearch(hsCode):
    url = 'http://www.hscode.net/Goods/GoodsList/?key='+str(hsCode)
    
    result = session.get(url, headers=afterSearchHEADERS, verify=False)
    return result.text

def getRateForm(hsCode,ran):
    url = 'http://www.hscode.net/Goods/GetHsCodeInfo/?hscode='+str(hsCode)+"&ran="+str(ran)
    afterSearchDataHEADERS["Referer"] = "http://www.hscode.net/goods/hscodelist/?key="+str(hsCode)
    result = session.get(url, headers=afterSearchDataHEADERS, verify=False)
    return result.text

def getSuperviseForm(hsCode,ran):
    url = 'http://www.hscode.net/Goods/GetMark/?hscode='+str(hsCode)+"&ran="+str(ran)
    afterSearchDataHEADERS["Referer"] = "http://www.hscode.net/goods/hscodelist/?key="+str(hsCode)
    result = session.get(url, headers=afterSearchDataHEADERS, verify=False)
    return result.text

def parseDetailInfo(hsCode,content,superviseMessage):
    hsCodeMessage = json.loads(content)
    supervise = json.loads(superviseMessage)
    control_mark = supervise["control_mark"][:-6]
    control_mark = control_mark.replace('<br/>','')
#    print type(hscodes)
    return DetailInfo(hsCodeMessage["low_rate"],hsCodeMessage["high_rate"],hsCodeMessage["out_rate"],hsCodeMessage["back_rate"],control_mark,hsCodeMessage["Tax10Code"])
    
    
    

class DetailInfo(object):
    def __init__(self, low_rate, high_rate, out_rate, back_rate,control_mark,hs_code):
        self.low_rate = low_rate
        self.high_rate = high_rate
        self.out_rate = out_rate
        self.back_rate = back_rate
        self.control_mark = control_mark
        self.hs_code = hs_code
    
def insertDB(data):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into hs_code_message(hs_code, low_rate, high_rate, out_rate, back_rate, control_mark) values('%s', '%s', '%s', '%s', '%s', '%s')"%(data.hs_code, data.low_rate, data.high_rate, data.out_rate,data.back_rate,data.control_mark)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def getHsCode():
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT hs_code as code FROM hs_code"
    cursor.execute(sql)
    code = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return code


import random
ran = "0."+str(random.randint(1000000000000000,9999999999999999))
default = crawlDefault()
tup = getHsCode()
for r in tup:
    hsCode = r[0]
    searchData = search(hsCode)
    afterSearchData = afterSearch(hsCode)
    rateSearchForm = getRateForm(hsCode,ran)
    superviseMessage = getSuperviseForm(hsCode,ran)
    message = parseDetailInfo(hsCode,rateSearchForm,superviseMessage)
    insertDB(message)
