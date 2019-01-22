package com.naswork.model;

import java.util.Date;

public class SupplierDebt {
    private Integer id;

    private Integer supplierOrderId;

    private Double total;

    private Date updateTimestamp;
    
    private String orderNumber;
    
    private String supplierCode;
    
    private Double paid;
    
    private Double surplus;

    public Double getSurplus() {
		return surplus;
	}

	public void setSurplus(Double surplus) {
		this.surplus = surplus;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Double getPaid() {
		return paid;
	}

	public void setPaid(Double paid) {
		this.paid = paid;
	}
}