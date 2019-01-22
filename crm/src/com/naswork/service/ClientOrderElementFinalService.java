package com.naswork.service;

import com.naswork.model.ClientOrderElementFinal;

public interface ClientOrderElementFinalService {
	 int deleteByPrimaryKey(Integer id);

	    int insert(ClientOrderElementFinal record);

	    int insertSelective(ClientOrderElementFinal record);

	    ClientOrderElementFinal selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(ClientOrderElementFinal record);

	    int updateByPrimaryKey(ClientOrderElementFinal record);
	    
	    void updateStatus(ClientOrderElementFinal record);
}
