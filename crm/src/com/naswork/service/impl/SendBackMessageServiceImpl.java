package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SendBackMessageDao;
import com.naswork.model.SendBackMessage;
import com.naswork.service.SendBackMessageService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("sendBackMessageService")
public class SendBackMessageServiceImpl implements SendBackMessageService {

	@Resource
	private SendBackMessageDao sendBackMessageDao;
	
	@Override
	public void insertSelective(SendBackMessage record) {
		sendBackMessageDao.insertSelective(record);
	}

	@Override
	public SendBackMessage selectByPrimaryKey(Integer id) {
		return sendBackMessageDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(SendBackMessage record) {
		sendBackMessageDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void listPage(PageModel<SendBackMessage> page, String where,
			GridSort sort) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(sendBackMessageDao.listPage(page));
	}
	
	public void getAddListPage(PageModel<SendBackMessage> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(sendBackMessageDao.getAddListPage(page));
	}

}
