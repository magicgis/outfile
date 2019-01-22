package com.naswork.dao;

import java.util.List;

import com.naswork.model.ClientInquiryElement;
import com.naswork.module.marketing.controller.clientinquiry.ClientDownLoadVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.partsinformation.PartsInformationVo;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.vo.PageModel;

public interface ClientInquiryElementDao {
    int deleteByPrimaryKey(Integer id);

    void insert(ClientInquiryElement record);

    int insertSelective(ClientInquiryElement record);

    ClientInquiryElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientInquiryElement record);

    int updateByPrimaryKey(ClientInquiryElement record);
    
    public List<ClientInquiryElement> findByclientInquiryId(Integer id);
    
    public Integer findMaxItem(Integer id);
    
    public Integer getId(ClientInquiryElement clientInquiryElement);
    
    public List<ElementVo> getEle(Integer clientInuqiryId);
    
    List<ElementVo> findElementByCieId(Integer clientInuqiryElementId);
    
    List<ElementVo> findSupplierCodeByCieId(Integer clientInuqiryElementId);
    
    public List<ClientDownLoadVo> getPricesByElementVo(ElementVo elementVo);
    
    public List<PartsInformationVo> marketpartPage(PageModel<PartsInformationVo> page);
    
    public List<PartsInformationVo> purchasepartPage(PageModel<PartsInformationVo> page);
    
    public List<PartsInformationVo> purchasepartWithPartPage(PageModel<PartsInformationVo> page);
    
    public List<PartsInformationVo> purchasepart(PageModel<PartsInformationVo> page);
    
    public List<PartsInformationVo> purchasepartWithPart(PageModel<PartsInformationVo> page);
    
    public List<PartsInformationVo> supplierAbilityPage(PageModel<PartsInformationVo> page);
    
    public List<ElementVo> getSupplierCode(Integer clientInuqiryId);
    
    public List<ClientInquiryElement> getTypeCode(PageModel<ClientInquiryElement> page);
    
    public List<ClientInquiryElement> findByTypeCode(ClientInquiryElement clientInquiryElement);
    
    public List<ClientInquiryElement> findCieBySi(Integer supplierInquiryId);
    
    public List<PartsInformationVo> marketPartInformation(PageModel<PartsInformationVo> page);
    
    public List<PartsInformationVo> purchasePartInformation(PageModel<PartsInformationVo> page);
    
    public List<ClientDownLoadVo> getPricesByPartVo(PartsInformationVo partsInformationVo);
    
    public List<String> selectByMainId(Integer mainId);
    
    public ClientInquiryElement selectBySupplierInuqiryElementId(Integer supplierInquiryElementId);
    
    public List<ClientInquiryElement> selectByCsn(Integer csn,Integer clientInquiryId);
    
    public List<ClientInquiryElement> selectBySupplierInquiryId(Integer supplierInquiryId);
    
    public List<PartsInformationVo> marketpartAllNullPage(PageModel<PartsInformationVo> page);
    
    public List<PartsInformationVo> marketpartCoeNullPage(PageModel<PartsInformationVo> page);
    
    public List<ClientInquiryElement> selectBySupplierInquiryIdWhenAuto(Integer supplierInquiryId);
    
    public ClientInquiryElement selectStorageByElementId(String elementId,String sourceNumber);
    
    public List<PartsInformationVo> selectByBsnPartPage(PageModel<PartsInformationVo> page);
    
    public List<ClientInquiryElement> getCommissionListByClientInquiryId(Integer clientInquiryId,Integer supplierId);
    
    public ClientInquiryElement selectByEpeId(Integer exportPackageElementId);
    
    public List<ClientInquiryElement> selectByClientOrderId(Integer clientOrderId);
    
    public List<ClientInquiryElement> selectBySupplierOrderId(Integer supplierOrderId);
    
    public List<ClientInquiryElement> getLotsInquiryElements(PageModel<String> page);
    
    public Integer getMaxItem(Integer clientInquiryId);
    
    public List<ClientInquiryElement> getList();
    
    public List<String> getCageCode(String shortCode);
    
    public void updateCageCode(String cageCode,String sn);
    
    ClientInquiryElement  findByElementAndMian(Integer elemenetId,Integer clientInquiryId);
    
    public List<ClientInquiryElement> elementPage(PageModel<ClientInquiryElement> page);
    
    public List<ClientInquiryElement> elementPageForMarketHis(PageModel<ClientInquiryElement> page);
    
    public List<ClientInquiryElement> getDataOnce(PageModel<ClientInquiryElement> page);
    
    public List<ClientInquiryElement> getElementList(PageModel<ClientInquiryElement> page);
    
    public List<ClientInquiryElement> findByclientInquiryIdNoBsn(PageModel<FactoryVo> page);
    
    public List<ClientInquiryElement> getListBySupplierAbility(Integer clientInquiryId,Integer supplierId);
    
    public ClientInquiryElement selectByPartNumber(String partNumber,String clientInquiryId);
    
    public List<String> getBsnByShortPart(String shortPart);
    
    public Double getTotalPrice(Integer id);
    
    public List<ClientInquiryElement> getDateBySelect(String ids);
    
    public List<ClientInquiryElement> getListBySelect(String ids,String supplierId);
    
    public Integer getDangerList(ClientInquiryElement clientInquiryElement);
    
    public List<ClientInquiryElement> getByMainId(Integer mainId);
    
    public List<ClientInquiryElement> selectByMainIdAndId(Integer mainId,Integer id);
    
    public List<ClientInquiryElement> getByItem(Integer clientInquiryId,Integer item);
    
    public void updateByPartNumber(String partNumber);
    
}