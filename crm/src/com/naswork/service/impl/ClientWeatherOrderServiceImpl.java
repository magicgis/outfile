package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientWeatherOrderDao;
import com.naswork.dao.ClientWeatherOrderElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.StorageToOrderEmailDao;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.ExchangeRate;
import com.naswork.model.StorageToOrderEmail;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientWeatherOrderService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("clientWeatherOrderService")
public class ClientWeatherOrderServiceImpl implements ClientWeatherOrderService {

	@Resource
	private ClientWeatherOrderDao clientWeatherOrderDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private ClientWeatherOrderElementDao clientWeatherOrderElementDao;
	@Resource
	private StorageToOrderEmailDao storageToOrderEmailDao;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return clientWeatherOrderDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ClientWeatherOrder record) {
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getCurrencyId());
		record.setExchangeRate(exchangeRate.getRate());
		return clientWeatherOrderDao.insert(record);
	}

	@Override
	public int insertSelective(ClientWeatherOrder record) {
		return clientWeatherOrderDao.insertSelective(record);
	}

	@Override
	public ClientWeatherOrder selectByPrimaryKey(Integer id) {
		return clientWeatherOrderDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(ClientWeatherOrder record) {
		if (record.getPrepayRate()!=null &&record.getReceivePayRate()!=null && record.getShipPayRate()!=null) {
			record.setPrepayRate(record.getPrepayRate()/100);
    		record.setReceivePayRate(record.getReceivePayRate()/100);
        	record.setShipPayRate(record.getShipPayRate()/100);
        	ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getCurrencyId());
        	record.setExchangeRate(exchangeRate.getRate());
		}
		clientWeatherOrderDao.updateByPrimaryKeySelective(record);
		//判断是否作废或者取消合同
		if (record.getOrderStatusId().equals(64) || 64 == record.getOrderStatusId() || 
				record.getOrderStatusId().equals(1000093) || 1000093 == record.getOrderStatusId()) {
			List<String> ex = clientWeatherOrderElementDao.getExecutionId(record.getId());
			StringBuffer ids = new StringBuffer();
			for (String string : ex) {
				clientWeatherOrderElementDao.unableTask(string);
				List<Integer> dbids = clientWeatherOrderElementDao.getTaskDbid(string);
				for (Integer integer : dbids) {
					ids.append(integer).append(",");
				}
			}
			clientWeatherOrderElementDao.unableSpzt(record.getId());
			if (ids.length() > 0) {
				ids.deleteCharAt(ids.length()-1);
				clientWeatherOrderElementDao.deleteParticipation(ids.toString());
			}
		}
		
	}
	
	//取消合同明细
	public void cancelElement(String selectIds){
		String[] orderIds = selectIds.split(",");
		for (int i = 0; i < orderIds.length; i++) {
			List<String> ex = clientWeatherOrderElementDao.getExecutionIdByElementId(new Integer(orderIds[i]));
			StringBuffer ids = new StringBuffer();
			for (String string : ex) {
				clientWeatherOrderElementDao.unableTask(string);
				List<Integer> dbids = clientWeatherOrderElementDao.getTaskDbid(string);
				for (Integer integer : dbids) {
					ids.append(integer).append(",");
				}
			}
			if (ids.length() > 0) {
				ids.deleteCharAt(ids.length()-1);
				clientWeatherOrderElementDao.deleteParticipation(ids.toString());
			}
			clientWeatherOrderElementDao.unableSpztByElementId(new Integer(orderIds[i]));
		}
	}

	@Override
	public int updateByPrimaryKey(ClientWeatherOrder record) {
		return clientWeatherOrderDao.updateByPrimaryKey(record);
	}

	/*
     * 列表页面数据
     */
    public void listPage(PageModel<ClientOrderVo> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientWeatherOrderDao.listPage(page));
    }
    

    /*
     * 根据id查询
     */
    public ClientOrderVo findById(Integer id){
    	return clientWeatherOrderDao.findById(id);
    }
    
    public ClientWeatherOrder getClientOrder(Integer clientOrderElementId){
    	return clientWeatherOrderDao.getClientOrder(clientOrderElementId);
    }

	@Override
	public Integer findseq(int id) {
		return clientWeatherOrderDao.findseq(id);
	}
	
	public List<StorageToOrderEmail> getUnfinishListUser(){
		return storageToOrderEmailDao.getUnfinishListUser();
	}
	
	public boolean sendStorageToOrderEmail(List<StorageToOrderEmail> users){
		try {
			for (StorageToOrderEmail storageToOrderEmail : users) {
				List<EmailVo> element = storageToOrderEmailDao.getEmailElements(storageToOrderEmail.getUserId().toString(), storageToOrderEmail.getOrderNumber(),storageToOrderEmail.getNowImportpackNumber());
				clientOrderElementService.sendEmail(element, storageToOrderEmail.getUserId().toString());
				for (int i = 0; i < element.size(); i++) {
					StorageToOrderEmail update = new StorageToOrderEmail();
					update.setId(element.get(i).getId());
					update.setEmailStatus(1);
					storageToOrderEmailDao.updateByPrimaryKeySelective(update);
				}
				
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
    
}
