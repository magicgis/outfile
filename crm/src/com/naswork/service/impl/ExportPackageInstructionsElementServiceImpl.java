package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ExportPackageInstructionsElementDao;
import com.naswork.model.ExportPackageInstructionsElement;
import com.naswork.service.ExportPackageInstructionsElementService;
@Service("exportPackageInstructionsElementServiceImpl")
public class ExportPackageInstructionsElementServiceImpl implements ExportPackageInstructionsElementService {

	@Resource
	private ExportPackageInstructionsElementDao exportPackageInstructionsElementDao;
	@Override
	public void deleteByPrimaryKey(Integer id) {
		exportPackageInstructionsElementDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insert(ExportPackageInstructionsElement record) {
		exportPackageInstructionsElementDao.insert(record);
	}

	@Override
	public void insertSelective(ExportPackageInstructionsElement record) {
		exportPackageInstructionsElementDao.insertSelective(record);
	}

	@Override
	public ExportPackageInstructionsElement selectByPrimaryKey(Integer id) {
		return exportPackageInstructionsElementDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(ExportPackageInstructionsElement record) {
		exportPackageInstructionsElementDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void updateByPrimaryKey(ExportPackageInstructionsElement record) {
		exportPackageInstructionsElementDao.updateByPrimaryKey(record);
	}

	@Override
	public ExportPackageInstructionsElement findElement(ExportPackageInstructionsElement record) {
		return exportPackageInstructionsElementDao.findElement(record);
	}
	
	public ExportPackageInstructionsElement selectByImportElementId(Integer importPackageElementId){
		return exportPackageInstructionsElementDao.selectByImportElementId(importPackageElementId);
	}
	
	public void updateExportStatus(Integer importPackageElementId,Integer exportPackageInstructionsId){
		exportPackageInstructionsElementDao.updateExportStatus(importPackageElementId, exportPackageInstructionsId);
	}
	
	public List<Integer> getImportElementId(Integer id){
		return exportPackageInstructionsElementDao.getImportElementId(id);
	}

}
