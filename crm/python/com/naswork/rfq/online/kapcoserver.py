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
ROUTES = ['/crawKapco']
class MailService(object):
    def __init__(self, falconApi):
        self.logger = self._getLogger()
        self.logger.info("crawStroge init!")
        for route in ROUTES:
            falconApi.add_route(route, self)
        self.partInfo = ''
        self.index = 0
        self.realIndex = 0
        self.id = id
        self.quoteId = 0
        self.clientCode = "0"
            
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
        try:
            resp.status = falcon.HTTP_200
            #getMessage = req.params['partList']
            clientInquiryId= req.params['clientInquiryId']
            self.logger.info("begin clientInquiryId = "+clientInquiryId)
            self.id = clientInquiryId
            bizTypeId= req.params['bizTypeId']
            clientCode= req.params['clientCode']
            quoteNumber= req.params['quoteNumber']
            supplier= req.params['supplier']
#            getMessage = getMessage.decode('UTF-8')
#            partlist = eval(getMessage)
            #partlist = json.loads(getMessage)
            partlist = kapco.getInquiryElement(clientInquiryId)
            self.partInfo = partlist
            self.logger.info(partlist)
            getClientCode = kapco.getClientCode(self.id)
            self.clientCode = str(getClientCode[0])
#            partlist = partlist.decode("unicode-escape")
        except Exception,ex:
            self.logger.error(str(traceback.format_exc()))
            self.logger.error(str(Exception)+":"+str(ex))
            cId_insert(clientInquiryId)
        try:
            if (clientCode == "8" or clientCode == "9") and supplier == "0":
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
                para = kapco.getLoginPara(default)
                loginres = kapco.login_go(userid, userpass,para)
                kapco.checklogin(loginres)
                foo = [5,7,9,11,12,13,14,15,16,17,18]
                for ind,part in enumerate(self.partInfo):
                    self.index = ind
                    self.realIndex = ind
                    tupRecord = kapco.getSearchCountInAWeek(part['pn'])
                    count = int(tupRecord[0])
                    if count > 0:
                        self.logger.info(str(part['pn'] + " had crawl in a week"))
                        if self.clientCode == '-1':
                            kapco.copyRecord(part['pn'],str(part['id']),id)
                        continue;
    #                part['pn'] = part['pn'].decode("unicode-escape")
                    self.logger.info("kapco search "+str(part['pn']))
                    para = kapco.getLoginPara(loginres)
                    kapco.find_send(part['pn'],para)
                    findpage = kapco.find_page()
                    findjson = kapco.find_result()
                    jsonres = json.loads(findjson)
                    for partnum in jsonres:
                        partres = kapco.datahandle(partnum)
                        if len(partres) >= 10:
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
            self.logger.error(str(traceback.format_exc()))
            self.logger.error(str(Exception)+":"+str(ex))
            self.crawlByBreakPoint()
            
            
        
    def _getLogger(self):  
        import logging  
        import os  
        import inspect  
          
        logger = logging.getLogger('[kapcoFalcon]')  
          
        this_file = inspect.getfile(inspect.currentframe())  
        dirpath = os.path.abspath(os.path.dirname(this_file))  
        dirpath = dirpath+"\\log"
        handler = logging.FileHandler(os.path.join(dirpath, "kapcoservice.log"))  
          
        formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')  
        handler.setFormatter(formatter)  
          
        logger.addHandler(handler)  
        logger.setLevel(logging.INFO)  
          
        return logger
    
    def crawlByBreakPoint(self,retry = 1):
        try:
            foo = [5,7,9,11,12,13,14,15,16,17,18]
            currentList = self.partInfo[self.realIndex:]
            for ind,part in enumerate(currentList):
                self.index = ind
                tupRecord = kapco.getSearchCountInAWeek(part['pn'])
                count = int(tupRecord[0])
                if count > 0 and self.clientCode == '-1':
                    self.logger.info(str(part['pn'] + " had crawl in a week"))
                    if self.clientCode == '-1':
                        kapco.copyRecord(part['pn'],str(part['id']),self.quoteId)
                    continue;
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
                retry = 0
            else:
                self.realIndex = self.realIndex + self.index
            if len(self.partInfo) > self.realIndex:
                retry = retry + 1
                self.crawlByBreakPoint(retry)
            else:
                kapco.logout()
                kapco.updateStatus(self.id)
                
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
    conf = c.readConfig()
    servicehost = conf['service_host']
    serviceport = conf['kapco_service_port']
    MailService(app)
    httpd = simple_server.make_server(servicehost, serviceport, app, handler_class=RestRequestHandler)
    httpd.serve_forever()
except Exception,ex:
    mail = sendEmail()
    mail.sendExceptionEmail()
    self.logger.error(str(traceback.format_exc()))
    self.logger.error(str(Exception)+":"+str(ex))

        