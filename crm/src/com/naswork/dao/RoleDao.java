package com.naswork.dao;

import java.util.List;

import com.naswork.model.Role;
import com.naswork.model.RoleMenu;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

public interface RoleDao {
	
	public List<RoleVo> findAllRoles(PageData pd);

	public List<RoleVo> findRolesPage(PageModel page);

	public List<RoleMenu> findFullMenuByRole(PageData pd);
	
	public int deleteRoleMenuByRoleId(String roleId);
	
	public int insertRoleMenu(PageData pd);
	
	public List<UserVo> getPeoplePage(PageModel<UserVo> page);
	
	public RoleVo selectByRoleId(Integer roleId);
	
	public List<Integer> getRoleIdByUserId(Integer userId);
	
	public void insertRole(Role role);
	
	int  selectMaxRoleId();
}
