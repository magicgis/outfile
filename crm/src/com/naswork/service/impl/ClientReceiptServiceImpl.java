package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientReceiptDao;
import com.naswork.model.ClientReceipt;
import com.naswork.module.finance.controller.clientreceipt.ClientReceiptVo;
import com.naswork.service.ClientReceiptService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("clientReceiptService")
public class ClientReceiptServiceImpl implements ClientReceiptService {

	@Resource
	private ClientReceiptDao clientReceiptDao;
	
	@Override
	public int insertSelective(ClientReceipt record) {
		return clientReceiptDao.insertSelective(record);
	}

	@Override
	public ClientReceipt selectByPrimaryKey(Integer id) {
		return clientReceiptDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ClientReceipt record) {
		return clientReceiptDao.updateByPrimaryKeySelective(record);
	}

	public void listPage(PageModel<ClientReceiptVo> page,String where,GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(clientReceiptDao.listPage(page));
	}
	
	public ClientReceiptVo findById(Integer id){
		return clientReceiptDao.findById(id);
	}
}
