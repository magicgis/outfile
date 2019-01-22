package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.StaticClientQuotePrice;
import com.naswork.model.StaticSupplierQuotePrice;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

public interface StaticSupplierQuotePriceService {

    int insertSelective(StaticSupplierQuotePrice record);

    StaticSupplierQuotePrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaticSupplierQuotePrice record);
    
    public void listPage(PageModel<StaticSupplierQuotePrice> page, String where,
			GridSort sort);
    
    public MessageVo uploadExcel(MultipartFile multipartFile);
    
    public ResultVo addWithPage(List<StaticSupplierQuotePrice> list);
    
    public void edit(StaticSupplierQuotePrice staticSupplierQuotePrice);
    
    public int deleteByPrimaryKey(Integer id);
	
}
