package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientReceipt;
import com.naswork.module.finance.controller.clientreceipt.ClientReceiptVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientReceiptService {

    int insertSelective(ClientReceipt record);

    ClientReceipt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientReceipt record);
    
    public void listPage(PageModel<ClientReceiptVo> page,String where,GridSort sort);
    
    public ClientReceiptVo findById(Integer id);
	
}
