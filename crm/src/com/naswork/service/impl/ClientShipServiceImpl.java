package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientShipDao;
import com.naswork.dao.ExportPackageElementDao;
import com.naswork.model.ClientShip;
import com.naswork.model.ExportPackageElement;
import com.naswork.module.storage.controller.clientship.ClientShipVo;
import com.naswork.service.ClientShipService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("clientShipService")
public class ClientShipServiceImpl implements ClientShipService {

	@Resource
	private ClientShipDao clientShipDao;
	@Resource
	private ExportPackageElementDao exportPackageElementDao;
	
	@Override
	public int insertSelective(ClientShip record) {
		List<Integer> elementIds = exportPackageElementDao.findByExportId(record.getExportPackageId());
		for (Integer integer : elementIds) {
			ExportPackageElement exportPackageElement = new ExportPackageElement();
			exportPackageElement.setId(integer);
			exportPackageElement.setStatus(1);
			exportPackageElementDao.updateByPrimaryKeySelective(exportPackageElement);
		}
		return clientShipDao.insertSelective(record);
	}

	@Override
	public ClientShip selectByPrimaryKey(Integer id) {
		return clientShipDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ClientShip record) {
		return clientShipDao.updateByPrimaryKeySelective(record);
	}
	
	public void listPage(PageModel<ClientShipVo> page,String where,GridSort sort){
		page.put("where", where);
		if (sort!=null) {
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else {
			page.put("orderby", null);
		}
		
		page.setEntities(clientShipDao.listPage(page));
	}
	
	public ClientShipVo findById(Integer id){
		return clientShipDao.findById(id);
	}
	
	public String getTemplet(String clientId){
		return clientShipDao.getTemplet(clientId);
	}
}
