package com.naswork.model;

import java.math.BigDecimal;

public class InfoData {
	private String name;
	
	private String data;
	
	private BigDecimal trend;
	
	private Integer alarm;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getAlarm() {
		return alarm;
	}

	public void setAlarm(Integer alarm) {
		this.alarm = alarm;
	}

	public BigDecimal getTrend() {
		return trend;
	}

	public void setTrend(BigDecimal trend) {
		this.trend = trend;
	}

}
