# -*- coding:utf-8 -*-
import os
import sys
import traceback
import shutil
import MySQLdb
import datetime
import time
from lydsjanalytic import *

DELTA_TIME = 60


def parseFile(filename):
    fl = list(open(filename))
    items = map(lambda x: x.strip('\n'), fl)
    filterList = filter(lambda l: len(l) > 1, items)
    return map(lambda x: x.split('|'), filterList)
def genRID(field):
    return field.replace(' ','').replace('-', '').replace(':','')

def parseRealtimeUserCount(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        insertSql = '''INSERT INTO raw_sftp_ssrs (recordedDateTime, recordIdentifier, 
        areaId, subscriberCount) values 
        '''
        values = list()
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            values.append('("%s", "%s", %s, %s)' % (row[0], genRID(row[0]), row[1], row[2]))
        if len(values) > 0:
            cur.execute(insertSql + ','.join(values))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        
        return True
        
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False

def parseOnehourNewUser(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        insertSql = '''INSERT INTO raw_sftp_yxsxzrs (recordedDateTime, recordIdentifier, 
        areaId, subscriberCount) values 
        '''
        values = list()
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            values.append('("%s", "%s", %s, %s)' % (row[0], genRID(row[0]), row[1], row[2]))
        if len(values) > 0:
            cur.execute(insertSql + ','.join(values))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        return True
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False
        
def parseRealtimeResidentTime(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        insertSql = '''INSERT INTO raw_sftp_sszl (recordedDateTime, recordIdentifier, areaId,
         stayTimeSpan, subscriberCount) values 
        '''
        values = list()
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            values.append('("%s", "%s", %s, %s, %s)' % (row[0], genRID(row[0]), row[1], row[2], row[3]))
        if len(values) > 0:
            cur.execute(insertSql + ','.join(values))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        return True
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False
        

def parseResidentTime(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        insertSql = '''INSERT INTO raw_sftp_qtzl (recordedDateTime, recordIdentifier, areaId, 
         stayTimeSpan, sourceScope, subscriberCount) values 
        '''
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        values = list()
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            values.append('("%s", "%s", %s, %s, %s, %s)' % (row[0], genRID(row[0]), row[1], row[2], row[3], row[4]))
        if len(values) > 0:
            cur.execute(insertSql + ','.join(values))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        return True
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False
        

def parseRealtimeUserOrigin(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        insertSql = '''INSERT INTO raw_sftp_ssrqly (recordedDateTime, recordIdentifier, areaId, 
         sourceScope, sourceName, subscriberCount) values 
        '''
        values = list()
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            values.append('("%s", "%s", %s, %s, "%s", %s)' % (row[0], genRID(row[0]), row[1], row[2], row[3], row[4]))
        if len(values) > 0:
            cur.execute(insertSql + ','.join(values))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        return True
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False
        

def parseAlldayUserOrigin(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        insertSql = '''INSERT INTO raw_sftp_qtrqly (recordedDateTime, recordIdentifier, areaId, 
         sourceScope, sourceName, subscriberCount) values 
        '''
        values = list()
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            values.append('("%s", "%s", %s, %s, "%s", %s)' % (row[0], genRID(row[0]), row[1], row[2], row[3], row[4]))
        if len(values) > 0:
            cur.execute(insertSql + ','.join(values))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        return True
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False
        
    return True      

def parseFiveminUserDestination(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        insertSql = '''INSERT INTO raw_sftp_wfzrqlx (recordedDateTime, recordIdentifier, areaId, 
         destinationScope, destinationName, subscriberCount) values 
        '''
        values = list()
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            values.append('("%s", "%s", %s, %s, "%s", %s)' % (row[0], genRID(row[0]), row[1], row[2], row[3], row[4]))
        if len(values) > 0:
            cur.execute(insertSql + ','.join(values))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        return True
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False
        
    return True      

def parseAlldayUserDestination(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        insertSql = '''INSERT INTO raw_sftp_qtrqlx (recordedDateTime, recordIdentifier, areaId, 
         destinationScope, destinationName, subscriberCount) values 
        '''
        values = list()
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            values.append('("%s", "%s", %s, %s, "%s", %s)' % (row[0], genRID(row[0]), row[1], row[2], row[3], row[4]))
        if len(values) > 0:
            cur.execute(insertSql + ','.join(values))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        return True
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False
        
    return True      

def parseUserFlowCount(pid, filename, conn, cur, tjList):
    try:
        content = parseFile(filename)
        inSql = '''INSERT INTO raw_sftp_sslr (recordedDateTime, recordIdentifier, areaId, 
            subscriberCount, timeOnly) values 
        '''
        outSql = '''INSERT INTO raw_sftp_sslc (recordedDateTime, recordIdentifier, areaId, 
            subscriberCount) values 
        '''
        minDate = datetime.datetime.now()
        maxDate = datetime.datetime(1970,1,1)
        inValues = list()
        outValues = list()
        for row in content:
            d = datetime.datetime.strptime(row[0], '%Y-%m-%d %H:%M:%S')
            if d > maxDate:
                maxDate = d
            if d < minDate:
                minDate = d
            inValues.append('("%s", "%s", %s, %s, "%s")' % (row[0], genRID(row[0]), row[1], row[3], row[0].split(" ")[1]))
            outValues.append('("%s", "%s", %s, %s)' % (row[0], genRID(row[0]), row[1], row[4]))
        if len(inValues) > 0:
            cur.execute(inSql + ','.join(inValues))
        else:
            print pid + 'No valid rows found for:', filename
        if len(outValues) > 0:
            cur.execute(outSql + ','.join(outValues))
            for tj in tjList:
                tj(pid, 'sftp', conn, cur, minDate.strftime('%Y-%m-%d %H:%M:%S'), maxDate.strftime('%Y-%m-%d %H:%M:%S'))
            conn.commit()
        else:
            print pid + 'No valid rows found for:', filename
        return True
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
        return False
        
    return True      

def sortCmp(f1, f2):
    f1ds = f1[f1.rfind('_')+1:f1.rfind('.')]
    f2ds = f2[f2.rfind('_')+1:f2.rfind('.')]
    if len(f1ds) == 8:
        f1dt = datetime.datetime.strptime(f1ds, '%Y%m%d')
    elif len(f1ds) == 10:
        f1dt = datetime.datetime.strptime(f1ds, '%Y%m%d%H%M')    
    else:
        f1dt = datetime.datetime.strptime(f1ds, '%Y%m%d%H%M%S')
    if len(f2ds) == 8:
        f2dt = datetime.datetime.strptime(f2ds, '%Y%m%d')
    elif len(f2ds) == 10:    
        f2dt = datetime.datetime.strptime(f2ds, '%Y%m%d%H%M')
    else:
        f2dt = datetime.datetime.strptime(f2ds, '%Y%m%d%H%M%S')
        
    if f1dt < f2dt:
        return -1
    elif f1dt > f2dt:
        return 0
    else:
        return 1
def parse(pid, ftpDir, doneDir):    
    connPara = {'host':'172.31.250.2','user':'root', 'passwd':'Abcde01!', 'db':'lydsj', 'charset':'utf8', 'port':3306}
    #connPara = {'host':'localhost','user':'test', 'passwd':'test', 'db':'recognize', 'charset':'utf8', 'port':3306}
    conn = MySQLdb.Connect(**(connPara))
    conn.ping(True)
    cur = conn.cursor()
    successCount = 0
    failureCount = 0
    potentialCount = 0 
    for root, dirs, files in os.walk(ftpDir, topdown=False):
        #order by time
        sortFiles = files.sort(sortCmp)
        for filename in files:
            if not filename.endswith('.csv'):
                continue
            rindex = filename.rfind('_')
            if rindex > 0:
                key = filename[:rindex]
                if key in fileNameDict:
                    func = fileNameDict[key]
                    tjList = updateDict[key]
                    srcfile = os.path.join(root, filename)
                    ctime = time.time()
                    ftime = os.path.getmtime(srcfile)
                    if ctime - ftime < DELTA_TIME:
                        potentialCount = potentialCount + 1 
                        print pid, 'Skip', srcfile, 'as it is suspected to be uploading ' 
                        continue
                    dstFile = os.path.join(doneDir, filename)
                    if func(pid, srcfile, conn, cur, tjList):
                        shutil.move(srcfile,dstFile)  
                        print pid + 'Success to handle ', srcfile
                        successCount = successCount + 1
                    else:
                        print pid + 'Fail to handle ', srcfile
                        failureCount = failureCount + 1
                else:
                    print pid + 'No function found for:', key
                    failureCount = failureCount + 1
    conn.close()
    n = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print pid + 'Finish @%s. Succeeds: %d, fail: %d, uploading (suspected): %d' % (n, successCount, failureCount, potentialCount)

fileNameDict = {
  'mzly_realtime_user_count':parseRealtimeUserCount,
  'mzly_Onehour_new_user':  parseOnehourNewUser,
  'mzly_realtime_resident_time': parseRealtimeResidentTime,
  'mzly_resident_time': parseResidentTime,
  'mzly_realtime_user_origin': parseRealtimeUserOrigin,
  'mzly_allday_user_origin': parseAlldayUserOrigin,
  'mzly_fivemin_user_destination': parseFiveminUserDestination,
  'mzly_Allday_user_destination': parseAlldayUserDestination,
  'user_flow_count': parseUserFlowCount
} 

updateDict = {
  'mzly_realtime_user_count':[],
  'mzly_Onehour_new_user':  [],
  'mzly_realtime_resident_time': [analyseSszl],
  'mzly_resident_time': [analyseQtzl],
  'mzly_realtime_user_origin': [analyseSsrqly],
  'mzly_allday_user_origin': [analyseQtrqly],
  'mzly_fivemin_user_destination': [],
  'mzly_Allday_user_destination': [analyseQtrqlx],
  'user_flow_count': [analyseSslr]
}
'''
updateDict = {
  'mzly_realtime_user_count':[],
  'mzly_Onehour_new_user':  [],
  'mzly_realtime_resident_time': [],
  'mzly_resident_time': [],
  'mzly_realtime_user_origin': [],
  'mzly_allday_user_origin': [],
  'mzly_fivemin_user_destination': [],
  'mzly_Allday_user_destination': [],
  'user_flow_count': []
}
'''
if __name__ == '__main__':
    pid = '[' + str(os.getpid()) + ']'
    n = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print pid, '=======Start of running @ %s ====' % n
    lockFile = ".lock"
    if os.path.exists(lockFile):
        print pid + 'Last launch of script is still running'
        sys.exit(0)
    try:
        f = open(lockFile, 'w')
        f.write(str(pid))
        f.close()
        ftpDir = sys.argv[1]
        doneDir = sys.argv[2]
        parse(pid, ftpDir, doneDir)
    except Exception:
        traceInfo = traceback.format_exc()
        print pid + traceInfo
    if os.path.exists(lockFile):
        os.remove(lockFile)    