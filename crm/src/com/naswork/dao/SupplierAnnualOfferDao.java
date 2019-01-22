package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierAnnualOffer;
import com.naswork.vo.PageModel;

public interface SupplierAnnualOfferDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierAnnualOffer record);

    int insertSelective(SupplierAnnualOffer record);

    SupplierAnnualOffer selectByPrimaryKey(Integer id);
    
    List<SupplierAnnualOffer> selectByBn(String bsn);
    
    SupplierAnnualOffer selectByBnAndSupplier(String bsn,Integer supplierId);

    int updateByPrimaryKeySelective(SupplierAnnualOffer record);

    int updateByPrimaryKey(SupplierAnnualOffer record);
    
    public List<SupplierAnnualOffer> annualOfferListPage(PageModel<SupplierAnnualOffer> page);
}