package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjnewykrcMonthly;
import com.naswork.service.TjnewykrcMonthlyService;

/*节假日全市客源地占比分析*/
@RestController
@RequestMapping("/v2/mywllyrs")
public class TjLyrsMonthlyController extends BaseController {
	@Resource
	private TjnewykrcMonthlyService tjnewykrcMonthlyService;
	@RequestMapping("/{year_id}/{id_type}/{id}")
	public HashMap<String, Object> getQsmyjdrc(@PathVariable Integer year_id,@PathVariable Integer id_type,@PathVariable Integer id){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<TjnewykrcMonthly> list=new ArrayList<TjnewykrcMonthly>();
		List<String> dateList=new ArrayList<String>();
		List<Integer> rcList=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
			 list=tjnewykrcMonthlyService.getLyrsMonthly(year_id, id);/**各县区每月接待人次  **/
				for(int i=0;i<list.size();i++){
					dateList.add(list.get(i).getRecordmonth()+"月");
					rcList.add(list.get(i).getSubscribercount());
				}
		
		list3.add(rcList);
		hashMap.put("x", dateList);
		hashMap.put("y", list3);
		return hashMap;
		
	}
}
