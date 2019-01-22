package com.asiainfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

import com.asiainfo.bean.AsiainfoHeader;
import com.asiainfo.httpsrest.SSLRestHttpclient;
import com.asiainfo.sign.RSAEncrypt;
import com.asiainfo.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
	private static Logger log =Logger.getLogger("TESTMAIN");
	
	private static final int max_error_num = Integer.parseInt(StringUtil.getProperty("config.properties", "MAX_ERROR_NUM"));
	private static final String jdbc_class_name = StringUtil.getProperty("config.properties", "JDBC_CLASS_NAME");
	private static final String jdbc_db = StringUtil.getProperty("config.properties", "JDBC_DB");
	private static final String db_user_name = StringUtil.getProperty("config.properties", "DB_USER_NAME");
	private static final String db_psw = StringUtil.getProperty("config.properties", "DB_PSW");

	private static Connection conn = null;
	private static ResultSet rs = null;
	private static Statement state = null;
	private static PreparedStatement ps = null;
	
	private static SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static String rspBody = ""; /*request的body*/
	private static String errorBody = ""; /*错误的body*/
	private static String lastRequestDateTime = ""; /*上次请求的时间，从此时间按偏移量获得此次数据*/
	
	private static long maxRunTime = 4*60*1000; /*每次最大运行时间*/
	private static boolean isReachMaxTime = false; /*是否达到最大时间*/
	private static int sleepTime = 1000; /*sleep time*/
	
	private static int apiId = 1;
	private static String tableName = "ssrs";
	
	private static int delayTime = 15; /*API提供数据的延时数 分钟*/
	private static int timeUnitType = 0; /*时间粒度type*/
	/*0:1day 1:5min 2:ihour*/
	
	public static String generateSerialNo() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString();
		return uuidStr.replace("-", "");
	}

	public static String generateNonce() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString().toUpperCase();
		return uuidStr.replace("-", "");
	}

	public static String generateCurrentTimeStr() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return dateFormat.format(date);
	}

	public static final String appid = "100386";

	public static String[] areaIds = { "11229", "11230", "11231", "11256", "11233", "11257", "11234", "11258", "11235",
			"11236", "11237", "11238", "11239", "11240", "11241", "11242", "11243", "11244", "11245", "11246", "11247",
			"11248", "11249", "11250", "11251", "11252", "11253", "11254", "11255", "11259" };
	
	/*api对应的数据库表名*/
	public static String[] dbTables = {"", "qtrqlx", "wfzrqlx", "qtzl", "sslc", "sszl", "sslr", "yxsxzrs", 
			"ssrqly", "qtrqly", "ssrs"};

	public static String getIds(int start, int len) {
		if (start >= areaIds.length) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int end = start + len;
		int l = len;
		if (end >= areaIds.length) {
			end = areaIds.length;
			l = end - start;
		}
		for (int index = start; index < end; index++) {
			if (index < end - 1) {
				sb.append(areaIds[index]).append(";");
			} else {
				sb.append(areaIds[index]);
			}
		}
		return sb.toString();
	}
	
	/*MAIN函数*/
	public static void main(String[] args) throws Exception {
			apiId = Integer.parseInt(args[0]); 
			setFuncParam();
			boolean haveOtherRun = checkApiRunStatus();
			if(haveOtherRun == false){
				int[] arr = getDateArr(lastRequestDateTime);
				/*转换上次请求时间为Calendar类型*/
				Calendar cd = Calendar.getInstance();
				cd.set(Calendar.YEAR, arr[0]);
				cd.set(Calendar.MONTH, arr[1]-1);
				cd.set(Calendar.DAY_OF_MONTH, arr[2]);
				cd.set(Calendar.HOUR_OF_DAY, arr[3]);
				cd.set(Calendar.MINUTE, arr[4]);
				cd.set(Calendar.SECOND, arr[5]);
				
				long startTime = System.currentTimeMillis();/*开始时间*/
				long curTime = 0;/*当前时间*/
				long duration = 0;/*经过时间*/
				boolean isSuccess = false;/*api调用是否返回成功*/
				int countNum = 0; /*记录api调用错误次数，达到最大次数则记录到失败数据库*/
				String timeStr = "";
				Date tmpTime = new Date();
				Date curDate = new Date();
				int timeDiff = 0;
				while(true){
					switch(timeUnitType){
					case 0: cd.add(Calendar.DAY_OF_MONTH, 1); break;
					case 1: cd.add(Calendar.MINUTE, 5); break;
					case 2: cd.add(Calendar.HOUR_OF_DAY, 1); break;
					}
					/*5min粒度 延时15min*/
					tmpTime = cd.getTime();
					timeStr = getDateStr(tmpTime);
					timeDiff = getMinDiff(tmpTime, curDate);
					if(timeDiff < delayTime){
						//log.info(timeDiff);
						switch(timeUnitType){
						case 0: cd.add(Calendar.DAY_OF_MONTH, -1); break;
						case 1: cd.add(Calendar.MINUTE, -5); break;
						case 2: cd.add(Calendar.HOUR_OF_DAY, -1); break;
						}
						lastRequestDateTime = getDateStr(cd.getTime());
						log.info("已达最新时间");
						break;
					}
					runOneBatch(curTime, duration, startTime, isSuccess, countNum, timeStr, 1);
					countNum = 0;
					runOneBatch(curTime, duration, startTime, isSuccess, countNum, timeStr, 2);
					countNum = 0;
					runOneBatch(curTime, duration, startTime, isSuccess, countNum, timeStr, 3);
					if(isReachMaxTime == true){
						lastRequestDateTime = getDateStr(cd.getTime());
						log.info("达到时间限度");
						break;
					}
				}
				writeRunRecord();
			}
	}
	
	/*决定一些变量值*/
	private static void setFuncParam() {
		tableName = dbTables[apiId]; /*表名*/
		switch (apiId) {
		case 1:
			timeUnitType = 0;
			delayTime = 2 * 24 * 60;
			break;
		case 2:
			timeUnitType = 1;
			delayTime = 2 * 24 * 60;
			break;
		case 3:
			timeUnitType = 0;
			delayTime = 1 * 24 * 60;
			break;
		case 4:
			timeUnitType = 1;
			delayTime = 15;
			break;
		case 5:
			timeUnitType = 1;
			delayTime = 15;
			break;
		case 6:
			timeUnitType = 1;
			delayTime = 15;
			break;
		case 7:
			timeUnitType = 2;
			delayTime = 60;
			break;
		case 8:
			timeUnitType = 1;
			delayTime = 15;
			break;
		case 9:
			timeUnitType = 0;
			delayTime = 24 * 60;
			break;
		case 10:
			timeUnitType = 1;
			delayTime = 15;
			break;
		}
	}

	
	
	/*运行一批景点*/
	private static void runOneBatch(long curTime, long duration, long startTime, boolean isSuccess, int countNum, String timeStr, int n){
		int num = 0;
		switch(n){
			case 1: num = 0; break;
			case 2: num = 10; break;
			case 3: num = 20; break;
		}
		try{
			while(true){
				curTime = System.currentTimeMillis();
				duration = curTime - startTime;
				if(n==3 && duration > maxRunTime){
					isReachMaxTime = true;
					break; /*超过最长时间则此次不进行*/
				}
				countNum ++;
				isSuccess = sendHttpFunc(num, 10, timeStr);
				if(isSuccess == true) break;
				if(countNum == max_error_num){ /*达到最大次数则记录到error*/
					recordError(errorBody);
					break;
				}
			}
			Thread.sleep(sleepTime);
			log.info(timeStr + "  第" + n + "批运行完毕");
		} catch (Exception e){
			log.info(timeStr + "  第" + n + "批运行失败");
			e.printStackTrace();
		}
	}
	
	/*更新jar status*/
	private static void writeRunRecord() {
		connDB();
		try {
			String sql = "update jar_status set status = '0', lastRequestDateTime = '" + lastRequestDateTime + "' where apiId = '"+apiId+"'";
			state.executeUpdate(sql);
			log.info("writeRunRecord over");
		} catch (Exception e) {
			log.info("writeRunRecord fail");
			e.printStackTrace();
		} finally {
			 closeDB();
		}
	}

	/*查表来检查该api是否有在run*/
	private static boolean checkApiRunStatus() {
		boolean res = false;
		connDB();
		try{
			String sql = "select * from jar_status where apiId = '"+apiId+"'";
			rs = state.executeQuery(sql);
			while(rs.next()){
				String status = rs.getString("status");
				lastRequestDateTime = rs.getString("lastRequestDateTime");
				String[] strarr = lastRequestDateTime.split("\\.");
				lastRequestDateTime = strarr[0];
				String lastRunTime = rs.getString("lastRunTime");
				/*status=0,不在运行*/
				if("0".equals(status) == true) res = false;
				else{
					/*status=1,需要先check lastRunTime是不是很久了*/
					long lastRun = simpleFormat.parse(lastRunTime).getTime();
					long curTime = (new Date()).getTime();
					int days = (int)((curTime - lastRun)/(1000 * 60 * 60 * 24));
					if(days > 1) res = false; /*超过一天则判断上次写数据库失败*/
					else res = true;
				}
				break;
			}
			if(res == false){/*更新jar的status为运行状态*/
				String nowStr = getDateStr(new Date());
				sql = "update jar_status set status = '1', lastRunTime = '" + nowStr + "' where apiId = '"+apiId+"'";
				state.executeUpdate(sql);
			}
		} catch (Exception e){
			log.info("checkApiRunStatus fail");
			e.printStackTrace();
		} finally {
			 closeDB();
		}
		log.info("checkApiRunStatus over");
		return res;
	}
	
	/*获取两个日期的分钟间隔*/
	public static int getMinDiff(Date start, Date end){
		long startNum =start.getTime();
		long endNum = end.getTime();
		int days = (int)((endNum - startNum)/(1000 * 60));
		return days;
	}
	
	
	/*记录达到最大运行次数error*/
	public static void recordError(String body) {
		connDB();
		try {
			String insertTime = getDateStr(new Date());
			String sql = "insert into request_failure values(null,'"+apiId+"','" + body + "','0','"+insertTime+"')";
			state.executeUpdate(sql);
			log.info("recordError over");
		} catch (Exception e) {
			log.info("recordError fail");
			e.printStackTrace();
		} finally{
			 closeDB();
		}
	}

	public static boolean sendHttpFunc(int start, int len, String timeStr) throws Exception {
		boolean isSuccess = true;
		String aIdStr = getIds(start, len);
		String resource = "";
		String body = "";
		switch (apiId) {
		case 1:
			resource = "subscriberdestinationdaystatistics/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr
					+ "\", \"applicationId\":\"038831ba4d979a825070\",\"recordedDateTime\":\"" + timeStr + "\"}";
			break;
		case 2:
			resource = "subscriberdestinationstatistics/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr + "\", \"applicationId\":\"038831ba4d979a825070\""
					+ ", \"recordedDateTime\":\"" + timeStr + "\"}";
			break;
		case 3:
			resource = "residentsubscriberdaystatistics/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr + "\", \"applicationId\":\"038831ba4d979a825070\",\"recordedDateTime\":"
					+ "\"" + timeStr + "\"}";
			break;
		case 4:
			resource = "outflowareasubscribercounts/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr + "\", \"applicationId\":\"038831ba4d979a825070\",\"recordedDateTime\":"
					+ "\"" + timeStr + "\"}";
			break;
		case 5:
			resource = "residentsubscriberstatistics/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr + "\", \"applicationId\":\"038831ba4d979a825070\",\"recordedDateTime\":"
					+ "\"" + timeStr + "\"}";
			break;
		case 6:
			resource = "inflowsubscribercounts/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr + "\", \"applicationId\":\"038831ba4d979a825070\",\"recordedDateTime\""
					+ ":\"" + timeStr + "\"}";
			break;
		case 7:
			resource = "1hourincreasedsubscribercounts/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr
					+ "\", \"applicationId\":\"038831ba4d979a825070\",\"recordedDateTime\":\"" + timeStr + "\"}";
			break;
		case 8:
			resource = "subscriberplacestatistics/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr
					+ "\", \"applicationId\":\"038831ba4d979a825070\", \"algorithmType\":1,\"recordedDateTime\":\""
					+ timeStr + "\"}";
			break;
		case 9:
			resource = "subscribersourcedaystatistics/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr
					+ "\", \"applicationId\":\"038831ba4d979a825070\", \"algorithmType\":1,\"recordedDateTime\":\""
					+ timeStr + "\"}";
			break;
		case 10:
			resource = "subscribercounts/v1.1.1";
			body = "{\"areaIds\": \"" + aIdStr
					+ "\", \"applicationId\":\"038831ba4d979a825070\",\"recordedDateTime\":\"" + timeStr + "\"}";
			break;
		}
		errorBody = body;
		AsiainfoHeader head = new AsiainfoHeader();
		// 报文头信息
		head.setAppId(appid);
		head.setBusiSerial(generateSerialNo());
		head.setNonce(generateNonce());
		head.setTimestamp(generateCurrentTimeStr());
		String rsp = SSLRestHttpclient.post(head, resource, body);
		switch(apiId){
		case 1: isSuccess = writeDB1(rsp); break;
		case 2: isSuccess = writeDB2(rsp); break;
		case 3: isSuccess = writeDB3(rsp); break;
		case 4: isSuccess = writeDB4(rsp); break;
		case 5: isSuccess = writeDB5(rsp); break;
		case 6: isSuccess = writeDB6(rsp); break;
		case 7: isSuccess = writeDB7(rsp); break;
		case 8: isSuccess = writeDB8(rsp); break;
		case 9: isSuccess = writeDB9(rsp); break;
		case 10: isSuccess = writeDB10(rsp); break;
		}
		log.info("sendHttpFunc over");
		Thread.sleep(sleepTime);
		return isSuccess;
	}
	
	/*将API1的结果写入数据库*/
	public static boolean writeDB1(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String destinationScope = "";
					String destinationName = "";
					String recordIdentifier = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						destinationScope = data.getString("destinationScope");
						destinationName = data.getString("destinationName");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+destinationScope+"','"+destinationName+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API2的结果写入数据库*/
	public static boolean writeDB2(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String destinationScope = "";
					String destinationName = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						destinationScope = data.getString("destinationScope");
						destinationName = data.getString("destinationName");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+destinationScope+"','"+destinationName+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API3的结果写入数据库*/
	public static boolean writeDB3(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String sourceScope = "";
					String stayTimeSpan = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						sourceScope = data.getString("sourceScope");
						stayTimeSpan = data.getString("stayTimeSpan");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+sourceScope+"','"+stayTimeSpan+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API4的结果写入数据库*/
	public static boolean writeDB4(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String sourceScope = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						sourceScope = data.getString("sourceScope");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+sourceScope+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API5的结果写入数据库*/
	public static boolean writeDB5(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String sourceScope = "";
					String stayTimeSpan = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						sourceScope = data.getString("sourceScope");
						stayTimeSpan = data.getString("stayTimeSpan");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+sourceScope+"','"+stayTimeSpan+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API6的结果写入数据库*/
	public static boolean writeDB6(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String sourceScope = "";
					String insertTime = "";
					String timeOnly = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						sourceScope = data.getString("sourceScope");
						timeOnly = getTimeOnly(recordedDateTime);
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+sourceScope+"','"+timeOnly+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API7的结果写入数据库*/
	public static boolean writeDB7(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String sourceScope = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						sourceScope = data.getString("sourceScope");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+sourceScope+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API8的结果写入数据库*/
	public static boolean writeDB8(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String sourceScope = "";
					String sourceName = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						sourceScope = data.getString("sourceScope");
						sourceName = data.getString("sourceName");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+sourceScope+"','"+sourceName+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API9的结果写入数据库*/
	public static boolean writeDB9(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String sourceScope = "";
					String sourceName = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						sourceScope = data.getString("sourceScope");
						sourceName = data.getString("sourceName");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+sourceScope+"','"+sourceName+"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*将API10的结果写入数据库*/
	public static boolean writeDB10(String rsp){
		boolean isSuccess = true;
		connDB();
		try{
			JSONObject json = JSONObject.fromObject(rsp);
			log.info("JSON:"+json);
			String resptype = json.getString("resptype");
			log.info("返回类型：" + resptype);
			/*返回类型为0 表示成功*/
			if("0".equals(resptype) == true){
				JSONObject result = json.getJSONObject("result");
				JSONArray dataArr = result.getJSONArray("respdata");
				if(dataArr.size()>0){
					String sql = "insert into "+tableName+" values";
					String recordedDateTime = "";
					String areaId = "";
					String subscriberCount = "";
					String recordIdentifier = "";
					String insertTime = "";
					for(int i=0; i<dataArr.size(); i++){
						JSONObject data = dataArr.getJSONObject(i);
						recordedDateTime = data.getString("recordedDateTime");
						areaId = data.getString("areaId");
						subscriberCount = data.getString("subscriberCount");
						recordIdentifier = getRecordIdentifier(recordedDateTime);
						insertTime = getDateStr(new Date());
						if(i!=0) sql += ",";
						sql += "(null,'"+recordedDateTime+
								"','"+areaId+"','"+subscriberCount+
								"','"+recordIdentifier+"','"+insertTime+"')";
					}
					sql += ";";
					log.info(sql);
					state.executeUpdate(sql);
					log.info("insert success");
				}
			}
			else isSuccess = false;
			log.info("writeDB over");
			return isSuccess;
		} catch (Exception e){
			log.info("writeDB fail");
			e.printStackTrace();
			return false;
		} finally{
			 closeDB();
		}
	}
	
	/*构造标识符*/
	private static String getRecordIdentifier(String str) {
		String res = "";
		String[] str1 = str.split(" ");
		String[] str2 = str1[0].split("-");
		for(int i=0; i<3; i++){
			res += str2[i];
		}
		String[] str3 = str1[1].split("\\:");
		for(int i=0; i<3; i++){
			res += str3[i];
		}
		return res;
	}
	
	private static String getTimeOnly(String str){
		String res = "";
		String[] strArr = str.split(" ");
		res = strArr[1];
		return res;
	}

	public static void genKey() {
		String filepath = "/root/mkey";
		RSAEncrypt.genKeyPair(filepath);
	}
	
	private static String getDateStr(Date sj){
		String str = simpleFormat.format(sj);
		return str;
	}
	
	/*获取日期数组*/
	private static int[] getDateArr(String str){
		int[] res = new int[6];
		String[] str1 = str.split(" ");
		String[] str2 = str1[0].split("-");
		res[0] = Integer.parseInt(str2[0]);
		res[1] = Integer.parseInt(str2[1]);
		res[2] = Integer.parseInt(str2[2]);
		String[] str3 = str1[1].split("\\:");
		res[3] = Integer.parseInt(str3[0]);
		res[4] = Integer.parseInt(str3[1]);
		res[5] = Integer.parseInt(str3[2]);
		return res;
	}
	
	/*连接数据库*/
	private static void connDB(){
		try {
			Class.forName(jdbc_class_name);
			String db = jdbc_db;
			conn = DriverManager.getConnection(db, db_user_name, db_psw);// 链接到数据库
			state = conn.createStatement(); // 容器
			log.info("connDB over");
		} catch (Exception e) {
			log.info("connDB fail");
			e.printStackTrace();
		}// 加载驱动
	}
	
	/*关闭数据库连接*/
	private static void closeDB(){
		try{
			conn.close();
			log.info("connDB over");
		} catch (Exception e){
			log.info("connDB fail");
			e.printStackTrace();
		}
	}

}