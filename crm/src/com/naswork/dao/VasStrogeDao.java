package com.naswork.dao;

import java.util.List;

import com.naswork.model.VasStroge;
import com.naswork.vo.PageModel;

public interface VasStrogeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VasStroge record);

    int insertSelective(VasStroge record);

    VasStroge selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VasStroge record);

    int updateByPrimaryKey(VasStroge record);
    
    public List<VasStroge> getFileInformationPage(PageModel<VasStroge> page);
    
    public List<VasStroge> findByIds(String ids);
    
    public List<String> getShortPart(Integer id);
}