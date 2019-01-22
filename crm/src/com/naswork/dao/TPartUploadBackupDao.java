package com.naswork.dao;

import java.util.List;

import com.naswork.model.TPart;
import com.naswork.model.TPartUploadBackup;
import com.naswork.vo.PageModel;

public interface TPartUploadBackupDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(TPartUploadBackup record);

    int insertSelective(TPart tPart);

    TPartUploadBackup selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(TPartUploadBackup record);

    int updateByPrimaryKey(TPartUploadBackup record);
    
    public List<TPartUploadBackup> findByUserIdPage(PageModel<TPartUploadBackup> page);
    
    public void delete(Integer userId);
}