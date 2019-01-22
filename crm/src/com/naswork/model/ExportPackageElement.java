package com.naswork.model;

import java.util.Date;
import java.util.List;

public class ExportPackageElement {
    private Integer id;

    private Integer exportPackageId;

    private Integer importPackageElementId;

    private Double amount;

    private String remark;

    private Date updateTimestamp;
    
    private Integer exportRackageInstructionsElementId;
    
    private Integer status;
    
    private List<ImportPackageElement> list;
    
    private Integer exportPackageElementId;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExportPackageId() {
        return exportPackageId;
    }

    public void setExportPackageId(Integer exportPackageId) {
        this.exportPackageId = exportPackageId;
    }

    public Integer getImportPackageElementId() {
        return importPackageElementId;
    }

    public void setImportPackageElementId(Integer importPackageElementId) {
        this.importPackageElementId = importPackageElementId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getExportRackageInstructionsElementId() {
		return exportRackageInstructionsElementId;
	}

	public void setExportRackageInstructionsElementId(
			Integer exportRackageInstructionsElementId) {
		this.exportRackageInstructionsElementId = exportRackageInstructionsElementId;
	}

	public List<ImportPackageElement> getList() {
		return list;
	}

	public void setList(List<ImportPackageElement> list) {
		this.list = list;
	}

	/**
	 * @return the exportPackageElementId
	 */
	public Integer getExportPackageElementId() {
		return exportPackageElementId;
	}

	/**
	 * @param exportPackageElementId the exportPackageElementId to set
	 */
	public void setExportPackageElementId(Integer exportPackageElementId) {
		this.exportPackageElementId = exportPackageElementId;
	}

}