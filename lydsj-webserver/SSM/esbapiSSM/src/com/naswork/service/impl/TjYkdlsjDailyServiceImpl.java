package com.naswork.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjYkdlsjDailyDao;
import com.naswork.model.TjYkdlsjDaily;
import com.naswork.model.TjYkdlsjDailyKey;
import com.naswork.service.TjYkdlsjDailyService;

@Service("tjYkdlsjDailyService")
public class TjYkdlsjDailyServiceImpl implements TjYkdlsjDailyService {
	@Resource
	private TjYkdlsjDailyDao tjYkdlsjDailyDao;
	
	@Override
	public int deleteByPrimaryKey(TjYkdlsjDailyKey key) {
		return tjYkdlsjDailyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjYkdlsjDaily record) {
		return tjYkdlsjDailyDao.insert(record);
	}

	@Override
	public int insertSelective(TjYkdlsjDaily record) {
		return tjYkdlsjDailyDao.insertSelective(record);
	}

	@Override
	public TjYkdlsjDaily selectByPrimaryKey(TjYkdlsjDailyKey key) {
		return tjYkdlsjDailyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjYkdlsjDaily record) {
		return tjYkdlsjDailyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjYkdlsjDaily record) {
		return tjYkdlsjDailyDao.updateByPrimaryKey(record);
	}

	@Override
	public Date getMaxTime() {
		return tjYkdlsjDailyDao.getMaxTime();
	}

	@Override
	public Integer getNum(Integer id, String idStr, Integer type) {
		return tjYkdlsjDailyDao.getNum(id, idStr, type);
	}

}
