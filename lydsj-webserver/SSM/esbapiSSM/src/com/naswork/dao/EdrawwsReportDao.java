package com.naswork.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.naswork.model.EdrawwsReport;
import com.naswork.model.EdrawwsReportKey;

@Component("edrawwsReportDao")
public interface EdrawwsReportDao {
    int deleteByPrimaryKey(EdrawwsReportKey key);

    int insert(EdrawwsReport record);

    int insertSelective(EdrawwsReport record);

    EdrawwsReport selectByPrimaryKey(EdrawwsReportKey key);

    int updateByPrimaryKeySelective(EdrawwsReport record);

    int updateByPrimaryKey(EdrawwsReport record);
    /**
     * 查询最近3天的警情信息并倒叙排列
     * @return
     */
    public List<EdrawwsReport> getPoliceWarning();
}