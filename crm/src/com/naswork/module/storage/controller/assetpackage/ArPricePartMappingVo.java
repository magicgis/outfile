package com.naswork.module.storage.controller.assetpackage;

import java.util.Date;

/**
 * @Author: white
 * @Date: create in 2018-08-21 12:38
 * @Description:
 * @Modify_By:
 */
public class ArPricePartMappingVo {

    //自增id
    private Integer id;
    //件号
    private String partNumber;
    //ar评估价
    private String arPrice;
    //更新时间
    private Date updateTimestamp;
    //创建人名称
    private String username;
    //ar销售价
    private String arSalePrice;
    //dom
    private String dom;
    //描述
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
