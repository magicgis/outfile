package com.naswork.service.rfqstock;

import com.naswork.model.rfqstock.nsncenter.CageInfo;
import com.naswork.model.rfqstock.nsncenter.StockInfo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface NsnCenterService {

	public void searchPageByCage(PageModel<CageInfo> page, String parameter, GridSort sort);

	public void searchPageByNsnPart(PageModel<StockInfo> page, String parameter, GridSort sort);

}
