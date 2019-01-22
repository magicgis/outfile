package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementNotmatch;
import com.naswork.model.ClientOrderElementUpload;
import com.naswork.vo.PageModel;

public interface ClientOrderElementNotmatchService {
	int deleteByPrimaryKey(Integer id);

    int insert(ClientOrderElement record);

    int insertSelective(ClientOrderElementNotmatch record);

    ClientOrderElementNotmatch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientOrderElementNotmatch record);

    int updateByPrimaryKey(ClientOrderElementNotmatch record);
    
    void deleteByUserId(Integer userId);
    
    List<ClientOrderElement> selectByUserId(Integer userId);
    
    public void listpage(PageModel<ClientOrderElementNotmatch> page);
}
