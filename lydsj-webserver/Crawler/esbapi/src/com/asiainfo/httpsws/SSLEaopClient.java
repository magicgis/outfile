package com.asiainfo.httpsws;

import com.asiainfo.httpsws.eaopbase.EaopServer;
import com.asiainfo.httpsws.eaopbase.EaopServer_Service;
import com.asiainfo.sign.RSASignature;
import com.asiainfo.util.StringUtil;

public class SSLEaopClient {
	
	public static void main(String[] args) throws Exception {
		SSLInit.init();
        EaopServer_Service srv = new EaopServer_Service();
        EaopServer server;
        long st;
        StringBuffer xml = new StringBuffer();
        // 0013GPRS流量查询 GPRS_FLOW
        xml.append("<queryreq>");
        xml.append("<msgheader>");
        xml.append("<menuid>1</menuid>");
        xml.append("<process_code>query</process_code>");
        xml.append("<req_time>填请求时间</req_time>");
        xml.append("<req_seq>填请求流水</req_seq>");
        xml.append("<channelinfo>");
        xml.append("<operatorid>0</operatorid>");
        xml.append("<channelid>填渠道标识</channelid>");
        xml.append("<unitid>0</unitid>");
        xml.append("</channelinfo>");
        xml.append("</msgheader>");
        xml.append("<msgbody>");
        xml.append("<userinfo>");
        xml.append("<servernum>填电话号码</servernum>");
        xml.append("</userinfo>");
        xml.append("<serviceinfo>");
        xml.append("<id>GPRS_FLOW</id>");
        xml.append("</serviceinfo>");
        xml.append("</msgbody>");
        xml.append("</queryreq>");
        System.out.println(xml);
        st = System.currentTimeMillis();
        System.out.println("invoke handle:");
//        // 第一种调用方法，方法里面可以设置请求，连接超时时间
//        System.out.println(srv.handle(xml.toString()));
//        System.out.println(String.format("get service cost %s ms", System.currentTimeMillis() - st));
//        st = System.currentTimeMillis();
        server = srv.getEaopServerPort();
//        System.out.println("invoke handle:");
        // 第二种调用方法，框架默认生成的
        //不启用新的sign签名算法调用
        System.out.println(server.handle(xml.toString()));
        System.out.println(String.format("get service cost %s ms", System.currentTimeMillis() - st));
        //启用sign参数进行新的签名算法
        System.out.println(server.handle2(xml.toString(),RSASignature.sign(xml.toString(),StringUtil.getProperty(
    			"config.properties", "SIGN_PRIVATE_KEY"))));
        System.out.println(String.format("get service cost %s ms", System.currentTimeMillis() - st));

	}

}
