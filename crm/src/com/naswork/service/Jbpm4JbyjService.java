package com.naswork.service;

import java.util.List;

import com.naswork.model.Jbpm4Jbyj;

/**
 * @since 2016年05月18日 18:52:45
 * @author auto
 * @version v1.0
 */
public interface Jbpm4JbyjService {

	
	 /**
	 * 根据主键查询对象
	 * @param jbyjUuid
	 * @return
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public Jbpm4Jbyj findById(String jbyjUuid);
	
	/**
	 * 新增
	 * @param Jbpm4Jbyj
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public void add(Jbpm4Jbyj Jbpm4Jbyj);
	
	/**
	 * 更新
	 * @param Jbpm4Jbyj
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public void modify(Jbpm4Jbyj Jbpm4Jbyj);
	
	/**
	 * 删除
	 * @param jbyjUuid
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public void remove(String jbyjUuid);

	/**
	 * 根据业务主键查询工作项内容
	 * @param businessKey
	 * @return
	 * @since 2016年5月18日 下午7:14:33
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public List<Jbpm4Jbyj> findGyJbyjByBusinessKey(String businessKey);

	/**
	 * 取得第一条经办记录
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月19日 上午11:37:19
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public Jbpm4Jbyj findFirstJbyjByProcessInstanceId(String processInstanceId);

	/**
	 * 根据业务关键字取第一条经办记录
	 * @param businessKey
	 * @return
	 * @since 2016年5月19日 下午8:02:57
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public Jbpm4Jbyj findFirstJbrByBusinessKey(String businessKey);

	/**
	 * 根据任务ID查询经办记录
	 * @param taskId
	 * @return
	 * @since 2016年5月22日 下午12:52:34
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public Jbpm4Jbyj findGyJbyjByTaskId(String taskId);
	
	/**
	 * 根据任务名称和业务ID获取经办意见
	 * @param taskName
	 * @param businessKey
	 * @return
	 * @since 2016年5月23日 下午2:23:45
	 * @author yef<yef@everygold.com>
	 * @version v1.0
	 */
	Jbpm4Jbyj findByTask(String taskName, String businessKey);

	/**
	 * 根据流程实例ID删除经办意见记录
	 * @param processInstanceId
	 * @since 2016年5月24日 下午3:04:03
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public void deleteJbyjsByProcessInstance(String processInstanceId);
	
	Jbpm4Jbyj findGyJbyjByBusinessKeyAndOutcome(String businessKey,String outcome);

}