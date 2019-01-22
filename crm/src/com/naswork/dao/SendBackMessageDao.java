package com.naswork.dao;

import java.util.List;

import com.naswork.model.SendBackMessage;
import com.naswork.vo.PageModel;

public interface SendBackMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SendBackMessage record);

    int insertSelective(SendBackMessage record);

    SendBackMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SendBackMessage record);

    int updateByPrimaryKey(SendBackMessage record);
    
    public List<SendBackMessage> listPage(PageModel<SendBackMessage> page);
    
    public List<SendBackMessage> getAddListPage(PageModel<SendBackMessage> page);
}