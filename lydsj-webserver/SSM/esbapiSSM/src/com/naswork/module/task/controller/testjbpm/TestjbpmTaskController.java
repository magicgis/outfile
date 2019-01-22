package com.naswork.module.task.controller.testjbpm;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.filter.ContextHolder;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.module.workflow.vo.Outcome;
import com.naswork.module.workflow.vo.OutcomeUser;
import com.naswork.module.workflow.vo.PageParamVo;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.UserVo;

/**
 * 流程test的控制器
 * @since 2017年09月24日 上午20:05:22
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/testjbpm")
public class TestjbpmTaskController extends WorkFlowController {
	
	
	/**
	 * 节点二
	 * @return
	 * @since 2017年09月24日 上午20:05:22
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/jde", method = RequestMethod.GET)
	public String jde(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		//-- 连线选人配置
 		//选人，按角色id,多个角色用；隔开
 		OutcomeUser ou = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_ROLE,  "0;1", "");
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("节点一完成") ).addTo(pageParam);
 		
 		//这里可以传参 pageParam.setUrlTaskInfo("/testJbpm/proessListPage?type=add&id=");
 		pageParam.setUrlTaskInfo("/testJbpm/proessListPage");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 节点三
	 * @return
	 * @since 2017年09月24日 上午20:05:22
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/jds", method = RequestMethod.GET)
	public String jds(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		//-- 连线选人配置
 		//不选人，角色id空着就可以，直接指定下一任务处理人为发起人
 		OutcomeUser ou = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_ALL,  "", "");
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("节点一完成") ).addTo(pageParam);
 		
 		//这里可以传参 pageParam.setUrlTaskInfo("/testJbpm/proessListPage?type=add&id=");
 		pageParam.setUrlTaskInfo("/testJbpm/proessListPage");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * fork1
	 * @return
	 * @since 2017年09月24日 上午20:05:22
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/fork1", method = RequestMethod.GET)
	public String fork1(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * join1
	 * @return
	 * @since 2017年09月24日 上午20:05:22
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/join1", method = RequestMethod.GET)
	public String join1(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 节点一
	 * @return
	 * @since 2017年09月24日 上午20:05:22
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/jdy", method = RequestMethod.GET)
	public String jdy(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		//-- 连线选人配置
 		//不选人，角色id空着就可以，直接指定下一任务处理人为发起人
 		OutcomeUser ou = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "", (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER));
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou).setColor("green").setTaskDescription( WFUtils.getDescriptionStr("节点一完成") ).addTo(pageParam);
 		
 		//这里可以传参 pageParam.setUrlTaskInfo("/testJbpm/proessListPage?type=add&id=");
 		pageParam.setUrlTaskInfo("/testJbpm/proessListPage");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 结束节点
	 * @return
	 * @since 2017年09月24日 上午20:05:22
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/jsjd", method = RequestMethod.GET)
	public String jsjd(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		//-- 连线选人配置
 		//不选人，角色id空着就可以，直接指定下一任务处理人为发起人
 		OutcomeUser ou = OutcomeUser.createOutComeUser(OutcomeUser.SELECT_TYPE_NO,  "", (String) flowService.getVariable(taskId, WorkFlowConstants.START_USER));
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou).setColor("green").setBeforeEvent("testJbpmService#changeSpzt").setTaskDescription( WFUtils.getDescriptionStr("节点一完成") ).addTo(pageParam);
 		
 		//这里可以传参 pageParam.setUrlTaskInfo("/testJbpm/proessListPage?type=add&id=");
 		pageParam.setUrlTaskInfo("/testJbpm/proessListPage");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));
		return WorkFlowConstants.MAIN_VIEW;
	}
}
