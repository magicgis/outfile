package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.naswork.vo.RankVo;
import org.springframework.stereotype.Service;

import com.naswork.dao.TjKydfxMonthlyDao;
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.MyModel;
import com.naswork.model.TjKydfxMonthly;
import com.naswork.model.TjKydfxMonthlyKey;
import com.naswork.service.TjKydfxMonthlyService;

@Service("tjKydfxMonthlyService")
public class TjKydfxMonthlyServiceImpl implements TjKydfxMonthlyService {
	@Resource
	private TjKydfxMonthlyDao tjKydfxMonthlyDao;

	@Override
	public int deleteByPrimaryKey(TjKydfxMonthlyKey key) {
		return tjKydfxMonthlyDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjKydfxMonthly record) {
		return tjKydfxMonthlyDao.insert(record);
	}

	@Override
	public int insertSelective(TjKydfxMonthly record) {
		return tjKydfxMonthlyDao.insertSelective(record);
	}

	@Override
	public TjKydfxMonthly selectByPrimaryKey(TjKydfxMonthlyKey key) {
		return tjKydfxMonthlyDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjKydfxMonthly record) {
		return tjKydfxMonthlyDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjKydfxMonthly record) {
		return tjKydfxMonthlyDao.updateByPrimaryKey(record);
	}

	@Override
	public List<MyModel> selectTop5ById1(String idStr, Integer area_range) {
		return tjKydfxMonthlyDao.selectTop5ById1(idStr, area_range);
	}

	@Override
	public List<MyModel> selectTop5ById2(String idStr, Integer area_range, Integer id) {
		return tjKydfxMonthlyDao.selectTop5ById2(idStr, area_range, id);
	}

	@Override
	public List<TjKydfxMonthly> selectByScope(Integer id, Integer scope, String idStr) {
		return tjKydfxMonthlyDao.selectByScope(id, scope, idStr);
	}

	@Override
	public List<KydfxTop5Data> selectTop5(Integer id, Integer scope, String idStr) {
		return tjKydfxMonthlyDao.selectTop5(id, scope, idStr);
	}

	@Override
	public List<TjKydfxMonthly> selectAllByMonth(Integer id, Integer year, Integer month, Integer area_range) {
		return tjKydfxMonthlyDao.selectAllByMonth(id, year, month, area_range);
	}

	@Override
	public List<KydfxTop5Data> KydpmByMonth(Integer id, Integer year, Integer month, Integer area_range) {
		return tjKydfxMonthlyDao.KydpmByMonth(id, year, month, area_range);
	}

	@Override
	public List<KydfxTop5Data> getQskydpm(Integer year, Integer area_range) {
		return tjKydfxMonthlyDao.getQskydpm(year, area_range);
	}

	@Override
	public List<RankVo> getkydfx(Integer id, Integer year_id, Integer month_id, Integer area_range) {
		return tjKydfxMonthlyDao.getkydfx(id,year_id,month_id,area_range);
	}

}
