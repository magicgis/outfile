package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.StockMarketSupplierMapDao;
import com.naswork.model.StockMarketSupplierMap;
import com.naswork.service.StockMarketSupplierMapService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("stockMarketSupplierMapService")
public class StockMarketSupplierMapServiceImpl implements
		StockMarketSupplierMapService {

	@Resource
	private StockMarketSupplierMapDao stockMarketSupplierMapDao;
	
	public void insertSelective(StockMarketSupplierMap record){
		stockMarketSupplierMapDao.insertSelective(record);
	}

    public StockMarketSupplierMap selectByPrimaryKey(Integer id){
    	return stockMarketSupplierMapDao.selectByPrimaryKey(id);
    }

    public void updateByPrimaryKeySelective(StockMarketSupplierMap record){
    	stockMarketSupplierMapDao.updateByPrimaryKeySelective(record);
    }
    
    public void listPage(PageModel<StockMarketSupplierMap> page,GridSort sort){
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(stockMarketSupplierMapDao.listPage(page));
    }
    
    public void deleteByPrimaryKey(Integer id){
    	stockMarketSupplierMapDao.deleteByPrimaryKey(id);
    }
    
    public StockMarketSupplierMap checkRecord(StockMarketSupplierMap stockMarketSupplierMap){
    	return stockMarketSupplierMapDao.checkRecord(stockMarketSupplierMap);
    }
	
}
