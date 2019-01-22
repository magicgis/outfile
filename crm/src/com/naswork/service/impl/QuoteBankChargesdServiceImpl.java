package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.QuoteBankChargesdDao;
import com.naswork.model.QuoteBankCharges;
import com.naswork.model.SystemCode;
import com.naswork.service.QuoteBankChargesService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("quoteBankChargesdService")
public class QuoteBankChargesdServiceImpl implements QuoteBankChargesService {
	@Resource
	private QuoteBankChargesdDao quoteBankChargesdDao;

	@Override
	public int insert(QuoteBankCharges record) {
		return quoteBankChargesdDao.insert(record);
	}

	@Override
	public int insertSelective(QuoteBankCharges record) {
		return quoteBankChargesdDao.insertSelective(record);
	}
	
	public void quoteBankChargesPage(PageModel<QuoteBankCharges> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(quoteBankChargesdDao.quoteBankChargesPage(page));
	}

	@Override
	public QuoteBankCharges findByClientId(Integer clientId) {
		return quoteBankChargesdDao.findByClientId(clientId);
	}

	@Override
	public void updateByPrimaryKey(QuoteBankCharges record) {
		quoteBankChargesdDao.updateByPrimaryKey(record);
	}

}
