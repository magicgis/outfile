from PyQt4.QtCore import *
from PyQt4.QtGui import *
from PyQt4.QtWebKit import *
from PyQt4.QtNetwork import *
import time
import sys

class BrowserRender(QWebView):
    def __init__(self, show=True):
        '''
        if the show is true then we can see webview
        :param show:
        '''
        self.app = QApplication(sys.argv)
        QWebView.__init__(self)
        if show:
            self.show()

    def download(self, url,my_cookie_dict, timeout=60):
        '''
        download the url if timeout is false
        :param url: the download url
        :param timeout: the timeout time
        :return: html if not timeout
        '''
        loop = QEventLoop()
        timer = QTimer()
        timer.setSingleShot(True)
        
        self.cookie_jar = QNetworkCookieJar()
        cookies = []
        for key, values in my_cookie_dict.items():
            my_cookie = QNetworkCookie(QByteArray(key), QByteArray(values))
            #my_cookie.setDomain('.baidu.com')
            cookies.append(my_cookie)
        self.cookie_jar.setAllCookies(cookies)
        
        self.request = QNetworkRequest()
        self.request.setUrl(QUrl(url))
        self.request.setRawHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0")
        
        
        timer.timeout.connect(loop.quit)
        self.page().userAgentForUrl = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"
        self.loadFinished.connect(loop.quit)
        self.load(self.request)
        timer.start(timeout*1000)
        loop.exec_()
        if timer.isActive():
            timer.stop()
            return self.html()
        else:
            print "Request time out "+url

    def html(self):
        '''
        shortcut to return the current html
        :return:
        '''
        return self.page().mainFrame().toHtml()

    def find(self, pattern):
        '''
        find all elements that match the pattern
        :param pattern:
        :return:
        '''
        return self.page().mainFrame().findAllElements(pattern)

    def attr(self, pattern, name, value):
        '''
        set attribute for matching pattern
        :param pattern:
        :param name:
        :param value:
        :return:
        '''
        for e in self.find(pattern):
            e.setAttribute(name, value)

    def text(self, pattern, value):
        '''
        set plaintext for matching pattern
        :param pattern:
        :param value:
        :return:
        '''
        for e in self.find(pattern):
            e.setPlainText(value)

    def click(self, pattern):
        '''
        click matching pattern
        :param pattern:
        :return:
        '''
        for e in self.find(pattern):
            e.evaluateJavaScript("this.click()")

    def wait_load(self, pattern, timeout=60):
        '''
        wait untill pattern is found and return matches
        :param pattern:
        :param timeout:
        :return:
        '''
        deadtiem = time.time() + timeout
        while time.time() < deadtiem:
            self.app.processEvents()
            matches = self.find(pattern)
            if matches:
                return matches
        print "wait load timed out"

br = BrowserRender()
cookie_dict = {"__cfduid":"de7ceb8fdd5657d991947f756a79426151526289645"," __psuid":"9eacdb2f63cb4dc07945bf5e14c59fc3"," _ga":"GA1.2.450555718.1526289641"," _gid":"GA1.2.1016925894.1528788469","testSeed":"1955558313"," _pk_ses.1.9f6a":"%2A"," _pk_cvar.1.9f6a":"false","PHPSESSID":"n3isem1vk03dt56ao0c2aq66s3"," _gat":"1"," _pk_id.1.9f6a":"6feb741d266f83e7.1526289645.47.1528789613.."}
html = br.download("https://www.planespotters.net/production-list/Boeing/737",cookie_dict)
br.attr('#search_term', 'value', '.')
br.text('#page_size option:checked', '1000')
br.click('#search')
elements = br.wait_load('#results a')
countries = [e.toPlainText().strip() for e in elements]
print countries