package com.naswork.module.tj;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjLmlyrsDaily;
import com.naswork.model.TjLmlyrsMonthly;
import com.naswork.model.TjLmlyrsRealtime;
import com.naswork.service.TjLmlyrsDailyService;
import com.naswork.service.TjLmlyrsMonthlyService;
import com.naswork.service.TjLmlyrsRealtimeService;

/*来梅旅游人数*/
@RestController
@RequestMapping("/v1/lmlyrs")
public class LmlyrsController extends BaseController {
	@Resource
	private TjLmlyrsMonthlyService tjLmlyrsMonthlyService;
	@Resource
	private TjLmlyrsDailyService tjLmlyrsDailyService;
	@Resource
	private TjLmlyrsRealtimeService tjLmlyrsRealtimeService;
	
	/*定义time_range的mapping*/
	private final static int RealTimeId = 1;
	private final static int YesterdayId = 2;
	private final static int LastMonthId = 3;
	private final static int DailyId = 4;
	private final static int MonthlyId = 5;
	private final static int TodayId = 6;
	/*取多少天的数据*/
	private final static int DaysNum = -8;
	/*取多少月的数据*/
	private final static int MonthsNum = 7;
	
	@RequestMapping("/{id}/{time_range}")
	public HashMap<String, Object> getLmlyrs(@PathVariable Integer id, @PathVariable Integer time_range) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		/*case YesterdayId:
			res = getJdrsYesterday(id);
			break;*/
		case LastMonthId:
			res = getLmlyrsLastMonth(id);
			break;
		case DailyId:
			res = getLmlyrsDaily(id);
			break;
		case MonthlyId:
			res = getLmlyrsMonthly(id);
			break;
		case TodayId:
			res = getToday(id);
			break;
		}
		return res;
	}
	
	private HashMap<String, Object> getToday(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		String maxIdStr = tjLmlyrsRealtimeService.getMaxIdentifier();
		String str = "";
		String idStr = "";
		Integer num = 0;
		Map<String, Integer> map = new LinkedHashMap<>();
		Date date = new Date();
		String[] strs = MyTools.getDateArr(date);
		idStr = strs[0]+strs[1]+strs[2]+"000000";
		List<TjLmlyrsRealtime> list = tjLmlyrsRealtimeService.selectByIdStr(id, idStr);
		for(TjLmlyrsRealtime tmp: list){
			date = tmp.getRecorddatetime();
			str = getTimeOnly(date);
			idStr = getIdStr(date);
			num = tmp.getSubscribercount();
			if(num == null) num = 0;
			map.put(str, num);
			if(maxIdStr.equals(idStr) == true) break;
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	private String getIdStr(Date date) {
		String str = MyTools.getDateTimeStr(date);
		String[] arr = str.split(" ");
		String[] arr1 = arr[0].split("-");
		String[] arr2 = arr[1].split("\\:");
		String res = arr1[0]+arr1[1]+arr1[2]+arr2[0]+arr2[1]+arr2[2];
		return res;
	}

	private String getTimeOnly(Date date) {
		String res = "";
		String str = MyTools.getDateTimeStr(date);
		String[] arr = str.split(" ");
		res = arr[1];
		return res;
	}

	/*获取上个月的数据*/
	private HashMap<String, Object> getLmlyrsLastMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] arr = MyTools.getDateArr(date);
		String year = arr[0];
		String month = arr[1];
		TjLmlyrsMonthly tmp = tjLmlyrsMonthlyService.selectByIdAngYearAndMonth(id, year, month);
		Integer totalNum = 0;
		if(tmp!=null && tmp.getSubscribercount()!=0) totalNum = tmp.getSubscribercount();
		Map<String, Integer> map = new LinkedHashMap<>();
		map.put(month + "月", totalNum);
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	/*取DaysNum日数据*/
	private HashMap<String, Object> getLmlyrsDaily(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		String startDay = MyTools.getStartDate(DaysNum);
		List<TjLmlyrsDaily> list = tjLmlyrsDailyService.selectByIdFromStartDay(id, startDay);
		int i=0;
		Integer totalNum = 0;
		Date date = new Date();
		String str = "";
		Map<String, Integer> map = new LinkedHashMap<>();
		for(TjLmlyrsDaily tmp: list){
			date = tmp.getRecorddate();
			str = MyTools.getChineseDateStr(date);
			totalNum = tmp.getSubscribercount();
			map.put(str, totalNum);
			i++;
		}
		int cont = -1*(DaysNum+2) + 1;
		String[] arr = MyTools.getDateArr(date);
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.YEAR, Integer.parseInt(arr[0]));
		cd.set(Calendar.MONTH, Integer.parseInt(arr[1])-1);
		cd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
		while(i<cont){
			cd.add(Calendar.DATE, 1);
			date = cd.getTime();
			str = MyTools.getChineseDateStr(date);
			map.put(str, 0);
			i++;
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	/*取MonthsNum月数据*/
	private HashMap<String, Object> getLmlyrsMonthly(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Map<String, Integer> map = new LinkedHashMap<>();
		List<TjLmlyrsMonthly> list = tjLmlyrsMonthlyService.selectSomeMonth(id, MonthsNum);
		String str = "";
		Integer num = 0;
		for(TjLmlyrsMonthly tmp: list){
			str = ""+tmp.getRecordyear()+"年"+tmp.getRecordmonth()+"月";
			num = tmp.getSubscribercount();
			if(num == null) num = 0;
			map.put(str, num);
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}
	
}
