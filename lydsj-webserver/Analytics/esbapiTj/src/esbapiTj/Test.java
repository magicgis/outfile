package esbapiTj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Test {
	private static Logger log =Logger.getLogger("TESTMAIN");
	
	private static String jdbc_class_name = "com.mysql.jdbc.Driver";
	private static String jdbc_db = "jdbc:mysql://127.0.0.1:3306/lydsj?useUnicode/=true&characterEncoding/=utf-8";
	private static String db_user_name = "root";
	private static String db_psw = "Abcde01!";
	
	private static Connection conn = null;
	private static ResultSet rs = null;
	private static Statement state = null;
	private static PreparedStatement ps = null;
	
	private static SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/*MAIN函数*/
	public static void main(String[] args){
		int tjId = Integer.parseInt(args[0]);
		try {
			switch(tjId){ //选择进行哪一种统计
			case 1:
				sqMonthlyFunc(1); /*市区-接待人数-按月*/
				break;
			case 2:
				sqDailyFunc(2); /*市区-接待人数-按天*/
				break;
			case 3:
				xqMonthlyFunc(3); /*县区-接待人数-按月*/
				break;
			case 4:
				xqDailyFunc(4); /*县区-接待人数-按天*/
				break;
			case 5:
				xqRealtimeFunc(5); /*县区-接待人数-实时*/
				break;
			case 6:
				jqMonthlyFunc(6); /*景区-接待人数-按月*/
				break;
			case 7:
				jqDailyFunc(7); /*景区-接待人数-按天*/
				break;
			case 8:
				jqRealtimeFunc(8); /*景区-接待人数-实时*/
				break;
			case 9:
				sqMonthlyFunc(9); /*市区-来梅旅游人数-按月*/
				break;
			case 10:
				sqDailyFunc(10); /*市区-来梅旅游人数-按天*/
				break;
			case 11:
				xqMonthlyFunc(11); /*县区-来梅旅游人数-按月*/
				break;
			case 12:
				xqDailyFunc(12); /*县区-来梅旅游人数-按天*/
				break;
			case 13:
				jqMonthlyFunc(13); /*景区-来梅旅游人数-按月*/
				break;
			case 14:
				jqDailyFunc(14); /*景区-来梅旅游人数-按天*/
				break;
			case 15:
				sqMonthlyFunc(15); /*市区-出梅人数-按月*/
				break;
			case 16:
				sqDailyFunc(16); /*市区-出梅人数-按天*/
				break;
			case 17:
				xqMonthlyFunc(17); /*县区-出梅人数-按月*/
				break;
			case 18:
				xqDailyFunc(18); /*县区-出梅人数-按天*/
				break;
			case 19:
				jqMonthlyFunc(19); /*景区-出梅人数-按月*/
				break;
			case 20:
				jqDailyFunc(20); /*景区-出梅人数-按天*/
				break;
			case 21:
				kydfxMonthly(1, 2); /*省内-市区-客源地分析-按月*/
				kydfxMonthly(1, 3); /*国内-市区-客源地分析-按月*/
				break;
			case 22:
				kydfxDaily(1, 2); /*省内-市区-客源地分析-按天*/
				kydfxDaily(2, 3); /*国内-市区-客源地分析-按天*/
				break;
			case 23:
				kydfxRealtime(1, 2); /*省内-市区-客源地分析-实时*/
				kydfxRealtime(1, 3);/*国内-市区-客源地分析-实时*/
				break;
			case 24:
				kydfxMonthly(2, 2); /*省内-县区-客源地分析-按月*/
				kydfxMonthly(2, 3); /*国内-县区-客源地分析-按月*/
				break;
			case 25:
				kydfxDaily(2, 2); /* 省内-县区-客源地分析-按天 */
				kydfxDaily(2, 3); /* 国内-县区-客源地分析-按天 */
				break;
			case 26:
				kydfxRealtime(2, 2); /*省内-县区-客源地分析-实时*/
				kydfxRealtime(2, 3);/*国内-县区-客源地分析-实时*/
				break;
			case 27:
				kydfxMonthly(3, 2); /*省内-景区-客源地分析-按月*/
				kydfxMonthly(3, 3); /*国内-景区-客源地分析-按月*/
				break;
			case 28:
				kydfxDaily(3, 2); /* 省内-景区-客源地分析-按天 */
				kydfxDaily(3, 3); /* 国内-景区-客源地分析-按天 */
				break;
			case 29:
				kydfxRealtime(3, 2); /*省内-景区-客源地分析-实时*/
				kydfxRealtime(3, 3);/*国内-景区-客源地分析-实时*/
				break;
			case 30:
				monthlyFunc(1, 30); /*客源地对比分析-按月*/
				monthlyFunc(2, 30);
				monthlyFunc(3, 30);
				break;
			case 31:
				dailyFunc(1, 31); /*客源地对比分析-按天*/
				dailyFunc(2, 31);
				dailyFunc(3, 31);
				break;
			case 32:
				monthlyFunc(1, 32); /*游客逗留时间-按月*/
				monthlyFunc(2, 32);
				monthlyFunc(3, 32);
				break;
			case 33:
				dailyFunc(1, 33); /*游客逗留时间-按天*/
				dailyFunc(2, 33);
				dailyFunc(3, 33);
				break;
			case 34:
				monthlyFunc(1, 34); /*游客入园时间-按月*/
				monthlyFunc(2, 34);
				monthlyFunc(3, 34);
				break;
			case 35:
				dailyFunc1(1); /* 游客入园时间-按天 */
				dailyFunc1(2);
				dailyFunc1(3);
				break;
			case 36:
				realTimeFunc(1); /*来梅旅游人数-实时*/
				realTimeFunc(2);
				realTimeFunc(3);
				break;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void realTimeFunc(int level) {
		connDB();
		String tableName = "tj_lmlyrs_realtime";
		String maxTimeTable = "sslr";
		String infoStr = "来梅旅游人数-实时";
		switch (level) {
		case 1:
			infoStr = "市区-" + infoStr;
			break;
		case 2:
			infoStr = "县区-" + infoStr;
			break;
		case 3:
			infoStr = "景区-" + infoStr;
			break;
		}
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] arr1 = getDateArr(newestDateTime);
			Calendar cd = Calendar.getInstance();
			cd.set(Calendar.YEAR, arr1[0]);
			cd.set(Calendar.MONTH, arr1[1]-1);
			cd.set(Calendar.DAY_OF_MONTH, arr1[2]);
			cd.set(Calendar.HOUR_OF_DAY, arr1[3]);
			cd.set(Calendar.MINUTE, arr1[4]);
			cd.set(Calendar.SECOND, arr1[5]);
			cd.add(Calendar.DATE, -7);
			Date startDate = cd.getTime();
			String startDay = getDateStr(startDate);
			String endDay = newestDateTime;
			String selectSql = "";
			switch(level){
			case 1:
				selectSql = "SELECT t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM `sslr` t1 "
						+ " where t1.sourceScope in (2,3,4) and t1.recordedDateTime >='" + startDay + "' "
						+ " and t1.recordedDateTime <='" + endDay + "' " + " group by t1.recordedDateTime"
						+ " order by t1.recordedDateTime asc;";
				break;
			case 2:
				selectSql = "SELECT t2.districtId,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM `sslr` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in (2,3,4) and t1.recordedDateTime>=" + "'" + startDay
						+ "' and t1.recordedDateTime<='" + endDay + "' " + " group by t2.districtId,t1.recordedDateTime"
						+ " order by t2.districtId,t1.recordedDateTime asc;";
				break;
			case 3:
				selectSql = "SELECT t2.id,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM `sslr` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in (2,3,4) and t1.recordedDateTime>='" + startDay
						+ "' and t1.recordedDateTime<='" + endDay + "' " + " group by t2.id,t1.recordedDateTime"
						+ " order by t2.id,t1.recordedDateTime asc;";
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String id = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				switch (level) {
				case 1:
					id = "1000";
					break;
				case 2:
					id = rs.getString("districtId");
					break;
				case 3:
					id = rs.getString("id");
					break;
				}
				/*首先查该条记录是否已经存储在数据库中*/
				isExist = false;
				tmpSql = "SELECT * from "+tableName+" "
						+ "where id = '"+id+"' and recordDateTime = '"+recordedDateTime+"' and recordIdentifier = '"+recordIdentifier+"'";
				tmpRs = tmpState.executeQuery(tmpSql);
				while(tmpRs.next()){
					isExist = true;
					break;
				}
				if(isExist == false){
					tmpSql = "insert into "+tableName+" values('"+id+"', '"+recordedDateTime+"', '"+totalNum+"', '"+level+"', '"+recordIdentifier+"', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				}
				else{
					tmpSql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ "where id = '"+id+"' and recordDateTime = '"+recordedDateTime+"' and recordIdentifier = '"+recordIdentifier+"'";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		

	}

	private static void dailyFunc1(int level) {
		connDB();
		String tableName = "tj_ykrysj_daily";
		String infoStr = "-游客入园时间-按天";
		switch (level) {
		case 1:
			infoStr = "市区" + infoStr;
			break;
		case 2:
			infoStr = "县区" + infoStr;
			break;
		case 3:
			infoStr = "景区" + infoStr;
			break;
		}
		try {
			int contNum = 0;
			Calendar cd = Calendar.getInstance();
			Date date = cd.getTime();
			while(contNum < 7){
				date = cd.getTime();
				String dateStr = getDateStr(date);
				String s = getDateOnly(dateStr);
				String startTime = s+" 00:00:00";
				String endTime =s+" 23:59:59";
				String selectSql = "";
				switch (level) {
				case 1:
					selectSql = "SELECT t1.timeOnly,sum(t1.subscriberCount) as totalNum FROM `sslr` t1"
							+ " where t1.recordedDateTime <= '"+endTime+"' "
							+ " and t1.recordedDateTime >= '"+startTime+"' "
							+ " group by t1.timeOnly;";
					break;
				case 2:
					selectSql = "SELECT t2.districtId,t1.timeOnly,sum(t1.subscriberCount) as totalNum FROM `sslr` t1"
							+ " join jqqd t2 on (t1.areaId = t2.areaId)"
							+ " where t1.recordedDateTime <= '"+endTime+"' "
							+ " and t1.recordedDateTime >= '"+startTime+"' "
							+ " group by  t2.districtId,t1.timeOnly;";
					break;
				case 3:
					selectSql = "SELECT t2.id,t1.timeOnly,sum(t1.subscriberCount) as totalNum FROM `sslr` t1"
							+ " join jqqd t2 on (t1.areaId = t2.areaId)"
							+ " where t1.recordedDateTime <= '"+endTime+"' "
							+ " and t1.recordedDateTime >= '"+startTime+"' "
							+ " group by  t2.id,t1.timeOnly;";
					break;
					
				}
				rs = state.executeQuery(selectSql);
				String totalNum = "";
				String tmpSql = "";
				ResultSet tmpRs = null;
				String dateOnly = "";
				String recordIdentifier = "";
				boolean isExist = false;
				Statement tmpState = state = conn.createStatement(); // 容器
				String timeOnly = "";
				String id = "";
				String name = "timeOnly";
				while (rs.next()) {
					recordIdentifier = getRecordIdentifier(startTime);
					totalNum = rs.getString("totalNum");
					timeOnly = rs.getString("timeOnly");
					dateOnly = s;
					switch(level){
					case 1:
						id = "1000";
						break;
					case 2:
						id = rs.getString("districtId");
						break;
					case 3:
						id = rs.getString("id");
						break;
					}
					/* 首先查该条记录是否已经存储在数据库中 */
					isExist = false;
					tmpSql = "SELECT * from " + tableName + " " + "where id = '"+id+"' and recordDate = '" + dateOnly
							+ "' and "+name+" = '" + timeOnly + "'";
					tmpRs = tmpState.executeQuery(tmpSql);
					while (tmpRs.next()) {
						isExist = true;
						break;
					}
					if (isExist == false) {
						tmpSql = "insert into " + tableName + " values('"+id+"', '" + dateOnly + "', '" + totalNum
								+ "','"+timeOnly+"', '"+level+"', " + "'" + recordIdentifier + "', null)";
						tmpState.executeUpdate(tmpSql);
						log.info(infoStr + " insert success");
					} else {
						tmpSql = "update " + tableName + " set subscriberCount = '" + totalNum + "' "
								+ " where id = '"+id+"' and recordDate = '" + dateOnly + "' and "+name+" = '"
								+ timeOnly + "'";
						tmpState.executeUpdate(tmpSql);
						log.info(infoStr + " update success");
					}
				}
				contNum++;
				cd.add(Calendar.DATE, -1);
			}
			log.info(infoStr + " over");
		} catch (Exception e) {
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	/*monthly function for 客源地对比分析&游客逗留时间*/
	private static void dailyFunc(int level, int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String tag = "";
		switch (tjId) {
		case 31:
			tableName = "tj_kyddbfx_daily";
			infoStr = "客源地对比分析-按天";
			break;
		case 33:
			tableName = "tj_ykdlsj_daily";
			infoStr = "游客逗留时间-按天";
			break;
		}
		switch (level) {
		case 1:
			infoStr = "市区" + infoStr;
			break;
		case 2:
			infoStr = "县区" + infoStr;
			break;
		case 3:
			infoStr = "景区" + infoStr;
			break;
		}
		tag = "" + tjId + "" + level;
		try {
			String selectSql = "";
			switch (tag) {
			case "311":
				selectSql = "select t.recordedDateTime,t.sourceScope,sum(t.subscriberCount) as totalNum from qtrqly t "
						+ " where t.sourceScope in(2,3,4)"
						+ " group by t.recordedDateTime,t.sourceScope;";
				break;
			case "312":
				selectSql = "select t2.districtId,t1.recordedDateTime,t1.sourceScope,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in(2,3,4)"
						+ " group by t2.districtId,t1.recordedDateTime,t1.sourceScope;";
				break;
			case "313":
				selectSql = "select t2.id,t1.recordedDateTime,t1.sourceScope,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in(2,3,4)"
						+ " group by t2.id,t1.recordedDateTime,t1.sourceScope;";
				break;
			case "331":
				selectSql = "SELECT t.recordedDateTime,t.stayTimeSpan,sum(t.subscriberCount) as totalNum FROM `qtzl` t"
						+ " group by t.recordedDateTime,t.stayTimeSpan;";
				break;
			case "332":
				selectSql = "SELECT t2.districtId,t1.recordedDateTime,t1.stayTimeSpan,sum(t1.subscriberCount) as totalNum FROM `qtzl` t1"
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " group by t2.districtId,t1.recordedDateTime,t1.stayTimeSpan;";
				break;
			case "333":
				selectSql = "SELECT t2.id,t1.recordedDateTime,t1.stayTimeSpan,sum(t1.subscriberCount) as totalNum FROM `qtzl` t1"
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " group by t2.id,t1.recordedDateTime,t1.stayTimeSpan;";
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String dateOnly = "";
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			String aux = "";
			String id = "";
			String name = "";
			while (rs.next()) {
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				dateOnly = getDateOnly(recordedDateTime);
				switch (tjId) {
				case 31:
					aux = rs.getString("sourceScope");
					name = "sourceScope";
					break;
				case 33:
					aux = rs.getString("stayTimeSpan");
					name = "stayTimeSpan";
					break;
				}
				switch(level){
				case 1:
					id = "1000";
					break;
				case 2:
					id = rs.getString("districtId");
					break;
				case 3:
					id = rs.getString("id");
					break;
				}
				/* 首先查该条记录是否已经存储在数据库中 */
				isExist = false;
				tmpSql = "SELECT * from " + tableName + " " + "where id = '"+id+"' and recordDate = '" + dateOnly
						+ "' and "+name+" = '" + aux + "'";
				tmpRs = tmpState.executeQuery(tmpSql);
				while (tmpRs.next()) {
					isExist = true;
					break;
				}
				if (isExist == false) {
					tmpSql = "insert into " + tableName + " values('"+id+"', '" + dateOnly + "', '" + totalNum
							+ "','"+aux+"', '"+level+"', " + "'" + recordIdentifier + "', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				} else {
					tmpSql = "update " + tableName + " set subscriberCount = '" + totalNum + "' "
							+ " where id = '"+id+"' and recordDate = '" + dateOnly + "' and "+name+" = '"
							+ aux + "'";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e) {
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	/*monthly function for 客源地对比分析&游客逗留时间*/
	private static void monthlyFunc(int level, int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = "";
		String tag = "" + tjId + level;
		switch (tjId) {
		case 30:
			tableName = "tj_kyddbfx_monthly";
			infoStr = "客源地对比分析-按月";
			maxTimeTable = "qtrqly";
			break;
		case 32:
			tableName = "tj_ykdlsj_monthly";
			infoStr = "游客逗留时间-按月";
			maxTimeTable = "qtzl";
			break;
		case 34:
			tableName = "tj_ykrysj_monthly";
			infoStr = "游客入园时间-按月";
			maxTimeTable = "sslr";
			break;
		}
		switch (level) {
		case 1:
			infoStr = "市区-" + infoStr;
			break;
		case 2:
			infoStr = "县区-" + infoStr;
			break;
		case 3:
			infoStr = "景区-" + infoStr;
			break;
		}
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] dateArr = getDateArr(newestDateTime);
			String recordYear = String.valueOf(dateArr[0]);
			String recordMonth = String.valueOf(dateArr[1]);
			String startDay = getCurMonth1stDay(newestDateTime);
			String endDay = newestDateTime;
			String totalNum = "";
			String id = "";
			String recordIdentifier = getRecordIdentifier(startDay);
			String sourceScope = "";
			// get totalNum
			switch(tag){
			/*客源地对比分析*/
			case "301": /*市区*/
				sql = "select t.sourceScope, sum(t.subscriberCount) as totalNum from qtrqly t "
						+ " where t.sourceScope in(2,3,4) and t.recordedDateTime>='" + startDay + "' and "
						+ " t.recordedDateTime<='" + endDay + "' " + " group by t.sourceScope;";
				break;
			case "302": /*县区*/
				sql = "select t2.districtId, t1.sourceScope,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in(2,3,4) and t1.recordedDateTime >='" + startDay + "' "
						+ "and t1.recordedDateTime <='" + endDay + "'" + " group by t2.districtId,t1.sourceScope;";
				break;
			case "303": /* 景区 */
				sql = "select t2.id,t1.sourceScope,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in(2,3,4) and t1.recordedDateTime>='"+startDay+"' and t1.recordedDateTime<='"+endDay+"'"
						+ " group by t2.id,t1.sourceScope;";
				break;
			/* 游客逗留时间 */
			case "321":
				sql = "SELECT t.stayTimeSpan,sum(t.subscriberCount) as totalNum FROM `qtzl` t"
						+ " where t.recordedDateTime >= '"+startDay+"' and  t.recordedDateTime <= '"+endDay+"'"
						+ " group by t.stayTimeSpan;";
				break;
			case "322":
				sql = "SELECT t2.districtId,t1.stayTimeSpan,sum(t1.subscriberCount) as totalNum FROM `qtzl` t1"
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.recordedDateTime >= '"+startDay+"' and t1.recordedDateTime <= '"+endDay+"'"
						+ " group by t2.districtId, t1.stayTimeSpan;";
				break;
			case "323":
				sql = "SELECT t2.id,t1.stayTimeSpan,sum(t1.subscriberCount) as totalNum FROM `qtzl` t1"
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.recordedDateTime >= '"+startDay+"' and t1.recordedDateTime <= '"+endDay+"'"
						+ " group by t2.id, t1.stayTimeSpan;";
				break;
			/*游客入园时间*/
			case "341":
				sql = "SELECT t1.timeOnly,sum(t1.subscriberCount)as totalNum FROM `sslr` t1"
						+ " where t1.recordedDateTime <= '"+endDay+"' "
						+ " and t1.recordedDateTime >= '"+startDay+"' "
						+ " group by t1.timeOnly;";
				break;
			case "342":
				sql = "SELECT t2.districtId,t1.timeOnly,sum(t1.subscriberCount) as totalNum FROM `sslr` t1"
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.recordedDateTime <= '"+endDay+"' "
						+ " and t1.recordedDateTime >= '"+startDay+"' "
						+ " group by t2.districtId,t1.timeOnly;";
				break;
			case "343":
				sql = "SELECT t2.id,t1.timeOnly,sum(t1.subscriberCount) as totalNum FROM `sslr` t1"
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.recordedDateTime <= '"+endDay+"' "
						+ " and t1.recordedDateTime >= '"+startDay+"' "
						+ " group by  t2.id,t1.timeOnly;;";
				break;
			}
			rs = state.executeQuery(sql);
			HashMap<String, String> map = new HashMap<>();
			while (rs.next()) {
				switch(tjId){
				case 30: sourceScope = rs.getString("sourceScope"); break;
				case 32: sourceScope = rs.getString("stayTimeSpan"); break;
				case 34: sourceScope = rs.getString("timeOnly"); break;
				}
				totalNum = rs.getString("totalNum");
				switch (level) {
				case 1:
					id = "1000";
					break;
				case 2:
					id = rs.getString("districtId");
					break;
				case 3:
					id = rs.getString("id");
					break;
				}
				String s = id + " " + sourceScope;
				map.put(s, totalNum);
			}
			/*遍历Map*/
			for(String key: map.keySet()){
				String[] strs = key.split(" ");
				id = strs[0];
				sourceScope = strs[1];
				totalNum = map.get(key);
				/*判断是否有数据可以update*/
				boolean isExist = false;
				switch (tjId) {
				case 30:
					sql = "select * from " + tableName + "" + " where id = '" + id + "' and recordYear = '" + recordYear
							+ "' and recordMonth = '" + recordMonth + "' " + "and sourceScope = '" + sourceScope + "' ";
					break;
				case 32:
					sql = "select * from " + tableName + "" + " where id = '" + id + "' and recordYear = '" + recordYear
							+ "' and recordMonth = '" + recordMonth + "' " + "and stayTimeSpan = '" + sourceScope + "' ";
					break;
				case 34:
					sql = "select * from " + tableName + "" + " where id = '" + id + "' and recordYear = '" + recordYear
							+ "' and recordMonth = '" + recordMonth + "' " + "and timeOnly = '" + sourceScope
							+ "' ";
					break;
				}
				rs = state.executeQuery(sql);
				while (rs.next()) {
					isExist = true;
					break;
				}
				if (dateArr[2] == 1 || isExist == false) { // 若是每月第一天or不存在，则insert
					sql = "insert into " + tableName + " values('" + id + "', '" + recordYear + "', '" + recordMonth
							+ "'," + " '" + totalNum + "', '" + sourceScope + "', '" + level + "', '" + recordIdentifier
							+ "', null)";
					state.executeUpdate(sql);
					log.info(infoStr + " insert success");
				} else { // 不是第一天，为update
					switch (tjId) {
					case 30:
						sql = "update " + tableName + " set subscriberCount = '" + totalNum + "' " + "where id = '" + id
								+ "' and recordYear = '" + recordYear + "' and recordMonth = '" + recordMonth + "' "
								+ " and sourceScope = '" + sourceScope + "'";
						break;
					case 32:
						sql = "update " + tableName + " set subscriberCount = '" + totalNum + "' " + "where id = '" + id
								+ "' and recordYear = '" + recordYear + "' and recordMonth = '" + recordMonth + "' "
								+ " and stayTimeSpan = '" + sourceScope + "'";
						break;
					case 34:
						sql = "update " + tableName + " set subscriberCount = '" + totalNum + "' " + "where id = '" + id
								+ "' and recordYear = '" + recordYear + "' and recordMonth = '" + recordMonth + "' "
								+ " and timeOnly = '" + sourceScope + "'";
						break;
					}
					state.executeUpdate(sql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
	}

	/*市区-客源地分析-实时*/
	private static void kydfxRealtime(int level, int sourceScope) {
		connDB();
		String tableName = "tj_kydfx_realtime";
		String infoStr = "客源地分析-实时";
		switch(level){
		case 1: infoStr = "市区-" + infoStr; break;
		case 2: infoStr = "县区-" + infoStr; break;
		case 3: infoStr = "景区-" + infoStr; break;
		}
		if(sourceScope == 2) infoStr += "-省内";
		else infoStr += "-国内";
		String maxTimeTable = "ssrqly";
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] arr1 = getDateArr(newestDateTime);
			Calendar cd = Calendar.getInstance();
			cd.set(Calendar.YEAR, arr1[0]);
			cd.set(Calendar.MONTH, arr1[1]-1);
			cd.set(Calendar.DAY_OF_MONTH, arr1[2]);
			cd.set(Calendar.HOUR_OF_DAY, arr1[3]);
			cd.set(Calendar.MINUTE, arr1[4]);
			cd.set(Calendar.SECOND, arr1[5]);
			cd.add(Calendar.DATE, -3);
			Date startDate = cd.getTime();
			String startDay = getDateStr(startDate);
			String endDay = newestDateTime;
			String selectSql = "";
			switch(level){
			case 1: selectSql = "select t.recordedDateTime, t.sourceName,sum(t.subscriberCount) as totalNum from ssrqly t "
					+ " where t.recordedDateTime >= '"+startDay+"' and t.recordedDateTime <= '"+endDay+"'"
					+ " and t.sourceScope = "+sourceScope+""
					+ " group by t.recordedDateTime,t.sourceName;";
				break;
			case 2:
				selectSql = "select t2.districtId,t1.recordedDateTime,t1.sourceName,sum(t1.subscriberCount) as totalNum from ssrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)" + " where t1.sourceScope = " + sourceScope + " "
						+ "and t1.recordedDateTime >= '" + startDay + "' and t1.recordedDateTime <= '" + endDay + "'"
						+ " group by t2.districtId,t1.recordedDateTime,t1.sourceName;";
				break;
			case 3:
				selectSql = "select t2.id,t1.recordedDateTime,t1.sourceName,sum(t1.subscriberCount) as totalNum from ssrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)" + " where t1.recordedDateTime >= '" + startDay
						+ "' and t1.recordedDateTime <= '" + endDay + "'" + " and t1.sourceScope = " + sourceScope + ""
						+ " group by t2.id,t1.recordedDateTime,t1.sourceName;";
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String sourceName = "";
			String id = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				sourceName = rs.getString("sourceName");
				switch(level){
				case 1: id = "1000"; break;
				case 2: id = rs.getString("districtId"); break;
				case 3: id = rs.getString("id"); break;
				}
				/*首先查该条记录是否已经存储在数据库中*/
				isExist = false;
				tmpSql = "SELECT * from "+tableName+" "
						+ "where id = '"+id+"' and recordDateTime = '"+recordedDateTime+"' and sourceName = '"+sourceName+"' and"
								+ " sourceScope = " + sourceScope;
				tmpRs = tmpState.executeQuery(tmpSql);
				while(tmpRs.next()){
					isExist = true;
					break;
				}
				if(isExist == false){
					tmpSql = "insert into "+tableName+" values('"+id+"', '"+recordedDateTime+"', '"+sourceName+"',"
							+ " '"+totalNum+"', '"+sourceScope+"', '"+level+"', '"+recordIdentifier+"', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				}
				else{
					tmpSql = "update " + tableName + " set subscriberCount = '" + totalNum + "' " + "where id = '"
							+ id + "' and recordDateTime = '" + recordedDateTime + "' " + "and sourceName = '"
							+ sourceName + "' and sourceScope = " + sourceScope;
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
		
	}

	/*客源地分析-按天*/
	private static void kydfxDaily(int level, int sourceScope) {
		connDB();
		String tableName = "tj_kydfx_daily";
		String infoStr = "客源地分析-按天";
		switch(level){
		case 1: infoStr = "市区-" + infoStr; break;
		case 2: infoStr = "县区-" + infoStr; break;
		case 3: infoStr = "景区-" + infoStr; break;
		}
		if(sourceScope == 2) infoStr += "-省内";
		else infoStr += "-国内";
		try{
			String selectSql = "";
			switch(level){
			case 1:
				selectSql = "select t.recordedDateTime, t.sourceName,sum(t.subscriberCount) as totalNum from qtrqly t "
						+ " where t.sourceScope = "+sourceScope+""
						+ " group by t.recordedDateTime,t.sourceName;";
				break;
			case 2:
				selectSql = "select t2.districtId,t1.recordedDateTime,t1.sourceName,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope = "+sourceScope+""
						+ " group by t2.districtId,t1.recordedDateTime,t1.sourceName;";
				break;
			case 3:
				selectSql = "select t2.id,t1.recordedDateTime,t1.sourceName,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope = "+sourceScope+""
						+ " group by t2.id,t1.recordedDateTime,t1.sourceName;";
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String sourceName = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String dateOnly = "";
			String id = "";
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			while(rs.next()){
				switch(level){
				case 1: id = "1000"; break;
				case 2: id = rs.getString("districtId"); break;
				case 3: id = rs.getString("id"); break;
				}
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				sourceName = rs.getString("sourceName");
				dateOnly = getDateOnly(recordedDateTime);
				/*首先查该条记录是否已经存储在数据库中*/
				isExist = false;
				tmpSql = "SELECT * from " + tableName + " " + "where id = '"+id+"' and recordDate = '" + dateOnly
						+ "' and sourceName= '" + sourceName + "' and sourceScope " + "= " + sourceScope + "";
				tmpRs = tmpState.executeQuery(tmpSql);
				while(tmpRs.next()){
					isExist = true;
					break;
				}
				if(isExist == false){
					tmpSql = "insert into "+tableName+" values('"+id+"', '"+dateOnly+"', '"+sourceName+"', "
							+ "'"+totalNum+"', '"+sourceScope+"', '"+level+"', '"+recordIdentifier+"', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				}
				else{
					tmpSql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ " where id = '"+id+"' and recordDate = '"+dateOnly+"' and sourceName = '"+sourceName+"'"
									+ " and sourceScope = "+ sourceScope;
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
	}

	/*市区-客源地分析*/
	private static void kydfxMonthly(int level, int sourceScope) {
		connDB();
		String tableName = "tj_kydfx_monthly";
		String infoStr = "客源地分析-按月";
		switch(level){
		case 1: infoStr = "市区-" + infoStr; break;
		case 2: infoStr = "县区-" + infoStr; break;
		case 3: infoStr = "景区-" + infoStr; break;
		}
		if(sourceScope == 2) infoStr += "-省内";
		else infoStr += "-国内";
		String maxTimeTable = "qtrqly";
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] dateArr = getDateArr(newestDateTime);
			String recordYear = String.valueOf(dateArr[0]);
			String recordMonth = String.valueOf(dateArr[1]);
			String startDay = getCurMonth1stDay(newestDateTime);
			String endDay = newestDateTime;
			String totalNum = "";
			String id = "";
			String sourceName = "";
			String recordIdentifier = getRecordIdentifier(startDay);
			// get totalNum
			switch(level){
			case 1:sql = "select t.sourceName,sum(t.subscriberCount) as totalNum from qtrqly t "
					+ " where t.recordedDateTime >= '"+startDay+"' and  t.recordedDateTime <= '"+endDay+"'"
					+ " and t.sourceScope = "+sourceScope+""
					+ " group by t.sourceName;";
			break;
			case 2:
				sql = "select t2.districtId, t1.sourceName, sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.recordedDateTime >= '"+startDay+"' and t1.recordedDateTime <= '"+endDay+"'"
						+ " and t1.sourceScope = "+sourceScope+""
						+ " group by t2.districtId, t1.sourceName;";
			break;
			case 3:
				sql = "select t2.id,t1.sourceName,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.recordedDateTime >= '"+startDay+"' and t1.recordedDateTime <= '"+endDay+"'"
						+ " and t1.sourceScope = "+sourceScope+""
						+ " group by t2.id,t1.sourceName;";
			break;
			}
			rs = state.executeQuery(sql);
			HashMap<String, String> map = new HashMap<>();
			while (rs.next()) {
				sourceName = rs.getString("sourceName");
				totalNum = rs.getString("totalNum");
				switch(level){
				case 1: id = "1000"; break;
				case 2: id = rs.getString("districtId"); break;
				case 3: id = rs.getString("id"); break;
				}
				sourceName += " " + id;
				map.put(sourceName, totalNum);
			}
			/*遍历Map*/
			for(String key: map.keySet()){
				String[] strs = key.split(" ");
				sourceName = strs[0];
				id = strs[1];
				totalNum = map.get(key);
				/*判断是否有数据可以update*/
				boolean isExist = false;
				sql = "select * from " + tableName + "" + " where id = '"+id+"' and recordYear = '" + recordYear
						+ "' and recordMonth = '" + recordMonth + "' " + "and sourceScope = '"+sourceScope+"' "
						+ "and sourceName = '"+ sourceName + "'";
				rs = state.executeQuery(sql);
				while(rs.next()){
					isExist = true;
					break;
				}
				if(dateArr[2] == 1 || isExist == false){ //若是每月第一天or不存在，则insert
					sql = "insert into "+tableName+" values('"+id+"', '"+recordYear+"', '"+recordMonth+"', '"+sourceName+"', "
							+ " '"+totalNum+"', '"+sourceScope+"', '"+level+"', '"+recordIdentifier+"', null)";
					state.executeUpdate(sql);
					log.info(infoStr + " insert success");
				}
				else{ //不是第一天，为update
					sql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ "where id = '"+id+"' and recordYear = '"+recordYear+"' and recordMonth = '"+recordMonth+"' "
									+ " and sourceName = '"+sourceName+"'"
									+ " and sourceScope = '"+sourceScope+"'";
					state.executeUpdate(sql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
	}

	/*景区实时function*/
	private static void jqRealtimeFunc(int tjId) {
		connDB();
		String tableName = "";
		String maxTimeTable = "";
		String infoStr = "";
		switch(tjId){
		case 8: 
			tableName = "tj_jdrs_realtime";
			maxTimeTable = "ssrqly";
			infoStr = "景区-接待人数-实时";
			break;
		}
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] arr1 = getDateArr(newestDateTime);
			Calendar cd = Calendar.getInstance();
			cd.set(Calendar.YEAR, arr1[0]);
			cd.set(Calendar.MONTH, arr1[1]-1);
			cd.set(Calendar.DAY_OF_MONTH, arr1[2]);
			cd.set(Calendar.HOUR_OF_DAY, arr1[3]);
			cd.set(Calendar.MINUTE, arr1[4]);
			cd.set(Calendar.SECOND, arr1[5]);
			cd.add(Calendar.DATE, -7);
			Date startDate = cd.getTime();
			String startDay = getDateStr(startDate);
			String endDay = newestDateTime;
			String selectSql = "";
			switch(tjId){
			case 8:
				selectSql = "SELECT t1.recordedDateTime,t2.id,sum(t1.subscriberCount) as totalNum FROM `ssrqly` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId) "
						+ " where t1.recordedDateTime >= '"+startDay+"' and  t1.recordedDateTime <= '"+endDay+"'"
						+ " and t1.sourceScope =5 "
						+ " group by t1.recordedDateTime,t2.id;";	
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String id = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				id = rs.getString("id");
				/*首先查该条记录是否已经存储在数据库中*/
				isExist = false;
				tmpSql = "SELECT * from "+tableName+" "
						+ "where id = '"+id+"' and recordDateTime = '"+recordedDateTime+"' and recordIdentifier = '"+recordIdentifier+"'";
				tmpRs = tmpState.executeQuery(tmpSql);
				while(tmpRs.next()){
					isExist = true;
					break;
				}
				if(isExist == false){
					tmpSql = "insert into "+tableName+" values('"+id+"', '"+recordedDateTime+"', '"+totalNum+"', '3', '"+recordIdentifier+"', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				}
				else{
					tmpSql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ "where id = '"+id+"' and recordDateTime = '"+recordedDateTime+"' and recordIdentifier = '"+recordIdentifier+"'";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
	}

	/*景区按天function*/
	private static void jqDailyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		switch(tjId){
		case 7:
			tableName = "tj_jdrs_daily";
			infoStr = "景区-接待人数-按天";
			break;
		case 14:
			tableName = "tj_lmlyrs_daily";
			infoStr = "景区-来梅旅游人数-按天";
			break;
		case 20:
			tableName = "tj_cmrs_daily";
			infoStr = "景区-来梅旅游人数-按天";
			break;
		}
		try{
			String selectSql = "";
			switch(tjId){
			case 7:
				selectSql = "SELECT t2.id, t1.recordedDateTime,t2.areaId,t2.spotName,sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId) "
						+ " where t1.sourceScope =5 "
						+ " group by t1.recordedDateTime,t2.id,t2.spotName;";
				break;
			case 14:
				selectSql = "SELECT t2.id,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in (2,3,4)"
						+ " group by t2.id,t1.recordedDateTime"
						+ " order by t2.id,t1.recordedDateTime asc;";
				break;
			case 20:
				selectSql = "SELECT t2.id,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM `qtrqlx` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.destinationScope in (0,2,3,4)"
						+ " group by t2.id,t1.recordedDateTime"
						+ " order by t2.id,t1.recordedDateTime asc;";
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String id = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String dateOnly = "";
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				id = rs.getString("id");
				dateOnly = getDateOnly(recordedDateTime);
				/*首先查该条记录是否已经存储在数据库中*/
				isExist = false;
				tmpSql = "SELECT * from "+tableName+" "
						+ "where id = '"+id+"' and recordDate = '"+dateOnly+"' and recordIdentifier = '"+recordIdentifier+"'";
				tmpRs = tmpState.executeQuery(tmpSql);
				while(tmpRs.next()){
					isExist = true;
					break;
				}
				if(isExist == false){
					tmpSql = "insert into "+tableName+" values('"+id+"', '"+dateOnly+"', '"+totalNum+"', '3', "
							+ "'"+recordIdentifier+"', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				}
				else{
					tmpSql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ " where id = '"+id+"' and recordDate = '"+dateOnly+"' and recordIdentifier = '"+recordIdentifier+"'";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
	}

	/*县区实时function*/
	private static void xqRealtimeFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = "";
		switch(tjId){
		case 5:
			tableName = "tj_jdrs_realtime";
			infoStr = "县区-接待人数-实时";
			maxTimeTable = "ssrqly";
			break;
		}
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] arr1 = getDateArr(newestDateTime);
			Calendar cd = Calendar.getInstance();
			cd.set(Calendar.YEAR, arr1[0]);
			cd.set(Calendar.MONTH, arr1[1]-1);
			cd.set(Calendar.DAY_OF_MONTH, arr1[2]);
			cd.set(Calendar.HOUR_OF_DAY, arr1[3]);
			cd.set(Calendar.MINUTE, arr1[4]);
			cd.set(Calendar.SECOND, arr1[5]);
			cd.add(Calendar.DATE, -7);
			Date startDate = cd.getTime();
			String startDay = getDateStr(startDate);
			String endDay = newestDateTime;
			String selectSql = "";
			switch(tjId){
			case 5:
				selectSql = "SELECT t1.recordedDateTime,t2.districtId,t2.districtName,sum(t1.subscriberCount) as totalNum FROM `ssrqly` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId) "
						+ " where t1.recordedDateTime >= '"+startDay+"' and t1.recordedDateTime <= '"+endDay+"'"
						+ " and t1.sourceScope =5 "
						+ " group by t1.recordedDateTime,t2.districtId,t2.districtName;";
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String districtId = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				districtId = rs.getString("districtId");
				/*首先查该条记录是否已经存储在数据库中*/
				isExist = false;
				tmpSql = "SELECT * from "+tableName+" "
						+ "where id = '"+districtId+"' and recordDateTime = '"+recordedDateTime+"' and recordIdentifier = '"+recordIdentifier+"'";
				tmpRs = tmpState.executeQuery(tmpSql);
				while(tmpRs.next()){
					isExist = true;
					break;
				}
				if(isExist == false){
					tmpSql = "insert into "+tableName+" values('"+districtId+"', '"+recordedDateTime+"', '"+totalNum+"', '2', '"+recordIdentifier+"', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				}
				else{
					tmpSql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ "where id = '"+districtId+"' and recordDateTime = '"+recordedDateTime+"' and recordIdentifier = '"+recordIdentifier+"'";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
	}

	/*县区按天function*/
	private static void xqDailyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		switch(tjId){
		case 4:
			tableName = "tj_jdrs_daily";
			infoStr = "县区-接待人数-按天";
			break;
		case 12:
			tableName = "tj_lmlyrs_daily";
			infoStr = "县区-来梅旅游人数-按天";
			break;
		case 18:
			tableName = "tj_cmrs_daily";
			infoStr = "县区-出梅人数-按天";
			break;
		}
		try{
			String selectSql = "";
			switch(tjId){
			case 4:
				selectSql = "SELECT t1.recordedDateTime,t2.districtId,t2.districtName,sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope = 5 "
						+ " group by t1.recordedDateTime,t2.districtId,t2.districtName "
						+ " order by t1.recordedDateTime asc;";
				break;
			case 12:
				selectSql = "SELECT t2.districtId,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in (2,3,4)"
						+ " group by t2.districtId,t1.recordedDateTime"
						+ " order by t2.districtId,t1.recordedDateTime asc;";
				break;
			case 18:
				selectSql = "SELECT t2.districtId,t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM `qtrqlx` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.destinationScope in (0,2,3,4)"
						+ " group by t2.districtId,t1.recordedDateTime"
						+ " order by t2.districtId,t1.recordedDateTime asc;";
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String districtId = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String dateOnly = "";
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				districtId = rs.getString("districtId");
				dateOnly = getDateOnly(recordedDateTime);
				/*首先查该条记录是否已经存储在数据库中*/
				isExist = false;
				tmpSql = "SELECT * from "+tableName+" "
						+ "where id = '"+districtId+"' and recordDate = '"+dateOnly+"' and recordIdentifier = '"+recordIdentifier+"'";
				tmpRs = tmpState.executeQuery(tmpSql);
				while(tmpRs.next()){
					isExist = true;
					break;
				}
				if(isExist == false){
					tmpSql = "insert into "+tableName+" values('"+districtId+"', '"+dateOnly+"', '"+totalNum+"', '2', "
							+ "'"+recordIdentifier+"', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				}
				else{
					tmpSql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ " where id = '"+districtId+"' and recordDate = '"+dateOnly+"' and recordIdentifier = '"+recordIdentifier+"'";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	/*县区按月function*/
	private static void xqMonthlyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = "";
		switch(tjId){
		case 3:
			tableName = "tj_jdrs_monthly";
			infoStr = "县区-接待人数-按月";
			maxTimeTable = "qtrqly";
			break;
		case 11:
			tableName = "tj_lmlyrs_monthly";
			infoStr = "县区-来梅旅游人数-按月";
			maxTimeTable = "qtrqly";
			break;
		case 17:
			tableName = "tj_cmrs_monthly";
			infoStr = "县区-出梅人数-按月";
			maxTimeTable = "qtrqlx";
			break;
		}
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] dateArr = getDateArr(newestDateTime);
			String recordYear = String.valueOf(dateArr[0]);
			String recordMonth = String.valueOf(dateArr[1]);
			String startDay = getCurMonth1stDay(newestDateTime);
			String endDay = newestDateTime;
			String totalNum = "";
			String recordIdentifier = getRecordIdentifier(startDay);
			// get totalNum
			switch(tjId){
			case 3:
				sql = "SELECT t2.districtId,t2.districtName,sum(t1.subscriberCount) as totalNum "
						+ "FROM `qtrqly` t1 join jqqd t2 on (t1.areaId = t2.areaId) " + "where t1.recordedDateTime <= '"
						+ endDay + "' " + "and t1.recordedDateTime >= '" + startDay + "' and t1.sourceScope =5 "
						+ "group by t2.districtId,t2.districtName;";
				break;
			case 11:
				sql = "SELECT t2.districtId,sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in (2,3,4)"
						+ " and t1.recordedDateTime <= '"+endDay+"' "
						+ " and t1.recordedDateTime >= '"+startDay+"' "
						+ " group by t2.districtId"
						+ " order by t2.districtId asc;";
				break;
			case 17:
				sql = "SELECT t2.districtId, sum(t1.subscriberCount) as totalNum FROM `qtrqlx` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.destinationScope in (0,2,3,4)"
						+ " and t1.recordedDateTime <= '"+endDay+"' "
						+ " and t1.recordedDateTime >= '"+startDay+"' "
						+ " group by t2.districtId"
						+ " order by t2.districtId asc;";
				break;
			}
			rs = state.executeQuery(sql);
			// the map of districtId and totalNum
			Map<String, String> totalNumMap = new HashMap<>();
			String districtId = "";
			while (rs.next()) {
				districtId = rs.getString("districtId");
				totalNum = rs.getString("totalNum");
				totalNumMap.put(districtId, totalNum);
			}
			String[] xqIds = {"1001", "1002", "1003", "1004", "1005", "1006", "1007", "1008"};
			for(int i=0; i<xqIds.length; i++){
				String xqId = xqIds[i];
				totalNum = totalNumMap.get(xqId);
				/*判断是否有数据可以update*/
				boolean isExist = false;
				sql = "select * from "+tableName+""
						+ " where id = '"+xqId+"' and recordYear = '"+recordYear+"' and recordMonth = '"+recordMonth+"' "
								+ "and level = '2' and recordIdentifier = '"+recordIdentifier+"'";
				rs = state.executeQuery(sql);
				while(rs.next()){
					isExist = true;
					break;
				}
				if(dateArr[2] == 1 || isExist == false){ //若是每月第一天or不存在，则insert
					sql = "insert into "+tableName+" values("+xqId+", '"+recordYear+"', '"+recordMonth+"',"
							+ " '"+totalNum+"', '2', '"+recordIdentifier+"', null)";
					state.executeUpdate(sql);
					log.info(infoStr + " insert success");
				}
				else{ //不是第一天，为update
					sql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ "where id = '"+xqId+"' and recordYear = '"+recordYear+"' and recordMonth = '"+recordMonth+"' and level = '2'"
									+ " and recordIdentifier = '"+recordIdentifier+"'";
					state.executeUpdate(sql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	/*市区按天function*/
	private static void sqDailyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		switch(tjId){
		case 2:
			tableName = "tj_jdrs_daily";
			infoStr = "市区-接待人数-按天";
			break;
		case 10:
			tableName = "tj_lmlyrs_daily";
			infoStr = "市区-来梅旅游人数-按天";
			break;
		case 16:
			tableName = "tj_cmrs_daily";
			infoStr = "市区-出梅人数-按天";
			break;
		}
		try{
			String selectSql = "";
			switch(tjId){
			case 2:
				selectSql = "SELECT t1.recordedDateTime, sum(t1.subscriberCount) as totalNum "
						+ "FROM `qtrqly` t1 "
						+ "where t1.sourceScope = 5 "
						+ "group by t1.recordedDateTime "
						+ "order by t1.recordedDateTime asc;";
				break;
			case 10:
				selectSql = "SELECT t1.recordedDateTime, sum(t1.subscriberCount) as totalNum "
						+ "FROM `qtrqly` t1 "
						+ "where t1.sourceScope in (2,3,4)"
						+ "group by t1.recordedDateTime "
						+ "order by t1.recordedDateTime asc;";
				break;
			case 16:
				selectSql = "SELECT t1.recordedDateTime, sum(t1.subscriberCount) as totalNum FROM `qtrqlx` t1 "
						+ " where t1.destinationScope in (0,2,3,4)"
						+ " group by t1.recordedDateTime"
						+ " order by t1.recordedDateTime asc;";
				break;
			}
			rs = state.executeQuery(selectSql);
			String recordedDateTime = "";
			String totalNum = "";
			String tmpSql = "";
			ResultSet tmpRs = null;
			String dateOnly = "";
			String recordIdentifier = "";
			boolean isExist = false;
			Statement tmpState = state = conn.createStatement(); // 容器
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				dateOnly = getDateOnly(recordedDateTime);
				/*首先查该条记录是否已经存储在数据库中*/
				isExist = false;
				tmpSql = "SELECT * from "+tableName+" "
						+ "where id = '1000' and recordDate = '"+dateOnly+"' and recordIdentifier = '"+recordIdentifier+"'";
				tmpRs = tmpState.executeQuery(tmpSql);
				while(tmpRs.next()){
					isExist = true;
					break;
				}
				if(isExist == false){
					tmpSql = "insert into "+tableName+" values('1000', '"+dateOnly+"', '"+totalNum+"', '1', "
							+ "'"+recordIdentifier+"', null)";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " insert success");
				}
				else{
					tmpSql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ " where id = '1000' and recordDate = '"+dateOnly+"' and recordIdentifier = '"+recordIdentifier+"'";
					tmpState.executeUpdate(tmpSql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
	}

	/*市区按月function*/
	private static void sqMonthlyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = "";
		switch(tjId){
		case 1:
			tableName = "tj_jdrs_monthly";
			infoStr = "市局-接待人数-按月";
			maxTimeTable = "qtrqly";
			break;
		case 9:
			tableName = "tj_lmlyrs_monthly";
			infoStr = "市区-来梅旅游人数-按月";
			maxTimeTable = "qtrqly";
			break;
		case 15:
			tableName = "tj_cmrs_monthly";
			infoStr = "市区-出梅人数-按月";
			maxTimeTable = "qtrqlx";
			break;
		}
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] dateArr = getDateArr(newestDateTime);
			String recordYear = String.valueOf(dateArr[0]);
			String recordMonth = String.valueOf(dateArr[1]);
			String startDay = getCurMonth1stDay(newestDateTime);
			String endDay = newestDateTime;
			String totalNum = "";
			String recordIdentifier = getRecordIdentifier(startDay);
			// get totalNum
			switch(tjId){
			case 1:
				sql = "SELECT sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 "
						+ "where t1.sourceScope = 5 and t1.recordedDateTime <= '" + endDay + "' "
						+ "and t1.recordedDateTime >= '" + startDay + "'";
				break;
			case 9:
				sql = "SELECT sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 "
						+ "where t1.sourceScope in (2,3,4) and t1.recordedDateTime <= '" + endDay + "' "
						+ "and t1.recordedDateTime >= '" + startDay + "'";
				break;
			case 15:
				sql = "SELECT sum(t1.subscriberCount) as totalNum FROM `qtrqlx` t1 "
						+ " where t1.destinationScope in (0,2,3,4) and t1.recordedDateTime<='" + endDay + "' "
						+ " and t1.recordedDateTime>='" + startDay + "'";
				break;
			}
			rs = state.executeQuery(sql);
			while (rs.next()) {
				totalNum = rs.getString("totalNum");
				break;
			}
			/*判断是否有数据可以update*/
			boolean isExist = false;
			sql = "select * from " + tableName + "" + " where id = '1000' and recordYear = '" + recordYear
					+ "' and recordMonth = '" + recordMonth + "' " + "and level = '1' and recordIdentifier = '"
					+ recordIdentifier + "'";
			rs = state.executeQuery(sql);
			while(rs.next()){
				isExist = true;
				break;
			}
			if(dateArr[2] == 1 || isExist == false){ //若是每月第一天or不存在，则insert
				sql = "insert into "+tableName+" values(1000, '"+recordYear+"', '"+recordMonth+"',"
						+ " '"+totalNum+"', '1', '"+recordIdentifier+"', null)";
				state.executeUpdate(sql);
				log.info(infoStr + " insert success");
			}
			else{ //不是第一天，为update
				sql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
						+ "where id = '1000' and recordYear = '"+recordYear+"' and recordMonth = '"+recordMonth+"' and level = '1'"
								+ " and recordIdentifier = '"+recordIdentifier+"'";
				state.executeUpdate(sql);
				log.info(infoStr + " update success");
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	/*景区按月function*/
	private static void jqMonthlyFunc(int tjId){
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = ""; /*从哪个表获取最新时间*/
		switch (tjId) {
		case 6:/*景区-接待人数-按月*/
			tableName = "tj_jdrs_monthly";
			maxTimeTable = "qtrqly";
			infoStr = "景区-接待人数-按月";
			break;
		case 13:/*景区-来梅旅游人数-按月 */
			tableName = "tj_lmlyrs_monthly";
			maxTimeTable = "qtrqly";
			infoStr = "景区-来梅旅游人数-按月";
			break;
		case 19:
			tableName = "tj_cmrs_monthly";
			maxTimeTable = "qtrqlx";
			infoStr = "景区-出梅人数-按月";
			break;
		}
		try{
			/*首先获取最新时间*/
			String sql = "select max(recordedDateTime) as newestDateTime from "+maxTimeTable+";";
			rs = state.executeQuery(sql);
			String newestDateTime =  "";
			while(rs.next()){
				newestDateTime = rs.getString("newestDateTime");
				break;
			}
			newestDateTime = removeDot(newestDateTime);
			int[] dateArr = getDateArr(newestDateTime);
			String recordYear = String.valueOf(dateArr[0]);
			String recordMonth = String.valueOf(dateArr[1]);
			String startDay = getCurMonth1stDay(newestDateTime);
			String endDay = newestDateTime;
			String totalNum = "";
			String recordIdentifier = getRecordIdentifier(startDay);
			// get totalNum
			switch(tjId){
			case 6:
				sql = "SELECT t2.id, t2.areaId,t2.spotName,sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId) "
						+ " where t1.recordedDateTime <= '"+endDay+"' "
						+ " and t1.recordedDateTime >= '"+startDay+"' "
						+ " and t1.sourceScope = 5 "
						+ " group by t2.id,t2.spotName;";
				break;
			case 13:
				sql = "SELECT t2.id, t2.areaId,t2.spotName,sum(t1.subscriberCount) as totalNum FROM `qtrqly` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId) "
						+ " where t1.recordedDateTime <= '"+endDay+"' "
						+ " and t1.recordedDateTime >= '"+startDay+"' "
						+ " and t1.sourceScope in (2,3,4)"
						+ " group by t2.id,t2.spotName;";
				break;
			case 19:
				sql = "SELECT t2.id, sum(t1.subscriberCount) as totalNum FROM `qtrqlx` t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.destinationScope in (0,2,3,4)"
						+ " and t1.recordedDateTime <= '"+endDay+"' "
						+ " and t1.recordedDateTime >= '"+startDay+"' "
						+ " group by t2.id"
						+ " order by t2.id asc;";
				break;
			}
			rs = state.executeQuery(sql);
			// the map of id and totalNum
			Map<String, String> totalNumMap = new HashMap<>();
			String id = "";
			while (rs.next()) {
				id = rs.getString("id");
				totalNum = rs.getString("totalNum");
				totalNumMap.put(id, totalNum);
			}
			for(int i=1; i<=30; i++){
				String jqId = String.valueOf(i);
				totalNum = totalNumMap.get(jqId);
				/*判断是否有数据可以update*/
				boolean isExist = false;
				sql = "select * from "+tableName+""
						+ " where id = '"+jqId+"' and recordYear = '"+recordYear+"' and recordMonth = '"+recordMonth+"' "
								+ "and level = '3' and recordIdentifier = '"+recordIdentifier+"'";
				rs = state.executeQuery(sql);
				while(rs.next()){
					isExist = true;
					break;
				}
				if(dateArr[2] == 1 || isExist == false){ //若是每月第一天or不存在，则insert
					sql = "insert into "+tableName+" values("+jqId+", '"+recordYear+"', '"+recordMonth+"',"
							+ " '"+totalNum+"', '3', '"+recordIdentifier+"', null)";
					state.executeUpdate(sql);
					log.info(infoStr + " insert success");
				}
				else{ //不是第一天，为update
					sql = "update "+tableName+" set subscriberCount = '"+totalNum+"' "
							+ "where id = '"+jqId+"' and recordYear = '"+recordYear+"' and recordMonth = '"+recordMonth+"' and level = '3'"
									+ " and recordIdentifier = '"+recordIdentifier+"'";
					state.executeUpdate(sql);
					log.info(infoStr + " update success");
				}
			}
			log.info(infoStr + " over");
		} catch (Exception e){
			log.info(infoStr + " fail");
			e.printStackTrace();
		} finally {
			closeDB();
		}
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
	
	/*获取datetime的日期部分*/
	private static String getDateOnly(String str){
		String res = "";
		String[] strArr = str.split(" ");
		res = strArr[0];
		return res;
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
	
	private static String getDateStr(Date sj){
		String str = simpleFormat.format(sj);
		return str;
	}
	
	private static String removeDot(String res){
		String[] strs = res.split("\\.");
		return strs[0];
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
	
	/*获取str所在月的第一个日期*/
	private static String getCurMonth1stDay(String str) {
		String res = "";
		String[] str1Arr = str.split(" ");
		String str1 = str1Arr[0];
		String[] str2Arr = str1.split("-");
		res = str2Arr[0] + "-" + str2Arr[1] + "-00 00:00:00"; 
		return res;
	}
}
