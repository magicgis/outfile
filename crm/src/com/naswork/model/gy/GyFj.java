package com.naswork.model.gy;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @since 2016年05月05日 15:52:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class GyFj implements Serializable {
	
	private static  long serialVersionUID = -6433973845480062878L;
	private Integer fileType;

	private String fjId;
	
	private String ywId;
	
	private String ywTableName;
	
	private String ywTablePkName;
	
	private String fjName;
	
	private String fjPath;
	
	private String fjType;
	
	private Long fjLength;
	
	private String userId;
	
	private String folderName;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getYwId() {
		return ywId;
	}

	public void setYwId(String ywId) {
		this.ywId = ywId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private Date lrsj;
	
	
	public String getFjId() {
		return fjId;
	}

	public void setFjId(String fjId) {
		this.fjId = fjId;
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
	public String getFjName() {
		return fjName;
	}

	public void setFjName(String fjName) {
		this.fjName = fjName;
	}
	public String getFjPath() {
		return fjPath;
	}

	public void setFjPath(String fjPath) {
		this.fjPath = fjPath;
	}
	public String getFjType() {
		return fjType;
	}

	public void setFjType(String fjType) {
		this.fjType = fjType;
	}
	public Long getFjLength() {
		return fjLength;
	}

	public void setFjLength(Long fjLength) {
		this.fjLength = fjLength;
	}
	public Date getLrsj() {
		return lrsj;
	}

	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
}