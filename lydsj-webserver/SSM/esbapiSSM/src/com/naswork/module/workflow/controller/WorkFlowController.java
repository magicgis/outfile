/**
 * 
 */
package com.naswork.module.workflow.controller;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.Deployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Participation;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.controller.BaseController;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.constants.WorkFlowConstants.SpztEnum;
import com.naswork.module.workflow.vo.PageParamVo;
import com.naswork.service.FlowService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

/**
 * @since 2016年12月16日 
 * @author Giam
 * @version v1.0
 */
@Controller
@RequestMapping("/workflow")
public class WorkFlowController extends BaseController {
	

	@Resource
	protected FlowService flowService;
	@Resource
	protected Jbpm4JbyjService jbpm4JbyjService;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	
	/**
	 * 流程部署
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deploymentPage", method=RequestMethod.GET)
	public String deploymentPage(HttpServletRequest request){
		return "/workflow/manage/deploymentPage";
	}
	
	/**
	 * 新增流程部署
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deploymentAdd", method = RequestMethod.GET)
	public String deploymentAdd(HttpServletRequest request){
		return "/workflow/manage/deploymentAdd"; 
	}
	
	/**
	 * 选人
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selectAssigneer", method = RequestMethod.GET)
	public String selectAssigneer(HttpServletRequest request){
		String type = request.getParameter("type");
		String roleid = request.getParameter("roleid");
		
		request.setAttribute("type", type);
		request.setAttribute("roleid", roleid);
		
		return "/workflow/assignee/selectAssigneer";
	}
	
	/**
	 * 选人数据列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAssigneerList", method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo getAssigneerList(HttpServletRequest request){
		String type = request.getParameter("type");
		String roleid = request.getParameter("roleid");
		JQGridMapVo jqgrid = new JQGridMapVo();
		List<String> roleidList = null;
		
		if( StringUtils.isNotEmpty( roleid ) ) {
			String[] roleids = roleid.split(";");
			roleidList = new ArrayList<String>();
			for( String item : roleids ){
				roleidList.add(item);
			}
		}
		
		List<Map<String, Object>> userList = flowService.findUserByRoleid(roleidList);
		jqgrid.setRows(userList);
		
		return jqgrid;
	}
	
	/**
	 * 流程部署上传
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deploymentUpload", method = RequestMethod.POST)
	public @ResponseBody String deploymentUpload(HttpServletRequest request){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String deploymentName = multipartRequest.getParameter("deploymentName");
		MultipartFile multipartFile = multipartRequest.getFile("file");
		String result = "1";
		try {
			flowService.saveNewDeploye(multipartFile.getInputStream(), deploymentName);
			result = "0";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 流程部署数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/deploymentData", method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo deploymentData(HttpServletRequest request) 
	{
		PageModel page = getPage(request);
		Map<String, String> param = new HashMap<String, String>();
		GridSort sort = getSort(request);
		flowService.findDeploymentPage(page, param, sort);
		
		JQGridMapVo vo = new JQGridMapVo();
		vo.setRows(page.getEntities());
		vo.setPage(page.getPageNo());
		vo.setTotal(page.getPageCount());
		vo.setRecords(page.getRecordCount());
		
		return vo;
	}
	
	/**
	 * 流程部署删除
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deploymentDelete", method = RequestMethod.POST)
	public @ResponseBody ResultVo deploymentDelete(HttpServletRequest request) 
	{
		boolean success = false;
		String message = "";
		String id = getString(request, "id");
		try
		{
			flowService.deletDeploymentById(id);
			success = true;
			message = "删除部署对象成功！";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			message = "删除部署对象失败！";
		}
		return new ResultVo(success, message); 
	}
	
	/**
	 * 流程定义数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/definitionData", method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo definitionData(HttpServletRequest request) 
	{
		PageModel page = getPage(request);
		Map<String, String> param = new HashMap<String, String>();
		String id = getString(request, "id");
		param.put("id", id);
		GridSort sort = getSort(request);
		flowService.findProcessDefinitionPage(page, param, sort);
		JQGridMapVo jqgrid = new JQGridMapVo();
		
		jqgrid.setPage(page.getPageNo());
		jqgrid.setRecords(page.getRecordCount());
		jqgrid.setTotal(page.getPageCount());
		jqgrid.setRows(page.getEntities());
		
		return jqgrid;
	}
	
	/**
	 * 查看流程图
	 * 
	 * @param request
	 * @param taskId
	 * @param businessKey
	 * @param lcdyId
	 * @return
	 * @since 2016年12月24日 下午5:012月:13
	 * @author nameless
	 * @version v1.0
	 */
	@RequestMapping(value = "/viewlct", method = RequestMethod.GET)
	public String viewlct(
			HttpServletRequest request,
			@RequestParam(value = "taskId", required = false) String taskId,
			@RequestParam(value = "businessKey", required = false) String businessKey,
			@RequestParam(value = "processDefinitionId", required = false) String processDefinitionId,
			@RequestParam(value = "history", required = false, defaultValue = "false") Boolean history) {
		ProcessDefinition processDefinition = null;
		if (StringUtils.isNotBlank(taskId)) {
			if (history) {
				processDefinition = flowService
						.findHistoryProcessDefinitionByTaskId(taskId);
			} else {
				processDefinition = flowService
						.findProcessDefinitionByTaskId(taskId);
//				ActivityCoordinates coordinates = flowService
//						.getActivityCoordinatesByTaskId(taskId);
				ProcessInstance processInstance = flowService.findProcessInstanceByTaskId(taskId);
				List<ActivityCoordinates> coordinates=flowService.getActivityCoordinatesListByProcessInstanceId(processInstance.getId());
				List<ActivityCoordinates> hisCoordinates = flowService
						.getHisActivityCoordinatesByTaskId(taskId);
				if (coordinates != null)
					request.setAttribute("coordinates", coordinates);
				if (hisCoordinates != null)
					request.setAttribute("hisCoordinates", hisCoordinates);
			}
			request.setAttribute("taskId", taskId);
		} else if (StringUtils.isNotBlank(businessKey)) {
			if (history) {
				processDefinition = flowService
						.findHistoryProcessDefinitionByBusinessKey(businessKey);
			} else {
				processDefinition = flowService.findProcessDefinitionByBusinessKey(businessKey);
				ActivityCoordinates coordinates = flowService
						.getActivityCoordinatesByBusinessKey(businessKey);
				List<ActivityCoordinates> hisCoordinates = flowService
						.getHisActivityCoordinatesByBusinessKey(businessKey);
				if (coordinates != null)
					request.setAttribute("coordinates", coordinates);
				if (hisCoordinates != null)
					request.setAttribute("hisCoordinates", hisCoordinates);
			}
			request.setAttribute("businessKey", businessKey);
		} else if (StringUtils.isNotBlank(processDefinitionId)) {
			processDefinition = flowService
					.findProcessDefinitionByProcessDefinitionId(processDefinitionId);
			request.setAttribute("processDefinitionId", processDefinitionId);
		}
		// 读取图片高宽
		try {
			InputStream is = flowService.findImageInputStream(processDefinition
					.getId());
			BufferedImage bufimg = ImageIO.read(is);
			int width = bufimg.getWidth();
			int height = bufimg.getHeight();
			request.setAttribute("width", width);
			request.setAttribute("height", height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("pic", "/workflow/image/" + processDefinition.getId());
		return "/workflow/lct";
	}
	
	/**
	 * 流程图
	 * 
	 * @param response
	 * @param id
	 * @param imageName
	 * @return
	 * @since 2016年12月24日 下午5:17:43
	 * @author nameless
	 * @version v1.0
	 */
	@RequestMapping(value = "/image/{processDefinitionId}", method = RequestMethod.GET)
	public String workflowPic(HttpServletResponse response,
			@PathVariable String processDefinitionId) {
		InputStream in = flowService.findImageInputStream(processDefinitionId);
		try {
			OutputStream out = response.getOutputStream();
			for (int b = -1; (b = in.read()) != -1;) {
				out.write(b);
			}
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 查看流程图
	 * 
	 * @param request
	 * @param businessKey
	 *            业务主键ID
	 * @param lcdyId
	 *            流程定义ID
	 * @return
	 * @since 2016年12月18日 下午4:32:02
	 * @author nameless
	 * @version v1.0
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewFlowInfo", method = RequestMethod.GET)
	public String viewFlowInfo(
			HttpServletRequest request,
			@RequestParam(value = "businessKey", required = false) String businessKey,
			@RequestParam(value = "processDefinitionId", required = false) String processDefinitionId)
			throws Exception {
		if (StringUtils.isNotBlank(businessKey)) {// 业务关键字不为空
			// 工作项内容
			List<Jbpm4Jbyj> jbyjList = flowService.findCommentByBusinessKey(businessKey);
			request.setAttribute("comments", jbyjList);
			Jbpm4Jbyj gyJbyj = flowService.findFirstJbrByBusinessKey(businessKey);
			request.setAttribute("starter", gyJbyj);

			String spzt = flowService.findSpztByBusinessKey(businessKey);// 根据业务关键字查询对应的业务记录审批状态

			if (SpztEnum.BU_TONG_GUO.toValue().equals(spzt) || SpztEnum.TONG_GUO.toValue().equals(spzt)) {// 通过或者不通过,表示流程已经结束,从历史中查
				ProcessDefinition processDefinition = flowService.findHistoryProcessDefinitionByBusinessKey(businessKey);
				HistoryProcessInstance processInstance = flowService.findHistoryProcessInstanceByBusinessKey(businessKey);
				Deployment deployment = flowService.findDeploymentByDeploymentId(processDefinition.getDeploymentId());

				request.setAttribute("deployment", deployment);
				request.setAttribute("processDefinition", processDefinition);
				request.setAttribute("processInstance", processInstance);
				request.setAttribute("businessKey", businessKey);
				request.setAttribute("history", true);

				return "/workflow/historylcinfowithcomments";
			} else if (SpztEnum.SHENG_HE_ZHONG.toValue().equals(spzt)) {// 流程中
				Task task = flowService.findTaskByBusinessKey(businessKey);
				HistoryProcessInstance processInstance = flowService.findHistoryProcessInstanceByTaskId(task.getId());
				ProcessDefinition processDefinition = flowService.findProcessDefinitionByTaskId(task.getId());
				Deployment deployment = flowService.findDeploymentByDeploymentId(processDefinition.getDeploymentId());

				String assigee = task.getAssignee();
				if (StringUtils.isNotBlank(assigee)) {
					request.setAttribute("assigee",getDmMc("SWRYSF_DM", assigee));
					request.setAttribute("assigeeId", assigee);
				} else {
					assigee = "";
					String ids = "";
					List<Participation> candidateUsers = flowService.findCandidateUsers(task.getId());
					for (Participation participation : candidateUsers) {
						if (StringUtils.isNotBlank(participation.getUserId())) {
							assigee += getDmMc("SWRYSF_DM",participation.getUserId())+ ",";
							ids += participation.getUserId() + ",";
						}
					}
					request.setAttribute("assigee",assigee.replaceAll(",$", ""));
					request.setAttribute("assigeeId", ids.replaceAll(",$", ""));
				}

				request.setAttribute("deployment", deployment);
				request.setAttribute("processDefinition", processDefinition);
				request.setAttribute("processInstance", processInstance);
				request.setAttribute("task", task);
				request.setAttribute("history", false);
									
				return "/workflow/lcinfowithcomments";
			} else if (SpztEnum.DENG_JI.toValue().equals(spzt)){
				// 登记状态
				throw new Exception("审批状态（SPZT）为登记,请确认是否没有在发起流程时修改业务记录的状态。");
			}else{
				throw new Exception("审批状态SPZT无法获取,请检查业务关键字是否正确，或者记录是否已被删除。");
			}
		} else if (StringUtils.isNotBlank(processDefinitionId)) {// 流程定义ID不为空
			return "redirect:/workflow/viewlct?processDefinitionId="+processDefinitionId;
		} else {
			throw new Exception("缺少关键参数，业务关键字或者流程定义ID");
		}
	}
	
	/**
	 * 我的任务
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mytask", method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo mytask(HttpServletRequest request)
	{
		PageModel page = getPage(request);
		UserVo user = getCurrentUser(request);
		GridSort sort = getSort(request);
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("userCode", user.getUserId());
		
		flowService.findMyTaskPage(page, param, sort);
		
		JQGridMapVo jqgrid = new JQGridMapVo();
		jqgrid.setPage(page.getPageNo());
		jqgrid.setRecords(page.getRecordCount());
		jqgrid.setTotal(page.getPageCount());
		jqgrid.setRows(page.getEntities());
		
		return jqgrid;
	}
	
	/**
	 * 我完成的任务
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/myfinished", method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo myfinished(HttpServletRequest request)
	{
		PageModel page = getPage(request);
		UserVo user = getCurrentUser(request);
		GridSort sort = getSort(request);
		
		String searchName = getString(request, "modelName");
		String endTime = getString(request, "endTime");
		String endTimeTo = getString(request, "endTimeTo");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("assignee", user.getUserId());
		param.put("searchName", searchName);
		param.put("startBefore",  endTime);
		param.put("startAfter",  endTimeTo);
		
		flowService.findMyFinishedData(page, param, sort);
		JQGridMapVo jqgrid = new JQGridMapVo();
		jqgrid.setPage(page.getPageNo());
		jqgrid.setRecords(page.getRecordCount());
		jqgrid.setTotal(page.getPageCount());
		jqgrid.setRows(page.getEntities());
		
		return jqgrid;
	}
	
	/**
	 * 完成任务
	 * 
	 * @param request
	 * @param taskId
	 *            任务ID,必须
	 * @param outcome
	 *            连线名称,必须
	 * @param comment
	 *            经办意见
	 * @param assignee
	 *            处理人
	 * @param beforeEvent
	 *            提交任务前的事件,可用于提交前增加参数等,serviceName#functionName,返回Map<String,
	 *            Object>
	 * @param afterEvent
	 *            提交任务后的事件,可用于更新业务表的记录状态,serviceName#functionName
	 * @return
	 * @since 2016年12月18日 下午5:01:49
	 * @author nameless
	 * @version v1.0
	 */
	@RequestMapping(value = "/completeTask", method = RequestMethod.POST)
	public @ResponseBody
	ResultVo completeTask(
			HttpServletRequest request,
			@RequestParam("taskId") String taskId,
			@RequestParam("outcome") String outcome,
			@RequestParam(value = "comment", required = false) String comment,
			@RequestParam(value = "assignee", required = false) String assignee,
			@RequestParam(value = "taskDescription", required = false) String taskDescription,
			@RequestParam(value = "beforeEvent", required = false) String beforeEvent,
			@RequestParam(value = "afterEvent", required = false) String afterEvent,
			@RequestParam(value = "taskInfoUrl") String taskInfoUrl) {
		boolean success = false;
		String message = "";
		try {
			flowService.completeTask(taskId, outcome, assignee, comment,taskDescription, beforeEvent, afterEvent,taskInfoUrl);
			success = true;
			message = "任务提交成功";
		} catch (Exception e) {
			success = false;
			message = "任务提交失败,原因:" + e.getCause().getMessage();
			logError("任务提交失败", logger, e);
		}

		return new ResultVo(success, message);
	}
	
	/**
	 * 历史任务
	 * @param taskId
	 * @return
	 * @since 2016年12月22日 下午12:48:17
	 * @author nameless
	 * @version v1.0
	 */
	@RequestMapping(value = "/historyTask/{taskId}",method = RequestMethod.GET)
	public String historyTask(HttpServletRequest request, @PathVariable String taskId){
		HistoryTask task = flowService.findHistoryTaskByTaskId(taskId);
		Jbpm4Jbyj gyJbyj = jbpm4JbyjService.findGyJbyjByTaskId(taskId);
		
		request.setAttribute("task", task);
		request.setAttribute("gyJbyj", gyJbyj);
		
		return "/workflow/historyTask";
	}
	
	//=========================================================
	
	/**
	 * 生成默认的流程参数对象
	 * 
	 * @param request
	 * @return
	 * @since 2016年12月24日上午10:10:57
	 * @author nameless
	 * @version v1.0
	 */
	protected PageParamVo getDeafaultPageParam(HttpServletRequest request) {
		String taskId = getString(request, "taskId");
		Jbpm4Task jbpm4Task=jbpm4TaskDao.selectByPrimaryKey(new BigDecimal(taskId));
 		String executionId=jbpm4Task.getExecutionId();
 //		 String execution=t.getExecutionId();
 		 String[] executions=executionId.split("\\.");
 		 if(executions.length>4){
 		 String id=executions[0]+"."+executions[1]+"."+executions[2]+"."+executions[3];
		 Jbpm4Task record=new Jbpm4Task();
		 record.setId(id);
		 record.setExecutionId(executionId);
		 jbpm4TaskDao.updateByExecutionId(record);
		 jbpm4TaskDao.updateJbpm4Execution(record);
		 jbpm4TaskDao.updateJbpm4HistActinst(record);
		 jbpm4TaskDao.updateJbpm4HistTask(record);
 		 }
		String businessKey = flowService.findBusinessKeyByTaskId(taskId);
		PageParamVo pageParamVo = new PageParamVo();
		pageParamVo.setTaskId(taskId);// 任务ID（必要参数）
		pageParamVo.setBusinessKey(businessKey);
		ProcessInstance processInstance = flowService.findProcessInstanceByTaskId(taskId);
		Assert.notNull(processInstance, "流程实例为空");

		pageParamVo.setProcessInstanceId(processInstance.getId());// 流程实例ID
		pageParamVo.setSubmitUrl(WorkFlowConstants.DEFAULT_SUBMIT_URL); // 默认提交方法
		return pageParamVo;
	}

}
