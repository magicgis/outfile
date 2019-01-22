package com.naswork.module.workflow.vo;

import java.util.Comparator;


public class Outcome implements Comparator<Outcome> {
	
	//连线名称
	private String name;
	
	private String color = "blue";
	
	//是否检查待办事项（1：是，0：否）
	private int checkJob;
	
	//下一节点处理人
	private OutcomeUser outcomeUser = new OutcomeUser();
	
	private String beforeEvent;//完成前的事件
	private String afterEvent;//完成任务后的事件
	private String taskDescription;//任务说明
	
	@Override
	public int compare(Outcome o1, Outcome o2) {
		if(o1.getName().hashCode() > o2.getName().hashCode()){
			return 1;
		}else{
			return -1;
		}
	}
	
	public static Outcome createOutcome()
	{
		return new Outcome();
	}
	
	/**
	 * 获取连线
	 * @param name 连线名称
	 * @param checkJob 是否检查待办
	 * @return
	 */
	public static Outcome createOutcome(String name, int checkJob)
	{
		return createOutcome().setName(name).setCheckJob(checkJob);
	}
	
	/**
	 * 获取连线
	 * @param name 连线名称
	 * @param checkJob 是否检查待办（1：是，0：否）
	 * @param outcomeUser 连线指定提交人
	 * @return
	 */
	public static Outcome createOutcome(String name, int checkJob, OutcomeUser outcomeUser){
		return createOutcome(name, checkJob).setOutcomeUser(outcomeUser);
	}
	
	public Outcome(){
	}
	
	public Outcome(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Outcome setName(String name) {
		this.name = name;
		return this;
	}

	public int getCheckJob() {
		return checkJob;
	}

	public Outcome setCheckJob(int checkJob) {
		this.checkJob = checkJob;
		return this;
	}

	public OutcomeUser getOutcomeUser() {
		return outcomeUser;
	}

	public Outcome setOutcomeUser(OutcomeUser outcomeUser) {
		this.outcomeUser = outcomeUser;
		return this;
	}

	public String getColor() {
		return color;
	}

	public Outcome setColor(String color) {
		this.color = color;
		return this;
	}

	public String getBeforeEvent() {
		return beforeEvent;
	}

	public Outcome setBeforeEvent(String beforeEvent) {
		this.beforeEvent = beforeEvent;
		return this;
	}

	public String getAfterEvent() {
		return afterEvent;
	}

	public Outcome setAfterEvent(String afterEvent) {
		this.afterEvent = afterEvent;
		return this;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public Outcome setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
		return this;
	}
	
	public PageParamVo addTo(PageParamVo pageParam){
		return pageParam.addOutCome(this);
	}
}
