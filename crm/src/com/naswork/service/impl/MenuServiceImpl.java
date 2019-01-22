/**
 * 
 */
package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.naswork.dao.MenuDao;
import com.naswork.dao.RoleDao;
import com.naswork.model.Menu;
import com.naswork.model.RoleMenu;
import com.naswork.service.MenuService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.LigerTreeVo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;

/**
 * @since 2016年3月29日 上午9:25:48
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {
	@Resource
	private MenuDao menuDao;

	@Override
	public List<LigerTreeVo> searchFullTree() {
		List<LigerTreeVo> list = new ArrayList<LigerTreeVo>();
		List<Menu> menus = menuDao.findFullMenu();
		
		for (Menu menu : menus) {
			LigerTreeVo vo = new LigerTreeVo();
			vo.setId(String.valueOf(menu.getMenuId()));
			vo.setIschecked(false);
			String parentMenuId = menu.getParentMenuId();
			if(parentMenuId==null){
				vo.setPid("0");
			}else{
				vo.setPid(parentMenuId);
			}
			List<RoleVo> roles = menuDao.searchRoleByMenuId(menu.getMenuId());
			StringBuilder sb = new StringBuilder();
			for(RoleVo role: roles){
				sb.append(role.getRoleName()+";");
			}

			vo.setText(menu.getMenuName()+"("+menu.getMenuOrder()+")"+"("+sb.toString()+")");
			String isLeaf = menu.getIsLeaf();
			if(StringUtils.isNotEmpty(isLeaf) && isLeaf.equals("Y")){
				vo.setUrl(menu.getMenuUrl());
			}
			list.add(vo);
		}
		return list;
	}

	@Override
	public void findPage(PageModel<Menu> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		sort.setName(ConvertUtil.toDBName(sort.getName()));
		page.put("orderby", ConvertUtil.convertSort(sort));
		page.setEntities(menuDao.findPage(page));
	}

	@Override
	public Menu findById(String id, List<RoleVo> allRoles) {
		Menu menu= menuDao.findById(id);
		if(allRoles!=null){
			List<RoleVo> roles = menuDao.searchRoleByMenuId(menu.getMenuId());
			
			List<String> roleIdList = new ArrayList<String>();
			
			//option1
			List<String> unRoleIdList = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			for(RoleVo role: roles){
				sb.append(role.getRoleName()+";");
				roleIdList.add(role.getRoleId());
			}
			menu.setRoleIdList(roleIdList);
			for(RoleVo role:allRoles){
				if(!roleIdList.contains(role.getRoleId())){
					unRoleIdList.add(role.getRoleId());
				}
			}
			menu.setUnRoleIdList(unRoleIdList);
			
			/*
			//option 2
			StringBuilder roleIdListStr = new StringBuilder();
			for(RoleVo role: roles){
				roleIdListStr.append(",").append(role.getRoleId());
				roleIdList.add(role.getRoleId());
			}
			if(roleIdListStr.length()==0){
				menu.setRoleIdList("");
			}
			else{
				menu.setRoleIdList(roleIdListStr.substring(1));
			}
			StringBuilder unRoleIdListStr = new StringBuilder();
			for(RoleVo role:allRoles){
				if(!roleIdList.contains(role.getRoleId())){
					unRoleIdListStr.append(",").append(role.getRoleId());
				}
			}
			if(unRoleIdListStr.length()==0){
				menu.setUnRoleIdList("");
			}else{
				menu.setUnRoleIdList(unRoleIdListStr.substring(1));
			}
			*/
			
		}
		return menu;
	}

	@Override
	public void add(Menu menu) {
		menuDao.insert(menu);
	}

	@Override
	public void modify(Menu menu, String[] roleIdList) {
		menuDao.update(menu);
		menuDao.deleteRoleByMenuId(menu.getMenuId());
		for(String roleId: roleIdList){
			PageData pdTmp = new PageData();
			pdTmp.put("roleId", roleId);
			pdTmp.put("menuId", menu.getMenuId());
			menuDao.insertRoleByMenuId(pdTmp);
		}
	}

	@Override
	public void remove(String id) {
		Menu menu = findById(id,null);
		Assert.notNull(menu);
		PageData pd = new PageData();
		pd.put("menuOrder", menu.getMenuOrder());
		List<Menu> menus = findList(pd);
		for (Menu menu2 : menus) {
			menuDao.delete(menu2.getMenuId());
			//删除菜单相关的权限
			//TODO qxDao.deleteGwQx(menu2.getMenuId());
		}
	}

	/**
	 * 查列表
	 * @param pd
	 * @return
	 * @since 2016年3月31日 上午10:36:12
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	private List<Menu> findList(PageData pd) {
		return menuDao.findList(pd);
	}
	
	@Override
	public List<LigerTreeVo> searchTreeByGwxh(String gwxh) {
		List<LigerTreeVo> list = new ArrayList<LigerTreeVo>();
		List<Menu> menus =  menuDao.findFullMenu();
		List<Map<String, Object>> gwmenus = menuDao.findGwmenus(gwxh);
		Set<String> menuset = new HashSet<String>();
		for (Map<String, Object> gwmenu : gwmenus) {
			menuset.add((String) gwmenu.get("MENU_ID"));
		}
		for (Menu menu : menus) {
			try {
				LigerTreeVo vo = new LigerTreeVo();
				vo.setId(String.valueOf(menu.getMenuId()));
				if(menu.getParentMenuId()==null){
					vo.setPid("0");
				}else{
					vo.setPid(menu.getParentMenuId());
				}
				if(StringUtils.isNotEmpty(menu.getIsLeaf()) && menu.getIsLeaf().equals("Y")){
					vo.setUrl(menu.getMenuUrl());
				}
				vo.setText(menu.getMenuName()+"("+menu.getMenuOrder()+")");
				if(menuset.contains(menu.getMenuId())){
					vo.setIschecked(Boolean.TRUE);
				}
				list.add(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
