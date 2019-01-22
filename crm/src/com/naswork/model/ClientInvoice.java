package com.naswork.model;

import java.util.Date;

public class ClientInvoice {
	private String exportPackageId;
	
	private Integer invoiceType;
	
    private Integer id;

    private String invoiceNumber;

    private Integer clientOrderId;

    private Date invoiceDate;

    private String remark;

    private Date updateTimestamp;

    private Integer terms;
    
    private Integer invoiceStatusId;
    
    private Integer clientShipId;
    
    public Integer getInvoiceStatusId() {
		return invoiceStatusId;
	}

	public void setInvoiceStatusId(Integer invoiceStatusId) {
		this.invoiceStatusId = invoiceStatusId;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(Integer clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public String getExportPackageId() {
		return exportPackageId;
	}

	public void setExportPackageId(String exportPackageId) {
		this.exportPackageId = exportPackageId;
	}

	public Integer getTerms() {
        return terms;
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
    }

	/**
	 * @return the clientShipId
	 */
	public Integer getClientShipId() {
		return clientShipId;
	}

	/**
	 * @param clientShipId the clientShipId to set
	 */
	public void setClientShipId(Integer clientShipId) {
		this.clientShipId = clientShipId;
	}
}