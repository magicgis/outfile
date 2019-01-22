package com.everygold.module.task.controller.paymenta;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

/**
 * 流程PaymentProcess的控制器
 * @since 2016年12月28日 上午14:42:53
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/paymenta")
public class PaymentaTaskController extends WorkFlowController {
	
	
	/**
	 * 财务审核
	 * @return
	 * @since 2016年12月28日 上午14:42:53
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cwsh", method = RequestMethod.GET)
	public String cwsh(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

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
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 付款
	 * @return
	 * @since 2016年12月28日 上午14:42:53
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/fk", method = RequestMethod.GET)
	public String fk(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

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
}
