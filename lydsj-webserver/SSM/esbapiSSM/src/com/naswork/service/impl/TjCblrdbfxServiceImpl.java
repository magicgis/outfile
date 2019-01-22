package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naswork.dao.TjCblrdbfxMonthlyMapper;
import com.naswork.model.CostVSProfit;
import com.naswork.service.TjCblrdbfxService;

/**
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 成本利润对比分析
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午12:52:53
 * 
 */
@Service
public class TjCblrdbfxServiceImpl implements TjCblrdbfxService {
	@Autowired
	TjCblrdbfxMonthlyMapper tjCblrdbfxMonthlyMapper;

	/**
	 * @see com.naswork.service.TjCblrdbfxService#getOneCostVSProfitYearly()
	 */
	public Map<String, Object> getCostVSProfitYearly(Integer year, Integer level) {
		Map<String, Integer> paramMap = new HashMap<>();
		Map<String, Object> res = new HashMap<>();

		List<Map<String, Object>> valueList = new ArrayList<>();
		// 封装参数
		paramMap.put("year", year);
		paramMap.put("level", level);
		// 计算成本和利润
		CostVSProfit costVSProfit = tjCblrdbfxMonthlyMapper.getCostVSProfitYearly(paramMap);

		// 封装数据
		Map<String, Object> costMap = new HashMap<>();
		Map<String, Object> profitMap = new HashMap<>();
		if (costVSProfit != null) {
			costMap.put("value", costVSProfit.getCost());
			costMap.put("name", "成本");
			profitMap.put("value", costVSProfit.getProfit());
			profitMap.put("name", "利润");
		}else {
			res.put("data", valueList);
			return res;
		}
		valueList.add(costMap);
		valueList.add(profitMap);

		res.put("data", valueList);
		return res;
	}

}
