package com.naswork.service;

import java.util.List;
import java.util.Map;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.KydfxTop5DataQB;
import com.naswork.model.TjLmlyrsRealtimeCur;
import com.naswork.model.TjLmlyrsRealtimeCurKey;

public interface TjLmlyrsRealtimeCurService {
	int deleteByPrimaryKey(TjLmlyrsRealtimeCurKey key);

    int insert(TjLmlyrsRealtimeCur record);

    int insertSelective(TjLmlyrsRealtimeCur record);

    TjLmlyrsRealtimeCur selectByPrimaryKey(TjLmlyrsRealtimeCurKey key);

    int updateByPrimaryKeySelective(TjLmlyrsRealtimeCur record);

    int updateByPrimaryKey(TjLmlyrsRealtimeCur record);

	List<TjLmlyrsRealtimeCur> selectByLevel(Integer level);

	List<KydfxTop5Data> selectTop5Xq(Integer id);

	List<KydfxTop5Data> selectTop5Jq();
	
	
	List<KydfxTop5DataQB> selectTop5XqQB(Integer id);

	List<KydfxTop5DataQB> selectTop5JqQB();
	
	
	List<Map<String, Object>> selectYjData(Integer id);
	
	List<Map<String, Object>> selectYjDataGroup(Integer id);
}
