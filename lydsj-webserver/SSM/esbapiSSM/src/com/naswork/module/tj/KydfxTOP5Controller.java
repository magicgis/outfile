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
import com.naswork.model.MyModel;
import com.naswork.model.TjKydfxDaily;
import com.naswork.model.TjKydfxRealtimeCur;
import com.naswork.service.TjKydfxDailyService;
import com.naswork.service.TjKydfxMonthlyService;
import com.naswork.service.TjKydfxRealtimeCurService;

@RestController
@RequestMapping("/v1/kydfxtop5")
public class KydfxTOP5Controller extends BaseController {
	@Resource
	private TjKydfxDailyService tjKydfxDailyService;
	@Resource
	private TjKydfxMonthlyService tjKydfxMonthlyService;
	@Resource
	private TjKydfxRealtimeCurService tjKydfxRealtimeCurService;
	
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
	
	@RequestMapping("/{id}/{time_range}/{area_range}")
	public HashMap<String, Object> getKydfxTop5(@PathVariable Integer id, @PathVariable Integer time_range,  @PathVariable Integer area_range) {

		if (area_range==4) {
			area_range=null;//如果传参过来的area_range=4，则赋值为空，查询所有的客源地来梅人数，并排名前五
		}
		if (area_range!=null&&area_range==5) {
			area_range=4;//如果传参过来的area_range=5，则赋值4，查询国际的客源地来梅人数，并排名前五
		}

		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		case YesterdayId:
			res = getYesterday(id, area_range);
			break;
		case LastMonthId:
			res = getLastMonth(id, area_range);
			break;
		case RealTimeId:
			res = getRealtime(id, area_range);
			break;
		}
		return res;
	}

	private HashMap<String, Object> getRealtime(Integer id, Integer area_range) {
		HashMap<String, Object> res = new HashMap<>();
		String[] title = { "排名", "地点", "人数" };
		Integer scope = area_range;
		if(area_range!=null&&area_range.intValue() == 1) scope = 3;
		List<KydfxTop5Data> list = new ArrayList<>();
		list = tjKydfxRealtimeCurService.selectTop5(id, scope);
		List< List<Object> > dList = new ArrayList<>();
		Integer i = 1;
		for(KydfxTop5Data tmp: list){
			List<Object> d = new ArrayList<>();
			d.add(i);
			d.add(tmp.getName());
			d.add(tmp.getValue());
			dList.add(d);
			i++;
		}
		res.put("title", title);
		res.put("data", dList);
		return res;
	}

	private HashMap<String, Object> getLastMonth(Integer id, Integer area_range) {
		HashMap<String, Object> res = new HashMap<>();
		String[] title = { "排名", "地点", "人数" };
		Integer scope = area_range;
		if(area_range!=null&&area_range.intValue() == 1) scope = 3;
		List<KydfxTop5Data> list = new ArrayList<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+"00000000";
		list = tjKydfxMonthlyService.selectTop5(id, scope, idStr);
		List< List<Object> > dList = new ArrayList<>();
		Integer i = 1;
		for(KydfxTop5Data tmp: list){
			List<Object> d = new ArrayList<>();
			d.add(i);
			d.add(tmp.getName());
			d.add(tmp.getValue());
			dList.add(d);
			i++;
		}
		res.put("title", title);
		res.put("data", dList);
		return res;
	}

	private HashMap<String, Object> getYesterday(Integer id, Integer area_range) {
		HashMap<String, Object> res = new HashMap<>();
		String[] title = { "排名", "地点", "人数" };
		Integer scope = area_range;
		if(area_range!=null&&area_range.intValue() == 1) scope = 3;
		List<KydfxTop5Data> list = new ArrayList<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -2);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+strs[2]+"000000";
		list = tjKydfxDailyService.selectTop5(id, scope, idStr);
		List< List<Object> > dList = new ArrayList<>();
		Integer i = 1;
		for(KydfxTop5Data tmp: list){
			List<Object> d = new ArrayList<>();
			d.add(i);
			d.add(tmp.getName());
			d.add(tmp.getValue());
			dList.add(d);
			i++;
		}
		res.put("title", title);
		res.put("data", dList);
		return res;
	}
	
}
