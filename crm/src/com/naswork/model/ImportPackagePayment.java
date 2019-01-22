package com.naswork.model;

import java.math.BigDecimal;
import java.util.Date;

import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.stdDSA;

public class ImportPackagePayment {
	private Double counterFee;
	
    private Integer id;

    private Integer importPackageId;

    private Date paymentDate;

    private String paymentNumber;

    private String remark;

    private Date updateTimestamp;

    private Double paymentPercentage;
    
    private Integer supplierId;
    
    private String importNumber;
    
    private Double paymentTotal;
    
    private Integer supplierOrderId;
    
    private Integer paymentType;
    
    private String orderNumber;
    
    private String code; 
    
    private String supplierCode;
    
    private String paymentStatusValue;
    
    private Double shouldPay;
    
    private Integer paymentStatusId;
    
    private String leadTime;
    
    private Integer spzt;
    
    public Integer getSpzt() {
		return spzt;
	}

	public void setSpzt(Integer spzt) {
		this.spzt = spzt;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getSupplierOrderId() {
		return supplierOrderId;
	}

	public void setSupplierOrderId(Integer supplierOrderId) {
		this.supplierOrderId = supplierOrderId;
	}

	public Integer getId() {
        return id;
    }

    public Double getCounterFee() {
		return counterFee;
	}

	public void setCounterFee(Double counterFee) {
		this.counterFee = counterFee;
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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
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

    public Double getPaymentPercentage() {
        return paymentPercentage;
    }

    public void setPaymentPercentage(Double paymentPercentage) {
        this.paymentPercentage = paymentPercentage;
    }

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getImportNumber() {
		return importNumber;
	}

	public void setImportNumber(String importNumber) {
		this.importNumber = importNumber;
	}

	public Double getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(Double paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getPaymentStatusValue() {
		return paymentStatusValue;
	}

	public void setPaymentStatusValue(String paymentStatusValue) {
		this.paymentStatusValue = paymentStatusValue;
	}

	public Double getShouldPay() {
		return shouldPay;
	}

	public void setShouldPay(Double shouldPay) {
		this.shouldPay = shouldPay;
	}

	public Integer getPaymentStatusId() {
		return paymentStatusId;
	}

	public void setPaymentStatusId(Integer paymentStatusId) {
		this.paymentStatusId = paymentStatusId;
	}

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

}