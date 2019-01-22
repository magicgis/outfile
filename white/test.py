#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Author: white<white@naswork.com>
# @DateTime: 2018/10/23 9:50
# @Descrition: test.py

import logging
import rsa
import cookielib,random
import datetime,time,traceback,binascii
import re,json,urllib,base64,requests,traceback
from bs4 import BeautifulSoup
from com.naswork.rfq.white.sessioncrawler import SessionCrawler


class WeiboCrawler(object):

    def __init__(self,channel=None,logger = None):
        self.logger = logging.getLogger()
        self.channel = channel
        self.user_name_password= self.get_username_password()
        self.user_name = self.user_name_password[0]
        self.pass_word = self.user_name_password[1]
        print 'username:%s'% self.user_name
        self.session = SessionCrawler(sleepRange=[3,8])

    def get_username_password(self):
        user_name_password1 = '17825769929:4ms7e2v3zx'
        user_name_password2 = '18211493432:7fagvqyi9p'
        user_name_password3 = '17827278983:0nenzag325'
        user_name_password4 = '13922771190:5aqa10wvwf'
        user_name_password5 = '15999916968:2i45j5b49y'

        user_list = [user_name_password1,user_name_password2,user_name_password3,user_name_password4,user_name_password5]

        user_choice = random.choice(user_list)
        user_name_password = user_choice.split(':');
        return user_name_password

    def get_random_password(self):
        user_agent = [
            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.2595.400 QQBrowser/9.6.10872.400",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0.1 Safari/602.2.14",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;  Trident/5.0)",
        ]
        random_header = random.choice(user_agent)
        return  random_header

    def ptest(self):
        self.__login()

    def __login(self):
        self.user_uniqueid = None
        self.user_nick = None

        s_user_name = self.__get_username(self.user_name)
        json_data = self.__get_json_data(su_value = s_user_name)
        if not json_data:
            return False
        s_pass_word = self.__get_password(json_data['servertime'],json_data['nonce'],json_data['pubkey'])
        print 'username:%s' % s_user_name
        print 'username:%s' % s_pass_word

        post_data = {
            "entry":"weibo",
            "gateway":"1",
            "from":"",
            "savestate":"7",
            "userticket":"1",
            "vsnf":"1",
            "service":"miniblog",
            "encoding":"UTF-8",
            "pwencode": "rsa2",
            "sr": "1280*800",
            "prelt": "529",
            "url": "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack",
            "rsakv": json_data["rsakv"],
            "servertime": json_data["servertime"],
            "nonce": json_data["nonce"],
            "su": s_user_name,
            "sp": s_pass_word,
            "returntype": "TEXT",
        }
        print json_data

        if json_data['showpin'] == 1:
            url = "http://login.sina.com.cn/cgi/pin.php?r=%d&s=0&p=%s" % (int(time.time()),json_data["pcid"])
            print url
            with open("captcha.jpg","wb") as file_out:
                file_out.write(self.session.get(url, textRspOnly=False).content)

            print self.session.get(url,textRspOnly=False).content
            code = raw_input("请输入验证码：")
            post_data['pcid'] = json_data["pcid"]
            post_data['door'] = code

        login_url_1 = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.18)&_=%d" % int(time.time())
        json_data_1 = self.session.post(login_url_1, data=post_data, textRspOnly=False).json()

        if json_data_1['retcode'] == "0":
            params = {
                "callback":"",
                "client":"",
                "ticket":"",
                "ssosavestate":"",
                "_":int(time.time()*1000)
            }
            response = self.session.get("https://passport.weibo.com/wbsso/login", textRspOnly=False, params=params)
            json_data_2 = json.loads(re.search(r"\((?P<result>.*)\)", response.text).group("result"))
            if json_data_2['result'] is True:
                print 'success'
                self.user_uniqueid = json_data_2["userinfo"]["uniqueid"]
                self.user_nick = json_data_2["userinfo"]["displayname"]
                self.logger.info("WeiBoLogin succeed: %s",json_data_2)
                self.__save_cookies_lwp(response.cookies)
            else:
                print 'failed1'
                self.logger.warning("WeiBoLogin failed: %s",json_data_2)
        else:
            print 'failed2'
            self.logger.warning("WeiBoLogin failed: %s",json_data_1)
        return True if self.user_uniqueid and self.user_nick else False

    def __get_username(self,user_name):
        username_quote = urllib.quote(user_name)
        username_base64 = base64.b64encode(username_quote.encode("utf-8"))
        return  username_base64.decode("utf-8")

    def __get_json_data(self,su_value):
        params = {
            "entry":"weibo",
            "callback":"sinaSSOController.preloginCallBack",
            "rsakt":"mod",
            "checkpin":"1",
            "client":"ssologin.js(v1.4.18)",
            "su":su_value,
            "_":int(time.time()*1000)
        }
        try:
            response = self.session.get("http://login.sina.com.cn/sso/prelogin.php", params=params)
            json_data = json.loads(re.search(r"\((?P<data>.*)\)", response).group("data"))
        except Exception:
            json_data = {}
            self.logger.error("WeiBoLogin get_json_data error: %s", traceback.format_exc())

        self.logger.debug("WeiBoLogin get_json_data: %s", json_data)
        return json_data

    def __get_password(self,servertime,nonce,pubkey):
        string  = (str(servertime) + "\t" + str(nonce)+"\n"+str(self.pass_word)).encode("utf-8")
        public_key = rsa.PublicKey(int(pubkey,16),int("10001",16))
        password = rsa.encrypt(string,public_key)
        password = binascii.b2a_hex(password)
        return password.decode()

if __name__ == '__main__':
    crawler = WeiboCrawler();
    crawler.ptest();