package com.naswork.model;

import java.util.Date;

public class SupplierInquiryElement {
    private Integer id;

    private Integer supplierInquiryId;

    private Integer clientInquiryElementId;

    private Date updateTimestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierInquiryId() {
        return supplierInquiryId;
    }

    public void setSupplierInquiryId(Integer supplierInquiryId) {
        this.supplierInquiryId = supplierInquiryId;
    }

    public Integer getClientInquiryElementId() {
        return clientInquiryElementId;
    }

    public void setClientInquiryElementId(Integer clientInquiryElementId) {
        this.clientInquiryElementId = clientInquiryElementId;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}