package com.naswork.model;

import java.util.Date;

public class SupplierOrderElementFj {
    private Integer id;

    private String supplierOrderElementIds;

    private Date updateTimestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupplierOrderElementIds() {
        return supplierOrderElementIds;
    }

    public void setSupplierOrderElementIds(String supplierOrderElementIds) {
        this.supplierOrderElementIds = supplierOrderElementIds;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}