package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.CheckStorageByLocationDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.model.CheckStorageByLocation;
import com.naswork.model.ExportPackageElement;
import com.naswork.model.ImportPackageElement;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.service.CheckStorageByLocationService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

@Service("checkStorageByLocationService")
public class CheckStorageByLocationServiceImpl implements
		CheckStorageByLocationService {

	@Resource
	private CheckStorageByLocationDao checkStorageByLocationDao;
	@Resource
	private ImportPackageElementDao importPackageElementDao;
	
	@Override
	public void deleteByPrimaryKey(Integer id) {
		checkStorageByLocationDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insertSelective(CheckStorageByLocation record) {
		checkStorageByLocationDao.insertSelective(record);
	}

	@Override
	public CheckStorageByLocation selectByPrimaryKey(Integer id) {
		return checkStorageByLocationDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(CheckStorageByLocation record) {
		checkStorageByLocationDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<CheckStorageByLocation> selectByLocation(String location) {
		return checkStorageByLocationDao.selectByLocation(location);
	}

	@Override
	public void listPage(
			PageModel<ImportPackageElement> page,GridSort sort) {
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(checkStorageByLocationDao.listPage(page));
	}
	
	public void checkAndInsertRecord(String location){
		try {
			List<CheckStorageByLocation> checkList = checkStorageByLocationDao.selectByLocation(location);
			if (checkList.size() == 0) {
				PageModel<StorageDetailVo> page = new PageModel<StorageDetailVo>();
				page.put("where", "a.location = '"+location+"'");
				List<StorageDetailVo> storageList = importPackageElementDao.getStorageWithTerm(page);
				for (StorageDetailVo storageDetailVo : storageList) {
					CheckStorageByLocation checkStorageByLocation = new CheckStorageByLocation();
					checkStorageByLocation.setLocation(location);
					checkStorageByLocation.setImportPackageElementId(storageDetailVo.getId());
					checkStorageByLocationDao.insertSelective(checkStorageByLocation);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ResultVo checkStorage(ImportPackageElement importPackageElement){
		try {
			List<CheckStorageByLocation> ipes = checkStorageByLocationDao.selectByImportPackageElementId(importPackageElement.getId());
			if (ipes.size() > 0) {
				for (int i = 0; i <ipes.size(); i++) {
					checkStorageByLocationDao.deleteByPrimaryKey(ipes.get(i).getId());
				}
				return new ResultVo(true, "更新记录完成！");
			}else {
				return new ResultVo(false, "记录在清点库存记录内不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "操作异常！");
		}
	}
	
	public List<CheckStorageByLocation> selectByImportPackageElementId(Integer importPackageElementId){
		return checkStorageByLocationDao.selectByImportPackageElementId(importPackageElementId);
	}

}
