package com.naswork.service;

import com.naswork.model.gy.GyHelp;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

/**
 * @since 2016年05月05日 15:52:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface GyHelpService {

	
	 /**
	 * 根据主键查询对象
	 * @param helpUuid
	 * @return
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public GyHelp findById(String helpUuid);
	
	/**
	 * 新增
	 * @param GyHelp
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void add(GyHelp GyHelp);
	
	/**
	 * 更新
	 * @param GyHelp
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void modify(GyHelp GyHelp);
	
	/**
	 * 删除
	 * @param helpUuid
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void remove(String helpUuid);

	/**
	 * 分页查询
	 * @param page
	 * @param parameter
	 * @param sort
	 * @since 2016年5月6日 下午12:03:39
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void searchPage(PageModel<GyHelp> page, String parameter,
			GridSort sort);

	/**
	 * 根据关键字查询帮助,如果有重复key,只显示第一条
	 * @param code
	 * @return
	 * @since 2016年5月7日 上午9:27:14
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public GyHelp findByCode(String code);

}