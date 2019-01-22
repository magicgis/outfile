package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjYkdlsjRealtimeCurDao;
import com.naswork.model.Ring;
import com.naswork.model.TjYkdlsjRealtimeCur;
import com.naswork.model.TjYkdlsjRealtimeCurKey;
import com.naswork.service.TjYkdlsjRealtimeCurService;

@Service("tjYkdlsjRealtimeCurService")
public class TjYkdlsjRealtimeCurServiceImpl implements TjYkdlsjRealtimeCurService {
	@Resource
	private TjYkdlsjRealtimeCurDao tjYkdlsjRealtimeCurDao; 

	@Override
	public int deleteByPrimaryKey(TjYkdlsjRealtimeCurKey key) {
		return tjYkdlsjRealtimeCurDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjYkdlsjRealtimeCur record) {
		return tjYkdlsjRealtimeCurDao.insert(record);
	}

	@Override
	public int insertSelective(TjYkdlsjRealtimeCur record) {
		return tjYkdlsjRealtimeCurDao.insertSelective(record);
	}

	@Override
	public TjYkdlsjRealtimeCur selectByPrimaryKey(TjYkdlsjRealtimeCurKey key) {
		return tjYkdlsjRealtimeCurDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjYkdlsjRealtimeCur record) {
		return tjYkdlsjRealtimeCurDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjYkdlsjRealtimeCur record) {
		return tjYkdlsjRealtimeCurDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjYkdlsjRealtimeCur> selectById(Integer id) {
		return tjYkdlsjRealtimeCurDao.selectById(id);
	}

}
