package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.vo.PageModel;

public interface ClientWeatherOrderElementDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientWeatherOrderElement record);

    int insertSelective(ClientWeatherOrderElement record);

    ClientWeatherOrderElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientWeatherOrderElement record);

    int updateByPrimaryKey(ClientWeatherOrderElement record);
    
    public List<ClientOrderElementVo> findByOrderIdPage(PageModel<ClientOrderElementVo> page);
    
    public List<ClientOrderElementVo> findByOrderId(Integer id);
    
    public List<ClientOrderElementVo> elementList(Integer id);
    
	public List<ClientOrderElementVo> findItem(Integer id);
	
	List<ClientWeatherOrderElement> selectByForeignKey(Integer clientOrderId);
	
	Double sumPrice(Integer clientWeatherOrderId);
	
	List<Integer>  findUser(Integer clientOrderElementId);
	
	ClientOrderElementVo findByclientOrderELementId(Integer id);
	
	Integer findByOrderNumberAndItem(String orderNumber,String item);
	
	public List<ClientOrderElementVo> findOrderElementFinal(ClientQuote clientQuote);
	
	public List<ClientOrderElementVo> getElementByIds(PageModel<String> page);
	
	public List<ClientWeatherOrderElement> getRealPrice(Integer clientOrderElementId);
	
	public List<String> getExecutionId(Integer id);
	
	public void unableTask(String executionId);
	
	public void unableSpzt(Integer id);
	
	public List<ClientWeatherOrderElement> getElementIdsById(Integer id);
	
	public List<Integer> getTaskDbid(String executionId);
	
	public void deleteParticipation(String ids);
	
	public Double getTotalPrice(Integer clientWeatherOrderId);
	
	public List<String> getExecutionIdByElementId(Integer id);
	
	public void unableSpztByElementId(Integer id);
    
}