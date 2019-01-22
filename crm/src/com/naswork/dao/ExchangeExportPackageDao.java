package com.naswork.dao;

import java.util.List;

import com.naswork.model.ExchangeExportPackage;
import com.naswork.vo.PageModel;

public interface ExchangeExportPackageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeExportPackage record);

    int insertSelective(ExchangeExportPackage record);

    ExchangeExportPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeExportPackage record);

    int updateByPrimaryKey(ExchangeExportPackage record);
    
    public List<ExchangeExportPackage> listPage(PageModel<ExchangeExportPackage> page);
    
    public Double sumByImportId(Integer exchangeImportPackageId);
}