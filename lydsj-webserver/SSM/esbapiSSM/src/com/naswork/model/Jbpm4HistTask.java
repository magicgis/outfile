package com.naswork.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @since 2016年04月28日 16:22:01
 * @author Giam
 * @version v1.0
 */
public class Jbpm4HistTask implements Serializable {

	
	private Integer dbid;
	
	private Integer dbversion;
	
	private String execution;
	
	private String outcome;
	
	private String assignee;
	
	private Integer priority;
	
	private String state;
	
	private Date create;
	
	private Date end;
	
	private Integer duration;
	
	private Integer nextidx;
	
	private Integer supertask;
	
	private String activityName;
	
	private String procdefId;
	
	private String objName;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getProcdefId() {
		return procdefId;
	}

	public void setProcdefId(String procdefId) {
		this.procdefId = procdefId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	
	public String getExecution() {
		return execution;
	}

	public void setExecution(String execution) {
		this.execution = execution;
	}
	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public Integer getDbversion() {
		return dbversion;
	}

	public void setDbversion(Integer dbversion) {
		this.dbversion = dbversion;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getNextidx() {
		return nextidx;
	}

	public void setNextidx(Integer nextidx) {
		this.nextidx = nextidx;
	}

	public Integer getSupertask() {
		return supertask;
	}

	public void setSupertask(Integer supertask) {
		this.supertask = supertask;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
}