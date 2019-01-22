package com.naswork.module.tjv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.common.controller.BaseController;
import com.naswork.model.TjnewstationRealtime;
import com.naswork.service.TjnewstationRealtimeService;

@RestController
@RequestMapping("/v2/jtsnssjc")
public class TjnewstationRealtimeController extends BaseController {
	@Resource
	private TjnewstationRealtimeService tjnewstationRealtimeService;
	
	@RequestMapping("/{id_type}/{id}")
	public HashMap<String, Object> getStationCountRealtime(@PathVariable Integer id_type, @PathVariable Integer id) {

		HashMap<String, Object> res = new HashMap<>();
		String[] title = { "枢纽点",  "人次" };
		List<TjnewstationRealtime> list = new ArrayList<>();
		list =tjnewstationRealtimeService.getStationCountRealtime();
		List< List<Object> > dList = new ArrayList<>();
		for(TjnewstationRealtime tmp: list){
			List<Object> d = new ArrayList<>();
			d.add(tmp.getSpotName());
			d.add(tmp.getSubscribercount());
			dList.add(d);
		}
		res.put("title", title);
		res.put("data", dList);
		return res;
	}

}
