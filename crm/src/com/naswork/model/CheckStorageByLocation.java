package com.naswork.model;

public class CheckStorageByLocation {
    private Integer id;

    private Integer importPackageElementId;

    private String location;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}