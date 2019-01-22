package com.naswork.module.marketing.controller.clientquote;

import java.util.Date;

public class ElementVo {

	private Integer id;

    private Integer clientInquiryId;

    private Integer elementId;

    private String partNumber;

    private Integer item;

    private String description;

    private String remark;

    private String unit;

    private Double amount;

    private Date updateTimestamp;
    
    private byte[] partNumberCode;

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

	public Integer getElementId() {
		return elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public byte[] getPartNumberCode() {
		return partNumberCode;
	}

	public void setPartNumberCode(byte[] partNumberCode) {
		this.partNumberCode = partNumberCode;
	}
}
