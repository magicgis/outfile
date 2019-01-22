package com.naswork.service;

import com.naswork.model.CrmStock;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface CrmStockService {
	void findStockPage(PageModel<CrmStock> page, String searchString,GridSort sort);
	
	public void getDataOnce(PageModel<CrmStock> page, String searchString, GridSort sort);
	
	void findCagePage(PageModel<CrmStock> page, String searchString,GridSort sort);
	
}
