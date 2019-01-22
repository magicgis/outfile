# -*- coding:utf-8 -*-
'''
Created on 5 Oct 2017

@author: eyaomai
'''
from com.naswork.sentiment.crawler.tencentnews import TencentNewsCrawler
from com.naswork.sentiment.crawler.neteasenews import NeteaseNewsCrawler
from com.naswork.sentiment.crawler.sinanews import SinaNewsCrawler
from com.naswork.sentiment.crawler.ifengnews import IFengNewsCrawler
from com.naswork.sentiment.crawler.peoplenews import PeopleNewsCrawler
from com.naswork.sentiment.crawler.zhihu import ZhihuCrawler
from com.naswork.sentiment.crawler.weibo import WeiboCrawler
from com.naswork.sentiment.crawler.baidutieba import BaiduTieBaCrawler
from com.naswork.sentiment.crawler.tianya import TianYaCrawler
from com.naswork.sentiment.crawler.wechat import WechatCrawler
from com.naswork.sentiment.crawler.xinhuanews import XinHuaNewsCrawler
from com.naswork.sentiment.crawler.supetimetable import SuperTimeTableCrawler
from com.naswork.sentiment.crawler.douban import DouBanCrawler
from com.naswork.sentiment.crawler.sinanewsblog import SinanewsBlogCrawler

CRAWLER_CODE_TENCENT_NEWS = 'qqnews'
CRAWLER_CODE_NETEASE_NEWS = '163news'
CRAWLER_CODE_SINA_NEWS = 'sinanews'
CRAWLER_CODE_IFENG_NEWS = 'ifengnews'
CRAWLER_CODE_PEOPLE_NEWS = 'peoplenews'
CRAWLER_CODE_SINA_WEIBO = 'sinaweibo'
CRAWLER_CODE_ZHIHU = 'zhihu'
CRAWLER_CODE_BAIDU_TIEBA = 'tieba'
CRAWLER_CODE_TIANYA = 'tianya'
CRAWLER_CODE_WECHAT = 'wechat'
CRAWLER_CODE_XINHUA = 'xinhua'
CRAWLER_CODE_XIAKELIAO = 'xiakeliao'
CRAWLER_CODE_DOUBAN = 'douban'
CRAWLER_CODE_SINABLOG = 'sinablog'


class CrawlerFactory(object):
    def __init__(self):
        pass
    
    @staticmethod
    def getCrawler(channel, logger=None):
        if channel.crawler_code == CRAWLER_CODE_TENCENT_NEWS:
            return TencentNewsCrawler(channel, logger)        
        elif channel.crawler_code == CRAWLER_CODE_NETEASE_NEWS:
            return NeteaseNewsCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_SINA_NEWS:
            return SinaNewsCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_IFENG_NEWS:
            return IFengNewsCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_PEOPLE_NEWS:
            return PeopleNewsCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_SINA_WEIBO:
            return WeiboCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_ZHIHU:
            return ZhihuCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_BAIDU_TIEBA:
            return BaiduTieBaCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_TIANYA:
            return TianYaCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_WECHAT:
            return WechatCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_XINHUA:
            return XinHuaNewsCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_XIAKELIAO:
            return SuperTimeTableCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_DOUBAN:
            return DouBanCrawler(channel, logger)
        elif channel.crawler_code == CRAWLER_CODE_SINABLOG:
            return SinanewsBlogCrawler(channel, logger)
        else:
            return None
