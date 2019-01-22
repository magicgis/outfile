package com.naswork.model;

import java.util.Date;

public class AviallQuoteElement {
    private Integer id;

    private String partNumber;

    private String description;

    private String unitPrice;

    private String currency;

    private String certification;

    private String leadTime;

    private Integer aviallQuoteId;

    private Integer clientInquiryElementId;

    private String stockMessage;

    private Date updateDatetime;
    
    private Integer ifDanger;
    
    private String moq;
    
    private String unit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber.replace(" ", "");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getAviallQuoteId() {
        return aviallQuoteId;
    }

    public void setAviallQuoteId(Integer aviallQuoteId) {
        this.aviallQuoteId = aviallQuoteId;
    }

    public Integer getClientInquiryElementId() {
        return clientInquiryElementId;
    }

    public void setClientInquiryElementId(Integer clientInquiryElementId) {
        this.clientInquiryElementId = clientInquiryElementId;
    }

    public String getStockMessage() {
        return stockMessage;
    }

    public void setStockMessage(String stockMessage) {
        this.stockMessage = stockMessage;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

	/**
	 * @return the ifDanger
	 */
	public Integer getIfDanger() {
		return ifDanger;
	}

	/**
	 * @param ifDanger the ifDanger to set
	 */
	public void setIfDanger(Integer ifDanger) {
		this.ifDanger = ifDanger;
	}

	/**
	 * @return the moq
	 */
	public String getMoq() {
		return moq;
	}

	/**
	 * @param moq the moq to set
	 */
	public void setMoq(String moq) {
		this.moq = moq;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
}