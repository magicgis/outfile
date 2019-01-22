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
import json
import datetime
from random import choice
from string import strip
import time
LOGGER_NAME_CRAWL = 'satair'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
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

defaultHeaders = {
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Cache-Control":"no-cache",
    "Connection": "keep-alive",
    "Host": "www.stockmarket.aero",
    "Pragma":"no-cache",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent": AGENT,
}

beforeLoginHeaders = {
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, br",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Cache-Control":"no-cache",
    "Connection": "keep-alive",
    "Host": "www.stockmarket.aero",
    "Pragma":"no-cache",
    "Referer":"http://www.stockmarket.aero/StockMarket/Welcome.do",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent": AGENT,
}

loginHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Content-Type":"application/x-www-form-urlencoded",
"Host":"www.stockmarket.aero",
"Origin":"https://www.stockmarket.aero",
"Pragma":"no-cache",
#"Referer":"https://www.partsbase.com/landing/login?ImpMSContactID=0",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
}

afterLoginHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate",
"Accept-Language":"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.stockmarket.aero",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
}

searchHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate",
"Accept-Language":"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3",
"Connection":"keep-alive",
"Host":"www.stockmarket.aero",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36",
"Upgrade-Insecure-Requests":"1"
}

logoutHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate",
"Accept-Language":"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3",
"Authorization":"296880,021048B98,MLAQSBhs+OoiRRxVQDf98Nxy/i8sT5ixjiOSqjaxSDVpLRqvlCfEbpnwDgIvdcANNeAv2Y2xi+1Bg47kHJPCqL4WIszy331Yq2Vu1mdrykRf2bNtE8GjKTacO4bWuxkqEj/NIFAyOZ21esEh2eyOTJnODSNrWiE3DJTtsBx/ya5EH2SE3Y6UNY6CStCTHTJI",
"Connection":"keep-alive",
"Host":"www.stockmarket.aero",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
}

sendEmailHeaders={
"Accept":"*/*",
"Accept-Encoding":"gzip, deflate",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Content-Length":"156",
"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8",
"Host":"www.stockmarket.aero",
"Origin":"http://www.stockmarket.aero",
"Pragma":"no-cache",
"X-Requested-With":"XMLHttpRequest",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
}

clickFirstHeaders={
"Accept":"*/*",
"Accept-Encoding":"gzip, deflate",
"Accept-Language":"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3",
"Connection":"keep-alive",
"Host":"www.stockmarket.aero",
"X-Requested-With":"XMLHttpRequest",
"Content-Length":"0",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
}

clickSecondHeaders={
"Accept":"*/*",
"Accept-Encoding":"gzip, deflate",
"Accept-Language":"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3",
"Connection":"keep-alive",
"Host":"www.stockmarket.aero",
"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8",
"X-Requested-With":"XMLHttpRequest",
"Content-Length":"160",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
}

nextPageHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.stockmarket.aero",
"Pragma":"no-cache",
"X-Requested-With":"XMLHttpRequest",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
}

getEmailHeaders={
"Accept":"*/*",
"Accept-Encoding":"gzip, deflate",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.stockmarket.aero",
"Pragma":"no-cache",
"X-Requested-With":"XMLHttpRequest",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36"
}

session = requests.session()


def crawlDefault(retry = 3):
    try:
        url = 'http://www.stockmarket.aero/StockMarket/Welcome.do'
        result = session.get(url, headers=defaultHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlDefault(retry = retry-1)

def toLogin(retry = 3):
    try:
        cookie = session.cookies
        sessionId = cookie.values()[0]
        url = 'https://www.stockmarket.aero/StockMarket/LoadLogin.do;jsessionid='+sessionId
        result = session.get(url, headers=beforeLoginHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return toLogin(retry = retry-1)

def crawlLoginDo(retry = 3):
    try:
        url = 'https://www.stockmarket.aero/StockMarket/LoginAction.do'
        checkBody = {'username': 'purchaser@betterairgroup.com', 'password': 'Purchaser111222', 'group1': 'signIn'}
        result = session.post(url,checkBody, headers=loginHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDo(retry = retry-1)

def afterLoginDo(retry = 3):
    try:
        cookie = session.cookies
        sessionId = cookie.values()[0]
        url = 'http://www.stockmarket.aero/StockMarket/Welcome.do;jsessionid='+sessionId
        result = session.get(url, headers=afterLoginHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return afterLoginDo(retry = retry-1)

#def beforeSearch(part,retry = 3):
#    try:
#        url = 'https://www.partsbase.com/api/api/PbSearch/AutocompleteItems?employeeId=274712&searchText='+part
#        params = {'employeeId':'274712',
#                  'searchText':part}
#        searchHeaders['Referer'] = "https://www.partsbase.com/search/cFindBasicParts"
#        result = session.get(url,params, headers=searchHeaders, verify=False,timeout=120)
#        return result.text
#    except Exception,ex:
#        if retry < 1:
#            return "nothing"
#        return crawlLoginDo(retry = retry-1)
    
    
def searchPart(part,retry = 3):
    try:
        url = 'http://www.stockmarket.aero/StockMarket/SearchActionR.do?theAction=search&pager.offset=0&condition=STOCK&startRow=1&loadPppConfirmTab=&communityName=&partial=false&itemdetailaction=&partNumber='+part
#        params = {'theAction': 'search', 'pager.offset': '0', 'condition': 'ALL','startRow':'1','loadPppConfirmTab':'','communityName':'','partial':'false','itemdetailaction':'','partNumber':'D31865-111'}
        result = session.get(url, headers=searchHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        print ex
        return searchPart(part,retry = retry-1)
    
def logout(retry = 3):
    try:
        url = 'http://www.stockmarket.aero/StockMarket/LogoutAction.do'
        result = session.get(url, headers=logoutHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        print ex
        return logout(retry = retry-1)
    
def getElement(content,supplierArray,supplierMap,part,id,commissionElementId):
    content = content.replace('<img alt="Display Image" src="images/check_trans2.gif" border="0" height="10" /></a></div>','</a>')
    soup = BeautifulSoup(content)
    trs = soup.findAll('tr',{'style':'display: none;'})
    for tr in trs:
        row = tr.attrs['class']
#        aLink = tr.findAll('a',{'class':'showlink'})
#        if len(aLink):
#            onclick = aLink[0].attrs['onclick']
#            onclickTexts = onclick.split(",")
#            if len(onclickTexts) >= 2:
#                onclickTexts = onclickTexts[1].split("=")
#                if len(onclickTexts) >= 2:
                    #row = onclickTexts[1]
        regexString = "grp"+row[0]
        regexp = re.compile(regexString)
        trs = soup.findAll('tr', {'id': regexp})
        if len(trs) > 0:
            names = trs[0].findAll('td',{'class':'vendname'})
            if len(names) > 0:
                inArray = False
                for supplier in supplierArray:
                    supplier = supplier.replace("(","").replace(")","")
                    name = names[0].text.strip().replace("(","").replace(")","")
                    result = re.match(supplier.strip(),name)
                    if result != None:
                        value = supplierMap.get(names[0].text.strip())
                        if value == None:
                            supplierMap[names[0].text.strip()] = row[0]
                            inArray = True
                            break
                        elif value == row[0]:
                            inArray = True
                            break
                if inArray:
                    regexString = ".*cell"
                    regexp = re.compile(regexString)
                    tds = tr.findAll('td',{'class':regexp})
                    partNumber = tds[0].findAll('a')[0].text.strip()
                    partNumberShortList = re.findall(r"[a-z0-9A-Z]",partNumber)
                    partNumberShort = ''.join(partNumberShortList)
                    partShortList = re.findall(r"[a-z0-9A-Z]",part)
                    partShort = ''.join(partShortList)
                    amount = ''
                    if len(tds) >= 3:
                        amount = tds[2].text.strip()
                    condition = ''
                    if len(tds) >= 4:
                        condition = tds[3].text.strip()
                    if partNumberShort == partShort:
                        if condition == 'N/A' or condition == 'OR' or condition == 'REQUEST' or condition == 'AR' or condition == 'SV' or condition == 'OH' or condition == 'FN' or condition == 'NE' or condition == 'NS' or condition == 'NA':
                            if condition == 'N/A' or condition == 'OR' or condition == 'REQUEST' or condition == 'NA':
                                condition = 'AR'
                            elif condition == 'FN' or condition == 'NS' or condition == 'OR':
                                condition = 'NE'
                            currentMessage = {}
                            currentMessage['partNumber'] = partNumber
                            currentMessage['amount'] = amount
                            currentMessage['condition'] = condition
                            currentMessage['supplierCode'] = MySQLdb.escape_string(names[0].text.strip())
                            currentMessage['foreignKey'] = str(id)
                            currentMessage['commissionElementId'] = commissionElementId
                            currentMessage['isCopy'] = 0
                            if amount != "" and int(amount) > 0:
                                insertStockMarketCrawlMessage(currentMessage)


def crawlNextPage(content,supplierArray,supplierMap,part,id,commissionElementId):
    soup = BeautifulSoup(content)
    next = soup.findAll('a', {'title': 'Next Page'})
    while len(next) > 0:
        foo = [1,2]
        time.sleep(choice(foo))
        url = next[0].attrs['href']
        index = url.find('&')
        url = url[index:]
        nextText = nextPage(url)
        getElement(nextText,supplierArray,supplierMap,part,id,commissionElementId)
        soup = BeautifulSoup(nextText)
        next = soup.findAll('a', {'title': 'Next Page'})

    
def getEachRow(content,pn,quoteNumber,amount,needBy,clientInquiryId,clientInquiryElementId):
    firstLevel = list()
    secondLevel = list()
    thirdLevel = list()
    soup = BeautifulSoup(content)
    pages = list()
    regexString = "grp.*"
    regexp = re.compile(regexString)
    trs = soup.findAll('tr',{'id':regexp})
    foo = [4,5,6,7,8]
    amount = 1 
    ASA_100String = ".*ASA-100.*"
    AS9120String = ".*AS9120.*"
    AC00_56String = ".*AC00-56.*"
    ISO9001String = ".*ISO 9001.*"
    regexASA_100 = re.compile(ASA_100String)
    regexAS9120 = re.compile(AS9120String)
    regexAC00_56 = re.compile(AC00_56String)
    regexISO9001 = re.compile(ISO9001String)
    content.find("#A90A08")
    pageNo = soup.findAll('font',{'color':'#A90A08'})
    for tr in trs:
        ASA_100 = tr.findAll('img',{'title':regexASA_100})
        AS9120 = tr.findAll('img',{'title':regexAS9120})
        AC00_56 = tr.findAll('img',{'title':regexAC00_56})
        ISO9001 = tr.findAll('img',{'title':regexISO9001})
        if len(ASA_100) > 0 or len(AS9120) > 0 or len(AC00_56) > 0 :
            firstLevel.append(str(tr)+str(pageNo[0]))
        elif len(ISO9001) > 0 :
            secondLevel.append(str(tr)+str(pageNo[0]))
        else :
            thirdLevel.append(str(tr)+str(pageNo[0]))
    next = soup.findAll('a',{'title':'Next Page'})
    pages.append(soup)
    while len(next) > 0:
        url = next[0].attrs['href']
        index = url.find('&')
        url = url[index:]
        nextText = nextPage(url)
        soup2 = BeautifulSoup(nextText)
        pages.append(soup2)
        regexString = "grp.*"
        regexp = re.compile(regexString)
        trs = soup2.findAll('tr',{'id':regexp})
        amount = 1
        pageNo = soup2.findAll('font',{'color':'#A90A08'})
        for tr in trs:
            ASA_100 = tr.findAll('img',{'title':regexASA_100})
            AS9120 = tr.findAll('img',{'title':regexAS9120})
            AC00_56 = tr.findAll('img',{'title':regexAC00_56})
            ISO9001 = tr.findAll('img',{'title':regexISO9001})
            if len(ASA_100) > 0 or len(AS9120) > 0 or len(AC00_56) > 0 :
                firstLevel.append(str(tr)+str(pageNo[0]))
            elif len(ISO9001) > 0 :
                secondLevel.append(str(tr)+str(pageNo[0]))
            else :
                thirdLevel.append(str(tr)+str(pageNo[0]))
        next = soup2.findAll('a',{'title':'Next Page'})
    print firstLevel
    print secondLevel
    print thirdLevel
    allLevel = firstLevel + secondLevel + thirdLevel
    for record in allLevel:
        record = BeautifulSoup(record)
        tr = record.findAll("tr")[0]
        cmps = tr.findAll('input',{'class':"cmp"})
        if len(cmps) > 0:
            cmp = cmps[0].attrs['value']
            first = clickSendFirst(cmp,pn)
            rowId = tr.attrs['id'][3:]
            cellId = "pd"+rowId+"0"
            cells = list()
            for index,page in enumerate(pages):
                pageNo = record.findAll('font',{'color':'#A90A08'})[0].text.strip()
                if index+1 == int(pageNo):
                    cells = page.findAll('a',{'id':cellId})
                if len(cells):
                    break
            if len(cells):
                pama = cells[0].attrs['onclick']
                if len(pama) > 0:
                    pama = pama[19:]
                    pamas = pama.split("\',\'")
                    key = pamas[0][2:]
                    second = clickSendSecond(key,pn)
                    second.find("Submit RFQ")
                    sysStm = key.split("=")[-1]
                    message = getEmailMessage(sysStm[:-1])
                    soup3 = BeautifulSoup(message)
                    trs = soup3.findAll('tr')
                    if len(trs) > 0:
                        email = trs[2].findAll('td')[1].text.strip()
                        emailIndex = email.find('@')
                        endWith = email[emailIndex:]
                        print endWith
                        tup = selectByEmail(endWith)
                        if tup:
                            id = int(tup[0])
                            note = checkIfHasRecord(clientInquiryId,clientInquiryElementId,str(id))
                            if  not note:
                                insertDB(clientInquiryId,clientInquiryElementId,str(id),pn,email)
                            else:
                                amount = amount - 1
                        else:
                            names = tr.findAll('td',{'class':'vendname'})
                            print names[0].text.strip()
                            insertStockMarket(clientInquiryId,clientInquiryElementId,names[0].text.strip().replace('\'',''),0)
#                            if len(names) > 0:
#                                email = sendEmail(quoteNumber,amount,needBy)
#                                success = email.find("Your RFQ has been sent successfully")
#                                if success > 0:
#                                    insertStockMarket(clientInquiryId,clientInquiryElementId,names[0].text.strip().replace('\'',''),1)
#                                else:
#                                    insertStockMarket(clientInquiryId,clientInquiryElementId,names[0].text.strip().replace('\'',''),0)
                        if amount == 10 :
                            break
                        amount = amount + 1

def nextPage(param,retry = 3):
    try:
        url = 'http://www.stockmarket.aero/StockMarket/SearchActionR.do?loadPppConfirmTab=&communityName=PUBLIC&itemdetailaction='+param
        result = session.get(url, headers=nextPageHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        print ex
        return nextPage(param,retry = retry-1)
    
def getEmailMessage(sysStm,retry = 3):
    try:
        url = 'http://www.stockmarket.aero/StockMarket/LoadVendorDetailAction.do?&sysStm='+sysStm+'&_=1510649174530'
        result = session.get(url, headers=getEmailHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        print ex
        return getEmailMessage(sysStm,retry = retry-1)
                
            
    
def clickSendFirst(cmp,pn,retry = 3):
    try:
        url = 'http://www.stockmarket.aero/StockMarket/rowClickAction.do?cmp='+cmp+'&pn='+pn
        params = {'cmp': cmp, 'pn': pn}
        result = session.post(url,params, headers=clickFirstHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        print ex
        return clickSendFirst(cmp,pn,retry = retry-1)
    
def clickSendSecond(idMap,pn,retry = 3):
    try:
        url = 'http://www.stockmarket.aero/StockMarket/LoadItemDetailAction.do?actiontype=rfq&idMap='+idMap
        params = {'imgclick': "N", 'theAction': "search","itemdetailaction":"","showRfqAllPopup":"","showPppConfirm":"","condition":"","partNumber":pn,"partial":"false","communityName":"PUBLIC","theAction":""}
        result = session.post(url,params, headers=clickSecondHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        print ex
        return clickSendFirst(cmp,pn,retry = retry-1)    

    
def sendEmail(quoteNumber,amount,needBy,retry = 3):
    try:
        url = 'http://www.stockmarket.aero/StockMarket/RFQConfirmAction.do'
        params = {'refNumber': quoteNumber, 'quanitity': amount, 'needBy': needBy,'priority':'ROUTINE','headerNotes':'','isCapability':'T','capabilityType':'Exchange Capability','returnToResults':''}
        result = session.post(url,params, headers=sendEmailHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        print ex
        return sendEmail(quoteNumber,amount,needBy,retry = retry-1)
    
def selectByEmail(email):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT sc.SUPPLIER_ID FROM supplier_contact sc WHERE sc.EMAIL LIKE '%%%s%%' LIMIT 0,1"%(email)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def checkIfHasRecord(clientInquiryId,clientInquiryElementId,supplierId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT pae.id FROM part_and_email pae WHERE pae.CLIENT_INQUIRY_ID = '%s' AND pae.CLIENT_INQUIRY_ELEMENT_ID = '%s' AND pae.SUPPLIER_ID = '%s'"%(clientInquiryId,clientInquiryElementId,supplierId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def insertDB(clientInquiryId,clientInquiryElementId,supplierId,part,email):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into part_and_email(CLIENT_INQUIRY_ID,CLIENT_INQUIRY_ELEMENT_ID,SUPPLIER_ID,PART_NUMBER,EMAIL,SOURCE) values('%s', '%s', '%s', '%s', '%s', '%s')"%(clientInquiryId,clientInquiryElementId,supplierId,part,email,'stockmarket')
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    

def insertStockMarket(clientInquiryId,clientInquiryElementId,name,emailStatus):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into stock_market(CLIENT_INQUIRY_ID,CLIENT_INQUIRY_ELEMENT_ID,NAME,SEND_STATUS) values('%s', '%s', '%s', '%s')"%(clientInquiryId,clientInquiryElementId,name,emailStatus)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def insertStockMarketCrawlMessage(data):
    conn = MySQLdb.connect(host="127.0.0.1",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    dt = datetime.datetime.now().strftime("%Y-%m-%d")
    sql = "insert into stock_market_crawl_message(PART_NUMBER,AMOUNT,CONDITION_VALUE,SUPPLIER_CODE,CRAWL_DATE,STOCK_MARKET_CRAWL_ID,SUPPLIER_COMMISSION_SALE_ELEMENT_ID,IS_COPY) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')"%(data['partNumber'],data['amount'],data['condition'],data['supplierCode'],str(dt),data['foreignKey'],data['commissionElementId'],data['isCopy'])
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def insertMain(stockMarketCrawlId):
    conn = MySQLdb.connect(host="127.0.0.1", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    today = datetime.datetime.now().strftime("%Y-%m-%d")
    sql = "insert into stock_market_crawl(COMPLETE,EXCEL_CONPLETE,CRAWL_DATE,SUPPLIER_COMMISSION_SALE_ID) values('%s', '%s', '%s', '%s')" % ('0','0',str(today),str(stockMarketCrawlId))
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def getLastInsert(supplierCommissionId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT MAX(ID) FROM stock_market_crawl where SUPPLIER_COMMISSION_SALE_ID = '%s'"%(supplierCommissionId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def updateStatus(id):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "UPDATE stock_market_crawl SET COMPLETE = 1 WHERE ID = '%s'" % (id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()


def getElementList(stockMarketCrawlId):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT DISTINCT scse.ID,scse.PART_NUMBER FROM stock_market_crawl smc LEFT JOIN supplier_commission_for_stockmarket scs ON scs.ID = smc.SUPPLIER_COMMISSION_SALE_ID LEFT JOIN supplier_commission_for_stockmarket_element scse ON scs.ID = scse.SUPPLIER_COMMISSION_FOR_STOCKMARKET_ID WHERE smc.ID = '%s' AND scse.PART_NUMBER != ''"%(str(stockMarketCrawlId))
    cursor.execute(sql)
    l = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return l

def getElementDirt(stockMarketCrawlId):
    pl = getElementList(stockMarketCrawlId)
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

def getSupplier(id):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT " \
          "s.NAME_IN_STOCKMARKET AS supplier_name_in_stockmarket " \
        "FROM " \
        "supplier_commission_for_stockmarket_element scse " \
        "LEFT JOIN supplier_commission_for_stockmarket scs ON scs.ID = scse.SUPPLIER_COMMISSION_FOR_STOCKMARKET_ID " \
        "LEFT JOIN stock_market_supplier_map smsm ON smsm.AIR_TYPE_ID = scs.AIR_TYPE_ID " \
        "LEFT JOIN supplier s ON s.ID = smsm.SUPPLIER_ID " \
        "WHERE " \
            "scse.ID = '%s' AND smsm.ID IS NOT NULL AND s.NAME_IN_STOCKMARKET IS NOT NULL " \
        "UNION " \
            "SELECT " \
                "s.NAME_IN_STOCKMARKET AS supplier_name_in_stockmarket " \
            "FROM " \
                "supplier_commission_for_stockmarket_element scse " \
            "LEFT JOIN stock_market_add_supplier smas ON smas.SUPPLIER_COMMISSION_SALE_ELEMENT_ID = scse.ID " \
            "LEFT JOIN supplier s ON s.ID = smas.SUPPLIER_ID " \
            "WHERE " \
                "scse.ID = '%s'  AND smas.ID IS NOT NULL AND s.NAME_IN_STOCKMARKET IS NOT NULL"%(id,id)
    cursor.execute(sql)
    l = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return l

def getSupplierList(id):
    pl = getSupplier(id)
    supplierlist = []
    for l in pl:
        supplierlist.append(l[0])
    return supplierlist

#手动爬虫
def getElementListWithHand():
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT DISTINCT h.`Part Number` FROM harvest h"
    cursor.execute(sql)
    l = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return l

def getElementDirtWithHand():
    pl = getElementListWithHand()
    partlist = []
    for l in pl:
        data = {}
        if len(l) > 0:
            data["pn"] = str(l[0])
            data["id"] = str(2)
        if len(data) > 0:
            partlist.append(data)
    return partlist

def getSupplierWithHand():
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT s.NAME_IN_STOCKMARKET FROM stock_market_supplier_map smsm LEFT JOIN supplier s ON s.ID = smsm.SUPPLIER_ID WHERE smsm.AIR_TYPE_ID = 123 AND s.NAME_IN_STOCKMARKET IS NOT NULL"
    cursor.execute(sql)
    l = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return l

def checkExitRecord(id,part):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    #sql = "SELECT * FROM stock_market_crawl smc LEFT JOIN stock_market_crawl_message smcm ON smcm.STOCK_MARKET_CRAWL_ID = smc.ID WHERE smc.ID = '%s' AND smcm.PART_NUMBER = '%s'"%(id,part)
    #sql = "SELECT smcm.PART_NUMBER,smcm.AMOUNT,smcm.CONDITION_VALUE,smcm.SUPPLIER_CODE FROM stock_market_crawl smc LEFT JOIN stock_market_crawl_message smcm ON smcm.STOCK_MARKET_CRAWL_ID = smc.ID WHERE smc.ID = '%s' AND smcm.PART_NUMBER = '%s' AND smcm.SUPPLIER_COMMISSION_SALE_ELEMENT_ID = (SELECT DISTINCT smcm.SUPPLIER_COMMISSION_SALE_ELEMENT_ID FROM stock_market_crawl smc LEFT JOIN stock_market_crawl_message smcm ON smcm.STOCK_MARKET_CRAWL_ID = smc.ID WHERE smc.ID = '%s' AND smcm.PART_NUMBER = '%s' LIMIT 0,1)"%(id,part,id,part)
    sql="SELECT smcm.PART_NUMBER,smcm.AMOUNT,smcm.CONDITION_VALUE,smcm.SUPPLIER_CODE FROM stock_market_crawl_message smcm "\
            "WHERE smcm.PART_NUMBER = '%s' AND DATEDIFF(NOW(),smcm.UPDATE_TIMESTAMP) <= 6 "\
            "AND smcm.SUPPLIER_COMMISSION_SALE_ELEMENT_ID = ( "\
               " SELECT DISTINCT "\
                    "smcm.SUPPLIER_COMMISSION_SALE_ELEMENT_ID "\
                "FROM "\
                   " stock_market_crawl smc "\
                "LEFT JOIN stock_market_crawl_message smcm ON smcm.STOCK_MARKET_CRAWL_ID = smc.ID "\
                "WHERE smcm.PART_NUMBER = '%s' "\
              "ORDER BY smcm.UPDATE_TIMESTAMP DESC "\
                "LIMIT 0, "\
                "1 "\
            ") "\
            "AND smcm.STOCK_MARKET_CRAWL_ID = ( "\
                "SELECT DISTINCT "\
                    "smcm.STOCK_MARKET_CRAWL_ID "\
                "FROM "\
                    "stock_market_crawl smc "\
                "LEFT JOIN stock_market_crawl_message smcm ON smcm.STOCK_MARKET_CRAWL_ID = smc.ID "\
                "WHERE smcm.PART_NUMBER = '%s' "\
              "ORDER BY smcm.UPDATE_TIMESTAMP DESC "\
                "LIMIT 0, "\
                "1 "\
            ") "\
            "AND smcm.IS_COPY = 0"%(str(part),str(part),str(part))
    cursor.execute(sql)
    data = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return data

def getSupplierListWithHand():
    pl = getSupplierWithHand()
    supplierlist = []
    for l in pl:
        supplierlist.append(l[0])
    return supplierlist

def _getLogger():
    import logging
    import os
    import inspect

    logger = logging.getLogger('[StockMarket]')

    this_file = inspect.getfile(inspect.currentframe())
    dirpath = os.path.abspath(os.path.dirname(this_file))
    dirpath = dirpath + "\\log"
    handler = logging.FileHandler(os.path.join(dirpath, "StockMarket.log"))

    formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')
    handler.setFormatter(formatter)

    logger.addHandler(handler)
    logger.setLevel(logging.INFO)
    return logger

# quoteNumber = "1010417-A192929"
# amount = "1"
# now = datetime.datetime.now()
# delta = datetime.timedelta(days=3)
# n_days = now + delta
# needBy = n_days.strftime('%m/%d/%Y')
# default = crawlDefault()
# toLogin = toLogin()
# login = crawlLoginDo()
# afterLogin = afterLoginDo()
# afterLogin.find("purchaser@betterairgroup.com")
# search = searchPart("AE1502U02")
# search.find("Avtrade Ltd")
# getEachRow(search,"AE1502U02",quoteNumber,amount,needBy,"3","33")
# clickSendFirst()
# clickSendSecond()
# email = sendEmail(quoteNumber,amount,needBy)
# success = email.find("Your RFQ has been sent successfully")
# logout()

def doCrawl(partList,index,logger,id,retry=0):
    #partList = getElementDirt()
    #partList = getElementDirtWithHand()
    try:
        if index > 0:
            partList = partList[index:]
        for ind, part in  enumerate(partList):
            tup = checkExitRecord(id,part['pn'])
            if len(tup) == 0:
                logger.info("serach "+part['pn'])
                index = ind
                supplierMap = {}
                supplierArray = getSupplierList(part['id'])
                search = searchPart(part['pn'])
                getElement(search,supplierArray,supplierMap,part['pn'],id,part['id'])
                crawlNextPage(search,supplierArray,supplierMap,part['pn'],id,part['id'])
                foo = [3,4,5,6,7]
                time.sleep(choice(foo))
            else:
                logger.info(part['pn']+" had search in a week")
                for row in tup :
                    currentMessage = {}
                    currentMessage['partNumber'] = str(row[0])
                    currentMessage['amount'] = str(row[1])
                    currentMessage['condition'] = str(row[2])
                    currentMessage['supplierCode'] = MySQLdb.escape_string(str(row[3]))
                    currentMessage['foreignKey'] = str(id)
                    currentMessage['commissionElementId'] = part['id']
                    currentMessage['isCopy'] = 1
                    insertStockMarketCrawlMessage(currentMessage)
    except Exception, ex:
        if retry == 1:
            retry = 0
            index = index + 1
        else:
            retry = retry + 1
        logger.error(str(traceback.format_exc()))
        logger.error(str(Exception) + ":" + str(ex))
        doCrawl(partList,index,logger,id,retry)

#partList = getElementDirt()
#logger = _getLogger()
#index = 0
#insertMain()
#tup = getLastInsert()
#id = int(tup[0])
#default = crawlDefault()
#doCrawl(partList,index,logger)
#updateStatus(id)


# schedule.every(5).minutes.do(doCrawl)
# while True:
#     schedule.run_pending()
#     time.sleep(1)




    
