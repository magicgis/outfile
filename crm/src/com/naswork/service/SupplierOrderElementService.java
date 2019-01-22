package com.naswork.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierOrderElementFj;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

public interface SupplierOrderElementService {

	void insertSelective(SupplierOrderElement record);
	
	 SupplierOrderElement selectBySqeIdAndSoId(Integer supplierOrderId,Integer supplierQuoteElementId);
	
	void updateByPrimaryKeySelective(SupplierOrderElement record);
	
	/*
     * 根据elementId查询
     */
    public AddSupplierOrderElementVo findByElementId(Integer id);
    
    /**
     * 获取库存数量
     * @param partNumber
     * @return
     */
    public Double getStorageAmount(String partNumber);
    
    /**
     * 获取供应商发货清单
     */
    public void getImportPreparePage(PageModel<SupplierOrderElement> page, String where,
			GridSort sort);
    
    /**
     * 搜索报价件号
     */
    public void listByPartNumberPage(PageModel<SupplierQuoteElement> page, String where,
			GridSort sort);
    
    /**
     * 搜索维修报价件号
     */
    public void repairListByPartNumberPage(PageModel<SupplierQuoteElement> page, String where,
			GridSort sort);
    
    /**
     * 增加库存明细
     */
    public void StorageOrderElement(SupplierQuoteElement supplierQuoteElement,Integer clientOrderId);
    
    SupplierOrderElement selectByPrimaryKey(Integer id);
    
    
    /**
     * 查询付款记录
     */
    public SupplierOrderElement getPaymentMessage(Integer supplierOrderElementId);
    
    Double getImportAmount(Integer id);
    
    Integer getSupplierOrderELementId(Integer id);
    
    /**
     * 获取供应商ID
     */
    public Integer getSupplierId(Integer supplierOrderElementId);
    
    public MessageVo uploadExcel(MultipartFile multipartFile, String clientinquiryquotenumber);
    /**
     * 根据订单ID获取明细
     */
    public List<SupplierOrderElement> selectBySupplierOrderId(Integer supplierOrderId);

	List<SupplierOrderElement> findStorageByPn(String quotePartNumber);

	Integer getElementId(Integer clientQuoteElementId);

	Integer getSqeElementId(Integer clientQuoteElementId);

	List<SupplierListVo> findByElementIdsAndCoeId(Integer cieElementId, Integer sqeElementId,
			Integer clientOrderElementId);

	com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo findByClientOrderElementId(Integer clientOrderElementId);

	List<SupplierListVo> getOnPassagePartNumber(Integer cieElementId, Integer sqeElementId);
	
	String findBsnBySoeId(Integer id);
	
	public AddSupplierOrderElementVo getImportDateBySoeId(Integer supplierOrderElementId);
	
	public Double getOtherOrderAmount(Integer clientOrderElementId,Integer supplierOrderElementId);
	
	public SupplierOrderElement getOrdersAmount(Integer clientOrderElementId);
	
	public Double getAmountByCLientElementId(Integer clientOrderElementId);
	
	public List<String> getImportNumberById(Integer id);
	
	public Integer getShelfLifeBySoeId(String soeId);
	
	public List<SupplierOrderElementFj> getByOrderId(PageModel<String> page);
	
	public List<SupplierOrderElementFj> selectForeignKey(String ids);
	
	public List<Integer> getShipByOrderNumber(String orderNumber);
	
	public ResultVo checkAptitude(Integer id);
	
	public Double getTotalByOrder(Integer supplierOrderId);
}
