package com.naswork.model;

public class ExportPackageInstructionsElement {
	private Integer exportPackageStatus;
	
    private Integer id;

    private Integer importPackageElementId;

    private Integer exportPackageInstructionsId;
    
    private Double amount;


	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getId() {
        return id;
    }

    public Integer getExportPackageStatus() {
		return exportPackageStatus;
	}

	public void setExportPackageStatus(Integer exportPackageStatus) {
		this.exportPackageStatus = exportPackageStatus;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImportPackageElementId() {
        return importPackageElementId;
    }

    public void setImportPackageElementId(Integer importPackageElementId) {
        this.importPackageElementId = importPackageElementId;
    }

    public Integer getExportPackageInstructionsId() {
        return exportPackageInstructionsId;
    }

    public void setExportPackageInstructionsId(Integer exportPackageInstructionsId) {
        this.exportPackageInstructionsId = exportPackageInstructionsId;
    }
}