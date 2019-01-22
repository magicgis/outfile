package com.naswork.model;

import java.util.Date;

public class KapcoQuoteElement {
    private Integer id;

    private String partNumber;

    private String description;

    private String unitPrice;

    private String unit;

    private String currency;

    private String cageCode;

    private String certification;

    private String leadTime;

    private Integer kapcoQuoteId;

    private Integer clientInquiryElementId;

    private String stockMessage;

    private String information;

    private Date updateDatetime;

    private Integer amount;

    private Integer isreplace;
    
    private Integer danger;

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
        this.partNumber = partNumber;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCageCode() {
        return cageCode;
    }

    public void setCageCode(String cageCode) {
        this.cageCode = cageCode;
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

    public Integer getKapcoQuoteId() {
        return kapcoQuoteId;
    }

    public void setKapcoQuoteId(Integer kapcoQuoteId) {
        this.kapcoQuoteId = kapcoQuoteId;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getIsreplace() {
        return isreplace;
    }

    public void setIsreplace(Integer isreplace) {
        this.isreplace = isreplace;
    }

	/**
	 * @return the danger
	 */
	public Integer getDanger() {
		return danger;
	}

	/**
	 * @param danger the danger to set
	 */
	public void setDanger(Integer danger) {
		this.danger = danger;
	}
}