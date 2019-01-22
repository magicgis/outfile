package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.Kydfxv2;
import com.naswork.service.PlateAnalyticService;

@RestController
@RequestMapping("/v2/qszjyfx")
public class QszjyfxController extends BaseController {
	@Resource
	private PlateAnalyticService paService;
	
	
	@RequestMapping("/{year_id}/{id_type}/{id}")
	public HashMap<String, Object> getKydfxTop5(@PathVariable Integer year_id,@PathVariable Integer id_type,@PathVariable Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		String []name={"国内","省内","本市"};
		Double d3=0.0;
		Double d2=0.0;
		Double d1=0.0;
		List<Object> list = new ArrayList<>();

		Kydfxv2 k3=new Kydfxv2();
		Kydfxv2 k2=new Kydfxv2();
		Kydfxv2 k1=new Kydfxv2();
			d3=paService.getGuoneiPercent(year_id);
			d2=paService.getShengneiPercent(year_id);
			d1=paService.getBenshiPercent(year_id);
			k3.setName(name[0]);
			k3.setValue(d3);
			list.add(k3);
			k2.setName(name[1]);
			k2.setValue(d2);
			list.add(k2);
			k1.setName(name[2]);
			k1.setValue(d1);
			list.add(k1);
		res.put("data", list);
		return res;
	
		
	}
	

}
