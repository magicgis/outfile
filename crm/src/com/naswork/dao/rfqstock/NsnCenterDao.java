package com.naswork.dao.rfqstock;

import java.util.List;
import java.util.Map;

import com.naswork.model.rfqstock.nsncenter.CageInfo;
import com.naswork.model.rfqstock.nsncenter.StockInfo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;

public interface NsnCenterDao {

	public List<CageInfo> searchPageByCage(PageModel<CageInfo> page);

	public List<StockInfo> searchPageByNsnPart(PageModel<StockInfo> page);

	public List<Map<String, Object>> findReplaceNsn(PageData pd);
}
