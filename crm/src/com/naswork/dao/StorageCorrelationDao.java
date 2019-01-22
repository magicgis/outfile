package com.naswork.dao;

import java.util.List;

import org.apache.pdfbox.pdmodel.PageMode;

import com.naswork.model.StorageCorrelation;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.vo.PageModel;

public interface StorageCorrelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StorageCorrelation record);

    int insertSelective(StorageCorrelation record);

    StorageCorrelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StorageCorrelation record);

    int updateByPrimaryKey(StorageCorrelation record);
    
    public List<StorageCorrelation> selectByImportId(Integer importId);
    
    public List<StorageCorrelation> selectByCorrelationId(PageModel<String> page);
    
    public StorageCorrelation getList(PageModel<StorageDetailVo> page);
    
}