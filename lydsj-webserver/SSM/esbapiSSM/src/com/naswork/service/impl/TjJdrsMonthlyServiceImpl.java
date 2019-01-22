package com.naswork.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjJdrsMonthlyDao;
import com.naswork.model.TjJdrsMonthly;
import com.naswork.model.TjJdrsMonthlyKey;
import com.naswork.service.TjJdrsMonthlyService;

@Service("TjJdrsMonthlyService")
public class TjJdrsMonthlyServiceImpl implements TjJdrsMonthlyService {
	@Resource
	private TjJdrsMonthlyDao tjJdrsMonthlyDao;

	@Override
	public int deleteByPrimaryKey(TjJdrsMonthlyKey key) {
		return tjJdrsMonthlyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjJdrsMonthly record) {
		return tjJdrsMonthlyDao.insert(record);
	}

	@Override
	public int insertSelective(TjJdrsMonthly record) {
		return tjJdrsMonthlyDao.insertSelective(record);
	}

	@Override
	public TjJdrsMonthly selectByPrimaryKey(TjJdrsMonthlyKey key) {
		return tjJdrsMonthlyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjJdrsMonthly record) {
		return tjJdrsMonthlyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjJdrsMonthly record) {
		return tjJdrsMonthlyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjJdrsMonthly> selectById(Integer id, String idStr) {
		return tjJdrsMonthlyDao.selectById(id, idStr);
	}

	@Override
	public TjJdrsMonthly selectByYearAndMonth(Integer id, String year, String month) {
		return tjJdrsMonthlyDao.selectByYearAndMonth(id, year, month);
	}

	@Override
	public Integer getMonthNum(Integer id, String identifier) {
		return tjJdrsMonthlyDao.getMonthNum(id, identifier);
	}

	@Override
	public List<TjJdrsMonthly> selectSomeMonths(Integer id, Integer monthsnum) {
		return tjJdrsMonthlyDao.selectSomeMonths(id, monthsnum);
	}

	@Override
	public BigDecimal curMonthTrend(Integer id, String identifier) {
		return tjJdrsMonthlyDao.curMonthTrend(id, identifier);
	}

	@Override
	public int getTotalJdNum(Integer id, Integer year) {
		return tjJdrsMonthlyDao.getTotalJdNum(id, year);
	}

}
