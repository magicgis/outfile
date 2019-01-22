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
import com.naswork.model.JqIncomeMonthly;
import com.naswork.model.Tjqs3ajqsrfxV2;
import com.naswork.service.JqIncomeMonthlyService;
import com.naswork.service.Tjqs3ajqsrfxV2Service;

/*景区收入v2*/
@RestController
@RequestMapping("/v2/jqsrfx")
public class JqsrfxMonthlyController extends BaseController {
	@Resource
	private Tjqs3ajqsrfxV2Service tjqs3ajqsrfxV2Service;
	@Resource
	private JqIncomeMonthlyService jqIncomeMonthlyService;
	@RequestMapping("/{year_id}/{id_type}/{id}")
	public HashMap<String, Object> getJqIncome(@PathVariable String year_id,@PathVariable Integer id_type,@PathVariable Integer id){
		SimpleDateFormat sdf=new SimpleDateFormat("M");
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<String> dateList=new ArrayList<String>();
		List<String> incomelist=new ArrayList<String>();
		List<Object> objectlist=new ArrayList<Object>();
		if (id_type==1||id_type==2||id_type==3) {
			List<JqIncomeMonthly> list=jqIncomeMonthlyService.getJqIncomeMonthlyV2(year_id, id);
			for(int i=0;i<list.size();i++){
				String dateStr=sdf.format(list.get(i).getStatsdate())+"月";
				dateList.add(dateStr);
				String result = list.get(i).getIncome().toString();
				incomelist.add(result);
			}
		}else {
			List<Tjqs3ajqsrfxV2> list=tjqs3ajqsrfxV2Service.getQs3ajqsrfxV2(year_id);
			for(int i=0;i<list.size();i++){
				String dateStr=sdf.format(list.get(i).getStatsdate())+"月";
				dateList.add(dateStr);
				String result = list.get(i).getIncome().toString();
				incomelist.add(result);
			}
		}
		objectlist.add(incomelist);
		hashMap.put("x", dateList);
		hashMap.put("y", objectlist);
		return hashMap;
	}
}
