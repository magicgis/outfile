#!/usr/bin/python
# -*- coding:utf-8 -*-

import sys

from PyQt5.QtCore import *
from PyQt5.QtWidgets import *
from PyQt5.QtWebKitWidgets import *

stockMessageHeaders={
":authority":"www.planespotters.net",
":method":"GET",
":path":"/production-list/Boeing/737",
":scheme":"https",
"accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
"accept-encoding":"gzip, deflate, br",
"accept-language":"zh-CN,zh;q=0.9",
"cache-control":"no-cache",
"cookie":"__cfduid=d1cd99511976214b5fe8d502570238c841526006272; __psuid=3be71a8e4a7f0d3934e4a1b0f80c0cd0; _ga=GA1.2.1618606427.1526006271; testSeed=1421821937; _gid=GA1.2.2024071425.1527149221; _pk_ses.1.9f6a=%2A; _pk_cvar.1.9f6a=false; _gat=1; PHPSESSID=fkcreg4a9b51taurs06330suv6; _pk_id.1.9f6a=93b004828eff069c.1526006273.36.1527234944..",
"pragma":"no-cache",
"upgrade-insecure-requests":"1",
"user-agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
}

class Form(QWidget):
    def __init__(self, parent=None):
        super(Form, self).__init__(parent)

        # self.setWindowOpacity(1)
        # self.setWindowFlags(Qt.FramelessWindowHint)
        # self.setAttribute(Qt.WA_TranslucentBackground)
        # self.showFullScreen()
        rect = QApplication.desktop().screenGeometry()
        self.resize(rect.width(), rect.height())
        self.setWindowFlags(Qt.FramelessWindowHint | Qt.WindowStaysOnTopHint)

        self.webview = QWebView()

        vbox = QVBoxLayout()
        vbox.addWidget(self.webview)

        main = QGridLayout()
        main.setSpacing(0)
        main.addLayout(vbox, 0, 0)

        self.setLayout(main)

        # self.setWindowTitle("CoDataHD")
        # webview.load(QUrl('http://www.cnblogs.com/misoag/archive/2013/01/09/2853515.html'))
        # webview.show()

    def load(self, url):
        self.webview.load(QUrl(url))
        self.webview.set_header(stockMessageHeaders)
        self.webview.show()

if __name__ == '__main__':
    app = QApplication(sys.argv)
    screen = Form()
    screen.show()
    url = "https://www.baidu.com"
    screen.load(url)
    sys.exit(app.exec_())