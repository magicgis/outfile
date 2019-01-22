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
 * @ClassName:  JjrjqjdtopnController   
 * @Description:景区接待人数TOP5（来梅/含本市）  
 * @author: Li Zhenning 
 * @date:   2018-5-17 下午5:13:48   
 *
 */
@RestController
@RequestMapping("/v1/jjrjqjdtopn")
public class JjrjqjdtopnController extends BaseController {
	@Resource
	private HolidayService holidayService;
	
	@RequestMapping("/{time_range}/{area_range}")
	public Map<String, Object> jjrjqjd(@PathVariable Integer time_range, 
			@PathVariable Integer area_range) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<Map<String, Object>> list = holidayService.selectjqjdrsTop5(area_range, time_range);
		if(list == null || list.size() == 0){
			return resultMap;
		}
		String[] title = {"排名","景点", "人数"};
		List<List<Object>> data = new ArrayList<List<Object>>();
		List<Object> dataBean = null;
		for (Map<String, Object> map : list) {
			dataBean = new ArrayList<Object>();
			dataBean.add(map.get("rank"));
			dataBean.add(map.get("name"));
			dataBean.add(map.get("number"));
			data.add(dataBean);
		}
		resultMap.put("title", title);
		resultMap.put("data", data);
		return resultMap;
	}
}
