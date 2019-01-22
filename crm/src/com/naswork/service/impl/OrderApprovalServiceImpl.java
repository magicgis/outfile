package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientDao;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.model.Client;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.OrderApproval;
import com.naswork.module.marketing.controller.clientorder.orderReview;
import com.naswork.module.task.controller.orderapproval.OrderApprovalVo;
import com.naswork.service.FlowService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.OrderApprovalService;
import com.naswork.vo.PageModel;
@Service("orderApprovalService")
public class OrderApprovalServiceImpl implements OrderApprovalService {

	@Resource
	private OrderApprovalDao orderApprovalDao;
	@Resource
	private FlowService flowService;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private ClientDao clientDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ImportpackageElementService importpackageElementService;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return orderApprovalDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderApproval record) {
		return orderApprovalDao.insert(record);
	}

	@Override
	public int insertSelective(OrderApproval record) {
		return orderApprovalDao.insertSelective(record);
	}

	@Override
	public OrderApproval selectByPrimaryKey(Integer id) {
		return orderApprovalDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderApproval record) {
		return orderApprovalDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrderApproval record) {
		return orderApprovalDao.updateByPrimaryKey(record);
	}

	@Override
	public void selectByClientOrderIdPage(PageModel<OrderApprovalVo> page) {
		page.setEntities(orderApprovalDao.selectByClientOrderIdPage(page)); 
	}
	
	@Override
	public  List<OrderApproval> selectByCoeIdAndState(Integer clientQuoteElementId,Integer state,Integer type) {
		return orderApprovalDao.selectByCoeIdAndState(clientQuoteElementId,state,type);
	}

	
	
	/**
	 * 定义流程结束前事件
	 * @throws Exception 
	 * **/
	public Object quotepass(String businessKey,
			String taskId, String outcome, String assignee, String comment) throws Exception{
		Map<String, Object> variables = new HashMap<String, Object>();
//		String id=flowService.getIdFromBusinessKey(businessKey);
//		List<OrderApproval> orderApproval=orderApprovalDao.selectByIdAndState(Integer.parseInt(id),0);
//		if(orderApproval.size()>0){
//			throw new Exception("有报价超过有效期");
//		}else{
//			
//			List<orderReview> list=clientOrderDao.orderReviewData(Integer.parseInt(id));
//			Client  client= clientDao.selectByPrimaryKey(list.get(0).getClientId());
//			for (orderReview orderReview : list) {
//				Double orderPrice = orderReview.getOrderPrice();
//				Double quotePrice = orderReview.getQuotePrice();
//				Double freight=orderReview.getFreight();
//				Double fixedCost = orderReview.getFixedCost();
//				Double profitMargin=(orderPrice*(1-fixedCost/100)-(quotePrice+freight))/orderPrice*(1-fixedCost/100);
//				 if(profitMargin<client.getProfitMargin()){
//					 OrderApproval quoteapproval=orderApprovalDao.selectByCoeIdAndState(orderReview.getClientQuoteElementId(),0,0);
//					 if(null!=quoteapproval){
//						 OrderApproval profitapproval=orderApprovalDao.selectByCoeIdAndState(orderReview.getClientQuoteElementId(),0,1);
//						 if(null!=profitapproval){
//							 orderApprovalDao.deleteByPrimaryKey(profitapproval.getId());
//						 }
//						 continue;
//					 }else{
//					 OrderApproval approval=new OrderApproval();
//					 approval.setClientOrderId(Integer.parseInt(id));
//					 approval.setClientOrderElementId(orderReview.getClientOrderElementId());
//					 approval.setSupplierQuoteElementId(orderReview.getSupplierQuoteElementId());
//					 approval.setClientQuoteElementId(orderReview.getClientQuoteElementId());
//					 approval.setState(0);
//					 approval.setType(1);
//					 orderApprovalDao.insert(approval);
//					 }
//				}
//			}
//			List<OrderApproval> profitapproval=orderApprovalDao.selectByIdAndState(Integer.parseInt(id),0);
//			if(profitapproval.size()>0){
//				variables.put("to", "利润低于标准");
//			}else{
//				ClientOrder record=new ClientOrder();
//				record.setId(Integer.parseInt(id));
//				record.setSpzt(235);
//				clientOrderDao.updateByPrimaryKeySelective(record);
//				variables.put("to", "结束");
//			}
//		}
		return variables;
//	
	}
	
	/**
	 * 定义流程结束前事件
	 * @throws Exception 
	 * **/
	public Object pass(String businessKey,
			String taskId, String outcome, String assignee, String comment) throws Exception{
		Map<String, Object> variables = new HashMap<String, Object>();
		String id=flowService.getIdFromBusinessKey(businessKey);
		
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(taskId));
		
		orderApproval.setState(1);
		
		orderApprovalDao.updateByClientOrderElementIdAndType(orderApproval);
		
		ClientOrderElement record=new ClientOrderElement();
		record.setId(Integer.parseInt(id));
		record.setSpzt(235);
		clientOrderElementDao.updateByPrimaryKeySelective(record);
		ClientOrderElement clientOrderElement=new ClientOrderElement();
		clientOrderElement.setId(orderApproval.getClientOrderId());
		clientOrderElement.setSpzt(232);
		List<ClientOrderElement> list=clientOrderElementDao.findSpztByClientOrderId(clientOrderElement);
		if(list.size()==0){
			 List<OrderApproval> storageList=new ArrayList<OrderApproval>();
			 List<OrderApproval> onStorageList=new ArrayList<OrderApproval>();
			clientOrderElement=new ClientOrderElement();
			clientOrderElement.setId(orderApproval.getClientOrderId());
			List<ClientOrderElement> clientOrderElements=clientOrderElementDao.findSpztByClientOrderId(clientOrderElement);
			for (ClientOrderElement clientOrderElement2 : clientOrderElements) {
				 List<OrderApproval> approvals=orderApprovalDao.selectByClientOrderElementId(clientOrderElement2.getId());
				 OrderApproval approval=new OrderApproval();
				 approval.setClientOrderElementId(clientOrderElement2.getId());
				 String ipeIds="";
				 String soeIds="";
				 for (OrderApproval orderApproval2 : approvals) {
					if(orderApproval2.getType().equals(2)){
						ipeIds+=orderApproval2.getImportPackageElementId()+",";
					}else if(orderApproval2.getType().equals(3)){
						soeIds+=orderApproval2.getSupplierOrderElementId()+",";
					}
				}
				 if(ipeIds.equals("")){
					 approval.setSoeIds(soeIds);
					 onStorageList.add(approval);
				 }else{
				 approval.setIpeIds(ipeIds);
				 storageList.add(approval);
				 }
			}
//			importpackageElementService.splitOrder(storageList, onStorageList);
		}
			return variables;
	
	}
	
	/**
	 * 定义流程结束前事件
	 * @throws Exception 
	 * **/
	public Object financeNoPass(String businessKey,
			String taskId, String outcome, String assignee, String comment) throws Exception{
		Map<String, Object> variables = new HashMap<String, Object>();
		
			
			String id=flowService.getIdFromBusinessKey(businessKey);
			List<OrderApproval> list2= orderApprovalDao.selectByCoeIdAndState(Integer.parseInt(id), 0, 1);
			if(list2.size()==0){
				variables.put("to", "结束");
				ClientOrderElement record=new ClientOrderElement();
				record.setId(Integer.parseInt(id));
				record.setSpzt(235);
				clientOrderElementDao.updateByPrimaryKeySelective(record);
				OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(taskId));
				ClientOrderElement clientOrderElement=new ClientOrderElement();
				clientOrderElement.setId(orderApproval.getClientOrderId());
				clientOrderElement.setSpzt(232);
				List<ClientOrderElement> list=clientOrderElementDao.findSpztByClientOrderId(clientOrderElement);
				if(list.size()==0){
					 List<OrderApproval> storageList=new ArrayList<OrderApproval>();
					 List<OrderApproval> onStorageList=new ArrayList<OrderApproval>();
					clientOrderElement=new ClientOrderElement();
					clientOrderElement.setId(orderApproval.getClientOrderId());
					List<ClientOrderElement> clientOrderElements=clientOrderElementDao.findSpztByClientOrderId(clientOrderElement);
					for (ClientOrderElement clientOrderElement2 : clientOrderElements) {
						 List<OrderApproval> approvals=orderApprovalDao.selectByClientOrderElementId(clientOrderElement2.getId());
						 OrderApproval approval=new OrderApproval();
						 approval.setClientOrderElementId(clientOrderElement2.getId());
						 String ipeIds="";
						 String soeIds="";
						 for (OrderApproval orderApproval2 : approvals) {
							if(orderApproval2.getType().equals(2)){
								ipeIds+=orderApproval2.getImportPackageElementId()+",";
							}else if(orderApproval2.getType().equals(3)){
								soeIds+=orderApproval2.getSupplierOrderElementId()+",";
							}
						}
						 if(ipeIds.equals("")){
							 approval.setSoeIds(soeIds);
							 onStorageList.add(approval);
						 }else{
						 approval.setIpeIds(ipeIds);
						 storageList.add(approval);
						 }
					}
//					importpackageElementService.splitOrder(storageList, onStorageList);
				}
				
			}else{
			variables.put("to", " ");
			}
		return variables;
	}
	
	/**
	 * 定义流程结束前事件
	 * @throws Exception 
	 * **/
	public Object noPass(String businessKey,
			String taskId, String outcome, String assignee, String comment) throws Exception{
		Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("to", "此项目作废");
			String id=flowService.getIdFromBusinessKey(businessKey);
			
			OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(taskId));
			
			orderApproval.setState(2);
			
			orderApprovalDao.updateByPrimaryKeySelective(orderApproval);

			ClientOrderElement record=new ClientOrderElement();
			record.setId(Integer.parseInt(id));
			record.setSpzt(233);
			clientOrderElementDao.updateByPrimaryKeySelective(record);
			ClientOrderElement clientOrderElement=new ClientOrderElement();
			clientOrderElement.setId(orderApproval.getClientOrderId());
			clientOrderElement.setSpzt(232);
			List<ClientOrderElement> list=clientOrderElementDao.findSpztByClientOrderId(clientOrderElement);
			if(list.size()==0){
				 List<OrderApproval> storageList=new ArrayList<OrderApproval>();
				 List<OrderApproval> onStorageList=new ArrayList<OrderApproval>();
				clientOrderElement=new ClientOrderElement();
				clientOrderElement.setId(orderApproval.getClientOrderId());
				List<ClientOrderElement> clientOrderElements=clientOrderElementDao.findSpztByClientOrderId(clientOrderElement);
				for (ClientOrderElement clientOrderElement2 : clientOrderElements) {
					 List<OrderApproval> approvals=orderApprovalDao.selectByClientOrderElementId(clientOrderElement2.getId());
					 OrderApproval approval=new OrderApproval();
					 approval.setClientOrderElementId(clientOrderElement2.getId());
					 String ipeIds="";
					 String soeIds="";
					 for (OrderApproval orderApproval2 : approvals) {
						if(orderApproval2.getType().equals(2)){
							ipeIds+=orderApproval2.getImportPackageElementId()+",";
						}else if(orderApproval2.getType().equals(3)){
							soeIds+=orderApproval2.getSupplierOrderElementId()+",";
						}
					}
					 if(ipeIds.equals("")){
						 approval.setSoeIds(soeIds);
						 onStorageList.add(approval);
					 }else{
					 approval.setIpeIds(ipeIds);
					 storageList.add(approval);
					 }
				}
//				importpackageElementService.splitOrder(storageList, onStorageList);
			}
			return variables;
	}

	@Override
	public List<OrderApproval> selectByIdAndState(Integer id, Integer state) {
		return orderApprovalDao.selectByIdAndState(id, state);
	}

	@Override
	public void supplierWeayherOrderPage(PageModel<OrderApprovalVo> page) {
		page.setEntities(orderApprovalDao.supplierWeayherOrderPage(page)); 		
	}

	@Override
	public void updateClientOrderPage(PageModel<OrderApprovalVo> page) {
		page.setEntities(orderApprovalDao.updateClientOrderPage(page)); 		
	}
	
	@Override
	public void clientOrderELmentFinalPage(PageModel<OrderApprovalVo> page) {
		page.setEntities(orderApprovalDao.clientOrderELmentFinalPage(page)); 			
	}

	@Override
	public List<OrderApproval> selectByCoeId(Integer clientQuoteElementId) {
		return orderApprovalDao.selectByCoeId(clientQuoteElementId);
	}

	@Override
	public List<Integer> findTaskId(Integer clientOrderElementId) {
		return orderApprovalDao.findTaskId(clientOrderElementId);
	}

	@Override
	public void finalOrderPricePage(PageModel<OrderApprovalVo> page) {
		page.setEntities(orderApprovalDao.finalOrderPricePage(page)); 	
		
	}

	@Override
	public Double findStorageAmount(Integer clientOrderElementId,Integer state) {
		return orderApprovalDao.findStorageAmount(clientOrderElementId,state );
	}

	@Override
	public List<OrderApproval> orderApprovalUseOnpassStorage(Integer supplierOrderElementId) {
		return orderApprovalDao.orderApprovalUseOnpassStorage(supplierOrderElementId);
	}

	public Double getUsedStorageAmount(Integer suppleirQuoteElementId,Integer importPackageElementId,Integer id){
		return orderApprovalDao.getUsedStorageAmount(suppleirQuoteElementId, importPackageElementId, id);
	}
	
	public List<Integer> supplierWeayherOrder(PageModel<OrderApprovalVo> page){
		return orderApprovalDao.supplierWeayherOrder(page);
	}

	
}
