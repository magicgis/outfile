package com.naswork.model;

import java.util.Date;

public class StorageCorrelation {
    private Integer id;

    private Integer importPackageElementId;

    private String correlationImportPackageElementId;

    private Date updateTimestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImportPackageElementId() {
        return importPackageElementId;
    }

    public void setImportPackageElementId(Integer importPackageElementId) {
        this.importPackageElementId = importPackageElementId;
    }

    public String getCorrelationImportPackageElementId() {
        return correlationImportPackageElementId;
    }

    public void setCorrelationImportPackageElementId(String correlationImportPackageElementId) {
        this.correlationImportPackageElementId = correlationImportPackageElementId;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}