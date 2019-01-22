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
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.Zjykydtop5;
import com.naswork.service.PlateAnalyticService;
import com.naswork.utils.PlateUtils;

@RestController
@RequestMapping("/v1/zjykydtop5")
public class Zjykydtop5Controller extends BaseController {
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
	private final static int DaysNum = -8;
	/*取多少月的数据*/
	private final static int MonthsNum = 0;
	
	@RequestMapping("/{id}/{time_range}/{area_range}")
	public HashMap<String, Object> getTop5(@PathVariable Integer id, @PathVariable Integer time_range,
			@PathVariable Integer area_range) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		Integer aux = area_range;
		if(area_range.equals(1)==true) area_range = 3;
		switch (time_range) {
		/*case YesterdayId:
			res = getJdrsYesterday(id);
			break;*/
		case LastMonthId:
			res = getLastMonth(id, aux);
			break;
		case YesterdayId:
			res = getDaily(id,area_range);
			break;
		/*case MonthlyId:
			res = getCmrsMonthly(id);
			break;*/
		}
		return res;
	}
	
	private HashMap<String, Object> getDaily(Integer id,Integer area) {
		
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		String endTime = DATETIME_FORMAT.format(cd.getTime());
		//获取区域ID
		Integer[] areaIds = paService.getAreaIds(id);
		
		
		List<Map<String, String>> list = paService.getZjykydTop5(area, endTime, areaIds);
		if(list == null || list.size() == 0){
			cd.add(Calendar.DATE, -1);
			endTime = DATETIME_FORMAT.format(cd.getTime());
			list = paService.getZjykydTop5(area, endTime, areaIds);
		}
		List<String> xlist = new ArrayList<String>();
		xlist.add("排名");
		if(area == 3){
			xlist.add("省份");
		}else{
			xlist.add("市");
		}
		
		xlist.add("车辆数");
		List<List<String>> ylist = new ArrayList<List<String>>();
		List<String> dataList = null;
		Map<String, String> map;
		for (int i = 0; i < list.size(); i++) {
			dataList = new ArrayList<String>();
			map = list.get(i);
			dataList.add(String.valueOf(i+1));
			if(area == 3){
				dataList.add(map.get("province")+"-"+PlateUtils.getProvince(map.get("province")));
			}else{
				dataList.add(PlateUtils.getCity(map.get("province"), map.get("city")));
			}
			dataList.add(String.valueOf(map.get("totalNum")));
			ylist.add(dataList);
		}
		
		res.put("title", xlist);
		res.put("data", ylist);
		return res;
	}
	
	
	/*area=1国内，area=2省内*/
	private HashMap<String, Object> getLastMonth(Integer id, Integer area) {
		HashMap<String, Object> res = new HashMap<>();
		Integer scope = 0;
		if(area.intValue() == 1) scope = 3;
		else scope = 2;
		Calendar cd = Calendar.getInstance();
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String endTime = strs[0]+"-"+strs[1]+"-00 00:00:00";
		cd.add(Calendar.MONTH, -1);
		date = cd.getTime();
		strs = MyTools.getDateArr(date);
		String startTime = strs[0]+"-"+strs[1]+"-00 00:00:00";
		List<KydfxTop5Data> list = new ArrayList<>();
		if(area.intValue() == 1){
			if(id.intValue() > 1000) list = paService.selectTop5Xq(id, startTime, endTime);
			else if(id.intValue()==1000) list = paService.selectTop5Sq(startTime, endTime);
			else list = paService.selectTop5Jq(id, startTime, endTime);
		}
		else {
			if(id.intValue() > 1000) list = paService.selectTop5XqSn(id, startTime, endTime);
			else if(id.intValue()==1000) list = paService.selectTop5SqSn(startTime, endTime);
			else list = paService.selectTop5JqSn(id, startTime, endTime);
		}
		String[] title = {"排名","省份","车辆数"};
		if(area.intValue()==2) title[1] = "市";
		res.put("title", title);
		Integer i = 1;
		List< List<Object> > ylist = new ArrayList<>();
		for(KydfxTop5Data tmp: list){
			if(tmp.getName()==null) continue;
			List<Object> tlist = new ArrayList<>();
			tlist.add(i);
			String s = tmp.getName();
			Double num = 0.0;
			if(tmp.getValue()!=null) num = tmp.getValue();
			if(area.intValue()==1) s = s +"-"+ PlateUtils.getProvince(tmp.getName());
			else s = PlateUtils.getYueCity(s);
			tlist.add(s);
			tlist.add(num);
			ylist.add(tlist);
			i++;
		}
		res.put("data", ylist);
		return res;
	}
}
