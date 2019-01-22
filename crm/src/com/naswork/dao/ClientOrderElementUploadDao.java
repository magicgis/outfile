package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementUpload;
import com.naswork.vo.PageModel;

public interface ClientOrderElementUploadDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientOrderElementUpload record);

    int insertSelective(ClientOrderElement record);

    ClientOrderElementUpload selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientOrderElementUpload record);

    int updateByPrimaryKey(ClientOrderElementUpload record);
    
    public List<ClientOrderElementUpload> listPage(PageModel<ClientOrderElementUpload> page);
    
    public void deleteMessage(Integer userId);
    
}