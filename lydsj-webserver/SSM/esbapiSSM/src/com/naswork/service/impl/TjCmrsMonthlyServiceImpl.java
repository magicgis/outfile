package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjCmrsMonthlyDao;
import com.naswork.model.TjCmrsMonthly;
import com.naswork.model.TjCmrsMonthlyKey;
import com.naswork.service.TjCmrsMonthlyService;

@Service("tjCmrsMonthlyService")
public class TjCmrsMonthlyServiceImpl implements TjCmrsMonthlyService {
	@Resource
	private TjCmrsMonthlyDao tjCmrsMonthlyDao;

	@Override
	public int deleteByPrimaryKey(TjCmrsMonthlyKey key) {
		return tjCmrsMonthlyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjCmrsMonthly record) {
		return tjCmrsMonthlyDao.insert(record);
	}

	@Override
	public int insertSelective(TjCmrsMonthly record) {
		return tjCmrsMonthlyDao.insertSelective(record);
	}

	@Override
	public TjCmrsMonthly selectByPrimaryKey(TjCmrsMonthlyKey key) {
		return tjCmrsMonthlyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjCmrsMonthly record) {
		return tjCmrsMonthlyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjCmrsMonthly record) {
		return tjCmrsMonthlyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjCmrsMonthly> selectByIdFromMonth(Integer id, String idStr) {
		return tjCmrsMonthlyDao.selectByIdFromMonth(id, idStr);
	}

	@Override
	public TjCmrsMonthly selectByIdAndIdstr(Integer id, String idStr) {
		return tjCmrsMonthlyDao.selectByIdAndIdstr(id, idStr);
	}

	@Override
	public List<TjCmrsMonthly> selectSomeMonths(Integer id, Integer monthsNum) {
		return tjCmrsMonthlyDao.selectSomeMonths(id, monthsNum);
	}


}
