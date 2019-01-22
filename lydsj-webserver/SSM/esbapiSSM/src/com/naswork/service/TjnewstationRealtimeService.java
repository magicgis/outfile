package com.naswork.service;

import java.util.List;

import com.naswork.model.TjnewstationRealtime;

public interface TjnewstationRealtimeService {
    /**
     * 各交通枢纽实时客流监测 
     * @return
     */
    public List<TjnewstationRealtime> getStationCountRealtime();
}
