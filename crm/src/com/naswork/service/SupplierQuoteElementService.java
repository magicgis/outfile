package com.naswork.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.SupplierQuoteElement;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierInquiryEmelentVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

public interface SupplierQuoteElementService {
	
	 void updateByPrimaryKey(SupplierQuoteElement record);
	 
	 public MessageVo uploadExcel(MultipartFile multipartFile, Integer id) throws Exception;
	 
	 public MessageVo uploadPdf(MultipartFile multipartFile, Integer id) throws Exception;
	 
	 public MessageVo quoteUploadExcel(MultipartFile multipartFile, String data) throws Exception;
	 
	 List<SupplierInquiryEmelentVo> findsupplierinquiryelement(String supplierinquiryid,String supplierQuoteId);
	 
	 List<SupplierQuoteVo> findSupplierQuoteElement(Integer id);
	 
	 public ResultVo insertSelective(List<SupplierQuoteElement> list,Integer id) throws Exception;
	 
	 SupplierQuoteElement selectByPrimaryKey(Integer id);
	 
	 void insert(SupplierQuoteElement supplierQuoteElement);
	 
	 void updateByPrimaryKeySelective(SupplierQuoteElement record);
	 
	 void updateBySupplierQuoteId(SupplierQuoteElement record);
	 
	 public SupplierQuoteElement selectBySupplierOrderElementId(Integer supplierOrderElementId);
	 
	 public Integer getTaxReimbursementId(Integer supplierQuoteElementId);
	 
	 public Double getLatestPrice(String partNumber);
	 
	 public boolean addQuote(SupplierQuoteElement supplierQuoteElement);
	 
	 public void getByShortPartPage(PageModel<SupplierQuoteElement> page,GridSort sort);
	 
	 public void getByShortPart(PageModel<SupplierQuoteElement> page,GridSort sort);

	 public List<SupplierQuoteElement> getByOrderELementId(PageModel<String> page);
	 
	 public List<SupplierQuoteElement> getCompetitorPrice(String clientId,String shortPart);
	 
	 public boolean addQuotePriceInOrder(SupplierQuoteElement supplierQuoteElement,Integer clientQuoteElementId,String price,UserVo userVo);
	 
	 public Double getCountByQuoteId(Integer quoteId);
	 
	 public List<SupplierQuoteElement> getBySupplierQuoteId(Integer supplierQuoteId);
	 
	 public List<SupplierQuoteElement> getStorage(PageModel<SupplierQuoteElement> page);
	 
	 public Double getTotalByQuoteId(Integer supplierQuoteId);
	 
	 public void getHighPriceCrawl(PageModel<SupplierQuoteElement> page,GridSort sort);
}
