/**
 * 
 */
package com.naswork.module.workflow.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PageParamVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//任务ID
	private String taskId;
	
	//流程实例ID
	private String processInstanceId;
	
	//业务ID
	private String businessKey;
	
	//待办功能
	private List<Job> jobs;
	
	//连线
	private List<Outcome> outcomes;
	
	//提交的URL
	private String submitUrl;
	
	//任务的详情页面
	private String urlTaskInfo;
	
	public PageParamVo()
	{
	}
	
	public PageParamVo(String taskId, List<Outcome> outcomes)
	{
		this.taskId = taskId;
		this.outcomes = outcomes;
	}
	
	public PageParamVo(String taskId, List<Outcome> outcomes, String url, String urlTaskInfo)
	{
		this.taskId = taskId;
		this.outcomes = outcomes;
		this.submitUrl = url;
		this.urlTaskInfo = urlTaskInfo;
	}
	
	public PageParamVo(String taskId, List<Job> jobs, List<Outcome> outcomes, String url, String urlTaskInfo)
	{
		this.taskId = taskId;
		this.jobs = jobs;
		this.outcomes = outcomes;
		this.submitUrl = url;
		this.urlTaskInfo = urlTaskInfo;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<Outcome> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<Outcome> outcomes) {
		this.outcomes = outcomes;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public String getUrlTaskInfo() {
		return urlTaskInfo;
	}

	public void setUrlTaskInfo(String urlTaskInfo) {
		this.urlTaskInfo = urlTaskInfo;
	}
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public PageParamVo addOutCome(Outcome outcome){
		if(this.outcomes==null){
			this.outcomes = new ArrayList<Outcome>();
		}
		this.outcomes.add(outcome);
		return this;
	}
	
	public PageParamVo addOutCome(String name, OutcomeUser outcomeUser, int checkJob){
		Outcome outcome = Outcome.createOutcome(name, checkJob, outcomeUser);
		addOutCome(outcome);
		return this;
	}
	
	public PageParamVo addOutCome(String name, OutcomeUser outcomeUser, int checkJob,String color){
		Outcome outcome = Outcome.createOutcome(name, checkJob, outcomeUser).setColor(color);
		addOutCome(outcome);
		return this;
	}
	
	public PageParamVo addOutCome(String name, OutcomeUser outcomeUser, int checkJob,String color,String taskDescription){
		Outcome outcome = Outcome.createOutcome(name, checkJob, outcomeUser).setColor(color);
		if(StringUtils.isNotBlank(taskDescription))
			outcome.setTaskDescription(taskDescription);
		addOutCome(outcome);
		return this;
	}
	
	public PageParamVo addOutCome(String name, OutcomeUser outcomeUser, int checkJob,String color,String taskDescription,String beforeEvent,String afterEvent){
		Outcome outcome = Outcome.createOutcome(name, checkJob, outcomeUser).setColor(color);
		if(StringUtils.isNotBlank(taskDescription))
			outcome.setTaskDescription(taskDescription);
		if(StringUtils.isNotBlank(beforeEvent))
			outcome.setBeforeEvent(beforeEvent);
		if(StringUtils.isNotBlank(afterEvent))
			outcome.setAfterEvent(afterEvent);
		addOutCome(outcome);
		return this;
	}
	
	public PageParamVo addOutCome(String name, OutcomeUser outcomeUser, int checkJob,String color,String beforeEvent,String afterEvent){
		Outcome outcome = Outcome.createOutcome(name, checkJob, outcomeUser).setColor(color);
		if(StringUtils.isNotBlank(beforeEvent))
			outcome.setBeforeEvent(beforeEvent);
		if(StringUtils.isNotBlank(afterEvent))
			outcome.setAfterEvent(afterEvent);
		addOutCome(outcome);
		return this;
	}
	
	/**
	 * 添加待办事项
	 * @param job
	 * @return
	 */
	public PageParamVo addJob(Job job){
		if(this.jobs == null){
			this.jobs = new ArrayList<Job>();
		}
		this.jobs.add(job);
		return this;
	}
	
	/**
	 * 添加待办事项
	 * @param jobName 待办事项名称
	 * @param status 状态（1：已完成，0：未完成）
	 * @param url 待办事项打开路径
	 * @return
	 */
	public PageParamVo addJob(String jobName, int status, String url){
		Job job = new Job(jobName, status, url);
		return addJob(job);
	}
}
