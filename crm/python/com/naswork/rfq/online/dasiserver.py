# -*- coding: utf-8 -*-
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
import dasi
ROUTES = ['/crawDasi']
class MailService(object):
    def __init__(self, falconApi):
        self.logger = self._getLogger()
        self.logger.info("crawDasi init!")
        for route in ROUTES:
            falconApi.add_route(route, self)
        self.partInfo = ''
        self.index = 0
        self.realIndex = 0
        self.id = id
        self.quoteId = 0
            
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
            clientInquiryId = req.params['clientInquiryId']
            self.logger.info("begin clientInquiryId = " + clientInquiryId)
            clientCode = req.params['clientCode']
            quoteNumber = req.params['quoteNumber']
            supplier = req.params['supplier']
            partlist = dasi.getInquiryElement(clientInquiryId)
            self.logger.info(partlist)
            logger = self.logger
            dasi.insertDasi(clientInquiryId)
            tup = dasi.getLastInsert()
            id = int(tup[0])
            self.logger.info("Dasi "+str(id)+" start!")
            default = dasi.crawlDefault()
            dasi.doCrawl(partlist,id,logger,default,0)
            self.logger.info("Dasi "+str(id)+" finish!")
            dasi.updateStatus(id)
        except Exception,ex:
            self.logger.error(str(traceback.format_exc()))
            self.logger.error(str(Exception) + ":" + str(ex))
            
    def _getLogger(self):
        import logging
        import os
        import inspect
    
        logger = logging.getLogger('[Dasi]')
    
        this_file = inspect.getfile(inspect.currentframe())
        dirpath = os.path.abspath(os.path.dirname(this_file))
        dirpath = dirpath + "\\log"
        handler = logging.FileHandler(os.path.join(dirpath, "Dasi.log"))
    
        formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')
        handler.setFormatter(formatter)
    
        logger.addHandler(handler)
        logger.setLevel(logging.INFO)
        return logger
    
class errorLogger():
    def writeLogger(self,ex):
        import logging
        import os
        import inspect
    
        logger = logging.getLogger('[Dasi]')
    
        this_file = inspect.getfile(inspect.currentframe())
        dirpath = os.path.abspath(os.path.dirname(this_file))
        dirpath = dirpath + "\\log"
        handler = logging.FileHandler(os.path.join(dirpath, "Dasi.log"))
    
        formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')
        handler.setFormatter(formatter)
    
        logger.addHandler(handler)
        logger.setLevel(logging.ERROR)
        logger.error(ex)
try:            
    app = falcon.API()
    c = Configuration('G:/GitLib/crm/python/conf/service.cfg')
    conf = c.readConfig()
    servicehost = conf['service_host']
    serviceport = conf['dasi_service_port']
    MailService(app)
    httpd = simple_server.make_server(servicehost, serviceport, app, handler_class=RestRequestHandler)
    httpd.serve_forever()
except Exception,ex:
    logger = errorLogger()
    logger.writeLogger(str(traceback.format_exc()))
    logger.writeLogger(str(Exception)+":"+str(ex))

        