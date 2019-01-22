package com.naswork.service;

import java.util.List;

import com.naswork.model.RRoleUser;
import com.naswork.vo.GridSort;
import com.naswork.vo.LigerTreeVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

public interface RoleService {

	/**
	 * 获取所有role
	 * @param page
	 * @param searchString
	 * @param sort
	 */
	public void findRolePage(PageModel<RoleVo> page, String searchString, GridSort sort);

	/**
	 * 根据权限id来获取树
	 * @return
	 */
	List<LigerTreeVo> searchFullTreeByRole(String roleId);
	
	public void updateRoleMenu(String roleId, String[] menuIdList, ResultVo resultVo);
	
	/**
	 * 人员分配
	 */
	public void PeopleList(PageModel<UserVo> page);
	
	/**
	 * 增加人员
	 */
	public void insertPeople(RRoleUser rRoleUser);
	
	/**
	 * 获取用户
	 */
	public List<UserVo> getUsers();
	
	/**
	 * 删除用户
	 */
	public void deletePeople(RRoleUser rRoleUser);
	
	/**
	 * 根据ID查询
	 * @param roleId
	 * @return
	 */
	public RoleVo selectByRoleId(Integer roleId);
	
	public List<Integer> getRoleIdByUserId(Integer userId);
}
