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
    'Host':'kart.kapco-global.com',
    'Upgrade-Insecure-Requests':'1',
    'User-Agent':agent
    }
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def login_go(userid, userpass, retry = 3):
    try:
        if retry>0:
            url = 'https://kart.kapco-global.com/'
            headers = {
            'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding':'gzip, deflate, br',
            'Accept-Language':'zh-CN,zh;q=0.8',
            'Cache-Control':'max-age=0',
            'Connection':'keep-alive',
            'Content-Length':'2502',
            'Content-Type':'application/x-www-form-urlencoded',
            'Host':'kart.kapco-global.com',
            'Origin':'https://kart.kapco-global.com',
            'Referer':'https://kart.kapco-global.com/',
            'Upgrade-Insecure-Requests':'1',
            'User-Agent':agent
            }
            Params = {
            '__EVENTTARGET':'',
            '__EVENTARGUMENT':'',
            '__VIEWSTATE':'wX7Jn76r3YCD1Z9+ci2EKzU6K5ndqAAvFANCMj7OhFvYpJmGaxsC7BRhpqOZLTCljF+lknG1xMKS1KOFcAioIcxxd6ewtSZA63+LHm2w28zPshZ7Bd4lvJKTsYwxk8H0OBlbBdrnF/jRCOftwzajHryzU/T2uX06W6WLJya1d7S+nQf6Nk4QyP9IzFQBr+MIIU8YmTk8ZVQsuGtSc6t4gXbsadX6f3AHhtWOjSHsASrr64GT3KhY/D/wVq0sW3OF2N8Vd4vbqKeclr/VxVrTNsdFpeJFuGoiZCNJ4IaEEfImO2RfO0kQENmhfLYR1GafAPoCmdws8S50oqKnsxgXuX0Ua6B17t2+O2GG62Tb1pJ375+NhZB8McSqiNJ0PsBr3GeSS2xUU3JOB7Sk65VmJw8h0tj/QcPvY7su47uv+ThZcO8UN0BEuqsIaOsH227B1BO94QxeiuZ4SKtz+2rAkEbRFaCfZg6Gj8scpYi3qA+96LiEHfA2j+KQAMxPZWidWyx0I0Im1YX9sgYdm9tALoi0P7v+r6MNtxUFeOuSXztH+EkbubTmQs4kRGyYWG4g3A2/JhmCS/bEZJvk6GZ0k6A3+7Hp2O6u71s7ipYgSoT5qBNf9Q8kADRjK5wCgIKep3Y4sCizA0iexct9Az1kbvbOQ7s5n75rJJES6gB0lcCHW1rdsRPdGBHNuMbIXU48O/2Rj5jasmV7W+WyQIHHblMCRV0AuLpc+R5kudFC9g9nHPEnkI8XmQ7YQQNFH7+Npvdl5a7DYWIZ8GBJxWNuJ/QHK5DLNzw/DLX4kgq+VJI334K8vi42Sdl/cPu5/bi+LchFGVQoB+rZdudo/DMlZjE4m0UMEbSE0QGi7AhPDw7V29TlEqx+GE1OKCZBI0kCwXqGYfZSl23FBPaTsHSkYowaDbiFLYf9TXrs5VovbGnuXkAXaKgtOl0MT/MOxLmt0SaAwtgyL/qH/P92Y0w2AzbUhazHj+FZCyxvnuGnE7CYWb6PUbfZ+S5qIjR94JUxKwEoUNOHLYgH62H2OT5SZ2eDS9Fy/gRJ0pqpNuz4l8wi9poLgTqJ4OREyurh8Xz9gZwdWLNjndpBFynFf6tBz8bVWgmUxUNP7veGg0h5aLjWdRqvUBfiR76Hk5ssww/npVVsVtKDRNa4A/wpwcoDFE06GbO4O005HCSk/pmRlotFXZK/8YqV/y+xRV3vf3otRPBONnlTPyl5lGEeZsasULVlRqabhDS+Zxq8saj3I+B952MFQQkooLBAkXOLOtCzMGC9jQ2yuG8ujJYTj13QbpeWI+mRRGLdLHPlHiYVoMicL9KlLuHH14o6y6Zg8iuLjkUwG2fKg4+YURgu9ZsJuldc5i2e6GjZwEaHUYFQBp0Okd+2LD4epvib1tnm7fRIUsHoZ2ssCulZN2CevhJvvTkiLhH1EfHzGtx1a50z+w7Udxy3KD+6Z/3eHBOFWzI6BSynAjGkkb0MNrr9irJXyw==',
            '__VIEWSTATEGENERATOR':'8D0E13E6',
            '__EVENTVALIDATION':'xwLK3PH7YbnlhRy0UrIkNJMTqNujBJiTCDMx9dyisMrLTZEBDX9yRF+a8FhvnPEI4hId8uZhIPdLtpdfvYCDvQ0lD0ocbifX7uTKVJzCmw9D7JDF98b6buc4ojmYtSRa3EaAOR2hmwSoU74Deb6RPScgxPI/L+gvhN3EZ/WxpLddEgBTDO/mE5m0HgWhmntksK9+dpaJvQ9Q0XTnn2HmfdHOvTsA8MOihjoAdp6nrC8+gumiYyqF2LFkPowPjN717R8v62dUs4qRAyK17TEkoqZaAkxonM1lzSu7gkV1pRdw8QGj1K/WyO2YTzDUevbqpTglxtBFQVgM96YubucKjKpZJK8GniRa3OkzymtjTNE=',
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
    'Host':'kart.kapco-global.com',
    'Referer':'https://kart.kapco-global.com/PartSearch.aspx',
    'User-Agent': agent,
    'X-Requested-With':'XMLHttpRequest'
    }
    request.get(url, headers=headers, timeout=60)

def afterlogout():
    url = 'https://kart.kapco-global.com/'
    headers = {
    'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
    'Accept-Encoding':'gzip, deflate, sdch, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Cache-Control':'max-age=0',
    'Connection':'keep-alive',
    'Host':'kart.kapco-global.com',
    'Referer':'https://kart.kapco-global.com/PartSearch.aspx',
    'Upgrade-Insecure-Requests':'1',
    'User-Agent':agent
    }
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def find_send(partnum):
    url = 'https://kart.kapco-global.com/PartSearch.aspx'
    headers = {
    'Accept':'*/*',
    'Accept-Encoding':'gzip, deflate, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Cache-Control':'no-cache',
    'Connection':'keep-alive',
    'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8',
    'Host':'kart.kapco-global.com',
    'Origin':'https://kart.kapco-global.com',
    'Referer':'https://kart.kapco-global.com/PartSearch.aspx',
    'User-Agent':agent,
    'X-MicrosoftAjax':'Delta=true',
    'X-Requested-With':'XMLHttpRequest'
    }
    params = {
    'ctl00$MainContent$scriptmanager1':'ctl00$MainContent$updatepnl|ctl00$MainContent$btnSearch',
    '__EVENTTARGET':'',
    '__EVENTARGUMENT':'',
    '__VIEWSTATE':'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',
    '__VIEWSTATEGENERATOR':'4439047B',
    '__EVENTVALIDATION':'URCHbwLJMYmDyTZPdBuGePqTA8d3XYwvrbj7bilE+Cj91srMF9ch7KcOkz2wqb2jDhHR4elRIC5xKgrY69ZVRgmDP480g7deExKQNWELw8fQdZ31QZAkor7RaBsf5WhxWqtyPH/U8fJY0oSGmrbTTl7yjwgIP4uSwxUWJ4/PEWaPjwRyr0DUhdMy5ug8OjCPgKUTFnTyX2h/s64tviR+5CDVIxHueGoZMOo5OyTL8vtpJKVbbCvyv29XH5whIgHy1mJID2LqzRWWH0pYQ1qg52KonibBoL0gCVQAjzZAF1p99F9GJwpa2ruCfHlCCZ1Pi64W6P9l5qQqA51lZaKKu3Z0Du7UkRwQFiRdmR2mIHBRYK5T2Dop0QcTnuyoBR/y+QrxVzTE/dEUIYGIkB+Uoz0t/k+eVeHAknj1np/H1Azaso7kH4KRtjXukUJdXBxcbDo8dvHBgtCHRWwd2CfMGBYHkjPAW/7xNcx+eFhrt38XEjuuu8NW2czEaVySqKU5mThU5sfwmJ7kFRoXKpgkJvvRPrGbR+axUqjgPyxFSB4=',
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
    url = 'https://kart.kapco-global.com/PartSearchResults.aspx'
    headers = {
    'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
    'Accept-Encoding':'gzip, deflate, sdch, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Connection':'keep-alive',
    'Host':'kart.kapco-global.com',
    'Referer':'https://kart.kapco-global.com/PartSearch.aspx',
    'Upgrade-Insecure-Requests':'1',
    'User-Agent': agent
    }
    res = request.get(url, headers=headers, timeout=60)
    return res.text

def find_result():
    nd = '151038'+str(random.randint(1000000,9999999))
    url = 'https://kart.kapco-global.com/Handlers/PartSearchHandler.ashx'
    headers = {
    'Accept':'application/json, text/javascript, */*; q=0.01',
    'Accept-Encoding':'gzip, deflate, br',
    'Accept-Language':'zh-CN,zh;q=0.8',
    'Connection':'keep-alive',
    'Content-Length':'88',
    'Content-Type':'application/json; charset=UTF-8',
    'Host':'kart.kapco-global.com',
    'Origin':'https://kart.kapco-global.com',
    'Referer':'https://kart.kapco-global.com/PartSearchResults.aspx',
    'X-Requested-With':'XMLHttpRequest',
    'User-Agent': agent
    }
    params = {"_search":'false',"nd":nd,"rows":'10',"page":'1',"sidx":"cust_part","sord":"desc"}
    res = request.post(url, data=params, headers=headers, timeout=60)
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
    
    sql = "insert into kapco_quote_element(PART_NUMBER, DESCRIPTION, UNIT_PRICE, UNIT, CURRENCY, STOCK_MESSAGE, INFORMATION, AMOUNT, ISREPLACE,KAPCO_QUOTE_ID,CLIENT_INQUIRY_ELEMENT_ID) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, %s, %s, %s)"%(PART['1partnum'],PART['2description'],PART['3price'],PART['4unit'],PART['5currency'],PART['6stock'],PART['7info'],PART['8amount'],PART['9replace'],clientInquiryId,clientInquiryElementId)
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
    PART_NUMBER = part['Item_number']
    
    PART = {}
    REPLACE = '0'
    
    if part['Item_description1'].find('This is an approved substitute part for part') >= 0:
#         replaceinfo = re.findall('This is an approved substitute part for part [^<\s]+', part['Item_description1'])[0]
        REPLACE = '1'
    
    if part['Price'] == '' or part['Price'] == '0' or part['Item_description1'].find('This part number is not recognized in Kapco Global system') >=0 :
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
        PART['3price'] = UNIT_PRICE
        PART['4unit'] = UNIT
        PART['5currency'] = CURRENCY
        PART['6stock'] = STOCK_MESSAGE
        PART['7info'] = INFORMATION
        PART['8amount'] = AMOUNT
        PART['9replace'] = REPLACE
    
    return PART

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
    sql = "select id from kapco_quote where client_inquiry_id = '%s'"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id


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

