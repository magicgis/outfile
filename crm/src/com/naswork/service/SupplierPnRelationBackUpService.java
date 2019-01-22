package com.naswork.service;

import java.util.List;

import com.naswork.model.SupplierPnRelationBackUp;
import com.naswork.model.SupplierPnRelationBackUpKey;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.vo.PageModel;

public interface SupplierPnRelationBackUpService {

	int insertSelective(SupplierPnRelationBackUp record);

    SupplierPnRelationBackUp selectByPrimaryKey(Integer key);

    int updateByPrimaryKeySelective(SupplierPnRelationBackUp record);
    
    public int getByUserIdPage(PageModel<FactoryVo> page,Integer userId);
    
    public void deleteMessage(Integer userId);
}
