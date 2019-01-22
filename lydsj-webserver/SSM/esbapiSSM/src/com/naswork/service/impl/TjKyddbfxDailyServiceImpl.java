package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjKyddbfxDailyDao;
import com.naswork.model.TjKyddbfxDaily;
import com.naswork.model.TjKyddbfxDailyKey;
import com.naswork.service.TjKyddbfxDailyService;

@Service("tjKyddbfxDailyService")
public class TjKyddbfxDailyServiceImpl implements TjKyddbfxDailyService {
	@Resource
	private TjKyddbfxDailyDao tjKyddbfxDailyDao; 

	@Override
	public int deleteByPrimaryKey(TjKyddbfxDailyKey key) {
		return tjKyddbfxDailyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjKyddbfxDaily record) {
		return tjKyddbfxDailyDao.insert(record);
	}

	@Override
	public int insertSelective(TjKyddbfxDaily record) {
		return tjKyddbfxDailyDao.insertSelective(record);
	}

	@Override
	public TjKyddbfxDaily selectByPrimaryKey(TjKyddbfxDailyKey key) {
		return tjKyddbfxDailyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjKyddbfxDaily record) {
		return tjKyddbfxDailyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjKyddbfxDaily record) {
		return tjKyddbfxDailyDao.updateByPrimaryKey(record);
	}

	@Override
	public TjKyddbfxDaily selectByIdAndDate4(Integer id, String idStr) {
		return tjKyddbfxDailyDao.selectByIdAndDate4(id, idStr);
	}

	@Override
	public TjKyddbfxDaily selectByIdAndDate3(Integer id, String idStr) {
		return tjKyddbfxDailyDao.selectByIdAndDate3(id, idStr);
	}

	@Override
	public TjKyddbfxDaily selectByIdAndDate2(Integer id, String idStr) {
		return tjKyddbfxDailyDao.selectByIdAndDate2(id, idStr);
	}

}
