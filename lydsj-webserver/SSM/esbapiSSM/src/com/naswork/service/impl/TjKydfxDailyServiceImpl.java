package com.naswork.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjKydfxDailyDao;
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.MyModel;
import com.naswork.model.TjKydfxDaily;
import com.naswork.model.TjKydfxDailyKey;
import com.naswork.service.TjKydfxDailyService;

@Service("tjKydfxDailyService")
public class TjKydfxDailyServiceImpl implements TjKydfxDailyService {
	@Resource
	private TjKydfxDailyDao tjKydfxDailyDao;

	@Override
	public int deleteByPrimaryKey(TjKydfxDailyKey key) {
		return tjKydfxDailyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjKydfxDaily record) {
		return tjKydfxDailyDao.insert(record);
	}

	@Override
	public int insertSelective(TjKydfxDaily record) {
		return tjKydfxDailyDao.insertSelective(record);
	}

	@Override
	public TjKydfxDaily selectByPrimaryKey(TjKydfxDailyKey key) {
		return tjKydfxDailyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjKydfxDaily record) {
		return tjKydfxDailyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjKydfxDaily record) {
		return tjKydfxDailyDao.updateByPrimaryKey(record);
	}

	@Override
	public Date getMaxDate() {
		return tjKydfxDailyDao.getMaxDate();
	}

	@Override
	public List<MyModel> selectTop5ById1(String maxDateStr, Integer areaRange) {
		return tjKydfxDailyDao.selectTop5ById1(maxDateStr, areaRange);
	}

	@Override
	public List<MyModel> selectTop5ById2(Integer id, String maxDateStr, Integer areaRange) {
		return tjKydfxDailyDao.selectTop5ById2(id, maxDateStr, areaRange);
	}

	@Override
	public List<TjKydfxDaily> selectByScope(Integer id, Integer scope, String idStr) {
		return tjKydfxDailyDao.selectByScope(id, scope, idStr);
	}

	@Override
	public List<KydfxTop5Data> selectTop5(Integer id, Integer scope, String idStr) {
		return tjKydfxDailyDao.selectTop5(id, scope, idStr);
	}

}
