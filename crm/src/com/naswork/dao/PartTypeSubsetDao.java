package com.naswork.dao;

import java.util.List;

import com.naswork.model.PartTypeSubset;
import com.naswork.vo.PageModel;

public interface PartTypeSubsetDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PartTypeSubset record);

    int insertSelective(PartTypeSubset record);

    PartTypeSubset selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartTypeSubset record);

    int updateByPrimaryKey(PartTypeSubset record);
    
    public List<PartTypeSubset> listPage(PageModel<PartTypeSubset> page);
    
    public List<PartTypeSubset> selectByCode(String code);
    
    public List<PartTypeSubset> list();
    
}