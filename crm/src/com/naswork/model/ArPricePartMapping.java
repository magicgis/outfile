package com.naswork.model;

import java.util.Date;

public class ArPricePartMapping {
    //自增id
    private Integer id;
    //件号
    private String partNumber;
    //ar评估价
    private String arPrice;
    //创建时间
    private Date updateTimestamp;
    //创建者id
    private Integer createUserId;
    //ar销售价
    private String arSalePrice;
    //dom
    private String dom;
    //描述description
    private String description;


    public String getArPrice() {
        return arPrice;
    }

    public void setArPrice(String arPrice) {
        this.arPrice = arPrice;
    }

    public String getArSalePrice() {
        return arSalePrice;
    }

    public void setArSalePrice(String arSalePrice) {
        this.arSalePrice = arSalePrice;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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



    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	/**
	 * @return the createUserId
	 */
	public Integer getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
}