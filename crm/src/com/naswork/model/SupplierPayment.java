package com.naswork.model;

import java.util.Date;

public class SupplierPayment {
    private Integer id;

    private Integer importPackageId;

    private Integer currencyId;

    private Double exchangeRate;

    private Date payDate;

    private Double paySum;

    private String remark;

    private Date updateTimestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImportPackageId() {
        return importPackageId;
    }

    public void setImportPackageId(Integer importPackageId) {
        this.importPackageId = importPackageId;
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

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Double getPaySum() {
        return paySum;
    }

    public void setPaySum(Double paySum) {
        this.paySum = paySum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}