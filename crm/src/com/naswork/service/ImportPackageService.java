package com.naswork.service;

import java.util.Date;
import java.util.List;

import com.naswork.model.ClientContact;
import com.naswork.model.ExportPackage;
import com.naswork.model.ImportPackage;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.importpackage.SupplierVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ImportPackageService {
	void findListDatePage(PageModel<ImportPackageListVo> page, String searchString, GridSort sort);

	 void updateByPrimaryKey(ImportPackage record);
	 
	 Integer findmaxseq(ImportPackage importPackage);
	 
	 List<SupplierVo> findsupplier(Integer supplierid);
	 
	 List<ClientContact> findclient(Integer clientid);
	 
	 public Integer getMaxSeq(ExportPackage exportPackage);
	 
	 void insert(ImportPackage record);
	 
	 List<ImportPackageListVo> findImportPackageDate(String  id);
	 
	 List<ImportPackageListVo> findOriginalNumber(Integer  originalNumber);
	 
	 int getOriginalNumber(Date importDate);
	 
	  int getOriginalNumberPrefix(Date importDate);
	  
	  ImportPackage selectByPrimaryKey(Integer id);
	 
	 public void updateStatusByPrimaryKey(ImportPackage importPackage);
	 
	 public ImportPackage getFeeMessage(Integer importPackageId);
}
