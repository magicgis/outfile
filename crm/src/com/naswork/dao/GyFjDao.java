package com.naswork.dao;

import java.util.List;

import com.naswork.model.gy.GyFj;
import com.naswork.vo.PageModel;

/**
 * @since 2016年05月05日 15:52:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface GyFjDao {

	
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
	public int insert(GyFj gyFj);
	
	/**
	 * 更新
	 * @param gyFj
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public int update(GyFj gyFj);
	
	/**
	 * 删除
	 * @param fjUuid
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	int delete(String fjUuid);

	/**
	 * 分页查询
	 * @param page
	 * @return
	 * @since 2016年5月5日 下午4:45:05
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public List<GyFj> findPage(PageModel<GyFj> page);

	/**
	 * 根据多个id查询
	 * @param ids
	 * @return
	 * @since 2016年5月7日 下午4:53:07
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public List<GyFj> findByIds(String ids);
	
	/**
	 * 根据业务ID获取附件
	 * @param ywid
	 * @return
	 * @since 2016年5月23日 下午7:13:53
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<GyFj> findByYwid(String ywid);
	
	/**
	 * 获取件号报价上传附件
	 * @param ywId
	 * @return
	 */
	public List<GyFj> getQuoteAttach(PageModel<String> page);

}