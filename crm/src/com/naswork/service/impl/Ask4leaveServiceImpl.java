package com.naswork.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;

import com.naswork.dao.Ask4leaveDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Ask4leave;
import com.naswork.model.RRoleUser;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.constants.WorkFlowConstants.ProcessKeys;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.service.Ask4leaveService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

/**
 * @since 2016年12月24日 00:41:52
 * @author auto
 * @version v1.0
 */
@Service("ask4leaveService")
public class Ask4leaveServiceImpl extends FlowServiceImpl implements Ask4leaveService {

	@Resource
	private Ask4leaveDao ask4leaveDao;
	
	 /**
	 * 根据主键查询对象
	 * @param id
	 * @return
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public Ask4leave findById(String id){
		return ask4leaveDao.findById(id);
	}
	
	/**
	 * 新增
	 * @param ask4leave
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public void add(Ask4leave ask4leave){
		ask4leaveDao.insert(ask4leave);
		
	}
	
	/**
	 * 更新
	 * @param ask4leave
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public void modify(Ask4leave ask4leave){
		ask4leaveDao.update(ask4leave);
	}
	
	/**
	 * 删除
	 * @param id
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public void remove(String id){
		ask4leaveDao.delete(id);
	}

	@Override
	public void findNotePage(PageModel<Ask4leave> page, GridSort sort) {
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(ask4leaveDao.findPage(page));
	}

	@Override
	public void submitReview(Ask4leave vo,String ids) {

		UserVo user= ContextHolder.getCurrentUser();
		String processName = "请假流程";
		String buninessKey = "ASK4LEAVE.ID."+String.valueOf(vo.getId());//-- 业务主键,根据表明区分综合审核报告还是结果分析报告
		//List<RRoleUser> userIds = findUseridByRoleid("1");
		try{
				
			Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
			variables.put(WorkFlowConstants.START_USER, ContextHolder.getCurrentUser().getUserId());//-- 发起人
			variables.put(WorkFlowConstants.TASK_INFO, processName);//-- 流程信息
			ProcessInstance processInstance = startProcessInstance(ProcessKeys.Ask4leaveProcess.toValue(), buninessKey, variables);//-- 创建过程实例， 
			Task task = findTaskByProcessInstanceId(processInstance.getId());//-- 获取任务
			
			task.setDescription( WFUtils.getDescriptionStr(processName, WFUtils.DESCRIPTION_START) );
			WFUtils.addTaskParticipatingUser(ids, task.getId(), taskService);
			//WFUtils.addTaskParticipatingUser("1", task.getId(), taskService);
			
			vo.setSpzt("1");
			ask4leaveDao.update(vo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}