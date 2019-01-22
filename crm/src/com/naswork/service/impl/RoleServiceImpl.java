package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.RRoleUserDao;
import com.naswork.dao.RoleDao;
import com.naswork.dao.UserDao;
import com.naswork.model.RRoleUser;
import com.naswork.model.Role;
import com.naswork.model.RoleMenu;
import com.naswork.service.RoleService;
import com.naswork.vo.GridSort;
import com.naswork.vo.LigerTreeVo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Service("roleService")
public class RoleServiceImpl implements RoleService{
	@Resource
	private RoleDao roleDao;
	@Resource
	private RRoleUserDao rRoleUserDao;
	@Resource
	private UserDao userDao;

	@Override
	public void findRolePage(PageModel<RoleVo> page, String searchString, GridSort sort) {
		page.setEntities(roleDao.findRolesPage(page));
		
	}

	@Override
	public List<LigerTreeVo> searchFullTreeByRole(String roleId) {
		List<LigerTreeVo> list = new ArrayList<LigerTreeVo>();
		PageData pd = new PageData();
		pd.put("roleId", roleId);
		List<RoleMenu> menus = roleDao.findFullMenuByRole(pd);
		
		for (RoleMenu menu : menus) {
			LigerTreeVo vo = new LigerTreeVo();
			vo.setId(String.valueOf(menu.getMenuId()));
			if(menu.getChecked().equals("Y")){
				vo.setIschecked(true);
			}else{
				vo.setIschecked(false);
			}
			
			String parentMenuId = menu.getParentMenuId();
			if(parentMenuId==null){
				vo.setPid("0");
			}else{
				vo.setPid(parentMenuId);
			}

			vo.setText(menu.getMenuName());
			list.add(vo);
		}
		return list;
	}

	@Override
	public void updateRoleMenu(String roleId, String[] menuIdList, ResultVo resultVo) {
		roleDao.deleteRoleMenuByRoleId(roleId);
		for(String menuId: menuIdList){
			PageData pd = new PageData();
			pd.put("roleId", roleId);
			pd.put("menuId", menuId);
			roleDao.insertRoleMenu(pd);
		}
		resultVo.setSuccess(true);
		resultVo.setMessage("权限菜单更新成功");
	}

	public void PeopleList(PageModel<UserVo> page){
		page.setEntities(roleDao.getPeoplePage(page));
	}
	
	public void insertPeople(RRoleUser rRoleUser) {
		rRoleUserDao.insertSelective(rRoleUser);
	}
	
	public List<UserVo> getUsers() {
		return userDao.getUsers();
	}
	
	public void deletePeople(RRoleUser rRoleUser){
		rRoleUserDao.deletePeople(rRoleUser);
	}
	
	public RoleVo selectByRoleId(Integer roleId){
		return roleDao.selectByRoleId(roleId);
	}
	
	public List<Integer> getRoleIdByUserId(Integer userId){
		return roleDao.getRoleIdByUserId(userId);
	}

	@Override
	public void insertRole(Role role) {
		roleDao.insertRole(role);
	}

	@Override
	public int selectMaxRoleId() {
		return roleDao.selectMaxRoleId();
	}
}
