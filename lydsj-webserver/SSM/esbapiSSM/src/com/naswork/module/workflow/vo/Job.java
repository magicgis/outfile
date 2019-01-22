package com.naswork.module.workflow.vo;

import java.util.ArrayList;
import java.util.List;

public class Job  {

	//任务名称
	private String jobName;
	
	//任务图标
	private String image;
	
	//任务状态（0：未完成，1：已完成）
	private int status = 0;
	
	//排序
	private int sort;
	
	//待办页面
	private String url;
	
	//依赖任务
	private List<Job> depends;
	
	//是否可以处理（0：不可进行处理，1：可以进行处理）
	private int canDo = 1;
	
	public Job()
	{
	}
	
	public Job(String jobName, int status, String url)
	{
		this.jobName = jobName;
		this.status = status;
		this.url = url;
	}
	
	public Job(String jobName, String image, int status, int sort, String url, List<Job> depends, int canDo)
	{
		this.jobName = jobName;
		this.image = image;
		this.status = status;
		this.sort = sort;
		this.url = url;
		this.depends = depends;
		this.canDo = canDo;
	}
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<Job> getDepends() {
		return depends;
	}

	public void setDepends(List<Job> depends) {
		this.depends = depends;
	}
	
	public int getCanDo() {
		return canDo;
	}

	public void setCanDo(int canDo) {
		this.canDo = canDo;
	}

	/**
	 * 添加依赖任务
	 * @param job
	 */
	public void addDepend(Job job)
	{
		if (this.depends == null)
		{
			this.setDepends(new ArrayList<Job>());
		}
		this.depends.add(job);
		if (job.status == 0)
		{
			this.setCanDo(0);
		}
	}
}
