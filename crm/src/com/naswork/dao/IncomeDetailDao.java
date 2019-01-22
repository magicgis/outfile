package com.naswork.dao;

import java.util.List;

import com.naswork.model.IncomeDetail;
import com.naswork.vo.PageModel;

public interface IncomeDetailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeDetail record);

    int insertSelective(IncomeDetail record);

    IncomeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeDetail record);

    int updateByPrimaryKey(IncomeDetail record);
    
    public List<IncomeDetail> getByIncomeIdPage(PageModel<IncomeDetail> page);
    
    public Integer getTerms(Integer clientOrderElementId);
    
    public Double getTotalIncome(Integer clientOrderId);
    
    public Double getOrderTotal(Integer clientOrderId);
    
    public IncomeDetail getTotalByClientOrderElementId(Integer clientOrderElementId);
}