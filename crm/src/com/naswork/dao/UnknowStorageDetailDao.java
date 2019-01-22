package com.naswork.dao;

import java.util.List;

import com.naswork.model.UnknowStorageDetail;
import com.naswork.vo.PageModel;

public interface UnknowStorageDetailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UnknowStorageDetail record);

    int insertSelective(UnknowStorageDetail record);

    UnknowStorageDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnknowStorageDetail record);

    int updateByPrimaryKey(UnknowStorageDetail record);
    
    public List<UnknowStorageDetail> listPage(PageModel<UnknowStorageDetail> page);
}