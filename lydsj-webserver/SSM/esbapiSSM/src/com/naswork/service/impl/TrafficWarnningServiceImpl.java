package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.EdrawtwReportDao;
import com.naswork.model.EdrawtwReport;
import com.naswork.service.TrafficWarnningService;
@Service("trafficWarnningService")
public class TrafficWarnningServiceImpl implements TrafficWarnningService {
	@Resource
	private EdrawtwReportDao edrawtwReportDao;
	@Override
	public List<EdrawtwReport> getCurtraffic() {
		return edrawtwReportDao.getCurtraffic();
	}
	@Override
	public List<EdrawtwReport> getThreeTraffic() {
		return edrawtwReportDao.getThreeTraffic();
	}

}
