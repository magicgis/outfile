package com.naswork.service;

import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.ClientInquiryElement;
import com.naswork.model.SupplierAnnualOffer;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.PageModel;

public interface SupplierAnnualOfferService {
	 int deleteByPrimaryKey(Integer id);

	    int insert(SupplierAnnualOffer record);

	    int insertSelective(SupplierAnnualOffer record);

	    SupplierAnnualOffer selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(SupplierAnnualOffer record);

	    int updateByPrimaryKey(SupplierAnnualOffer record);
	    
	    public void annualOfferListPage(PageModel<SupplierAnnualOffer> page);
	    
	    public MessageVo excelUpload(MultipartFile multipartFile,Integer id,String supplierPnType);
	    
	    void addSupplierQuote(ClientInquiryElement clientInquiryElement) throws ParseException;
}
