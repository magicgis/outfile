package com.naswork.model;

import java.util.Date;

public class ClientReceipt {
    private Integer id;

    private Integer exportPackageId;

    private Date receiveDate;

    private Double receiveSum;

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

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Double getReceiveSum() {
        return receiveSum;
    }

    public void setReceiveSum(Double receiveSum) {
        this.receiveSum = receiveSum;
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