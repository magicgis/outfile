package com.naswork.service;

import java.util.List;

import com.naswork.model.ImportStorageLocationList;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ImportStorageLocationListService {
	int deleteByPrimaryKey(Integer id);

    int insert(ImportStorageLocationList record);

    int insertSelective(ImportStorageLocationList record);

    ImportStorageLocationList selectByPrimaryKey(Integer id);
    
    ImportStorageLocationList selectByLocation(String location);
    
    List<ImportStorageLocationList> selectSourceNumber(String location);
    
    List<ImportStorageLocationList> selectAll();

    int updateByPrimaryKeySelective(ImportStorageLocationList record);

    int updateByPrimaryKey(ImportStorageLocationList record);
    
    public void listPage(PageModel<ImportStorageLocationList> page);
    
    public void addWithTable(List<ImportStorageLocationList> list,Integer id);
    
    public void getLocationByInstructionsId(PageModel<ImportStorageLocationList> page, String where,
			GridSort sort);
}
