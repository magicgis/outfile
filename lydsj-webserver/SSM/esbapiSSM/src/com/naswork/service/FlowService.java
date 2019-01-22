/**
 * 
 */
package com.naswork.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.jbpm.api.Deployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.task.Participation;
import org.jbpm.api.task.Task;

import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.RRoleUser;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

/**
 * 工作流
 * 
 * @since 2016年5月18日 下午4:36:35
 * @author auth
 * @version v1.0
 */
public interface FlowService {

	/**
	 * 根据业务关键字查询对应的业务记录审批状态
	 * 
	 * @param businessKey
	 * @return spzt
	 * @since 2016年5月18日 下午4:44:26
	 * @author auth
	 * @version v1.0
	 */
	String findSpztByBusinessKey(String businessKey);

	/**
	 * 根据业务关键字查询当前的任务
	 * 
	 * @param businessKey
	 * @return task
	 * @since 2016年5月18日 下午4:46:58
	 * @author auth
	 * @version v1.0
	 */
	Task findTaskByBusinessKey(String businessKey);

	/**
	 * 根据任务ID查询任务
	 * @param TaskId
	 * @return
	 * @since 2016年5月18日 下午4:50:22
	 * @author auth
	 * @version v1.0
	 */
	Task findTaskByTaskId(String TaskId);
	
	/**
	 * 根据任务ID查询流程实例
	 * 
	 * @param taskId
	 * @return  ProcessInstance
	 * @since 2016年5月18日 下午4:48:16
	 * @author auth
	 * @version v1.0
	 */
	ProcessInstance findProcessInstanceByTaskId(String taskId);
	
	/**
	 * 根据业务关键字查询审批记录
	 * @param businessKey
	 * @return
	 * @since 2016年5月18日 下午7:20:23
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	List<Jbpm4Jbyj> findCommentByBusinessKey(String businessKey);
	
	/**
	 * 根据业务关键字取得第一条经办记录
	 * @param businessKey
	 * @return
	 * @since 2016年5月19日 下午8:00:16
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	Jbpm4Jbyj findFirstJbrByBusinessKey(String businessKey);

	/**
	 * 根据业务关键字查询流程实例
	 * 
	 * @param businessKey
	 * @return ProcessInstance
	 * @since 2016年5月18日 下午4:48:16
	 * @author auth
	 * @version v1.0
	 */
	ProcessInstance findProcessInstanceByBusinessKey(String businessKey);

	/**
	 * 根据流程实例ID查询流程实例
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月18日 下午5:36:07
	 * @author auth
	 * @version v1.0
	 */
	ProcessInstance findProcessInstanceByProcessInstanceId(String processInstanceId);
	
	/**
	 * 根据任务ID查询业务关键字
	 * @param taskId
	 * @return
	 * @since 2016年5月18日 下午5:45:58
	 * @author auth
	 * @version v1.0
	 */
	String findBusinessKeyByTaskId(String taskId);
	
	/**
	 * 根据任务ID查询业务关键字
	 * @param taskId
	 * @param history 历史
	 * @return
	 * @since 2016年5月18日 下午5:45:58
	 * @author auth
	 * @version v1.0
	 */
	String findBusinessKeyByTaskId(String taskId,boolean history);
	
	/**
	 * 根据流程实例ID查询业务关键字
	 * @param taskId
	 * @return
	 * @since 2016年5月18日 下午5:45:58
	 * @author auth
	 * @version v1.0
	 */
	String findBusinessKeyByProcessInstanceId(String processInstanceId);
	
	/**
	 * 完成任务
	 * @param taskId
	 * @param outcome
	 * @param assignee
	 * @param comment
	 * @param taskDescription
	 * @param beforeEvent serviceName#functionName,返回Map<String,Object>
	 * @param afterEvent serviceName#functionName
	 * @since 2016年5月18日 下午5:06:42
	 * @author auth
	 * @version v1.0
	 * @param taskInfoUrl 
	 * @throws Exception 
	 */
	void completeTask(String taskId, String outcome, String assignee,
			String comment,String taskDescription, String beforeEvent, String afterEvent, String taskInfoUrl) throws Exception;

	/**
	 * 根据任务ID查询历史流程实例
	 * @param taskId
	 * @return
	 * @since 2016年5月18日 下午5:54:29
	 * @author auth
	 * @version v1.0
	 */
	HistoryProcessInstance findHistoryProcessInstanceByTaskId(String taskId);

	/**
	 * 根据流程实例ID查询历史流程实例
	 * @param executionId
	 * @return
	 * @since 2016年5月18日 下午7:19:20
	 * @author auth
	 * @version v1.0
	 */
	HistoryProcessInstance findHistoryProcessInstanceByProcessInstanceId(
			String executionId);

	/**
	 * 根据任务ID查询历史任务
	 * @param taskId
	 * @return
	 * @since 2016年5月18日 下午7:19:36
	 * @author auth
	 * @version v1.0
	 */
	HistoryTask findHistoryTaskByTaskId(String taskId);

	/**
	 * 根据流程实例ID查询任务
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月18日 下午7:19:54
	 * @author auth
	 * @version v1.0
	 */
	Task findTaskByProcessInstanceId(String processInstanceId);
	
	/**
	 * 启动流程
	 * @param ProcessDefinitionKey
	 * @param buninessKey
	 * @param variables
	 * @return
	 * @since 2016年5月19日 上午9:34:02
	 * @author auth
	 * @version v1.0
	 */
	ProcessInstance startProcessInstance(String ProcessDefinitionKey,
			String buninessKey, Map<String, Object> variables);
	
	/**
	 * 启动流程
	 * @param processDefinitionKey	流程KEY
	 * @param buninessKey	业务KEY
	 * @param assignees		流程任务办理人
	 * @param taskDescription	流程描述信息
	 * @param variables		流程参数
	 * @since 2016年5月21日 下午9:11:45
	 * @author auth
	 * @version v1.0
	 */
	void startProcessInstance(String processDefinitionKey,
			String buninessKey, String assignees, String taskDescription, Map<String, Object> variables);
	
	/**
	 * 根据流程定义ID取得流程定义
	 * @param processDefinitionId
	 * @return
	 * @since 2016年5月19日 上午10:12:49
	 * @author auth
	 * @version v1.0
	 */
	ProcessDefinition findProcessDefinitionByProcessDefinitionId(String processDefinitionId);
	
	/**
	 * 根据流程实例ID取得流程定义
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月19日 上午10:14:07
	 * @author auth
	 * @version v1.0
	 */
	ProcessDefinition findProcessDefinitionByProcessInstanceId(String processInstanceId);
	
	/**
	 * 根据任务ID取得流程定义
	 * @param taskId
	 * @return
	 * @since 2016年5月19日 上午10:14:07
	 * @author auth
	 * @version v1.0
	 */
	ProcessDefinition findProcessDefinitionByTaskId(String taskId);
	
	/**
	 * 根据业务关键字取得流程定义
	 * @param businessKey
	 * @return
	 * @since 2016年5月19日 上午10:14:58
	 * @author auth
	 * @version v1.0
	 */
	ProcessDefinition findProcessDefinitionByBusinessKey(String businessKey);

	/**
	 *  根据业务关键字查询流程定义，从历史流程实例中查
	 * @param businessKey
	 * @return
	 * @since 2016年5月19日 下午6:44:33
	 * @author auth
	 * @version v1.0
	 */
	ProcessDefinition findHistoryProcessDefinitionByBusinessKey(
			String businessKey);

	/**
	 * 从历史中查询流程定义
	 * @param taskId
	 * @return
	 * @since 2016年5月19日 下午7:43:22
	 * @author auth
	 * @version v1.0
	 */
	ProcessDefinition findHistoryProcessDefinitionByTaskId(String taskId);
	
	/**
	 * 从业务关键字中取得业务主键
	 * @param businessKey
	 * @return
	 * @since 2016年5月19日 上午10:24:39
	 * @author auth
	 * @version v1.0
	 */
	String getIdFromBusinessKey(String businessKey);
	
	/**
	 * 根据部署ID取得部署对象
	 * @param deploymentId
	 * @return
	 * @since 2016年5月19日 上午10:25:46
	 * @author auth
	 * @version v1.0
	 */
	Deployment findDeploymentByDeploymentId(String deploymentId);
	
	/**
	 * 根据流程实例ID取得部署对象
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月19日 上午10:31:57
	 * @author auth
	 * @version v1.0
	 */
	Deployment findDeploymentByProcessInstanceId(String processInstanceId);
	
	/**
	 * 根据任务ID取得部署对象
	 * @param taskId
	 * @return
	 * @since 2016年5月19日 上午10:32:12
	 * @author auth
	 * @version v1.0
	 */
	Deployment findDeploymentByTaskId(String taskId);
	
	/**
	 * 根据业务关键字取得部署对象
	 * @param businessKey
	 * @return
	 * @since 2016年5月19日 上午10:32:22
	 * @author auth
	 * @version v1.0
	 */
	Deployment findDeploymentByBusinessKey(String businessKey);
	
	/**
	 * 根据流程定义ID获得流程图的图片流
	 * @param processDefinitionId
	 * @return
	 * @since 2016年5月19日 上午10:44:04
	 * @author auth
	 * @version v1.0
	 */
	InputStream findImageInputStream(String processDefinitionId);

	/**
	 * 级联删除流程,慎用
	 * @param deploymentId
	 * @since 2016年5月19日 上午10:49:52
	 * @author auth
	 * @version v1.0
	 */
	void deleteDeploymentCascadeByDeploymentId(String deploymentId);

	/**
	 * 根据部署ID删除部署对象（如果已经启动流程就不能删除了）
	 * @param deploymentId
	 * @since 2016年5月19日 上午10:50:22
	 * @author auth
	 * @version v1.0
	 */
	void deleteDeploymentByDeploymentId(String deploymentId);

	/**
	 * 取得任务修候选者
	 * @param taskId
	 * @return
	 * @since 2016年5月19日 上午11:59:40
	 * @author auth
	 * @version v1.0
	 */
	List<Participation> findCandidateUsers(String taskId);

	/**
	 * 获取任务参数
	 * @param taskId
	 * @param startUser
	 * @return
	 * @since 2016年5月19日 下午4:20:19
	 * @author auth
	 * @version v1.0
	 */
	Object getVariable(String taskId, String variableName);

	/**
	 * 根据任务ID查询任务节点坐标信息
	 * @param taskId
	 * @return
	 * @since 2016年5月19日 下午5:21:22
	 * @author auth
	 * @version v1.0
	 */
	ActivityCoordinates getActivityCoordinatesByTaskId(String taskId);

	/**
	 * 根据任务ID查询所有相关历史坐标信息
	 * @param taskId
	 * @return
	 * @since 2016年5月19日 下午5:21:44
	 * @author auth
	 * @version v1.0
	 */
	List<ActivityCoordinates> getHisActivityCoordinatesByTaskId(String taskId);
	
	/**
	 * 根据流程实例ID查询所有历史坐标信息
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月19日 下午5:22:25
	 * @author auth
	 * @version v1.0
	 */
	ActivityCoordinates getActivityCoordinatesByProcessInstanceId(String processInstanceId);

	/**
	 * 根据流程实例ID查询所有相关历史坐标信息
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月19日 下午5:22:14
	 * @author auth
	 * @version v1.0
	 */
	List<ActivityCoordinates> getHisActivityCoordinatesByProcessInstanceId(String processInstanceId);
	
	/**
	 * 根据业务关键字查询所有历史坐标信息
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月19日 下午5:22:25
	 * @author auth
	 * @version v1.0
	 */
	ActivityCoordinates getActivityCoordinatesByBusinessKey(String businessKey);

	/**
	 * 根据业务关键字查询所有相关历史坐标信息
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月19日 下午5:22:14
	 * @author auth
	 * @version v1.0
	 */
	List<ActivityCoordinates> getHisActivityCoordinatesByBusinessKey(String businessKey);
	
	/**
	 * 根据业务关键字查询历史流程实例
	 * @param businessKey
	 * @return
	 * @since 2016年5月19日 下午6:38:45
	 * @author auth
	 * @version v1.0
	 */
	HistoryProcessInstance findHistoryProcessInstanceByBusinessKey(
			String businessKey);

	/**
	 * 加工选人参数
	 * @param params
	 * @return
	 * @since 2016年5月20日 上午11:15:22
	 * @author auth
	 * @version v1.0
	 */
	Map<String, String> convertOutcomeUserParams(List<Map<String, String>> params);

	/**
	 * 拾取任务
	 * @param taskIds
	 * @since 2016年5月22日 上午9:16:52
	 * @author auth
	 * @version v1.0
	 */
	void claimTask(String taskIds);

	/**
	 * 查询组任务数据
	 * @param page
	 * @param map
	 * @param sort
	 * @since 2016年5月22日 上午9:20:27
	 * @author auth
	 * @version v1.0
	 */
	void findGroupTaskListPage(PageModel page, Map<String, String> map,
			GridSort sort);

	/**
	 * 分页查询我的待办
	 * @param page
	 * @param param
	 * @param sort
	 * @since 2016年5月22日 上午9:51:38
	 * @author auth
	 * @version v1.0
	 */
	void findMyTaskPage(PageModel page, Map<String, String> param, GridSort sort);

	/**
	 * 分页查询我的已办
	 * @param page
	 * @param param
	 * @param sort
	 * @since 2016年5月22日 上午9:51:51
	 * @author auth
	 * @version v1.0
	 */
	void findMyFinishedData(PageModel page, Map<String, String> param,
			GridSort sort);

	/**
	 * 分页查询已部署的流程列表
	 * @param page
	 * @param param
	 * @param sort
	 * @since 2016年5月22日 上午9:57:16
	 * @author auth
	 * @version v1.0
	 */
	void findDeploymentPage(PageModel page, Map<String, String> param,GridSort sort);

	/**
	 * 部署新流程
	 * @param inputStream
	 * @param deploymentName
	 * @since 2016年5月22日 上午9:57:34
	 * @author auth
	 * @version v1.0
	 */
	void saveNewDeploye(InputStream inputStream, String deploymentName);

	/**
	 * 删除部署
	 * @param id
	 * @since 2016年5月22日 上午9:57:40
	 * @author auth
	 * @version v1.0
	 */
	void deletDeploymentById(String id);

	/**
	 * 分页查询流程定义列表
	 * @param page
	 * @param param
	 * @param sort
	 * @since 2016年5月22日 上午9:57:56
	 * @author auth
	 * @version v1.0
	 */
	void findProcessDefinitionPage(PageModel page, Map<String, String> param,GridSort sort);
	
	/**
	 * 根据流程实例ID删除流程(慎用)
	 * @param processInstanceId
	 * @since 2016年5月24日 下午2:56:50
	 * @author auth
	 * @version v1.0
	 */
	void deleteFlowByProcessInstanceId(String processInstanceId);
	
	/**
	 * 根据业务关键字删除流程(慎用)
	 * @param businessKey
	 * @since 2016年5月24日 下午3:00:01
	 * @author auth
	 * @version v1.0
	 */
	void deleteFlowByBusinessKey(String businessKey);
	
	/**
	 * 获取发起时的任务描述信息
	 * @param buinessKey
	 * @param nsrmc
	 * @param processName
	 * @return
	 * @since 2016年5月28日 上午10:50:48
	 * @author auth
	 * @version v1.0
	 */
	String getStartDescription(String buinessKey, String processName);
	
	/**
	 * 获取审批时的任务描述信息
	 * @param buinessKey
	 * @param nsrmc
	 * @param processName
	 * @return
	 * @since 2016年5月28日 上午10:51:09
	 * @author auth
	 * @version v1.0
	 */
	String getSpDescription(String buinessKey, String nsrmc, String processName);
	
	/**
	 * 根据角色ID查找相关人员ID
	 * @param roleid
	 * @return
	 */
	public List<Map<String, Object>> findUserByRoleid(List<String> roleidList);
	
	List<ActivityCoordinates> getActivityCoordinatesListByProcessInstanceId(String processInstanceId);
}
