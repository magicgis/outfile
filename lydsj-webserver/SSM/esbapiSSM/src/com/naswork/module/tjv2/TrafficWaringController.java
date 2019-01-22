package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.model.EdrawtwReport;
import com.naswork.service.TrafficWarnningService;
/**
 * 查询最近3天的交通信息并倒叙排列，如果凑不够3天继续往前找
 * @author zhangwenwen
 *
 */
@RestController
@RequestMapping("/v2/gslkyj")
public class TrafficWaringController {
	@Resource
	private TrafficWarnningService trafficWarnningService;
	@RequestMapping("/{id_type}/{id}")
	public HashMap<String, Object> getWeatherWaring(@PathVariable Integer id_type,@PathVariable Integer id){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<Object> dataList=new ArrayList<Object>();
		List<EdrawtwReport> warnningList=new ArrayList<>();
		 warnningList=trafficWarnningService.getCurtraffic();
		if (warnningList.size()<3) {
			warnningList=trafficWarnningService.getThreeTraffic();
		}
		hashMap.put("name", "梅州市交通局");
		for (EdrawtwReport edrawtwReport : warnningList) {
			HashMap<String, Object> hashMap2=new HashMap<String, Object>();
			hashMap2.put("publish_time", edrawtwReport.getPublishTime());
			hashMap2.put("content", edrawtwReport.getContent());
			dataList.add(hashMap2);
		}
		hashMap.put("data", dataList);
		return hashMap;
	}
}
