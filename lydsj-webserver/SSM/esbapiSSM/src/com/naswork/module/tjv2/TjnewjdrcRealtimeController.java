package com.naswork.module.tjv2;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjnewjdrcRealtime;
import com.naswork.service.TjnewjdrcRealtimeService;

/**
 * 景区实时接待人次
 * @author zhangwenwen
 *
 */
@RestController
@RequestMapping("/v2/jqdrssjdrc")
public class TjnewjdrcRealtimeController extends BaseController {
	@Resource
	private TjnewjdrcRealtimeService tjnewjdrcRealtimeService;
	@RequestMapping("/{id}")
	public HashMap<String, Object> getQsmyjdrc(@PathVariable Integer id){
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		Date today=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String curDate=df.format(today);
		String morning=curDate+" 08:30:00";
		String  night=curDate+" 19:30:00";
		 List<TjnewjdrcRealtime> list=new ArrayList<TjnewjdrcRealtime>();
		List<String> dateList=new ArrayList<String>();
		List<Integer> rcList=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
			 list=tjnewjdrcRealtimeService.getNewjdrcRealTime(curDate,morning,night,id);/**景区实时接待人次  **/
				for(int i=0;i<list.size();i++){
					dateList.add(list.get(i).getSecond());
					rcList.add(list.get(i).getSubscribercount());
				}
		list3.add(rcList);
		hashMap.put("x", dateList);
		hashMap.put("y", list3);
		return hashMap;
		
	}
}
