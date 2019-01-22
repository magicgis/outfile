package com.naswork.model;

import java.util.Date;

public class ExportPackage {
    private Integer id;

    private String exportNumber;

    private Date exportDate;

    private Integer clientId;

    private String remark;

    private Date updateTimestamp;

    private Double exchangeRate;

    private Integer currencyId;

    private Integer seq;
    
    private Integer logisticsWay;
    
    private String awb;
    
    private Double weight;
    
    private String size;
    
    private Integer exportPackageInstructionsId;
    
    private String exportPackageInstructionsNumber;
    
    private Double exportFee;
    
    private Double freight;
    
    private Double exportAmount;
    
    private Double totalAmount;
    
    private Date realExportDate;
    
    private Integer feeCurrencyId;
    
    private Double feeRate;
    
    private Integer createUser;
    
    private Integer lastUpdateUser;
    
    private String createUserName;
    
    private String lastUpdateUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExportNumber() {
        return exportNumber;
    }

    public void setExportNumber(String exportNumber) {
        this.exportNumber = exportNumber;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
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

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

	public Integer getLogisticsWay() {
		return logisticsWay;
	}

	public void setLogisticsWay(Integer logisticsWay) {
		this.logisticsWay = logisticsWay;
	}

	public String getAwb() {
		return awb;
	}

	public void setAwb(String awb) {
		this.awb = awb;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getExportPackageInstructionsNumber() {
		return exportPackageInstructionsNumber;
	}

	public void setExportPackageInstructionsNumber(
			String exportPackageInstructionsNumber) {
		this.exportPackageInstructionsNumber = exportPackageInstructionsNumber;
	}

	public Integer getExportPackageInstructionsId() {
		return exportPackageInstructionsId;
	}

	public void setExportPackageInstructionsId(Integer exportPackageInstructionsId) {
		this.exportPackageInstructionsId = exportPackageInstructionsId;
	}

	/**
	 * @return the exportFee
	 */
	public Double getExportFee() {
		return exportFee;
	}

	/**
	 * @param exportFee the exportFee to set
	 */
	public void setExportFee(Double exportFee) {
		this.exportFee = exportFee;
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
	 * @return the exportAmount
	 */
	public Double getExportAmount() {
		return exportAmount;
	}

	/**
	 * @param exportAmount the exportAmount to set
	 */
	public void setExportAmount(Double exportAmount) {
		this.exportAmount = exportAmount;
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
	 * @return the realExportDate
	 */
	public Date getRealExportDate() {
		return realExportDate;
	}

	/**
	 * @param realExportDate the realExportDate to set
	 */
	public void setRealExportDate(Date realExportDate) {
		this.realExportDate = realExportDate;
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