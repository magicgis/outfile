package com.naswork.dao;

import java.util.List;

import com.naswork.model.CrmStock;
import com.naswork.model.TPart;
import com.naswork.module.crmstock.controller.LiluVo;
import com.naswork.vo.PageModel;

public interface CrmStockDao {

	List<CrmStock> findStockPage(PageModel<CrmStock> page);
	
	List<CrmStock>  findCagePage(PageModel<CrmStock> page);
	
	List<CrmStock> getDataOnce(PageModel<CrmStock> page);
	
	public List<LiluVo> getLiluList();
	
	CrmStock findByBsn(String bsn);
	
	void updateByBsn(CrmStock crmStock);
	
	void update(TPart crmStock);
	
	void updateBycageCode(TPart crmStock);
	
	List<TPart> finddata(String cageCode);
}
