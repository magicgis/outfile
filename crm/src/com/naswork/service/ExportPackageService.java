package com.naswork.service;

import java.util.List;

import com.naswork.model.Client;
import com.naswork.model.ClientShip;
import com.naswork.model.ExportPackage;
import com.naswork.module.storage.controller.exportpackage.ExportPackageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ExportPackageService {

	 int insertSelective(ExportPackage record);

	 ExportPackage selectByPrimaryKey(Integer id);

	 int updateByPrimaryKeySelective(ExportPackage record);
	 
	 /*
	  * 列表
	  */
	 public void listPage(PageModel<ExportPackageVo> page,String where,GridSort sort);
	 
	 ExportPackage findByCidAndexportDate(ExportPackageVo exportPackageVo);
	 
	 /*
	  * 根据出库指令查询
	  */
	 public Client getByExportPackageInstructionsNumber(String exportPackageInstructionsNumber);
	 
	 /*
      * 根据出库指令查询
	  */
     public Integer findByExportPackageInstructionsNumber(String exportPackageInstructionsNumber);
     
     public boolean createInvoice(ClientShip clientShip);
     
     public List<ExportPackage> getByIpeId(Integer importPackageElementId);
     
     public ExportPackage getToTalAmount(Integer exportPackageId);
	
}
