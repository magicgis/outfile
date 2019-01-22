package com.asiainfo.httpsws;


import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.asiainfo.util.StringUtil;

/**
 * <鎻忚堪>
 * 
 * @author galleon
 * @date 2012-10-22
 */
public class SSLInit {
	//static String host = "172.20.35.170";
	static int port = 9090;
	static String host = "221.179.11.204";
	static {
		host =  StringUtil.getProperty(
				"config.properties", "serverHost");;
		port = Integer.parseInt( StringUtil.getProperty(
				"config.properties", "serverPort"));

		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				if("localhost".equals(hostname)||"127.0.0.1".equals(hostname)){
					return true;
				} else {
					return false;
				}
			}
		});
	}

	public static void main(String[] args) throws Exception {
		//		System.out.println(System.getProperty("java.home"));
		SSLContext ctx = init();
	}

	/**
	 * @description
	 * 读取配置文件，初始化证书
	 *
	 * @return
	 * @throws Exception
	 * @example
	 */
	public static SSLContext init() throws Exception {
		// keystore path and password
		String pass =  StringUtil.getProperty(
				"config.properties", "KEY_STORE_PASSWORD");
		String keyf = StringUtil.getProperty(
				"config.properties", "KEY_STORE_CLIENT_PATH");
		String trustf =StringUtil.getProperty(
				"config.properties", "KEY_STORE_CLIENT_PATH");
		String keyType = StringUtil.getProperty(
				"config.properties", "KEY_STORE_TYPE");
		String trustType = StringUtil.getProperty(
				"config.properties", "KEY_STORE_TYPE");
		if (pass == null)
			pass = "";
		// set up a connection
		SSLContext ctx = null;
		try {
			System.setProperty("javax.net.ssl.keyStore", keyf);
			System.setProperty("javax.net.ssl.keyStorePassword", pass);
			System.setProperty("javax.net.ssl.trustStore", trustf);
			System.setProperty("javax.net.ssl.trustStorePassword", pass);

			// init context
			ctx = SSLContext.getInstance("TLS");
			//			SSLContext.setDefault(ctx);
			if ("1.6".compareTo(System.getProperty("java.specification.version")) <= 0) {
				Class<?> clz = SSLContext.class;
				clz.getMethod("setDefault", clz).invoke(clz, ctx);
			}

			KeyStore ks = KeyStore.getInstance(keyType);
			KeyStore tks = KeyStore.getInstance(trustType);

			// load keystore
			ks.load(new FileInputStream(keyf), pass.toCharArray());
			tks.load(new FileInputStream(trustf), pass.toCharArray());

			String alg = KeyManagerFactory.getDefaultAlgorithm();
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(alg);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(alg);
			kmf.init(ks, pass.toCharArray());
			tmf.init(tks);
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

			System.out.println("load keystore success.");

		} catch (Exception e) {
			ctx = null;
			System.out.println("establish connection error.");
			e.printStackTrace();
		}
		return ctx;
	}

	/**
	 * @description
	 * 测试配置证书后与目标服务器ip端口是否连接成功
	 *
	 * @param ctx
	 * @example
	 */
	public static void testHandshake(SSLContext ctx) {
		SSLSocket socket = null;
		long st = System.currentTimeMillis();
		try {
			SSLSocketFactory ssf = ctx.getSocketFactory();
			// create socket
			socket = (SSLSocket) ssf.createSocket(host, port);
			System.out.println(String.format("%s:%s create socket success.", host, port));
			// handshake
			socket.startHandshake();
			System.out.println("handshake success. cost " + (System.currentTimeMillis() - st));
		} catch (Exception e) {
			System.out.println("handshake error. cost " + (System.currentTimeMillis() - st));
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
