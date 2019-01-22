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

@RestController
@RequestMapping("/v2/jqjdrcypm")
public class TjJqjdrcypmController extends BaseController {
	@Resource
	private TjnewjdrcMonthlyService tjnewjdrcMonthlyService;
	
	@RequestMapping("/{year_id}/{month_id}/{id_type}/{id}")
	public HashMap<String, Object> getTjJqjdrcndpm( @PathVariable Integer year_id,  @PathVariable Integer month_id,  @PathVariable Integer id_type,@PathVariable Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		if (id_type==1||id_type==2||id_type==3) {
			String[] title = { "排名", "地点", "人次" };
			 List<TjnewjdrcMonthly> list = tjnewjdrcMonthlyService.getBxjdrcndpm(year_id, month_id, id);
			List< List<Object> > dList = new ArrayList<>();
			Integer i = 1;
			for(TjnewjdrcMonthly tmp: list){
				List<Object> d = new ArrayList<>();
				d.add(i);
				d.add(tmp.getName());
				d.add(tmp.getValue());
				dList.add(d);
				i++;
			}
			res.put("title", title);
			res.put("data", dList);
		}else{
			String[] title = { "排名", "景点","县区", "人次" };
			 List<TjnewjdrcMonthly> list = tjnewjdrcMonthlyService.getJq3ajdrcypm(year_id, month_id, id_type-1);
			List< List<Object> > dList = new ArrayList<>();
			Integer i = 1;
			for(TjnewjdrcMonthly tmp: list){
				List<Object> d = new ArrayList<>();
				d.add(i);
				d.add(tmp.getName());
				d.add(tmp.getDistrictName());
				d.add(tmp.getValue());
				dList.add(d);
				i++;
			}
			res.put("title", title);
			res.put("data", dList);
		}
		return res;
	
	}

}
