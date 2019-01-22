package com.naswork.service;

import java.util.Date;
import java.util.List;

import com.naswork.model.ClientInquiry;
import com.naswork.model.PartAndEmail;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierContact;
import com.naswork.model.SupplierInquiry;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageVo;
import com.naswork.module.purchase.controller.supplierinquiry.SupplierVo;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface SupplierInquiryService {

	 /*
     * 获取供应商
     */
    public void findSupplier(PageModel<SupplierVo> page,String where);
	
    /*
     * 获取供应商条数
     */
    public Integer findCount();
    
    /*
     * 获取明细条数
     */
    public Integer findCount2(Integer id);
    
    /*
     * 插入
     */
    public int insertSelective(SupplierInquiry supplierInquiry);
    
    /*
     * 根据客户询价的ID查询
     */
    public SupplierInquiry findClientInquiryElement(Integer id);
    
    /*
     * 获取供应商询价ID
     */
    public Integer findId(SupplierInquiry supplierInquiry);
    
    /*
     * 管理页面数据
     */
    public void listManagePage(PageModel<ManageVo> page,String where,
			GridSort sort);
    
    /*
     * 获取供应商
     */
    public List<SupplierVo> suppliers(PageModel<SupplierVo> page);
    
    /*
     * 供应商管理明细
     */
    public void ManageElementPage(PageModel<ManageElementVo> page);
    
    public List<ManageElementVo> ManageElement(PageModel<ManageElementVo> page);
    
    /*
     * 主键查询
     */
    public ManageVo selectByPrimaryKey(PageModel<ManageVo> page,Integer id);
    
    /*
     * 根据供应商询价Id查询
     */
    public Integer findBySupplierInquiryId(Integer supplierInquiryId);
    
    /*
     * 拼接单号
     */
    public String getQuoteNumberSeq(Date date,Integer supplierId);
    
    public SupplierInquiry selectByPrimaryKey(Integer id);
    
    /*
     * 获取供应商信息
     */
    public Supplier getSupplier(Integer supplierId);
    
    /*
     * 获取供应商代码
     */
    public List<String> getQuoteNumbers(Integer clientInquiryId);
    
    /**
     * 查询供应商
     * */
    SupplierInquiry lookSupplierByImportPackage(ClientInquiry ci,Integer supplierId,String supplierCode);
    
    /*
     * 供应商
     */
    public List<Supplier> getSupplier(PageModel<Supplier> page);
    
    public List<ManageElementVo> Elements(Integer supplierInquiryId);
    
    public String getEleCount(Integer id);
    
    int updateByPrimaryKeySelective(SupplierInquiry record);
    
    public int getInquiryList(PageModel<FactoryVo> page,Integer clientInquiryId);
    
    
    List<SupplierInquiryStatistic> supplierInquiryStat(SupplierInquiryStatistic supplierInquiryStatistic);
    
    public void inquiryPage(PageModel<ClientInquiryVo> page, String where,
			GridSort sort);
    
    public Integer getQuoteCount(Integer supplierInquiryId);
    
    public void findByClientQuoteNumberPage(PageModel<SupplierInquiry> page,String where,GridSort sort);
    
    public void sendEmail(SupplierInquiry supplierInquiry,String userId,String firstText,String secondText,List<SupplierContact> list,Integer ifAuto);
    
    public void sendEmail(SupplierInquiry supplierInquiry,String userId,String firstText,String secondText);
    
    public List<SupplierContact> getContacts(String[] suppliers);
    
    public List<ManageElementVo> ManageElementWhenAuto(Integer supplierInquiryId);
    
    public boolean sendEmail(String elementIds,Integer id);
    
    public void getPrepareEmailList(PageModel<PartAndEmail> page,String where,GridSort sort);
    
    public void cancelRecord(PartAndEmail partAndEmail);
    
}

