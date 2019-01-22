package com.naswork.model;

import java.util.Date;

public class StorageToOrderEmail {
    private Integer id;

    private Integer clientId;

    private String nowImportpackNumber;

    private String oldImportpackNumber;

    private String partNumber;

    private String description;

    private String orderNumber;

    private Integer supplierId;

    private Integer userId;

    private Integer emailStatus;

    private Date updateTimestamp;

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

    public String getNowImportpackNumber() {
        return nowImportpackNumber;
    }

    public void setNowImportpackNumber(String nowImportpackNumber) {
        this.nowImportpackNumber = nowImportpackNumber;
    }

    public String getOldImportpackNumber() {
        return oldImportpackNumber;
    }

    public void setOldImportpackNumber(String oldImportpackNumber) {
        this.oldImportpackNumber = oldImportpackNumber;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}