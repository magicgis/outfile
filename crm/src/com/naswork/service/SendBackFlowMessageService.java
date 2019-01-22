package com.naswork.service;

import java.util.List;

import com.naswork.model.SendBackFlowMessage;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

/**
 * @author: Tanoy
 * @date:2018年8月23日 下午5:24:06
 * @version :1.0
 * 
 */
public interface SendBackFlowMessageService {
	
	public int insertSelective(SendBackFlowMessage record);

	public SendBackFlowMessage selectByPrimaryKey(Integer id);

	public int updateByPrimaryKeySelective(SendBackFlowMessage record);
	
	public void getSendBackListPage(PageModel<SendBackFlowMessage> page,String where,GridSort sort);

}
