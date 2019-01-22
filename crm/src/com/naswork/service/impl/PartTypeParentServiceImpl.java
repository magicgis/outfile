package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.PartTypeParentDao;
import com.naswork.model.PartTypeParent;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.PartTypeParentService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("partTypeParentService")
public class PartTypeParentServiceImpl implements PartTypeParentService {

	@Resource
	private PartTypeParentDao partTypeParentDao;
	
	@Override
	public int insertSelective(PartTypeParent record) {
		return partTypeParentDao.insertSelective(record);
	}

	@Override
	public PartTypeParent selectByPrimaryKey(Integer id) {
		return partTypeParentDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(PartTypeParent record) {
		return partTypeParentDao.updateByPrimaryKeySelective(record);
	}
	
	/*
     * 列表页面数据
     * 
     */
    public void listPage(PageModel<PartTypeParent> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(partTypeParentDao.listPage(page));
    }

}
