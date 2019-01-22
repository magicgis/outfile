package com.naswork.service;

import com.naswork.model.QuoteBankCharges;
import com.naswork.model.SystemCode;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface QuoteBankChargesService {
	   int insert(QuoteBankCharges record);

	    int insertSelective(QuoteBankCharges record);
	    
		public void quoteBankChargesPage(PageModel<QuoteBankCharges> page, String where,
				GridSort sort);
		
		QuoteBankCharges findByClientId(Integer clientId);
		
		 void updateByPrimaryKey(QuoteBankCharges record);
}
