package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjnewjdrcMonthly;
import com.naswork.service.TjnewjdrcMonthlyService;

/*节假日全市客源地占比分析*/
@RestController
@RequestMapping("/v2/gxqmyjdrc")
public class TjgxqjdrcMonthlyController extends BaseController {
	@Resource
	private TjnewjdrcMonthlyService tjnewjdrcMonthlyService;
	@RequestMapping("/{year_id}/{month_id}")
	public HashMap<String, Object> getQsmyjdrc(@PathVariable Integer year_id,@PathVariable Integer month_id){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<TjnewjdrcMonthly> list=new ArrayList<TjnewjdrcMonthly>();
		List<String> dateList=new ArrayList<String>();
		List<Integer> rcList=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
			 list=tjnewjdrcMonthlyService.getGxqjdrcMonthly(year_id, month_id);/**各县区每月接待人次  **/
				for(int i=0;i<list.size();i++){
					dateList.add(list.get(i).getName());
					rcList.add(list.get(i).getValue());
				}
		
		list3.add(rcList);
		hashMap.put("x", dateList);
		hashMap.put("y", list3);
		return hashMap;
		
	}
}
