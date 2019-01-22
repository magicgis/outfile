package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementNotmatch;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.vo.PageModel;

public interface ClientOrderElementNotmatchDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientOrderElement record);
    
    int add(ClientWeatherOrderElement clientWeatherOrderElement);

    int insertSelective(ClientOrderElementNotmatch record);

    ClientOrderElementNotmatch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientOrderElementNotmatch record);

    int updateByPrimaryKey(ClientOrderElementNotmatch record);
    
    void deleteByUserId(Integer userId);
    
    List<ClientOrderElement> selectByUserId(Integer userId);
    
    public List<ClientOrderElementNotmatch> listpage(PageModel<ClientOrderElementNotmatch> page);
}