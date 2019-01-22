package com.naswork.model;

import java.util.Date;

public class ExchangeImportPackage {

	private Integer id;

    private Integer clientOrderElementId;

    private String location;

    private Date importDate;

    private Double amount;

    private Integer certificationId;

    private Integer conditionId;

    private String shipNumber;

    private Date exportDate;
    
    private String orderNumber;
    
    private Date orderDate;
    
    private String partNumber;
    
    private Double remainAmount;
    
    private String clientCode;
    
    private Integer conId;
    
    private String conCode;
    
    private String conValue;
    
    private Integer cerId;
    
    private String cerCode;
    
    private String cerValue;
    
    private Date updateTimestamp;
    
    private String description;
    
    private String remark;
    
    private String bizValue;
    
    private Integer complete;
    
    private String sn;
    
    private Integer emailStatus;
    
    private Integer repairType;
    
    private String repairTypeValue;
    
    
    /**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}

	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	/**
	 * @return the remainAmount
	 */
	public Double getRemainAmount() {
		return remainAmount;
	}

	/**
	 * @param remainAmount the remainAmount to set
	 */
	public void setRemainAmount(Double remainAmount) {
		this.remainAmount = remainAmount;
	}

	/**
	 * @return the clientCode
	 */
	public String getClientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientOrderElementId() {
        return clientOrderElementId;
    }

    public void setClientOrderElementId(Integer clientOrderElementId) {
        this.clientOrderElementId = clientOrderElementId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(Integer certificationId) {
        this.certificationId = certificationId;
    }

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    public String getShipNumber() {
        return shipNumber;
    }

    public void setShipNumber(String shipNumber) {
        this.shipNumber = shipNumber;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

	/**
	 * @return the conId
	 */
	public Integer getConId() {
		return conId;
	}

	/**
	 * @param conId the conId to set
	 */
	public void setConId(Integer conId) {
		this.conId = conId;
	}

	/**
	 * @return the conCode
	 */
	public String getConCode() {
		return conCode;
	}

	/**
	 * @param conCode the conCode to set
	 */
	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

	/**
	 * @return the conValue
	 */
	public String getConValue() {
		return conValue;
	}

	/**
	 * @param conValue the conValue to set
	 */
	public void setConValue(String conValue) {
		this.conValue = conValue;
	}

	/**
	 * @return the cerId
	 */
	public Integer getCerId() {
		return cerId;
	}

	/**
	 * @param cerId the cerId to set
	 */
	public void setCerId(Integer cerId) {
		this.cerId = cerId;
	}

	/**
	 * @return the cerCode
	 */
	public String getCerCode() {
		return cerCode;
	}

	/**
	 * @param cerCode the cerCode to set
	 */
	public void setCerCode(String cerCode) {
		this.cerCode = cerCode;
	}

	/**
	 * @return the cerValue
	 */
	public String getCerValue() {
		return cerValue;
	}

	/**
	 * @param cerValue the cerValue to set
	 */
	public void setCerValue(String cerValue) {
		this.cerValue = cerValue;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the updateTimestamp
	 */
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	/**
	 * @param updateTimestamp the updateTimestamp to set
	 */
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	/**
	 * @return the bizValue
	 */
	public String getBizValue() {
		return bizValue;
	}

	/**
	 * @param bizValue the bizValue to set
	 */
	public void setBizValue(String bizValue) {
		this.bizValue = bizValue;
	}

	/**
	 * @return the complete
	 */
	public Integer getComplete() {
		return complete;
	}

	/**
	 * @param complete the complete to set
	 */
	public void setComplete(Integer complete) {
		this.complete = complete;
	}

	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
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
	 * @return the repairType
	 */
	public Integer getRepairType() {
		return repairType;
	}

	/**
	 * @param repairType the repairType to set
	 */
	public void setRepairType(Integer repairType) {
		this.repairType = repairType;
	}

	/**
	 * @return the repairTypeValue
	 */
	public String getRepairTypeValue() {
		return repairTypeValue;
	}

	/**
	 * @param repairTypeValue the repairTypeValue to set
	 */
	public void setRepairTypeValue(String repairTypeValue) {
		this.repairTypeValue = repairTypeValue;
	}
}