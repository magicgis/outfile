package com.everygold.module.task.controller.Ask4leaveProcess;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

/**
 * 流程Ask4leaveProcess的控制器
 * @since 2016年12月23日 上午23:36:08
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/Ask4leaveProcess")
public class Ask4leaveProcessTaskController extends WorkFlowController {
	
	
	/**
	 * 行政部门审核
	 * @return
	 * @since 2016年12月23日 上午23:36:08
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xzbmsh", method = RequestMethod.GET)
	public String xzbmsh(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

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
		// TODO add your code

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
		// TODO add your code

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
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive1
	 * @return
	 * @since 2016年12月23日 上午23:36:08
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive1", method = RequestMethod.GET)
	public String exclusive1(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive2
	 * @return
	 * @since 2016年12月23日 上午23:36:08
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive2", method = RequestMethod.GET)
	public String exclusive2(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
}
