package com.naswork.module.task.controller.contractreview;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.module.workflow.vo.Outcome;
import com.naswork.module.workflow.vo.OutcomeUser;
import com.naswork.module.workflow.vo.PageParamVo;
import com.naswork.service.UserService;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

/**
 * 流程ContractreviewProcess的控制器
 * @since 2017年04月13日 上午14:34:58
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/Contractreview")
public class ContractreviewTaskController extends WorkFlowController {
	@Resource
	private Jbpm4JbyjDao jbyjDao;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	@Resource
	private UserService userService;
	
	/**
	 * 采购生成供应商预订单
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgscgysydd", method = RequestMethod.GET)
	public String cgscgysydd(HttpServletRequest request,@RequestParam String taskId) {
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
		
		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
		
 		String[] taksids=idsForTask.split(",");
 		
 		UserVo userVo = getCurrentUser(request);
 		
 		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
 		
 		List<Integer> userList = userService.getLeadersByRole(new Integer(roleVo.getRoleId()));

 		//将获取到的领导id进行封装
		String ass = null;
 		if(userList != null){
 			if(userList.size()>1){
				ass = StringUtils.join(userList.toArray(),";");
			}else{
				ass = userList.get(0).toString();
			}
		}

 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );
 		
 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(ass);

 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#weatherpass").setTaskDescription( WFUtils.getDescriptionStr("生成供应商预订单") ).addTo(pageParam);
 		
 		new Outcome("提交部门领导审核").setOutcomeUser(ou3).setCheckJob(0).setColor("orange").setTaskDescription(WFUtils.getDescriptionStr("提交部门领导审核供应商预订单")).addTo(pageParam);
 		
 		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		/*String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}*/
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/purchaseAddSupplierWeatherOrder?clientOrderElementId="+taskId+"&type=add");
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 销售生成客户订单
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xssckhdd", method = RequestMethod.GET)
	public String xssckhdd(HttpServletRequest request,@RequestParam String taskId) {
		
		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
		
		String[] taksids=idsForTask.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		String assignee="";
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
		/*for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
			if(assignee.equals("")||assignee.equals("0")){
		 		Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", businessKey2);
		 		if(null==jbpm4Jbyj){
		 			 jbpm4Jbyj=jbpm4JbyjService.findByTask("采购审核", businessKey2);
		 		} 
		 		if(null==jbpm4Jbyj){
		 			 jbpm4Jbyj=jbpm4JbyjService.findByTask("采购审核库存", businessKey2);
		 		}
		 		assignee=jbpm4Jbyj.getUserId();
			}
		}*/
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(assignee);
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#finalpass").setTaskDescription( WFUtils.getDescriptionStr("销售生成客户订单") ).addTo(pageParam);
 		
 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(assignee);
 		//-- 连线配置
 		new Outcome("退回").setOutcomeUser(ou3).setCheckJob(0).setColor("orange").setBeforeEvent("clientOrderElementService#returnUser").setTaskDescription(WFUtils.getDescriptionStr("退回")).addTo(pageParam);

 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/marketingAddOrder?clientOrderElementId="+taskId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 检查是否有库存
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/jcsfykc", method = RequestMethod.GET)
	public String jcsfykc(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 采购审核
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgshkc", method = RequestMethod.GET)
	public String cgsh(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
		
		String[] taksids=idsForTask.split(",");
	
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(ContextHolder.getCurrentUser().getUserId().toString());
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );

 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("2");
 		
 		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
 		
 		String coeId=ids[ids.length-1];
 		Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findGyJbyjByBusinessKeyAndOutcome("ORDER_APPROVAL.ID."+coeId, "发起");
		String desc=jbpm4Jbyj.getUserName()+"发起【合同审批】等待您的处理!";
		/*for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}*/
 		
 		//-- 连线配置
 		new Outcome("使用").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#pass").setTaskDescription( WFUtils.getDescriptionStr("库存审核完成") ).addTo(pageParam);
 		
 		
 		//-- 连线配置
 		new Outcome("不使用").setOutcomeUser(ou1).setCheckJob(0).setColor("orange").setBeforeEvent("clientOrderElementService#nopass").setTaskDescription(desc).addTo(pageParam);
 		
 		new Outcome("提交审核").setOutcomeUser(ou3).setCheckJob(0).setColor("orange").setTaskDescription(WFUtils.getDescriptionStr("提交审核库存利润")).addTo(pageParam);
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/purchaseConfirmProfit?clientOrderElementId="+taskId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlshkc", method = RequestMethod.GET)
	public String zjlsh(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
		
		String[] taksids=idsForTask.split(",");
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
// 		String assignee=jbpm4JbyjService.findByTask("采购审核", pageParam.getBusinessKey()).getUserId();
 		Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("采购审核", pageParam.getBusinessKey());
		if(null==jbpm4Jbyj){
			 jbpm4Jbyj=jbpm4JbyjService.findByTask("采购审核库存", pageParam.getBusinessKey());
		}
		String assignee=jbpm4Jbyj.getUserId();
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(assignee);
 		
 		//-- 连线配置
 		new Outcome("回馈意见").setOutcomeUser(ou1).setCheckJob(0).setTaskDescription(WFUtils.getDescriptionStr("总经理回馈审批意见")).setColor("green").addTo(pageParam);
 		
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		/*for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}*/
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/storageProfit?clientOrderElementId="+taskId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive1
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive1", method = RequestMethod.GET)
	public String exclusive1(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 销售作废客户订单
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xszfkhdd", method = RequestMethod.GET)
	public String xszfkhdd(HttpServletRequest request,@RequestParam String taskId) {
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
 		
 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("");

 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou3).setCheckJob(0).setBeforeEvent("clientOrderElementService#cancellationorder").setColor("orange").addTo(pageParam);
 	 	
 		String[] taksids=idsForTask.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		/*for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}*/
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/cancellationOrder?clientOrderElementId="+taskId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 采购询问供应商能否降价
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgxwgysnfjj", method = RequestMethod.GET)
	public String cgxwgysnfjj(HttpServletRequest request,@RequestParam String taskId) {

		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
		
		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
 		
 		String[] taksids=idsForTask.split(",");
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("");
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );

 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("2");
 		//-- 连线配置
 		new Outcome("调整完成").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#theprice").setTaskDescription( WFUtils.getDescriptionStr("供应商价格调整") ).addTo(pageParam);
 		
 		new Outcome("提交审核").setOutcomeUser(ou3).setCheckJob(0).setColor("orange").setTaskDescription(WFUtils.getDescriptionStr("提交审核订单利润")).addTo(pageParam);

 		new Outcome("取消合同").setOutcomeUser(ou2).setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("供应商价格调整") ).addTo(pageParam);

		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		/*for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}*/
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/thePrice?clientOrderElementId="+taskId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive3
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive3", method = RequestMethod.GET)
	public String exclusive3(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive4
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive4", method = RequestMethod.GET)
	public String exclusive4(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 销售修改或作废客户订单项价格
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xsxghzfkhddxjg", method = RequestMethod.GET)
	public String xsxghzfkhddxjg(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( ContextHolder.getCurrentUser().getUserId());
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("2");
 		
 		Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", pageParam.getBusinessKey());
 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(jbpm4Jbyj.getUserId());

 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#weatherorderpass").setTaskDescription( "销售生成客户订单" ).addTo(pageParam);
 		
 		new Outcome("提交审核").setOutcomeUser(ou2).setCheckJob(0).setColor("orange").setTaskDescription(WFUtils.getDescriptionStr("提交审核")).addTo(pageParam);
 		
 		new Outcome("退回").setOutcomeUser(ou3).setCheckJob(0).setColor("orange").setBeforeEvent("clientOrderElementService#returnUser").setTaskDescription(WFUtils.getDescriptionStr("退回")).addTo(pageParam);
 	 	
 		String[] taksids=idsForTask.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/marketingUpdateWeatherOrder?clientOrderElementId="+taskId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核订单利润
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlshddlr", method = RequestMethod.GET)
	public String zjlshddlr(HttpServletRequest request,@RequestParam String taskId) {
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
 		
 		Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("采购询问供应商能否降价", pageParam.getBusinessKey());
 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(jbpm4Jbyj.getUserId());

 		//-- 连线配置
 		new Outcome("回馈意见").setOutcomeUser(ou3).setCheckJob(0).setTaskDescription(WFUtils.getDescriptionStr("总经理回馈审批意见")).setColor("green").addTo(pageParam);
 	 	
 		String[] taksids=idsForTask.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/orderprofitmarginapproval?clientOrderElementId="+taskId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核利润
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlshlr", method = RequestMethod.GET)
	public String zjlshjg(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
 		
 		Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("销售修改或作废客户订单项价格", pageParam.getBusinessKey());
 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(jbpm4Jbyj.getUserId());

 		//-- 连线配置
 		new Outcome("回馈意见").setOutcomeUser(ou3).setCheckJob(0).setTaskDescription(WFUtils.getDescriptionStr("总经理回馈审批意见")).setColor("green").addTo(pageParam);
 	 	
 		String[] taksids=idsForTask.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/managerApproval?clientOrderElementId="+taskId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive2
	 * @return
	 * @since 2017年04月13日 上午14:34:58
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive2", method = RequestMethod.GET)
	public String exclusive2(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive5
	 * @return
	 * @since 2018年08月22日 上午15:43:06
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive5", method = RequestMethod.GET)
	public String exclusive5(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 部门领导查看
	 * @return
	 * @since 2018年08月22日 上午15:43:06
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/bmldck", method = RequestMethod.GET)
	public String bmldck(HttpServletRequest request,@RequestParam String taskId) {
PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
		
		String idsForTask = "";
		List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(taskId);
		if (listTask.size() > 0) {
			List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
		    idsForTask=jbpm4Tasks.get(0).getId();
		    for (int i = 1; i < jbpm4Tasks.size(); i++) {
		    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
		    	 if(list.size()>1){
		    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
		    			 continue;
					 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单") || jbpm4Tasks.get(i).getTaskdefname().equals("部门领导查看")){
						 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
						 for (Jbpm4Task jbpm4Task2 : list1) {
							if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
								continue;
							}
						}
					 }
		    		 ;
		    	 }
		    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
			}
		}
		
 		String[] taksids=idsForTask.split(",");
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );
 		
 		Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", pageParam.getBusinessKey());
 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(jbpm4Jbyj.getUserId());

 		//-- 连线配置
 		new Outcome("通过").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#weatherpass").setTaskDescription( WFUtils.getDescriptionStr("部门领导查看") ).addTo(pageParam);
 		
 		new Outcome("退回").setOutcomeUser(ou3).setCheckJob(0).setColor("orange").setBeforeEvent("clientOrderElementService#returnUser").setTaskDescription(WFUtils.getDescriptionStr("退回")).addTo(pageParam);
 		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		/*String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}*/
 		
 		pageParam.setUrlTaskInfo("/market/clientweatherorder/purchaseAddSupplierWeatherOrder?clientOrderElementId="+taskId+"&type=check");
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
}
