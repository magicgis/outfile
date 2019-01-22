package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.IncomeDao;
import com.naswork.model.Income;
import com.naswork.service.IncomeService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("incomeService")
public class IncomeServiceImpl implements IncomeService {
	@Resource
	private IncomeDao incomeDao;

	public void IncomePage(PageModel<Income> page){
		page.setEntities(incomeDao.IncomePage(page));
	}
	
	public int insertSelective(Income record){
		return incomeDao.insertSelective(record);
	}

	public Income selectByPrimaryKey(Integer id){
		return incomeDao.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(Income record){
		return incomeDao.updateByPrimaryKeySelective(record);
	}
	
    public void listPage(PageModel<Income> page,String where,GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(incomeDao.listPage(page));
    }
    
    public Double getCompleteTotal(Integer clientOrderId){
    	return incomeDao.getCompleteTotal(clientOrderId);
    }
	
}
