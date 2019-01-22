'''
Created on 21 July 2018

@author: tanoy
'''
import urllib2
import cookielib
from poster.encode import multipart_encode, MultipartParam
from poster.streaminghttp import register_openers
from bs4 import BeautifulSoup
import traceback
import MySQLdb
import math
import ssl
import requests
import re
import time
from random import choice

LOGGER_NAME_CRAWL = 'satair'
opener = register_openers()
AGENT = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36'
HEADERS = {
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate, br",
    "Accept-Language":"zh-CN,zh;q=0.9",
    "Cache-Control":"no-cache",
    "Connection":"keep-alive",
    "Host":"spares.satair.com",
    "Pragma":"no-cache",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent":AGENT
}

boundry = '----WebKitFormBoundarySxSIwKAigNZPmrMU'
class RedirctHandler(urllib2.HTTPRedirectHandler):
    """docstring for RedirctHandler"""

    def http_error_301(self, req, fp, code, msg, headers):
        print code, msg, headers

    def http_error_302(self, req, fp, code, msg, headers):
        print code, msg, headers


cookie = cookielib.CookieJar()
handler = urllib2.HTTPCookieProcessor(cookie)
opener.add_handler(handler)
# opener.add_handler(RedirctHandler)

defaultHeaders = {
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate",
    "Accept-Language":"zh-CN,zh;q=0.9",
    "Cache-Control":"no-cache",
    "Connection":"keep-alive",
    "Host":"store.dasi.com",
    "Pragma":"no-cache",
    "Referer":"http://store.dasi.com/search.aspx",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent": AGENT
}

searchHeaders = {
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate",
    "Accept-Language":"zh-CN,zh;q=0.9",
    "Cache-Control":"no-cache",
    "Connection":"keep-alive",
    "Content-Type":"multipart/form-data; boundary=----WebKitFormBoundarySxSIwKAigNZPmrMU",
    "Host":"store.dasi.com",
    "Origin":"http://store.dasi.com",
    "Pragma":"no-cache",
    "Referer":"http://store.dasi.com/search.aspx",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent": AGENT
} 

serachPara = {
    "ctl00_ctl00_cph1_cph1_sm1_HiddenField":";;AjaxControlToolkit, Version=4.1.40412.0, Culture=neutral, PublicKeyToken=28f01b0e84b6d53e:en-GB:acfc7575-cdee-46af-964f-5d85d9cdcf92:effe2a26:7dd386e9:475a4ef5:1d3ed089:5546a2b:497ef277:a43b07eb:751cdd15:dfad98a5:3cf12cf1"
}

session = requests.session()


def crawlDefault(retry=3):
    try:
        url = 'http://store.dasi.com/search.aspx'
        result = session.get(url, headers=defaultHeaders, verify=False, timeout=120)
        return result.text
    except Exception, ex:
        if retry < 1:
            return "nothing"
        return crawlDefault(retry=retry - 1)


def crawlSearchDo(para,retry=3):
    try:
        url = 'http://store.dasi.com/search.aspx'
        checkBody = encode_multipart_formdata(boundry,para)
        searchHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url, checkBody, headers=searchHeaders, verify=False, timeout=120)
        return result.text
    except Exception, ex:
        if retry < 1:
            return "nothing"
        return crawlSearchDo(para,retry=retry - 1)

def searchDo(part,retry=3):
    try:
        url = 'https://spares.satair.com/portal/stocks/status/stocks.jsp.port'
        params = {
            'menu-id':'info',
            'mode':'portal',
            'REQUEST':'INQUIRY_SINGLE',
            'ACTION':'INQUIRY',
            'clearBasketMessages':'true',
            'E_PNR':str(part),
            'E_MFR':'',
            'INTERCHANGEABLES':'TRUE',
            'SUPPLIERS':'FALSE',
            'SUBMIT.x':'0',
            'SUBMIT.y':'0',
            'SUBMIT':'Submit'
        }
        loginHeaders["Content-Length"] = str(len(params))
        result = session.post(url, data=params, headers=loginHeaders, verify=False, timeout=120)
        return result.text
    except Exception, ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDo(retry=retry - 1)

def encode_multipart_formdata(boundry, dataDict):
    dataList = list()
    for key in dataDict:
        value = dataDict[key]
        dataList.append('--'+boundry)
        dataList.append( 'Content-Disposition: form-data; name="%s"' % key )
        dataList.append( '' )
        dataList.append(value)
    dataList.append('--'+boundry+'--')  
    return '\r\n'.join(dataList)

def getPara(content,part):
    soup = BeautifulSoup(content)
    viewstate = soup.findAll('input',{'id':'__VIEWSTATE'})
    eventvalidation = soup.findAll('input',{'id':'__EVENTVALIDATION'})
    eventtarget = soup.findAll('input',{'id':'__EVENTTARGET'})
    eventargument = soup.findAll('input',{'id':'__EVENTARGUMENT'})
    viewstategenerator = soup.findAll('input',{'id':'__VIEWSTATEGENERATOR'})
    multisearch = soup.findAll('input',{'name':'ctl00$ctl00$cph1$cph1$ctrlSearch$btnMultiSearch'})
    txtSearchTerm = soup.findAll('input',{'name':'ctl00$ctl00$cph1$cph1$ctrlSearch$txtSearchTerm'})
    hdnSearchType = soup.findAll('input',{'name':'ctl00$ctl00$cph1$cph1$ctrlSearch$hdnSearchType'})
    ddlCondition = soup.findAll('select',{'name':'ctl00$ctl00$cph1$cph1$ctrlSearch$ddlCondition'})
    hdnUID = soup.findAll('input',{'name':'ctl00$ctl00$cph1$cph1$hdnUID'})
    para = {}
    para['ctl00$ctl00$cph1$cph1$ctrlSearch$txtMultiSearchTerm']= part
    para['ctl00_ctl00_cph1_cph1_sm1_HiddenField'] = ';;AjaxControlToolkit, Version=4.1.40412.0, Culture=neutral, PublicKeyToken=28f01b0e84b6d53e:en-GB:acfc7575-cdee-46af-964f-5d85d9cdcf92:effe2a26:7dd386e9:475a4ef5:1d3ed089:5546a2b:497ef277:a43b07eb:751cdd15:dfad98a5:3cf12cf1'
    para['ddlConditionValue'] = '0'
    if viewstate != None and len(viewstate) > 0:
        viewstateValue = viewstate[0].attrs['value'].strip()
        para['__VIEWSTATE'] = viewstateValue
    else:
         para['__VIEWSTATE'] = ''
        
    if eventvalidation != None and len(eventvalidation) > 0:
        eventvalidationValue = eventvalidation[0].attrs['value'].strip()
        para['__EVENTVALIDATION'] = eventvalidationValue
    else:
         para['__EVENTVALIDATION'] = ''
        
    if eventtarget != None and len(eventtarget) > 0:
        eventtargetValue = eventtarget[0].attrs['value'].strip()
        para['__EVENTTARGET'] = eventtargetValue
    else:
         para['__EVENTTARGET'] = ''
        
    if eventargument != None and len(eventargument) > 0:
        eventargumentValue = eventargument[0].attrs['value'].strip()
        para['__EVENTARGUMENT'] = eventargumentValue
    else:
         para['__EVENTARGUMENT'] = ''
        
    if viewstategenerator != None and len(viewstategenerator) > 0:
        viewstategeneratorValue = viewstategenerator[0].attrs['value'].strip()
        para['__VIEWSTATEGENERATOR'] = viewstategeneratorValue
    else:
         para['__VIEWSTATEGENERATOR'] = ''
        
    if multisearch != None and len(multisearch) > 0:
        multisearchValue = multisearch[0].attrs['value'].strip()
        para['ctl00$ctl00$cph1$cph1$ctrlSearch$btnMultiSearch'] = multisearchValue
    else:
         para['ctl00$ctl00$cph1$cph1$ctrlSearch$btnMultiSearch'] = ''
        
    if txtSearchTerm != None and len(txtSearchTerm) > 0:
        txtSearchTermValue = txtSearchTerm[0].attrs['value'].strip()
        para['ctl00$ctl00$cph1$cph1$ctrlSearch$txtSearchTerm'] = txtSearchTermValue
    else:
         para['ctl00$ctl00$cph1$cph1$ctrlSearch$txtSearchTerm'] = ''
        
    if hdnSearchType != None and len(hdnSearchType) > 0:
        hdnSearchTypeValue = hdnSearchType[0].attrs['value'].strip()
        para['ctl00$ctl00$cph1$cph1$ctrlSearch$hdnSearchType'] = hdnSearchTypeValue
    else:
         para['ctl00$ctl00$cph1$cph1$ctrlSearch$hdnSearchType'] = ''
        
#    if ddlCondition != None and len(ddlCondition) > 0:
#        ddlConditionValue = ddlCondition[0].attrs['value'].strip()
#        para['ddlConditionValue'] = ddlConditionValue
#    else:
#         para['ddlConditionValue'] = ''
        
    if hdnUID != None and len(hdnUID) > 0:
        hdnUIDValue = hdnUID[0].attrs['value'].strip()
        para['ctl00$ctl00$cph1$cph1$hdnUID'] = hdnUIDValue
    else:
         para['ctl00$ctl00$cph1$cph1$hdnUID'] = ''
    return para


def insertDasiRecord(data):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "insert into dasi_message(DASI_ID,PART_NUMBER,STORAGE_AMOUNT,CLIENT_INQUIRY_ELEMENT_ID) values('%s', '%s', '%s', '%s')" % (data['dasiId'],data['partNumber'],data['storageAmount'],data['elementId'])
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def getRecord(content,part,dasiId,elementId):
    soup = BeautifulSoup(content)
    tables = soup.findAll('table',{'id':'ctl00_ctl00_cph1_cph1_ctrlSearch_ctrlProductsInGrid_gvProducts_ctl03_gvVariants'})#ctl00_ctl00_cph1_cph1_ctrlSearch_ctrlProductsInGrid_gvProducts
    if len(tables) > 0:
#        tbodys = tables[0].findAll('tbody')
#        if len(tbodys) > 0:
            trs = tables[0].findAll('tr')
            if len(trs) > 1:
                for index,tr in enumerate(trs):
                    if index > 0:
                        tds = tr.findAll('td')
                        if len(tds) > 8:
                            spans = tds[7].findAll('span')
                            if len(spans) > 0:
                                dirt = {}
                                amount = spans[0].text.strip()
                                dirt['storageAmount'] = amount
                                dirt['partNumber'] = part
                                dirt['dasiId'] = dasiId
                                dirt['elementId'] = str(elementId)
                                insertDasiRecord(dirt)
def getRowValue(td):
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
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "select cie.ID,cie.PART_NUMBER AS pn from client_inquiry_element cie WHERE cie.CLIENT_INQUIRY_ID = '%s'" % (
        clientInquiryId)
    cursor.execute(sql)
    l = cursor.fetchall()
    cursor.close()
    conn.commit()
    conn.close()
    return l


def insertDasi(clientInquiryId):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "insert into dasi(CLIENT_INQUIRY_ID,SEND_STATUS,COMPLETE) values('%s', '%s', '%s')" % (str(clientInquiryId),'0','0')
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def getLastInsert():
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT MAX(ID) FROM dasi"
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def updateStatus(id):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "UPDATE dasi SET COMPLETE = 1 WHERE ID = '%s'" % (id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()



def updateStatus(id):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "update dasi set complete = 1 where id = '%s'" % (id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()


def getSearchCountInAWeek(part):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT COUNT(*) FROM dasi_message dm WHERE DATEDIFF(NOW(),dm.UPDATE_TIMESTAMP) <= 7 AND dm.PART_NUMBER = '%s' ORDER BY dm.ID DESC" % (part)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id



def doCrawl(partList,id,logger,default,index,retry=0):
    try:
        if index > 0:
            partList = partList[index:]
        for ind,part in enumerate(partList):
            count = getSearchCountInAWeek(part['pn'])
            if int(count[0]) > 0:
                logger.info(part['pn']+" had crawl in a week!")
            else:
                logger.info("search "+part['pn'])
                index = ind
                checkBody = getPara(default,part['pn'])
                search = crawlSearchDo(checkBody,part['pn'])
                getRecord(search,part['pn'],id,part['id'])
                foo = [3,5,7,9,11]
                time.sleep(choice(foo))
    except Exception, ex:
        if retry == 1:
            retry = 0
            index = index + 1
        else:
            retry = retry + 1
        logger.error(str(traceback.format_exc()))
        logger.error(str(Exception) + ":" + str(ex))
        doCrawl(partList,id,logger,default,index,retry)
    



