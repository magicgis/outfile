package com.naswork.dao;

import java.util.List;

import com.naswork.model.Client;
import com.naswork.model.ClientContact;
import com.naswork.vo.PageModel;

public interface ClientDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Client record);

    int insertSelective(Client record);

    Client selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Client record);

    int updateByPrimaryKey(Client record);
    
    /**
     * 查询所有客户代码
     */
    public List<Client> findAll(PageModel<Client> page);
    
    /**
     * 查询联系人
     */
    public List<ClientContact> findContacts(PageModel<String> page);
    
    public List<Client> listPage(PageModel<Client> page);
    
    /**
     * 根据代码查询
     */
    public Client findByCode(String code);
    
    public Integer getCurrencyId(Integer id);
    
    /**
     * 获取模板
     */
    public String getTemplet(Integer clientId);
    
    /**
     * 获取主页网址
     */
    public List<String> getUrl();
    
    /**
     * 根据出库指令查询
     */
    public Client getByExportPackageInstructionsNumber(String exportPackageInstructionsNumber);
}