package com.naswork.model;

import java.util.Date;

public class OrderApproval {
	private Integer occupy; 
	
	private Double price;
	
	private Integer supplierWeatherOrderElementId;
	
	private String taskId;
	
	private Double amount;
	
	private Integer spzt;
	
	private String soeIds;
	
	private String ipeIds;
	
	private Integer supplierOrderElementId;
	
	private Integer importPackageElementId;
	
	private String ProcessInstanceId;
	
    private Integer id;

    private Integer supplierQuoteElementId;

    private Integer clientOrderElementId;

    private Integer clientOrderId;

    private Integer clientQuoteElementId;

    private Integer state;

    private Integer type;

    private Date updateTimestamp;
    
    private String handle;
    
    private String desc;
    
    private String userId;
    
    private Double bankCost;
    
    private Double finalBankCharges;


	public Integer getOccupy() {
		return occupy;
	}


	public void setOccupy(Integer occupy) {
		this.occupy = occupy;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getHandle() {
		return handle;
	}


	public void setHandle(String handle) {
		this.handle = handle;
	}


	public Integer getId() {
        return id;
    }


	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public Integer getSpzt() {
		return spzt;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public Integer getSupplierWeatherOrderElementId() {
		return supplierWeatherOrderElementId;
	}


	public void setSupplierWeatherOrderElementId(Integer supplierWeatherOrderElementId) {
		this.supplierWeatherOrderElementId = supplierWeatherOrderElementId;
	}


	public void setSpzt(Integer spzt) {
		this.spzt = spzt;
	}


	public String getProcessInstanceId() {
		return ProcessInstanceId;
	}


	public void setProcessInstanceId(String processInstanceId) {
		ProcessInstanceId = processInstanceId;
	}


	public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierQuoteElementId() {
        return supplierQuoteElementId;
    }

    public void setSupplierQuoteElementId(Integer supplierQuoteElementId) {
        this.supplierQuoteElementId = supplierQuoteElementId;
    }

    public Integer getClientOrderElementId() {
        return clientOrderElementId;
    }

    public void setClientOrderElementId(Integer clientOrderElementId) {
        this.clientOrderElementId = clientOrderElementId;
    }

    public Integer getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(Integer clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public Integer getClientQuoteElementId() {
        return clientQuoteElementId;
    }

    public void setClientQuoteElementId(Integer clientQuoteElementId) {
        this.clientQuoteElementId = clientQuoteElementId;
    }

    public Integer getState() {
        return state;
    }

    public String getSoeIds() {
		return soeIds;
	}


	public void setSoeIds(String soeIds) {
		this.soeIds = soeIds;
	}


	public String getIpeIds() {
		return ipeIds;
	}


	public void setIpeIds(String ipeIds) {
		this.ipeIds = ipeIds;
	}


	public Integer getImportPackageElementId() {
		return importPackageElementId;
	}


	public void setImportPackageElementId(Integer importPackageElementId) {
		this.importPackageElementId = importPackageElementId;
	}


	public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }



	public Integer getSupplierOrderElementId() {
		return supplierOrderElementId;
	}


	public void setSupplierOrderElementId(Integer supplierOrderElementId) {
		this.supplierOrderElementId = supplierOrderElementId;
	}


	public void setType(Integer type) {
        this.type = type;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }


	/**
	 * @return the bankCost
	 */
	public Double getBankCost() {
		return bankCost;
	}


	/**
	 * @param bankCost the bankCost to set
	 */
	public void setBankCost(Double bankCost) {
		this.bankCost = bankCost;
	}


	/**
	 * @return the finalBankCharges
	 */
	public Double getFinalBankCharges() {
		return finalBankCharges;
	}


	/**
	 * @param finalBankCharges the finalBankCharges to set
	 */
	public void setFinalBankCharges(Double finalBankCharges) {
		this.finalBankCharges = finalBankCharges;
	}


}