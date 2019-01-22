package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientInquiry;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.dueremind.DueRemindListVo;
import com.naswork.vo.PageModel;

public interface DueRemindDao {

	List<DueRemindListVo> findDueRemindPage(PageModel<DueRemindListVo> page);
	
	void refuse(ClientInquiry clientInquiry);
	
	int findQuoteCount(Integer clientinquiryid);
	
	int findInquiryCount(Integer clientinquiryid);
}
