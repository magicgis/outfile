package com.naswork.dao;

import java.util.Date;
import java.util.List;

import com.naswork.model.ClientInquiry;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.ImportPackagePaymentElementPrepare;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.importpackage.SupplierImportElementVo;
import com.naswork.module.purchase.controller.importpackage.SupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.ClientOrderElementVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ImportPackageElementDao {
    int deleteByPrimaryKey(Integer id);

    void insert(ImportPackageElement record);

    int insertSelective(ImportPackageElement record);

    ImportPackageElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImportPackageElement record);
    
    List<ImportPackageElementVo> findByApprovalStatus(Integer importpackageid,Integer approvalstatus);

    void updateByPrimaryKey(ImportPackageElement record);
    
    int findSupplierBySupplierOrderNumber(String orderNumber);

    List<ImportPackageElement> findCertStatus(Integer importpackageId);
    
    List<ImportPackageElementVo> findElementListDatePage(PageModel<ImportPackageElementVo> page);
    
    List<SupplierOrderElementVo> findsupplierorderDatePage(PageModel<SupplierOrderElementVo> page);
    
    Double findsupplierorderImportAmount(Integer supplierOrderELmentId);
    
    SupplierOrderElementVo findByclientOrderElementId(Integer clientOrderElementId);

    void splitOrder(ClientOrderElementVo clientOrderElementVo );
    
	 List<ImportPackageElementVo> findStorageByCoeId(Integer  id);
    
    List<SupplierOrderElementVo> findsupplierorderDate(Integer supplierOrderElementId);
    
    ClientInquiry findBySourceNumber(String sourceNumber);
    
	int findMAXquoteNumberSeq(Date date);
	
	int findMAXid();
	
	ImportPackageElementVo findimportpackageelement(String importpackageelementid);
	
	SupplierImportElementVo findSupplierimportElement(Integer importpackageelementid);
	
	List<ImportPackageElementVo> findByIpid(Integer ipid);
	
	 List<StorageFlowVo> findstorageFlowPage(PageModel<StorageFlowVo> page);
	 
	 StorageFlowVo findtotalList(PageModel<StorageFlowVo> page);
	
	public List<StorageDetailVo> StoragePage(PageModel<StorageDetailVo> page);
	
	public List<StorageDetailVo> getHasLifeStorage();
	
	List<StorageDetailVo> ecportpackageInstructionsData(StorageFlowVo vo);
	
	StorageDetailVo BoxWeight(ImportPackageElement parame);
	
	List<ImportPackageElementVo> findByLocationIm(String locatin);
	
	List<ImportPackageElementVo> findByLocationEx(String locatin);
	
	List<ImportPackageElementVo> findByClientId(String clientId);
	 
	 List<ImportPackageElementVo> findByClientIdAndOrdernum(String clientId,String orderNumber);
	 
	 List<ImportPackageElementVo>  findByLocationAndStatus(String locatin);
	 
	 int findexportpackage(Integer importpackageelementid);
	 
	 public StorageDetailVo getCountAndPrice();
	 
	 List<ImportPackageElementVo> findImportpackageData(ImportPackageElementVo imortpackagedate);
	 
	 int findStock(ImportPackageElementVo imortpackagedate);
	 
	 public Double getAmountBySupplierOrderId(Integer supplierOrderId);
	 
	 public List<StorageDetailVo> getStorageAmountByQuote(PageModel<StorageDetailVo> page);
	 
	 public List<SupplierOrderElementVo> selectELementBySoe(Integer supplierOrderElementId);
	 
	 public Double selectImportBySoeIdAndCoeId(Integer supplierOrderElementId,Integer clientOrderElementId);
	 
	 /**
	  * 根据出库指令查询
	  * @param exportPackageId
	  * @return
	  */
	 public List<ImportPackageElement> selectByExportPackageId(PageModel<ExportPackageElementVo> page);
	 
	 /**
	  * 根据userId查询多出的件
	  * @param userId
	  * @return
	  */
	 public List<ImportPackageElement> selectByUserId(Integer userId);
	 
	 ImportPackageElementVo selectStorageByImportPackageElementId(String importPacgageElementId);
	 /**
	  * 根据订单明细id查询总入库量
	  * @param supplierOrderElementId
	  * @return
	  */
	 public Double getTotalAmountByOrderELementId(Integer supplierOrderElementId);
	 
	 Integer findImportPackageSign(Integer supplierOrderELementId);
	 
	 List<ImportPackageElement> findSign(Integer supplierOrderELementId);
	 
	 List<ImportPackageElement> findAbnormalPart(Integer importPackageId);
	 
	 List<ImportPackageElement> tasklistPage(PageModel<ImportPackageElement> page);
	 
	 List<ImportPackageElementVo> findByLocation();
	 
	 List<ImportPackageElementVo> getLocationInUse();
	 
	 StorageFlowVo findSupplierIdByIpeId(Integer id);
	 
	 List<ImportPackageElementVo> findStorageByElementId(Integer cieElementId,Integer sqeElementId);
	 
	 List<StorageFlowVo> findStorageBySupplierQuoteElementId(Integer supplierQuoteElementId);
	 
	 StorageFlowVo findStorageBySoeIdAndIpeId(Integer supplierQuoteElementId,Integer importPackageElementId);
	 
	 public int getImportCountByCoeId(Integer clientOrderElementId);
	 
	 public List<ImportPackagePaymentElementPrepare> getPaymentList(Integer importPackageId);
	 
	 public List<Integer> getIdByLocation(String loaction);
	 
	 public List<StorageDetailVo> getUnchangeLocationPage(PageModel<StorageDetailVo> page);
	 
	Integer findSupplierQuoteElementId(Integer importPackageElementId);
		
	StorageFlowVo findImportPackageElementId(Integer supplierQuoteElementId);
	
	public List<ImportPackageElement> exportLotsExcelElement(PageModel<String> page);
	
	public List<ImportPackageElement> selectByLocation(String location);
	
	public List<ImportPackageElement> selectByLocationId(Integer locationId);
	
	public List<ImportPackageElement> getByInstructionsId(Integer id);
	
	public List<Integer> getByLocation(String location,String id);
	
	public List<StorageDetailVo> getNotInInstructionsPage(PageModel<StorageDetailVo> page);
	
	public List<ImportPackageElement> getBySoeId(Integer supplierOrderElementId);
	
	public Double getLockStorageAmount(Integer supplierQuoteElementId,Integer importPackageElementId);
	
	public List<StorageFlowVo> getCorrelationList(PageModel<StorageDetailVo> page);
	
	public List<ImportPackageElement> getListHasLife();
	
	public List<StorageDetailVo> getStorageWithTerm(PageModel<StorageDetailVo> page);
	
	public ImportPackageElement getImportPackageElementByLocationAndCoeId(PageModel<ImportPackageElement> page);
	
	public List<ImportPackageElementVo> getRestLocation();
	
}