package com.naswork.service;

import java.util.List;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.TjKydfxRealtimeCur;
import com.naswork.model.TjKydfxRealtimeCurKey;

public interface TjKydfxRealtimeCurService {
	int deleteByPrimaryKey(TjKydfxRealtimeCurKey key);

	int insert(TjKydfxRealtimeCur record);

	int insertSelective(TjKydfxRealtimeCur record);

	TjKydfxRealtimeCur selectByPrimaryKey(TjKydfxRealtimeCurKey key);

	int updateByPrimaryKeySelective(TjKydfxRealtimeCur record);

	int updateByPrimaryKey(TjKydfxRealtimeCur record);

	List<TjKydfxRealtimeCur> selectByLevel(Integer id, Integer level);

	List<KydfxTop5Data> selectTop5(Integer id, Integer scope);
}
