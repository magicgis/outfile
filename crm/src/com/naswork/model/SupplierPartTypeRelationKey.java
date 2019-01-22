package com.naswork.model;

public class SupplierPartTypeRelationKey {
    private Integer supplierId;

    private Integer partTypeSubsetId;
    
    private Integer id;
	 
	 private String value;
	 
	 private String code;
	 
	 private String name;

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getPartTypeSubsetId() {
        return partTypeSubsetId;
    }

    public void setPartTypeSubsetId(Integer partTypeSubsetId) {
        this.partTypeSubsetId = partTypeSubsetId;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
}