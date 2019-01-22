package com.naswork.service;

import java.util.List;

import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.vo.PageModel;

public interface ImportPackagePaymentElementService {

	int insertSelective(ImportPackagePaymentElement record);

    ImportPackagePaymentElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImportPackagePaymentElement record);
    
    void updateBySupplierOrderElementId(ImportPackagePaymentElement record);
    
    public void listPage(PageModel<ImportPackagePaymentElement> page);
    
    void tasklistPage(PageModel<ImportPackagePaymentElement> page);
    
    public void addPaymentElemnt(List<ImportPackagePaymentElement> list,Integer importPackagePaymentId);
    
    public void edit(ImportPackagePaymentElement importPackagePaymentElement);
    
    public List<ImportPackagePaymentElement> elementList(Integer importPackagePaymentId);
    
    public void addPaymentElemntTotal(List<ImportPackagePaymentElement> list);
    
    public List<ImportPackagePaymentElement> selectBySupplierOrderElementId(Integer supplierOrderElementId);
    
    ImportPackagePaymentElement  elementData(Integer importPackagePaymentElementId);
	
}
