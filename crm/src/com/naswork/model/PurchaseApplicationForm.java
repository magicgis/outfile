package com.naswork.model;

import java.util.Date;

public class PurchaseApplicationForm {
    private Integer id;

    private Integer userId;

    private Date applicationDate;

    private String applicationNumber;

    private Integer clientOrderId;

    private Date updateTimestamp;

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

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Integer getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(Integer clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}