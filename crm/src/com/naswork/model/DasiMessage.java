package com.naswork.model;

import java.util.Date;

public class DasiMessage {
    private Integer id;

    private Integer dasiId;

    private String partNumber;

    private String storageAmount;

    private Date updateTimestamp;

    private Integer clientInquiryElementId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDasiId() {
        return dasiId;
    }

    public void setDasiId(Integer dasiId) {
        this.dasiId = dasiId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getStorageAmount() {
        return storageAmount;
    }

    public void setStorageAmount(String storageAmount) {
        this.storageAmount = storageAmount;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Integer getClientInquiryElementId() {
        return clientInquiryElementId;
    }

    public void setClientInquiryElementId(Integer clientInquiryElementId) {
        this.clientInquiryElementId = clientInquiryElementId;
    }
}