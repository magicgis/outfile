package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientQuoteElementUpload;
import com.naswork.vo.PageModel;

public interface ClientQuoteElementUploadService {
	void deleteByPrimaryKey(Integer id);

    void insert(ClientQuoteElementUpload record);

    void insertSelective(ClientQuoteElementUpload record);

    ClientQuoteElementUpload selectByPrimaryKey(Integer id);

    void updateByPrimaryKeySelective(ClientQuoteElementUpload record);

    void updateByPrimaryKey(ClientQuoteElementUpload record);
    
    void selectByUserId(PageModel<ClientQuoteElementUpload> page);
}
