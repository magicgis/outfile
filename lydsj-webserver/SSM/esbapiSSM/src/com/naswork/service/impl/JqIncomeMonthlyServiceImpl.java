package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.JqIncomeMonthlyDao;
import com.naswork.model.JqIncomeMonthly;
import com.naswork.service.JqIncomeMonthlyService;

@Service("jqIncomeMonthlyService")
public class JqIncomeMonthlyServiceImpl implements JqIncomeMonthlyService {
	@Resource
	private JqIncomeMonthlyDao jqIncomeMonthlyDao;
	@Override
	public List<JqIncomeMonthly> getJqIncomeMonthly(Integer id) {
		return jqIncomeMonthlyDao.getJqIncomeMonthly(id);
	}
	@Override
	public List<JqIncomeMonthly> getJqIncomeMonthlyV2(String year, Integer id) {
		return jqIncomeMonthlyDao.getJqIncomeMonthlyV2(year, id);
	}

}
