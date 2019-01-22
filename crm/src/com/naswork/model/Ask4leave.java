package com.naswork.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @since 2016年12月24日 00:41:52
 * @author auto
 * @version v1.0
 */
public class Ask4leave implements Serializable {

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 请假人员姓名
	 */
	private String qjrXm;
	/**
	 * 请假人员ID
	 * 
	 */
	private String qjrId;
	/**
	 * 所属部门
	 */
	private String ssbm;
	/**
	 * 部门职务
	 */
	private String bmzw;
	/**
	 * 请假类别
	 */
	private String qjlbDm;
	/**
	 * 请假类别名称
	 */
	private String qjlbMc;
	/**
	 * 请假理由
	 */
	private String qjly;
	/**
	 * 请假开始时间
	 */
	private Date kssj;
	/**
	 * 请假结束时间
	 */
	private Date jssj;
	/**
	 * 请假人签名
	 */
	private String qjrqm;
	/**
	 * 管理层审批意见
	 */
	private String glcyj;
	/**
	 * 管理层签名
	 */
	private String glcqm;
	/**
	 * 审批人ID
	 */
	private String sprId;
	/**
	 * 假条创建时间
	 */
	private Date cjsj;
	/**
	 * 审批状态
	 */
	private String spzt;
	
	public Integer getId() {
		return id;
	}
	public String getQjrXm() {
		return qjrXm;
	}
	public String getSsbm() {
		return ssbm;
	}
	public String getBmzw() {
		return bmzw;
	}
	public String getQjlbDm() {
		return qjlbDm;
	}
	public String getQjlbMc() {
		return qjlbMc;
	}
	public String getQjly() {
		return qjly;
	}
	public Date getKssj() {
		return kssj;
	}
	public Date getJssj() {
		return jssj;
	}
	public String getQjrqm() {
		return qjrqm;
	}
	public String getGlcyj() {
		return glcyj;
	}
	public String getGlcqm() {
		return glcqm;
	}
	public String getSprId() {
		return sprId;
	}
	public Date getCjsj() {
		return cjsj;
	}
	public String getSpzt() {
		return spzt;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setQjrXm(String qjrXm) {
		this.qjrXm = qjrXm;
	}
	public void setSsbm(String ssbm) {
		this.ssbm = ssbm;
	}
	public void setBmzw(String bmzw) {
		this.bmzw = bmzw;
	}
	public void setQjlbDm(String qjlbDm) {
		this.qjlbDm = qjlbDm;
	}
	public void setQjlbMc(String qjlbMc) {
		this.qjlbMc = qjlbMc;
	}
	public void setQjly(String qjly) {
		this.qjly = qjly;
	}
	public void setKssj(Date kssj) {
		this.kssj = kssj;
	}
	public void setJssj(Date jssj) {
		this.jssj = jssj;
	}
	public void setQjrqm(String qjrqm) {
		this.qjrqm = qjrqm;
	}
	public void setGlcyj(String glcyj) {
		this.glcyj = glcyj;
	}
	public void setGlcqm(String glcqm) {
		this.glcqm = glcqm;
	}
	public void setSprId(String sprId) {
		this.sprId = sprId;
	}
	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}
	public void setSpzt(String spzt) {
		this.spzt = spzt;
	}
	public String getQjrId() {
		return qjrId;
	}
	public void setQjrId(String qjrId) {
		this.qjrId = qjrId;
	}
	
}