package com.naswork.service;

import com.naswork.model.HierarchicalRelationship;
import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.vo.PageModel;

public interface HierarchicalRelationshipService {
	 int deleteByPrimaryKey(Integer id);

	    int insert(HierarchicalRelationship record);

	    int insertSelective(HierarchicalRelationship record);

	    HierarchicalRelationship selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(HierarchicalRelationship record);

	    int updateByPrimaryKey(HierarchicalRelationship record);
	    
	    public void relationListPage(PageModel<HierarchicalRelationship> page);
}
