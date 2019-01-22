package com.naswork.module.tjv3;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.service.TjSrfxService;

/**
 * 
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 涉旅企业收入分析
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午3:32:53
 *
 */
@RestController
@RequestMapping("/v3")
public class TjSrfxController {
	@Autowired
	TjSrfxService tjSrfxService;

	@RequestMapping("/slqyysrfx/{year}")
	public Map<String, Object> getTotalIncomeMonthly(@PathVariable Integer year) {
		return tjSrfxService.getTotalIncomeMonthly(year);

	}

	@RequestMapping("/slqynsrfx")
	public Map<String, Object> getTotalSumIncomeYearly() {
		return tjSrfxService.getTotalSumIncomeYearly();

	}

	@RequestMapping("/slqynsrdbfx")
	public Map<String, Object> getAllSumIncomeYearly() {
		return tjSrfxService.getAllSumIncomeYearly();

	}

	@RequestMapping("/ysrfx/{year}/{level}")
	public Map<String, Object> getOneIncomeMonthly(@PathVariable Integer year, @PathVariable Integer level) {
		return tjSrfxService.getOneIncomeMonthly(year, level);

	}

	@RequestMapping("/nsrfx/{level}")
	public Map<String, Object> getOneSumIncomeYearly(@PathVariable Integer level) {
		return tjSrfxService.getOneSumIncomeYearly(level);

	}

}
