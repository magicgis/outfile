package com.naswork.module.tj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.service.PlateAnalyticService;

@RestController
@RequestMapping("/v1/zjykyddbfx")
public class ZjykyddbfxController extends BaseController {
	@Resource
	private PlateAnalyticService paService;
	private static final  SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static final  SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/*定义time_range的mapping*/
	private final static int RealTimeId = 1;
	private final static int YesterdayId = 2;
	private final static int LastMonthId = 3;
	private final static int DailyId = 4;
	private final static int MonthlyId = 5;
	/*取多少天的数据*/
	//private final static int DaysNum = -8;
	/*取多少月的数据*/
	//private final static int MonthsNum = 0;
	
	@RequestMapping("/{id}/{time_range}")
	public HashMap<String, Object> getKydfxTop5(@PathVariable Integer id, @PathVariable Integer time_range) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		case YesterdayId:
			res = getDaily(id);
			break;
		case LastMonthId:
			res = getLastMonth(id);
			break;
			/*case DailyId:
			res = getDaily(id);
			break;
		case MonthlyId:
			res = getKydfxTop5CurMonth(id, area_range);
			break;*/
		}
		return res;
	}
	
	private HashMap<String, Object> getDaily(Integer id) {
		
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date endDate = cd.getTime();
		String endTime = DATETIME_FORMAT.format(endDate);
		cd.add(Calendar.DATE, -7);
		Date startDate = cd.getTime();
		String startTime = DATETIME_FORMAT.format(startDate);
		List<String> xlist = getBetweenDates(startDate, endDate);
		//获取区域ID
		Integer[] areaIds = paService.getAreaIds(id);
		
		
		Integer[] num1 = paService.getZjykydDaily(1, startTime, endTime,areaIds);
		Integer[] num2 = paService.getZjykydDaily(2, startTime, endTime,areaIds);
		Integer[] num3 = paService.getZjykydDaily(3, startTime, endTime,areaIds);
		
		
		List<Integer[]> ylist = new ArrayList<Integer[]>();
		ylist.add(num3); 
		ylist.add(num2); 
		ylist.add(num1);
		res.put("x", xlist);
		res.put("y", ylist);
		return res;
	}
	
	private static List<String> getBetweenDates(Date start, Date end) {
	    List<String> result = new ArrayList<String>();
	    Calendar tempStart = Calendar.getInstance();
	    tempStart.setTime(start);
	    
	    Calendar tempEnd = Calendar.getInstance();
	    tempEnd.setTime(end);
	    while (!tempStart.after(tempEnd)) {
	        result.add(DATE_FORMAT.format(tempStart.getTime()));
	        tempStart.add(Calendar.DAY_OF_YEAR, 1);
	    }
	    return result;
	}

	private HashMap<String, Object> getLastMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String endTime = strs[0]+"-"+strs[1]+"-"+"01 00:00:00";
		cd.add(Calendar.MONTH, -1);
		date = cd.getTime();
		strs = MyTools.getDateArr(date);
		String startTime = strs[0]+"-"+strs[1]+"-"+"01 00:00:00";
		String name = strs[1]+"月";
		Integer num1 = 0;
		Integer num2 = 0;
		Integer num3 = 0;
		if (id.equals(1000) == true) {
			num1 = paService.getSqLastMonth(3, startTime, endTime);
			if (num1 == null)
				num1 = 0;
			num2 = paService.getSqLastMonth(2, startTime, endTime);
			if (num2 == null)
				num2 = 0;
			num3 = paService.getSqLastMonth(1, startTime, endTime);
			if (num3 == null)
				num3 = 0;
		}
		else if(id > 1000){
			num1 = paService.getXqLastMonth(3, startTime, endTime, id);
			if (num1 == null)
				num1 = 0;
			num2 = paService.getXqLastMonth(2, startTime, endTime, id);
			if (num2 == null)
				num2 = 0;
			num3 = paService.getXqLastMonth(1, startTime, endTime, id);
			if (num3 == null)
				num3 = 0;
		}
		else{
			num1 = paService.getJqLastMonth(3, startTime, endTime, id);
			if (num1 == null)
				num1 = 0;
			num2 = paService.getJqLastMonth(2, startTime, endTime, id);
			if (num2 == null)
				num2 = 0;
			num3 = paService.getJqLastMonth(1, startTime, endTime, id);
			if (num3 == null)
				num3 = 0;
		}
		String[] arr1 = {name};
		Integer[] a1 = {num1};
		Integer[] a2 = {num2};
		Integer[] a3 = {num3};
		List<Integer[]> list = new ArrayList<>();
		list.add(a1); list.add(a2); list.add(a3);
		res.put("x", arr1);
		res.put("y", list);
		return res;
	}

	

}
