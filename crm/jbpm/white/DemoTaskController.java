package com.everygold.module.task.controller.Demo;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

/**
 * 流程Demo的控制器
 * @since 2018年09月28日 上午10:58:01
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/Demo")
public class DemoTaskController extends WorkFlowController {
	
	
	/**
	 * 员工请假
	 * @return
	 * @since 2018年09月28日 上午10:58:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/ygqj", method = RequestMethod.GET)
	public String ygqj(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 一级部门审核
	 * @return
	 * @since 2018年09月28日 上午10:58:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/yjbmsh", method = RequestMethod.GET)
	public String yjbmsh(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 二级部门审核
	 * @return
	 * @since 2018年09月28日 上午10:58:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/ejbmsh", method = RequestMethod.GET)
	public String ejbmsh(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 结果1
	 * @return
	 * @since 2018年09月28日 上午10:58:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/jg1", method = RequestMethod.GET)
	public String jg1(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 结果2
	 * @return
	 * @since 2018年09月28日 上午10:58:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/jg2", method = RequestMethod.GET)
	public String jg2(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 员工拿到申请结果
	 * @return
	 * @since 2018年09月28日 上午10:58:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/ygndsqjg", method = RequestMethod.GET)
	public String ygndsqjg(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
}
