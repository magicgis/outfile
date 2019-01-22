package com.naswork.service;

import java.util.Date;
import java.util.List;

import com.naswork.model.KydfxTop5Data;
import com.naswork.model.MyModel;
import com.naswork.model.TjKydfxDaily;
import com.naswork.model.TjKydfxDailyKey;

public interface TjKydfxDailyService {
	int deleteByPrimaryKey(TjKydfxDailyKey key);

    int insert(TjKydfxDaily record);

    int insertSelective(TjKydfxDaily record);

    TjKydfxDaily selectByPrimaryKey(TjKydfxDailyKey key);

    int updateByPrimaryKeySelective(TjKydfxDaily record);

    int updateByPrimaryKey(TjKydfxDaily record);

	Date getMaxDate();

	List<MyModel> selectTop5ById1(String maxDateStr, Integer areaRange);

	List<MyModel> selectTop5ById2(Integer id, String maxDateStr, Integer areaRange);

	List<TjKydfxDaily> selectByScope(Integer id, Integer scope, String idStr);

	List<KydfxTop5Data> selectTop5(Integer id, Integer scope, String idStr);
}
