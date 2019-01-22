package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.OnPassageStorageDao;
import com.naswork.model.OnPassageStorage;
import com.naswork.service.OnPassageStroageService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("onPassageStroageService")
public class OnPassageStroageServiceImpl implements OnPassageStroageService {

	@Resource
	private OnPassageStorageDao onPassageStroageDao;
	
	@Override
	public int insertSelective(OnPassageStorage record) {
		return onPassageStroageDao.insertSelective(record);
	}

	@Override
	public OnPassageStorage selectByPrimaryKey(Integer id) {
		return onPassageStroageDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OnPassageStorage record) {
		return onPassageStroageDao.updateByPrimaryKeySelective(record);
	}
	
	public void listPage(PageModel<OnPassageStorage> page,String where,GridSort sort){
		if(where != null && !"".equals(where)){
			page.put("where", where);
		}
		
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(onPassageStroageDao.listPage(page));
	}
	
	public List<OnPassageStorage> selectOnPassageAmount(PageModel<OnPassageStorage> page){
		return onPassageStroageDao.selectBySupplierQuoteElementId(page);
	}
	
	public void updateBySoeIdAndCoeId(Integer supplierOrderElementId,Integer clientOrderElementId){
		onPassageStroageDao.updateBySoeIdAndCoeId(supplierOrderElementId, clientOrderElementId);
	}

}
