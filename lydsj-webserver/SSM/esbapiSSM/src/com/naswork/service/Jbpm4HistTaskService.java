package com.naswork.service;

import java.util.List;
import java.util.Map;

import com.naswork.model.Jbpm4HistTask;

/**
 * @since 2016年12月16日 
 * @author Giam
 * @version v1.0
 */
public interface Jbpm4HistTaskService {
	/**
	 * 获取已办任务
	 * @param Jbpm4HistTask
	 * @since 2016年12月16日 
	 * @author Giam
	 * @version v1.0
	 */
	public List<Jbpm4HistTask> findFinishData(Map<String,String> param);
 

}