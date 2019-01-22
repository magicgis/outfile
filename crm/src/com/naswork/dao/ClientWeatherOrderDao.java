package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientOrder;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.vo.PageModel;

public interface ClientWeatherOrderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientWeatherOrder record);

    int insertSelective(ClientWeatherOrder record);

    ClientWeatherOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientWeatherOrder record);

    int updateByPrimaryKey(ClientWeatherOrder record);
    
    public List<ClientOrderVo> listPage(PageModel<ClientOrderVo> page);
    
    public ClientOrderVo findById(Integer id);
    
    public ClientWeatherOrder getClientOrder(Integer clientOrderElementId);
    
    Integer findseq(int clientquoteid);
}