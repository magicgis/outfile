package com.naswork.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.Jbpm4HistTaskDao;
import com.naswork.model.Jbpm4HistTask;
import com.naswork.service.Jbpm4HistTaskService;

/**
 * @since 2016年12月16日 
 * @author Giam
 * @version v1.0
 */
@Service("jbpm4HistTaskService")
public class Jbpm4HistTaskServiceImpl implements Jbpm4HistTaskService{

	@Resource
	private Jbpm4HistTaskDao jbpm4HistTaskDao;

	@Override
	public List<Jbpm4HistTask> findFinishData(Map<String, String> param) {
		//查询条件
		String assignee = param.get("assignee");
		String deploymentName = param.get("searchName");
		String startBefore = param.get("startBefore");
		String startAfter = param.get("startAfter");
		
		return jbpm4HistTaskDao.findFinishData(assignee, startBefore, startAfter, deploymentName);
	}
	
	

}