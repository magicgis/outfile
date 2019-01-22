package com.naswork.model;

import java.util.Date;

public class SeqKey extends SeqKeyKey {
    private Integer seq;

    private Date updateTimestamp;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}