package com.naswork.model;

public class SupplierPnRelationBackUp {
    private Integer id;

    private String ata;

    private String aircraft;

    private String condition;

    private Double qty;

    private Integer type;

    private Integer supplyAbility;

    private Integer stockAbility;

    private Integer repairAbility;

    private Integer exchangeAbility;

    private String sn;

    private String repair;

    private String cert;

    private String remark;

    private Integer userId;

    private String msn;
    
    private String partNumber;
    
    private String description;
    
    private Integer supplierId;
    
    private String bsn;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the ata
	 */
	public String getAta() {
		return ata;
	}

	/**
	 * @param ata the ata to set
	 */
	public void setAta(String ata) {
		this.ata = ata;
	}

	/**
	 * @return the aircraft
	 */
	public String getAircraft() {
		return aircraft;
	}

	/**
	 * @param aircraft the aircraft to set
	 */
	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * @return the qty
	 */
	public Double getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(Double qty) {
		this.qty = qty;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the supplyAbility
	 */
	public Integer getSupplyAbility() {
		return supplyAbility;
	}

	/**
	 * @param supplyAbility the supplyAbility to set
	 */
	public void setSupplyAbility(Integer supplyAbility) {
		this.supplyAbility = supplyAbility;
	}

	/**
	 * @return the stockAbility
	 */
	public Integer getStockAbility() {
		return stockAbility;
	}

	/**
	 * @param stockAbility the stockAbility to set
	 */
	public void setStockAbility(Integer stockAbility) {
		this.stockAbility = stockAbility;
	}

	/**
	 * @return the repairAbility
	 */
	public Integer getRepairAbility() {
		return repairAbility;
	}

	/**
	 * @param repairAbility the repairAbility to set
	 */
	public void setRepairAbility(Integer repairAbility) {
		this.repairAbility = repairAbility;
	}

	/**
	 * @return the exchangeAbility
	 */
	public Integer getExchangeAbility() {
		return exchangeAbility;
	}

	/**
	 * @param exchangeAbility the exchangeAbility to set
	 */
	public void setExchangeAbility(Integer exchangeAbility) {
		this.exchangeAbility = exchangeAbility;
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
	 * @return the repair
	 */
	public String getRepair() {
		return repair;
	}

	/**
	 * @param repair the repair to set
	 */
	public void setRepair(String repair) {
		this.repair = repair;
	}

	/**
	 * @return the cert
	 */
	public String getCert() {
		return cert;
	}

	/**
	 * @param cert the cert to set
	 */
	public void setCert(String cert) {
		this.cert = cert;
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
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the msn
	 */
	public String getMsn() {
		return msn;
	}

	/**
	 * @param msn the msn to set
	 */
	public void setMsn(String msn) {
		this.msn = msn;
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
	 * @return the supplierId
	 */
	public Integer getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId the supplierId to set
	 */
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the bsn
	 */
	public String getBsn() {
		return bsn;
	}

	/**
	 * @param bsn the bsn to set
	 */
	public void setBsn(String bsn) {
		this.bsn = bsn;
	}
    

}