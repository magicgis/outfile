package com.naswork.service;

import java.util.List;

import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackagePaymentElementPrepare;
import com.naswork.vo.PageModel;

public interface ImportPackagePaymentElementPrepareService {

	public void insertSelective(ImportPackagePaymentElementPrepare record);

	public ImportPackagePaymentElementPrepare selectByPrimaryKey(Integer id);

	public int updateByPrimaryKeySelective(ImportPackagePaymentElementPrepare record);
	
	public void add(ImportPackagePaymentElementPrepare record);
	
	public List<ImportPackagePaymentElementPrepare> selectBySupplierId(Integer supplierId);
	
    public List<ImportPackagePaymentElementPrepare> selectByImportPackageId(Integer importPackageId);
    
    public Double selectPrepayRateById(Integer id);
    
    /**
     * 检查预付100%订单的单货情况
     * @param importPackageId
     * @param userId
     */
    public boolean checkImportAmount(Integer importPackageId,Integer userId);
    
    /**
     * 到期到期信息
     * @return
     */
    public void checkOverLeadTime();
    
    /**
     * 通知采购财务入库情况
     * @param importPackage
     * @param userId
     */
    public boolean importCondition(ImportPackage importPackage,Integer userId);
	
}
