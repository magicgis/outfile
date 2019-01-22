package com.naswork.model;

import java.util.Date;

public class SupplierOrderElement {
	private Date orderDate;
	
	private String code;
	
    private Integer id;

    private Integer supplierOrderId;

    private Integer clientOrderElementId;

    private Integer supplierQuoteElementId;

    private Double amount;

    private Double price;

    private String leadTime;

    private Date deadline;

    private Date updateTimestamp;
    
    private String awb;
    
    private Integer importStatus;
    
    private String partNumber;
    
    private String orderNumber;
    
    private Integer shipWayId;
    
    private String destination;
    
    private String shipWayValue;
    
    private Double storageAmount;
    
    private Date paymentDate;
    
    private Double paymentPercentage;
    
    private Integer supplierOrderElementId;
    
    private Integer orderStatusId;
    
    private Date invoiceDate;
    
    private Integer shipLeadTime;
    
    private Double totalPrice;
    
    private Integer taxReimbursementId;
    
    private Double supplierOrderAmount;
    
    private Double clientOrderAmount;
    
    private Integer item;
    
    private Double bankCost;
    
    private Double profitOtherFee;
    
    private Integer elementStatusId;
    
    private String unit;
    
    public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Double getStorageAmount() {
		return storageAmount;
	}

	public void setStorageAmount(Double storageAmount) {
		this.storageAmount = storageAmount;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierOrderId() {
        return supplierOrderId;
    }

    public void setSupplierOrderId(Integer supplierOrderId) {
        this.supplierOrderId = supplierOrderId;
    }

    public Integer getClientOrderElementId() {
        return clientOrderElementId;
    }

    public void setClientOrderElementId(Integer clientOrderElementId) {
        this.clientOrderElementId = clientOrderElementId;
    }

    public Integer getSupplierQuoteElementId() {
        return supplierQuoteElementId;
    }

    public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
        this.supplierQuoteElementId = supplierQuoteElementId;
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

    public String getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(String leadTime) {
        this.leadTime = leadTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public String getAwb() {
		return awb;
	}

	public void setAwb(String awb) {
		this.awb = awb;
	}

	public Integer getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(Integer importStatus) {
		this.importStatus = importStatus;
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

	public Integer getShipWayId() {
		return shipWayId;
	}

	public void setShipWayId(Integer shipWayId) {
		this.shipWayId = shipWayId;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getShipWayValue() {
		return shipWayValue;
	}

	public void setShipWayValue(String shipWayValue) {
		this.shipWayValue = shipWayValue;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getPaymentPercentage() {
		return paymentPercentage;
	}

	public void setPaymentPercentage(Double paymentPercentage) {
		this.paymentPercentage = paymentPercentage;
	}

	public Integer getSupplierOrderElementId() {
		return supplierOrderElementId;
	}

	public void setSupplierOrderElementId(Integer supplierOrderElementId) {
		this.supplierOrderElementId = supplierOrderElementId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Integer getShipLeadTime() {
		return shipLeadTime;
	}

	public void setShipLeadTime(Integer shipLeadTime) {
		this.shipLeadTime = shipLeadTime;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getTaxReimbursementId() {
		return taxReimbursementId;
	}

	public void setTaxReimbursementId(Integer taxReimbursementId) {
		this.taxReimbursementId = taxReimbursementId;
	}

	public Double getSupplierOrderAmount() {
		return supplierOrderAmount;
	}

	public void setSupplierOrderAmount(Double supplierOrderAmount) {
		this.supplierOrderAmount = supplierOrderAmount;
	}

	public Double getClientOrderAmount() {
		return clientOrderAmount;
	}

	public void setClientOrderAmount(Double clientOrderAmount) {
		this.clientOrderAmount = clientOrderAmount;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	/**
	 * @return the bankCost
	 */
	public Double getBankCost() {
		return bankCost;
	}

	/**
	 * @param bankCost the bankCost to set
	 */
	public void setBankCost(Double bankCost) {
		this.bankCost = bankCost;
	}

	/**
	 * @return the profitOtherFee
	 */
	public Double getProfitOtherFee() {
		return profitOtherFee;
	}

	/**
	 * @param profitOtherFee the profitOtherFee to set
	 */
	public void setProfitOtherFee(Double profitOtherFee) {
		this.profitOtherFee = profitOtherFee;
	}

	/**
	 * @return the elementStatusId
	 */
	public Integer getElementStatusId() {
		return elementStatusId;
	}

	/**
	 * @param elementStatusId the elementStatusId to set
	 */
	public void setElementStatusId(Integer elementStatusId) {
		this.elementStatusId = elementStatusId;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

}