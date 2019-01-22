package com.naswork.dao;

import java.util.List;

import com.naswork.model.WarnMessage;

public interface WarnMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WarnMessage record);

    int insertSelective(WarnMessage record);

    WarnMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarnMessage record);

    int updateByPrimaryKey(WarnMessage record);
    
    public List<Integer> selectByUserId(Integer userId);
    
    public List<WarnMessage> getMessage(Integer clientInuqiryId);
    
    public void updateStatus(Integer clientInquiryId);
}