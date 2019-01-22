package com.naswork.module.tjv2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.model.Tj3ajqtsblfxV2Monthly;
import com.naswork.service.Tj3ajqtsblfxV2MonthlyService;

/*3a景区团散比例*/
@RestController
@RequestMapping("/v2/qs3ajqtsbfx")
public class Jqtsblfx3aController {
	@Resource
	private Tj3ajqtsblfxV2MonthlyService tj3ajqtsblfxV2MonthlyService;
	
	@RequestMapping("/{year}")
	public HashMap<String, Object> getTsbl(@PathVariable String year){
		SimpleDateFormat sdf=new SimpleDateFormat("M");
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<Tj3ajqtsblfxV2Monthly> list=tj3ajqtsblfxV2MonthlyService.getJqtsblfx3a(year);
		List<String> dateList=new ArrayList<String>();
		List<Integer> list1=new ArrayList<Integer>();
		List<Integer> list2=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
		for(int i=0;i<list.size();i++){
			String dateStr=sdf.format(list.get(i).getStatsdate())+"月";
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
