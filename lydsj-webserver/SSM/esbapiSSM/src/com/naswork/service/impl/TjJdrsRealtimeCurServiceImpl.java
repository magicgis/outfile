package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjJdrsRealtimeCurDao;
import com.naswork.model.TjJdrsRealtimeCur;
import com.naswork.model.TjJdrsRealtimeCurKey;
import com.naswork.service.TjJdrsRealtimeCurService;

@Service("tjJdrsRealtimeCurService")
public class TjJdrsRealtimeCurServiceImpl implements TjJdrsRealtimeCurService {
	@Resource
	private TjJdrsRealtimeCurDao tjJdrsRealtimeCurDao; 

	@Override
	public int deleteByPrimaryKey(TjJdrsRealtimeCurKey key) {
		return tjJdrsRealtimeCurDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjJdrsRealtimeCur record) {
		return tjJdrsRealtimeCurDao.insert(record);
	}

	@Override
	public int insertSelective(TjJdrsRealtimeCur record) {
		return tjJdrsRealtimeCurDao.insertSelective(record);
	}

	@Override
	public TjJdrsRealtimeCur selectByPrimaryKey(TjJdrsRealtimeCurKey key) {
		return tjJdrsRealtimeCurDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjJdrsRealtimeCur record) {
		return tjJdrsRealtimeCurDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjJdrsRealtimeCur record) {
		return tjJdrsRealtimeCurDao.updateByPrimaryKey(record);
	}

	@Override
	public TjJdrsRealtimeCur selectCur(Integer id) {
		return tjJdrsRealtimeCurDao.selectCur(id);
	}


}
