package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.model.EdrawwsReport;
import com.naswork.service.PoliceWarningService;
/**
 * 查询最近3天的警情信息并倒叙排列
 * @author zhangwenwen
 *
 */
@RestController
@RequestMapping("/v2/zajq")
public class PoliceWaringController {
	@Resource
	private PoliceWarningService policeWarningService;
	@RequestMapping("/{id_type}/{id}")
	public HashMap<String, Object> getWeatherWaring(@PathVariable Integer id_type,@PathVariable Integer id){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<Object> dataList=new ArrayList<Object>();
		List<EdrawwsReport> warnningList=policeWarningService.getPoliceWarning();
		hashMap.put("name", "梅州市公安局");
		for (EdrawwsReport edrawwsReport : warnningList) {
			HashMap<String, Object> hashMap2=new HashMap<String, Object>();
			hashMap2.put("publish_time", edrawwsReport.getPublishTime());
			hashMap2.put("content", edrawwsReport.getContent());
			dataList.add(hashMap2);
		}
		hashMap.put("data", dataList);
		return hashMap;
	}
}
