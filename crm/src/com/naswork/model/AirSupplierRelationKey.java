package com.naswork.model;

public class AirSupplierRelationKey {
	 private Integer id;
	 
	 private String code;
	 
	 private String name;
	
	private Integer supplierId;

    private Integer airId;
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getAirId() {
        return airId;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAirId(Integer airId) {
        this.airId = airId;
    }
}