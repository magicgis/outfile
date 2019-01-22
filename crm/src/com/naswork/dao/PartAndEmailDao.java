package com.naswork.dao;

import java.util.List;

import com.naswork.model.PartAndEmail;
import com.naswork.vo.PageModel;

public interface PartAndEmailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PartAndEmail record);

    int insertSelective(PartAndEmail record);

    PartAndEmail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartAndEmail record);

    int updateByPrimaryKey(PartAndEmail record);
    
    public List<Integer> getSupplierList();
    
    public List<String> getEmailList();
    
    public List<PartAndEmail> getBySupplierIdOrEmail(PageModel<String> page);
    
    public List<PartAndEmail> getElement(PageModel<String> page);
    
    public int getCountByTime(String time);
    
    public List<PartAndEmail> getPrepareEmailPage(PageModel<PartAndEmail> page);
    
    public void cancelRecord(PartAndEmail partAndEmail);
}