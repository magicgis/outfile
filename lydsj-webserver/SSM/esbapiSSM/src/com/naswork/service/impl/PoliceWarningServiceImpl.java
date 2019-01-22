package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.EdrawwsReportDao;
import com.naswork.model.EdrawwsReport;
import com.naswork.service.PoliceWarningService;

@Service("policeWarningService")
public class PoliceWarningServiceImpl implements PoliceWarningService {
	@Resource
	private EdrawwsReportDao edrawwsReportDao;
	@Override
	public List<EdrawwsReport> getPoliceWarning() {
		return edrawwsReportDao.getPoliceWarning();
	}

}
