package com.naswork.module.tj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.model.JqJdrsmMonthly;
import com.naswork.service.JqJdrsmMonthlyService;

/*团散比例*/
@RestController
@RequestMapping("/v1/tsbl")
public class TsBlController {
	@Resource
	private JqJdrsmMonthlyService jqJdrsmMonthlyService;
	
	@RequestMapping("/{id}/5")
	public HashMap<String, Object> getTsbl(@PathVariable Integer id){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<JqJdrsmMonthly> list=jqJdrsmMonthlyService.getTsPercent(id);
		List<String> dateList=new ArrayList<String>();
		List<Integer> list1=new ArrayList<Integer>();
		List<Integer> list2=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
		for(int i=0;i<list.size();i++){
			String dateStr=sdf.format(list.get(i).getStatsdate());
			dateList.add(dateStr);
			list1.add(list.get(i).getGtbl());
			list2.add(list.get(i).getSkbl());
		}
		list3.add(list1);
		list3.add(list2);
		hashMap.put("x", dateList);
		hashMap.put("y", list3);
		return hashMap;
		
	}
}
