package com.naswork.dao;

import java.util.List;
import java.util.Map;

import com.naswork.model.OnpostEmail;
import com.naswork.vo.PageModel;

public interface OnpostEmailDao {
    int deleteByPrimaryKey(Integer postId);

    int insert(OnpostEmail record);

    int insertSelective(OnpostEmail record);

    OnpostEmail selectByPrimaryKey(Integer postId);
    
    List<Map<String , Object>> checkonpost();

    int updateById(PageModel<String> page);

    int updateByPrimaryKey(OnpostEmail record);
}