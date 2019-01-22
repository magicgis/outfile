import sys
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *
from PyQt5.QtWebKitWidgets import *

#use QtWebkit to get the final webpage
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

r = WebRender("https://www.planespotters.net/production-list/Boeing/737")
html = r.frame.toHtml()
page = etree.HTML(html.encode('utf-8'))