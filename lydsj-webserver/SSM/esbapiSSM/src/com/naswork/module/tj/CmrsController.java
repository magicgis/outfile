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
import com.naswork.model.TjCmrsDaily;
import com.naswork.model.TjCmrsMonthly;
import com.naswork.service.TjCmrsDailyService;
import com.naswork.service.TjCmrsMonthlyService;

/*出梅人数*/
@RestController
@RequestMapping("/v1/cmrs")
public class CmrsController extends BaseController {
	@Resource
	private TjCmrsMonthlyService tjCmrsMonthlyService;
	@Resource
	private TjCmrsDailyService tjCmrsDailyService;
	
	/*定义time_range的mapping*/
	private final static int RealTimeId = 1;
	private final static int YesterdayId = 2;
	private final static int LastMonthId = 3;
	private final static int DailyId = 4;
	private final static int MonthlyId = 5;
	/*取多少天的数据*/
	private final static int DaysNum = -9;
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
			res = getCmrsLastMonth(id);
			break;
		case DailyId:
			res = getCmrsDaily(id);
			break;
		case MonthlyId:
			res = getCmrsMonthly(id);
			break;
		}
		return res;
	}
	
	private HashMap<String, Object> getCmrsDaily(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Map<String, Integer> map = new LinkedHashMap<>();
		String startDay = MyTools.getStartDate(DaysNum);
		String str = "";
		Integer totalNum = 0;
		
		List<TjCmrsDaily> list = tjCmrsDailyService.selectByIdAndStartDay(id, startDay);
		Date date = new Date();
		int i=0;
		for(TjCmrsDaily tmp: list){
			date = tmp.getRecorddate();
			str = MyTools.getChineseDateStr(date);
			totalNum = tmp.getSubscribercount();
			map.put(str, totalNum);
			i++;
		}
		String[] arr = MyTools.getDateArr(date);
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.YEAR, Integer.parseInt(arr[0]));
		cd.set(Calendar.MONTH, Integer.parseInt(arr[1])-1 );
		cd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr[2]));
		/*
		int cont = -1*(DaysNum+2) + 1;
		while(i<cont){
			cd.add(Calendar.DATE, 1);
			i++;
			date = cd.getTime();
			str = MyTools.getChineseDateStr(date);
			map.put(str, 0);
		}
		*/
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	private HashMap<String, Object> getCmrsLastMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] arr = MyTools.getDateArr(date);
		String idStr = arr[0]+arr[1]+"00000000";
		TjCmrsMonthly tmp = tjCmrsMonthlyService.selectByIdAndIdstr(id, idStr);
		Integer totalNum = 0;
		String str = arr[1] + "月";
		if(tmp!=null && tmp.getSubscribercount()!=null) totalNum = tmp.getSubscribercount();
		Map<String, Integer> map = new LinkedHashMap<>();
		map.put(str, totalNum);
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	/*获取MonthsNum个月的数据*/
	private HashMap<String, Object> getCmrsMonthly(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Map<String, Integer> map = new LinkedHashMap<>();
		List<TjCmrsMonthly> list = tjCmrsMonthlyService.selectSomeMonths(id, MonthsNum);
		String str = "";
		Integer num = 0;
		for(TjCmrsMonthly tmp: list){
			str = ""+tmp.getRecordyear()+"年"+tmp.getRecordmonth()+"月";
			num = tmp.getSubscribercount();
			if(num == null) num = 0;
			map.put(str, num);
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}
	
	
}
