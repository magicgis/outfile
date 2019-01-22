package com.naswork.model;

import java.util.Date;

public class StockMarketCrawl {
    private Integer id;

    private Integer complete;

    private Integer excelConplete;

    private Date updateTimestamp;

    private Date crawlDate;

    private Integer supplierCommissionSaleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Integer getExcelConplete() {
        return excelConplete;
    }

    public void setExcelConplete(Integer excelConplete) {
        this.excelConplete = excelConplete;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Date getCrawlDate() {
        return crawlDate;
    }

    public void setCrawlDate(Date crawlDate) {
        this.crawlDate = crawlDate;
    }

    public Integer getSupplierCommissionSaleId() {
        return supplierCommissionSaleId;
    }

    public void setSupplierCommissionSaleId(Integer supplierCommissionSaleId) {
        this.supplierCommissionSaleId = supplierCommissionSaleId;
    }
}