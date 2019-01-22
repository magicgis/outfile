package com.naswork.dao;

import com.naswork.model.EmailRecord;

public interface EmailRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EmailRecord record);

    int insertSelective(EmailRecord record);

    EmailRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmailRecord record);

    int updateByPrimaryKey(EmailRecord record);
}