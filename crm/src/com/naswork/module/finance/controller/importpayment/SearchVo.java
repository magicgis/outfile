package com.naswork.module.finance.controller.importpayment;

import java.util.Date;

public class SearchVo {

	private Integer id;
	
	private String importPackageNumber;
	
	private String supplierOrderNumber;
	
	private Integer supplierId;
	
	private String supplierCode;
	
	private Double prepayRate;
	
	private Double shipPayRate;
	
	private Double receivePayRate;
	
	private Double receivePayPeriod;
	
	private Date orderDate;
	
	private Date importDate;
	
	private String paymentType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImportPackageNumber() {
		return importPackageNumber;
	}

	public void setImportPackageNumber(String importPackageNumber) {
		this.importPackageNumber = importPackageNumber;
	}

	public String getSupplierOrderNumber() {
		return supplierOrderNumber;
	}

	public void setSupplierOrderNumber(String supplierOrderNumber) {
		this.supplierOrderNumber = supplierOrderNumber;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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

	public Double getReceivePayRate() {
		return receivePayRate;
	}

	public void setReceivePayRate(Double receivePayRate) {
		this.receivePayRate = receivePayRate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Double getReceivePayPeriod() {
		return receivePayPeriod;
	}

	public void setReceivePayPeriod(Double receivePayPeriod) {
		this.receivePayPeriod = receivePayPeriod;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

}
