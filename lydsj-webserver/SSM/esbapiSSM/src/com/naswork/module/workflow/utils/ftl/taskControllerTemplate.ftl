package com.everygold.module.task.controller.${processName};

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.controller.WorkFlowController;

/**
 * 流程${processKey}的控制器
 * @since ${now}
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/${processName}")
public class ${className}TaskController extends WorkFlowController {
	
	<#list methodlist as method>
	
	/**
	 * ${method.methodDes}
	 * @return
	 * @since ${now}
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "${method.methodUrl}", method = RequestMethod.GET)
	public String ${method.methodName}(HttpServletRequest request,@RequestParam String taskId) {
		// TODO add your code

		return WorkFlowConstants.MAIN_VIEW;
	}
	</#list>
}
