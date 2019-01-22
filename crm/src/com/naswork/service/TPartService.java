package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.CrmStock;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.model.TPartUploadBackup;
import com.naswork.module.marketing.controller.clientinquiry.BlackList;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.PageModel;

public interface TPartService {

    int insertSelective(TPart record);

    TPart selectByPrimaryKey(String bsn);

    int updateByPrimaryKeySelective(TPart record);
    
    void update(TPart record);
    
    public List<String> getBsn(String partNumberCode);
    
    /*
     * excel上传
     */
    public MessageVo excelUpload(MultipartFile multipartFile,Integer userId);
    
    /*
     * 列表
     */
    public void listPage(PageModel<TPartUploadBackup> page);
    
    void blacklist(PageModel<BlackList> page);
    
    /*
     * 删除
     */
    public void delete(Integer userId);
    
    List<TPart> selectByPartNumberCode(String partNumberCode);
    
    public void add(Integer clientInquiryElementId,String msn);
    
    public MessageVo updateType(MultipartFile multipartFile);
    
    public List<String> getCageCodeByShortCode(String shortCode);
    
    public String getBsnByPartAndMsn(String shortPartNum,String msn);
    
    public List<CrmStock> selectByMark(Integer correlationMark);
    
    public List<CrmStock> selectByMarkAndBsn(PageModel<CrmStock> page);
    
    public List<TPart> getByImportElementId(Integer importElementId);
    
    public List<SystemCode> getSystemByType(String type);
    
    public List<TPart> selectByPart(String partNumber);
	
}
