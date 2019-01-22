package com.naswork.service;

import java.util.List;

import com.naswork.model.MpiMessage;
import com.naswork.model.SystemCode;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface MpiService {

	public void insertSelective(MpiMessage record);

	public MpiMessage selectByPrimaryKey(Integer id);

	public void updateByPrimaryKeySelective(MpiMessage record);
	
	 public void listPage(PageModel<MpiMessage> page, String where,
				GridSort sort);
	 
	 public List<SystemCode> getList();
	 
	 public void deleteByPrimaryKey(Integer id);
	
}
