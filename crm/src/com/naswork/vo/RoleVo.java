package com.naswork.vo;

import java.io.Serializable;

public class RoleVo implements Serializable {

	private static final long serialVersionUID = -6066637434399245863L;
	private String roleId;
	private String roleName;
	private String roleComment;
	public String getRoleComment() {
		return roleComment;
	}
	public void setRoleComment(String roleComment) {
		this.roleComment = roleComment;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
