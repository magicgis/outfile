package com.naswork.service;

import java.util.List;

import com.naswork.model.WarnMessage;

public interface WarnMessageService {

    int insertSelective(WarnMessage record);

    WarnMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WarnMessage record);
    
    public List<Integer> selectByUserId(Integer userId);
    
    public List<WarnMessage> getMessage(Integer clientInuqiryId);
    
    public void updateStatus(Integer clientInquiryId);
	
}
