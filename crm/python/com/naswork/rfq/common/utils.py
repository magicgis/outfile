'''

@author: eyaomai
'''
import sys
import logging
import logging.config
import traceback
import json


class Logging(object):

    LOGGER = logging.getLogger()
    LOGGER_NAME_DEFAULT = 'default'
    LOGGER_NAME_UT = 'ut'
    LOGGER_NAME_CRAWL = 'crawl'

    @staticmethod
    def initLogger(filename):
        logging.config.fileConfig(filename)
        Logging.LOGGER = Logging.getLogger(Logging.LOGGER_NAME_DEFAULT)
    @staticmethod
    def getLogger(name):
        return logging.getLogger(name)

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

import threading
import time    
class StoppableThread(threading.Thread):
    def __init__(self, defaultGran):
        super(StoppableThread, self).__init__()
        self.__shutDown = False
        self.__defaultGran = defaultGran
        self.setDaemon(True)
    
    def shutDown(self):
        self.__shutDown = True
    
    def looseSleep(self, interval):
        '''
        LooseSleep is to sleep with checking whether shutDown
        '''
        startTime = time.time()
        while (True):
            if time.time() - startTime >= interval:
                break
            if self.__shutDown is True:
                break
            time.sleep(self.__defaultGran)
            
    def isShutDown(self):
        return self.__shutDown

class OneTimeThread(threading.Thread):
    def __init__(self, func, paraDict, timeoutValue=None):
        self.__func = func
        self.__paraDict = paraDict
        self.__timeoutValue = timeoutValue
        super(OneTimeThread, self).__init__()
        self.setDaemon(True)

    def run(self):
        if self.__timeoutValue is not None:
            time.sleep(self.__timeoutValue)
        self.__func(**self.__paraDict)
    
import Queue
class RequestHandler(threading.Thread):
    '''
    Base class for request handling. The class that subclass to this is supposed to overwrite following functions:
    -- _handlRequest(self,request): request handling function
    -- _shutDown(self): shutdown to ensure all resource are released
    
    When initiate the instance, you are supposed to pass in a time value to define how fast the request can be responded
    the value defaults to 0.1 second.
    '''
    def __init__(self, sleep_time=0.1, priorityNum=1):
        super(RequestHandler, self).__init__()
        self.shutdownFlag = False
        self.__event = threading.Event()
        self.__requestQueueList = list()
        if priorityNum<=0:
            priorityNum = 1
        for i in range(0, priorityNum):
            self.__requestQueueList.append(Queue.Queue())

        self.__sleep_time = sleep_time
        self.logger = Logging.LOGGER
        self.setDaemon(True)

    def __stopHandler(self):
        self.shutdownFlag = True
        self.__event.set()

    def addRequest(self, request, priority=0):
        if priority >= len(self.__requestQueueList):
            priority = len(self.__requestQueueList) - 1
        self.__requestQueueList[priority].put(request)
        self.__event.set()
    
    def getQueueSize(self):
        return sum([q.qsize() for q in self.__requestQueueList])
    def _isEmpty(self):
        for queue in self.__requestQueueList:
            if not queue.empty():
                return False
        return True

    def _handleRequest(self, request):
        '''
        _handleRequest is protected function to be inherited by subclass
        The default implementation only handles shutdown action
        The subclass can firstly invoke its super, determine whether to go or not by
        the return value: true means the request has been handled by super, false means
        super not yet handle.
        '''
        if not 'action' in request:            
            return False
        action = request['action']
        
        if cmp(action, 'shutdown')==0:
            self._shutDown()
            return True
        return False
        
    def _shutDown(self):
        self.__stopHandler()
        
    def shutDown(self, force=False):
        if  force is True:
            self._shutDown()
            return
            
        request = dict()
        request['action']='shutdown'
        self.addRequest(request)

    def readConfig(self, json_config_file):
        c = Configuration(json_config_file)
        return c.readConfig()

    def run(self):
        while True:
            self.__event.wait()
            if self.shutdownFlag is True:
                return
            
            self.__event.clear()
            while self._isEmpty() is False:
                request = None
                currentQueue = None
                for queue in self.__requestQueueList:
                    try:
                        request = queue.get_nowait()
                        currentQueue = queue
                        break
                    except Queue.Empty:
                        continue
                if request is None:
                    continue
                try:
                    self._handleRequest(request)
                except Exception:
                    traceInfo = traceback.format_exc()
                    self.logger.error('Fail to handle request: %s',traceInfo)
                currentQueue.task_done()
            if self.__sleep_time>0:
                time.sleep(self.__sleep_time)

class Audit(threading.Thread):
    '''
    Audit general for all purpose to periodically waken up to perform a specific action, which
    is wrapped in json format parameter: {'action':'audit', 'parameter':<parameter>} and sent to its controller to handle
    the audit request.
    '''
    def __init__(self, controller, interval, parameter=None, onetime=False, priority=0):
        super(Audit, self).__init__()
        self.__controller = controller
        self.interval = interval
        self.__shutDown = False
        self.__onetime = onetime
        self.__parameter = parameter
        self.__priority = priority
        self.finished = threading.Event()
        self.setDaemon(True)

    def shutDown(self):        
        self.__shutDown = True
        self.finished.set()
    
    def run(self):
        if self.__onetime:
            self.finished.wait(self.interval)
            if self.__shutDown is True:                
                return
            self.__doWork()
            return
            
        while(True):
            self.finished.wait(self.interval)
            if self.__shutDown is True:                
                return
            self.__doWork()
    
    def __doWork(self):
        request = dict()
        request['action']='audit'
        if self.__parameter is not None:
            request['parameter'] = self.__parameter
            #DEBUG only
            if self.__parameter == 'strategyCutoffAudit':
                try:
                    Logging.LOGGER.debug('strategyCutoffAudit dowork')
                    queuelist = self.__controller._RequestHandler__requestQueueList
                    queueInfo = [q.qsize() for q in queuelist]
                    Logging.LOGGER.debug('strategyCutoffAudit dowork, queueinfo:%s', queueInfo)
                except:
                    pass
        self.__controller.addRequest(request, self.__priority)                  

class MySqlProxy(StoppableThread):
    def __init__(self, host, port, user, passwd, db, interval=60*60, logger=None):
        import MySQLdb
        self.connPara = {'host':host,'user':user, 'passwd':passwd, 'db':db, 'charset':'utf8', 'port':port}
        self.conn = MySQLdb.Connect(**(self.connPara))
        self.conn.ping(True)
        self.cur=self.conn.cursor()
        self.__interval = interval
        self.logger = logger
        if self.logger is None:
            self.logger = Logging.LOGGER
        super(MySqlProxy, self).__init__(5)
        
    def run(self):
        while(True):
            self.looseSleep(self.__interval)
            if self.isShutDown():
                try:
                    self.close()
                    self.logger.info('Close Mysql connection')
                except Exception,e:
                    pass
                return
                           
            
    def execute(self, sql):
        try:
            return self.cur.execute(sql)
        except Exception, e:
            self.logger.error('ERROR sql:%s; Exception is %s', sql, str(e))
            raise Exception()
            return -1

    def commit(self):
        self.conn.commit()
        
    def rollback(self):
        self.conn.rollback()
    
    def close(self):
        try:
            self.conn.close()
            self.cur.close()
        except Exception:
            pass

import os
class PIDUtils(threading.Thread):
    def __init__(self, appname, stopFunc, sleep_time, logger=None):
        self.__appname = appname
        self.__stopFunc = stopFunc
        self.__sleep_time = sleep_time
        self.logger = logger
        if self.logger is None:
            self.logger = Logging.LOGGER
        super(PIDUtils, self).__init__()
    
    def run(self):
        while(True):
            #print 'checking pid:%s' % str(PIDUtils.isPidFileExist(self.__appname))
            if not PIDUtils.isPidFileExist(self.__appname):
                self.__stopFunc()
                self.logger.info('App:%s is shutdown', self.__appname)
                time.sleep(30)
                self.logger.info('Following thread is still active:%s', threading._active)
                return
            time.sleep(self.__sleep_time)
            
    @staticmethod
    def isPidFileExist(appname):
        return os.path.exists('./.'+appname+'.pid')
    @staticmethod
    def writePid(appname, pid):
        pidfile = open('./.'+appname+'.pid','w')
        pidfile.write(str(pid))
        pidfile.flush()
        pidfile.close()

import datetime
class UTValidateInfo(object):
    def __init__(self, funcName, para):
        self.funcName = funcName
        self.para = para


class DBInformation():
    def __init__(self,
                 dbHost   = 'localhost',
                 dbPort   = 3306,
                 dbUser   = 'stocklens',
                 dbPasswd = 'stocklens',
                 dbName   = 'stock'):
        self.dbHost   = dbHost
        self.dbPort   = dbPort
        self.dbUser   = dbUser
        self.dbPasswd = dbPasswd
        self.dbName   = dbName

    def toString(self):
        displayStr = '		Configuration Information: DB Information\n'
        displayStr = displayStr + '	%s: %s\n' % ('dbHost',  self.dbHost)
        displayStr = displayStr + '	%s: %s\n' % ('dbPort',  self.dbPort)
        displayStr = displayStr + '	%s: %s\n' % ('dbUser',  self.dbUser)
        displayStr = displayStr + '	%s: %s\n' % ('dbPasswd',self.dbPasswd)
        displayStr = displayStr + '	%s: %s\n' % ('dbName',  self.dbName)
        return displayStr
