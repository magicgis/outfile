package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.SupplierAnnualOffer;
import com.naswork.model.SupplierCageRelationKey;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.partsinformation.SupplierAbilityVo;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierCageRelationService {

    int deleteByPrimaryKey(SupplierCageRelationKey key);

    int insert(SupplierCageRelationKey record);

    int insertSelective(SupplierCageRelationKey record);
    
    /**
     * excel上传
     */
    public MessageVo factoryExcel(MultipartFile multipartFile, Integer id);
    
    /**
     * 列表
     */
    public void listPage(PageModel<FactoryVo> page);
    
    /**
     * 单条新增
     */
    public void add(String msn,Integer supplierId);
    
    public void listByAbilityPage(PageModel<SupplierAbilityVo> page, String where,
			GridSort sort);
    
    public List<Integer> selectByMsn(String msn);
    
	
}
