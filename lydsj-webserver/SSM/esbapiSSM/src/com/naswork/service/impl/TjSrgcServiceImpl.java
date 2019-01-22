package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjSrgcMonthlyMapper;
import com.naswork.model.TjSrgcMonthly;
import com.naswork.service.TjSrgcfxService;

/**
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 收入构成
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午1:49:44
 * 
 */
@Service
public class TjSrgcServiceImpl implements TjSrgcfxService {

	@Resource
	TjSrgcMonthlyMapper tjSrgcMonthlyMapper;

	/**
	 * @see com.naswork.service.TjSrgcfxService#getOneCNYmlnYearly()
	 */
	@Override
	public Map<String, Object> getOneCNYmlnYearly(Integer year, Integer level) {
		Map<String, Object> res = new HashMap<>();
		Map<String, Integer> paramMap = new HashMap<>();
		paramMap.put("year", year);
		paramMap.put("level", level);
		List<TjSrgcMonthly> oneCNYmlnYearly = tjSrgcMonthlyMapper.getOneCNYmlnYearly(paramMap);
		if (oneCNYmlnYearly != null) {
			List<Map<String, Object>> valueList = new ArrayList<>();
			for (TjSrgcMonthly t : oneCNYmlnYearly) {
				if (t != null) {
					Map<String, Object> map = new HashMap<>();
					map.put("name", t.getName());
					map.put("value", t.getIncome());
					valueList.add(map);
				}
			}
			res.put("data", valueList);
		}
		return res;
	}

}
