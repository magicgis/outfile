package com.naswork.service;

import java.util.List;

import com.naswork.model.VasStroge;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface VasStrogeService {
	
	void insertSelective(VasStroge record);

    VasStroge selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(VasStroge record);
    
    public void getFileInformationPage(PageModel<VasStroge> page,String where,GridSort sort);
    
    public List<VasStroge> findByIds(String[] ids);
    
    public List<String> getShortPart(Integer id);

}
