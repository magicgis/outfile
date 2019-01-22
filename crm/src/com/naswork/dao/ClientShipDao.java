package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientShip;
import com.naswork.module.storage.controller.clientship.ClientShipVo;
import com.naswork.vo.PageModel;

public interface ClientShipDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientShip record);

    int insertSelective(ClientShip record);

    ClientShip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientShip record);

    int updateByPrimaryKey(ClientShip record);
    
    public List<ClientShipVo> listPage(PageModel<ClientShipVo> page);
    
    public ClientShipVo findById(Integer id);
    
    public String getTemplet(String clientId);
    
}