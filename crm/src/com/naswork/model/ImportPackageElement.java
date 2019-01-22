package com.naswork.model;

import java.util.Date;
import java.util.List;

public class ImportPackageElement {
	private Integer approvalStatus;
	
	private String flow;
	
	private Integer supplierOrderElementId;
	
	private Integer supplierQuoteElementId;
	
	private String code;
	
	private String phone;
	
	private String mobile;
	
	private String address;
	
	private String contactName;
	
	private Integer supplierId;
	
	private Integer importPackageElementId;
	
	private Integer spzt;
	
	private Integer importPackageSign;
	
	private Date manufactureDate;
	
	private Date inspectionDate;
	
	private String manufactureDateString;
	
	private String inspectionDateString;
	
	private Integer completeComplianceCertificate;
	 
	private Integer complianceCertificate;
	
	private String parame;
	
	private Double boxWeight;
	
    private Integer id;

    private Integer importPackageId;

    private Integer elementId;

    private String partNumber;

    private String unit;

    private Double amount;

    private Double price;

    private String remark;

    private String location;

    private Date updateTimestamp;

    private Integer certificationId;

    private Integer conditionId;

    private String description;

    private String serialNumber;

    private Integer originalNumber;

    private Date certificationDate;
    
    private String orderNumber;

    private String batchNumber;
    
    private Integer clientOrderElementId;
    
    private Integer certificationStatusId;
    
    private String importPackageNumber;
    
    private Integer hasLife;
    
    private Integer restLife;
    
    private Date expireDate;
    
    private Integer restLifeEmail;
    
    private Integer restLifeNextMonth;
    
    private String supplierOrderNumber;
    
    private String clientOrderNumber;
    
    private String certificationNumber;
    
    private Integer shelfLife;
    
    private String supplierCode;
    
	public Integer getCertificationStatusId() {
		return certificationStatusId;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getSupplierQuoteElementId() {
		return supplierQuoteElementId;
	}

	public Integer getSupplierOrderElementId() {
		return supplierOrderElementId;
	}

	public void setSupplierOrderElementId(Integer supplierOrderElementId) {
		this.supplierOrderElementId = supplierOrderElementId;
	}

	public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
		this.supplierQuoteElementId = supplierQuoteElementId;
	}

	public Integer getImportPackageElementId() {
		return importPackageElementId;
	}

	public void setImportPackageElementId(Integer importPackageElementId) {
		this.importPackageElementId = importPackageElementId;
	}

	public Integer getSpzt() {
		return spzt;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public void setSpzt(Integer spzt) {
		this.spzt = spzt;
	}

	public Integer getImportPackageSign() {
		return importPackageSign;
	}

	public void setImportPackageSign(Integer importPackageSign) {
		this.importPackageSign = importPackageSign;
	}

	public void setCertificationStatusId(Integer certificationStatusId) {
		this.certificationStatusId = certificationStatusId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Double getBoxWeight() {
		return boxWeight;
	}

	public void setBoxWeight(Double boxWeight) {
		this.boxWeight = boxWeight;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getImportPackageId() {
        return importPackageId;
    }

    public void setImportPackageId(Integer importPackageId) {
        this.importPackageId = importPackageId;
    }

    public Integer getElementId() {
        return elementId;
    }

    public Date getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber.replace(" ", "");
    }

    public String getParame() {
		return parame;
	}

	public void setParame(String parame) {
		this.parame = parame;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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

    public Integer getCompleteComplianceCertificate() {
		return completeComplianceCertificate;
	}

	public void setCompleteComplianceCertificate(Integer completeComplianceCertificate) {
		this.completeComplianceCertificate = completeComplianceCertificate;
	}

	public Integer getComplianceCertificate() {
		return complianceCertificate;
	}

	public void setComplianceCertificate(Integer complianceCertificate) {
		this.complianceCertificate = complianceCertificate;
	}

	public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getOriginalNumber() {
        return originalNumber;
    }

    public void setOriginalNumber(Integer originalNumber) {
        this.originalNumber = originalNumber;
    }

    public Date getCertificationDate() {
        return certificationDate;
    }

    public void setCertificationDate(Date certificationDate) {
        this.certificationDate = certificationDate;
    }

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getClientOrderElementId() {
		return clientOrderElementId;
	}

	public void setClientOrderElementId(Integer clientOrderElementId) {
		this.clientOrderElementId = clientOrderElementId;
	}

	public String getImportPackageNumber() {
		return importPackageNumber;
	}

	public void setImportPackageNumber(String importPackageNumber) {
		this.importPackageNumber = importPackageNumber;
	}

	/**
	 * @return the manufactureDateString
	 */
	public String getManufactureDateString() {
		return manufactureDateString;
	}

	/**
	 * @param manufactureDateString the manufactureDateString to set
	 */
	public void setManufactureDateString(String manufactureDateString) {
		this.manufactureDateString = manufactureDateString;
	}

	/**
	 * @return the inspectionDateString
	 */
	public String getInspectionDateString() {
		return inspectionDateString;
	}

	/**
	 * @param inspectionDateString the inspectionDateString to set
	 */
	public void setInspectionDateString(String inspectionDateString) {
		this.inspectionDateString = inspectionDateString;
	}

	/**
	 * @return the hasLife
	 */
	public Integer getHasLife() {
		return hasLife;
	}

	/**
	 * @param hasLife the hasLife to set
	 */
	public void setHasLife(Integer hasLife) {
		this.hasLife = hasLife;
	}

	/**
	 * @return the restLife
	 */
	public Integer getRestLife() {
		return restLife;
	}

	/**
	 * @param restLife the restLife to set
	 */
	public void setRestLife(Integer restLife) {
		this.restLife = restLife;
	}

	/**
	 * @return the expireDate
	 */
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate the expireDate to set
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return the restLifeEmail
	 */
	public Integer getRestLifeEmail() {
		return restLifeEmail;
	}

	/**
	 * @param restLifeEmail the restLifeEmail to set
	 */
	public void setRestLifeEmail(Integer restLifeEmail) {
		this.restLifeEmail = restLifeEmail;
	}

	/**
	 * @return the restLifeNextMonth
	 */
	public Integer getRestLifeNextMonth() {
		return restLifeNextMonth;
	}

	/**
	 * @param restLifeNextMonth the restLifeNextMonth to set
	 */
	public void setRestLifeNextMonth(Integer restLifeNextMonth) {
		this.restLifeNextMonth = restLifeNextMonth;
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
	 * @return the certificationNumber
	 */
	public String getCertificationNumber() {
		return certificationNumber;
	}

	/**
	 * @param certificationNumber the certificationNumber to set
	 */
	public void setCertificationNumber(String certificationNumber) {
		this.certificationNumber = certificationNumber;
	}

	/**
	 * @return the shelfLife
	 */
	public Integer getShelfLife() {
		return shelfLife;
	}

	/**
	 * @param shelfLife the shelfLife to set
	 */
	public void setShelfLife(Integer shelfLife) {
		this.shelfLife = shelfLife;
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

}