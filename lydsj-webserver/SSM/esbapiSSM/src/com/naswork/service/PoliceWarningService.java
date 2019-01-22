package com.naswork.service;

import java.util.List;

import com.naswork.model.EdrawwsReport;

public interface PoliceWarningService {
	
    /**
     * 查询最近3天的警情信息并倒叙排列
     * @return
     */
    public List<EdrawwsReport> getPoliceWarning();
}
