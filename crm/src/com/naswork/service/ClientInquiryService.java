package com.naswork.service;

import java.util.Date;
import java.util.List;

//import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientInquiryElementUpload;
import com.naswork.model.TPart;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientinquiry.EmailTemplateVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientinquiry.TenderVo;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

public interface ClientInquiryService {

	/*
     * 列表页面数据
     * 
     */
    public void listPage(PageModel<ClientInquiryVo> page, String where,
			GridSort sort);
    
    /*
     * 新增数据
     * 
     */
    public ClientInquiry add(ClientInquiry clientInquiry);
    
    /*
	 * 根据主键查询
	 */
	public ClientInquiry findById(Integer id);
	
	/*
	 * 更新
	 */
	public int updateByPrimaryKeySelective(ClientInquiry record);
	
	/*
     * 明细列表页面数据
     * 
     */
    public void ElePage(PageModel<ElementVo> page,GridSort sort);
    
    /*
     * 明细信息
     */
    public List<ElementVo> elelmentList(PageModel<ElementVo> page);
    
    /*
     * 手动增加信息
     */
    public List<ElementVo> tenderElelment(Integer id);
    
    /*
     * excel上传
     */
    public MessageVo uploadExcel(MultipartFile multipartFile, Integer id,Integer userId,UserVo userVo);
    
    /*
     * 竞争对手上传
     */
    public MessageVo uploadCompetitor(MultipartFile multipartFile, Integer id);
    
    /*
     * 根据单号获取id
     */
    public Integer findByQuoteNumber(String quoteNumber);
    
    /*
     * 保存竞争对手报价
     */
    public ResultVo saveTender(TenderVo tenderVo,Integer id);
    
    /*
     * 存档
     */
    public void excelBackup(POIExcelWorkBook wb,String FilePath,String FileName,String ywId,String ywTableName,String ywTablePkName);
    
    List<TPart> findisblacklist(String partnumbercode);
    
    /*
     *错误信息 
     */
    public void getErrorPage(PageModel<ClientInquiryElementUpload> page);
    
    /*
     * 删除错误信息
     */
    public void deleteError(Integer userId);
    
    public String getCodeFromPartNumber(String partNumber);
    
    /*
     * 新增询价单并发送邮件
     */
    public boolean sendEmail(Integer id,Integer specialCode,Boolean free);
    
    /*
     * 查找是否有未处理的参数处理异常
     */
    public void onpostcheck();
    
    /*
     * 发送通知异常的邮件
     */
//    public void sendpostexc(int id, String quote);
    
    /**
     * 询价单时间拼接
     */
    public String getDateStr(Date date);
    
    ClientInquiry selectByPrimaryKey(Integer id);
    
    public boolean supplierCommissionSale (Integer clientInquiryId);
    
    public EmailTemplateVo[] getEmailTemplates();
    
    public String getNumber(String partNumber);
    
}
