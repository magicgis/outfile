package com.naswork.dao;

import java.util.List;

import com.naswork.model.ImportPackagePayment;
import com.naswork.module.finance.controller.importpayment.SearchVo;
import com.naswork.vo.PageModel;

public interface ImportPackagePaymentDao {
    int deleteByPrimaryKey(Integer id);

    void insert(ImportPackagePayment record);

    int insertSelective(ImportPackagePayment record);

    ImportPackagePayment selectByPrimaryKey(Integer id);
    
	ImportPackagePayment findBySupplierOrderId(Integer supplierOrderId);
	
	ImportPackagePayment findBypaymentNumber(String paymentNumber);

    int updateByPrimaryKeySelective(ImportPackagePayment record);

    int updateByPrimaryKey(ImportPackagePayment record);
    
    public List<ImportPackagePayment> findBySupplierIdPage(PageModel<ImportPackagePayment> page);
    
    public int getCountByImportPackageId(Integer importPackageId);
    
    public List<ImportPackagePayment> listPage(PageModel<ImportPackagePayment> page);
    
    public ImportPackagePayment listById(Integer importPackagePaymentId);
    
    public List<SearchVo> selectByImportNumber(PageModel<SearchVo> page);
    
    public List<SearchVo> selectByOrderNumber(PageModel<SearchVo> page);
    
    public int getCountBySupplierOrderId(Integer supplierOrderId);
    
    public List<ImportPackagePayment> selectBySupplierOrderId(Integer supplierOrderId);
    
    public List<SearchVo> getShouldPrepaymentOrderPage(PageModel<SearchVo> page);
    
    public List<SearchVo> getShouldShipPaymentOrderPage(PageModel<SearchVo> page);
    
    public List<SearchVo> getShouldReceivePaymentOrderPage(PageModel<SearchVo> page);
    
    Integer findSpztById(Integer id);
}