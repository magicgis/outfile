package com.naswork.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.naswork.model.EdrawtwReport;
import com.naswork.model.EdrawtwReportKey;
@Component("edrawtwReportDao")
public interface EdrawtwReportDao {
    int deleteByPrimaryKey(EdrawtwReportKey key);

    int insert(EdrawtwReport record);

    int insertSelective(EdrawtwReport record);

    EdrawtwReport selectByPrimaryKey(EdrawtwReportKey key);

    int updateByPrimaryKeySelective(EdrawtwReport record);

    int updateByPrimaryKey(EdrawtwReport record);
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