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
from base64 import encode
from platform import system
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

loginHeaders={
#"Accept":"*/*",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
#"Content-Length":0,
#"Content-Type":"application/x-www-form-urlencoded;charset=UTF-8",
"Host":"www.aviall.com",
#"Origin":"https://www.aviall.com",
"Upgrade-Insecure-Requests":"1",
"Referer":"https://www.aviall.com/aviallstorefront/login?targetUrl=https%3A%2F%2Fwww.aviall.com%2Faviallstorefront%2F",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36",
"X-Requested-With":"XMLHttpRequest"
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
        session = requests.session()
        url = 'https://www.aviall.com/aviallstorefront/login?targetUrl=https%3A%2F%2Fwww.aviall.com%2Faviallstorefront%2F'
        #params = {'targetUrl':'https://www.aviall.com/aviallstorefront/'}
        result = session.get(url, headers=HEADERS, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            raise Exception("can't not connect the internet")
        return crawlDefault(retry = retry-1)

def crawlLoginDo(retry = 3):
    try:
        url = 'https://www.aviall.com/aviallstorefront/j_spring_security_check'
        params = {'isepubs':'false',
                'j_username':'purchaser@betterairgroup.com',
                'j_password':'Reset123',
                'CSRFToken':'undefined'}
        loginHeaders["Content-Length"] = "103"
        loginHeaders["Accept"] = "*/*"
        loginHeaders["Origin"] = "https://www.aviall.com"
        loginHeaders["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8"
        result = session.post(url,data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            raise Exception("can't not connect the internet")
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
        url = 'https://www.aviall.com/aviallstorefront/'
        loginHeaders["Accept"] = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
        result = session.get(url, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            raise Exception("can't not connect the internet")
        return crawlAfterLoginDo(retry = retry-1)

def getPrice(part,retry = 3):
    try:
        url = 'https://www.aviall.com/aviallstorefront/search/results?q='+urllib2.quote(part)+'%3Arelevance&page=0&isOnlyProductIds=false&isOrderForm=false&skuIndex=0&isCreateOrderForm=false&searchResultType=&isCallPandA=true&showPage='
        loginHeaders["Referer"] = "https://www.aviall.com/aviallstorefront/search?text="+part
        loginHeaders["Accept"] = "*/*"
        loginHeaders["Pragma"] = "no-cache"
        params = {'q':part+':relevance',
                'page':'0',
                'isOnlyProductIds':'false',
                'skuIndex':'0',
                'isCreateOrderForm':'false',
                'searchResultType':'',
                'isCallPandA':'true',
                'showPage':''};
        result = session.get(url,data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            raise Exception("can't not connect the internet")
        return getPrice(part,retry = retry-1)

def beforeSearch(part,retry = 3):
    try:
        url = 'https://www.aviall.com/aviallstorefront/search/autocomplete/SearchBox?term='+part
        searchHeadersPublic["Accept"] = "application/json, text/javascript, */*; q=0.01"
        searchHeadersPublic["X-Requested-With"] = "XMLHttpRequest"
        searchHeadersPublic["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8"
        searchHeadersPublic["Referer"] = "https://www.aviall.com/aviallstorefront/"
        result = session.get(url, headers=searchHeadersPublic, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return beforeSearch(part,retry = retry-1)

def crawlStockMessage(productId,part,retry = 3):
    try:
        p = productId.replace("=","%3D")
        url = 'https://www.aviall.com/aviallstorefront//p/currentProduct.json?productCode='+p+"&_=1504509717591"
        stockMessageHeaders["Referer"] = "https://www.aviall.com/aviallstorefront/p/"+part
        params = {'productCode':productId,
                '_':'1504509717591'};
        result = session.get(url, data=params, headers=stockMessageHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlStockMessage(productId,part,retry = retry-1)

def crawlSearch(part,retry = 3):
    try:
        url = 'https://www.aviall.com/aviallstorefront/search?text='+urllib2.quote(part)
        searchHeadersPublic["Referer"] = "https://www.aviall.com/aviallstorefront/"
        result = session.get(url, headers=searchHeadersPublic, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSearch(part,retry = retry-1)


def crawlSelectAfterSearch(choose,part,retry = 3):
    try:
        url = 'https://www.aviall.com'+choose
        searchHeaders["Referer"] = "https://www.aviall.com/aviallstorefront/search?text="+part
        result = session.get(url, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSelectAfterSearch(choose,part,retry = retry-1)

def chooseSelect(content,description,ifPrice):
    soup = BeautifulSoup(content)
    record = soup.findAll('a',{'class':'productMainLink baselink level3 basefont blue hoverBrightBlue'})
    if len(record)==0:
        raise Exception('Has No Record')
    #if description == '':
        #select = record[0].findAll('a')
        #return record[0].attrs['href']
        #return "/aviallstorefront/p/"+str(ifPrice[0].attrs['id'][13:])
    #else:    
    divs = soup.findAll('div',{'class':'details img_width_24'})
    ohc = False;
    for div in divs:
        divTag = div.findAll('div',{'class':"basefont breakWord level5"})
        divTag[0].find("OHC")
        if len(divTag) > 0:
            desc = divTag[0].text
            if desc.find("OHC") > 0 or desc.find("ohc") > 0:
                ohc = True;
                continue
        regexString = ".*"+description+".*"
        regexp = re.compile(regexString)  
        match = div.findAll('a',{'title':regexp})
        if len(match)>0:
            return match[0].attrs['href']
        #select = record[0].find('a')
        productSplit = record[0].attrs['href'].split("/")
        productId = productSplit[len(productSplit)-1]
        for product in ifPrice:
            if productId == str(product.attrs['id'][13:]):   
                return record[0].attrs['href']
    if ohc == False:
        return "/aviallstorefront/p/"+str(ifPrice[0].attrs['id'][13:])
    else:
        return ""

def checkDescriptionAfterSearch(content):
    soup = BeautifulSoup(content)
    record = soup.findAll('div',{'class':'basefont breakWord level5'})
    if len(record)==0:
        raise Exception('Has No Record')
    match = soup.findAll('a',{'text':'CHARGER:'})
    if len(match)>0:
        return match[0].attrs['href']
    select = record.findAll('a')
    return select[0].attrs['href']


def getNoResultMessage(pn,id,elementId,content):
    soup = BeautifulSoup(content)
    regexString = ".*basefont babyBlue level1 title1.*"
    regexp = re.compile(regexString)  
    match = soup.findAll('div',{'class':regexp})
    if len(match):  
        remark = match[0].string.replace("\'","").strip()#.decode('unicode-escape').replace("\"","").replace("\\r","").replace("\\n","").replace("\\t","")
        div = match[0].findNext('div')
        if div :
            ps = div.findAll('p')
            if len(ps) >= 1:
                te = ps[1].text.strip()
                remark = remark + "," + te
        insertNoResultDB(pn,id,elementId,remark)
        if remark.find("Sorry") > -1:
            return True
        else:
            return False
        
def getWarnMessage(pn,id,elementId,text):
    soup = BeautifulSoup(text)
    regexString = "basefont light level5"+".*"
    regexp = re.compile(regexString)
    divs = soup.findAll('div',{'class':regexp})
    if len(divs) > 0:
        warn = divs[0].text.strip()
        if warn:
          insertNoResultDB(pn,id,elementId,warn)  
 
def ifHasPrice(content):
    #print content
    soup = BeautifulSoup(content)
    #print soup
    regexString = "addToCartForm.*"
    regexp = re.compile(regexString)
    record = soup.findAll('form',{'id':regexp})
    if len(record)==0:
        return "0";
    else :
        #return record[0].attrs['id'];
        return record;

def getLeadTime(content,dataInfo):
    soup = BeautifulSoup(content)
    leadTime = content.find('lead time')
    stock = content.find('on-hand')
    if stock > 0:
        dataInfo['lead_time'] = 0
        availableStock = soup.findAll('a',{'text':'Available Stock'})
        productId = availableStock[0].attrs['data-product_id']
        stockTable = crawlStockMessage(productId)
        stockMessage = getStockMessage(stockTable)
        dataInfo['stockMessage'] = stockMessage
    else:
        regexString = ".*"+"lead"+".*"
        regexp = re.compile(regexString)
        tds = soup.findAll('td',{'class':'textRight verticalTop'})
        for td in tds:
            div = td.findAll('div',{'class':'basefont level5'})
            if len(div) > 0:
                dataInfo.lead_time = div[0].text
        

def getStockMessage(content):
    soup = BeautifulSoup(content)
    table = soup.findAll('div')
    if len(table)==0:
        raise Exception('Has No table')
    tbodys = table[0].findAll("tbody")
    if len(tbodys)==0:
        raise Exception('Has No tbodys')
    trs = tbodys[0].findAll("tr")
    stockMessage = '';
    for tr in trs:
        tds = tr.findAll('td')
        location = getRowValue(tds[0])
        amount = getRowValue(tds[1]).replace(",","")
        if stockMessage == '':
            stockMessage = location +" "+ amount
        else:
            stockMessage = stockMessage + ',' + location +" "+ amount
    return stockMessage



def getRowValue(td):
    return td.text.strip()


class DetailInfo(object):
    def __init__(self, faa_approval_code,pn,description,price,lead_time,currency,stockMessage,clientInquiryElementId,moq,ifDanger,unit):
        self.faa_approval_code = faa_approval_code
        self.pn = pn
        self.price = price
        self.lead_time = lead_time
        self.currency = currency
        self.stockMessage = stockMessage
        self.description = description
        self.clientInquiryElementId = clientInquiryElementId
        self.moq = moq
        self.ifDanger = ifDanger
        self.unit = unit

def getData(content,infoList,clientInquiryElementId,text,choose):
    soup = BeautifulSoup(content)
    regexString = ".*"+"fullWidth separate spacing15 basefont"+".*"
    regexp = re.compile(regexString)
    belowTable = soup.findAll('table',{'class':regexp})
    if len(belowTable)==0:
        raise Exception('No table fond for detail belowTable')
    tbodys = belowTable[0].findAll('tbody')
    trs = tbodys[0].findAll('tr')
    faa_approval_code = ''
    Hazmat_Code = ''
    Hazmat = ''
    ifDanger = 0
    #for tr in trs:
    tds = trs[1].findAll('td')
    thirdTds = trs[2].findAll('td')
    '''danger'''
    dangerFirstDiv = tds[2].find('div')
    dangerDivs = dangerFirstDiv.findAll('div')
    if dangerDivs[1].string.strip() is not None:
        Hazmat_Code = dangerDivs[1].string.strip().encode('utf-8')
    dangerFirstDiv = thirdTds[4].find('div')
    dangerDivs = dangerFirstDiv.findAll('div')
    if dangerDivs[1].string.strip() is not None:
        Hazmat = dangerDivs[1].string.strip().encode('utf-8')
    if Hazmat == 'Y' or Hazmat_Code != '':
        ifDanger = 1
    #for td in tds:
    firstDiv = tds[4].find('div')
    divs = firstDiv.findAll('div')
    #for div in divs:
    if divs[1].string is not None:
        faa_approval_code = divs[1].string.encode('utf-8')
    tableDiv = soup.findAll('div',{'class':'border bottomBD textBD textRight'})
    #print soup
    headTable = tableDiv[0].findAll('table',{'class':'fullWidth'})
    if len(headTable)==0:
        raise Exception('No table fond for detail headTable')
    trs = headTable[0].findAll('tr')
    tds = trs[0].findAll('td')
    divs1 = tds[0].findAll('div')
    strong = tds[0].findAll('strong')
    h1 = tds[0].findAll('h1')
    part = ''
    if len(strong) > 0:
        part = strong[0].text.strip().split('=')[0].strip()
    else:
        part = divs1[0].text.strip().split(':')[1].strip()    
    
    description = h1[0].text.strip()
    currency = '';
    price = '';
    if len(tds) >=2:
        regexString = ".*"+"basefont level2 textLeft"+".*"
        regexp = re.compile(regexString)
        divs2 = tds[1].findAll('div',{'class':regexp})
#        unitPrice = ''
#        if not divs2:
#            regexString = "basefont level3"
#            regexp = re.compile(regexString)
#            divs2 = tds[1].findAll('div',{'class':regexp})
        unit = 'EA'
        unitPrice = divs2[0].text.strip().split()
        currency = unitPrice[0]
        price = unitPrice[1][1:].replace(",","")
        if len(unitPrice) > 2:
            unit = unitPrice[2].strip()
        moq = '';
        moqTopDivs = soup.findAll('div',{'class':'inlineblock verticalTop'})
        if len(moqTopDivs) > 0:
            for moqTopDiv in moqTopDivs:
                moqDiv = moqTopDiv.findAll('div',{'class':'basefont level5'})
                if len(moqDiv) > 0:
                    moq = moqDiv[0].text
        lead_time = ''
        stockMessage = ''
        #leadTime = content.find('lead time')
        stock = content.find('on-hand')
        if stock > 0:
            lead_time = 0
            availableStock = soup.findAll('a',{'name':'invertoryReadyToShip'})
            productId = availableStock[0].attrs['data-product_id']
            stockTable = crawlStockMessage(productId,part)
            stockMessage = getStockMessage(stockTable)
        else :
            chooseSplit = choose.split("/")
            productId = chooseSplit[len(chooseSplit)-1]
            soup = BeautifulSoup(text)
            a = soup.find("a",{"data-product_id":productId})
            lead_time = a.findNext("div").text.strip()
    #        regexString = ".*"+"lead"+".*"
    #        regexp = re.compile(regexString)
    #        tds = soup.findAll('td',{'class':'textRight verticalTop'})
    #        for td in tds:
    #            div = td.findAll('div',{'class':'basefont level5'})
    #            if len(div) > 0:
    #                lead_time = div[0].text
        if float(price) > 0:
            dataInfo = DetailInfo(faa_approval_code,part,description,price,lead_time,currency,stockMessage,clientInquiryElementId,moq,ifDanger,unit)
            #getLeadTime(content,dataInfo)
            #print dataInfo
            infoList.append(dataInfo)
        else:
            dataInfo = DetailInfo(faa_approval_code,part,description,0,lead_time,currency,stockMessage,clientInquiryElementId,moq,ifDanger,unit)
            infoList.append(dataInfo)
        file5 = 'D:\\CrawlBackup\\aviall\\do_record_'+str(clientInquiryElementId)+"_"+part+''+'.html'
        try:
            with open(file5, 'w') as file_5:
                file_5.write(content.encode("utf-8"))
        except Exception,e1:
            print e1
                
def getTdValue(td):
    return td.text.strip()

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

def insertDB(data,aviallQuoteId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into aviall_quote_element(part_number, description, unit_price,currency,certification,lead_time,aviall_quote_id,client_inquiry_element_id,stock_message,moq,IF_DANGER,UNIT) values('%s', '%s', '%s', '%s', '%s', '%s', '%d','%s', '%s', '%s', '%s', '%s')"%(data.pn, data.description, data.price,data.currency,data.faa_approval_code,data.lead_time,aviallQuoteId,data.clientInquiryElementId,data.stockMessage,data.moq,data.ifDanger,data.unit)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def insertNoResultDB(partNumber,aviallQuoteId,clientInquiryElementId,remark):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into aviall_quote_element(part_number, aviall_quote_id,client_inquiry_element_id,remark) values('%s', '%s', '%s', '%s')"%(partNumber,aviallQuoteId,clientInquiryElementId,remark)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def addAviallQuote(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into aviall_quote(client_inquiry_id) values('%s')"%(clientInquiryId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    print 'create aviall_quote complete!'
    
def selectByClientInquiryId(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "select max(id) from aviall_quote where client_inquiry_id = '%s'"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def getSearchCountInAWeek(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT COUNT(*) FROM aviall_quote_element sqe WHERE DATEDIFF(NOW(),sqe.UPDATE_DATETIME) <= 7 AND sqe.PART_NUMBER = '%s' ORDER BY sqe.ID DESC"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def updateStatus(id):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "update aviall_quote set complete = 1 where client_inquiry_id = '%s'"%(id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

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
            "sqe.IF_DANGER, "\
            "sqe.LEAD_TIME, "\
            "sqe.MOQ, "\
            "sqe.PART_NUMBER, "\
            "sqe.REMARK, "\
            "sqe.STOCK_MESSAGE, "\
            "sqe.UNIT_PRICE, "\
            "sqe.UNIT "\
        "FROM "\
            "aviall_quote_element sqe "\
        "WHERE "\
            "DATEDIFF(NOW(), sqe.UPDATE_DATETIME) <= 7 "\
        "AND sqe.PART_NUMBER = '%s' "\
        "AND sqe.CLIENT_INQUIRY_ELEMENT_ID = ( "\
            "SELECT "\
                "aqe.CLIENT_INQUIRY_ELEMENT_ID "\
            "FROM "\
                "aviall_quote_element aqe "\
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
    sql = "insert into aviall_quote_element(part_number, description, unit_price,currency,certification,lead_time,aviall_quote_id,client_inquiry_element_id,stock_message,moq,IF_DANGER,UNIT,REMARK) values('%s', '%s', '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s', '%s', '%s', '%s')"%(data['PART_NUMBER'], data['DESCRIPTION'], data['UNIT_PRICE'],data['CURRENCY'],data['CERTIFICATION'],data['LEAD_TIME'],data['crawlId'],data['elementId'],data['STOCK_MESSAGE'],data['MOQ'],data['IF_DANGER'],data['UNIT'],data['REMARK'])
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
                currentMessage['IF_DANGER'] = str(row[3])
            else:
                currentMessage['IF_DANGER'] = ''
            if str(row[4]) != 'None':
                currentMessage['LEAD_TIME'] = str(row[4])
            else:
                currentMessage['LEAD_TIME'] = ''
            if str(row[5]) != 'None':
                currentMessage['MOQ'] = str(row[5])
            else:
                currentMessage['MOQ'] = ''
            if str(row[6]) != 'None':
                currentMessage['PART_NUMBER'] = str(row[6])
            else:
                currentMessage['PART_NUMBER'] = ''
            if str(row[7]) != 'None':
                currentMessage['REMARK'] = str(row[7])
            else:
                currentMessage['REMARK'] = ''
            if str(row[8]) != 'None':
                currentMessage['STOCK_MESSAGE'] = str(row[8])
            else:
                currentMessage['STOCK_MESSAGE'] = ''
            if str(row[9]) != 'None':
                currentMessage['UNIT_PRICE'] = str(row[9])
            else:
                currentMessage['UNIT_PRICE'] = ''
            if str(row[10]) != 'None':
                currentMessage['UNIT'] = str(row[10])
            else:
                currentMessage['UNIT'] = ''
            currentMessage['crawlId'] = crawlId
            currentMessage['elementId'] = elementId
            insertDBForCopy(currentMessage)
            
    
#partList = ['1/1/60']
#for part in partList:
#    session = requests.session()
#    #part = "72303102-6=77"  
#    infoList = list();
#    default = crawlDefault()
#    select = default.find('Login | Aviall')
#    main = crawlLoginDo()
#    check = main.find('My Account')
#    result = crawlAfterLoginDo()
#    check = result.find('Go with Aviall')
#    price = getPrice(part)
#    text = price.split("},")[1].decode('unicode-escape').replace("\\","") 
#    #.split("\":")[1]
#    print text
##    logout()
##    session.close()
#    ifPrice = ifHasPrice(text)
#    if ifPrice != "0":
#        session = requests.session()
#        default = crawlDefault()
#        main = crawlLoginDo()
#        result = crawlAfterLoginDo()
#        clientInquiryElementId = "1"
#        before = beforeSearch(part)
#        search = crawlSearch(part)
#        #search = crawlSearch(part)
#        #price = getPrice(part)
#        #price1 = price.find('each')
#        #if price1 > 0:
#        #    #search1 = search.find('CHARGER: MAGNET,');
#        #    choose = chooseSelect(search,"CHARGER")
#        #    select = crawlSelectAfterSearch(choose,part)
#        #    select1 = select.find('120 day lead time')
#        #    select = getData(select)
#        choose = chooseSelect(search,"",ifPrice)
#        select = crawlSelectAfterSearch(choose,part)
#        select1 = select.find('120 day lead time')
#        dataInfo = getData(select,infoList,clientInquiryElementId,text,choose)
#        aviallQuoteId = 1
#        for info in infoList:
#            insertDB(info,aviallQuoteId)
#        logout()


    
