package com.naswork.service;

import java.util.List;

import com.naswork.model.Client;
import com.naswork.model.ClientBankMessage;
import com.naswork.model.ClientContact;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

public interface ClientService {
	
	/**
     * 查询所有客户代码
     */
    public List<Client> findAll(PageModel<Client> page);
    
   /**
    * 查询联系人
    */
   public List<ClientContact> findContacts(PageModel<String> page);
   
   /**
    * 客户信息
    */
   public void listPage(PageModel<Client> page, String where,
			GridSort sort);
   
   public void insertSelective(Client record,List<UserVo> userVos);
   
   public Client selectByPrimaryKey(Integer id);
   
   public int updateByPrimaryKeySelective(Client record);
   
   /**
    * 根据代码查询
    */
   public Client findByCode(String code);
   
   /**
    * 查看银行
    */
   public void bankList(PageModel<ClientBankMessage> page);
   
   /**
    * 新增银行
    */
   public void insertBank(ClientBankMessage clientBankMessage);
   
   /**
    * 更新银行信息
    */
   public void updateBank(ClientBankMessage clientBankMessage);
   
   public Integer getCurrencyId(Integer id);
   
   /**
    * 获取模板
    */
   public String getTemplet(Integer clientId);
   
   /**
    * 获取主页网址
    */
   public List<String> getUrl();
}
