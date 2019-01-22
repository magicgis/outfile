package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.SupplierOrderElement;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.vo.PageModel;

public interface SupplierOrderElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierOrderElement record);

    int insertSelective(SupplierOrderElement record);

    SupplierOrderElement selectByPrimaryKey(Integer id);
    
    SupplierOrderElement selectBySqeIdAndSoId(Integer supplierOrderId,Integer supplierQuoteElementId);

    int updateByPrimaryKeySelective(SupplierOrderElement record);

    int updateByPrimaryKey(SupplierOrderElement record);
    
    /*
     * 新增供应商订单明细列表-供应商订单模块
     */
    public List<AddSupplierOrderElementVo> AddSupplierOrderPage(PageModel<AddSupplierOrderElementVo> page);
    
    /*
     * 新增供应商订单明细列表-客户订单模块
     */
    public List<ClientOrderElementVo> ClientOrderPage(PageModel<ClientOrderElementVo> page);
    
    List<ClientOrderElementVo>  ClientOrderData(ClientQuote clientQuote);
    
    List<ClientOrderElementVo> CompletedSupplierWeatherOrderData(Integer clientOrderId);
    
    List<ClientOrderElementVo>  SupplierWeatherOrderData(ClientQuote clientQuote);
    
    /*
     * 供应商列表
     */
    public List<SupplierListVo> SupplierListPage(PageModel<SupplierListVo> page);
    
    List<SupplierListVo> SupplierListData(Integer sqeElementId,Integer cieElementId,Integer ciieElementId);
    
    /*
     * 获取客户询价elementId
     */
    public Integer getElementId(Integer clientQuoteElementId);
    
    /*
     * 获取供应商报价elementId
     */
    public Integer getSqeElementId(Integer clientQuoteElementId);
    
    /*
     * 获取客户订单信息
     */
    public ClientOrderElementVo findById(Integer id);
    
    /*
     * 供应商管理明细
     */
    public List<AddSupplierOrderElementVo> SupplierOrderElementPage(PageModel<AddSupplierOrderElementVo> page);
    
    public List<AddSupplierOrderElementVo> SupplierOrderElement(Integer id);
    
    /*
     * 根据elementId查询
     */
    public AddSupplierOrderElementVo findByElementId(Integer id);
    
    /*
     * 未完成工作
     */
    public List<AddSupplierOrderElementVo> unFinishWorkPage(PageModel<AddSupplierOrderElementVo> page);
    
    public double getMinPrice(PageModel<SupplierListVo> page);
    
    public Double getStorageAmount(String partNumber);
    
    List<SupplierOrderElement> findStorageByPn(String partNumber);
    
    public List<SupplierOrderElement> getSupplierElement(String awb);
    
    public List<SupplierOrderElement> getImportPreparePage(PageModel<SupplierOrderElement> page);
    
    ClientOrderElementVo findByClientOrderElementId(Integer clientOrderElementId);
    
    List<SupplierListVo> findByElementIds(Integer clientInquiryElementId,Integer supplierQuoteElementId);
    
    List<SupplierListVo>  findByElementIdsAndCoeId(Integer clientInquiryElementId,Integer supplierQuoteElementId,Integer clientOrderId);
    
    public Double getAmountBySupplierOrderId(Integer supplierOrderId);
    
    public List<SupplierOrderElement> selectBySupplierOrderId(Integer supplierOrderId);
    
    public SupplierOrderElement getPaymentMessage(Integer supplierOrderElementId);
    
    //Double getImportAmount(Integer id);
    
    Integer getSupplierOrderELementId(Integer id);
    
    public Integer getSupplierId(Integer supplierOrderElementId);
    
    public List<SupplierListVo> getOnPassagePartNumber(Integer clientInquiryElementId,Integer supplierQuoteElementId);
    
    String findBsnBySoeId(Integer id);
    
    public AddSupplierOrderElementVo getImportDateBySoeId(Integer supplierOrderElementId);  
    
    public Double getOtherOrderAmount(Integer clientOrderElementId,Integer supplierOrderElementId);
    
    public List<SupplierOrderElement> selectBySupplierOrderIdAll(Integer supplierOrderId);
    
    public SupplierOrderElement getOrdersAmount(Integer clientOrderElementId);
    
    public int getOrderCount(Integer supplierOrderId);
    
    public Double getAmountByCLientElementId(Integer clientOrderElementId);
    
    SupplierOrderElement findOrderPrice(Integer elementId);
    
    public List<String> getImportNumberById(Integer id);
    
    public Integer getShelfLifeBySoeId(String soeId);
    
    public Double getImportAmount(Integer id);
    
    public List<Integer> getShipByOrderNumber(String orderNumber);
    
    public List<SupplierOrderElement> selectByImportPackageId(Integer importPackageId);
    
    public Double getTotalByOrder(Integer supplierOrderId);
    
}