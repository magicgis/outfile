package com.naswork.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.naswork.model.Jbpm4HistTask;

/**
 * @since 2016年12月16日 
 * @author Giam
 * @version v1.0
 */
public interface Jbpm4HistTaskDao {

	/**
	 * 获取已办任务
	 * @param Jbpm4HistTask
	 * @since 2016年12月16日 
	 * @author Giam
	 * @version v1.0
	 */
	public List<Jbpm4HistTask> findFinishData(@Param("assignee") String assignee,
			@Param("startBefore") String startBefore,
			@Param("startAfter") String startAfter,
			@Param("deploymentName") String deploymentName);
	
	void updateAssignee(Jbpm4HistTask histTask);
}