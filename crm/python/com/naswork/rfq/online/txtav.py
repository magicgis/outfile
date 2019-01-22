#coding:utf-8
'''
Created on 2017.11.15

@author: fu_fe
'''

import requests
from random import choice
import datetime
import MySQLdb
from bs4 import BeautifulSoup
import re
import time

''' record '''

def pagerecorder(pagetext, file_name = 'page.html', encodes='utf-8'):
    try:
        with open(file_name, 'w') as file_object:
            file_object.write(pagetext.encode(encodes))
    except Exception,e:
        logger(str(e))
        print e

def nowtime():
    return datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')

def logger(text, addtime='need', filename='txtav.log', encodes='utf-8'):
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
Agent = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0'

def default_go():
    url = 'https://ww2.txtav.com/Parts'
    headers = {
    'Host': 'ww2.txtav.com',
    'User-Agent': Agent,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate, br',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1'}
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def login_go(logindata, userid, userpass):
    url = 'https://ww2.txtav.com/Account/Login'
    headers = {
    'Host': 'ww2.txtav.com',
    'User-Agent': Agent,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate, br',
    'Referer': 'https://ww2.txtav.com/Parts',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1'}
    data = {
    '__RequestVerificationToken' : logindata['Req'],
    'returnurl' : logindata['Url'],
    'Email' : userid,
    'Password' : userpass }
    res = request.post(url, data, headers=headers, timeout=60)
    return res.text

def index_go():
    url = 'https://ww2.txtav.com/Parts'
    headers = {
    'Host': 'ww2.txtav.com',
    'User-Agent': Agent,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate, br',
    'Referer': 'https://ww2.txtav.com/Parts',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1'}
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def logoff_go(logoffdata):
    url = 'https://ww2.txtav.com/Account/LogOff'
    headers = {
    'Host': 'ww2.txtav.com',
    'User-Agent': Agent,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate, br',
    'Referer': 'https://ww2.txtav.com/Parts',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1'}
    data = '__RequestVerificationToken='+logoffdata
    res = request.post(url, data, headers=headers, timeout=60)
    return res.text

def pn_search(partnum):
    url = 'https://ww2.txtav.com/Parts/Search?pn='+partnum
    headers = {
    'Host': 'ww2.txtav.com',
    'User-Agent': Agent,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate, br',
    'Referer': 'https://ww2.txtav.com/Parts',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1'}
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def get_res(href):
    url = 'https://ww2.txtav.com'+href
    headers = {
    'Host': 'ww2.txtav.com',
    'User-Agent': Agent,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate, br',
    'Referer': 'https://ww2.txtav.com/Parts/Search?pn=B-3339',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1'}
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def get_replacepn(params):
    url = 'https://ww2.txtav.com/Parts/PartSearch/PartsDetail'
    headers = {
    'Host': 'ww2.txtav.com',
    'User-Agent': Agent,
    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2',
    'Accept-Encoding': 'gzip, deflate, br',
    'Referer': 'https://ww2.txtav.com/Parts/PartSearch/PartsDetail?PartNumbers=' + params['[0].PartNumber'],
    'Content-Type': 'application/x-www-form-urlencoded',
    'Connection': 'keep-alive',
    'Upgrade-Insecure-Requests': '1'}
    res = request.post(url, params, headers=headers, timeout=60)
    return res.text

''' sql '''

def readsql():
    conn = MySQLdb.connect(host="127.0.0.1",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT * FROM search6012 limit 80,20"
    cursor.execute(sql)
    res = cursor.fetchall()
    cursor.close()
    conn.close()
    return res

def insertsql(parts,txtavQuoteId,clientInquiryElementId):
    conn = MySQLdb.connect(host="127.0.0.1",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    for part in parts:
        sql = "insert into txtav_quote_element(PART_NUMBER, DESCRIPTION, UNIT_PRICE, UNIT, CURRENCY, STOCK_MESSAGE, INFORMATION, AMOUNT, ISREPLACE,TXTAV_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID,DANGER) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s)"%(part[0],part[1],part[2],part[3],part[4],part[5],part[6],part[7],part[8],txtavQuoteId,clientInquiryElementId,part[9])
        cursor.execute(sql)
    
    conn.commit()
    conn.close()

''' handle '''

def checklogin(page, username):
    if page.find(username) >= 0:
        return 'online'
    else:
        return 'offline'

def getlogindata(page):
    logindata = {}
    soup = BeautifulSoup(page)#, 'lxml')
    
    form = soup.find(name='form', attrs={'action':'/Account/Login'})
    inputs = form.find_all('input')
    logindata['Req'] = inputs[0]['value']
    logindata['Url'] = inputs[1]['value']
    return logindata

def getlogoffdata(page):
    soup = BeautifulSoup(page)#, 'lxml')
    
    form = soup.find(name='form', attrs={'action':'/Account/LogOff'})
    inputtag = form.input
    return inputtag['value']
    
def search_handle(page,part):
    soup = BeautifulSoup(page)#, 'lxml')
    #tdiv = soup.find('div', {'style':'margin-bottom:20px;margin-right:5px;padding:5px;border:1px solid #ccc'})
    tdiv = soup.find('div', {'style':'border:1px solid #ccc; padding:15px'})
    partNum = tdiv.a.text.strip()
    if partNum == part:
        return tdiv.a['href']

def replace_check(page):
    params = {}
    
    soup = BeautifulSoup(page)#, 'lxml')
    
    warndiv = soup.select('div[class="alert alert-warning"]')
    
    if len(warndiv) > 0:
        ta = warndiv[0]('a')
        if len(ta) > 0:
            fo = soup.select('#form1')[0]
            for tin in fo.select('[type="hidden"]'):
                value = tin.get('value')
                if value == None:
                    value = ''
                value = value.replace(' ', '+')
                params[tin['name']] = value
            
            params['[0].ChangeToNewPN'] = ta[0].text.strip()
#             del params['[0].AvailableLocations[0].NextAvailDate']
            
            return params
    return 'god'

def searchres_handle(searchpage, isreplace, primary_part):
    parts = []
    info = ''
    amount = '1'
    
    soup = BeautifulSoup(searchpage)#, 'lxml')
    
    
    product = soup.select('div[class="form-inline productDescription noajax"]')[0]
    
    spans = product.select('span[class="code"]')
    
    danger = 0
    
    for span in spans:
        haz = span.findAll(text="HAZARDOUS MATERIAL")
        if len(haz) > 0:
            ifYes = str(span).find("Yes")
            if ifYes >= 0:
                danger = 1
    
    partnum = product.select('span[class="price styleSecondColor fsize20"]')[0].text.strip()
    
    description = product.select('div[style="padding-bottom:0px"] p')[0].text.strip()
    
    for s in product.select('.code')[1]('strong'):
        s.extract()
    unit = product.select('.code')[1].text.strip()
    
    infodivs = soup.select('div[class="alert alert-danger"]')
    if len(infodivs) > 0:
        for infodiv in infodivs:
            info += infodiv.text.strip()
            
            if info.find('a multiple of the Package Qty') >= 0:
                amountmsg = re.findall('Must be a multiple of the Package Qty [0-9]+', info)[0]
                amount = re.findall('[0-9]+', amountmsg)[0]
    if amount == '1':
        amount = '0'
    info = info.replace('Click here to accept: ', '')
    
    if isreplace == '1':
        if info != '' and info[-1] != '.':
            info += '. '
        info += 'this is a replace part for part '+primary_part
    
    ttables = soup('table') 
    if len(ttables) == 0:
        return parts
    ttable = soup('table')[0]
    
    ttrs = ttable.find_all('tr')
    
    for ttr in ttrs:
        ttds = ttr.find_all('td')
        if len(ttds) == 6:
            num = ttds[1].text.strip()
#            if num == '0':
#                continue
            
            warehouse = ttds[0].text.strip()
            stock = warehouse + '(' + num + ')'
            price = ttds[3].text.strip()
            currency = re.findall('[^0-9,.\s]+', price)[0]
            price = re.findall('[0-9,.]+', price)[0]
            douCount = re.findall(',', price)
            pointCount = re.findall('[.]', price)
            while len(douCount) > 1:
                price = price.replace(",","",1)
                douCount = re.findall(',', price)
                if len(douCount) == 1:
                    price.replace(",",".")
            while len(pointCount) > 1:
                price = price.replace(".","",1)
                douCount = re.findall('[.]', price)
            dou = price.find(",")
            point = price.find(".")
            if dou < point:
                price = price.replace(",","")
            else:
                price = price.replace(".","").replace(",",".")
            if float(price) > 0:
                part = [partnum, description, price, unit, currency, stock, info, amount, isreplace,danger]
                parts.append(part)
            else:
                part = [partnum, description, str(0), unit, currency, stock, info, amount, isreplace,danger]
                parts.append(part)
        
    return parts

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
        
def addTxtavQuote(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into txtav_quote(client_inquiry_id) values('%s')"%(clientInquiryId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def selectByClientInquiryId(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "select max(id) from txtav_quote where client_inquiry_id = '%s'"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

def updateStatus(id):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "update txtav_quote set complete = 1 where client_inquiry_id = '%s'"%(id)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def getSearchCountInAWeek(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT COUNT(*) FROM txtav_quote_element sqe WHERE DATEDIFF(NOW(),sqe.UPDATE_DATETIME) <= 7 AND sqe.PART_NUMBER = '%s' ORDER BY sqe.ID DESC"%(clientInquiryId)
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
            "txtav_quote_element sqe "\
        "WHERE "\
            "DATEDIFF(NOW(), sqe.UPDATE_DATETIME) <= 7 "\
        "AND sqe.PART_NUMBER = '%s' "\
        "AND sqe.CLIENT_INQUIRY_ELEMENT_ID = ( "\
            "SELECT "\
                "aqe.CLIENT_INQUIRY_ELEMENT_ID "\
            "FROM "\
                "txtav_quote_element aqe "\
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
    sql = "insert into txtav_quote_element(PART_NUMBER, DESCRIPTION, UNIT_PRICE, UNIT, CURRENCY, STOCK_MESSAGE, INFORMATION, AMOUNT, ISREPLACE,TXTAV_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID,DANGER) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s, %s)"%(data['PART_NUMBER'], data['DESCRIPTION'], data['UNIT_PRICE'],data['UNIT'],data['CURRENCY'],data['STOCK_MESSAGE'],data['INFORMATION'],data['AMOUNT'],data['ISREPLACE'],data['crawlId'],data['elementId'],data['DANGER'])
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
            if row[1] != 'None':
                currentMessage['CURRENCY'] = row[1]
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

#''' main '''
#
#username = 'Michelle'
#userid = 'michelle@betterairgroup.com'
#userpass = 'Beech061125'
#
#logger('', '')
#
#defaultpage = default_go()
#logger('Accessing defaultpage')
#
#logindata = getlogindata(defaultpage)
#
#loginpage = login_go(logindata, userid, userpass)
#logger('login go')
#
#loginstate = checklogin(loginpage, username)
#if loginstate == 'offline':
#    logger('login fail')
#    print 'login fail'
#    pagerecorder(defaultpage, 'default.html')
#    pagerecorder(loginpage, 'login.html')
#    exit(0)
#else:
#    logger(loginstate)
#    print loginstate
#
#pnres = readsql()
## pnres = [[1, '3100458-03'], [2, '3-1527-2W'], [3, 'MS21042-4']]
#
#timetosleep = [1,2,3]
#for pn in pnres:
#    time.sleep(choice(timetosleep))
#    
#    print '------------'+str(pn[0])+'-------------'
#    partnum = pn[1]
#    logger(' \tNo.'+str(pn[0]) +' : '+ partnum + ' begin')
#    
#    nowpart = partnum
#    try:
#        searchpage = pn_search(partnum)
#        logger('search '+partnum+' finish')
#        if searchpage.find('Parts Search Results for:') >= 0 :
#            if searchpage.find('Sorry, no matches found for') >= 0:
#                logger(partnum + ' has no result')
#                continue
#            href = search_handle(searchpage)
#            searchpage = get_res(href)
#            logger('get result')
#        
#        isreplace = '0'
#    
#        handledparts = []
#        breakflag = False
#        while True:
#            parts = searchres_handle(searchpage, isreplace, partnum)
#            logger(nowpart + ' search result handle finish')
#            
#            for part in parts:
#                print part
#                logger(part)
#            
#            if len(parts) > 0:
#                insertsql(parts)
#                logger('insert finish')
#            
#            handledparts.append(nowpart)
#            
#            params_rep = replace_check(searchpage)
#            if params_rep == 'god' :
#                logger('no more replace part')
#                break
#            
#            nowpart = params_rep['[0].ChangeToNewPN']
#            
#            for handledpart in handledparts:
#                if nowpart == handledpart:
#                    breakflag = True
#                    break
#            if breakflag:
#                logger('no more replace part')
#                break
#                    
#            logger('replace part '+ nowpart + ' begin')
#            isreplace = '1'
#            searchpage = get_replacepn(params_rep)
#            logger('get result')
#            
#    except Exception,e:
#        print e
#        logger('error : '+str(e))
#        pagerecorder(searchpage, 'page\\'+nowpart+'.html')
#
#    logger(partnum + ' finish')
#
#
#logoffdata = getlogoffdata(searchpage)
#
#logoffpage = logoff_go(logoffdata)
#pagerecorder(logoffpage, 'logoff.html')
#logger('logoff go')
#
#logger(checklogin(logoffpage, username))
