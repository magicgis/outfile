package com.naswork.module.task.controller.orderapproval;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.model.AuthorityRelation;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.module.marketing.controller.clientorder.orderReview;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.module.workflow.vo.Outcome;
import com.naswork.module.workflow.vo.OutcomeUser;
import com.naswork.module.workflow.vo.PageParamVo;
import com.naswork.service.AuthorityRelationService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.FlowService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.utils.json.JsonUtils;

/**
 * 流程OrderapprovalProcess的控制器
 * @since 2017年01月06日 上午15:05:40
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/orderapproval")
public class OrderapprovalTaskController extends WorkFlowController {
	@Resource
	private FlowService flowService;
	@Resource
	private Jbpm4JbyjService jbpm4JbyjService;
	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private AuthorityRelationService authorityRelationService;
	
	/**
	 * 财务审核
	 * @return
	 * @since 2017年01月06日 上午15:05:40
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cwsh", method = RequestMethod.GET)
	public String cwsh(HttpServletRequest request,@RequestParam String taskId) {
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
		
		
		String assignee="";
				
					Integer supplierId=clientOrderService.selectSupplierId(Integer.parseInt(ids[ids.length-1]));
						 List<AuthorityRelation>  authorityRelations=authorityRelationService.selectBySupplierId(supplierId);
							
						 for (AuthorityRelation authorityRelation : authorityRelations) {
							 assignee=authorityRelation.getUserId()+"";
						}
				
				
		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(assignee);

 		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,"");
 		
 		//-- 连线配置
 		new Outcome("通过").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setBeforeEvent("orderApprovalService#pass").setTaskDescription( WFUtils.getDescriptionStr("通过") ).addTo(pageParam);
 		new Outcome("不通过").setOutcomeUser(ou3).setCheckJob(1).setColor("orange").setBeforeEvent("orderApprovalService#financeNoPass").setTaskDescription( WFUtils.getDescriptionStr("供应商能否降价") ).addTo(pageParam);
 		
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/confirmProfit?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	
	
	/**
	 * 采购询问供应商能否降价
	 * @return
	 * @since 2017年01月06日 上午15:05:40
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgxwgysnfjj", method = RequestMethod.GET)
	public String cgxwgysnfjj(HttpServletRequest request,@RequestParam String taskId) {
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
		
		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("2");

 		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,"");
 		
 		//-- 连线配置
 		new Outcome("已降价").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setBeforeEvent("orderApprovalService#pass").setTaskDescription( WFUtils.getDescriptionStr("通过") ).addTo(pageParam);
 		new Outcome("不能降价").setOutcomeUser(ou3).setCheckJob(1).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("供应商不能降价") ).addTo(pageParam);
 		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/adjustPrice?clientOrderElementId="+coeId);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核
	 * @return
	 * @since 2017年01月06日 上午15:05:40
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlsh", method = RequestMethod.GET)
	public String zjlsh(HttpServletRequest request,@RequestParam String taskId) {
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
		
		OutcomeUser ou3 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,"");

 		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,"");
 		
 		//-- 连线配置
 		new Outcome("通过").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setBeforeEvent("orderApprovalService#pass").setTaskDescription( WFUtils.getDescriptionStr("通过") ).addTo(pageParam);
 		new Outcome("不通过").setOutcomeUser(ou3).setCheckJob(1).setColor("orange").setBeforeEvent("orderApprovalService#noPass").setTaskDescription( WFUtils.getDescriptionStr("不通过") ).addTo(pageParam);
 		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String coeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			coeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/sales/clientorder/confirmProfit?clientOrderElementId="+coeId+"&type="+1);
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	

	
}
