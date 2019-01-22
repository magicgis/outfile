package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientBankMessage;
import com.naswork.vo.PageModel;

public interface ClientBankMessageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientBankMessage record);

    int insertSelective(ClientBankMessage record);

    ClientBankMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientBankMessage record);

    int updateByPrimaryKey(ClientBankMessage record);
    
    public List<ClientBankMessage> listPage(PageModel<ClientBankMessage> page);
}