package com.naswork.dao;

import java.util.List;

import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.vo.PageModel;

public interface ImportPackagePaymentElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ImportPackagePaymentElement record);

    int insertSelective(ImportPackagePaymentElement record);

    ImportPackagePaymentElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImportPackagePaymentElement record);
    
    void updateBySupplierOrderElementId(ImportPackagePaymentElement record);

    int updateByPrimaryKey(ImportPackagePaymentElement record);
    
    public List<ImportPackagePaymentElement> listPage(PageModel<ImportPackagePaymentElement> page);
    
    List<ImportPackagePaymentElement> tasklistPage(PageModel<ImportPackagePaymentElement> page);
    
    public List<ImportPackagePaymentElement> elementList(Integer importPackagePaymentId);
    
    ImportPackagePaymentElement  elementData(Integer importPackagePaymentElementId);
    
    public List<ImportPackagePaymentElement> selectBySupplierOrderElementId(Integer supplierOrderElementId);
    
    ImportPackagePaymentElement  selectByImportPackagePaymentId(Integer importPackagePaymentId);
    
    public List<ImportPackagePaymentElement> getPaymentRecord(Integer supplierOrderElementId);
}