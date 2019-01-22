/**
 * 
 */
package com.naswork.service.impl;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jbpm.api.Deployment;
import org.jbpm.api.DeploymentQuery;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.history.HistoryActivityInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Participation;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.naswork.dao.FlowDao;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.OrderApprovalDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Jbpm4HistTask;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.OrderApproval;
import com.naswork.service.FlowService;
import com.naswork.service.Jbpm4HistTaskService;
import com.naswork.utils.GetPinyin;
import com.naswork.utils.SpringUtils;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

/**
 * @since 2016年12月16日 
 * @author Giam
 * @version v1.0
 */
/**
@Author: Modify by white
@DateTime: 2018/10/30 10:26
@Description: 修改领导审核人
*/
@Service("flowService")
public class FlowServiceImpl implements FlowService {
	protected Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "processEngine")
	protected ProcessEngine processEngine;

	@Resource(name = "repositoryService")
	protected RepositoryService repositoryService;

	@Resource(name = "taskService")
	protected TaskService taskService;

	@Resource(name = "executionService")
	protected ExecutionService executionService;

	@Resource(name = "historyService")
	protected HistoryService historyService;

	@Resource(name = "identityService")
	protected IdentityService identityService;

	@Resource
	private FlowDao flowDao;
	
	@Resource
	private Jbpm4JbyjDao jbyjDao;
	
	@Resource
	private OrderApprovalDao orderApprovalDao;
	
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;

	@Resource(name = "jbpm4HistTaskService")
	protected Jbpm4HistTaskService jbpm4HistTaskService;

	@Override
	public String findSpztByBusinessKey(String businessKey) {
		String[] businessKeyArr = businessKey.split("\\.");
		Assert.isTrue(businessKeyArr.length == 3, "业务主键不符合要求,表名.主键列名.主键值");
		PageData pd = new PageData();
		pd.put("tableName", businessKeyArr[0]);
		pd.put("PkColumn", businessKeyArr[1]);
		pd.put("id", businessKeyArr[2]);
		Map<String, Object> map = flowDao.findObjectMapByBusinessKey(pd);
		if (map == null)
			return null;
		return  map.get("SPZT").toString();
	}

	@Override
	public Task findTaskByBusinessKey(String businessKey) {
		ProcessInstance processInstance = findProcessInstanceByBusinessKey(businessKey);
		return findTaskByProcessInstanceId(processInstance.getId());
	}

	@Override
	public Task findTaskByTaskId(String taskId) {
		return taskService.getTask(taskId);
	}

	@Override
	public ProcessInstance findProcessInstanceByTaskId(String taskId) {
		Task task = findTaskByTaskId(taskId);
		Assert.notNull(task, "任务不存在");
		return findProcessInstanceByProcessInstanceId(task.getExecutionId());
	}

	@Override
	public ProcessInstance findProcessInstanceByBusinessKey(String businessKey) {
		return executionService.createProcessInstanceQuery()
				.processInstanceKey(businessKey).uniqueResult();
	}

	@Override
	public ProcessInstance findProcessInstanceByProcessInstanceId(
			String processInstanceId) {
		return executionService.findProcessInstanceById(processInstanceId);
	}

	@Override
	public String findBusinessKeyByTaskId(String taskId) {
		return findBusinessKeyByTaskId(taskId, false);
	}

	@Override
	public String findBusinessKeyByTaskId(String taskId, boolean history) {
		if (history) {
			HistoryProcessInstance historyProcessInstance = findHistoryProcessInstanceByTaskId(taskId);
			return historyProcessInstance.getKey();
		} else {
			ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);
			return processInstance.getKey();
		}
	}

	@Override
	public HistoryProcessInstance findHistoryProcessInstanceByTaskId(
			String taskId) {
		HistoryTask task = findHistoryTaskByTaskId(taskId);
		Assert.notNull(task, "历史任务不存在");
		return findHistoryProcessInstanceByProcessInstanceId(task
				.getExecutionId());
	}

	@Override
	public HistoryProcessInstance findHistoryProcessInstanceByProcessInstanceId(
			String processInstanceId) {
		return historyService.createHistoryProcessInstanceQuery()
				.processInstanceId(processInstanceId).uniqueResult();
	}

	@Override
	public HistoryTask findHistoryTaskByTaskId(String taskId) {
		return historyService.createHistoryTaskQuery().taskId(taskId)
				.uniqueResult();
	}

	@Override
	public String findBusinessKeyByProcessInstanceId(String processInstanceId) {
		ProcessInstance processInstance = findProcessInstanceByProcessInstanceId(processInstanceId);
		return processInstance.getKey();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void completeTask(String taskId, String outcome, String assignee,
			String comment, String taskDescription, String beforeEvent,
			String afterEvent, String taskInfoUrl,String dbids) throws Exception {
		if(null!=dbids&&!"".equals(dbids)){
		String[] taskIds=dbids.split(",");
		Map<String,String> map=new HashMap<String, String>();
		for (int i = 0; i < taskIds.length; i++) {
			String desc=null;
			String userId=null;
//			OrderApproval approval=orderApprovalDao.selectByPrimaryKey(Integer.parseInt(approvalIds[i]));
//			Task task=findTaskByProcessInstanceId(approval.getProcessInstanceId());
//			
//			if(null==task||null!=map.get(approval.getProcessInstanceId())){
//			continue;	
//			}else{
//				map.put(approval.getProcessInstanceId(), approval.getProcessInstanceId());
//			}
//			taskId=task.getId();
//			String[] taskInfoUrls=taskInfoUrl.split("=");
//			taskInfoUrl=taskInfoUrls[0]+"="+approval.getClientOrderElementId();
			taskId=taskIds[i];
			Jbpm4Task jbpm4Task=jbpm4TaskDao.selectByPrimaryKey(new BigDecimal(taskIds[i]));
			String selectId="";
			if(null!=jbpm4Task.getRelationId()){
				selectId=jbpm4Task.getRelationId().toString();
			}else{
				selectId=jbpm4Task.getYwTableElementId().toString();
			}
			String[] oneline=taskInfoUrl.split("&");
			String lastline="";
			if(oneline.length>1){
				 lastline="&"+oneline[1];
			}
			String[] twoline=oneline[0].split("=");
			taskInfoUrl=twoline[0]+"="+selectId+lastline;
		Map<String, Object> variables = new HashMap<String, Object>();
		// 根据任务ID查询任务
		Task task = findTaskByTaskId(taskId);
		Assert.notNull(task, "任务已经不存在了");
		String businessKey = findBusinessKeyByTaskId(task.getId());
		String processInstanceId = task.getExecutionId();// 在本系统中，执行ID和流程实例ID是相同的

		// 先执行提交前方法
		if (StringUtils.isNotBlank(beforeEvent)) {
			logger.debug("定义了完成任务前事件:" + beforeEvent);
			try {
//				taskId=approvalIds[i];
				Map<String, Object> returnvariables = (Map<String, Object>) executeBefore(beforeEvent, businessKey, taskId, outcome, assignee,comment);
				if (returnvariables != null && !returnvariables.isEmpty())
					variables.putAll(returnvariables);
				Object object=variables.get("assignee");
				Object object2=variables.get("desc");
				if(null!=object){
					userId=object.toString();
				}
				if(null!=object2){
					desc=object2.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("执行完成任务前的事件出错", e);
				throw e;
			}
		}
//		taskId=task.getId();
		// 获取流程实例ID
		taskService.addTaskComment(taskId, comment);// 保存任务经办记录
		// 存多一份到系统自己的表中
		 UserVo user = ContextHolder.getCurrentUser();
		 Jbpm4Jbyj jbyj = new Jbpm4Jbyj();
			jbyj.setProcessinstanceId(processInstanceId);
			jbyj.setTaskId(task.getId());
			jbyj.setBusinessKey(businessKey);
			jbyj.setUserId(user.getUserId());
			jbyj.setCreateTime(new Date());
			jbyj.setOutcome(outcome);
			jbyj.setUserName(user.getUserName());
			jbyj.setRoleId(String.valueOf(user.getRoleId()));
			jbyj.setTaskName(task.getActivityName());// 任务名称
			jbyj.setTaskSzmpy(GetPinyin.getPinYinHeadChar(task.getActivityName()));
			jbyj.setTaskInfoUrl(taskInfoUrl);
			
			Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findGyJbyjByTaskId(task.getId());
			 if(null!=jbpm4Jbyj){
				 jbyj.setJbyjId(jbpm4Jbyj.getJbyjId());
				 jbyjDao.update(jbyj);
			 }else{
				// jbyj.setJbyj(comment);
					jbyjDao.insert(jbyj);
			 }
		
		Jbpm4Task jbpm4Task2=jbpm4TaskDao.selectByExecutionId(processInstanceId);

		taskService.completeTask(taskId, outcome, variables);
		ProcessInstance processInstance = findProcessInstanceByProcessInstanceId(processInstanceId);
		
		jbpm4Task2.setExecutionId(processInstanceId);
		jbpm4TaskDao.updateByExecutionId(jbpm4Task2);
		jbpm4TaskDao.updateJbpm4HistTask(jbpm4Task2);
		jbpm4TaskDao.updateJbpm4HistActinst(jbpm4Task2);
		jbpm4Task2.setId(taskId);
		if (!"".equals(jbpm4Task2.getMessage())) {
			jbpm4Task2.setMessage(jbpm4Task2.getMessage());
		}
		jbpm4TaskDao.updateMessage(jbpm4Task2);
		
		
		if (processInstance != null) {// 还存在流程实例说明流程未结束
			task = findTaskByProcessInstanceId(processInstanceId);// 根据流程实例ID查询下一个任务
			if (StringUtils.isNotBlank(taskDescription)) {// 如果任务描述不为空
				if(null!=desc){
					task.setDescription(desc);
				}else{
				task.setDescription(taskDescription);
				}
			} else {
				task.setDescription(task.getActivityName());// 描述默认是节点名称
			}

			// 设置任务处理人或者候选者
			Assert.notNull(assignee, "任务经办人为空");
			if (assignee.indexOf(";") != -1) {
				String[] ids = assignee.split(";");
				for (String id : ids) {
					taskService.addTaskParticipatingUser(task.getId(), id,Participation.CANDIDATE);
				}
			} else {
				if(null!=userId){
					taskService.assignTask(task.getId(), userId);
					taskService.addTaskParticipatingUser(task.getId(), userId,Participation.OWNER);
				}else{
					taskService.assignTask(task.getId(), assignee);
					taskService.addTaskParticipatingUser(task.getId(), assignee,Participation.OWNER);
				}
			}
			if (!"".equals(comment)) {
				jbpm4Task2.setMessage(comment);
			}
			jbpm4Task2.setId(task.getId());
			jbpm4TaskDao.updateTaskMessage(jbpm4Task2);
		}

		// 执行提交后方法
//		if (StringUtils.isNotBlank(afterEvent)) {
//			try {
//				executeAfter(afterEvent, businessKey,dbids);
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("执行完成任务后的事件出错", e);
//				throw e;
//			}
//		}
		}
		}else{
			String desc=null;
			String userId=null;
			Map<String, Object> variables = new HashMap<String, Object>();
			// 根据任务ID查询任务
			Task task = findTaskByTaskId(taskId);
			Assert.notNull(task, "任务已经不存在了");
			String businessKey = findBusinessKeyByTaskId(task.getId());
			String processInstanceId = task.getExecutionId();// 在本系统中，执行ID和流程实例ID是相同的

			// 先执行提交前方法
			if (StringUtils.isNotBlank(beforeEvent)) {
				logger.debug("定义了完成任务前事件:" + beforeEvent);
				try {
					Map<String, Object> returnvariables = (Map<String, Object>) executeBefore(beforeEvent, businessKey, taskId, outcome, assignee,comment);
					if (returnvariables != null && !returnvariables.isEmpty())
						variables.putAll(returnvariables);
					Object object=variables.get("assignee");
					Object object2=variables.get("desc");
					if(null!=object){
						userId=object.toString();
					}
					if(null!=object2){
						desc=object2.toString();
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("执行完成任务前的事件出错", e);
					throw e;
				}
			}
			taskId=task.getId();
			// 获取流程实例ID
			taskService.addTaskComment(taskId, comment);// 保存任务经办记录
			// 存多一份到系统自己的表中
			 UserVo user = ContextHolder.getCurrentUser();
			Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findGyJbyjByTaskId(task.getId());
			 if(null!=jbpm4Jbyj){
				// jbpm4Jbyj.setJbyj(comment);
				 jbyjDao.update(jbpm4Jbyj);
			 }else{
					Jbpm4Jbyj jbyj = new Jbpm4Jbyj();
					jbyj.setProcessinstanceId(processInstanceId);
					jbyj.setTaskId(task.getId());
					jbyj.setBusinessKey(businessKey);
					jbyj.setUserId(user.getUserId());
					jbyj.setCreateTime(new Date());
					jbyj.setOutcome(outcome);
					//jbyj.setJbyj(comment);
					jbyj.setUserName(user.getUserName());
					jbyj.setRoleId(String.valueOf(user.getRoleId()));
					jbyj.setTaskName(task.getActivityName());// 任务名称
					jbyj.setTaskSzmpy(GetPinyin.getPinYinHeadChar(task.getActivityName()));
					jbyj.setTaskInfoUrl(taskInfoUrl);
					jbyjDao.insert(jbyj);
			 }

			Jbpm4Task jbpm4Task2=jbpm4TaskDao.selectByExecutionId(processInstanceId);

			taskService.completeTask(taskId, outcome, variables);
			ProcessInstance processInstance = findProcessInstanceByProcessInstanceId(processInstanceId);
			
			jbpm4Task2.setExecutionId(processInstanceId);
			jbpm4TaskDao.updateByExecutionId(jbpm4Task2);
			jbpm4TaskDao.updateJbpm4HistTask(jbpm4Task2);
			jbpm4TaskDao.updateJbpm4HistActinst(jbpm4Task2);
			if (processInstance != null) {// 还存在流程实例说明流程未结束
				task = findTaskByProcessInstanceId(processInstanceId);// 根据流程实例ID查询下一个任务
				if (StringUtils.isNotBlank(taskDescription)) {// 如果任务描述不为空
					if(null!=desc){
						task.setDescription(desc);
					}else{
					task.setDescription(taskDescription);
					}
				} else {
					task.setDescription(task.getActivityName());// 描述默认是节点名称
				}

				// 设置任务处理人或者候选者
				Assert.notNull(assignee, "任务经办人为空");
				if (assignee.indexOf(";") != -1) {
					String[] ids = assignee.split(";");
					for (String id : ids) {
						taskService.addTaskParticipatingUser(task.getId(), id,Participation.CANDIDATE);
					}
				} else {
					if(null!=userId){
						taskService.assignTask(task.getId(), userId);
						taskService.addTaskParticipatingUser(task.getId(), userId,Participation.OWNER);
					}else{
					taskService.assignTask(task.getId(), assignee);
					taskService.addTaskParticipatingUser(task.getId(), assignee,Participation.OWNER);
					}
				}
			}

			// 执行提交后方法
//			if (StringUtils.isNotBlank(afterEvent)) {
//				try {
//					executeAfter(afterEvent, businessKey,dbids);
//				} catch (Exception e) {
//					e.printStackTrace();
//					logger.error("执行完成任务后的事件出错", e);
//					throw e;
//				}
//			}
		}
	}

	@Override
	public Task findTaskByProcessInstanceId(String processInstanceId) {
		return taskService.createTaskQuery()
				.processInstanceId(processInstanceId).uniqueResult();
	}

	/**
	 * 执行完成任务前方法
	 * 
	 * @param eventName
	 * @param businessKey
	 * @param taskId
	 * @since 2015年9月24日 下午2:20:15
	 * @author chenguojun(mailto:chengj@everygold.com)
	 * @version v1.0
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private Object executeBefore(String eventName, String businessKey,
			String taskId, String outcome, String assignee, String comment)
			throws Exception {
		// 拆分eventName,以#分隔,第一个参数是springbeanid,第二个参数是方法名
		String[] params = eventName.split("#");
		String springbeanid = params[0];
		String functionName = params[1];
		if (StringUtils.isNotBlank(springbeanid)
				&& StringUtils.isNotBlank(functionName)) {
			Object object = SpringUtils.getBean(springbeanid);
			Class[] parameterTypes = new Class[5];
			parameterTypes[0] = String.class;
			parameterTypes[1] = String.class;
			parameterTypes[2] = String.class;
			parameterTypes[3] = String.class;
			parameterTypes[4] = String.class;
			Method method = object.getClass().getMethod(functionName,
					parameterTypes);
			return method.invoke(object, new Object[] { businessKey, taskId,
					outcome, assignee, comment });
		} else {
			logger.debug("由于springbeanid为空或者方法名为空,所以忽略事件,不执行");
		}
		return null;
	}

	/**
	 * 执行任务完成后方法
	 * 
	 * @param eventName
	 * @param taskId
	 * @param outcome
	 * @param assignee
	 * @param comment
	 * @return
	 * @throws Exception
	 * @since 2016年5月18日 下午5:41:21
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	private void executeAfter(String eventName, String businessKey,String ids)
			throws Exception {
		// 拆分eventName,以#分隔,第一个参数是springbeanid,第二个参数是方法名
		String[] params = eventName.split("#");
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
			method.invoke(object, new Object[] { businessKey,ids });
		} else {
			logger.debug("由于springbeanid为空或者方法名为空,所以忽略事件,不执行");
		}
	}

	@Override
	public ProcessInstance startProcessInstance(String ProcessDefinitionKey,String businessKey, Map<String, Object> variables) {
		ProcessInstance processInstance = executionService.startProcessInstanceByKey(ProcessDefinitionKey, variables,businessKey);

		return processInstance;
	}

	/**
	 * 启动流程
	 * 
	 * @param processDefinitionKey
	 *            流程KEY
	 * @param buninessKey
	 *            业务KEY
	 * @param assignees
	 *            流程任务办理人
	 * @param taskDescription
	 *            流程描述信息
	 * @param variables
	 *            流程参数
	 * @since 2016年5月21日 下午9:11:45
	 * @author yef<yef@everygold.com>
	 * @version v1.0
	 */
	public void startProcessInstance(String processDefinitionKey,
			String buninessKey, String assignees, String taskDescription,
			Map<String, Object> variables) {
		// 启动流程
		ProcessInstance instance = startProcessInstance(processDefinitionKey,
				buninessKey, variables);

		// 为流程任务设置办理人
		Task task = findTaskByProcessInstanceId(instance.getId());

		if (StringUtils.isNotEmpty(taskDescription)) {
			task.setDescription(taskDescription);
		}

		String taskId = task.getId();
		if (assignees.indexOf(";") != -1) {
			String[] arr = assignees.split(";");
			for (String sfId : arr) {
				taskService.addTaskParticipatingUser(taskId, sfId,
						Participation.CANDIDATE);
			}
		} else {
			taskService.assignTask(taskId, assignees);
		}
	}

	@Override
	public ProcessDefinition findProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) {
		return repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).uniqueResult();
	}

	@Override
	public ProcessDefinition findProcessDefinitionByProcessInstanceId(
			String processInstanceId) {
		ProcessInstance processInstance = findProcessInstanceByProcessInstanceId(processInstanceId);
		return findProcessDefinitionByProcessDefinitionId(processInstance
				.getProcessDefinitionId());
	}

	@Override
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);
		return findProcessDefinitionByProcessDefinitionId(processInstance
				.getProcessDefinitionId());
	}

	@Override
	public ProcessDefinition findProcessDefinitionByBusinessKey(
			String businessKey) {
		ProcessInstance processInstance = findProcessInstanceByBusinessKey(businessKey);
		return findProcessDefinitionByProcessDefinitionId(processInstance
				.getProcessDefinitionId());
	}

	@Override
	public ProcessDefinition findHistoryProcessDefinitionByBusinessKey(
			String businessKey) {
		HistoryProcessInstance processInstance = findHistoryProcessInstanceByBusinessKey(businessKey);
		return findProcessDefinitionByProcessDefinitionId(processInstance
				.getProcessDefinitionId());
	}

	@Override
	public String getIdFromBusinessKey(String businessKey) {
		if (businessKey.indexOf(".") != -1) {
			return businessKey.substring(businessKey.lastIndexOf(".") + 1);
		}
		return null;
	}

	@Override
	public Deployment findDeploymentByDeploymentId(String deploymentId) {
		return repositoryService.createDeploymentQuery()
				.deploymentId(deploymentId).uniqueResult();
	}

	@Override
	public Deployment findDeploymentByProcessInstanceId(String processInstanceId) {
		ProcessDefinition processDefinition = findProcessDefinitionByProcessInstanceId(processInstanceId);
		return findDeploymentByDeploymentId(processDefinition.getDeploymentId());
	}

	@Override
	public Deployment findDeploymentByTaskId(String taskId) {
		ProcessDefinition processDefinition = findProcessDefinitionByTaskId(taskId);
		return findDeploymentByDeploymentId(processDefinition.getDeploymentId());
	}

	@Override
	public Deployment findDeploymentByBusinessKey(String businessKey) {
		ProcessDefinition processDefinition = findProcessDefinitionByBusinessKey(businessKey);
		return findDeploymentByDeploymentId(processDefinition.getDeploymentId());
	}

	@Override
	public InputStream findImageInputStream(String processDefinitionId) {
		ProcessDefinition processDefinition = findProcessDefinitionByProcessDefinitionId(processDefinitionId);
		return repositoryService.getResourceAsStream(
				processDefinition.getDeploymentId(),
				processDefinition.getImageResourceName());
	}

	@Override
	public void deleteDeploymentCascadeByDeploymentId(String deploymentId) {
		repositoryService.deleteDeploymentCascade(deploymentId);
	}

	@Override
	public void deleteDeploymentByDeploymentId(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId);
	}

	@Override
	public List<Participation> findCandidateUsers(String taskId) {
		List<Participation> participations = taskService
				.getTaskParticipations(taskId);
		List<Participation> candidateUsers = new ArrayList<Participation>();
		for (Participation participation : participations) {
			if (participation.getType().equals(Participation.CANDIDATE)) {
				candidateUsers.add(participation);
			}
		}
		return candidateUsers;
	}

	@Override
	public Object getVariable(String taskId, String variableName) {
		return taskService.getVariable(taskId, variableName);
	}

	@Override
	public ActivityCoordinates getActivityCoordinatesByTaskId(String taskId) {
		ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);
		return getActivityCoordinatesByProcessInstanceId(processInstance
				.getId());
	}

	@Override
	public List<ActivityCoordinates> getHisActivityCoordinatesByTaskId(
			String taskId) {
		ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);
		return getHisActivityCoordinatesByProcessInstanceId(processInstance
				.getId());
	}

	@Override
	public ActivityCoordinates getActivityCoordinatesByProcessInstanceId(
			String processInstanceId) {
		ProcessInstance pi = findProcessInstanceByProcessInstanceId(processInstanceId);
		Set<String> activies = pi.findActiveActivityNames();
		String acName = activies.iterator().next();
		ActivityCoordinates ac = repositoryService.getActivityCoordinates(
				pi.getProcessDefinitionId(), acName);
		return ac;
	}

	@Override
	public List<ActivityCoordinates> getHisActivityCoordinatesByProcessInstanceId(
			String processInstanceId) {
		List<HistoryActivityInstance> hais = historyService
				.createHistoryActivityInstanceQuery()
				.processInstanceId(processInstanceId).list();
		List<ActivityCoordinates> list = new ArrayList<ActivityCoordinates>();
		// 获取流程定义ID
		String defId = findProcessDefinitionByProcessInstanceId(
				processInstanceId).getId();
		// 获取当前节点用以排除
		ActivityCoordinates current = getActivityCoordinatesByProcessInstanceId(processInstanceId);
		for (HistoryActivityInstance hai : hais) {
			ActivityCoordinates ac = repositoryService.getActivityCoordinates(
					defId, hai.getActivityName());
			int x = ac.getX();
			int y = ac.getY();
			// 排除当前节点
			if (x != current.getX() && y != current.getY()) {
				boolean exist = false;
				// 去除重复节点（审批回退等操作造成）
				for (ActivityCoordinates act : list) {
					if (act.getX() == x && act.getY() == y) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					list.add(ac);
				}
			}
		}
		return list;
	}

	@Override
	public ActivityCoordinates getActivityCoordinatesByBusinessKey(
			String businessKey) {
		ProcessInstance processInstance = findProcessInstanceByBusinessKey(businessKey);
		return getActivityCoordinatesByProcessInstanceId(processInstance
				.getId());
	}

	@Override
	public List<ActivityCoordinates> getHisActivityCoordinatesByBusinessKey(
			String businessKey) {
		ProcessInstance processInstance = findProcessInstanceByBusinessKey(businessKey);
		return getHisActivityCoordinatesByProcessInstanceId(processInstance
				.getId());
	}

	@Override
	public HistoryProcessInstance findHistoryProcessInstanceByBusinessKey(
			String businessKey) {
		return historyService.createHistoryProcessInstanceQuery()
				.processInstanceKey(businessKey).uniqueResult();
	}

	@Override
	public ProcessDefinition findHistoryProcessDefinitionByTaskId(String taskId) {
		HistoryProcessInstance processInstance = findHistoryProcessInstanceByTaskId(taskId);
		return findProcessDefinitionByProcessDefinitionId(processInstance
				.getProcessDefinitionId());
	}

	@Override
	public Map<String, String> convertOutcomeUserParams(
			List<Map<String, String>> params) {
		Map<String, String> swjggws = new LinkedHashMap<String, String>();
		for (Map<String, String> map : params) {
			String key = map.get("swjgDm");
			// 如果为空
			if (StringUtils.isBlank(key))
				continue;

			if (swjggws.containsKey(key)) {
				String value = swjggws.get(key);
				if (StringUtils.isNotBlank(value)) {
					swjggws.put(key, value + "," + map.get("gwDm"));
				}
			} else {
				swjggws.put(key, map.get("gwDm"));
			}

		}
		return swjggws;
	}

	@Override
	public void claimTask(String taskIds) {
		String[] idarr = taskIds.split(",");
		for (String taskId : idarr) {
			if (StringUtils.isNotEmpty(taskId)) {
				taskService.takeTask(taskId, ContextHolder.getCurrentUser().getUserId());
			}
		}
	}

	@Override
	public void findGroupTaskListPage(PageModel page,Map<String, String> param, GridSort sort) {
		TaskQuery query = taskService.createTaskQuery().candidate(ContextHolder.getCurrentUser().getUserId());
		long recordCount = query.count();
		page.setRecordCount(Long.valueOf(recordCount).intValue());
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		if (recordCount > 0) {
			List<Task> list = query.orderAsc("createTime").page(page.getCurrentIndex() - 1, page.getPageSize())	.list();// 执行查询
			for (Task task : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", task.getId());
				Deployment deployment = findDeploymentByTaskId(task.getId());
				map.put("deploymentName", deployment.getName());
				map.put("name", task.getDescription());
				ls.add(map);
			}
		}
		page.setEntities(ls);
	}

	@Override
	public void findMyTaskPage(PageModel page, Map<String, String> param,
			GridSort sort) {

		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String userCode = (String) param.get("userCode");

		TaskQuery query = taskService.createTaskQuery();
		query.assignee(userCode);

		long recordCount = query.count();
		page.setRecordCount(Long.valueOf(recordCount).intValue());
		List<Task> taskList = query
				.page(page.getCurrentIndex() - 1, page.getPageSize())
				.orderDesc("createTime").list();

		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		for (Task task : taskList) {
			Map<String, Object> map = new HashMap<String, Object>();
			Deployment deployment = findDeploymentByTaskId(task.getId());
			map.put("id", task.getId());
			map.put("name", task.getName());
			map.put("deploymentName", deployment.getName());
			map.put("form", task.getFormResourceName());
			map.put("description", task.getDescription());
			map.put("createTime", dateFormater.format(task.getCreateTime()));
			map.put("taskName", task.getName()+" － "+task.getDescription());

			ls.add(map);
		}
		page.setEntities(ls);
	}

	@Override
	public void findMyFinishedData(PageModel page, Map<String, String> param,
			GridSort sort) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		List<Jbpm4HistTask> resultList = jbpm4HistTaskService.findFinishData(param);

		for (Jbpm4HistTask his : resultList) {

			Map<String, Object> newMap = new HashMap();
			newMap.put("id", his.getDbid());
			newMap.put("name", his.getName());
			newMap.put("curentName", his.getActivityName());
			newMap.put("createTime", dateFormater.format(his.getCreate()));
			newMap.put("endTime", his.getEnd()!=null?dateFormater.format(his.getEnd()):"");
			newList.add(newMap);
		}

		long recordCount = newList.size();
		page.setRecordCount(Long.valueOf(recordCount).intValue());

		if (newList.size() > 0) {
			int rowNumBegin = 0;
			if (page.getPageNo() != 0) {
				rowNumBegin = (page.getPageNo() - 1) * page.getPageSize() + 1;
			} else {
				rowNumBegin = 1;
			}
			int rowNumEnd = page.getPageNo() * page.getPageSize();
			if (rowNumEnd > newList.size()) {
				rowNumEnd = newList.size();
			}
			if (rowNumBegin == newList.size()) {
				returnList.add(newList.get(rowNumBegin - 1));
			} else {
				for (int i = rowNumBegin - 1; i < rowNumEnd; i++) {
					returnList.add(newList.get(i));
				}
			}
		}

		page.setEntities(returnList);
	}

	@Override
	public void findDeploymentPage(PageModel page, Map<String, String> param,
			GridSort sort) {
		DeploymentQuery query = repositoryService.createDeploymentQuery();
		long recordCount = query.count();
		page.setRecordCount(Long.valueOf(recordCount).intValue());
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();

		// 按时间戳倒序
		List<Deployment> list = query
				.page(page.getCurrentIndex() - 1, page.getPageSize())
				.orderDesc("timestamp").list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Deployment dep : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dep.getId());
			map.put("name", dep.getName());
			map.put("time", format.format(dep.getTimestamp()));
			ls.add(map);
		}
		page.setEntities(ls);
	}

	@Override
	public void saveNewDeploye(InputStream inputStream, String deploymentName) {
		// 将InputStream流转化成ZipInputStream流
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		repositoryService.createDeployment()// 创建部署对象
				.addResourcesFromZipInputStream(zipInputStream)// 部署对象流
				.setName(deploymentName)// 部署对象名称
				.setTimestamp(System.currentTimeMillis())// 部署对象的时间戳
				.deploy();// 部署执行

	}

	@Override
	public void deletDeploymentById(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId);
	}

	@Override
	public void findProcessDefinitionPage(PageModel page,
			Map<String, String> param, GridSort sort) {
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery();
		if (StringUtils.isNotEmpty(param.get("id"))) {
			query.deploymentId(param.get("id"));
		}
		if (StringUtils.isNotEmpty(param.get("processDefinitionKey"))) {
			query.processDefinitionKey(param.get("processDefinitionKey"));
		}
		if (StringUtils.isNotEmpty(param.get("processDefinitionName"))) {
			query.processDefinitionNameLike("%"
					+ param.get("processDefinitionName") + "%");
		}
		long recordCount = query.count();
		page.setRecordCount(Long.valueOf(recordCount).intValue());
		List<ProcessDefinition> list = query.page(page.getCurrentIndex() - 1,
				page.getPageSize()).list();
		page.setEntities(list);

		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		for (ProcessDefinition pd : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", pd.getId());
			map.put("name", pd.getName());
			map.put("key", pd.getKey());
			map.put("version", pd.getVersion());
			map.put("diagramResourceName", pd.getImageResourceName());
			map.put("deploymentId", pd.getDeploymentId());
			ls.add(map);
		}
		page.setEntities(ls);
	}

	@Override
	public void deleteFlowByBusinessKey(String businessKey) {
		ProcessInstance processInstance = findProcessInstanceByBusinessKey(businessKey);
		Assert.notNull(processInstance, "找不到业务关键字为"+businessKey+"的流程!");
		deleteFlowByProcessInstanceId(processInstance.getId());
	}

	/**
	 * 获取发起时的任务描述信息
	 * @param buinessKey
	 * @param nsrmc
	 * @param processName
	 * @return
	 * @since 2016年5月28日 上午10:50:48
	 * @author yef<yef@everygold.com>
	 * @version v1.0
	 */
	public String getStartDescription(String buinessKey, String processName) {
		//【发起人】发起【流程名称】
		StringBuilder description = new StringBuilder();
		String desc = "【%s】发起【%s】";
		
		return String.format(desc, ContextHolder.getCurrentUser().getUserName(),processName);
	}
	
	/**
	 * 获取审批时的任务描述信息
	 * @param buinessKey
	 * @param nsrmc
	 * @param processName
	 * @return
	 * @since 2016年5月28日 上午10:51:09
	 * @author yef<yef@everygold.com>
	 * @version v1.0
	 */
	public String getSpDescription(String buinessKey, String nsrmc, String processName) {
		//【发起人】发起【纳税人名称】的【流程名称】，等待您的处理！
		StringBuilder description = new StringBuilder();
		return description.toString();
	}

	@Override
	public void deleteFlowByProcessInstanceId(String processInstanceId) {
		executionService.deleteProcessInstanceCascade(processInstanceId);
	}
	
	@Override
	public List<Jbpm4Jbyj> findCommentByBusinessKey(String businessKey) {
		return jbyjDao.findGyJbyjByBusinessKey(businessKey);
	}

	@Override
	public Jbpm4Jbyj findFirstJbrByBusinessKey(String businessKey) {
		return jbyjDao.findFirstJbrByBusinessKey(businessKey);
	}
	
	@Override
	public List<Map<String, Object>> findUserByRoleid(List<String> roleidList) {
		PageData pd = new PageData();
		pd.put("roleidList", roleidList);
		return flowDao.findUserByRoleid(pd);
	}

	@Override
	public void findSubTaskPage(PageModel page, Map<String, String> param, GridSort sort) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String userCode = (String) param.get("userCode");
//
//		TaskQuery query = taskService.createTaskQuery();
//		query.assignee(userCode);
//
//		long recordCount = query.count();
//		page.setRecordCount(Long.valueOf(recordCount).intValue());
		List<Jbpm4Task> taskList =jbpm4TaskDao.selectByAssigneeAndYwId(page);

		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		for (Jbpm4Task task : taskList) {
			Map<String, Object> map = new HashMap<String, Object>();
			Deployment deployment = findDeploymentByTaskId(task.getId());
			map.put("id", task.getId());
			map.put("name", task.getName());
			map.put("deploymentName", deployment.getName());
			map.put("form", task.getForm());
			map.put("description", task.getDescr());
			map.put("createTime", dateFormater.format(task.getCreate()));
			map.put("taskName", task.getName()+" － "+task.getDescr());
			map.put("ywTableElementId", task.getYwTableElementId());
			map.put("tableName", task.getExecutionId());
			ls.add(map);
		}
		page.setEntities(ls);
	}

	@Override
	public int deleteHIStTask(String execution) {
		return jbpm4TaskDao.deleteHIStTask(execution);
	}

	@Override
	public int deleteHistProcinst(String id) {
		return jbpm4TaskDao.deleteHistProcinst(id);
	}

	@Override
	public int deleteHistActinst(String execution) {
		return jbpm4TaskDao.deleteHistActinst(execution);
	}

	@Override
	public int deleteExecution(String id) {
		return jbpm4TaskDao.deleteExecution(id);
	}
	@Override
	public int deleteByPrimaryKey(BigDecimal dbid) {
		return jbpm4TaskDao.deleteByPrimaryKey(dbid);
	}
	@Override
	public int deleteByParticipation(BigDecimal dbid) {
		return jbpm4TaskDao.deleteByParticipation(dbid);
	}
	@Override
	public Jbpm4Task selectByExecutionId(String executionId) {
		return jbpm4TaskDao.selectByExecutionId(executionId);
	}

	
	@Override
	public List<Jbpm4Jbyj> selectByYwTableElementId(String clientOrderElementId,String processinstanceId) {
		return jbyjDao.selectByYwTableElementId(clientOrderElementId,processinstanceId);
	}

	@Override
	public int updateBytaskName(Jbpm4Jbyj gyJbyj) {
		return jbyjDao.updateBytaskName(gyJbyj);
	}

	@Override
	public List<Jbpm4Task> selectWeatherOrder(Jbpm4Task record) {
		return jbpm4TaskDao.selectWeatherOrder(record);
	}

	@Override
	public Jbpm4Task selectDbversion(Jbpm4Task record) {
		return jbpm4TaskDao.selectDbversion(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(Jbpm4Task record) {
		return jbpm4TaskDao.updateByPrimaryKeySelective(record);
	}
}
