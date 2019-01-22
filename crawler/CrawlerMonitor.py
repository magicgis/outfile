# _*_ coding:utf-8 _*_

import smtplib
from email.mime.text import MIMEText
from com.naswork.sentiment.common.utils import MySqlProxy
import datetime
import sys

reload(sys)
sys.setdefaultencoding('utf8')

# 发送邮件


class SendEmail(object):

    def __init__(self):

        self.username = '1849016592@qq.com'
        self.passwd = 'psxqujbskkfhhbjd'
        self.recv = 'haidalinshuhao@163.com'
        self.mail_host = 'smtp.qq.com'
        self.port = 25

    def send(self,title,content):
        msg = MIMEText(content,'plain', 'utf-8')
        msg['Subject'] = title
        msg['From'] = self.username
        msg['To'] = self.recv
        smtp = smtplib.SMTP(self.mail_host, port=self.port)
        smtp.login(self.username, self.passwd)
        smtp.sendmail(self.username, self.recv, msg.as_string())
        smtp.quit()

#写入数据库
class InsertDB():

    def __init__(self):

        self.host = 'a002.nscc-gz.cn'
        self.port = 10416
        self.user = 'test'
        self.passward = 'test'
        self.db='sa2'

    def Insert(self,channelId,entityId,errorcontent):
        dbProxy = MySqlProxy(self.host, self.port, self.user, self.passward, self.db)
        add_time = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        valueList = []
        valueList.append('(%d,"%s","%s","%s")' % (
                channelId,
                entityId,
                add_time,
                errorcontent
            ))
        sql = """INSERT INTO sa_monitor(CHANNEL_ID,
                       ENTITY_ID, ADD_DATETIME, ERROR_CONTENT)
                       VALUES %s"""
        tmp = sql % (','.join(valueList))
        dbProxy.execute(tmp)
        dbProxy.commit()
        dbProxy.close()







