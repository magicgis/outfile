package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientShip;
import com.naswork.module.storage.controller.clientship.ClientShipVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientShipService {

    int insertSelective(ClientShip record);

    ClientShip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientShip record);
    
    public void listPage(PageModel<ClientShipVo> page,String where,GridSort sort);
    
    public ClientShipVo findById(Integer id);
    
    public String getTemplet(String clientId);
	
}
