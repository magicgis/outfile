package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.KydfxData;
import com.naswork.model.TjKydfxMonthly;
import com.naswork.service.TjKydfxMonthlyService;

/*客源地分析*/
@RestController
@RequestMapping("/v2/kydfx")
public class KydfxV2Controller extends BaseController {
	@Resource
	private TjKydfxMonthlyService tjKydfxMonthlyService;
	
	@RequestMapping("/{id}/{year_id}/{month_id}/{area_range}")
	public HashMap<String, Object> getKydfx(@PathVariable Integer id, 
			@PathVariable Integer year_id, @PathVariable Integer month_id, @PathVariable Integer area_range) {
		HashMap<String, Object> res = new HashMap<>();
		if (area_range!=null&&area_range==2) {
			area_range=2;		//如果传参过来的area_range=2，则赋值为2，查询省内的客源地来梅人数，并排名
		}else if (area_range!=null&&area_range==1) {
			area_range=3;		//如果传参过来的area_range=1，则赋值3，查询国内的客源地来梅人数并排名
		}else if (area_range!=null&&area_range==5) {
			area_range=4;		//如果传参过来的area_range=5，则赋值4，查询国际的客源地来梅人数并排名
		}else if (area_range!=null&&area_range==3) {
			area_range=5;		//如果传参过来的area_range=3，则赋值5，来梅:非本市，就是外地游客，国外+国内+省内并排名
		}else if (area_range!=null&&area_range==4) {
			area_range=6;		//如果传参过来的area_range=3，则赋值6，全部:含本市，就是外地游客+本市游客，国外+国内+省内+本市并排名
		}
		List<TjKydfxMonthly> list = tjKydfxMonthlyService.selectAllByMonth(id, year_id, month_id, area_range);
		List<KydfxData> dataList = new ArrayList<>();
		for(TjKydfxMonthly tmp: list){
			String name = "";
			Integer num = 0;
			KydfxData k = new KydfxData();
			name = tmp.getSourcename();
			if(area_range.equals(2)==true) name += "市";
			if(tmp.getPercent()!=null) num = tmp.getPercent();
			k.setName(name);
			k.setValue(num);
			dataList.add(k);
		}
		res.put("data", dataList);
		return res;
	}



}
