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
import MySQLdb
import math
import ssl
import requests
import re
import time
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
    "Upgrade-Insecure-Requests":"1"
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
#  'user_model_username':'CASSIE',
#  'user_model_password':'Sat0126U88',
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

selectLocationFormData={
  'htmlbevt_ty':'htmlb:tableView:cellClick:null',
  'htmlbevt_frm':'MainForm',
  'htmlbevt_oid':'order_ctr_basket_table',
  'htmlbevt_id':'plant_atp:_:ATP',
  'htmlbevt_cnt':'2',
  'htmlbevt_par1':'',
  'htmlbevt_par2':'16',
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
  'order_ctr_basket_table_pager_Index':'1',
  'order_ctr_basket_table_RowCount':'12',
  'order_ctr_basket_table_AllColumnNames':'EXT_MATERIAL/MATERIAL/CAGE/MAKTX/QTY/UNIT/LINE_AMOUNT/ADD_COST/CURRENCY/P_UNIT/DLV_DAYS/AOG_CRITICAL/TEXTZ002/PLANTNAME/KURZTEXT/ATP/DELETE_MATNR/',
  'order_ctr_basket_table-TS':'NONE',
  'order_ctr_basket_table_VisibleFirstRow':'1',
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

selectLocationTableData={
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
  'order_ctr_material_table_AllColumnNames':'MATERIAL/MAKTX/DLV_DATE/CURR_ISO/P_UNIT/BASE_UOM/KURZTEXT/PLANTNAME/',
  'order_ctr_material_table-TS':'SINGLESELECT',
  'order_ctr_material_table_VisibleFirstRow':'1',
  'order_ctr_basket_amount':'0.00',
#  'order_ctr_material_table_VisibleFirstRowKey':'',
  'order_ctr_material_table-chk':'0:0',
  'order_ctr_material_table-prevchk':'0:0',
  'order_ctr_material_table__selectedRow':'order_ctr_material_table_selrow_0:0'
}

deleteAllTableData={
  'htmlbevt_ty':'htmlb:tableView:headerClick:null',
  'htmlbevt_frm':'MainForm',
  'htmlbevt_oid':'order_ctr_basket_table',
  'htmlbevt_id':'onHeaderClick_basket',
  'htmlbevt_cnt':'3',
  'htmlbevt_par1':'17',
  'htmlbevt_par2':'DELETE_MATNR',
  'htmlbevt_par3':'FALSE',
  'onInputProcessing':'htmlb',
  'sap-htmlb-design':'',
  'billto_partner':'0000517257',
  'order_model_req_date':time.strftime("%d.%m.%Y"),
  'order_model_ponum':'',
  'material_model_qty':'1',
  #'material_model_mfrpn':'HA30-0188406',
  'user_model_shipto_partner':'521371',
  'order_model_add_postal_code':'00000',
  'order_model_add_city':'Kowloon',
  'order_model_add_name':'Betterair Trade Co Ltd',
  'order_model_add_region':'',
  'order_model_add_country':'HK',
  'order_model_add_name2':'',
  'user_model_terms.zterm':'Net 30 days',
  'order_model_add_street':'Unit 04, 7/F Bright Way Tower',
  'user_model_terms.inco2':'EXW .',
  'order_model_add_street4':'',
  'order_model_carrier':'',
  'order_model_ship_method':'',
  'order_model_acct_num':'',
  'order_model_add_street5':'',
  'order_ctr_basket_table_pager_Index':'1',
  'order_ctr_basket_table_AllColumnNames':'EXT_MATERIAL/MATERIAL/CAGE/MAKTX/QTY/UNIT/LINE_AMOUNT/ADD_COST/CURRENCY/P_UNIT/DLV_DAYS/AOG_CRITICAL/TEXTZ002/PLANTNAME/KURZTEXT/ATP/DELETE_MATNR/',
  'order_ctr_basket_table-TS':'NONE',
  'order_ctr_basket_table_VisibleFirstRow':'1',
  'order_ctr_basket_amount':'0.00',
}

toMainAfterSelectLocationData={
  'htmlbevt_ty':'htmlb:button:click:null',
  'htmlbevt_frm':'MainForm',
  'htmlbevt_oid':'order_ctr_add_plant_to_basket',
  'htmlbevt_id':'add_plant_to_basket',
  'htmlbevt_cnt':'0',
#  'htmlbevt_par1':'',
  'onInputProcessing':'htmlb',
  'sap-htmlb-design':'',
  'order_ctr_material_table_pager_Index':'1',
  'order_ctr_material_table_RowCount':'3',
  'order_ctr_material_table_AllColumnNames':'MATERIAL/MAKTX/DLV_DATE/CURR_ISO/P_UNIT/BASE_UOM/KURZTEXT/PLANTNAME/',
  'order_ctr_material_table-TS':'SINGLESELECT',
  'order_ctr_material_table_VisibleFirstRow':'1',
  'order_ctr_basket_amount':'0.00',
#  'order_ctr_material_table_VisibleFirstRowKey':'',
  'order_ctr_material_table-chk':'0:0',
  'order_ctr_material_table-prevchk':'0:0',
  'order_ctr_material_table__selectedRow':'order_ctr_material_table_selrow_0:0'
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
"Upgrade-Insecure-Requests":"1",
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
    "Upgrade-Insecure-Requests":"1",
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
    "Upgrade-Insecure-Requests":"1",
    "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"
}

deleteHeaders={
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate, br",
    "Accept-Language":"zh-CN,zh;q=0.9",
    "Cache-Control":"no-cache",
    "Connection":"keep-alive",
    #"Content-Length":0,
    "Content-Type":'multipart/form-data; boundary=%s' % boundry2,
    "Host":"ecom.satair.com",
    "Origin":"https://ecom.satair.com",
    "Pragmas":"no-cache",
    "Referer":"https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"
}

selectLocationHeaders={
    "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding":"gzip, deflate, br",
    "Accept-Language":"zh-CN,zh;q=0.8",
    "Cache-Control":"max-age=0",
    "Connection":"keep-alive",
    #"Content-Length":0,
    "Content-Type":'multipart/form-data; boundary=%s' % boundry2,
    "Host":"ecom.satair.com",
    "Origin":"https://ecom.satair.com",
    "Referer":"https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do",
    "Upgrade-Insecure-Requests":"1",
    "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"
}

loginOutHeaders={
"Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"Accept-Encoding":"gzip, deflate, br",
"Accept-Language":"zh-CN,zh;q=0.9",
"Cache-Control":"no-cache",
"Connection":"keep-alive",
"Host":"ecom.satair.com",
"Pragma":"no-cache",
"Upgrade-Insecure-Requests":"1",
"Upgrade-Insecure-Requests":"1",
"User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"
}

session = requests.session()

def crawlDefault(retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/default.htm'
        result = session.get(url, headers=HEADERS, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlDefault(retry = retry-1)
    

def crawlLoginDo(retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
        result = session.get(url, headers=HEADERS, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDo(retry = retry-1)
    
def LoginOut(retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
        result = session.get(url, headers=loginOutHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return LoginOut(retry = retry-1)
    

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

def crawlLoginDoCheckBoxMultiPart(retry = 3):
    try:
        loginUrl = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
        checkBody = encode_multipart_formdata(boundry,formCheckData)
        loginHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(loginUrl, checkBody, headers=loginHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDoCheckBoxMultiPart(retry = retry-1)


def crawlLoginDoMultiPart(retry = 3):
    try:
        loginUrl = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
        checkBody = encode_multipart_formdata(boundry,formData)
        loginHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(loginUrl, checkBody, headers=loginHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlLoginDoMultiPart(retry = retry-1)

def crawlMain(retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
        result = session.get(url, headers=mainHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlMain(retry = retry-1)

def crawlSearchByPageMain(index,length,size,retry = 3):
    try:
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
        searchHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url,checkBody, headers=searchHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSearchByPageMain(index,length,size,retry = retry-1)

def crawlSearchMain(part,retry = 3):
    try:
        #print 'search '+part
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
        searchFormData['material_model_mfrpn'] = part
        searchFormData['material_model_qty']='1'
        checkBody = encode_multipart_formdata(boundry2,searchFormData)
        searchHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url,checkBody, headers=searchHeaders, verify=False,timeout=120)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSearchMain(part,retry = retry-1)
    

def crawlSelectMain(elementDic,par1,retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
        selectFormData['order_ctr_material_table_VisibleFirstRowKey'] = elementDic['order_ctr_material_table_VisibleFirstRowKey']
        selectFormData['htmlbevt_par1'] = par1
        checkBody = encode_multipart_formdata(boundry2,selectFormData)
        searchHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url,checkBody, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSelectMain(elementDic,par1,retry = retry-1)
    
def crawlSelectLocationMain(location,retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
        selectLocationTableData['order_ctr_material_table_VisibleFirstRowKey'] = location['VisibleFirstRowKey']
        selectLocationTableData['htmlbevt_par1'] = location['part1']
        checkBody = encode_multipart_formdata(boundry2,selectLocationTableData)
        searchHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url,checkBody, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSelectLocationMain(location,retry = retry-1)
    
def toMainAfterSelectLocation(location,retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
        toMainAfterSelectLocationData['order_ctr_material_table_VisibleFirstRowKey'] = location['VisibleFirstRowKey']
        chk = location['index']+'-'+location['VisibleFirstRowKey']
        selectedRow = 'order_ctr_material_table_selrow_'+chk
        toMainAfterSelectLocationData['order_ctr_material_table-chk'] = chk
        toMainAfterSelectLocationData['order_ctr_material_table-prevchk-chk'] = chk
        toMainAfterSelectLocationData['order_ctr_material_table__selectedRow'] = selectedRow
#        selectLocationTableData['htmlbevt_par1'] = location['part1']
        checkBody = encode_multipart_formdata(boundry2,toMainAfterSelectLocationData)
        searchHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url,checkBody, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return toMainAfterSelectLocation(location,retry = retry-1)

def crawlSearchMoqMain(keyWord,content,part,nextKeyWord = 'EA',retry = 3):
    try:
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
        searchHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url,checkBody, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSearchMoqMain(keyWord,content,part,nextKeyWord = 'EA',retry = retry-1)
    
def deleteAllCrawl(datalist,retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
        
#        for data in datalist:
#            deleteAllTableData[str(data.aog)] = "00"
#            deleteAllTableData[str(data.tail)] = ""
        deleteAllTableData["order_ctr_basket_table_RowCount"] = str(len(datalist))
        checkBody = encode_multipart_formdata(boundry2,deleteAllTableData)
        deleteHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url,checkBody, headers=deleteHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return deleteAllCrawl(datalist,retry = retry-1)
    
def selectAll(index,part,clientInquiryId,id):
    time.sleep(4)
    text = crawlSearchMain(part['pn'])
    danger = text.find('alert(\'Dangerous goods additional packing fee may apply\');')
    if danger > 0:
        addDangerRecord(clientInquiryId,str(part['id']))
    select = text.find('Choose Material')
    if select > 0:
        elementDic = getSelectElement(text)
        pars = parseSelectInfo('',text,part['pn'],clientInquiryId)
        if len(pars) > index:
            selectContent = crawlSelectMain(elementDic,pars[index])
            soup = BeautifulSoup(selectContent)
            soup.findAll('table')
            partNumber = getEnterPartNumber(selectContent)
            quantity = selectContent.find('order quantity')
            rounded  = selectContent.find('rounded to')
            if selectContent.find('order quantity') > 0:
                crawlSearchMoqMain('quantity',selectContent,partNumber)
            elif selectContent.find('rounded to') > 0:
                crawlSearchMoqMain('rounded',selectContent,partNumber)
            else :
                warn = getReason(selectContent)
                if warn:
                    insertNoResult(part['pn'],id,"0",warn)

def crawlSelectLocationTable(par1,retry = 3):
    try:
        url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
        selectLocationFormData['htmlbevt_par1'] = par1
        checkBody = encode_multipart_formdata(boundry2,selectLocationFormData)
        searchHeaders["Content-Length"] = str(len(checkBody))
        result = session.post(url,checkBody, headers=searchHeaders, verify=False,timeout=90)
        return result.text
    except Exception,ex:
        if retry < 1:
            return "nothing"
        return crawlSelectLocationTable(par1,retry = retry-1)
    
def selectLocationInfo(content):
    import datetime
    soup = BeautifulSoup(content)
    choose = content.find('Choose Plant')
    if choose > 0:
        table = soup.findAll('table',{'style':'empty-cells:show;'})
        if len(table)==0:
            raise Exception('No table fond for detail info')
        
        trs = table[0].findAll('tr')
        infos = list()
        
        
        for index in range(len(trs)):
            tds = trs[index].findAll('td')
            if len(tds) > 1:
                plant = getRowValue(tds[8]).split(" ")
                #list = [u'SATAIR', u'A/S', u'Copenhagen']
                leadtime = getRowValue(tds[3]).replace(".","-")
                for lo in plant:
                    if str(lo) == 'Singapore' or str(lo) == 'Copenhagen' or str(lo) == 'UK' or str(lo) == 'Miami' or str(lo) == 'Ashburn':
                        l = {'part1':tds[0].findAll('button',{'class':'urSTRowUnSelIcon'})[0].attrs['onclick'].split("'")[-2],'plant':lo,'leadtime':leadtime}
                        infos.append(l)
                #l = {'part1':tds[0].findAll('button',{'class':'urSTRowUnSelIcon'})[0].attrs['onclick'].split("'")[-2],'plant':plant[len(plant)-1],'leadtime':leadtime}
                #infos.append(l)
        if (infos):
            index = 10
            loca = ""
            plant = ""
            leadtime = infos[0]['leadtime']
            leadtimeDateTime =  time.strptime(infos[0]['leadtime'], "%d-%m-%Y")
            sameLeadtime = 0
            for info in infos:
                if  time.mktime(time.strptime(info['leadtime'], "%d-%m-%Y"))<time.mktime(leadtimeDateTime):
                    leadtime = info['leadtime']
                    leadtimeDateTime = time.strptime(leadtime, "%d-%m-%Y")
            minList = list()
            for info in infos:
                if info['leadtime'] == leadtime:
                    minList.append(info)
            if len(minList) == 1:
                loca = str(minList[0]['part1'])
                plant = str(minList[0]['plant'])
                leadtime = str(minList[0]['leadtime'])
            else:
                for info in minList:
                    distance = int(getLocationDistance(str(info['plant']))[0])
                    if distance<index:
                        index = distance
                        loca = str(info['part1'])
                        plant = str(info['plant'])
                        leadtime = str(info['leadtime'])
            l = {'part1':loca,'plant':plant,'leadtime':leadtime}
            colNum = infos.index(l)+1
            VisibleFirstRowKey = soup.findAll('input',{'name':'order_ctr_material_table_VisibleFirstRowKey'})[0].attrs['value']
            location = {'part1':loca,'VisibleFirstRowKey':VisibleFirstRowKey,'index':str(colNum)}
            return location
        
    
def changeLocation(content):
    soup = BeautifulSoup(content)
#    part = content.find('value="HA30-0188406"')
    #print time.strftime("%Y-%m-%d %H:%M:%S")+'begin satair getting message!'
    choose = content.find('Choose Material')
    choose = content.find('Your shopping basket is empty')
    test = content.find('I agree to Satair')
    if choose < 0:
        table = soup.findAll('table',{'id':'order_ctr_basket_table'})
        if len(table)==0:
            #raise Exception('No table fond for detail info')
            return ""
        tbodys = table[0].findAll('tbody')
        trs = tbodys[0].findAll('tr')
        tds = trs[0].findAll('td')
        formTable = tds[0].findAll('table')
        fromTrs = formTable[0].findAll('tr')
        warn = soup.findAll('span',{'id':'order_ctr_messagebar_main-txt'})
        len(fromTrs)
        for tr in fromTrs:
            if len(tr.findAll('span',{'style':'white-space:nowrap;'})) > 0:
                rr = tr.attrs['rr']
                if len(rr) > 1:
                    text = crawlSelectLocationTable(rr,retry = 3)
                    location = selectLocationInfo(text)
                    crawlSelectLocationMain(location)
                    toMainAfterSelectLocation(location)

def getReason(text):
    soup = BeautifulSoup(text)
    warn = soup.findAll('font',{'color':'red'})
    if len(warn) > 0:
        return warn[0].get_text()

def parseDetailInfo(infoList,content,ifDanger):
    soup = BeautifulSoup(content)
#    part = content.find('value="HA30-0188406"')
    #print time.strftime("%Y-%m-%d %H:%M:%S")+'begin satair getting message!'
    choose = content.find('Choose Material')
    choose = content.find('Your shopping basket is empty')
    test = content.find('I agree to Satair')
    if choose < 0:
        table = soup.findAll('table',{'id':'order_ctr_basket_table'})
        if len(table)==0:
            #raise Exception('No table fond for detail info')
            return ""
        
        tbodys = table[0].findAll('tbody')
        trs = tbodys[0].findAll('tr')
        tds = trs[0].findAll('td')
        formTable = tds[0].findAll('table')
        fromTrs = formTable[0].findAll('tr')
        warn = soup.findAll('span',{'id':'order_ctr_messagebar_main-txt'})
        len(fromTrs)
        #print fromTrs
        for tr in fromTrs:
            tds = tr.findAll('td')
            if len(tds) >= 16:
                sourcePn = getRowValue(tds[0])
                enterPn = ''
                if sourcePn is not None and sourcePn != "":
                    enterPn = sourcePn.split(":")[0]
                pn = getRowValue(tds[1])
                cage_code = getRowValue(tds[2])
                description = getRowValue(tds[3])
                amount = getRowValue(tds[4])
                unit = getRowValue(tds[5])
                unit_price = getRowValue(tds[6]).replace('.','').replace(',','.')
                currency =  getRowValue(tds[8])
                leadTime = getRowValue(tds[10])
                AOG = tds[11].findAll('select')[0].attrs['id']
                TAIL = tds[12].findAll('input')[0].attrs['id']
                plant =  getRowValue(tds[14])
                certification =  getRowValue(tds[15])
                if_danger = ""
                if(description.find('battery') >= 0 or description.find('BATTERY') >= 0 or description.find('Battery') >= 0):
                    if_danger = "1"
                if float(unit_price) > 0:
                    infoList.append(DetailInfo(pn, cage_code, description, unit,unit_price,currency,certification,amount,leadTime,plant,enterPn,ifDanger,AOG,TAIL))
                else:
                    infoList.append(DetailInfo(pn, cage_code, description, unit,str(0),currency,certification,amount,leadTime,plant,enterPn,ifDanger,AOG,TAIL))
        
        return infoList
    
def parseSelectInfo(desc,content,part,clientInquiryId):
    soup = BeautifulSoup(content)
    choose = content.find('Choose Material')
    keys = list()
    if choose > 0:
        table = soup.findAll('table',{'style':'empty-cells:show;'})
        if len(table)==0:
            raise Exception('No table fond for detail info')
        
        trs = table[0].findAll('tr')
        parList = list() 
        size = len(trs)
        
#        for tr in trs:
#            tds = tr.findAll('td')
#            if len(tds) > 1:
#                if getRowValue(tds[2]).upper() == desc.upper():
#                    text = tds[0].findAll('button',{'class':'urSTRowUnSelIcon'})[0].attrs['onclick'].split("'")
#                    return text[-2]
        
#        insertEmailList(part,clientInquiryId)
        for tr in trs:
            td = tr.findAll('td')
            if len(td) > 1:
                text = td[0].findAll('button',{'class':'urSTRowUnSelIcon'})[0].attrs['onclick'].split("'")
                keys.append(text[-2])
        return keys
        
def getEnterPartNumber(content):
    soup = BeautifulSoup(content)
    part = soup.findAll('input',{'name':'material_model_mfrpn'})[0].attrs['value']
    return part
    
                    
def getRowValue(td):
    return td.text.strip()

class DetailInfo(object):
    def __init__(self, pn, cage_code, description, unit,unit_price,currency,certification,amount,leadTime,plant,enterPn,if_danger,aog='0',tail='0'):
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
        self.if_danger = if_danger
        self.aog = aog
        self.tail = tail
        
class TestInfo(object):
    def __init__(self, pn, cage_code, description, unit,unit_price,currency,certification,amount,leadTime,plant,enterPn,if_danger):
        self['pn'] = pn
        self['cage_code'] = cage_code
        self['description'] = description
        self['unit'] = unit
        self['unit_price'] = unit_price
        self['currency'] = currency
        self['certification'] = certification
        self['amount'] = amount
        self['leadTime'] = leadTime
        self['plant'] = plant
        self['enterPn'] = enterPn
        self['if_danger'] = if_danger
        
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


    
def insertDB(data,satairQuoteId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_quote_element(part_number, cage_code, description, unit,unit_price,currency,certification,lead_time,plant,enter_partnumber,amount,satair_quote_id,if_danger) values('%s', '%s', '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s', '%s', '%s', '%s')"%(data.pn, data.cage_code, data.description, data.unit,data.unit_price,data.currency,data.certification,data.leadTime,data.plant,data.enterPn,data.amount,satairQuoteId,data.if_danger)
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def insertNoResult(pn,satairQuoteId,if_danger,remark):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into satair_quote_element(part_number,satair_quote_id,if_danger,remark) values('%s', '%s', '%s', '%s')"%(pn,satairQuoteId,if_danger,remark)
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
    elementDic = {'order_ctr_material_table_VisibleFirstRowKey':VisibleFirstRowKey}
    return elementDic

def addSatairQuote(clientInquiryId):
        conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
        cursor = conn.cursor()
        sql = "insert into satair_quote(client_inquiry_id) values('%s')"%(clientInquiryId)
        cursor.execute(sql)
        cursor.close()
        conn.commit()
        conn.close()
        #print 'create satair_quote complete!'
        
def addDangerRecord(clientInquiryId,clientInquiryElementId):
        conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
        cursor = conn.cursor()
        sql = "insert into satair_danger(client_inquiry_id,client_inquiry_element_id) values('%s','%s')"%(clientInquiryId,clientInquiryElementId)
        cursor.execute(sql)
        cursor.close()
        conn.commit()
        conn.close()
        #print 'create satair_quote complete!'
    
def selectByClientInquiryId(clientInquiryId):
        conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
        cursor = conn.cursor()
        sql = "select max(id) from satair_quote where client_inquiry_id = '%s'"%(clientInquiryId)
        cursor.execute(sql)
        id = cursor.fetchone()
        cursor.close()
        conn.commit()
        conn.close()
        return id
    
def updateStatus(id):
        conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
        cursor = conn.cursor()
        sql = "update satair_quote set complete = 1 where client_inquiry_id = '%s'"%(id)
        cursor.execute(sql)
        cursor.close()
        conn.commit()
        conn.close()

def getLocationDistance(location):
        conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
        cursor = conn.cursor()
        sql = "SELECT location.`CODE` FROM system_code location WHERE location.`VALUE` = '%s'"%(location)
        cursor.execute(sql)
        code = cursor.fetchone()
        cursor.close()
        conn.commit()
        conn.close()
        return code
    
def getSearchCountInAWeek(clientInquiryId):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "SELECT COUNT(*) FROM satair_quote_element sqe WHERE DATEDIFF(NOW(),sqe.UPDATE_DATETIME) <= 7 AND sqe.PART_NUMBER = '%s' ORDER BY sqe.ID DESC"%(clientInquiryId)
    cursor.execute(sql)
    id = cursor.fetchone()
    cursor.close()
    conn.commit()
    conn.close()
    return id

#a = {}
#partlist = list()
#a['pn'] = '998502-13'
#partlist.append(a)
#b = {}
#b['pn'] = 'SAS911-002A'
#partlist.append(b)
#c = {}
#c['pn'] = '14403-003C'
#partlist.append(c)
#default = crawlDefault()
#loginDo = crawlLoginDo()
#result = crawlLoginDoCheckBoxMultiPart()
#response = crawlLoginDoMultiPart()
##detailInfo2 = parseDetailInfo(response)
#mainContent = crawlMain()
#
##partlist.append(TestInfo('998502-13', '', '', '','','','','','','','',''))
##partlist.append(TestInfo('SAS911-002A', '', '', '','','','','','','','',''))
##partlist.append(TestInfo('14403-003C', '', '', '','','','','','','','',''))
##partlist.append(DetailInfo('1FA14012-8', '', 'Hydraulic Actuator', '','','','','','','','',''))
##partlist.append(DetailInfo('251A2420-7', '', 'Control Valve Assy', '','','','','','','','',''))
##partlist.append(DetailInfo('141A4810-14', '', 'Windshield', '','','','','','','','',''))
##partlist.append(DetailInfo('7800001-008', '', 'Valve Assy, Rinse Water', '','','','','','','','',''))
##partlist.append(DetailInfo('MXP147-5', '', 'Oxygen Mask', '','','','','','','','',''))
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
#    text = crawlSearchMain(part['pn'])
#    danger = text.find('alert(\'Dangerous goods additional packing fee may apply\');')
#    if danger > 0:
#        addDangerRecord(clientInquiryId,str(part['id']))
#    select = text.find('Choose Material')
#    if select > 0:
#        elementDic = getSelectElement(text)
#        pars = parseSelectInfo('',text,part['pn'],1)
#        if len(pars) > 0:
#            selectContent = crawlSelectMain(elementDic,pars[0])
#            partNumber = getEnterPartNumber(selectContent)
#            quantity = selectContent.find('order quantity')
#            rounded  = selectContent.find('rounded to')
#            #print type(selectContent)
#            if selectContent.find('order quantity') > 0:
#                crawlSearchMoqMain('quantity',selectContent,partNumber)
#            elif selectContent.find('rounded to') > 0:
#                crawlSearchMoqMain('rounded',selectContent,partNumber)
#            else :
#                warn = getReason(selectContent)
#                if warn:
#                    insertNoResult(part['pn'],1,"0",warn)
#            for index in range(len(pars)):
#                if index != 0:
#                    selectAll(index,part,1,111111)
#    quantity = text.find('order quantity')
#    rounded  = text.find('rounded to')
#    if text.find('order quantity') > 0:
#        crawlSearchMoqMain('quantity',text,part['pn'])
#    elif text.find('rounded to') > 0:
#        crawlSearchMoqMain('rounded',text,part['pn'])
#    else :
#        warn = getReason(text)
#        if warn:
#            insertNoResult(part['pn'],1,"0",warn)
#    
#    for i in range(0,1):
#        searchContent = crawlSearchByPageMain(i+1,1,len(partlist))
#        changeLocation(searchContent)
#    for i in range(0,1):
#        searchContent = crawlSearchByPageMain(i+1,1,len(partlist))
#        dataList = parseDetailInfo(infoList,searchContent)
#    if(dataList):
#        for detailInfo in dataList:
#            insertDB(detailInfo,2)
#    deleteAllCrawl(dataList)
#    dataList = list()
#    infoList = list()
            
    
