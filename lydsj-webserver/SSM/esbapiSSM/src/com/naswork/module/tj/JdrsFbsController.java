package com.naswork.module.tj;

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
import com.naswork.model.Jqqd;
import com.naswork.model.TjLmlyrsDaily;
import com.naswork.model.TjLmlyrsMonthly;
import com.naswork.model.TjLmlyrsRealtimeCur;
import com.naswork.service.JqqdService;
import com.naswork.service.TjLmlyrsDailyService;
import com.naswork.service.TjLmlyrsMonthlyService;
import com.naswork.service.TjLmlyrsRealtimeCurService;

/*接待人数-非本市*/
@RestController
@RequestMapping("/v1/jdrsfbs")
public class JdrsFbsController extends BaseController {
	@Resource
	private TjLmlyrsMonthlyService tjLmlyrsMonthlyService;
	@Resource
	private TjLmlyrsDailyService tjLmlyrsDailyService;
	@Resource
	private JqqdService jqqdService;
	@Resource
	private TjLmlyrsRealtimeCurService tjLmlyrsRealtimeCurService;
	
	/*定义time_range的mapping*/
	private final static int RealTimeId = 1;
	private final static int YesterdayId = 2;
	private final static int LastMonthId = 3;
	private final static int DailyId = 4;
	private final static int MonthlyId = 5;
	private final static int TodayId = 6;
	/*取多少天的数据*/
	private final static int DaysNum = 7;
	/*取多少月的数据*/
	private final static int MonthsNum = 7;
	
	private static HashMap<Integer, String> jqNameMap = new HashMap<>();
	
	private static HashMap<Integer, String> xqNameMap = new HashMap<>();
	
	@RequestMapping("/{id}/{time_range}")
	public HashMap<String, Object> getLmlyrs(@PathVariable Integer id, @PathVariable Integer time_range) {
		initNameMap();
		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		case RealTimeId:
			res = getRealtime(id);
			break;
		case YesterdayId:
			res = getYesterday(id);
			break;
		case LastMonthId:
			res = getLastMonth(id);
			break;
		}
		return res;
	}
	
	private HashMap<String, Object> getRealtime(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Map<String, Integer> map = new LinkedHashMap<>();
		Integer level = 3;
		if(id.intValue() > 1000) level = 2;
		else level = 3;
		List<TjLmlyrsRealtimeCur> list = tjLmlyrsRealtimeCurService.selectByLevel(level);
		Integer tid = 0;
		String tname = "";
		Integer tnum = 0;
		for(TjLmlyrsRealtimeCur tmp: list){
			tid = tmp.getId();
			if(level == 2) tname = xqNameMap.get(tid);
			else if(level == 3) tname = jqNameMap.get(tid);
			else tname = "";
			tnum = tmp.getSubscribercount();
			if(tnum == null) tnum = 0;
			map.put(tname, tnum);
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	/*初始化县区景区name和id的map*/
	private void initNameMap() {
		List<Jqqd> list = jqqdService.selectAll();
		String name1 = "";
		String name2 = "";
		Integer num1 = 0;
		Integer num2 = 0;
		boolean isExist = false;
		for(Jqqd tmp: list){
			name1 = tmp.getSpotname();
			num1 = tmp.getId();
			jqNameMap.put(num1, name1);
			name2 = tmp.getDistrictname();
			num2 = tmp.getDistrictid();
			isExist = xqNameMap.containsKey(num2);
			if(isExist == false){
				xqNameMap.put(num2, name2);
			}
		}
	}

	/*上个月*/
	private HashMap<String, Object> getLastMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		List<TjLmlyrsMonthly> list = new ArrayList<>();
		Map<String, Integer> map = new LinkedHashMap<>();
		Integer level = 0;
		if (id > 1000) level = 2;
		else level = 3;
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+"00000000";
		list = tjLmlyrsMonthlyService.selectByLevel(level, idStr);
		Integer tid = 0;
		String tname = "";
		Integer tnum = 0;
		for(TjLmlyrsMonthly tmp: list){
			tid = tmp.getId();
			if(level == 2) tname = xqNameMap.get(tid);
			else if(level == 3) tname = jqNameMap.get(tid);
			else tname = "";
			tnum = tmp.getSubscribercount();
			if(tnum == null) tnum = 0;
			map.put(tname, tnum);
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

	/*昨天*/
	private HashMap<String, Object> getYesterday(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Map<String, Integer> map = new LinkedHashMap<>();
		Integer level = 0;
		if (id > 1000) level = 2;
		else if (id <= 1000) level = 3;
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -2);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+strs[2]+"000000";
		List<TjLmlyrsDaily> list = tjLmlyrsDailyService.selectByLevel(level, idStr);
		Integer tid = 0;
		String tname = "";
		Integer tnum = 0;
		for(TjLmlyrsDaily tmp: list){
			tid = tmp.getId();
			if(level == 2) tname = xqNameMap.get(tid);
			else if(level == 3) tname = jqNameMap.get(tid);
			else tname = "";
			tnum = tmp.getSubscribercount();
			if(tnum == null) tnum = 0;
			map.put(tname, tnum);
		}
		MyTools.getJsonFromMapLine(map, res);
		return res;
	}

}
