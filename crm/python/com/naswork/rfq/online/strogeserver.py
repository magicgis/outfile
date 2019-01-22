import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

import falcon
import json
from wsgiref import simple_server
from com.naswork.rfq.common.utils import Configuration  
from com.naswork.rfq.common.restsimpleserver import RestRequestHandler
import ast
import math
import time
from threading import Thread 
import json
import aviationpartsoutlet
from random import choice
ROUTES = ['/crawStroge']
class MailService(object):
    def __init__(self, falconApi):
        self.logger = self._getLogger()
        self.logger.info("crawStroge init!")
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
        try:
            resp.status = falcon.HTTP_200
            message = req.params['partList']
            clientInquiryId= req.params['clientInquiryId']
            partlist = eval(message)
            self.logger.info(partlist)
            foo = [3,4,5,6,7,8]
            for part in partlist:
                default = aviationpartsoutlet.crawlDefault()
                search = aviationpartsoutlet.crawlSearch(part['pn'])
                aviationpartsoutlet.getMessage(search,part)
#                if len(urls) > 0 :
#                    for url in urls:
#                        con = aviationpartsoutlet.getMessage(search)
#                        aviationpartsoutlet.beforescan(url)
#                        bytes = aviationpartsoutlet.scan1()
#                        location = "D:\CRM\Files\mis\download\\"+part['pn']+"-"+con+".pdf"
#                        aviationpartsoutlet.filerecorder(bytes, location)
#                else:
#                    con = aviationpartsoutlet.getMessage(search)
                time.sleep(choice(foo))
        except Exception,ex:
            import traceback
            self.logger.error(str(traceback.format_exc()))
            self.logger.error(str(Exception)+":"+str(ex))
        
    def _getLogger(self):  
        import logging  
        import os  
        import inspect  
          
        logger = logging.getLogger('[strogeFalcon]')  
          
        this_file = inspect.getfile(inspect.currentframe())  
        dirpath = os.path.abspath(os.path.dirname(this_file))  
        handler = logging.FileHandler(os.path.join(dirpath, "strogeservice.log"))  
          
        formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')  
        handler.setFormatter(formatter)  
          
        logger.addHandler(handler)  
        logger.setLevel(logging.INFO)  
          
        return logger
        
            
app = falcon.API()
c = Configuration('G:/GitLib/crm/python/conf/service.cfg')
conf = c.readConfig()
servicehost = conf['service_host']
serviceport = conf['stroge_service_port']
MailService(app)
httpd = simple_server.make_server(servicehost, serviceport, app, handler_class=RestRequestHandler)
httpd.serve_forever()

        