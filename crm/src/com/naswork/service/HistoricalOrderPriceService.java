package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.HistoricalOrderPrice;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface HistoricalOrderPriceService {

    int insertSelective(HistoricalOrderPrice record);

    HistoricalOrderPrice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HistoricalOrderPrice record);
    
    public void clientPage(PageModel<HistoricalOrderPrice> page, String where,
			GridSort sort);
    
    public void supplierPage(PageModel<HistoricalOrderPrice> page, String where,
			GridSort sort);
    
    public MessageVo clientUploadExcel(MultipartFile multipartFile);
    
    public MessageVo supplierUploadExcel(MultipartFile multipartFile);
    
    public List<HistoricalOrderPrice> selectByCLientInquiryId(Integer clientInuqiryId);
    
    public List<HistoricalOrderPrice> selectByBsn(String bsn);

    public HistoricalOrderPrice getPriceByBsn(String bsn);
    
    public MessageVo uploadExcel(MultipartFile multipartFile);
    
    public List<HistoricalOrderPrice> getByClient(String clientId,String bsn,String year);
    
    public List<HistoricalOrderPrice> getByPart(String partNumber);
}
