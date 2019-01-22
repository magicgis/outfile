package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientInquiryElementUpload;
import com.naswork.vo.PageModel;

public interface ClientInquiryElementUploadDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientInquiryElementUpload record);

    int insertSelective(ClientInquiryElement record);

    ClientInquiryElementUpload selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientInquiryElementUpload record);

    int updateByPrimaryKey(ClientInquiryElementUpload record);
    
    public List<ClientInquiryElementUpload> getErrorPage(PageModel<ClientInquiryElementUpload> page);
    
    public void deleteError(Integer userId);
}