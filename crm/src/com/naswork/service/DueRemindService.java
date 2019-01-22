package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientInquiry;
import com.naswork.module.marketing.controller.dueremind.DueRemindListVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface DueRemindService {

	void findDueRemind(PageModel<DueRemindListVo> page,String searchString, GridSort sort);
	
	void refuse(ClientInquiry clientInquiry);
	
	int findQuoteCount(Integer clientinquiryid);
	
	int findInquiryCount(Integer clientinquiryid);
}
