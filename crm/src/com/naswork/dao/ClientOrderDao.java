package com.naswork.dao;

import java.util.Date;
import java.util.List;

import com.naswork.model.ClientOrder;
import com.naswork.model.ClientQuote;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientorder.orderReview;
import com.naswork.module.statistics.controller.StatisticsVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientOrderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientOrder record);

    int insertSelective(ClientOrder record);

    ClientOrder selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(ClientOrder record);

    int updateByPrimaryKey(ClientOrder record);
    
    /*
     * 列表页面数据
     */
    public List<ClientOrderVo> listPage(PageModel<ClientOrderVo> page);
    
    /*
     * 根据id查询
     */
    public ClientOrderVo findById(Integer id);
    
    /*
     * 根据单号找Id
     */
    public Integer findIdByOrderNumber(String orderNumber);
    
    /**
     * 根据订单id和报价明细id查询
     * **/
    public ClientOrderElementVo findByCoIdAndCqeId(Integer coId,Integer cqeId);
    
    /**
     * 到期提醒
     * **/
    public List<ClientOrderVo> dueReminlistPage(PageModel<ClientOrderVo> page);
    
    /**
     * 查询订单
     * **/
    ClientOrder findOrderByImportPacksge(Integer clientQuoteId);
    
    /*
     * 查询客户订单明细数量
     */
    public List<ClientOrderVo> getOrderAmount(Integer orderId);
    
    /*
     * 查询入库明细数量
     */
    public List<ClientOrderVo> getImportAmount(Integer orderId);
    
    /*
     * 获取订单Id
     */
    public List<ClientOrder> getOrderId(Integer clientId);
    
    /*
     * 客户询价单统计
     */
    public List<StatisticsVo> statistics(PageModel<StatisticsVo> page);
    
    /*
     * 客户报价单统计
     */
    public List<StatisticsVo> getQuote();
    
    /*
     * 客户订单统计
     */
    public List<StatisticsVo> getOrder();
    
    /*
     * 出库统计
     */
    public List<StatisticsVo> getExport();
    
    /*
     *获取出库信息 
     */
    public ClientOrder getExportMessage(Integer clientOrderId);
    
    /*
     *获取订单金额 
     */
    public Double getOrderTotalPrice(Integer clientOrderId);
    
    /**
     * 根据明细Id获取clientOrder
     */
    public ClientOrder getClientOrder(Integer clientOrderElementId);
    
    List<orderReview> orderReviewData(Integer clientOrderId);
    
    Integer selectSupplierId(Integer clientOrderElementId);
    
    public List<ClientOrder> selectByClientInquiryId(Integer clientInquiryId);
    
    List<ClientOrderElementVo> findSupplierId(Integer ClientOrderElementId);
    
    public Double getOrderPrice(Integer clientQuoteId);
    
    ClientOrder findClientWeatherOrderId(Integer clientWeatherOrderId);
    
    public Double getIncomeTotalByOrderId(Integer id);
    
    public List<Date> getExportDates(Integer id);
    
    public ClientOrder selectByOrderNumber(String orderNumber);
    
}