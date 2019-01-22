package com.naswork.dao;

import java.util.List;

import com.naswork.model.CrmStock;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.module.crmstock.controller.CaacVo;
import com.naswork.module.marketing.controller.clientinquiry.BlackList;
import com.naswork.vo.PageModel;

public interface TPartDao {
    int deleteByPrimaryKey(String bsn);

    int insert(TPart record);

    int insertSelective(TPart record);

    TPart selectByPrimaryKey(String bsn);

    int updateByPrimaryKeySelective(TPart record);

    int updateByPrimaryKey(TPart record);
    
    /*
     * 获取BSN
     */
    public List<String> getBsn(String partNumberCode);
    
    /*
     * 获取件号名
     */
    public List<TPart> getTPart(TPart tPart);
    
    /*
     * 获取寿命
     */
    public List<Integer> getShelfLife(TPart tPart);
    
    List<TPart> selectByPartNumberCode(String partNumberCode);
    
    List<BlackList> blacklist(PageModel<BlackList> page);
    
    public List<String> selectByPartNumAndCageCode(String partNum,String CageCode,String msnFlag);
    
    public List<CaacVo> getMessageInCaac(String partNumber);
    
    public List<CaacVo> getPartInCaac();
    
    public void updateWrongPart(String replacePartNumber,String partNumber);
    
    public List<TPart> getTPartByShort(TPart tPart);
    
    public Integer getMaxCorrelationMark();
    
    public List<String> getCageCodeByShortCode(String shortCode);
    
    public String getBsnByPartAndMsn(String shortPartNum,String msn);
    
    public List<String> getBsnByPartNumAndMsn(String partNumber,String msn);
    
    public List<CrmStock> selectByMark(Integer correlationMark);
    
    public List<CrmStock> selectByMarkAndBsn(PageModel<CrmStock> page);
    
    public List<TPart> getByImportElementId(Integer importElementId);
    
    public List<SystemCode> getSystemByType(String type);
    
    public List<TPart> selectByPart(String partNumber);
    
    public List<SystemCode> getSystemCodeByValue(String value);
}