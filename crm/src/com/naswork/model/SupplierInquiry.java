package com.naswork.model;

import java.util.Date;

public class SupplierInquiry {
    private Integer id;

    private Integer supplierId;

    private Integer clientInquiryId;

    private String quoteNumber;

    private Date inquiryDate;

    private Date deadline;

    private String remark;

    private Date updateTimestamp;
    
    private String bsn;
    
    private Integer emailStatus;
    
    private Integer autoAdd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getClientInquiryId() {
        return clientInquiryId;
    }

    public void setClientInquiryId(Integer clientInquiryId) {
        this.clientInquiryId = clientInquiryId;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Date getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(Date inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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

	public String getBsn() {
		return bsn;
	}

	public void setBsn(String bsn) {
		this.bsn = bsn;
	}

	/**
	 * @return the emailStatus
	 */
	public Integer getEmailStatus() {
		return emailStatus;
	}

	/**
	 * @param emailStatus the emailStatus to set
	 */
	public void setEmailStatus(Integer emailStatus) {
		this.emailStatus = emailStatus;
	}

	/**
	 * @return the autoAdd
	 */
	public Integer getAutoAdd() {
		return autoAdd;
	}

	/**
	 * @param autoAdd the autoAdd to set
	 */
	public void setAutoAdd(Integer autoAdd) {
		this.autoAdd = autoAdd;
	}
}