package com.naswork.dao;

import com.naswork.model.ArPricePartMapping;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.module.storage.controller.assetpackage.ArPricePartMappingVo;
import com.naswork.vo.PageModel;
import org.omg.CORBA.Object;

import java.util.List;

public interface ArPricePartMappingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ArPricePartMapping record);

    int insertSelective(ArPricePartMapping record);

    ArPricePartMapping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArPricePartMapping record);

    int updateByPrimaryKey(ArPricePartMapping record);
    
    public ArPricePartMapping selectByPartNumber(String partNumber);

    List<ArPricePartMappingVo> getARPriceDataPage(PageModel<ArPricePartMappingVo> page);

    List<ArPricePartMapping> getArPriceListById(int id);

    ArPricePartMapping getNewArPriceByPartNumber(String partNumber);
}