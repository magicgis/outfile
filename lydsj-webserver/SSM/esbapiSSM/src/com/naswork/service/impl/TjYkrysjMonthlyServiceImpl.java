package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjYkrysjMonthlyDao;
import com.naswork.model.TjYkrysjMonthly;
import com.naswork.model.TjYkrysjMonthlyKey;
import com.naswork.service.TjYkrysjMonthlyService;

@Service("tjYkrysjMonthlyService")
public class TjYkrysjMonthlyServiceImpl implements TjYkrysjMonthlyService {
	@Resource
	private TjYkrysjMonthlyDao tjYkrysjMonthlyDao; 
	
	@Override
	public int deleteByPrimaryKey(TjYkrysjMonthlyKey key) {
		return tjYkrysjMonthlyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjYkrysjMonthly record) {
		return tjYkrysjMonthlyDao.insert(record);
	}

	@Override
	public int insertSelective(TjYkrysjMonthly record) {
		return tjYkrysjMonthlyDao.insertSelective(record);
	}

	@Override
	public TjYkrysjMonthly selectByPrimaryKey(TjYkrysjMonthlyKey key) {
		return tjYkrysjMonthlyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjYkrysjMonthly record) {
		return tjYkrysjMonthlyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjYkrysjMonthly record) {
		return tjYkrysjMonthlyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjYkrysjMonthly> selectByMonth(Integer id, String idStr) {
		return tjYkrysjMonthlyDao.selectByMonth(id, idStr);
	}

}
