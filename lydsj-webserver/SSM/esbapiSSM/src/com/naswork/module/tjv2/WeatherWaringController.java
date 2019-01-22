package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.model.EdrawwwReport;
import com.naswork.service.WeatherWaringService;
/**
 * 查询梅州各县区预警信息，与气象局官网同步
 * @author zhangwenwen
 *
 */
@RestController
@RequestMapping("/v2/tqyj")
public class WeatherWaringController {
	@Resource
	private WeatherWaringService weatherWaringService;
	@RequestMapping("/{id_type}/{id}")
	public HashMap<String, Object> getWeatherWaring(@PathVariable Integer id_type,@PathVariable Integer id){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<Object> dataList=new ArrayList<Object>();
		List<EdrawwwReport> warnningList=weatherWaringService.getWeatherWarning();
		hashMap.put("name", "梅州市");
		for (EdrawwwReport edrawwwReport : warnningList) {
			HashMap<String, Object> hashMap2=new HashMap<String, Object>();
			hashMap2.put("publish_time", edrawwwReport.getPublishTime());
			hashMap2.put("content", edrawwwReport.getCountry()+edrawwwReport.getContent()+edrawwwReport.getLevel()+"级");
			dataList.add(hashMap2);
			
		}
		hashMap.put("data", dataList);
		return hashMap;
	}
}
