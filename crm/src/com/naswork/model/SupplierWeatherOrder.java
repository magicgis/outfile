package com.naswork.model;

import java.util.List;

public class SupplierWeatherOrder {
    private Integer id;

    private Integer supplierId;

    private Integer clientWeatherOrderId;

    private Double bankCost;

    private Double feeForExchangeBill;

    private Double otherFee;
    
    private String supplierCode;
    
    private List<SupplierWeatherOrder> list;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getClientWeatherOrderId() {
        return clientWeatherOrderId;
    }

    public void setClientWeatherOrderId(Integer clientWeatherOrderId) {
        this.clientWeatherOrderId = clientWeatherOrderId;
    }

    public Double getBankCost() {
        return bankCost;
    }

    public void setBankCost(Double bankCost) {
        this.bankCost = bankCost;
    }

    public Double getFeeForExchangeBill() {
        return feeForExchangeBill;
    }

    public void setFeeForExchangeBill(Double feeForExchangeBill) {
        this.feeForExchangeBill = feeForExchangeBill;
    }

    public Double getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(Double otherFee) {
        this.otherFee = otherFee;
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
	 * @return the list
	 */
	public List<SupplierWeatherOrder> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<SupplierWeatherOrder> list) {
		this.list = list;
	}
}