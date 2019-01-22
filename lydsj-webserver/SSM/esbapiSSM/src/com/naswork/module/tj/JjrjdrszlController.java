package com.naswork.module.tj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.service.HolidayService;

/**
 * 
 * @ClassName:  JjrjdrszlController   
 * @Description:接待人数总览（来梅/含本市） 
 * @author: Li Zhenning 
 * @date:   2018-5-18 下午4:01:43   
 *
 */
@RestController
@RequestMapping("/v1/jjrjdrszl")
public class JjrjdrszlController extends BaseController {
	
	@Resource
	private HolidayService holidayService;
	
	@RequestMapping("/{time_range}/{area_range}")
	public Map<String, Object> jjrjqjd(@PathVariable Integer time_range, 
			@PathVariable Integer area_range) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = holidayService.selectjjrjdrszl(area_range, time_range);
		if(list == null || list.size() == 0){
			return resultMap;
		}
		String years = String.valueOf(list.get(0).get("years"));
		String[] x = years.split(",");
		List<Map<String, Object>> y = new ArrayList<Map<String, Object>>();
		Map<String, Object> yBean = null;
		for (Map<String, Object> map : list) {
			yBean = new HashMap<String, Object>();
			yBean.put("name", map.get("name"));
			yBean.put("data", String.valueOf(map.get("numbers")).split(","));
			y.add(yBean);
		}
		resultMap.put("x", x);
		resultMap.put("y", y);
		return resultMap;
	}
}
