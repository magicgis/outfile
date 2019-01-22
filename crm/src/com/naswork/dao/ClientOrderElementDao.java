package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.vo.PageModel;

public interface ClientOrderElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientOrderElement record);

    public int insertSelective(ClientOrderElement record);

    ClientOrderElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientOrderElement record);

    int updateByPrimaryKey(ClientOrderElement record);
    
   List<ClientOrderElementVo> getCount(Integer clientOrderId);
   
   List<ClientOrderElementVo> findByClientOrderElementId(Integer clientOrderElementId);
    
    /*
     * 列表数据
     */
    public List<ClientOrderElementVo> listPage(PageModel<ClientOrderElementVo> page);
    
    public List<ClientOrderElementVo> list(Integer id);
    
    /*
     * 新增页面显示数据
     */
    public ClientOrderElementVo findByClientQuoteElementId(Integer id);
    
    /*
     * 获取询价明细item
     */
    public List<ClientOrderElementVo> findItem(Integer id);
    
    /*
     * 未完成工作
     */
    public List<ClientOrderElementVo> unFinishPage(PageModel<ClientOrderElementVo> page);
    
    /*
     * 根据客户订单id查询
     */
    public List<ClientOrderElementVo> findByOrderIdPage(PageModel<ClientOrderElementVo> page);
    
    /*
     * 根据客户订单id查询
     */
    public List<ClientOrderElementVo> findByOrderId(Integer id);
    
    ClientOrderElementVo findByclientOrderELementId(Integer id);
    
    /*
     *货款到期提醒
     */
    public List<ClientOrderElementVo> getDeadLineOrderPage(PageModel<ClientOrderElementVo> page);
    
    /*
     * 根据出库明细ID查询
     */
    public ClientOrderElement findByExportPackageElementId(Integer exportPackageElementId);
    
    /*
     * 根据付款明细ID查询 
     */
    public ClientOrderElement findByIncomeDetailId(Integer incomeDetailId);
    
    Integer findByOrderNumberAndItem(String orderNumber,String item);
    
    List<ClientOrderElement> findSpztByClientOrderId(ClientOrderElement record);
    public void removeByClientOrderId(Integer clientOrderId);
    
    public Double getTotalAmount(Integer clientOrderId);
    
    String findBsnByCoeId(Integer id);
    
    List<ClientOrderElement> selectByForeignKey(Integer clientOrderId);
    
    Integer findClientIdByCoeId(Integer clientOrderElement);
    
    
    List<Integer>  findUser(Integer clientOrderElementId);
    
    List<ClientOrderElementVo> clientOrderData(Integer clientOrderId);
    
    public List<ClientOrderElement> selectByCLientOrderId(Integer clientOrderId);
    
    Double sumPrice(Integer clientWeatherOrderId);
    
    void updateBybankCharges(ClientOrderElement clientOrderElement);
    
    public List<ClientOrderElementVo> getUnfinishOrderPage(PageModel<ClientOrderElementVo> page);
    
    public Integer getTotalCount(Integer clientOrderId);
    
    public Integer getUnfinishCount(Integer clientOrderId);
    
    public List<String> getSupplierNames(Integer clientOrderElementId);
    
    public Double getTotalById(Integer id);
    
    public List<ClientOrderElement> checkOrderElement(ClientOrderElement clientOrderElement);
}