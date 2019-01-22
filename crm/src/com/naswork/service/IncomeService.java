package com.naswork.service;

import java.util.List;

import com.naswork.model.Income;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface IncomeService {

	public void IncomePage(PageModel<Income> page);
	
	int insertSelective(Income record);

    Income selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Income record);
    
    /**
     * 收款列表
     */
    public void listPage(PageModel<Income> page,String where,GridSort sort);
    
    public Double getCompleteTotal(Integer clientOrderId);
	
}
