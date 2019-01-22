package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SupplierPnRelationBackUpDao;
import com.naswork.dao.SupplierPnRelationDao;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierPnRelationBackUp;
import com.naswork.model.SupplierPnRelationBackUpKey;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.SupplierPnRelationBackUpService;
import com.naswork.vo.PageModel;

@Service("supplierPnRelationBackUpService")
public class SupplierPnRelationBackUpServiceImpl implements
		SupplierPnRelationBackUpService {
	
	@Resource
	private SupplierPnRelationBackUpDao supplierPnRelationBackUpDao;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SupplierPnRelationDao supplierPnRelationDao;

	@Override
	public int insertSelective(SupplierPnRelationBackUp record) {
		return supplierPnRelationBackUpDao.insertSelective(record);
	}

	@Override
	public SupplierPnRelationBackUp selectByPrimaryKey(
			Integer key) {
		return supplierPnRelationBackUpDao.selectByPrimaryKey(key);
	}

	@Override
	public int updateByPrimaryKeySelective(SupplierPnRelationBackUp record) {
		return supplierPnRelationBackUpDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int getByUserIdPage(PageModel<FactoryVo> page,Integer userId) {
		List<SupplierPnRelationBackUp> eleList = supplierPnRelationBackUpDao.getByUserId(userId);
		//SupplierInquiry supplierInquiry = findClientInquiryElement(clientInquiryId);
		List<FactoryVo> facList = new ArrayList<FactoryVo>();
		List<FactoryVo> List = new ArrayList<FactoryVo>();
		for (SupplierPnRelationBackUp supplierPnRelationBackUp : eleList) {
			/*FactoryVo factoryVo = new FactoryVo();
			factoryVo.setPartNum(clientInquiryElement.getPartNumber());
			factoryVo.setPartName(clientInquiryElement.getDescription());
			List.add(factoryVo);*/
			String partNumberCode = clientInquiryElementService.getCodeFromPartNumber(supplierPnRelationBackUp.getPartNumber());
			facList = supplierPnRelationDao.getTpart(partNumberCode);
			List<FactoryVo> match = new ArrayList<FactoryVo>();
			List<FactoryVo> unMatch = new ArrayList<FactoryVo>();

			for (int j = 0; j < facList.size(); j++) {
				facList.get(j).setId(supplierPnRelationBackUp.getId());
				facList.get(j).setConsultPartName(supplierPnRelationBackUp.getDescription());
				facList.get(j).setConsultPartNum(supplierPnRelationBackUp.getPartNumber());
				String[] name = facList.get(j).getPartName().split(",");
				for (int i = 0; i < name.length; i++) {
					if (supplierPnRelationBackUp.getDescription() != null) {
						if (name[i].toLowerCase().equals(supplierPnRelationBackUp.getDescription().toLowerCase())) {
							facList.get(j).setIfMatch(1);
							match.add(facList.get(j));
							break;
						}
					}else {
						facList.get(j).setIfMatch(0);
						unMatch.add(facList.get(j));
						break;
					}
					
				}
				if (facList.get(j).getIfMatch() == null){
					facList.get(j).setIfMatch(0);
					unMatch.add(facList.get(j));
				}
				//List.add(factoryVo2);
			}
			if (facList.size()==0) {
				FactoryVo faVo = new FactoryVo();
				faVo.setId(supplierPnRelationBackUp.getId());
				faVo.setConsultPartName(supplierPnRelationBackUp.getDescription());
				faVo.setConsultPartNum(supplierPnRelationBackUp.getPartNumber());
				faVo.setPartNum("没找到匹配的件号！");
				List.add(faVo);
			}
			for (FactoryVo factoryVo2 : match) {
				List.add(factoryVo2);
			}
			for (FactoryVo factoryVo2 : unMatch) {
				List.add(factoryVo2);
			}
			//List.add(new FactoryVo());
		}
		page.setEntities(List);
		return List.size();
	}
	
	public void deleteMessage(Integer userId){
		supplierPnRelationBackUpDao.deleteMessage(userId);
	}

}
