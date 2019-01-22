package com.naswork.model;

import java.util.Date;

public class SendBackMessage {
    private Integer id;

    private Integer exportPackageElementId;

    private Integer manageStatus;

    private Double amount;

    private String remark;

    private Date updateTimestamp;
    
    private String exportNumber;
    
    private String importNumber;
    
    private String supplierOrderNumber;
    
    private String clientOrderNumber;
    
    private String manageStatusValue;
    
    private String supplierCode;
    
    private String clientCode;
    
    private String description;
    
    private String serialNumber;
    
    private String partNumber;
    
    private String location;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExportPackageElementId() {
        return exportPackageElementId;
    }

    public void setExportPackageElementId(Integer exportPackageElementId) {
        this.exportPackageElementId = exportPackageElementId;
    }

    public Integer getManageStatus() {
        return manageStatus;
    }

    public void setManageStatus(Integer manageStatus) {
        this.manageStatus = manageStatus;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

	/**
	 * @return the exportNumber
	 */
	public String getExportNumber() {
		return exportNumber;
	}

	/**
	 * @param exportNumber the exportNumber to set
	 */
	public void setExportNumber(String exportNumber) {
		this.exportNumber = exportNumber;
	}

	/**
	 * @return the importNumber
	 */
	public String getImportNumber() {
		return importNumber;
	}

	/**
	 * @param importNumber the importNumber to set
	 */
	public void setImportNumber(String importNumber) {
		this.importNumber = importNumber;
	}

	/**
	 * @return the supplierOrderNumber
	 */
	public String getSupplierOrderNumber() {
		return supplierOrderNumber;
	}

	/**
	 * @param supplierOrderNumber the supplierOrderNumber to set
	 */
	public void setSupplierOrderNumber(String supplierOrderNumber) {
		this.supplierOrderNumber = supplierOrderNumber;
	}

	/**
	 * @return the clientOrderNumber
	 */
	public String getClientOrderNumber() {
		return clientOrderNumber;
	}

	/**
	 * @param clientOrderNumber the clientOrderNumber to set
	 */
	public void setClientOrderNumber(String clientOrderNumber) {
		this.clientOrderNumber = clientOrderNumber;
	}

	/**
	 * @return the manageStatusValue
	 */
	public String getManageStatusValue() {
		return manageStatusValue;
	}

	/**
	 * @param manageStatusValue the manageStatusValue to set
	 */
	public void setManageStatusValue(String manageStatusValue) {
		this.manageStatusValue = manageStatusValue;
	}

	/**
	 * @return the supplierCode
	 */
	public String getSupplierCode() {
		return supplierCode;
	}

	/**
	 * @param supplierCode the supplierCode to set
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
		this.partNumber = partNumber;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}