/**
 * 
 */
package com.naswork.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.naswork.model.Menu;

/**
 * @since 2015年8月19日 下午4:21:54
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class UserVo implements Serializable {
	private static final long serialVersionUID = -5280286857008596806L;
	private String userId;
	private String userName;
	private String password;
	private String loginName;
	private String information;
    private String email;
    private String emailPassword;
    private String fax;
    private String phone;
	private String roleDisplay;
	private List<String> unRoleIdList;
	private Integer roleId;
	public List<String> getUnRoleIdList() {
		return unRoleIdList;
	}

	public void setUnRoleIdList(List<String> unRoleIdList) {
		this.unRoleIdList = unRoleIdList;
	}

	public String getRoleDisplay() {
		return roleDisplay;
	}

	public void setRoleDisplay(String roleDisplay) {
		this.roleDisplay = roleDisplay;
	}

	//用户根菜单
	private List<Menu> rootmenus;
	//用户子菜单(menuId为key的map,值为菜单树)
	private Map<String, List<LigerTreeVo>> submenus;
	//权限列表
	private List<String> roleIdList;
	
	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Menu> getRootmenus() {
		return rootmenus;
	}

	public void setRootmenus(List<Menu> rootmenus) {
		this.rootmenus = rootmenus;
	}

	public Map<String, List<LigerTreeVo>> getSubmenus() {
		return submenus;
	}

	public void setSubmenus(Map<String, List<LigerTreeVo>> submenus) {
		this.submenus = submenus;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	
}