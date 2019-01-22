'''
Created on 25 Feb 2017

@author: eyaomai
'''
import os
import sys
sys.path.append(sys.argv[0][:sys.argv[0].rfind(os.path.join('com','naswork'))])

from com.naswork.rfq.common.utils import MySqlProxy, Logging
if __name__ == '__main__':
    Logging.initLogger(os.path.join('conf','crawler.logging.win.cfg'))
    '''
    Fix the ABZLJ
    '''
    dbProxy = MySqlProxy(host='localhost', port=3306, user='test', passwd='test', db='airbus')
    fl = list(open('f:\\tmp\\revert.txt'))
    remainingSet = set()
    for item in fl:
        fields = item.split('\t')
        remainingSet.add(fields[0])
    sql = 'select part_num from part_clean where cage_code="1ABZLJ"'
    dbProxy.execute(sql)
    resultList = dbProxy.cur.fetchall()
    print 'In total, there are %d items and %d reserved items' % (len(resultList), len(remainingSet))
     
    #fo = open('f:\\tmp\\update.txt','w')
    count = 0
    for item in resultList:
        part_num = item[0]
        if part_num not in remainingSet:
            count+=1
            #fo.write(part_num+'\n')
            sql = '''
            update part_clean set cage_code=
            (select concat('0',substring(vendor_code,2,6)) from part where part_no="%s" limit 1)
            where part_num="%s"
            '''%(part_num, part_num)
            #print sql
            dbProxy.execute(sql)
            
            #break
            if count == (len(resultList) - len(remainingSet)):
                print 'Finish all %d items' % count
                break
            if count%50==0:
                print 'Handle %d items' % count
            
    dbProxy.commit()
    #fo.flush()
    #fo.close()