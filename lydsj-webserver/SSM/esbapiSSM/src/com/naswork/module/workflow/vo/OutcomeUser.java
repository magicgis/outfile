package com.naswork.module.workflow.vo;

import java.util.ArrayList;
import java.util.List;

import com.naswork.vo.RoleVo;

public class OutcomeUser {

	/**
	 * 无需选人,则判断是否赋值用户id和角色id，任务默认发送所有符合条件的人
	 */
	public static String SELECT_TYPE_NO = "0";
	/**
	 * 角色选人，弹窗选人
	 */
	public static String SELECT_TYPE_ROLE = "1";
	/**
	 * 所有人，弹窗列出所有人
	 */
	public static String SELECT_TYPE_ALL = "2";
	
	private String type = "";

	private String roleId = "";

	private List<RoleVo> params = new ArrayList<RoleVo>();

	private String assignee = "";

	public static OutcomeUser createOutComeUser() {
		return new OutcomeUser();
	}

	/**
	 * 获取连线指定提交人
	 * 
	 * @param type
	 *            选人类型（0：无需选人，1  分别角色筛选，  2 所有人）
	 * @param roleId
	 *            选人控件机关根节点
	 * @return
	 */
	public static OutcomeUser createOutComeUser(String type, String roleId) {
		return createOutComeUser().setType(type).setRoleId(roleId);
	}

	/**
	 * 获取连线指定提交人
	 * 
	 * @param type
	 *            选人类型（0：无需选人，1  分别角色筛选，  2 所有人）
	 * @param roleId
	 *            选人控件机关根节点
	 * @param consignors
	 *            直接指定提交人（一般用作回退）
	 * @return
	 */
	public static OutcomeUser createOutComeUser(String type, String roleId,String assignee) {
		return createOutComeUser().setType(type).setRoleId(roleId).setAssignee(assignee);
	}
	

	public String getType() {
		return type;
	}

	public OutcomeUser setType(String type) {
		this.type = type;
		return this;
	}

	public String getRoleId() {
		return roleId;
	}
	public OutcomeUser setRoleId(String roleId) {
		this.roleId = roleId;
		return this;
	}

	public List<RoleVo> getParams() {
		return params;
	}

	public OutcomeUser setParams(List<RoleVo> params) {
		this.params = params;
		return this;
	}

	public String getAssignee() {
		return assignee;
	}

	public OutcomeUser setAssignee(String assignee) {
		this.assignee = assignee;
		return this;
	}

	public OutcomeUser addRole(RoleVo roleVo) {
		if (this.params == null) {
			this.params = new ArrayList<RoleVo>();
		}
		this.params.add(roleVo);
		return this;
	}

	@Override
	public String toString() {
		return "OutcomeUser [type=" + type + ", roleId=" + roleId + ", params=" + params + ", assignee=" + assignee + "]";
	}
}
