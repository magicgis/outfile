package com.naswork.service;

import java.util.List;

import com.naswork.model.SendBackMessage;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SendBackMessageService {

	 public void insertSelective(SendBackMessage record);

	 SendBackMessage selectByPrimaryKey(Integer id);

	 public void updateByPrimaryKeySelective(SendBackMessage record);
	 
	 public void listPage(PageModel<SendBackMessage> page,String where,GridSort sort);
	 
	 public void getAddListPage(PageModel<SendBackMessage> page, String where,
				GridSort sort);
	
}
