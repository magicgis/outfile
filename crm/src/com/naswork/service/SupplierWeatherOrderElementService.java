package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierWeatherOrder;
import com.naswork.model.SupplierWeatherOrderElement;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.vo.PageModel;

public interface SupplierWeatherOrderElementService {
	   int deleteByPrimaryKey(Integer id);

	    int insert(SupplierWeatherOrderElement record);

	    int insertSelective(SupplierWeatherOrderElement record);

	    SupplierWeatherOrderElement selectByPrimaryKey(Integer id);
	    
	    List<SupplierWeatherOrderElement> selectByClientOrderElementId(Integer id);

	    int updateByPrimaryKeySelective(SupplierWeatherOrderElement record);

	    int updateByPrimaryKey(SupplierWeatherOrderElement record);
	    
	    public MessageVo uploadExcel(MultipartFile multipartFile, String clientOrderId,String taskdefname);
	    
	    public MessageVo updateuploadExcel(MultipartFile multipartFile, String clientOrderId);
	    
	    public MessageVo addSupplierWeatherOrder(SupplierWeatherOrderElement element,SupplierQuoteElement supplierQuoteElement);
	    
	    ClientOrderElementVo findById(Integer id);
	    
	    public List<SupplierWeatherOrder> getFeeInfo(PageModel<SupplierWeatherOrder> page);
	    
	    public boolean saveFee(List<SupplierWeatherOrder> list);
	    
	    public SupplierWeatherOrder getByOrderIdAndSupplier(Integer clientWeatherOrderId,Integer supplierId);
	    
	    public Double getAmontBySupplier(Integer clientWeatherOrderId,Integer supplierId);
	    
	    public Double getAmountByClientOrder(Integer clientWeatherOrderElementId);
}
