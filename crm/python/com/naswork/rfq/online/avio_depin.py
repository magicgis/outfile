
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
LOGGER_NAME_CRAWL = 'klx'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3047.4 Safari/537.36"
loginstate = 0
Username = "LIU, CINDY"
Userid = 'BETTERAIRGROUP.MQIU'
Userpass = 'affvTmXr'

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

defaultHeaders={
"Accept":"application/json, text/javascript, */*; q=0.01",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Content-Length":"211",
"Content-Type":"application/json;charset=\"UTF-8\"",
"Host":"adcommerce3.avio-diepen.com",
"Origin":"https://adcommerce3.avio-diepen.com",
"Pragma":"no-cache",
"User-Agent":AGENT,
"X-Requested-With":"XMLHttpRequest"
}

loginHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Content-Length":"72",
"Host":"adcommerce3.avio-diepen.com",
"Content-Type":"application/x-www-form-urlencoded",
"Origin":"https://adcommerce3.avio-diepen.com",
"Pragma":"no-cache",
"Upgrade-Insecure-Requests":"1",
"User-Agent":AGENT
}

searchHeaders={
'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
'Accept-Encoding':'gzip, deflate, br',
'Accept-Language':'zh-CN,zh;q=0.8',
'Cache-Control':'max-age=0',
'Connection':'keep-alive',
'Content-Length':'638',
'Content-Type':'application/x-www-form-urlencoded',
'Host':'www.shopklx.com',
'Origin':'https://www.shopklx.com',
'Referer':'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp',
'Upgrade-Insecure-Requests':'1',
'User-Agent':AGENT
}

searchdetailheaders={
'Accept':'text/html, */*; q=0.01',
'Accept-Encoding':'gzip, deflate, sdch, br',
'Accept-Language':'zh-CN,zh;q=0.8',
'Connection':'keep-alive',
'Host':'www.shopklx.com',
'Referer':'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp',
'User-Agent':AGENT,
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
        url = 'https://adcommerce3.avio-diepen.com/adcomsvc'
        params = "{tt-xml: [{num: 1, parent: 0, nodename: \"xeml\", val: \"\", attlist: \"\"},{num: 2, parent: 1, nodename: \"sub\", val: \"p-init\", attlist: \"\"},{num: 3, parent: 1, nodename: \"v-cookie\", val: \"xelogin\", attlist: \"\"}]}"
        result = session.post(url, params, headers=defaultHeaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'

def crawlLoginDo(ran):
#     cookie = session.cookies
#     sessionId = cookie.values()[0]
    try:
        url = "https://adcommerce3.avio-diepen.com/_tmp.html"
        params = {'.username':'purchaser@betterairgroup.com',
                '.password':'DnU9h2F4',
                '.department':''}
        result = session.get(url, data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'
    
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
        url = 'https://www.shopklx.com/index.jsp?_DARGS=/index.jsp'
        params = {
            '_dyncharset':'UTF-8',
            '_dynSessConf':ran ,
            '/com/klx/search/MultiPartSearchFormHandler.lazySearch':'false',
            '_D:/com/klx/search/MultiPartSearchFormHandler.lazySearch':'' ,
            '_D:/com/klx/profile/SessionBean.searchTerm': '',
            '/com/klx/profile/SessionBean.searchTerm':searchPart,
            'repositoryKey':'en_US',
            'noCrumbs':'',
            '/com/klx/search/MultiPartSearchFormHandler.activeTab':'box',
            '_D:/com/klx/search/MultiPartSearchFormHandler.activeTab': '',
            'files[]':'',
            '/com/klx/search/MultiPartSearchFormHandler.search':'Search',
            '_D:/com/klx/search/MultiPartSearchFormHandler.search': '',
            '_DARGS':'/index.jsp'
        }
        result = session.post(url, data = params, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,e:
        'do nothing'

def searchdetail():
    try:
        ran = str(random.randint(1000000000000,2000000000000))
        url = 'https://www.shopklx.com/en/search/LoadDefaultUserRefinement.jsp?loadRefinement=true&pageCount=1&_=' + ran
        result = session.get(url, headers=searchdetailheaders, verify=False,timeout=90)
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
    if logindo > 0:
        afterlogin = loginhanddle(ran)
        print '(loginhanddle run)'
    #tanoy
    check_login(afterlogin)
    return ran

def check_login(checkpage, retry = 3):
    global handler_id
    global loginstate
    logininfo = checkpage.find(Username)
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
            tr = tbody.tr
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
            file5 = 'error\do_record_'+part_num+'.html'
            try:
                with open(file5, 'w') as file_5:
                    file_5.write(searchxhr.encode("utf-8"))
            except Exception,e1:
                print e1
            search_handle(soup, part_num, info, id, elementId)
    else:
        search_handle(soup, part_num, info, id, elementId)

def search_handle(searchxhr, part_num, INFORMATION,id, elementId):
    res1 = searchxhr.findAll(attrs={ "parent-part" : part_num })
    if len(res1)==0 or len(res1[-1].findAll('td')) < 10:
        ''' position 3 '''
        INFORMATION = 'this part has no detail information.'
        save_nodetail(part_num, INFORMATION,id,elementId)
    else:
        listd = res1[-1].findAll('td')
        PARTNUM = listd[0].text.strip()
        DESCRIPTION = listd[1].text.strip()
        UNIT_PRICE = listd[10].text.strip()
        UNIT = listd[9].text.strip().upper()
        CURRENCY = (listd[10].text.strip())[0]
        CAGE_CODE = listd[2].text.strip()
        
        res2 = searchxhr.find(attrs={ "class" : "country" })
        quantity = res2.parent
        
        lis = quantity.findAll('li')
        lens = len(lis)
        
        quantitystr = ''
        i = 0
        while i < lens:
            if lis[i] != lis[-1] :
                if lis[i].get('class') == ['country']:
                    if lis[i+1].get('class') != ['country']:
                        i += 1
                        continue
            
            text = lis[i].text
            area = ''.join(filter(lambda x:(x.isalpha() or x == ' '), text))
            area = area.strip()
            num = (re.findall("[\d,]+",text))[0]
            quantitystr = quantitystr + area +'(' + num.replace(",","") +')' + ','
            i += 1
        
        STOCK_MESSAGE = quantitystr[:-1]
        
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
    sql = "select id from klx_quote where client_inquiry_id = '%s'"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def updateStatus(id):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "update klx_quote set complete = 1 where id = '%s'"%(id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()


default = crawlDefault()



