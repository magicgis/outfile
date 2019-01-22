package com.naswork.model;

public class ClientInquiryAlterElement {
    private Integer id;

    private Integer clientInquiryElementId;

    private String alterPartNumber;

    private Integer elementId;

    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientInquiryElementId() {
        return clientInquiryElementId;
    }

    public void setClientInquiryElementId(Integer clientInquiryElementId) {
        this.clientInquiryElementId = clientInquiryElementId;
    }

    public String getAlterPartNumber() {
        return alterPartNumber;
    }

    public void setAlterPartNumber(String alterPartNumber) {
        this.alterPartNumber = alterPartNumber;
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}