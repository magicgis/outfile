package com.naswork.module.tj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjJdrsDaily;
import com.naswork.model.TjJdrsMonthly;
import com.naswork.model.JsonFormat.Series;
import com.naswork.model.JsonFormat.XAxis;
import com.naswork.service.TjJdrsDailyService;
import com.naswork.service.TjJdrsMonthlyService;

/*接待人数*/
@RestController
@RequestMapping("/v1/jdrs")
public class JdrsController extends BaseController {
	@Resource
	private TjJdrsMonthlyService tjJdrsMonthlyService;
	@Resource
	private TjJdrsDailyService tjJdrsDailyService;
	
	/*定义time_range的mapping*/
	private final static int RealTimeId = 1;
	private final static int YesterdayId = 2;
	private final static int LastMonthId = 3;
	private final static int DailyId = 4;
	private final static int MonthlyId = 5;
	/*取多少天的数据*/
	private final static int DaysNum = -8;
	/*取多少月的数据*/
	private final static int MonthsNum = 7;
	
	@RequestMapping("/{id}/{time_range}")
	public HashMap<String, Object> getJdrs(@PathVariable Integer id, @PathVariable Integer time_range) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		/*case YesterdayId:
			res = getJdrsYesterday(id);
			break;*/
		case LastMonthId:
			res = getJdrsLastMonth(id);
			break;
		case DailyId:
			res = getJdrsDaily(id);
			break;
		case MonthlyId:
			res = getJdrsMonthly(id);
			break;
		}
		return res;
	}
	
	/*获取昨天的接待人数*/
	private HashMap<String, Object> getJdrsYesterday(Integer id) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date date = cd.getTime();
		String yesterday = getDateStr(date);
		Integer totalNum = tjJdrsDailyService.selectByIdAndDate(id, date);
		Map<String, Integer> map = new LinkedHashMap<>();
		String str = getChineseDateStr(date);
		if(totalNum == null) totalNum = 0;
		map.put(str, totalNum);
		getJsonFromMap(map, res);
		return res;
	}

	/*返回上一个月的数据*/
	private HashMap<String, Object> getJdrsLastMonth(Integer id) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] strs = getDateArr(date);
		TjJdrsMonthly tmp = tjJdrsMonthlyService.selectByYearAndMonth(id, strs[0], strs[1]);
		Integer totalNum = 0;
		if(tmp!=null && tmp.getSubscribercount() != null) totalNum = tmp.getSubscribercount();
		String str = strs[1] + "月";
		Map<String, Integer> map = new LinkedHashMap<>();
		map.put(str, totalNum);
		getJsonFromMap(map, res);
		return res;
	} 

	/*返回MonthsNum月数据*/
	private HashMap<String, Object> getJdrsMonthly(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Map<String, Integer> map = new LinkedHashMap<>();
		List<TjJdrsMonthly> list = tjJdrsMonthlyService.selectSomeMonths(id, MonthsNum);
		String str = "";
		Integer num = 0;
		for(TjJdrsMonthly tmp: list){
			str = ""+tmp.getRecordyear()+"年"+tmp.getRecordmonth()+"月";
			num = tmp.getSubscribercount();
			if(num == null) num = 0;
			map.put(str, num);
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	/*返回DaysNum日数据*/
	private HashMap<String, Object> getJdrsDaily(int id) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		Date date = new Date();
		String startDay = getStartDate();
		List<TjJdrsDaily> list = tjJdrsDailyService.selectById(id, startDay);
		Map<String, Integer> map = new LinkedHashMap<>();
		String dateStr = "";
		Integer totalNum = 0;
		int i = 0;
		for(TjJdrsDaily tmp: list){
			date = tmp.getRecorddate();
			dateStr = getChineseDateStr(date);
			totalNum =tmp.getSubscribercount();
			map.put(dateStr, totalNum);
			i++;
		}
		String[] dateArr = getDateArr(date);
		Calendar cd = Calendar.getInstance();
		int year = Integer.parseInt(dateArr[0]);
		int month = Integer.parseInt(dateArr[1]);
		int day = Integer.parseInt(dateArr[2]);
		cd.set(Calendar.YEAR, year);
		cd.set(Calendar.MONTH, month-1);
		cd.set(Calendar.DAY_OF_MONTH, day+1);
		int cont = -1*DaysNum -1;
		while(i<cont){
			date = cd.getTime();
			dateStr = getChineseDateStr(date);
			cd.add(Calendar.DATE, 1);
			totalNum = 0;
			map.put(dateStr, totalNum);
			i++;
		}
		getJsonFromMap(map, res);
		return res;
	}
	
	private void getJsonFromMap(Map<String, Integer> map, HashMap<String, Object> res){
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
	
	private String getStartDate() {
		String res = "";
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, DaysNum);
		Date d = cd.getTime();
		res = getDateStr(d);
		return res;
	}

	private String getChineseDateStr(Date sj) {
		String res = "";
		String str = getDateStr(sj);
		String[] strs = str.split("-");
		res = strs[1] + "月" + strs[2] + "日";
		return res;
	}

	private String get1stDate(Date curDate){
		String curStr = getDateTimeStr(curDate);
		String[] arr = curStr.split(" ");
		String[] arr1 = arr[0].split("-");
		String res = arr1[0] + "-" + arr1[1] + "-01";
		return res;
	}
	
	private String getDateTimeStr(Date sj){
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = simpleFormat.format(sj);
		return str;
	}
	
	private String getDateStr(Date sj){
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = simpleFormat.format(sj);
		return str;
	}
	
	private String[] getDateArr(Date sj){
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = simpleFormat.format(sj);
		String[] strs = str.split("-");
		return strs;
	}
	
//	测试接待人数
//	@RequestMapping("/totalJdNum/{id}")
//	@ResponseBody
//	public HashMap<String, Integer> getTotalJdNum(@PathVariable Integer id){
//		String yearstr=getDateArr(new Date())[0];
//		int year=Integer.parseInt(yearstr);
//		Integer totalJdNum=tjJdrsMonthlyService.getTotalJdNum(id, year);
//		HashMap<String, Integer> hashMap=new HashMap<String, Integer>();
//		hashMap.put("totalJdNum", totalJdNum);
//		return hashMap;
//		
//	}
}
