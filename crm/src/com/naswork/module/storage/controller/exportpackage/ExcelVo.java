package com.naswork.module.storage.controller.exportpackage;

import java.util.Date;

public class ExcelVo {

	private String exportNumber;
	
	private Date exportDate;
	
	private String clientCode;
	
	private String partNumber;
	
	private String description;
	
	private String unit;
	
	private Double amount;
	
	private Double basePrice;
	
	private String orderNumber;
	
	private String sourceOrderNumber;
	
	private String sourceNumber;
	
	private String location;
	
	private String REMARK;

	public String getExportNumber() {
		return exportNumber;
	}

	public void setExportNumber(String exportNumber) {
		this.exportNumber = exportNumber;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSourceOrderNumber() {
		return sourceOrderNumber;
	}

	public void setSourceOrderNumber(String sourceOrderNumber) {
		this.sourceOrderNumber = sourceOrderNumber;
	}

	public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	
}
