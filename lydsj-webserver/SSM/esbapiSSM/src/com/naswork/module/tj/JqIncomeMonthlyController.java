package com.naswork.module.tj;

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
import com.naswork.service.JqIncomeMonthlyService;

/*景区收入*/
@RestController
@RequestMapping("/v1/jqsr")
public class JqIncomeMonthlyController extends BaseController {
	@Resource
	private JqIncomeMonthlyService jqIncomeMonthlyService;
	@RequestMapping("/{id}/5")
	public HashMap<String, Object> getJqIncome(@PathVariable Integer id){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		HashMap<String, Object> hashMap=new HashMap<String, Object>();
		List<JqIncomeMonthly> list=jqIncomeMonthlyService.getJqIncomeMonthly(id);
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
