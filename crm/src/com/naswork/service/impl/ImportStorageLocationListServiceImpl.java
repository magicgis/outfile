package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ImportStorageLocationListDao;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.ImportStorageLocationListService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("importStorageLocationListServiceImpl")
public class ImportStorageLocationListServiceImpl implements ImportStorageLocationListService {

	@Resource
	private ImportStorageLocationListDao importStorageLocationListDao;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return importStorageLocationListDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ImportStorageLocationList record) {
		return importStorageLocationListDao.insert(record);
	}

	@Override
	public int insertSelective(ImportStorageLocationList record) {
		return importStorageLocationListDao.insertSelective(record);
	}

	@Override
	public ImportStorageLocationList selectByPrimaryKey(Integer id) {
		return importStorageLocationListDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ImportStorageLocationList record) {
		return importStorageLocationListDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ImportStorageLocationList record) {
		return importStorageLocationListDao.updateByPrimaryKey(record);
	}

	@Override
	public List<ImportStorageLocationList> selectAll() {
		return importStorageLocationListDao.selectAll();
	}

	@Override
	public ImportStorageLocationList selectByLocation(String location) {
		return importStorageLocationListDao.selectByLocation(location);
	}
	
	public void listPage(PageModel<ImportStorageLocationList> page){
		page.setEntities(importStorageLocationListDao.listPage(page));
	}
	
	public void addWithTable(List<ImportStorageLocationList> list,Integer id) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setStorehouseAddressId(id);
			importStorageLocationListDao.insertSelective(list.get(i));
		}
	}

	@Override
	public List<ImportStorageLocationList> selectSourceNumber(String location) {
		return importStorageLocationListDao.selectSourceNumber(location);
	}
	
	public void getLocationByInstructionsId(PageModel<ImportStorageLocationList> page, String where,
			GridSort sort){
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(importStorageLocationListDao.getLocationByInstructionsIdPage(page));
	}

}
