package com.naswork.model;

import java.util.Date;

public class ArrearsUse {
    private Integer id;
    
    private Integer importPackagePaymentId;

    private String importPackagePaymentElementId;

    private String supplierCode;

    private Double total;

    private Double counterFee;

    private Date updateTimestamp;

    public Integer getImportPackagePaymentId() {
		return importPackagePaymentId;
	}

	public void setImportPackagePaymentId(Integer importPackagePaymentId) {
		this.importPackagePaymentId = importPackagePaymentId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getImportPackagePaymentElementId() {
		return importPackagePaymentElementId;
	}

	public void setImportPackagePaymentElementId(String importPackagePaymentElementId) {
		this.importPackagePaymentElementId = importPackagePaymentElementId;
	}

	public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getCounterFee() {
        return counterFee;
    }

    public void setCounterFee(Double counterFee) {
        this.counterFee = counterFee;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}