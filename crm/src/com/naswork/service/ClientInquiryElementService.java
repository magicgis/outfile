package com.naswork.service;

import java.util.HashMap;
import java.util.List;

import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ImportPackageElement;
import com.naswork.module.marketing.controller.clientinquiry.ClientDownLoadVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.partsinformation.PartsInformationVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

public interface ClientInquiryElementService {

	ResultVo insertSelective(List<ClientInquiryElement> list,Integer clientInquiryId);
	
	public ResultVo insertForCopyTable(List<ClientInquiryElement> list,Integer clientInquiryId);
	
	void insertByimportPackage(ClientInquiryElement clientInquiryElement);
	
	int updateByPrimaryKeySelective(ClientInquiryElement record);
	
	void update(ClientInquiryElement record);
	
	ClientInquiryElement selectByPrimaryKey(Integer id);
	
	public List<ClientInquiryElement> findByclientInquiryId(Integer id); 
	
	/*
     * 获取下载模板数据
     */
    public List<ElementVo> getEle(Integer clientInuqiryId);
    
    public List<ClientDownLoadVo> getPricesByElementVo(ElementVo elementVo);
    
    public ExchangeRate getValue(Integer currencyId);
    
    public Integer findMaxItem(Integer id);
    
    /*
     * 销售部件资料
     */
    public void marketpartPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort);
    
    /*
     * 采购部件资料
     */
    public void purchasepartPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort);
    
    /*
     * 采购部件资料带件号模糊查询
     */
    public void purchasepartPageWithPart(PageModel<PartsInformationVo> page, String where,
			GridSort sort);
    
    /*
     * 供应商能力清单
     */
    public void supplierAbilityPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort);
    
    public String getCodeFromPartNumber(String partNumber);
    
    public List<ElementVo> getSupplierCode(Integer clientInuqiryId);
    
    public List<ClientInquiryElement> findCieBySi(Integer supplierInquiryId);
    
    public List<PartsInformationVo> marketPartInformation(PageModel<PartsInformationVo> page);
    
    public List<PartsInformationVo> purchasePartInformation(PageModel<PartsInformationVo> page);
    
    public List<ClientDownLoadVo> getPricesByPartVo(PartsInformationVo partsInformationVo);
    
    public List<String> selectByMainId(Integer mainId);
    
    public void marketpartAllNullPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort);
    
    public void marketpartCoeNullPage(PageModel<PartsInformationVo> page, String where,
			GridSort sort);
    
    public boolean updateBsn(String[] bsns,String[] clientInquiryElementIds);
    
    public void selectByBsnPartPage(PageModel<PartsInformationVo> page);
    
    public void addStaticElement(ClientInquiry clientInquiry,ClientQuote clientQuote);
    
    public void searchSatair(List<ClientInquiryElement> list,Integer clientInquiryId,Integer supplier,Integer commitCrawl,HashMap<String, Boolean> crawlSupplier);
    
    public void getEmail(List<ClientInquiryElement> list,Integer clientInquiryId);
    
    public void matchElement(List<ClientInquiryElement> list);
    
    public ClientInquiryElement selectByEpeId(Integer exportPackageElementId);
    
    public String getShortPartNumber(String partNumber);
    
    public List<ClientInquiryElement> getLotsInquiryElements(PageModel<String> page);
    
    public List<ClientInquiryElement> getList();
    
    public List<String> getCageCode(String shortCode);
    
    public void updateCageCode(String cageCode,String sn);
    
    public void elementPage(PageModel<ClientInquiryElement> page,String where,GridSort sort);
    
    public void elementPageForMarketHis(PageModel<ClientInquiryElement> page,String where,GridSort sort);
    
    public void getDataOnce(PageModel<ClientInquiryElement> page,String where,GridSort sort);
    
    public boolean updateStatus(PageModel<ClientInquiryElement> page);
    
    public void sendByCrawEmail();
    
    public Double getTotalPrice(Integer id);
    
    public int getCountByTime(String time);
    
    public List<ClientInquiryElement> getDateBySelect(String ids);
    
    public boolean QQEmail(List<ClientInquiryElement> list,ClientInquiry clientInquiry,UserVo user);
}
