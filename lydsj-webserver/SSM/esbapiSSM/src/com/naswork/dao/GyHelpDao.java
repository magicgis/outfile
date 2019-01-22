package com.naswork.dao;

import java.util.List;

import com.naswork.model.gy.GyHelp;
import com.naswork.vo.PageModel;

/**
 * @since 2016年05月05日 15:52:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface GyHelpDao {

	
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
	public int insert(GyHelp gyHelp);
	
	/**
	 * 更新
	 * @param gyHelp
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public int update(GyHelp gyHelp);
	
	/**
	 * 删除
	 * @param helpUuid
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	int delete(String helpUuid);

	/**
	 * 分页查询
	 * @param page
	 * @return
	 * @since 2016年5月6日 下午12:04:31
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public List<GyHelp> findPage(PageModel<GyHelp> page);

	/**
	 * 根据code查询帮助列表
	 * @param key
	 * @return
	 * @since 2016年5月7日 上午9:29:50
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public List<GyHelp> findByCode(String code);

}