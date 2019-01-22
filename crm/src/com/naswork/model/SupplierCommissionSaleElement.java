package com.naswork.model;

import java.util.Date;
import java.util.List;

public class SupplierCommissionSaleElement {
    private Integer id;

    private Integer supplierCommissionSaleId;

    private String remark;

    private Date updateTimestamp;
    
    private String partNumber;
    
    private Double amount;
    
    private List<SupplierCommissionSaleElement> list;
    
    private String description;
    
    private Integer conditionId;
    
    private Integer certificationId;
    
    private Double price;
    
    private String unit;
    
    private String serialNumber;
    
    private Integer tsn;
    
    private Integer csn;
    
    private Date validity;
    
    private String alt;
    
    private String conditionCode;
    
    private String certificationCode;
    
    private String leadTime;
    
    private String location;
    
    private Double feeForExchangeBill;
    
    private Double bankCost;
    
    private Double hazmatFee;
    
    private Double otherFee;
    
    private String tagSrc;
    
    private String tagDate;
    
    private String trace;
    
    private String warranty;
    
    private String coreCharge;
    
    private String shortPartNumber;
    
    private String quoteFeeForExchangeBill;
    
    private String quoteBankCost;
    
    private String quoteHazmatFee;
    
    private String quoteOtherFee;

    private Integer moq;
    
    private String inquiryCount;
    
    private String inquiryAmount;
    
    private String clientCode;
    
    private String average;
    
    private String supplierCode;
    
    private String supplierName;
    
    private String supplierNameInStockmarket;

    private String airType;

	public String getAirType() {
		return airType;
	}

	public void setAirType(String airType) {
		this.airType = airType;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierCommissionSaleId() {
        return supplierCommissionSaleId;
    }

    public void setSupplierCommissionSaleId(Integer supplierCommissionSaleId) {
        this.supplierCommissionSaleId = supplierCommissionSaleId;
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

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.replace(" ", "");
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public List<SupplierCommissionSaleElement> getList() {
		return list;
	}

	public void setList(List<SupplierCommissionSaleElement> list) {
		this.list = list;
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
	 * @return the conditionId
	 */
	public Integer getConditionId() {
		return conditionId;
	}

	/**
	 * @param conditionId the conditionId to set
	 */
	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	/**
	 * @return the certificationId
	 */
	public Integer getCertificationId() {
		return certificationId;
	}

	/**
	 * @param certificationId the certificationId to set
	 */
	public void setCertificationId(Integer certificationId) {
		this.certificationId = certificationId;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
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
	 * @return the validity
	 */
	public Date getValidity() {
		return validity;
	}

	/**
	 * @param validity the validity to set
	 */
	public void setValidity(Date validity) {
		this.validity = validity;
	}

	/**
	 * @return the alt
	 */
	public String getAlt() {
		return alt;
	}

	/**
	 * @param alt the alt to set
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * @return the tsn
	 */
	public Integer getTsn() {
		return tsn;
	}

	/**
	 * @param tsn the tsn to set
	 */
	public void setTsn(Integer tsn) {
		this.tsn = tsn;
	}

	/**
	 * @return the csn
	 */
	public Integer getCsn() {
		return csn;
	}

	/**
	 * @param csn the csn to set
	 */
	public void setCsn(Integer csn) {
		this.csn = csn;
	}

	/**
	 * @return the conditionCode
	 */
	public String getConditionCode() {
		return conditionCode;
	}

	/**
	 * @param conditionCode the conditionCode to set
	 */
	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	/**
	 * @return the certificationCode
	 */
	public String getCertificationCode() {
		return certificationCode;
	}

	/**
	 * @param certificationCode the certificationCode to set
	 */
	public void setCertificationCode(String certificationCode) {
		this.certificationCode = certificationCode;
	}

	/**
	 * @return the leadTime
	 */
	public String getLeadTime() {
		return leadTime;
	}

	/**
	 * @param leadTime the leadTime to set
	 */
	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
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
	 * @return the feeForExchangeBill
	 */
	public Double getFeeForExchangeBill() {
		return feeForExchangeBill;
	}

	/**
	 * @param feeForExchangeBill the feeForExchangeBill to set
	 */
	public void setFeeForExchangeBill(Double feeForExchangeBill) {
		this.feeForExchangeBill = feeForExchangeBill;
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
	 * @return the hazmatFee
	 */
	public Double getHazmatFee() {
		return hazmatFee;
	}

	/**
	 * @param hazmatFee the hazmatFee to set
	 */
	public void setHazmatFee(Double hazmatFee) {
		this.hazmatFee = hazmatFee;
	}

	/**
	 * @return the otherFee
	 */
	public Double getOtherFee() {
		return otherFee;
	}

	/**
	 * @param otherFee the otherFee to set
	 */
	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	/**
	 * @return the tagSrc
	 */
	public String getTagSrc() {
		return tagSrc;
	}

	/**
	 * @param tagSrc the tagSrc to set
	 */
	public void setTagSrc(String tagSrc) {
		this.tagSrc = tagSrc;
	}

	/**
	 * @return the tagDate
	 */
	public String getTagDate() {
		return tagDate;
	}

	/**
	 * @param tagDate the tagDate to set
	 */
	public void setTagDate(String tagDate) {
		this.tagDate = tagDate;
	}

	/**
	 * @return the trace
	 */
	public String getTrace() {
		return trace;
	}

	/**
	 * @param trace the trace to set
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}

	/**
	 * @return the warranty
	 */
	public String getWarranty() {
		return warranty;
	}

	/**
	 * @param warranty the warranty to set
	 */
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	/**
	 * @return the coreCharge
	 */
	public String getCoreCharge() {
		return coreCharge;
	}

	/**
	 * @param coreCharge the coreCharge to set
	 */
	public void setCoreCharge(String coreCharge) {
		this.coreCharge = coreCharge;
	}

	/**
	 * @return the shortPartNumber
	 */
	public String getShortPartNumber() {
		return shortPartNumber;
	}

	/**
	 * @param shortPartNumber the shortPartNumber to set
	 */
	public void setShortPartNumber(String shortPartNumber) {
		this.shortPartNumber = shortPartNumber;
	}

	/**
	 * @return the quoteFeeForExchangeBill
	 */
	public String getQuoteFeeForExchangeBill() {
		return quoteFeeForExchangeBill;
	}

	/**
	 * @param quoteFeeForExchangeBill the quoteFeeForExchangeBill to set
	 */
	public void setQuoteFeeForExchangeBill(String quoteFeeForExchangeBill) {
		this.quoteFeeForExchangeBill = quoteFeeForExchangeBill;
	}

	/**
	 * @return the quoteBankCost
	 */
	public String getQuoteBankCost() {
		return quoteBankCost;
	}

	/**
	 * @param quoteBankCost the quoteBankCost to set
	 */
	public void setQuoteBankCost(String quoteBankCost) {
		this.quoteBankCost = quoteBankCost;
	}

	/**
	 * @return the quoteHazmatFee
	 */
	public String getQuoteHazmatFee() {
		return quoteHazmatFee;
	}

	/**
	 * @param quoteHazmatFee the quoteHazmatFee to set
	 */
	public void setQuoteHazmatFee(String quoteHazmatFee) {
		this.quoteHazmatFee = quoteHazmatFee;
	}

	/**
	 * @return the quoteOtherFee
	 */
	public String getQuoteOtherFee() {
		return quoteOtherFee;
	}

	/**
	 * @param quoteOtherFee the quoteOtherFee to set
	 */
	public void setQuoteOtherFee(String quoteOtherFee) {
		this.quoteOtherFee = quoteOtherFee;
	}

	/**
	 * @return the moq
	 */
	public Integer getMoq() {
		return moq;
	}

	/**
	 * @param moq the moq to set
	 */
	public void setMoq(Integer moq) {
		this.moq = moq;
	}

	/**
	 * @return the inquiryCount
	 */
	public String getInquiryCount() {
		return inquiryCount;
	}

	/**
	 * @param inquiryCount the inquiryCount to set
	 */
	public void setInquiryCount(String inquiryCount) {
		this.inquiryCount = inquiryCount;
	}

	/**
	 * @return the inquiryAmount
	 */
	public String getInquiryAmount() {
		return inquiryAmount;
	}

	/**
	 * @param inquiryAmount the inquiryAmount to set
	 */
	public void setInquiryAmount(String inquiryAmount) {
		this.inquiryAmount = inquiryAmount;
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
	 * @return the average
	 */
	public String getAverage() {
		return average;
	}

	/**
	 * @param average the average to set
	 */
	public void setAverage(String average) {
		this.average = average;
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
	 * @return the supplierName
	 */
	public String getSupplierName() {
		return supplierName;
	}

	/**
	 * @param supplierName the supplierName to set
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/**
	 * @return the supplierNameInStockmarket
	 */
	public String getSupplierNameInStockmarket() {
		return supplierNameInStockmarket;
	}

	/**
	 * @param supplierNameInStockmarket the supplierNameInStockmarket to set
	 */
	public void setSupplierNameInStockmarket(String supplierNameInStockmarket) {
		this.supplierNameInStockmarket = supplierNameInStockmarket;
	}

}