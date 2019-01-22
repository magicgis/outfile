package com.naswork.model.rfqstock.nsncenter;

import java.io.Serializable;

public class StockInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7262676211718833360L;

	private String cageId;
	
	private String cageName;
	
	private String nsnId;
	
	private String partNum;
	
	private String nsnName;
	
	private String fsgId;
	
	private String fscId;
	
	private String replacedbyNsnId;

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

	public String getNsnId() {
		return nsnId;
	}

	public void setNsnId(String nsnId) {
		this.nsnId = nsnId;
	}

	public String getPartNum() {
		return partNum;
	}

	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}

	public String getNsnName() {
		return nsnName;
	}

	public void setNsnName(String nsnName) {
		this.nsnName = nsnName;
	}

	public String getFsgId() {
		return fsgId;
	}

	public void setFsgId(String fsgId) {
		this.fsgId = fsgId;
	}

	public String getFscId() {
		return fscId;
	}

	public void setFscId(String fscId) {
		this.fscId = fscId;
	}

	public String getReplacedbyNsnId() {
		return replacedbyNsnId;
	}

	public void setReplacedbyNsnId(String replacedbyNsnId) {
		this.replacedbyNsnId = replacedbyNsnId;
	}
	
	
}
