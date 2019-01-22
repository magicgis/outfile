package com.naswork.service;

import com.naswork.model.Ask4leave;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

/**
 * @since 2016年12月24日 00:41:52
 * @author auto
 * @version v1.0
 */
public interface Ask4leaveService {

	
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
	 * 新增
	 * @param Ask4leave
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public void add(Ask4leave Ask4leave);
	
	/**
	 * 更新
	 * @param Ask4leave
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public void modify(Ask4leave Ask4leave);
	
	/**
	 * 删除
	 * @param id
	 * @since 2016年12月24日 00:41:52
	 * @author auto
	 * @version v1.0
	 */
	public void remove(String id);

	/**
	 * 找表格数据
	 * @param page
	 * @param sort
	 */
	public void findNotePage(PageModel<Ask4leave> page, GridSort sort);

	public void submitReview(Ask4leave vo,String ids);

}