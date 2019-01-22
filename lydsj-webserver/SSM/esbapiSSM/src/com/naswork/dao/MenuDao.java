/**
 * 
 */
package com.naswork.dao;

import java.util.List;
import java.util.Map;

import com.naswork.model.Menu;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;

/**
 * @since 2016年3月29日 上午9:26:16
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface MenuDao {

	/**
	 * 完整菜单
	 * @return
	 * @since 2016年3月29日 下午12:02:57
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<Menu> findFullMenu();

	/**
	 * 分页查询
	 * @param pd
	 * @since 2016年3月30日 下午4:10:53
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 * @param page 
	 */
	List<Menu> findPage(PageModel<Menu> page);

	/**
	 * 根据主键查询对象
	 * @param pd
	 * @return
	 * @since 2016年3月31日 上午9:37:05
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	Menu findById(String id);

	/**
	 * 新增
	 * @param menu
	 * @since 2016年3月31日 上午10:10:59
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	int insert(Menu menu);

	/**
	 * 更新
	 * @param menu
	 * @since 2016年3月31日 上午10:11:11
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	int update(Menu menu);

	/**
	 * 删除
	 * @param pd
	 * @since 2016年3月31日 上午10:11:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	int delete(String string);

	/**
	 * 查列表
	 * @param pd
	 * @return
	 * @since 2016年3月31日 上午10:36:20
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<Menu> findList(PageData pd);

	/**
	 * 查询岗位菜单
	 * @param gwxh
	 * @return
	 * @since 2016年4月12日 上午10:02:11
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<Map<String,Object>> findGwmenus(String gwxh);
	
	List<RoleVo> searchRoleByMenuId(String menuId);
	
	public int deleteRoleByMenuId(String menuId);
	
	public int insertRoleByMenuId(PageData pd);
	
}
