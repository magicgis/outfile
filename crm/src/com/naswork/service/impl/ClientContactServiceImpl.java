package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientContactDao;
import com.naswork.model.ClientContact;
import com.naswork.service.ClientContactService;
import com.naswork.vo.PageModel;

@Service("clientContactService")
public class ClientContactServiceImpl implements ClientContactService {

	@Resource
	private ClientContactDao clientContactDao;
	
	/*
     * 根据客户ID查询
     */
    public List<ClientContact> selectByclientId(PageModel<ClientContact> page){
    	return clientContactDao.listPage(page);
    }
    

    /*
     * 主键查询
     */
    public ClientContact selectByPrimaryKey(Integer id){
    	return clientContactDao.selectByPrimaryKey(id);
    }
    
    /*
     * 客户联系人信息
     */
    public void list(PageModel<ClientContact> page) {
    	page.setEntities(clientContactDao.listPage(page));
	}
    
    public int insertSelective(ClientContact record){
    	record.setId(clientContactDao.maxId()+1);
    	return clientContactDao.insertSelective(record);
    }
    
    public int updateByPrimaryKeySelective(ClientContact record){
    	return clientContactDao.updateByPrimaryKeySelective(record);
    }
}
