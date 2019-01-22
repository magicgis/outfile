package com.naswork.service;

import java.util.List;

import com.naswork.model.IncomeDetail;
import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface IncomeDetailService {
	
	void insertSelective(List<ClientInvoiceExcelVo> list,Integer incomeId,Integer clientInvoiceId);

    IncomeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeDetail record);
    
    public void getByInvoiceIdPage(PageModel<IncomeDetail> page, String where,
			GridSort sort);
    
    public IncomeDetail getTotalByClientOrderElementId(Integer clientOrderElementId);

}
