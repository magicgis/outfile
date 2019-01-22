package com.naswork.dao;

import java.util.List;

import com.naswork.model.Jbpm4Jbyj;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;


/**
 * @since 2016年05月18日 18:52:45
 * @author auto
 * @version v1.0
 */
public interface Jbpm4JbyjDao {

	
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
	 * 查询分页
	 * @param jbyjUuid
	 * @return
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public List<Jbpm4Jbyj> findPage(PageModel<Jbpm4Jbyj> page);
	
	/**
	 * 新增
	 * @param Jbpm4Jbyj
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public int insert(Jbpm4Jbyj gyJbyj);
	
	/**
	 * 更新
	 * @param gyJbyj
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	public int update(Jbpm4Jbyj gyJbyj);
	
	int updateBytaskName(Jbpm4Jbyj gyJbyj);
	
	/**
	 * 删除
	 * @param jbyjUuid
	 * @since 2016年05月18日 18:52:45
	 * @author auto
	 * @version v1.0
	 */
	int delete(String jbyjUuid);

	/**
	 * 根据业务关键字查询审批记录
	 * @param businessKey
	 * @return
	 * @since 2016年5月18日 下午7:22:51
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public List<Jbpm4Jbyj> findGyJbyjByBusinessKey(String businessKey);

	/**
	 * 获取第一条经办意见
	 * @param processInstanceId
	 * @return
	 * @since 2016年5月19日 上午11:43:25
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public Jbpm4Jbyj findFirstJbyjByProcessInstanceId(String processInstanceId);

	/**
	 * 获取第一条经办意见
	 * @param businessKey
	 * @return
	 * @since 2016年5月19日 下午8:04:49
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public Jbpm4Jbyj findFirstJbrByBusinessKey(String businessKey);
	
	
	Jbpm4Jbyj findLastJbrByBusinessKey(String businessKey);

	/**
	 * 根据任务ID查询经办记录
	 * @param taskId
	 * @return
	 * @since 2016年5月22日 下午12:53:25
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public Jbpm4Jbyj findGyJbyjByTaskId(String taskId);

	/**
	 * 根据流程实例ID和任务首字母拼音取得经办意见
	 * @param pd
	 * @return
	 * @since 2016年5月23日 上午11:42:00
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public Jbpm4Jbyj findGyJbyjByProcessInstanceIdAndTaskSzmpy(PageData pd);

	/**
	 * 根据任务名称和业务ID获取经办意见
	 * @param pd
	 * @return
	 * @since 2016年5月23日 下午2:23:45
	 * @author yef<yef@everygold.com>
	 * @version v1.0
	 */
	Jbpm4Jbyj findByTask(PageData pd);

	/**
	 * 根据流程实例ID删除经办记录
	 * @param processInstanceId
	 * @since 2016年5月24日 下午3:04:47
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public void deleteJbyjsByProcessInstance(String processInstanceId);
	
	List<Jbpm4Jbyj> selectByYwTableElementId(String clientOrderElementId,String processinstanceId);
	
	Jbpm4Jbyj findGyJbyjByBusinessKeyAndOutcome(String businessKey,String outcome);
}