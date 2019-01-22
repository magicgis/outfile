package com.naswork.dao;

import java.util.List;

import com.naswork.model.CheckStorageByLocation;
import com.naswork.model.ImportPackageElement;
import com.naswork.vo.PageModel;

public interface CheckStorageByLocationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CheckStorageByLocation record);

    int insertSelective(CheckStorageByLocation record);

    CheckStorageByLocation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CheckStorageByLocation record);

    int updateByPrimaryKey(CheckStorageByLocation record);
    
    public List<CheckStorageByLocation> selectByLocation(String location);
    
    public List<ImportPackageElement> listPage(PageModel<ImportPackageElement> page);
    
    public List<CheckStorageByLocation> selectByImportPackageElementId(Integer importPackageElementId);
} 