from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from bs4 import BeautifulSoup
import time, sys, io
import MySQLdb
from random import choice
reload(sys)
sys.setdefaultencoding( "utf-8" )


pageHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/production-list/Boeing/737?p=2",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"cookie":"__cfduid=d7d4b8004a3b03f93328977530b28ea5e1525849643; __psuid=7619aa2b80bfa3a4abe1ab36c935defb; _ga=GA1.2.373894729.1525849647; testSeed=571077140; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gid=GA1.2.1859258666.1526542900; _gat=1; PHPSESSID=hlej2f7vfilhen2satfnlni6l7; _pk_id.1.9f6a=3ef5cef84e9bfbc6.1526304667.24.1526542961..",
"pragma":"no-cache",
"referer":"https://www.planespotters.net/production-list/Boeing/737",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
}
#sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf8')

def scrape(p):

    #chromedriver = r"\driver\chromedriver.exe"
    #chromedriver = "G:\GitLib\crm\python\com\naswork\rfq\online\driver\chromedriver.exe"
    URL = "https://www.planespotters.net/production-list/Boeing/737?p="+str(p)
    #URL = "file:///C:/Users/Tanoy/Desktop/html.html"

    try:
        driver = webdriver.Chrome(r"G:\GitLib\crm\python\com\naswork\rfq\online\driver\chromedriver.exe")
        driver.set_window_position(-10000, 0)
        driver.get(URL)
#        print(driver.get_cookies())
#        driver.add_cookie({'__cfduid':'d7d4b8004a3b03f93328977530b28ea5e1525849643','__psuid':'7619aa2b80bfa3a4abe1ab36c935defb','_ga':'GA1.2.373894729.1525849647','testSeed':'571077140','_pk_ses.1.9f6a':'%2A','_pk_cvar.1.9f6a':'false','_gid':'GA1.2.1859258666.1526542900','_gat':'1','PHPSESSID':'hlej2f7vfilhen2satfnlni6l7','_pk_id.1.9f6a':'3ef5cef84e9bfbc6.1526304667.24.1526542961..'});
#        driver.get_cookies()
        time.sleep(10)
        result = driver.execute_script("return document.body.innerHTML").encode('utf-8')
        print result
        result.find("27841")
    except TimeoutException as e:
        print(e)

    soup = BeautifulSoup(result)
    soup.find_all("27841")
    driver.close()
    return soup

    
    
def getMessage(soup,pageNum):
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
        msn = tds[1].findAll('span')[0].text
        ln = tds[2].findAll('span')[0].text
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
        
        dict = {'aircraft': aircraft,'airline':airline.replace("'"," "),'reg':reg,'status':status,'prev_reg':prev_reg,'remark':remark,'delivered':delivered,'pageNum':pageNum,'msn':msn,'ln':ln};
        insertDB(dict)
        
def insertDB(dict):
    conn = MySQLdb.connect(host="localhost",user="betterair",passwd="betterair",db="crm",charset="utf8")
    cursor = conn.cursor()
    sql = "insert into planespotters_2(MSN, LN, AIRCRAFT_TYPE,REG,AIRLINE,DELIVERED,STATUS,PREV_REG,REMARK,PAGE_NUM) values('%s', '%s', '%s', '%s', '%s', '%s', '%s','%s', '%s', '%s')"%(dict['msn'],dict['ln'],dict['aircraft'],dict['reg'],dict['airline'],dict['delivered'],dict['status'],dict['prev_reg'],dict['remark'],dict['pageNum'])
    cursor.execute(sql)
    cursor.close()
    conn.commit()
    conn.close()
    
def getRowValue(td):
    return td.text.strip()
for pageNum in range(1,1000):
    context = scrape(pageNum)
    getMessage(context,pageNum)
    print "page " + str(pageNum) + " complete!" 
    foo = [30,31,32,33,34,35,36,37,38,39,40]
    time.sleep(choice(foo)),