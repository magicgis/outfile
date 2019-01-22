package com.naswork.service;

import com.naswork.model.ClientProfitmargin;
import com.naswork.model.SystemCode;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientProfitmarginService {
	  int deleteByPrimaryKey(Integer id);

	    int insert(ClientProfitmargin record);

	    int insertSelective(ClientProfitmargin record);

	    ClientProfitmargin selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(ClientProfitmargin record);

	    int updateByPrimaryKey(ClientProfitmargin record);
	    
	    ClientProfitmargin selectByClientId(Integer clientId);
	    
	    public void listPage(PageModel<ClientProfitmargin> page, String where,
				GridSort sort);
}
