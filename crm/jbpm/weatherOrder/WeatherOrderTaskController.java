package com.everygold.module.task.controller.Contractreview;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

/**
 * 流程ContractreviewProcess的控制器
 * @since 2018年08月22日 上午16:01:01
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/Contractreview")
public class WeatherOrderTaskController extends WorkFlowController {
	
	
	/**
	 * 采购生成供应商预订单
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgscgysydd", method = RequestMethod.GET)
	public String cgscgysydd(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 销售生成客户订单
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xssckhdd", method = RequestMethod.GET)
	public String xssckhdd(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 检查是否有库存
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/jcsfykc", method = RequestMethod.GET)
	public String jcsfykc(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 采购审核库存
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgshkc", method = RequestMethod.GET)
	public String cgshkc(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核库存
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlshkc", method = RequestMethod.GET)
	public String zjlshkc(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive1
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive1", method = RequestMethod.GET)
	public String exclusive1(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 要求客户取消订单
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/yqkhqxdd", method = RequestMethod.GET)
	public String yqkhqxdd(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 采购询问供应商能否降价
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgxwgysnfjj", method = RequestMethod.GET)
	public String cgxwgysnfjj(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive3
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive3", method = RequestMethod.GET)
	public String exclusive3(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive4
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive4", method = RequestMethod.GET)
	public String exclusive4(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 销售修改或作废客户订单项价格
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/xsxghzfkhddxjg", method = RequestMethod.GET)
	public String xsxghzfkhddxjg(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核订单利润
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlshddlr", method = RequestMethod.GET)
	public String zjlshddlr(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 总经理审核利润
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/zjlshlr", method = RequestMethod.GET)
	public String zjlshlr(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive2
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive2", method = RequestMethod.GET)
	public String exclusive2(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * exclusive5
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/exclusive5", method = RequestMethod.GET)
	public String exclusive5(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 部门领导查看
	 * @return
	 * @since 2018年08月22日 上午16:01:01
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/bmldck", method = RequestMethod.GET)
	public String bmldck(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
}
