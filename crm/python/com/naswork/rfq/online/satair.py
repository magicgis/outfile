'''
Created on 11 Feb 2017

@author: eyaomai
'''
import urllib2
import cookielib
from poster.encode import multipart_encode,MultipartParam
from poster.streaminghttp import register_openers
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
opener.add_handler(RedirctHandler)
boundry = '----WebKitFormBoundaryUykxyObQxRDUYdjh'
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
    'User-Agent': AGENT,
    'Host': "ecom.satair.com",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding": "gzip, deflate, sdch, br",
    "Accept-Language": "en,zh;q=0.8,zh-CN;q=0.6",
    "Connection": "keep-alive",
    "Upgrade-Insecure-Requests":1,
    "Referer":"https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do"   
}



def crawlDefault():
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/default.htm'
    request = urllib2.Request(url, None, HEADERS)  
    response = opener.open(request)
    return response.read()

def crawlLoginDo():
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
    request = urllib2.Request(url, None, HEADERS)
    response = opener.open(request)
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
    return response.read()

def crawlLoginDoMultiPart():
    loginUrl = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/public/login.do'
    checkBody = encode_multipart_formdata(boundry,formData)
    #dataGen, tmpHeaders = multipart_encode(formData)
    #tmpHeaders.update(loginHeaders)
    #request = urllib2.Request(loginUrl, dataGen, tmpHeaders)
    loginHeaders["Content-Length"] = len(checkBody)
    request = urllib2.Request(loginUrl, checkBody, loginHeaders)
    response = opener.open(request)
    return response
    #return response.read()

def crawlMain():
    url = 'https://ecom.satair.com/satair(bD1lbiZjPTM3MA==)/private/main.do'
    request = urllib2.Request(url, None, mainHeaders)  
    response = opener.open(request)
    return response.read()
    
default = crawlDefault()
loginDo = crawlLoginDo()
result = crawlLoginDoCheckBoxMultiPart()
response = crawlLoginDoMultiPart()