package com.naswork.module.storage.controller.exportpackage;

import java.util.Date;

public class ExportPackageVo {

	private Integer id;
	
	private String exportNumber;
	
	private Date exportDate;
	
	private Integer seq;
	
	private Integer clientId;
	
	private String clientCode;
	
	private String clientName;
	
	private Integer currencyId;
	
	private String currencyCode;
	
	private String currencyValue;
	
	private Double exchangeRate;
	
	private String remark;
	
	private Date updateTimestamp;
	
	private String exportPackageInstructionsNumber;
	
	private Double weight;
	
	private String size;
	
	private String awb;
	
	private String logisticsValue;
	
	private Double exportFee;
	
	private Double freight;
	
	private Date realExportDate;
	
	private Integer feeCurrencyId;
	
	private String feeCurrencyCode;
	
	private String feeCurrencyValue;
	
	private String createUserName;

	public String getExportPackageInstructionsNumber() {
		return exportPackageInstructionsNumber;
	}

	public void setExportPackageInstructionsNumber(String exportPackageInstructionsNumber) {
		this.exportPackageInstructionsNumber = exportPackageInstructionsNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getAwb() {
		return awb;
	}

	public void setAwb(String awb) {
		this.awb = awb;
	}

	public String getLogisticsValue() {
		return logisticsValue;
	}

	public void setLogisticsValue(String logisticsValue) {
		this.logisticsValue = logisticsValue;
	}

	/**
	 * @return the exportFee
	 */
	public Double getExportFee() {
		return exportFee;
	}

	/**
	 * @param exportFee the exportFee to set
	 */
	public void setExportFee(Double exportFee) {
		this.exportFee = exportFee;
	}

	/**
	 * @return the freight
	 */
	public Double getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight to set
	 */
	public void setFreight(Double freight) {
		this.freight = freight;
	}

	/**
	 * @return the realExportDate
	 */
	public Date getRealExportDate() {
		return realExportDate;
	}

	/**
	 * @param realExportDate the realExportDate to set
	 */
	public void setRealExportDate(Date realExportDate) {
		this.realExportDate = realExportDate;
	}

	/**
	 * @return the feeCurrencyId
	 */
	public Integer getFeeCurrencyId() {
		return feeCurrencyId;
	}

	/**
	 * @param feeCurrencyId the feeCurrencyId to set
	 */
	public void setFeeCurrencyId(Integer feeCurrencyId) {
		this.feeCurrencyId = feeCurrencyId;
	}

	/**
	 * @return the feeCurrencyCode
	 */
	public String getFeeCurrencyCode() {
		return feeCurrencyCode;
	}

	/**
	 * @param feeCurrencyCode the feeCurrencyCode to set
	 */
	public void setFeeCurrencyCode(String feeCurrencyCode) {
		this.feeCurrencyCode = feeCurrencyCode;
	}

	/**
	 * @return the feeCurrencyValue
	 */
	public String getFeeCurrencyValue() {
		return feeCurrencyValue;
	}

	/**
	 * @param feeCurrencyValue the feeCurrencyValue to set
	 */
	public void setFeeCurrencyValue(String feeCurrencyValue) {
		this.feeCurrencyValue = feeCurrencyValue;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	
}
