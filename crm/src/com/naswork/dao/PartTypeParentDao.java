package com.naswork.dao;

import java.util.List;

import com.naswork.model.PartTypeParent;
import com.naswork.vo.PageModel;

public interface PartTypeParentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PartTypeParent record);

    int insertSelective(PartTypeParent record);

    PartTypeParent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartTypeParent record);

    int updateByPrimaryKey(PartTypeParent record);
    
    public List<PartTypeParent> listPage(PageModel<PartTypeParent> page);
}