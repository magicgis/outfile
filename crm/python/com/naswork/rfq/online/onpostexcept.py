'''
Created on 2017.12.7

@author: fu_fe
'''
import MySQLdb

def cId_insert(cid):
    conn = MySQLdb.connect(host="127.0.0.1",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    
    sql = 'INSERT INTO onpost_email(clientInquiryId,record_date) VALUES ('+str(cid)+',NOW())'
    cursor.execute(sql)
    
    conn.commit()
    conn.close()