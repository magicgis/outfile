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
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.KydfxTop5DataQB;
import com.naswork.service.TjLmlyrsDailyService;
import com.naswork.service.TjLmlyrsMonthlyService;
import com.naswork.service.TjLmlyrsRealtimeCurService;

/*接待人数-非本市top5*/
@RestController
@RequestMapping("/v1/ssklyj	")
public class SsklyjController extends BaseController {
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
	public Map<String, Object> getLmlyrs(@PathVariable Integer id, @PathVariable Integer time_range) {
		Map<String, Object> res = new HashMap<String, Object>();
		switch (time_range) {
		case RealTimeId:
			res = getRealtime(id);
			break;
		/*
		case YesterdayId:
			res = getYesterday(id);
			break;
		case LastMonthId:
			res = getLastMonth(id);
			break;
		*/
		}
		return res;
	}


	private Map<String, Object> getRealtime(Integer id) {
		Map<String, Object> res = new HashMap<String, Object>();
		
		List<Map<String, Object>> alarms = rService.selectYjDataGroup(id);
		List<Map<String, Object>> details = rService.selectYjData(id);
		
		res.put("alarms", alarms);
		res.put("details", details);
		return res;
	}
}
