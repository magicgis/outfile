package com.naswork.module.tjv3;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.service.TjCblrdbfxService;

/**
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 涉旅企业成本利润对比分析
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日  下午3:26:38
 * 
 */
@RestController
@RequestMapping("/v3")
public class TjCblrdbfxController {
	@Autowired
	TjCblrdbfxService tjCblrdbfxService;

	@RequestMapping("/cblrdbfx/{year}/{level}")
	public Map<String, Object> getAllCostVSProfitYearly(@PathVariable Integer year, @PathVariable Integer level) {
		return tjCblrdbfxService.getCostVSProfitYearly(year, level);
	}

}
