'''
Created on 20 December 2017

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
from falcon.hooks import before

def pagerecorder(pagetext, file_name = 'page.html', encodes='utf-8'):
    try:
        with open(file_name, 'w') as file_object:
            file_object.write(pagetext.encode(encodes))
    except Exception,e:
        print e

def filerecorder(bytes, file_name):
    try:
        with open(file_name, 'wb') as file_object:
            file_object.write(bytes)
            file_object.close()
    except Exception,e:
        print e

LOGGER_NAME_CRAWL = 'satair'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0"
HEADERS = {
    "User-Agent": AGENT,
    "Host": "www.aviationpartsoutlet.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate",
    "Accept-Language": "zh-CN,zh;q=0.9",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1",
    "Cache-Control":"max-age=0"
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

session = requests.session()

def crawlDefault(retry = 3):
    try:
        url = 'http://www.aviationpartsoutlet.com/parts/'
        #params = {'targetUrl':'https://www.aviall.com/aviallstorefront/'}
        result = session.get(url, headers=HEADERS, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlDefault(retry = retry-1)

def crawlSearch(part,retry = 3):
    try:
        url = 'http://www.aviationpartsoutlet.com/parts/'+part
        result = session.get(url, headers=HEADERS, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSearch(retry = retry-1)
    
def toPdf(url,retry = 3):
    try:
        result = session.get(url, headers=HEADERS, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return toPdf(retry = retry-1)

SCANHEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0",
    "Host": "www.aviationpartsoutlet.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate",
    "Accept-Language": "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":"1"
}
def getPdf(retry = 3):
    try:
        url = 'http://www.aviationpartsoutlet.com/Images/graphics/loading11.gif'
        result = session.get(url, headers=HEADERS, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return toPdf(retry = retry-1)
    
def beforescan(url):
    header = {
    'Host': 'www.aviationpartsoutlet.com',
    'User-Agent': AGENT,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate',
    'Referer': 'http://www.aviationpartsoutlet.com/parts/025277-300-07',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1'
    }
    result = session.get(url, headers=header, verify=False,timeout=90)
    return result.text
    
def scan1(retry = 3):
    try:
        url = 'http://www.aviationpartsoutlet.com/scan'
        result = session.get(url, headers=HEADERS, verify=False,timeout=90)
        return result.content
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return scan1(retry = retry-1)
    
def scan2(retry = 3):
        url = 'http://www.aviationpartsoutlet.com/scan/'
        req = urllib2.Request(url,SCANHEADERS)
        debug_handler = urllib2.HTTPHandler(debuglevel = 1)
        opener = urllib2.build_opener(debug_handler)
        response = opener.open(url,timeout=90)
        response.info().getheader('Location')
        return response.read()


def getUrl(content):
    li = content.find_next("li")
    if li:
        span = ""
        urls = list()
        regexString = "ctl00_ContentPlaceHolder1_sr1_RadTreeView1_i[0-9]_i0_RadGrid1_ctl00"
        regexp = re.compile(regexString)
        tables = li.findAll('table',{'id':regexp})
        if len(tables) > 0:
            for table in tables:
                if table.find(text="Documentation"):
                    tbodys = table.findAll('tbody')
                    if len(tbodys) > 0:
                        trs = tbodys[0].findAll('tr')
                        for tr in trs:
                            tds = tr.contents
                            if len(tds) >= 7:
                                aTarges = tds[6].findAll('a')
                                if aTarges:
                                    url = aTarges[0].attrs['href']
                                    urls.append(url)
        return urls
#    span1 = soup.findAll('span',{'id':'ctl00_ContentPlaceHolder1_sr1_RadTreeView1_i4_i0_RadGrid1_ctl00_ctl04_lblDocs'})
#    span2 = soup.findAll('span',{'id':'ctl00_ContentPlaceHolder1_sr1_RadTreeView1_i1_i0_RadGrid1_ctl00_ctl04_lblDocs'})
#    if len(span1)==0 and len(span2)==0:
#        raise Exception('Has No Record')
#    elif len(span1)==0:
#        span = span2
#    elif len(span2)==0:
#        span = span1
#    aTarges = span[0].findAll('a')
#    if aTarges:
#        url = aTarges[0].attrs['href']
#        return url


def getMessage(content,part):
    soup = BeautifulSoup(content)
#    span = soup.findAll('span',{'id':'ctl00_ContentPlaceHolder1_sr1_RadTreeView1_i4_i0_RadGrid1_ctl00_ctl04_lblDocs'})
#    if len(span)==0:
#        raise Exception('Has No Record')
#    aTarges = span[0].findAll('a')
#    if aTarges:
#        url = aTarges[0].attrs['href']
#        return url
    divs = soup.findAll('div',{'class':'rtTemplate'})
    if len(divs) > 0:
        for div in divs:
            table = div.findAll('table')
            if len(table) > 0:
                trs = table[0].findAll('tr')
                if len(trs) > 0:
                    tds = trs[0].findAll('td')
                    if len(tds) > 0:
                        con = ' '
                        if len(tds) >= 1:
                            con = getTdValue(tds[0])
                        amount = ' '
                        if len(tds) >= 2:
                            amount = tds[1].findAll('span')[0].text.strip()
                        remark = ' '
                        if len(tds) >= 4:
                            remark = getTdValue(tds[3])
                        partNumber = soup.findAll('span',{'id':'ctl00_ContentPlaceHolder1_sr1_lblPart'})[0].text.strip()
                        shortPartNumber = ''
                        for c in partNumber:
                            if re.match('[a-zA-Z0-9]', c):
                                shortPartNumber = shortPartNumber+c
                        desc = soup.findAll('span',{'id':'ctl00_ContentPlaceHolder1_sr1_lblDescription'})[0].text.strip()
                        tup = checkIfHasRecord(partNumber,con)
                        urls = getUrl(table[0])
                        hasFile = 0
                        if len(urls) > 0 :
                            hasFile  = 1
                            for url in urls:
                                beforescan(url)
                                bytes = scan1()
                                location = "D:\CRM\Files\mis\download\\"+part['pn']+"-"+con+".pdf"
                                filerecorder(bytes, location)
                        if tup:
                            id = int(tup[0])
                            if hasFile == 1:
                                updateRecord(con,amount,remark,partNumber,desc,id,shortPartNumber,"/mis/download/"+part['pn']+"-"+con+".pdf",part['pn']+"-"+con)
                            else:
                                updateRecord(con,amount,remark,partNumber,desc,id,shortPartNumber,"","")
                        else:
                            if hasFile == 1:
                                insertDB(con,amount,remark,partNumber,desc,shortPartNumber,"/mis/download/"+part['pn']+"-"+con+".pdf",part['pn']+"-"+con)
                            else:
                                insertDB(con,amount,remark,partNumber,desc,shortPartNumber,"","")
def getTdValue(td):
    if td.text:
        return td.text.strip()
    else:
        return td.text

def insertDB(con,amount,remark,partNumber,desc,shortPartNumber,filePath,fileName):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into vas_stroge(PART_NUMBER, DESCRIPTION, ABILITY,AMOUNT,REMARK,SHORT_PART_NUMBER,FILE_PATH,FILE_NAME) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')"%(partNumber,desc,con,amount,remark,shortPartNumber,filePath,fileName)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def checkIfHasRecord(partNumber,ability):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "select id from vas_stroge where PART_NUMBER = '%s' and ABILITY = '%s'"%(partNumber,ability)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def updateRecord(con,amount,remark,partNumber,desc,id,shortPartNumber,filePath,fileName):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "update vas_stroge set PART_NUMBER = '%s', DESCRIPTION = '%s', ABILITY = '%s',AMOUNT = '%s',REMARK = '%s',SHORT_PART_NUMBER = '%s',FILE_PATH = '%s',FILE_NAME = '%s' where id = '%s'"%(partNumber,desc,con,amount,remark,shortPartNumber,filePath,fileName,id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    

    
#default = crawlDefault()
#search = crawlSearch("025277-300-07")
#beforescan()
#bytes = scan1()
#filerecorder(bytes, "scan.pdf")


# scan2()




    
