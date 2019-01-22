package com.naswork.dao;

import java.util.List;

import com.naswork.model.StorageToOrderEmail;
import com.naswork.module.marketing.controller.clientorder.EmailVo;

public interface StorageToOrderEmailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(StorageToOrderEmail record);

    int insertSelective(StorageToOrderEmail record);

    StorageToOrderEmail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StorageToOrderEmail record);

    int updateByPrimaryKey(StorageToOrderEmail record);
    
    public void insertSelectiveByEmailVo(EmailVo emailVo);
    
    public List<StorageToOrderEmail> getUnfinishListUser();
    
    public List<EmailVo> getEmailElements(String userId,String orderNumber,String oldImportNumber);
}