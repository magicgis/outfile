package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.JqJdrsmMonthlyDao;
import com.naswork.model.JqJdrsmMonthly;
import com.naswork.service.JqJdrsmMonthlyService;

@Service("jqJdrsmMonthlyService")
public class JqJdrsmMonthlyServiceImpl implements JqJdrsmMonthlyService {
	@Resource
	private JqJdrsmMonthlyDao jqJdrsmMonthlyDao;
	@Override
	public List<JqJdrsmMonthly> getTsPercent(Integer id) {
		return jqJdrsmMonthlyDao.getTsPercent(id);
	}
	@Override
	public List<JqJdrsmMonthly> getTsPercentV2(String year, Integer id) {
		return jqJdrsmMonthlyDao.getTsPercentV2(year, id);
	}

}
