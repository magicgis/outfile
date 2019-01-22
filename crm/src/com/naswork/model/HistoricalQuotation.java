package com.naswork.model;

import java.util.Date;

public class HistoricalQuotation {
    private String bsn;

    private Integer clientOrderElementId;

    private Integer previousClientOrderElementId;

    private Integer supplierOrderElementId;

    private Integer previousSupplierOrderElementId;

    private Date updateTimestamp;

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public Integer getClientOrderElementId() {
        return clientOrderElementId;
    }

    public void setClientOrderElementId(Integer clientOrderElementId) {
        this.clientOrderElementId = clientOrderElementId;
    }

    public Integer getPreviousClientOrderElementId() {
        return previousClientOrderElementId;
    }

    public void setPreviousClientOrderElementId(Integer previousClientOrderElementId) {
        this.previousClientOrderElementId = previousClientOrderElementId;
    }

    public Integer getSupplierOrderElementId() {
        return supplierOrderElementId;
    }

    public void setSupplierOrderElementId(Integer supplierOrderElementId) {
        this.supplierOrderElementId = supplierOrderElementId;
    }

    public Integer getPreviousSupplierOrderElementId() {
        return previousSupplierOrderElementId;
    }

    public void setPreviousSupplierOrderElementId(Integer previousSupplierOrderElementId) {
        this.previousSupplierOrderElementId = previousSupplierOrderElementId;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}