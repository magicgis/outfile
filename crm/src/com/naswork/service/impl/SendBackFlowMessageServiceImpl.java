package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.SendBackFlowMessageDao;
import com.naswork.model.SendBackFlowMessage;
import com.naswork.service.SendBackFlowMessageService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

/**
 * @author: Tanoy
 * @date:2018年8月23日 下午5:24:55
 * @version :1.0
 * 
 */
@Service("sendBackFlowMessageService")
public class SendBackFlowMessageServiceImpl implements
		SendBackFlowMessageService {

	@Resource
	private SendBackFlowMessageDao sendBackFlowMessageDao;
	
	@Override
	public int insertSelective(SendBackFlowMessage record) {
		return sendBackFlowMessageDao.insertSelective(record);
	}

	@Override
	public SendBackFlowMessage selectByPrimaryKey(Integer id) {
		return sendBackFlowMessageDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SendBackFlowMessage record) {
		return sendBackFlowMessageDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void getSendBackListPage(
			PageModel<SendBackFlowMessage> page,String where,GridSort sort) {
		if (where != null && !"".equals(where)) {
			page.put("where", where);
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<SendBackFlowMessage> list = sendBackFlowMessageDao.getSendBackListPage(page);
    	page.setEntities(sendBackFlowMessageDao.getSendBackListPage(page));
	}

}
