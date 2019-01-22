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
LOGGER_NAME_CRAWL = 'satair'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
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
    "User-Agent": AGENT,
    "Host": "www.partsbase.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, br",
    "Accept-Language": "zh-CN,zh;q=0.8",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1"
}

loginHeaders={
"Accept":"application/json, text/plain, */*",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Content-Length":"79",
"Content-Type":"application/json",
"Host":"www.partsbase.com",
"Origin":"https://www.partsbase.com",
"Pragma":"no-cache",
"Referer":"https://www.partsbase.com/landing/login?ImpMSContactID=0",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36",
"X_ALT_REFERER":""
}

searchHeaders={
"Accept":"application/json, text/plain, */*",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Authorization":"296880,021048B98,MLAQSBhs+OoiRRxVQDf98Nxy/i8sT5ixjiOSqjaxSDVpLRqvlCfEbpnwDgIvdcANNeAv2Y2xi+1Bg47kHJPCqL4WIszy331Yq2Vu1mdrykRf2bNtE8GjKTacO4bWuxkqeaceRYnNqaIPue5qgyTwrDGW/rTbKXrfjlTqd4QkTzagMnstA2Iyd1V2b3sjs/YgApsmFOaOvayIjoR4M3cCHsgJWp1kbSbwYLJnAeQ+DF8=",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Content-Type":"application/json",
"Host":"www.partsbase.com",
"Origin":"https://www.partsbase.com",
"Pragma":"no-cache",
"Content-Length":"470",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36",
"X_ALT_REFERER":""
}

logoutHeaders={
"Accept":"application/json, text/plain, */*",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8",
"Authorization":"296880,021048B98,MLAQSBhs+OoiRRxVQDf98Nxy/i8sT5ixjiOSqjaxSDVpLRqvlCfEbpnwDgIvdcANNeAv2Y2xi+1Bg47kHJPCqL4WIszy331Yq2Vu1mdrykRf2bNtE8GjKTacO4bWuxkqEj/NIFAyOZ21esEh2eyOTJnODSNrWiE3DJTtsBx/ya5EH2SE3Y6UNY6CStCTHTJI",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"www.partsbase.com",
"Pragma":"no-cache",
"Content-Length":"0",
"Referer":"https://www.partsbase.com/landing/logout",
"X_ALT_REFERER":"",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
}

formData={
"customerId":"135879",
"showResultsInOneTab":"null",
"countryId":"0",
"countryName":"",
"searchQuery":"D31865-111",
"sortOrder":"0",
"conditionCode":"",
"maxResultsNumber":"200",
"descriptionFilter":"",
"searchType":"Parts",
"employeeId":"296880",
"key":"021048B98",
"numberOfPartsToSearch":"1",
"currentItem":"D31865-111",
"isPremiumSearch":"",
"premSearchType":"",
"contactId":"274712",
"partDescriptions":"[""]"
}

session = requests.session()

def encode_multipart_formdata(dataDict):
    dataList = list()
    for key in dataDict:
        value = dataDict[key]
        dataList.append(key+":"+value)
    return ','.join(dataList)

def crawlDefault(retry = 3):
    try:
        url = 'https://www.partsbase.com/landing/login'
        #params = {'targetUrl':'https://www.aviall.com/aviallstorefront/'}
        result = session.get(url, headers=defaultHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlDefault(retry = retry-1)

def crawlLoginDo(retry = 3):
    try:
        url = 'https://www.partsbase.com/api/api/login'
        checkBody = "{username: \"kriskris\", password: \"aviation\", impMsContactId: \"0\"}"
        result = session.post(url,checkBody, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDo(retry = retry-1)
    
def searchPart(part,retry = 3):
    try:
        url = 'https://www.partsbase.com/api/api/pbSearch/searchResults'
        checkBody = "{conditionCode: \"\", contactId: \"274712\", countryId: \"0\",countryName:\"\",currentItem:\""+part+"\",customerId:135879,descriptionFilter:\"\",employeeId:296880,isPremiumSearch:\"\",key:\"021048B98\",maxResultsNumber:200,numberOfPartsToSearch:\"1\",partDescriptions:[\"\"],  0:\"\",premSearchType:\"\",searchQuery:\""+part+"\",searchType:\"Parts\",showResultsInOneTab:null,sortOrder:\"0\"}"
        #checkBody = encode_multipart_formdata(formData)
        searchHeaders["Content-Length"] = "470"
        result = session.post(url,checkBody, headers=searchHeaders,timeout=90)
        return result.text
#        request = urllib2.Request(url, checkBody, searchHeaders)
#        response = opener.open(request)
#        return response.read()
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return searchPart(part,retry = retry-1)
    
def logoutInTest(retry = 3):
    try:
        url = 'https://www.partsbase.com/api/api/logout?employeeId=296880&ipAddress=223.73.197.141'
        params = {'employeeId':'296880','ipAddress':'223.73.197.141'}
        result = session.post(url, params,headers=logoutHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return logout(retry = retry-1)
    

def logout(retry = 3):
    try:
        url = 'https://www.partsbase.com/api/api/logout?employeeId=296880&ipAddress=113.119.58.121'
        params = {'employeeId':'296880','ipAddress':'113.119.58.121'}
        result = session.post(url, params,headers=logoutHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return logout(retry = retry-1)

def insertData(emails,clientInquiryId,part):
    amount = 1
    for email in emails:
        if email:
            emailIndex = email.find('@')
            endWith = email[emailIndex:]
            print endWith
            tup = selectByEmail(endWith)
            supplierId = " "
            if tup:
                supplierId = int(tup[0])
                info = DetailInfo(clientInquiryId,part['id'],email,part['pn'],str(supplierId))
                insertDB(info)
            else:
                info = DetailInfo(clientInquiryId,part['id'],email,part['pn'],str(supplierId))
                insertDBWithOutSupplier(info)
            if amount == 10 :
                break
            amount = amount + 1
    
class DetailInfo(object):
    def __init__(self, CLIENT_INQUIRY_ID, CLIENT_INQUIRY_ELEMENT_ID, EAMIL, PART_NUMBER,SUPPLIER_ID):
        self.CLIENT_INQUIRY_ID = CLIENT_INQUIRY_ID
        self.CLIENT_INQUIRY_ELEMENT_ID = CLIENT_INQUIRY_ELEMENT_ID
        self.EAMIL = EAMIL
        self.PART_NUMBER = PART_NUMBER
        self.SUPPLIER_ID = SUPPLIER_ID
        
def insertDB(data):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into part_and_email(CLIENT_INQUIRY_ID,CLIENT_INQUIRY_ELEMENT_ID,EMAIL,PART_NUMBER,SUPPLIER_ID,SOURCE) values('%s', '%s', '%s', '%s', '%s', '%s')"%(data.CLIENT_INQUIRY_ID,data.CLIENT_INQUIRY_ELEMENT_ID,data.EAMIL,data.PART_NUMBER,data.SUPPLIER_ID,'partbase')
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def insertDBWithOutSupplier(data):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into part_and_email(CLIENT_INQUIRY_ID,CLIENT_INQUIRY_ELEMENT_ID,EMAIL,PART_NUMBER,SOURCE) values('%s', '%s', '%s', '%s', '%s')"%(data.CLIENT_INQUIRY_ID,data.CLIENT_INQUIRY_ELEMENT_ID,data.EAMIL,data.PART_NUMBER,'partbase')
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
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

    

#default = crawlDefault()
#login = crawlLoginDo()
#success = login.find("We are sorry")
#if success > 0:
#    logoutInTest()
#    login = crawlLoginDo()
#search = searchPart('622-8701-004')
#jsondata = json.loads(search)
#result = jsondata["searchResults"]
#emails = list()
#for re in result:
#    if re["condCode"]:
#        if re["condCode"]=='OH' or re["condCode"]=='SV':
#            today = re["lastUpdateDate"]
#            d1 = datetime.datetime.strptime(today, '%m/%d/%Y')
#            now = datetime.datetime.now()
#            delta = now - d1
#            if delta.days<=30:
#                if re["email"]:
#                    emails.append(re["email"])
#insertData(emails,'12321')




    
