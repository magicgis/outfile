package com.naswork.dao;

import java.util.List;

import com.naswork.model.SupplierInquiry;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageElementVo;
import com.naswork.module.purchase.controller.supplierinquiry.ManageVo;
import com.naswork.module.purchase.controller.supplierinquiry.SaveVo;
import com.naswork.module.purchase.controller.supplierinquiry.SupplierVo;
import com.naswork.module.purchase.controller.supplierinquirystatistic.SupplierInquiryStatistic;
import com.naswork.vo.PageModel;

public interface SupplierInquiryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierInquiry record);

    int insertSelective(SupplierInquiry record);
    
    List<SupplierInquiry> findSupplierInquiry(SupplierInquiry record);

    SupplierInquiry selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierInquiry record);

    int updateByPrimaryKey(SupplierInquiry record);
    
    /*
     * 获取供应商
     */
    public List<SupplierVo> listPage(PageModel<SupplierVo> page);
    
    /*
     * 获取供应商条数
     */
    public Integer findCount();
    
    /*
     * 获取明细条数
     */
    public Integer findCount2(Integer id);
    
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
    public List<ManageVo> listManagePage(PageModel<ManageVo> page);
    
    /*
     * 供应商管理明细
     */
    public List<ManageElementVo> ManageElementPage(PageModel<ManageElementVo> page);
    
    public List<ManageElementVo> ManageElement(Integer supplierInquiryId);
    
    /*
     * 获取客户询价ID
     */
    public Integer findClientInquiryId(Integer id);
    
    /*
     * 获取供应商代码
     */
    public List<String> getQuoteNumbers(Integer clientInquiryId);
    
    /*
     * 查询供应商
     * **/
    SupplierInquiry findSupplierByImportPackage(Integer clientinquiryid,Integer supplierId);
    
    /*
     * 报价比例
     */
    public SaveVo getEleCount(Integer id);
    
    
    List<SupplierInquiryStatistic> supplierInquiryStat(SupplierInquiryStatistic supplierInquiryStatistic);
    
    public List<ClientInquiryVo> inquiryPage(PageModel<ClientInquiryVo> page);
    
    public Integer getQuoteCount(Integer supplierInquiryId);
    
    public List<SupplierInquiry> findByClientQuoteNumberPage(PageModel<SupplierInquiry> page);
    
    public List<ManageElementVo> ManageElementWhenAuto(Integer supplierInquiryId); 
    
    public int getCountByCLientInuqiryId(Integer clientInquiryId);
    
    public List<SupplierInquiry> getByInquiryIdAndSupplierId(Integer clientInquiryId,Integer supplierId);
    
    public Integer getSupplierId(String supplierQuoteElementId);
    
    public List<SupplierInquiry> getInquiry(String supplierId,String clientInquiry,String inquiryDate);
    
    public int getElementCount(String clientInquiryElementId,String supplierInquiryId);
}