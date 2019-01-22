package com.naswork.model;

import java.util.List;

public class ImportStorageLocationList {
    private Integer id;

    private String location;
    
    private Integer storehouseAddressId;
    
    private List<ImportStorageLocationList> list;
    
    private String sourceNumber;

    public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

	public Integer getStorehouseAddressId() {
		return storehouseAddressId;
	}

	public void setStorehouseAddressId(Integer storehouseAddressId) {
		this.storehouseAddressId = storehouseAddressId;
	}

	public List<ImportStorageLocationList> getList() {
		return list;
	}

	public void setList(List<ImportStorageLocationList> list) {
		this.list = list;
	}
}