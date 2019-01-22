package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientInvoice;

import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;

import com.naswork.module.finance.controller.clientinvoice.ListDataVo;
import com.naswork.vo.PageModel;


public interface ClientInvoiceDao {
    void deleteByPrimaryKey(Integer id);
    
    void autoinsert(ClientInvoice record);

    void insert(ClientInvoice record);

    void insertSelective(ClientInvoice record);

    ClientInvoice selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(ClientInvoice record);

    void updateByPrimaryKey(ClientInvoice record);
    
    List<ClientInvoice> selectByclientOrderElementId(Integer id);
    
    public ClientInvoiceExcelVo getMessage(Integer invoiceId);

    List<ListDataVo> listDataPage(PageModel<ListDataVo> page);
    
    List<ListDataVo> findByOrderNumberPage(PageModel<ListDataVo> page);
    
    ListDataVo findById(String id);
    
    ClientInvoice selectByCode(String invoiceNumber);
    
    List<ClientInvoice> selectByclientOrderId(Integer clientOrderId,Integer type);
    
    public List<ClientInvoice> selectByclientOrderIdAndShipId(Integer clientOrderId,Integer type,Integer shipId);
    
    public int getInvoiceCount(Integer clientOrderId);
    
    public Double getFinishedTerm(Integer clientOrderId);
    
    List<ListDataVo>  findExportNumber(Integer clientInvoiceId);
    
    List<ClientInvoice> findByexportMunber(ListDataVo vo );
    
    public Integer getInvoiceIdByCoId(Integer clientOrderId);
}