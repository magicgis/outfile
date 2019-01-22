package com.naswork.service;

import java.util.List;

import com.naswork.model.ExchangeImportPackage;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

public interface ExchangeImportPackageService {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeImportPackage record);

    int insertSelective(ExchangeImportPackage record);

    ExchangeImportPackage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeImportPackage record);

    int updateByPrimaryKey(ExchangeImportPackage record);
    
    public void warnListPage(PageModel<ExchangeImportPackage> page, String where,
			GridSort sort);
    
    public void listPage(PageModel<ExchangeImportPackage> page, String where,
			GridSort sort);
    
    public MessageVo email(UserVo userVo);

}
