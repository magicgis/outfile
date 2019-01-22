package com.naswork.model;

public class AuthorityRelation {
    private Integer id;

    private Integer userId;

    private Integer clientId;

    private Integer supplierId;
    
    private Integer storehouseAddressId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getStorehouseAddressId() {
		return storehouseAddressId;
	}

	public void setStorehouseAddressId(Integer storehouseAddressId) {
		this.storehouseAddressId = storehouseAddressId;
	}
}