package com.naswork.model;

import java.math.BigDecimal;
import java.util.Date;

public class Jbpm4Task {
	private String userName;
	
	private Integer relationId;
	
	private String tableName;
	
	private String deploymentName;
	
	private String taskNumber;
	
    private String id;

    private String class_;

    private Long dbversion;

    private String name;

    private String descr;

    private String state;

    private String susphiststate;

    private String assignee;

    private String form;

    private Long priority;

    private Date create;

    private Date duedate;

    private Long progress;

    private Short signalling;

    private String executionId;

    private String activityName;

    private Short hasvars;

    private BigDecimal supertask;

    private BigDecimal execution;

    private BigDecimal procinst;

    private BigDecimal swimlane;

    private String taskdefname;

    private Integer ywTableId;

    private Integer ywTableElementId;

    private String ids;
    
    private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIds() {
		return ids;
	}

	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getId() {
		return id;
	}

	public String getDeploymentName() {
		return deploymentName;
	}

	public void setDeploymentName(String deploymentName) {
		this.deploymentName = deploymentName;
	}


	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClass_() {
        return class_;
    }

    public void setClass_(String class_) {
        this.class_ = class_;
    }

    public Long getDbversion() {
        return dbversion;
    }

    public void setDbversion(Long dbversion) {
        this.dbversion = dbversion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSusphiststate() {
        return susphiststate;
    }

    public void setSusphiststate(String susphiststate) {
        this.susphiststate = susphiststate;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }

    public Short getSignalling() {
        return signalling;
    }

    public void setSignalling(Short signalling) {
        this.signalling = signalling;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Short getHasvars() {
        return hasvars;
    }

    public void setHasvars(Short hasvars) {
        this.hasvars = hasvars;
    }

    public BigDecimal getSupertask() {
        return supertask;
    }

    public void setSupertask(BigDecimal supertask) {
        this.supertask = supertask;
    }

    public BigDecimal getExecution() {
        return execution;
    }

    public void setExecution(BigDecimal execution) {
        this.execution = execution;
    }

    public BigDecimal getProcinst() {
        return procinst;
    }

    public void setProcinst(BigDecimal procinst) {
        this.procinst = procinst;
    }

    public BigDecimal getSwimlane() {
        return swimlane;
    }

    public void setSwimlane(BigDecimal swimlane) {
        this.swimlane = swimlane;
    }

    public String getTaskdefname() {
        return taskdefname;
    }

    public void setTaskdefname(String taskdefname) {
        this.taskdefname = taskdefname;
    }

    public Integer getYwTableId() {
        return ywTableId;
    }

    public void setYwTableId(Integer ywTableId) {
        this.ywTableId = ywTableId;
    }

    public Integer getYwTableElementId() {
        return ywTableElementId;
    }

    public void setYwTableElementId(Integer ywTableElementId) {
        this.ywTableElementId = ywTableElementId;
    }
}