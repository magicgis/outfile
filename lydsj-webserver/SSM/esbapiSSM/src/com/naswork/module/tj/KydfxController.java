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
import com.naswork.model.KydfxData;
import com.naswork.model.TjKydfxDaily;
import com.naswork.model.TjKydfxMonthly;
import com.naswork.model.TjKydfxRealtimeCur;
import com.naswork.service.TjKydfxDailyService;
import com.naswork.service.TjKydfxMonthlyService;
import com.naswork.service.TjKydfxRealtimeCurService;

/*客源地分析*/
@RestController
@RequestMapping("/v1/kydfx")
public class KydfxController extends BaseController {
	@Resource
	private TjKydfxMonthlyService tjKydfxMonthlyService;
	@Resource
	private TjKydfxDailyService tjKydfxDailyService;
	@Resource
	private TjKydfxRealtimeCurService tjKydfxRealtimeCurService; 
	
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
	
	@RequestMapping("/{id}/{time_range}/{area_range}")
	public HashMap<String, Object> getKydfx(@PathVariable Integer id, @PathVariable Integer time_range, @PathVariable Integer area_range) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		case RealTimeId:
			res = getRealtime(id, area_range);
			break;
		case YesterdayId:
			res = getYesterday(id, area_range);
			break;
		case LastMonthId:
			res = getLastMonth(id, area_range);
			break;
		}
		return res;
	}

	private HashMap<String, Object> getRealtime(Integer id, Integer areaRange) {
		HashMap<String, Object> res = new HashMap<>();
		Integer scope = 2;
		if(areaRange.equals(1) == true) scope = 3;
		List<TjKydfxRealtimeCur> list = new ArrayList<>();
		list = tjKydfxRealtimeCurService.selectByLevel(id, scope);
		List<KydfxData> klist = new ArrayList<>();
		for(TjKydfxRealtimeCur tmp: list){
			KydfxData d = new KydfxData();
			String name = tmp.getSourcename();
			if(scope.equals(2)==true) name += "市";
			Integer num = tmp.getSubscribercount();
			if(num == null) num = 0;
			d.setName(name);
			d.setValue(num);
			klist.add(d);
		}
		res.put("data", klist);
		return res;
	}

	private HashMap<String, Object> getLastMonth(Integer id, Integer areaRange) {
		HashMap<String, Object> res = new HashMap<>();
		List<TjKydfxMonthly> list = new ArrayList<>();
		Integer scope = 0;
		if(areaRange.intValue() == 1) scope = 3;
		else if(areaRange.intValue() == 2) scope = 2;
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+"00000000";
		list = tjKydfxMonthlyService.selectByScope(id, scope, idStr);
		List<KydfxData> dataList = new ArrayList<>();
		for(TjKydfxMonthly tmp: list){
			String name = "";
			Integer num = 0;
			KydfxData k = new KydfxData();
			name = tmp.getSourcename();
			if(areaRange.equals(2)==true) name += "市";
			if(tmp.getSubscribercount()!=null) num = tmp.getSubscribercount();
			k.setName(name);
			k.setValue(num);
			dataList.add(k);
		}
		res.put("data", dataList);
		return res;
	}

	private HashMap<String, Object> getYesterday(Integer id, Integer areaRange) {
		HashMap<String, Object> res = new HashMap<>();
		List<TjKydfxDaily> list = new ArrayList<>();
		Integer scope = 0;
		if(areaRange.intValue() == 1) scope = 3;
		else if(areaRange.intValue() == 2) scope = 2;
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -2);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+strs[2]+"000000";
		list = tjKydfxDailyService.selectByScope(id, scope, idStr);
		List<KydfxData> dataList = new ArrayList<>();
		for(TjKydfxDaily tmp: list){
			String name = "";
			Integer num = 0;
			KydfxData k = new KydfxData();
			name = tmp.getSourcename();
			if(areaRange.equals(2)==true) name += "市";
			if(tmp.getSubscribercount()!=null) num = tmp.getSubscribercount();
			k.setName(name);
			k.setValue(num);
			dataList.add(k);
		}
		res.put("data", dataList);
		return res;
	}
}
