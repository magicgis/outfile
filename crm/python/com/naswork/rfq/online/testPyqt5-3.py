#-*- coding:utf-8 -*-
import sys
from HTMLParser import HTMLParser
from bs4 import BeautifulSoup
from PyQt4.QtWebKit import *
from PyQt4.QtGui import *
from PyQt4.QtCore import *
class Render(QWebPage):
    def __init__(self,url):
        self.app = QApplication(sys.argv)
        QWebPage.__init__(self)
        self.loadFinished.connect(self._loadFinished)
        self.mainFrame().load(QUrl(url))
        self.app.exec_()
    def _loadFinished(self, result):
        self.frame = self.mainFrame()
        self.app.quit()

url = 'https://movie.douban.com'
r = Render(url)
html = r.frame.toHtml()
html = html.toUtf8()
html = unicode(html,'utf8','ignore')  #把html转为我们所需要的python unicode类型
soup=BeautifulSoup(html,'html.parser')
for item in soup.select('img'):
    print item['alt']+" "+item['src']   #获取img标签下图片 以及名字信息