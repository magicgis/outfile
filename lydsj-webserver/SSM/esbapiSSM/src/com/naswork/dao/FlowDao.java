/**
 * 
 */
package com.naswork.dao;

import java.util.List;
import java.util.Map;

import com.naswork.vo.PageData;
import com.naswork.vo.UserVo;

/**
 * @since 2016年12月16日 
 * @author Giam
 * @version v1.0
 */
public interface FlowDao {

	/**
	 * 查询
	 * @param businessKey
	 * @return
	 * @since 2016年12月16日 
	 * @author Giam
	 * @version v1.0
	 */
	Map<String, Object> findObjectMapByBusinessKey(PageData pd);

	
	List<Map<String, Object>> findUserByRoleid(PageData pd); 
}
