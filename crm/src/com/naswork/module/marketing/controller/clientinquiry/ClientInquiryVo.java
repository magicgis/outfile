package com.naswork.module.marketing.controller.clientinquiry;

import java.util.Date;

import com.mysql.fabric.xmlrpc.base.Data;

public class ClientInquiryVo{
	/*
	 * 客户询价ID
	 */
	private Integer id;
	
	/*
	 * 客户ID
	 */
	private Integer clientId;
	
	/*
	 * 客户代码
	 */
	private String clientCode;
	
	/*
	 * 客户名
	 */
	private String clientName;
	
	/*
	 * 客户联系方式ID
	 */
	private Integer clientContactId;
	
	/*
	 * 客户联系名
	 */
	private String clientContactName;
	
	/*
	 * 客户联系电话
	 */
	private String clientContactPhone;
	
	/*
	 * 客户传真
	 */
	private String clientContactFax;
	
	/*
	 * 货币ID
	 */
	private Integer currencyId;
	
	/*
	 * 货币代码
	 */
	private String currencyCode;
	
	/*
	 * 货币名称
	 */
	private String currencyValue;
	
	/*
	 * 汇率
	 */
	private String rate;
	
	/*
	 * 商业类型ID
	 */
	private Integer bizTypeId;
	
	/*
	 * 商业类型代码
	 */
	private String bizTypeCode;
	
	/*
	 * 商业类型名称
	 */
	private String bizTypeValue;
	
	/*
	 * 飞机类型ID
	 */
	private Integer airTypeId;
	
	/*
	 * 飞机类型代码
	 */
	private String airTypeCode;
	
	/*
	 * 飞机类型名称
	 */
	private String airTypeValue;
	
	/*
	 * 询价状态ID
	 */
	private Integer inquiryStatusId;
	
	/*
	 * 询价状态代码
	 */
	private String inquiryStatusCode;
	
	/*
	 * 询价状态
	 */
	private String inquiryStatusValue;
	
	/*
	 * 询价日期
	 */
	private Date inquiryDate;
	
	/*
	 * 截至日期
	 */
	private Date deadline;
	
	/*
	 * 客户询价单号
	 */
	private String sourceNumber;
	
	/*
	 * 询价单号
	 */
	private String quoteNumber;
	
	/*
	 * 期限
	 */
	private String overdue;
	
	/*
	 * 条款
	 */
	private String terms;
	
	/*
	 * 备注
	 */
	private String remark;
	
	/*
	 * 更新时间
	 */
	private Date updateTimestamp;
	
	private Integer supplierQuoteCount;
	
	private String proportion;
	
	private Date realDeadline;
	
	private Double total;
	
	private Integer crawlEmail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getClientContactId() {
		return clientContactId;
	}

	public void setClientContactId(Integer clientContactId) {
		this.clientContactId = clientContactId;
	}

	public String getClientContactName() {
		return clientContactName;
	}

	public void setClientContactName(String clientContactName) {
		this.clientContactName = clientContactName;
	}

	public String getClientContactPhone() {
		return clientContactPhone;
	}

	public void setClientContactPhone(String clientContactPhone) {
		this.clientContactPhone = clientContactPhone;
	}

	public String getClientContactFax() {
		return clientContactFax;
	}

	public void setClientContactFax(String clientContactFax) {
		this.clientContactFax = clientContactFax;
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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Integer getBizTypeId() {
		return bizTypeId;
	}

	public void setBizTypeId(Integer bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	public String getBizTypeCode() {
		return bizTypeCode;
	}

	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	public String getBizTypeValue() {
		return bizTypeValue;
	}

	public void setBizTypeValue(String bizTypeValue) {
		this.bizTypeValue = bizTypeValue;
	}

	public Integer getAirTypeId() {
		return airTypeId;
	}

	public void setAirTypeId(Integer airTypeId) {
		this.airTypeId = airTypeId;
	}

	public String getAirTypeCode() {
		return airTypeCode;
	}

	public void setAirTypeCode(String airTypeCode) {
		this.airTypeCode = airTypeCode;
	}

	public String getAirTypeValue() {
		return airTypeValue;
	}

	public void setAirTypeValue(String airTypeValue) {
		this.airTypeValue = airTypeValue;
	}

	public Integer getInquiryStatusId() {
		return inquiryStatusId;
	}

	public void setInquiryStatusId(Integer inquiryStatusId) {
		this.inquiryStatusId = inquiryStatusId;
	}

	public String getInquiryStatusCode() {
		return inquiryStatusCode;
	}

	public void setInquiryStatusCode(String inquiryStatusCode) {
		this.inquiryStatusCode = inquiryStatusCode;
	}

	public String getInquiryStatusValue() {
		return inquiryStatusValue;
	}

	public void setInquiryStatusValue(String inquiryStatusValue) {
		this.inquiryStatusValue = inquiryStatusValue;
	}

	public Date getInquiryDate() {
		return inquiryDate;
	}

	public void setInquiryDate(Date inquiryDate) {
		this.inquiryDate = inquiryDate;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public String getOverdue() {
		return overdue;
	}

	public void setOverdue(String overdue) {
		this.overdue = overdue;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
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

	public Integer getSupplierQuoteCount() {
		return supplierQuoteCount;
	}

	public void setSupplierQuoteCount(Integer supplierQuoteCount) {
		this.supplierQuoteCount = supplierQuoteCount;
	}

	public String getProportion() {
		return proportion;
	}

	public void setProportion(String proportion) {
		this.proportion = proportion;
	}

	/**
	 * @return the realDeadline
	 */
	public Date getRealDeadline() {
		return realDeadline;
	}

	/**
	 * @param realDeadline the realDeadline to set
	 */
	public void setRealDeadline(Date realDeadline) {
		this.realDeadline = realDeadline;
	}

	/**
	 * @return the total
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Double total) {
		this.total = total;
	}

	/**
	 * @return the crawlEmail
	 */
	public Integer getCrawlEmail() {
		return crawlEmail;
	}

	/**
	 * @param crawlEmail the crawlEmail to set
	 */
	public void setCrawlEmail(Integer crawlEmail) {
		this.crawlEmail = crawlEmail;
	}

	

}
