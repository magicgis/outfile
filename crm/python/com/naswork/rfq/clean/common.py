from com.naswork.rfq.common.utils import MySqlProxy,Logging,RequestHandler,OneTimeThread
import string
import time
import traceback
CONFIG_FILE_DBHOST = 'dbHost'
CONFIG_FILE_DBPORT = 'dbPort'
CONFIG_FILE_DBUSER = 'dbUser'
CONFIG_FILE_DBPASS = 'dbPasswd'
CONFIG_FILE_SRC_DBNAME = 'srcDbName'
CONFIG_FILE_DST_DBNAME = 'dstDbName'
CONFIG_FILE_BATCH_SIZE = 'batchSize'
CONFIG_FILE_START_NO = 'startNo'
CAGE_CHAR_SEQ = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
PUN_TABLE = string.maketrans("","")
class CleanerCommon(RequestHandler):
    CLEAN_FLAG_INIT = 0
    CLEAN_FLAG_CLEAN = 1
    CLEAN_FLAG_DUPLICATE = 2
    CLEAN_FLAG_PART_EXISTENCE = 3
    CLEAN_FLAG_MSN_EXISTENCE = 4
    CLEAN_FLAG_UNMATCH_PART_NAME = 5
    def __init__(self, confFile):
        '''
        Constructor
        '''
        self.logger = Logging.LOGGER
        self._readConfig(confFile)
        self.currentNo = self.startNo
        self.processFinish = False
        super(CleanerCommon, self).__init__()
    
    def _readConfig(self,confFile):
        className = self.__class__.__name__
        rindex = className.rfind('.')
        if rindex>=0:
            className = className[rindex+1:]
        self.config = self.readConfig(confFile)[className]
        self._dbHost = self.config[CONFIG_FILE_DBHOST]
        self._dbPort = self.config[CONFIG_FILE_DBPORT]
        self._dbUser = self.config[CONFIG_FILE_DBUSER]
        self._dbPasswd = self.config[CONFIG_FILE_DBPASS]
        self._srcDbName = self.config[CONFIG_FILE_SRC_DBNAME]
        self._dstDbName = self.config[CONFIG_FILE_DST_DBNAME]
        self.srcDbProxy = MySqlProxy(self._dbHost, self._dbPort, self._dbUser, self._dbPasswd, self._srcDbName, logger=self.logger) 
        self.dstDbProxy = MySqlProxy(self._dbHost, self._dbPort, self._dbUser, self._dbPasswd, self._dstDbName, logger=self.logger)
        self.batchSize = self.config[CONFIG_FILE_BATCH_SIZE]
        self.startNo = self.config[CONFIG_FILE_START_NO]
        
    def start(self):
        super(CleanerCommon, self).start()
        if self.srcDbProxy is not None:
            self.srcDbProxy.start()
        if self.dstDbProxy is not None:
            self.dstDbProxy.start()
        
        OneTimeThread(self.__process,{}).start()
        
    def _shutDown(self):
        super(CleanerCommon, self)._shutDown()
        if self.processFinish:
            self.__shutDownDb()
            
    def __shutDownDb(self):
        if self.srcDbProxy is not None:
            self.srcDbProxy.shutDown()
        if self.dstDbProxy is not None:
            self.dstDbProxy.shutDown()
        
    def __process(self):
        startTime = time.time()
        count = 0
        while self.processFinish is False and self.shutdownFlag is False:
            cStart = time.time()
            try:
                self._handleNextBatch()
            except Exception, e:
                try:
                    self.srcDbProxy.rollback()
                except Exception, e1:
                    self.logger.error('Fail to rollback src')
                
                try:
                    self.dstDbProxy.rollback()
                except Exception, e2:
                    self.logger.error('Fail to rollback dst')
                
                traceInfo = traceback.format_exc()
                self.logger.error('Fail to run batch:%s', traceInfo)
            count+=1
            currentTime = time.time()
            cSpeed = (currentTime - cStart)
            avgSpeed = (currentTime - startTime)/count
            self.logger.debug('Next StartNo: %d, Current speed: %s seconds. Avg speed: %s (seconds per batch)' % (self.currentNo, cSpeed, avgSpeed))
        self.logger.info('Done for running all')
        
    def _handleNextBatch(self):
        pass
    
    def generateMsn(self, cage_code, selfDefined):    
        if selfDefined:            
            prefix = '1'
        else:
            prefix = '0'
        
        return prefix+'-'+cage_code
    
    def generateSPN(self, partNum):
        for x in string.punctuation:
            partNum = partNum.replace(x,'')
        return partNum.replace(' ','')
    
    def generatePSNSeq(self, spnDict):
        spnList = ','.join(map(lambda x: '"'+x+'"', spnDict.keys()))
        sql = 'select spn,seq from s_spn where spn in (%s)' % spnList
        self.dstDbProxy.execute(sql)
        results = self.dstDbProxy.cur.fetchall()
        updateDict = dict()
        for result in results:
            spn = result[0]
            seq = result[1]
            nextSeq = spnDict[spn]['count']+seq
            spnDict[spn]['startSeq'] = seq + 1
            updateDict[spn]= nextSeq
        sql = 'delete from s_spn where spn in (%s)' % spnList
        self.dstDbProxy.execute(sql)
        sql = 'insert into s_spn (spn,seq) values '
        values = list()
        for key in spnDict:
            values.append('("%s",%d)' % (key, spnDict[key]['startSeq']+spnDict[key]['count']-1))
        sql = sql + ','.join(values)
        self.dstDbProxy.execute(sql)
    
    def generateCageCodeList(self, n=1):
        sql = 'select cage_code from s_cage_code'
        self.dstDbProxy.execute(sql)
        result = self.dstDbProxy.cur.fetchone()
        cageCodeList = list()
        cage_code = result[0]
        for i in range(n): 
            cage_code = self.__getNextCageCodeSeq(cage_code)
            cageCodeList.append(cage_code)
        sql = 'udpate s_cage_code set cage_code="%s"' % cageCodeList[-1]
        self.dstDbProxy.execute(sql)
        return cageCodeList
    
    def __getNextCageCodeSeq(self, cage_code):
        if cage_code == '':
            return '00000'
        indexList = []
        for charIndex in range(0,len(cage_code)):
            indexList.append(CAGE_CHAR_SEQ.index(cage_code[charIndex]))
        charIndex = len(indexList)-1
        while charIndex>=0:
            indexList[charIndex] +=1
            if indexList[charIndex] == len(CAGE_CHAR_SEQ):
                indexList[charIndex] = 0
                charIndex-=1
            else:
                break
        resultCageCode = ''
        for item in indexList:
            resultCageCode+=CAGE_CHAR_SEQ[item]
        return resultCageCode
    
    def genColValueStr(self, value):
        if value is None:
            return "null"
        else:
            return '"'+value.replace('"','\\"')+'"'
    
    def genColValueNum(self, value):
        if value is None or value=="":
            return "null" 
        else:
            return value   