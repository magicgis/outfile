package com.naswork.dao;

import java.util.List;

import com.naswork.model.SendBackFlowMessage;
import com.naswork.vo.PageModel;

public interface SendBackFlowMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SendBackFlowMessage record);

    int insertSelective(SendBackFlowMessage record);

    SendBackFlowMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SendBackFlowMessage record);

    int updateByPrimaryKey(SendBackFlowMessage record);
    
    public List<SendBackFlowMessage> getSendBackListPage(PageModel<SendBackFlowMessage> page);
}