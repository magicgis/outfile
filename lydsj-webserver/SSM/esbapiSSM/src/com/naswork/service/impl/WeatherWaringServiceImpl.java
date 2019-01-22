package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.EdrawwwReportDao;
import com.naswork.model.EdrawwwReport;
import com.naswork.service.WeatherWaringService;
@Service("weatherWaringService")
public class WeatherWaringServiceImpl implements WeatherWaringService {
	@Resource
	private EdrawwwReportDao edrawwwReportDao;
	@Override
	public List<EdrawwwReport> getWeatherWarning() {
		return edrawwwReportDao.getWeatherWarning();
	}

}
