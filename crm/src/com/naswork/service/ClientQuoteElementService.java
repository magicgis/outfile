package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;

public interface ClientQuoteElementService {

	 /*
     * 根据询价明细ID查询报价明细ID
     */
    public ClientQuoteElement findCqe(Integer clientInquiryElementId,Integer clientQuoteId);
    
    void insertSelective(ClientQuoteElement record);
    
    void insert(ClientQuoteElement record);
    
    void updateByPrimaryKey(ClientQuoteElement record);
    
    public MessageVo uploadExcel(MultipartFile multipartFile, String clientinquiryquotenumber);
    
    ClientQuoteElement selectByPrimaryKey(Integer id);
    
    public Object inspectProfitMargin(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public List<ClientQuoteElementVo> selectByElementId(String partCode);
    
    /*public List<ClientQuoteElement> getByClientInquiryElementId(Integer clientInquiryElementId);*/
    
    public boolean addInHistoryQuote(ClientQuoteElement clientQuoteElement);
    
    public Double getTotalById(Integer id);
    
    public ClientQuoteElement selectByClientInquiryElementId(Integer clientInquiryElementId);
    
    public void insertInHis(ClientQuoteElement clientQuoteElement);
    
	
}
