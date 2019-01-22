
# -*- coding: utf-8 -*-
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
import random
from pip._vendor.requests.models import Response
from _mysql import result
import json
LOGGER_NAME_CRAWL = 'klx'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3047.4 Safari/537.36"
loginstate = 0
Username = "LIU, CINDY"
Userid = 'BETTERAIRGROUP.PBETTERAIR'
Userpass = 'GZBA0919'

HEADERS = {
    "User-Agent": AGENT,
    "Host": "www.shopklx.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch, br",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Cache-Control": "max-age=0",
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
#"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
#"Accept-Encoding":"gzip, deflate, sdch, br",
#"Accept-Language":"zh-CN,zh;q=0.8",
#"Connection":"keep-alive",
#"Host":"www.shopklx.com",
#"Referer":"https://www.shopklx.com/",
#"Upgrade-Insecure-Requests":"1",
#"User-Agent":AGENT

"Accept":"application/json, text/javascript, */*; q=0.01",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.9",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Content-Length":"52",
"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8",
"Host":"www.shopklx.com",
"Origin":"https://www.shopklx.com",
"Pragma":"no-cache",
"Referer":"https://www.shopklx.com/index.jsp?DPSLogout=true",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
"X-NewRelic-ID":"UgUPV1BRGwAHVFVUBwA=",
"X-Requested-With":"XMLHttpRequest"
}

afterLoginHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.9",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.shopklx.com",
"Pragma":"no-cache",
"Referer":"https://www.shopklx.com/index.jsp?DPSLogout=true",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"
}

loginhandleHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"max-age=0",
"Connection":"keep-alive",
"Content-Length":"902",
"Host":"www.shopklx.com",
"Content-Type":"application/x-www-form-urlencoded",
"Origin":"https://www.shopklx.com",
"Referer":"https://www.shopklx.com/index.jsp?_requestid=492945",
"Upgrade-Insecure-Requests":"1",
"User-Agent":AGENT
}

searchHeaders={
'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
'Accept-Encoding':'gzip, deflate, br',
'Accept-Language':'zh-CN,zh;q=0.8',
#'Cache-Control':'max-age=0',
'Cache-Control':'no-cache',
'Connection':'keep-alive',
'Content-Length':'638',
'Content-Type':'application/x-www-form-urlencoded',
'Host':'www.shopklx.com',
'Origin':'https://www.shopklx.com',
#'Referer':'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp',
'Referer':'https://www.shopklx.com/?_DARGS=/',
'Upgrade-Insecure-Requests':'1',
'User-Agent':AGENT,
'Pragma': 'no-cache'
}

searchdetailheaders={
# 'Accept':'text/html, */*; q=0.01',
# 'Accept-Encoding':'gzip, deflate, sdch, br',
# 'Accept-Language':'zh-CN,zh;q=0.8',
# 'Connection':'keep-alive',
# 'Host':'www.shopklx.com',
# 'Referer':'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp',
# 'User-Agent':AGENT,
# 'X-NewRelic-ID':'UgUPV1BRGwAHVFVUBwA=',
# 'X-Requested-With':'XMLHttpRequest'                     
'Accept':'text/html, */*; q=0.01',
'Accept-Encoding':'gzip, deflate, br',
'Accept-Language':'zh-CN,zh;q=0.9',
'Cache-Control':'no-cache',
'Connection':'keep-alive',
'Host':'www.shopklx.com',
'Pragma':'no-cache',
'Referer':'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp',
'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36',
'X-NewRelic-ID':'UgUPV1BRGwAHVFVUBwA=',
'X-Requested-With':'XMLHttpRequest'
}

searchselectheaders={
'Accept':'*/*',
'Accept-Encoding':'gzip, deflate, sdch, br',
'Accept-Language':'zh-CN,zh;q=0.8',
'Connection':'keep-alive',
'Host':'www.shopklx.com',
'Referer':'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp',
'User-Agent':'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3047.4 Safari/537.36',
'X-NewRelic-ID':'UgUPV1BRGwAHVFVUBwA=',
'X-Requested-With':'XMLHttpRequest'
}

logoutHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"max-age=0",
"Connection":"keep-alive",
"Content-Length":"502",
"Host":"www.shopklx.com",
"Content-Type":"application/x-www-form-urlencoded",
"Origin":"https://www.shopklx.com",
"Referer":"https://www.shopklx.com/index.jsp?_requestid=510074",
"Upgrade-Insecure-Requests":"1",
"User-Agent":AGENT
}

def _getLogger(self):  
    import logging  
    import os  
    import inspect  
    
    logger = logging.getLogger('[Falcon]')  
      
    this_file = inspect.getfile(inspect.currentframe())  
    dirpath = os.path.abspath(os.path.dirname(this_file))  
    handler = logging.FileHandler(os.path.join(dirpath, "falconservice.log"))  
      
    formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')  
    handler.setFormatter(formatter)  
      
    logger.addHandler(handler)  
    logger.setLevel(logging.INFO)

session = requests.session()
#session = requests

def crawlDefault():
    try:
        url = 'https://www.shopklx.com/'
        result = session.get(url, headers=HEADERS, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'

def crawlLoginDo(ran):
#     cookie = session.cookies
#     sessionId = cookie.values()[0]
    try:
#        url = 'https://www.shopklx.com/index.jsp?_dyncharset=UTF-8&_dynSessConf='+ran+'&%2Fatg%2Fuserprofiling%2FProfileFormHandler.value.login='+Userid+'&_D%3A%2Fatg%2Fuserprofiling%2FProfileFormHandler.value.login=+&%2Fatg%2Fuserprofiling%2FProfileFormHandler.value.password='+Userpass+'&_D%3A%2Fatg%2Fuserprofiling%2FProfileFormHandler.value.password=+&%2Fcom%2Fklx%2Fprofile%2FSessionBean.loginFlyout=false&_D%3A%2Fcom%2Fklx%2Fprofile%2FSessionBean.loginFlyout=+&%2Fcom%2Fklx%2Fprofile%2FSessionBean.anonymousSkUId=&_D%3A%2Fcom%2Fklx%2Fprofile%2FSessionBean.anonymousSkUId=+&%2Fcom%2Fklx%2Fprofile%2FSessionBean.anonymousQuantity=&_D%3A%2Fcom%2Fklx%2Fprofile%2FSessionBean.anonymousQuantity=+&%2Fcom%2Fklx%2Fprofile%2FSessionBean.addAllToQuote=false&_D%3A%2Fcom%2Fklx%2Fprofile%2FSessionBean.addAllToQuote=+&%2Fatg%2Fuserprofiling%2FProfileFormHandler.loginErrorURL=index.jsp&_D%3A%2Fatg%2Fuserprofiling%2FProfileFormHandler.loginErrorURL=+&%2Fatg%2Fuserprofiling%2FProfileFormHandler.loginSuccessURL=index.jsp&_D%3A%2Fatg%2Fuserprofiling%2FProfileFormHandler.loginSuccessURL=+&Login=submit&_D%3ALogin=+&_DARGS=%2Fglobal%2Fincludes%2Fnav%2FnavLogin.jsp'
#        params = {'_dyncharset':'UTF-8',
#                '_dynSessConf':ran,
#                '/atg/userprofiling/ProfileFormHandler.value.login':Userid,
#                '_D:/atg/userprofiling/ProfileFormHandler.value.login':'',
#                '/atg/userprofiling/ProfileFormHandler.value.password':Userpass,
#                '_D:/atg/userprofiling/ProfileFormHandler.value.password':'',
#                '/com/klx/profile/SessionBean.loginFlyout':'false',
#                '_D:/com/klx/profile/SessionBean.loginFlyout':'',
#                '/com/klx/profile/SessionBean.anonymousSkUId':'',
#                '_D:/com/klx/profile/SessionBean.anonymousSkUId':'',
#                '/com/klx/profile/SessionBean.anonymousQuantity':'',
#                '_D:/com/klx/profile/SessionBean.anonymousQuantity':'',
#                '/com/klx/profile/SessionBean.addAllToQuote':'false',
#                '_D:/com/klx/profile/SessionBean.addAllToQuote':'',
#                '/atg/userprofiling/ProfileFormHandler.loginErrorURL':'index.jsp',
#                '_D:/atg/userprofiling/ProfileFormHandler.loginErrorURL':'',
#                '/atg/userprofiling/ProfileFormHandler.loginSuccessURL':'index.jsp',
#                '_D:/atg/userprofiling/ProfileFormHandler.loginSuccessURL':'',
#                'Login':'submit',
#                '_D:Login':'',
#                '_DARGS':'/global/includes/nav/navLogin.jsp'}
        url = 'https://www.shopklx.com/en/ajax/login.jsp?v=1538121454394'
        params = {'userName':'BETTERAIRGROUP.PBETTERAIR',
                'password':'GZBA0919',
                'v':'1538121454394'}
        result = session.post(url, data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'
        
def crawlAfterLoginDo():
#     cookie = session.cookies
#     sessionId = cookie.values()[0]
    try:
        url = 'https://www.shopklx.com/index.jsp'
        result = session.get(url, headers=afterLoginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        print e


    
def loginhanddle(ran):
    try:
        url = 'https://www.shopklx.com/?_DARGS=/registrationCompanyRequest.jsp'
        params = {
                'dyncharset':'UTF-8' ,
                '_dynSessConf':ran ,
                '/atg/userprofiling/ProfileFormHandler.value.login':Userid ,
                '_D:/atg/userprofiling/ProfileFormHandler.value.login':''  ,
                '/atg/userprofiling/ProfileFormHandler.value.password':Userpass ,
                '_D:/atg/userprofiling/ProfileFormHandler.value.password':''  ,
                '/atg/userprofiling/ProfileFormHandler.activateSession':'true' ,
                '_D:/atg/userprofiling/ProfileFormHandler.activateSession':''  ,
                '/atg/userprofiling/ProfileFormHandler.loginErrorURL':'/index.jsp' ,
                '_D:/atg/userprofiling/ProfileFormHandler.loginErrorURL':''  ,
                '/atg/userprofiling/ProfileFormHandler.loginSuccessURL':'/index.jsp' ,
                '_D:/atg/userprofiling/ProfileFormHandler.loginSuccessURL':''  ,
                '/atg/userprofiling/ProfileFormHandler.login':'ACTIVATE THIS SESSION' ,
                '_D:/atg/userprofiling/ProfileFormHandler.login':''  ,
                '_DARGS':'/registrationCompanyRequest.jsp' 
                }
        result = session.post(url, data = params, headers=loginhandleHeaders, verify=False, timeout=90)
        return result.text
    except Exception,e:
        'do nothing'

def logout(ran):
    try:
        url = 'https://www.shopklx.com/index.jsp?_DARGS=/global/includes/nav/topNavigation.jsp'
        params = {
                '_dyncharset':'UTF-8',
                '_dynSessConf':ran,
                '/atg/userprofiling/ProfileFormHandler.logoutSuccessURL':'/index.jsp',
                '_D:/atg/userprofiling/ProfileFormHandler.logoutSuccessURL': '',
                '/atg/userprofiling/ProfileFormHandler.logoutErrorURL':'/index.jsp',
                '_D:/atg/userprofiling/ProfileFormHandler.logoutErrorURL': '',
                '/atg/userprofiling/ProfileFormHandler.logout':'LOG OUT',
                '_D:/atg/userprofiling/ProfileFormHandler.logout': '',
                '_DARGS':'/global/includes/nav/topNavigation.jsp'
                  }
        result = session.post(url, data = params, headers=logoutHeaders, verify=False,timeout=90)
        session.close()
        return result.text
    except Exception,e:
        'do nothing'

def search(ran, searchPart):
    try:
#        url = 'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp'
#        params = {
#            '_dyncharset':'UTF-8',
#            '_dynSessConf':ran ,
#            '/com/klx/search/MultiPartSearchFormHandler.lazySearch':'false',
#            '_D:/com/klx/search/MultiPartSearchFormHandler.lazySearch':'' ,
#            '_D:/com/klx/profile/SessionBean.searchTerm': '',
#            '/com/klx/profile/SessionBean.searchTerm':searchPart,
#            'repositoryKey':'en_US',
#            'noCrumbs':'',
#            '/com/klx/search/MultiPartSearchFormHandler.activeTab':'box',
#            '_D:/com/klx/search/MultiPartSearchFormHandler.activeTab': '',
#            'files[]':'',
#            '/com/klx/search/MultiPartSearchFormHandler.search':'Search',
#            '_D:/com/klx/search/MultiPartSearchFormHandler.search': '',
#            '_DARGS':'/index.jsp'
#        }
        
        url = 'https://www.shopklx.com/?_DARGS=/'
        params = {
            '_dyncharset':'UTF-8',
            '_dynSessConf':ran,
            '/com/klx/search/MultiPartSearchFormHandler.activeTab':'box',
            '_D:/com/klx/search/MultiPartSearchFormHandler.activeTab':' ',
            '/com/klx/search/MultiPartSearchFormHandler.lazySearch':'false',
            '_D:/com/klx/search/MultiPartSearchFormHandler.lazySearch':' ',
            '_D:bulk-part':' ',
            'bulk-part':searchPart,
            'files[]':'',
            '/com/klx/search/MultiPartSearchFormHandler.search':'Search',
            '_D:/com/klx/search/MultiPartSearchFormHandler.search':' ',
            '_DARGS':'/'
        }
        result = session.post(url, data = params, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'
        
def getDetail(url):
    url = "https://www.shopklx.com"+str(url)
    try:
        detailHeaders={
                        "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
                        "Accept-Encoding":"gzip, deflate, br",
                        "Accept-Language":"zh-CN,zh;q=0.9",
                        "Cache-Control":"no-cache",
                        "Connection":"keep-alive",
                        "Host":"www.shopklx.com",
                        "Pragma":"no-cache",
                        "Upgrade-Insecure-Requests":"1",
                        "User-Agent":AGENT
        }
        result = session.get(url, headers=detailHeaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'
        
def getDetailPartNumber(url):
    content = getDetail(url)
    soup = BeautifulSoup(content)
    partNumber = soup.findAll('span',{'class':'p-num'})[0].text
    if partNumber:
        if partNumber.strip() != "":
            return partNumber
        else:
            partNumbers = soup.findAll('h4',{'class':'sub-heading'})
            if len(partNumbers) > 0:
                partNumberH4 = partNumbers[0].text.strip().split(" ")[0].strip()
                partNumber = partNumberH4
                return partNumber
    return "";

def searchdetail():
    try:
        ran = str(random.randint(1000000000000,2000000000000))
        url = 'https://www.shopklx.com/en/search/LoadDefaultUserRefinement_new.jsp?v=1538287961183&loadRefinement=true&pageCount=1'
        params = {
            'v':'1538287961183',
            'loadRefinement':'loadRefinement',
            'pageCount':'1'
        }
        result = session.get(url, data = params, headers=searchdetailheaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'

def searchselect(a, b, c, d):
    try:
        ran = str(random.randint(1000000000000,2000000000000))
        url = 'https://www.shopklx.com/en/search/loadSelectedPrimePart.jsp?loadRefinement=true&selectedPart='+a+'&selectedSkuId='+b+'&parentPart='+c+'&multiPartialParent=&isFromPartialSearch=false&lpartToReset=&sinventoryValue='+d+'&_='+ran
        result = session.get(url, headers=searchselectheaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'

def login_do():
    ''' index '''
    Res1 = crawlDefault()
     
    ''' recordindex '''
#         file1 = 'index.html'
#         with open(file1, 'w') as file_1:
#             file_1.write(Res1.encode("utf-8"))
    
    soup = BeautifulSoup(Res1)#, 'lxml')
    
    stt = soup.find(attrs={"name": "_dynSessConf"})
    
    ''' handle id '''
    ran = stt['value']
    
    ''' login '''
    global afterlogin
    afterlogin = crawlLoginDo(ran)
    logindo = afterlogin.find("username is incorrect")
    ll = afterlogin.find("PURCHASER BETTERAIR")
    lb = crawlAfterLoginDo()
    llb = lb.find("PURCHASER BETTERAIR")
    if logindo > 0:
        afterlogin = loginhanddle(ran)
        print '(loginhanddle run)'
    #tanoy
    check_login(lb)
    return ran

def check_login(checkpage, retry = 3):
    global handler_id
    global loginstate
    logininfo = checkpage.find("PURCHASER BETTERAIR")
    if logininfo >= 0:
        if retry == 3 :
            loginstate = 1
#             print 'online_now'
        else:
            loginstate = 2
            print '(login_again)'
    else:
        if retry > 0:
            handler_id = login_do()
            check_login(checkpage=afterlogin, retry = retry-1)
        else:
            print '(login fail)'
            exit(0)
            
def search_do(handler_id, part_num, id,elementId, retry = 2):
    #time.sleep(1)
    searchpage = search(handler_id, part_num)
    if searchpage:
        check_login(searchpage)
    if loginstate == 2:
        return 'search_again'
    
    searchxhr = searchdetail()
    beforehandle(searchxhr, part_num,id,elementId)
    return ''

def beforehandle(searchxhr, part_num,id,elementId):
    soup = BeautifulSoup(searchxhr)#, 'lxml')
    info = ''
    erro = soup.findAll(attrs={ "class":"erro-msg" })
    if len(erro) > 0:
        erromsg = erro[-1].text
        if erromsg.find('This part is not recognized') >= 0:
            ''' position 2 '''
            info = 'This part is not recognized.'
            save_nodetail(part_num, info, id, elementId)
        elif erromsg.find('Out of stock in your warehouse location') >= 0:
            ''' position 6 '''
            info = 'Out of stock in your warehouse location.'
            save_nodetail(part_num, info, id, elementId)
        elif erromsg.find('There is a sales restriction on this part') >= 0:
            ''' position new'''
            info = 'There is a sales restriction on this part.'
            save_nodetail(part_num, info, id, elementId)
        elif erromsg.find('This part is only sold in increments of') >= 0:
            ''' position 5 '''
            info = erro[-1].text
            search_handle(soup, part_num, info, id, elementId)
        elif erromsg.find('made by multiple manufacturers') >= 0 or erromsg.find('found an exact match under multiple part numbers') >= 0:
            ''' position 4 '''
            tbody = soup.find('tbody')
            trs = tbody.findAll('tr')
            len(trs)
            for tr in trs:
                selectedPart = tr['prime-part']
                selectedSkuId = tr['sku-id']
                parentPart = tr['parent-part']
                td = tr.findAll('td')
                sinventoryValue = (td[8].text).replace(',','').strip()
                searchxhr = searchselect(selectedPart, selectedSkuId, parentPart, sinventoryValue)
    #             file5 = 'error\do_xhr2_'+part_num+'.html'
    #             try:
    #                 with open(file5, 'w') as file_5:
    #                     file_5.write(searchxhr.encode("utf-8"))
    #             except Exception,e1:
    #                 print '(recordfail)'+e1
                beforehandle(searchxhr, part_num,id,elementId)
        elif erromsg.find('Back to Multiple Match List') >= 0 or erromsg.find('Back To List Of Manufacturers') >= 0:
            ''' after position 4 '''
            search_handle(soup, part_num, info, id, elementId)
        elif erromsg.find('<br ') >= 0 or erromsg.strip() == '':
            search_handle(soup, part_num, info, id, elementId)
        else:
            print '(other position)' + part_num + ' : ' + erromsg.strip()
            file5 = 'error\do_record_'+part_num+''+'.html'
            try:
                with open(file5, 'w') as file_5:
                    file_5.write(searchxhr.encode("utf-8"))
            except Exception,e1:
                print e1
            search_handle(soup, part_num, info, id, elementId)
    else:
        search_handle(soup, part_num, info, id, elementId)

def search_handle(searchxhr, part_num, INFORMATION,id, elementId):
    res1 = searchxhr.findAll('tr',attrs={ "parent-part" : part_num })
    if len(res1)==0 or len(res1[-1].findAll('td')) < 10:
        ''' position 3 '''
        INFORMATION = 'this part has no detail information.'
        save_nodetail(part_num, INFORMATION,id,elementId)
    else:
        listd = res1[-1].findAll('td')
        ths = res1[0].findAll('th')
#        priceIndex = 0
#        for index in range(len(ths)):
#            if ths[index].find('Price') >= 0:
#                priceIndex = index
        
        for line in res1:
            listLine = line.findAll('td')
            if len(listLine) > 0:
                pIndex = 9
#                if priceIndex != 0:
#                    pIndex = priceIndex
                if listLine[pIndex].text.strip() != "-":
                    listd = line.findAll('td')
        a = listd[0].findAll('a')
        url = a[0].attrs['href']
        partNumber = getDetailPartNumber(url).strip()
        PARTNUM = listd[0].text.strip()
        if PARTNUM != partNumber:
            if partNumber != "":
                PARTNUM = partNumber
        DESCRIPTION = listd[1].text.strip()
        UNIT_PRICE = ''
        UNIT = listd[8].text.strip().upper()
#        if priceIndex != 0:
#            UNIT_PRICE = listd[priceIndex].text.strip()
#        else:
#            UNIT_PRICE = listd[9].text.strip()
        UNIT_PRICE = listd[9].text.strip()
        CURRENCY = ''
        #if len(listd[9].text.strip()) > 0:
        if len(UNIT_PRICE) > 0:
            CURRENCY = (UNIT_PRICE)[0]
#            if priceIndex != 0:
#                CURRENCY = (UNIT_PRICE)[0]
#            else:
#                CURRENCY = (UNIT_PRICE)[0]
            #CURRENCY = (listd[9].text.strip())[0]
        CAGE_CODE = listd[2].text.strip()
        STOCK_MESSAGE = ''
        
        sms = searchxhr.findAll('div',attrs={ "class" : "qty-wrapper mrg-lft-6-per" })
        if(sms):
            for sm in sms:
                stockArray = sm.findAll('div')
                if (len(stockArray) >= 2):
                    if (STOCK_MESSAGE == ''):
                        stockArray[1] = stockArray[1].text.strip().replace(',','')
                        STOCK_MESSAGE = stockArray[0].text.strip() + '(' + stockArray[1] + ')'
                    else:
                        STOCK_MESSAGE = STOCK_MESSAGE + ',' + stockArray[0].text.strip() + '(' + stockArray[1].text.strip() + ')'
		#sm = searchxhr.findAll('div',attrs={"class" : "qty-wrapper mrg-lft-6-per"})
#		if (sm){
#			for s in sm:
#				stockArray = s.findAll('div')
#				if (len(stockArray) > 2):
#					if (STOCK_MESSAGE == ''):
#						STOCK_MESSAGE = stockArray[0].text.strip() + '(' + stockArray[1].text.strip() + ')'
#					else:
#						STOCK_MESSAGE = STOCK_MESSAGE + ',' + stockArray[0].text.strip() + '(' + stockArray[1].text.strip() + ')'

		
        #res2 = searchxhr.find(attrs={ "class" : "country" })
        #if (res2):
            #quantitystr = ''
            #quantity = res2.parent
            #lis = quantity.findAll('li')
            #lens = len(lis)
            
            
            #i = 0
            #while i < lens:
                #if lis[i] != lis[-1] :
                    #if lis[i].get('class') == ['country']:
                        #if lis[i+1].get('class') != ['country']:
                            #i += 1
                            #continue
                
                #text = lis[i].text
                #area = ''.join(filter(lambda x:(x.isalpha() or x == ' '), text))
                #area = area.strip()
                #num = (re.findall("[\d,]+",text))[0]
                #quantitystr = quantitystr + area +'(' + num.replace(",","") +')' + ','
                #i += 1
            
            #STOCK_MESSAGE = quantitystr[:-1]
        
        AMOUNT = '1'
        if INFORMATION.find('This part is only sold in increments of') >= 0:
            AMOUNT = ((re.findall("[\d,]+",INFORMATION))[0]).replace(',', '')
        
        INFORMATION = INFORMATION.strip()
        save_detail(PARTNUM, DESCRIPTION, UNIT_PRICE, UNIT, CURRENCY, CAGE_CODE, STOCK_MESSAGE, INFORMATION,id,elementId, AMOUNT)

def save_detail(a,b,c,d,e,f,g,h,id,elementId, AMOUNT):
    conn = MySQLdb.connect(host="127.0.0.1",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
     
    sql = "insert into klx_quote_element(PART_NUMBER, DESCRIPTION, UNIT_PRICE, UNIT, CURRENCY, CAGE_CODE, STOCK_MESSAGE, INFORMATION,KLX_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID,AMOUNT) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)"%(a,b,c,d,e,f,g,h,id,elementId,AMOUNT)
    cursor.execute(sql)
     
    print a + ' : finish'
    conn.commit()
    conn.close()

def save_nodetail(a,b,id,elementId):
    conn = MySQLdb.connect(host="127.0.0.1",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
     
    sql = "insert into klx_quote_element(PART_NUMBER, INFORMATION,KLX_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID) values('%s', '%s', '%s', '%s')"%(a, b,id,elementId)
    cursor.execute(sql)
     
    print a + ' : no_detail_finish'
    conn.commit()
    conn.close()

def readsql():
    conn = MySQLdb.connect(host="127.0.0.1",user="root",passwd="root",db="klx",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT * FROM search limit 0,10"
    cursor.execute(sql)
    res = cursor.fetchall()
    cursor.close()
    conn.close()
    for resde in res:
        print '---------------'+str(resde[0])+'--------------'
        
        while True:
            if search_do(handler_id, resde[1]) != 'search_again':
                break

def log_out_do(ran):
    ''' log out '''
    global loginstate
    if loginstate == 1 or loginstate == 2:
        logoutres = logout(ran)
        if logoutres.find(Username) < 0:
            loginstate = 0
            print '(logout succeed)'

def getInquiryElement(clientInquiryId):
    pl = getInquiryList(clientInquiryId)
    partlist = []
    for l in pl:
        data = {}
        if len(l) > 0:
            data["id"] = str(l[0])
        if len(l) > 1:
            data["pn"] = str(l[1])
        if len(data) > 0:
            partlist.append(data)
    return partlist

def getInquiryList(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "select cie.ID,cie.PART_NUMBER AS pn from client_inquiry_element cie WHERE cie.CLIENT_INQUIRY_ID = '%s'"%(clientInquiryId)
    cursor.execute(sql)
    l = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return l
            
def addKlxQuote(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into klx_quote(client_inquiry_id) values('%s')"%(clientInquiryId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def selectByClientInquiryId(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "select max(id) from klx_quote where client_inquiry_id = '%s'"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def updateStatus(id):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "update klx_quote set complete = 1 where client_inquiry_id = '%s'"%(id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def getSearchCountInAWeek(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT COUNT(*) FROM klx_quote_element sqe WHERE DATEDIFF(NOW(),sqe.UPDATE_DATETIME) <= 7 AND sqe.PART_NUMBER = '%s' ORDER BY sqe.ID DESC"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def getClientCode(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT c.`CODE` FROM client_inquiry ci LEFT JOIN client c ON c.ID = ci.CLIENT_ID WHERE ci.ID = '%s'"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def getRecordInWeek(part):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    #sql = "SELECT * FROM stock_market_crawl smc LEFT JOIN stock_market_crawl_message smcm ON smcm.STOCK_MARKET_CRAWL_ID = smc.ID WHERE smc.ID = '%s' AND smcm.PART_NUMBER = '%s'"%(id,part)
    #sql = "SELECT smcm.PART_NUMBER,smcm.AMOUNT,smcm.CONDITION_VALUE,smcm.SUPPLIER_CODE FROM stock_market_crawl smc LEFT JOIN stock_market_crawl_message smcm ON smcm.STOCK_MARKET_CRAWL_ID = smc.ID WHERE smc.ID = '%s' AND smcm.PART_NUMBER = '%s' AND smcm.SUPPLIER_COMMISSION_SALE_ELEMENT_ID = (SELECT DISTINCT smcm.SUPPLIER_COMMISSION_SALE_ELEMENT_ID FROM stock_market_crawl smc LEFT JOIN stock_market_crawl_message smcm ON smcm.STOCK_MARKET_CRAWL_ID = smc.ID WHERE smc.ID = '%s' AND smcm.PART_NUMBER = '%s' LIMIT 0,1)"%(id,part,id,part)
    sql="SELECT "\
            "sqe.CERTIFICATION, "\
            "sqe.CURRENCY, "\
            "sqe.DESCRIPTION, "\
            "sqe.LEAD_TIME, "\
            "sqe.PART_NUMBER, "\
            "sqe.STOCK_MESSAGE, "\
            "sqe.UNIT_PRICE, "\
            "sqe.UNIT, "\
            "sqe.CAGE_CODE, "\
            "sqe.INFORMATION, "\
            "sqe.AMOUNT "\
        "FROM "\
            "klx_quote_element sqe "\
        "WHERE "\
            "DATEDIFF(NOW(), sqe.UPDATE_DATETIME) <= 7 "\
        "AND sqe.PART_NUMBER = '%s' "\
        "AND sqe.CLIENT_INQUIRY_ELEMENT_ID = ( "\
            "SELECT "\
                "aqe.CLIENT_INQUIRY_ELEMENT_ID "\
            "FROM "\
                "klx_quote_element aqe "\
            "WHERE "\
                "DATEDIFF(NOW(), aqe.UPDATE_DATETIME) <= 7 "\
            "AND aqe.PART_NUMBER = '%s' "\
            "ORDER BY "\
                "aqe.CLIENT_INQUIRY_ELEMENT_ID DESC "\
            "LIMIT 0, "\
            "1 "\
        ")"%(str(part),str(part))
    cursor.execute(sql)
    data = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return data

def insertDBForCopy(data):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into klx_quote_element(PART_NUMBER, DESCRIPTION, UNIT_PRICE, UNIT, CURRENCY, CAGE_CODE, STOCK_MESSAGE, INFORMATION,KLX_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID,AMOUNT) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)"%(data['PART_NUMBER'], data['DESCRIPTION'], data['UNIT_PRICE'],data['UNIT'],data['CURRENCY'],data['CAGE_CODE'],data['STOCK_MESSAGE'],data['INFORMATION'],data['crawlId'],data['elementId'],data['AMOUNT'])
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def copyRecord(part,elementId,crawlId):
    tup = getRecordInWeek(part)
    if len(tup) > 0:
        for row in tup :
            currentMessage = {}
            if str(row[0]) != 'None':
                currentMessage['CERTIFICATION'] = str(row[0])
            else:
                currentMessage['CERTIFICATION'] = ''
            if str(row[1]) != 'None':
                currentMessage['CURRENCY'] = str(row[1])
            else:
                currentMessage['CURRENCY'] = ''
            if str(row[2]) != 'None':
                currentMessage['DESCRIPTION'] = str(row[2])
            else:
                currentMessage['DESCRIPTION'] = ''
            if str(row[3]) != 'None':
                currentMessage['LEAD_TIME'] = str(row[3])
            else:
                currentMessage['LEAD_TIME'] = ''
            if str(row[4]) != 'None':
                currentMessage['PART_NUMBER'] = str(row[4])
            else:
                currentMessage['PART_NUMBER'] = ''
            if str(row[5]) != 'None':
                currentMessage['STOCK_MESSAGE'] = str(row[5])
            else:
                currentMessage['STOCK_MESSAGE'] = ''
            if str(row[6]) != 'None':
                currentMessage['UNIT_PRICE'] = str(row[6])
            else:
                currentMessage['UNIT_PRICE'] = ''
            if str(row[7]) != 'None':
                currentMessage['UNIT'] = str(row[7])
            else:
                currentMessage['UNIT'] = ''
            if str(row[8]) != 'None':
                currentMessage['CAGE_CODE'] = str(row[8])
            else:
                currentMessage['CAGE_CODE'] = ''
            if str(row[9]) != 'None':
                currentMessage['INFORMATION'] = str(row[9])
            else:
                currentMessage['INFORMATION'] = ''
            if str(row[10]) != 'None':
                currentMessage['AMOUNT'] = str(row[10])
            else:
                currentMessage['AMOUNT'] = ''
            currentMessage['crawlId'] = crawlId
            currentMessage['elementId'] = elementId
            insertDBForCopy(currentMessage)


