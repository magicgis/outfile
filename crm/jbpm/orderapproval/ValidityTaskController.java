package com.everygold.module.task.controller.validity;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

/**
 * 流程ValidityProcess的控制器
 * @since 2017年01月06日 上午15:04:56
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/validity")
public class ValidityTaskController extends WorkFlowController {
	
	
	/**
	 * 采购确认
	 * @return
	 * @since 2017年01月06日 上午15:04:56
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgqr", method = RequestMethod.GET)
	public String cgqr(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 销售调整
	 * @return
	 * @since 2017年01月06日 上午15:04:56
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xsdz", method = RequestMethod.GET)
	public String xsdz(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive1
	 * @return
	 * @since 2017年01月06日 上午15:04:56
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
	 * @since 2017年01月06日 上午15:04:56
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive2", method = RequestMethod.GET)
	public String exclusive2(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
}
