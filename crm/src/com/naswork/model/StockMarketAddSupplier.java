package com.naswork.model;

public class StockMarketAddSupplier {
    private Integer id;

    private Integer supplierCommissionSaleElementId;

    private Integer supplierId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierCommissionSaleElementId() {
        return supplierCommissionSaleElementId;
    }

    public void setSupplierCommissionSaleElementId(Integer supplierCommissionSaleElementId) {
        this.supplierCommissionSaleElementId = supplierCommissionSaleElementId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }
}