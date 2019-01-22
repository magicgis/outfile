package com.naswork.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.ClientInquiry;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.ImportPackagePaymentElementPrepare;
import com.naswork.model.OrderApproval;
import com.naswork.model.StorageCorrelation;
import com.naswork.model.SupplierImportElement;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
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
import com.naswork.vo.ResultVo;

public interface ImportpackageElementService {

	void findElementListDatePage(PageModel<ImportPackageElementVo> page, String searchString, GridSort sort);
	
	int findSupplierBySupplierOrderNumber(String orderNumber);
	
	List<ImportPackageElementVo> findByIpid(Integer ipid);
	
	void findsupplierorderDatePage(PageModel<SupplierOrderElementVo> page, String searchString, GridSort sort);
	
	Double findsupplierorderImportAmount(Integer supplierOrderELmentId);
	
    SupplierOrderElementVo findByclientOrderElementId(Integer clientOrderElementId);
    
    String splitOrder(StorageFlowVo supplierListVo );
    
//    String splitOrder( List<OrderApproval> storageList,List<OrderApproval> onStorageList );
	
	List<ImportPackageElementVo> findImportpackageData(ImportPackageElementVo imortpackagedate);
	
	int findStock(ImportPackageElementVo imortpackagedate);

	List<SupplierOrderElementVo> findsupplierorderDate(Integer supplierOrderElementId);
	
	ClientInquiry lookBySourceNumber() ;
	
	void Storage(ImportPackageElement importPackageElement,List<ImportPackageListVo> imortpackagedate,SupplierImportElement supplierImportElement) throws Exception;

	void alterstoragea(ImportPackageElementVo importPackageElementVo, ImportPackageElement importPackageElement) throws Exception;
	
	void insert(ImportPackageElement record);
	
	  List<ImportPackageElement> findCertStatus(Integer importpackageId);
	
	void sendReturnGoodsEmail(List<EmailVo> emailVos,String  userId);
	
	void sendAlterstorageaEmail(List<EmailVo> emailVos,String  userId);
	
	int findMAXid();
	
	ImportPackageElementVo findimportpackageelement(String importpackageelementid);
	
	ImportPackageElement selectByPrimaryKey(Integer id);
	
	List<ImportPackageElement> findAbnormalPart(Integer importPackageId);
	
	SupplierImportElementVo findSupplierimportElement(Integer importpackageelementid);
	
	 void updateByPrimaryKey(ImportPackageElement record);
	 
	 void updateByPrimaryKeySelective(ImportPackageElement record);
	 
	 public void StoragePage(PageModel<StorageDetailVo> page,String where,GridSort sort);
	 
	 public void getNotInInstructionsPage(PageModel<StorageDetailVo> page,GridSort sort);
	 
	 List<StorageDetailVo> ecportpackageInstructionsData(StorageFlowVo vo);
	 
	 StorageDetailVo BoxWeight(ImportPackageElement parame);
	 
	 List<ImportPackageElementVo> findByLocationIm(String locatin);
	 
	 void findstorageFlowPage(PageModel<StorageFlowVo> page, String searchString, GridSort sort);
	
	 StorageFlowVo  findtotalList(PageModel<StorageFlowVo> page, String searchString);
	 
	 List<ImportPackageElementVo> findByLocationEx(String locatin);
	 
	 List<ImportPackageElementVo> findByClientIdAndOrdernum(String clientId,String orderNumber);
	 
	 List<ImportPackageElementVo> findByClientId(String clientId);
	 
	 List<ImportPackageElementVo>  findByLocationAndStatus(String locatin);
	 
	 int findexportpackage(Integer importpackageelementid);
	 
	 public StorageDetailVo getCountAndPrice();
	 
	 List<ImportPackageElementVo> findStorageByCoeId(Integer  id);
	 
	 public void checkLastPayment(ImportPackageElement importPackageElement,Integer supplierOrderElementId);
	 
	 public List<StorageDetailVo> getStorageAmountByQuote(PageModel<StorageDetailVo> page);
	 
	 public List<SupplierOrderElementVo> selectELementBySoe(Integer supplierOrderElementId);
	 
	 public Double selectImportBySoeIdAndCoeId(Integer supplierOrderElementId,Integer clientOrderElementId);
	 
	 public SupplierImportElement onPassageImport(ImportPackageElement importPackageElement,SupplierImportElement supplierImportElement);
	 
	 public List<ImportPackageElement> selectByExportPackageId(PageModel<ExportPackageElementVo> page);
	 
	 /**
	  * 根据userId查询多出的件
	  * @param userId
	  * @return
	  */
	 public List<ImportPackageElement> selectByUserId(Integer userId);

	 Integer findImportPackageSign(Integer supplierOrderELementId);
	 
	 List<ImportPackageElement> findSign(Integer supplierOrderELementId);
	 
	 void abnormalPartRequest(List<ImportPackageElement> elementVos,String ids);
	 
	 void tasklistPage(PageModel<ImportPackageElement> page);
	 
	 List<ImportPackageElementVo> findByLocation();
	 
	 public Object over(String businessKey,
				String taskId, String outcome, String assignee, String comment);
	 
	 public Object returnOver(String businessKey,
				String taskId, String outcome, String assignee, String comment);
	 
	 public Object altOver(String businessKey,
				String taskId, String outcome, String assignee, String comment);
	 
	 public void abnormalEmail(List<ImportPackageElement> elementVos,String ids,ImportPackage importPackage);
	 
	 List<ImportPackageElementVo> findStorageByElementId(Integer cieElementId,Integer sqeElementId);
	 
	 List<StorageFlowVo> findStorageBySupplierQuoteElementId(Integer supplierQuoteElementId);
	 
	 StorageFlowVo findStorageBySoeIdAndIpeId(Integer supplierQuoteElementId,Integer importPackageElementId);
	 
	 public String generateImportNumber(ImportPackage importPackage);
	 
		public void Storage(StorageFlowVo storageFlowVo)throws Exception;
		
	public List<ImportPackagePaymentElementPrepare> getPaymentList(Integer importPackageId);
	
	public void changeLocation(String input,String aim);
	
	public void getUnchangeLocation(PageModel<StorageDetailVo> page,GridSort sort);
	
	Integer findSupplierQuoteElementId(Integer importPackageElementId);
	
	StorageFlowVo findImportPackageElementId(Integer supplierQuoteElementId);
	
	public List<ImportPackageElement> exportLotsExcelElement(PageModel<String> page);
	
	public MessageVo uploadExcel(MultipartFile multipartFile);
	
	public List<ImportPackageElement> selectByLocation(String location);
	
	public List<ImportPackageElement> selectByLocationId(Integer locationId);
	
	public List<Integer> getByLocation(String location,String id);
	
	public List<ImportPackageElement> getBySoeId(Integer supplierOrderElementId);
	
	public Double getLockStorageAmount(Integer supplierQuoteElementId,Integer importPackageElementId);
	
	public boolean addStorageCorrelation(StorageCorrelation storageCorrelation);
	
	public void correlationPage(PageModel<StorageDetailVo> page);
	
	public boolean deleteCorrelation(String id);
	
	public List<StorageDetailVo> getHasLifeStorage();
	
	public void sendEmail(List<ImportPackageElement> elements);
	
	public List<ImportPackageElementVo> getLocationInUse();
	
	public ResultVo updateLocation(String[] ids,String location);
	
	public ImportPackageElement getImportPackageElementByLocationAndCoeId(PageModel<ImportPackageElement> page);
	
	public void missionComplete(ImportPackage importPackage);
	
	public List<ImportPackageElementVo> getRestLocation();
}
