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
	
	/*MAIN����*/
	public static void main(String[] args){
		int tjId = Integer.parseInt(args[0]);
		try {
			switch(tjId){ //ѡ�������һ��ͳ��
			case 1:
				sqMonthlyFunc(1); /*����-�Ӵ�����-����*/
				break;
			case 2:
				sqDailyFunc(2); /*����-�Ӵ�����-����*/
				break;
			case 3:
				xqMonthlyFunc(3); /*����-�Ӵ�����-����*/
				break;
			case 4:
				xqDailyFunc(4); /*����-�Ӵ�����-����*/
				break;
			case 5:
				xqRealtimeFunc(5); /*����-�Ӵ�����-ʵʱ*/
				break;
			case 6:
				jqMonthlyFunc(6); /*����-�Ӵ�����-����*/
				break;
			case 7:
				jqDailyFunc(7); /*����-�Ӵ�����-����*/
				break;
			case 8:
				jqRealtimeFunc(8); /*����-�Ӵ�����-ʵʱ*/
				break;
			case 9:
				sqMonthlyFunc(9); /*����-��÷��������-����*/
				break;
			case 10:
				sqDailyFunc(10); /*����-��÷��������-����*/
				break;
			case 11:
				xqMonthlyFunc(11); /*����-��÷��������-����*/
				break;
			case 12:
				xqDailyFunc(12); /*����-��÷��������-����*/
				break;
			case 13:
				jqMonthlyFunc(13); /*����-��÷��������-����*/
				break;
			case 14:
				jqDailyFunc(14); /*����-��÷��������-����*/
				break;
			case 15:
				sqMonthlyFunc(15); /*����-��÷����-����*/
				break;
			case 16:
				sqDailyFunc(16); /*����-��÷����-����*/
				break;
			case 17:
				xqMonthlyFunc(17); /*����-��÷����-����*/
				break;
			case 18:
				xqDailyFunc(18); /*����-��÷����-����*/
				break;
			case 19:
				jqMonthlyFunc(19); /*����-��÷����-����*/
				break;
			case 20:
				jqDailyFunc(20); /*����-��÷����-����*/
				break;
			case 21:
				kydfxMonthly(1, 2); /*ʡ��-����-��Դ�ط���-����*/
				kydfxMonthly(1, 3); /*����-����-��Դ�ط���-����*/
				break;
			case 22:
				kydfxDaily(1, 2); /*ʡ��-����-��Դ�ط���-����*/
				kydfxDaily(2, 3); /*����-����-��Դ�ط���-����*/
				break;
			case 23:
				kydfxRealtime(1, 2); /*ʡ��-����-��Դ�ط���-ʵʱ*/
				kydfxRealtime(1, 3);/*����-����-��Դ�ط���-ʵʱ*/
				break;
			case 24:
				kydfxMonthly(2, 2); /*ʡ��-����-��Դ�ط���-����*/
				kydfxMonthly(2, 3); /*����-����-��Դ�ط���-����*/
				break;
			case 25:
				kydfxDaily(2, 2); /* ʡ��-����-��Դ�ط���-���� */
				kydfxDaily(2, 3); /* ����-����-��Դ�ط���-���� */
				break;
			case 26:
				kydfxRealtime(2, 2); /*ʡ��-����-��Դ�ط���-ʵʱ*/
				kydfxRealtime(2, 3);/*����-����-��Դ�ط���-ʵʱ*/
				break;
			case 27:
				kydfxMonthly(3, 2); /*ʡ��-����-��Դ�ط���-����*/
				kydfxMonthly(3, 3); /*����-����-��Դ�ط���-����*/
				break;
			case 28:
				kydfxDaily(3, 2); /* ʡ��-����-��Դ�ط���-���� */
				kydfxDaily(3, 3); /* ����-����-��Դ�ط���-���� */
				break;
			case 29:
				kydfxRealtime(3, 2); /*ʡ��-����-��Դ�ط���-ʵʱ*/
				kydfxRealtime(3, 3);/*����-����-��Դ�ط���-ʵʱ*/
				break;
			case 30:
				monthlyFunc(1, 30); /*��Դ�ضԱȷ���-����*/
				monthlyFunc(2, 30);
				monthlyFunc(3, 30);
				break;
			case 31:
				dailyFunc(1, 31); /*��Դ�ضԱȷ���-����*/
				dailyFunc(2, 31);
				dailyFunc(3, 31);
				break;
			case 32:
				monthlyFunc(1, 32); /*�οͶ���ʱ��-����*/
				monthlyFunc(2, 32);
				monthlyFunc(3, 32);
				break;
			case 33:
				dailyFunc(1, 33); /*�οͶ���ʱ��-����*/
				dailyFunc(2, 33);
				dailyFunc(3, 33);
				break;
			case 34:
				monthlyFunc(1, 34); /*�ο���԰ʱ��-����*/
				monthlyFunc(2, 34);
				monthlyFunc(3, 34);
				break;
			case 35:
				dailyFunc1(1); /* �ο���԰ʱ��-���� */
				dailyFunc1(2);
				dailyFunc1(3);
				break;
			case 36:
				realTimeFunc(1); /*��÷��������-ʵʱ*/
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
		String infoStr = "��÷��������-ʵʱ";
		switch (level) {
		case 1:
			infoStr = "����-" + infoStr;
			break;
		case 2:
			infoStr = "����-" + infoStr;
			break;
		case 3:
			infoStr = "����-" + infoStr;
			break;
		}
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
			Statement tmpState = state = conn.createStatement(); // ����
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
				/*���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ���*/
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
		String infoStr = "-�ο���԰ʱ��-����";
		switch (level) {
		case 1:
			infoStr = "����" + infoStr;
			break;
		case 2:
			infoStr = "����" + infoStr;
			break;
		case 3:
			infoStr = "����" + infoStr;
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
				Statement tmpState = state = conn.createStatement(); // ����
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
					/* ���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ��� */
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

	/*monthly function for ��Դ�ضԱȷ���&�οͶ���ʱ��*/
	private static void dailyFunc(int level, int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String tag = "";
		switch (tjId) {
		case 31:
			tableName = "tj_kyddbfx_daily";
			infoStr = "��Դ�ضԱȷ���-����";
			break;
		case 33:
			tableName = "tj_ykdlsj_daily";
			infoStr = "�οͶ���ʱ��-����";
			break;
		}
		switch (level) {
		case 1:
			infoStr = "����" + infoStr;
			break;
		case 2:
			infoStr = "����" + infoStr;
			break;
		case 3:
			infoStr = "����" + infoStr;
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
			Statement tmpState = state = conn.createStatement(); // ����
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
				/* ���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ��� */
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

	/*monthly function for ��Դ�ضԱȷ���&�οͶ���ʱ��*/
	private static void monthlyFunc(int level, int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = "";
		String tag = "" + tjId + level;
		switch (tjId) {
		case 30:
			tableName = "tj_kyddbfx_monthly";
			infoStr = "��Դ�ضԱȷ���-����";
			maxTimeTable = "qtrqly";
			break;
		case 32:
			tableName = "tj_ykdlsj_monthly";
			infoStr = "�οͶ���ʱ��-����";
			maxTimeTable = "qtzl";
			break;
		case 34:
			tableName = "tj_ykrysj_monthly";
			infoStr = "�ο���԰ʱ��-����";
			maxTimeTable = "sslr";
			break;
		}
		switch (level) {
		case 1:
			infoStr = "����-" + infoStr;
			break;
		case 2:
			infoStr = "����-" + infoStr;
			break;
		case 3:
			infoStr = "����-" + infoStr;
			break;
		}
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
			/*��Դ�ضԱȷ���*/
			case "301": /*����*/
				sql = "select t.sourceScope, sum(t.subscriberCount) as totalNum from qtrqly t "
						+ " where t.sourceScope in(2,3,4) and t.recordedDateTime>='" + startDay + "' and "
						+ " t.recordedDateTime<='" + endDay + "' " + " group by t.sourceScope;";
				break;
			case "302": /*����*/
				sql = "select t2.districtId, t1.sourceScope,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in(2,3,4) and t1.recordedDateTime >='" + startDay + "' "
						+ "and t1.recordedDateTime <='" + endDay + "'" + " group by t2.districtId,t1.sourceScope;";
				break;
			case "303": /* ���� */
				sql = "select t2.id,t1.sourceScope,sum(t1.subscriberCount) as totalNum from qtrqly t1 "
						+ " join jqqd t2 on (t1.areaId = t2.areaId)"
						+ " where t1.sourceScope in(2,3,4) and t1.recordedDateTime>='"+startDay+"' and t1.recordedDateTime<='"+endDay+"'"
						+ " group by t2.id,t1.sourceScope;";
				break;
			/* �οͶ���ʱ�� */
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
			/*�ο���԰ʱ��*/
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
			/*����Map*/
			for(String key: map.keySet()){
				String[] strs = key.split(" ");
				id = strs[0];
				sourceScope = strs[1];
				totalNum = map.get(key);
				/*�ж��Ƿ������ݿ���update*/
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
				if (dateArr[2] == 1 || isExist == false) { // ����ÿ�µ�һ��or�����ڣ���insert
					sql = "insert into " + tableName + " values('" + id + "', '" + recordYear + "', '" + recordMonth
							+ "'," + " '" + totalNum + "', '" + sourceScope + "', '" + level + "', '" + recordIdentifier
							+ "', null)";
					state.executeUpdate(sql);
					log.info(infoStr + " insert success");
				} else { // ���ǵ�һ�죬Ϊupdate
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

	/*����-��Դ�ط���-ʵʱ*/
	private static void kydfxRealtime(int level, int sourceScope) {
		connDB();
		String tableName = "tj_kydfx_realtime";
		String infoStr = "��Դ�ط���-ʵʱ";
		switch(level){
		case 1: infoStr = "����-" + infoStr; break;
		case 2: infoStr = "����-" + infoStr; break;
		case 3: infoStr = "����-" + infoStr; break;
		}
		if(sourceScope == 2) infoStr += "-ʡ��";
		else infoStr += "-����";
		String maxTimeTable = "ssrqly";
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
			Statement tmpState = state = conn.createStatement(); // ����
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
				/*���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ���*/
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

	/*��Դ�ط���-����*/
	private static void kydfxDaily(int level, int sourceScope) {
		connDB();
		String tableName = "tj_kydfx_daily";
		String infoStr = "��Դ�ط���-����";
		switch(level){
		case 1: infoStr = "����-" + infoStr; break;
		case 2: infoStr = "����-" + infoStr; break;
		case 3: infoStr = "����-" + infoStr; break;
		}
		if(sourceScope == 2) infoStr += "-ʡ��";
		else infoStr += "-����";
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
			Statement tmpState = state = conn.createStatement(); // ����
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
				/*���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ���*/
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

	/*����-��Դ�ط���*/
	private static void kydfxMonthly(int level, int sourceScope) {
		connDB();
		String tableName = "tj_kydfx_monthly";
		String infoStr = "��Դ�ط���-����";
		switch(level){
		case 1: infoStr = "����-" + infoStr; break;
		case 2: infoStr = "����-" + infoStr; break;
		case 3: infoStr = "����-" + infoStr; break;
		}
		if(sourceScope == 2) infoStr += "-ʡ��";
		else infoStr += "-����";
		String maxTimeTable = "qtrqly";
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
			/*����Map*/
			for(String key: map.keySet()){
				String[] strs = key.split(" ");
				sourceName = strs[0];
				id = strs[1];
				totalNum = map.get(key);
				/*�ж��Ƿ������ݿ���update*/
				boolean isExist = false;
				sql = "select * from " + tableName + "" + " where id = '"+id+"' and recordYear = '" + recordYear
						+ "' and recordMonth = '" + recordMonth + "' " + "and sourceScope = '"+sourceScope+"' "
						+ "and sourceName = '"+ sourceName + "'";
				rs = state.executeQuery(sql);
				while(rs.next()){
					isExist = true;
					break;
				}
				if(dateArr[2] == 1 || isExist == false){ //����ÿ�µ�һ��or�����ڣ���insert
					sql = "insert into "+tableName+" values('"+id+"', '"+recordYear+"', '"+recordMonth+"', '"+sourceName+"', "
							+ " '"+totalNum+"', '"+sourceScope+"', '"+level+"', '"+recordIdentifier+"', null)";
					state.executeUpdate(sql);
					log.info(infoStr + " insert success");
				}
				else{ //���ǵ�һ�죬Ϊupdate
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

	/*����ʵʱfunction*/
	private static void jqRealtimeFunc(int tjId) {
		connDB();
		String tableName = "";
		String maxTimeTable = "";
		String infoStr = "";
		switch(tjId){
		case 8: 
			tableName = "tj_jdrs_realtime";
			maxTimeTable = "ssrqly";
			infoStr = "����-�Ӵ�����-ʵʱ";
			break;
		}
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
			Statement tmpState = state = conn.createStatement(); // ����
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				id = rs.getString("id");
				/*���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ���*/
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

	/*��������function*/
	private static void jqDailyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		switch(tjId){
		case 7:
			tableName = "tj_jdrs_daily";
			infoStr = "����-�Ӵ�����-����";
			break;
		case 14:
			tableName = "tj_lmlyrs_daily";
			infoStr = "����-��÷��������-����";
			break;
		case 20:
			tableName = "tj_cmrs_daily";
			infoStr = "����-��÷��������-����";
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
			Statement tmpState = state = conn.createStatement(); // ����
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				id = rs.getString("id");
				dateOnly = getDateOnly(recordedDateTime);
				/*���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ���*/
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

	/*����ʵʱfunction*/
	private static void xqRealtimeFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = "";
		switch(tjId){
		case 5:
			tableName = "tj_jdrs_realtime";
			infoStr = "����-�Ӵ�����-ʵʱ";
			maxTimeTable = "ssrqly";
			break;
		}
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
			Statement tmpState = state = conn.createStatement(); // ����
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				districtId = rs.getString("districtId");
				/*���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ���*/
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

	/*��������function*/
	private static void xqDailyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		switch(tjId){
		case 4:
			tableName = "tj_jdrs_daily";
			infoStr = "����-�Ӵ�����-����";
			break;
		case 12:
			tableName = "tj_lmlyrs_daily";
			infoStr = "����-��÷��������-����";
			break;
		case 18:
			tableName = "tj_cmrs_daily";
			infoStr = "����-��÷����-����";
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
			Statement tmpState = state = conn.createStatement(); // ����
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				districtId = rs.getString("districtId");
				dateOnly = getDateOnly(recordedDateTime);
				/*���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ���*/
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

	/*��������function*/
	private static void xqMonthlyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = "";
		switch(tjId){
		case 3:
			tableName = "tj_jdrs_monthly";
			infoStr = "����-�Ӵ�����-����";
			maxTimeTable = "qtrqly";
			break;
		case 11:
			tableName = "tj_lmlyrs_monthly";
			infoStr = "����-��÷��������-����";
			maxTimeTable = "qtrqly";
			break;
		case 17:
			tableName = "tj_cmrs_monthly";
			infoStr = "����-��÷����-����";
			maxTimeTable = "qtrqlx";
			break;
		}
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
				/*�ж��Ƿ������ݿ���update*/
				boolean isExist = false;
				sql = "select * from "+tableName+""
						+ " where id = '"+xqId+"' and recordYear = '"+recordYear+"' and recordMonth = '"+recordMonth+"' "
								+ "and level = '2' and recordIdentifier = '"+recordIdentifier+"'";
				rs = state.executeQuery(sql);
				while(rs.next()){
					isExist = true;
					break;
				}
				if(dateArr[2] == 1 || isExist == false){ //����ÿ�µ�һ��or�����ڣ���insert
					sql = "insert into "+tableName+" values("+xqId+", '"+recordYear+"', '"+recordMonth+"',"
							+ " '"+totalNum+"', '2', '"+recordIdentifier+"', null)";
					state.executeUpdate(sql);
					log.info(infoStr + " insert success");
				}
				else{ //���ǵ�һ�죬Ϊupdate
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

	/*��������function*/
	private static void sqDailyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		switch(tjId){
		case 2:
			tableName = "tj_jdrs_daily";
			infoStr = "����-�Ӵ�����-����";
			break;
		case 10:
			tableName = "tj_lmlyrs_daily";
			infoStr = "����-��÷��������-����";
			break;
		case 16:
			tableName = "tj_cmrs_daily";
			infoStr = "����-��÷����-����";
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
			Statement tmpState = state = conn.createStatement(); // ����
			while(rs.next()){
				recordedDateTime = rs.getString("recordedDateTime");
				recordedDateTime = removeDot(recordedDateTime);
				recordIdentifier = getRecordIdentifier(recordedDateTime);
				totalNum = rs.getString("totalNum");
				dateOnly = getDateOnly(recordedDateTime);
				/*���Ȳ������¼�Ƿ��Ѿ��洢�����ݿ���*/
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

	/*��������function*/
	private static void sqMonthlyFunc(int tjId) {
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = "";
		switch(tjId){
		case 1:
			tableName = "tj_jdrs_monthly";
			infoStr = "�о�-�Ӵ�����-����";
			maxTimeTable = "qtrqly";
			break;
		case 9:
			tableName = "tj_lmlyrs_monthly";
			infoStr = "����-��÷��������-����";
			maxTimeTable = "qtrqly";
			break;
		case 15:
			tableName = "tj_cmrs_monthly";
			infoStr = "����-��÷����-����";
			maxTimeTable = "qtrqlx";
			break;
		}
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
			/*�ж��Ƿ������ݿ���update*/
			boolean isExist = false;
			sql = "select * from " + tableName + "" + " where id = '1000' and recordYear = '" + recordYear
					+ "' and recordMonth = '" + recordMonth + "' " + "and level = '1' and recordIdentifier = '"
					+ recordIdentifier + "'";
			rs = state.executeQuery(sql);
			while(rs.next()){
				isExist = true;
				break;
			}
			if(dateArr[2] == 1 || isExist == false){ //����ÿ�µ�һ��or�����ڣ���insert
				sql = "insert into "+tableName+" values(1000, '"+recordYear+"', '"+recordMonth+"',"
						+ " '"+totalNum+"', '1', '"+recordIdentifier+"', null)";
				state.executeUpdate(sql);
				log.info(infoStr + " insert success");
			}
			else{ //���ǵ�һ�죬Ϊupdate
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

	/*��������function*/
	private static void jqMonthlyFunc(int tjId){
		connDB();
		String tableName = "";
		String infoStr = "";
		String maxTimeTable = ""; /*���ĸ����ȡ����ʱ��*/
		switch (tjId) {
		case 6:/*����-�Ӵ�����-����*/
			tableName = "tj_jdrs_monthly";
			maxTimeTable = "qtrqly";
			infoStr = "����-�Ӵ�����-����";
			break;
		case 13:/*����-��÷��������-���� */
			tableName = "tj_lmlyrs_monthly";
			maxTimeTable = "qtrqly";
			infoStr = "����-��÷��������-����";
			break;
		case 19:
			tableName = "tj_cmrs_monthly";
			maxTimeTable = "qtrqlx";
			infoStr = "����-��÷����-����";
			break;
		}
		try{
			/*���Ȼ�ȡ����ʱ��*/
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
				/*�ж��Ƿ������ݿ���update*/
				boolean isExist = false;
				sql = "select * from "+tableName+""
						+ " where id = '"+jqId+"' and recordYear = '"+recordYear+"' and recordMonth = '"+recordMonth+"' "
								+ "and level = '3' and recordIdentifier = '"+recordIdentifier+"'";
				rs = state.executeQuery(sql);
				while(rs.next()){
					isExist = true;
					break;
				}
				if(dateArr[2] == 1 || isExist == false){ //����ÿ�µ�һ��or�����ڣ���insert
					sql = "insert into "+tableName+" values("+jqId+", '"+recordYear+"', '"+recordMonth+"',"
							+ " '"+totalNum+"', '3', '"+recordIdentifier+"', null)";
					state.executeUpdate(sql);
					log.info(infoStr + " insert success");
				}
				else{ //���ǵ�һ�죬Ϊupdate
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

	/*�������ݿ�*/
	private static void connDB(){
		try {
			Class.forName(jdbc_class_name);
			String db = jdbc_db;
			conn = DriverManager.getConnection(db, db_user_name, db_psw);// ���ӵ����ݿ�
			state = conn.createStatement(); // ����
			log.info("connDB over");
		} catch (Exception e) {
			log.info("connDB fail");
			e.printStackTrace();
		}// ��������
	}
	
	/*�ر����ݿ�����*/
	private static void closeDB(){
		try{
			conn.close();
			log.info("connDB over");
		} catch (Exception e){
			log.info("connDB fail");
			e.printStackTrace();
		}
	}
	
	/*��ȡdatetime�����ڲ���*/
	private static String getDateOnly(String str){
		String res = "";
		String[] strArr = str.split(" ");
		res = strArr[0];
		return res;
	}
	
	/*�����ʶ��*/
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
	
	/*��ȡ��������*/
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
	
	/*��ȡstr�����µĵ�һ������*/
	private static String getCurMonth1stDay(String str) {
		String res = "";
		String[] str1Arr = str.split(" ");
		String str1 = str1Arr[0];
		String[] str2Arr = str1.split("-");
		res = str2Arr[0] + "-" + str2Arr[1] + "-00 00:00:00"; 
		return res;
	}
}
