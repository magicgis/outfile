import sys
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *
from PyQt5.QtWebKitWidgets import *
from bs4 import BeautifulSoup
from random import choice
import time

#use QtWebkit to get the final webpage
import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )
class WebRender(QWebPage):
    def __init__(self, url):
        self.app = QApplication(sys.argv)
        QWebPage.__init__(self)
        self.loadFinished.connect(self.__loadFinished)
        
        self.mainFrame().load(QUrl(url))
        self.app.exec_()

    def __loadFinished(self, result):
        self.frame = self.mainFrame()
        self.app.quit()
        
def getKey(content):
    soup = BeautifulSoup(content)
    csrfs = soup.findAll('input',{'id':'csrf'})
    csrf = "Q0IvcURhTmx0dVdsZEZxRENpNEJVQT09"
    if csrfs:
        csrf = str(csrfs[0].attrs['value'])
    return csrf
        
def getTableMessage(content,pageNum):
    soup = BeautifulSoup(content)
    table = soup.findAll('table',{'class':'DataTable'})
    if len(table)==0:
        raise Exception('Has No table')
    tbodys = table[0].findAll("tbody")
    if len(tbodys)==0:
        raise Exception('Has No tbodys')
    trs = tbodys[0].findAll("tr")
    stockMessage = '';
    for tr in trs:
        tds = tr.findAll('td')
        url = tds[1].findAll('a')[0].attrs['href']
        aircraft = getRowValue(tds[3])
        airline = ''
        airlinesplie = ''
        if getRowValue(tds[5]):
            airline = getRowValue(tds[5])
        if len(tds[5].findAll('a')) > 0:
            airlineurl = tds[5].findAll('a')[0].attrs['href']
            airlinesplie = airlineurl.split("/")[2]
        regurl = tds[4].findAll('a')[0].attrs['href']
        regs = str(regurl).split("/")
        regurl = regs[len(regs)-2]
        reg = ''
        if airlinesplie == '':
            reg = str(regurl)
        else:
            regurl = str(regurl).split(airlinesplie)
            reg = str(regurl[0])[:-1]
        delivered = getRowValue(tds[6])
        status = getRowValue(tds[7])
        prev_reg = getRowValue(tds[8])
        remark = getRowValue(tds[9]).replace('\'','\\\'')
        
        dict = {'aircraft': aircraft,'airline':airline.replace("'"," "),'reg':reg,'status':status,'prev_reg':prev_reg,'remark':remark,'delivered':delivered,'pageNum':pageNum};
        foo = [5,6,7,8,9]
        time.sleep(choice(foo))
        getDetailMessage(url,dict)
        insertDB(dict)
    return stockMessage

def getRowValue(td):
    return td.text.strip()

def insertDB(dict):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into planespotters_2(MSN, LN, AIRCRAFT_TYPE,REG,AIRLINE,DELIVERED,STATUS,PREV_REG,REMARK,PAGE_NUM) values('%s', '%s', '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s')"%(dict['msn'],dict['ln'],dict['aircraft'],dict['reg'],dict['airline'],dict['delivered'],dict['status'],dict['prev_reg'],dict['remark'],dict['pageNum'])
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def createFile(content,pageNum):
    name = 'D:/crawl/' + str(pageNum) + '.html'
    file = open(name,'w')
    file.write(content)

#login
#for pageNum in range(1,1000):
#    loginFrame = WebRender("https://www.planespotters.net/production-list/Boeing/737?p="+str(pageNum))
#    loginHtml = loginFrame.frame.toHtml()
#    createFile(loginHtml,pageNum)
#    if pageNum % 5 == 0:
#        foo = [25,26,27,28,29,30]
#        time.sleep(choice(foo))
#    else:
#        foo = [8,9,10,11,12,13,14,15]
#        time.sleep(choice(foo))
    #csrfs = getKey(loginHtml)

mainFrame = WebRender("https://www.planespotters.net/production-list/Boeing/737")
mainHtml = mainFrame.frame.toHtml()
print mainHtml

page = etree.HTML(html.encode('utf-8'))