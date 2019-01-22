'''
Created on 2017.11.10

@author: fu_fe
'''

import requests
import random
from random import choice
import datetime
import MySQLdb
import json
from bs4 import BeautifulSoup
import re
import time

''' record '''

'''user message'''
username = 'Tracy Chen'
userid = 'purchaser@betterairgroup.com'
userpass = 'Ccx111222'

def pagerecorder(pagetext, file_name = 'page.html', encodes='utf-8'):
    try:
        with open(file_name, 'w') as file_object:
            file_object.write(pagetext.encode(encodes))
    except Exception,e:
        logger(str(e))
        print e

def nowtime():
    return datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')

def logger(text, addtime='need', filename='kapco.log', encodes='utf-8'):
    try:
        text = str(text)
        text += '\n'
        if addtime=='need':
            text = nowtime() + ' -> ' + text
        with open(filename, 'a') as file_object:
            file_object.write(text.encode(encodes))
    except Exception,e:
        print e

''' request '''

request = requests.session()
agent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3047.4 Safari/537.36'

def default_go():
    url = 'https://kart.kapco-global.com/'
    headers = {
    'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
    'Accept-Encoding':'gzip, deflate, sdch, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Cache-Control':'max-age=0',
    'Connection':'keep-alive',
    'Host':'procart.proponent.com',
    'Upgrade-Insecure-Requests':'1',
    'User-Agent':agent
    }
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def login_go(userid, userpass,para, retry = 3):
    try:
        if retry>0:
            #url = 'https://kart.kapco-global.com/'
            url = 'https://procart.proponent.com/'
            headers = {
            'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding':'gzip, deflate, br',
            'Accept-Language':'zh-CN,zh;q=0.9',
            'Cache-Control':'no-cache',
            'Connection':'keep-alive',
            'Content-Length':'2352',
            'Content-Type':'application/x-www-form-urlencoded',
            'Host':'procart.proponent.com',
            'Origin':'https://procart.proponent.com',
            'Referer':'https://procart.proponent.com/',
            'Pragma':'no-cache',
            'Upgrade-Insecure-Requests':'1',
            'User-Agent':agent
            }
            Params = {
            '__EVENTTARGET':'',
            '__EVENTARGUMENT':'',
            '__VIEWSTATE':para['__VIEWSTATE'],
            '__VIEWSTATEGENERATOR':'8D0E13E6',
            '__EVENTVALIDATION':para['__EVENTVALIDATION'],
            'ctl00$MainContent$txtUserName':userid,
            'ctl00$MainContent$txtPassword':userpass,
            'ctl00$MainContent$btnLogin':'LOGIN',
            'ctl00$MainContent$txtEmail':'',
            'ctl00$MainContent$txtPosition':'',
            'ctl00$MainContent$txtFirstName':'',
            'ctl00$MainContent$txtLastName':'',
            'ctl00$MainContent$txtCompany':'',
            'ctl00$MainContent$txtAddress':'',
            'ctl00$MainContent$txtPostal':'',
            'ctl00$MainContent$txtPhone':''
            }
            res = request.post(url, data=Params, headers=headers, timeout=60)
            return res.text
    except Exception,e:
        print e
        logger('login exception : '+e)
        print 'loginfail, retryless:'+str(retry)
        login_go(userid, userpass, retry = retry - 1)

def logout_go():
    url = 'https://kart.kapco-global.com/Handlers/LogOut.ashx'
    headers = {
    'Accept':'*/*',
    'Accept-Encoding':'gzip, deflate, sdch, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Connection':'keep-alive',
    'Host':'procart.proponent.com',
    'Referer':'https://procart.proponent.com/PartSearch.aspx',
    'User-Agent': agent,
    'X-Requested-With':'XMLHttpRequest'
    }
    request.get(url, headers=headers, timeout=60)

def afterlogout():
    url = 'https://procart.proponent.com/'
    headers = {
    'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
    'Accept-Encoding':'gzip, deflate, sdch, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Cache-Control':'max-age=0',
    'Connection':'keep-alive',
    'Host':'procart.proponent.com',
    'Referer':'https://procart.proponent.com/PartSearch.aspx',
    'Upgrade-Insecure-Requests':'1',
    'User-Agent':agent
    }
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def find_send(partnum,para):
    url = 'https://procart.proponent.com/PartSearch.aspx'
    headers = {
    'Accept':'*/*',
    'Accept-Encoding':'gzip, deflate, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Cache-Control':'no-cache',
    'Connection':'keep-alive',
    'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8',
    'Host':'procart.proponent.com',
    'Origin':'https://procart.proponent.com',
    'Referer':'https://procart.proponent.com/PartSearch.aspx',
    'User-Agent':agent,
    'X-MicrosoftAjax':'Delta=true',
    'X-Requested-With':'XMLHttpRequest'
    }
    params = {
    'ctl00$MainContent$scriptmanager1':'ctl00$MainContent$updatepnl|ctl00$MainContent$btnSearch',
    '__EVENTTARGET':'',
    '__EVENTARGUMENT':'',
    '__VIEWSTATE':para['__VIEWSTATE'],
    '__VIEWSTATEGENERATOR':'4439047B',
    '__EVENTVALIDATION':para['__EVENTVALIDATION'],
    'ctl00$MainContent$rptControls$ctl00$txtPart':partnum,
    'ctl00$MainContent$rptControls$ctl00$txtQty':'1',
    'ctl00$MainContent$rptControls$ctl01$txtPart':'',
    'ctl00$MainContent$rptControls$ctl01$txtQty':'',
    'ctl00$MainContent$rptControls$ctl02$txtPart':'',
    'ctl00$MainContent$rptControls$ctl02$txtQty':'',
    'ctl00$MainContent$rptControls$ctl03$txtPart':'',
    'ctl00$MainContent$rptControls$ctl03$txtQty':'',
    'ctl00$MainContent$rptControls$ctl04$txtPart':'',
    'ctl00$MainContent$rptControls$ctl04$txtQty':'',
    'ctl00$MainContent$txtBulkPart':'',
    'ctl00$MainContent$txtWildSearchPart':'',
    '__ASYNCPOST':'true',
    'ctl00$MainContent$btnSearch':'SEARCH'
    }
    request.post(url, data=params, headers=headers, timeout=60)

def find_page():
    url = 'https://procart.proponent.com/PartSearchResults.aspx'
    headers = {
    'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
    'Accept-Encoding':'gzip, deflate, sdch, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Connection':'keep-alive',
    'Host':'procart.proponent.com',
    'Referer':'https://procart.proponent.com/PartSearch.aspx',
    'Upgrade-Insecure-Requests':'1',
    'User-Agent': agent
    }
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def find_result():
    nd = '151945'+str(random.randint(1000000,9999999))
    url = 'https://procart.proponent.com/Handlers/PartSearchHandler.ashx'
    headers = {
    'Accept':'application/json, text/javascript, */*; q=0.01',
    'Accept-Encoding':'gzip, deflate, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Connection':'keep-alive',
    'Content-Length':'88',
    'Content-Type':'application/json; charset=UTF-8',
    'Host':'procart.proponent.com',
    'Origin':'https://procart.proponent.com',
    'Referer':'https://procart.proponent.com/PartSearchResults.aspx',
    'X-Requested-With':'XMLHttpRequest',
    'User-Agent': agent
    }
    params = {"_search":'false',"nd":nd,"rows":'10',"page":'1',"sidx":"cust_part","sord":"desc"}
    res = request.post(url, data=params, headers=headers, timeout=60)
    return res.text

def getDetail(partNumber):
    url = 'https://procart.proponent.com/Handlers/PartSearchHandler.ashx?PartNo='+str(partNumber)+'&PartDetails=true'
    headers = {
    'Accept':'*/*',
    'Accept-Encoding':'gzip, deflate, br',
    'Accept-Language':'zh-CN,zh;q=0.9',
    'Cache-Control':'no-cache',
    'Connection':'keep-alive',
    'Host':'procart.proponent.com',
    'Referer':'https://procart.proponent.com/PartSearch.aspx',
    'X-Requested-With':'XMLHttpRequest',
    'User-Agent':agent
    }
    res = request.get(url, headers=headers, timeout=60)
    return res.text

''' sql '''

def readsql():
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT * FROM search1005 limit 100,50"
    cursor.execute(sql)
    res = cursor.fetchall()
    cursor.close()
    conn.close()
    return res

def save_1(PART,clientInquiryId,clientInquiryElementId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    
    sql = "insert into kapco_quote_element(PART_NUMBER, DESCRIPTION, UNIT_PRICE, UNIT, CURRENCY, LEAD_TIME, STOCK_MESSAGE, INFORMATION, AMOUNT, ISREPLACE,KAPCO_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID,DANGER) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s)"%(PART['1partnum'],PART['2description'],PART['3price'],PART['4unit'],PART['5currency'],PART['6leadtime'],PART['7stock'],PART['8info'].replace("'","\\'"),PART['9amount'],PART['10replace'],clientInquiryId,clientInquiryElementId,PART['11danger'])
    cursor.execute(sql)
    
    conn.commit()
    conn.close()

def save_2(PART,clientInquiryId,clientInquiryElementId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    
    sql = "insert into kapco_quote_element(PART_NUMBER, INFORMATION,KAPCO_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID) values('%s', '%s', '%s', '%s')"%(PART['1partnum'],PART['7info'],clientInquiryId,clientInquiryElementId)
    cursor.execute(sql)
    
    conn.commit()
    conn.close()


''' handle '''
    
def checklogin(page):
    try:
        file5 = 'error\do_record_1.html'
        with open(file5, 'w') as file_5:
            file_5.write(page.encode("utf-8"))
    except Exception,e1:
        print e1
    if page.find(username) >=0:
        logger('online now')
        print 'online now'
    else:
        logger('login fail')
        print 'bye'
        exit(0)

def logout():
    logout_go()
    if afterlogout().find(username) < 0:
        logger('logout finish')
        print 'logout finish'

def datahandle(part):
    PART_NUMBER = part['Cust_part']
    
    PART = {}
    REPLACE = '0'
    LEAD_TIME = '0'
    
    if part['Item_description1'].find('This is an approved substitute part for part') >= 0:
#         replaceinfo = re.findall('This is an approved substitute part for part [^<\s]+', part['Item_description1'])[0]
        REPLACE = '1'
    
    #if part['Price'] == '' or part['Price'] == '0' or part['Item_description1'].find('This part number is not recognized in Kapco Global system') >=0 :
    if part['Price'] == '' or part['Item_description1'].find('This part number is not recognized in Kapco Global system') >=0 :
        INFORMATION = 'This part number is not recognized in Kapco Global system.'
        if REPLACE != '1':
            PART['1partnum'] = PART_NUMBER
            PART['7info'] = INFORMATION
    else:
        DESCRIPTION = part['Item_description1'].replace('</br>', ', ')
        UNIT_PRICE = part['Price']
        UNIT = part['Um']
        CURRENCY = '$'
        
        STOCK_MESSAGE = ''
        for warehouse in part['LstWarehouses']:
            displayname = warehouse['DisplayName'].split(':')[1].strip()
            num = warehouse['WhsQty']
            STOCK_MESSAGE += displayname + '(' + str(num) + ')' + ','
        STOCK_MESSAGE = STOCK_MESSAGE[:-1]
        
        if part['other_fields'] == None:
            INFORMATION = ''
            AMOUNT = '1'
        else:
            INFORMATION = part['other_fields'].replace('</br>', '. ')
            info = BeautifulSoup(INFORMATION)
            for s in info('a'):
                s.extract()
            INFORMATION = info.text
            
            DATEinfo = re.findall('[\d]+ days', INFORMATION)
            if len(DATEinfo) == 0:
                DATEinfo = re.findall('[\d]+ calendar days', INFORMATION)
            
            if INFORMATION.find('NEXT BUSINESS DAY') >=0 or INFORMATION.find('next business day') >=0:
                LEAD_TIME = '5'
            if len(DATEinfo) > 0:
                LEAD_TIME = re.findall('[\d]+', DATEinfo[0])[0]
        
            AMOUNTinfo1 = re.findall('This part is sold in [\d,]+ [A-Z]+ increments only', INFORMATION)
            AMOUNTinfo2 = re.findall('Minimum order quantity for this part is [\d,]+ [A-Z]+', INFORMATION)
            AMOUNT1 = '1'
            AMOUNT2 = '1'
            if len(AMOUNTinfo1) > 0:
                AMOUNT1 = re.findall('[\d,]+', AMOUNTinfo1[0])[0]
            if len(AMOUNTinfo2) > 0:
                AMOUNT2 = re.findall('[\d,]+', AMOUNTinfo2[0])[0]
            AMOUNT = max(AMOUNT1, AMOUNT2)
        
        PART['1partnum'] = PART_NUMBER
        PART['2description'] = DESCRIPTION
        danger = getDetailInformation(PART_NUMBER)
        if float(UNIT_PRICE) > 0:
            PART['3price'] = UNIT_PRICE
        else:
            PART['3price'] = str(0)
        PART['4unit'] = UNIT
        PART['5currency'] = CURRENCY
        PART['6leadtime'] = LEAD_TIME
        PART['7stock'] = STOCK_MESSAGE
        PART['8info'] = INFORMATION
        PART['9amount'] = AMOUNT
        PART['10replace'] = REPLACE
        PART['11danger'] = danger
    
    return PART

def getDetailInformation(PART_NUMBER):
    details = getDetail(PART_NUMBER)
    soup = BeautifulSoup(details)
    regexString = "divHazmat"+".*"
    regexp = re.compile(regexString)
    divs = soup.findAll('div',{'id':regexp})
    if len(divs) > 0:
        ps = divs[0].findAll('p')
        if len(ps) > 0:
            isYes = str(ps[0]).find("Y")
            if isYes >= 0:
                return 1
            else:
                return 0

def getLoginPara(content):
    para = {}
    soup = BeautifulSoup(content)
    viewstat = soup.findAll('input',{'id':'__VIEWSTATE'})
    eventvalidation = soup.findAll('input',{'id':'__EVENTVALIDATION'})
    if len(viewstat) > 0:
        para['__VIEWSTATE'] = viewstat[0].attrs['value'].strip()
    else:
        para['__VIEWSTATE'] = 'FytBSlgJRpUYBPfwJUIl9PRW7aKtx46NXE3B3EpokCb5mOzhEtNsaKiZOLrU99UZ7YacMzfPucfh6CBpwD7D2x34bBKz05x4JUOH/QGC8CXGiOdlX/JLHgqJ74aOaA0rNTn7sAl1PzZBnoT0e0ux1mgwGRu5yXsh1tk9qvLiIAB0WWjco/Se0fjSyhz+ZzQ+wnIMsKjAHvCaU+V0ffSHfLBbL8TfJjXIc9yuc8cnBlLeJE0nJT52ckBMlqxDNPOCgTgWuM86BP9QU2Dpjw/pVcS+GiSnOEE5oNU4yMj5sTkW03EhffhT/TDSEbES0r/4HhvwJ1AV2PmXI9HoVj1XS5rZRTWHC37K+3ehBC1qA0MBRZOJlgSjSBBP2NJXEVhG5jM9lE02gfqyauVJN169HFqwPAzGfHL3SBGO6ziA1AXSNmWYKR/mofFt+eY1u6lKw6+MwaULrm4r4+xBwRMPsrrh2SCIl4jdUGZj5fEjGxtBIZiaf6xmZMoagHK9lk/Ag2Hj6Fps4zknIkXD6s0ks4sJ96a1ckcRFjbjskiba53Fb2fUw3ve0iNy8+l+Kov/JOicBqmD6pyLjYT6ZXG9+wbZ6NqY4jVAKYzCfJturR0x1NpUua/QZvZw/IkycAxWwk+zLlHE/dM35y8Tbcr3bTJ3PYLVMRMnLs+B0QE4OrZ9rVneeGQLCGgpQPdnHfRw3SHL8N63oonzHbbb3zCo3vuO87skBr/E6sPsKmfybOsactjjKmGk/7nkbWW0UiSnsEnkp5tA7w39Kh3ZxBXt9DuO9SYve0UkZkoVkWvTrnbZV3oHunNm088SXeXfIj3I1j5mkUasjK1ae4ZDnJJoOulahhruw4OTInCA/8MEZhTs1GIngJtZSkwE3y2HZWtqbp8PdfSVHfjKiZnPNMOkdfZDcukM0vYVmKeiav7cZcNezbjJVSY1+EsIzaqRa/89IbCbNPBmTCcmG2S4VGqZIufFg3vw/L0fpdUXB1YWjZnK5QC/dbWTom3pgbIZPBSaU6QAk4QKEMNu1J6wf0y01aTdn6xA1XTzdTAOJ2SNqWcFcQDXjXW+gtfXvpfG+XNFlmbfWCmEgz9ucUNAfSw7ijbUVZpqYrWTZkGnY1d3rxJwFIFgRVHdNAkDYJTsPSCSc7M3GgAoDRLoGz6MkqHjGeHL0sNZU6sQuQhCueR/2TqkrQghfBpiVWhvRKpbUGYykQxYY0XK1xYBnaXrXx5Tbgw2e44UFU9sv3uxlTD/2XfAiPooWimlBZCEB2GvcmnKBwvMegcC3EqcbZLaOHxZe/DeJ3LWS7R2I6a79zniRaKCjtFKkO8/cUgyZ2aMR8lsWXmiha3rEDnVYA79EPgDBkUFjFwbz1e0tUuPS2DVVZ8='
    if len(eventvalidation) > 0:
        para['__EVENTVALIDATION'] = eventvalidation[0].attrs['value'].strip()
    else:
        para['__EVENTVALIDATION'] = '8b14JAKMm+o5TyXHATCLX3J0pRLLmYMJYsEvYRoBlmJkTV19guwHo8LqEQW/fHz9M2TSTTCQaN3m6DzDal4rolt4UyGQpncbbXB0F5TE7mXQuSeCK7z9IpqwTI507WtYPvbzcAISAppI+IpyH+yZybHkSfTcTXcm4CUPUqSkf7uJYvxux0d6ZcZ5W7AHQWyuECFPU2UGzXCO7SmuZrPrt6ZhCyMgZXf8Zy3sHU7WDxicT4jQt/Hh4OzsFg2ogWkzi824ZbsqpET5qC/G1gpcrA+UUzQ8ahnrcESpI1gD7w1D3GBBf/+gDzz4i+gyGwXdGEE/lid7f3s8+VvJA3YhqXeBpVAm6G/xXUvJGnYBudE='
    return para
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
    sql = "insert into kapco_quote(client_inquiry_id) values('%s')"%(clientInquiryId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def selectByClientInquiryId(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "select max(id) from kapco_quote where client_inquiry_id = '%s'"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def updateStatus(id):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "update kapco_quote set complete = 1 where client_inquiry_id = '%s'"%(id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def getSearchCountInAWeek(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT COUNT(*) FROM kapco_quote_element sqe WHERE DATEDIFF(NOW(),sqe.UPDATE_DATETIME) <= 7 AND sqe.PART_NUMBER = '%s' ORDER BY sqe.ID DESC"%(clientInquiryId)
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
            "sqe.AMOUNT, "\
            "sqe.DANGER, "\
            "sqe.ISREPLACE "\
        "FROM "\
            "kapco_quote_element sqe "\
        "WHERE "\
            "DATEDIFF(NOW(), sqe.UPDATE_DATETIME) <= 7 "\
        "AND sqe.PART_NUMBER = '%s' "\
        "AND sqe.CLIENT_INQUIRY_ELEMENT_ID = ( "\
            "SELECT "\
                "aqe.CLIENT_INQUIRY_ELEMENT_ID "\
            "FROM "\
                "kapco_quote_element aqe "\
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
    sql = "insert into kapco_quote_element(PART_NUMBER, DESCRIPTION, UNIT_PRICE, UNIT, CURRENCY, LEAD_TIME, STOCK_MESSAGE, INFORMATION, AMOUNT, ISREPLACE,KAPCO_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID,DANGER) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s)"%(data['PART_NUMBER'], data['DESCRIPTION'], data['UNIT_PRICE'],data['UNIT'],data['CURRENCY'],data['LEAD_TIME'],data['STOCK_MESSAGE'],data['INFORMATION'],data['AMOUNT'],data['ISREPLACE'],data['crawlId'],data['elementId'],data['DANGER'])
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
            if str(row[11]) != 'None':
                currentMessage['DANGER'] = str(row[11])
            else:
                currentMessage['DANGER'] = ''
            if str(row[12]) != 'None':
                currentMessage['ISREPLACE'] = str(row[12])
            else:
                currentMessage['ISREPLACE'] = ''
            currentMessage['crawlId'] = crawlId
            currentMessage['elementId'] = elementId
            insertDBForCopy(currentMessage)


''' main '''

#logger('', '')
#
#
#default = default_go()
#logger('Accessing defaultpage')
## pagerecorder(default, 'default.html')
#
#loginres = login_go(userid, userpass)
#logger('do login')
## pagerecorder(loginres, 'login.html')
#
#checklogin(loginres)
#
#res = readsql()
#logger('readsql finish')
#
#timetosleep = [1,2,3,4,5,6]
#for part in res:
#    print '---------------'+str(part[0])+'--------------'
#    logger(str(part[0]) + ' : ' + part[1] + ' begin')
#    time.sleep(choice(timetosleep))
#    
#    try:
#        find_send(part[1])
#        logger('partnum send')
#        findpage = find_page()
#        logger('get page')
#        findjson = find_result()
#        logger('get result')
#    except Exception,e:
#        logger(e)
#        print 'request error : '+ str(e)
#        continue
#    
#    jsonres = json.loads(findjson)
#    
#    try:
#        for partnum in jsonres:
#            
#            partres = datahandle(partnum)
#            
#            for key, value in sorted(partres.items()):
#                print key +' : '+value
#                logger('\t\t'+key +' : '+value)
#            
#            if len(partres) == 9:
#                save_1(partres)
#                logger('save finish')
#            elif len(partres) == 2:
#                save_2(partres)
#                logger('save finish')
#            else:
#                logger('no record')
#            
#    except Exception,e:
#        print 'error-'+part[1]+' : '+str(e)
#        logger('error'+' : '+str(e))
#        pagerecorder(findpage, 'recorder\\'+part[1]+'.html')
#        pagerecorder(findjson, 'recorder\\'+part[1]+'.json')
#        
#logout()

