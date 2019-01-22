package com.asiainfo.httpsrest;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.asiainfo.bean.AsiainfoHeader;
import com.asiainfo.sign.RSASignature;
import com.asiainfo.util.AsiainfoHashMap;
import com.asiainfo.util.StringUtil;

public class SSLRestHttpclient {
	private static final String KEY_STORE_TYPE = StringUtil.getProperty(
			"config.properties", "KEY_STORE_TYPE");
	private static final String SCHEME_HTTPS = StringUtil.getProperty(
			"config.properties", "SCHEME_HTTPS");
	private static final int HTTPS_PORT = Integer.parseInt(StringUtil
			.getProperty("config.properties", "HTTPS_PORT"));
	private static final String HTTPS_URL = StringUtil.getProperty(
			"config.properties", "HTTPS_URL");
	private static final String KEY_STORE_CLIENT_PATH = StringUtil.getProperty(
			"config.properties", "KEY_STORE_CLIENT_PATH");
	private static final String KEY_STORE_PASSWORD = StringUtil.getProperty(
			"config.properties", "KEY_STORE_PASSWORD");
	private static final String sign_private_key = StringUtil.getProperty(
			"config.properties", "SIGN_PRIVATE_KEY");

	public static HttpClient getsslhttpClient() throws Exception {
		HttpClient httpClient = new DefaultHttpClient();

//		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
//
//		KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
//		KeyStore tks = KeyStore.getInstance(KEY_STORE_TYPE);
//
//		ks.load(new FileInputStream(KEY_STORE_CLIENT_PATH),
//				KEY_STORE_PASSWORD.toCharArray());
//		tks.load(new FileInputStream(KEY_STORE_CLIENT_PATH),
//				KEY_STORE_PASSWORD.toCharArray());
//
//		kmf.init(ks, KEY_STORE_PASSWORD.toCharArray());
//		tmf.init(tks);
//
//		SSLSocketFactory socketFactory = new SSLSocketFactory(ks,
//				KEY_STORE_PASSWORD, tks);
//		Scheme sch = new Scheme(SCHEME_HTTPS, HTTPS_PORT, socketFactory);
//		httpClient.getConnectionManager().getSchemeRegistry().register(sch);

		return httpClient;
	}
	
	
	  public static String post(AsiainfoHeader header,String resource, String body) throws Exception {  
		    HttpClient httpclient=getsslhttpClient();
	        try {  
	        	AsiainfoHashMap head=AsiainfoHashMap.toAsiainfoHashMap(header);
	        	String url = HTTPS_URL + resource;
	            HttpPost postMethod = new HttpPost(url);	            
	            String content=RSASignature.getSignContent(RSASignature.getSortedMap(head))+body; 
	            
	            String signstr=RSASignature.sign(content,sign_private_key);  
	            
	            Set keys=head.keySet();
	            Iterator it=keys.iterator();
	            while(it.hasNext()){
	            	String a= (String)it.next();
	            	postMethod.setHeader(a, head.get(a));
	            }
	            postMethod.setHeader("sign",signstr);
	            
	            StringEntity entity = new StringEntity(body, "application/json", "UTF-8");
	        	postMethod.setEntity(entity);
	        	System.out.println("url:" + url);
	        	System.out.println("Header:");
		        for (Header h: postMethod.getAllHeaders()){
		        	System.out.println(h.toString());
		        }
	        	
	        	System.out.println("Body:" + entity.toString());
	        	System.out.println("Body payload:" + body);
	        	 
	        	 HttpResponse response = httpclient.execute(postMethod);
	        	 
	        	 StatusLine respHttpStatus = response.getStatusLine();
	        	 int staus= respHttpStatus.getStatusCode();
	        	 if(staus==200){
		        	 HttpEntity responseBody = response.getEntity();
		        	 return EntityUtils.toString(responseBody,"UTF-8");
	        	 }else{
	        		 return "«Î«Û ß∞‹:"+staus;
	        	 }
	                        
	        }catch(Exception e){
	        	e.printStackTrace();
	        } finally {  
	        	httpclient.getConnectionManager().shutdown();  
	        }  
	        return null;
	    }  
	
	

}
