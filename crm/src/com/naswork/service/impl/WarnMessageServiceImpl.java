package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.WarnMessageDao;
import com.naswork.model.WarnMessage;
import com.naswork.service.WarnMessageService;

@Service("warnMessageService")
public class WarnMessageServiceImpl implements WarnMessageService {

	@Resource
	private WarnMessageDao warnMessageDao;
	
	
	
	@Override
	public int insertSelective(WarnMessage record) {
		return warnMessageDao.insertSelective(record);
	}

	@Override
	public WarnMessage selectByPrimaryKey(Integer id) {
		return warnMessageDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(WarnMessage record) {
		return warnMessageDao.updateByPrimaryKeySelective(record);
	}
	
	public List<Integer> selectByUserId(Integer userId){
		return warnMessageDao.selectByUserId(userId);
	}
	
	public List<WarnMessage> getMessage(Integer clientInquiryId){
		return warnMessageDao.getMessage(clientInquiryId);
	}

	public void updateStatus(Integer clientInquiryId){
		warnMessageDao.updateStatus(clientInquiryId);
	}
	
}
