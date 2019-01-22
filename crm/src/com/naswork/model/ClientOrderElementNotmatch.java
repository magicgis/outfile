package com.naswork.model;

import java.util.Date;

public class ClientOrderElementNotmatch {
	private String partName;
	
    private Integer id;

    private Integer clientOrderId;

    private Integer clientQuoteElementId;

    private Double amount;

    private Double price;

    private String leadTime;

    private Date deadline;

    private Integer certificationId;

    private Double fixedCost;

    private String remark;

    private String userId;

    private String partNumber;

    private String description;

    private Integer item;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(Integer clientOrderId) {
        this.clientOrderId = clientOrderId;
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

    public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }
}