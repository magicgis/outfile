package com.naswork.module.crmstock.controller;

public class CaacVo {

	private String partNumber;
	
	private String cageCode;
	
	private String oem;
	
	private Integer qty;
	
	private String partName;
	
	private Integer ataChapterSection;
	
	private String replacePartNumber;

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getCageCode() {
		return cageCode;
	}

	public void setCageCode(String cageCode) {
		this.cageCode = cageCode;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Integer getAtaChapterSection() {
		return ataChapterSection;
	}

	public void setAtaChapterSection(Integer ataChapterSection) {
		this.ataChapterSection = ataChapterSection;
	}

	public String getReplacePartNumber() {
		return replacePartNumber;
	}

	public void setReplacePartNumber(String replacePartNumber) {
		this.replacePartNumber = replacePartNumber;
	}
	
}
