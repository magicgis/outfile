package com.naswork.module.system.controller.suppliermanage;

import com.alibaba.druid.stat.TableStat.Name;

public class FactoryVo {
	
	private Integer key;
	
	private String consultPartNum;
	
	private String consultPartName;

	private String name;
	
	private String code;
	
	private String msn;
	
	private String partNum;
	
	private String partName;
	
	private String bsn;
	
	private Integer id;
	
	private Integer supplierId;
	
	private Integer ifMatch;
	
	private String manName;
	
	private String cageCode;
	
	public String getManName() {
		return manName;
	}

	public void setManName(String manName) {
		this.manName = manName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getPartNum() {
		return partNum;
	}

	public void setPartNum(String partNum) {
		this.partNum = partNum;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}

	public Integer getIfMatch() {
		return ifMatch;
	}

	public void setIfMatch(Integer ifMatch) {
		this.ifMatch = ifMatch;
	}

	public String getConsultPartNum() {
		return consultPartNum;
	}

	public void setConsultPartNum(String consultPartNum) {
		this.consultPartNum = consultPartNum;
	}

	public String getConsultPartName() {
		return consultPartName;
	}

	public void setConsultPartName(String consultPartName) {
		this.consultPartName = consultPartName;
	}

	public String getCageCode() {
		return cageCode;
	}

	public void setCageCode(String cageCode) {
		this.cageCode = cageCode;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}
	
}
