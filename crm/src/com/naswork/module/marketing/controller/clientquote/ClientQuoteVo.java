package com.naswork.module.marketing.controller.clientquote;

import java.util.Date;

public class ClientQuoteVo {
	private Double fixedCost;
	
	private Integer clientCurrencyId;
	
	private Double freight;
	    
	private Double lowestFreight;
	
	private Integer clientTemplateType;
	
	private String termsOfDeliveryValue;
	
	private Integer termsOfDelivery;
	
	private String clientShipWay;
	
	private Double prepayRate;
	
	private Double shipPayRate;
	
	private Integer shipPayPeriod;
	
	private Double receivePayRate;
	
	private Integer receivePayPeriod;

	private Integer id;
	
	private Date quoteDate;
	
	private Double exchangeRate;
	
	private Double profitMargin;

	private Date updateTimestamp;
	
	private String quoteNumber;
	
	private Integer client_inquiry_id;
	
	private String sourceNumber;
	
	private String client_inquiry_quote_number;
	
	private Date inquiryDate;
	
	private Date deadline;
	
	private Integer bizTypeId;

    private Integer airTypeId;
    
    private Integer client_id;
    
    private String  client_code;
    
    private String  client_name;
    
    private Integer client_contact_id;
    
    private String client_contact_name;
    
    private String client_contact_phone;
    
    private String client_contact_fax;

    private Integer currency_id;

    private String currency_code;

    private String currency_value;
    
    private String biz_type_code;

    private String biz_type_value;
    
    private String air_type_code;

    private String air_type_value;
    
    private Double total;
    
	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getLowestFreight() {
		return lowestFreight;
	}

	public void setLowestFreight(Double lowestFreight) {
		this.lowestFreight = lowestFreight;
	}
    
	public Double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public Integer getId() {
		return id;
	}

	public Integer getClientCurrencyId() {
		return clientCurrencyId;
	}

	public void setClientCurrencyId(Integer clientCurrencyId) {
		this.clientCurrencyId = clientCurrencyId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(Date quoteDate) {
		this.quoteDate = quoteDate;
	}

	public Integer getClientTemplateType() {
		return clientTemplateType;
	}

	public void setClientTemplateType(Integer clientTemplateType) {
		this.clientTemplateType = clientTemplateType;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public String getClientShipWay() {
		return clientShipWay;
	}

	public void setClientShipWay(String clientShipWay) {
		this.clientShipWay = clientShipWay;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}



	public String getTermsOfDeliveryValue() {
		return termsOfDeliveryValue;
	}

	public void setTermsOfDeliveryValue(String termsOfDeliveryValue) {
		this.termsOfDeliveryValue = termsOfDeliveryValue;
	}

	public Integer getTermsOfDelivery() {
		return termsOfDelivery;
	}

	public void setTermsOfDelivery(Integer termsOfDelivery) {
		this.termsOfDelivery = termsOfDelivery;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Integer getClient_inquiry_id() {
		return client_inquiry_id;
	}

	public void setClient_inquiry_id(Integer client_inquiry_id) {
		this.client_inquiry_id = client_inquiry_id;
	}

	public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public String getClient_inquiry_quote_number() {
		return client_inquiry_quote_number;
	}

	public void setClient_inquiry_quote_number(String client_inquiry_quote_number) {
		this.client_inquiry_quote_number = client_inquiry_quote_number;
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

	public Integer getBizTypeId() {
		return bizTypeId;
	}

	public void setBizTypeId(Integer bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	public Integer getAirTypeId() {
		return airTypeId;
	}

	public void setAirTypeId(Integer airTypeId) {
		this.airTypeId = airTypeId;
	}

	public Integer getClient_id() {
		return client_id;
	}

	public Double getPrepayRate() {
		return prepayRate;
	}

	public void setPrepayRate(Double prepayRate) {
		this.prepayRate = prepayRate;
	}


	public Double getShipPayRate() {
		return shipPayRate;
	}

	public void setShipPayRate(Double shipPayRate) {
		this.shipPayRate = shipPayRate;
	}


	public Double getReceivePayRate() {
		return receivePayRate;
	}

	public void setReceivePayRate(Double receivePayRate) {
		this.receivePayRate = receivePayRate;
	}


	public Integer getShipPayPeriod() {
		return shipPayPeriod;
	}

	public void setShipPayPeriod(Integer shipPayPeriod) {
		this.shipPayPeriod = shipPayPeriod;
	}

	public Integer getReceivePayPeriod() {
		return receivePayPeriod;
	}

	public void setReceivePayPeriod(Integer receivePayPeriod) {
		this.receivePayPeriod = receivePayPeriod;
	}

	public Integer getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(Integer currency_id) {
		this.currency_id = currency_id;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getCurrency_value() {
		return currency_value;
	}

	public void setCurrency_value(String currency_value) {
		this.currency_value = currency_value;
	}

	public String getBiz_type_code() {
		return biz_type_code;
	}

	public void setBiz_type_code(String biz_type_code) {
		this.biz_type_code = biz_type_code;
	}

	public String getBiz_type_value() {
		return biz_type_value;
	}

	public void setBiz_type_value(String biz_type_value) {
		this.biz_type_value = biz_type_value;
	}

	public String getAir_type_code() {
		return air_type_code;
	}

	public void setAir_type_code(String air_type_code) {
		this.air_type_code = air_type_code;
	}

	public String getAir_type_value() {
		return air_type_value;
	}

	public void setAir_type_value(String air_type_value) {
		this.air_type_value = air_type_value;
	}

	public void setClient_id(Integer client_id) {
		this.client_id = client_id;
	}


	public Integer getClient_contact_id() {
		return client_contact_id;
	}

	public void setClient_contact_id(Integer client_contact_id) {
		this.client_contact_id = client_contact_id;
	}


	public String getClient_code() {
		return client_code;
	}

	public void setClient_code(String client_code) {
		this.client_code = client_code;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	public String getClient_contact_name() {
		return client_contact_name;
	}

	public void setClient_contact_name(String client_contact_name) {
		this.client_contact_name = client_contact_name;
	}

	public String getClient_contact_phone() {
		return client_contact_phone;
	}

	public void setClient_contact_phone(String client_contact_phone) {
		this.client_contact_phone = client_contact_phone;
	}

	public String getClient_contact_fax() {
		return client_contact_fax;
	}

	public void setClient_contact_fax(String client_contact_fax) {
		this.client_contact_fax = client_contact_fax;
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
}
