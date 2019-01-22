package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjnewgykMonthlyDao;
import com.naswork.model.TjnewgykMonthly;
import com.naswork.service.TjnewgykMonthlyService;
@Service("TjnewgykMonthlyService")
public class TjnewgykMonthlyServiceImpl implements TjnewgykMonthlyService {
	@Resource
	private TjnewgykMonthlyDao tjnewgykMonthlyDao;
	@Override
	public List<TjnewgykMonthly> getQsgyyjdrs(Integer year) {
		return tjnewgykMonthlyDao.getQsgyyjdrs(year);
	}

}
