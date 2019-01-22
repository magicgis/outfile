# -*- coding:utf-8 -*-
import os
import sys
import traceback
import MySQLdb
import datetime
import time

raw_table_mapping_http = {
  'qtrqly' : 'qtrqly',
  'ssrqly': 'ssrqly',
  'qtrqlx': 'qtrqlx',
  'qtzl': 'qtzl',
  'sslr': 'sslr',
  'sszl': 'sszl'
}
raw_table_mapping_sftp = {
  'qtrqly': 'raw_sftp_qtrqly',
  'ssrqly': 'raw_sftp_ssrqly',
  'qtrqlx': 'raw_sftp_qtrqlx',
  'qtzl': 'raw_sftp_qtzl',
  'sslr': 'raw_sftp_sslr',
  'sszl': 'raw_sftp_sszl'
}

tableMapping = {
  'http': raw_table_mapping_http,
 'sftp':raw_table_mapping_sftp                 
}

def updateHolidayStatistic(pid, conn, cur, rawStartTime, rawEndTime):
    #判断是否是节假日
    startDate = rawStartTime[:10]
    endDate = rawEndTime[:10]
    sql = '''
    SELECT holiday_type, holiday_start_date, holiday_end_date, holiday_year from holiday where
    (holiday_start_date <= "%s" and holiday_end_date>="%s") or 
    (holiday_start_date <= "%s" and holiday_end_date>="%s")
    ''' % (startDate, startDate, endDate, endDate)
    cur.execute(sql)
    results = cur.fetchall()
    if len(results) == 0:
        return
    for item in results:
        htype = item[0]
        hst = item[1].strftime('%Y-%m-%d')
        hed = item[2].strftime('%Y-%m-%d')
        hyear = item[3]
        print pid, 'Plan to update holiday statistic for',hst, hed
        daily_table = 'tj_lmlyrs_daily'
        holiday_table = 'tj_lmlyrs_holiday'
        _updateJdrsLmlyrsHoliday(daily_table, holiday_table, conn, cur, htype, hyear, hst, hed)
        daily_table = 'tj_jdrs_daily'
        holiday_table = 'tj_jdrs_holiday'
        _updateJdrsLmlyrsHoliday(daily_table, holiday_table, conn, cur, htype, hyear, hst, hed)
        daily_table = 'tj_kydfx_daily'
        holiday_table = 'tj_kydfx_holiday'        
        _updateTjKydfxHoliday(daily_table, holiday_table, conn, cur, htype, hyear, hst, hed)
        
def _updateJdrsLmlyrsHoliday(daily_table, holiday_table, conn, cur, htype, hyear, queryStartTime, queryEndTime):    
                
    sqlDaily = '''
    select id,  sum(subscriberCount), level
    from %s where recordDate>="%s" and recordDate<="%s" 
    group by id,  level
    '''%(daily_table, queryStartTime, queryEndTime)
    insertUpdateSql = '''
    INSERT INTO %s (id, holiday_year, holiday_type, subscriberCount, level) values %s 
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    cur.execute(sqlDaily)
    results = cur.fetchall()
    values = list()
    for item in results:
        values.append('(%d, %d, %d, %d, %d)' % (item[0], hyear, htype, item[1], item[2]))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (holiday_table, ','.join(values)))        

def _updateTjKydfxHoliday(daily_table, holiday_table, conn, cur, htype, hyear, queryStartTime, queryEndTime):
    #holiday 表
    sqlDaily = '''
    select id, sourceName, sourceScope, sum(subscriberCount), level
    from %s where recordDate>="%s" and recordDate<="%s" 
    group by id, sourceName, sourceScope, level
    '''%(daily_table, queryStartTime, queryEndTime)
    insertUpdateSql = '''
    INSERT INTO %s (id, holiday_year, holiday_type, sourceName, sourceScope, subscriberCount, level) values %s 
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    cur.execute(sqlDaily)
    results = cur.fetchall()
    values = list()
    for item in results:
        values.append('(%d, %d, %d, "%s", %d, %d, %d)' % (item[0], hyear, htype, item[1], item[2], item[3], item[4]))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (holiday_table, ','.join(values)))        

    
def analyseQtrqly(pid, srcType, conn, cur, rawStartTime=None, rawEndTime=None, isClear=True):
    if rawStartTime is None:
        rawStartTime = '2018-04-10 00:00:00'
    if rawEndTime is None:
        rawEndTime = datetime.datetime.now().strftime('%Y-%m-%d 00:00:00')
    rawTableKey = 'qtrqly'
    srcTableName = tableMapping[srcType][rawTableKey]
    print pid, 'Start analyzing qtrqly'
    #更新"景区-来梅旅游人数-按天", "景区-来梅旅游人数-按月"
    print pid, 'Start updating tj_lmlyrs: daily, monthly tables'
    updateTjLmlyrs(srcTableName, conn, cur, rawStartTime, rawEndTime)
    print pid, 'Start updating tj_jdrs: daily, monthly tables'
    updateTjJdrs(srcTableName, conn, cur, rawStartTime, rawEndTime)
    print pid, 'Start updating tj_kydfx: daily, monthly tables'
    updateTjKydfx(srcTableName, conn, cur, rawStartTime, rawEndTime)
    print pid, 'Start updating tj_kyddbfx'
    updateTjKyddbfx(srcTableName, conn, cur, rawStartTime, rawEndTime)
    updateHolidayStatistic(pid, conn, cur, rawStartTime, rawEndTime)

def analyseQtrqlx(pid, srcType, conn, cur, rawStartTime=None, rawEndTime=None, isClear=True):
    if rawStartTime is None:
        rawStartTime = '2018-04-10 00:00:00'
    if rawEndTime is None:
        rawEndTime = datetime.datetime.now().strftime('%Y-%m-%d 00:00:00')
    rawTableKey = 'qtrqlx'
    srcTableName = tableMapping[srcType][rawTableKey]
    print pid, 'Start analyzing qtrqlx'
    #
    print pid, 'Start updating tj_cmrs: daily, monthly tables'
    updateTjCmrs(srcTableName, conn, cur, rawStartTime, rawEndTime)
    

def analyseSsrqly(pid, srcType, conn, cur, rawStartTime=None, rawEndTime=None, isClear=True):
    if rawStartTime is None:
        rawStartTime = '2018-04-10 00:00:00'
    if rawEndTime is None:
        rawEndTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    rawTableKey = 'ssrqly'
    srcTableName = tableMapping[srcType][rawTableKey]
    print pid, 'Start analyzing ssrqly'
    #更新来梅旅游人数-实时
    print pid, 'Start updating tj_lmlyrs: realtime tables'
    updateTjLmlyrsRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear)
    print pid, 'Start updating tj_jdrs: realtime tables'
    updateTjJdrsRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear)
    print pid, 'Start updating tj_kydfx: realtime tables'
    updateTjKydfxRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear)

def analyseQtzl(pid, srcType, conn, cur, rawStartTime=None, rawEndTime=None, isClear=True):
    if rawStartTime is None:
        rawStartTime = '2018-04-10 00:00:00'
    if rawEndTime is None:
        rawEndTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    rawTableKey = 'qtzl'
    srcTableName = tableMapping[srcType][rawTableKey]
    print pid, 'Start analyzing qtzl'
    print pid, 'Start updating tj_ykdlsj: daily,monthly tables'
    updateTjYkdlsj(srcTableName, conn, cur, rawStartTime, rawEndTime)

def analyseSslr(pid, srcType, conn, cur, rawStartTime=None, rawEndTime=None, isClear=True):
    if rawStartTime is None:
        rawStartTime = '2018-04-10 00:00:00'
    if rawEndTime is None:
        rawEndTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    rawTableKey = 'sslr'
    srcTableName = tableMapping[srcType][rawTableKey]
    print pid, 'Start analyzing sslr'
    print pid, 'Start updating tj_ykrysj: daily,monthly tables'
    updateTjYkrysj(srcTableName, conn, cur, rawStartTime, rawEndTime)
    updateTjYkrysjRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear)

def analyseSszl(pid, srcType, conn, cur, rawStartTime=None, rawEndTime=None, isClear=True):
    if rawStartTime is None:
        rawStartTime = '2018-04-10 00:00:00'
    if rawEndTime is None:
        rawEndTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    rawTableKey = 'sszl'
    srcTableName = tableMapping[srcType][rawTableKey]
    print pid, 'Start analyzing sszl'
    print pid, 'Start updating tj_ykdlsj_realtime'
    updateTjYkdlsjRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear)

def updateTjYkrysjRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear):
    realTimeTable = 'tj_ykrysj_realtime'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    if isClear:
        #0. delete last 24 hour
        n = datetime.datetime.strptime(queryEndTime, '%Y-%m-%d %H:%M:%S') - datetime.timedelta(days=1)
        deleteSql = 'delete from %s where recordDateTime<>"%s"' % (realTimeTable + '_cur', rawEndTime)
        cur.execute(deleteSql)
    #_updateTjYkdlsjRealtime(srcTableName, conn, cur, queryStartTime, queryEndTime, realTimeTable)
    _updateTjYkrysjRealtimeCur(srcTableName, conn, cur, rawEndTime, realTimeTable + '_cur')

def _updateTjYkrysjRealtimeCur(srcTableName, conn, cur, currentTime, realTimeTable):
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.timeOnly, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime = "%s" 
    group by t2.id,t1.recordedDateTime,t1.timeOnly
    ''' % (srcTableName, currentTime)
    
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDatetime,timeOnly, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    
    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, 3, "%s")' % (item[0], recordedDateTime, item[2], item[3],  recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.timeOnly,sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime = "%s" 
        group by t2.districtId,t1.recordedDateTime,t1.timeOnly    
    '''% (srcTableName, currentTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, 2, "%s")' % (item[0], recordedDateTime, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.timeOnly ,sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime = "%s"  
        group by t1.recordedDateTime,t1.timeOnly
    '''% (srcTableName, currentTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, 1, "%s")' % (item[0], recordedDateTime, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))        


def updateTjYkdlsjRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear):
    realTimeTable = 'tj_ykdlsj_realtime'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    if isClear:
        #0. delete last 24 hour
        n = datetime.datetime.strptime(queryEndTime, '%Y-%m-%d %H:%M:%S') - datetime.timedelta(days=1)
        deleteSql = 'delete from %s where recordDateTime<="%s"' % (realTimeTable, n.strftime('%Y-%m-%d %H:%M:%S'))
        cur.execute(deleteSql)
        deleteSql = 'delete from %s where recordDateTime<>"%s"' % (realTimeTable + '_cur', rawEndTime)
        cur.execute(deleteSql)
    _updateTjYkdlsjRealtime(srcTableName, conn, cur, queryStartTime, queryEndTime, realTimeTable)
    _updateTjYkdlsjRealtimeCur(srcTableName, conn, cur, rawEndTime, realTimeTable + '_cur')

def _updateTjYkdlsjRealtime(srcTableName, conn, cur, queryStartTime, queryEndTime, realTimeTable):
    #按照景区、地区、市三级生成
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.stayTimeSpan, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime >= "%s" and recordedDateTime <= "%s" 
    group by t2.id,t1.recordedDateTime,t1.stayTimeSpan
    ''' % (srcTableName, queryStartTime, queryEndTime)
    
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDatetime,stayTimeSpan, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    
    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 3, "%s")' % (item[0], recordedDateTime, item[2], item[3],  recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.stayTimeSpan,sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t2.districtId,t1.recordedDateTime,t1.stayTimeSpan    
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 2, "%s")' % (item[0], recordedDateTime, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.stayTimeSpan ,sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t1.recordedDateTime,t1.stayTimeSpan
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 1, "%s")' % (item[0], recordedDateTime, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))        
    
def _updateTjYkdlsjRealtimeCur(srcTableName, conn, cur, currentTime, realTimeTable):
    #按照景区、地区、市三级生成
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.stayTimeSpan, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime = "%s" 
    group by t2.id,t1.recordedDateTime,t1.stayTimeSpan
    ''' % (srcTableName, currentTime)
    
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDatetime,stayTimeSpan, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    
    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 3, "%s")' % (item[0], recordedDateTime, item[2], item[3],  recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.stayTimeSpan,sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime = "%s" 
        group by t2.districtId,t1.recordedDateTime,t1.stayTimeSpan    
    '''% (srcTableName, currentTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 2, "%s")' % (item[0], recordedDateTime, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.stayTimeSpan ,sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime = "%s" 
        group by t1.recordedDateTime,t1.stayTimeSpan
    '''% (srcTableName, currentTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 1, "%s")' % (item[0], recordedDateTime, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))        
        
def updateTjYkrysj(srcTableName, conn, cur, rawStartTime, rawEndTime):
    daily_table = 'tj_ykrysj_daily'
    monthly_table = 'tj_ykrysj_monthly'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    #按照景区、地区、市三级生成
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.timeOnly, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime >= "%s" and recordedDateTime <= "%s" 
    group by t2.id,t1.recordedDateTime,t1.timeOnly
    ''' % (srcTableName, queryStartTime, queryEndTime)
    
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDate,timeOnly, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    
    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, 3, "%s")' % (item[0], recordDate, item[2], item[3],  recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.timeOnly,sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t2.districtId,t1.recordedDateTime,t1.timeOnly    
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, 2, "%s")' % (item[0], recordDate, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.timeOnly ,sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t1.recordedDateTime,t1.timeOnly
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, 1, "%s")' % (item[0], recordDate, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))        
    
    #1.4 monthy 表
    sqlDaily = '''
    select id, year(recordDate), month(recordDate), timeOnly, sum(subscriberCount), level
    from %s where recordDate>="%s" and recordDate<="%s" 
    group by id, year(recordDate), month(recordDate),timeOnly, level
    '''%(daily_table, queryStartTime[:7] +'-01', queryEndTime)
    insertUpdateSql = '''
    INSERT INTO %s (id, recordYear, recordMonth, timeOnly, subscriberCount, level, recordIdentifier) values %s 
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    cur.execute(sqlDaily)
    results = cur.fetchall()
    values = list()
    for item in results:
        if item[2] < 10:
            recordIdentifier = str(item[1]) + '0' + str(item[2]) +'00000000'
        else:
            recordIdentifier = str(item[1]) + str(item[2]) +'00000000'  
        values.append('(%d, %d, %d, "%s", %d, %d, "%s")' % (item[0], item[1], item[2], item[3], item[4], item[5], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (monthly_table, ','.join(values)))        
        
    #conn.commit()    
    
def updateTjLmlyrsRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear):
    realtime_table = 'tj_lmlyrs_realtime'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    if isClear:
        #0. delete last 24 hour
        n = datetime.datetime.strptime(queryEndTime, '%Y-%m-%d %H:%M:%S') - datetime.timedelta(days=1)
        deleteSql = 'delete from %s where recordDateTime<="%s"' % (realtime_table, n.strftime('%Y-%m-%d %H:%M:%S'))
        cur.execute(deleteSql)
    scope = 't1.sourceScope in (2,3,4)'
    _updateTjLmlyrsJdrsRealtime(srcTableName, conn, cur, queryStartTime, queryEndTime, isClear, realtime_table, scope)
    _updateTjLmlyrsJdrsRealtimeCur(srcTableName, conn, cur, queryEndTime, realtime_table+'_cur', scope)
    if isClear:
        #1. delete the ones which are not current 
        deleteSql = 'delete from %s where recordDateTime<>"%s"' % (realtime_table + '_cur', rawEndTime)
        cur.execute(deleteSql)

def updateTjJdrsRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear):
    realtime_table = 'tj_jdrs_realtime'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    if isClear:
        #0. delete last 24 hour for history
        n = datetime.datetime.strptime(queryEndTime, '%Y-%m-%d %H:%M:%S') - datetime.timedelta(days=1)
        deleteSql = 'delete from %s where recordDateTime<="%s"' % (realtime_table, n.strftime('%Y-%m-%d %H:%M:%S'))
        cur.execute(deleteSql)
    scope = 't1.sourceScope <> 5'
    _updateTjLmlyrsJdrsRealtime(srcTableName, conn, cur, queryStartTime, queryEndTime, isClear, realtime_table, scope)
    _updateTjLmlyrsJdrsRealtimeCur(srcTableName, conn, cur, queryEndTime, realtime_table + '_cur', scope)
    if isClear:
        #1. delete the ones which are not current 
        deleteSql = 'delete from %s where recordDateTime<>"%s"' % (realtime_table + '_cur', rawEndTime)
        cur.execute(deleteSql)

def _updateTjLmlyrsJdrsRealtimeCur(srcTableName, conn, cur, currentTime, realtime_table, scope):
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDateTime, subscriberCount, level, recordIdentifier, trend, alarm) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    #1.1 景区
    sqlArea = '''
    select tt.id, tt.recordedDateTime, tt.totalNum, 
    case 
        when t3.subscriberCount<>0 and t3.subscriberCount is not null
        then (tt.totalNum - t3.subscriberCount)/t3.subscriberCount
        else 0
    end as trend,
    case 
      when tt.totalNum/b.capacity > b.l2_threshold
      then 2 
      when tt.totalNum/b.capacity > b.l1_threshold
      then 1
      else 0
    end as alarm
    from (
    SELECT t2.id as id,t1.recordedDateTime as recordedDateTime, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 
    join jqqd t2 on (t1.areaId = t2.areaId)
    where %s and recordedDateTime = "%s"  
    group by t2.id,t1.recordedDateTime
    ) tt
    left join %s t3 on (t3.id=tt.id and t3.recordDateTime<>tt.recordedDateTime)
    left join bi_org_info b on b.id=tt.id

    ''' % (srcTableName, scope, currentTime, realtime_table)

    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", %d, 3, "%s", %f, %d)' % (item[0], recordedDateTime, item[2], recordIdentifier, item[3], item[4]))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realtime_table, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
    select tt.id, tt.recordedDateTime, tt.totalNum, 
    case 
        when t3.subscriberCount<>0 and t3.subscriberCount is not null
        then (tt.totalNum - t3.subscriberCount)/t3.subscriberCount
        else 0
    end as trend,
    case 
      when tt.totalNum/b.capacity > b.l2_threshold
      then 2 
      when tt.totalNum/b.capacity > b.l1_threshold
      then 1
      else 0
    end as alarm
    from (
        SELECT t2.districtId as id,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        
        where %s and recordedDateTime = "%s" 
        group by t2.districtId,t1.recordedDateTime
    )tt 
    left join %s t3 on (t3.id=tt.id and t3.recordDateTime<>tt.recordedDateTime)
    left join bi_org_info b on b.id=tt.id
    '''% (srcTableName, scope, currentTime, realtime_table)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", %d, 2, "%s", %f, %d)' % (item[0], recordedDateTime, item[2], recordIdentifier, item[3], item[4]))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realtime_table, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
    select tt.id, tt.recordedDateTime, tt.totalNum, 
    case 
        when t3.subscriberCount<>0 and t3.subscriberCount is not null
        then (tt.totalNum - t3.subscriberCount)/t3.subscriberCount
        else 0
    end as trend,
    case 
      when tt.totalNum/b.capacity > b.l2_threshold
      then 2 
      when tt.totalNum/b.capacity > b.l1_threshold
      then 1
      else 0
    end as alarm
    from (
        SELECT 1000 as id, t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where %s and recordedDateTime = "%s" 
        group by t1.recordedDateTime
    )tt
    left join %s t3 on (t3.id=tt.id and t3.recordDateTime<>tt.recordedDateTime)
    left join bi_org_info b on b.id=tt.id
    '''% (srcTableName, scope, currentTime, realtime_table)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", %d, 1, "%s", %f, %d)' % (item[0], recordedDateTime, item[2], recordIdentifier, item[3], item[4]))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realtime_table, ','.join(values)))
    
    
def _updateTjLmlyrsJdrsRealtime(srcTableName, conn, cur, queryStartTime, queryEndTime, isClear, realtime_table, scope):     
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDateTime, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where %s and recordedDateTime >= "%s" and recordedDateTime <= "%s" 
    group by t2.id,t1.recordedDateTime
    ''' % (srcTableName, scope, queryStartTime, queryEndTime)

    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", %d, 3, "%s")' % (item[0], recordedDateTime, item[2], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realtime_table, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where %s and recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t2.districtId,t1.recordedDateTime    
    '''% (srcTableName, scope, queryStartTime, queryEndTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", %d, 2, "%s")' % (item[0], recordedDateTime, item[2], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realtime_table, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where %s and recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t1.recordedDateTime 
    '''% (srcTableName, scope, queryStartTime, queryEndTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", %d, 1, "%s")' % (item[0], recordedDateTime, item[2], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realtime_table, ','.join(values)))
    
    #conn.commit()
    
    
def updateTjKydfxRealtime(srcTableName, conn, cur, rawStartTime, rawEndTime, isClear):
    realTimeTable = 'tj_kydfx_realtime'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    if isClear:
        #0. delete last 24 hour
        n = datetime.datetime.strptime(queryEndTime, '%Y-%m-%d %H:%M:%S') - datetime.timedelta(days=1)
        deleteSql = 'delete from %s where recordDateTime<="%s"' % (realTimeTable, n.strftime('%Y-%m-%d %H:%M:%S'))
        cur.execute(deleteSql)
        deleteSql = 'delete from %s where recordDateTime<>"%s"' % (realTimeTable + '_cur', rawEndTime)
        cur.execute(deleteSql)
    _updateTjKydfxRealtime(srcTableName, conn, cur, queryStartTime, queryEndTime, realTimeTable)
    _updateTjKydfxRealtimeCur(srcTableName, conn, cur, rawEndTime, realTimeTable + '_cur')

def _updateTjKydfxRealtimeCur(srcTableName, conn, cur, currentTime, realTimeTable):
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDateTime, sourceName, sourceScope, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.sourceName, t1.sourceScope, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime = "%s" 
    group by t2.id,t1.recordedDateTime, t1.sourceName, t1.sourceScope
    ''' % (srcTableName, currentTime)

    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", "%s", %d, %d, 3, "%s")' % (item[0], recordedDateTime, item[2], item[3], item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.sourceName, t1.sourceScope, sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where recordedDateTime = "%s" 
        group by t2.districtId,t1.recordedDateTime, t1.sourceName, t1.sourceScope    
    '''% (srcTableName, currentTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", "%s", %d, %d, 2, "%s")' % (item[0], recordedDateTime, item[2], item[3], item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.sourceName, t1.sourceScope, sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where recordedDateTime = "%s" 
        group by t1.recordedDateTime, t1.sourceName, t1.sourceScope 
    '''% (srcTableName, currentTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", "%s", %d, %d, 1, "%s")' % (item[0], recordedDateTime, item[2], item[3], item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
    
def _updateTjKydfxRealtime(srcTableName, conn, cur, queryStartTime, queryEndTime, realTimeTable):
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDateTime, sourceName, sourceScope, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.sourceName, t1.sourceScope, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime >= "%s" and recordedDateTime <= "%s" 
    group by t2.id,t1.recordedDateTime, t1.sourceName, t1.sourceScope
    ''' % (srcTableName, queryStartTime, queryEndTime)

    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", "%s", %d, %d, 3, "%s")' % (item[0], recordedDateTime, item[2], item[3], item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.sourceName, t1.sourceScope, sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t2.districtId,t1.recordedDateTime, t1.sourceName, t1.sourceScope    
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", "%s", %d, %d, 2, "%s")' % (item[0], recordedDateTime, item[2], item[3], item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.sourceName, t1.sourceScope, sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t1.recordedDateTime, t1.sourceName, t1.sourceScope 
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordedDateTime = item[1].strftime("%Y-%m-%d %H:%M:%S")
        recordIdentifier = item[1].strftime("%Y%m%d%H%M%S")
        values.append('(%d, "%s", "%s", %d, %d, 1, "%s")' % (item[0], recordedDateTime, item[2], item[3], item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (realTimeTable, ','.join(values)))
    
    #conn.commit()    
    
def updateTjYkdlsj(srcTableName, conn, cur, rawStartTime, rawEndTime):
    daily_table = 'tj_ykdlsj_daily'
    monthly_table = 'tj_ykdlsj_monthly'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    #按照景区、地区、市三级生成
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.stayTimeSpan, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime >= "%s" and recordedDateTime <= "%s" 
    group by t2.id,t1.recordedDateTime,t1.stayTimeSpan
    ''' % (srcTableName, queryStartTime, queryEndTime)
    
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDate,stayTimeSpan, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    
    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 3, "%s")' % (item[0], recordDate, item[2], item[3],  recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.stayTimeSpan,sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t2.districtId,t1.recordedDateTime,t1.stayTimeSpan    
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 2, "%s")' % (item[0], recordDate, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.stayTimeSpan ,sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t1.recordedDateTime,t1.stayTimeSpan
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 1, "%s")' % (item[0], recordDate, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))        
    
    #1.4 monthy 表
    sqlDaily = '''
    select id, year(recordDate), month(recordDate), stayTimeSpan, sum(subscriberCount), level
    from %s where recordDate>="%s" and recordDate<="%s" 
    group by id, year(recordDate), month(recordDate),stayTimeSpan, level
    '''%(daily_table, queryStartTime[:7] +'-01', queryEndTime)
    insertUpdateSql = '''
    INSERT INTO %s (id, recordYear, recordMonth, stayTimeSpan, subscriberCount, level, recordIdentifier) values %s 
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    cur.execute(sqlDaily)
    results = cur.fetchall()
    values = list()
    for item in results:
        if item[2] < 10:
            recordIdentifier = str(item[1]) + '0' + str(item[2]) +'00000000'
        else:
            recordIdentifier = str(item[1]) + str(item[2]) +'00000000'  
        values.append('(%d, %d, %d, %d, %d, %d, "%s")' % (item[0], item[1], item[2], item[3], item[4], item[5], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (monthly_table, ','.join(values)))        
        
    #conn.commit()    
    

def updateTjKydfx(srcTableName, conn, cur, rawStartTime, rawEndTime):
    #计算客源地分析-按天
    #计算客源地分析-按月
    daily_table = 'tj_kydfx_daily'
    monthly_table = 'tj_kydfx_monthly'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    #按照景区、地区、市三级生成
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.sourceName, t1.sourceScope, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime >= "%s" and recordedDateTime <= "%s" 
    group by t2.id,t1.recordedDateTime,t1.sourceName,t1.sourceScope
    ''' % (srcTableName, queryStartTime, queryEndTime)
    
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDate,sourceName, sourceScope, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    
    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, %d, 3, "%s")' % (item[0], recordDate, item[2], item[3],  item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.sourceName, t1.sourceScope,sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t2.districtId,t1.recordedDateTime,t1.sourceName, t1.sourceScope    
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, %d, 2, "%s")' % (item[0], recordDate, item[2], item[3],  item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.sourceName, t1.sourceScope,sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t1.recordedDateTime,t1.sourceName, t1.sourceScope
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", "%s", %d, %d, 1, "%s")' % (item[0], recordDate, item[2], item[3],  item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))        
    
    #1.4 monthy 表
    sqlDaily = '''
    select id, year(recordDate), month(recordDate), sourceName, sourceScope, sum(subscriberCount), level
    from %s where recordDate>="%s" and recordDate<="%s" 
    group by id, year(recordDate), month(recordDate),sourceName, sourceScope, level
    '''%(daily_table, queryStartTime[:7] +'-01', queryEndTime)
    insertUpdateSql = '''
    INSERT INTO %s (id, recordYear, recordMonth, sourceName, sourceScope, subscriberCount, level, recordIdentifier) values %s 
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    cur.execute(sqlDaily)
    results = cur.fetchall()
    values = list()
    for item in results:
        if item[2] < 10:
            recordIdentifier = str(item[1]) + '0' + str(item[2]) +'00000000'
        else:
            recordIdentifier = str(item[1]) + str(item[2]) +'00000000'  
        values.append('(%d, %d, %d, "%s", %d, %d, %d, "%s")' % (item[0], item[1], item[2], item[3], item[4], item[5], item[6], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (monthly_table, ','.join(values)))        
        
    #conn.commit()    

def updateTjJdrs(srcTableName, conn, cur, rawStartTime, rawEndTime):
    #计算daily_table，景区-接待人数-按天
    #计算monthly_table，景区-接待人数-按月
    daily_table = 'tj_jdrs_daily'
    monthly_table = 'tj_jdrs_monthly'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    scope = 't1.sourceScope <> 5'
    _updateJdrsLmlyrs(srcTableName, conn, cur, queryStartTime, queryEndTime, daily_table, monthly_table, scope)

def updateTjLmlyrs(srcTableName, conn, cur, rawStartTime, rawEndTime):    
    #计算daily_table，景区-来梅旅游人数-按天
    #计算monthly_table，景区-来梅旅游人数-按月
    daily_table = 'tj_lmlyrs_daily'
    monthly_table = 'tj_lmlyrs_monthly'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    scope = 't1.sourceScope in (2,3,4)'
    #按照景区、地区、市三级生成
    _updateJdrsLmlyrs(srcTableName, conn, cur, queryStartTime, queryEndTime, daily_table, monthly_table, scope)

def updateTjCmrs(srcTableName, conn, cur, rawStartTime, rawEndTime):
    daily_table = 'tj_cmrs_daily'
    monthly_table = 'tj_cmrs_monthly'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    scope = 't1.destinationScope in (0,2,3,4)'
    #按照景区、地区、市三级生成
    _updateJdrsLmlyrs(srcTableName, conn, cur, queryStartTime, queryEndTime, daily_table, monthly_table, scope)
    
def updateTjKyddbfx(srcTableName, conn, cur, rawStartTime, rawEndTime):
    daily_table = 'tj_kyddbfx_daily'
    monthly_table = 'tj_kyddbfx_monthly'
    queryStartTime = rawStartTime[:11] + '00:00:00'
    queryEndTime = rawEndTime
    #按照景区、地区、市三级生成
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, t1.sourceScope, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where recordedDateTime >= "%s" and recordedDateTime <= "%s" 
    group by t2.id,t1.recordedDateTime,t1.sourceScope
    ''' % (srcTableName, queryStartTime, queryEndTime)
    
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDate,sourceScope, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    
    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s",  %d, %d, 3, "%s")' % (item[0], recordDate, item[2], item[3],  recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, t1.sourceScope,sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t2.districtId,t1.recordedDateTime, t1.sourceScope    
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 2, "%s")' % (item[0], recordDate, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, t1.sourceScope,sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where  recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t1.recordedDateTime, t1.sourceScope
    '''% (srcTableName, queryStartTime, queryEndTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, %d, 1, "%s")' % (item[0], recordDate, item[2], item[3], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))        
    
    #1.4 monthy 表
    sqlDaily = '''
    select id, year(recordDate), month(recordDate), sourceScope, sum(subscriberCount), level
    from %s where recordDate>="%s" and recordDate<="%s" 
    group by id, year(recordDate), month(recordDate),sourceScope, level
    '''%(daily_table, queryStartTime[:7] +'-01', queryEndTime)
    insertUpdateSql = '''
    INSERT INTO %s (id, recordYear, recordMonth, sourceScope, subscriberCount, level, recordIdentifier) values %s 
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    cur.execute(sqlDaily)
    results = cur.fetchall()
    values = list()
    for item in results:
        if item[2] < 10:
            recordIdentifier = str(item[1]) + '0' + str(item[2]) +'00000000'
        else:
            recordIdentifier = str(item[1]) + str(item[2]) +'00000000'  
        values.append('(%d, %d, %d, %d, %d, %d, "%s")' % (item[0], item[1], item[2], item[3], item[4], item[5], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (monthly_table, ','.join(values)))        
        
    #conn.commit()    
def addOneMonth(dt):
    year = dt.year
    month = dt.month
    if month == 12:
        return datetime.datetime(year+1, 1, 1)
    else:
        return datetime.datetime(year, month + 1, 1)
def minusOneMonth(dt):
    year = dt.year
    month = dt.month
    if month == 1:
        return datetime.datetime(year-1, 12, 1)
    else:
        return datetime.datetime(year, month - 1, 1)
    
def _updateJdrsLmlyrs(srcTableName, conn, cur, queryStartTime, queryEndTime, daily_table, monthly_table, scope):    
    #1.1 景区
    sqlArea = '''
    SELECT t2.id,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum 
    FROM %s t1 join jqqd t2 on (t1.areaId = t2.areaId)
    where %s and recordedDateTime >= "%s" and recordedDateTime <= "%s" 
    group by t2.id,t1.recordedDateTime
    ''' % (srcTableName, scope, queryStartTime, queryEndTime)
    
    insertUpdateSql = '''
    INSERT INTO %s (id, recordDate, subscriberCount, level, recordIdentifier) values %s
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    
    cur.execute(sqlArea)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, 3, "%s")' % (item[0], recordDate, item[2], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
        
    #1.2 地区
    sqlDistrict = '''
        SELECT t2.districtId,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM %s t1 
        join jqqd t2 on (t1.areaId = t2.areaId)
        where %s and recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t2.districtId,t1.recordedDateTime    
    '''% (srcTableName, scope, queryStartTime, queryEndTime)
    cur.execute(sqlDistrict)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, 2, "%s")' % (item[0], recordDate, item[2], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))
    
    #1.3 市
    sqlCity = '''
        SELECT 1000, t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM %s t1
        join jqqd t2 on (t1.areaId = t2.areaId)
        where %s and recordedDateTime >= "%s" and recordedDateTime <= "%s" 
        group by t1.recordedDateTime 
    '''% (srcTableName, scope, queryStartTime, queryEndTime)
    cur.execute(sqlCity)
    results = cur.fetchall()
    values = list()
    for item in results:
        recordDate = item[1].strftime("%Y-%m-%d")
        recordIdentifier = item[1].strftime("%Y%m%d000000")
        values.append('(%d, "%s", %d, 1, "%s")' % (item[0], recordDate, item[2], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (daily_table, ','.join(values)))        
    
    #1.4 monthy 表
    sqlDaily = '''
    select id, year(recordDate), month(recordDate), sum(subscriberCount), level
    from %s where recordDate>="%s" and recordDate<="%s" 
    group by id, year(recordDate), month(recordDate), level
    '''%(daily_table, queryStartTime[:7] +'-01', queryEndTime)
    insertUpdateSql = '''
    INSERT INTO %s (id, recordYear, recordMonth, subscriberCount, level, recordIdentifier) values %s 
    ON DUPLICATE KEY UPDATE subscriberCount=VALUES(subscriberCount)
    '''
    cur.execute(sqlDaily)
    results = cur.fetchall()
    values = list()
    for item in results:
        if item[2] < 10:
            recordIdentifier = str(item[1]) + '0' + str(item[2]) +'00000000'
        else:
            recordIdentifier = str(item[1]) + str(item[2]) +'00000000'  
        values.append('(%d, %d, %d, %d, %d, "%s")' % (item[0], item[1], item[2], item[3], item[4], recordIdentifier))
    if len(values) > 0:
        cur.execute(insertUpdateSql % (monthly_table, ','.join(values)))        
        
    #conn.commit()
    
    #1.5 update for daily trend
    dailyDateSt = datetime.datetime.strptime(queryStartTime[:10] + ' 00:00:00', '%Y-%m-%d %H:%M:%S')
    dailyDateEd = datetime.datetime.strptime(queryEndTime[:10] + ' 00:00:00', '%Y-%m-%d %H:%M:%S')
    ctDayList = list()
    ptDayList = list()
    ctDayList.append('"' + dailyDateEd.strftime('%Y-%m-%d') + '"')
    ptDayList.append('"' + (dailyDateEd - datetime.timedelta(days=1)).strftime('%Y-%m-%d') + '"')
    while dailyDateSt < dailyDateEd:
        ctDayList.append('"' + dailyDateSt.strftime('%Y-%m-%d') + '"')
        ptDayList.append('"' + (dailyDateSt - datetime.timedelta(days=1)).strftime('%Y-%m-%d') + '"')
        dailyDateSt = dailyDateSt + datetime.timedelta(days=1)
    dailyTrendSelect = '''
    select ct.id, ct.recordDate, ct.recordIdentifier,
      case 
          when pt.subscriberCount<>0 and pt.subscriberCount is not null
          then (ct.subscriberCount - pt.subscriberCount)/pt.subscriberCount
          else 0 
      end
      as trend
    from
        (select id, recordDate, subscriberCount,recordIdentifier from %s where recordDate in (%s)) ct
    left join
        (select id, recordDate, subscriberCount,recordIdentifier from %s where recordDate in (%s)) pt
    on ct.id=pt.id and 
        pt.recordDate = subdate(ct.recordDate,1)
    ''' % (daily_table, ','.join(ctDayList), daily_table, ','.join(ptDayList))
    cur.execute(dailyTrendSelect)
    results = cur.fetchall()
    values = list()
    dailyTrendInsUpd = '''
    INSERT INTO %s (id, recordDate, recordIdentifier, trend) values %s
    ON DUPLICATE KEY UPDATE trend=VALUES(trend)
    '''
    for item in results:
        recordDate = item[1].strftime('%Y-%m-%d')
        values.append('(%d, "%s", "%s", %f)' % (item[0], recordDate, item[2], item[3]))
    if len(values) > 0:
        cur.execute(dailyTrendInsUpd % (daily_table, ','.join(values)))

    #1.6 update for monthly trend
    monthDateSt = datetime.datetime.strptime(queryStartTime[:7] + '-01 00:00:00', '%Y-%m-%d %H:%M:%S')
    monthDateEd = datetime.datetime.strptime(queryEndTime[:7] + '-01 00:00:00', '%Y-%m-%d %H:%M:%S')
    ctMonthList = list()
    ptMonthList = list()
    ctMonthList.append('"' + monthDateEd.strftime('%Y%m00000000') + '"')
    ptMonthList.append('"' + minusOneMonth(monthDateEd).strftime('%Y%m00000000') + '"')
    while monthDateSt < monthDateEd:
        ctMonthList.append('"' + monthDateSt.strftime('%Y%m00000000') + '"')
        ptMonthList.append('"' + minusOneMonth(monthDateSt).strftime('%Y%m00000000') + '"')
        monthDateSt = addOneMonth(monthDateSt)
    monthlyTrendSelect = '''
    select ct.id, ct.recordYear, ct.recordMonth, ct.recordIdentifier,
      case 
          when pt.subscriberCount<>0 and pt.subscriberCount is not null
          then (ct.subscriberCount - pt.subscriberCount)/pt.subscriberCount
          else 0 
      end
      as trend
    from
        (select id, recordYear, recordMonth, recordIdentifier, subscriberCount from %s where recordIdentifier in (%s)) ct
    left join
        (select id, recordYear, recordMonth, subscriberCount, recordIdentifier from %s where recordIdentifier in (%s)) pt
    on ct.id=pt.id and 
        (
            (pt.recordYear = ct.recordYear and pt.recordMonth=ct.recordMonth-1) or
            (pt.recordYear = ct.recordYear-1 and pt.recordMonth = 12 and ct.recordMonth=1)
        )
    ''' % (monthly_table, ','.join(ctMonthList), monthly_table, ','.join(ptMonthList))
    cur.execute(monthlyTrendSelect)
    results = cur.fetchall()
    values = list()
    monthlyTrendInsUpd = '''
    INSERT INTO %s (id, recordYear, recordMonth, recordIdentifier, trend) values %s
    ON DUPLICATE KEY UPDATE trend=VALUES(trend)
    '''
    for item in results:
        values.append('(%d, %d, %d, "%s", %f)' % (item[0], item[1], item[2], item[3], item[4]))
    if len(values) > 0:
        cur.execute(monthlyTrendInsUpd % (monthly_table, ','.join(values)))


if __name__ == '__main__':
    connPara = {'host':'172.31.250.2','user':'root', 'passwd':'Abcde01!', 'db':'lydsj', 'charset':'utf8', 'port':3306}
    #connPara = {'host':'localhost','user':'test', 'passwd':'test', 'db':'recognize', 'charset':'utf8', 'port':3306}
    pid = '[' + str(os.getpid()) + ']'
    if len(sys.argv) <= 1:
        startDate = '2018-04-10 00:00:00'
    else:
        startDate = sys.argv[1] + ' 00:00:00'
        
    conn = MySQLdb.Connect(**(connPara))
    conn.ping(True)
    cur = conn.cursor()
    if len(sys.argv) <= 2:
        d = datetime.datetime.strptime(startDate, '%Y-%m-%d %H:%M:%S')
        n = datetime.datetime.now()
        while d < n: 
            st = d.strftime('%Y-%m-%d 00:00:00')
            ed = d.strftime('%Y-%m-%d 23:59:59')
            print pid, 'Handling %s to %s' % (st, ed)
            t1 = time.time()
            analyseQtrqly(pid, 'sftp', conn, cur, st, ed)
            analyseSsrqly(pid, 'sftp', conn, cur, st, ed)
            analyseQtrqlx(pid, 'sftp', conn, cur, st, ed)
            analyseQtzl(pid, 'sftp', conn, cur, st, ed)
            analyseSslr(pid, 'sftp', conn, cur, st, ed)
            analyseSszl(pid, 'sftp', conn, cur, st, ed)
            conn.commit()
            t2 = time.time()
            print pid, 'It costs %d seconds to handle' % (int(t2 - t1))
            d = d + datetime.timedelta(days=1)
    elif sys.argv[2] == 'holiday':
        updateHolidayStatistic(pid, conn, cur, startDate, startDate)
        conn.commit()