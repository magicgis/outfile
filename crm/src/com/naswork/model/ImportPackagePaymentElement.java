package com.naswork.model;

import java.util.Date;

public class ImportPackagePaymentElement {
	private String userName;
	
	private String importDate;
	
	private String supplierCode;
	
	private Double arrearsTotal;
	
	private String jbyj;
	
	private Integer importPackagePaymentElementId;
	
	private Integer spzt;
	
    private Integer id;

    private Integer importPackagePaymentId;

    private Integer importPackageElementId;

    private Double paymentSum;

    private String remark;

    private Date updateTimestamp;

    private Integer supplierOrderElementId;
    
    private String orderNumber;
    
    private String partNumber;
    
    private String dataItem;
    
    private Double amount;
    
    private Double shouldPay;
    
    private Double paymentPercentage;
    
    private String description;
    
    private String serialNumber;
    
    private String unit;
    
    private String quoteNumber;
    
    private Double price;
    
    private String paymentNumber;
    
    private Double total;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}

	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getId() {
        return id;
    }

    public Integer getImportPackagePaymentElementId() {
		return importPackagePaymentElementId;
	}

	public void setImportPackagePaymentElementId(Integer importPackagePaymentElementId) {
		this.importPackagePaymentElementId = importPackagePaymentElementId;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImportPackagePaymentId() {
        return importPackagePaymentId;
    }

    public void setImportPackagePaymentId(Integer importPackagePaymentId) {
        this.importPackagePaymentId = importPackagePaymentId;
    }

    public Integer getImportPackageElementId() {
        return importPackageElementId;
    }

    public void setImportPackageElementId(Integer importPackageElementId) {
        this.importPackageElementId = importPackageElementId;
    }

    public Double getPaymentSum() {
        return paymentSum;
    }

    public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public void setPaymentSum(Double paymentSum) {
        this.paymentSum = paymentSum;
    }

    public String getRemark() {
        return remark;
    }

    public Double getArrearsTotal() {
		return arrearsTotal;
	}

	public void setArrearsTotal(Double arrearsTotal) {
		this.arrearsTotal = arrearsTotal;
	}


	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getJbyj() {
		return jbyj;
	}

	public void setJbyj(String jbyj) {
		this.jbyj = jbyj;
	}

	public Integer getSpzt() {
		return spzt;
	}

	public void setSpzt(Integer spzt) {
		this.spzt = spzt;
	}

	public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Integer getSupplierOrderElementId() {
        return supplierOrderElementId;
    }

    public void setSupplierOrderElementId(Integer supplierOrderElementId) {
        this.supplierOrderElementId = supplierOrderElementId;
    }

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	public String getDataItem() {
		return dataItem;
	}

	public void setDataItem(String dataItem) {
		this.dataItem = dataItem;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getShouldPay() {
		return shouldPay;
	}

	public void setShouldPay(Double shouldPay) {
		this.shouldPay = shouldPay;
	}

	public Double getPaymentPercentage() {
		return paymentPercentage;
	}

	public void setPaymentPercentage(Double paymentPercentage) {
		this.paymentPercentage = paymentPercentage;
	}

	/**
	 * @return the total
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Double total) {
		this.total = total;
	}

}