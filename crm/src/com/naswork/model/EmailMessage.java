package com.naswork.model;

import java.util.Date;

public class EmailMessage {
    private Integer id;

    private Integer supplierInquiryId;

    private String email;

    private String cc;

    private String bcc;

    private Date updateDatetime;
    
	public EmailMessage(Integer supplierInquiryId, String email,
			String cc, String bcc,Date updateDatetime) {
		super();
		this.supplierInquiryId = supplierInquiryId;
		this.email = email;
		this.cc = cc;
		this.bcc = bcc;
		this.updateDatetime = updateDatetime;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierInquiryId() {
        return supplierInquiryId;
    }

    public void setSupplierInquiryId(Integer supplierInquiryId) {
        this.supplierInquiryId = supplierInquiryId;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    
    public EmailMessage(){
    	
    }

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}