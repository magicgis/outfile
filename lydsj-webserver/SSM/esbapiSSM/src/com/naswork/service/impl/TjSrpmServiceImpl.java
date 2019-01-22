package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjSrpmMonthlyMapper;
import com.naswork.model.TjSrpmMonthly;
import com.naswork.service.TjSrpmService;

/**
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 涉旅企业收入排名
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午1:57:19
 * 
 */
@Service
public class TjSrpmServiceImpl implements TjSrpmService {

	@Resource
	TjSrpmMonthlyMapper tjSrpmMonthlyMapper;

	/**
	 * @see com.naswork.service.TjSrpmService#getOneRankingMonthly(java.lang.Integer,
	 *      java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Map<String, Object> getOneIncomeRankingMonthly(Integer year, Integer month, Integer level, Integer rank) {
		Map<String, Object> res = new HashMap<>();
		Map<String, Integer> paramMap = new HashMap<>();
		paramMap.put("year", year);
		paramMap.put("month", month);
		paramMap.put("level", level);
		if (rank > 0 && rank < 11) {
			paramMap.put("rank", rank);
		}
		List<TjSrpmMonthly> incomeRank = tjSrpmMonthlyMapper.getOneIncomeRankingMonthly(paramMap);
		List<List<String>> valueList = formatData(incomeRank);
		res.put("data", valueList);
		res.put("title", "月收入排名");
		return res;
	}

	/**
	 * @see com.naswork.service.TjSrpmService#getOneRankingYearly(java.lang.Integer,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Map<String, Object> getOneSumIncomeRankingYearly(Integer year, Integer level, Integer rank) {

		Map<String, Object> res = new HashMap<>();
		Map<String, Integer> paramMap = new HashMap<>();
		paramMap.put("year", year);
		paramMap.put("level", level);
		if (rank > 0 && rank < 11) {
			paramMap.put("rank", rank);
		}
		List<TjSrpmMonthly> incomeRank = tjSrpmMonthlyMapper.getOneSumIncomeRankingYearly(paramMap);
		List<List<String>> valueList = formatData(incomeRank);
		res.put("data", valueList);
		res.put("title", "月收入排名");
		return res;
	}

	private List<List<String>> formatData(List<TjSrpmMonthly> incomeRank) {
		List<List<String>> valueList = new ArrayList<>();
		int k = 1;
		if (incomeRank != null) {
			for (int i = 0; i < incomeRank.size(); i++) {
				TjSrpmMonthly t = incomeRank.get(i);
				if (t != null) {
					List<String> list = new ArrayList<>();
					list.add(k++ + "");
					// list.add(t.getCode());
					list.add(t.getName());
					list.add(t.getIncome() + "");
					valueList.add(list);
				}
			} // end for
		} // end if
		return valueList;
	}
}
