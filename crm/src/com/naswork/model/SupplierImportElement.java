package com.naswork.model;

import java.util.Date;

public class SupplierImportElement extends SupplierImportElementKey {
    private Double amount;

    private Date updateTimestamp;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}