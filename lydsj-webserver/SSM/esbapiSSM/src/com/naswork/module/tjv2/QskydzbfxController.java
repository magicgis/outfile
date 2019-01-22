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
import com.naswork.service.TjKyddbfxMonthlyService;

/*客源地对比分析*/
@RestController
@RequestMapping("/v2/qskydzbfx")
public class QskydzbfxController extends BaseController {
	@Resource
	private TjKyddbfxMonthlyService tjKyddbfxMonthlyService;

	
	@RequestMapping("/{year_id}/{id_type}/{id}")
	public HashMap<String, Object> getKyddbfx(@PathVariable Integer year_id,@PathVariable Integer id_type,@PathVariable Integer id) {
		
		String []name={"国外","国内","省内"};
		HashMap<String, Object> res = new HashMap<>();
		Double d4=0.0;
		Double d3=0.0;
		Double d2=0.0;
		List<Object> datas = new ArrayList<>();
		Kydfxv2 k4=new Kydfxv2();
		Kydfxv2 k3=new Kydfxv2();
		Kydfxv2 k2=new Kydfxv2();
			d4=tjKyddbfxMonthlyService.getGuojiPercent(year_id);
			d3=tjKyddbfxMonthlyService.getGuogneiPercent(year_id);
			d2=tjKyddbfxMonthlyService.getShengneiPercent(year_id);
			k4.setName(name[0]);
			k4.setValue(d4);
			datas.add(k4);	//国际
			k3.setName(name[1]);
			k3.setValue(d3);
			datas.add(k3);	//国内
			k2.setName(name[2]);
			k2.setValue(d2);
			datas.add(k2);	//省内
		res.put("data", datas);
		return res;
	}
}
