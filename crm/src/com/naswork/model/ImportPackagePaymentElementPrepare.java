package com.naswork.model;

import java.util.Date;
import java.util.List;

public class ImportPackagePaymentElementPrepare {
    private Integer id;

    private Integer supplierId;

    private Integer supplierOrderElementId;

    private Double amount;

    private Date updateTimestamp;
    
    private String partNumber;
    
    private String orderNumber;
    
    private String dataItem;
    
    private Double paymentPercentage;
    
    private List<ImportPackagePaymentElement> list;
    
    private Integer importPackageId;
    
    private Double price;
    
    private Double orderAmount;
    
    private Double totalPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierOrderElementId() {
        return supplierOrderElementId;
    }

    public void setSupplierOrderElementId(Integer supplierOrderElementId) {
        this.supplierOrderElementId = supplierOrderElementId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getDataItem() {
		return dataItem;
	}

	public void setDataItem(String dataItem) {
		this.dataItem = dataItem;
	}

	public List<ImportPackagePaymentElement> getList() {
		return list;
	}

	public void setList(List<ImportPackagePaymentElement> list) {
		this.list = list;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Double getPaymentPercentage() {
		return paymentPercentage;
	}

	public void setPaymentPercentage(Double paymentPercentage) {
		this.paymentPercentage = paymentPercentage;
	}

	public Integer getImportPackageId() {
		return importPackageId;
	}

	public void setImportPackageId(Integer importPackageId) {
		this.importPackageId = importPackageId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
}