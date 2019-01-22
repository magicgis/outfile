package com.naswork.service;

import java.util.List;

import com.naswork.model.ExchangeExportPackage;
import com.naswork.vo.PageModel;

public interface ExchangeExportPackageService {
	
    int insertSelective(ExchangeExportPackage record);

    ExchangeExportPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeExportPackage record);
    
    public void listPage(PageModel<ExchangeExportPackage> page);
    
    public int deleteByPrimaryKey(Integer id);
    
    public Double sumByImportId(Integer exchangeImportPackageId);

}
