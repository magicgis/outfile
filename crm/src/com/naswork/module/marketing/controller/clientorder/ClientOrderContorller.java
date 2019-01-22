package com.naswork.module.marketing.controller.clientorder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.controller.BaseController;
import com.naswork.dao.OnPassageStorageDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementFinal;
import com.naswork.model.ClientOrderElementNotmatch;
import com.naswork.model.ClientOrderElementUpload;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.ClientWeatherOrderElementBackUp;
import com.naswork.model.Income;
import com.naswork.model.IncomeDetail;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.OrderApproval;
import com.naswork.model.OrderBankCharges;
import com.naswork.model.PurchaseApplicationForm;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierWeatherOrderElement;
import com.naswork.model.SystemCode;
import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierListVo;
import com.naswork.module.statistics.controller.StatisticsVo;
import com.naswork.module.task.controller.orderapproval.OrderApprovalVo;
import com.naswork.service.AuthorityRelationService;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInvoiceElementService;
import com.naswork.service.ClientInvoiceService;
import com.naswork.service.ClientOrderElementFinalService;
import com.naswork.service.ClientOrderElementNotmatchService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientQuoteElementService;
import com.naswork.service.ClientService;
import com.naswork.service.ClientWeatherOrderElementService;
import com.naswork.service.ContractReviewService;
import com.naswork.service.FlowService;
import com.naswork.service.GyExcelService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.IncomeDetailService;
import com.naswork.service.IncomeService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.service.OrderApprovalService;
import com.naswork.service.OrderBankChargesService;
import com.naswork.service.PurchaseApplicationFormService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.SupplierWeatherOrderElementService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.ColumnVo;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value="/sales/clientorder")
public class ClientOrderContorller extends BaseController{

	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private ClientQuoteElementService clientQuoteElementService;
	@Resource
	private UserService userService;
	@Resource
	private IncomeService incomeService;
	@Resource 
	private ClientInvoiceService clientInvoiceService;
	@Resource 
	private ClientInvoiceElementService clientInvoiceElementService;
	@Resource
	private IncomeDetailService incomeDetailService;
	@Resource
	private PurchaseApplicationFormService purchaseApplicationFormService;
	@Resource
	private GyExcelService gyExcelService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private AuthorityRelationService authorityRelationService;
	@Resource
	private OrderApprovalService orderApprovalService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private ClientService clientService;
	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private OnPassageStorageDao onPassageStroageDao;
	@Resource
	private Jbpm4JbyjService jbpm4JbyjService;
	@Resource
	private SupplierWeatherOrderElementService supplierWeatherOrderElementService;
	@Resource
	private ClientOrderElementFinalService clientOrderElementFinalService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	@Resource
	private FlowService flowService;
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private OrderBankChargesService orderBankChargesService;
	@Resource
	private ClientOrderElementNotmatchService clientOrderElementNotmatchService;
	@Resource
	private ClientWeatherOrderElementService clientWeatherOrderElementService;
	@Resource
	private ContractReviewService contractReviewService;
	/*
	 * 跳转列表页面
	 */
	@RequestMapping(value="/tolist",method=RequestMethod.GET)
	public String tolist(HttpServletRequest request) {
		UserVo userVo=getCurrentUser(request);
		request.setAttribute("userId", userVo.getUserId());
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			return "/marketing/clientorder/clientorderListbyorder";
		}else{
			return "/marketing/clientorder/clientorderListbyinvoice";
		}
	}
	
	/*
	 * 列表页面数据
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String partNumber = request.getParameter("partNumber");
		if ("".equals(where)) {
			where = null;
		}
		if (!"".equals(where) && partNumber != null) {
			page.put("partNumber", "'%"+partNumber+"%'");
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		
		clientOrderService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderVo clientOrderVo : page.getEntities()) {
				ClientOrder clientOrder = clientOrderService.getExportMessage(clientOrderVo.getId());
				if (clientOrder!=null) {
					StringBuffer amountPercent = new StringBuffer();
					StringBuffer totalPercent = new StringBuffer();
					DecimalFormat df=new DecimalFormat("#.##");
					if (clientOrder.getTotalAmount().equals(new Double(0))) {
						amountPercent.append(0).append("%");
					}else {
						amountPercent.append(df.format(clientOrder.getTotalAmount()/clientOrder.getOrderAmount()*100)).append("%");
					}
					if (clientOrder.getTotal().equals(new Double(0))) {
						totalPercent.append(0).append("%");
					}else {
						totalPercent.append(df.format(clientOrder.getTotal()/clientOrder.getClientOrderPrice()*100)).append("%");
					}
					
					clientOrderVo.setExportAmountPercent(amountPercent.toString());
					clientOrderVo.setExportTotalPercent(totalPercent.toString());
					if (clientOrder.getTotal() != null) {
						clientOrderVo.setExportTotal(clientOrder.getTotal());
					}
				}
				Double incomeTotal = clientOrderService.getIncomeTotalByOrderId(clientOrderVo.getId());
				if (incomeTotal != null) {
					clientOrderVo.setIncomeTotal(incomeTotal);
				}
				List<Date> exportDates = clientOrderService.getExportDates(clientOrderVo.getId());
				StringBuffer exportDate = new StringBuffer();
				if (exportDates.size() > 0 && exportDates.get(0) != null) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					for (Date date : exportDates) {
						if (date != null) {
							exportDate.append(simpleDateFormat.format(date)).append(" | ");
						}
					}
				}
				clientOrderVo.setExportDates(exportDate.toString());
				clientOrderVo.setPrepayRate(clientOrderVo.getPrepayRate()*100);
				clientOrderVo.setReceivePayRate(clientOrderVo.getReceivePayRate()*100);
				clientOrderVo.setShipPayRate(clientOrderVo.getShipPayRate()*100);
				Double total = clientOrderElementService.getTotalById(clientOrderVo.getId());
				if (total != null) {
					clientOrderVo.setTotal(total);
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/*
	 * 修改页面
	 */
	@RequestMapping(value="/clientorderEdit",method=RequestMethod.GET)
	public String clientorderEdit(HttpServletRequest request) {
		Integer id =new Integer(getString(request, "id"));
		ClientOrderVo clientOrderVo = clientOrderService.findById(id);
		clientOrderVo.setPrepayRate(clientOrderVo.getPrepayRate()*100);
		clientOrderVo.setReceivePayRate(clientOrderVo.getReceivePayRate()*100);
		clientOrderVo.setShipPayRate(clientOrderVo.getShipPayRate()*100);
		request.setAttribute("clientOrderVo", clientOrderVo);
		return "/marketing/clientorder/clientorderEdit";
	}
	
	/**
	 * 证书
	 */
	@RequestMapping(value="/certification",method=RequestMethod.POST)
	public @ResponseBody ResultVo classifyList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String certification=request.getParameter("certification");
		List<SystemCode> list=new ArrayList<SystemCode>();
		if(!"".equals(certification)&&null!=certification){
			String[] certifications=certification.split(",");
			for (String string : certifications) {
				SystemCode code=new SystemCode();
				code.setCode(string);
				list.add(code);
			}
		}
		
		success = true;
		JSONArray json = new JSONArray();
			json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*
	 * 货币信息
	 */
	@RequestMapping(value="/findCurrency",method=RequestMethod.POST)
	public @ResponseBody ResultVo findCurrency(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		Integer id =new Integer(getString(request, "id"));
		ClientOrderVo clientOrderVo = clientOrderService.findById(id);
		List<SystemCode> list2 = systemCodeService.findCurrency();
		List<SystemCode> list = new ArrayList<SystemCode>();
		for (SystemCode systemCode : list2) {
			if (systemCode.getId().equals(clientOrderVo.getCurrencyId())) {
				list.add(systemCode);
			}
		}
		for (SystemCode systemCode : list2) {
			if (!systemCode.getId().equals(clientOrderVo.getCurrencyId())) {
				list.add(systemCode);
			}
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	
	/*
	 * 状态
	 */
	@RequestMapping(value="/zt",method=RequestMethod.POST)
	public @ResponseBody ResultVo zt(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		Integer id=new Integer(getString(request, "id"));
		ClientOrderVo clientOrderVo = clientOrderService.findById(id);
		List<SystemCode> list2=systemCodeService.findOrderStatus();
		List<SystemCode> list=new ArrayList<SystemCode>();
		SystemCode systemCode=new SystemCode();
		for (SystemCode systemCode2 : list2) {
			if (systemCode2.getId()==clientOrderVo.getOrderStatusId()) {
				systemCode=systemCode2;
			}
		}
		list.add(systemCode);
		for (SystemCode systemCode2 : list2) {
			if (systemCode2.getId()!=clientOrderVo.getOrderStatusId()) {
				list.add(systemCode2);
			}
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*
	 * 修改订单
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public @ResponseBody ResultVo Edit(HttpServletRequest request,@ModelAttribute ClientOrder clientOrder) {
		boolean success = false;
		String msg = "";
		UserVo userVo = getCurrentUser(request);
		
		if (clientOrder.getId()!=null) {
			clientOrder.setUpdateTimestamp(new Date());
			clientOrder.setCreateUserId(new Integer(userVo.getUserId()));
			clientOrderService.updateByPrimaryKeySelective(clientOrder);
			success=true;
			msg="修改成功！";
		}else {
			msg="修改失败！";
		}
		return new ResultVo(success, msg);
	}
	
	
	
	/*
	 * 明细列表
	 */
	@RequestMapping(value="/element",method=RequestMethod.GET)
	public String element(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ClientOrderVo clientOrderVo = clientOrderService.findById(id);
		request.setAttribute("id", id);
		request.setAttribute("orderNumber", clientOrderVo.getOrderNumber());
		return "/marketing/clientorder/clientOrderElementList";
	}
	
	/*
	 * 明细列表数据
	 */
	@RequestMapping(value="/elementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		Integer id = new Integer(getString(request, "id"));
		ClientOrderVo clientOrderVo = clientOrderService.findById(id);
		
		page.put("id", id);
		
		clientOrderElementService.findByOrderIdPage(page,sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			
			for (ClientOrderElementVo clientOrderElemenrVo : page.getEntities()) {
				IncomeDetail incomeDetail = incomeDetailService.getTotalByClientOrderElementId(clientOrderElemenrVo.getId());
				if (incomeDetail != null ) {
					clientOrderElemenrVo.setTotal(incomeDetail.getTotal());
					clientOrderElemenrVo.setReceiveDate(incomeDetail.getReceiveDate());
				}
				StringBuffer amountPercent = new StringBuffer();
				DecimalFormat df=new DecimalFormat("#.##");
				if (clientOrderElemenrVo.getExportAmount() != null) {
					if (clientOrderElemenrVo.getExportAmount().equals(new Double(0))) {
						amountPercent.append(0).append("%");
						clientOrderElemenrVo.setExportAmountPercent(amountPercent.toString());
					}else {
						amountPercent.append(df.format(clientOrderElemenrVo.getExportAmount()/clientOrderElemenrVo.getClientOrderAmount()*100)).append("%");
						clientOrderElemenrVo.setExportAmountPercent(amountPercent.toString());
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElemenrVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
	
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/*
	 * 明细上传页面
	 */
	@RequestMapping(value="/addelement",method=RequestMethod.GET)
	public String addelement(HttpServletRequest request) {
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		request.setAttribute("clientOrderId", clientOrderId);
 		return "/marketing/clientorder/addElement";
	}
	
	
	/*
	 * excel上传 
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = clientOrderElementService.UploadExcel(multipartFile,clientOrderId,new Integer(userVo.getUserId()));
		
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/*
	 * 覆盖excel上传 
	 */
	@RequestMapping(value="/coverExcel",method=RequestMethod.POST)
	public @ResponseBody String coverExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = clientOrderElementService.coverExcel(multipartFile,clientOrderId,new Integer(userVo.getUserId()));
		
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	
	/*
	 * 新增订单明细页面
	 */
	@RequestMapping(value="/addOrder",method=RequestMethod.GET)
	public String addOrder(HttpServletRequest request) {
		PageModel<ClientOrderElementVo> page=getPage(request);
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		List<ClientOrderElementVo> orderList = clientOrderElementService.findByOrderId(clientOrderId);
		ClientOrderVo clientOrderVo = clientOrderService.findById(clientOrderId);
		page.put("id", clientOrderId);
		List<ClientOrderElementVo> elementList = clientOrderElementService.elementList(clientOrderVo.getClientQuoteId());
		List<ClientOrderElementVo> list = new ArrayList<ClientOrderElementVo>();
		if (orderList.size()!=0) {
			for (int i = 0; i < elementList.size(); i++) {
				for (ClientOrderElementVo clientOrderElement : orderList) {
					if (elementList.get(i).getId().equals(clientOrderElement.getId())) {
						elementList.remove(i);
						i=-1;
						break;
					}
				}
			}
		}
		//list=elementList;
		
		request.setAttribute("list", elementList);
		return "/marketing/clientorder/addOrderElementTable";
	}
	
	/*
	 * 保存订单
	 */
	/*@RequestMapping(value="/saveOrder",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveOrder(HttpServletRequest request,@ModelAttribute ClientOrderElement clientOrderElement) {
		String orderNumber = getString(request, "orderNumber");
		Integer clientInquiryElementId =new Integer(getString(request, "clientInquiryElementId"));
		boolean success = false;
		String msg = "";
		if (clientOrderElement.getPrice()!=null) {
			Integer clientOrderId = clientOrderService.findIdByOrderNumber(orderNumber);
			Integer clientQuoteElementId = clientQuoteElementService.findIdByCieID(clientInquiryElementId);
			clientOrderElement.setClientOrderId(clientOrderId);
			clientOrderElement.setClientQuoteElementId(clientQuoteElementId);
			clientOrderElement.setUpdateTimestamp(new Date());
			clientOrderElementService.insertSelective(clientOrderElement);
			success=true;
			msg="保存成功！";
		}else{
			msg="保存失败！";
		}
		
		return new ResultVo(success, msg);
	}*/
	
	
	/*
	 * 修改页面
	 */
	/*@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(HttpServletRequest request) {
		Integer id =new Integer(getString(request, "id"));
		String orderNumber = getString(request, "orderNumber");
		ClientOrderElementVo clientOrderElementVo = clientOrderElementService.findByClientQuoteElementId(id);
		request.setAttribute("clientOrderElementVo", clientOrderElementVo);
		request.setAttribute("orderNumber", orderNumber);
		return "/marketing/clientorder/editElementOrder";
	}*/
	
	/*
	 * 保存修改
	 */
	/*@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute ClientOrderElement clientOrderElement) {
		boolean success = false;
		String message = "";
		
		if (clientOrderElement.getId()!=null) {
			clientOrderElement.setUpdateTimestamp(new Date());;
			clientOrderElementService.updateByPrimaryKeySelective(clientOrderElement);
			success = true;
			message = "修改成功！";
		}else{
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
	}*/
	
	

	/*
	 * 修改订单明细
	 */
	@RequestMapping(value="/editElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editElement(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String a = getString(request, "clientOrderElementId");
		UserVo userVo=getCurrentUser(request);
		List<EmailVo> emailVos=new ArrayList<EmailVo>();
		if(a!=null && !"".equals(a)){
			ClientOrderElementVo data=clientOrderElementService.findByclientOrderELementId(new Integer(a));
			ClientOrder clientOrder = clientOrderService.getClientOrder(new Integer(a));
			ClientOrderElement clientOrderElement = new ClientOrderElement();
			clientOrderElement.setId(new Integer(a));
			clientOrderElement.setAmount(new Double(getString(request, "clientOrderAmount")));
			clientOrderElement.setPrice(new Double(getString(request, "clientOrderPrice")));
			String leadTime = getString(request, "clientOrderLeadTime");
			String partNumber = getString(request, "inquiryPartNumber");
			String unit = getString(request, "unit");
			clientOrderElement.setLeadTime(leadTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(clientOrder.getOrderDate());
			calendar.add(Calendar.DATE, new Integer(leadTime));
			clientOrderElement.setDeadline(calendar.getTime());
			clientOrderElement.setCertificationId(Integer.parseInt(request.getParameter("certificationCode")));
			String status = request.getParameter("orderStatusValue");
			ClientOrderElement clientOrderElement2 = clientOrderElementService.selectByPrimaryKey(new Integer(a));
			ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientOrderElement2.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			if (status != null && !"".equals(status)) {
				clientOrderElement2.setElementStatusId(new Integer(status));
			}
			if (partNumber != null && !"".equals(partNumber)) {
				clientOrderElement2.setPartNumber(partNumber);
			}
			if (unit != null && !"".equals(unit)) {
				clientOrderElement2.setUnit(unit);
			}
			clientOrderElementService.updateByPrimaryKeySelective(clientOrderElement2);
			if (status.equals("711")) {
				clientOrderElement.setOrderStatusId(64);
			}
			
			String fixedCost=request.getParameter("fixedCost");
			if(null!=fixedCost&&!"".equals(fixedCost)){
				clientOrderElement.setFixedCost(new Double(fixedCost));
			}
			clientOrderElement.setRemark(request.getParameter("remark"));
			clientOrderElementService.updateByPrimaryKeySelective(clientOrderElement);
			
			List<ClientInvoice> clientInvoices=clientInvoiceService.selectByclientOrderElementId(clientOrderElement.getId());
			for (ClientInvoice clientInvoice : clientInvoices) {
				ClientInvoiceElement record=new ClientInvoiceElement();
				record.setClientInvoiceId(clientInvoice.getId());
				record.setAmount(clientOrderElement.getAmount());
				clientInvoiceElementService.updateByClientOrderElementId(record);
			}
			
			
//			List<ImportPackageElementVo> list=importpackageElementService.findStorageByCoeId(clientOrderElement.getId());
//			if(null!=list.get(0)&&list.size()>0){
//				if(list.get(0).getAmount().equals(data.getClientOrderAmount())&&list.get(0).getAmount()>clientOrderElement.getAmount()){
//					EmailVo emailVo=new EmailVo();
//					emailVo.setNowImportpackNumber(list.get(0).getImportNumber());
//					emailVo.setPartNumber(data.getQuotePartNumber());
//					emailVo.setNowAmount(clientOrderElement.getAmount());
//					emailVo.setOldAmount(data.getClientOrderAmount());
//					emailVo.setClientOrderNumber(data.getOrderNumber());
//					emailVos.add(emailVo);
//					importpackageElementService.sendAlterstorageaEmail(emailVos, userVo.getUserId());
//				}
//			}
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
			/*if ("".equals(getString(request, "clientOrderElementId"))) {
				message = "无订单数据不可以修改！";
				return new EditRowResultVo(success, message);
			}
			ClientOrderElementVo clientOrderElementVo = clientOrderElementService.findByClientQuoteElementId(new Integer(getString(request, "clientOrderElementId")));
			ClientOrderElement clientOrderElement2 = clientOrderElementService.selectByPrimaryKey(clientOrderElementVo.getClientOrderElementId());
			ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(clientOrderElement2.getClientOrderId());
			ClientOrderElement clientOrderElement = new ClientOrderElement();
			clientOrderElement.setId(clientOrderElementVo.getClientOrderElementId());
			clientOrderElement.setAmount(new Double(getString(request, "clientOrderAmount")));
			clientOrderElement.setPrice(new Double(getString(request, "clientOrderPrice")));
			String leadTime = getString(request, "clientOrderLeadTime");
			clientOrderElement.setLeadTime(leadTime);
			//String date = getString(request, "clientOrderDeadline");
			//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			//Date today = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(clientOrder.getOrderDate());
			calendar.add(Calendar.DATE, new Integer(leadTime));
			clientOrderElement.setDeadline(calendar.getTime());
			clientOrderElementService.updateByPrimaryKeySelective(clientOrderElement);*/
			
		
		return new EditRowResultVo(success, message);
	}
	
	/*
	 * 修改订单明细
	 */
	@RequestMapping(value="/updateDesc",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateDesc(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		String id = getString(request, "id");
		String desc=getString(request, "description");
		ClientOrderElementNotmatch record=new ClientOrderElementNotmatch();
		record.setId(Integer.parseInt(id));
		record.setDescription(desc);
		clientOrderElementNotmatchService.updateByPrimaryKeySelective(record);
		return new EditRowResultVo(success, message);
	}
	
	
	/*
	 * 文件上传
	 */
	@RequestMapping(value="/fileUpload",method=RequestMethod.GET)
	public String fileUpload(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "client_order");
		return "/marketing/clientinquiry/fileUpload";
	}
	
	/*
	 * 多条新增
	 */
	@RequestMapping(value="/addOrderElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo addOrderElement(HttpServletRequest request,@ModelAttribute ClientOrderVo clientOrderVo) {
		boolean success = false;
		String message = "";
		Integer clientOrderId = new Integer(getString(request, "clientOrderId"));
		UserVo userVo=getCurrentUser(request);
		if (clientOrderVo.getList().size()>0) {
			clientOrderElementService.insertSelective(clientOrderVo.getList(),clientOrderId,userVo.getUserId());
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		if(success){
			ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(clientOrderId);
			if(clientOrder.getPrepayRate()>0){
				List<ClientInvoice> clientInvoices=clientInvoiceService.selectByclientOrderId(clientOrderId,1);
				if(null!=clientInvoices&&clientInvoices.size()>0){
					for (ClientOrderElement elementDataVo : clientOrderVo.getList()) {
						if (elementDataVo.getAmount()!=null) {
						 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
						 clientInvoiceElement.setAmount(elementDataVo.getAmount());
						 Double terms=clientOrder.getPrepayRate()*100;
						 clientInvoiceElement.setTerms(terms.intValue());
						 clientInvoiceElement.setClientInvoiceId(clientInvoices.get(0).getId());
						 clientInvoiceElement.setClientOrderElementId(elementDataVo.getId());
						 clientInvoiceElementService.insert(clientInvoiceElement);
						}
					}
				}else{
					String[] ziMu={"AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM",
							"AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
					for (int i = 0; i < ziMu.length; i++) {
						ClientInvoice incoiceNumber=clientInvoiceService.selectByCode(clientOrder.getOrderNumber().substring(4)+ziMu[i]);
						if(null!=incoiceNumber){
							continue;
						}else{
							ClientInvoice clientInvoice=new ClientInvoice();
							clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+ziMu[i]);
							clientInvoice.setClientOrderId(clientOrderId);
							clientInvoice.setInvoiceDate(new Date());
							Double terms=clientOrder.getPrepayRate()*100;
							clientInvoice.setTerms(terms.intValue());
							clientInvoice.setInvoiceType(1);
							clientInvoice.setInvoiceStatusId(0);
							clientInvoiceService.insert(clientInvoice);
							for (ClientOrderElement elementDataVo : clientOrderVo.getList()) {
								if (elementDataVo.getAmount()!=null) {
								 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
								 clientInvoiceElement.setAmount(elementDataVo.getAmount());
								 clientInvoiceElement.setTerms(terms.intValue());
								 clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
								 clientInvoiceElement.setClientOrderElementId(elementDataVo.getId());
								 clientInvoiceElementService.insert(clientInvoiceElement);
								 
								}
							}
						}
						break;
					}
				}
			}
			//生成形式发票
			List<ClientInvoice> clientInvoices=clientInvoiceService.selectByclientOrderId(clientOrderId,0);
			if(null!=clientInvoices&&clientInvoices.size()>0){
				for (ClientOrderElement elementDataVo : clientOrderVo.getList()) {
					if (elementDataVo.getAmount()!=null) {
					 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
					 clientInvoiceElement.setAmount(elementDataVo.getAmount());
					 clientInvoiceElement.setTerms(100);
					 clientInvoiceElement.setClientInvoiceId(clientInvoices.get(0).getId());
					 clientInvoiceElement.setClientOrderElementId(elementDataVo.getId());
					 ClientInvoiceElement invoiceElement=clientInvoiceElementService.selectByCoeIdAndCiId(clientInvoiceElement);
					 if(null==invoiceElement){
						 clientInvoiceElementService.insert(clientInvoiceElement);
					 }
					}
				}
			}else{
				ClientInvoice clientInvoice=new ClientInvoice();
				clientInvoice.setInvoiceNumber(clientOrder.getOrderNumber().substring(4)+"PR");
				clientInvoice.setClientOrderId(clientOrderId);
				clientInvoice.setInvoiceDate(new Date());
				clientInvoice.setTerms(100);
				clientInvoice.setInvoiceType(0);
				clientInvoice.setInvoiceStatusId(0);
				clientInvoiceService.insert(clientInvoice);
				for (ClientOrderElement elementDataVo : clientOrderVo.getList()) {
					if (elementDataVo.getAmount()!=null) {
					 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
					 clientInvoiceElement.setAmount(elementDataVo.getAmount());
					 clientInvoiceElement.setTerms(100);
					 clientInvoiceElement.setClientInvoiceId(clientInvoice.getId());
					 clientInvoiceElement.setClientOrderElementId(elementDataVo.getId());
					 clientInvoiceElementService.insert(clientInvoiceElement);
					 
					}
				}
			}
		}
		return new ResultVo(success,message);
	}
	
	
	/**
	 * 跳转未完成工作页面
	 */
	@RequestMapping(value="/toUnFinishWork",method=RequestMethod.GET)
	public String toUnFinishWork() {
		return "/marketing/unfinishwork/unFinishWorkList";
	}
	
	/**
	 * 未发货清单
	 */
	@RequestMapping(value="/unFinish",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo unFinish(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		
		clientOrderElementService.getUnfinishOrderPage(page,where,sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElementVo clientOrderElemenrVo : page.getEntities()) {
				Integer unfinish = clientOrderElementService.getUnfinishCount(clientOrderElemenrVo.getId());
				Integer total = clientOrderElementService.getTotalCount(clientOrderElemenrVo.getId());
				StringBuffer terms = new StringBuffer();
				terms.append(unfinish).append("/").append(total);
				clientOrderElemenrVo.setTerms(terms.toString());
				BigDecimal i=new BigDecimal(total);
				BigDecimal q=new BigDecimal(unfinish);
				if (!q.equals(BigDecimal.ZERO)){
					BigDecimal p=q.divide(i,10,BigDecimal.ROUND_HALF_DOWN);
					BigDecimal p2=p.multiply(new BigDecimal(100));
					String proportion=p2.setScale(0, BigDecimal.ROUND_HALF_UP)+"%"+"("+unfinish+"/"+total+")";
					clientOrderElemenrVo.setTerms(proportion);
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElemenrVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			
			clientOrderElementService.unFinish(page,where,sort);
			List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElementVo clientOrderElemenrVo : page.getEntities()) {
				Double b1 = clientOrderElemenrVo.getClientOrderAmount();  
				Double b2 = clientOrderElemenrVo.getExportAmount();  
				clientOrderElemenrVo.setAmount(b1-b2); 
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElemenrVo);
				exportList.add(map);
			}
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportModel ="[{\"name\":\"订单号\",\"width\":103,\"align\":0,\"property\":\"orderNumber\"},"
					              +"{\"name\":\"件号\",\"width\":180,\"align\":0,\"property\":\"quotePartNumber\"},"
					              +"{\"name\":\"另件号\",\"width\":217,\"align\":0,\"property\":\"alterPartNumber\"},"
					              +"{\"name\":\"未出货数量\",\"width\":50,\"align\":0,\"property\":\"amount\"},"
					              +"{\"name\":\"周期\",\"width\":80,\"align\":0,\"property\":\"clientOrderLeadTime\"},"
					              +"{\"name\":\"截至日期\",\"width\":80,\"align\":0,\"property\":\"clientOrderDeadline\"},"
					              +"{\"name\":\"备注\",\"width\":80,\"align\":0,\"property\":\"clientQuoteRemark\"}]";
					exportService.exportGridToXls("未发货清单", exportModel, exportList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	
	@RequestMapping(value="/excel",method=RequestMethod.POST)
	public void excel(HttpServletResponse response){
		PageModel<ClientOrderElementVo> page=new PageModel<ClientOrderElementVo>();
		clientOrderElementService.unFinish(page,null,null);
		List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
		for (ClientOrderElementVo clientOrderElemenrVo : page.getEntities()) {
			Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElemenrVo);
			exportList.add(map);
		}
			try {
				String exportModel ="[{\"name\":\"订单号\",\"width\":103,\"align\":0,\"property\":\"orderNumber\"},"
				              +"{\"name\":\"件号\",\"width\":180,\"align\":0,\"property\":\"quotePartNumber\"},"
				              +"{\"name\":\"另件号\",\"width\":217,\"align\":0,\"property\":\"alterPartNumber\"},"
				              +"{\"name\":\"未出货数量\",\"width\":50,\"align\":0,\"property\":\"amount\"},"
				              +"{\"name\":\"周期\",\"width\":80,\"align\":0,\"property\":\"clientOrderLeadTime\"},"
				              +"{\"name\":\"截至日期\",\"width\":80,\"align\":0,\"property\":\"clientOrderDeadline\"},"
				              +"{\"name\":\"备注\",\"width\":80,\"align\":0,\"property\":\"clientQuoteRemark\"}]";
				exportService.exportGridToXls("未发货清单", exportModel, exportList, response);
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("导出数据出错!", e);
			}
			
	}
	
	/**
	 * 未发货清单明细
	 */
	@RequestMapping(value="/unFinishElement",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo unFinishElement(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		page.put("clientOrderId", getString(request, "id"));
		
		clientOrderElementService.unFinish(page,where,sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElementVo clientOrderElemenrVo : page.getEntities()) {
				Double b1 = clientOrderElemenrVo.getClientOrderAmount();  
				Double b2 = clientOrderElemenrVo.getExportAmount();  
				clientOrderElemenrVo.setAmount(b1-b2); 
				List<String> names = clientOrderElementService.getSupplierNames(clientOrderElemenrVo.getClientOrderElementId());
				if (names.size() > 0) {
					StringBuffer name = new StringBuffer();
					for (String string : names) {
						name.append(string).append(",");
					}
					name.deleteCharAt(name.length()-1);
					clientOrderElemenrVo.setSupplierCode(name.toString());
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElemenrVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("未发货清单", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/*
	 * 客户询价单统计
	 */
	@RequestMapping(value="/toStatistics",method=RequestMethod.GET)
	public String toStatistics(HttpServletRequest request) {
		/*PageModel<StatisticsVo> page=getPage(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		int count = clientOrderService.statisticsPage(page, where, sort);
		request.setAttribute("count", count);*/
		return "/marketing/statistics/statisticsList";
	}
	
	/*
	 * 统计列表
	 */
	@RequestMapping(value="/Statistics",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo Statistics(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StatisticsVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		clientOrderService.statisticsPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StatisticsVo statisticsVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(statisticsVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("客户询价单统计", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		//导出
		if (StringUtils.isNotEmpty(request.getParameter("exportModel"))) {
				try {
					exportService.exportGridToXls("客户询价单统计",
							request.getParameter("exportModel"),
							jqgrid.getRows(), response);
					return null;
				} catch (Exception e) {
					logger.warn("导出数据出错!", e);
				}
		}
		
		return jqgrid;
	}
	
	/**
	 * 错误信息
	 */
	@RequestMapping(value="/toErrorMessage",method=RequestMethod.GET)
	public String toErrorMessage(HttpServletRequest request){
		return "/marketing/clientorder/errorlist";
	}
	
	/**
	 * 错误信息
	 */
	@RequestMapping(value="/toOrderDesc",method=RequestMethod.GET)
	public String toOrderDesc(HttpServletRequest request){
		return "/marketing/clientorder/orderdesclist";
	}
	
	/**
	 * 预订单信息有误页面
	 */
	@RequestMapping(value="/toOrderNotMatch",method=RequestMethod.GET)
	public String toOrderNotMatch(HttpServletRequest request){
		return "/marketing/clientweatherorder/ordernotmatchlist";
	}
	
	/**
	 * 错误信息列表
	 */
	@RequestMapping(value="/errorMessage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo errorMessage(HttpServletRequest request) {
		PageModel<ClientOrderElementUpload> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		page.put("userId", userVo.getUserId());
		
		clientOrderElementService.errorList(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElementUpload clientOrderElementUpload : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElementUpload);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
	
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/**
	 * 描述不匹配列表
	 */
	@RequestMapping(value="/descNotMatch",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo descNotMatch(HttpServletRequest request) {
		PageModel<ClientOrderElementNotmatch> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		page.put("userId", userVo.getUserId());
		
		clientOrderElementNotmatchService.listpage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElementNotmatch clientOrderElementNotmatch : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElementNotmatch);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
	
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/**
	 * 预订单信息有误页面
	 */
	@RequestMapping(value="/orderNotMatch",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo orderNotMatch(HttpServletRequest request) {
		PageModel<ClientWeatherOrderElementBackUp> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		page.put("userId", userVo.getUserId());
		
		clientWeatherOrderElementService.getErroeListPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientWeatherOrderElementBackUp clientWeatherOrderElementBackUp : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(clientWeatherOrderElementBackUp);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
	
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	
	/**
	 * 根据item关联生成另件号询价与报价记录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/connectByItem",method=RequestMethod.POST)
	public @ResponseBody ResultVo connectByItem(HttpServletRequest request) {
		try {
			String ids = getString(request, "ids");
			String[] elementIds = ids.split(",");
			boolean success = clientWeatherOrderElementService.connectByItem(elementIds);
			if (success) {
				return new ResultVo(success, "关联成功！");
			}else {
				return new ResultVo(success, "异常！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "异常！");
		}
	}
	
	/**
	 * 不录入记录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/unCommit",method=RequestMethod.POST)
	public @ResponseBody ResultVo unCommit(HttpServletRequest request) {
		try {
			String ids = getString(request, "ids");
			String[] elementIds = ids.split(",");
			boolean success = clientWeatherOrderElementService.unCommit(elementIds);
			if (success) {
				return new ResultVo(success, "修改成功！");
			}else {
				return new ResultVo(success, "异常！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "异常！");
		}
	}
	
	/**
	 * 检测预订单错误列表是否处理完
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkFinish",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkFinish(HttpServletRequest request) {
		try {
			String id = getString(request, "id");
			List<ClientWeatherOrderElementBackUp> list = clientWeatherOrderElementService.checkErrorRecord(new Integer(id));
			if (list.size() == 0) {
				return new ResultVo(true, "修改成功！");
			}else {
				return new ResultVo(false, "没有处理完记录！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "异常！");
		}
	}
	
	/**
	 * 删除错误信息
	 */
	@RequestMapping(value="/deleteError",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteError(HttpServletRequest request) {
		UserVo userVo=getCurrentUser(request);
		clientOrderElementService.deleteMessage(new Integer(userVo.getUserId()));
		return new ResultVo(true, "删除成功！");
	}
	
	/**
	 * 删除错误信息
	 */
	@RequestMapping(value="/deleteBackUp",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteBackUp(HttpServletRequest request) {
		try {
			String id = getString(request, "id");
			List<ClientWeatherOrderElementBackUp> list = clientWeatherOrderElementService.selectByOrderId(new Integer(id));
			for (int i = 0; i < list.size(); i++) {
				ClientWeatherOrderElement clientWeatherOrderElement = new ClientWeatherOrderElement(list.get(i));
				clientWeatherOrderElementService.insertSelective(clientWeatherOrderElement);
			}
			clientWeatherOrderElementService.deleteMessage(new Integer(id));
			return new ResultVo(true, "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
		
	}
	
	/**
	 * 删除描述不匹配的信息
	 */
	@RequestMapping(value="/deleteDate",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteDate(HttpServletRequest request) {
		UserVo userVo=getCurrentUser(request);
		clientOrderElementNotmatchService.deleteByUserId(new Integer(userVo.getUserId()));
		return new ResultVo(true, "删除成功！");
	}
	
	/**
	 * 修改描述后新增订单明细
	 */
	@RequestMapping(value="/insertDate",method=RequestMethod.POST)
	public @ResponseBody ResultVo insertDate(HttpServletRequest request,HttpServletResponse response) {
		UserVo userVo=getCurrentUser(request);
		 List<ClientOrderElement>  clientOrderElements=clientOrderElementNotmatchService.selectByUserId(new Integer(userVo.getUserId()));
		 MessageVo messageVo= clientOrderElementService.addClientOrder(clientOrderElements);
		 clientOrderElementNotmatchService.deleteByUserId(new Integer(userVo.getUserId()));
		 return new ResultVo(messageVo.getFlag(),messageVo.getMessage());
	}
	
	
	/**
	 * 收款页面
	 */
	@RequestMapping(value="/toIncome",method=RequestMethod.GET)
	public String toIncome(HttpServletRequest request){
		String id = getString(request, "id");
		ClientInvoice clientInvoice = clientInvoiceService.selectByPrimaryKey(new Integer(id));
		request.setAttribute("clientInvoiceNumber", clientInvoice.getInvoiceNumber());
		request.setAttribute("term", clientInvoice.getTerms());
		request.setAttribute("clientInvoiceId", id);
		return "/marketing/clientorder/incomeList";
	}
	
	/**
	 * 收款列表
	 */
	@RequestMapping(value="/IncomeList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo IncomeList(HttpServletRequest request) {
		PageModel<Income> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		page.put("clientInvoiceId", getString(request, "clientInvoiceId"));
		
		incomeService.IncomePage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Income income : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(income);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
	
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/**
	 * 修改收款单
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo saveEdit(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			Income income = new Income();
			income.setId(new Integer(getString(request, "id")));
			income.setUpdateTimestamp(new Date());
			String date = getString(request, "receiveDate");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			income.setReceiveDate(sdf.parse(date));
			//income.setTotalSum(new Double(getString(request, "totalSum")));
			income.setRemark(getString(request, "remark"));
			incomeService.updateByPrimaryKeySelective(income);
			message = "新增成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增失败！";
		}
		
		return new EditRowResultVo(success, message);
				
	}
	
	/**
	 * 新增收款页面
	 */
	@RequestMapping(value="/toAddIncome",method=RequestMethod.GET)
	public String toAddIncome(HttpServletRequest request){
		ClientInvoice clientInvoice = clientInvoiceService.selectByPrimaryKey(new Integer(getString(request, "id")));
		request.setAttribute("clientInvoice", clientInvoice);
		request.setAttribute("today", new Date());
		return "/marketing/clientorder/addIncome";
	}
	
	/**
	 * 保存新增收款
	 */
	@RequestMapping(value="/saveAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute Income income){
		String message = "";
		boolean success = false;
		if (income.getClientInvoiceId()!=null) {
			income.setUpdateTimestamp(new Date());
			incomeService.insertSelective(income);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 收款明细列表
	 */
	@RequestMapping(value="/incomeDetail",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo incomeDetail(HttpServletRequest request){
		PageModel<IncomeDetail> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		page.put("incomeId", getString(request, "id"));
		
		incomeDetailService.getByInvoiceIdPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (IncomeDetail incomeDetail : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(incomeDetail);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
	
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/**
	 * 修改明细
	 */
	@RequestMapping(value="/editIncomeDetail",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editIncomeDetail(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			IncomeDetail incomeDetail = new IncomeDetail();
			incomeDetail.setId(new Integer(getString(request, "id")));
			incomeDetail.setTotal(new Double(getString(request, "total")));
			incomeDetail.setRemark(getString(request, "remark"));
			incomeDetail.setClientOrderElementId(new Integer(getString(request, "clientOrderElementId")));
			incomeDetailService.updateByPrimaryKeySelective(incomeDetail);
			message = "新增成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增失败！";
		}
		
		return new EditRowResultVo(success, message);
				
	}
	
	/**
	 * 新增明细
	 */
	@RequestMapping(value="/toAddDetail",method=RequestMethod.GET)
	public String toAddDetail(HttpServletRequest request){
		Integer clientInvoiceId = new Integer(getString(request, "clientInvoiceId"));
		List<ClientInvoiceExcelVo> eleList = clientInvoiceService.getEleMessage(clientInvoiceId);
		request.setAttribute("eleList", eleList);
		return "/marketing/clientorder/addInvoiceElementTable";
	}
	
	/**
	 * 保存新增收款明细
	 */
	@RequestMapping(value="/saveAddDetail",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAddDetail(HttpServletRequest request,@ModelAttribute ClientInvoiceElement clientInvoiceElement){
		String message = "";
		boolean success = false;
		Integer incomeId = new Integer(getString(request, "id"));
		Integer clientInvoiceId = new Integer(getString(request, "clientInvoiceId"));
		if (clientInvoiceElement.getVoList()!=null) {
			incomeDetailService.insertSelective(clientInvoiceElement.getVoList(),incomeId,clientInvoiceId);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 新增采购申请单
	 */
	@RequestMapping(value="/addPurchaseApply",method=RequestMethod.POST)
	public @ResponseBody ResultVo addPurchaseApply(HttpServletRequest request){
		String message = "";
		boolean success = false;
		UserVo userVo = getCurrentUser(request);
		Integer clientOrderId = new Integer(getString(request, "id"));
		try {
			PurchaseApplicationForm purchaseApplicationForm = purchaseApplicationFormService.findByClientOrderId(clientOrderId);
			if (purchaseApplicationForm==null) {
				purchaseApplicationForm = purchaseApplicationFormService.add(userVo, clientOrderId);
			}
			/*StringBuffer businessKey = new StringBuffer();
			businessKey.append("PurchaseApplicationFormExcel.id.").append(clientOrderId).append(".PurchaseApplicationFormExcel");
			gyExcelService.generateExcel(businessKey.toString());*/
			message = userVo.getUserId().toString();
			success = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 跳转货款到期提醒
	 */
	@RequestMapping(value="/toDeadlineOrder",method=RequestMethod.GET)
	public String toDeadlineOrder(){
		return "/marketing/deadlineOrder/deadlineElementList";
	}
	
	/*
	 * 货款到期提醒
	 */
	@RequestMapping(value="/deadlineOrder",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo deadlineOrder(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		clientOrderElementService.getDeadLineOrderPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElementVo clientOrderElementVo : page.getEntities()) {
				clientOrderElementVo.setReceivePayRate(clientOrderElementVo.getReceivePayRate()*100);
				clientOrderElementVo.setShipPayRate(clientOrderElementVo.getShipPayRate()*100);
				clientOrderElementVo.setRate(clientOrderElementVo.getRate()*100);
				clientOrderElementVo.setRemainTotal(clientOrderElementVo.getTotal() - clientOrderElementVo.getIncomeTotal());
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElementVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("到期未付款名单", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/*
	 * 货款到期提醒
	 */
	@RequestMapping(value="/getDeadlineOrderCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getDeadlineOrderCount(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderElementVo> page=new PageModel<ClientOrderElementVo>();
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		clientOrderElementService.getDeadLineOrderPage(page, where, sort);
		String count = String.valueOf(page.getRecordCount());
		return new ResultVo(true, count);
	}
	
	/**
	 * 合同审批
	 */
	@RequestMapping(value="/orderReview",method=RequestMethod.POST)
	public @ResponseBody ResultVo orderReview(HttpServletRequest request){
		String message = "";
		boolean success = false;
//		UserVo userVo = getCurrentUser(request);
		String id=request.getParameter("id");
		 List<ClientOrderElement> clientOrderElements=clientOrderElementService.selectByForeignKey(Integer.parseInt(id));
		 for (ClientOrderElement clientOrderElement : clientOrderElements) {
//			 Double amount=clientOrderElement.getAmount();
//			 Double fixedCost=clientOrderElement.getFixedCost();
				Integer cieElementId = supplierOrderElementService.getElementId(clientOrderElement.getClientQuoteElementId());
				Integer sqeElementId = supplierOrderElementService.getSqeElementId(clientOrderElement.getClientQuoteElementId());
				List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
				List<OnPassageStorage> onpasssupplierList =new ArrayList<OnPassageStorage>();
				List<ImportPackageElementVo> elementVos=importpackageElementService.findStorageByElementId(cieElementId, sqeElementId);
				clientOrderElementService.Stock(elementVos, clientOrderElement,supplierList);
				List<SupplierListVo> onPassageList = supplierOrderElementService.getOnPassagePartNumber(cieElementId, sqeElementId);
				clientOrderElementService.onPassageStock(onPassageList, clientOrderElement, onpasssupplierList);
				
//				if(stock){//返回是true的话可以直接转库存
//					
//				}else{//否则要判断多种情况
					for (StorageFlowVo flowVo : supplierList) {
						OrderApproval orderApproval=new OrderApproval();
						orderApproval.setImportPackageElementId(flowVo.getImportPackageElementId());
						orderApproval.setSupplierQuoteElementId(flowVo.getId());
						orderApproval.setClientOrderElementId(clientOrderElement.getId());
						orderApproval.setClientOrderId(clientOrderElement.getClientOrderId());
						orderApproval.setAmount(flowVo.getStorageAmount());
						orderApproval.setPrice(flowVo.getPrice());
						if(flowVo.getProfitMargin()<0.18){
							orderApproval.setState(0);//0 利润不通过
							orderApproval.setType(0);//0 自有库存
						}else{
							orderApproval.setState(1);//0 利润通过
							orderApproval.setType(0);
						}	
						orderApprovalService.insert(orderApproval);
					}
//				}
//					for (OnPassageStorage onPassageStorage : onpasssupplierList) {
//						OrderApproval orderApproval=new OrderApproval();
//						orderApproval.setSupplierOrderElementId(onPassageStorage.getSupplierOrderElementId());
//						orderApproval.setClientOrderElementId(clientOrderElement.getId());
//						orderApproval.setClientOrderId(clientOrderElement.getClientOrderId());
//						orderApproval.setAmount(onPassageStorage.getAmount());
//						orderApproval.setPrice(onPassageStorage.getPrice());
//						if(onPassageStorage.getProfitMargin()<0.18){
//							orderApproval.setState(0);//0 利润不通过
//							orderApproval.setType(1);//0 在途库存
//						}else{
//							orderApproval.setState(1);//0 利润通过
//							orderApproval.setType(1);
//						}	
//						orderApprovalService.insert(orderApproval);
//					}
				 List<Integer> integers= clientOrderElementService.findUser(clientOrderElement.getClientOrderId());
				
				 String ids=integers.get(0).toString();
				 for (int i = 1; i < integers.size(); i++) {
					 ids+=","+integers.get(i).toString();
					}
				 clientOrderElementService.orderApproval(clientOrderElement, ids,null);
		}
		 message = "发起成功";
		 success = true;
		return new ResultVo(success, message);
	}
	
	
	


	
	/*
	 * 跳转销售修改列表页面
	 */
	@RequestMapping(value="/marketingUpdateWeatherOrder",method=RequestMethod.GET)
	public String marketingUpdateWeatherOrder(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientorder/updateweatherorder";
	}
	

	
	/*
	 * 跳转采购修改供应商价格
	 */
	@RequestMapping(value="/updateWeatherOrderPrice",method=RequestMethod.GET)
	public String updateWeatherOrderPrice(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientorder/updatesupplierprice";
	}
	

	
	/*
	 * 跳转总经理审核订单利润列表页面
	 */
	@RequestMapping(value="/finalOrderPrice",method=RequestMethod.GET)
	public String finalOrderPrice(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientorder/finalorderprice";
	}
	
	/*
	 * 跳转销售修改订单列表页面
	 */
	@RequestMapping(value="/updatefinalOrderPrice",method=RequestMethod.GET)
	public String updatefinalOrderPrice(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientorder/updatefinalorder";
	}
	
	/**
	 * 动态列
	 * **/
	@RequestMapping(value = "/list/dynamicColNames", method = RequestMethod.POST)
	public @ResponseBody
	ColumnVo excelListDynamicCol(HttpServletRequest request,
			HttpServletResponse response) {
		String clientOrderElementId= request.getParameter("clientOrderElementId");//查询条件
		String[] ids=clientOrderElementId.split(",");
		 List<String> supplierCode = new ArrayList<String>();
		for (String string : ids) {
			OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(string));
			 List<SupplierWeatherOrderElement> list=supplierWeatherOrderElementService.selectByClientOrderElementId(approval.getClientOrderElementId());
			for (SupplierWeatherOrderElement supplierWeatherOrderElement : list) {
				SupplierWeatherOrderElement supplierQuoteElement=supplierWeatherOrderElementService.selectByPrimaryKey(supplierWeatherOrderElement.getId());
				SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteElementId());
				if(!supplierCode.contains(element.getCode())){
					supplierCode.add(element.getCode());
				}
			}
		}
		
		List<String> displayNames = new ArrayList<String>();
		List<String> colNames = new ArrayList<String>();
		Collections.sort(supplierCode);
		for(int i=0;i<supplierCode.size();i++){
//			String[] codeAndPrice=supplierCode.get(i).split("-");
			displayNames.add(supplierCode.get(i));
			colNames.add(supplierCode.get(i));			
		}
		ColumnVo result = new ColumnVo();
		result.setColumnDisplayNames(displayNames);
		result.setColumnKeyNames(colNames);
		
		return result;
	}
	
	
	
	/**
	 * 检查数量是否要做预订单
	 * **/
	@RequestMapping(value="/checkAmount",  method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo checkAmount(HttpServletRequest request){
		
		boolean success = true;
		String message = "更新完成！";
		String ids=request.getParameter("ids");
		String allId=request.getParameter("id");
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		List<Integer>list=new ArrayList<Integer>();
		List<OrderApproval> deletelist=new ArrayList<OrderApproval>();
		List<OrderApproval> delete=new ArrayList<OrderApproval>();
		String[] id=ids.split(",");
		String deleteId="";
		for (String string : id) {
			OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(string));
			if(deleteId.indexOf(approval.getClientOrderElementId().toString())<0){
				List<OrderApproval> approvals=orderApprovalService.selectByCoeId(approval.getClientOrderElementId());
					 deleteId=deleteId+approval.getClientOrderElementId().toString();
					 deletelist.addAll(approvals);
			}
			 
			if(!list.contains(approval.getClientOrderElementId())){
			list.add(approval.getClientOrderElementId());
			}
			if(map.containsKey(approval.getClientOrderElementId())){
				Double amount=map.get(approval.getClientOrderElementId());
				amount=amount+approval.getAmount();
				map.put(approval.getClientOrderElementId(), amount);
			}else{
			map.put(approval.getClientOrderElementId(), approval.getAmount());
			}
		}
		for (Integer integer : list) {
			ClientOrderElement clientOrderElement=	clientOrderElementService.selectByPrimaryKey(integer);
			Double orderAmount=clientOrderElement.getAmount();
			Double amount=map.get(integer);
			if(orderAmount>amount){
				clientOrderElementService.orderApproval(clientOrderElement, "7",null);
			}
		}
		for (String string : id) {
			for (OrderApproval orderApproval : deletelist) {
				if(orderApproval.getId().toString().equals(string)){
					delete.add(orderApproval);
				}
			}
		}
		deletelist.removeAll(delete);
		for (OrderApproval orderApproval : deletelist) {
			flowService.deleteByPrimaryKey(new BigDecimal(orderApproval.getTaskId()));
		}
		
		EditRowResultVo result = new EditRowResultVo(success, message);
		return result;
		
	}
	
	/*
	 * 跳转采购降价列表页面
	 */
	@RequestMapping(value="/adjustPrice",method=RequestMethod.GET)
	public String adjustPrice(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
		request.setAttribute("history", request.getParameter("history"));
			return "/purchase/supplierquote/profitelementlist";
	}
	
	
	
	/*
	 * 采购修改供应商订单页面
	 */
	@RequestMapping(value="/updateSupplierOrderPage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo updateSupplierOrderPage(HttpServletRequest request,HttpServletResponse response) {
		PageModel<OrderApprovalVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		String taskId=request.getParameter("taskId");
		if(null!=taskId&&!"".equals(taskId)){
			page.put("taskId", taskId);
		}
		
		page.put("state", request.getParameter("state"));
		
		String ids =request.getParameter("clientOrderElementId");
//		String[] id=ids.split(",");
//		String oaId="0";
//		for (String string : id) {
//			OrderApproval approval=orderApprovalService.selectByPrimaryKey(Integer.parseInt(string));
//			oaId=oaId+","+approval.getClientOrderElementId();
//		}
//		page.put("clientOrderElementId", oaId);
		page.put("clientOrderElementId", request.getParameter("clientOrderElementId"));
		orderApprovalService.updateClientOrderPage(page);
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OrderApprovalVo orderApprovalVo : page.getEntities()) {
				SupplierWeatherOrderElement supplierQuoteElement=supplierWeatherOrderElementService.selectByPrimaryKey(orderApprovalVo.getSupplierWeatherOrderElementId());
				SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteElementId());
				orderApprovalVo.setCode(element.getCode());
				Map<String, Object> map = EntityUtil.entityToTableMap(orderApprovalVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	
	
	/*
	 * 总经理审核订单利润数据列表页面数据
	 */
	@RequestMapping(value="/finalOrderDataPage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo finalOrderDataPage(HttpServletRequest request,HttpServletResponse response) {
		PageModel<OrderApprovalVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		String taskId=request.getParameter("taskId");
		if(null!=taskId&&!"".equals(taskId)){
			page.put("taskId", taskId);
		}
		
		page.put("state", request.getParameter("state"));
		page.put("clientOrderElementId", request.getParameter("clientOrderElementId"));
		
		orderApprovalService.finalOrderPricePage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OrderApprovalVo orderApprovalVo : page.getEntities()) {
				SupplierWeatherOrderElement supplierWeatherOrderElement=supplierWeatherOrderElementService.selectByPrimaryKey(orderApprovalVo.getSupplierWeatherOrderElementId());
				SupplierWeatherOrderElement supplierQuoteElement=supplierWeatherOrderElementService.selectByPrimaryKey(supplierWeatherOrderElement.getId());
				SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteElementId());
				Double sqePrice=supplierQuoteElement.getPrice()*element.getExchangeRate()/orderApprovalVo.getExchangeRate();
				Double price=new BigDecimal(sqePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				orderApprovalVo.setPrice(price);
				Double orderPrice=orderApprovalVo.getOrderPrice();
				Double fixedCost=orderApprovalVo.getFixedCost();
				 if(fixedCost<1){
					 fixedCost=fixedCost*orderPrice;
				 }
				if(orderPrice>0){
					Double profitMargin=((orderPrice-fixedCost-price)/orderPrice)*100;
					orderApprovalVo.setProfitMargin(profitMargin);
				}
			
				orderApprovalVo.setClientProfitMargin(18.0);
				Map<String, Object> map = EntityUtil.entityToTableMap(orderApprovalVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	/**
	 * 新增供应商预订单
	 * **/
	@RequestMapping(value="/addSupplierWeatherOrder",  method=RequestMethod.POST)
	public @ResponseBody ResultVo addSupplierWeatherOrder(HttpServletRequest request,
			@ModelAttribute SupplierWeatherOrderElement record)
	{
		boolean success = true;
		String message = "新增完成！";
//		boolean in=false;
		try {
//			List<OrderApproval> nopassStock=orderApprovalService.selectByCoeIdAndState(record.getClientOrderElementId(), 0, 0);
//			List<SupplierWeatherOrderElement> elements=supplierWeatherOrderElementService.selectByClientOrderElementId(record.getClientOrderElementId());
			Jbpm4Task jbpm4Task =new Jbpm4Task();
			jbpm4Task.setYwTableElementId(record.getClientOrderElementId());
			jbpm4Task.setTaskdefname("采购生成供应商预订单");
			List<Jbpm4Task>  jbpm4Tasks=flowService.selectWeatherOrder(jbpm4Task);
			record.setSupplierStatus(1);
			ClientWeatherOrderElement clientWeatherOrderElement=clientWeatherOrderElementService.selectByPrimaryKey(record.getClientOrderElementId());
			Double amount = supplierWeatherOrderElementService.getAmountByClientOrder(clientWeatherOrderElement.getId());
			if (amount != null) {
				if (clientWeatherOrderElement.getAmount() > amount) {
					supplierWeatherOrderElementService.insert(record);
					for (Jbpm4Task jbpm4Task2 : jbpm4Tasks) {
						OrderApproval orderApproval=new OrderApproval();
						orderApproval.setId(jbpm4Task2.getRelationId());
						orderApproval.setTaskId(jbpm4Task2.getId());
						orderApproval.setSupplierWeatherOrderElementId(record.getId());
						orderApprovalService.updateByPrimaryKeySelective(orderApproval);
						break;
					}
					if(jbpm4Tasks.size()<1){
						
						OrderApproval orderApproval=new OrderApproval();
						orderApproval.setClientOrderElementId(clientWeatherOrderElement.getId());
						Jbpm4Task task=	flowService.selectDbversion(jbpm4Task);
						if(task.getDescr().indexOf("不使用")>-1){
							orderApproval.setHandle("有库存");
						}else{
							orderApproval.setHandle("无库存");
						}
						Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findGyJbyjByBusinessKeyAndOutcome("ORDER_APPROVAL.ID."+task.getRelationId(), "发起");
						orderApproval.setDesc(jbpm4Jbyj.getUserName()+"发起【合同审批】等待您的处理!");
						orderApproval.setUserId(jbpm4Jbyj.getUserId());
						contractReviewService.orderApproval(clientWeatherOrderElement, ContextHolder.getCurrentUser().getUserId(),orderApproval);
						
						if(orderApproval.getHandle().equals("有库存")){
							flowService.completeTask(orderApproval.getTaskId(), "不使用", "", "","", "", "","/market/clientweatherorder/purchaseConfirmProfit?clientOrderElementId="+orderApproval.getId(),orderApproval.getTaskId());
//							Jbpm4Task task2=flowService.selectByExecutionId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
//							flowService.completeTask(task2.getId(), "不使用",  ContextHolder.getCurrentUser().getUserId(), "","", "", "","",task2.getId());
						}
						
						Jbpm4Task task2=flowService.selectByExecutionId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
						task2.setDescr(task.getDescr());
						flowService.updateByPrimaryKeySelective(task2);
						orderApproval.setSupplierWeatherOrderElementId(record.getId());
						orderApprovalService.updateByPrimaryKeySelective(orderApproval);
						
						List<Jbpm4Jbyj> jbpm4Jbyjs=flowService.selectByYwTableElementId(record.getClientOrderElementId().toString(),"ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
						for (int i = 0; i < jbpm4Jbyjs.size(); i++) {
								Jbpm4Jbyj gyJbyj=new Jbpm4Jbyj();
								gyJbyj.setProcessinstanceId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
								gyJbyj.setUserId(jbpm4Jbyjs.get(i).getUserId());
								gyJbyj.setUserName(jbpm4Jbyjs.get(i).getUserName());
								gyJbyj.setTaskName(jbpm4Jbyjs.get(i).getTaskName());
								flowService.updateBytaskName(gyJbyj);
						}
					}
				}else{
					success = false;
					message = "已经满足下单条件，请检查完数据后提交审核";
				}
			}else {
				supplierWeatherOrderElementService.insert(record);
				for (Jbpm4Task jbpm4Task2 : jbpm4Tasks) {
					OrderApproval orderApproval=new OrderApproval();
					orderApproval.setId(jbpm4Task2.getRelationId());
					orderApproval.setTaskId(jbpm4Task2.getId());
					orderApproval.setSupplierWeatherOrderElementId(record.getId());
					orderApprovalService.updateByPrimaryKeySelective(orderApproval);
					break;
				}
				if(jbpm4Tasks.size()<1){
					
					OrderApproval orderApproval=new OrderApproval();
					orderApproval.setClientOrderElementId(clientWeatherOrderElement.getId());
					Jbpm4Task task=	flowService.selectDbversion(jbpm4Task);
					if(task.getDescr().indexOf("不使用")>-1){
						orderApproval.setHandle("有库存");
					}else{
						orderApproval.setHandle("无库存");
					}
					Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findGyJbyjByBusinessKeyAndOutcome("ORDER_APPROVAL.ID."+task.getRelationId(), "发起");
					orderApproval.setDesc(jbpm4Jbyj.getUserName()+"发起【合同审批】等待您的处理!");
					orderApproval.setUserId(jbpm4Jbyj.getUserId());
					contractReviewService.orderApproval(clientWeatherOrderElement, ContextHolder.getCurrentUser().getUserId(),orderApproval);
					
					if(orderApproval.getHandle().equals("有库存")){
						flowService.completeTask(orderApproval.getTaskId(), "不使用", "", "","", "", "","/market/clientweatherorder/purchaseConfirmProfit?clientOrderElementId="+orderApproval.getId(),orderApproval.getTaskId());
//						Jbpm4Task task2=flowService.selectByExecutionId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
//						flowService.completeTask(task2.getId(), "不使用",  ContextHolder.getCurrentUser().getUserId(), "","", "", "","",task2.getId());
					}
					
					Jbpm4Task task2=flowService.selectByExecutionId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
					task2.setDescr(task.getDescr());
					flowService.updateByPrimaryKeySelective(task2);
					orderApproval.setSupplierWeatherOrderElementId(record.getId());
					orderApprovalService.updateByPrimaryKeySelective(orderApproval);
					
					List<Jbpm4Jbyj> jbpm4Jbyjs=flowService.selectByYwTableElementId(record.getClientOrderElementId().toString(),"ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
					for (int i = 0; i < jbpm4Jbyjs.size(); i++) {
							Jbpm4Jbyj gyJbyj=new Jbpm4Jbyj();
							gyJbyj.setProcessinstanceId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
							gyJbyj.setUserId(jbpm4Jbyjs.get(i).getUserId());
							gyJbyj.setUserName(jbpm4Jbyjs.get(i).getUserName());
							gyJbyj.setTaskName(jbpm4Jbyjs.get(i).getTaskName());
							flowService.updateBytaskName(gyJbyj);
					}
					
					
				}
			}
			
			
			
//			for (OrderApproval orderApproval : nopassStock) {
//				if(elements.size()>=1){
//					if(null==orderApproval.getTaskId()){
//						Jbpm4Task jbpm4Task=flowService.selectByExecutionId("ContractreviewProcess.ORDER_APPROVAL.ID."+orderApproval.getId());
//						orderApproval.setTaskId(jbpm4Task.getId());
//						orderApproval.setSupplierWeatherOrderElementId(record.getId());
//						orderApprovalService.updateByPrimaryKeySelective(orderApproval);
//						in=true;
//						break;
//					}
//				}else{
//					if(null!=orderApproval.getTaskId()&&null==orderApproval.getSupplierWeatherOrderElementId()){
//						orderApproval.setSupplierWeatherOrderElementId(record.getId());
//						orderApprovalService.updateByPrimaryKeySelective(orderApproval);
//						in=true;
//						break;
//					}
//				}
//			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "新增失败";
		}
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 修改供应商预订单
	 * **/
	@RequestMapping(value="/updateSupplierWeatherOrder",  method=RequestMethod.POST)
	public @ResponseBody ResultVo updateSupplierWeatherOrder(HttpServletRequest request,
			@ModelAttribute SupplierWeatherOrderElement record)
	{
		boolean success = true;
		String message = "修改完成！";
		String id=request.getParameter("supplierWeatherOrderElementId");
		try {
			record.setId(Integer.parseInt(id));
			supplierWeatherOrderElementService.updateByPrimaryKey(record);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "修改失败";
		}
		return new ResultVo(success, message);
		
	}
	
	/*
	 * 采购修改订单明细
	 */
	@RequestMapping(value="/updateSupplierPrice",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateSupplierPrice(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String a = getString(request, "supplierWeatherOrderElementId");
		if(a!=null && !"".equals(a)){
			SupplierWeatherOrderElement element = new SupplierWeatherOrderElement();
			element.setId(new Integer(a));
			element.setAmount(new Double(getString(request, "amount")));
			element.setPrice(new Double(getString(request, "price")));
			supplierWeatherOrderElementService.updateByPrimaryKeySelective(element);
			
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		return new EditRowResultVo(success, message);
	}
	
	
	
	/*
	 * 销售修改订单明细
	 */
	@RequestMapping(value="/updateFinalElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateFinalElement(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String a = getString(request, "clientOrderElementId");
		if(a!=null && !"".equals(a)){
			ClientOrderElementFinal clientOrderElement = new ClientOrderElementFinal();
			clientOrderElement.setId(new Integer(a));
			clientOrderElement.setAmount(new Double(getString(request, "orderAmount")));
			clientOrderElement.setPrice(new Double(getString(request, "orderPrice")));
			clientOrderElement.setOrderStatusId(Integer.parseInt(request.getParameter("orderStatusValue")));
			clientOrderElementFinalService.updateByPrimaryKeySelective(clientOrderElement);
			
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 文件上传修改预订单
	 */
	@RequestMapping(value="/updateWeatherOrderuploadExcel",method=RequestMethod.POST)
	public @ResponseBody String updateWeatherOrderuploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		String clientOrderId=request.getParameter("clientOrderId");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		MessageVo messageVo = supplierWeatherOrderElementService.updateuploadExcel(multipartFile, clientOrderId);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * 订单完成录入
	 */
	@RequestMapping(value="/finshOrder",method=RequestMethod.POST)
	public @ResponseBody ResultVo finshOrder(HttpServletRequest request,HttpServletResponse response) {
		boolean success=true;
		String message="完成";
		Double bankCharges=0.0;
		String clientOrderId=request.getParameter("clientOrderId");
		String clientId=request.getParameter("clientId");
		 Double smPrice=clientOrderElementService.sumPrice(Integer.parseInt(clientOrderId));
		  List<OrderBankCharges> list=orderBankChargesService.orderBankChargesByClientId(clientId);
		  for (OrderBankCharges orderBankCharges : list) {
			  if(null==orderBankCharges.getOrderPriceAbove()&&smPrice<orderBankCharges.getOrderPriceFollowing()){
				  bankCharges=orderBankCharges.getBankCharges();  break;
			  }else
			  if(null==orderBankCharges.getOrderPriceFollowing()&&smPrice>orderBankCharges.getOrderPriceAbove()){
				  bankCharges=orderBankCharges.getBankCharges();  break;
			  }else
			  if(smPrice>orderBankCharges.getOrderPriceAbove()&&smPrice<orderBankCharges.getOrderPriceFollowing()){
				  bankCharges=orderBankCharges.getBankCharges();  break;
			  }
		}
		  ClientOrder clientOrder=new ClientOrder();
		  clientOrder.setComplete(1);
		  clientOrder.setId(Integer.parseInt(clientOrderId));
		  clientOrderService.updateByPrimaryKeySelective(clientOrder);
		  ClientOrderElement clientOrderElement=new ClientOrderElement();
		  clientOrderElement.setClientOrderId(Integer.parseInt(clientOrderId));
		  clientOrderElement.setBankCharges(bankCharges);
		  clientOrderElementService.updateBybankCharges(clientOrderElement);
		
		return new ResultVo(success, message);
	}
	
	
	/**
	 * 通过实体类获取页面传的时间值可以绑定，自动转化
	 * 
	 * @return
	 * @since 2016年5月7日 下午13:34
	 * @author giam<giam@naswork.com>
	 * @version v1.0
	 * @param binder
	 */
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	    dateFormat.setLenient(false);  
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));  
	}
}
