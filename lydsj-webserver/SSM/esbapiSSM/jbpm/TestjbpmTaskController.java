package com.everygold.module.task.controller.testjbpm;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

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
		// TODO add your code

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
		// TODO add your code

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
		// TODO add your code

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
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
}
