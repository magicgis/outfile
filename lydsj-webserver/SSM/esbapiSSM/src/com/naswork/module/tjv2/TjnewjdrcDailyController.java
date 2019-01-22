package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjnewjdrcDaily;
import com.naswork.service.TjnewjdrcDailyService;

/**
 * 全市/景区/全市3A景区每日接待人次
 * @author zhangwenwen
 *
 */
@RestController
@RequestMapping("/v2/mrjdrc")
public class TjnewjdrcDailyController extends BaseController {
	@Resource
	private TjnewjdrcDailyService tjnewjdrcDailyService;
	@RequestMapping("/{year_id}/{month_id}/{id_type}/{id}/{displayOption}")
	public HashMap<String, Object> getQsmyjdrc(@PathVariable Integer year_id,@PathVariable Integer month_id,
			@PathVariable Integer id_type,@PathVariable Integer id,@PathVariable Integer displayOption){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		
		List<TjnewjdrcDaily> list=new ArrayList<TjnewjdrcDaily>();
		List<String> dateList=new ArrayList<String>();
		List<Integer> rcList=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
		if (id_type==1||id_type==2||id_type==3) {
			 list=tjnewjdrcDailyService.getmrijdrcDaily(year_id, month_id, id);/**查询全市/本县区/景区每日接待人次    **/
		}else {   																 /**全市3A景区每月接待人次 **/
			list=tjnewjdrcDailyService.getQs3amrijdrcDaily(year_id, month_id, id_type-1);
		}
		
		if (displayOption==3) {
			for(int i=0;i<list.size();i++){
				dateList.add(list.get(i).getDay()+"日");
				rcList.add(list.get(i).getSubscribercount());
			}
		} else if (displayOption==4) {
			for(int i=0;i<list.size();i++){
				dateList.add(list.get(i).getMonth()+"月"+list.get(i).getDay()+"日");
				rcList.add(list.get(i).getSubscribercount());
			}
		} 


		list3.add(rcList);
		hashMap.put("x", dateList);
		hashMap.put("y", list3);
		return hashMap;
		
	}
}
