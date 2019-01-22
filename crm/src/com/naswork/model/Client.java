package com.naswork.model;

import java.util.Date;

public class Client {
	private String clientTypeValue;
	
	private Integer certification;
	
	private Double profitMargin;
	
    private Integer id;

    private String code;

    private String name;

    private Integer currencyId;
    
    private String value;

    private String postCode;

    private String address;

    private String shipAddress;

    private String phone;

    private String fax;

    private String email;

    private String remark;

    private Date updateTimestamp;
    
    private Double prepayRate;
	
   	private Double shipPayRate;
   	
   	private Integer shipPayPeriod;
   	
   	private Double receivePayRate;
   	
   	private Integer receivePayPeriod;
   	
   	private Integer owner;
   	
   	private String ownerName;
   	
   	private Date createDate;
   	
   	private Integer clientStatusId;
   	
   	private Integer taxReturn;
   	
   	private Integer clientLevelId;
   	
   	private String clientStage;
   	
   	private String clientSource;
   	
   	private String clientAbility;
   	
   	private String clientShortName;
   	
   	private String clientType;
   	
   	private Integer clientShipWay;
   	
   	private String shipWayName;
   	
   	private Integer termsOfDelivery;
   	
   	private String delivery;
   	
   	private String clientStatusValue;
   	
   	private String clientLevelValue;
   	
   	private Integer clientTemplateType;
   	
   	private String clientTemplateTypeValue;
   	
   	private String invoiceTempletValue;
   	
   	private Integer invoiceTemplet;
   	
   	private String url;
   	
   	private Double fixedCost;
   	
   	private Integer shipTemplet;
   	
   	private String shipTempletValue;
   	
   	private Double lowestFreight;
   	
   	private Double freight;
   	
   	private Double bankCost;
   	
   	private String location;
   	
   	private String userIds;
   	
    public String getClientTypeValue() {
		return clientTypeValue;
	}

	public void setClientTypeValue(String clientTypeValue) {
		this.clientTypeValue = clientTypeValue;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(Double profitMargin) {
		this.profitMargin = profitMargin;
	}

	public String getCode() {
        return code;
    }

    public Integer getCertification() {
		return certification;
	}

	public void setCertification(Integer certification) {
		this.certification = certification;
	}

	public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getPrepayRate() {
		return prepayRate;
	}

	public void setPrepayRate(Double prepayRate) {
		this.prepayRate = prepayRate;
	}

	public Double getShipPayRate() {
		return shipPayRate;
	}

	public void setShipPayRate(Double shipPayRate) {
		this.shipPayRate = shipPayRate;
	}

	public Integer getShipPayPeriod() {
		return shipPayPeriod;
	}

	public void setShipPayPeriod(Integer shipPayPeriod) {
		this.shipPayPeriod = shipPayPeriod;
	}

	public Double getReceivePayRate() {
		return receivePayRate;
	}

	public void setReceivePayRate(Double receivePayRate) {
		this.receivePayRate = receivePayRate;
	}

	public Integer getReceivePayPeriod() {
		return receivePayPeriod;
	}

	public void setReceivePayPeriod(Integer receivePayPeriod) {
		this.receivePayPeriod = receivePayPeriod;
	}

	public String getClientSource() {
		return clientSource;
	}

	public void setClientSource(String clientSource) {
		this.clientSource = clientSource;
	}

	public String getClientAbility() {
		return clientAbility;
	}

	public void setClientAbility(String clientAbility) {
		this.clientAbility = clientAbility;
	}

	public String getClientShortName() {
		return clientShortName;
	}

	public void setClientShortName(String clientShortName) {
		this.clientShortName = clientShortName;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public Integer getClientStatusId() {
		return clientStatusId;
	}

	public void setClientStatusId(Integer clientStatusId) {
		this.clientStatusId = clientStatusId;
	}

	public Integer getTaxReturn() {
		return taxReturn;
	}

	public void setTaxReturn(Integer taxReturn) {
		this.taxReturn = taxReturn;
	}

	public Integer getClientLevelId() {
		return clientLevelId;
	}

	public void setClientLevelId(Integer clientLevelId) {
		this.clientLevelId = clientLevelId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Integer getClientShipWay() {
		return clientShipWay;
	}

	public void setClientShipWay(Integer clientShipWay) {
		this.clientShipWay = clientShipWay;
	}

	public String getShipWayName() {
		return shipWayName;
	}

	public void setShipWayName(String shipWayName) {
		this.shipWayName = shipWayName;
	}

	public Integer getTermsOfDelivery() {
		return termsOfDelivery;
	}

	public void setTermsOfDelivery(Integer termsOfDelivery) {
		this.termsOfDelivery = termsOfDelivery;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getClientStatusValue() {
		return clientStatusValue;
	}

	public void setClientStatusValue(String clientStatusValue) {
		this.clientStatusValue = clientStatusValue;
	}


	public String getClientLevelValue() {
		return clientLevelValue;
	}

	public void setClientLevelValue(String clientLevelValue) {
		this.clientLevelValue = clientLevelValue;
	}

	public String getClientStage() {
		return clientStage;
	}

	public void setClientStage(String clientStage) {
		this.clientStage = clientStage;
	}

	public Integer getClientTemplateType() {
		return clientTemplateType;
	}

	public void setClientTemplateType(Integer clientTemplateType) {
		this.clientTemplateType = clientTemplateType;
	}

	public String getClientTemplateTypeValue() {
		return clientTemplateTypeValue;
	}

	public void setClientTemplateTypeValue(String clientTemplateTypeValue) {
		this.clientTemplateTypeValue = clientTemplateTypeValue;
	}

	public String getInvoiceTempletValue() {
		return invoiceTempletValue;
	}

	public void setInvoiceTempletValue(String invoiceTempletValue) {
		this.invoiceTempletValue = invoiceTempletValue;
	}

	public Integer getInvoiceTemplet() {
		return invoiceTemplet;
	}

	public void setInvoiceTemplet(Integer invoiceTemplet) {
		this.invoiceTemplet = invoiceTemplet;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public Integer getShipTemplet() {
		return shipTemplet;
	}

	public void setShipTemplet(Integer shipTemplet) {
		this.shipTemplet = shipTemplet;
	}

	public String getShipTempletValue() {
		return shipTempletValue;
	}

	public void setShipTempletValue(String shipTempletValue) {
		this.shipTempletValue = shipTempletValue;
	}

	/**
	 * @return the lowestFreight
	 */
	public Double getLowestFreight() {
		return lowestFreight;
	}

	/**
	 * @param lowestFreight the lowestFreight to set
	 */
	public void setLowestFreight(Double lowestFreight) {
		this.lowestFreight = lowestFreight;
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
	 * @return the bankCost
	 */
	public Double getBankCost() {
		return bankCost;
	}

	/**
	 * @param bankCost the bankCost to set
	 */
	public void setBankCost(Double bankCost) {
		this.bankCost = bankCost;
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

	/**
	 * @return the userIds
	 */
	public String getUserIds() {
		return userIds;
	}

	/**
	 * @param userIds the userIds to set
	 */
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

}