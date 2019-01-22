package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientContact;
import com.naswork.vo.PageModel;

public interface ClientContactDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientContact record);

    int insertSelective(ClientContact record);

    ClientContact selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientContact record);

    int updateByPrimaryKey(ClientContact record);
    
    /*
     * 根据客户ID查询
     */
    public List<ClientContact> listPage(PageModel<ClientContact> page);
    
    public Integer maxId();
    
    public List<ClientContact> selectByClientId(Integer clientId);
}