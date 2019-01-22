package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjnewykrcMonthlyDao;
import com.naswork.model.TjnewykrcMonthly;
import com.naswork.service.TjnewykrcMonthlyService;

@Service("TjnewykrcMonthlyService")
public class TjnewykrcMonthlyServiceImpl implements TjnewykrcMonthlyService {
	@Resource
	private TjnewykrcMonthlyDao tjnewykrcMonthlyDao;

	@Override
	public List<TjnewykrcMonthly> getLyrsMonthly(Integer year, Integer id) {
		return tjnewykrcMonthlyDao.getLyrsMonthly(year, id);
	}

}
