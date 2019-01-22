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
ROUTES = ['/crawKlx']
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
        self.hand_id = ""
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
            #partlist = eval(getMessage)
            #partlist = json.loads(getMessage)
            partlist = klx.getInquiryElement(clientInquiryId)
            self.partInfo = partlist
            self.logger.info(partlist)
            getClientCode = klx.getClientCode(self.id)
            self.clientCode = str(getClientCode[0])
#            partlist = partlist.decode("unicode-escape")
        except Exception,ex:
            self.logger.error(str(traceback.format_exc()))
            self.logger.error(str(Exception)+":"+str(ex))
            cId_insert(clientInquiryId)
        try:
            if (clientCode == "8" or clientCode == "9" or clientCode == "70" or clientCode == "370") and supplier == "0":
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
                    tupRecord = klx.getSearchCountInAWeek(part['pn'])
                    count = int(tupRecord[0])
                    if count > 0:
                        self.logger.info(str(part['pn'] + " had crawl in a week"))
                        if self.clientCode == '-1':
                            klx.copyRecord(part['pn'],str(part['id']),id)
                        continue;
                    #part['pn'] = part['pn'].encode("UTF-8")
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
            
        
    def _getLogger(self):  
        import logging  
        import os  
        import inspect  
          
        logger = logging.getLogger('[klxFalcon]')  
          
        this_file = inspect.getfile(inspect.currentframe())  
        dirpath = os.path.abspath(os.path.dirname(this_file))  
        dirpath = dirpath+"\\log"
        handler = logging.FileHandler(os.path.join(dirpath, "klxservice.log"))  
          
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
                tupRecord = klx.getSearchCountInAWeek(part['pn'])
                count = int(tupRecord[0])
                if count > 0:
                    self.logger.info(str(part['pn'] + " had crawl in a week"))
                    if self.clientCode == '-1':
                        klx.copyRecord(part['pn'],str(part['id']),self.quoteId)
                    continue;
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
                retry = 0
            else:
                self.realIndex = self.realIndex + self.index
            if len(self.partInfo) > self.realIndex:
                retry = retry + 1
                self.crawlByBreakPoint(retry)
            else:
                klx.log_out_do(self.hand_id)
                klx.updateStatus(self.id)
                
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
    serviceport = conf['klx_service_port']
    MailService(app)
    httpd = simple_server.make_server(servicehost, serviceport, app, handler_class=RestRequestHandler)
    httpd.serve_forever()
except Exception,ex:
    mail = sendEmail()
    mail.sendExceptionEmail()
    self.logger.error(str(traceback.format_exc()))
    self.logger.error(str(Exception)+":"+str(ex))

        