package com.naswork.model;

import java.util.Date;

public class Element {
    private Integer id;

    private Date updateTimestamp;

    private Byte[] partNumberCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Byte[] getPartNumberCode() {
        return partNumberCode;
    }

    public void setPartNumberCode(Byte[] partNumberCode) {
        this.partNumberCode = partNumberCode;
    }
}