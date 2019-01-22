package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.finance.controller.clientinvoice.ListDataVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;


public interface ClientInvoiceService {
    void deleteByPrimaryKey(Integer id);

    void insert(ClientInvoice record);
    
    void autoinsert(ClientInvoice record) throws Exception;

    void insertSelective(ClientInvoice record);

    ClientInvoice selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(ClientInvoice record);

    void updateByPrimaryKey(ClientInvoice record);
    
    List<ClientInvoice> selectByclientOrderElementId(Integer id);
    
    public ClientInvoiceExcelVo getMessage(Integer invoiceId);
    
    public List<ClientInvoiceExcelVo> getEleMessage(Integer invoiceId);
    

    void listDataPage(PageModel<ListDataVo> page, String searchString, GridSort sort);
    
    void findByOrderNumber(PageModel<ListDataVo> page, String searchString, GridSort sort);
    
    ListDataVo findById(String id);
    
    ClientInvoice selectByCode(String invoiceNumber);
    
    List<ClientInvoice> selectByclientOrderId(Integer clientOrderId,Integer type);
    
    List<ListDataVo>  findExportNumber(Integer clientInvoiceId);
    
    List<ClientInvoice> findByexportMunber(ListDataVo vo );
    
    public ClientInvoiceElement getTotalByCoId(Integer clientOrderId);
    
    public Integer getInvoiceIdByCoId(Integer clientOrderId);

}
