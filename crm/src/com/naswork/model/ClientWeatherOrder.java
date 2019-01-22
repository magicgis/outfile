package com.naswork.model;

import java.util.Date;

public class ClientWeatherOrder {
    private Integer id;

    private Integer clientQuoteId;

    private Integer currencyId;

    private Double exchangeRate;

    private String sourceNumber;

    private String orderNumber;

    private Integer seq;

    private Date orderDate;

    private String terms;

    private String remark;

    private Integer orderStatusId;

    private Date updateTimestamp;

    private Double prepayRate;

    private Double shipPayRate;

    private Integer shipPayPeriod;

    private Double receivePayRate;

    private Integer receivePayPeriod;

    private String lc;

    private String importersRegistration;

    private Integer createUserId;

    private Integer purchaseApply;

    private String certification;
    
    private Integer urgentLevelId;
    
    private String urgentLevelValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientQuoteId() {
        return clientQuoteId;
    }

    public void setClientQuoteId(Integer clientQuoteId) {
        this.clientQuoteId = clientQuoteId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(String sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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

    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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

    public Integer getShipPayPeriod() {
        return shipPayPeriod;
    }

    public void setShipPayPeriod(Integer shipPayPeriod) {
        this.shipPayPeriod = shipPayPeriod;
    }

    public Double getReceivePayRate() {
        return receivePayRate;
    }

    public void setReceivePayRate(Double receivePayRate) {
        this.receivePayRate = receivePayRate;
    }

    public Integer getReceivePayPeriod() {
        return receivePayPeriod;
    }

    public void setReceivePayPeriod(Integer receivePayPeriod) {
        this.receivePayPeriod = receivePayPeriod;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getImportersRegistration() {
        return importersRegistration;
    }

    public void setImportersRegistration(String importersRegistration) {
        this.importersRegistration = importersRegistration;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getPurchaseApply() {
        return purchaseApply;
    }

    public void setPurchaseApply(Integer purchaseApply) {
        this.purchaseApply = purchaseApply;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

	/**
	 * @return the urgentLevelId
	 */
	public Integer getUrgentLevelId() {
		return urgentLevelId;
	}

	/**
	 * @param urgentLevelId the urgentLevelId to set
	 */
	public void setUrgentLevelId(Integer urgentLevelId) {
		this.urgentLevelId = urgentLevelId;
	}

	/**
	 * @return the urgentLevelValue
	 */
	public String getUrgentLevelValue() {
		return urgentLevelValue;
	}

	/**
	 * @param urgentLevelValue the urgentLevelValue to set
	 */
	public void setUrgentLevelValue(String urgentLevelValue) {
		this.urgentLevelValue = urgentLevelValue;
	}
}