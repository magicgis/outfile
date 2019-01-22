package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientInvoiceElement;
import com.naswork.module.finance.controller.clientinvoice.ElementDataVo;
import com.naswork.module.finance.controller.clientinvoice.ListDataVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientInvoiceElementService {
	void deleteByPrimaryKey(Integer id);

    void insert(ClientInvoiceElement record);

    void insertSelective(ClientInvoiceElement record);

    ClientInvoiceElement selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(ClientInvoiceElement record);

    void updateByPrimaryKey(ClientInvoiceElement record);
    
    void updateByClientOrderElementId(ClientInvoiceElement record);
    
    void elementDataPage(PageModel<ElementDataVo> page, String searchString, GridSort sort);
    
    List<ElementDataVo> findByCoidAndCiid(String clientOrderId,String clientInvoiceId);
    
    ClientInvoiceElement selectByCoeIdAndCiId(ClientInvoiceElement clientInvoiceElement);
    
    List<ElementDataVo> findByCoid(String clientOrderId);
}
