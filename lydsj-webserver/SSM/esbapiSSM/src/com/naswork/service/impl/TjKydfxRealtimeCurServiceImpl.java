package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjKydfxRealtimeCurDao;
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjKydfxRealtimeCur;
import com.naswork.model.TjKydfxRealtimeCurKey;
import com.naswork.service.TjKydfxRealtimeCurService;

@Service("tjKydfxRealtimeCurService")
public class TjKydfxRealtimeCurServiceImpl implements TjKydfxRealtimeCurService {
	@Resource
	private TjKydfxRealtimeCurDao tjKydfxRealtimeCurDao;

	@Override
	public int deleteByPrimaryKey(TjKydfxRealtimeCurKey key) {
		return tjKydfxRealtimeCurDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjKydfxRealtimeCur record) {
		return tjKydfxRealtimeCurDao.insert(record);
	}

	@Override
	public int insertSelective(TjKydfxRealtimeCur record) {
		return tjKydfxRealtimeCurDao.insertSelective(record);
	}

	@Override
	public TjKydfxRealtimeCur selectByPrimaryKey(TjKydfxRealtimeCurKey key) {
		return tjKydfxRealtimeCurDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjKydfxRealtimeCur record) {
		return tjKydfxRealtimeCurDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjKydfxRealtimeCur record) {
		return tjKydfxRealtimeCurDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjKydfxRealtimeCur> selectByLevel(Integer id, Integer level) {
		return tjKydfxRealtimeCurDao.selectByLevel(id, level);
	}

	@Override
	public List<KydfxTop5Data> selectTop5(Integer id, Integer scope) {
		return tjKydfxRealtimeCurDao.selectTop5(id, scope);
	}

}
