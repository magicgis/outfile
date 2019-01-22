package com.naswork.model.JsonFormat;

import java.util.List;

/*a.json格式*/
public class AJson {
	private XAxis xAxis;
	
	private List<Series> series;

	public XAxis getxAxis() {
		return xAxis;
	}

	public void setxAxis(XAxis xAxis) {
		this.xAxis = xAxis;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}
}
