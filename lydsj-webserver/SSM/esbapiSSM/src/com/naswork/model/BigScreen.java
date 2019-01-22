package com.naswork.model;

import java.math.BigDecimal;
import java.util.List;

public class BigScreen {
	private String id;
	
	private Integer level;
	
	private String areaName;
	
	private List<BigDecimal> realPos;
	
	private Integer zoom;
	
	private List<InfoData> infoData;
	
	private List<SubArea> subArea;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public List<BigDecimal> getRealPos() {
		return realPos;
	}

	public void setRealPos(List<BigDecimal> realPos) {
		this.realPos = realPos;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	public List<InfoData> getInfoData() {
		return infoData;
	}

	public void setInfoData(List<InfoData> infoData) {
		this.infoData = infoData;
	}

	public List<SubArea> getSubArea() {
		return subArea;
	}

	public void setSubArea(List<SubArea> subArea) {
		this.subArea = subArea;
	}

}
