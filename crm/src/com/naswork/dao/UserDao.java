/**
 * 
 */
package com.naswork.dao;

import java.util.List;
import java.util.Map;

import com.naswork.model.Menu;
import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;


/**
 * 用户
 * @since 2016年3月17日 上午9:51:19
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface UserDao {

	/**
	 * 根据税务人员代码查询包括主身份的用户,正常记录只有一条
	 * @param loginName
	 * @return
	 * @since 2016年3月18日 上午11:34:52
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<UserVo> searchUserByLoginName(PageData pd);

	/**
	 * 根据税务人员代码查询用户
	 * @param loginName
	 * @return
	 * @since 2016年3月18日 下午1:57:19
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<UserVo> searchAllSfUserBySwryDm(PageData pd);

	/**
	 * 根据身份代码查询主菜单 
	 * @param swrysfDm
	 * @return
	 * @since 2016年3月18日 下午1:58:12
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<Menu> searchUserRootMenusByRole(PageData pd);

	/**
	 * 查询用户子菜单
	 * @param rootmenus
	 * @param swrysfDm
	 * @return
	 * @since 2016年3月18日 下午1:58:38
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<Menu> searchUserSubmenus(PageData pd);

	/**
	 * 更新密码
	 * @param pd
	 * @since 2016年3月25日 上午10:14:17
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	void updatePasswordByLoginName(PageData pd);

	/**
	 * 查询密码是否存在
	 * @param pd
	 * @return
	 * @since 2016年4月13日 上午10:54:06
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	List<Map<String, Object>> findPassword(PageData pd);

	/**
	 * 新增密码
	 * @param pd
	 * @since 2016年4月13日 上午10:54:49
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	void insertpassword(PageData pd);

	List<UserVo> findPage(PageModel<UserVo> page);
	
	/**
	 * 根据userid查找该用户的所有权限
	 */
	List<RoleVo> searchRoleByUserId(String userId);
	
	/**
	 * 根据用户id查找用户
	 * @param userId
	 * @return
	 */
	UserVo findUserByUserId(String userId);

	public int insert(UserVo userVo);
	
	public int deleteRoleByUserId(String userId);
	
	public int deleteUser(String userId);
	
	public int insertRoleByUserId(PageData pd);
	
	
	/**
	 * 选择性插入
	 * @param user
	 * @return
	 */
	public int insertSelective(UserVo userVo);
	
	/**
	 * 更新
	 * @param 
	 * @since 
	 * @author 
	 * @version v1.0
	 */
	public int update(UserVo userVo);
	
	/**
	 * 获取用户
	 * @param 
	 * @return
	 */
	public List<UserVo> getOwner();
	
	/**
	 * 权限
	 */
	public List<PowerVo> getPowerPage(PageModel<PowerVo> page);
	
	/**
	 * 获取用户
	 */
	public List<UserVo> getUsers();
	
	/**
	 * 获取身份
	 */
	public RoleVo getPower(Integer userId);
	
	List<UserVo> searchEmailByRoleName();
	
	List<UserVo> findEmailByorderNumber(String orderNumber);
	/**
	 * 根据id查询
	 */
	public UserVo findById(Integer userId);
	
	/**
	 * 根据供应商查询邮箱
	 * @param supplierId
	 * @return
	 */
	public List<UserVo> selectBySupplierId(Integer supplierId);
	
	/**
	 * 根据职位获取用户信息
	 * @param roleName
	 * @return
	 */
	public List<UserVo> getByRole(String roleName);
	
	public List<UserVo> getByImportIdAndRoleName(PageModel<UserVo> page);
	
	public List<UserVo> getEmailBySupplierCode(String supplierCode);
	
	public List<UserVo> getUserIdByRoleId(String RoleId);
	
	public List<Integer> getLeadersByRole(Integer roleId);

}
