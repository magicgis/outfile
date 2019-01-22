package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.SupplierOrder;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.module.purchase.controller.supplierorder.Address;
import com.naswork.module.purchase.controller.supplierorder.StorageOrderVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.vo.PageModel;

public interface SupplierOrderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierOrder record);

    int insertSelective(SupplierOrder record);

    SupplierOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierOrder record);

    int updateByPrimaryKey(SupplierOrder record);
    
    /*
     * 供应商订单管理
     */
    public List<SupplierOrderManageVo> SupplierOrderManagePage(PageModel<SupplierOrderManageVo> page);
    
    /*
     * 根据ID查询
     */
    public SupplierOrderManageVo findById(Integer id);
    
    /*
     * 根据客户订单ID查询
     */
    public SupplierOrder findByClientOrderId(SupplierOrder supplierOrder);
    
    /**
     * 到期提醒
     * **/
    public List<SupplierOrderManageVo>  dueRemindSupplierOrderManagePage(PageModel<SupplierOrderManageVo> page);

    /**
     * 查询供应商订单
     * **/
    List<SupplierOrder> findSupplierOrder(Integer clientOrderId,Integer supplierid,Integer orderstatusid);
    
    /**
     * 未下订单
     */
    public List<ClientOrder> NoOrderPage(PageModel<ClientOrder> page);
    
    /**
     * 未订货明细
     */
    public List<ClientOrderElement> NoOrderELementPage(PageModel<ClientOrderElement> page);
    
    public List<ClientOrderElement> NoOrderELementDataPage(PageModel<ClientOrderElement> page);

    
    List<SupplierInquiryStatistic> supplierOrderStat(SupplierInquiryStatistic supplierInquiryStatistic);
    
    public StorageOrderVo getIds(Integer clientOrderId);
    
    public SupplierOrderManageVo getPaymentPercent(Integer supplierOrderId);
    
    public SupplierOrder selectBySupplierOrderElementId(Integer supplierOrderElementId);
    
    /**
     * 查询付款未到货订单
     * @param ImportPackagePaymentId
     * @return
     */
    public List<SupplierOrder> getIdByLeadTime(Integer userId);
    
    /**
     * 获取email正文内容
     * @param supplierOrderId
     * @return
     */
    public List<SupplierOrder> getEmailTextList(Integer supplierOrderId);
    
    public Address getAddress(Integer id);
    
    public Integer getDestination(Integer id);
    
    public SupplierOrder selectByOrderNumber(String orderNumber);
}