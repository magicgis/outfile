package com.naswork.service;

import java.util.List;

import com.naswork.model.ClientOrder;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.StorageToOrderEmail;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientWeatherOrderService {
	 int deleteByPrimaryKey(Integer id);

	    int insert(ClientWeatherOrder record);

	    int insertSelective(ClientWeatherOrder record);

	    ClientWeatherOrder selectByPrimaryKey(Integer id);

	    void updateByPrimaryKeySelective(ClientWeatherOrder record);

	    int updateByPrimaryKey(ClientWeatherOrder record);
	    
	    public void listPage(PageModel<ClientOrderVo> page, String where,
				GridSort sort);
	    
	    public ClientOrderVo findById(Integer id);
	    
	    public ClientWeatherOrder getClientOrder(Integer clientOrderElementId);
	    
	    Integer findseq(int id);
	    
	    /**
	     * 作废明细
	     * @param id
	     */
	    public void cancelElement(String selectIds);
	    
	    public List<StorageToOrderEmail> getUnfinishListUser();
	    
	    public boolean sendStorageToOrderEmail(List<StorageToOrderEmail> users);
	    
}
