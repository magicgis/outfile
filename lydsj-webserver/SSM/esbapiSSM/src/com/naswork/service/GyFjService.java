package com.naswork.service;

import java.util.List;

import com.naswork.model.gy.GyFj;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

/**
 * @since 2016年05月05日 15:52:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface GyFjService {

	
	 /**
	 * 根据主键查询对象
	 * @param fjUuid
	 * @return
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public GyFj findById(String fjUuid);
	
	/**
	 * 新增
	 * @param GyFj
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void add(GyFj GyFj);
	
	/**
	 * 更新
	 * @param GyFj
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void modify(GyFj GyFj);
	
	/**
	 * 删除
	 * @param fjUuid
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void remove(String fjUuid);

	/**
	 * 分页查询
	 * @param page
	 * @param parameter
	 * @param sort
	 * @since 2016年5月5日 下午4:44:02
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void searchPage(PageModel<GyFj> page, String parameter, GridSort sort);

	/**
	 * 根据多个ID查询附件集合
	 * @param idarr
	 * @return
	 * @since 2016年5月7日 下午4:50:07
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public List<GyFj> findByIds(String[] idarr);

	/**
	 * 删除多个附件
	 * @param ids
	 * @since 2016年5月10日 下午7:06:55
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void removeFjByIds(String ids);
	
	/**
	 * 根据业务ID获取附件
	 * @param ywid
	 * @return
	 * @since 2016年5月23日 下午7:13:53
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<GyFj> findByYwid(String ywid);

}