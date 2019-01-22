package com.naswork.dao;

import java.util.List;

import com.naswork.model.ImportPackagePaymentElementPrepare;
import com.naswork.vo.PageModel;

public interface ImportPackagePaymentElementPrepareDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ImportPackagePaymentElementPrepare record);

    int insertSelective(ImportPackagePaymentElementPrepare record);

    ImportPackagePaymentElementPrepare selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImportPackagePaymentElementPrepare record);

    int updateByPrimaryKey(ImportPackagePaymentElementPrepare record);
    
    public ImportPackagePaymentElementPrepare selectBySupplierIdAndSupplierElementId(ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare);
    
    public List<ImportPackagePaymentElementPrepare> selectBySupplierId(Integer importPackageId);
    
    public List<ImportPackagePaymentElementPrepare> selectByImportPackageId(Integer importPackageId);
    
    public Double selectPrepayRateById(Integer id);
    
    public List<ImportPackagePaymentElementPrepare> getImportElementByImportId(Integer importPackageId);
}