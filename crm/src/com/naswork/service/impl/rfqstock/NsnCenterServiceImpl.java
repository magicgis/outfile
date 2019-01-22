package com.naswork.service.impl.rfqstock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.rfqstock.NsnCenterDao;
import com.naswork.model.rfqstock.nsncenter.CageInfo;
import com.naswork.model.rfqstock.nsncenter.StockInfo;
import com.naswork.service.rfqstock.NsnCenterService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;

@Service("nsnCenterService")
public class NsnCenterServiceImpl implements NsnCenterService {
	@Resource
	private NsnCenterDao nsnCenterDao;
	
	@Override
	public void searchPageByCage(PageModel<CageInfo> page, String parameter, GridSort sort) {
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(nsnCenterDao.searchPageByCage(page));

	}

	@Override
	public void searchPageByNsnPart(PageModel<StockInfo> page, String parameter, GridSort sort) {
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<StockInfo> stockList = nsnCenterDao.searchPageByNsnPart(page);
		this.fetchReplaceNsn(stockList);
		page.setEntities(stockList);
	}
	
	private void fetchReplaceNsn(List<StockInfo> stockInfoList){
		StringBuilder sb = new StringBuilder();
		String formatString = " (nsn_id='%s' and cage_id='%s' and part_num='%s') ";
		for(StockInfo stockInfo: stockInfoList){
			sb.append(" OR ").append(String.format(formatString, 
					stockInfo.getNsnId(),
					stockInfo.getCageId(),
					stockInfo.getPartNum()));
		}
		PageData pd = new PageData();
		pd.put("where", sb.toString());
		List<Map<String, Object>> replaceNsnList = this.nsnCenterDao.findReplaceNsn(pd);
		
		
		Map<String, Set<String>> nsnIdMap = new HashMap<String, Set<String>>();
		for(Map<String, Object> map: replaceNsnList){
			String key = (String)map.get("cage_id")+(String)map.get("nsn_id")+(String)map.get("part_num");
			if(!nsnIdMap.containsKey(key)){
				nsnIdMap.put(key, new HashSet<String>());
			}
			nsnIdMap.get(key).add((String)map.get("replacedby_nsn_id"));
		}
		
		for(StockInfo stockInfo: stockInfoList){
			String key = stockInfo.getCageId()+stockInfo.getNsnId()+stockInfo.getPartNum();
			if(nsnIdMap.containsKey(key)){
				StringBuilder sbReplaced = new StringBuilder();
				for(String value:nsnIdMap.get(key)){
					sbReplaced.append(value).append(",");
				}
				stockInfo.setReplacedbyNsnId(sbReplaced.substring(0, sbReplaced.length()-1));
			}
		}
	}

}
