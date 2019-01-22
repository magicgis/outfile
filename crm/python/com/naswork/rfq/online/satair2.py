'''
Created on 11 Feb 2017

@author: tanoy
'''
import urllib2
import cookielib
from poster.encode import multipart_encode,MultipartParam
from poster.streaminghttp import register_openers
from bs4 import BeautifulSoup
from com.naswork.rfq.common.utils import Logging,  PIDUtils 
from com.naswork.rfq.common.crawlercommon import CrawlerConstants, CrawlerManager, CrawlerBase
import traceback
import time
import MySQLdb
import math
import ssl
LOGGER_NAME_CRAWL = 'satair'
opener = register_openers()
AGENT = 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36'
HEADERS = {
    'User-Agent': AGENT,
    'Host': "ecom.satair.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch, br",
    "Accept-Language": "en,zh;q=0.8,zh-CN;q=0.6",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":1
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
formCheckData={
  'htmlbevt_ty':'htmlb:checkbox:click:null',
  'htmlbevt_frm':'LoginForm',
  'htmlbevt_oid':'agreeChkBox',
  'htmlbevt_id':'event_agg_chkbox',
  'htmlbevt_cnt':'0',
  'onInputProcessing':'htmlb',
  'sap-htmlb-design':'',
  'user_model_customer_nr':'',
  'user_model_username':'',
  'user_model_password':'',
  'user_model_tnc_check':'X',
  'user_model_tnc_check__cb':'X'
}

formData={
  'htmlbevt_ty':'htmlb:button:click:null',
  'htmlbevt_frm':'LoginForm',
  'htmlbevt_oid':'button_login',
  'htmlbevt_id':'button_login',
  'htmlbevt_cnt':'0',
  'onInputProcessing':'htmlb',
  'sap-htmlb-design':'',
  'user_model_customer_nr':'517257',
  'user_model_username':'LIANA',
  'user_model_password':'BACRMtest0926',
  'user_model_tnc_check':'X',
  'user_model_tnc_check__cb':'X'
}

searchFormData={
  'htmlbevt_ty':'htmlb:inputField:click:null',
  'htmlbevt_frm':'MainForm',
  'htmlbevt_oid':'order_ctr_matnr',
  'htmlbevt_id':'submitonenter',
  'htmlbevt_cnt':'0',
  'onInputProcessing':'htmlb',
  'sap-htmlb-design':'',
  'billto_partner':'0000517257',
  'order_model_req_date':time.strftime("%d.%m.%Y"),
  'order_model_ponum':'',
  'material_model_qty':'1',
  #'material_model_mfrpn':'HA30-0188406',
  'user_model_shipto_partner':'521371',
  'order_model_add_postal_code':'00000',
  'order_model_add_city':'San Po Kong, Kowloon',
  'order_model_add_name':'Betterair Trade Co Ltd',
  'order_model_add_region':'',
  'order_model_add_country':'HK',
  'order_model_add_name2':'',
  'user_model_terms.zterm':'Net 30 days',
  'order_model_add_street':'Rm A1 Flat A 9/F, Van Fat Factory B',
  'user_model_terms.inco2':'EXW .',
  'order_model_add_street4':'',
  'order_model_carrier':'',
  'order_model_ship_method':'',
  'order_model_acct_num':'',
  'order_model_add_street5':'',
  'order_ctr_basket_table_pager_Index':'0',
  'order_ctr_basket_table_RowCount':'0',
  'order_ctr_basket_table_AllColumnNames':'EXT_MATERIAL/MATERIAL/CAGE/MAKTX/QTY/UNIT/LINE_AMOUNT/ADD_COST/CURRENCY/P_UNIT/DLV_DAYS/AOG_CRITICAL/TEXTZ002/PLANTNAME/KURZTEXT/ATP/DELETE_MATNR/',
  'order_ctr_basket_table-TS':'NONE',
  'order_ctr_basket_table_VisibleFirstRow':'0',
  'order_ctr_basket_amount':'0.00',
}

pageFormData={
  'htmlbevt_ty':'htmlb:tableView:navigate:null',
  'htmlbevt_frm':'MainForm',
  'htmlbevt_oid':'order_ctr_basket_table',
  'htmlbevt_id':'tvNavigator',
  'htmlbevt_cnt':'1',
  'htmlbevt_par1':'',
  'onInputProcessing':'htmlb',
  'sap-htmlb-design':'',
  'billto_partner':'0000517257',
  'order_model_req_date':time.strftime("%d.%m.%Y"),
  'order_model_ponum':'',
  'material_model_qty':'1',
  #'material_model_mfrpn':'HA30-0188406',
  'user_model_shipto_partner':'521371',
  'order_model_add_postal_code':'00000',
  'order_model_add_city':'San Po Kong, Kowloon',
  'order_model_add_name':'Betterair Trade Co Ltd',
  'order_model_add_region':'',
  'order_model_add_country':'HK',
  'order_model_add_name2':'',
  'user_model_terms.zterm':'Net 30 days',
  'order_model_add_street':'Rm A1 Flat A 9/F, Van Fat Factory B',
  'user_model_terms.inco2':'EXW .',
  'order_model_add_street4':'',
  'order_model_carrier':'',
  'order_model_ship_method':'',
  'order_model_acct_num':'',
  'order_model_add_street5':'',
  'order_ctr_basket_table_pager_Index':'0',
  'order_ctr_basket_table_RowCount':'12',
  'order_ctr_basket_table_AllColumnNames':'EXT_MATERIAL/MATERIAL/CAGE/MAKTX/QTY/UNIT/LINE_AMOUNT/ADD_COST/CURRENCY/P_UNIT/DLV_DAYS/AOG_CRITICAL/TEXTZ002/PLANTNAME/KURZTEXT/ATP/DELETE_MATNR/',
  'order_ctr_basket_table-TS':'NONE',
  'order_ctr_basket_table_VisibleFirstRow':'11',
  'order_ctr_basket_amount':'0.00',
}

selectFormData={
  'htmlbevt_ty':'htmlb:tableView:rowSelection:null',
  'htmlbevt_frm':'MainForm',
  'htmlbevt_oid':'order_ctr_basket_table',
  'htmlbevt_id':'matnr_selected',
  'htmlbevt_cnt':'1',
#  'htmlbevt_par1':'',
  'onInputProcessing':'htmlb',
  'sap-htmlb-design':'',
  'order_ctr_basket_table_pager_Index':'1',
  'order_ctr_basket_table_RowCount':'0',
  'order_ctr_material_table_AllColumnNames':'MATERIAL/MAKTX/',
  'order_ctr_material_table-TS':'SINGLESELECT',
  'order_ctr_material_table_VisibleFirstRow':'1',
  'order_ctr_basket_amount':'0.00',
}

loginHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"en,zh;q=0.8,zh-CN;q=0.6",
"Cache-Control":"max-age=0",
"Connection":"keep-alive",
#"Content-Length":0,
"Content-Type":'multipart/form-data; boundary=%s' % boundry,
"Host":"ecom.satair.com",
"Origin":"ecom.satair.com",
"Upgrade-Insecure-Requests":1,
"Referer":"https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"
}
mainHeaders={
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate, br",
    "Accept-Language":"en,zh;q=0.8,zh-CN;q=0.6",
    "Cache-Control":"max-age=0",
    "Connection":"keep-alive",
    #"Content-Length":0,
    "Host":"ecom.satair.com",
    "Referer":"https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do",
    "Upgrade-Insecure-Requests":1,
    "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"
}

searchHeaders={
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate, br",
    "Accept-Language":"en,zh;q=0.8,zh-CN;q=0.6",
    "Cache-Control":"max-age=0",
    "Connection":"keep-alive",
    #"Content-Length":0,
    "Content-Type":'multipart/form-data; boundary=%s' % boundry2,
    "Host":"ecom.satair.com",
    "Origin":"ecom.satair.com",
    "Referer":"https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do",
    "Upgrade-Insecure-Requests":1,
    "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"
}



def crawlDefault():
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/default.htm'
    context = ssl._create_unverified_context()
    request = urllib2.Request(url, None, HEADERS)  
    response = opener.open(request)
#    request = urllib2.Request(url, None)
#    request.add_header('User-Agent', AGENT)
#    request.add_header('Host', "ecom.satair.com")
#    request.add_header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
#    request.add_header("Accept-Encoding", "gzip, deflate, sdch, br")
#    request.add_header("Accept-Language", "en,zh;q=0.8,zh-CN;q=0.6")
#    request.add_header("Connection", "keep-alive")
#    request.add_header("Upgrade-Insecure-Requests",1)
#    response = urllib2.urlopen(request,context = context)
    return response.read()

def crawlLoginDo():
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
    request = urllib2.Request(url, None, HEADERS)
    response = opener.open(request)
#    request = urllib2.Request(url, None)
#    request.add_header('User-Agent', AGENT)
#    request.add_header('Host', "ecom.satair.com")
#    request.add_header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
#    request.add_header("Accept-Encoding", "gzip, deflate, sdch, br")
#    request.add_header("Accept-Language", "en,zh;q=0.8,zh-CN;q=0.6")
#    request.add_header("Connection", "keep-alive")
#    request.add_header("Upgrade-Insecure-Requests",1)
#    context = ssl._create_unverified_context()
#    response = urllib2.urlopen(request,context = context)
    return response.read()
    

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

def crawlLoginDoCheckBoxMultiPart():
    loginUrl = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
    checkBody = encode_multipart_formdata(boundry,formCheckData)
    #dataGen, tmpHeaders = multipart_encode(formCheckData)
    #tmpHeaders.update(loginHeaders)
    #loginHeaders.up
    #request = urllib2.Request(loginUrl, dataGen, tmpHeaders)
    loginHeaders["Content-Length"] = len(checkBody)
    request = urllib2.Request(loginUrl, checkBody, loginHeaders)
    response = opener.open(request)
#    request = urllib2.Request(loginUrl, checkBody)
#    request.add_header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
#    request.add_header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
#    request.add_header("Accept-Encoding", "gzip, deflate, br")
#    request.add_header("Accept-Language", "en,zh;q=0.8,zh-CN;q=0.6")
#    request.add_header("Connection", "keep-alive")
#    request.add_header("Upgrade-Insecure-Requests",1)
#    request.add_header("Cache-Control","max-age=0")
#    request.add_header("Content-Type",'multipart/form-data; boundary=%s' % boundry)
#    request.add_header("Host","ecom.satair.com")
#    request.add_header("Origin","ecom.satair.com",)
#    request.add_header("Referer","https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do")
#    request.add_header("Upgrade-Insecure-Requests",1)
#    request.add_header("Upgrade-Insecure-Requests",1)
#    request.add_header("Content-Length",len(checkBody))
#    context = ssl._create_unverified_context()
#    response = urllib2.urlopen(request,context = context)
    return response.read()


def crawlLoginDoMultiPart():
    loginUrl = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
    checkBody = encode_multipart_formdata(boundry,formData)
    #dataGen, tmpHeaders = multipart_encode(formData)
    #tmpHeaders.update(loginHeaders)
    #request = urllib2.Request(loginUrl, dataGen, tmpHeaders)
    loginHeaders["Content-Length"] = len(checkBody)
#    request = urllib2.Request(loginUrl, checkBody, loginHeaders)
#    response = opener.open(request)
#    request = urllib2.Request(loginUrl, checkBody)
#    request.add_header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
#    request.add_header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
#    request.add_header("Accept-Encoding", "gzip, deflate, br")
#    request.add_header("Accept-Language", "en,zh;q=0.8,zh-CN;q=0.6")
#    request.add_header("Connection", "keep-alive")
#    request.add_header("Upgrade-Insecure-Requests",1)
#    request.add_header("Cache-Control","max-age=0")
#    request.add_header("Content-Type",'multipart/form-data; boundary=%s' % boundry)
#    request.add_header("Host","ecom.satair.com")
#    request.add_header("Origin","ecom.satair.com",)
#    request.add_header("Referer","https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do")
#    request.add_header("Upgrade-Insecure-Requests",1)
#    request.add_header("Upgrade-Insecure-Requests",1)
#    request.add_header("Content-Length",len(checkBody))
#    context = ssl._create_unverified_context()
#    response = urllib2.urlopen(request,context = context)
    return response.read()

def crawlMain():
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
    request = urllib2.Request(url, None, mainHeaders, loginHeaders)  
    context = ssl._create_unverified_context()
    response = urllib2.urlopen(request,context = context)
    return response.read()

def crawlSearchByPageMain(index,length,size):
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
    page = ''
    if index == length:
        page = 'Index,1,10,'+str(size)+',P'
    else:
        page = 'Index,1'+str(index)+',10,'+str(size)+',P'
    pageFormData.update({'order_ctr_basket_table_pager_Index':str(index)})
    pageFormData.update({'htmlbevt_par1':page})
    pageFormData.update({'order_ctr_basket_table_RowCount':str(size)})
    checkBody = encode_multipart_formdata(boundry2,pageFormData)
    searchHeaders["Content-Length"] = len(checkBody)
    request = urllib2.Request(url, checkBody, searchHeaders, loginHeaders)
    context = ssl._create_unverified_context()
    response = urllib2.urlopen(request,context = context)
    return response.read()

def crawlSearchMain(part):
    print 'search '+part
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
    searchFormData['material_model_mfrpn'] = part
    searchFormData['material_model_qty']='1'
    checkBody = encode_multipart_formdata(boundry2,searchFormData)
    searchHeaders["Content-Length"] = len(checkBody)
    request = urllib2.Request(url, checkBody, searchHeaders, loginHeaders)
    context = ssl._create_unverified_context()
    response = urllib2.urlopen(request,context = context)
    return response.read()

def crawlSelectMain(elementDic,par1):
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
    selectFormData['order_ctr_material_table_VisibleFirstRowKey'] = elementDic['order_ctr_material_table_VisibleFirstRowKey']
#    selectFormData['order_ctr_material_table-chk'] = elementDic.order_ctr_material_table-chk
#    selectFormData['order_ctr_material_table-prevchk'] = elementDic.order_ctr_material_table-prevchk
#    selectFormData['order_ctr_material_table__selectedRow'] = elementDic.order_ctr_material_table__selectedRow
    selectFormData['htmlbevt_par1'] = par1
    checkBody = encode_multipart_formdata(boundry2,selectFormData)
    searchHeaders["Content-Length"] = len(checkBody)
    request = urllib2.Request(url, checkBody, searchHeaders, loginHeaders)
    context = ssl._create_unverified_context()
    response = urllib2.urlopen(request,context = context)
    return response.read()

def crawlSearchMoqMain(keyWord,content,part,nextKeyWord = 'EA'):
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
    soup = BeautifulSoup(content)
    warn = soup.findAll('span',{'id':'order_ctr_messagebar_main-txt'})
    amount = '1'
    flag = content.find(nextKeyWord)
    for w in warn:
            elements = w.text.split()
            for index in range(len(elements)):
                if elements[index] == keyWord:
                    if keyWord == 'quantity':
                        if flag > 0:
                            amount = elements[index+1]
                        else:
                            amount = elements[index+2]
                    elif keyWord == 'rounded':
                        amount = elements[index+2]
    searchFormData['material_model_mfrpn'] = part
    searchFormData['material_model_qty']=amount
    checkBody = encode_multipart_formdata(boundry2,searchFormData)
    searchHeaders["Content-Length"] = len(checkBody)
    request = urllib2.Request(url, checkBody, searchHeaders, loginHeaders)
    context = ssl._create_unverified_context()
    response = urllib2.urlopen(request,context = context)
    return response.read()

#def parseDetailInfo(content):
#    soup = BeautifulSoup(content)
##    part = content.find('value="HA30-0188406"')
#    choose = content.find('Choose Material')
#    if choose < 0:
#        table = soup.findAll('table',{'id':'order_ctr_basket_table'})
#        if len(table)==0:
#            raise Exception('No table fond for detail info')
#        
#        tbodys = table[0].findAll('tbody')
#        trs = tbodys[0].findAll('tr')
#        tds = trs[0].findAll('td')
#        formTable = tds[0].findAll('table')
#        fromTrs = formTable[0].findAll('tr')
#        warn = soup.findAll('span',{'id':'order_ctr_messagebar_main-txt'})
#        infoList = list()
#        for tr in fromTrs:
#            tds = tr.findAll('td')
#            if len(tds) >= 16:
#                enterPn = getRowValue(tds[0])
#                pn = getRowValue(tds[1])
#                cage_code = getRowValue(tds[2])
#                description = getRowValue(tds[3])
#                amount = getRowValue(tds[4])
#                unit = getRowValue(tds[5])
#                unit_price = getRowValue(tds[6])
#                currency =  getRowValue(tds[8])
#                leadTime = getRowValue(tds[10])
#                plant =  getRowValue(tds[14])
#                certification =  getRowValue(tds[15])
#                infoList.append(DetailInfo(pn, cage_code, description, unit,unit_price,currency,certification,amount,leadTime,plant,enterPn))
#        
#        return infoList
    
def parseDetailInfo(infoList,content):
    soup = BeautifulSoup(content)
#    part = content.find('value="HA30-0188406"')
    print time.strftime("%Y-%m-%d %H:%M:%S")+'begin getting message!'
    choose = content.find('Choose Material')
    choose = content.find('Your shopping basket is empty')
    test = content.find('I agree to Satair')
    if choose < 0:
        table = soup.findAll('table',{'id':'order_ctr_basket_table'})
        if len(table)==0:
            raise Exception('No table fond for detail info')
        
        tbodys = table[0].findAll('tbody')
        trs = tbodys[0].findAll('tr')
        tds = trs[0].findAll('td')
        formTable = tds[0].findAll('table')
        fromTrs = formTable[0].findAll('tr')
        warn = soup.findAll('span',{'id':'order_ctr_messagebar_main-txt'})
        for tr in fromTrs:
            tds = tr.findAll('td')
            if len(tds) >= 16:
                enterPn = getRowValue(tds[0])
                pn = getRowValue(tds[1])
                cage_code = getRowValue(tds[2])
                description = getRowValue(tds[3])
                amount = getRowValue(tds[4])
                unit = getRowValue(tds[5])
                unit_price = getRowValue(tds[6]).replace('.','').replace(',','.')
                currency =  getRowValue(tds[8])
                leadTime = getRowValue(tds[10])
                plant =  getRowValue(tds[14])
                certification =  getRowValue(tds[15])
                if float(unit_price) > 0:
                    infoList.append(DetailInfo(pn, cage_code, description, unit,unit_price,currency,certification,amount,leadTime,plant,enterPn))
        
        return infoList
    
def parseSelectInfo(desc,content,part,clientInquiryId):
    soup = BeautifulSoup(content)
#    part = content.find('value="HA30-0188406"')
    choose = content.find('Choose Material')
    if choose > 0:
        table = soup.findAll('table',{'style':'empty-cells:show;'})
        if len(table)==0:
            raise Exception('No table fond for detail info')
        
#        tbodys = table[0].findAll('tbody')
        trs = table[0].findAll('tr')
#        tds = trs[0].findAll('td')
#        formTable = tds[0].findAll('table')
#        fromTrs = formTable[0].findAll('tr')
#        warn = soup.findAll('span',{'id':'order_ctr_messagebar_main-txt'})
        parList = list() 
        size = len(trs)
        
        for tr in trs:
            tds = tr.findAll('td')
            if len(tds) > 1:
                if getRowValue(tds[2]) == desc:
                    text = tds[0].findAll('button',{'class':'urSTRowUnSelIcon'})[0].attrs['onclick'].split("'")
                    return text[-2]
        
        insertEmailList(part,clientInquiryId)
        td = trs[1].findAll('td')
        if len(tds) > 1:
            text = td[0].findAll('button',{'class':'urSTRowUnSelIcon'})[0].attrs['onclick'].split("'")
            return text[-2]
                    
def getRowValue(td):
    return td.text.strip()

class DetailInfo(object):
    def __init__(self, pn, cage_code, description, unit,unit_price,currency,certification,amount,leadTime,plant,enterPn):
        self.pn = pn
        self.cage_code = cage_code
        self.description = description
        self.unit = unit
        self.unit_price = unit_price
        self.currency = currency
        self.certification = certification
        self.amount = amount
        self.leadTime = leadTime
        self.plant = plant
        self.enterPn = enterPn
    
def insertDB(data,satairQuoteId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_quote_element(part_number, cage_code, description, unit,unit_price,currency,certification,lead_time,plant,enter_partnumber,amount,satair_quote_id) values('%s', '%s', '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s', '%s', '%s')"%(data.pn, data.cage_code, data.description, data.unit,data.unit_price,data.currency,data.certification,data.leadTime,data.plant,data.enterPn,data.amount,satairQuoteId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()

def insertEmailList(part,clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_email(part_number, client_inquiry_id) values('%s', '%s')"%(part,clientInquiryId)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def getSelectElement(content):
    soup = BeautifulSoup(content)
    VisibleFirstRowKey = soup.findAll('input',{'name':'order_ctr_material_table_VisibleFirstRowKey'})[0].attrs['value']
#    chk = soup.findAll('input',{'name':'order_ctr_material_table-chk'})[0].attrs['value']
#    prevchk = soup.findAll('input',{'name':'order_ctr_material_table-prevchk'})[0].attrs['value']
#    selectedRow = soup.findAll('input',{'name':'order_ctr_material_table__selectedRow'})[0].attrs['value']
    elementDic = {'order_ctr_material_table_VisibleFirstRowKey':VisibleFirstRowKey
#                  'order_ctr_material_table-chk':chk,
#                  'order_ctr_material_table-prevchk':prevchk,
#                  'order_ctr_material_table__selectedRow':selectedRow
                  }
    return elementDic

def addSatairQuote(clientInquiryId):
        conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
        cursor = conn.cursor()
        sql = "insert into satair_quote(client_inquiry_id) values('%s')"%(clientInquiryId)
        cursor.execute(sql)
        cursor.close()
        conn.commit()
        conn.close()
        print 'create satair_quote complete!'
    
def selectByClientInquiryId(clientInquiryId):
        conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
        cursor = conn.cursor()
        sql = "select id from satair_quote where client_inquiry_id = '%s'"%(clientInquiryId)
        cursor.execute(sql)
        id = cursor.fetchone()
        cursor.close()
        conn.commit()
        conn.close()
        return id
    
def updateStatus(id):
        conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
        cursor = conn.cursor()
        sql = "update satair_quote set complete = 1 where id = '%s'"%(id)
        cursor.execute(sql)
        cursor.close()
        conn.commit()
        conn.close()

#price = '1.546,42'
#price = price.replace('.', '').replace(',','.')
#default = crawlDefault()
#loginDo = crawlLoginDo()
#result = crawlLoginDoCheckBoxMultiPart()
#response = crawlLoginDoMultiPart()
##detailInfo2 = parseDetailInfo(response)
#mainContent = crawlMain()
#partlist = list()
#partlist.append(DetailInfo('ABS0691-050', '', 'STUD', '','','','','','','',''))
#partlist.append(DetailInfo('EN6049-006-05-5', '', 'Sleeving', '','','','','','','',''))
#partlist.append(DetailInfo('168400-33', '', 'PAWN OF RELEASE', '','','','','','','',''))
#partlist.append(DetailInfo('23080-1903', '', 'BRUSH', '','','','','','','',''))
#partlist.append(DetailInfo('5011-02-0001', '', 'SPRING', '','','','','','','',''))
##partlist.append(DetailInfo('3118597-01', '', '', '','','','','','','',''))
##partlist.append(DetailInfo('80A0005-02', '', '', '','','','','','','',''))
##partlist.append(DetailInfo('A1170776', '', '', '','','','','','','',''))
##partlist.append(DetailInfo('ABS1040-64', '', '', '','','','','','','',''))
##partlist.append(DetailInfo('1152682-3', '', '', '','','','','','','',''))
#foo = [2,3,4]
#from random import choice
##for part in partlist:
##    searchContent = crawlSearchMain(part)
##    dataList = list()
##    time.sleep(choice(foo))
##dataList = parseDetailInfo(searchContent)
#
#index = int(math.ceil(len(partlist)/float(10)))
#infoList = list()
#dataList = list()
#for part in partlist:
#    text = crawlSearchMain(part.pn)
#    select = text.find('Choose Material')
#    if select > 0:
#        elementDic = getSelectElement(text)
#        par1 = parseSelectInfo(part.description,text)
#        crawlSelectMain(elementDic,par1)
#    quantity = text.find('order quantity')
#    rounded  = text.find('rounded to')
#    if text.find('order quantity') > 0:
#        crawlSearchMoqMain('quantity',text,part.pn)
#    elif text.find('rounded to') > 0:
#        crawlSearchMoqMain('rounded',text,part.pn)
#    time.sleep(choice(foo))
#for i in range(0,index):
#    searchContent = crawlSearchByPageMain(i+1,index,len(partlist))
#    dataList = parseDetailInfo(infoList,searchContent)
#for detailInfo in dataList:
#    insertDB(detailInfo,2)
    
