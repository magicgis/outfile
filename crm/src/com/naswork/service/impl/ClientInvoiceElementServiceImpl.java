package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientInvoiceElementDao;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.module.finance.controller.clientinvoice.ElementDataVo;
import com.naswork.module.finance.controller.clientinvoice.ListDataVo;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("clientInvoiceElementServiceImpl")
public class ClientInvoiceElementServiceImpl implements com.naswork.service.ClientInvoiceElementService {

	@Resource
	private ClientInvoiceElementDao clientInvoiceELementDao;
	@Override
	public void deleteByPrimaryKey(Integer id) {
		clientInvoiceELementDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insert(ClientInvoiceElement record) {
		clientInvoiceELementDao.insert(record);
	}

	@Override
	public void insertSelective(ClientInvoiceElement record) {
		clientInvoiceELementDao.insertSelective(record);
	}

	@Override
	public ClientInvoiceElement selectByPrimaryKey(Integer id) {
		return clientInvoiceELementDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(ClientInvoiceElement record) {
		clientInvoiceELementDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void updateByPrimaryKey(ClientInvoiceElement record) {
		clientInvoiceELementDao.updateByPrimaryKey(record);
	}

	@Override
	public void elementDataPage(PageModel<ElementDataVo> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ElementDataVo> list=clientInvoiceELementDao.elementDataPage(page);
		page.setEntities(list);
	}

	@Override
	public List<ElementDataVo> findByCoidAndCiid(String clientOrderId, String clientInvoiceId) {
		return clientInvoiceELementDao.findByCoidAndCiid(clientOrderId, clientInvoiceId);
	}

	@Override
	public ClientInvoiceElement selectByCoeIdAndCiId(ClientInvoiceElement clientInvoiceElement) {
		return clientInvoiceELementDao.selectByCoeIdAndCiId(clientInvoiceElement);
	}

	@Override
	public List<ElementDataVo> findByCoid(String clientOrderId) {
		return clientInvoiceELementDao.findByCoid(clientOrderId);
	}

	@Override
	public void updateByClientOrderElementId(ClientInvoiceElement record) {
		clientInvoiceELementDao.updateByClientOrderElementId(record);		
	}

}
