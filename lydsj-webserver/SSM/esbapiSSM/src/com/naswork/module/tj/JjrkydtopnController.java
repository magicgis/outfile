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
 * @ClassName:  JjrkydtopnController   
 * @Description:客源地TOP5（国内/省内） 
 * @author: Li Zhenning 
 * @date:   2018-5-18 下午4:43:57   
 *
 */
@RestController
@RequestMapping("/v1/jjrkydtopn")
public class JjrkydtopnController extends BaseController {
	
	@Resource
	private HolidayService holidayService;
	
	@RequestMapping("/{time_range}/{area_range}")
	public Map<String, Object> jjrjqjd(@PathVariable Integer time_range, 
			@PathVariable Integer area_range) {
		if (area_range==4) {//保持跟前端路径一致进行的转换,应该为0的时候查询全部，但是还没找到对应的表，查询全部还没做
			area_range=0;
		}
		if (area_range!=0&&area_range==5) {//保持跟前端路径一致进行的转换
			area_range=4;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = holidayService.selectjjrkydtopn(area_range, time_range);
		if(list == null || list.size() == 0){
			return resultMap;
		}
		String[] title = {"排名","地点", "人数"};
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
