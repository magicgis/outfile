package com.asiainfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.asiainfo.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MyTest {
	/*private static final String jdbc_class_name = StringUtil.getProperty("config.properties", "JDBC_CLASS_NAME");
	private static final String jdbc_db = StringUtil.getProperty("config.properties", "JDBC_DB");
	private static final String db_user_name = StringUtil.getProperty("config.properties", "DB_USER_NAME");
	private static final String db_psw = StringUtil.getProperty("config.properties", "DB_PSW");
	
	public static void main(String[] args) throws Exception {
		String testStr = "{\"respcode\":\"0\",\"respdesc\":\"鎴愬姛\",\"resptype\":\"0\",\"result\":{\"respdata\":[{\"areaId\":\"11229\",\"recordedDateTime\":\"2018-04-16 10:10:00\",\"subscriberCount\":\"487\"}]}}";
		JSONObject json = JSONObject.fromObject(testStr);
		JSONObject result = json.getJSONObject("result");
		JSONArray dataArr = result.getJSONArray("respdata");
		if(dataArr.size()>0){
			for(int i=0; i<dataArr.size(); i++){
				JSONObject data = dataArr.getJSONObject(i);
				System.out.println(data.get("areaId"));
				System.out.println(data.get("recordedDateTime"));
			}
		}
		writeDB();
    }
	
	public static void writeDB(){
		try{
			String res[] = getDateArr(new Date());
			int mNum = Integer.parseInt(res[4]);
			System.out.println(mNum);
			System.out.println(mNum%2);
			int sNum = Integer.parseInt(res[5]);
			System.out.println(sNum);
			if(mNum%2==0 && sNum == 0){
				String str = res[3]+"-"+res[4]+"-"+res[5];
				Class.forName(jdbc_class_name);// 加载驱动
				String jdbc = jdbc_db;
				Connection conn = DriverManager.getConnection(jdbc, "root", "Pxpanxin");// 链接到数据库
				Statement state = conn.createStatement(); // 容器
				String sql = "insert into xs values('1108','" + str + "','新')"; // SQL语句
				state.executeUpdate(sql);
				conn.close();// 关闭通道
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	private static String[] getDateArr(Date sj){
		String[] res = {};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String str = sdf.format(sj);
		res = str.split("-");
		return res;
	}
	
	private static String getDateStr(Date sj) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(sj);
		return str;
	}*/
}
