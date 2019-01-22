package com.naswork.model;

import java.util.Date;

public class KlxQuote {
    private Integer id;

    private Integer clientInquiryId;

    private Integer supplierQuoteId;

    private Integer complete;

    private Date updateDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientInquiryId() {
        return clientInquiryId;
    }

    public void setClientInquiryId(Integer clientInquiryId) {
        this.clientInquiryId = clientInquiryId;
    }

    public Integer getSupplierQuoteId() {
        return supplierQuoteId;
    }

    public void setSupplierQuoteId(Integer supplierQuoteId) {
        this.supplierQuoteId = supplierQuoteId;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}