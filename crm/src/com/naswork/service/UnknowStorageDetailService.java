package com.naswork.service;

import java.util.List;

import com.naswork.model.UnknowStorageDetail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface UnknowStorageDetailService {

	public void insertSelective(UnknowStorageDetail record);

    UnknowStorageDetail selectByPrimaryKey(Integer id);

    public void updateByPrimaryKeySelective(UnknowStorageDetail record);
    
    public void listPage(PageModel<UnknowStorageDetail> page,String where,GridSort sort);
	
}
