package com.naswork.model;

import java.util.Date;

public class ClientWeatherOrderElementBackUp {
    public ClientWeatherOrderElementBackUp() {
		super();
	}

	public ClientWeatherOrderElementBackUp(ClientWeatherOrderElement clientWeatherOrderElement) {
		super();
		this.clientWeatherOrderId = clientWeatherOrderElement.getClientWeatherOrderId();
		this.clientQuoteElementId = clientWeatherOrderElement.getClientQuoteElementId();
		this.amount = clientWeatherOrderElement.getAmount();
		this.price = clientWeatherOrderElement.getPrice();
		this.leadTime = clientWeatherOrderElement.getLeadTime();
		this.deadline = clientWeatherOrderElement.getDeadline();
		this.certificationId = clientWeatherOrderElement.getCertificationId();
		this.fixedCost = clientWeatherOrderElement.getFixedCost();
		this.remark = clientWeatherOrderElement.getRemark();
		this.bankCharges = clientWeatherOrderElement.getBankCharges();
		this.description = clientWeatherOrderElement.getDescription();
		this.unit = clientWeatherOrderElement.getUnit();
	}

	public ClientWeatherOrderElementBackUp(Integer userId, Integer item,
			String partNumber, String error,
			Integer clientWeatherOrderId,
			Double amount, Double price, String leadTime, Date deadline,
			Integer certificationId, Double fixedCost, String remark,
			 String description, String unit) {
		super();
		this.userId = userId;
		this.item = item;
		this.partNumber = partNumber;
		this.error = error;
		this.clientWeatherOrderId = clientWeatherOrderId;
		this.amount = amount;
		this.price = price;
		this.leadTime = leadTime;
		this.deadline = deadline;
		this.certificationId = certificationId;
		this.fixedCost = fixedCost;
		this.remark = remark;
		this.description = description;
		this.unit = unit;
	}

	private Integer id;

    private Integer userId;

    private Integer item;

    private String partNumber;

    private String error;

    private Integer line;

    private Integer clientWeatherOrderId;

    private Integer clientQuoteElementId;

    private Double amount;

    private Double price;

    private String leadTime;

    private Date deadline;

    private Date updateTimestamp;

    private Integer certificationId;

    private Double fixedCost;

    private String remark;

    private Double bankCharges;

    private String description;

    private String unit;

    private Integer errorFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getClientWeatherOrderId() {
        return clientWeatherOrderId;
    }

    public void setClientWeatherOrderId(Integer clientWeatherOrderId) {
        this.clientWeatherOrderId = clientWeatherOrderId;
    }

    public Integer getClientQuoteElementId() {
        return clientQuoteElementId;
    }

    public void setClientQuoteElementId(Integer clientQuoteElementId) {
        this.clientQuoteElementId = clientQuoteElementId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Integer getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(Integer certificationId) {
        this.certificationId = certificationId;
    }

    public Double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getBankCharges() {
        return bankCharges;
    }

    public void setBankCharges(Double bankCharges) {
        this.bankCharges = bankCharges;
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

    public Integer getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(Integer errorFlag) {
        this.errorFlag = errorFlag;
    }
}