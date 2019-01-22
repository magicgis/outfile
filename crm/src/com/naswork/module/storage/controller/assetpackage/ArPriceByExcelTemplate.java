package com.naswork.module.storage.controller.assetpackage;

/**
 * @Program: ArPriceByExcelTemplate
 * @Description: AR评估价格上传模板
 * @Author: White
 * @DateTime: 2018-10-12 09:51:42
 **/

public class ArPriceByExcelTemplate {

    //件号
    private String partNumber;
    //描述
    private String description;
    //dom
    private String dom;
    //ar评估价
    private Double arEvaluationPrice;
    //ar出售价
    private Double arSalePrice;

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

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public Double getArEvaluationPrice() {
        return arEvaluationPrice;
    }

    public void setArEvaluationPrice(Double arEvaluationPrice) {
        this.arEvaluationPrice = arEvaluationPrice;
    }

    public Double getArSalePrice() {
        return arSalePrice;
    }

    public void setArSalePrice(Double arSalePrice) {
        this.arSalePrice = arSalePrice;
    }
}
