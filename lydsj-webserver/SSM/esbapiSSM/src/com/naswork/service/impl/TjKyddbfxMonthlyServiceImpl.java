package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjKyddbfxMonthlyDao;
import com.naswork.model.TjKyddbfxMonthly;
import com.naswork.model.TjKyddbfxMonthlyKey;
import com.naswork.service.TjKyddbfxMonthlyService;

@Service("tjKyddbfxMonthlyService")
public class TjKyddbfxMonthlyServiceImpl implements TjKyddbfxMonthlyService {
	@Resource
	private TjKyddbfxMonthlyDao tjKyddbfxMonthlyDao;

	@Override
	public int deleteByPrimaryKey(TjKyddbfxMonthlyKey key) {
		return tjKyddbfxMonthlyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjKyddbfxMonthly record) {
		return tjKyddbfxMonthlyDao.insert(record);
	}

	@Override
	public int insertSelective(TjKyddbfxMonthly record) {
		return tjKyddbfxMonthlyDao.insertSelective(record);
	}

	@Override
	public TjKyddbfxMonthly selectByPrimaryKey(TjKyddbfxMonthlyKey key) {
		return tjKyddbfxMonthlyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjKyddbfxMonthly record) {
		return tjKyddbfxMonthlyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjKyddbfxMonthly record) {
		return tjKyddbfxMonthlyDao.updateByPrimaryKey(record);
	}

	@Override
	public TjKyddbfxMonthly getList4FromMonth(Integer id, String idStr) {
		return tjKyddbfxMonthlyDao.getList4FromMonth(id, idStr);
	}

	@Override
	public TjKyddbfxMonthly getList3FromMonth(Integer id, String idStr) {
		return tjKyddbfxMonthlyDao.getList3FromMonth(id, idStr);
	}

	@Override
	public TjKyddbfxMonthly getList2FromMonth(Integer id, String idStr) {
		return tjKyddbfxMonthlyDao.getList2FromMonth(id, idStr);
	}

	@Override
	public List<Integer> getMonths(Integer year) {
		return tjKyddbfxMonthlyDao.getMonths(year);
	}

	@Override
	public Double getGuojiPercent(Integer year) {
		return tjKyddbfxMonthlyDao.getGuojiPercent(year);
	}

	@Override
	public Double getGuogneiPercent(Integer year) {
		return tjKyddbfxMonthlyDao.getGuogneiPercent(year);
	}

	@Override
	public Double getShengneiPercent(Integer year) {
		return tjKyddbfxMonthlyDao.getShengneiPercent(year);
	}

}
