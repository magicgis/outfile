package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjCmrsDailyDao;
import com.naswork.model.TjCmrsDaily;
import com.naswork.model.TjCmrsDailyKey;
import com.naswork.service.TjCmrsDailyService;

@Service("tjCmrsDailyService")
public class TjCmrsDailyServiceImpl implements TjCmrsDailyService {
	@Resource
	private TjCmrsDailyDao tjCmrsDailyDao;

	@Override
	public int deleteByPrimaryKey(TjCmrsDailyKey key) {
		return tjCmrsDailyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjCmrsDaily record) {
		return tjCmrsDailyDao.insert(record);
	}

	@Override
	public int insertSelective(TjCmrsDaily record) {
		return tjCmrsDailyDao.insertSelective(record);
	}

	@Override
	public TjCmrsDaily selectByPrimaryKey(TjCmrsDailyKey key) {
		return tjCmrsDailyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjCmrsDaily record) {
		return tjCmrsDailyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjCmrsDaily record) {
		return tjCmrsDailyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjCmrsDaily> selectByIdAndStartDay(Integer id, String startDay) {
		return tjCmrsDailyDao.selectByIdAndStartDay(id, startDay);
	}

}
