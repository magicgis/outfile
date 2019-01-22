package com.naswork.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.naswork.model.EdrawwwReport;
import com.naswork.model.EdrawwwReportKey;
@Component("edrawwwReportDao")
public interface EdrawwwReportDao {
    int deleteByPrimaryKey(EdrawwwReportKey key);

    int insert(EdrawwwReport record);

    int insertSelective(EdrawwwReport record);

    EdrawwwReport selectByPrimaryKey(EdrawwwReportKey key);

    int updateByPrimaryKeySelective(EdrawwwReport record);

    int updateByPrimaryKey(EdrawwwReport record);
    /**
     * 查询天气预警信息，与梅州气象官网同步 
     * @return
     */
    public List<EdrawwwReport> getWeatherWarning();
}