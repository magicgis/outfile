package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjYkdlsjMonthlyDao;
import com.naswork.model.TjYkdlsjMonthly;
import com.naswork.model.TjYkdlsjMonthlyKey;
import com.naswork.service.TjYkdlsjMonthlyService;

@Service("tjYkdlsjMonthlyService")
public class TjYkdlsjMonthlyServiceImpl implements TjYkdlsjMonthlyService {
	@Resource
	private TjYkdlsjMonthlyDao tjYkdlsjMonthlyDao;

	@Override
	public int deleteByPrimaryKey(TjYkdlsjMonthlyKey key) {
		return tjYkdlsjMonthlyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjYkdlsjMonthly record) {
		return tjYkdlsjMonthlyDao.insert(record);
	}

	@Override
	public int insertSelective(TjYkdlsjMonthly record) {
		return tjYkdlsjMonthlyDao.insertSelective(record);
	}

	@Override
	public TjYkdlsjMonthly selectByPrimaryKey(TjYkdlsjMonthlyKey key) {
		return tjYkdlsjMonthlyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjYkdlsjMonthly record) {
		return tjYkdlsjMonthlyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjYkdlsjMonthly record) {
		return tjYkdlsjMonthlyDao.updateByPrimaryKey(record);
	}

	@Override
	public Integer getNum(Integer id, String idStr, Integer type) {
		return tjYkdlsjMonthlyDao.getNum(id, idStr, type);
	}

	@Override
	public Double getQsdlsjfx(Integer little,Integer great,Integer year, Integer month) {
		return tjYkdlsjMonthlyDao.getQsdlsjfx(little, great, year, month);
	}

	@Override
	public Double getJqdlsjfx(Integer little, Integer great, Integer year, Integer month, Integer id) {
		return tjYkdlsjMonthlyDao.getJqdlsjfx(little, great, year, month, id);
	}

}
