package com.naswork.service;

import java.util.List;

import com.naswork.model.ExportPackageInstructions;
import com.naswork.model.ImportPackageElement;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ExportPackageInstructionsService {
	  void deleteByPrimaryKey(Integer id);

	    void insert(ExportPackageInstructions record);

	    void insertSelective(ExportPackageInstructions record);

	    ExportPackageInstructions selectByPrimaryKey(Integer id);
	    
	    void listDataPage(PageModel<ExportPackageInstructions> page,String where,GridSort sort);
	    
	    void flowLlistDataPage(PageModel<ExportPackageInstructions> page,String where,GridSort sort);

	    void updateByPrimaryKeySelective(ExportPackageInstructions record);

	    void updateByPrimaryKey(ExportPackageInstructions record);
	    
	    ExportPackageInstructions findByNumber(String number);
	    
	    public List<ImportPackageElement> getByInstructionsId(Integer id);
}
