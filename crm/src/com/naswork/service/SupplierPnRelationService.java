package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.SupplierPnRelationKey;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.module.marketing.controller.partsinformation.SupplierAbilityVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierPnRelationService {
	void selectByPrimaryKeyPage(PageModel<SupplierPnRelationKey> page);
	
	List<SupplierPnRelationKey> selectByNameAndNum(String partName);
	
	  void deleteByPrimaryKey(SupplierPnRelationKey key);

	    void insert(SupplierPnRelationKey record);
	    
	    SupplierPnRelationKey  selectBybsn(String bsn,String supplierId);

	    void insertSelective(SupplierPnRelationKey record);
	    
	/*
	 * 根据bsn获取supplierId    
	 */
	public List<Integer> selectByBsn(String bsn);
	
	public void listPage(PageModel<SupplierAbilityVo> page, String where,
			GridSort sort);
	
	 /*
     * excel上传
     */
    public MessageVo excelUpload(MultipartFile multipartFile,Integer id,String supplierPnType);
    
    void updateByPrimaryKey(SupplierPnRelationKey record);
    
    void updateByPrimarySelectiveKey(SupplierPnRelationKey record);
    
    public boolean saveMatch(String[] bsn,String[] ids);
}
