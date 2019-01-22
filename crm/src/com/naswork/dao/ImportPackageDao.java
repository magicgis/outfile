package com.naswork.dao;

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

public interface ImportPackageDao {
    int deleteByPrimaryKey(Integer id);

    void insert(ImportPackage record);

    int insertSelective(ImportPackage record);

    ImportPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImportPackage record);

    void updateByPrimaryKey(ImportPackage record);
    
    List<ImportPackageListVo> findListDatePage(PageModel<ImportPackageListVo> page);
    
    List<StorageFlowVo> StorageFlowPage(PageModel<StorageFlowVo> page);
	
    
    Integer findmaxseq(ImportPackage importPackage);
    
    List<ImportPackage>  findStorageAlertOrder(ImportPackage importPackage);
    
    List<SupplierVo> findsupplier(Integer supplierid);
    
    List<ClientContact> findclient(Integer clientid);
    
    
    List<ImportPackageListVo> findImportPackageDate(String id);
    
    List<ImportPackageListVo> findOriginalNumber(Integer  originalNumber);
    
    int findMaxOriginalNumber(Integer onStart, Integer onEnd);
    
    public ImportPackage selectByImportPackageElementId(Integer importPakcageElementId);
    
    public void updateStatusByPrimaryKey(ImportPackage importPackage);
    
    public ImportPackage getFeeMessage(Integer importPackageId);
}