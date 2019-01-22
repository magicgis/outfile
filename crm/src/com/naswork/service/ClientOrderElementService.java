package com.naswork.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.model.ClientInvoiceElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementUpload;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.OrderApproval;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ClientOrderElementService {

	ClientOrderElement selectByPrimaryKey(Integer id);
	
	/*
     * 列表数据
     */
    public void listPage(PageModel<ClientOrderElementVo> page);
    
    /*
     * 订单明细信息
     */
    public List<ClientOrderElementVo> elementList(Integer id);
    
    /*
     * 新增页面显示数据
     */
    public ClientOrderElementVo findByClientQuoteElementId(Integer id);
    
    public void insertSelective(List<ClientOrderElement> list,Integer clientOrderId,String userId);
    
    public void insertSelective(ClientOrderElement clientOrderElement);
   
    public void sendEmail(List<EmailVo> emailVos,String userId);
    
    public int updateByPrimaryKeySelective(ClientOrderElement record);
    
    /*
     * excel上传
     */
	public MessageVo UploadExcel(MultipartFile multipartFile,Integer clientOrderId,Integer userId);
	

	
	/*
	 * 未到货列表
	 */
	public void unFinish(PageModel<ClientOrderElementVo> page,String where,GridSort sort);
	
	public void getUnfinishOrderPage(PageModel<ClientOrderElementVo> page,String where,GridSort sort);
	
	void insert(ClientOrderElement clientOrderElement);
	
	public void findByOrderIdPage(PageModel<ClientOrderElementVo> page,GridSort sort);
	
	public List<ClientOrderElementVo> findByOrderId(Integer id);
	
	public void errorList(PageModel<ClientOrderElementUpload> page);
	
	public void deleteMessage(Integer userId);
	
	ClientOrderElementVo findByclientOrderELementId(Integer id);
	
	/*
     *货款到期提醒
     */
    public void getDeadLineOrderPage(PageModel<ClientOrderElementVo> page,String where,GridSort sort);
    
    /*
     * 根据出库明细ID查询
     */
    public ClientOrderElement findByExportPackageElementId(Integer exportPackageElementId);
    
    public void orderApproval(ClientOrderElement clientOrderElement,String ids,OrderApproval orderApproval);
    
    List<ClientOrderElement> findSpztByClientOrderId(ClientOrderElement record);
    
    
    public Double getTotalAmount(Integer clientOrderId);
    
    public MessageVo coverExcel(MultipartFile multipartFile,Integer clientOrderId,Integer userId);
    
    
    String findBsnByCoeId(Integer id);
    
    public List<EmailVo> Storage(List<StorageFlowVo> supplierList,ClientOrderElement clientOrderElement,ClientOrder clientOrder);
    
    List<ClientOrderElement> selectByForeignKey(Integer clientOrderId);
    
    boolean Stock(List<ImportPackageElementVo> elementVos,ClientOrderElement clientOrderElement,List<StorageFlowVo> supplierList);
    
    boolean onPassageStock(List<SupplierListVo> elementVos,ClientOrderElement clientOrderElement,List<OnPassageStorage> supplierList);
    
    public Object pass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    
    public Object nopass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object weatherpass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object finalpass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object pricenopass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object pricepass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object zjlnopass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object weatherorderpass(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object theprice(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object cancellationorder(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object returnUser(String businessKey,
			String taskId, String outcome, String assignee, String comment);
    
    public Object syncFinish(String businessKey,String dbids);
    
    public Object syncNoUse(String businessKey,String dbids);
    
    Integer findClientIdByCoeId(Integer clientOrderElementId);
    
    List<Integer>  findUser(Integer clientOrderElementId);
    
    Double sumPrice(Integer clientWeatherOrderId);
    
    void updateBybankCharges(ClientOrderElement clientOrderElement);
    
    public MessageVo addClientOrder(List<ClientOrderElement>  clientOrderElements);
    
    public Integer getTotalCount(Integer clientOrderId);

    public Integer getUnfinishCount(Integer clientOrderId);
    
    public List<String> getSupplierNames(Integer clientOrderElementId);
    
    public Double getTotalById(Integer id);
    
    public List<ClientOrderElement> checkOrderElement(ClientOrderElement clientOrderElement);
   
}
