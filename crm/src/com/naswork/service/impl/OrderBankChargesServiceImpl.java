package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.OrderBankChargesDao;
import com.naswork.model.OrderBankCharges;
import com.naswork.model.QuoteBankCharges;
import com.naswork.service.OrderBankChargesService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("orderBankChargesService")
public class OrderBankChargesServiceImpl implements OrderBankChargesService {
	@Resource
	private OrderBankChargesDao orderBankChargesDao;
	@Override
	public int insert(OrderBankCharges record) {
		return orderBankChargesDao.insert(record);
	}

	@Override
	public int insertSelective(OrderBankCharges record) {
		return orderBankChargesDao.insertSelective(record);
	}
	
	public void orderBankChargesPage(PageModel<OrderBankCharges> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(orderBankChargesDao.orderBankChargesPage(page));
	}

	@Override
	public void updateByPrimaryKey(OrderBankCharges record) {
		orderBankChargesDao.updateByPrimaryKey(record);		
	}

	@Override
	public List<OrderBankCharges> orderBankChargesByClientId(String clientId) {
		return orderBankChargesDao.orderBankChargesByClientId(clientId);
	}

}
