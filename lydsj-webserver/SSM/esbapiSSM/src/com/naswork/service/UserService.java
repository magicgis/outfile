/**
 * 
 */
package com.naswork.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

/**
 * @since 2015-4-24 上午10:08:33
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface UserService {

	/**
	 * 根据用户名密码登录
	 * @param loginName
	 * @param password
	 * @param request
	 * @return
	 * @since 2015年8月20日 上午9:47:14
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 * @throws Exception 
	 */
	ResultVo login(String loginName, String password, HttpServletRequest request) throws Exception;

	/**
	 * 查询用户菜单
	 * @param user
	 * @return
	 * @since 2015年10月8日 上午10:18:28
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public UserVo queryUserMenu(UserVo user);

	/**
	 * 修改密码
	 * @param swryDm
	 * @param newpassword
	 * @param oldpassword
	 * @return
	 * @since 2016年3月25日 上午10:07:07
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	ResultVo updatePasswByLoginName(String loginName, String newpassword,
			String oldpassword) throws Exception;
	
	/**
	 * 设置业务数据
	 * @param user
	 * @return
	 * @since 2016年5月17日 下午3:50:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public UserVo setYwValue(UserVo user);
	
	/**
	 * 
	 * @param page
	 * @param searchString
	 * @param sort
	 */
	public void findPage(PageModel<UserVo> page, String searchString, GridSort sort, List<RoleVo> allRoles);
	/**
	 * 根据用户id查找用户
	 * @param userId
	 * @return
	 */
	public UserVo findUserByUserId(String userId, List<RoleVo> allRoles);
	
	public void add(UserVo userVo, String[] roleIdList);
	
	/**
	 * 更新
	 * @param 
	 * @since 2016年05月04日 14:38:41
	 * @author xiaohu<xiongxh@everygold.com>
	 * @version v1.0
	 */
	public void modify(UserVo userVo, String[] roleIdList);
	
	public void remove(String userId);
	
	/**
	 * 选择性插入
	 * @param user
	 * @return
	 */
	public int insertSelective(UserVo userVo);
	
	/**
	 * 获取用户
	 * @param 
	 * @return
	 */
	public List<UserVo> getOwner();
	
	/**
	 * 获取权限列表
	 */
	public void powerList(PageModel<PowerVo> page);
	
	/**
	 * 修改用户
	 */
	public void updateById(UserVo userVo);
	
	/**
	 * 获取身份
	 */
	public RoleVo getPower(Integer userId);
	
	/**
	 * 根据id查询
	 */
	public UserVo findById(Integer userId);
	
	public List<RoleVo> searchRoleByUserId(String userId);
	
	/**
	 * @author zhangwenwen
	 * 根据用户名、密码参数查询登陆用户
	 * @param userVo
	 * @return
	 */
	public UserVo getLoginUser(String loginName,String password);
	
}
