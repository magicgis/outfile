package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.utils.GetPinyin;
import com.naswork.vo.PageData;

/**
 * @since 2016年05月18日 18:52:45
 * @author auto
 * @version v1.0
 */
@Service("gyJbyjService")
public class Jbpm4JbyjServiceImpl implements Jbpm4JbyjService{

	@Resource
	private Jbpm4JbyjDao jbyjDao;
	
	 /**
	 * 根据主键查询对象
	 * @param jbyjUuid
	 * @return
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public Jbpm4Jbyj findById(String jbyjUuid){
		return jbyjDao.findById(jbyjUuid);
	}
	
	/**
	 * 新增
	 * @param gyJbyj
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public void add(Jbpm4Jbyj gyJbyj){
		jbyjDao.insert(gyJbyj);
		
	}
	
	/**
	 * 更新
	 * @param gyJbyj
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public void modify(Jbpm4Jbyj gyJbyj){
		jbyjDao.update(gyJbyj);
	}
	
	/**
	 * 删除
	 * @param jbyjUuid
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public void remove(String jbyjUuid){
		jbyjDao.delete(jbyjUuid);
	}

	@Override
	public List<Jbpm4Jbyj> findGyJbyjByBusinessKey(String businessKey) {
		return jbyjDao.findGyJbyjByBusinessKey(businessKey);
	}

	@Override
	public Jbpm4Jbyj findFirstJbyjByProcessInstanceId(String processInstanceId) {
		return jbyjDao.findFirstJbyjByProcessInstanceId(processInstanceId);
	}

	@Override
	public Jbpm4Jbyj findFirstJbrByBusinessKey(String businessKey) {
		return jbyjDao.findFirstJbrByBusinessKey(businessKey);
	}

	@Override
	public Jbpm4Jbyj findGyJbyjByTaskId(String taskId) {
		return jbyjDao.findGyJbyjByTaskId(taskId);
	}

	/**
	 * 根据任务名称和业务ID获取经办意见
	 * @param taskName
	 * @param businessKey
	 * @return
	 * @since 2016年5月23日 下午2:23:45
	 * @author yef<yef@everygold.com>
	 * @version v1.0
	 */
	public Jbpm4Jbyj findByTask(String taskName, String businessKey) {
		PageData pd = new PageData();
		pd.put("businessKey", businessKey);
		pd.put("taskSzmpy", GetPinyin.getPinYinHeadChar(taskName));
		return jbyjDao.findByTask(pd);
	}

	@Override
	public void deleteJbyjsByProcessInstance(String processInstanceId) {
		jbyjDao.deleteJbyjsByProcessInstance(processInstanceId);
	}

	@Override
	public Jbpm4Jbyj findGyJbyjByBusinessKeyAndOutcome(String businessKey, String outcome) {
		return jbyjDao.findGyJbyjByBusinessKeyAndOutcome(businessKey, outcome);
	}
}