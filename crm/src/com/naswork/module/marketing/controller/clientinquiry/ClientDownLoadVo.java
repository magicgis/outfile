package com.naswork.module.marketing.controller.clientinquiry;

import org.springframework.context.annotation.Primary;

public class ClientDownLoadVo {
	private Double counterFee;

	private String code;
	
	private Double price;
	
	private String quoteNumber;
	
	private Integer id;
	
	private String remark;
	
	private Integer clientInquiryId;
	
	private Integer currencyId;

	public Double getCounterFee() {
		return counterFee;
	}

	public void setCounterFee(Double counterFee) {
		this.counterFee = counterFee;
	}

	public Integer getClientInquiryId() {
		return clientInquiryId;
	}

	public void setClientInquiryId(Integer clientInquiryId) {
		this.clientInquiryId = clientInquiryId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the currencyId
	 */
	public Integer getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId the currencyId to set
	 */
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	
}
