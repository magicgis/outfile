package com.naswork.service;

import java.util.List;

import com.naswork.model.OnPassageStorage;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface OnPassageStroageService {

    public int insertSelective(OnPassageStorage record);

    public OnPassageStorage selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(OnPassageStorage record);
    
    public void listPage(PageModel<OnPassageStorage> page,String where,GridSort sort);
    
    public List<OnPassageStorage> selectOnPassageAmount(PageModel<OnPassageStorage> page);
    
    public void updateBySoeIdAndCoeId(Integer supplierOrderElementId,Integer clientOrderElementId);
	
}
