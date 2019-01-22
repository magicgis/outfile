package com.naswork.service;

import java.util.List;

import com.naswork.module.storage.controller.assetpackage.ScfseVo;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.SupplierCommissionForStockmarketElement;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

public interface SupplierCommissionForStockmarketElementService {

	public void insertSelective(SupplierCommissionForStockmarketElement record);

	public SupplierCommissionForStockmarketElement selectByPrimaryKey(Integer id);

	public void updateByPrimaryKeySelective(SupplierCommissionForStockmarketElement record);
	
	public MessageVo uploadExcel(MultipartFile multipartFile, Integer id);
	
	public void listPage(PageModel<SupplierCommissionForStockmarketElement> page,GridSort sort);
	
	public ResultVo AddInquiryAndCrawlElement(Integer id,Integer airType);
	 
	public List<SupplierCommissionForStockmarketElement> selectByForeign(Integer id);
	
	public Double getCrawlPercent(Integer id);

	List<ScfseVo> getListData(PageModel<ScfseVo> pageModel);

	String getMainPricesByPartNumber(String partNum);

	List<String> getNewPricesByPartNumber(PageModel<String> page);

	List<String> getOldPricesByPartNumber(String partNum);

	String getMroRepairByPartNumber(String partNum);

	String getMroOverhaulByPartNumber(String partNum);

	MessageVo uploadArPriceExcel(MultipartFile multipartFile);

}
