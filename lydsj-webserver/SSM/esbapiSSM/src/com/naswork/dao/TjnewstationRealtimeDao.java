package com.naswork.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.naswork.model.TjnewstationRealtime;
import com.naswork.model.TjnewstationRealtimeKey;
@Component("tjnewstationRealtimeDao")
public interface TjnewstationRealtimeDao {
    int deleteByPrimaryKey(TjnewstationRealtimeKey key);

    int insert(TjnewstationRealtime record);

    int insertSelective(TjnewstationRealtime record);

    TjnewstationRealtime selectByPrimaryKey(TjnewstationRealtimeKey key);

    int updateByPrimaryKeySelective(TjnewstationRealtime record);

    int updateByPrimaryKey(TjnewstationRealtime record);
    /**
     * 各交通枢纽实时客流监测 
     * @return
     */
    public List<TjnewstationRealtime> getStationCountRealtime();
}