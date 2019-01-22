package com.naswork.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.naswork.dao.PurchaseApplicationFormDao;
import com.naswork.dao.SeqKeyDao;
import com.naswork.model.PurchaseApplicationForm;
import com.naswork.model.SeqKey;
import com.naswork.service.PurchaseApplicationFormService;
import com.naswork.vo.UserVo;

@Service("purchaseApplicationFormService")
public class PurchaseApplicationFormServiceImpl implements PurchaseApplicationFormService{
	@Resource
	private PurchaseApplicationFormDao purchaseApplicationFormDao;
	@Resource
	private SeqKeyDao seqKeyDao;

    public int insertSelective(PurchaseApplicationForm record){
    	return purchaseApplicationFormDao.insertSelective(record);
    }

    public PurchaseApplicationForm selectByPrimaryKey(Integer id){
    	return purchaseApplicationFormDao.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(PurchaseApplicationForm record){
    	return purchaseApplicationFormDao.updateByPrimaryKeySelective(record);
    }
    
    public PurchaseApplicationForm add(UserVo userVo,Integer clientOrderId){
    	PurchaseApplicationForm purchaseApplicationForm = new PurchaseApplicationForm();
    	purchaseApplicationForm.setUserId(new Integer(userVo.getUserId()));
    	purchaseApplicationForm.setApplicationDate(new Date());
    	purchaseApplicationForm.setApplicationNumber(getApplicationNumber());
    	purchaseApplicationForm.setClientOrderId(clientOrderId);
    	purchaseApplicationFormDao.insertSelective(purchaseApplicationForm);
    	return purchaseApplicationForm;
    }
    
    public String getApplicationNumber(){
    	SeqKey seqKey = new SeqKey();
    	StringBuffer buffer = new StringBuffer();
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String yearKey = Integer.toString(year % 100);
    	seqKey.setKeyName(yearKey);
    	seqKey.setSchemaName("crm");
    	seqKey.setTableName("purchase_application_form");
    	SeqKey seqKey2 = seqKeyDao.findMaxSeq(seqKey);
    	Integer seqNumber=null;
    	if (seqKey2!=null) {
			seqNumber = seqKey2.getSeq().intValue();
			seqNumber++;
			seqKey.setSeq(seqNumber);
			seqKeyDao.updateByPrimaryKeySelective(seqKey);
		}else{
			seqKey.setUpdateTimestamp(new Date());
			seqKey.setSeq(1001);
			seqNumber=1001;
			seqKeyDao.insertSelective(seqKey);
		}
    	buffer.append(year).append(month).append(day).append(seqNumber);
    	return buffer.toString();
    }
    
    public PurchaseApplicationForm findByClientOrderId(Integer clientOrderId){
    	return purchaseApplicationFormDao.findByClientOrderId(clientOrderId);
    }
	
}
