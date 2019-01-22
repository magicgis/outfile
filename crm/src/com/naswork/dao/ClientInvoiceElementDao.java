package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientInvoiceElement;

import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.finance.controller.clientinvoice.ElementDataVo;
import com.naswork.module.finance.controller.clientinvoice.ListDataVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientInvoiceElementDao {
    void deleteByPrimaryKey(Integer id);

    void insert(ClientInvoiceElement record);

    void insertSelective(ClientInvoiceElement record);

    ClientInvoiceElement selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(ClientInvoiceElement record);

    void updateByPrimaryKey(ClientInvoiceElement record);
    
    void updateByClientOrderElementId(ClientInvoiceElement record);
    
    List<ElementDataVo> elementDataPage(PageModel<ElementDataVo> page);
    
    List<ElementDataVo> findByCoidAndCiid(String clientOrderId,String clientInvoiceId);
    
    ClientInvoiceElement selectByCoeIdAndCiId(ClientInvoiceElement clientInvoiceElement);
    
    public List<ClientInvoiceExcelVo> getEleMessage(Integer invoiceId);
    
    List<ElementDataVo> findByCoid(String clientOrderId);
    
    public ClientInvoiceElement getTotalByCoId(Integer clientOrderId);
    
    public ClientInvoiceElement selectByOrderId(Integer clientOrderElementId);

}