#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Author: white<white@naswork.com>
# @DateTime: 2018/10/26 14:43
# @Descrition: betterair.py
import requests
import json
import md5
import cookielib
cookies = 'JSESSIONID=D1402FA62ED4ECB979DF228C50A1AA2F; liger-home-tab=%5B%7B%22tabid%22%3A%22home%22%2C%22text%22%3A%22%E6%88%91%E7%9A%84%E4%B8%BB%E9%A1%B5%22%2C%22showClose%22%3Afalse%2C%22url%22%3A%22%2Fcrm%2Fhome%2Findex%22%7D%2C%7B%22tabid%22%3A1540536053180%2C%22text%22%3A%22%E8%B5%84%E4%BA%A7%E5%8C%85%22%2C%22url%22%3A%22%2Fcrm%2Fstorage%2Fassetpackage%2FtoList%22%7D%5D'

def getData():

    # if load_cookies_lwp() :
    #     login()

    headers = {
        'Host': 'betterair.vicp.hk:9000',
        'Origin': 'http://betterair.vicp.hk:9000',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36',
        'Referer': 'http://betterair.vicp.hk:9000/crm/storage/assetpackage/toList'
    }
    postData ={
        '_search': 'false',
        'nd': 1540539086340,
        'rows': 20,
        'page': 1,
        'sidx': 'scfs.update_timestamp',
        'sord': 'desc'
    }
    data = requests.post("http://betterair.vicp.hk:9000/crm/storage/assetpackage/listData", allow_redirects=False,data=postData,headers=headers,cookies=load_cookies_lwp())
    if data.status_code == 302:
        print data.status_code
        login()
        return getData()
    else:
        jsonData = json.loads(data.text)
        return jsonData['rows']
def login():
    headers = {
        'Host': 'betterair.vicp.hk:9000',
        'Origin': 'http://betterair.vicp.hk:9000',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36',
        'Referer': 'http://betterair.vicp.hk:9000/crm/login'
    }

    postData={
        'enPassword':md5.md5('admin123456').hexdigest(),
        'username':'t1'
    }
    # print md5.md5('admin123456').hexdigest()
    result = requests.post("http://betterair.vicp.hk:9000/crm/login",params=postData,headers=headers, allow_redirects=False)
    # print result.status_code
    save_cookies_lwp(result.cookies)

def save_cookies_lwp(cookiejar):
    """
    保存cookies到本地
    """
    filename = 'password'
    lwp_cookiejar = cookielib.LWPCookieJar()
    for c in cookiejar:
        args = dict(vars(c).items())
        args['rest'] = args['_rest']
        del args['_rest']
        c = cookielib.Cookie(**args)
        lwp_cookiejar.set_cookie(c)
    lwp_cookiejar.save(filename, ignore_discard=True)

def load_cookies_lwp():
    """
    读取本地cookies
    """
    filename = 'password'
    lwp_cookiejar = cookielib.LWPCookieJar()
    lwp_cookiejar.load(filename, ignore_discard=True)
    return lwp_cookiejar

def main():
    data = getData()
    for item in data:
        print item['number']

if __name__ == '__main__':
    main()
    # login()







