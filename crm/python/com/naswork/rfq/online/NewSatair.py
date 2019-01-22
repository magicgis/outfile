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

loginHeaders = {
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate, br",
    "Accept-Language":"zh-CN,zh;q=0.9",
    "Cache-Control":"no-cache",
    "Connection":"keep-alive",
    "Content-Type":"application/x-www-form-urlencoded",
    "Host":"spares.satair.com",
    "Origin":"https://spares.satair.com",
    "Pragma":"no-cache",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent": AGENT
}

session = requests.session()


def crawlDefault(retry=3):
    try:
        url = 'https://spares.satair.com/portal/stocks/status/stocks.jsp.port'
        result = session.get(url, headers=HEADERS, verify=False, timeout=120)
        return result.text
    except Exception, ex:
        if retry < 1:
            return "nothing"
        return crawlDefault(retry=retry - 1)


def crawlLoginDo(retry=3):
    try:
        url = 'https://spares.satair.com/portal/stocks/status/stocks.jsp.port'
        params = {
            'AuthAction':'login',
            'menu-id': 'info',
            'userId': '517257-LIANA',
            'password': '517257Liana2',#'Liana517257',
            'tac': 'on',
        }
        loginHeaders["Content-Length"] = str(len(params))
        result = session.post(url, data=params, headers=loginHeaders, verify=False, timeout=120)
        return result.text
    except Exception, ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDo(retry=retry - 1)

def searchDo(part,retry=3):
    try:
        url = 'https://spares.satair.com/portal/stocks/status/stocks.jsp.port'
        params = {
            'menu-id':'single_part_inquiry',
            'mode':'portal',
            'REQUEST':'INQUIRY_SINGLE',
            'ACTION':'INQUIRY',
            'clearBasketMessages':'true',
            'E_PNR':str(part),
            'E_MFR':'',
            'INTERCHANGEABLES':'TRUE',
            'SUPPLIERS':'FALSE',
            'SUBMIT.x':'0',
            'SUBMIT.y':'0'
        }
        loginHeaders["Content-Length"] = str(len(params))
        result = session.post(url, data=params, headers=loginHeaders, verify=False, timeout=120)
        return result.text
    except Exception, ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDo(retry=retry - 1)


def parseDetailInfo(content,part,satairQuoteId):
    soup = BeautifulSoup(content)
    table = soup.findAll('table', {'class': 'portlet-table-collapse'})
    if len(table) == 0:
        currentMessage = {}
        warnMessage = soup.find('td', {
            'style': 'padding: 12px; border-style:solid; border-color:white; border-width:1px; border-top-style:none'})
        if warnMessage and len(warnMessage) > 0:
            currentMessage['warnMessage'] = warnMessage.text.strip()
        else:
            currentMessage['warnMessage'] = "The provided part number is not known to our backend system."
        currentMessage['part'] = part
        insertNoMessageDB(currentMessage, satairQuoteId)
        return None
    tbodys = table[0].findAll('tbody')
    trs = tbodys[0].findAll('tr',{'style':'vertical-align: top;'})
    if len(trs) > 0:
        message = {}
        saveList = list()
        for index,tr in enumerate(trs):
            tds = tr.findAll('td')
            if len(tds) == 13:
                currentMessage = {}
                currentMessage['part'] = message['part']
                currentMessage['cage'] = message['cage']
                currentMessage['description'] = message['description']
                currentMessage['location'] = tds[1].findAll('span')[0].text.strip()
                currentMessage['unit'] = tds[4].findAll('span')[0].text.strip()
                currentMessage['price'] = tds[5].findAll('span')[0].text.strip()
                currentMessage['currency'] = tds[6].findAll('span')[0].text.strip()
                currentMessage['leadtime'] = tds[7].findAll('span')[0].text.strip()
                moq = tds[9].findAll('span')[0].text.strip()
                if moq == "":
                    currentMessage['amount'] = '1'
                else:
                    currentMessage['amount'] = moq
                currentMessage["certification"] = getCertification(tds[2])
                amount = tds[3].findAll('span')[0].text.strip()
                if amount == '':
                    currentMessage["storageAmount"] = "0"
                else:
                    currentMessage["storageAmount"] = amount
                if getDanger(tds[10]):
                    currentMessage["danger"] = 1
                else:
                    currentMessage["danger"] = 0
                saveList.append(currentMessage)
            elif len(tds) > 13:
                currentMessage = {}
                if tds[1].find('img'):
                    message['part'] = tds[2].findAll('span')[0].text.strip()
                    message['cage'] = tds[4].findAll('span')[0].text.strip()
                    message['description'] = tds[5].findAll('span')[0].text.strip()
                    currentMessage['part'] = message['part']
                    currentMessage['cage'] = message['cage']
                    currentMessage['description'] = message['description']
                    currentMessage['location'] = tds[12].findAll('span')[0].text.strip()
                    currentMessage['unit'] = tds[15].findAll('span')[0].text.strip()
                    currentMessage['price'] = tds[16].findAll('span')[0].text.strip()
                    currentMessage['currency'] = tds[17].findAll('span')[0].text.strip()
                    currentMessage['leadtime'] = tds[18].findAll('span')[0].text.strip()
                    moq = tds[20].findAll('span')[0].text.strip()
                    if moq == "":
                        currentMessage['amount'] = '1'
                    else:
                        currentMessage['amount'] = moq
                    currentMessage["certification"] = getCertification(tds[13])
                    amount = tds[14].findAll('span')[0].text.strip()
                    if amount == '':
                        currentMessage["storageAmount"] = "0"
                    else:
                        currentMessage["storageAmount"] = amount
                    if getDanger(tds[21]):
                        currentMessage["danger"] = 1
                    else:
                        currentMessage["danger"] = 0
                    saveList.append(currentMessage)
                else:
                    message['part'] = tds[1].findAll('span')[0].text.strip()
                    message['cage'] = tds[3].findAll('span')[0].text.strip()
                    message['description'] = tds[4].findAll('span')[0].text.strip()
                    currentMessage['part'] = message['part']
                    currentMessage['cage'] = message['cage']
                    currentMessage['description'] = message['description']
                    currentMessage['location'] = tds[11].findAll('span')[0].text.strip()
                    currentMessage['unit'] = tds[14].findAll('span')[0].text.strip()
                    currentMessage['price'] = tds[15].findAll('span')[0].text.strip()
                    currentMessage['currency'] = tds[16].findAll('span')[0].text.strip()
                    currentMessage['leadtime'] = tds[17].findAll('span')[0].text.strip()
                    moq = tds[19].findAll('span')[0].text.strip()
                    if moq == "":
                        currentMessage['amount'] = '1'
                    else:
                        currentMessage['amount'] = moq
                    currentMessage["certification"] = getCertification(tds[12])
                    amount = tds[13].findAll('span')[0].text.strip()
                    if amount == '':
                        currentMessage["storageAmount"] = "0"
                    else:
                        currentMessage["storageAmount"] = amount
                    if getDanger(tds[20]):
                        currentMessage["danger"] = 1
                    else:
                        currentMessage["danger"] = 0
                    saveList.append(currentMessage)
                
        return saveList
    # else:
    #     currentMessage = {}
    #     warnMessage = soup.find('td',{'style':'padding: 12px; border-style:solid; border-color:white; border-width:1px; border-top-style:none'})
    #     if len(warnMessage) > 0:
    #         currentMessage['warnMessage'] = warnMessage.text.strip()
    #     else:
    #         currentMessage['warnMessage'] = "The provided part number is not known to our backend system."
    #     currentMessage['part'] = part
    #     insertNoMessageDB(currentMessage,satairQuoteId)
    #     return None


def insertDB(data, satairQuoteId,elementId):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_quote_element(part_number, cage_code, description, unit,unit_price,currency,certification,lead_time,plant,amount,satair_quote_id,if_danger,storage_amount,CLIENT_INQUIRY_ELEMENT_ID) values('%s', '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')" % (
    data['part'], data['cage'], data['description'], data['unit'], data['price'], data['currency'], data["certification"],
    data['leadtime'], data['location'], data["amount"], satairQuoteId, data["danger"],data['storageAmount'],elementId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def insertNoMessageDB(data, satairQuoteId):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_quote_element(part_number,SATAIR_QUOTE_ID,remark) values('%s','%s','%s')" % (str(data['part']),str(satairQuoteId),str(data['warnMessage']))
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def getCertification(message):
    te = message.findAll('img')
    if len(te) > 0:
        teStyle = te[0].attrs['onmouseover']
        if len(teStyle) > 0:
            ar = teStyle.split(";")
            if len(ar) > 0:
                ar = ar[-1].split("'")
                if len(ar) > 0:
                    ar = ar[1].split(":")
                    if len(ar) > 0:
                        return  ar[1].strip()
            else:
                return "";
        else:
            return "";

    else:
        return  "";

def getDanger(message):
    te = message.findAll('img')
    if len(te) > 0:
        index= str(te[0]).find("hazardous")
        if index >= 0:
            return True
        else:
            return False
    else:
        return False



def LogOut(retry=3):
    try:
        url = 'https://spares.satair.com/portal/info.jsp.port?menu-id=info&AuthAction=logout'
        params = {
            'menu-id':'info',
            'AuthAction':'logout'
        }
        result = session.get(url, data=params, headers=loginHeaders, verify=False, timeout=120)
        return result.text
    except Exception, ex:
        if retry < 1:
            return "nothing"
        return LogOut(retry=retry - 1)

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


def insertEmailList(part, clientInquiryId):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_email(part_number, client_inquiry_id) values('%s', '%s')" % (part, clientInquiryId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()


def getSelectElement(content):
    soup = BeautifulSoup(content)
    VisibleFirstRowKey = soup.findAll('input', {'name': 'order_ctr_material_table_VisibleFirstRowKey'})[0].attrs[
        'value']
    elementDic = {'order_ctr_material_table_VisibleFirstRowKey': VisibleFirstRowKey}
    return elementDic


def addSatairQuote(clientInquiryId):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_quote(client_inquiry_id) values('%s')" % (clientInquiryId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    # print 'create satair_quote complete!'


def addDangerRecord(clientInquiryId, clientInquiryElementId):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_danger(client_inquiry_id,client_inquiry_element_id) values('%s','%s')" % (
    clientInquiryId, clientInquiryElementId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    # print 'create satair_quote complete!'


def selectByClientInquiryId(clientInquiryId):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "select max(id) from satair_quote where client_inquiry_id = '%s'" % (clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id


def updateStatus(id):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "update satair_quote set complete = 1 where client_inquiry_id = '%s'" % (id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()


def getLocationDistance(location):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT location.`CODE` FROM system_code location WHERE location.`VALUE` = '%s'" % (location)
    cursor.execute(sql)
    code = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return code


def getSearchCountInAWeek(part):
    conn = MySQLdb.connect(host="localhost", user="betterair", passwd="betterair", db="crm", charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT COUNT(*) FROM satair_quote_element sqe WHERE DATEDIFF(NOW(),sqe.UPDATE_DATETIME) <= 7 AND sqe.PART_NUMBER = '%s' ORDER BY sqe.ID DESC" % (part)
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
#    sql="SELECT "\
#            "sqe.CERTIFICATION, "\
#            "sqe.CURRENCY, "\
#            "sqe.DESCRIPTION, "\
#            "sqe.LEAD_TIME, "\
#            "sqe.PART_NUMBER, "\
#            "sqe.UNIT_PRICE, "\
#            "sqe.UNIT, "\
#            "sqe.CAGE_CODE, "\
#            "sqe.AMOUNT, "\
#            "sqe.if_danger, "\
#            "sqe.STORAGE_AMOUNT, "\
#            "sqe.plant "\
#        "FROM "\
#            "satair_quote_element sqe "\
#        "WHERE "\
#            "DATEDIFF(NOW(), sqe.UPDATE_DATETIME) <= 7 "\
#        "AND sqe.PART_NUMBER = '%s' "\
#        "AND sqe.SATAIR_QUOTE_ID = ( "\
#            "SELECT "\
#                "aqe.SATAIR_QUOTE_ID "\
#            "FROM "\
#                "satair_quote_element aqe "\
#            "WHERE "\
#                "DATEDIFF(NOW(), aqe.UPDATE_DATETIME) <= 7 "\
#            "AND aqe.PART_NUMBER = '%s' "\
#            "ORDER BY "\
#                "aqe.SATAIR_QUOTE_ID DESC "\
#            "LIMIT 0, "\
#            "1 "\
#        ")"%(str(part),str(part))
        
    sql = "SELECT "\
            "sqe.CERTIFICATION, "\
            "sqe.CURRENCY, "\
            "sqe.DESCRIPTION, "\
            "sqe.LEAD_TIME, "\
            "sqe.PART_NUMBER, "\
            "sqe.UNIT_PRICE, "\
            "sqe.UNIT, "\
            "sqe.CAGE_CODE, "\
            "sqe.AMOUNT, "\
            "sqe.if_danger, "\
            "sqe.STORAGE_AMOUNT, "\
            "sqe.plant "\
        "FROM "\
            "satair_quote_element sqe "\
        "WHERE "\
            "DATEDIFF(NOW(), sqe.UPDATE_DATETIME) <= 7 "\
        "AND sqe.PART_NUMBER = '%s' "\
        "AND sqe.CLIENT_INQUIRY_ELEMENT_ID = ( "\
            "SELECT "\
                "aqe.CLIENT_INQUIRY_ELEMENT_ID "\
            "FROM "\
                "satair_quote_element aqe "\
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
    sql = "insert into satair_quote_element(part_number, cage_code, description, unit,unit_price,currency,certification,lead_time,plant,amount,satair_quote_id,if_danger,storage_amount,CLIENT_INQUIRY_ELEMENT_ID) values('%s', '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')"%(data['PART_NUMBER'], data['CAGE_CODE'], data['DESCRIPTION'],data['UNIT'],data['UNIT_PRICE'],data['CURRENCY'],data['CERTIFICATION'],data['LEAD_TIME'],data['plant'],data['AMOUNT'],data['crawlId'],data['if_danger'],data['STORAGE_AMOUNT'],data['elementId'])
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
                currentMessage['UNIT_PRICE'] = str(row[5])
            else:
                currentMessage['UNIT_PRICE'] = ''
            if str(row[6]) != 'None':
                currentMessage['UNIT'] = str(row[6])
            else:
                currentMessage['UNIT'] = ''
            if str(row[7]) != 'None':
                currentMessage['CAGE_CODE'] = str(row[7])
            else:
                currentMessage['CAGE_CODE'] = ''
            if str(row[8]) != 'None':
                currentMessage['AMOUNT'] = str(row[8])
            else:
                currentMessage['AMOUNT'] = ''
            if str(row[9]) != 'None':
                currentMessage['if_danger'] = str(row[9])
            else:
                currentMessage['if_danger'] = ''
            if str(row[10]) != 'None':
                currentMessage['STORAGE_AMOUNT'] = str(row[10])
            else:
                currentMessage['STORAGE_AMOUNT'] = ''
            if str(row[11]) != 'None':
                currentMessage['plant'] = str(row[11])
            else:
                currentMessage['plant'] = ''
            currentMessage['crawlId'] = crawlId
            currentMessage['elementId'] = elementId
            insertDBForCopy(currentMessage)
            

# partlist = list()
# partlist.append("ABS2311-01XF")
# partlist.append("802300-14")
# partlist.append("NSA936501TA2003")
# partlist.append("1500-3640-001")
# default = crawlDefault()
# login = crawlLoginDo()
# if login.find("Limmy") >= 0:
#     print "login success!"
# for part in partlist:
#     message = searchDo(part)
#     saveList= parseDetailInfo(message,part,1)
#     if saveList != None:
#         for save in saveList:
#             insertDB(save,str(1))
#     foo = [4,5,6,7,8,9]
#     time.sleep(choice(foo))
# loginOut = LogOut()


