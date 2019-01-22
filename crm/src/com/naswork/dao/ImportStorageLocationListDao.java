package com.naswork.dao;

import java.util.List;

import com.naswork.model.ImportStorageLocationList;
import com.naswork.vo.PageModel;

public interface ImportStorageLocationListDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ImportStorageLocationList record);

    int insertSelective(ImportStorageLocationList record);

    ImportStorageLocationList selectByPrimaryKey(Integer id);
    
    List<ImportStorageLocationList> selectAll();
    
    ImportStorageLocationList selectByLocation(String location);
    
    List<ImportStorageLocationList> selectSourceNumber(String location);

    int updateByPrimaryKeySelective(ImportStorageLocationList record);

    int updateByPrimaryKey(ImportStorageLocationList record);
    
    public List<ImportStorageLocationList> listPage(PageModel<ImportStorageLocationList> page);
    
    public List<ImportStorageLocationList> getLocationByInstructionsIdPage(PageModel<ImportStorageLocationList> page);
}