package com.naswork.module.task.controller.abnormalpart;

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
 * 流程AbnormalpartProcess的控制器
 * @since 2017年02月07日 上午15:06:38
 * @author auto
 * @version v1.0
 */
@Controller
@RequestMapping("/task/abnormalpart")
public class AbnormalpartTaskController extends WorkFlowController {
	
	
	/**
	 * 采购确认异常件
	 * @return
	 * @since 2017年02月07日 上午15:06:38
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/cgqrycj", method = RequestMethod.GET)
	public String cgqrycj(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String ipeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			ipeId+=","+ids2[ids2.length-1];
		}
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("8");
 		
 		OutcomeUser ou2 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("8");

 		OutcomeUser ou3 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("8");
 		
 		//-- 连线配置
 		new Outcome("退货").setOutcomeUser(ou1).setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("异常件已确认，处理结果为退货") ).addTo(pageParam);
 		new Outcome("ALT").setOutcomeUser(ou2).setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("异常件已确认，处理结果为ALT") ).addTo(pageParam);
 		new Outcome("库存").setOutcomeUser(ou3).setCheckJob(0).setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("异常件已确认，处理结果为库存") ).addTo(pageParam);
//
// 		String businessKey=flowService.findBusinessKeyByTaskId(taskId);
// 		
// 		String[] ids=businessKey.split("\\.");
 		
 		pageParam.setUrlTaskInfo("/importpackage/elementlist?id="+ipeId);

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 物流退货
	 * @return
	 * @since 2017年02月07日 上午15:06:38
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/wlth", method = RequestMethod.GET)
	public String wlth(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String ipeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			ipeId+=","+ids2[ids2.length-1];
		}
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("");
 		
 		
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou1).setCheckJob(0).setBeforeEvent("importPackElemnetService#returnOver").setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("异常件已处理，处理结果为退货") ).addTo(pageParam);
 		
 		pageParam.setUrlTaskInfo("/importpackage/abnormalpartelementlist?id="+ipeId+"&type="+"wlth");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 物流异常件转ALT
	 * @return
	 * @since 2017年02月07日 上午15:06:38
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/wlycjzalt", method = RequestMethod.GET)
	public String wlycjzalt(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String ipeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			ipeId+=","+ids2[ids2.length-1];
		}
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("");
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou1).setCheckJob(0).setBeforeEvent("importPackElemnetService#altOver").setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("异常件已处理，处理结果为转ALT") ).addTo(pageParam);
 		
 		pageParam.setUrlTaskInfo("/importpackage/abnormalpartelementlist?id="+ipeId+"&type="+"wlycjzalt");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
	
	/**
	 * 物流转库存
	 * @return
	 * @since 2017年02月07日 上午15:06:38
	 * @author auto
	 * @version v1.0
	 */
	@RequestMapping(value = "/wlzkc", method = RequestMethod.GET)
	public String wlzkc(HttpServletRequest request,@RequestParam String taskId) {
		UserVo user = ContextHolder.getCurrentUser();
		
		String[] taksids=taskId.split(",");
		
		String businessKey=flowService.findBusinessKeyByTaskId(taksids[0]);
 		
 		String[] ids=businessKey.split("\\.");
		
		String ipeId=ids[ids.length-1];
		for (int i = 1; i < taksids.length; i++) {
			String businessKey2=flowService.findBusinessKeyByTaskId(taksids[i]);
			String[] ids2=businessKey2.split("\\.");
			ipeId+=","+ids2[ids2.length-1];
		}
		
 		PageParamVo pageParam = getDeafaultPageParam(request);//-- 页面参数

 		OutcomeUser ou1 = OutcomeUser.createOutComeUser().setType( OutcomeUser.SELECT_TYPE_NO ).setAssignee("");
 		
 		//-- 连线配置
 		new Outcome("完成").setOutcomeUser(ou1).setCheckJob(0).setBeforeEvent("importPackElemnetService#over").setColor("orange").setTaskDescription( WFUtils.getDescriptionStr("异常件已处理，处理结果为转库存") ).addTo(pageParam);
 		
 		pageParam.setUrlTaskInfo("/importpackage/abnormalpartelementlist?id="+ipeId+"&type="+"wlzkc");

		request.setAttribute("pageParam", JsonUtils.toJson(pageParam));

		return WorkFlowConstants.MAIN_VIEW;
	}
}
