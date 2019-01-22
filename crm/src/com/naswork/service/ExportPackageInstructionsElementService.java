package com.naswork.service;

import java.util.List;

import com.naswork.model.ExportPackageInstructionsElement;

public interface ExportPackageInstructionsElementService {
	void deleteByPrimaryKey(Integer id);

	void insert(ExportPackageInstructionsElement record);

	void insertSelective(ExportPackageInstructionsElement record);

    ExportPackageInstructionsElement selectByPrimaryKey(Integer id);
    
    ExportPackageInstructionsElement findElement(ExportPackageInstructionsElement record);

    void updateByPrimaryKeySelective(ExportPackageInstructionsElement record);

    void updateByPrimaryKey(ExportPackageInstructionsElement record);
    
    public ExportPackageInstructionsElement selectByImportElementId(Integer importPackageElementId);
    
    public void updateExportStatus(Integer importPackageElementId,Integer exportPackageInstructionsId);
    
    public List<Integer> getImportElementId(Integer id);
}
