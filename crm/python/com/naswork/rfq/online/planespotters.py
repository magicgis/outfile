'''
Created on 9 May 2018

@author: tanoy
'''
import sys
#reload(sys)
#sys.setdefaultencoding( "utf-8" )
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
import random
from base64 import encode
import HTMLParser
from random import choice
import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )
LOGGER_NAME_CRAWL = 'planespotters'
opener = register_openers()
AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36"
HEADERS = {
    ":authority": "www.planespotters.net",
    ":method": "GET",
    ":path": "/user/login",
    ":scheme": "https",
    "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "accept-encoding": "gzip, deflate, br",
    "accept-language": "zh-CN,zh;q=0.9",
    "cache-control": "no-cache",
    "pragma": "no-cache",
    "upgrade-insecure-requests": "1",
    "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
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
":authority":"www.planespotters.net",
":method":"POST",
":path":"/user/login",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"content-length":"87",
"content-type":"application/x-www-form-urlencoded",
"origin":"https://www.planespotters.net",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/user/login",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

mainHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/user/login",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

stockMessageHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/production-list/Boeing/737",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"cookie":"__cfduid=de7ceb8fdd5657d991947f756a79426151526289645; _pk_id.1.9f6a=6feb741d266f83e7.1526289645.52.1530168262..; __psuid=9eacdb2f63cb4dc07945bf5e14c59fc3; _ga=GA1.2.450555718.1526289641; PHPSESSID=u3ri92i5hmmnqk8qcurcnbjod5; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; testSeed=2040896619; _gid=GA1.2.1323715350.1530168244; _gat=1",
"pragma":"no-cache",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

pageHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/production-list/Boeing/737?p=2",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"cookie":"__cfduid=d7d4b8004a3b03f93328977530b28ea5e1525849643; __psuid=7619aa2b80bfa3a4abe1ab36c935defb; _ga=GA1.2.373894729.1525849647; testSeed=571077140; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gid=GA1.2.468000177.1527501919; PHPSESSID=rg6jk5o99tdmh0kadlteehn415; _gat=1; _pk_id.1.9f6a=3ef5cef84e9bfbc6.1526304667.78.1527502504..",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/production-list/Boeing/737",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
}

foreFoxPageHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
"Connection":"keep-alive",
"Cookie":"__cfduid=de7ceb8fdd5657d991947f756a79426151526289645; _pk_id.1.9f6a=6feb741d266f83e7.1526289645.30.1528091720..; __psuid=9eacdb2f63cb4dc07945bf5e14c59fc3; _ga=GA1.2.450555718.1526289641; testSeed=1955558313; PHPSESSID=bmjoa9td4p7phenidbv6f7gq76; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gid=GA1.2.1240921322.1528091666",
"Host":"www.planespotters.net",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"
}

detailHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/airframe/Boeing/737/N707SA-Southwest-Airlines/OAx3fwdg",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"cookie":"__cfduid=d7d4b8004a3b03f93328977530b28ea5e1525849643; __psuid=7619aa2b80bfa3a4abe1ab36c935defb; _ga=GA1.2.373894729.1525849647; testSeed=571077140; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gid=GA1.2.468000177.1527501919; PHPSESSID=rg6jk5o99tdmh0kadlteehn415; _gat=1; _pk_id.1.9f6a=3ef5cef84e9bfbc6.1526304667.78.1527502504..",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/production-list/Boeing/737",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

session = requests.session()

class logging(object):
    def __init__(self, falconApi):
        self.logger = self._getLogger()
        
    

def crawlDefault(retry = 3):
    try:
        session = requests.session()
        url = 'https://www.planespotters.net/user/login'
        result = session.get(url, headers=HEADERS, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            raise Exception("can't not connect the internet")
        return crawlDefault(retry = retry-1)

def crawlLoginDo(content,retry = 3):
    try:
        soup = BeautifulSoup(content)
        csrfs = soup.findAll('input',{'id':'csrf'})
        csrf = "Q0IvcURhTmx0dVdsZEZxRENpNEJVQT09"
        if csrfs:
            csrf = str(csrfs[0].attrs['value'])
        url = 'https://www.planespotters.net/user/login'
        params = {'username':'tomcat200',
                'password':'CRM654321',
                'csrf':csrf,
                'redirectId':''}
        result = session.post(url,data=params, headers=loginHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            raise Exception("can't not connect the internet")
        return crawlLoginDo(retry = retry-1)
    

def getMessage(retry = 3):
    try:
        url = 'https://www.planespotters.net/production-list/Boeing/737'
        result = session.get(url, headers=stockMessageHeaders, verify=False,timeout=90)
        session.close()
        return result.text
    except Exception,ex:
        if retry < 1:
            _getLogger().error(str(traceback.format_exc()))
            _getLogger().error(str(Exception)+":"+str(ex))
            return "nothing"
        return getMessage(retry = retry-1)

def crawlAfterLoginDo(retry = 3):
    try:
        url = 'https://www.planespotters.net/'
        result = session.get(url, headers=mainHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            _getLogger().error(str(traceback.format_exc()))
            _getLogger().error(str(Exception)+":"+str(ex))
            raise Exception("can't not connect the internet")
        return crawlAfterLoginDo(retry = retry-1)


def getDetial(url,retry = 3):
    try:
        url = 'https://www.planespotters.net'+url
        result = session.get(url, headers=detailHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            _getLogger().error(str(traceback.format_exc()))
            _getLogger().error(str(Exception)+":"+str(ex))
            return "nothing"
        return getDetial(url,retry = retry-1)
    
def nextPage(pageNum,retry = 3):
    try:
        url = 'https://www.planespotters.net/production-list/Boeing/737?p='+str(pageNum)
        result = session.get(url, headers=foreFoxPageHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            _getLogger().error(str(traceback.format_exc()))
            _getLogger().error(str(Exception)+":"+str(ex))
            return "nothing"
        return nextPage(pageNum,retry = retry-1)
    
def checkNextPage(content,pageNum):
    soup = BeautifulSoup(content)
    div = soup.findAll('div',{'class','pages'})
    if len(div) == 0:
        return False
    pages = div[0].findAll('a',{'class','link'})
    for page in pages:
        if str(pageNum) == str(page.text):
            return True
    return False

def getDetailMessage(url,dict):
    content = getDetial(url)
    #print content
    soup = BeautifulSoup(content)
    h2 = soup.findAll('h2',{'class','margin-top-20'})
    msn = ''
    ln = ''
    if h2:
        h2 = h2[0].text
        st1 = str(h2).split("-")
        st2 =  str(st1[len(st1)-1]).split("/")
        if len(st2) > 1:
            msn = st2[0].strip()
        if len(st2) >= 2:
            ln = st2[1].strip()
    
#    tables = soup.findAll('table',{'class','DataTable'})
#    table = tables[len(tables)-1]
#    tbodys = table.findAll("tbody")
#    if len(tbodys)==0:
#        raise Exception('Has No tbodys')
#    trs = tbodys[0].findAll("tr")
#    tds = trs[len(trs)-1].findAll('td')
#    delivered = getRowValue(tds[3])
#    h = HTMLParser.HTMLParser()
#    delivered = h.unescape(str(delivered))
#    dict['delivered'] = delivered
    dict['ln'] = ln
    dict['msn'] = msn

def getTableMessage(content,pageNum):
    soup = BeautifulSoup(content)
    table = soup.findAll('table',{'class':'DataTable'})
    if len(table)==0:
        raise Exception('Has No table')
    tbodys = table[0].findAll("tbody")
    if len(tbodys)==0:
        raise Exception('Has No tbodys')
    trs = tbodys[0].findAll("tr")
    stockMessage = '';
    for tr in trs:
        tds = tr.findAll('td')
        url = tds[1].findAll('a')[0].attrs['href']
        aircraft = getRowValue(tds[3])
        airline = ''
        airlinesplie = ''
        if getRowValue(tds[5]):
            airline = getRowValue(tds[5])
        if len(tds[5].findAll('a')) > 0:
            airlineurl = tds[5].findAll('a')[0].attrs['href']
            airlinesplie = airlineurl.split("/")[2]
        regurl = tds[4].findAll('a')[0].attrs['href']
        regs = str(regurl).split("/")
        regurl = regs[len(regs)-2]
        reg = ''
        if airlinesplie == '':
            reg = str(regurl)
        else:
            regurl = str(regurl).split(airlinesplie)
            reg = str(regurl[0])[:-1]
        delivered = getRowValue(tds[6])
        status = getRowValue(tds[7])
        prev_reg = getRowValue(tds[8])
        remark = getRowValue(tds[9]).replace('\'','\\\'')
        
        dict = {'aircraft': aircraft,'airline':airline.replace("'"," "),'reg':reg,'status':status,'prev_reg':prev_reg,'remark':remark,'delivered':delivered,'pageNum':pageNum};
        foo = [5,6,7,8,9]
        time.sleep(choice(foo))
        getDetailMessage(url,dict)
        insertDB(dict)
    return stockMessage


def createFile(content,pageNum):
    name = 'D:/crawl/' + str(pageNum) + '.html'
    file = open(name,'w')
    file.write(content)

def getRowValue(td):
    return td.text.strip()


def insertDB(dict):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into planespotters_2(MSN, LN, AIRCRAFT_TYPE,REG,AIRLINE,DELIVERED,STATUS,PREV_REG,REMARK,PAGE_NUM) values('%s', '%s', '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s')"%(dict['msn'],dict['ln'],dict['aircraft'],dict['reg'],dict['airline'],dict['delivered'],dict['status'],dict['prev_reg'],dict['remark'],dict['pageNum'])
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def _getLogger():  
    import logging  
    import os  
    import inspect  
      
    logger = logging.getLogger('[planespotters]')  
      
    this_file = inspect.getfile(inspect.currentframe())  
    dirpath = os.path.abspath(os.path.dirname(this_file))  
    dirpath = dirpath+"\\log"
    handler = logging.FileHandler(os.path.join(dirpath, "planespotters.log"))  
      
    formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')  
    handler.setFormatter(formatter)  
      
    logger.addHandler(handler)  
    logger.setLevel(logging.INFO)  
      
    return logger
    

#default = crawlDefault()
#login = crawlLoginDo(default)
#login.find("Wrong email/password combination")
#print login
try:
    message = getMessage()
    message = nextPage(2)
    #type = sys.getfilesystemencoding() ok
    #message = message.decode('utf-8','ignore').encode(type) ok 
    print message
    createFile(message,1)
    #getTableMessage(message,"1")
#    message = nextPage(15)
#    getTableMessage(message,"15")
    for pageNum in range(2,1000):
        if checkNextPage(message,pageNum):
            message = nextPage(pageNum)
            print message
            #getTableMessage(message,pageNum)
            createFile(message,pageNum)
            if pageNum % 5 == 0:
                foo = [25,26,27,28,29,30]
                time.sleep(choice(foo))
            else:
                foo = [8,9,10,11,12,13,14,15]
                time.sleep(choice(foo))
except Exception,ex:
   _getLogger().error(str(traceback.format_exc()))
   _getLogger().error(str(Exception)+":"+str(ex))
    
