package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.DueRemindDao;
import com.naswork.model.ClientInquiry;
import com.naswork.module.marketing.controller.dueremind.DueRemindListVo;
import com.naswork.service.DueRemindService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("dueRemindService")
public class DueRemindServiceImpl implements DueRemindService {

	@Resource
	private DueRemindDao dueRemindDao;
	@Override
	public void findDueRemind(PageModel<DueRemindListVo> page, String searchString, GridSort sort) {
		if(null!=searchString&&!searchString.equals("")){
		page.put("searchString", searchString);
		}
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<DueRemindListVo> list= dueRemindDao.findDueRemindPage(page);
		page.setEntities(list);
	}
	@Override
	public void refuse(ClientInquiry clientInquiry) {
		dueRemindDao.refuse(clientInquiry);		
	}
	@Override
	public int findQuoteCount(Integer clientinquiryid) {
		return dueRemindDao.findQuoteCount(clientinquiryid);
	}
	@Override
	public int findInquiryCount(Integer clientinquiryid) {
		return dueRemindDao.findInquiryCount(clientinquiryid);
	}

}
