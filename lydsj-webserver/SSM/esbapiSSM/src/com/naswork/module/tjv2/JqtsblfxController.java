package com.naswork.module.tjv2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.model.JqJdrsmMonthly;
import com.naswork.model.Tj3ajqtsblfxV2Monthly;
import com.naswork.service.JqJdrsmMonthlyService;
import com.naswork.service.Tj3ajqtsblfxV2MonthlyService;

/*团散比例V2*/
@RestController
@RequestMapping("/v2/jqtsbfx")
public class JqtsblfxController {
	@Resource
	private Tj3ajqtsblfxV2MonthlyService tj3ajqtsblfxV2MonthlyService;
	@Resource
	private JqJdrsmMonthlyService jqJdrsmMonthlyService;
	
	@RequestMapping("/{year_id}/{id_type}/{id}")
	public HashMap<String, Object> getTsbl(@PathVariable String year_id,@PathVariable Integer id_type,@PathVariable Integer id){
		SimpleDateFormat sdf=new SimpleDateFormat("M");
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<String> dateList=new ArrayList<String>();
		List<Integer> list1=new ArrayList<Integer>();
		List<Integer> list2=new ArrayList<Integer>();
		List<Object> list3=new ArrayList<Object>();
		
		
		if (id_type==1||id_type==2||id_type==3) {
			List<JqJdrsmMonthly> list=jqJdrsmMonthlyService.getTsPercentV2(year_id, id);
			for(int i=0;i<list.size();i++){
				String dateStr=sdf.format(list.get(i).getStatsdate())+"月";
				dateList.add(dateStr);
				list1.add(list.get(i).getGtbl());
				list2.add(list.get(i).getSkbl());
			}
		}else {
			List<Tj3ajqtsblfxV2Monthly> list=tj3ajqtsblfxV2MonthlyService.getJqtsblfx3a(year_id);
			for(int i=0;i<list.size();i++){
				String dateStr=sdf.format(list.get(i).getStatsdate())+"月";
				dateList.add(dateStr);
				list1.add(list.get(i).getGtbl());
				list2.add(list.get(i).getSkbl());
			}
		}
		
		list3.add(list1);
		list3.add(list2);
		hashMap.put("x", dateList);
		hashMap.put("y", list3);
		return hashMap;
		
	}
}
