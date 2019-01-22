package com.naswork.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;
import org.springframework.stereotype.Service;

import com.naswork.dao.TestJbpmDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Ask4leave;
import com.naswork.model.TestJbpm;
import com.naswork.module.workflow.constants.WorkFlowConstants;
import com.naswork.module.workflow.constants.WorkFlowConstants.ProcessKeys;
import com.naswork.module.workflow.utils.WFUtils;
import com.naswork.service.TestJbpmService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("testJbpmService")
public class TestJbpmServiceImpl extends FlowServiceImpl implements TestJbpmService {

	@Resource
	private TestJbpmDao testJbpmDao;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return testJbpmDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(TestJbpm record) {
		return testJbpmDao.insert(record);
	}

	@Override
	public int insertSelective(TestJbpm record) {
		return testJbpmDao.insertSelective(record);
	}

	@Override
	public TestJbpm selectByPrimaryKey(Integer id) {
		return testJbpmDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(TestJbpm record) {
		return testJbpmDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TestJbpm record) {
		return testJbpmDao.updateByPrimaryKey(record);
	}
	
	/**
	 * 查询，列表页面显示
	 * **/
	@Override
	public void findPage(PageModel<TestJbpm> page, GridSort sort) {
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(testJbpmDao.findPage(page));
	}

	
	/**
	 * 发起流程任务
	 * **/
	@Override
	public void submitReview(TestJbpm vo,String ids) {

		UserVo user= ContextHolder.getCurrentUser();
		String processName = "测试流程";
		String buninessKey = "TEST_JBPM.ID."+String.valueOf(vo.getId());//-- 业务主键,根据表明区分综合审核报告还是结果分析报告
		//List<RRoleUser> userIds = findUseridByRoleid("1");
		try{
				
			Map<String, Object> variables = new HashMap<String, Object>();//-- 创建流程参数Map
			variables.put(WorkFlowConstants.START_USER, ContextHolder.getCurrentUser().getUserId());//-- 发起人
			variables.put(WorkFlowConstants.TASK_INFO, processName);//-- 流程信息
			ProcessInstance processInstance = startProcessInstance(ProcessKeys.TestProcess.toValue(), buninessKey, variables);//-- 创建过程实例， 
			Task task = findTaskByProcessInstanceId(processInstance.getId());//-- 获取任务
			
			task.setDescription( WFUtils.getDescriptionStr(processName, WFUtils.DESCRIPTION_START) );
			WFUtils.addTaskParticipatingUser(ids, task.getId(), taskService);
			
			vo.setSpzt(WorkFlowConstants.SpztEnum.SHENG_HE_ZHONG.toString());
			testJbpmDao.updateByPrimaryKey(vo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void changeSpzt(String businessKey,
			String taskId, String outcome, String assignee, String comment){
		String id=getIdFromBusinessKey(businessKey);
		TestJbpm vo=new TestJbpm();
		vo.setId(Integer.parseInt(id));
		vo.setSpzt(WorkFlowConstants.SpztEnum.TONG_GUO.toString());
		testJbpmDao.updateByPrimaryKey(vo);
		
	}
	
}
