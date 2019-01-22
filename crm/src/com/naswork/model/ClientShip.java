package com.naswork.model;

import java.util.Date;

public class ClientShip {
    private Integer id;

    private Integer exportPackageId;

    private String shipNumber;

    private Date shipDate;

    private Integer clientContactId;

    private Integer shipContactId;

    private String weight;

    private String dimensions;

    private String shipInvoiceNumber;

    private String remark;

    private Date updateTimestamp;
    
    
    


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExportPackageId() {
        return exportPackageId;
    }

    public void setExportPackageId(Integer exportPackageId) {
        this.exportPackageId = exportPackageId;
    }

    public String getShipNumber() {
        return shipNumber;
    }

    public void setShipNumber(String shipNumber) {
        this.shipNumber = shipNumber;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public Integer getShipContactId() {
        return shipContactId;
    }

    public void setShipContactId(Integer shipContactId) {
        this.shipContactId = shipContactId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getShipInvoiceNumber() {
        return shipInvoiceNumber;
    }

    public void setShipInvoiceNumber(String shipInvoiceNumber) {
        this.shipInvoiceNumber = shipInvoiceNumber;
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

}