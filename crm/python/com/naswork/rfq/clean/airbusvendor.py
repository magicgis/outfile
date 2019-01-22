'''
Created on 2 Mar 2017

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils

LOGGER_NAME_CLENER = 'abv' 

class AirBusVendor(CleanerCommon):
    '''
    Import the airbus vendor into part 2.0
    '''


    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(AirBusVendor, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)
    
    def _handleNextBatch(self):
        sql = '''
            SELECT CAGE_CODE, CAGE_NAME, ADDRESS from VENDOR             
        '''
        self.srcDbProxy.execute(sql)
        resultList = self.srcDbProxy.cur.fetchall()
        if len(resultList)==0:
            self.processFinish = True
            return
        insertCount = 0
        updateCount = 0
        exitingCount = 0
        for result in resultList:
            self.currentNo+=1
            cageCode = result[0]
            cageName = result[1]
            newNameList = cageName.split(' ')
            sql = 'select man_name from t_manufactory where cage_code="%s"'%cageCode
            count = self.dstDbProxy.execute(sql)
            if count >0:
                exitingRecord = self.dstDbProxy.cur.fetchone()
                exitingName = exitingRecord[0]
                existingNameSet = set(exitingName.split(' '))
                noNew = True
                for item in newNameList:
                    if item not in existingNameSet:
                        exitingName+=' '+item
                        noNew = False
                if noNew == False:
                    sql = 'update t_manufactory set man_name="%s" where cage_code="%s"'% (exitingName.replace('"','\\"'), cageCode)
                    self.dstDbProxy.execute(sql)
                    updateCount+=1
                else:
                    exitingCount+=1
            else:
                msn='0-'+cageCode
                sql = '''insert t_manufactory (MSN, CAGE_CODE, MAN_NAME) VALUES
                ("%s", "%s", "%s")
                ''' %(msn, cageCode, cageName.replace('"','\\"'))
                self.dstDbProxy.execute(sql)
                insertCount+=1
        self.dstDbProxy.commit()
        self.processFinish = True
        self.logger.info('Totally processed %d. Insert:%d, Update:%d, Existing:%d', 
                         self.currentNo,
                         insertCount,
                         updateCount,
                         exitingCount)

if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CLENER, pid)
    Logging.initLogger(os.path.join('conf','crawler.logging.win.cfg'))
    ins = AirBusVendor(os.path.join('conf',LOGGER_NAME_CLENER+'.cfg'))
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CLENER, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                                