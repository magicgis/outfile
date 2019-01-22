package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;

import com.naswork.dao.ArrearsUseDao;
import com.naswork.dao.ImportPackageDao;
import com.naswork.dao.ImportPackagePaymentDao;
import com.naswork.dao.ImportPackagePaymentElementDao;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.SupplierDebtDao;
import com.naswork.dao.SupplierOrderDao;
import com.naswork.dao.UserDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ArrearsUse;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierDebt;
import com.naswork.model.SupplierOrder;
import com.naswork.module.finance.controller.importpayment.SearchVo;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.constants.WorkFlowConstants.ProcessKeys;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.service.FlowService;
import com.naswork.service.ImportPackagePaymentService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;
@Service("importPackagePaymentService")
public class ImportPackagePaymentServiceImpl  extends FlowServiceImpl implements ImportPackagePaymentService {
	@Resource
	private ImportPackagePaymentDao importPackagePaymentDao;
	@Resource
	private SupplierDao supplierDao;
	@Resource
	private ImportPackageDao importPackageDao;
	@Resource
	private SupplierOrderDao supplierOrderDao;
	@Resource
	private FlowService flowService;
	@Resource
	private Jbpm4JbyjDao jbyjDao;
	@Resource
	private ArrearsUseDao arrearsUseDao;
	@Resource
	private SupplierDebtDao supplierDebtDao;
	@Resource
	private ImportPackagePaymentElementDao importPackagePaymentElementDao;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	@Resource
	private UserDao userDao;
	@Resource
	private Jbpm4JbyjService jbpm4JbyjService;
	
	@Override
	public void insert(ImportPackagePayment record) {
		importPackagePaymentDao.insert(record);
	}
	
	public int insertSelective(ImportPackagePayment record){
		return importPackagePaymentDao.insertSelective(record);
	}

	public ImportPackagePayment selectByPrimaryKey(Integer id){
		return importPackagePaymentDao.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ImportPackagePayment record){
		return importPackagePaymentDao.updateByPrimaryKeySelective(record);
	}

	public void add(ImportPackagePayment record){
		ImportPackage importPackage = importPackageDao.selectByPrimaryKey(record.getImportPackageId());
		SupplierOrder supplierOrder = supplierOrderDao.selectByPrimaryKey(record.getSupplierOrderId());
		String[] ziMu={"PA","PB","PC","PD","PE","PF","PG","PH","PI","PJ","PK","PL","PM",
				"PN","PO","PP","PQ","PR","PS","PT","PU","PV","PW","PX","PY","PZ"};
		int index = importPackagePaymentDao.getCountByImportPackageId(importPackage.getId());
		Supplier supplier = supplierDao.selectByPrimaryKey(record.getSupplierId());
		if (supplier!=null) {
			record.setPaymentPercentage(supplier.getShipPayRate());
		}else {
			record.setPaymentPercentage(0.00);
			record.setPaymentPercentage(new Double(0));
		}
		if (importPackage!=null) {
			record.setPaymentNumber(importPackage.getImportNumber()+ziMu[index]);
		}
		if (supplierOrder!=null) {
			record.setPaymentNumber(supplierOrder.getOrderNumber().substring(4)+ziMu[index]);
		}
		record.setSupplierId(record.getSupplierId());
		record.setPaymentStatusId(234);
		record.setPaymentType(1);
		importPackagePaymentDao.insertSelective(record);
	}
	
	public void findByImportPackageIdPage(PageModel<ImportPackagePayment> page){
		page.setEntities(importPackagePaymentDao.findBySupplierIdPage(page));
	}

	@Override
	public ImportPackagePayment findBySupplierOrderId(Integer supplierOrderId) {
		return importPackagePaymentDao.findBySupplierOrderId(supplierOrderId);
	}

	@Override
	public ImportPackagePayment findBypaymentNumber(String paymentNumber) {
		return importPackagePaymentDao.findBypaymentNumber(paymentNumber);
	}
	
	public void listPage(PageModel<ImportPackagePayment> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(importPackagePaymentDao.listPage(page));
		
	}
	
    public void selectByImportNumber(PageModel<SearchVo> page,String where){
    	page.put("where", where);
    	page.setEntities(importPackagePaymentDao.selectByImportNumber(page));
    }
    
    public void selectByOrderNumber(PageModel<SearchVo> page,String where){
    	page.put("where", where);
    	page.setEntities(importPackagePaymentDao.selectByOrderNumber(page));
    }
    
    public void addBySearch(ImportPackagePayment payment) {
    	ImportPackagePayment importPackagePayment = new ImportPackagePayment();
		SupplierOrder supplierOrder = supplierOrderDao.selectByPrimaryKey(payment.getId());
		ImportPackage importPackage =importPackageDao.selectByPrimaryKey(payment.getId());
		
		String[] ziMu={"PA","PB","PC","PD","PE","PF","PG","PH","PI","PJ","PK","PL","PM",
				"PN","PO","PP","PQ","PR","PS","PT","PU","PV","PW","PX","PY","PZ"};
		if (supplierOrder!=null) {
			int index = importPackagePaymentDao.getCountBySupplierOrderId(supplierOrder.getId());
			importPackagePayment.setSupplierOrderId(payment.getId());
			importPackagePayment.setSupplierId(supplierOrder.getSupplierId());
			importPackagePayment.setPaymentNumber(supplierOrder.getOrderNumber().substring(4)+ziMu[index]);
		}
		if (importPackage!=null) {
			int index = importPackagePaymentDao.getCountByImportPackageId(importPackage.getId());
			importPackagePayment.setImportPackageId(payment.getId());
			importPackagePayment.setSupplierId(importPackage.getSupplierId());
			importPackagePayment.setPaymentNumber(importPackage.getImportNumber()+ziMu[index]);
		}
		importPackagePayment.setPaymentDate(payment.getPaymentDate());
		importPackagePayment.setPaymentType(payment.getPaymentType());
		importPackagePayment.setLeadTime(payment.getLeadTime());
		importPackagePayment.setPaymentStatusId(234);
		importPackagePayment.setRemark(payment.getRemark());
		importPackagePaymentDao.insertSelective(importPackagePayment);
	}

	@Override
	public void paymentRequest(ImportPackagePayment payment,String ids) {

		List<ImportPackagePaymentElement> importPackagePaymentElement=importPackagePaymentElementDao.elementList(payment.getId());
		payment.setSpzt(232);
		importPackagePaymentDao.updateByPrimaryKeySelective(payment);
		for (ImportPackagePaymentElement importPackagePaymentElement2 : importPackagePaymentElement) {
			if(!importPackagePaymentElement2.getSpzt().equals(234)||importPackagePaymentElement2.getSpzt()!=234){
				continue;
			}
		
			UserVo user= ContextHolder.getCurrentUser();
			
			String processName = "付款审批";
			String buninessKey = "IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+String.valueOf(importPackagePaymentElement2.getId());
			
			try{
				
				Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
				variables.put(WorkFlowConstants.START_USER, ContextHolder.getCurrentUser().getUserId());//-- 发起人
				variables.put(WorkFlowConstants.TASK_INFO, processName);//-- 流程信息
				ProcessInstance processInstance = startProcessInstance(ProcessKeys.PaymentProcess.toValue(), buninessKey, variables);//-- 创建过程实例， 
				Task task = findTaskByProcessInstanceId(processInstance.getId());//-- 获取任务
				
				task.setDescription( WFUtils.getDescriptionStr(processName, WFUtils.DESCRIPTION_START) );
				WFUtils.addTaskParticipatingUser(ids, task.getId(), taskService);
	//			WFUtils.addTaskParticipatingUser("1", task.getId(), taskService);
				
				Jbpm4Jbyj jbyj = new Jbpm4Jbyj();
				jbyj.setProcessinstanceId(processInstance.getId());
				jbyj.setTaskId("");
				jbyj.setBusinessKey(buninessKey);
				jbyj.setUserId(user.getUserId());
	//			jbyj.setCreateTime(new Date());
				jbyj.setOutcome("发起");
				jbyj.setJbyj("付款申请");
				jbyj.setUserName(user.getUserName());
				jbyj.setRoleId(String.valueOf(user.getRoleId()));
				jbyj.setTaskName("付款申请");// 任务名称
				jbyj.setTaskSzmpy("fksq");
				jbyj.setTaskInfoUrl("");
				jbyjDao.insert(jbyj);
				
				ImportPackagePaymentElement record=new ImportPackagePaymentElement();
				record.setId(importPackagePaymentElement2.getId());
				record.setSpzt(232);
				importPackagePaymentElementDao.updateByPrimaryKeySelective(record);
				
				Jbpm4Task jbpm4Task=new Jbpm4Task();
				jbpm4Task.setYwTableId(payment.getId());
				jbpm4Task.setYwTableElementId(importPackagePaymentElement2.getId());
				jbpm4Task.setExecutionId(processInstance.getId());
				jbpm4Task.setRelationId(importPackagePaymentElement2.getId());
				jbpm4TaskDao.updateByExecutionId(jbpm4Task);
				jbpm4TaskDao.updateJbpm4HistTask(jbpm4Task);
				jbpm4TaskDao.updateJbpm4HistActinst(jbpm4Task);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object noPass(String businessKey,
			String taskId, String outcome, String assignee, String comment){

		ImportPackagePaymentElement importPackagePaymentElement=new ImportPackagePaymentElement();
		String id=flowService.getIdFromBusinessKey(businessKey);
		importPackagePaymentElement.setId(Integer.parseInt(id));
		importPackagePaymentElement.setSpzt(233);
		importPackagePaymentElementDao.updateByPrimaryKeySelective(importPackagePaymentElement);
		ImportPackagePaymentElement element= importPackagePaymentElementDao.elementData(Integer.parseInt(id));
		Integer count=importPackagePaymentDao.findSpztById(element.getImportPackagePaymentId());
		ArrearsUse arrearsUse=new ArrearsUse();
		arrearsUse.setImportPackagePaymentElementId(id);
		arrearsUseDao.deleteByElementId(arrearsUse);
		
		if(count.equals(0)||count==0){
		ImportPackagePayment importPackagePayment=new ImportPackagePayment();
		importPackagePayment.setId(element.getImportPackagePaymentId());
		importPackagePayment.setSpzt(235);
		importPackagePaymentDao.updateByPrimaryKeySelective(importPackagePayment);
		}
		return null;
	}
	
	/**
	 * 根据流程id修改审批状态
	 * **/
	public Object pass(String businessKey,
			String taskId, String outcome, String assignee, String comment){

		ImportPackagePaymentElement importPackagePaymentElement=new ImportPackagePaymentElement();
		String id=flowService.getIdFromBusinessKey(businessKey);
		importPackagePaymentElement.setId(Integer.parseInt(id));
		importPackagePaymentElement.setSpzt(235);
		importPackagePaymentElementDao.updateByPrimaryKeySelective(importPackagePaymentElement);
		
		ImportPackagePaymentElement element= importPackagePaymentElementDao.elementData(Integer.parseInt(id));
		Integer count=importPackagePaymentDao.findSpztById(element.getImportPackagePaymentId());
		if(count.equals(0)||count==0){
		ImportPackagePayment importPackagePayment=new ImportPackagePayment();
		importPackagePayment.setId(element.getImportPackagePaymentId());
		importPackagePayment.setSpzt(235);
		importPackagePaymentDao.updateByPrimaryKeySelective(importPackagePayment);
		
		  ImportPackagePaymentElement paymentElement=importPackagePaymentElementDao.selectByImportPackagePaymentId(element.getImportPackagePaymentId());
		
			if(null!=paymentElement){
				Double total=paymentElement.getArrearsTotal();
				if(null!=total){
					List<SupplierDebt>  list=supplierDebtDao.dataBySupplierCode(paymentElement.getSupplierCode());
					for (SupplierDebt supplierDebt : list) {
						if(supplierDebt.getSurplus()>0){
							if(total>supplierDebt.getSurplus()){
								supplierDebt.setPaid(supplierDebt.getSurplus()+supplierDebt.getPaid());
								supplierDebtDao.updateByPrimaryKeySelective(supplierDebt);
								total=total-supplierDebt.getSurplus();
							}else{
								
								if(null!=supplierDebt.getPaid()){
									supplierDebt.setPaid(supplierDebt.getPaid()+total);
								}else{
									supplierDebt.setPaid(total);
								}
								supplierDebtDao.updateByPrimaryKeySelective(supplierDebt);
								break;
							}
						}
					}
				}
				Double shouldPay=paymentElement.getArrearsTotal();
				if(null!=shouldPay){
					List<ArrearsUse> arrearsUses=arrearsUseDao.selectByPrimaryKey(element.getImportPackagePaymentId());
					for (ArrearsUse arrearsUse : arrearsUses) {
						ImportPackagePaymentElement packagePaymentElement=new ImportPackagePaymentElement();
						packagePaymentElement.setId(Integer.parseInt(arrearsUse.getImportPackagePaymentElementId()));
						packagePaymentElement.setPaymentSum(arrearsUse.getTotal());
						importPackagePaymentElementDao.updateByPrimaryKeySelective(packagePaymentElement);
					}
//					List<ImportPackagePaymentElement> paymentElements=importPackagePaymentElementDao.elementList(Integer.parseInt(id));
//					for (ImportPackagePaymentElement packagePaymentElement : paymentElements) {
//						if(shouldPay>packagePaymentElement.getShouldPay()){
//							packagePaymentElement.setPaymentSum(packagePaymentElement.getShouldPay());
//							importPackagePaymentElementDao.updateByPrimaryKeySelective(packagePaymentElement);
//							shouldPay=shouldPay-packagePaymentElement.getShouldPay();
//						}else{
//							packagePaymentElement.setPaymentSum(shouldPay);
//							importPackagePaymentElementDao.updateByPrimaryKeySelective(packagePaymentElement);
//							break;
//						}
//					}
				}
			}
		  
		}
		return null;
	}

	@Override
	public ImportPackagePayment listById(Integer importPackagePaymentId) {
		return importPackagePaymentDao.listById(importPackagePaymentId);
	}
	
	public void getShouldPrepaymentOrder(PageModel<SearchVo> page,String where){
		page.put("where", where);
		page.setEntities(importPackagePaymentDao.getShouldPrepaymentOrderPage(page));
	}
	
    public void getShouldShipPaymentOrderPage(PageModel<SearchVo> page,String where){
    	page.put("where", where);
		page.setEntities(importPackagePaymentDao.getShouldShipPaymentOrderPage(page));
    }
    
    public void getShouldReceivePaymentOrderPage(PageModel<SearchVo> page,String where){
    	page.put("where", where);
		page.setEntities(importPackagePaymentDao.getShouldReceivePaymentOrderPage(page));
    }

	@Override
	public Object sendEmail(String businessKey, String dbids) {
		String id=flowService.getIdFromBusinessKey(businessKey);
		ImportPackagePaymentElement element=importPackagePaymentElementDao.selectByPrimaryKey(Integer.parseInt(id));
		ImportPackagePayment importPackagePayment = importPackagePaymentDao.listById(element.getImportPackagePaymentId());
	    	UserVo vo=ContextHolder.getCurrentUser();
	    	List<String> emails = new ArrayList<String>();
			StringBuffer buffer = new StringBuffer();
			buffer.append("付款单号:").append(importPackagePayment.getPaymentNumber()).append("审批通过，请进行付款");
			UserVo current = userDao.findUserByUserId(vo.getUserId());
			List<String> user = new ArrayList<String>();
			user.add(current.getEmail());
			
			ExchangeMail exchangeMail = new ExchangeMail();
			exchangeMail.setUsername(current.getEmail());
			exchangeMail.setPassword(current.getEmailPassword());
			exchangeMail.init();
			
			String assignee=jbpm4JbyjService.findByTask("财务审核", businessKey).getUserId();
			if(null==assignee||"".equals(assignee)){
				List<UserVo> finance = userDao.getByRole("财务");
				for (UserVo userVo2 : finance) {
					if (!emails.contains(userVo2.getEmail()) && userVo2.getEmail() != null&&!"".equals(userVo2.getEmail())) {
						emails.add(userVo2.getEmail());
					}
				}
			
			}else{
				UserVo userVo = userDao.findUserByUserId(assignee);
				if (!emails.contains(userVo.getEmail())&&!"".equals(userVo.getEmail())) {
				emails.add(userVo.getEmail());
				}
			}
			
			List<String> bcc = new ArrayList<String>();
			
			
			try {
				exchangeMail.doSend("SYM付款通知", emails, user, bcc, buffer.toString(), "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
	}
	
}
