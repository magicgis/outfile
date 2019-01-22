package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.StorehouseAddress;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface StorehouseAddressService {

    int insertSelective(StorehouseAddress record);

    StorehouseAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StorehouseAddress record);
    
    public void listPage(PageModel<StorehouseAddress> page, String where,
			GridSort sort);
    
    public List<StorehouseAddress> selectByName(String name);
    
    public List<StorehouseAddress> selectAll();
    
    public MessageVo uploadExcel(MultipartFile multipartFile, Integer id);
	
}
