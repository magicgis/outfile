package com.naswork.service;

import java.util.List;
import java.util.Map;

import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.SupplierOrder;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.Address;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.StorageOrderVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierOrderService {

	int insertSelective(SupplierOrder record);
	
	public SupplierOrder selectByPrimaryKey(Integer id);
	
	void updateByPrimaryKeySelective(SupplierOrder record);
	
    /*
     * 新增供应商订单明细列表
     */
    /*public void AddSupplierOrderPage(PageModel<AddSupplierOrderElementVo> page);*/
    
    /*
     * 新增供应商订单明细列表-客户订单模块
     */
    public void ClientOrderPage(PageModel<ClientOrderElementVo> page);
    
    List<ClientOrderElementVo>  ClientOrderData(ClientQuote clientQuote);
    
    List<ClientOrderElementVo>  CompletedSupplierWeatherOrderData(Integer clientOrderId);
    
    public List<Map<String, Object>> AddSupplierOrderPage(Integer id,String userId);
    
    /*
     * 供应商列表
     */
    public void SupplierListPage(PageModel<SupplierListVo> page);
    
    List<SupplierListVo> SupplierListData(Integer sqeElementId,Integer cieElementId,Integer ciieElementId);
    
    /*
     * 获取供应商数据
     */
    public List<SupplierListVo> SupplierListMessage(PageModel<SupplierListVo> page);
	
    /*
     * 获取elementId
     */
    public Integer getElementId(Integer clientQuoteElementId);
    
    public Integer getSqeElementId(Integer clientQuoteElementId);
    
    /*
     * 获取客户订单信息
     */
    public ClientOrderElementVo findById(Integer id);
    
    
    /*
     * 供应商订单管理
     */
    public void SupplierOrderManagePage(PageModel<SupplierOrderManageVo> page,String where,GridSort sort);

    /**
     * 到期提醒
     * **/
    public void  dueRemindSupplierOrderManagePage(PageModel<SupplierOrderManageVo> page,String where,GridSort sort);
    
    /*
     * 供应商管理明细
     */
    public void SupplierOrderElement(PageModel<AddSupplierOrderElementVo> page);
    
    public void SupplierOrderElementForSelect(PageModel<AddSupplierOrderElementVo> page);
    
    /*
     * 根据id查订单
     */
    public SupplierOrderManageVo findMessageById(Integer id);
    
    /*
     * 根据ID查询
     */
    public SupplierOrderManageVo findByOrderId(Integer id);
    
    /*
     * 根据客户订单ID查询
     */
    public SupplierOrder findByClientOrderId(SupplierOrder supplierOrder);
    
    /*
     * 根据订单ID查询
     */
    public List<AddSupplierOrderElementVo> findBySupplierOrderId(PageModel<AddSupplierOrderElementVo> page);
    
    public List<AddSupplierOrderElementVo> Elements(Integer id);
    
    /*
     * 未完成工作
     */
    public void unFinishWorkPage(PageModel<AddSupplierOrderElementVo> page,String where,GridSort sort);

    /**
     * 查询供应商订单
     * **/
    SupplierOrder lookSupplierOrder(ClientOrder clientOrder,Integer clientOrderId,Integer supplierid,String supplierCode,Integer currencyId,Double exchangeRate);
    
    public double getMinPrice(PageModel<SupplierListVo> page);
    
    /**
     * 未下订单
     */
    public void NoOrderPage(PageModel<ClientOrder> page,String where,GridSort sort);
    
    /**
     * 未订货明细
     */
    public void NoOrderELementPage(PageModel<ClientOrderElement> page);
    
    public void NoOrderELementDataPage(PageModel<ClientOrderElement> page);
    
    List<SupplierInquiryStatistic> supplierOrderStat(SupplierInquiryStatistic supplierInquiryStatistic);
    
    /**
     * 新增库存订单
     */
    public void addStorageOrder(Integer userId,Integer supplierId,Integer repair);
    
    /**
     * 查询各个单的ID
     */
    public StorageOrderVo getIds(Integer clientOrderId);
    
    ClientOrderElementVo findByClientOrderElementId(Integer clientOrderElementId);
    
    public SupplierOrderManageVo getPaymentPercent(Integer supplierOrderId);
    
    public SupplierOrder selectBySupplierOrderElementId(Integer supplierOrderElementId);
    
    public Address getAddress(Integer id);
    
    public Integer getDestination(Integer id);
    
    public SupplierOrder selectByOrderNumber(String orderNumber);
}
