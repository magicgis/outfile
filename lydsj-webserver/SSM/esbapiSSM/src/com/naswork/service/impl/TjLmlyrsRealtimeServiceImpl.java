package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjLmlyrsRealtimeDao;
import com.naswork.model.TjLmlyrsRealtime;
import com.naswork.model.TjLmlyrsRealtimeKey;
import com.naswork.service.TjLmlyrsRealtimeService;

@Service("tjLmlyrsRealtimeService")
public class TjLmlyrsRealtimeServiceImpl implements TjLmlyrsRealtimeService {
	@Resource
	private TjLmlyrsRealtimeDao tjLmlyrsRealtimeDao;

	@Override
	public int deleteByPrimaryKey(TjLmlyrsRealtimeKey key) {
		return tjLmlyrsRealtimeDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjLmlyrsRealtime record) {
		return tjLmlyrsRealtimeDao.insert(record);
	}

	@Override
	public int insertSelective(TjLmlyrsRealtime record) {
		return tjLmlyrsRealtimeDao.insertSelective(record);
	}

	@Override
	public TjLmlyrsRealtime selectByPrimaryKey(TjLmlyrsRealtimeKey key) {
		return tjLmlyrsRealtimeDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjLmlyrsRealtime record) {
		return tjLmlyrsRealtimeDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjLmlyrsRealtime record) {
		return tjLmlyrsRealtimeDao.updateByPrimaryKey(record);
	}

	@Override
	public String getMaxIdentifier() {
		return tjLmlyrsRealtimeDao.getMaxIdentifier();
	}

	@Override
	public List<TjLmlyrsRealtime> selectByIdStr(Integer id, String idStr) {
		return tjLmlyrsRealtimeDao.selectByIdStr(id, idStr);
	}

}
