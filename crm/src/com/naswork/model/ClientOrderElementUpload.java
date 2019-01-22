package com.naswork.model;

public class ClientOrderElementUpload {
    private Integer id;

    private Integer userId;

    private Integer item;

    private String partNumber;

    private String error;
    
    private Integer line;

    public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

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

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber.replace(" ", "");
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}