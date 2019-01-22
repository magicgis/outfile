package com.naswork.dao;

import com.naswork.model.EmailMessage;

public interface EmailMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EmailMessage record);

    int insertSelective(EmailMessage record);

    EmailMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmailMessage record);

    int updateByPrimaryKey(EmailMessage record);
}