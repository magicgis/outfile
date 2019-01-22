package com.naswork.model;

import java.util.Date;

public class Income {
	private Double uncollected;
	
	private Double invoiceSum;
	
    private Integer id;

    private Integer clientOrderId;

    private Date receiveDate;

    private Double totalSum;

    private String remark;

    private Date updateTimestamp;

    private String refNumber;

    private Integer clientInvoiceId;
    
    private Double total;
    
    private Double totalCount;
    
    private String invoiceNumber;
    
    private String orderNumber;

    public Integer getId() {
        return id;
    }

    public Double getInvoiceSum() {
		return invoiceSum;
	}

	public void setInvoiceSum(Double invoiceSum) {
		this.invoiceSum = invoiceSum;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(Integer clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    public String getRemark() {
        return remark;
    }

    public Double getUncollected() {
		return uncollected;
	}

	public void setUncollected(Double uncollected) {
		this.uncollected = uncollected;
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

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public Integer getClientInvoiceId() {
        return clientInvoiceId;
    }

    public void setClientInvoiceId(Integer clientInvoiceId) {
        this.clientInvoiceId = clientInvoiceId;
    }

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
}