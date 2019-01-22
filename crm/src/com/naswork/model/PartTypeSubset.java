package com.naswork.model;

import java.util.Date;
import java.util.List;

public class PartTypeSubset {
    private Integer id;

    private Integer partTypeParentId;

    private String code;

    private String value;

    private String remark;

    private Date updateTimestamp;
    
    private List<PartTypeSubset> list;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartTypeParentId() {
        return partTypeParentId;
    }

    public void setPartTypeParentId(Integer partTypeParentId) {
        this.partTypeParentId = partTypeParentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public List<PartTypeSubset> getList() {
		return list;
	}

	public void setList(List<PartTypeSubset> list) {
		this.list = list;
	}
}