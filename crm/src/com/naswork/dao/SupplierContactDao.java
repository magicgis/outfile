package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierContact;
import com.naswork.vo.PageModel;

public interface SupplierContactDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierContact record);

    int insertSelective(SupplierContact record);

    SupplierContact selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierContact record);

    int updateByPrimaryKey(SupplierContact record);
    
    List<SupplierContact> findSupplierContactPage(PageModel<SupplierContact> page);
    
    public List<SupplierContact> getEmails(Integer supplierId);
    
    public List<SupplierContact> findBySupplierId(Integer supplierId);
    
    public List<SupplierContact> getEmailPerson(Integer supplierId);
    
    public void updateEmailPersonBySupplierId(Integer supplierId);
    
    public List<SupplierContact> getEmailList(Integer supplierId);
}