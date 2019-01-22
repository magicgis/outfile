package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.MpiMessageDao;
import com.naswork.model.MpiMessage;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.MpiService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("miService")
public class MpiServiceImpl implements MpiService {

	@Resource
	private MpiMessageDao mpiMessageDao;
	
	@Override
	public void insertSelective(MpiMessage record) {
		mpiMessageDao.insertSelective(record);
	}

	@Override
	public MpiMessage selectByPrimaryKey(Integer id) {
		return mpiMessageDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(MpiMessage record) {
		mpiMessageDao.updateByPrimaryKeySelective(record);
	}
	
	/*
     * 列表页面数据
     * 
     */
    public void listPage(PageModel<MpiMessage> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(mpiMessageDao.listPage(page));
    }
    
    public List<SystemCode> getList(){
    	return mpiMessageDao.getSystemList();
    }
    
    public void deleteByPrimaryKey(Integer id){
    	mpiMessageDao.deleteByPrimaryKey(id);
    }

}
