package com.naswork.model;

import java.util.Date;
import java.util.List;

public class SupplierAptitude {
    private Integer id;

    private String name;

    private Date expireDate;

    private Integer lastUpdateUser;

    private Date updateTimestamp;
    
    private String lastUpdateUserName;
    
    private Integer supplierId;
    
    private List<SupplierAptitude> list;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Integer lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	/**
	 * @return the lastUpdateUserName
	 */
	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	/**
	 * @param lastUpdateUserName the lastUpdateUserName to set
	 */
	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

	/**
	 * @return the list
	 */
	public List<SupplierAptitude> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<SupplierAptitude> list) {
		this.list = list;
	}

	/**
	 * @return the supplierId
	 */
	public Integer getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId the supplierId to set
	 */
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
}