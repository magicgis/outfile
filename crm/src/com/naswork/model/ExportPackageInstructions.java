package com.naswork.model;

import java.util.Date;

public class ExportPackageInstructions {
    private Integer id;

    private String exportPackageInstructionsNumber;

    private String remark;

    private Date creatDate;
    
    private String code;
    
    private Integer clientId;

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExportPackageInstructionsNumber() {
        return exportPackageInstructionsNumber;
    }

    public void setExportPackageInstructionsNumber(String exportPackageInstructionsNumber) {
        this.exportPackageInstructionsNumber = exportPackageInstructionsNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

	/**
	 * @return the clientId
	 */
	public Integer getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
}