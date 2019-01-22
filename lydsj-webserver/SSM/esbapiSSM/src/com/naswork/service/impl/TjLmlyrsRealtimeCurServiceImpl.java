package com.naswork.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.TjLmlyrsRealtimeCurDao;
import com.naswork.model.KydfxTop5Data;
import com.naswork.model.KydfxTop5DataQB;
import com.naswork.model.TjLmlyrsRealtimeCur;
import com.naswork.model.TjLmlyrsRealtimeCurKey;
import com.naswork.service.TjLmlyrsRealtimeCurService;

@Service("tjLmlyrsRealtimeCurService")
public class TjLmlyrsRealtimeCurServiceImpl implements TjLmlyrsRealtimeCurService {
	@Resource
	private TjLmlyrsRealtimeCurDao tjLmlyrsRealtimeCurDao; 

	@Override
	public int deleteByPrimaryKey(TjLmlyrsRealtimeCurKey key) {
		return tjLmlyrsRealtimeCurDao.deleteByPrimaryKey(key);
	}

	@Override
	public int insert(TjLmlyrsRealtimeCur record) {
		return tjLmlyrsRealtimeCurDao.insert(record);
	}

	@Override
	public int insertSelective(TjLmlyrsRealtimeCur record) {
		return tjLmlyrsRealtimeCurDao.insertSelective(record);
	}

	@Override
	public TjLmlyrsRealtimeCur selectByPrimaryKey(TjLmlyrsRealtimeCurKey key) {
		return tjLmlyrsRealtimeCurDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(TjLmlyrsRealtimeCur record) {
		return tjLmlyrsRealtimeCurDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TjLmlyrsRealtimeCur record) {
		return tjLmlyrsRealtimeCurDao.updateByPrimaryKey(record);
	}

	@Override
	public List<TjLmlyrsRealtimeCur> selectByLevel(Integer level) {
		return tjLmlyrsRealtimeCurDao.selectByLevel(level);
	}

	@Override
	public List<KydfxTop5Data> selectTop5Xq(Integer id) {
		return tjLmlyrsRealtimeCurDao.selectTop5Xq(id);
	}

	@Override
	public List<KydfxTop5Data> selectTop5Jq() {
		return tjLmlyrsRealtimeCurDao.selectTop5Jq();
	}
	
	
	@Override
	public List<KydfxTop5DataQB> selectTop5XqQB(Integer id) {
		return tjLmlyrsRealtimeCurDao.selectTop5XqQB(id);
	}

	@Override
	public List<KydfxTop5DataQB> selectTop5JqQB() {
		return tjLmlyrsRealtimeCurDao.selectTop5JqQB();
	}
	
	public List<Map<String, Object>> selectYjData(Integer id){
		if(id == 1000){
			return tjLmlyrsRealtimeCurDao.selectYjData();
		}else if(id > 1000){
			return tjLmlyrsRealtimeCurDao.selectYjData_districtId(id);
		}else{
			return tjLmlyrsRealtimeCurDao.selectYjData_id(id);
		}
		
	}
	
	public List<Map<String, Object>> selectYjDataGroup(Integer id){
		if(id == 1000){
			return tjLmlyrsRealtimeCurDao.selectYjDataGroup();
		}else if(id > 1000){
			return tjLmlyrsRealtimeCurDao.selectYjDataGroup_districtId(id);
		}else{
			return tjLmlyrsRealtimeCurDao.selectYjDataGroup_id(id);
		}
	}

}
