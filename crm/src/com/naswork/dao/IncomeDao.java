package com.naswork.dao;

import java.util.List;

import com.naswork.model.Income;
import com.naswork.vo.PageModel;

public interface IncomeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Income record);

    int insertSelective(Income record);

    Income selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Income record);

    int updateByPrimaryKey(Income record);
    
    public List<Income> IncomePage(PageModel<Income> page);
    
    public Double getIncomeTotal(Integer clientInvoiceId);
    
    public Double getInvoiceTotal(Integer clientInvoiceId);
    
    /**
     * 收款列表
     */
    public List<Income> listPage(PageModel<Income> page);
    
    public Double getCompleteTotal(Integer clientOrderId);
    
    public Double getUnCompleteTotal(Integer clientOrderId);
}