package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjnewjdrcMonthlyDao;
import com.naswork.model.TjnewjdrcMonthly;
import com.naswork.service.TjnewjdrcMonthlyService;

@Service("tjnewjdrcMonthlyService")
public class TjnewjdrcMonthlyServiceImpl implements TjnewjdrcMonthlyService {
	@Resource
	private TjnewjdrcMonthlyDao tjnewjdrcMonthlyDao;
	@Override
	public List<TjnewjdrcMonthly> getTjnewjdrcMonthly(Integer year,Integer id) {
		return tjnewjdrcMonthlyDao.getTjnewjdrcMonthly(year, id);
	}
	@Override
	public List<TjnewjdrcMonthly> getBxqjdrcMonthly(Integer year, Integer id) {
		return tjnewjdrcMonthlyDao.getBxqjdrcMonthly(year, id);
	}
	@Override
	public List<TjnewjdrcMonthly> getQs3ajdrcMonthly(Integer year, Integer level) {
		return tjnewjdrcMonthlyDao.getQs3ajdrcMonthly(year, level);
	}
	@Override
	public List<TjnewjdrcMonthly> getGxqjdrcMonthly(Integer year, Integer month) {
		return tjnewjdrcMonthlyDao.getGxqjdrcMonthly(year, month);
	}
	@Override
	public List<TjnewjdrcMonthly> getJqjdrcndpm(Integer year, Integer id) {
		return tjnewjdrcMonthlyDao.getJqjdrcndpm(year, id);
	}
	@Override
	public List<TjnewjdrcMonthly> getJq3ajdrcndpm(Integer year) {
		return tjnewjdrcMonthlyDao.getJq3ajdrcndpm(year);
	}
	@Override
	public List<TjnewjdrcMonthly> getBxjdrcndpm(Integer year, Integer month, Integer id) {
		return tjnewjdrcMonthlyDao.getBxjdrcndpm(year, month, id);
	}
	@Override
	public List<TjnewjdrcMonthly> getJq3ajdrcypm(Integer year, Integer month, Integer level) {
		return tjnewjdrcMonthlyDao.getJq3ajdrcypm(year, month, level);
	}
	@Override
	public List<TjnewjdrcMonthly> getMx3ajqjdrs(Integer year, Integer id) {
		return tjnewjdrcMonthlyDao.getMx3ajqjdrs(year, id);
	}

}
