package com.everygold.module.task.controller.orderapproval;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

/**
 * 流程OrderapprovalProcess的控制器
 * @since 2017年01月12日 上午10:14:17
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/orderapproval")
public class OrderapprovalTaskController extends WorkFlowController {
	
	
	/**
	 * 财务审核
	 * @return
	 * @since 2017年01月12日 上午10:14:17
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cwsh", method = RequestMethod.GET)
	public String cwsh(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 采购询问供应商能否降价
	 * @return
	 * @since 2017年01月12日 上午10:14:17
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgxwgysnfjj", method = RequestMethod.GET)
	public String cgxwgysnfjj(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核
	 * @return
	 * @since 2017年01月12日 上午10:14:17
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlsh", method = RequestMethod.GET)
	public String zjlsh(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 销售作废不通过项目
	 * @return
	 * @since 2017年01月12日 上午10:14:17
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xszfbtgxm", method = RequestMethod.GET)
	public String xszfbtgxm(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
}
