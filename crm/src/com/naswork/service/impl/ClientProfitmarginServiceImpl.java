package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientProfitmarginDao;
import com.naswork.model.ClientProfitmargin;
import com.naswork.model.SystemCode;
import com.naswork.service.ClientProfitmarginService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("clientProfitmarginService")
public class ClientProfitmarginServiceImpl implements ClientProfitmarginService {

	@Resource
	private ClientProfitmarginDao  clientProfitmarginDao;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return clientProfitmarginDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ClientProfitmargin record) {
		return clientProfitmarginDao.insert(record);
	}

	@Override
	public int insertSelective(ClientProfitmargin record) {
		return clientProfitmarginDao.insertSelective(record);
	}

	@Override
	public ClientProfitmargin selectByPrimaryKey(Integer id) {
		return clientProfitmarginDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ClientProfitmargin record) {
		return clientProfitmarginDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ClientProfitmargin record) {
		return clientProfitmarginDao.updateByPrimaryKey(record);
	}

	@Override
	public ClientProfitmargin selectByClientId(Integer clientId) {
		return clientProfitmarginDao.selectByClientId(clientId);
	}
	
	public void listPage(PageModel<ClientProfitmargin> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(clientProfitmarginDao.listPage(page));
	}

}
