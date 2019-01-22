package com.naswork.dao;

import java.util.List;

import com.naswork.model.OrderApproval;
import com.naswork.module.task.controller.orderapproval.OrderApprovalVo;
import com.naswork.vo.PageModel;

public interface OrderApprovalDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderApproval record);

    int insertSelective(OrderApproval record);

    OrderApproval selectByPrimaryKey(Integer id);
    
    List<OrderApproval> selectByIdAndState(Integer id,Integer state);

    int updateByPrimaryKeySelective(OrderApproval record);
    
    void updateByClientOrderElementId(OrderApproval record);
    
    void  updateByClientOrderElementIdAndType(OrderApproval record);

    int updateByPrimaryKey(OrderApproval record);
    
    List<OrderApprovalVo> selectByClientOrderIdPage(PageModel<OrderApprovalVo> page);
    
    List<OrderApprovalVo> supplierWeayherOrderPage(PageModel<OrderApprovalVo> page);
    
    List<Integer> supplierWeayherOrder(PageModel<OrderApprovalVo> page);
    
    List<OrderApprovalVo> updateClientOrderPage(PageModel<OrderApprovalVo> page);
    
    List<OrderApprovalVo>   clientOrderELmentFinalPage(PageModel<OrderApprovalVo> page);
    
    List<OrderApprovalVo>   finalOrderPricePage(PageModel<OrderApprovalVo> page);
    
    List<OrderApproval> selectByCoeIdAndState(Integer clientQuoteElementId,Integer state,Integer type);
    
    Double  findStorageAmount(Integer clientOrderElementId,Integer state);
    
    List<OrderApprovalVo>  findStateByClientOrderId(Integer clientOrderId);
    
    List<OrderApproval> selectByClientOrderElementId(Integer clientQuoteElementId);
    
    List<OrderApproval> selectByCoeId(Integer clientQuoteElementId);
    
    List<Integer> findTaskId(Integer clientOrderElementId);
    
    Double findUseAmount(Integer clientOrderElementId);
    
    List<OrderApprovalVo> notUse(Integer clientOrderElementId);
    
    Double useStorageAmout(Integer supplierQuoteElementId,Integer importPackageElementId);
    
    Double useOnpassStorageAmout(Integer supplierOrderElementId);
    
    List<OrderApprovalVo>  syncFinishData(Integer elementId,Integer clientOrderId);
    
    List<OrderApproval> orderApprovalUseOnpassStorage(Integer supplierOrderElementId);
    
    List<OrderApproval> OnpassStorageAlterStorage(Integer supplierOrderElementId);
    
    public Double getUsedStorageAmount(Integer suppleirQuoteElementId,Integer importPackageElementId,Integer id);
}

