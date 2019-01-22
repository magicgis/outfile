package com.naswork.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjLmlyrsMonthlyDao;
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjLmlyrsMonthly;
import com.naswork.model.TjLmlyrsMonthlyKey;
import com.naswork.service.TjLmlyrsMonthlyService;

@Service("tjLmlyrsMonthlyService")
public class TjLmlyrsMonthlyServiceImpl implements TjLmlyrsMonthlyService {
	@Resource
	private TjLmlyrsMonthlyDao tjLmlyrsMonthlyDao;

	@Override
	public int deleteByPrimaryKey(TjLmlyrsMonthlyKey key) {
		return tjLmlyrsMonthlyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjLmlyrsMonthly record) {
		return tjLmlyrsMonthlyDao.insert(record);
	}

	@Override
	public int insertSelective(TjLmlyrsMonthly record) {
		return tjLmlyrsMonthlyDao.insertSelective(record);
	}

	@Override
	public TjLmlyrsMonthly selectByPrimaryKey(TjLmlyrsMonthlyKey key) {
		return tjLmlyrsMonthlyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjLmlyrsMonthly record) {
		return tjLmlyrsMonthlyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjLmlyrsMonthly record) {
		return tjLmlyrsMonthlyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjLmlyrsMonthly> selectByIdFromMonth(Integer id, String str) {
		return tjLmlyrsMonthlyDao.selectByIdFromMonth(id, str);
	}

	@Override
	public TjLmlyrsMonthly selectByIdAngYearAndMonth(Integer id, String year, String month) {
		return tjLmlyrsMonthlyDao.selectByIdAngYearAndMonth(id, year, month);
	}

	@Override
	public Integer CurMonthNum(Integer id, String idStr) {
		return tjLmlyrsMonthlyDao.curMonthNum(id, idStr);
	}

	@Override
	public List<TjLmlyrsMonthly> selectSomeMonth(Integer id, Integer monthsNum) {
		return tjLmlyrsMonthlyDao.selectSomeMonth(id, monthsNum);
	}

	@Override
	public List<TjLmlyrsMonthly> selectByLevel(Integer level, String idStr) {
		return tjLmlyrsMonthlyDao.selectByLevel(level, idStr);
	}

	@Override
	public BigDecimal curMonthTrend(Integer id, String identifier) {
		return tjLmlyrsMonthlyDao.curMonthTrend(id, identifier);
	}

	@Override
	public List<KydfxTop5Data> selectTop5Xq(String idStr, Integer id) {
		return tjLmlyrsMonthlyDao.selectTop5Xq(idStr, id);
	}
	
	@Override
	public List<KydfxTop5Data> selectTop5Jq(String idStr) {
		return tjLmlyrsMonthlyDao.selectTop5Jq(idStr);
	}

	@Override
	public int getTotalLmNum(Integer id, Integer year) {
		return tjLmlyrsMonthlyDao.getTotalLmNum(id, year);
	}

}
