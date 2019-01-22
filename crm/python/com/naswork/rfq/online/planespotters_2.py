'''
Created on 9 May 2018

@author: tanoy
'''
import sys
#reload(sys)
#sys.setdefaultencoding( "UTF-8" )
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
import random
from base64 import encode
import HTMLParser
from random import choice
import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )
LOGGER_NAME_CRAWL = 'planespotters'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
HEADERS = {
    ":authority": "www.planespotters.net",
    ":method": "GET",
    ":path": "/user/login",
    ":scheme": "https",
    "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "accept-encoding": "gzip, deflate, br",
    "accept-language": "zh-CN,zh;q=0.9",
    "cache-control": "no-cache",
    "pragma": "no-cache",
    "upgrade-insecure-requests": "1",
    "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
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

toLoginHeaders={
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate, br",
    "Accept-Language":"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
    "Connection":"keep-alive",
    "Host":"www.planespotters.net",
    "Referer":"https://www.planespotters.net/",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"
}

loginHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
"Connection":"keep-alive",
"Content-Length":"85",
"Content-Type":"application/x-www-form-urlencoded",
"Host":"www.planespotters.net",
"Referer":"https://www.planespotters.net/user/login",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"
}

mainHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/user/login",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

stockMessageHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
"Connection":"keep-alive",
"Cookie":"__cfduid=de7ceb8fdd5657d991947f756a79426151526289645; _pk_id.1.9f6a=6feb741d266f83e7.1526289645.52.1530168262..; __psuid=9eacdb2f63cb4dc07945bf5e14c59fc3; _ga=GA1.2.450555718.1526289641; PHPSESSID=u3ri92i5hmmnqk8qcurcnbjod5; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; testSeed=2040896619; _gid=GA1.2.1323715350.1530168244; _gat=1",
"Host":"www.planespotters.net",
"Referer":"https://www.planespotters.net/production-list/Boeing/737",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"
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
"cookie":"__cfduid=d1d1ab2d703c1c9001c132dc448d1078d1528699257; __psuid=8ea11fd2ffc79772a6076e3509f3b5b7; _ga=GA1.2.436002455.1528699253; testSeed=118802916; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gid=GA1.2.1634143001.1530167817; _gat=1; PHPSESSID=0euvb4p77v3l7cohr1urt0ks96; _pk_id.1.9f6a=09164f9407a280fd.1528699257.30.1530167833..",
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
"Cookie":"__cfduid=d1d1ab2d703c1c9001c132dc448d1078d1528699257; __psuid=8ea11fd2ffc79772a6076e3509f3b5b7; _ga=GA1.2.436002455.1528699253; testSeed=118802916; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gid=GA1.2.1634143001.1530167817; _gat=1; PHPSESSID=0euvb4p77v3l7cohr1urt0ks96; _pk_id.1.9f6a=09164f9407a280fd.1528699257.30.1530167833..",
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
"cookie":"__cfduid=d1d1ab2d703c1c9001c132dc448d1078d1528699257; __psuid=8ea11fd2ffc79772a6076e3509f3b5b7; _ga=GA1.2.436002455.1528699253; testSeed=118802916; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gid=GA1.2.1634143001.1530167817; _gat=1; PHPSESSID=0euvb4p77v3l7cohr1urt0ks96; _pk_id.1.9f6a=09164f9407a280fd.1528699257.30.1530167833..",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/production-list/Boeing/737",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

session = requests.session()

class logging(object):
    def __init__(self, falconApi):
        self.logger = self._getLogger()
        
    

def toLogin(retry = 3):
    try:
        session = requests.session()
        url = 'https://www.planespotters.net/user/login'
        result = session.get(url, headers=toLoginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            raise Exception("can't not connect the internet")
        return toLogin(retry = retry-1)

def crawlLoginDo(content,retry = 3):
    try:
        soup = BeautifulSoup(content)
        csrfs = soup.findAll('input',{'id':'csrf'})
        if csrfs:
            csrf = str(csrfs[0].attrs['value'])
        url = 'https://www.planespotters.net/user/login'
        params = {'username':'TommyLee',
                'password':'TommyLee',
                'csrf':csrf,
                 'redirectId':''}
        result = session.post(url,data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            raise Exception("can't not connect the internet")
        return crawlLoginDo(retry = retry-1)
    

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

def crawlAfterLoginDo(retry = 3):
    try:
        url = 'https://www.planespotters.net/'
        result = session.get(url, headers=mainHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            _getLogger().error(str(traceback.format_exc()))
            _getLogger().error(str(Exception)+":"+str(ex))
            raise Exception("can't not connect the internet")
        return crawlAfterLoginDo(retry = retry-1)


def getDetial(url,retry = 3):
    try:
        url = 'https://www.planespotters.net'+url
        result = session.get(url, headers=detailHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            _getLogger().error(str(traceback.format_exc()))
            _getLogger().error(str(Exception)+":"+str(ex))
            return "nothing"
        return getDetial(url,retry = retry-1)
    
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


    

#login = toLogin()
#type = sys.getfilesystemencoding()
#print sys.getdefaultencoding()
#login = login.decode("gbk",'ignore').encode(type)
#login = login.decode('utf-8','ignore').encode('gbk','ignore')
#login = login.decode('gbk','ignore')
#login = login.decode('gb2312','ignore')
#login = login.decode('utf8','ignore')
#login = unicode(login)
#
#
#
#login = unicode(login,'gbk')
#login = login.decode('gbk','ignore')
#login = login.encode('utf-8','ignore')
#main = crawlLoginDo(login)

#page = urllib2.request.urlopen('https://www.planespotters.net/user/login')
#html = page.read()
message = getMessage()
createFile(message,1)
    
