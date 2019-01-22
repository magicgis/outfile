package com.naswork.service;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.SupplierInquiryElement;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;

public interface SupplierInquiryElementService {

	/*
	 * 保存
	 */
	public int insertSelective(SupplierInquiryElement supplierInquiryElement);
	
	public MessageVo uploadExcel(MultipartFile multipartFile);
	
	public boolean addInquiry(String [] ids,Integer clientInquiryElementId);
	
	public boolean insertInquiry(Integer supplierId,Integer clientInquiryElementId);
	
}
