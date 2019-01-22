package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.StaticClientQuotePrice;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

public interface StaticClientQuotePriceService {

    int insertSelective(StaticClientQuotePrice record);

    StaticClientQuotePrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaticClientQuotePrice record);
    
    public void listPage(PageModel<StaticClientQuotePrice> page, String where,
			GridSort sort);
	
    public MessageVo uploadExcel(MultipartFile multipartFile);
    
    public ResultVo addWithPage(List<StaticClientQuotePrice> list);
    
    public int deleteByPrimaryKey(Integer id);
    
    public List<Integer> getClients();
    
    public int findByClientId(Integer clientId);
}
