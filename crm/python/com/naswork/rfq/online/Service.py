#coding:utf-8
'''
Created on 2017.5.5

@author: Tanoy
'''
import os
import sys
#sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])


import win32serviceutil   
import win32service   
import win32event  
import win32com
import win32api
import MySQLdb
#from com.naswork.rfq.common.utils import *
class Configuration(object):
    '''
    Configuration helper to read config in json format dict
    Pass the json_file when instantiate it and then invoke readConfig to get a dict with configuration
    '''
    def __init__(self, json_file):
        self.__json_file = json_file

    def readConfig(self):        
        with open(self.__json_file) as f:
            json_string = ''.join(f)
            json_string =json_string.replace("\r","").replace("\t","").replace("\n", "")
            import json
            return json.loads(json_string)

  
class PythonService(win32serviceutil.ServiceFramework):   
    """ 
    Usage: 'PythonService.py [options] install|update|remove|start [...]|stop|restart [...]|debug [...]' 
    Options for 'install' and 'update' commands only: 
     --username domain\username : The Username the service is to run under 
     --password password : The password for the username 
     --startup [manual|auto|disabled|delayed] : How the service starts, default = manual 
     --interactive : Allow the service to interact with the desktop. 
     --perfmonini file: .ini file to use for registering performance monitor data 
     --perfmondll file: .dll file to use when querying the service for 
       performance data, default = perfmondata.dll 
    Options for 'start' and 'stop' commands only: 
     --wait seconds: Wait for the service to actually start or stop. 
                     If you specify --wait with the 'stop' option, the service 
                     and all dependent services will be stopped, each waiting 
                     the specified period. 
    """  
    #服务名
    _svc_name_ = "FalconService"  
    #服务在windows系统中显示的名称
    _svc_display_name_ = "Falcon Service"  
    #服务的描述
    _svc_description_ = "Crawer Service."  
  
    def __init__(self, args):   
        win32serviceutil.ServiceFramework.__init__(self, args)   
        self.hWaitStop = win32event.CreateEvent(None, 0, 0, None)  
        self.logger = self._getLogger()  
        self.isAlive = True  
          
    def _getLogger(self):  
        import logging  
        import os  
        import inspect  
          
        logger = logging.getLogger('[FalconService]')  
          
        this_file = inspect.getfile(inspect.currentframe())  
        dirpath = os.path.abspath(os.path.dirname(this_file))  
        handler = logging.FileHandler(os.path.join(dirpath, "service.log"))  
          
        formatter = logging.Formatter('%(asctime)s %(name)-12s %(levelname)-8s %(message)s')  
        handler.setFormatter(formatter)  
          
        logger.addHandler(handler)  
        logger.setLevel(logging.INFO)  
          
        return logger  
  
    def SvcDoRun(self):  
        #执行代码
        try:
            self.logger.info("svc do run....")
            from falconserver import MailService
            app = falcon.API()
            c = Configuration('G:/GitLib/crm/python/conf/service.cfg')
            conf = c.readConfig()
            servicehost = conf['service_host']
            serviceport = conf['service_port']
            MailService(app)
            httpd = simple_server.make_server(servicehost, serviceport, app, handler_class=RestRequestHandler)
            httpd.serve_forever()
        except Exception,ex:
            self.logger.error(str(Exception)+":"+str(ex))
        #falconserver.__init__()  
        # �ȴ����ֹͣ   
        #win32event.WaitForSingleObject(self.hWaitStop, win32event.INFINITE)   
              
    def SvcStop(self):   
        # 服务已经停止
        self.logger.error("svc do stop....")  
        self.ReportServiceStatus(win32service.SERVICE_STOP_PENDING)   
        # �����¼�   
        win32event.SetEvent(self.hWaitStop)   
        self.isAlive = False  
  
if __name__=='__main__':   
    win32serviceutil.HandleCommandLine(PythonService)  