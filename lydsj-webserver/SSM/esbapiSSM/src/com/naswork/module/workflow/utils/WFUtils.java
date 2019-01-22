package com.naswork.module.workflow.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.TaskService;
import org.jbpm.api.task.Participation;

import com.naswork.filter.ContextHolder;
import com.naswork.model.RRoleUser;

public class WFUtils {

	/**
	 * 发起流程
	 */
	public static final int DESCRIPTION_START   = 0;
	/**
	 * 审批通过
	 */
	public static final int DESCRIPTION_PASS    = 1;
	/**
	 * 审批不通过
	 */
	public static final int DESCRIPTION_NOTPASS = 2;
	/**
	 * 退回
	 */
	public static final int DESCRIPTION_RETURN  = 3;
	
	/**
	 * 设置流程描述
	 * @param taskName 任务名称
	 */
	public static String getDescriptionStr(String taskName){
		return getDescriptionStr(taskName,DESCRIPTION_PASS);
	}
	
	public static String getDescriptionStr(String taskName,int type){
		String tplStart   = "%s发起【%s】等待您的处理!";
		String tplPass    = "%s审核【%s】通过，等待您的处理!";
		String tplNotPass = "%s审核【%s】不通过，等待您的处理!";
		String tplReturn  = "%s审核【%s】退回，等待您的处理!";
		
		String tpl = "";
		switch(type){
			case DESCRIPTION_START:
				tpl = tplStart;
				break;
			case DESCRIPTION_PASS:
				tpl = tplPass;
				break;
			case DESCRIPTION_NOTPASS:
				tpl = tplNotPass;
				break;
			case DESCRIPTION_RETURN:
				tpl = tplReturn;
				break;
		}
		
		return String.format(tpl, ContextHolder.getCurrentUser().getUserName(),taskName);
	}
	
	/**
	 * 添加接受任务人员
	 * @param userId 接受任人员
	 * @param taskId
	 * @param taskService
	 * @return
	 */
	public static boolean addTaskParticipatingUser(String userIds,String taskId,TaskService taskService){
		
		if( StringUtils.isEmpty(userIds) || StringUtils.isEmpty(taskId) ) return false;

		String[] assigneeArr = userIds.split(";");
		
		if( assigneeArr.length > 1 ) {
			for (String sfId : assigneeArr) {
				taskService.addTaskParticipatingUser(taskId, sfId, Participation.CANDIDATE);
			}
		}else{
			taskService.assignTask(taskId, assigneeArr[0]);
		}
		
		return true;
	}

	public static boolean addTaskParticipatingUser(List<RRoleUser> userIds, String taskId, TaskService taskService) {
		if( userIds == null || userIds.size() <= 0 ) return false;
		
		if( userIds.size() > 1 ) {
			for (RRoleUser info : userIds) {
				taskService.addTaskParticipatingUser(taskId, String.valueOf(info.getUserId()), Participation.CANDIDATE);
			}
		}else{
			taskService.assignTask(taskId, String.valueOf(userIds.get(0).getUserId()));
		}
		
		return true;
	}
	
}
