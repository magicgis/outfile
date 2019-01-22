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
"Authorization":"296880,021048B98,MLAQSBhs+OoiRRxVQDf98Nxy/i8sT5ixjiOSqjaxSDVpLRqvlCfEbpnwDgIvdcANNeAv2Y2xi+1Bg47kHJPCqL4WIszy331Yq2Vu1mdrykRf2bNtE8GjKTacO4bWuxkqEj/NIFAyOZ21esEh2eyOTJnODSNrWiE3DJTtsBx/ya5EH2SE3Y6UNY6CStCTHTJI",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Content-Type":"application/json",
"Host":"www.partsbase.com",
"Origin":"https://www.partsbase.com",
"Pragma":"no-cache",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36",
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

session = requests.session()

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
        checkBody = "{conditionCode: \"\", contactId: \"274712\", countryId: \"0\",countryName:\"\",currentItem:\""+part+"\",customerId:135879,descriptionFilter:\"\",employeeId:296880,isPremiumSearch:\"\",key:\"021048B98\",maxResultsNumber:200,numberOfPartsToSearch:\"1\",partDescriptions:[\"\"],  premSearchType:\"\",searchQuery:\""+part+"\",searchType:\"Parts\",showResultsInOneTab:null,sortOrder:\"0\"}"
        result = session.post(url,checkBody, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return searchPart(retry = retry-1)
    

def logout(retry = 3):
    try:
        url = 'https://www.partsbase.com/api/api/logout?employeeId=296880&ipAddress=113.119.58.121'
        params = {'employeeId':'296880','ipAddress':'113.119.58.121'}
        result = session.post(url, params,headers=logoutHeaders, verify=False,timeout=90)
        session.close()
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return logout(retry = retry-1)

def insertData(emails,part,clientInquiryId):
    for email in emails:
        if email:
            info = DetailInfo(clientInquiryId,str(part['id']),email,part['pn'])
            insertDB(info)
    
class DetailInfo(object):
    def __init__(self, CLIENT_INQUIRY_ID, CLIENT_INQUIRY_ELEMENT_ID, EAMIL, PART_NUMBER):
        self.CLIENT_INQUIRY_ID = CLIENT_INQUIRY_ID
        self.CLIENT_INQUIRY_ELEMENT_ID = CLIENT_INQUIRY_ELEMENT_ID
        self.EAMIL = EAMIL
        self.PART_NUMBER = PART_NUMBER
        
def insertDB(data):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into part_and_email(CLIENT_INQUIRY_ID,CLIENT_INQUIRY_ELEMENT_ID,EMAIL,PART_NUMBER) values('%s', '%s', '%s', '%s')"%(data.CLIENT_INQUIRY_ID,data.CLIENT_INQUIRY_ELEMENT_ID,data.EAMIL,data.PART_NUMBER)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

    
#default = crawlDefault()
#login = crawlLoginDo()
#search = searchPart("622-8701-004")
##result = search[searchResults]
#search.find("Unical Aviation,Inc")
#jsondata = json.loads(search)
#result = jsondata["searchResults"]
#eamils = list()
#for re in result:
#    if re["email"]:
#        eamils.append(re["email"])



    
