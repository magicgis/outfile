/**
 * 
 */
package com.naswork.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 * @since 2015年8月19日 上午11:10:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class Menu implements Serializable {
	private static final long serialVersionUID = -9012085354848777774L;
	private String menuId;
	private String parentMenuId;
	private String menuName;
	private String menuUrl;
	private String isLeaf;
	private String yxbz;
	private String menuOrder;
	private String rootMenuId;
	public String getRootMenuId() {
		return rootMenuId;
	}

	public void setRootMenuId(String rootMenuId) {
		this.rootMenuId = rootMenuId;
	}

	//option 1
	private List<String> roleIdList;
	private List<String> unRoleIdList;

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public List<String> getUnRoleIdList() {
		return unRoleIdList;
	}

	public void setUnRoleIdList(List<String> unRoleIdList) {
		this.unRoleIdList = unRoleIdList;
	}
	
	/*
	//option 2
	private String roleIdList;
	private String unRoleIdList;

	public String getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(String roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getUnRoleIdList() {
		return unRoleIdList;
	}

	public void setUnRoleIdList(String unRoleIdList) {
		this.unRoleIdList = unRoleIdList;
	}
	*/
	public String getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(String menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getYxbz() {
		return yxbz;
	}

	public void setYxbz(String yxbz) {
		this.yxbz = yxbz;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

}
