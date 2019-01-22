package com.naswork.module.tj;

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
import com.naswork.model.KydfxTop5Data;
import com.naswork.service.TjLmlyrsDailyService;
import com.naswork.service.TjLmlyrsMonthlyService;
import com.naswork.service.TjLmlyrsRealtimeCurService;

/*接待人数-非本市top5*/
@RestController
@RequestMapping("/v1/jdrsfbstop5")
public class Jdrsfbstop5Controller extends BaseController {
	@Resource
	private TjLmlyrsMonthlyService mService;
	@Resource
	private TjLmlyrsDailyService dService;
	@Resource
	private TjLmlyrsRealtimeCurService rService;
	
	
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
	
	@RequestMapping("/{id}/{time_range}")
	public HashMap<String, Object> getLmlyrs(@PathVariable Integer id, @PathVariable Integer time_range) {
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

	private HashMap<String, Object> getLastMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		String[] title = { "排名", "景点", "人数" };
		if (id.intValue() > 1000)
			title[1] = "县区";
		List< List<Object> > dataList = new ArrayList<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+"00000000";
		List<KydfxTop5Data> list = new ArrayList<>();
		if(id.intValue() > 1000){
			list = mService.selectTop5Xq(idStr, id);
		}
		else{
			list = mService.selectTop5Jq(idStr);
		}
		Integer i = 1;
		String name = "";
		Double num = 0.0;
		for(KydfxTop5Data tmp: list){
			name = tmp.getName();
			num = tmp.getValue();
			if(num == null) num = 0.0;
			List<Object> l = new ArrayList<>();
			l.add(i);
			l.add(name);
			l.add(num);
			dataList.add(l);
			i++;
		}
		res.put("title", title);
		res.put("data", dataList);
		return res;
	}

	private HashMap<String, Object> getYesterday(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		String[] title = { "排名", "景点", "人数" };
		if (id.intValue() > 1000)
			title[1] = "县区";
		List< List<Object> > dataList = new ArrayList<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -2);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+strs[2]+"000000";
		List<KydfxTop5Data> list = new ArrayList<>();
		if(id.intValue() > 1000){
			list = dService.selectTop5Xq(idStr, id);
		}
		else{
			list = dService.selectTop5Jq(idStr);
		}
		Integer i = 1;
		String name = "";
		Double num = 0.0;
		for(KydfxTop5Data tmp: list){
			name = tmp.getName();
			num = tmp.getValue();
			if(num == null) num = 0.0;
			List<Object> l = new ArrayList<>();
			l.add(i);
			l.add(name);
			l.add(num);
			dataList.add(l);
			i++;
		}
		res.put("title", title);
		res.put("data", dataList);
		return res;
	}

	private HashMap<String, Object> getRealtime(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Integer level = 3;
		String[] title = {"排名", "景点", "人数"};
		if(id.intValue() > 1000){
			title[1] = "县区";
			level = 2;
		}
		List< List<Object> > dataList = new ArrayList<>();
		List<KydfxTop5Data> list = new ArrayList<>();
		if(id.intValue() > 1000){
			list = rService.selectTop5Xq(id);
		}
		else{
			list = rService .selectTop5Jq();
		}
		Integer i = 1;
		String name = "";
		Double num = 0.0;
		for(KydfxTop5Data tmp: list){
			name = tmp.getName();
			num = tmp.getValue();
			if(num == null) num = 0.0;
			List<Object> l = new ArrayList<>();
			l.add(i);
			l.add(name);
			l.add(num);
			dataList.add(l);
			i++;
		}
		res.put("title", title);
		res.put("data", dataList);
		return res;
	}
}
