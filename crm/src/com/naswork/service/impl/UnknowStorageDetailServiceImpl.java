package com.naswork.service.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.naswork.dao.UnknowStorageDetailDao;
import com.naswork.model.UnknowStorageDetail;
import com.naswork.service.UnknowStorageDetailService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("unknowStorageDetailService")
public class UnknowStorageDetailServiceImpl implements
		UnknowStorageDetailService {

	@Resource
	private UnknowStorageDetailDao unknowStorageDetailDao;
	
	@Override
	public void insertSelective(UnknowStorageDetail record) {
		unknowStorageDetailDao.insertSelective(record);
	}

	@Override
	public UnknowStorageDetail selectByPrimaryKey(Integer id) {
		return unknowStorageDetailDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(UnknowStorageDetail record) {
		unknowStorageDetailDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void listPage(PageModel<UnknowStorageDetail> page, String where,
			GridSort sort) {
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
		page.setEntities(unknowStorageDetailDao.listPage(page));
	}

}
