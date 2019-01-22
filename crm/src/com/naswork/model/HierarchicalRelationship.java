package com.naswork.model;

public class HierarchicalRelationship {
	private String email;
	
	private String userId;
	
	private String userName;
	
    private Integer id;

    private Integer superiorUserId;

    private Integer subordinateUserId;

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSuperiorUserId() {
        return superiorUserId;
    }

    public void setSuperiorUserId(Integer superiorUserId) {
        this.superiorUserId = superiorUserId;
    }

    public Integer getSubordinateUserId() {
        return subordinateUserId;
    }

    public void setSubordinateUserId(Integer subordinateUserId) {
        this.subordinateUserId = subordinateUserId;
    }
}