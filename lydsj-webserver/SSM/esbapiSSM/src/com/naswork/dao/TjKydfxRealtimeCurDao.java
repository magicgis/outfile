package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjKydfxRealtimeCur;
import com.naswork.model.TjKydfxRealtimeCurKey;

public interface TjKydfxRealtimeCurDao {
    int deleteByPrimaryKey(TjKydfxRealtimeCurKey key);

    int insert(TjKydfxRealtimeCur record);

    int insertSelective(TjKydfxRealtimeCur record);

    TjKydfxRealtimeCur selectByPrimaryKey(TjKydfxRealtimeCurKey key);

    int updateByPrimaryKeySelective(TjKydfxRealtimeCur record);

    int updateByPrimaryKey(TjKydfxRealtimeCur record);

	List<TjKydfxRealtimeCur> selectByLevel(@Param("id") Integer id, @Param("scope") Integer level);

	List<KydfxTop5Data> selectTop5(@Param("id") Integer id, @Param("scope") Integer scope);
}