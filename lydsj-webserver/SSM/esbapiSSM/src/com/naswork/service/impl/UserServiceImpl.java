package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.naswork.common.constants.Constants;
import com.naswork.dao.UserDao;
import com.naswork.model.Menu;
import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.service.UserService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.MD5;
import com.naswork.vo.GridSort;
import com.naswork.vo.LigerTreeVo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

/**
 * @since 2015-4-24 上午10:09:04
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;

	@Override
	public ResultVo login(String loginName, String password,
			HttpServletRequest request) throws Exception {
		boolean success = false;
		String message = "";
		Assert.hasText(loginName, "用户名不能为空");
		Assert.hasText(password, "密码不能为空");

		PageData pd = new PageData();
		pd.put("loginName", loginName);
		List<UserVo> users = userDao.searchUserByLoginName(pd);

		if (users != null && users.size() > 0) {
			UserVo user = users.get(0);
			if (!password.equals(user.getPassword())) {
				success = false;
				message = "用户密码错误!";
				return new ResultVo(success, message);
			}
			
			// 查询角色
			List<RoleVo> roles = userDao.searchRoleByUserId(user.getUserId());
			List<String> roleIdList = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			for(RoleVo role: roles){
				sb.append(role.getRoleName()+";");
				roleIdList.add(role.getRoleId());
			}
			user.setRoleIdList(roleIdList);

			// 查询权限菜单
			user = queryUserMenu(user);

			// 保存用户对象到session
			HttpSession session = request.getSession();
			session.setAttribute(Constants.SESSION_USER, user);
			success = true;
			message = "登录成功";
		} else {
			success = false;
			message = "用户不存在";
		}

		return new ResultVo(success, message);
	}

	/**
	 * 查询用户菜单
	 * 
	 * @param user
	 * @return
	 * @since 2015年10月8日 上午10:15:29
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public UserVo queryUserMenu(UserVo user) {
		// 查询用户身份对应的菜单根节点
		PageData pd = new PageData();
		List<String> roleIdList= user.getRoleIdList();
		StringBuilder sb = new StringBuilder();
		for(String roleId: roleIdList){
			sb.append(roleId).append(",");
		}
		String roleIdListStr = sb.toString();
		if(roleIdListStr.lastIndexOf(",")==roleIdListStr.length()-1){
			roleIdListStr = roleIdListStr.substring(0, roleIdListStr.length()-1);
		}
		pd.put("roleIdList", roleIdListStr);
		//pd.setAllDblink();
		List<Menu> rootmenus = userDao.searchUserRootMenusByRole(pd);
		user.setRootmenus(rootmenus);

		Map<String, List<LigerTreeVo>> submenus = new HashMap<String, List<LigerTreeVo>>();
		for (Menu menu : rootmenus) {
			pd.clear();
			pd.put("menuId", menu.getMenuId());
			pd.put("roleIdList", roleIdListStr);
			//pd.setAllDblink();
			List<Menu> submenu = userDao.searchUserSubmenus(pd);
			List<LigerTreeVo> list = new ArrayList<LigerTreeVo>();
			for (Menu smenu : submenu) {
				LigerTreeVo vo = new LigerTreeVo();
				vo.setId(String.valueOf(smenu.getMenuId()));
				vo.setPid(smenu.getParentMenuId());
				if (StringUtils.isNotEmpty(smenu.getIsLeaf())
						&& smenu.getIsLeaf().equals("Y")) {
					vo.setUrl(smenu.getMenuUrl());
				}
				vo.setText(smenu.getMenuName());
				list.add(vo);
			}
			submenus.put(String.valueOf(menu.getMenuId()), list);
		}
		user.setSubmenus(submenus);
		return user;
	}

	@Override
	public ResultVo updatePasswByLoginName(String loginName, String newpassword,
			String oldpassword) throws Exception{
		PageData pd = new PageData();
		pd.put("loginName", loginName);
		List<UserVo> users = userDao.searchUserByLoginName(pd);
		
		String message = "";
		Boolean success = false;
		
		if (users != null && users.size() > 0) {
			if (!oldpassword.equals(users.get(0).getPassword())) {	
				success = false;
				message = "旧密码不正确,修改失败";
				return new ResultVo(success, message);
			} else {
					
					pd.clear();
					pd.put("loginName", loginName);
					pd.put("userpassword", newpassword);
					userDao.updatePasswordByLoginName(pd);
					//int a = 1/0;
					success = true;
					message = "修改密码成功";
					return new ResultVo(success, message);
					

			}
		}
		throw new Exception("User not found");

	}

	@Override
	public UserVo setYwValue(UserVo user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findPage(PageModel<UserVo> page, String searchString, GridSort sort, List<RoleVo> allRoles) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<UserVo> userList =userDao.findPage(page);
		for(UserVo user: userList){
			List<RoleVo> roles = userDao.searchRoleByUserId(user.getUserId());
			List<String> roleIdList = new ArrayList<String>();
			List<String> unRoleIdList = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			for(RoleVo role: roles){
				sb.append(role.getRoleName()+";");
				roleIdList.add(role.getRoleId());
			}
			user.setRoleIdList(roleIdList);
			
			for(RoleVo role:allRoles){
				if(!roleIdList.contains(role.getRoleId())){
					unRoleIdList.add(role.getRoleId());
				}
			}
			user.setUnRoleIdList(unRoleIdList);
			user.setRoleDisplay(sb.toString());
		}
		page.setEntities(userList);
		
		
	}

	@Override
	public UserVo findUserByUserId(String userId, List<RoleVo> allRoles) {
		UserVo user = userDao.findUserByUserId(userId);
		List<RoleVo> roles = userDao.searchRoleByUserId(user.getUserId());
		List<String> roleIdList = new ArrayList<String>();
		List<String> unRoleIdList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for(RoleVo role: roles){
			sb.append(role.getRoleName()+";");
			roleIdList.add(role.getRoleId());
		}
		user.setRoleIdList(roleIdList);
		for(RoleVo role:allRoles){
			if(!roleIdList.contains(role.getRoleId())){
				unRoleIdList.add(role.getRoleId());
			}
		}
		user.setUnRoleIdList(unRoleIdList);
		user.setRoleDisplay(sb.toString());
		return user;
	}

	@Override
	public void add(UserVo userVo, String[] roleIdList) {
		userDao.insert(userVo);
		PageData pd = new PageData();
		pd.put("loginName", userVo.getLoginName());
		List<UserVo> users = userDao.searchUserByLoginName(pd);
		String newUserId = users.get(0).getUserId();
		for(String roleId: roleIdList){
			PageData pdTmp = new PageData();
			pdTmp.put("roleId", roleId);
			pdTmp.put("userId", newUserId);
			userDao.insertRoleByUserId(pdTmp);
		}
	}

	@Override
	public void modify(UserVo userVo, String[] roleIdList) {
		userDao.update(userVo);
		userDao.deleteRoleByUserId(userVo.getUserId());
		for(String roleId: roleIdList){
			PageData pdTmp = new PageData();
			pdTmp.put("roleId", roleId);
			pdTmp.put("userId", userVo.getUserId());
			userDao.insertRoleByUserId(pdTmp);
		}
		
	}

	@Override
	public void remove(String userId) {
		userDao.deleteRoleByUserId(userId);
		userDao.deleteUser(userId);
		
	}
	
	public int insertSelective(UserVo userVo){
		MD5 md5 = new MD5();
		userVo.setPassword(md5.md5("123456"));
		return userDao.insertSelective(userVo);
	}

	public List<UserVo> getOwner(){
		return userDao.getOwner();
	}
	
	public void powerList(PageModel<PowerVo> page) {
		page.setEntities(userDao.getPowerPage(page));
	}
	
	
	public void updateById(UserVo userVo) {
		userDao.update(userVo);
	}
	
	/**
	 * 获取身份
	 */
	public RoleVo getPower(Integer userId){
		return userDao.getPower(userId);
	}
	
	public UserVo findById(Integer userId){
		return userDao.findById(userId);
	}
	
	
	public List<RoleVo> searchRoleByUserId(String userId){
		return userDao.searchRoleByUserId(userId);
	}

	@Override
	public UserVo getLoginUser(String loginName, String password) {
		return userDao.getLoginUser(loginName, password);
	}
}
