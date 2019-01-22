package com.naswork.dao;

import java.util.List;

import com.naswork.model.OnPassageStorage;
import com.naswork.vo.PageModel;

public interface OnPassageStorageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OnPassageStorage record);

    int insertSelective(OnPassageStorage record);

    OnPassageStorage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OnPassageStorage record);

    int updateByPrimaryKey(OnPassageStorage record);
    
    public List<OnPassageStorage> listPage(PageModel<OnPassageStorage> page);
    
    public List<OnPassageStorage> selectBySupplierQuoteElementId(PageModel<OnPassageStorage> page);
    
    public List<OnPassageStorage> getBySoeIdAndCoeId(OnPassageStorage record);
    
    public void updateBySoeIdAndCoeId(Integer supplierOrderElementId,Integer clientOrderElementId);
    
    public OnPassageStorage selectByCoeId(Integer clientOrderElementId);
}