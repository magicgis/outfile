'''
Created on 7 Dec 2016

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.clean.common import CleanerCommon
from com.naswork.rfq.common.utils import Logging, PIDUtils

LOGGER_NAME_CLENER = 'abl'

PART_TYPE_STANDARD = 83
PART_TYPE_IP = 41
PART_TYPE_UNKNOWN = 0

class AirBusLocalClean(CleanerCommon):
    '''
    classdocs
    '''


    def __init__(self, confFile):
        '''
        Constructor
        '''
        super(AirBusLocalClean, self).__init__(confFile)
        self.logger = Logging.getLogger(LOGGER_NAME_CLENER)

    def _handleNextBatch(self):
        sql = '''
            SELECT TID, CHSESU, PART_NO, PART_NAME, PARENT_PART_NO, VENDOR_CODE
            FROM PART
        where 
            clean_flag=0 and tid>%d
        order by tid asc limit %d
            
        '''% (self.currentNo, self.batchSize)
        self.srcDbProxy.execute(sql)
        results = self.srcDbProxy.cur.fetchall()
        if len(results)==0:
            self.processFinish = True
            return
        finishFlag = False
        if len(results)<self.batchSize:
            finishFlag = True

        itemSet = set()
        for result in results:
            tid = result[0]
            self.currentNo = max(tid, self.currentNo)
            ataCode = result[1][:2]
            partNo = result[2]            
            partName = self.__transferPartName(result[3])
            parentPartNo = result[4]
            vendorCode = result[5]
            vendorCodePrefix = 0
            partType = PART_TYPE_UNKNOWN
            if vendorCode == 'VACRT':
                partType = PART_TYPE_STANDARD
                vendorCodePrefix = 1
            else:
                if partNo.startswith('NSA') or \
                    partNo.startswith('MS') or \
                    partNo.startswith('ASN') or \
                    partNo.startswith('NAS') or \
                    partNo.startswith('AN'):
                    partType = PART_TYPE_STANDARD                    
                else:
                    if partNo[0].isalpha() and partNo[1:].isdigit():
                        partType = PART_TYPE_IP                        
            
            if partType == PART_TYPE_UNKNOWN and vendorCode == '':
                #not process right now
                continue
            if partType == PART_TYPE_STANDARD:
                vendorCode = 'VACRT'
                vendorCodePrefix = 1
            elif partType == PART_TYPE_IP:
                vendorCode = 'ABZLJ'
                vendorCodePrefix = 1
            if len(vendorCode) == 6:
                vendorCode = vendorCode[1:]
                vendorCodePrefix = 0
            vendorCode = str(vendorCodePrefix) + vendorCode
            itemSet.add(Item(tid, ataCode, partNo, partName, vendorCode, partType))
        
        sql = 'select part_num, ata_code, cage_code from part_clean where '
        conditionList = list()
        for item in itemSet:
            conditionList.append('(part_num="%s" and ata_code="%s" and cage_code="%s")' % (
                                    item.partNo, item.ataCode, item.vendorCode))
        if len(conditionList) > 0:
            sql += ' or '.join(conditionList)            
            self.dstDbProxy.execute(sql)
            results = self.dstDbProxy.cur.fetchall()
            for result in results:
                itemSet.remove(Item(0, result[1], result[0], 0, result[2], 0))
            if len(itemSet)>0:
                sql = 'INSERT INTO part_clean (part_num, ata_code, cage_code, part_name, part_type) values '
                sql += ','.join(map(lambda item: '("%s", "%s", "%s", "%s", %d)' % (
                                            item.partNo,
                                            item.ataCode,
                                            item.vendorCode,
                                            item.partName,
                                            item.partType), itemSet))
                self.dstDbProxy.execute(sql)
                self.__updateOriginal(itemSet)
                
                self.dstDbProxy.commit()
                self.srcDbProxy.commit()

        if finishFlag:
            self.processFinish = True
                 
    def __updateOriginal(self, itemSet):
        updateSql = 'UPDATE part set clean_flag=1 where tid in (%s)' %(
                            ','.join(map(lambda item: str(item.tid), itemSet)))
        self.srcDbProxy.execute(updateSql)

    def __transferPartName(self, partName):
        partName = partName.replace('* LM *','')
        vendorCodeFound = False
        lindex= partName.find('(V')
        rindex = -1        
        if lindex >0:
            rindex = partName[lindex:].rfind(')') 
            if rindex > 0:
                vendorCode = partName[lindex: lindex+rindex+1]
                if vendorCode == '(VACRT)':
                    vendorCodeFound = True
                else:
                    if len(vendorCode)==8:
                        vendorCodeFound = True
                if vendorCodeFound:
                    partName = partName.replace(vendorCode, '')
        return partName.strip()
    
class Item(object):
    def __init__(self, tid, ataCode, partNo, partName, vendorCode, partType):
        self.tid = tid
        self.ataCode = ataCode
        self.partNo = partNo
        self.partName = partName
        self.vendorCode = vendorCode
        self.partType = partType                
    def __eq__(self, y):
        if self.ataCode == y.ataCode and self.partNo == y.partNo and self.vendorCode == y.vendorCode:
            return True
        return False
    def __hash__(self):
        return hash(self.ataCode) ^ hash(self.partNo) ^ hash(self.vendorCode)

if __name__ == '__main__':
    pid = os.getpid()
    PIDUtils.writePid(LOGGER_NAME_CLENER, pid)
    Logging.initLogger(os.path.join('conf','crawler.logging.win.cfg'))
    ins = AirBusLocalClean(os.path.join('conf',LOGGER_NAME_CLENER+'.cfg'))
    ins.start()
    pidutils = PIDUtils(LOGGER_NAME_CLENER, ins.shutDown, 5, ins.logger)
    pidutils.start()
    sys.exit(0)                                                