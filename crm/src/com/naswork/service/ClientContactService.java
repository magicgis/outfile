package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientContact;
import com.naswork.vo.PageModel;

public interface ClientContactService {
	
	/*
     * 根据客户ID查询
     */
    public List<ClientContact> selectByclientId(PageModel<ClientContact> page);
    
    /*
     * 主键查询
     */
    public ClientContact selectByPrimaryKey(Integer id);
    
    /*
     * 客户联系人信息
     */
    public void list(PageModel<ClientContact> page);
    
    public int insertSelective(ClientContact record);
    
    public int updateByPrimaryKeySelective(ClientContact record);
}
