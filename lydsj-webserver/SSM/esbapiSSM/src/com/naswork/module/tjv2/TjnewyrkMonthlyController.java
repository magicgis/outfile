package com.naswork.module.tjv2;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjnewyrkMonthly;
import com.naswork.service.TjnewyrkMonthlyService;

/**
 * 全市一日游接待人数
 * @author zhangwenwen
 *
 */
@RestController
@RequestMapping("/v2/qsyryjdrs")
public class TjnewyrkMonthlyController extends BaseController {
	@Resource
	private TjnewyrkMonthlyService tjnewyrkMonthlyService;
	@RequestMapping("/{year_id}/{id_type}/{id}")
	public HashMap<String, Object> getQsmyjdrc(@PathVariable Integer year_id,@PathVariable Integer id_type,@PathVariable Integer id){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		 List<TjnewyrkMonthly> list=new ArrayList<TjnewyrkMonthly>();
		List<String> dateList=new ArrayList<String>();
		List<Integer> rcList=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
			 list=tjnewyrkMonthlyService.getQsyryjdrsMonthly(year_id);/**全市一日游接待人数  **/
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
