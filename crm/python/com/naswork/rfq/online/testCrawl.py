'''
Created on 11 Feb 2017

@author: tanoy
'''
import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )
import urllib2
import cookielib
from poster.encode import multipart_encode,MultipartParam
from poster.streaminghttp import register_openers
from bs4 import BeautifulSoup
import traceback
import time
import MySQLdb
import math
import ssl
import requests
import re
import json
from base64 import encode
LOGGER_NAME_CRAWL = 'satair'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
HEADERS = {
    "User-Agent": AGENT,
    "Host": "www.aviall.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch, br",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1",
    "Pragma":"no-cache",
    "Cache-Control":"no-cache",
    "Referer":"https://www.aviall.com/aviallstorefront/"
}
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
boundry = '----WebKitFormBoundaryUykxyObQxRDUYdjh'
boundry2 = '----WebKitFormBoundaryoIib5cgXwCxENRI3'

stockMessageHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/production-list/Boeing/737",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"cookie":"__cfduid=d7d4b8004a3b03f93328977530b28ea5e1525849643; __psuid=7619aa2b80bfa3a4abe1ab36c935defb; _ga=GA1.2.373894729.1525849647; testSeed=571077140; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gid=GA1.2.468000177.1527501919; PHPSESSID=rg6jk5o99tdmh0kadlteehn415; _gat=1; _pk_id.1.9f6a=3ef5cef84e9bfbc6.1526304667.78.1527502504..",
"pragma":"no-cache",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

pageHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/production-list/Boeing/737?p=2",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"cookie":"__cfduid=d1d1ab2d703c1c9001c132dc448d1078d1528699257; __psuid=8ea11fd2ffc79772a6076e3509f3b5b7; _ga=GA1.2.436002455.1528699253; _gid=GA1.2.91448838.1528699253; testSeed=118802916; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; PHPSESSID=tas7663qkrd7dfuq2hns23aps4; _gat=1; _pk_id.1.9f6a=09164f9407a280fd.1528699257.13.1528770347..",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/production-list/Boeing/737",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
}

foreFoxPageHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
"Connection":"keep-alive",
"Cookie":"__cfduid=d1d1ab2d703c1c9001c132dc448d1078d1528699257; __psuid=8ea11fd2ffc79772a6076e3509f3b5b7; _ga=GA1.2.436002455.1528699253; _gid=GA1.2.91448838.1528699253; testSeed=118802916; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; PHPSESSID=tas7663qkrd7dfuq2hns23aps4; _gat=1; _pk_id.1.9f6a=09164f9407a280fd.1528699257.13.1528770347..",
"Host":"www.planespotters.net",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"
}

detailHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/airframe/Boeing/737/N707SA-Southwest-Airlines/OAx3fwdg",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"cookie":"__cfduid=d1d1ab2d703c1c9001c132dc448d1078d1528699257; __psuid=8ea11fd2ffc79772a6076e3509f3b5b7; _ga=GA1.2.436002455.1528699253; _gid=GA1.2.91448838.1528699253; testSeed=118802916; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; PHPSESSID=tas7663qkrd7dfuq2hns23aps4; _gat=1; _pk_id.1.9f6a=09164f9407a280fd.1528699257.13.1528770347..",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/production-list/Boeing/737",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

session = requests.session()

def getMessage(retry = 3):
    try:
        url = 'https://www.planespotters.net/production-list/Boeing/737'
        result = session.get(url, headers=stockMessageHeaders, verify=False,timeout=90)
        session.close()
        return result.text
    except Exception,ex:
        if retry < 1:
            _getLogger().error(str(traceback.format_exc()))
            _getLogger().error(str(Exception)+":"+str(ex))
            return "nothing"
        return getMessage(retry = retry-1)
    
def nextPage(pageNum,retry = 3):
    try:
        url = 'https://www.planespotters.net/production-list/Boeing/737?p='+str(pageNum)
        result = session.get(url, headers=foreFoxPageHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            _getLogger().error(str(traceback.format_exc()))
            _getLogger().error(str(Exception)+":"+str(ex))
            return "nothing"
        return nextPage(pageNum,retry = retry-1)
    
def checkNextPage(content,pageNum):
    soup = BeautifulSoup(content)
    div = soup.findAll('div',{'class','pages'})
    if len(div) == 0:
        return False
    pages = div[0].findAll('a',{'class','link'})
    for page in pages:
        if str(pageNum) == str(page.text):
            return True
    return False

def createFile(content,pageNum):
    name = 'D:/crawl/' + str(pageNum) + '.html'
    file = open(name,'w')
    file.write(content)
    
try:
    #message = getMessage()
    message = getMessage()
    type = sys.getfilesystemencoding()
    message = message.decode('utf-8').encode(type)
    #type = sys.getfilesystemencoding() ok
    #message = message.decode('utf-8','ignore').encode(type) ok 
    createFile(message,1)
    #getTableMessage(message,"1")
#    message = nextPage(15)
#    getTableMessage(message,"15")
    for pageNum in range(2,1000):
        if checkNextPage(message,pageNum):
            message = nextPage(pageNum)
            createFile(message,pageNum)
            if pageNum % 5 == 0:
                foo = [25,26,27,28,29,30]
                time.sleep(choice(foo))
            else:
                foo = [8,9,10,11,12,13,14,15]
                time.sleep(choice(foo))
except Exception,ex:
   _getLogger().error(str(traceback.format_exc()))
   _getLogger().error(str(Exception)+":"+str(ex))
    



    
