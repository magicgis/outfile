package com.naswork.model;

import java.util.Date;

public class VstorageJournal {
    private String partNumber;

    private String description;

    private String unit;

    private Double amount;

    private Double price;

    private String location;

    private Double exchangeRate;

    private Double basePrice;

    private Double totalBasePrice;

    private Integer supplierId;

    private String supplierCode;

    private Integer clientId;

    private String clientCode;

    private String importNumber;

    private Date importDate;

    private String exportNumber;

    private Date exportDate;

    private String orderNumber;

    private String storageType;

    private Date storageDate;

    private byte[] partNumberCode;

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber.replace(" ", "");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getTotalBasePrice() {
        return totalBasePrice;
    }

    public void setTotalBasePrice(Double totalBasePrice) {
        this.totalBasePrice = totalBasePrice;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getImportNumber() {
        return importNumber;
    }

    public void setImportNumber(String importNumber) {
        this.importNumber = importNumber;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getExportNumber() {
        return exportNumber;
    }

    public void setExportNumber(String exportNumber) {
        this.exportNumber = exportNumber;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Date getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(Date storageDate) {
        this.storageDate = storageDate;
    }

    public byte[] getPartNumberCode() {
        return partNumberCode;
    }

    public void setPartNumberCode(byte[] partNumberCode) {
        this.partNumberCode = partNumberCode;
    }
}