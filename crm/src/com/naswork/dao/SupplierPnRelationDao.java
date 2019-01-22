package com.naswork.dao;

import java.util.List;

import com.naswork.model.Supplier;
import com.naswork.model.SupplierPnRelationKey;
import com.naswork.module.marketing.controller.partsinformation.SupplierAbilityVo;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.vo.PageModel;

public interface SupplierPnRelationDao {
	List<SupplierPnRelationKey> selectByPrimaryKeyPage(PageModel<SupplierPnRelationKey> page);
	
	List<SupplierPnRelationKey> selectByNameAndNum(String partName);
	
    void deleteByPrimaryKey(SupplierPnRelationKey key);

    void insert(SupplierPnRelationKey record);

    void insertSelective(SupplierPnRelationKey record);
    
    public List<FactoryVo> inquiryList(String partNumberCode);
    
    public List<FactoryVo> getTpart(String partNumberCode);
    
    public List<Integer> selectByBsn(String bsn);
    
    SupplierPnRelationKey  selectBybsn(String bsn,String supplierId);
    
    public List<SupplierAbilityVo> listPage(PageModel<SupplierAbilityVo> page);
    
    void updateByPrimaryKey(SupplierPnRelationKey record);

    void updateByPrimarySelectiveKey(SupplierPnRelationKey record);
    
    public List<FactoryVo> getTpartByMsnFlag(PageModel<FactoryVo> page);
}