package com.naswork.module.marketing.controller.clientinquiry;

public class BlackList {
	private Integer isBlacklist;
	
    private String bsn; 
    
    private String partName;
    
    private String partNum;
    
    private String cageCode;
    
    private String manName;

	public String getCageCode() {
		return cageCode;
	}

	public void setCageCode(String cageCode) {
		this.cageCode = cageCode;
	}

	public String getManName() {
		return manName;
	}

	public void setManName(String manName) {
		this.manName = manName;
	}

	public Integer getIsBlacklist() {
		return isBlacklist;
	}

	public void setIsBlacklist(Integer isBlacklist) {
		this.isBlacklist = isBlacklist;
	}

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartNum() {
		return partNum;
	}

	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}
}
