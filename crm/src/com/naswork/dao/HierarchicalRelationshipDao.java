package com.naswork.dao;

import java.util.List;

import com.naswork.model.HierarchicalRelationship;
import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.vo.PageModel;

public interface HierarchicalRelationshipDao {
    int deleteByPrimaryKey(Integer id);

    int insert(HierarchicalRelationship record);

    int insertSelective(HierarchicalRelationship record);

    HierarchicalRelationship selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HierarchicalRelationship record);

    int updateByPrimaryKey(HierarchicalRelationship record);
    
    public List<HierarchicalRelationship> relationListPage(PageModel<HierarchicalRelationship> page);
    
    List<HierarchicalRelationship> selectByUserId(String userId);
}