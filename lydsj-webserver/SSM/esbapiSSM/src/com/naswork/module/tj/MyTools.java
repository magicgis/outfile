package com.naswork.module.tj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTools {
	/*定义time_range的mapping*/
	public final static int RealTimeId = 1;
	public final static int YesterdayId = 2;
	public final static int LastMonthId = 3;
	public final static int DailyId = 4;
	public final static int MonthlyId = 5;
	/*取多少天的数据*/
	public final static int DaysNum = -8;
	/*取多少月的数据*/
	public final static int MonthsNum = 0;
	
	public static void getJsonFromMapLine(Map<String, Integer> map, HashMap<String, Object> res){
		List<String> xList = new ArrayList<>();
		List< List<Integer> > yList = new ArrayList<>();
		Integer totalNum = 0;
		List<Integer> yList1 = new ArrayList<>();
		for(String key: map.keySet()){
			totalNum = map.get(key);
			xList.add(key);
			yList1.add(totalNum);
		}
		yList.add(yList1);
		res.put("x", xList);
		res.put("y", yList);
	}
	
	public static String getStartDate(int num) {
		String res = "";
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, num);
		Date d = cd.getTime();
		res = getDateStr(d);
		return res;
	}

	public static String getChineseDateStr(Date sj) {
		String res = "";
		String str = getDateStr(sj);
		String[] strs = str.split("-");
		res = strs[1] + "月" + strs[2] + "日";
		return res;
	}

	public static String get1stDate(Date curDate){
		String curStr = getDateTimeStr(curDate);
		String[] arr = curStr.split(" ");
		String[] arr1 = arr[0].split("-");
		String res = arr1[0] + "-" + arr1[1] + "-01";
		return res;
	}
	
	public static String getDateTimeStr(Date sj){
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = simpleFormat.format(sj);
		return str;
	}
	
	public static String getDateStr(Date sj){
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = simpleFormat.format(sj);
		return str;
	}
	
	public static String[] getDateArr(Date sj){
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = simpleFormat.format(sj);
		String[] strs = str.split("-");
		return strs;
	}
}
