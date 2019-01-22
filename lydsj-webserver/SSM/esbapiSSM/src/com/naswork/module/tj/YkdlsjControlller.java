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
import com.naswork.model.Ring;
import com.naswork.model.TjYkdlsjRealtimeCur;
import com.naswork.service.TjYkdlsjDailyService;
import com.naswork.service.TjYkdlsjMonthlyService;
import com.naswork.service.TjYkdlsjRealtimeCurService;

/*游客逗留时间*/
@RestController
@RequestMapping("/v1/ykdlsj")
public class YkdlsjControlller extends BaseController {
	@Resource
	private TjYkdlsjDailyService tjYkdlsjDailyService;
	@Resource
	private TjYkdlsjMonthlyService tjYkdlsjMonthlyService;
	@Resource
	private TjYkdlsjRealtimeCurService rService;
	
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
	
	private final String[] names = {"<1小时", "1-2小时", "2-3小时", "3-4小时", "4-5小时",
			"5-6小时", "6-7小时", "7-8小时", ">8小时"};
	
	@RequestMapping("/{id}/{time_range}")
	public HashMap<String, Object> getYkdlsj(@PathVariable Integer id, @PathVariable Integer time_range) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		case LastMonthId:
			res = getLastMonth(id);
			break;
		}
		return res;
	}
	


	private HashMap<String, Object> getLastMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		List<Ring> list = new ArrayList<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0] + strs[1] + "00000000";
		Integer num = 0;
		for(int i=0; i<9; i++){
			Ring r = new Ring();
			num = 0;
			r.setName(names[i]);
			num = tjYkdlsjMonthlyService.getNum(id, idStr, i);
			if(num == null) num = 0;
			//r.setValue(num);
			list.add(r);
		}
		res.put("data", list);
		return res;
	}
}
