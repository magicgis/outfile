package com.naswork.service;

import java.util.List;

import com.naswork.model.EdrawtwReport;

public interface TrafficWarnningService {
    /**
     * 获取最近3天的交通信息
     * @return
     */
    public List<EdrawtwReport> getCurtraffic();
    /**
     * 最近3天的交通信息不够3条继续往前找 
     * @return
     */
    public  List<EdrawtwReport> getThreeTraffic();
}
