package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.ClientWeatherOrderElementBackUp;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientWeatherOrderElementService {
	int deleteByPrimaryKey(Integer id);

    int insert(ClientWeatherOrderElement record);

    int insertSelective(ClientWeatherOrderElement record);

    ClientWeatherOrderElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientWeatherOrderElement record);

    int updateByPrimaryKey(ClientWeatherOrderElement record);
    
    public void findByOrderIdPage(PageModel<ClientOrderElementVo> page,GridSort sort);
    
	public List<ClientOrderElementVo> findByOrderId(Integer id);
	
	public List<ClientOrderElementVo> elementList(Integer id);
	
	public void insertSelective(List<ClientOrderElement> list,Integer clientOrderId,String userId);
	
	public MessageVo UploadExcel(MultipartFile multipartFile,Integer clientOrderId,Integer userId);
	
	List<ClientWeatherOrderElement> selectByForeignKey(Integer clientOrderId);
	
	Double sumPrice(Integer clientWeatherOrderId);
	
	List<Integer>  findUser(Integer clientOrderElementId);
	 
	ClientOrderElementVo findByclientOrderELementId(Integer id);
	
	public MessageVo wetherorderuploadExcel(MultipartFile multipartFile,Integer clientOrderId,Integer userId);
	
	public MessageVo finalUploadExcel(MultipartFile multipartFile,Integer clientOrderId,Integer userId);
	
	public MessageVo uploadCancellationOrder(MultipartFile multipartFile,Integer clientOrderId,Integer userId);
	
	public MessageVo addClientOrder(List<ClientOrderElement>  clientOrderElements);
	
	public List<ClientOrderElementVo> getElementByIds(PageModel<String> page);
	
	public List<ClientWeatherOrderElement> getRealPrice(Integer clientOrderElementId);
	
	public void getErroeListPage(PageModel<ClientWeatherOrderElementBackUp> page);
	
	public boolean connectByItem(String[] ids);
	
	public boolean unCommit(String[] ids);
	
	public List<ClientWeatherOrderElementBackUp> checkErrorRecord(Integer id);
	
	public void deleteMessage(Integer userId);
	
	public List<ClientWeatherOrderElementBackUp> selectByOrderId(Integer id);
	
	public Double getTotalPrice(Integer clientWeatherOrderId);
}

