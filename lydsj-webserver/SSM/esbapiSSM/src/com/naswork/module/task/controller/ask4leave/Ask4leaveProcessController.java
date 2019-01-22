package com.naswork.module.task.controller.ask4leave;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.filter.ContextHolder;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.module.workflow.vo.Job;
import com.naswork.module.workflow.vo.Outcome;
import com.naswork.module.workflow.vo.OutcomeUser;
import com.naswork.module.workflow.vo.PageParamVo;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.UserVo;

/**
 * 流程Ask4leaveProcess的控制器
 * @since 2016年12月23日 上午23:36:08
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/Ask4leaveProcess")
public class Ask4leaveProcessController extends WorkFlowController {
	
	
	/**
	 * 行政部门审核
	 * @return
	 * @since 2016年12月23日 上午23:36:08
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xzbmsh", method = RequestMethod.GET)
	public String xzbmsh(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		//-- 连线选人配置
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "", (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER));
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "", (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER));
 		//-- 连线配置
 		new Outcome("提交审批").setOutcomeUser(ou1).setCheckJob(1).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("请假流程") ).addTo(pageParam);
 		new Outcome("退回").setOutcomeUser(ou2).setCheckJob(1).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("请假流程") ).addTo(pageParam);
 		
 		
 		pageParam.setUrlTaskInfo("/ask4leave/notePage?type=add&id=");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 请假人员重新提交申请
	 * @return
	 * @since 2016年12月23日 上午23:36:08
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/qjryzxtjsq", method = RequestMethod.GET)
	public String qjryzxtjsq(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		//-- 连线选人配置
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "", (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER));
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "", (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER));
 		//-- 连线配置
 		new Outcome("提交审批").setOutcomeUser(ou1).setCheckJob(1).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("请假流程") ).addTo(pageParam);
 		new Outcome("退回").setOutcomeUser(ou2).setCheckJob(1).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("请假流程") ).addTo(pageParam);
 		
 		pageParam.setUrlTaskInfo("/ask4leave/notePage?type=add&id=");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 管理层审批
	 * @return
	 * @since 2016年12月23日 上午23:36:08
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/glcsp", method = RequestMethod.GET)
	public String glcsp(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		//-- 连线选人配置
 		//  1;2  1;2;3
 		
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setRoleId("1;2;3;4").setAssignee( (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER) );//-- 不选人，默认一个

 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_ALL ).setRoleId("1;2;3;4").setAssignee( (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER) );

 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_ROLE ).setAssignee("3;4").setRoleId("1;2;4");//-- 选人，发送行政部门，可用;区分多个角色
 		
 		Job job = new Job("123", 1, "/ask4leave/notePage?type=add&id=");
 		pageParam.addJob(job);
 		//-- 连线配置
 		new Outcome("同意").setOutcomeUser(ou1).setCheckJob(0).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("请假流程") ).addTo(pageParam);
 		new Outcome("不同意").setOutcomeUser(ou2).setCheckJob(1).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("请假流程") ).addTo(pageParam);
 		new Outcome("退回").setOutcomeUser(ou3).setCheckJob(1).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("请假流程") ).addTo(pageParam);
 		
 		pageParam.setUrlTaskInfo("/ask4leave/notePage?type=add&id=");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 请假人员接收请假结果
	 * @return
	 * @since 2016年12月23日 上午23:36:08
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/qjryjsqjjg", method = RequestMethod.GET)
	public String qjryjsqjjg(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		//-- 连线选人配置
 		OutcomeUser ou1 = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "");
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou1).setCheckJob(1).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("请假流程") ).addTo(pageParam);
 		
 		pageParam.setUrlTaskInfo("/ask4leave/notePage?type=add&id=");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
}
