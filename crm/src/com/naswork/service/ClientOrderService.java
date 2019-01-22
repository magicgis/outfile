package com.naswork.service;

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

public interface ClientOrderService {
	void add(ClientOrder clientOrder);
	
	/*
     * 列表页面数据
     */
    public void listPage(PageModel<ClientOrderVo> page, String where,
			GridSort sort);
    
    public ClientOrder selectByPrimaryKey(Integer id);
    
    
    /**
     * 到期提醒
     * **/
    public void dueReminlistPage(PageModel<ClientOrderVo> page, String where,
			GridSort sort);
    
    /*
     * 根据id查询
     */
    public ClientOrderVo findById(Integer id);
    
    /*
     * 更新
     */
    public void updateByPrimaryKeySelective(ClientOrder clientOrder);
    
    /*
     * 根据单号找Id
     */
    public Integer findIdByOrderNumber(String orderNumber);
    
    /*
     * excel上传
     */
    /*public MessageVo UploadExcel(MultipartFile multipartFile, Integer id);*/
    
    /**
     * 根据订单id和报价明细id查询
     * **/
    public ClientOrderElementVo findByCoIdAndCqeId(Integer coId,Integer cqeId);
    
    /**
     * 查询订单
     * **/
    ClientOrder lookOrderByImportPacksge(ClientQuote cq,Integer clientQuoteId,Integer currencyId,Double exchangeRate);
    
    /*
     * 查询未发货数量
     */
    public List<ClientOrderVo> getImportAmount(Integer orderId);
    
    /*
     * 获取订单Id
     */
    public List<ClientOrder> getOrderId(Integer clientId);
    
    /*
     * 客户询价单统计
     */
    public int statisticsPage(PageModel<StatisticsVo> page, String where,
			GridSort sort);
    
    /*
     *获取出库信息 
     */
    public ClientOrder getExportMessage(Integer clientOrderId);
    
    /**
     * 根据明细Id获取clientOrder
     */
    public ClientOrder getClientOrder(Integer clientOrderElementId);
    
    List<orderReview> orderReviewData(Integer clientOrderId);
    
    Integer selectSupplierId(Integer clientOrderElementId);
    
    public List<ClientOrder> selectByClientInquiryId(Integer clientInquiryId);
    
    public Double getOrderPrice(Integer clientQuoteId);
    
    public Double getIncomeTotalByOrderId(Integer id);
    
    public List<Date> getExportDates(Integer id);
    
}
