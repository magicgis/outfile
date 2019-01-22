# -*- coding: utf-8 -*-
import sys
from PyQt4.QtGui import QApplication
from PyQt4.QtWebKit import QWebView
from PyQt4.QtCore import QUrl, QByteArray
from PyQt4.QtNetwork import QNetworkCookieJar, QNetworkCookie
import time


class Browser(QWebView):
    def __init__(self, my_cookie_dict):
        super(QWebView, self).__init__()
        # 将字典转化成QNetworkCookieJar的格式
        self.cookie_jar = QNetworkCookieJar()
        cookies = []
        for key, values in my_cookie_dict.items():
            my_cookie = QNetworkCookie(QByteArray(key), QByteArray(values))
            #my_cookie.setDomain('.baidu.com')
            cookies.append(my_cookie)
        self.cookie_jar.setAllCookies(cookies)
        # 如果没有在前面设置domain,那么可以在这里指定一个url作为domain
        # self.cookie_jar.setCookiesFromUrl(cookies, QUrl('https://www.baidu.com/'))

        # 最后cookiejar替换完成
        self.page().networkAccessManager().setCookieJar(self.cookie_jar)
        
        
    def _loadFinished(self, result):
        self.frame = self.mainFrame()
        self.app.quit()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    cookie_dict = {"__cfduid":"d1d1ab2d703c1c9001c132dc448d1078d1528699257"," __psuid":"8ea11fd2ffc79772a6076e3509f3b5b7"," _ga":"GA1.2.436002455.1528699253"," _gid":"GA1.2.91448838.1528699253","testSeed":"118802916"," _pk_ses.1.9f6a":"%2A"," _pk_cvar.1.9f6a":"false","PHPSESSID":"eg4m87ba5homcgg0legmvkriu5"," _gat":"1"," _pk_id.1.9f6a":"09164f9407a280fd.1528699257.20.1528786133.."}
    
    browser = Browser(cookie_dict)
    browser.load(QUrl('https://www.planespotters.net/production-list/Boeing/737'))
    time.sleep(20)
    print browser.page().mainFrame().toHtml()
    
    browser.show()
    app.exec_()
    browser.page().get_text()
    browser.page().html()
    browser.page().text()
    
#    browser.text
#    browser.tohtml
#    browser.show()
#    app.exec_()