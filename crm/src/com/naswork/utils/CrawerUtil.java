package com.naswork.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.naswork.codegenerator.PropertiesHelper;
import com.naswork.module.purchase.controller.supplierquote.CrawerVo;
public class CrawerUtil {
	private String url;
	private String partList;
	private Integer clientInquiryId;
	private Integer bizTypeId;
	private String clientCode;
	private String quoteNumber;
	private Integer supplier;
	
	//构造函数
	public CrawerUtil(String url,List<CrawerVo> partList,Integer clientInquiryId,Integer bizTypeId,String quoteNumber,String clientCode,Integer supplier){
		this.url = url;
		this.clientInquiryId = clientInquiryId;
		this.bizTypeId = bizTypeId;
		this.clientCode = clientCode;
		this.quoteNumber = quoteNumber;
		this.supplier = supplier;
		//拼装搜索内容
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		/*for (CrawerVo crawerVo : partList) {
			Map<String, Object> map = new Hashtable<String, Object>();
			map.put("\"pn\"", "\""+crawerVo.getPn().replaceAll("\"", "").replaceAll("\'", "")+"\"");
			map.put("\"description\"", "\"\"");
			map.put("\"id\"", "\""+crawerVo.getId()+"\"");
			map.put("\"amount\"", "\""+crawerVo.getAmount()+"\"");
			mapList.add(map);
		}
		this.partList = mapList.toString().replaceAll("=", ":");*/
	}
	
	//爬去数据
	public void crawMessage(){
		try {
			craw();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void craw() throws IOException{
		String params = /*"partList="+URLEncoder.encode(partList, "UTF-8")+"&clientInquiryId="*/"clientInquiryId="+URLEncoder.encode(clientInquiryId.toString(), "UTF-8")+"&quoteNumber="+URLEncoder.encode(quoteNumber, "UTF-8")+"&supplier="+URLEncoder.encode(supplier.toString(), "UTF-8")+"&bizTypeId="+URLEncoder.encode(bizTypeId.toString(), "UTF-8")+"&clientCode="+URLEncoder.encode(clientCode.toString(), "UTF-8");
		String correctUrl = url+"?"+params;
        // 设置header属性
        HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(correctUrl);
		post.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		post.setHeader("accept", "accept");
		post.setHeader("connection", "Keep-Alive");
		//建立连接
		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result.toString());
		rd.close();
	}
}
