package com.naswork.model;

import java.util.Date;

public class ImportPackage {
	private Double weight;
	
	private String supplierOrderNumber;
	
	private String logisticsNo;
	
	private Integer logisticsWay;
	
    private Integer id;

    private String importNumber;

    private Date importDate;

    private Integer supplierId;

    private String remark;

    private Date updateTimestamp;

    private Double exchangeRate;

    private Integer currencyId;

    private Integer seq;
    
    private Integer importStatus;
    
    private Double importFee;
    
    private Double freight;
    
    private Double totalAmount;
    
    private Integer feeCurrencyId;
    
    private Double feeRate;
    
    private Integer createUser;
    
    private Integer lastUpdateUser;
    
    private String createUserName;
    
    private String lastUpdateUserName;

    public Integer getId() {
        return id;
    }

    public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public String getImportNumber() {
        return importNumber;
    }

    public void setImportNumber(String importNumber) {
        this.importNumber = importNumber;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getSupplierOrderNumber() {
		return supplierOrderNumber;
	}

	public void setSupplierOrderNumber(String supplierOrderNumber) {
		this.supplierOrderNumber = supplierOrderNumber;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public Integer getLogisticsWay() {
		return logisticsWay;
	}

	public void setLogisticsWay(Integer logisticsWay) {
		this.logisticsWay = logisticsWay;
	}

	public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

	public Integer getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(Integer importStatus) {
		this.importStatus = importStatus;
	}

	/**
	 * @return the importFee
	 */
	public Double getImportFee() {
		return importFee;
	}

	/**
	 * @param importFee the importFee to set
	 */
	public void setImportFee(Double importFee) {
		this.importFee = importFee;
	}

	/**
	 * @return the freight
	 */
	public Double getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight to set
	 */
	public void setFreight(Double freight) {
		this.freight = freight;
	}

	/**
	 * @return the totalAmount
	 */
	public Double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the feeCurrencyId
	 */
	public Integer getFeeCurrencyId() {
		return feeCurrencyId;
	}

	/**
	 * @param feeCurrencyId the feeCurrencyId to set
	 */
	public void setFeeCurrencyId(Integer feeCurrencyId) {
		this.feeCurrencyId = feeCurrencyId;
	}

	/**
	 * @return the feeRate
	 */
	public Double getFeeRate() {
		return feeRate;
	}

	/**
	 * @param feeRate the feeRate to set
	 */
	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}

	/**
	 * @return the createUser
	 */
	public Integer getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the lastUpdateUser
	 */
	public Integer getLastUpdateUser() {
		return lastUpdateUser;
	}

	/**
	 * @param lastUpdateUser the lastUpdateUser to set
	 */
	public void setLastUpdateUser(Integer lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the lastUpdateUserName
	 */
	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	/**
	 * @param lastUpdateUserName the lastUpdateUserName to set
	 */
	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}
}