package com.naswork.model;

import java.util.Date;

public class OnpostEmail {
    private Integer postId;

    private Integer clientinquiryid;

    private Integer emailStatus;

    private Date recordDate;

    private Date sendDate;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getClientinquiryid() {
        return clientinquiryid;
    }

    public void setClientinquiryid(Integer clientinquiryid) {
        this.clientinquiryid = clientinquiryid;
    }

    public Integer getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}