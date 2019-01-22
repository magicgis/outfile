package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientWeatherOrderElementBackUp;
import com.naswork.vo.PageModel;

public interface ClientWeatherOrderElementBackUpDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientWeatherOrderElementBackUp record);

    int insertSelective(ClientWeatherOrderElementBackUp record);

    ClientWeatherOrderElementBackUp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientWeatherOrderElementBackUp record);

    int updateByPrimaryKey(ClientWeatherOrderElementBackUp record);
    
    public List<ClientWeatherOrderElementBackUp> getErrorListPage(PageModel<ClientWeatherOrderElementBackUp> page);
    
    public List<ClientWeatherOrderElementBackUp> checkErrorRecord(Integer id);
    
    public void deleteMessage(Integer id);
    
    public List<ClientWeatherOrderElementBackUp> selectByOrderId(Integer id);
}