package com.naswork.model.rfqstock.nsncenter;

import java.io.Serializable;

public class CageInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7514629106586481684L;
	
	private String cageId;
	
	private String cageName;
	
	private String additionalInfo;

	public String getCageId() {
		return cageId;
	}

	public void setCageId(String cageId) {
		this.cageId = cageId;
	}

	public String getCageName() {
		return cageName;
	}

	public void setCageName(String cageName) {
		this.cageName = cageName;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	
}
