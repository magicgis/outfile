package com.naswork.dao;

import com.naswork.model.ClientInquiryAlterElement;

public interface ClientInquiryAlterElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientInquiryAlterElement record);

    int insertSelective(ClientInquiryAlterElement record);

    ClientInquiryAlterElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientInquiryAlterElement record);

    int updateByPrimaryKey(ClientInquiryAlterElement record);
}