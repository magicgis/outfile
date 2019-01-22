/**
 * 
 */
package com.naswork.module.workflow.controller;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.apache.bcel.generic.RET;
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
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.HierarchicalRelationshipDao;
import com.naswork.dao.ImportPackageDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.ImportPackagePaymentDao;
import com.naswork.dao.ImportPackagePaymentElementDao;
import com.naswork.dao.Jbpm4HistTaskDao;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.dao.UserDao;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.HierarchicalRelationship;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.Jbpm4HistTask;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.constants.WorkFlowConstants.SpztEnum;
import com.naswork.module.workflow.vo.PageParamVo;
import com.naswork.service.ClientWeatherOrderElementService;
import com.naswork.service.ClientWeatherOrderService;
import com.naswork.service.FlowService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.SpringUtils;
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
	private UserDao userDao;
	@Resource
	private OrderApprovalDao orderApprovalDao;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	@Resource
	private ImportPackagePaymentDao importPackagePaymentDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private ImportPackagePaymentElementDao importPackagePaymentElementDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ImportPackageDao importPackageDao;
	@Resource
	private ImportPackageElementDao importPackageElementDao;
	@Resource
	private ClientWeatherOrderService clientWeatherOrderService;
	@Resource
	private ClientWeatherOrderElementService clientWeatherOrderElementService;
	@Resource
	private Jbpm4HistTaskDao jbpm4HistTaskDao;
	@Resource
	private HierarchicalRelationshipDao hierarchicalRelationshipDao;
	
	@Resource
	private Jbpm4JbyjDao jbyjDao;
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
			flowService.saveNewDeploye(multipartFile.getInputStream(), new String(deploymentName.getBytes("iso8859-1"),"UTF-8"));
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
				ActivityCoordinates coordinates = flowService
						.getActivityCoordinatesByTaskId(taskId);
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
			if(null!=gyJbyj){
			request.setAttribute("userName", userDao.findUserByUserId(gyJbyj.getUserId()).getUserName());
			}
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
					request.setAttribute("assigee",userDao.findUserByUserId(assigee).getUserName());
//					request.setAttribute("assigeeId", assigee);
				} else {
					assigee = "";
					String ids = "";
					List<Participation> candidateUsers = flowService.findCandidateUsers(task.getId());
					for (Participation participation : candidateUsers) {
						if (StringUtils.isNotBlank(participation.getUserId())) {
							assigee += userDao.findUserByUserId(participation.getUserId()).getUserName()+ ",";
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
	
	/*
	 * 列表页面数据
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<Jbpm4Task> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String where = "";
		String parm=request.getParameter("searchString");
		List<HierarchicalRelationship> relationships=hierarchicalRelationshipDao.selectByUserId(userVo.getUserId());
		if(relationships.size()>0){
			for (HierarchicalRelationship hierarchicalRelationship : relationships) {
				where+=" OR jt.assignee_="+hierarchicalRelationship.getUserId();
			}
			page.put("where", where);
		}
		if(null!=parm&&!"".equals(parm)){
			page.put("parm", parm);
		}
		page.put("assignee", userVo.getUserId());
		 List<Jbpm4Task> removelist=new ArrayList<Jbpm4Task>();
		 List<Jbpm4Task> entities=jbpm4TaskDao.selectByAssigneePage(page);
		 for (Jbpm4Task jbpm4Task : entities) {
			   List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(jbpm4Task);
			   boolean in=false;
			    for (int i = 0; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 in=true;
			    	 }else{
			    		 in=false;
			    		 break;
			    	 }
				}
			    if(in){
			    removelist.add(jbpm4Task);
			    }
		}
		 for (Jbpm4Task jbpm4Task : removelist) {
			 if(jbpm4Task.getTaskdefname().equals("销售生成客户订单")){
				 entities.remove(jbpm4Task);
			 }else if(jbpm4Task.getTaskdefname().equals("采购生成供应商预订单")){
				 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Task.getYwTableElementId());
				 for (Jbpm4Task jbpm4Task2 : list) {
					if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
						 entities.remove(jbpm4Task);
					}
				}
			 }
			}
			page.setEntities(entities);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Jbpm4Task jbpm4Task : page.getEntities()) {
				UserVo vo=userDao.findUserByUserId(jbpm4Task.getAssignee());
				jbpm4Task.setAssignee(vo.getUserId());
				jbpm4Task.setUserName(vo.getUserName());
			    List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(jbpm4Task);
			    String ids=jbpm4Tasks.get(0).getId();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	ids+=","+jbpm4Tasks.get(i).getId();
				}
			    jbpm4Task.setIds(ids);
			    if(jbpm4Task.getName().equals("采购审核")){
					 jbpm4Task.setName("采购审核库存");
				 }
				
			   String processId=jbpm4Task.getExecutionId();
				String[] processIds=processId.split("\\.");
				String tableName=processIds[1];
				jbpm4Task.setTableName(tableName);
				
				 /*if(tableName.equals("IMPORT_PACKAGE_PAYMENT_ELEMENT")){
					ImportPackagePayment importPackagePayment=importPackagePaymentDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
					jbpm4Task.setDeploymentName("付款申请");
					jbpm4Task.setTaskNumber(importPackagePayment.getPaymentNumber());
				}else if(tableName.equals("ORDER_APPROVAL")){
//					ClientOrder clientOrder=clientOrderDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
//					jbpm4Task.setDeploymentName("合同评审");
//					if(null==clientOrder){
						 ClientWeatherOrder clientWeatherOrder=clientWeatherOrderService.selectByPrimaryKey(jbpm4Task.getYwTableId());
						 if(jbpm4Task.getName().equals("采购审核")){
							 jbpm4Task.setName("采购审核库存");
						 }
							jbpm4Task.setTaskNumber(clientWeatherOrder.getSourceNumber());
							jbpm4Task.setDeploymentName("合同评审");
//					}else{
//						jbpm4Task.setTaskNumber(clientOrder.getSourceNumber());
//					}
				}else if(tableName.equals("IMPORT_PACKAGE_ELEMENT")){
					ImportPackage importPackage=importPackageDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
					jbpm4Task.setDeploymentName("异常件审批");
					jbpm4Task.setTaskNumber(importPackage.getImportNumber());
				}*/
				
				Map<String, Object> map = EntityUtil.entityToTableMap(jbpm4Task);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	

	/**
	 * 我的任务子任务
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/subTask", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> subTask(HttpServletRequest request)
	{
		Map<String, Object> m = new HashMap<String, Object>();
		PageModel page = getPage(request);
		UserVo user = getCurrentUser(request);
		GridSort sort = getSort(request);
		
		Map<String, String> param = new HashMap<String, String>();
		page.put("userCode", user.getUserId());
		String ids=request.getParameter("ids");
		if (ids != null) {
			page.put("ids", ids);
		}
		String id=request.getParameter("id");
		if (id != null) {
			List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(id);
			if (listTask.size() > 0) {
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
			    String idsForTask=jbpm4Tasks.get(0).getId();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	 idsForTask+=","+jbpm4Tasks.get(i).getId();
				}
			    page.put("ids", idsForTask);
			}
		}
		
		
//		String id=getString(request, "id"); 
//		try {
//			page.put("descr", new String(request.getParameter("descr").getBytes("iso8859-1"),"UTF-8"));
//			page.put("name",  new String(request.getParameter("name").getBytes("iso8859-1"),"UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
	
//		Jbpm4Task jbpm4Task=jbpm4TaskDao.selectByPrimaryKey(new BigDecimal(id));
//		page.put("ywTableId", id);
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		String where ="";
		List<HierarchicalRelationship> relationships=hierarchicalRelationshipDao.selectByUserId(user.getUserId());
		if(relationships.size()>0){
			for (HierarchicalRelationship hierarchicalRelationship : relationships) {
				where+=" OR jt.assignee_="+hierarchicalRelationship.getUserId()+")";
			}
			page.put("where", where);
		}
		flowService.findSubTaskPage(page, param, sort);
		int number=1;
		List<Map<String,Object>> rows = page.getEntities();
		for (Map<String, Object> map : rows) {
			Object ywTableElementId=map.get("ywTableElementId");
			Object processId=map.get("tableName");
			Object taskId=map.get("id");
			String[] processIds=processId.toString().split("\\.");
			String tableName=processIds[1];
			String partNumber="";
			Double price=0.0;
			Double amount=0.0;
			Double paymentPercentage=0.0;
			String remark="";
			Map<String, Object> cellMap = new HashMap<String, Object>();
			
			if(tableName.equals("IMPORT_PACKAGE_PAYMENT_ELEMENT")){
				ImportPackagePaymentElement importPackagePaymentElement=importPackagePaymentElementDao.elementData(Integer.parseInt(ywTableElementId.toString()));
				String[] cell = {taskId.toString(),
				importPackagePaymentElement.getPartNumber(),
				importPackagePaymentElement.getShouldPay().toString(),
				importPackagePaymentElement.getAmount().toString(),
				importPackagePaymentElement.getPaymentPercentage().toString(),
				importPackagePaymentElement.getRemark()
				};
				cellMap.put("cell", cell);
			}else if(tableName.equals("ORDER_APPROVAL")){
//				ClientOrderElementVo clientOrderElement=clientOrderElementDao.findByclientOrderELementId(Integer.parseInt(ywTableElementId.toString().toString()));
//				if(null==clientOrderElement){
				ClientOrderElementVo clientOrderElement=clientWeatherOrderElementService.findByclientOrderELementId(Integer.parseInt(ywTableElementId.toString()));
//				}
				String[] cell = {taskId.toString(),
						number+"",
						clientOrderElement.getItem().toString(),
						clientOrderElement.getQuotePartNumber(),
				clientOrderElement.getQuoteDescription(),
				clientOrderElement.getQuoteUnit(),
				clientOrderElement.getClientOrderAmount().toString(),
				clientOrderElement.getRemark()
				};
				
				cellMap.put("cell", cell);
			}else if(tableName.equals("IMPORT_PACKAGE_ELEMENT")){
				ImportPackageElement importPackageElement=importPackageElementDao.selectByPrimaryKey(Integer.parseInt(ywTableElementId.toString()));
				String[] cell = {taskId.toString(),
						importPackageElement.getPartNumber(),
						importPackageElement.getDescription(),
						importPackageElement.getUnit(),
				importPackageElement.getAmount().toString(),
				importPackageElement.getRemark()
				};
				cellMap.put("cell", cell);
			}
			
			number++;
//			cellMap.put("id", i);
			mapList.add(cellMap);
		}
		m.put("rows", mapList);
		m.put("total", 1);
		m.put("page", 1);
		m.put("records", 1);		
		return m;
	}
	
	/**
	 * 流程任务管理页面
	 * **/
	@RequestMapping(value = "/toProcessList",method = RequestMethod.GET)
	public String toProcessList(HttpServletRequest request){
		return "/system/processmanage/list";
	}
	
	/**
	 * 修改流程任务处理人页面
	 * **/
	@RequestMapping(value = "/touserlist",method = RequestMethod.GET)
	public String touserlist(HttpServletRequest request){
		return "/system/processmanage/userList";
	}
	
	/**
	 * 修改处理人
	 */
	@RequestMapping(value="/updateUser",method=RequestMethod.POST)
	public @ResponseBody ResultVo updateUser(HttpServletRequest request) {
		String message = "修改完成";
		boolean success = true;
		String ids=request.getParameter("ids");
		String userId=request.getParameter("userId");
		String[] id=ids.split(",");
		for (int i = 0; i < id.length; i++) {
			Jbpm4HistTask histTask=new Jbpm4HistTask();
			histTask.setDbid(Integer.parseInt(id[i]));
			histTask.setAssignee(userId);
			jbpm4HistTaskDao.updateAssignee(histTask);
			Jbpm4Task record=new Jbpm4Task();
			record.setId(id[i]);
			record.setAssignee(userId);
			jbpm4TaskDao.updateByPrimaryKeySelective(record);
		}
		return new ResultVo(success, message);
	}
	
	/*
	 * 流程任务管理页面数据
	 */
	@RequestMapping(value="/processListData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo processListData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<Jbpm4Task> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String parm = request.getParameter("searchString");
		 List<Jbpm4Task> removelist=new ArrayList<Jbpm4Task>();
		 if(null!=parm&&!"".equals(parm)){
				page.put("parm", parm);
			}
		 List<Jbpm4Task> entities=jbpm4TaskDao.selectByAssigneePage(page);
		 for (Jbpm4Task jbpm4Task : entities) {
			 if(null==jbpm4Task.getAssignee()||"".equals(jbpm4Task.getAssignee())){
				 jbpm4Task.setAssignee("0");
			 }
			   List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(jbpm4Task);
			   boolean in=false;
			    for (int i = 0; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 in=true;
			    	 }else{
			    		 in=false;
			    		 break;
			    	 }
				}
			    if(in){
			    removelist.add(jbpm4Task);
			    }
		}
		 for (Jbpm4Task jbpm4Task : removelist) {
			 if(jbpm4Task.getTaskdefname().equals("销售生成客户订单")){
				 entities.remove(jbpm4Task);
			 }else if(jbpm4Task.getTaskdefname().equals("采购生成供应商预订单")){
				 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Task.getYwTableElementId());
				 for (Jbpm4Task jbpm4Task2 : list) {
					if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
						 entities.remove(jbpm4Task);
					}
				}
			 }
			}
			page.setEntities(entities);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Jbpm4Task jbpm4Task : page.getEntities()) {
				UserVo vo=userDao.findUserByUserId(jbpm4Task.getAssignee());
				if(null!=vo){
					jbpm4Task.setAssignee(vo.getUserId());
					jbpm4Task.setUserName(vo.getUserName());
				
			    List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(jbpm4Task);
			    String ids=jbpm4Tasks.get(0).getId();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	ids+=","+jbpm4Tasks.get(i).getId();
				}
			    jbpm4Task.setIds(ids);
			    if(jbpm4Task.getName().equals("采购审核")){
					 jbpm4Task.setName("采购审核库存");
				 }
				}
			/*	String processId=jbpm4Task.getExecutionId();
				String[] processIds=processId.split("\\.");
				String tableName=processIds[1];
				jbpm4Task.setTableName(tableName);
				if(tableName.equals("IMPORT_PACKAGE_PAYMENT_ELEMENT")){
					ImportPackagePayment importPackagePayment=importPackagePaymentDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
					jbpm4Task.setDeploymentName("付款申请");
					jbpm4Task.setTaskNumber(importPackagePayment.getPaymentNumber());
				}else if(tableName.equals("ORDER_APPROVAL")){
//					ClientOrder clientOrder=clientOrderDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
//					jbpm4Task.setDeploymentName("合同评审");
//					if(null==clientOrder){
						 ClientWeatherOrder clientWeatherOrder=clientWeatherOrderService.selectByPrimaryKey(jbpm4Task.getYwTableId());
						
							jbpm4Task.setTaskNumber(clientWeatherOrder.getSourceNumber());
							jbpm4Task.setDeploymentName("合同评审");
//					}else{
//						jbpm4Task.setTaskNumber(clientOrder.getSourceNumber());
//					}
				}else if(tableName.equals("IMPORT_PACKAGE_ELEMENT")){
					ImportPackage importPackage=importPackageDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
					jbpm4Task.setDeploymentName("异常件审批");
					jbpm4Task.setTaskNumber(importPackage.getImportNumber());
				}*/
				
				Map<String, Object> map = EntityUtil.entityToTableMap(jbpm4Task);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
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
	
	/*
	 * 列表页面数据
	 */
	@RequestMapping(value="/myFinishedListData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo myFinishedListData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<Jbpm4Task> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String parm = request.getParameter("searchString");
		page.put("assignee", userVo.getUserId());
		 if(null!=parm&&!"".equals(parm)){
				page.put("parm", parm);
			}
		List<Jbpm4Task> jbpm4Tasks =jbpm4TaskDao.myFinishedTaskDataPage(page);
		page.setEntities(jbpm4Tasks);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Jbpm4Task jbpm4Task : page.getEntities()) {
				 if(jbpm4Task.getActivityName().equals("采购审核")){
					 jbpm4Task.setActivityName("采购审核库存");
				 }
				 	String processId=jbpm4Task.getExecutionId();
				String[] processIds=processId.split("\\.");
				String tableName=processIds[1];
				jbpm4Task.setTableName(tableName);
				/*if(tableName.equals("IMPORT_PACKAGE_PAYMENT_ELEMENT")){
					ImportPackagePayment importPackagePayment=importPackagePaymentDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
					jbpm4Task.setDeploymentName("付款申请");
					jbpm4Task.setTaskNumber(importPackagePayment.getPaymentNumber());
				}else if(tableName.equals("ORDER_APPROVAL")){
//					ClientOrder clientOrder=clientOrderDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
//					jbpm4Task.setDeploymentName("合同评审");
//					if(null==clientOrder){
						 ClientWeatherOrder clientWeatherOrder=clientWeatherOrderService.selectByPrimaryKey(jbpm4Task.getYwTableId());
						 if(jbpm4Task.getActivityName().equals("采购审核")){
							 jbpm4Task.setActivityName("采购审核库存");
						 }
							jbpm4Task.setTaskNumber(clientWeatherOrder.getSourceNumber());
							jbpm4Task.setDeploymentName("合同评审");
//					}else{
//						jbpm4Task.setTaskNumber(clientOrder.getSourceNumber());
//					}
				}else if(tableName.equals("IMPORT_PACKAGE_ELEMENT")){
					ImportPackage importPackage=importPackageDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
					jbpm4Task.setDeploymentName("异常件审批");
					jbpm4Task.setTaskNumber(importPackage.getImportNumber());
				}*/
				Map<String, Object> map = EntityUtil.entityToTableMap(jbpm4Task);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	

	/**
	 * 我的任务子任务
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/myFinishedListsUBData", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> myFinishedListsUBData(HttpServletRequest request, HttpServletResponse resp) throws UnsupportedEncodingException
	{
		Map<String, Object> m = new HashMap<String, Object>();
		UserVo user = getCurrentUser(request);
		GridSort sort = getSort(request);
		request.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		
		Map<String, String> param = new HashMap<String, String>();
		String ywTableId=request.getParameter("ywTableId");
		
		
		String activityName=new String(request.getParameter("activityName").getBytes("iso8859-1"),"UTF-8");
		String name=new String(request.getParameter("name").getBytes("iso8859-1"),"UTF-8");
		/*String activityName = request.getParameter("activityName");
		String name = request.getParameter("name");*/
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		UserVo userVo=getCurrentUser(request);
		 List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.myFinishedListsUBData(userVo.getUserId(),ywTableId,activityName,name);
		 if(jbpm4Tasks.size()==0&&activityName.equals("采购审核库存")){
			 jbpm4Tasks=jbpm4TaskDao.myFinishedListsUBData(userVo.getUserId(),ywTableId,"采购审核",name);
		 }
		 int number=1;
		 for (Jbpm4Task jbpm4Task : jbpm4Tasks) {
//			 ClientOrderElementVo clientOrderElement=clientOrderElementDao.findByclientOrderELementId(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
				Map<String, Object> cellMap = new HashMap<String, Object>();
//				String[] cell = {
//						jbpm4Task.getId(),
//						clientOrderElement.getQuotePartNumber(),
//						clientOrderElement.getQuoteDescription(),
//						clientOrderElement.getQuoteUnit(),
//						clientOrderElement.getClientOrderAmount().toString()
//				};
//				cellMap.put("cell", cell);
				
				
				String processId=jbpm4Task.getExecutionId();
				String[] processIds=processId.split("\\.");
				String tableName=processIds[1];
				if(tableName.equals("IMPORT_PACKAGE_PAYMENT_ELEMENT")){
					ImportPackagePaymentElement importPackagePaymentElement=importPackagePaymentElementDao.elementData(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
					String[] cell = {jbpm4Task.getId(),
					importPackagePaymentElement.getPartNumber(),
					importPackagePaymentElement.getShouldPay().toString(),
					importPackagePaymentElement.getAmount().toString(),
					importPackagePaymentElement.getPaymentPercentage().toString(),
					importPackagePaymentElement.getRemark(),
					jbpm4Task.getUserName()
					};
					cellMap.put("cell", cell);
				}else if(tableName.equals("ORDER_APPROVAL")){
//					ClientOrderElementVo clientOrderElement=clientOrderElementDao.findByclientOrderELementId(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
//					if(null==clientOrderElement){
					ClientOrderElementVo clientOrderElement=clientWeatherOrderElementService.findByclientOrderELementId(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
//					}
					String[] cell = {jbpm4Task.getId(),
							number+"",
							clientOrderElement.getItem().toString(),
							clientOrderElement.getQuotePartNumber(),
					clientOrderElement.getQuoteDescription(),
					clientOrderElement.getQuoteUnit(),
					clientOrderElement.getClientOrderAmount().toString(),
					jbpm4Task.getUserName()
					};
					cellMap.put("cell", cell);
				}else if(tableName.equals("IMPORT_PACKAGE_ELEMENT")){
					ImportPackageElement importPackageElement=importPackageElementDao.selectByPrimaryKey(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
					String[] cell = {jbpm4Task.getId(),
							importPackageElement.getPartNumber(),
							importPackageElement.getDescription(),
							importPackageElement.getUnit(),
					importPackageElement.getAmount().toString(),
					importPackageElement.getRemark(),
					jbpm4Task.getUserName()
					};
					cellMap.put("cell", cell);
				}
				number++;
//			cellMap.put("id", i);
			mapList.add(cellMap);
		}
		
		m.put("rows", mapList);
		m.put("total", 1);
		m.put("page", 1);
		m.put("records", 1);		
		return m;
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
			@RequestParam(value = "taskInfoUrl") String taskInfoUrl,
			@RequestParam(value ="id") String id){
		boolean success = false;
		String message = "";
		try {
//			String[] ids=taskId.split(",");
//			for (int i = 0; i < ids.length; i++) {
//				OrderApproval approval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(ids[i]));
//				Task task=flowService.findTaskByProcessInstanceId(approval.getProcessInstanceId());
//				taskId=task.getId();
//				String[] taskInfoUrls=taskInfoUrl.split("=");
//				taskInfoUrl=taskInfoUrls[0]+"="+approval.getClientOrderElementId();
			flowService.completeTask(taskId, outcome, assignee, comment,taskDescription, beforeEvent, afterEvent,taskInfoUrl,id);
			// 执行提交后方法
						if (StringUtils.isNotBlank(afterEvent)) {
							try {
								String[] params = afterEvent.split("#");
								String springbeanid = params[0];
								String functionName = params[1];
								if (StringUtils.isNotBlank(springbeanid)
										&& StringUtils.isNotBlank(functionName)) {
									Object object = SpringUtils.getBean(springbeanid);
									Class[] parameterTypes = new Class[2];
									parameterTypes[0] = String.class;
									parameterTypes[1] = String.class;
									Method method = object.getClass().getMethod(functionName,
											parameterTypes);
									if(taskId.indexOf(",")>-1){
										taskId=taskId.split(",")[0];
									}
									Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findGyJbyjByTaskId(taskId);
									method.invoke(object, new Object[] { jbpm4Jbyj.getBusinessKey(),id });
								} else {
									logger.debug("由于springbeanid为空或者方法名为空,所以忽略事件,不执行");
								}
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("执行完成任务后的事件出错", e);
								throw e;
							}
						}
//			}
			success = true;
			message = "任务提交成功";
		} catch (Exception e) {
			e.printStackTrace();
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
		String message=jbpm4TaskDao.findMessage(task.getId());
		gyJbyj.setJbyj(message);
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
		String[] ids=taskId.split(",");
		String businessKey = flowService.findBusinessKeyByTaskId(ids[0]);
		PageParamVo pageParamVo = new PageParamVo();
		pageParamVo.setTaskId(taskId);// 任务ID（必要参数）
		pageParamVo.setBusinessKey(businessKey);
		ProcessInstance processInstance = flowService.findProcessInstanceByTaskId(ids[0]);
		Assert.notNull(processInstance, "流程实例为空");

		pageParamVo.setProcessInstanceId(processInstance.getId());// 流程实例ID
		pageParamVo.setSubmitUrl(WorkFlowConstants.DEFAULT_SUBMIT_URL); // 默认提交方法
		return pageParamVo;
	}

}
