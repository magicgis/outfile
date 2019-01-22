package com.naswork.dao;

import java.util.List;

import com.naswork.model.Ask4leave;
import com.naswork.vo.PageModel;


/**
 * @since 2016年12月24日 00:41:52
 * @author auto
 * @version v1.0
 */
public interface Ask4leaveDao {

	
	 /**
	 * 根据主键查询对象
	 * @param id
	 * @return
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public Ask4leave findById(String id);
	
	 /**
	 * 查询分页
	 * @param id
	 * @return
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public List<Ask4leave> findPage(PageModel<Ask4leave> page);
	
	/**
	 * 新增
	 * @param Ask4leave
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public int insert(Ask4leave ask4leave);
	
	/**
	 * 更新
	 * @param ask4leave
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public int update(Ask4leave ask4leave);
	
	/**
	 * 删除
	 * @param id
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	int delete(String id);

}