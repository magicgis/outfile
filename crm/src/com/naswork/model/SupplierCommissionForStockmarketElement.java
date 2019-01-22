package com.naswork.model;

import java.util.Date;
import java.util.List;

/**
@Author: Modify by white
@DateTime: 2018/10/11 9:56
@Description: 添加CSN,TSN,ATA属性
*/
/**
@Author: Modify by white
@DateTime: 2018/10/24 17:08
@Description: 添加packagePrice属性
*/
public class SupplierCommissionForStockmarketElement {
    private Integer id;
    
    private Integer supplierCommissionForStockmarketId;

    private String partNumber;

    private String description;

    private String serialNumber;

    private Double amount;

    private String condition;

    private String dom;

    private String manufacturer;

    private String arPrice;

    private String remark;

    private Date updateTimestamp;
    
    private Integer item;
    
    private String alt;
    
    private Integer isReplace;
    
    private List<SupplierCommissionForStockmarketElement> list;

    private String tsn;

    private String csn;

    private String ata;

    private String packagePrice;

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getTsn() {
        return tsn;
    }

    public void setTsn(String tsn) {
        this.tsn = tsn;
    }

    public String getCsn() {
        return csn;
    }

    public void setCsn(String csn) {
        this.csn = csn;
    }

    public String getAta() {
        return ata;
    }

    public void setAta(String ata) {
        this.ata = ata;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getArPrice() {
        return arPrice;
    }

    public void setArPrice(String arPrice) {
        this.arPrice = arPrice;
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
	 * @return the list
	 */
	public List<SupplierCommissionForStockmarketElement> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<SupplierCommissionForStockmarketElement> list) {
		this.list = list;
	}

	/**
	 * @return the supplierCommissionForStockmarketId
	 */
	public Integer getSupplierCommissionForStockmarketId() {
		return supplierCommissionForStockmarketId;
	}

	/**
	 * @param supplierCommissionForStockmarketId the supplierCommissionForStockmarketId to set
	 */
	public void setSupplierCommissionForStockmarketId(
			Integer supplierCommissionForStockmarketId) {
		this.supplierCommissionForStockmarketId = supplierCommissionForStockmarketId;
	}

	/**
	 * @return the item
	 */
	public Integer getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(Integer item) {
		this.item = item;
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
	 * @return the isReplace
	 */
	public Integer getIsReplace() {
		return isReplace;
	}

	/**
	 * @param isReplace the isReplace to set
	 */
	public void setIsReplace(Integer isReplace) {
		this.isReplace = isReplace;
	}
}