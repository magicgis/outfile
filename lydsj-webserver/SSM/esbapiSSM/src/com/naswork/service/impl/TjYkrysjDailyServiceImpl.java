package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjYkrysjDailyDao;
import com.naswork.model.TjYkrysjDaily;
import com.naswork.model.TjYkrysjDailyKey;
import com.naswork.service.TjYkrysjDailyService;

@Service("tjYkrysjDailyService")
public class TjYkrysjDailyServiceImpl implements TjYkrysjDailyService {
	@Resource
	private TjYkrysjDailyDao tjYkrysjDailyDao;

	@Override
	public int deleteByPrimaryKey(TjYkrysjDailyKey key) {
		return tjYkrysjDailyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjYkrysjDaily record) {
		return tjYkrysjDailyDao.insert(record);
	}

	@Override
	public int insertSelective(TjYkrysjDaily record) {
		return tjYkrysjDailyDao.insertSelective(record);
	}

	@Override
	public TjYkrysjDaily selectByPrimaryKey(TjYkrysjDailyKey key) {
		return tjYkrysjDailyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjYkrysjDaily record) {
		return tjYkrysjDailyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjYkrysjDaily record) {
		return tjYkrysjDailyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjYkrysjDaily> selectByMonth(Integer id, String idStr) {
		return tjYkrysjDailyDao.selectByMonth(id, idStr);
	}

	@Override
	public List<TjYkrysjDaily> selectOneDay(Integer id, String idStr) {
		return tjYkrysjDailyDao.selectOneDay(id, idStr);
	}

}
