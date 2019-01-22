package com.naswork.dao;

import java.util.List;

import org.jbpm.pvm.internal.query.Page;

import com.naswork.model.ClientReceipt;
import com.naswork.module.finance.controller.clientreceipt.ClientReceiptVo;
import com.naswork.vo.PageModel;

public interface ClientReceiptDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientReceipt record);

    int insertSelective(ClientReceipt record);

    ClientReceipt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientReceipt record);

    int updateByPrimaryKey(ClientReceipt record);
    
    public List<ClientReceiptVo> listPage(PageModel<ClientReceiptVo> page);
    
    public ClientReceiptVo findById(Integer id);
}