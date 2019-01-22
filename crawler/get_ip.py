# _*_ coding:utf-8 _*_

import requests
from CrawlerMonitor import SendEmail


class Queue(object):

    def __init__(self,size):
        self.size = size
        self.front = -1
        self.rear = -1
        self.queue = []

    def enqueue(self,ele):
        if self.isfull():
            print("queue is full")
            # raise exception("queue is full")
        else:
            self.queue.append(ele)
            self.rear = self.rear+1

    def dequeue(self):
        if self.isempty():
            print("queue is empty")
            # raise exception("queue is empty")
        else:
            value = self.queue.pop(0)
            self.front=self.front+1
        return value

    def isfull(self):
        return self.rear-self.front+1 == self.size
    def isempty(self):
        return self.front == self.rear
    def showQueue(self):
        print(self.queue)

def getIp():
    html = requests.get('http://api.xdaili.cn/xdaili-api//greatRecharge/getGreatIp?spiderId=91fc802fba7d4bf7ba7723a8a2519659&orderno=YZ20189972107o0VL5&returnType=2&count=10')
    result = html.json()
    ErrorInfo = result['ERRORCODE']
    title = 'ip代理池'
    email = SendEmail()
    if ErrorInfo == '10036' or '10038' or '10055':
        # content ='提取过快，至少5秒提取一次'
        # email.send(title,content)
        print('提取过快，至少5秒提取一次')

    elif ErrorInfo == '10032':
        content = '今日提取已达上限，请隔日提取或额外购买'
        email.send(title, content)
    IpJson = result['RESULT']
    ip_list = Queue(20)
    for i in IpJson:
        pre_ip = i["ip"] + ':' + i["port"]
        if checkIp(pre_ip):
            ip_list.enqueue(pre_ip)
        else:
            content = '已剔除不合格的ip' + pre_ip
            print(content)
            # email.send(title, content)
    return ip_list

def checkIp(ip):
    session=requests.session()
    url = 'https://weibo.com/'
    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'Host': 'weixin.sogou.com',
        'Referer': 'http://weixin.sogou.com/weixin?type=1&s_from=input&query=%E4%B8%AD%E5%B1%B1%E5%A4%A7%E5%AD%A6%E5%9B%A2%E5%A7%94&ie=utf8&_sug_=n&_sug_type_=',
        'Upgrade-Insecure-Requests': '1',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36'
    }
    proxies = {"http": "http://" + ip}
    try:
        html = session.get(url, headers=headers, proxies=proxies)
        if html.status_code == 202 or 201:
            return True
    except:
        return False






