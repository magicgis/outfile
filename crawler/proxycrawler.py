# _*_ coding:utf-8 _*_
'''
Create Time: 10 July 2018
#athor: Jondar
'''
import re
import json
import time
import requests
import urllib2
from random import choice
from requests.exceptions import ConnectionError
from bs4 import BeautifulSoup


class ProxyCrawler(object):
    '''
    获取代理
    '''
    def __init__(self):
        self.session = requests.Session()
        self.proxies = []
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36',
            'Accept-Encoding': 'gzip, deflate, sdch',
            'Accept-Language': 'en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7'
        }
        self.base_headers = {
            'User-Agent': 'Mozilla/4.0(compatible;MSIE7.0;WindowsNT5.1;360SE)',
            'Accept-Encoding': 'gzip, deflate, sdch',
            'Accept-Language': 'en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7'
        }

    def get_random_proxy(self):
        if self.test_proxy():
            return self.proxies
        else:
            return None

    def test_proxy(self):
        proxies = self.crawl_daili66()

        for pro in proxies:
            proxy = urllib2.ProxyHandler({'http:': "{}".format(pro)})
            opener = urllib2.build_opener(proxy)
            urllib2.install_opener(opener)
            try:
                test_url = 'http://httpbin.org/ip'
                req = urllib2.Request(test_url)
                res = urllib2.urlopen(req).read()
                print '{} is useful -- {}' . format(pro, res)
                http_proxy = 'http://{}'.format(pro)
                self.proxies.append(http_proxy)
            except Exception as e:
                print '{} is error -- {}'.format(pro, e)
            time.sleep(1)
        if self.proxies:
            return True
        else:
            return False

    def crawl_daili66(self, page_count=4):
        """
        获取代理66
        :param page_count: 页码
        :return: 代理
        """
        start_url = 'http://www.66ip.cn/{}.html'
        urls = [start_url.format(page) for page in range(1, page_count + 1)]
        proxies = list()
        for url in urls:
            print 'Crawling', url
            html = self.get_page(url)
            if html:
                soup = BeautifulSoup(html, 'lxml')
                trs = soup.find_all('tr')

                for tr in trs[2:]:
                    tdlist = tr.find_all('td')
                    ip = tdlist[0].string
                    port = tdlist[1].string
                    proxy = ':'.join([ip, port])
                    proxies.append(proxy)
        return proxies

    def get_page(self, url, options={}):
        '''
        抓取代理
        :param url:
        :param options:
        :return:
        '''
        headers = dict(self.base_headers, **options)
        session = requests.session()
        print '正在爬取', url
        try:
            response = session.get(url, headers=headers)
            print '抓取成功', url, response.status_code
            if response.status_code == 200:
                return response.text
        except ConnectionError:
            print '抓取失败', url
            return None


if __name__ == '__main__':
    crawler = ProxyCrawler()
    crawler.test_proxy()
