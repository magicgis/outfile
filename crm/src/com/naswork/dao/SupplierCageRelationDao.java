package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierAnnualOffer;
import com.naswork.model.SupplierCageRelationKey;
import com.naswork.module.marketing.controller.partsinformation.SupplierAbilityVo;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.vo.PageModel;

public interface SupplierCageRelationDao {
    int deleteByPrimaryKey(SupplierCageRelationKey key);

    int insert(SupplierCageRelationKey record);

    int insertSelective(SupplierCageRelationKey record);
    
    public Integer selectByPrimaryKey(SupplierCageRelationKey supplierCageRelationKey);
    
    public List<FactoryVo> listPage(PageModel<FactoryVo> page);
    
    public List<SupplierAbilityVo> listByAbilityPage(PageModel<SupplierAbilityVo> page);
    
    public List<Integer> selectByMsn(String msn);
    
    
}