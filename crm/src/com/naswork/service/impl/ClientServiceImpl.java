package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.List;

import javassist.expr.NewArray;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.AuthorityRelationDao;
import com.naswork.dao.ClientBankMessageDao;
import com.naswork.dao.ClientClassifyDao;
import com.naswork.dao.ClientDao;
import com.naswork.dao.UserDao;
import com.naswork.model.Client;
import com.naswork.model.ClientBankMessage;
import com.naswork.model.ClientClassify;
import com.naswork.model.ClientContact;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.service.ClientService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("clientService")
public class ClientServiceImpl implements ClientService {
	
	@Resource
	private ClientDao clientDao;
	@Resource
	private ClientBankMessageDao clientBankMessageDao;
	@Resource
	private AuthorityRelationDao authorityRelationDao;
	@Resource
	private ClientClassifyDao clientClassifyDao;
	@Resource
	private UserDao userDao;
	/**
     * 查询所有客户代码
     */
    public List<Client> findAll(PageModel<Client> page){
    	return clientDao.findAll(page);
    }
    
    /**
    * 查询联系人
    */
   public List<ClientContact> findContacts(PageModel<String> page){
    	return clientDao.findContacts(page);
    }
   
   /**
    * 客户信息
    */
   public void listPage(PageModel<Client> page, String where,
			GridSort sort){
	   if(where != null && !"".equals(where)){
		   page.put("where", where);
	   }
	   
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientDao.listPage(page));
   }
   
   public void insertSelective(Client record,List<UserVo> userVos){
	   double prepayRate = record.getPrepayRate()/100.0;
	   double shipPayRate = record.getShipPayRate()/100.0;
	   double receivePayRate = record.getReceivePayRate()/100.0;
	   record.setPrepayRate(prepayRate);
	   record.setShipPayRate(shipPayRate);
	   record.setReceivePayRate(receivePayRate);
	   clientDao.insertSelective(record);
	   String[] userIds = record.getUserIds().split(",");
	   List<Integer> ids = new ArrayList<Integer>();
	   ids.add(record.getOwner());
	   if (record.getCode().startsWith("9") || record.getCode().startsWith("8") || record.getCode().startsWith("7")) {
		   if (record.getCode().startsWith("9")) {
			   ids.add(18);
		   }
		   List<UserVo> purchaseIds = userDao.getUserIdByRoleId("7");
		   for (UserVo userVo : purchaseIds) {
			   PowerVo powerVo = new PowerVo();
			   powerVo.setUserId(new Integer(userVo.getUserId()));
			   powerVo.setClientId(record.getId());
			   authorityRelationDao.insertSelective(powerVo);
		   }
	   }
	   if (!"".equals(record.getUserIds())) {
		   for (int i = 0; i < userIds.length; i++) {
			   if (!ids.contains(new Integer(userIds[i]))) {
				   ids.add(new Integer(userIds[i]));
			   }
		   }
	   }
	   for (Integer integer : ids) {
		   PowerVo powerVo = new PowerVo();
		   powerVo.setUserId(integer);
		   powerVo.setClientId(record.getId());
		   authorityRelationDao.insertSelective(powerVo);
	   }
	   /*Integer userId = record.getOwner();
	   PowerVo powerVo = new PowerVo();
	   powerVo.setUserId(userId);
	   powerVo.setClientId(record.getId());
	   authorityRelationDao.insertSelective(powerVo);*/
	   /*for (UserVo userVo : userVos) {
		   if (!userVo.getUserId().equals(userId.toString())) {
			   PowerVo power = new PowerVo();
			   power.setUserId(new Integer(userVo.getUserId()));
			   power.setClientId(record.getId());
			   authorityRelationDao.insertSelective(power);
		   }
	   }*/
	   
	   /*if (record.getCode().startsWith("9")) {
		   int[] users = {10,11,18,22};
		   for (int i = 0; i < users.length; i++) {
			   if (users[i] != record.getOwner()) {
					powerVo.setUserId(users[i]);
					powerVo.setClientId(record.getId());
					authorityRelationDao.insertSelective(powerVo);
			   }				
		   }
		   List<UserVo> userIds = userDao.getUserIdByRoleId("7");
		   for (UserVo userVo : userIds) {
			   powerVo.setUserId(new Integer(userVo.getUserId()));
			   powerVo.setClientId(record.getId());
			   authorityRelationDao.insertSelective(powerVo);
		   }
	   }*/
	   if(null!=record.getClientType()){
		   ClientClassify clientClassify=new ClientClassify();
		   clientClassify.setClientClassifyId(record.getClientType());
		   clientClassify.setClientId(record.getId());
		   clientClassifyDao.insert(clientClassify);
	   }
   }
   
   public Client selectByPrimaryKey(Integer id){
	   return clientDao.selectByPrimaryKey(id);
   }
   
   public int updateByPrimaryKeySelective(Client record){
	   clientClassifyDao.deleteByPrimaryKey(record.getId());
	   if(null!=record.getClientType()){
		   ClientClassify clientClassify=new ClientClassify();
		   clientClassify.setClientClassifyId(record.getClientType());
		   clientClassify.setClientId(record.getId());
		   clientClassifyDao.insert(clientClassify);
	   }
	   double prepayRate = record.getPrepayRate()/100.0;
	   double shipPayRate = record.getShipPayRate()/100.0;
	   double receivePayRate = record.getReceivePayRate()/100.0;
	   record.setPrepayRate(prepayRate);
	   record.setShipPayRate(shipPayRate);
	   record.setReceivePayRate(receivePayRate);
	   return clientDao.updateByPrimaryKeySelective(record);
   }
   
   /**
    * 根据代码查询
    */
   public Client findByCode(String code){
	   return clientDao.findByCode(code);
   }
   
   /**
    * 查看银行
    */
   public void bankList(PageModel<ClientBankMessage> page) {
	   page.setEntities(clientBankMessageDao.listPage(page));
   }
   
   /**
    * 新增银行
    */
   public void insertBank(ClientBankMessage clientBankMessage){
	   clientBankMessageDao.insertSelective(clientBankMessage);
   }
   
   /**
    * 更新银行信息
    */
   public void updateBank(ClientBankMessage clientBankMessage){
	   clientBankMessageDao.updateByPrimaryKeySelective(clientBankMessage);
   }
   
   public Integer getCurrencyId(Integer id){
	   return clientDao.getCurrencyId(id);
   }
   
   public String getTemplet(Integer clientId){
	   return clientDao.getTemplet(clientId);
   }
   
   public List<String> getUrl(){
	   return clientDao.getUrl();
   }
}
