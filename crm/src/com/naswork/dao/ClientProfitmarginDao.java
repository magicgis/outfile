package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientProfitmargin;
import com.naswork.model.SystemCode;
import com.naswork.vo.PageModel;

public interface ClientProfitmarginDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientProfitmargin record);

    int insertSelective(ClientProfitmargin record);

    ClientProfitmargin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientProfitmargin record);

    int updateByPrimaryKey(ClientProfitmargin record);
    
    ClientProfitmargin selectByClientId(Integer clientId);
    
    public List<ClientProfitmargin> listPage(PageModel<ClientProfitmargin> page);
}