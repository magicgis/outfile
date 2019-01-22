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
import com.naswork.model.TjKyddbfxDaily;
import com.naswork.model.TjKyddbfxMonthly;
import com.naswork.service.TjKyddbfxDailyService;
import com.naswork.service.TjKyddbfxMonthlyService;

/*客源地对比分析*/
@RestController
@RequestMapping("/v1/kyddbfx")
public class KyddbfxController extends BaseController {
	@Resource
	private TjKyddbfxMonthlyService tjKyddbfxMonthlyService;
	@Resource
	private TjKyddbfxDailyService tjKyddbfxDailyService;
	
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
	
	@RequestMapping("/{id}/{time_range}")
	public HashMap<String, Object> getKyddbfx(@PathVariable Integer id, @PathVariable Integer time_range) {
		HashMap<String, Object> res = new LinkedHashMap<>();
		switch (time_range) {
		/*case YesterdayId:
			res = getYesterday(id);
			break;*/
		case LastMonthId:
			res = getLastMonth(id);
			break;
		case DailyId:
			res = getDaily(id);
			break;
		case MonthlyId:
			res = getMonthly(id);
			break;
		}
		return res;
	}


	private HashMap<String, Object> getDaily(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, DaysNum);
		int i = 0;
		int cont = -1*(DaysNum+2) + 1;
		List<String> names = new ArrayList<>();
		List< List<Integer> > datas = new ArrayList<>();
		List<Integer> list4 = new ArrayList<>();
		List<Integer> list3 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		Date date = new Date();
		String[] strs = {};
		String idStr = "";
		TjKyddbfxDaily tmp = new TjKyddbfxDaily();
		Integer num = 0;
		String s = "";
		while(i<cont){
			date = cd.getTime();
			strs = MyTools.getDateArr(date);
			idStr = strs[0]+strs[1]+strs[2]+"000000";
			s = MyTools.getChineseDateStr(date);
			names.add(s);
			tmp = tjKyddbfxDailyService.selectByIdAndDate4(id, idStr);
			if(tmp!=null && tmp.getSubscribercount()!=null) num = tmp.getSubscribercount();
			list4.add(num);
			num = 0;
			tmp = tjKyddbfxDailyService.selectByIdAndDate3(id, idStr);
			if(tmp!=null && tmp.getSubscribercount()!=null) num = tmp.getSubscribercount();
			list3.add(num);
			num = 0;
			tmp = tjKyddbfxDailyService.selectByIdAndDate2(id, idStr);
			if(tmp!=null && tmp.getSubscribercount()!=null) num = tmp.getSubscribercount();
			list2.add(num);
			num = 0;
			cd.add(Calendar.DATE, 1);
			i++;
		}
		datas.add(list4);
		datas.add(list3);
		datas.add(list2);
		res.put("x", names);
		res.put("y", datas);
		return res;
	}


	private HashMap<String, Object> getLastMonth(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.MONTH, -1);
		Date date = cd.getTime();
		String[] strs = {};
		String idStr = "";
		TjKyddbfxMonthly tmp = new TjKyddbfxMonthly();
		Integer num = 0;
		List<String> names = new ArrayList<>();
		List<List<Integer>> datas = new ArrayList<>();
		List<Integer> tmplist4 = new ArrayList<>();
		List<Integer> tmplist3 = new ArrayList<>();
		List<Integer> tmplist2 = new ArrayList<>();

		date = cd.getTime();
		strs = MyTools.getDateArr(date);
		idStr = strs[0] + strs[1] + "00000000";
		names.add(strs[1] + "月");
		tmp = tjKyddbfxMonthlyService.getList4FromMonth(id, idStr);
		if (tmp != null && tmp.getSubscribercount() != null)
			num = tmp.getSubscribercount();
		tmplist4.add(num);
		num = 0;
		tmp = tjKyddbfxMonthlyService.getList3FromMonth(id, idStr);
		if (tmp != null && tmp.getSubscribercount() != null)
			num = tmp.getSubscribercount();
		tmplist3.add(num);
		num = 0;
		tmp = tjKyddbfxMonthlyService.getList2FromMonth(id, idStr);
		if (tmp != null && tmp.getSubscribercount() != null)
			num = tmp.getSubscribercount();
		tmplist2.add(num);

		datas.add(tmplist4);
		datas.add(tmplist3);
		datas.add(tmplist2);
		res.put("x", names);
		res.put("y", datas);
		return res;
	}

	private HashMap<String, Object> getMonthly(Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		Date date = new Date();
		String[] strs = MyTools.getDateArr(date);
		String idStr = strs[0]+strs[1]+"00000000";
		Integer num1 = 0;
		Integer num2 = 0;
		Integer num3 = 0;
		TjKyddbfxMonthly t1 = tjKyddbfxMonthlyService.getList4FromMonth(id, idStr);
		TjKyddbfxMonthly t2 = tjKyddbfxMonthlyService.getList3FromMonth(id, idStr);
		TjKyddbfxMonthly t3 = tjKyddbfxMonthlyService.getList2FromMonth(id, idStr);
		if(t1!=null && t1.getSubscribercount()!=null) num1 = t1.getSubscribercount();
		if(t2!=null && t2.getSubscribercount()!=null) num2 = t2.getSubscribercount();
		if(t3!=null && t3.getSubscribercount()!=null) num3 = t3.getSubscribercount();
		List<Integer> list1 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		List<Integer> list3 = new ArrayList<>();
		list1.add(num1);
		list2.add(num2);
		list3.add(num3);
		String[] x = {strs[1]+"月"};
		List< List<Integer> > y = new ArrayList<>();
		y.add(list1);
		y.add(list2);
		y.add(list3);
		res.put("x", x);
		res.put("y", y);
		return res;
	}
}
