package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierPnRelationBackUp;
import com.naswork.model.SupplierPnRelationBackUpKey;
import com.naswork.vo.PageModel;

public interface SupplierPnRelationBackUpDao {
    int deleteByPrimaryKey(SupplierPnRelationBackUpKey key);

    int insert(SupplierPnRelationBackUp record);

    int insertSelective(SupplierPnRelationBackUp record);

    SupplierPnRelationBackUp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierPnRelationBackUp record);

    int updateByPrimaryKey(SupplierPnRelationBackUp record);
    
    public List<SupplierPnRelationBackUp> getByUserId(Integer userId);
    
    public void deleteMessage(Integer userId);
}