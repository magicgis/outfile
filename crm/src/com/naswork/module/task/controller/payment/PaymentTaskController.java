package com.naswork.module.task.controller.payment;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.dao.ImportPackagePaymentDao;
import com.naswork.filter.ContextHolder;
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
 * 流程PaymentProcess的控制器
 * @since 2016年12月27日 上午14:18:20
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/payment")
public class PaymentTaskController extends WorkFlowController {
	
	@Resource
	private FlowService flowService;
	@Resource
	private ImportPackagePaymentDao importPackagePaymentDao;
	@Resource
	private Jbpm4JbyjService jbpm4JbyjService;
	/**
	 * 财务审核
	 * @return
	 * @since 2016年12月27日 上午14:18:20
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cwsh", method = RequestMethod.GET)
	public String cwsh(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String ipeeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			ipeeId+=","+ids2[ids2.length-1];
		}
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("2");

 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee( (String) flowService.getVariable(taksids[0], WorkFlowConstants.START_USER) );
 		
 		//-- 连线配置
 		new Outcome("通过").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("付款申请通过") ).addTo(pageParam);
 		new Outcome("退回").setOutcomeUser(ou3).setCheckJob(1).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("付款申请退回") ).addTo(pageParam);
//
// 		String businessKey=flowService.findBusinessKeyByTaskId(taskId);
// 		
// 		String[] ids=businessKey.split("\\.");
 		
 		pageParam.setUrlTaskInfo("/finance/importpackagepayment/elementlist?id="+ipeeId+"&taskName="+"cwsh");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核
	 * @return
	 * @since 2016年12月28日 上午14:42:53
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlsh", method = RequestMethod.GET)
	public String zjlsh(HttpServletRequest request,@RequestParam String taskId) {
		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "");
		
		String assignee=jbpm4JbyjService.findByTask("财务审核", pageParam.getBusinessKey()).getUserId();
		
		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee(assignee);

		new Outcome("通过").setOutcomeUser(ou1).setBeforeEvent("importPackagePaymentService#pass").setAfterEvent("importPackagePaymentService#sendEmail").setCheckJob(0).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("付款申请通过") ).addTo(pageParam);

		new Outcome("退回").setOutcomeUser(ou3).setCheckJob(1).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("付款申请退回") ).addTo(pageParam);

		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String ipeeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			ipeeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/finance/importpackagepayment/elementlist?id="+ipeeId+"&taskName="+"zjlsh");
 		
 		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 付款
	 * @return
	 * @since 2016年12月27日 上午14:18:20
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/fk", method = RequestMethod.GET)
	public String fk(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "");
 		
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou1).setBeforeEvent("importPackagePaymentService#pass").setCheckJob(0).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("付款申请") ).addTo(pageParam);
 		
 		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String ipeeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			ipeeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/finance/importpackagepayment/elementlist?id="+ipeeId+"&taskName="+"fk");
 		
//		ImportPackagePayment payment=new ImportPackagePayment();
// 		payment.setPaymentStatusId(230);
//		importPackagePaymentDao.updateByPrimaryKeySelective(payment);

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 采购申请
	 * @return
	 * @since 2016年12月28日 上午14:42:53
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgsq", method = RequestMethod.GET)
	public String cgsq(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 采购重新申请
	 * @return
	 * @since 2016年12月27日 上午17:12:28
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgzxsq", method = RequestMethod.GET)
	public String cgzxsq(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数
 		
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setRoleId("6");
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "");

 		//-- 连线配置
 		new Outcome("付款申请").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("付款申请") ).addTo(pageParam);
 		
 		//-- 连线配置
 		new Outcome("退货").setOutcomeUser(ou2).setBeforeEvent("importPackagePaymentService#noPass").setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("流程结束") ).addTo(pageParam);
 		
 		//-- 连线配置
 		new Outcome("取消合同").setOutcomeUser(ou2).setBeforeEvent("importPackagePaymentService#noPass").setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("流程结束") ).addTo(pageParam);

 		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String ipeeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			ipeeId+=","+ids2[ids2.length-1];
		}
 		
 		pageParam.setUrlTaskInfo("/finance/importpackagepayment/elementlist?id="+ipeeId+"&taskName="+"cgzxsq");
 		
		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
}
