package com.naswork.service;

import com.naswork.model.PartTypeParent;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface PartTypeParentService {

    int insertSelective(PartTypeParent record);

    PartTypeParent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartTypeParent record);
    
    public void listPage(PageModel<PartTypeParent> page, String where,
			GridSort sort);
}
