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
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjYkrysjDaily;
import com.naswork.model.TjYkrysjMonthly;
import com.naswork.service.TjYkrysjDailyService;
import com.naswork.service.TjYkrysjMonthlyService;

@RestController
@RequestMapping("/v1/ykrysj")
public class YkrysjController extends BaseController {
	@Resource
	private TjYkrysjMonthlyService tjYkrysjMonthlyService; 
	@Resource
	private TjYkrysjDailyService tjYkrysjDailyService;
	
	/*定义time_range的mapping*/
	private final static int RealTimeId = 1;
	private final static int YesterdayId = 2;
	private final static int LastMonthId = 3;
	private final static int DailyId = 4;
	private final static int MonthlyId = 5;
	/*取多少天的数据*/
	private final static int DaysNum = -7;
	/*取多少月的数据*/
	private final static int MonthsNum = 0;
	
	@RequestMapping("/{id}/{time_range}")
	public HashMap<String, Object> getYkdlsj(@PathVariable Integer id, @PathVariable Integer time_range) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		case YesterdayId:
			res = getYesterday(id);
			break;
		case LastMonthId:
			res = getLastMonth(id);
			break;
		case RealTimeId:
			res = getRealtime(id);
			break;
		case MonthlyId:
			res = getCurMonth(id);
			break;
		}
		return res;
	}

	private HashMap<String, Object> getRealtime(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		List<TjYkrysjDaily> list = new ArrayList<>();
		Date date = new Date();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+strs[2]+"000000";
		list = tjYkrysjDailyService.selectOneDay(id, idStr);
		Map<String, Integer> map = new LinkedHashMap<>();
		String str = "";
		Integer num = 0;
		for(TjYkrysjDaily tmp: list){
			str = tmp.getTimeonly();
			num = tmp.getSubscribercount();
			if(num == null) num = 0;
			map.put(str, num);
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	private HashMap<String, Object> getYesterday(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+strs[2]+"000000";
		Map<String, Integer> map = new LinkedHashMap<>();
		List<TjYkrysjDaily> list = tjYkrysjDailyService.selectByMonth(id, idStr);
		String str = "";
		Integer num = 0;
		for(TjYkrysjDaily tmp: list){
			num = 0;
			if(tmp.getSubscribercount()!=null) num = tmp.getSubscribercount();
			str = tmp.getTimeonly();
			map.put(str, num);
		}
		if(list==null || list.size() == 0){
			Calendar cd1 = Calendar.getInstance();
			cd1.set(Calendar.HOUR_OF_DAY, 0);
			cd1.set(Calendar.MINUTE, 0);
			cd1.set(Calendar.SECOND, 0);
			Date d = cd1.getTime();
			String s = getTimeOnly(d);
			while(true){
				map.put(s, 0);
				cd1.add(Calendar.MINUTE, 5);
				d = cd1.getTime();
				s = getTimeOnly(d);
				if(s.equals("00:00:00")==true) break;
			}
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	private HashMap<String, Object> getLastMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+"00000000";
		Map<String, Integer> map = new LinkedHashMap<>();
		List<TjYkrysjMonthly> list = tjYkrysjMonthlyService.selectByMonth(id, idStr);
		String str = "";
		Integer num = 0;
		for(TjYkrysjMonthly tmp: list){
			num = 0;
			if(tmp.getSubscribercount()!=null) num = tmp.getSubscribercount();
			str = tmp.getTimeonly();
			map.put(str, num);
		}
		if(list==null || list.size() == 0){
			Calendar cd1 = Calendar.getInstance();
			cd1.set(Calendar.HOUR_OF_DAY, 0);
			cd1.set(Calendar.MINUTE, 0);
			cd1.set(Calendar.SECOND, 0);
			Date d = cd1.getTime();
			String s = getTimeOnly(d);
			while(true){
				map.put(s, 0);
				cd1.add(Calendar.MINUTE, 5);
				d = cd1.getTime();
				s = getTimeOnly(d);
				if(s.equals("00:00:00")==true) break;
			}
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}
	
	private String getTimeOnly(Date sj){
		String res = "";
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = simpleFormat.format(sj);
		String[] strs = str.split(" ");
		res = strs[1];
		return res;
	}

	private HashMap<String, Object> getCurMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Date date = new Date();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+"00000000";
		Map<String, Integer> map = new LinkedHashMap<>();
		List<TjYkrysjMonthly> list = tjYkrysjMonthlyService.selectByMonth(id, idStr);
		String str = "";
		Integer num = 0;
		for(TjYkrysjMonthly tmp: list){
			num = 0;
			if(tmp.getSubscribercount()!=null) num = tmp.getSubscribercount();
			str = tmp.getTimeonly();
			map.put(str, num);
		}
		if(list==null || list.size() == 0){
			Calendar cd1 = Calendar.getInstance();
			cd1.set(Calendar.HOUR_OF_DAY, 0);
			cd1.set(Calendar.MINUTE, 0);
			cd1.set(Calendar.SECOND, 0);
			Date d = cd1.getTime();
			String s = getTimeOnly(d);
			while(true){
				map.put(s, 0);
				cd1.add(Calendar.MINUTE, 5);
				d = cd1.getTime();
				s = getTimeOnly(d);
				if(s.equals("00:00:00")==true) break;
			}
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}
}
