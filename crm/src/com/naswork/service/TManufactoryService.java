package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.TManufactory;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;

public interface TManufactoryService {

    int insertSelective(TManufactory record);

    TManufactory selectByPrimaryKey(String msn);
    
    List<TManufactory> selectByCageCode(String cageCode);
    
    public MessageVo uploadExcel(MultipartFile multipartFile) throws Exception;

    int updateByPrimaryKeySelective(TManufactory record);
    
    /**
     * 根据MSN查询
     * @param cageCode
     * @return
     */
    public List<TManufactory> getMsnByCageCode(String cageCode);
    
    public List<TManufactory> selectByMsn(String msn);
	
}
