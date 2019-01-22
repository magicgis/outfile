package com.naswork.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @since 2016年05月18日 18:52:45
 * @author auto
 * @version v1.0
 */
public class Jbpm4Jbyj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 经办意见主键
	 */
	private String jbyjId;
	/**
	 * 流程实例ID
	 */
	private String processinstanceId;
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 业务主键
	 */
	private String businessKey;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 处理结果
	 */
	private String outcome;
	/**
	 * 经办意见
	 */
	private String jbyj;
	/**
	 * 用户姓名姓名
	 */
	private String userName;
	/**
	 * 用户角色ID
	 */
	private String roleId;
	/**
	 * 用户角色名称
	 */
	private String roleName;
	/**
	 * 任务节点名称
	 */
	private String taskName;
	/**
	 * 任务详情页面，用于历史流程
	 */
	private String taskInfoUrl;
	/**
	 * 任务节点名称首字母拼音
	 */
	private String taskSzmpy;

	public String getJbyjId() {
		return jbyjId;
	}

	public String getProcessinstanceId() {
		return processinstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public String getUserId() {
		return userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getOutcome() {
		return outcome;
	}

	public String getJbyj() {
		return jbyj;
	}

	public String getUserName() {
		return userName;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getTaskInfoUrl() {
		return taskInfoUrl;
	}

	public String getTaskSzmpy() {
		return taskSzmpy;
	}

	public void setJbyjId(String jbyjId) {
		this.jbyjId = jbyjId;
	}

	public void setProcessinstanceId(String processinstanceId) {
		this.processinstanceId = processinstanceId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public void setJbyj(String jbyj) {
		this.jbyj = jbyj;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setTaskInfoUrl(String taskInfoUrl) {
		this.taskInfoUrl = taskInfoUrl;
	}

	public void setTaskSzmpy(String taskSzmpy) {
		this.taskSzmpy = taskSzmpy;
	}
	
}