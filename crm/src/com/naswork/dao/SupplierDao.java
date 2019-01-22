package com.naswork.dao;

import java.util.List;

import com.naswork.model.Supplier;
import com.naswork.model.SupplierContact;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import org.apache.ibatis.annotations.Param;

public interface SupplierDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Supplier record);

    void insertSelective(Supplier record);

    Supplier selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(Supplier record);

    int updateByPrimaryKey(Supplier record);
    
    List<Supplier> listPage(PageModel<Supplier> page);
    
    Supplier findByCode(String code);
    
    Supplier findByShortName(String shortName);
	
	Supplier findByName(String name);
    
    public Integer getCurrencyId(String code);
    
    public List<Supplier> Suppliers(PageModel<Supplier> page);
    
    public Integer findByUrl(String url);
    
    List<Supplier>  selectAll();
    
    public List<Supplier> getOutTimeSupplier(Integer userId);
    
    public List<String> getRoleNameBySupplierId(Integer supplierId);
    
    public Supplier selectByCode(String code);
    
    public List<Integer> checkByCode(String code);

    List<Supplier> getSupplierByCodeAndUserId(@Param("userId") String userId, @Param("supplierCode") String supplierCode);
}