/**
 * 
 */
package com.naswork.dao;

import java.util.List;
import java.util.Map;

/**
 * @since 2016年3月18日 上午11:48:46
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface DblTableDao {
	/**
	 * 查询所有
	 * @return
	 * @since 2016年3月18日 上午11:49:34
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public List<Map<String, Object>> findAll();
	
}
