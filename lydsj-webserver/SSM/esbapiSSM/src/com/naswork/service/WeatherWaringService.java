package com.naswork.service;

import java.util.List;

import com.naswork.model.EdrawwwReport;

public interface WeatherWaringService {
	
	  /**
     * 查询天气预警信息，与梅州气象官网同步 
     * @return
     */
    public List<EdrawwwReport> getWeatherWarning();

}
