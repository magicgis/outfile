/**
 * 
 */
package com.naswork.service;

import java.util.List;

import com.naswork.model.Menu;
import com.naswork.vo.GridSort;
import com.naswork.vo.LigerTreeVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;

/**
 * @since 2016年3月29日 上午9:25:07
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface MenuService {

	/**
	 * 菜单树
	 * @return
	 * @since 2016年3月29日 下午12:00:10
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<LigerTreeVo> searchFullTree();

	/**
	 * 分页查询
	 * @param page
	 * @param searchString
	 * @param sort
	 * @since 2016年3月30日 下午4:02:44
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	void findPage(PageModel<Menu> page, String searchString, GridSort sort);

	/**
	 * 根据主键查询对象
	 * @param id
	 * @return
	 * @since 2016年3月31日 上午9:36:04
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	Menu findById(String id,List<RoleVo> allRoles);

	/**
	 * 新增
	 * @param menu
	 * @since 2016年3月31日 上午10:07:12
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	void add(Menu menu);

	/**
	 * 修改
	 * @param menuEntity
	 * @since 2016年3月31日 上午10:07:36
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	void modify(Menu menu, String[] roleIdList);

	/**
	 * 根据ID删除
	 * @param id
	 * @since 2016年3月31日 上午10:08:08
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	void remove(String id);

	/**
	 * 根据岗位序号查询权限
	 * @param gwxh
	 * @return
	 * @since 2016年4月12日 上午9:55:29
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<LigerTreeVo> searchTreeByGwxh(String gwxh);
	

}
