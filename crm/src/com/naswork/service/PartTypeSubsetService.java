package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.PartTypeSubset;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.PageModel;

public interface PartTypeSubsetService {

    int insertSelective(PartTypeSubset record);

    PartTypeSubset selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PartTypeSubset record);
    
    public void listPage(PageModel<PartTypeSubset> page);
    
    public MessageVo uploadExcel(MultipartFile multipartFile, Integer id);
    
    public List<PartTypeSubset> list();
    
    public List<PartTypeSubset> selectByCode(String code);
}
