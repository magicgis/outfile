package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientQuoteElementUpload;
import com.naswork.module.marketing.controller.clientinquiry.BlackList;
import com.naswork.vo.PageModel;

public interface ClientQuoteElementUploadDao {
	void deleteByPrimaryKey(Integer id);

    void insert(ClientQuoteElementUpload record);

    void insertSelective(ClientQuoteElementUpload record);

    ClientQuoteElementUpload selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(ClientQuoteElementUpload record);

    void updateByPrimaryKey(ClientQuoteElementUpload record);
    
    List<ClientQuoteElementUpload> selectByUserId(PageModel<ClientQuoteElementUpload> page);
}