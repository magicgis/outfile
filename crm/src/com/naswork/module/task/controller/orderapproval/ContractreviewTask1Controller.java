package com.naswork.module.task.controller.orderapproval;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.filter.ContextHolder;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.module.workflow.vo.Outcome;
import com.naswork.module.workflow.vo.OutcomeUser;
import com.naswork.module.workflow.vo.PageParamVo;
import com.naswork.service.FlowService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.UserVo;

/**
 * 流程ContractreviewProcess的控制器
 * @since 2017年03月06日 上午09:34:52
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task1/contractreview")
public class ContractreviewTask1Controller extends WorkFlowController {
	@Resource
	private Jbpm4JbyjService jbpm4JbyjService;
	
	@Resource
	private FlowService flowService;
	
	/**
	 * 采购生成供应商预订单
	 * @return
	 * @since 2017年03月06日 上午09:34:52
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgscgysydd", method = RequestMethod.GET)
	public String cgscgysydd(HttpServletRequest request,@RequestParam String taskId) {
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		String[] taksids=taskId.split(",");
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );

 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#weatherpass").setTaskDescription( WFUtils.getDescriptionStr("采购生成供应商预订单") ).addTo(pageParam);
 		
 		//-- 连线配置
 		
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/purchaseAddSupplierWeatherOrder?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 销售生成客户订单
	 * @return
	 * @since 2017年03月06日 上午09:34:52
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xssckhdd", method = RequestMethod.GET)
	public String xssckhdd(HttpServletRequest request,@RequestParam String taskId) {
		String assignee="";
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findByTask("采购生成供应商预订单", pageParam.getBusinessKey());
 		if(null==jbpm4Jbyj){
 			 jbpm4Jbyj=jbpm4JbyjService.findByTask("采购审核", pageParam.getBusinessKey());
 		}
 		assignee=jbpm4Jbyj.getUserId();
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(assignee);
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#finalpass").setTaskDescription( WFUtils.getDescriptionStr("销售生成客户订单") ).addTo(pageParam);
 		
 		//-- 连线配置
 		

 		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/marketingAddOrder?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 检查是否有库存
	 * @return
	 * @since 2017年03月06日 上午09:34:52
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
	 * @since 2017年03月06日 上午09:34:52
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgsh", method = RequestMethod.GET)
	public String cgsh(HttpServletRequest request,@RequestParam String taskId) {
	UserVo user = ContextHolder.getCurrentUser();
		
		String[] taksids=taskId.split(",");
	
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("2");
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );

 		//-- 连线配置
 		new Outcome("通过").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#pass").setTaskDescription( WFUtils.getDescriptionStr("库存利润通过") ).addTo(pageParam);
 		
 		//-- 连线配置
 		new Outcome("不通过").setOutcomeUser(ou1).setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("库存利润不通过") ).addTo(pageParam);
 		

 		
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/purchaseConfirmProfit?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核库存利润
	 * @return
	 * @since 2017年03月06日 上午09:34:52
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlshkclr", method = RequestMethod.GET)
	public String zjlshkclr(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String[] taksids=taskId.split(",");
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		String assignee=jbpm4JbyjService.findByTask("采购审核", pageParam.getBusinessKey()).getUserId();
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(assignee);
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );

 		//-- 连线配置
 		new Outcome("通过").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#pass").setTaskDescription( WFUtils.getDescriptionStr("库存利润通过") ).addTo(pageParam);
 		
 		//-- 连线配置
 		new Outcome("不通过").setOutcomeUser(ou1).setCheckJob(0).setColor("orange").setBeforeEvent("clientOrderElementService#nopass").setTaskDescription( WFUtils.getDescriptionStr("库存利润不通过") ).addTo(pageParam);
 		
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/purchaseConfirmProfit?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	
	
	/**
	 * 销售修改客户订单
	 * @return
	 * @since 2017年03月06日 上午09:34:52
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xsxgkhdd", method = RequestMethod.GET)
	public String xsxgkhdd(HttpServletRequest request,@RequestParam String taskId) {
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("");
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou2).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#zjlnopass").setTaskDescription( WFUtils.getDescriptionStr("修改客户订单") ).addTo(pageParam);
 		
 		//-- 连线配置
 		

 		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/updatefinalOrderPrice?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 采购询问供应商能否降价
	 * @return
	 * @since 2017年03月06日 上午09:34:52
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgxwgysnfjj", method = RequestMethod.GET)
	public String cgxwgysnfjj(HttpServletRequest request,@RequestParam String taskId) {
	UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("2");
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("2");

 		//-- 连线配置
 		new Outcome("已降价").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#pricenopass").setTaskDescription( WFUtils.getDescriptionStr("供应商已降价") ).addTo(pageParam);
 		
 		new Outcome("不能降价").setOutcomeUser(ou2).setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("供应商不能降价") ).addTo(pageParam);
 		//-- 连线配置
 		

 		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/updateWeatherOrderPrice?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	
	
	
	/**
	 * 销售修改或作废客户订单项价格
	 * @return
	 * @since 2017年03月06日 上午09:34:52
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xsxghzfkhddxjg", method = RequestMethod.GET)
	public String xsxghzfkhddxjg(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( ContextHolder.getCurrentUser().getUserId());
 		
// 		OutcomeUser ou2 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "");

 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("价格比原报价高") ).addTo(pageParam);
 		
// 		new Outcome("作废").setOutcomeUser(ou2).setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("作废订单") ).addTo(pageParam);
 		//-- 连线配置
 		

 		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/marketingUpdateWeatherOrder?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核订单利润
	 * @return
	 * @since 2017年03月06日 上午09:34:52
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlshddlr", method = RequestMethod.GET)
	public String zjlshddlr(HttpServletRequest request,@RequestParam String taskId) {
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
		
		String[] taksids=taskId.split(",");
		
		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "");

 		//-- 连线配置
 		new Outcome("通过").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setBeforeEvent("clientOrderElementService#pricepass").addTo(pageParam);

 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );
 		//-- 连线配置
 		new Outcome("不通过").setOutcomeUser(ou2).setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("订单利润不通过") ).addTo(pageParam);
 		
 		//-- 连线配置
 		
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/finalOrderPrice?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
}
