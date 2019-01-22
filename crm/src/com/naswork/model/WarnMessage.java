package com.naswork.model;

import java.util.Date;

public class WarnMessage {

	private Integer id;

    private Integer clientInquiryId;

    private String partNumber;

    private Integer readStatus;

    private Date updateTimestamp;
    
    private Integer clientId;
    
    public WarnMessage() {
    	
	}
    
    public WarnMessage(Integer clientInquiryId, String partNumber) {
		super();
		this.clientInquiryId = clientInquiryId;
		this.partNumber = partNumber;
	}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientInquiryId() {
        return clientInquiryId;
    }

    public void setClientInquiryId(Integer clientInquiryId) {
        this.clientInquiryId = clientInquiryId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber.replace(" ", "");
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

}