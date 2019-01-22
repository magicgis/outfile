import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

import falcon
import json
from wsgiref import simple_server
from com.naswork.rfq.common.utils import Configuration  
from com.naswork.rfq.common.restsimpleserver import RestRequestHandler
import satair3
import aviall
import partsbase
import ast
import math
import time
import datetime
import klx
import storemarket
import kapco
import re
import txtav
import traceback
from random import choice
from threading import Thread 
from onpostexcept import cId_insert
import smtplib  
from email.mime.text import MIMEText  
from email.header import Header
ROUTES = ['/crawSatair']
class MailService(object):
    def __init__(self, falconApi):
        self.logger = self._getLogger()
        self.logger.info("falcon init!")
        self.satairIndex = 0
        self.realIndex = 0
        self.satairList = list()
        self.index = 0
        self.quoteId = 0
        self.klxRealIndex = 0
        self.hand_id = ""
        self.partInfo = list()
        self.clientInquiryId = ""
        for route in ROUTES:
            falconApi.add_route(route, self)
            
    def on_get(self, req, resp):
        resp.status = falcon.HTTP_200
        title = unicode(req.params['title'],'utf-8')
        mailList = req.params['mailList'].split(',')
        print 'Receive req\nTitle:%s\MaiList:%s\n' % (title,mailList)
        for mailAddress in mailList:
            echoStr = 'echo "%s" | mail -s "%s" %s' % (title,title,mailAddress)
            print 'EchoStr:%s' %echoStr
            os.popen(echoStr)
        '''
        print 'Receive req, querystring:%s'%(req.query_string)
        print 'Receive req, params:%s'%(req.params)
        '''
    def on_post(self, req, resp):
        partlist = list()
        try:
            resp.status = falcon.HTTP_200
            #getMessage = req.params['partList']
            clientInquiryId= req.params['clientInquiryId']
            self.logger.info("begin clientInquiryId = "+clientInquiryId)
            bizTypeId= req.params['bizTypeId']
            clientCode= req.params['clientCode']
            quoteNumber= req.params['quoteNumber']
            supplier= req.params['supplier']
#            getMessage = getMessage.decode('UTF-8')
#            partlist = eval(getMessage)
            #partlist = json.loads(getMessage)
            partlist = satair3.getInquiryElement(clientInquiryId)
            self.logger.info(partlist)
#            partlist = partlist.decode("unicode-escape")
            print partlist
        except Exception,ex:
            self.logger.error(str(traceback.format_exc()))
            self.logger.error(str(Exception)+":"+str(ex))
            cId_insert(clientInquiryId)
        if supplier == "1":
                '''StoreMarket'''
#                storeMarketCraw = storeMarketThread(partlist,clientInquiryId,quoteNumber,self.logger)
#                storeMarketCraw.start()
                try:
                    now = datetime.datetime.now()
                    delta = datetime.timedelta(days=3)
                    n_days = now + delta
                    needBy = n_days.strftime('%m/%d/%Y')
                    default = storemarket.crawlDefault()
                    toLogin = storemarket.toLogin()
                    login = storemarket.crawlLoginDo()
                    login.find("purchaser@betterairgroup.com")
                    afterLogin = storemarket.afterLoginDo()
                    foo = [7,8,9,10,11,12,13]
                    for part in partlist:
        #                part['pn'] = part['pn'].decode("unicode-escape")
                        self.logger.info("storemarket search "+str(part['pn']))
                        search = storemarket.searchPart(part["pn"])
                        storemarket.getEachRow(search,part["pn"],self.quoteNumber,part["amount"],needBy,clientInquiryId,part["id"])
                        time.sleep(choice(foo))
                    self.logger.info("end storemarket clientinquiryid "+clientInquiryId)
                except Exception,ex:
                    storemarket.logout()
                    self.logger.error(str(traceback.format_exc()))
                    self.logger.error(str(Exception)+":"+str(ex))
#                try:
#                    default = partsbase.crawlDefault()
#                    login = partsbase.crawlLoginDo()
#                    success = login.find("We are sorry")
#                    if success > 0:
#                        partsbase.logoutInTest()
#                        login = partsbase.crawlLoginDo()
#                    foo = [4,5,6,7,8,12,13,14,15,16,17,18]
#                    for part in partlist:
##                        self.logger.info("partsbase search "+part["pn"].decode("unicode-escape"))
#                        #before = partsbase.beforeSearch(part["pn"])
#                        search = partsbase.searchPart(part["pn"].decode("unicode-escape"))
#                        jsondata = json.loads(search)
#                        result = jsondata["searchResults"]
#                        emails = list()
#                        for re in result:
#                            if re["condCode"]:
#                                if re["condCode"]=='OH' or re["condCode"]=='SV':
#                                    today = re["lastUpdateDate"]
#                                    d1 = datetime.datetime.strptime(today, '%m/%d/%Y')
#                                    now = datetime.datetime.now()
#                                    delta = now - d1
#                                    if delta.days<=30:
#                                        if re["email"]:
#                                            emails.append(re["email"])
#                        partsbase.insertData(emails,clientInquiryId,part)
#                        time.sleep(choice(foo))
#                    partsbase.logoutInTest()
#                    self.logger.info("end partbase clientinquiryid "+clientInquiryId)
#                except Exception,ex:
#                    partsbase.logoutInTest()
#                    self.logger.error(str(traceback.format_exc()))
#                    self.logger.error(str(Exception)+":"+str(ex))
        if (clientCode == "8" or clientCode == "9") and supplier == "0":
#            '''aviall'''
#            aviallCraw = aviallThread(partlist,clientInquiryId,self.logger)
#            aviallCraw.start()
#            '''klx'''
#            klxCraw = klxThread(partlist,clientInquiryId,self.logger)
#            klxCraw.start()
#            '''kapco'''
#            kapcoCraw = kapcoThread(partlist,clientInquiryId,self.logger)
#            kapcoCraw.start()
#            '''txtav'''
#            txtavCraw = txtavThread(partlist,clientInquiryId,self.logger)
#            txtavCraw.start()
            '''satair'''
            try:
                satair3.addSatairQuote(clientInquiryId)
                self.clientInquiryId = clientInquiryId
                tup = satair3.selectByClientInquiryId(clientInquiryId)
                id = int(tup[0])
                self.quoteId = id
                self.logger.info("create satair_quote complete!")
                self.logger.info("satair_quote id:"+str(id))
                default = satair3.crawlDefault()
                loginDo = satair3.crawlLoginDo()
                result = satair3.crawlLoginDoCheckBoxMultiPart()
                response = satair3.crawlLoginDoMultiPart()
                test = response.find('I agree to Satair')
                mainContent = satair3.crawlMain()
                test2 = mainContent.find('Use * for wildcard search in the Partno.')
                foo = [5,6,7,8,9,10]
                index = int(math.ceil(len(partlist)/float(10)))
                infoList = list()
                dataList = list()
                self.partInfo = partlist
                for ind,part in enumerate(partlist):
                    self.satairIndex = ind
                    self.realIndex = ind
                    tupRecord = satair3.getSearchCountInAWeek(part)
                    count = int(tupRecord[0])
                    if count > 0:
                        continue;
#                    part['pn'] = part['pn'].decode("gbk")
                    self.logger.info("satair search pn "+str(part['pn']))
                    text = satair3.crawlSearchMain(part['pn'])
                    danger = text.find('alert(\'Dangerous goods additional packing fee may apply\');')
                    ifDanger = 0
                    if danger > 0:
                        satair3.addDangerRecord(clientInquiryId,str(part['id']))
                        ifDanger = 1
                    select = text.find('Choose Material')
                    if select > 0:
                        elementDic = satair3.getSelectElement(text)
                        pars = satair3.parseSelectInfo('',text,part['pn'],clientInquiryId)
                        if len(pars) > 0:
                            selectContent = satair3.crawlSelectMain(elementDic,pars[0])
                            partNumber = satair3.getEnterPartNumber(selectContent)
                            quantity = selectContent.find('order quantity')
                            rounded  = selectContent.find('rounded to')
                            print type(selectContent)
                            if selectContent.find('order quantity') > 0:
                                satair3.crawlSearchMoqMain('quantity',selectContent,partNumber)
                            elif selectContent.find('rounded to') > 0:
                                satair3.crawlSearchMoqMain('rounded',selectContent,partNumber)
                            else :
                                warn = satair3.getReason(selectContent)
                                if warn:
                                    satair3.insertNoResult(part['pn'],id,"0",warn)
                            for index in range(len(pars)):
                                if index != 0:
                                    satair3.selectAll(index,part,clientInquiryId,id)
                        
                            #satair3.insertNoResult(part['pn'],id,"0",selectContent)
                    quantity = text.find('order quantity')
                    rounded  = text.find('rounded to')
                    if text.find('order quantity') > 0:
                        satair3.crawlSearchMoqMain('quantity',text,part['pn'])
                    elif text.find('rounded to') > 0:
                        satair3.crawlSearchMoqMain('rounded',text,part['pn'])
                    else :
                        warn = satair3.getReason(text)
                        if warn:
                            satair3.insertNoResult(part['pn'],id,"0",warn)
                    for i in range(0,1):
                        searchContent = satair3.crawlSearchByPageMain(i+1,1,len(partlist))
                        satair3.changeLocation(searchContent)
                    for i in range(0,1):
                        searchContent = satair3.crawlSearchByPageMain(i+1,1,len(partlist))
                        dataList = satair3.parseDetailInfo(infoList,searchContent,ifDanger)
                    if(dataList):
                        for detailInfo in dataList:
                            satair3.insertDB(detailInfo,id)
                    satair3.deleteAllCrawl(dataList)
                    dataList = list()
                    infoList = list()
                    time.sleep(choice(foo))
                satair3.updateStatus(clientInquiryId)
                satair3.LoginOut()
                self.logger.info("END satair_quote "+str(id))
            except Exception,ex:
                self.crawlByBreakPoint()

    def _getLogger(self):  
        import logging  
        import os  
        import inspect  
          
        logger = logging.getLogger('[satairFalcon]')  
          
        this_file = inspect.getfile(inspect.currentframe())  
        dirpath = os.path.abspath(os.path.dirname(this_file))  
        dirpath = dirpath+"\\log"
        handler = logging.FileHandler(os.path.join(dirpath, "satairservice.log"))  
          
        formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')  
        handler.setFormatter(formatter)  
          
        logger.addHandler(handler)  
        logger.setLevel(logging.INFO)  
          
        return logger
    

    def crawlByBreakPoint(self,retry = 1):
        try:
            foo = [5,7,9,11,12,13,14,15,16,17,18]
            currentList = self.partInfo[self.realIndex:]
            infoList = list()
            dataList = list()
            for ind,part in enumerate(currentList):
                self.satairIndex = ind
                tupRecord = satair3.getSearchCountInAWeek(part)
                count = int(tupRecord[0])
                if count > 0:
                    continue;
                self.logger.info("satair search pn "+str(part['pn']))
                text = satair3.crawlSearchMain(part['pn'])
                danger = text.find('alert(\'Dangerous goods additional packing fee may apply\');')
                if danger > 0:
                    satair3.addDangerRecord(self.clientInquiryId,str(part['id']))
                select = text.find('Choose Material')
                if select > 0:
                    elementDic = satair3.getSelectElement(text)
                    pars = satair3.parseSelectInfo('',text,part['pn'],self.clientInquiryId)
                    if len(pars) > 0:
                        selectContent = satair3.crawlSelectMain(elementDic,pars[0])
                        partNumber = satair3.getEnterPartNumber(selectContent)
                        quantity = selectContent.find('order quantity')
                        rounded  = selectContent.find('rounded to')
                        print type(selectContent)
                        if selectContent.find('order quantity') > 0:
                            satair3.crawlSearchMoqMain('quantity',selectContent,partNumber)
                        elif selectContent.find('rounded to') > 0:
                            satair3.crawlSearchMoqMain('rounded',selectContent,partNumber)
                        else :
                            warn = satair3.getReason(selectContent)
                            if warn:
                                satair3.insertNoResult(part['pn'],self.quoteId,"0",warn)
                        for index in range(len(pars)):
                            if index != 0:
                                satair3.selectAll(index,part,self.clientInquiryId,self.quoteId)
                quantity = text.find('order quantity')
                rounded  = text.find('rounded to')
                if text.find('order quantity') > 0:
                    satair3.crawlSearchMoqMain('quantity',text,part['pn'])
                elif text.find('rounded to') > 0:
                    satair3.crawlSearchMoqMain('rounded',text,part['pn'])
                else :
                    warn = satair3.getReason(text)
                    if warn:
                        satair3.insertNoResult(part['pn'],self.quoteId,"0",warn)
                
                for i in range(0,1):
                    searchContent = satair3.crawlSearchByPageMain(i+1,1,len(currentList))
                    satair3.changeLocation(searchContent)
                for i in range(0,1):
                    searchContent = satair3.crawlSearchByPageMain(i+1,1,len(currentList))
                    dataList = satair3.parseDetailInfo(infoList,searchContent)
                if(dataList):
                    for detailInfo in dataList:
                        satair3.insertDB(detailInfo,self.quoteId)
                satair3.deleteAllCrawl(dataList)
                dataList = list()
                infoList = list()
                time.sleep(choice(foo))
            satair3.updateStatus(self.clientInquiryId)
            satair3.LoginOut()
            self.logger.info("END satair_quote "+str(self.quoteId))
        except Exception,ex:
            if self.satairIndex == 0 and retry >= 2:
                self.logger.error(str(traceback.format_exc()))
                self.logger.error(str(Exception)+":"+str(ex))
                self.realIndex = self.realIndex + 1
                self.realIndex = self.realIndex + self.satairIndex
                retry = 0
            else:
                self.realIndex = self.realIndex + self.satairIndex
            if len(self.partInfo) > self.realIndex:
                retry = retry + 1
                self.crawlByBreakPoint(retry)
            else:
                satair3.updateStatus(self.clientInquiryId)
                satair3.LoginOut()


  
class aviallThread(Thread):  
    def __init__(self,partInfo,id,logging):  
        self.logger = logging
        Thread.__init__(self)
        self.partInfo = partInfo
        self.index = 0
        self.realIndex = 0
        self.id = id
        self.quoteId = 0
  
    def run(self):
        try:
            aviall.addAviallQuote(self.id)
            tup = aviall.selectByClientInquiryId(self.id)
            id = int(tup[0])
            self.quoteId = id
            self.logger.info("create aviall_quote complete!")
            self.logger.info("aviall_quote id:"+str(id))  
            foo = [5,7,9,11,12,13,14,15,16,17,18]
            for ind,part in enumerate(self.partInfo):
                self.index = ind
                self.realIndex = ind
                self.logger.info("aviall search pn "+str(part['pn'])) 
                infoList = list();
                default = aviall.crawlDefault()
                select = default.find('Login | Aviall')
                main = aviall.crawlLoginDo()
                check = main.find('My Account')
                result = aviall.crawlAfterLoginDo()
                check = result.find('Go with Aviall')
                price = aviall.getPrice(str(part['pn']))
                text = price.split("},")[1].decode('unicode-escape').replace("\\","") 
                ifPrice = aviall.ifHasPrice(text)
                #aviall.logout()
                default = aviall.crawlDefault()
                main = aviall.crawlLoginDo()
                result = aviall.crawlAfterLoginDo()
                clientInquiryElementId = "1"
                before = aviall.beforeSearch(str(part['pn']))
                search = aviall.crawlSearch(str(part['pn']))
                aviall.getNoResultMessage(str(part['pn']),str(id),str(part['id']),search)
                if ifPrice != "0":
                    choose = aviall.chooseSelect(search,part['description'],ifPrice)
                    select = aviall.crawlSelectAfterSearch(choose,str(part['pn']))
                    select1 = select.find('120 day lead time')
                    dataInfo = aviall.getData(select,infoList,str(part['id']),text,choose)
                    #aviallQuoteId = 1
                    for info in infoList:
                        aviall.insertDB(info,id)
                    aviall.logout()
                time.sleep(choice(foo))
            aviall.updateStatus(self.id)       
            self.logger.info("END aviall_quote "+str(id))
        except Exception,ex:
            self.crawlByBreakPoint()
#            aviall.logout()
#            self.logger.error(str(traceback.format_exc()))
#            self.logger.error(str(Exception)+":"+str(ex))
        
    def crawlByBreakPoint(self,retry = 1):
        try:
            foo = [5,7,9,11,12,13,14,15,16,17,18]
            currentList = self.partInfo[self.realIndex:]
            for ind,part in enumerate(currentList):
                self.index = ind
                self.logger.info("aviall search pn "+str(part['pn'])) 
                infoList = list();
                default = aviall.crawlDefault()
                select = default.find('Login | Aviall')
                main = aviall.crawlLoginDo()
                check = main.find('My Account')
                result = aviall.crawlAfterLoginDo()
                check = result.find('Go with Aviall')
                price = aviall.getPrice(str(part['pn']))
                text = price.split("},")[1].decode('unicode-escape').replace("\\","") 
                ifPrice = aviall.ifHasPrice(text)
                #aviall.logout()
                default = aviall.crawlDefault()
                main = aviall.crawlLoginDo()
                result = aviall.crawlAfterLoginDo()
                clientInquiryElementId = "1"
                before = aviall.beforeSearch(str(part['pn']))
                search = aviall.crawlSearch(str(part['pn']))
                aviall.getNoResultMessage(str(part['pn']),str(self.quoteId),str(part['id']),search)
                if ifPrice != "0":
                    choose = aviall.chooseSelect(search,part['description'],ifPrice)
                    select = aviall.crawlSelectAfterSearch(choose,str(part['pn']))
                    select1 = select.find('120 day lead time')
                    dataInfo = aviall.getData(select,infoList,str(part['id']),text,choose)
                    #aviallQuoteId = 1
                    for info in infoList:
                        aviall.insertDB(info,self.quoteId)
                    aviall.logout()
                time.sleep(choice(foo))
            aviall.updateStatus(self.id)       
            self.logger.info("END aviall_quote "+str(self.quoteId))
        except Exception,ex:
            if self.index == 0 and retry >= 2:
                self.logger.error(str(traceback.format_exc()))
                self.logger.error(str(Exception)+":"+str(ex))
                self.realIndex = self.realIndex + 1
            self.realIndex = self.realIndex + self.index
            if len(self.partInfo) > self.realIndex:
                retry = retry + 1
                self.crawlByBreakPoint(retry)
#            aviall.logout()
#            self.logger.error(str(traceback.format_exc()))
#            self.logger.error(str(Exception)+":"+str(ex))
    
class klxThread(Thread):  
    def __init__(self,partInfo,id,logging):  
        self.logger = logging
        Thread.__init__(self)
        self.partInfo = partInfo
        self.id = id
        self.index = 0
        self.realIndex = 0
        self.quoteId = 0
        self.hand_id = ""
  
    def run(self):
        try:
            klx.addKlxQuote(self.id)
            tup = klx.selectByClientInquiryId(self.id)
            id = int(tup[0])
            self.quoteId = id
            self.logger.info("create klx_quote complete!")
            self.logger.info("klx_quote id:"+str(id))  
            foo = [5,7,9,11,12,13,14,15,16,17,18]
            handler_id = klx.login_do()
            self.hand_id = handler_id
            for ind,part in enumerate(self.partInfo):
                self.index = ind
                self.realIndex = ind
#                part['pn'] = part['pn'].decode("unicode-escape")
                self.logger.info("klx search pn "+str(part['pn']))
                while True:
                    if klx.search_do(handler_id, part['pn'],str(id),str(part['id'])) != 'search_again':
                        break
                time.sleep(choice(foo))
            klx.log_out_do(handler_id)
            klx.updateStatus(self.id)       
            self.logger.info("END klx_quote "+str(id))
        except Exception,ex:
            self.crawlByBreakPoint()
#            klx.log_out_do(handler_id)
#            self.logger.error(str(traceback.format_exc()))
#            self.logger.error(str(Exception)+":"+str(ex))
            
    def crawlByBreakPoint(self,retry = 1):
        try:
            foo = [5,7,9,11,12,13,14,15,16,17,18]
            currentList = self.partInfo[self.realIndex:]
            for ind,part in enumerate(currentList):
                self.index = ind
                self.logger.info("klx search pn "+str(part['pn']))
                while True:
                    if klx.search_do(self.hand_id, part['pn'],str(self.quoteId),str(part['id'])) != 'search_again':
                        break
                time.sleep(choice(foo))
            klx.log_out_do(self.hand_id)
            klx.updateStatus(self.id)       
            self.logger.info("END klx_quote "+str(self.quoteId))
        except Exception,ex:
            if self.index == 0 and retry >= 2:
                self.logger.error(str(traceback.format_exc()))
                self.logger.error(str(Exception)+":"+str(ex))
                self.realIndex = self.realIndex + 1
                retry = 0
            self.realIndex = self.realIndex + self.index
            if len(self.partInfo) > self.realIndex:
                retry = retry + 1
                self.crawlByBreakPoint(retry)
            
class kapcoThread(Thread):  
    def __init__(self,partInfo,id,logging):  
        self.logger = logging
        Thread.__init__(self)
        self.partInfo = partInfo
        self.id = id
        self.index = 0
        self.realIndex = 0
        self.quoteId = 0
  
    def run(self):
        try:
            username = 'Tracy Chen'
            userid = 'purchaser@betterairgroup.com'
            userpass = 'Ccx111222'
            kapco.addKlxQuote(self.id)
            tup = kapco.selectByClientInquiryId(self.id)
            id = int(tup[0])
            self.quoteId = id
            self.logger.info("create kapco_quote complete!")
            self.logger.info("kapco_quote id:"+str(id))
            default = kapco.default_go()
            loginres = kapco.login_go(userid, userpass)
            kapco.checklogin(loginres)
            foo = [5,7,9,11,12,13,14,15,16,17,18]
            for ind,part in enumerate(self.partInfo):
                self.index = ind
                self.realIndex = ind
#                part['pn'] = part['pn'].decode("unicode-escape")
                self.logger.info("kapco search "+str(part['pn']))
                kapco.find_send(part['pn'])
                findpage = kapco.find_page()
                findjson = kapco.find_result()
                jsonres = json.loads(findjson)
                for partnum in jsonres:
                    partres = kapco.datahandle(partnum)
                    if len(partres) == 10:
                        kapco.save_1(partres,id,part['id'])
                    elif len(partres) == 2:
                        kapco.save_2(partres,id,part['id'])
                    else:
                        self.logger.info(part['pn']+' in kapco has no record')
                time.sleep(choice(foo))
            kapco.logout()
            kapco.updateStatus(self.id)
            self.logger.info("END kapco_quote "+str(id))
        except Exception,ex:
            self.crawlByBreakPoint()
#            kapco.logout()
#            self.logger.error(str(traceback.format_exc()))
#            self.logger.error(str(Exception)+":"+str(ex))
            
    def crawlByBreakPoint(self,retry = 1):
        try:
            foo = [5,7,9,11,12,13,14,15,16,17,18]
            currentList = self.partInfo[self.realIndex:]
            for ind,part in enumerate(currentList):
                self.index = ind
#                part['pn'] = part['pn'].decode("unicode-escape")
                self.logger.info("kapco search "+str(part['pn']))
                kapco.find_send(part['pn'])
                findpage = kapco.find_page()
                findjson = kapco.find_result()
                jsonres = json.loads(findjson)
                for partnum in jsonres:
                    partres = kapco.datahandle(partnum)
                    if len(partres) == 10:
                        kapco.save_1(partres,self.quoteId,part['id'])
                    elif len(partres) == 2:
                        kapco.save_2(partres,self.quoteId,part['id'])
                    else:
                        self.logger.info(part['pn']+' in kapco has no record')
                time.sleep(choice(foo))
            kapco.logout()
            kapco.updateStatus(self.id)
            self.logger.info("END kapco_quote "+str(self.quoteId))
        except Exception,ex:
            if self.index == 0 and retry >= 2:
                self.logger.error(str(traceback.format_exc()))
                self.logger.error(str(Exception)+":"+str(ex))
                self.realIndex = self.realIndex + 1
            self.realIndex = self.realIndex + self.index
            if len(self.partInfo) > self.realIndex:
                retry = retry + 1
                self.crawlByBreakPoint(retry)


class storeMarketThread(Thread):  
    def __init__(self,partInfo,id,quoteNumber,logging):  
        self.logger = logging
        Thread.__init__(self)
        self.partInfo = partInfo
        self.id = id
        self.quoteNumber = quoteNumber
  
    def run(self):
        try:
            now = datetime.datetime.now()
            delta = datetime.timedelta(days=3)
            n_days = now + delta
            needBy = n_days.strftime('%m/%d/%Y')
            default = storemarket.crawlDefault()
            toLogin = storemarket.toLogin()
            login = storemarket.crawlLoginDo()
            login.find("purchaser@betterairgroup.com")
            afterLogin = storemarket.afterLoginDo()
            foo = [7,8,9,10,11,12,13]
            for part in self.partInfo:
#                part['pn'] = part['pn'].decode("unicode-escape")
                self.logger.info("storemarket search "+str(part['pn']))
                search = storemarket.searchPart(part["pn"])
                storemarket.getEachRow(search,part["pn"],self.quoteNumber,part["amount"],needBy,self.id,part["id"])
                time.sleep(choice(foo))
            self.logger.info("end storemarket clientinquiryid "+self.id)
        except Exception,ex:
            storemarket.logout()
            self.logger.error(str(traceback.format_exc()))
            self.logger.error(str(Exception)+":"+str(ex))
            
class txtavThread(Thread):  
    def __init__(self,partInfo,id,logging):  
        self.logger = logging
        Thread.__init__(self)
        self.partInfo = partInfo
        self.id = id
        self.logoffdata = ''
        self.index = 0
        self.realIndex = 0
        self.quoteId = 0
  
    def run(self):
        try:
            username = 'Michelle'
            userid = 'michelle@betterairgroup.com'
            userpass = 'Beech061125'
            txtav.addTxtavQuote(self.id)
            tup = txtav.selectByClientInquiryId(self.id)
            id = int(tup[0])
            self.quoteId = id
            self.logger.info("create txtav_quote complete!")
            self.logger.info("txtav_quote id:"+str(id))
            defaultpage = txtav.default_go()
            loginstate = txtav.checklogin(defaultpage, username)
            if loginstate == 'offline':
                logindata = txtav.getlogindata(defaultpage)
                loginpage = txtav.login_go(logindata, userid, userpass)
#             loginstate = txtav.checklogin(loginpage, username)
#            if loginstate == 'offline':
#                txtav.pagerecorder(defaultpage, 'default.html')
#                txtav.pagerecorder(loginpage, 'login.html')
#                exit(0)
#            else:
#                logger(loginstate)
#                print loginstate
            
            foo = [7,8,9,10,11,12,13]
            searchpage = ''
            for ind,pn in enumerate(self.partInfo):
                self.index = ind
                self.realIndex = ind
#                pn['pn'] = pn['pn'].decode("gbk")
                time.sleep(choice(foo))
                self.logger.info("txtav search "+str(pn['pn']))
                partnum = pn['pn']
                nowpart = partnum
                searchpage = txtav.pn_search(partnum)
                if searchpage.find('Parts Search Results for:') >= 0 :
                    if searchpage.find('Sorry, no matches found for') >= 0:
                        nodetail_part = [partnum,'','','','','','Sorry, no matches found for '+partnum,1,0]
                        txtav.insertsql([nodetail_part],id,pn["id"])
                        continue
                    href = txtav.search_handle(searchpage,str(pn['pn']))
                    if href:
                        searchpage = txtav.get_res(href)
                        isreplace = '0'
                        handledparts = []
                        breakflag = False
                        while True:
                            parts = txtav.searchres_handle(searchpage, isreplace, partnum)
                            if len(parts) > 0:
                                txtav.insertsql(parts,id,pn["id"])
                            handledparts.append(nowpart)
                            params_rep = txtav.replace_check(searchpage)
                            if params_rep == 'god' :
                                break
                            nowpart = params_rep['[0].ChangeToNewPN']
                            for handledpart in handledparts:
                                if nowpart == handledpart:
                                    breakflag = True
                                    break
                            if breakflag:
                                break
                            isreplace = '1'
                            searchpage = txtav.get_replacepn(params_rep)
                        logoffdata = txtav.getlogoffdata(searchpage)
                        self.logoffdata = logoffdata
            logoffpage = txtav.logoff_go(self.logoffdata)
            txtav.updateStatus(self.id)
            self.logger.info("END txtav_quote "+str(id))
        except Exception,ex:
            self.crawlByBreakPoint()
#            txtav.logoff_go(self.logoffdata)
#            self.logger.error(str(traceback.format_exc()))
#            self.logger.error(str(Exception)+":"+str(ex))
            
    def crawlByBreakPoint(self,retry = 1):
        try:
            foo = [7,8,9,10,11,12,13]
            searchpage = ''
            currentList = self.partInfo[self.realIndex:]
            for ind,pn in enumerate(currentList):
                self.index = ind
#                pn['pn'] = pn['pn'].decode("gbk")
                time.sleep(choice(foo))
                self.logger.info("txtav search "+str(pn['pn']))
                partnum = pn['pn']
                nowpart = partnum
                searchpage = txtav.pn_search(partnum)
                if searchpage.find('Parts Search Results for:') >= 0 :
                    if searchpage.find('Sorry, no matches found for') >= 0:
                        nodetail_part = [partnum,'','','','','','Sorry, no matches found for '+partnum,1,0]
                        txtav.insertsql([nodetail_part],self.quoteId,pn["id"])
                        continue
                    href = txtav.search_handle(searchpage,str(pn['pn']))
                    if href:
                        searchpage = txtav.get_res(href)
                        isreplace = '0'
                        handledparts = []
                        breakflag = False
                        while True:
                            parts = txtav.searchres_handle(searchpage, isreplace, partnum)
                            if len(parts) > 0:
                                txtav.insertsql(parts,self.quoteId,pn["id"])
                            handledparts.append(nowpart)
                            params_rep = txtav.replace_check(searchpage)
                            if params_rep == 'god' :
                                break
                            nowpart = params_rep['[0].ChangeToNewPN']
                            for handledpart in handledparts:
                                if nowpart == handledpart:
                                    breakflag = True
                                    break
                            if breakflag:
                                break
                            isreplace = '1'
                            searchpage = txtav.get_replacepn(params_rep)
                        logoffdata = txtav.getlogoffdata(searchpage)
                        self.logoffdata = logoffdata
            logoffpage = txtav.logoff_go(self.logoffdata)
            txtav.updateStatus(self.id)
            self.logger.info("END txtav_quote "+str(self.quoteId))
        except Exception,ex:
            if self.index == 0 and retry >= 2:
                self.logger.error(str(traceback.format_exc()))
                self.logger.error(str(Exception)+":"+str(ex))
                self.realIndex = self.realIndex + 1
            self.realIndex = self.realIndex + self.index
            if len(self.partInfo) > self.realIndex:
                retry = retry + 1
                self.crawlByBreakPoint(retry)
                
class sendEmail():
    def sendExceptionEmail(self):
        user = "915736664@qq.com"
        pwd  = "ckplfetgvkkubcce"
        to   = "tanoy@naswork.com"
        msg = MIMEText("falconServer down")
        msg["Subject"] = "Exception"
        msg["From"]    = user
        msg["To"]      = to
        
        try:
            s = smtplib.SMTP_SSL("smtp.qq.com", 465)
            s.login(user, pwd)
            s.sendmail(user, to, msg.as_string())
            s.quit()
            print "Success!"
        except smtplib.SMTPException,e:
            print "Falied,%s"%e 


        
try:            
    app = falcon.API()
    c = Configuration('G:/GitLib/crm/python/conf/service.cfg')
    # c = Configuration('D:/javasoft/MyEclipse Professional 2014/workspace/crm/python/conf/service.cfg')
    conf = c.readConfig()
    servicehost = conf['service_host']
    serviceport = conf['satair_service_port']
    MailService(app)
    httpd = simple_server.make_server(servicehost, serviceport, app, handler_class=RestRequestHandler)
    httpd.serve_forever()
except Exception,ex:
    mail = sendEmail()
    mail.sendExceptionEmail()
    self.logger.error(str(traceback.format_exc()))
    self.logger.error(str(Exception)+":"+str(ex))

        