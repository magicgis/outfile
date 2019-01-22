package com.naswork.model.gy;

import java.io.Serializable;
import java.util.Date;

public class GyExcel  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2347375133703418226L;
	
	private String userName;

	private String excelFileId;
	
	private String ywId;
	
	private String ywTableName;
	
	private String ywTablePkName;
	
	private String excelFileName;
	
	private String excelFilePath;
	
	private Integer xh;
	
	private String excelTemplateName;
	
	private Long excelFileLength;
	
	private String userId;

	private Date lrsj;
	
	private String excelType;

	public String getExcelFileId() {
		return excelFileId;
	}

	public void setExcelFileId(String excelFileId) {
		this.excelFileId = excelFileId;
	}

	public String getYwId() {
		return ywId;
	}

	public void setYwId(String ywId) {
		this.ywId = ywId;
	}

	public String getYwTableName() {
		return ywTableName;
	}

	public void setYwTableName(String ywTableName) {
		this.ywTableName = ywTableName;
	}

	public String getYwTablePkName() {
		return ywTablePkName;
	}

	public void setYwTablePkName(String ywTablePkName) {
		this.ywTablePkName = ywTablePkName;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public String getExcelFilePath() {
		return excelFilePath;
	}

	public void setExcelFilePath(String excelFilePath) {
		this.excelFilePath = excelFilePath;
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public String getExcelTemplateName() {
		return excelTemplateName;
	}

	public void setExcelTemplateName(String excelTemplateName) {
		this.excelTemplateName = excelTemplateName;
	}

	public Long getExcelFileLength() {
		return excelFileLength;
	}

	public void setExcelFileLength(Long excelFileLength) {
		this.excelFileLength = excelFileLength;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLrsj() {
		return lrsj;
	}

	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}

	public String getExcelType() {
		return excelType;
	}

	public void setExcelType(String excelType) {
		this.excelType = excelType;
	}

	
}
