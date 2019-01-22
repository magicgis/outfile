package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.naswork.dao.TjnewyrkMonthlyDao;
import com.naswork.model.TjnewyrkMonthly;
import com.naswork.service.TjnewyrkMonthlyService;

@Service("TjnewyrkMonthlyService")
public class TjnewyrkMonthlyServiceImpl implements TjnewyrkMonthlyService {
	@Resource
	private TjnewyrkMonthlyDao TjnewyrkMonthlyDao;
	@Override
	public List<TjnewyrkMonthly> getQsyryjdrsMonthly(Integer year) {
		return TjnewyrkMonthlyDao.getQsyryjdrsMonthly(year);
	}

}
