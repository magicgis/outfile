package com.naswork.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjJdrsDailyDao;
import com.naswork.model.TjJdrsDaily;
import com.naswork.model.TjJdrsDailyKey;
import com.naswork.service.TjJdrsDailyService;

@Service("tjJdrsDailyService")
public class TjJdrsDailyServiceImpl implements TjJdrsDailyService {
	@Resource
	private TjJdrsDailyDao tjJdrsDailyDao;

	@Override
	public int deleteByPrimaryKey(TjJdrsDailyKey key) {
		return tjJdrsDailyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjJdrsDaily record) {
		return tjJdrsDailyDao.insert(record);
	}

	@Override
	public int insertSelective(TjJdrsDaily record) {
		return tjJdrsDailyDao.insertSelective(record);
	}

	@Override
	public TjJdrsDaily selectByPrimaryKey(TjJdrsDailyKey key) {
		return tjJdrsDailyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjJdrsDaily record) {
		return tjJdrsDailyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjJdrsDaily record) {
		return tjJdrsDailyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjJdrsDaily> selectById(Integer id, String startDate) {
		return tjJdrsDailyDao.selectById(id, startDate);
	}

	@Override
	public Integer selectByIdAndDate(Integer id, Date date) {
		return tjJdrsDailyDao.selectByIdAndDate(id, date);
	}

}
