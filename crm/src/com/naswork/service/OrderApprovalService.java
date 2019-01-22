package com.naswork.service;

import java.util.List;

import com.naswork.model.OrderApproval;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.task.controller.orderapproval.OrderApprovalVo;
import com.naswork.vo.PageModel;

public interface OrderApprovalService {
	   int deleteByPrimaryKey(Integer id);

	    int insert(OrderApproval record);

	    int insertSelective(OrderApproval record);

	    OrderApproval selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(OrderApproval record);

	    int updateByPrimaryKey(OrderApproval record);
	    
	   void selectByClientOrderIdPage(PageModel<OrderApprovalVo> page);
	   
	   void supplierWeayherOrderPage(PageModel<OrderApprovalVo> page);
	   
	   void updateClientOrderPage(PageModel<OrderApprovalVo> page);
	   
	   void finalOrderPricePage(PageModel<OrderApprovalVo> page);
	   
	   void  clientOrderELmentFinalPage(PageModel<OrderApprovalVo> page);
	   
	   List<Integer> findTaskId(Integer clientOrderElementId);
	   
	   Double  findStorageAmount(Integer clientOrderElementId,Integer state);
	   
		public Object quotepass(String businessKey,
				String taskId, String outcome, String assignee, String comment) throws Exception;
		
		public Object pass(String businessKey,
				String taskId, String outcome, String assignee, String comment) throws Exception;
		
		public Object noPass(String businessKey,
				String taskId, String outcome, String assignee, String comment) throws Exception;
		
		public Object financeNoPass(String businessKey,
				String taskId, String outcome, String assignee, String comment) throws Exception;
		
		 List<OrderApproval> selectByCoeIdAndState(Integer clientQuoteElementId,Integer state,Integer type);
		
		 List<OrderApproval> selectByIdAndState(Integer id,Integer state);
		 
		 List<OrderApproval> selectByCoeId(Integer clientQuoteElementId);
		 
		 List<OrderApproval> orderApprovalUseOnpassStorage(Integer supplierOrderElementId);
		 
		 public Double getUsedStorageAmount(Integer suppleirQuoteElementId,Integer importPackageElementId,Integer id);
		 
		 public List<Integer> supplierWeayherOrder(PageModel<OrderApprovalVo> page);
}
