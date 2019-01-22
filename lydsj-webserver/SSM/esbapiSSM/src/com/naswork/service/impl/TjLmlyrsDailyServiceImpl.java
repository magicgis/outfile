package com.naswork.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjLmlyrsDailyDao;
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjLmlyrsDaily;
import com.naswork.model.TjLmlyrsDailyKey;
import com.naswork.service.TjLmlyrsDailyService;

@Service("tjLmlyrsDailyService")
public class TjLmlyrsDailyServiceImpl implements TjLmlyrsDailyService {
	@Resource
	private TjLmlyrsDailyDao tjLmlyrsDailyDao;

	@Override
	public int deleteByPrimaryKey(TjLmlyrsDailyKey key) {
		return tjLmlyrsDailyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjLmlyrsDaily record) {
		return tjLmlyrsDailyDao.insert(record);
	}

	@Override
	public int insertSelective(TjLmlyrsDaily record) {
		return tjLmlyrsDailyDao.insertSelective(record);
	}

	@Override
	public TjLmlyrsDaily selectByPrimaryKey(TjLmlyrsDailyKey key) {
		return tjLmlyrsDailyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjLmlyrsDaily record) {
		return tjLmlyrsDailyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjLmlyrsDaily record) {
		return tjLmlyrsDailyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjLmlyrsDaily> selectByIdFromStartDay(Integer id, String startDay) {
		return tjLmlyrsDailyDao.selectByIdFromStartDay(id, startDay); 
	}

	@Override
	public Integer LastDayNum(Integer id, String identifier) {
		return tjLmlyrsDailyDao.LastDayNum(id, identifier);
	}

	@Override
	public List<TjLmlyrsDaily> selectByLevel(Integer level, String idStr) {
		return tjLmlyrsDailyDao.selectByLevel(level, idStr);
	}

	@Override
	public BigDecimal lastDayTrend(Integer id, String identifier) {
		return tjLmlyrsDailyDao.lastDayTrend(id, identifier);
	}

	@Override
	public List<KydfxTop5Data> selectTop5Xq(String idStr, Integer id) {
		return tjLmlyrsDailyDao.selectTop5Xq(idStr, id);
	}

	@Override
	public List<KydfxTop5Data> selectTop5Jq(String idStr) {
		return tjLmlyrsDailyDao.selectTop5Jq(idStr);
	}

}
