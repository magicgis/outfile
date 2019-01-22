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
        // 0013GPRS������ѯ GPRS_FLOW
        xml.append("<queryreq>");
        xml.append("<msgheader>");
        xml.append("<menuid>1</menuid>");
        xml.append("<process_code>query</process_code>");
        xml.append("<req_time>������ʱ��</req_time>");
        xml.append("<req_seq>��������ˮ</req_seq>");
        xml.append("<channelinfo>");
        xml.append("<operatorid>0</operatorid>");
        xml.append("<channelid>��������ʶ</channelid>");
        xml.append("<unitid>0</unitid>");
        xml.append("</channelinfo>");
        xml.append("</msgheader>");
        xml.append("<msgbody>");
        xml.append("<userinfo>");
        xml.append("<servernum>��绰����</servernum>");
        xml.append("</userinfo>");
        xml.append("<serviceinfo>");
        xml.append("<id>GPRS_FLOW</id>");
        xml.append("</serviceinfo>");
        xml.append("</msgbody>");
        xml.append("</queryreq>");
        System.out.println(xml);
        st = System.currentTimeMillis();
        System.out.println("invoke handle:");
//        // ��һ�ֵ��÷���������������������������ӳ�ʱʱ��
//        System.out.println(srv.handle(xml.toString()));
//        System.out.println(String.format("get service cost %s ms", System.currentTimeMillis() - st));
//        st = System.currentTimeMillis();
        server = srv.getEaopServerPort();
//        System.out.println("invoke handle:");
        // �ڶ��ֵ��÷��������Ĭ�����ɵ�
        //�������µ�signǩ���㷨����
        System.out.println(server.handle(xml.toString()));
        System.out.println(String.format("get service cost %s ms", System.currentTimeMillis() - st));
        //����sign���������µ�ǩ���㷨
        System.out.println(server.handle2(xml.toString(),RSASignature.sign(xml.toString(),StringUtil.getProperty(
    			"config.properties", "SIGN_PRIVATE_KEY"))));
        System.out.println(String.format("get service cost %s ms", System.currentTimeMillis() - st));

	}

}
