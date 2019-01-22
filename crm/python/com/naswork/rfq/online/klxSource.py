'''
Created on 11 Feb 2017

@author: tanoy
'''
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
LOGGER_NAME_CRAWL = 'klx'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
HEADERS = {
    "User-Agent": AGENT,
    "Host": "www.shopklx.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, br",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1",
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

loginHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.shopklx.com",
#"Content-Type":"application/x-www-form-urlencoded;charset=UTF-8",
"Pragma":"no-cache",
#"Origin":"https://www.aviall.com",
"Referer":"https://www.shopklx.com/index.jsp?DPSLogout=true",
"Upgrade-Insecure-Requests":"1",
"User-Agent":AGENT
}

searchHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, sdch, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.aviall.com",
"Pragma":"no-cache",
#"Referer":"https://www.aviall.com/aviallstorefront/",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
}

stockMessageHeaders={
"Accept":"*/*",
"Accept-Encoding":"gzip, deflate, sdch, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.aviall.com",
"Pragma":"no-cache",
"Content-Type":"application/x-www-form-urlencoded;charset=utf-8",
#"Referer":"https://www.aviall.com/aviallstorefront/p/5145-1-77=EM",
"X-Requested-With":"XMLHttpRequest",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
}

searchHeadersPublic={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, sdch, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.aviall.com",
"Pragma":"no-cache",
#"Referer":"https://www.aviall.com/aviallstorefront/",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
}

logoutHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, sdch, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.aviall.com",
"Pragma":"no-cache",
#"Referer":"https://www.aviall.com/aviallstorefront/",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
}

session = requests.session()

def crawlDefault(retry = 3):
    try:
        url = 'https://www.shopklx.com/?_requestid=19320'
        params = {'_requestid':'19320'}
        result = session.get(url,data=params, headers=HEADERS, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlDefault(retry = retry-1)

def crawlLoginDo(retry = 3):
    try:
        cookie = session.cookies
        sessionId = cookie.values()[0]
        url = 'https://www.shopklx.com/index.jsp;jsessionid='+sessionId+'?_dyncharset=UTF-8&_dynSessConf=-2421258971754030579&%2Fatg%2Fuserprofiling%2FProfileFormHandler.value.login=BETTERAIRGROUP.PBETTERAIR&_D%3A%2Fatg%2Fuserprofiling%2FProfileFormHandler.value.login=+&%2Fatg%2Fuserprofiling%2FProfileFormHandler.value.password=GZBA0919&_D%3A%2Fatg%2Fuserprofiling%2FProfileFormHandler.value.password=+&%2Fcom%2Fklx%2Fprofile%2FSessionBean.loginFlyout=false&_D%3A%2Fcom%2Fklx%2Fprofile%2FSessionBean.loginFlyout=+&%2Fcom%2Fklx%2Fprofile%2FSessionBean.anonymousSkUId=&_D%3A%2Fcom%2Fklx%2Fprofile%2FSessionBean.anonymousSkUId=+&%2Fcom%2Fklx%2Fprofile%2FSessionBean.anonymousQuantity=&_D%3A%2Fcom%2Fklx%2Fprofile%2FSessionBean.anonymousQuantity=+&%2Fcom%2Fklx%2Fprofile%2FSessionBean.addAllToQuote=false&_D%3A%2Fcom%2Fklx%2Fprofile%2FSessionBean.addAllToQuote=+&%2Fatg%2Fuserprofiling%2FProfileFormHandler.loginErrorURL=index.jsp&_D%3A%2Fatg%2Fuserprofiling%2FProfileFormHandler.loginErrorURL=+&%2Fatg%2Fuserprofiling%2FProfileFormHandler.loginSuccessURL=index.jsp&_D%3A%2Fatg%2Fuserprofiling%2FProfileFormHandler.loginSuccessURL=+&Login=submit&_D%3ALogin=+&_DARGS=%2Fglobal%2Fincludes%2Fnav%2FnavLogin.jsp'
        params = {'_dyncharset':'UTF-8',
                '_dynSessConf':'-2421258971754030579',
                '/atg/userprofiling/ProfileFormHandler.value.login':'BETTERAIRGROUP.PBETTERAIR',
                '_D:/atg/userprofiling/ProfileFormHandler.value.login':'',
                '/atg/userprofiling/ProfileFormHandler.value.password':'GZBA0919',
                '_D:/atg/userprofiling/ProfileFormHandler.value.password':'',
                '/com/klx/profile/SessionBean.loginFlyout':'false',
                '_D:/com/klx/profile/SessionBean.loginFlyout':'',
                '/com/klx/profile/SessionBean.anonymousSkUId':'',
                '_D:/com/klx/profile/SessionBean.anonymousSkUId':'',
                '/com/klx/profile/SessionBean.anonymousQuantity':'',
                '_D:/com/klx/profile/SessionBean.anonymousQuantity':'',
                '/com/klx/profile/SessionBean.addAllToQuote':'false',
                '_D:/com/klx/profile/SessionBean.addAllToQuote':'',
                '/atg/userprofiling/ProfileFormHandler.loginErrorURL':'index.jsp',
                '_D:/atg/userprofiling/ProfileFormHandler.loginErrorURL':'',
                '/atg/userprofiling/ProfileFormHandler.loginSuccessURL':'index.jsp',
                '_D:/atg/userprofiling/ProfileFormHandler.loginSuccessURL':'',
                'Login':'submit',
                '_D:Login':'',
                '_DARGS':'/global/includes/nav/navLogin.jsp'}
        result = session.get(url,data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDo(retry = retry-1)
    

def logout(retry = 3):
    try:
        url = 'https://www.aviall.com/aviallstorefront/logout'
        result = session.post(url, headers=logoutHeaders, verify=False,timeout=90)
        session.close()
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return logout(retry = retry-1)

def crawlAfterLoginDo(retry = 3):
    try:
        url = 'https://www.shopklx.com/index.jsp?_requestid=196777'
        params = {'_requestid':'19320'}
        result = session.get(url,data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlAfterLoginDo(retry = retry-1)
    
def activateThisSession(retry = 3):
    try:
        url = 'https://www.shopklx.com/index.jsp?_requestid=374817'
        params = {'_requestid':'374817'}
        result = session.get(url,data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return activateThisSession(retry = retry-1)

    
default = crawlDefault()
login = crawlLoginDo()
afterLogin = crawlAfterLoginDo()
ifLoginSuccess = afterLogin.find("BETTER")
if ifLoginSuccess < 0:
    afterLogin = activateThisSession()
    ifLoginSuccess = afterLogin.find("PURCHASER BETTERAIR")
print afterLogin


    
