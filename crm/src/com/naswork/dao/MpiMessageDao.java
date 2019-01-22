package com.naswork.dao;

import java.util.List;

import com.naswork.model.MpiMessage;
import com.naswork.model.SystemCode;
import com.naswork.vo.PageModel;

public interface MpiMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MpiMessage record);

    int insertSelective(MpiMessage record);

    MpiMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MpiMessage record);

    int updateByPrimaryKey(MpiMessage record);
    
    public List<MpiMessage> listPage(PageModel<MpiMessage> page);
    
    public List<SystemCode> getSystemList();
}