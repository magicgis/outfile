# -*- coding: utf-8 -*-
import sys
from PyQt5.QtCore import *  
from PyQt5.QtWidgets import *  
from PyQt5.QtGui import *  
from PyQt5.QtWebEngineWidgets import *
class MainWindow(QMainWindow):
    def __init__(self):
        super(QMainWindow, self).__init__()
        self.setWindowTitle("打开网页例子")
        #相当于初始化这个加载web的控件
        self.browser = QWebEnginerView()
        self.browser.setHtml('''
        <!DOCTYPE html>
        <html>
            <head>
                <meta charset="UTF-8">
                <title></title>
            </head>
            <body>
                <h1>Hello PyQt5</h1>
                <h1>Hello PyQt5</h1>
                <h1>hello PyQt5</h1>
                <h1>hello PyQt5</h1>
                <h1>hello PyQt5</h1>
                <h1>Hello PyQt5</h1>
            </body>
        <html>

        ''')
        self.setCentraWidget(self.browser)
if __name__=='__main__':
    app = QApplication(sys.argv)
    win = MainWindow()
    win.show()
    sys.exit(spp.exec_())
