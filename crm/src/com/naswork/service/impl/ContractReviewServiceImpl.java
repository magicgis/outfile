package com.naswork.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.pdfbox.pdmodel.PageMode;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;

import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.OnPassageStorageDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.dao.StorageCorrelationDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.SupplierQuoteElementDao;
import com.naswork.dao.SupplierWeatherOrderElementDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.ExchangeRate;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.OrderApproval;
import com.naswork.model.StorageCorrelation;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierWeatherOrderElement;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.constants.WorkFlowConstants.ProcessKeys;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.service.ContractReviewService;
import com.naswork.service.FlowService;
import com.naswork.service.SupplierOrderService;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;
@Service("contractReviewService")
public class ContractReviewServiceImpl extends FlowServiceImpl implements ContractReviewService {
	@Resource
	private ImportPackageElementDao importpackageElementDao;
	@Resource
	private OnPassageStorageDao onPassageStroageDao;
	@Resource
	private OrderApprovalDao orderApprovalDao;
	@Resource
	private Jbpm4JbyjDao jbyjDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	@Resource
	private FlowService flowService;
	@Resource
	private SupplierWeatherOrderElementDao supplierWeatherOrderElementDao;
	@Resource
	private SupplierQuoteElementDao supplierQuoteElementDao;
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private StorageCorrelationDao storageCorrelationDao;
	
	@Override
	public boolean Stock(List<ImportPackageElementVo> elementVos, ClientWeatherOrderElement clientWeatherOrderElement ,List<StorageFlowVo> supplierList) {
		boolean use=false;
		 Double amount=clientWeatherOrderElement.getAmount();
		 Double fixedCost=clientWeatherOrderElement.getFixedCost();
		 Double orderPrice=clientWeatherOrderElement.getPrice();
		 if(fixedCost<1){
			 fixedCost=fixedCost*clientWeatherOrderElement.getPrice();
		 }
		 Double bankCharges=clientWeatherOrderElement.getBankCharges()*orderPrice;
		 List<StorageFlowVo>  benchStandardList=new ArrayList<StorageFlowVo>();
		 Double benchStandardAmount=0.0;
		 List<StorageFlowVo>  benchSubstandardList=new ArrayList<StorageFlowVo>();
		 List<StorageFlowVo> fullList =new ArrayList<StorageFlowVo>();
		for (ImportPackageElementVo importPackageElementVo : elementVos) {
			 List<StorageFlowVo> flowVos=importpackageElementDao.findStorageBySupplierQuoteElementId(importPackageElementVo.getSupplierQuoteElementId());
			 if(flowVos.size()>0){
				 for (StorageFlowVo storageFlowVo : flowVos) {
					 PageModel<StorageDetailVo> page = new PageModel<StorageDetailVo>();
					 page.put("importId", storageFlowVo.getImportPackageElementId());
					 page.put("importIdLike", "'%"+storageFlowVo.getImportPackageElementId()+"%'");
					 StorageCorrelation storageCorrelation = storageCorrelationDao.getList(page);
					 /*if (storageCorrelation != null) {
						 StringBuffer str = new StringBuffer();
			    		 str.append("vs.id in (");
			    		 str.append(storageCorrelation.getImportPackageElementId()).append(",");
			    		 String[] ids = storageCorrelation.getCorrelationImportPackageElementId().split(",");
			    		 for (int i = 0; i < ids.length; i++) {
							 str.append(ids[i]).append(",");
						 }
			    		 str.delete(str.length()-1, str.length());
			    		 str.append(")");
			    		 page.put("where", str.toString());
			    		 List<StorageFlowVo> flows = importpackageElementDao.getCorrelationList(page);
			    		 for (int i = 0; i < flows.size(); i++) {
			    			 flows.get(i).setCorrelation(true);
						 }
			    		 supplierList.addAll(flows);
					 }else {*/
					 Integer sCurrencyId=storageFlowVo.getCurrencyId();
					 Integer cCurrencyId=clientWeatherOrderElement.getCurrencyId();
					 Double storagePrice=storageFlowVo.getPrice();
					 if(!sCurrencyId.equals(cCurrencyId)){
						storagePrice=storageFlowVo.getPrice()*storageFlowVo.getExchangeRate()/clientWeatherOrderElement.getExchangeRate();
					 }
					 Double useamount=orderApprovalDao.useStorageAmout(storageFlowVo.getId(), storageFlowVo.getImportPackageElementId());
					 Double storageAmount=storageFlowVo.getStorageAmount()-useamount;
					 storageFlowVo.setStorageAmount(storageAmount);
					 if(storageAmount<=0){
						 continue;
					 }else if(amount<=storageAmount){
						 storageFlowVo.setStorageAmount(amount);
					 }
					 //有库存，数量和利润是否满足
					 Double profitMargin=((orderPrice-fixedCost-bankCharges)-storagePrice)/orderPrice;
					 BigDecimal pm=new BigDecimal(profitMargin);
					 profitMargin=pm.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
					 storageFlowVo.setProfitMargin(profitMargin);
					 if(storageAmount>=amount){
						 if(profitMargin>=clientWeatherOrderElement.getClientProfitMargin() && clientWeatherOrderElement.getUnit().trim().equals(storageFlowVo.getUnit().trim())){
							 fullList.clear();
							 fullList.add(storageFlowVo);
							 supplierList.addAll(fullList);
							 use=true;
							 return use;//数量利润满足直接跳出
						 }else{
							 if (storageCorrelation != null) {
								 StringBuffer str = new StringBuffer();
					    		 str.append("vs.id in (");
					    		 str.append(storageCorrelation.getImportPackageElementId()).append(",");
					    		 String[] ids = storageCorrelation.getCorrelationImportPackageElementId().split(",");
					    		 for (int i = 0; i < ids.length; i++) {
									 str.append(ids[i]).append(",");
								 }
					    		 str.delete(str.length()-1, str.length());
					    		 str.append(")");
					    		 page.put("where", str.toString());
					    		 List<StorageFlowVo> flows = importpackageElementDao.getCorrelationList(page);
					    		 for (int i = 0; i < flows.size(); i++) {
					    			 flows.get(i).setCorrelation(true);
								 }
					    		 supplierList.addAll(flows);
							 }else {
								 fullList.add(storageFlowVo);
							 }
						 }
					 }else{
						 if (storageCorrelation != null) {
							 StringBuffer str = new StringBuffer();
				    		 str.append("vs.id in (");
				    		 str.append(storageCorrelation.getImportPackageElementId()).append(",");
				    		 String[] ids = storageCorrelation.getCorrelationImportPackageElementId().split(",");
				    		 for (int i = 0; i < ids.length; i++) {
								 str.append(ids[i]).append(",");
							 }
				    		 str.delete(str.length()-1, str.length());
				    		 str.append(")");
				    		 page.put("where", str.toString());
				    		 List<StorageFlowVo> flows = importpackageElementDao.getCorrelationList(page);
				    		 for (int i = 0; i < flows.size(); i++) {
				    			 flows.get(i).setCorrelation(true);
							 }
				    		 supplierList.addAll(flows);
						 }else {
							 if(profitMargin>=clientWeatherOrderElement.getClientProfitMargin() && clientWeatherOrderElement.getUnit().trim().equals(storageFlowVo.getUnit().trim())){
								 benchStandardAmount+=storageAmount;
								 benchStandardList.add(storageFlowVo);
								 if(benchStandardAmount>=amount){
									 supplierList.addAll(benchStandardList);
									 use=true;
									 return use;//数量利润满足直接跳出
								 }
							 }else{
								 benchSubstandardList.add(storageFlowVo);
							 }
						 }
					 }
					 /*}*/
				}
			 }
		}
		//前面没有跳出的情况下，所有库存发起审批
			    supplierList.addAll(fullList);
			    benchStandardList.addAll(benchSubstandardList);
			    supplierList.addAll(benchStandardList);
		return use;
	}
	
	@Override
	public boolean onPassageStock(List<SupplierListVo> elementVos, ClientWeatherOrderElement clientWeatherOrderElement ,
			List<OnPassageStorage> supplierList) {
		boolean use=false;
		 Double amount=clientWeatherOrderElement.getAmount();
		 Double fixedCost=clientWeatherOrderElement.getFixedCost();
		 Double orderPrice=clientWeatherOrderElement.getPrice();
		 if(fixedCost<1){
			 fixedCost=fixedCost*clientWeatherOrderElement.getPrice();
		 }
		 Double bankCharges=clientWeatherOrderElement.getBankCharges()*orderPrice;
		 List<OnPassageStorage>  benchStandardList=new ArrayList<OnPassageStorage>();
		 Double benchStandardAmount=0.0;
		 List<OnPassageStorage>  benchSubstandardList=new ArrayList<OnPassageStorage>();
		 List<OnPassageStorage> fullList =new ArrayList<OnPassageStorage>();
		 for (SupplierListVo supplierListVo : elementVos) {
			 PageModel<OnPassageStorage> onPassagePage = new PageModel<OnPassageStorage>();
				onPassagePage.put("where", "sqe.id = "+supplierListVo.getId());
				List<OnPassageStorage> onPassageStroage = onPassageStroageDao.selectBySupplierQuoteElementId(onPassagePage);
				for (OnPassageStorage onPassageStorage : onPassageStroage) {
					Integer sCurrencyId=onPassageStorage.getCurrencyId();
					Integer cCurrencyId=clientWeatherOrderElement.getCurrencyId();
					Double storagePrice=onPassageStorage.getPrice();
					if(!sCurrencyId.equals(cCurrencyId)){
					  storagePrice=onPassageStorage.getPrice()*onPassageStorage.getExchangeRate()/clientWeatherOrderElement.getExchangeRate();
					}
					 Double useAmount=orderApprovalDao.useOnpassStorageAmout(onPassageStorage.getSupplierOrderElementId());
					 Double storageAmount=onPassageStorage.getAmount()-useAmount;
					 onPassageStorage.setAmount(storageAmount);
					 if(storageAmount<=0){
						 continue;
					 }else if(amount<=storageAmount){
						 onPassageStorage.setAmount(amount);
					 }
					 Double profitMargin=((orderPrice-fixedCost-bankCharges)-storagePrice)/orderPrice;
					 BigDecimal pm=new BigDecimal(profitMargin);
					  profitMargin=pm.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
					 onPassageStorage.setProfitMargin(profitMargin);
					 if(storageAmount>=amount){ //有库存，数量和利润是否满足
						 if(profitMargin>=clientWeatherOrderElement.getClientProfitMargin() && clientWeatherOrderElement.getUnit().equals(onPassageStorage.getUnit())){
							 fullList.clear();
							 fullList.add(onPassageStorage);
							 supplierList.addAll(fullList);
							 use=true;
							 return use;//数量利润满足直接跳出
						 }else{
							 fullList.add(onPassageStorage);
						 }
					 }else{
						 if(profitMargin>=clientWeatherOrderElement.getClientProfitMargin() && clientWeatherOrderElement.getUnit().equals(onPassageStorage.getUnit())){
							 benchStandardAmount+=storageAmount;
							 benchStandardList.add(onPassageStorage);
							 if(benchStandardAmount>=amount){
								 supplierList.addAll(benchStandardList);
								 use=true;
								 return use;//数量利润满足直接跳出
							 }
						 }else{
							 benchSubstandardList.add(onPassageStorage);
						 }
					 }
				}
		}
		//前面没有跳出的情况下，所有库存发起审批
		    supplierList.addAll(fullList);
		    benchStandardList.addAll(benchSubstandardList);
		    supplierList.addAll(benchStandardList);
		return use;
	}
	
	
	@Override
	public void orderApproval(ClientWeatherOrderElement clientWeatherOrderElement, String ids,OrderApproval other) {
	   UserVo user= ContextHolder.getCurrentUser();
	   List<OrderApproval> orderApprovalList=new ArrayList<OrderApproval>();
	   List<OrderApproval> noPassStock=orderApprovalDao.selectByCoeIdAndState(clientWeatherOrderElement.getId(), 0, 0);//利润不通过的库存
	   List<OrderApproval> passStock=orderApprovalDao.selectByCoeIdAndState(clientWeatherOrderElement.getId(), 1, 0);//利润通过的库存
	   Double storageAmount=orderApprovalDao.findStorageAmount(clientWeatherOrderElement.getId(),0);//利润通过库存总数量
	   List<OrderApproval> onpassnoPassStock=orderApprovalDao.selectByCoeIdAndState(clientWeatherOrderElement.getId(), 0, 1);//利润不通过的在途库存
	   List<OrderApproval> onpasspassStock=orderApprovalDao.selectByCoeIdAndState(clientWeatherOrderElement.getId(), 1, 1);//利润通过的在途库存
	   Double onpassstorageAmount=orderApprovalDao.findStorageAmount(clientWeatherOrderElement.getId(),1);//利润通过在途库存总数量
	   Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
	   boolean in=false;
	if(null!=other){//流程中新建流程other不为空
		 variables.put("to", other.getHandle());
		 other.setClientOrderElementId(clientWeatherOrderElement.getId());
		 other.setClientOrderId(clientWeatherOrderElement.getClientWeatherOrderId());
		 orderApprovalDao.insert(other);
		 orderApprovalList.add(other);
	}else{//正常发起流程
	   if(storageAmount>=clientWeatherOrderElement.getAmount()){//有库存，数量利润通过
		  Double amount=0.0;
		   for (OrderApproval orderApproval : passStock) {
			   amount=amount+orderApproval.getAmount();
			   orderApprovalList.add(orderApproval);
			   if(amount>=clientWeatherOrderElement.getAmount()){
				   break;
			   }
		   }
		   in=true;
		   variables.put("to", "有库存");
	   }else if(onpassstorageAmount>=clientWeatherOrderElement.getAmount()){
		   Double amount=0.0;
		   for (OrderApproval orderApproval : onpasspassStock) {
			   amount=amount+orderApproval.getAmount();
			   orderApprovalList.add(orderApproval);
			   if(amount>=clientWeatherOrderElement.getAmount()){
				   break;
			   }
		   }
		   in=true;
		   variables.put("to", "有库存");
	   }else if((onpassstorageAmount+storageAmount)>=clientWeatherOrderElement.getAmount()){
		   Double amount=0.0;
		   for (OrderApproval orderApproval : passStock) {
			   amount=amount+orderApproval.getAmount();
			   orderApprovalList.add(orderApproval);
		   }
		   for (OrderApproval orderApproval : onpasspassStock) {
			   amount=amount+orderApproval.getAmount();
			   orderApprovalList.add(orderApproval);
			   if(amount>=clientWeatherOrderElement.getAmount()){
				   break;
			   }
		   }
		   in=true;
		   variables.put("to", "有库存");
	   }else if(noPassStock.size()>0||passStock.size()>0||onpassnoPassStock.size()>0||onpasspassStock.size()>0){//所有库存
		   variables.put("to", "有库存");
		   orderApprovalList.addAll(passStock);
		   orderApprovalList.addAll(noPassStock);
		   orderApprovalList.addAll(onpassnoPassStock);
		   orderApprovalList.addAll(onpasspassStock);
	   }
	   else{
		   variables.put("to", "无库存");
		   OrderApproval orderApproval=new OrderApproval();
		   orderApproval.setClientOrderElementId(clientWeatherOrderElement.getId());
		   orderApproval.setClientOrderId(clientWeatherOrderElement.getClientWeatherOrderId());
		   orderApprovalDao.insert(orderApproval);
		   orderApprovalList.add(orderApproval);
	   }
	}
	   Double approvalAmount=0.0;
	   for (OrderApproval orderApproval : orderApprovalList) {
		String processName = "合同审批";
		String buninessKey = "ORDER_APPROVAL.ID."+String.valueOf(orderApproval.getId());
		
		try{
			if(null!=other&&null!=other.getUserId()){
			variables.put(WorkFlowConstants.START_USER, other.getUserId());//-- 发起人
			}else{
				variables.put(WorkFlowConstants.START_USER, ContextHolder.getCurrentUser().getUserId());//-- 发起人
			}
			variables.put(WorkFlowConstants.TASK_INFO, processName);//-- 流程信息
			   
			ProcessInstance processInstance = startProcessInstance(ProcessKeys.ContractreviewProcess.toValue(), buninessKey, variables);//-- 创建过程实例， 
			Task task = findTaskByProcessInstanceId(processInstance.getId());//-- 获取任务
			if(null!=other&&null!=other.getDesc()){
				task.setDescription(other.getDesc());
			}else{
			task.setDescription( WFUtils.getDescriptionStr(processName, WFUtils.DESCRIPTION_START) );
			}
			WFUtils.addTaskParticipatingUser(ids, task.getId(), taskService);
			
			Jbpm4Jbyj jbyj = new Jbpm4Jbyj();
			jbyj.setProcessinstanceId(processInstance.getId());
			jbyj.setTaskId("");
			jbyj.setBusinessKey(buninessKey);
			jbyj.setUserId(user.getUserId());
//			jbyj.setCreateTime(new Date());
			jbyj.setOutcome("发起");
			jbyj.setJbyj("合同评审");
			jbyj.setUserName(user.getUserName());
			jbyj.setRoleId(String.valueOf(user.getRoleId()));
			jbyj.setTaskName("合同评审");// 任务名称
			jbyj.setTaskSzmpy("htps");
			jbyj.setTaskInfoUrl("");
			jbyjDao.insert(jbyj);
			
			orderApproval.setSpzt(Integer.parseInt(WorkFlowConstants.SpztEnum.SHENG_HE_ZHONG.toString()));//审核中
			orderApproval.setTaskId(task.getId());
			if(null!=other){
			  other.setTaskId(task.getId());
			}
			orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
			
			Jbpm4Task jbpm4Task=new Jbpm4Task();
			jbpm4Task.setYwTableId(clientWeatherOrderElement.getClientWeatherOrderId());
			jbpm4Task.setYwTableElementId(clientWeatherOrderElement.getId());
			jbpm4Task.setExecutionId(processInstance.getId());
			jbpm4Task.setRelationId(orderApproval.getId());
			jbpm4TaskDao.updateByExecutionId(jbpm4Task);
			jbpm4TaskDao.updateJbpm4HistTask(jbpm4Task);
			jbpm4TaskDao.updateJbpm4HistActinst(jbpm4Task);
			
			if(in){
				approvalAmount+=orderApproval.getAmount();
				if(approvalAmount>=clientWeatherOrderElement.getAmount()){
					approvalAmount=clientWeatherOrderElement.getAmount()-(approvalAmount-orderApproval.getAmount());
					orderApproval.setAmount(approvalAmount);
//					orderApproval.setOccupy(1);
					orderApprovalDao.updateByPrimaryKeySelective(orderApproval);
				}
				completeTask(task.getId(), "使用", ContextHolder.getCurrentUser().getUserId(), "","销售生成客户订单", "clientOrderElementService#pass",
						"","/market/clientweatherorder/purchaseConfirmProfit?clientOrderElementId="+orderApproval.getId(),task.getId());
				Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findGyJbyjByTaskId(task.getId());
				jbpm4Jbyj.setUserId("0");
				jbpm4Jbyj.setUserName("");
				jbpm4Jbyj.setTaskId(task.getId());
				jbpm4Jbyj.setOutcome("库存转订单");
				jbyjDao.update(jbpm4Jbyj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	   }
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object weatherpass(String businessKey,
			String taskId, String outcome, String assignee, String comment){
		Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
		String id=flowService.getIdFromBusinessKey(businessKey);
		OrderApproval orderApproval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(id));
		SupplierWeatherOrderElement supplierWeatherOrderElement=supplierWeatherOrderElementDao.selectByPrimaryKey(orderApproval.getSupplierWeatherOrderElementId());
//		SupplierOrder supplierOrder=new SupplierOrder();
//		supplierOrder.setClientOrderId(orderApproval.getClientOrderId());
//		SupplierQuoteElement element=supplierQuoteElementDao.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
//		supplierOrder.setSupplierId(element.getSupplierId());
//		SupplierOrder supplierOrder2 = supplierOrderService.findByClientOrderId(supplierOrder);
//		SupplierOrderElement supplierOrderElement=new SupplierOrderElement();
//		supplierOrderElement.setAmount(supplierWeatherOrderElement.getAmount());
//		supplierOrderElement.setPrice(supplierWeatherOrderElement.getPrice());
//		supplierOrderElement.setLeadTime(supplierWeatherOrderElement.getLeadTime());
//		supplierOrderElement.setDeadline(supplierWeatherOrderElement.getDeadline());
//		supplierOrderElement.setShipWayId(supplierWeatherOrderElement.getShipWayId());
//		supplierOrderElement.setDestination(supplierWeatherOrderElement.getDestination());
//		supplierOrderElement.setClientOrderElementId(orderApproval.getClientOrderElementId());
//		supplierOrderElement.setSupplierQuoteElementId(supplierWeatherOrderElement.getSupplierQuoteElementId());
//		if (supplierOrder2!=null &&null==supplierOrder2.getOrderType()) {
//			supplierOrderElement.setSupplierOrderId(supplierOrder2.getId());
//			supplierOrderElementDao.insertSelective(supplierOrderElement);
//		}else {
//			supplierOrder.setOrderDate(new Date());
//			Supplier supplier=supplierDao.selectByPrimaryKey(supplierOrder.getSupplierId());
//			ExchangeRate exchangeRate=exchangeRateDao.selectByPrimaryKey(supplier.getCurrencyId());
//			supplierOrder.setExchangeRate(exchangeRate.getRate());
//			supplierOrder.setCurrencyId(supplier.getCurrencyId());
//			supplierOrder.setPrepayRate(supplier.getPrepayRate());
//			supplierOrder.setShipPayRate(supplier.getShipPayRate());
//			supplierOrder.setReceivePayPeriod(supplier.getReceivePayPeriod());
//			supplierOrder.setReceivePayRate(supplier.getReceivePayRate());
//			supplierOrderService.insertSelective(supplierOrder);
//			supplierOrderElement.setSupplierOrderId(supplierOrder.getId());
//			supplierOrderElementDao.insertSelective(supplierOrderElement);
//		}
		ClientOrderElementVo clientOrderElementVo=clientOrderElementDao.findByClientQuoteElementId(orderApproval.getClientOrderElementId());
		Double weatherPrice=supplierWeatherOrderElement.getPrice();
		Double quotePrice=clientOrderElementVo.getSupplierQuotePrice();
		if(weatherPrice>quotePrice){
			variables.put("to", "供应商价格比原报价高");
		}else{
			variables.put("to", "价格无上涨");
		}
		
		return variables;
	}
    
}
