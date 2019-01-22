package com.naswork.service;

import com.naswork.model.PurchaseApplicationForm;
import com.naswork.vo.UserVo;

public interface PurchaseApplicationFormService {

    public int insertSelective(PurchaseApplicationForm record);

    public PurchaseApplicationForm selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(PurchaseApplicationForm record);
    
    public PurchaseApplicationForm add(UserVo userVo,Integer clientOrderId);
    
    public PurchaseApplicationForm findByClientOrderId(Integer clientOrderId);
	
}
