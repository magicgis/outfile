package com.naswork.service;

import com.naswork.model.Ask4leave;
import com.naswork.model.ImportPackagePayment;
import com.naswork.module.finance.controller.importpayment.SearchVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ImportPackagePaymentService {
	
	void insert(ImportPackagePayment record);
	
	public int insertSelective(ImportPackagePayment record);

	public ImportPackagePayment selectByPrimaryKey(Integer id);
	
	ImportPackagePayment findBySupplierOrderId(Integer supplierOrderId);
	
	ImportPackagePayment findBypaymentNumber(String paymentNumber);

	public int updateByPrimaryKeySelective(ImportPackagePayment record);
	
    public void add(ImportPackagePayment record);
	
	public void findByImportPackageIdPage(PageModel<ImportPackagePayment> page);
	
	public void listPage(PageModel<ImportPackagePayment> page, String where,
			GridSort sort);
	
	public void selectByImportNumber(PageModel<SearchVo> page,String where);
	
	public void selectByOrderNumber(PageModel<SearchVo> page,String where);
	
	public void addBySearch(ImportPackagePayment payment);
	
	public void paymentRequest(ImportPackagePayment payment,String ids);
	
	public Object noPass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
	
	public Object pass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
	
	public Object sendEmail(String businessKey,String dbids);
	
	ImportPackagePayment listById(Integer importPackagePaymentId);
	
	public void getShouldPrepaymentOrder(PageModel<SearchVo> page,String where);
	
	public void getShouldShipPaymentOrderPage(PageModel<SearchVo> page,String where);
	
	public void getShouldReceivePaymentOrderPage(PageModel<SearchVo> page,String where);
}
