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
import com.naswork.model.KydfxTop5DataQB;
import com.naswork.service.TjLmlyrsDailyService;
import com.naswork.service.TjLmlyrsMonthlyService;
import com.naswork.service.TjLmlyrsRealtimeCurService;

/*接待人数-非本市top5*/
@RestController
@RequestMapping("/v1/jdrstopn")
public class Jdrsqbtop5Controller extends BaseController {
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
	public List<KydfxTop5DataQB> getLmlyrs(@PathVariable Integer id, @PathVariable Integer time_range) {
		List<KydfxTop5DataQB> res = new ArrayList<KydfxTop5DataQB>();
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


	private List<KydfxTop5DataQB> getRealtime(Integer id) {
		List<KydfxTop5DataQB> list = new ArrayList<>();
		if(id.intValue() > 1000){
			list = rService.selectTop5XqQB(id);
		}
		else{
			list = rService .selectTop5JqQB();
		}
		return list;
	}
}
