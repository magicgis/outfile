package com.naswork.module.finance.controller.clientreceipt;

import java.util.Date;

public class ClientReceiptVo {

	private Integer id;
	
	private String clientCode;
	
	private String exportNumber;
	
	private Date exportDate;
	
	private String currencyValue;
	
	private Double receiveSum;
	
	private Date receiveDate;
	
	private String remark;
	
	private Date updateTimestamp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

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

	public String getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}

	public Double getReceiveSum() {
		return receiveSum;
	}

	public void setReceiveSum(Double receiveSum) {
		this.receiveSum = receiveSum;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	
	
}
