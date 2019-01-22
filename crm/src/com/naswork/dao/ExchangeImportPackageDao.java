package com.naswork.dao;

import java.util.List;

import com.naswork.model.ExchangeImportPackage;
import com.naswork.vo.PageModel;

public interface ExchangeImportPackageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeImportPackage record);

    int insertSelective(ExchangeImportPackage record);

    ExchangeImportPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeImportPackage record);

    int updateByPrimaryKey(ExchangeImportPackage record);
    
    public List<ExchangeImportPackage> warnListPage(PageModel<ExchangeImportPackage> page);
    
    public List<ExchangeImportPackage> listPage(PageModel<ExchangeImportPackage> page);
    
    public List<ExchangeImportPackage> getEmailList(Integer userId);
    
    public List<Integer> getEmailUserId();
}