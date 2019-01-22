package com.naswork.service;

import java.util.List;

import com.naswork.model.CheckStorageByLocation;
import com.naswork.model.ExportPackageElement;
import com.naswork.model.ImportPackageElement;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;

public interface CheckStorageByLocationService {

	public void deleteByPrimaryKey(Integer id);
	
	public void insertSelective(CheckStorageByLocation record);

	public CheckStorageByLocation selectByPrimaryKey(Integer id);

    public void updateByPrimaryKeySelective(CheckStorageByLocation record);
    
    public List<CheckStorageByLocation> selectByLocation(String location);
    
    public void listPage(PageModel<ImportPackageElement> page,GridSort sort);
    
    /**
     * 查询是否有历史清单记录，没有就按照当前库位新增记录，有就按照现有记录继续清点
     * @param location
     */
    public void checkAndInsertRecord(String location);
	
    public List<CheckStorageByLocation> selectByImportPackageElementId(Integer importPackageElementId);
    
    public ResultVo checkStorage(ImportPackageElement importPackageElement);
}
