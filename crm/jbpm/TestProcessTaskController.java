package com.everygold.module.task.controller.TestProcess;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

/**
 * 流程TestProcess的控制器
 * @since 2016年12月26日 上午18:30:10
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/TestProcess")
public class TestProcessTaskController extends WorkFlowController {
	
	
	/**
	 * 测试节点
	 * @return
	 * @since 2016年12月26日 上午18:30:10
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/csjd", method = RequestMethod.GET)
	public String csjd(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
}
