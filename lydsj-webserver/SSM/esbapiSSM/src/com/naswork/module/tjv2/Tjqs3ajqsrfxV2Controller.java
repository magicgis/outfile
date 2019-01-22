package com.naswork.module.tjv2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.Tjqs3ajqsrfxV2;
import com.naswork.service.Tjqs3ajqsrfxV2Service;

/*景区收入v2*/
@RestController
@RequestMapping("/v2/qs3ajqsrfx")
public class Tjqs3ajqsrfxV2Controller extends BaseController {
	@Resource
	private Tjqs3ajqsrfxV2Service tjqs3ajqsrfxV2Service;
	@RequestMapping("/{year_id}")
	public HashMap<String, Object> getJqIncome(@PathVariable String year_id){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<Tjqs3ajqsrfxV2> list=tjqs3ajqsrfxV2Service.getQs3ajqsrfxV2(year_id);
		List<String> dateList=new ArrayList<String>();
		List<String> incomelist=new ArrayList<String>();
		List<Object> objectlist=new ArrayList<Object>();
		for(int i=0;i<list.size();i++){
			String dateStr=sdf.format(list.get(i).getStatsdate());
			dateList.add(dateStr);
			String result = String.format("%.0f", list.get(i).getIncome());
			
			incomelist.add(result);
		}
		objectlist.add(incomelist);
		hashMap.put("x", dateList);
		hashMap.put("y", objectlist);
		return hashMap;
	}
}
