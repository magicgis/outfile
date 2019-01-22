package com.naswork.module.tjv3;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naswork.service.TjSrpmService;

/**
 * 
* Copyright: Copyright (c) 2018 cheng
* @Description 
*
* @version v1.0.0
* @author cheng
* @date 2018年11月3日 下午2:51:29 
*
 */
@RestController
@RequestMapping("/v3")
public class TjSrpmController {
	@Autowired
	TjSrpmService tjSrpmService; 

	@RequestMapping("/ysrpm/{rank}/{year}/{month}/{level}")
	public Map<String, Object> getOneIncomeRankingMonthly(@PathVariable Integer year, @PathVariable Integer month,
			@PathVariable Integer level, @PathVariable Integer rank) {
		return tjSrpmService.getOneIncomeRankingMonthly(year, month, level, rank);
	}
	@RequestMapping("/nsrpm/{rank}/{year}/{level}")
	public Map<String, Object> getOneSumIncomeRankingYearly(@PathVariable Integer year, @PathVariable Integer level,
			@PathVariable Integer rank) {
		return tjSrpmService.getOneSumIncomeRankingYearly(year, level, rank);
	}
}
