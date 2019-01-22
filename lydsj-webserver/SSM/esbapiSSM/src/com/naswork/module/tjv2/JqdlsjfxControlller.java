package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.Ring;
import com.naswork.service.TjYkdlsjMonthlyService;

/*游客逗留时间*/
@RestController
@RequestMapping("/v2/jqdlsjfx")
public class JqdlsjfxControlller extends BaseController {
	@Resource
	private TjYkdlsjMonthlyService tjYkdlsjMonthlyService;
	
	private final String[] names = { "0-2小时", "2-4小时", "4-6小时", "6-8小时",">8小时"};
	
	@RequestMapping("/{year_id}/{month_id}/{id_type}/{id}")
	public HashMap<String, Object> getYkdlsj(@PathVariable Integer year_id, @PathVariable Integer month_id,@PathVariable Integer id_type, @PathVariable Integer id) {
		HashMap<String, Object> res = new HashMap<>();
		List<Ring> list = new ArrayList<>();
		
		int num1=0;
		int num2=2;
		Double num5=0.0;
		if (id_type==1||id_type==2||id_type==3) {
			for(int i=0;i<5;i++){
				Ring r = new Ring();
				if (num2==10) {
					num5=tjYkdlsjMonthlyService.getJqdlsjfx(8, null, year_id, month_id, id);
				}else {
					num5=tjYkdlsjMonthlyService.getJqdlsjfx(num1, num2, year_id, month_id, id);
				}
				if (num5==null) num5=0.0;
				    r.setName(names[i]);
					r.setValue(num5);
					list.add(r);
					num1+=2;
					num2+=2;
			}
		}else {
			for(int i=0;i<5;i++){
				Ring r = new Ring();
			  if (num2==10) {
					num5=tjYkdlsjMonthlyService.getQsdlsjfx(8, null, year_id, month_id);
				}else {
					num5=tjYkdlsjMonthlyService.getQsdlsjfx(num1, num2, year_id, month_id);
				}
			  if (num5==null) num5=0.0;
				    r.setName(names[i]);
					r.setValue(num5);
					list.add(r);
					num1+=2;
					num2+=2;
			}
		}

		res.put("data", list);
		return res;
	}

}
