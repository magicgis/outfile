package com.asiainfo.httpsws.eaopbase;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * ʵ��������������֤�Ļ��ӿڡ� 
 * �������ڼ䣬��� URL ���������ͷ������ı�ʶ��������ƥ�䣬����֤���ƿ��Իص��˽ӿڵ�ʵ�ֳ�����ȷ���Ƿ�Ӧ�����������ӡ�
 */
public class MyHostnameVerifier implements HostnameVerifier {
	@Override
	public boolean verify(String hostname, SSLSession session) {
		if("localhost".equals(hostname)||"127.0.0.1".equals(hostname)){
			return true;
		} else {
			return false;
		}
	}
}