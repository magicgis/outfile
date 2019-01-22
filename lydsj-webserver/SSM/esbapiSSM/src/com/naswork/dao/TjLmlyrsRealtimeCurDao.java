package com.naswork.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.KydfxTop5DataQB;
import com.naswork.model.TjLmlyrsRealtimeCur;
import com.naswork.model.TjLmlyrsRealtimeCurKey;

public interface TjLmlyrsRealtimeCurDao {
    int deleteByPrimaryKey(TjLmlyrsRealtimeCurKey key);

    int insert(TjLmlyrsRealtimeCur record);

    int insertSelective(TjLmlyrsRealtimeCur record);

    TjLmlyrsRealtimeCur selectByPrimaryKey(TjLmlyrsRealtimeCurKey key);

    int updateByPrimaryKeySelective(TjLmlyrsRealtimeCur record);

    int updateByPrimaryKey(TjLmlyrsRealtimeCur record);

	List<TjLmlyrsRealtimeCur> selectByLevel(@Param("level") Integer level);

	List<KydfxTop5Data> selectTop5Xq(@Param("id") Integer id);

	List<KydfxTop5Data> selectTop5Jq();

	List<KydfxTop5DataQB> selectTop5XqQB(Integer id);

	List<KydfxTop5DataQB> selectTop5JqQB();
	
	List<Map<String, Object>> selectYjData();
	
	List<Map<String, Object>> selectYjDataGroup();
	
	List<Map<String, Object>> selectYjData_id(@Param("id") Integer id);
	
	List<Map<String, Object>> selectYjDataGroup_id(@Param("id") Integer id);
	
	List<Map<String, Object>> selectYjData_districtId(@Param("districtId") Integer districtId);
	
	List<Map<String, Object>> selectYjDataGroup_districtId(@Param("districtId") Integer districtId);
}