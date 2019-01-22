package com.naswork.module.marketing.controller.clientweatherorder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.mail.smime.handlers.x_pkcs7_mime;
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
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.dao.SupplierInquiryDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientOrderElementFinal;
import com.naswork.model.ClientProfitmargin;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.ExchangeRate;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.Jbpm4Task;
import com.naswork.model.OrderApproval;
import com.naswork.model.StorageToOrderEmail;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierWeatherOrder;
import com.naswork.model.SupplierWeatherOrderElement;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.task.controller.orderapproval.OrderApprovalVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientOrderElementFinalService;
import com.naswork.service.ClientOrderElementNotmatchService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientProfitmarginService;
import com.naswork.service.ClientQuoteElementService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.ClientWeatherOrderElementService;
import com.naswork.service.ClientWeatherOrderService;
import com.naswork.service.ExchangeRateService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.service.OrderApprovalService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.service.SupplierService;
import com.naswork.service.SupplierWeatherOrderElementService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/market/clientweatherorder")
public class ClientWeatherOrderController extends BaseController {
	
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private ClientWeatherOrderService clientWeatherOrderService;
	@Resource
	private ClientWeatherOrderElementService clientWeatherOrderElementService;
	@Resource
	private UserService userService;
	@Resource
	private OrderApprovalService orderApprovalService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	@Resource
	private SupplierWeatherOrderElementService supplierWeatherOrderElementService;
	@Resource
	private Jbpm4JbyjService jbpm4JbyjService;
	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private ClientOrderElementFinalService clientOrderElementFinalService;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private ClientQuoteElementService clientQuoteElementService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private ClientOrderElementNotmatchService clientOrderElementNotmatchService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ClientProfitmarginService clientProfitmarginService;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private ExchangeRateService exchangeRateService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientService clientService;
	
	
	/**
	 * 新增客户订单页面
	 * **/
	@RequestMapping(value="/addclientorder",method=RequestMethod.GET)
	public String addclientorder(HttpServletRequest request){
		request.setAttribute("client_quote_id", request.getParameter("client_quote_id"));
		request.setAttribute("client_inquiry_quote_number", request.getParameter("client_inquiry_quote_number"));
		String id=request.getParameter("client_quote_id");
		ClientQuote record=clientQuoteService.selectByPrimaryKey(Integer.valueOf(id));
		record.setPrepayRate(record.getPrepayRate()*100);
		record.setShipPayRate(record.getShipPayRate()*100);
		record.setReceivePayRate(record.getReceivePayRate()*100);
		request.setAttribute("record", record);
		return "/marketing/clientweatherorder/addclientorder";
	}
	
	/**
	 * 保存新增数据
	 */
	@RequestMapping(value="/save",  method=RequestMethod.POST)
	public @ResponseBody ResultVo save(HttpServletRequest request, @ModelAttribute ClientWeatherOrder clientWeatherOrder)
	{
		boolean result = true;
		String message = "新增成功！";
		String clientinquiryquotenumber=request.getParameter("clientinquiryquotenumber"); 
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		int id=clientWeatherOrder.getClientQuoteId();
		Integer seq=clientWeatherOrderService.findseq(id);
		int maxSeq;
		if(seq==null){
			maxSeq=0;
		}else{
			maxSeq=seq;
		}
		clientWeatherOrder.setSeq(++maxSeq);

		String orderNumber=clientinquiryquotenumber;
		if(maxSeq>1){
			orderNumber = orderNumber + "-" +maxSeq;
		}
		orderNumber = "ORD-" + orderNumber;//订单号组装
		clientWeatherOrder.setOrderNumber(orderNumber);
		clientWeatherOrder.setOrderStatusId(60);
		clientWeatherOrder.setPrepayRate(clientWeatherOrder.getPrepayRate()/100);
		clientWeatherOrder.setShipPayRate(clientWeatherOrder.getShipPayRate()/100);
		clientWeatherOrder.setReceivePayRate(clientWeatherOrder.getReceivePayRate()/100);
		clientWeatherOrder.setCreateUserId(Integer.parseInt(userId));
		clientWeatherOrderService.insert(clientWeatherOrder);
		return new ResultVo(result, message);
	}
	
	/*
	 * 跳转列表页面
	 */
	@RequestMapping(value="/tolist",method=RequestMethod.GET)
	public String tolist(HttpServletRequest request) {
		UserVo userVo=getCurrentUser(request);
		request.setAttribute("userId", userVo.getUserId());
			return "/marketing/clientweatherorder/clientweatherorderList";
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
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		clientWeatherOrderService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderVo clientOrderVo : page.getEntities()) {
				clientOrderVo.setPrepayRate(clientOrderVo.getPrepayRate()*100);
				clientOrderVo.setReceivePayRate(clientOrderVo.getReceivePayRate()*100);
				clientOrderVo.setShipPayRate(clientOrderVo.getShipPayRate()*100);
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
	 * 明细列表数据
	 */
	@RequestMapping(value="/elementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		Integer id = new Integer(getString(request, "id"));
		page.put("id", id);
		
		clientWeatherOrderElementService.findByOrderIdPage(page,sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElementVo clientOrderElemenrVo : page.getEntities()) {
				BigDecimal sqePrice=new BigDecimal(clientOrderElemenrVo.getSupplierQuotePrice());
				BigDecimal sqER=new BigDecimal(clientOrderElemenrVo.getSupplierQuoteExchangeRate());
				BigDecimal er=new BigDecimal(clientOrderElemenrVo.getExchangeRate());
				ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientOrderElemenrVo.getClientQuoteElementId());
				ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientQuoteElement.getClientQuoteId());
				ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
				Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
				ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
				if (!clientOrderElemenrVo.getSupplierQuoteCurrencyId().equals(clientOrderElemenrVo.getCurrencyId())) {
					//判断客户是否7字头
					if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
						if (!clientOrderElemenrVo.getSupplierQuoteCurrencyId().equals(usRate.getCurrencyId())) {
							ExchangeRate currenrRate = exchangeRateService.selectByPrimaryKey(clientOrderElemenrVo.getSupplierQuoteCurrencyId());
							//兑换成美元的比例
							Double relative = new BigDecimal(currenrRate.getRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
							relative = relative * usRate.getRelativeRate();
							sqePrice = clientQuoteService.caculatePrice(sqePrice, new BigDecimal(relative),er);
						}else {
							sqePrice = clientQuoteService.caculatePrice(sqePrice, new BigDecimal(clientOrderElemenrVo.getSupplierQuoteExchangeRate()),er);
						}
					}else {
						sqePrice = clientQuoteService.caculatePrice(sqePrice, new BigDecimal(clientOrderElemenrVo.getSupplierQuoteExchangeRate()),er);
					}
					
				}
				//sqePrice = clientQuoteService.caculatePrice(sqePrice, sqER,er);
				clientOrderElemenrVo.setSupplierQuotePrice(sqePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				List<ClientWeatherOrderElement> list = clientWeatherOrderElementService.getRealPrice(clientOrderElemenrVo.getId());
				StringBuffer codes = new StringBuffer();
				StringBuffer prices = new StringBuffer();
				for (ClientWeatherOrderElement clientWeatherOrderElement : list) {
					codes.append(clientWeatherOrderElement.getSupplierCode()).append(",");
					BigDecimal price=new BigDecimal(clientWeatherOrderElement.getPrice());
					BigDecimal supplierExchangeRate=new BigDecimal(clientWeatherOrderElement.getSupplierExchangeRate());
					sqePrice = clientQuoteService.caculatePrice(price, supplierExchangeRate,er);
					//clientOrderElemenrVo.setSupplierQuotePrice(sqePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					if (!clientWeatherOrderElement.getSupplierCurrencyId().equals(clientOrderElemenrVo.getCurrencyId())) {
						//判断客户是否7字头
						if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
							if (!clientWeatherOrderElement.getSupplierCurrencyId().equals(usRate.getCurrencyId())) {
								ExchangeRate currenrRate = exchangeRateService.selectByPrimaryKey(clientWeatherOrderElement.getSupplierCurrencyId());
								//兑换成美元的比例
								Double relative = new BigDecimal(currenrRate.getRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
								relative = relative * usRate.getRelativeRate();
								BigDecimal realSupplierPrice = clientQuoteService.caculatePrice(new BigDecimal(clientWeatherOrderElement.getPrice()), new BigDecimal(relative),er);
								prices.append(realSupplierPrice).append(",");
							}else {
								BigDecimal realSupplierPrice = clientQuoteService.caculatePrice(new BigDecimal(clientWeatherOrderElement.getPrice()), new BigDecimal(clientQuoteElement.getRelativeRate()),er);
								prices.append(realSupplierPrice).append(",");
							}
						}else {
							BigDecimal realSupplierPrice = clientQuoteService.caculatePrice(new BigDecimal(clientWeatherOrderElement.getPrice()), supplierExchangeRate,er);
							prices.append(realSupplierPrice).append(",");
						}
					}else {
						prices.append(clientWeatherOrderElement.getPrice()).append(",");
					}
					
				}
				if (codes.length() > 0) {
					codes.deleteCharAt(codes.length()-1);
				}
				if (prices.length() > 0) {
					prices.deleteCharAt(prices.length()-1);
					clientOrderElemenrVo.setRealSupplierCode(codes.toString());
					clientOrderElemenrVo.setRealSupplierQuotePrice(prices.toString());
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
	 * 新增订单明细页面
	 */
	@RequestMapping(value="/addOrder",method=RequestMethod.GET)
	public String addOrder(HttpServletRequest request) {
		PageModel<ClientOrderElementVo> page=getPage(request);
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		List<ClientOrderElementVo> orderList = clientWeatherOrderElementService.findByOrderId(clientOrderId);
		ClientOrderVo clientOrderVo = clientWeatherOrderService.findById(clientOrderId);
		page.put("id", clientOrderId);
		List<ClientOrderElementVo> elementList = clientWeatherOrderElementService.elementList(clientOrderVo.getClientQuoteId());
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
		return "/marketing/clientweatherorder/addOrderElementTable";
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
			//Integer leadtime = new Integer(clientOrderVo.g)
			//clientOrderVo.set
			clientWeatherOrderElementService.insertSelective(clientOrderVo.getList(),clientOrderId,userVo.getUserId());
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		return new ResultVo(success,message);
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
		MessageVo messageVo = clientWeatherOrderElementService.UploadExcel(multipartFile,clientOrderId,new Integer(userVo.getUserId()));
		
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/*
	 * 修改页面
	 */
	@RequestMapping(value="/clientorderEdit",method=RequestMethod.GET)
	public String clientorderEdit(HttpServletRequest request) {
		Integer id =new Integer(getString(request, "id"));
		ClientOrderVo clientOrderVo = clientWeatherOrderService.findById(id);
		clientOrderVo.setPrepayRate(clientOrderVo.getPrepayRate()*100);
		clientOrderVo.setReceivePayRate(clientOrderVo.getReceivePayRate()*100);
		clientOrderVo.setShipPayRate(clientOrderVo.getShipPayRate()*100);
		request.setAttribute("clientOrderVo", clientOrderVo);
		return "/marketing/clientweatherorder/clientorderEdit";
	}
	
	/*
	 * 修改订单
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public @ResponseBody ResultVo Edit(HttpServletRequest request, @ModelAttribute ClientWeatherOrder clientWeatherOrder) {
		boolean success = false;
		String msg = "";
		UserVo userVo = getCurrentUser(request);
		
		if (clientWeatherOrder.getId()!=null) {
			clientWeatherOrder.setUpdateTimestamp(new Date());
			clientWeatherOrder.setCreateUserId(new Integer(userVo.getUserId()));
			clientWeatherOrderService.updateByPrimaryKeySelective(clientWeatherOrder);
			success=true;
			msg="修改成功！";
		}else {
			msg="修改失败！";
		}
		return new ResultVo(success, msg);
	}
	
	/*
	 * 作废明细
	 */
	@RequestMapping(value="/cancelOrderElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo cancelOrderElement(HttpServletRequest request) {
		try {
			String ids = getString(request, "ids");
			clientWeatherOrderService.cancelElement(ids);
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改成功！");
		}
	}
	
	/*
	 * 修改订单明细
	 */
	@RequestMapping(value="/editElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editElement(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String a = getString(request, "id");
		if(a!=null && !"".equals(a)){
			ClientWeatherOrder clientWeatherOrder = clientWeatherOrderService.getClientOrder(new Integer(a));
			ClientWeatherOrderElement clientOrderElement = new ClientWeatherOrderElement();
			clientOrderElement.setId(new Integer(a));
			clientOrderElement.setAmount(new Double(getString(request, "clientOrderAmount")));
			clientOrderElement.setPrice(new Double(getString(request, "clientOrderPrice")));
			String leadTime = getString(request, "clientOrderLeadTime");
			String partNumber = getString(request, "inquiryPartNumber");
			String unit = getString(request, "unit");
			String description = getString(request, "inquiryDescription");
			clientOrderElement.setLeadTime(leadTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(clientWeatherOrder.getOrderDate());
			calendar.add(Calendar.DATE, new Integer(leadTime));
			clientOrderElement.setDeadline(calendar.getTime());
			clientOrderElement.setCertificationId(Integer.parseInt(request.getParameter("certificationCode")));
			String fixedCost=request.getParameter("fixedCost");
			if(null!=fixedCost&&!"".equals(fixedCost)){
				clientOrderElement.setFixedCost(new Double(fixedCost));
			}
			if (partNumber != null && !"".equals(partNumber)) {
				clientOrderElement.setPartNumber(partNumber);
			}
			if (unit != null && !"".equals(unit)) {
				clientOrderElement.setUnit(unit);
			}
			if (description != null && !"".equals(description)) {
				clientOrderElement.setDescription(description);
			}
			clientOrderElement.setRemark(request.getParameter("remark"));
			clientWeatherOrderElementService.updateByPrimaryKeySelective(clientOrderElement);
			
			
			
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		return new EditRowResultVo(success, message);
	}
	
	/*
	 * 跳转采购上传供应商预订单列表页面
	 */
	@RequestMapping(value="/purchaseAddSupplierWeatherOrder",method=RequestMethod.GET)
	public String purchaseAddSupplierWeatherOrder(HttpServletRequest request) {
		
		String[] ids=request.getParameter("clientOrderElementId").split(",");
		//OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		//ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		String his = request.getParameter("history");
		OrderApproval approval = new OrderApproval();
		if (his != null && "yes".equals(his)) {
			approval=	orderApprovalService.selectByPrimaryKey(new Integer(request.getParameter("clientOrderElementId")));
			request.setAttribute("clientOrderElementId", approval.getClientOrderElementId());
		}else {
			approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
			request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
		}
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
		UserVo userVo = getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
 		List<Integer> userList = userService.getLeadersByRole(new Integer(roleVo.getRoleId()));
 		boolean isLeader = false;
 		if (userList.size() > 0 && userList.get(0) != null) {
 			for (int i = 0; i < userList.size(); i++) {
 				if (userList.get(i).toString().equals(userVo.getUserId())) {
 					isLeader = true;
 					break;
 				}
 			}
		}
 		request.setAttribute("taskdefname", request.getParameter("type"));
 		/*if (isLeader) {
 			request.setAttribute("taskdefname", "check");
		}else {
			request.setAttribute("taskdefname", "add");
		}*/
		
		return "/marketing/clientweatherorder/addsupplierweatherorderelement";
	}
	
	/*
	 * 跳转采购降价列表页面
	 */
	@RequestMapping(value="/thePrice",method=RequestMethod.GET)
	public String thePrice(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
		/*String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());*/
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
		request.setAttribute("taskdefname", "update");
			return "/marketing/clientweatherorder/thepricelist";
	}
	
	/*
	 * 采购生成供应商预订单页面
	 */
	@RequestMapping(value="/supplierWeatherOrderDate",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierWeatherOrderDate(HttpServletRequest request,HttpServletResponse response) {
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
		
		String elementId =request.getParameter("clientOrderElementId");
		String idsForTask = "";
		if (elementId != null) {
			List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(elementId);
			if (listTask.size() > 0) {
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
			    idsForTask=jbpm4Tasks.get(0).getRelationId().toString();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单") || jbpm4Tasks.get(i).getTaskdefname().equals("部门领导查看")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	 idsForTask+=","+jbpm4Tasks.get(i).getRelationId();
				}
			}
		}
		String[] id=idsForTask.split(",");
		String oaId="0";
		String his = getString(request, "history");
		if (his != null && !"".equals(his)) {
			oaId = elementId;
		}else {
			for (String string : id) {
				OrderApproval approval=orderApprovalService.selectByPrimaryKey(Integer.parseInt(string));
				oaId=oaId+","+approval.getClientOrderElementId();
			}
		}
		String taskdefname=request.getParameter("taskdefname");
		if(taskdefname.equals("add")){
			taskdefname="采购生成供应商预订单";
		}else if(taskdefname.equals("check")){
			taskdefname="部门领导查看";
		}else if(taskdefname.equals("update")){
			taskdefname="采购询问供应商能否降价";
		}
		page.put("taskdefname", taskdefname);
		page.put("clientOrderElementId", oaId);
//		page.put("clientOrderElementId", request.getParameter("clientOrderElementId"));
		orderApprovalService.supplierWeayherOrderPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OrderApprovalVo orderApprovalVo : page.getEntities()) {
			   List<OrderApproval> passStock=orderApprovalService.selectByCoeIdAndState(orderApprovalVo.getClientOrderElementId(), 1, 0);
			   List<OrderApproval> onpassPassStock=orderApprovalService.selectByCoeIdAndState(orderApprovalVo.getClientOrderElementId(), 1, 1);
			   Double storageAmount=0.0;
			   Double onpassStorageAmount=0.0;
			   for (OrderApproval orderApproval : passStock) {//库存订单数量
					if(orderApproval.getOccupy()==1||orderApproval.getOccupy().equals(1)){
						storageAmount=storageAmount+orderApproval.getAmount();
					}
			   }
			   for (OrderApproval orderApproval : onpassPassStock) {//在途库存数量
				   if(orderApproval.getOccupy()==1||orderApproval.getOccupy().equals(1)){
						onpassStorageAmount=onpassStorageAmount+orderApproval.getAmount();
					}
			   }
			   orderApprovalVo.setOnpassStorageAmount(onpassStorageAmount);
			   orderApprovalVo.setStorageAmount(storageAmount);
			   
			   ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(orderApprovalVo.getClientOrderElementId());
				Double quotePrice=clientOrderElementVo.getSupplierQuotePrice();
				orderApprovalVo.setQuotePrice(quotePrice);
				SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(clientOrderElementVo.getSupplierQuoteElementId());
				orderApprovalVo.setCode(element.getCode());
				DecimalFormat df = new DecimalFormat("######0.00");
				if(null!=orderApprovalVo.getSupplierQuoteElementId()){
					SupplierQuoteElement weatherorder=supplierQuoteElementService.selectByPrimaryKey(orderApprovalVo.getSupplierQuoteElementId());
					orderApprovalVo.setCertificationCode(weatherorder.getCertificationCode());
					orderApprovalVo.setConditionCode(weatherorder.getConditionCode());
					orderApprovalVo.setLocation(weatherorder.getLocation());
					orderApprovalVo.setSupplierCode(weatherorder.getCode());
					SupplierWeatherOrder supplierWeatherOrder = supplierWeatherOrderElementService.getByOrderIdAndSupplier(orderApprovalVo.getClientWeatherOrderId(), weatherorder.getSupplierId());
					//订单额外费用
//					if (supplierWeatherOrder != null) {
//						
//						Double orderAmount = supplierWeatherOrderElementService.getAmontBySupplier(supplierWeatherOrder.getClientWeatherOrderId(),weatherorder.getSupplierId());
//						Double feeForExchangeBill = 0.0;
//						Double bankCharges = 0.0;
//						Double otherFee = 0.0;
//						if (supplierWeatherOrder.getBankCost() != null || supplierWeatherOrder.getFeeForExchangeBill() != null || supplierWeatherOrder.getOtherFee() != null) {
//							if (orderAmount != null) {
//								if (supplierWeatherOrder.getBankCost() != null) {
//									bankCharges = new Double(df.format(supplierWeatherOrder.getBankCost()/orderAmount*supplierWeatherOrderElement.getAmount()));
//								}
//								if (supplierWeatherOrder.getFeeForExchangeBill() != null) {
//									feeForExchangeBill = new Double(df.format(supplierWeatherOrder.getFeeForExchangeBill()/orderAmount*supplierWeatherOrderElement.getAmount()));
//								}
//								if (supplierWeatherOrder.getOtherFee() != null) {
//									otherFee = new Double(df.format(supplierWeatherOrder.getOtherFee()/orderAmount*supplierWeatherOrderElement.getAmount()));
//								}
//							 } 
//						}
//						if (supplierWeatherOrder.getBankCost() != null && !"".equals(supplierWeatherOrder.getBankCost())) {
//							orderApprovalVo.setBankCost(supplierWeatherOrder.getBankCost());
//						}
//						if (supplierWeatherOrder.getFeeForExchangeBill() != null && !"".equals(supplierWeatherOrder.getFeeForExchangeBill())) {
//							orderApprovalVo.setFeeForExchangeBill(supplierWeatherOrder.getFeeForExchangeBill());				
//						}
//						if (supplierWeatherOrder.getOtherFee() != null && !"".equals(supplierWeatherOrder.getOtherFee())) {
//							orderApprovalVo.setOtherFee(supplierWeatherOrder.getOtherFee());
//						}
//					}
				}
//				//报价的额外费用
//				ClientWeatherOrderElement clientWeatherOrderElement = clientWeatherOrderElementService.selectByPrimaryKey(orderApprovalVo.getClientOrderElementId());
//				ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
//				SupplierQuoteElement supplierQuoteElement = supplierQuoteElementService.selectByPrimaryKey(clientQuoteElement.getSupplierQuoteElementId());
//				SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteId());
//				Double quoteAmount = supplierQuoteElementService.getCountByQuoteId(supplierQuote.getId());
//				Double feeForExchangeBill = null;
//				Double bankCharges = null;
//				if (supplierQuote.getBankCost() != null || supplierQuote.getFeeForExchangeBill() != null) {
//					 if (quoteAmount != null) {
//						if (supplierQuote.getBankCost() != null) {
//							bankCharges = new Double(df.format(supplierQuote.getBankCost()/quoteAmount*supplierQuoteElement.getAmount()));
//						}
//						if (supplierQuote.getFeeForExchangeBill() != null) {
//							feeForExchangeBill = new Double(df.format(supplierQuote.getFeeForExchangeBill()/quoteAmount*supplierQuoteElement.getAmount()));
//						}
//					 } 
//				 }
//				 if (bankCharges == null) {
//					 bankCharges = 0.0;
//				 }
//				 if (feeForExchangeBill == null) {
//					 feeForExchangeBill = 0.0;
//				 }
//				 orderApprovalVo.setQuoteBankCost(bankCharges);
//				 orderApprovalVo.setQuoteFeeForExchangeBill(feeForExchangeBill);
				
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
	
	
	/*
	 * 筛选已下单的订单明细
	 */
	@RequestMapping(value="/hasSupplierOrderTask",method=RequestMethod.POST)
	public @ResponseBody ResultVo hasSupplierOrderTask(HttpServletRequest request,HttpServletResponse response) {
		try {
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
			
			//String ids =request.getParameter("clientOrderElementId");
			
			String elementId =request.getParameter("clientOrderElementId");
			String idsForTask = "";
			if (elementId != null) {
				List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(elementId);
				if (listTask.size() > 0) {
					List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
				    idsForTask=jbpm4Tasks.get(0).getRelationId().toString();
				    for (int i = 1; i < jbpm4Tasks.size(); i++) {
				    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
				    	 if(list.size()>1){
				    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
				    			 continue;
							 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
								 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
								 for (Jbpm4Task jbpm4Task2 : list1) {
									if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
										continue;
									}
								}
							 }
				    		 ;
				    	 }
				    	 idsForTask+=","+jbpm4Tasks.get(i).getRelationId();
					}
				}
			}
			
			String[] id=idsForTask.split(",");
			String oaId="0";
			for (String string : id) {
				OrderApproval approval=orderApprovalService.selectByPrimaryKey(Integer.parseInt(string));
				oaId=oaId+","+approval.getClientOrderElementId();
			}
			String taskdefname=request.getParameter("taskdefname");
			if(taskdefname.equals("add")){
				taskdefname="采购生成供应商预订单";
			}else if(taskdefname.equals("update")){
				taskdefname="采购询问供应商能否降价";
			}
			page.put("taskdefname", taskdefname);
			page.put("clientOrderElementId", oaId);
			List<Integer> orderApprovalIds = orderApprovalService.supplierWeayherOrder(page);
			StringBuffer str = new StringBuffer();
			for (Integer integer : orderApprovalIds) {
				if (str.length() == 0) {
					str.append(integer);
				}else {
					str.append(",").append(integer);
				}
			}
			
			JSONArray json = new JSONArray();
			json.add(orderApprovalIds);
			return new ResultVo(true, str.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(true, "");
		}
	}
	
	/*
	 * 跳转总经理审核库存页面
	 */
	@RequestMapping(value="/storageProfit",method=RequestMethod.GET)
	public String storageProfit(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
		String id= request.getParameter("clientOrderElementId");
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		/*String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());*/
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientweatherorder/storageelementlist";
	}
	/*
	 * 总经理审批库存列表页面数据
	 */
	@RequestMapping(value="/StorageDate",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo StorageDate(HttpServletRequest request,HttpServletResponse response) {
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
		
		String elementId =request.getParameter("clientOrderElementId");
		String idsForTask = "";
		if (elementId != null) {
			List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(elementId);
			if (listTask.size() > 0) {
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
			    idsForTask=jbpm4Tasks.get(0).getRelationId().toString();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	 idsForTask+=","+jbpm4Tasks.get(i).getRelationId();
				}
			}
		}
		
		//page.put("clientOrderElementId", request.getParameter("clientOrderElementId"));
		page.put("clientOrderElementId", idsForTask);
		
		orderApprovalService.selectByClientOrderIdPage(page);
		OrderApprovalVo entitie=new OrderApprovalVo();
		entitie.setPartNumber("总计");
		if (page.getEntities().size() > 0) {
			page.getEntities().add(entitie);
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			Double grossProfitAmount=0.0;
			Double grossProfit=0.0;
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OrderApprovalVo orderApprovalVo : page.getEntities()) {
				if(orderApprovalVo.getPartNumber().equals("总计")){
					orderApprovalVo.setProfitMargin(grossProfit);
					orderApprovalVo.setGrossProfitAmount(grossProfitAmount);
					orderApprovalVo.setType(2);
				}else{
					ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(orderApprovalVo.getClientOrderElementId());
					ClientProfitmargin clientProfitmargin=clientProfitmarginService.selectByClientId(clientOrderElementVo.getClientId());
					Double quotePrice=clientOrderElementVo.getSupplierQuotePrice();
					orderApprovalVo.setQuotePrice(quotePrice);
					SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(clientOrderElementVo.getSupplierQuoteElementId());
					orderApprovalVo.setCode(element.getCode());
	//				StorageFlowVo flowVo=importpackageElementService.findStorageBySoeIdAndIpeId(orderApprovalVo.getSupplierQuoteElementId(), orderApprovalVo.getImportPackageElementId());
					ClientWeatherOrder clientWeatherOrder=clientWeatherOrderService.selectByPrimaryKey(orderApprovalVo.getClientOrderId());
					Double profitMargin=0.0;
					
					Double fixedCost=clientOrderElementVo.getFixedCost();
					if(fixedCost != null && fixedCost<1){
						fixedCost=fixedCost*orderApprovalVo.getOrderPrice();
					}else if (fixedCost == null) {
						fixedCost = 0.00;
					}
					Double price=orderApprovalVo.getOrderPrice();
					Double bankCharges=clientOrderElementVo.getBankCharges();
					if(bankCharges != null && bankCharges<1){
						bankCharges=bankCharges*orderApprovalVo.getOrderPrice();
					}else if (bankCharges == null) {
						bankCharges = 0.00;
					}
					Double bankCost = 0.0;
					Double feeForExchangeBill = 0.0;
					Double hazmatFee = 0.0;
					SupplierQuoteElement supplierQuoteElement=supplierQuoteElementService.selectByPrimaryKey(clientOrderElementVo.getSupplierQuoteElementId());
					ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
					if (supplierQuoteElement.getBankCost() != null && supplierQuoteElement.getBankCost() > 0) {
						BigDecimal bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						bankCost = bankCostQuote.doubleValue()/orderApprovalVo.getOrderAmount();
					}
					if (supplierQuoteElement.getFeeForExchangeBill() != null && supplierQuoteElement.getFeeForExchangeBill() > 0) {
						BigDecimal feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						feeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getOrderAmount();
					}
					if (supplierQuoteElement.getHazmatFee() != null && supplierQuoteElement.getHazmatFee() > 0) {
						BigDecimal hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						hazmatFee = hzmatFeequote.doubleValue();
					}
					orderApprovalVo.setQuoteBankCost(new BigDecimal(bankCharges).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					orderApprovalVo.setQuoteFeeForExchangeBill(new BigDecimal(feeForExchangeBill).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					orderApprovalVo.setQuoteHazmatFee(new BigDecimal(hazmatFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					
					if(orderApprovalVo.getType().equals(1)||orderApprovalVo.getType()==1){//在途库存利润
						AddSupplierOrderElementVo elementVo=supplierOrderElementService.findByElementId(orderApprovalVo.getSupplierOrderElementId());
						orderApprovalVo.setImportPartNumber(elementVo.getQuotePartNumber());
						orderApprovalVo.setImportDescription(elementVo.getQuoteDescription());
						Integer sCurrencyId=elementVo.getCurrencyId();
						Integer cCurrencyId=clientWeatherOrder.getCurrencyId();
						if(!sCurrencyId.equals(cCurrencyId)){
							orderApprovalVo.setPrice(orderApprovalVo.getPrice()*elementVo.getExchangeRate()/clientWeatherOrder.getExchangeRate());
						}
						orderApprovalVo.setOrderPrice(orderApprovalVo.getOrderPrice());
						profitMargin=((orderApprovalVo.getOrderPrice()-fixedCost-bankCharges-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getPrice())/
								(orderApprovalVo.getOrderPrice()-fixedCost-bankCharges-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()))*100;
					}else{//自有库存利润
					ImportPackageElementVo elementVo=importpackageElementService.findimportpackageelement(orderApprovalVo.getImportPackageElementId().toString());
					Integer sCurrencyId=elementVo.getCurrencyId();
					Integer cCurrencyId=clientWeatherOrder.getCurrencyId();
					if(!sCurrencyId.equals(cCurrencyId)){
						orderApprovalVo.setPrice(orderApprovalVo.getPrice()*elementVo.getExchangeRate()/clientWeatherOrder.getExchangeRate());
					}
					orderApprovalVo.setOrderPrice(orderApprovalVo.getOrderPrice());
					profitMargin=((orderApprovalVo.getOrderPrice()-fixedCost-bankCharges-orderApprovalVo.getPrice())/orderApprovalVo.getOrderPrice())*100;
					}
					orderApprovalVo.setFixedCost(fixedCost);
					orderApprovalVo.setBankCharges(bankCharges);
					orderApprovalVo.setProfitMargin(profitMargin);
					orderApprovalVo.setClientProfitMargin(clientProfitmargin.getProfitMargin()*100);
					orderApprovalVo.setGrossProfitAmount(orderApprovalVo.getOrderPrice()-fixedCost-bankCharges-orderApprovalVo.getPrice());
					grossProfitAmount+=orderApprovalVo.getGrossProfitAmount();
					grossProfit+=profitMargin;
			}
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
	
	/*
	 * 跳转销售修改列表页面
	 */
	@RequestMapping(value="/marketingUpdateWeatherOrder",method=RequestMethod.GET)
	public String marketingUpdateWeatherOrder(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		/*String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());*/
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientweatherorder/updateweatherorder";
	}
	
	/*
	 * 销售修改或作废客户订单页面
	 */
	@RequestMapping(value="/updateClientOrderPage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo updateClientOrderPage(HttpServletRequest request,HttpServletResponse response) {
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
		
		String elementId =request.getParameter("clientOrderElementId");
		String idsForTask = "";
		if (elementId != null) {
			List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(elementId);
			if (listTask.size() > 0) {
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
			    idsForTask=jbpm4Tasks.get(0).getRelationId().toString();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    	 }
			    	 idsForTask+=","+jbpm4Tasks.get(i).getRelationId();
				}
			}
		}
		
		/*String ids =request.getParameter("clientOrderElementId");
		page.put("clientOrderElementId", request.getParameter("clientOrderElementId"));*/
		page.put("clientOrderElementId", idsForTask);
		orderApprovalService.updateClientOrderPage(page);
		OrderApprovalVo entitie=new OrderApprovalVo();
		entitie.setPartNumber("总计");
		if (page.getEntities().size() > 0) {
			page.getEntities().add(entitie);
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			Double grossProfitAmount=0.0;
			Double grossProfit=0.0;
			Double supplierPrice=0.0;
			Double clientPrice=0.0;
			Double countFixedCost=0.0;
			Double countBackChargs=0.0;
			Double countFeeForExchangeBill=0.0;
			Double countbankCost=0.0;
			Double countHazmatFee=0.0;
			Double countOtherFee=0.0;
					
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OrderApprovalVo orderApprovalVo : page.getEntities()) {
				Double price=orderApprovalVo.getOrderPrice();
				if(orderApprovalVo.getPartNumber().equals("总计")){
					grossProfit=1-((supplierPrice+countFixedCost+countbankCost+countFeeForExchangeBill+countBackChargs+countHazmatFee+countOtherFee)/(clientPrice));
					grossProfit = (clientPrice-supplierPrice-countFixedCost-countbankCost-countFeeForExchangeBill-countBackChargs-countHazmatFee-countOtherFee)/
							(clientPrice-countFixedCost-countbankCost-countFeeForExchangeBill-countBackChargs-countHazmatFee-countOtherFee);
					orderApprovalVo.setWeatherOrderprofitMargin(grossProfit*100);
					orderApprovalVo.setGrossProfitAmount(grossProfitAmount);
				}else{
					ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(orderApprovalVo.getClientOrderElementId());
					ClientProfitmargin clientProfitmargin=clientProfitmarginService.selectByClientId(clientOrderElementVo.getClientId());
					orderApprovalVo.setClientProfitMargin(clientProfitmargin.getProfitMargin()*100);
					Double fixedCost=clientOrderElementVo.getFixedCost();
					if(fixedCost != null && fixedCost < 1){
						fixedCost = price*fixedCost;
					}else if (fixedCost == null) {
						fixedCost = 0.0;
					}
					Double bankCharges=clientOrderElementVo.getBankCharges();
					if(bankCharges != null && bankCharges < 1){
						bankCharges = price*bankCharges;
					}else if (bankCharges == null) {
						bankCharges = 0.0;
					}
					countFixedCost+=(fixedCost*orderApprovalVo.getOrderAmount());
					countBackChargs+=(bankCharges*orderApprovalVo.getOrderAmount());
					SupplierQuoteElement quoteElement=supplierQuoteElementService.selectByPrimaryKey(clientOrderElementVo.getSupplierQuoteElementId());
					SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(quoteElement.getSupplierQuoteId());
					Double quoteAmount = supplierQuoteElementService.getCountByQuoteId(supplierQuote.getId());
					if (fixedCost != null) {
						orderApprovalVo.setFixedCost(new BigDecimal(fixedCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					}else {
						orderApprovalVo.setFixedCost(new Double(0));
					}
					if (bankCharges != null) {
						orderApprovalVo.setBankCharges(new BigDecimal(bankCharges).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					}else {
						orderApprovalVo.setBankCharges(new Double(0));
					}
					if(null!=orderApprovalVo.getSupplierWeatherOrderElementId()){//供应商预订单利润
						SupplierWeatherOrderElement supplierQuoteElement=supplierWeatherOrderElementService.selectByPrimaryKey(orderApprovalVo.getSupplierWeatherOrderElementId());
						SupplierQuoteElement supplierQuoteElementForOrder = supplierQuoteElementService.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteElementId());
						if(supplierQuoteElement.getSupplierStatus().equals(1)||supplierQuoteElement.getSupplierStatus()==1){
							ClientWeatherOrderElement clientWeatherOrderElement = clientWeatherOrderElementService.selectByPrimaryKey(orderApprovalVo.getClientOrderElementId());
							ClientWeatherOrder clientWeatherOrder = clientWeatherOrderService.selectByPrimaryKey(clientWeatherOrderElement.getClientWeatherOrderId());
							ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientWeatherOrder.getClientQuoteId());
							ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
							Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
							ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
							SupplierQuoteElement supplierQuoteElementForCqe = supplierQuoteElementService.selectByPrimaryKey(clientQuoteElement.getSupplierQuoteElementId());
							SupplierQuote suppleirQuoteForCqe = supplierQuoteService.selectByPrimaryKey(supplierQuoteElementForCqe.getSupplierQuoteId());
							SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteElementId());
							Integer sCurrencyId=element.getCurrencyId();
							Integer sqeCurrencyId=suppleirQuoteForCqe.getCurrencyId();
							Integer cCurrencyId=orderApprovalVo.getCurrencyId();
							Double sqePrice=supplierQuoteElement.getPrice();
							ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
							if(!sCurrencyId.equals(cCurrencyId)){
								//判断是否7开头客户
								//ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
								if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
									if (!sCurrencyId.toString().equals("11")) {
										ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(sCurrencyId);
										Double relative = new BigDecimal(currentRate.getRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
										relative = relative*usRate.getRelativeRate();
										sqePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(relative),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
									}else {
										sqePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
									}
								}else {
									//正常的价格计算方法
									sqePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(element.getExchangeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
									//weatherPrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
								}
								//sqePrice=supplierQuoteElement.getPrice()*element.getExchangeRate()/orderApprovalVo.getExchangeRate();
							}
							orderApprovalVo.setPrice(new BigDecimal(sqePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
							Double quoteFeeForExchangeBill = 0.0;
							Double quoteBankCost = 0.0;
							Double quoteHazmatFee = 0.0;
							Double quoteOtherFee = 0.0;
							if (orderApprovalVo.getQuoteBankCost() != null && orderApprovalVo.getQuoteBankCost() > 0) {
								BigDecimal bankCostQuote = new BigDecimal(0);
								if (client.getCode().startsWith("7")) {
									bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getQuoteBankCost()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
								}else {
									bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getQuoteBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
								}
								if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
									if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
										quoteBankCost = bankCostQuote.doubleValue()/orderApprovalVo.getClientMoq();
										countbankCost = countbankCost + quoteBankCost * orderApprovalVo.getOrderAmount();
									}else {
										quoteBankCost = bankCostQuote.doubleValue()/orderApprovalVo.getOrderAmount();
										countbankCost = countbankCost + bankCostQuote.doubleValue();
									}
								}else {
									quoteBankCost = bankCostQuote.doubleValue()/orderApprovalVo.getOrderAmount();
									countbankCost = countbankCost + bankCostQuote.doubleValue();
								}
							}
							if (orderApprovalVo.getQuoteFeeForExchangeBill() != null && orderApprovalVo.getQuoteFeeForExchangeBill() > 0) {
								BigDecimal feeForExchangeBillQuote = new BigDecimal(0);
								if (client.getCode().startsWith("7")) {
									feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getQuoteFeeForExchangeBill()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
								}else {
									feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getQuoteFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
								}
								if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
									if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
										quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getClientMoq();
										countFeeForExchangeBill = countFeeForExchangeBill + quoteFeeForExchangeBill*orderApprovalVo.getOrderAmount();
									}else {
										quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getOrderAmount();
										countFeeForExchangeBill = countFeeForExchangeBill + feeForExchangeBillQuote.doubleValue();
									}
								}else {
									quoteFeeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getOrderAmount();
									countFeeForExchangeBill = countFeeForExchangeBill + feeForExchangeBillQuote.doubleValue();
								}
							}
							if (orderApprovalVo.getQuoteHazmatFee() != null && orderApprovalVo.getQuoteHazmatFee() > 0) {
								BigDecimal hzmatFeequote = new BigDecimal(0);
								if (client.getCode().startsWith("7")) {
									hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getQuoteHazmatFee()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
								}else {
									hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getQuoteHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
								}
								quoteHazmatFee = hzmatFeequote.doubleValue();
							}
							countHazmatFee = quoteHazmatFee*orderApprovalVo.getOrderAmount();
							if (orderApprovalVo.getQuoteOtherFee() != null && orderApprovalVo.getQuoteOtherFee() > 0) {
								BigDecimal otherFee = new BigDecimal(0);
								if (client.getCode().startsWith("7")) {
									otherFee = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getQuoteOtherFee()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
								}else {
									otherFee = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getQuoteOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
								}
								if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
									if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
										quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getClientMoq();
										countOtherFee = countOtherFee + quoteOtherFee * orderApprovalVo.getOrderAmount();
									}else {
										quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getOrderAmount();
										countOtherFee = countOtherFee + otherFee.doubleValue();
									}
								}else {
									quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getOrderAmount();
									countOtherFee = countOtherFee + otherFee.doubleValue();
								}
								
							}
							orderApprovalVo.setQuoteBankCost(new BigDecimal(quoteBankCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
							orderApprovalVo.setQuoteFeeForExchangeBill(new BigDecimal(quoteFeeForExchangeBill).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
							orderApprovalVo.setQuoteHazmatFee(new BigDecimal(quoteHazmatFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
							orderApprovalVo.setQuoteOtherFee(new BigDecimal(quoteOtherFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
							SupplierInquiry supplierInquiry = supplierInquiryService.selectByPrimaryKey(suppleirQuoteForCqe.getSupplierInquiryId());
							Supplier supplier = supplierService.selectByPrimaryKey(supplierInquiry.getSupplierId());
							orderApprovalVo.setSupplierCode(supplier.getCode());
							Double quotePrice = element.getPrice();
							if(!sqeCurrencyId.equals(cCurrencyId)){
								//判断是否7开头客户
								//ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
								if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
									if (!sqeCurrencyId.toString().equals("11")) {
										ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(sqeCurrencyId);
										Double relative = new BigDecimal(currentRate.getRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
										relative = relative*usRate.getRelativeRate();
										quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElementForCqe.getPrice()), new BigDecimal(relative),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
									}else {
										quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElementForCqe.getPrice()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
									}
								}else {
									//正常的价格计算方法
									quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElementForCqe.getPrice()), new BigDecimal(element.getExchangeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
								}
							}
							orderApprovalVo.setQuotePrice(quotePrice);
							
							//订单利润
							Double weatherOrderprofitMargin=(price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getBankCharges()-orderApprovalVo.getFixedCost()-orderApprovalVo.getQuoteOtherFee()-orderApprovalVo.getPrice())
									/(price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getBankCharges()-orderApprovalVo.getFixedCost()-orderApprovalVo.getQuoteOtherFee());
							orderApprovalVo.setGrossProfitAmount((price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getBankCharges()-orderApprovalVo.getFixedCost()-orderApprovalVo.getQuoteOtherFee()-orderApprovalVo.getPrice()));
							orderApprovalVo.setWeatherOrderprofitMargin(weatherOrderprofitMargin*100);
							orderApprovalVo.setCode(element.getCode());
							
							supplierPrice+=(sqePrice*orderApprovalVo.getAmount());
							clientPrice+=(price*orderApprovalVo.getOrderAmount());
							grossProfitAmount+=(orderApprovalVo.getGrossProfitAmount()*orderApprovalVo.getOrderAmount());
						}else if(null!=supplierQuoteElement.getSupplierQuoteElementId()&&null!=supplierQuoteElement.getPrice()){
							SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteElementId());
							orderApprovalVo.setCode(element.getCode());
							Integer sCurrencyId=element.getCurrencyId();
							Integer cCurrencyId=orderApprovalVo.getCurrencyId();
							if(null!=supplierQuoteElement.getPrice()){
								Double sqePrice=supplierQuoteElement.getPrice();
								if(!sCurrencyId.equals(cCurrencyId)){
								 sqePrice=supplierQuoteElement.getPrice()*element.getExchangeRate()/orderApprovalVo.getExchangeRate();
								}
								orderApprovalVo.setPrice(new BigDecimal(sqePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
							}
							
						}
					}
				//供应商报价利润
//					SupplierQuoteElement sQuoteElement=supplierQuoteElementService.selectByPrimaryKey(clientOrderElementVo.getSupplierQuoteElementId());
//					Integer sCurrencyIdQuote=sQuoteElement.getCurrencyId();
//					Integer cCurrencyIdQuote=orderApprovalVo.getCurrencyId();
//					Double quotePrice=clientOrderElementVo.getSupplierQuotePrice();
//					if(!cCurrencyIdQuote.equals(sCurrencyIdQuote)){
//					 quotePrice=clientOrderElementVo.getSupplierQuotePrice()*sQuoteElement.getExchangeRate()/orderApprovalVo.getCqExchangeRate();
//					}
//					Double weatherQuoteprofitMargin=(price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getBankCharges()-orderApprovalVo.getFixedCost()-orderApprovalVo.getQuoteOtherFee()-quotePrice)/
//							(price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getBankCharges()-orderApprovalVo.getFixedCost()-orderApprovalVo.getQuoteOtherFee());
//					orderApprovalVo.setWeatherQuoteprofitMargin(weatherQuoteprofitMargin*100);
//					orderApprovalVo.setQuotePrice(new BigDecimal(quotePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//					orderApprovalVo.setSupplierCode(sQuoteElement.getCode());
				}
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
	
	/*
	 * 总经理审核列表页面
	 */
	@RequestMapping(value="/managerApproval",method=RequestMethod.GET)
	public String managerApproval(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		/*String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());*/
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientweatherorder/managerapproval";
	}
	
	/*
	 * 总经理审核订单利润列表页面
	 */
	@RequestMapping(value="/orderprofitmarginapproval",method=RequestMethod.GET)
	public String orderprofitmarginapproval(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		/*String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());*/
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientweatherorder/orderprofitmarginapproval";
	}
	
	/*
	 * 销售作废客户订单列表页面
	 */
	@RequestMapping(value="/cancellationOrder",method=RequestMethod.GET)
	public String cancellationOrder(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		/*String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());*/
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientweatherorder/cancellationorder";
	}
	
	/*
	 * 跳转销售生成客户订单列表页面
	 */
	@RequestMapping(value="/marketingAddOrder",method=RequestMethod.GET)
	public String marketingAddOrder(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		/*String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());*/
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientweatherorder/clientorderfinal";
	}
	
	/*
	 * 销售生成客户订单页面
	 */
	@RequestMapping(value="/clientOrderFinalDate",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo clientOrderFinalDate(HttpServletRequest request,HttpServletResponse response) {
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
		
		String elementId =request.getParameter("clientOrderElementId");
		String idsForTask = "";
		if (elementId != null) {
			List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(elementId);
			if (listTask.size() > 0) {
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
			    idsForTask=jbpm4Tasks.get(0).getRelationId().toString();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	 idsForTask+=","+jbpm4Tasks.get(i).getRelationId();
				}
			}
		}
		
//		page.put("clientOrderElementId", request.getParameter("clientOrderElementId"));
		page.put("clientOrderElementId", idsForTask);
		orderApprovalService.clientOrderELmentFinalPage(page);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OrderApprovalVo orderApprovalVo : page.getEntities()) {
				
				ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(orderApprovalVo.getClientOrderElementId());
				ClientProfitmargin clientProfitmargin=clientProfitmarginService.selectByClientId(clientOrderElementVo.getClientId());
				orderApprovalVo.setClientProfitMargin(clientProfitmargin.getProfitMargin()*100);
				Double weatherOrderfixedCost=clientOrderElementVo.getFixedCost();
				Double price=orderApprovalVo.getOrderPrice();
				Double weatherOrderBankCharges = clientOrderElementVo.getBankCharges();
				if(weatherOrderfixedCost != null && weatherOrderfixedCost < 1){
					weatherOrderfixedCost = price*weatherOrderfixedCost;
				}else if (weatherOrderfixedCost == null) {
					weatherOrderfixedCost = 0.00;
				}
				orderApprovalVo.setFixedCost(new BigDecimal(weatherOrderfixedCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				if(weatherOrderBankCharges != null && weatherOrderBankCharges < 1){
					weatherOrderBankCharges = price*weatherOrderBankCharges;
				}else if (weatherOrderBankCharges == null) {
					weatherOrderBankCharges = 0.00;
				}
				orderApprovalVo.setBankCharges(new BigDecimal(weatherOrderBankCharges).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				SupplierQuoteElement supplierQuoteElement=supplierQuoteElementService.selectByPrimaryKey(orderApprovalVo.getSupplierQuoteElementId());
				if (supplierQuoteElement == null) {
					supplierQuoteElement=supplierQuoteElementService.selectByPrimaryKey(clientOrderElementVo.getSupplierQuoteElementId());
				}
				SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(supplierQuoteElement.getSupplierQuoteId());
				Double quoteAmount = supplierQuoteElementService.getCountByQuoteId(supplierQuote.getId());
				Double feeForExchangeBill = 0.0;
				Double bankCharges = 0.0;
				Double hazmatFee = 0.0;
				Double quoteOtherFee = 0.0;
				ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
				ClientWeatherOrderElement clientWeatherOrderElement = clientWeatherOrderElementService.selectByPrimaryKey(orderApprovalVo.getClientOrderElementId());
				ClientWeatherOrder clientWeatherOrder = clientWeatherOrderService.selectByPrimaryKey(clientWeatherOrderElement.getClientWeatherOrderId());
				ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientWeatherOrder.getClientQuoteId());
				ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
				Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
				if (supplierQuoteElement.getBankCost() != null && supplierQuoteElement.getBankCost() > 0) {
					BigDecimal bankCostQuote = new BigDecimal(0);
					if (client.getCode().startsWith("7")) {
						bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getBankCost()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
					}else {
						bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
					}
					if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
						if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
							bankCharges = bankCostQuote.doubleValue()/orderApprovalVo.getClientMoq();
						}else {
							bankCharges = bankCostQuote.doubleValue()/orderApprovalVo.getOrderAmount();
						}
					}else {
						bankCharges = bankCostQuote.doubleValue()/orderApprovalVo.getOrderAmount();
					}
					
				}
				if (supplierQuoteElement.getFeeForExchangeBill() != null && supplierQuoteElement.getFeeForExchangeBill() > 0) {
					BigDecimal feeForExchangeBillQuote = new BigDecimal(0);
					if (client.getCode().startsWith("7")) {
						feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getFeeForExchangeBill()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
					}else {
						feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
					}
					if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
						if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
							feeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getClientMoq();
						}else {
							feeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getOrderAmount();
						}
					}else {
						feeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getOrderAmount();
					}
					
				}
				if (supplierQuoteElement.getHazmatFee() != null && supplierQuoteElement.getHazmatFee() > 0) {
					BigDecimal hzmatFeequote = new BigDecimal(0);
					if (client.getCode().startsWith("7")) {
						hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getHazmatFee()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
					}else {
						hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
					}
					hazmatFee = hzmatFeequote.doubleValue();
				}
				if (supplierQuoteElement.getOtherFee() != null && supplierQuoteElement.getOtherFee() > 0) {
					BigDecimal otherFee = new BigDecimal(0);
					if (client.getCode().startsWith("7")) {
						otherFee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getOtherFee()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
					}else {
						otherFee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
					}
					
					if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
						if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
							quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getClientMoq();
						}else {
							quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getOrderAmount();
						}
					}else {
						quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getOrderAmount();
					}
					
				}
				orderApprovalVo.setQuoteBankCost(new BigDecimal(bankCharges).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				orderApprovalVo.setQuoteFeeForExchangeBill(new BigDecimal(feeForExchangeBill).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				orderApprovalVo.setQuoteHazmatFee(new BigDecimal(hazmatFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				orderApprovalVo.setQuoteOtherFee(new BigDecimal(quoteOtherFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				
				if(null!=orderApprovalVo.getSupplierStatus()&&(orderApprovalVo.getSupplierStatus().equals(0)||orderApprovalVo.getSupplierStatus()==0)){
					if(null!=orderApprovalVo.getSupplierQuoteElementId()){
						SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(orderApprovalVo.getSupplierQuoteElementId());
						Integer sqCurrencyId=element.getCurrencyId();
						Integer cwoCurrencyId=orderApprovalVo.getCurrencyId();
						if(null!=orderApprovalVo.getSupplierOrderPrice()){
							Double sqePrice=orderApprovalVo.getSupplierOrderPrice();
							if(!sqCurrencyId.equals(cwoCurrencyId)){
							 sqePrice=orderApprovalVo.getSupplierOrderPrice()*element.getExchangeRate()/orderApprovalVo.getExchangeRate();
							}
							orderApprovalVo.setSupplierOrderPrice(new BigDecimal(sqePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
						}
						orderApprovalVo.setCode(element.getCode());
					}
				}else if(null!=orderApprovalVo.getSupplierQuoteElementId()){
				SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(orderApprovalVo.getSupplierQuoteElementId());
				Integer sqCurrencyId=element.getCurrencyId();
				Integer cwoCurrencyId=orderApprovalVo.getCurrencyId();
				Double sqePrice=orderApprovalVo.getSupplierOrderPrice();
				if(!sqCurrencyId.equals(cwoCurrencyId)){
					//判断是否7开头客户
					//ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
					ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
					if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
						if (!sqCurrencyId.toString().equals("11")) {
							ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(sqCurrencyId);
							Double relative = new BigDecimal(currentRate.getRelativeRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
							relative = relative*usRate.getRelativeRate();
							sqePrice = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getSupplierOrderPrice()), new BigDecimal(relative),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
						}else {
							sqePrice = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getSupplierOrderPrice()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
						}
					}else {
						//正常的价格计算方法
						sqePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(element.getExchangeRate()),new BigDecimal(orderApprovalVo.getExchangeRate())).doubleValue();
						//weatherPrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
					}
				 //sqePrice=orderApprovalVo.getSupplierOrderPrice()*element.getExchangeRate()/orderApprovalVo.getExchangeRate();
				}
				orderApprovalVo.setSupplierOrderPrice(sqePrice);
				SupplierWeatherOrderElement supplierWeatherOrderElement = supplierWeatherOrderElementService.selectByPrimaryKey(orderApprovalVo.getSupplierWeatherOrderElementId());
				SupplierQuoteElement supplierQuoteElementForOrder = supplierQuoteElementService.selectByPrimaryKey(supplierWeatherOrderElement.getSupplierQuoteElementId());
				Double weatherOrderprofitMargin=(price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getFixedCost()-orderApprovalVo.getBankCharges()-orderApprovalVo.getQuoteOtherFee()-sqePrice)/
						(price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getFixedCost()-orderApprovalVo.getBankCharges()-orderApprovalVo.getQuoteOtherFee());
				orderApprovalVo.setWeatherOrderprofitMargin(weatherOrderprofitMargin*100);
				orderApprovalVo.setCode(element.getCode());
				orderApprovalVo.setWeatherOrderAmount(orderApprovalVo.getWeatherOrderAmount());
				orderApprovalVo.setTotal(orderApprovalVo.getSupplierOrderPrice()*orderApprovalVo.getWeatherOrderAmount());
				orderApprovalVo.setTotal(new BigDecimal(orderApprovalVo.getTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}else{
					if(orderApprovalVo.getOccupy().equals(1)){
						if(orderApprovalVo.getType().equals(0)){
							orderApprovalVo.setStorageAmount(orderApprovalVo.getOaStorageAmount());
						}else{
							orderApprovalVo.setOnpassStorageAmount(orderApprovalVo.getOaStorageAmount());
						}
					}
				}
				
				Integer sqCurrencyId=supplierQuoteElement.getCurrencyId();
				Integer cwoCurrencyId=orderApprovalVo.getCurrencyId();
				Double quotePrice=clientOrderElementVo.getSupplierQuotePrice()+supplierQuoteElement.getFreight();
				if(!sqCurrencyId.equals(cwoCurrencyId)){
					//判断是否7开头客户
					//ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
					ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
					if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
						if (!sqCurrencyId.toString().equals("11")) {
							ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(sqCurrencyId);
							Double relative = new BigDecimal(currentRate.getRelativeRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
							relative = relative*usRate.getRelativeRate();
							quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(relative),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
						}else {
							quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
						}
					}else {
						//正常的价格计算方法
						quotePrice = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getPrice()), new BigDecimal(supplierQuoteElement.getExchangeRate()),new BigDecimal(orderApprovalVo.getExchangeRate())).doubleValue();
						//weatherPrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
					}
					//quotePrice=(supplierQuoteElement.getPrice()+supplierQuoteElement.getFreight())*supplierQuoteElement.getExchangeRate()/orderApprovalVo.getCqExchangeRate();
				}
				orderApprovalVo.setQuotePrice(new BigDecimal(quotePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				
				Double weatherQuoteprofitMargin=(price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getFixedCost()-orderApprovalVo.getBankCharges()-orderApprovalVo.getQuoteOtherFee()-quotePrice)/
						(price-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getFixedCost()-orderApprovalVo.getBankCharges()-orderApprovalVo.getQuoteOtherFee());
				orderApprovalVo.setWeatherQuoteprofitMargin(weatherQuoteprofitMargin*100);
				orderApprovalVo.setSupplierCode(supplierQuoteElement.getCode());
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
	
	/*
	 * 总经理审核订单利润页面
	 */
	@RequestMapping(value="/orderProfitmargin",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo orderProfitmargin(HttpServletRequest request,HttpServletResponse response) {
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
		
		String elementId =request.getParameter("clientOrderElementId");
		String idsForTask = "";
		if (elementId != null) {
			List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(elementId);
			if (listTask.size() > 0) {
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
			    idsForTask=jbpm4Tasks.get(0).getRelationId().toString();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	 idsForTask+=","+jbpm4Tasks.get(i).getRelationId();
				}
			}
		}
		
		//page.put("clientOrderElementId", request.getParameter("clientOrderElementId"));
		page.put("clientOrderElementId", idsForTask);
		OrderApprovalVo entitie=new OrderApprovalVo();
		entitie.setPartNumber("总计");
		orderApprovalService.clientOrderELmentFinalPage(page);
		if (page.getEntities().size() > 0) {
			page.getEntities().add(entitie);
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			Double grossProfitAmount=0.0;
			Double grossProfit=0.0;
			Double supplierPrice=0.0;
			Double clientPrice=0.0;
			Double countFixedCost=0.0;
			Double countBackChargs=0.0;
			Double countbankCost=0.0;
			Double countfeeForExchangeBill=0.0;
			Double counthazmatFee=0.0;
			Double countOther = 0.0;
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OrderApprovalVo orderApprovalVo : page.getEntities()) {
				if(orderApprovalVo.getPartNumber().equals("总计")){
					//grossProfit=1-((supplierPrice+countFixedCost+countBackChargs)/clientPrice);
					grossProfit = (clientPrice - countFixedCost - countBackChargs - countbankCost - countfeeForExchangeBill - counthazmatFee - countOther - supplierPrice)/
							(clientPrice - countFixedCost - countBackChargs - countbankCost - countfeeForExchangeBill - counthazmatFee - countOther);
					orderApprovalVo.setWeatherOrderprofitMargin(grossProfit*100);
					orderApprovalVo.setGrossProfitAmount(grossProfitAmount);
				}else{
					ClientOrderElementFinal clientOrderElementFinal=clientOrderElementFinalService.selectByPrimaryKey(orderApprovalVo.getClientOrderElementId());
	//				ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(orderApprovalVo.getClientOrderElementId());
					ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(orderApprovalVo.getClientOrderElementId());
					ClientProfitmargin clientProfitmargin=clientProfitmarginService.selectByClientId(clientOrderElementVo.getClientId());
					Double fixedCost=clientOrderElementFinal.getFixedCost();
					if (fixedCost == null) {
						fixedCost = 0.0;
					}else if (fixedCost != null && fixedCost < 1) {
						fixedCost = fixedCost*clientOrderElementFinal.getPrice();
					}
					countFixedCost+=(fixedCost*orderApprovalVo.getOrderAmount());
					Double price=clientOrderElementFinal.getPrice();
					Double bankCharges=clientOrderElementFinal.getBankCharges();
					if (bankCharges == null) {
						bankCharges = 0.0;
					}else if (bankCharges != null && bankCharges < 1) {
						bankCharges = bankCharges*clientOrderElementFinal.getPrice();
					}
					countBackChargs+=(bankCharges*orderApprovalVo.getOrderAmount());
					orderApprovalVo.setFixedCost(fixedCost);
					orderApprovalVo.setBankCharges(bankCharges);
					if(null!=orderApprovalVo.getSupplierQuoteElementId()){
					SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(orderApprovalVo.getSupplierQuoteElementId());
					Integer sCurrencyId=element.getCurrencyId();
					Integer cCurrencyId=orderApprovalVo.getCurrencyId();
					Double sqePrice=orderApprovalVo.getSupplierOrderPrice();
					ClientWeatherOrderElement clientWeatherOrderElement = clientWeatherOrderElementService.selectByPrimaryKey(orderApprovalVo.getClientWeatherOrderId());
					ClientWeatherOrder clientWeatherOrder = clientWeatherOrderService.selectByPrimaryKey(clientWeatherOrderElement.getClientWeatherOrderId());
					ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientWeatherOrder.getClientQuoteId());
					ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
					Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
					if(!sCurrencyId.equals(cCurrencyId)){
						//判断是否7开头客户
						ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
						ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
						if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
							if (!sCurrencyId.toString().equals("11")) {
								ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(sCurrencyId);
								Double relative = new BigDecimal(currentRate.getRelativeRate()).divide(new BigDecimal(usRate.getRate()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
								relative = relative*usRate.getRelativeRate();
								sqePrice = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getSupplierOrderPrice()), new BigDecimal(relative),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
							}else {
								sqePrice = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getSupplierOrderPrice()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
							}
						}else {
							//正常的价格计算方法
							sqePrice = clientQuoteService.caculatePrice(new BigDecimal(orderApprovalVo.getSupplierOrderPrice()), new BigDecimal(element.getExchangeRate()),new BigDecimal(clientWeatherOrder.getExchangeRate())).doubleValue();
							//weatherPrice=supplierWeatherOrderElement.getPrice()*element.getExchangeRate()/clientWeatherOrder.getExchangeRate();
						}
					 //sqePrice=orderApprovalVo.getSupplierOrderPrice()*element.getExchangeRate()/orderApprovalVo.getExchangeRate();
					}
					Double bankCost = 0.0;
					Double feeForExchangeBill = 0.0;
					Double hazmatFee = 0.0;
					Double quoteOtherFee = 0.0;
					SupplierQuoteElement supplierQuoteElement=supplierQuoteElementService.selectByPrimaryKey(clientOrderElementVo.getSupplierQuoteElementId());
					ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
					if (supplierQuoteElement.getBankCost() != null && supplierQuoteElement.getBankCost() > 0) {
						BigDecimal bankCostQuote = new BigDecimal(0);
						if (client.getCode().startsWith("7")) {
							bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getBankCost()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						}else {
							bankCostQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						}
						if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
							if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
								bankCost = bankCostQuote.doubleValue()/orderApprovalVo.getClientMoq();
								countbankCost = countbankCost + bankCost * orderApprovalVo.getOrderAmount();
							}else {
								bankCost = bankCostQuote.doubleValue()/orderApprovalVo.getOrderAmount();
								countbankCost = countbankCost + bankCostQuote.doubleValue();
							}
						}else {
							bankCost = bankCostQuote.doubleValue()/orderApprovalVo.getOrderAmount();
							countbankCost = countbankCost + bankCostQuote.doubleValue();
						}
						
					}
					if (supplierQuoteElement.getFeeForExchangeBill() != null && supplierQuoteElement.getFeeForExchangeBill() > 0) {
						BigDecimal feeForExchangeBillQuote = new BigDecimal(0);
						if (client.getCode().startsWith("7")) {
							feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getFeeForExchangeBill()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						}else {
							feeForExchangeBillQuote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						}
						if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
							if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
								feeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getClientMoq();
								countfeeForExchangeBill = countfeeForExchangeBill + feeForExchangeBill * orderApprovalVo.getOrderAmount();
							}else {
								feeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getOrderAmount();
								countfeeForExchangeBill = countfeeForExchangeBill + feeForExchangeBillQuote.doubleValue();
							}
						}else {
							feeForExchangeBill = feeForExchangeBillQuote.doubleValue()/orderApprovalVo.getOrderAmount();
							countfeeForExchangeBill = countfeeForExchangeBill + feeForExchangeBillQuote.doubleValue();
						}
						
					}
					if (supplierQuoteElement.getHazmatFee() != null && supplierQuoteElement.getHazmatFee() > 0) {
						BigDecimal hzmatFeequote = new BigDecimal(0);
						if (client.getCode().startsWith("7")) {
							hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						}else {
							hzmatFeequote = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						}
						hazmatFee = hzmatFeequote.doubleValue();
					}
					counthazmatFee = counthazmatFee + (hazmatFee * orderApprovalVo.getOrderAmount());
					if (supplierQuoteElement.getOtherFee() != null && supplierQuoteElement.getOtherFee() > 0) {
						BigDecimal otherFee = new BigDecimal(0);
						if (client.getCode().startsWith("7")) {
							otherFee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						}else {
							otherFee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(orderApprovalVo.getExchangeRate()));
						}
						if (orderApprovalVo.getClientMoq() != null && !"".equals(orderApprovalVo.getClientMoq())) {
							if (orderApprovalVo.getClientMoq() > orderApprovalVo.getOrderAmount()) {
								quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getClientMoq();
								countOther = countOther + quoteOtherFee * orderApprovalVo.getOrderAmount();
							}else {
								quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getOrderAmount();
								countOther = countOther + otherFee.doubleValue();
							}
						}else {
							quoteOtherFee = otherFee.doubleValue()/orderApprovalVo.getOrderAmount();
							countOther = countOther + otherFee.doubleValue();
						}
						
					}
					orderApprovalVo.setQuoteBankCost(new BigDecimal(bankCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					orderApprovalVo.setQuoteFeeForExchangeBill(new BigDecimal(feeForExchangeBill).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					orderApprovalVo.setQuoteHazmatFee(new BigDecimal(hazmatFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					orderApprovalVo.setQuoteOtherFee(new BigDecimal(quoteOtherFee).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					orderApprovalVo.setSupplierOrderPrice(new BigDecimal(sqePrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
					Double weatherOrderprofitMargin=(price-fixedCost-bankCharges-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getQuoteOtherFee()-orderApprovalVo.getSupplierOrderPrice())
							/(price-fixedCost-bankCharges-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-orderApprovalVo.getQuoteOtherFee());
					orderApprovalVo.setWeatherOrderprofitMargin(weatherOrderprofitMargin*100);
					orderApprovalVo.setCode(element.getCode());
					orderApprovalVo.setTotal(sqePrice*orderApprovalVo.getWeatherOrderAmount());
					supplierPrice+=(sqePrice*orderApprovalVo.getWeatherOrderAmount());
					clientPrice+=(price*orderApprovalVo.getOrderAmount());
					orderApprovalVo.setGrossProfitAmount(price-fixedCost-bankCharges-orderApprovalVo.getQuoteBankCost()-orderApprovalVo.getQuoteFeeForExchangeBill()-orderApprovalVo.getQuoteHazmatFee()-sqePrice);
					grossProfitAmount+=(orderApprovalVo.getGrossProfitAmount()*orderApprovalVo.getOrderAmount());
//					grossProfit+=orderApprovalVo.getWeatherOrderprofitMargin();
					}
					orderApprovalVo.setProfitMargin(clientProfitmargin.getProfitMargin()*100);
				}
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
	
	/*
	 * 跳转采购确认利润列表页面
	 */
	@RequestMapping(value="/purchaseConfirmProfit",method=RequestMethod.GET)
	public String confirmProfit(HttpServletRequest request) {
		request.setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
		String id= request.getParameter("clientOrderElementId");
//		ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientOrderElementId")));
//		request.setAttribute("clientOrder", clientOrder);
//		request.setAttribute("type", request.getParameter("type"));
		/*String[] ids=request.getParameter("clientOrderElementId").split(",");
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(Integer.parseInt(ids[0]));
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());*/
		List<Jbpm4Task> list = jbpm4TaskDao.getListById(request.getParameter("clientOrderElementId"));
		OrderApproval approval=	orderApprovalService.selectByPrimaryKey(list.get(0).getRelationId());
		ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(approval.getClientOrderElementId());
		request.setAttribute("clientOrderId", clientOrderElementVo.getClientOrderId());
		request.setAttribute("orderNumber", clientOrderElementVo.getOrderNumber());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
			return "/marketing/clientweatherorder/elementlist";
	}
	
	/*
	 * 采购审批库存列表页面数据
	 */
	@RequestMapping(value="/orderApprovalDate",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo valitidyDate(HttpServletRequest request,HttpServletResponse response) {
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
		
		String elementId =request.getParameter("clientOrderElementId");
		String idsForTask = "";
		if (elementId != null) {
			List<Jbpm4Task> listTask = jbpm4TaskDao.getListById(elementId);
			if (listTask.size() > 0) {
				List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.findYwTableElementId(listTask.get(0));
			    idsForTask=jbpm4Tasks.get(0).getRelationId().toString();
			    for (int i = 1; i < jbpm4Tasks.size(); i++) {
			    	 List<Jbpm4Task> list=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
			    	 if(list.size()>1){
			    		 if(jbpm4Tasks.get(i).getTaskdefname().equals("销售生成客户订单")){
			    			 continue;
						 }else if(jbpm4Tasks.get(i).getTaskdefname().equals("采购生成供应商预订单")){
							 List<Jbpm4Task> list1=jbpm4TaskDao.findOnlyYwTableElementId(jbpm4Tasks.get(i).getYwTableElementId());
							 for (Jbpm4Task jbpm4Task2 : list1) {
								if(jbpm4Task2.getTaskdefname().equals("采购审核库存")||jbpm4Task2.getTaskdefname().equals("采购审核")||jbpm4Task2.getTaskdefname().equals("总经理审核")){
									continue;
								}
							}
						 }
			    		 ;
			    	 }
			    	 idsForTask+=","+jbpm4Tasks.get(i).getRelationId();
				}
			}
		}
		
		page.put("state", request.getParameter("state"));
		//page.put("clientOrderElementId", request.getParameter("clientOrderElementId"));
		page.put("clientOrderElementId", idsForTask);
		
		
		orderApprovalService.selectByClientOrderIdPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (OrderApprovalVo orderApprovalVo : page.getEntities()) {
				ClientOrderElementVo clientOrderElementVo=clientWeatherOrderElementService.findByclientOrderELementId(orderApprovalVo.getClientOrderElementId());
				Double quotePrice=clientOrderElementVo.getSupplierQuotePrice();
				orderApprovalVo.setQuotePrice(quotePrice);//供应商报价价格
				SupplierQuoteElement element=supplierQuoteElementService.selectByPrimaryKey(clientOrderElementVo.getSupplierQuoteElementId());
				orderApprovalVo.setCode(element.getCode());
				//Double usedAmount = orderApprovalService.getUsedStorageAmount(orderApprovalVo.getSupplierQuoteElementId(), orderApprovalVo.getImportPackageElementId(), orderApprovalVo.getId());
				//orderApprovalVo.setStorageUsedAmount(usedAmount);
				if(orderApprovalVo.getType().equals(1)||orderApprovalVo.getType()==1){//在途库存
					AddSupplierOrderElementVo elementVo=supplierOrderElementService.findByElementId(orderApprovalVo.getSupplierOrderElementId());
					orderApprovalVo.setImportPartNumber(elementVo.getQuotePartNumber());
					orderApprovalVo.setImportDescription(elementVo.getQuoteDescription());
				}

				if (!orderApprovalVo.getStorageUnit().equals(orderApprovalVo.getOrderUnit())) {
					orderApprovalVo.setDifferentUnit(1);
				}

//				StorageFlowVo flowVo=importpackageElementService.findStorageBySoeIdAndIpeId(orderApprovalVo.getSupplierQuoteElementId(), orderApprovalVo.getImportPackageElementId());
//				if(orderApprovalVo.getType().equals(1)||orderApprovalVo.getType()==1){
//					SupplierOrder supplierOrder=supplierOrderService.selectBySupplierOrderElementId(orderApprovalVo.getSupplierOrderElementId());
//					orderApprovalVo.setImportPrice(orderApprovalVo.getPrice()*supplierOrder.getExchangeRate());
//				}else{
//				ClientOrder clientOrder=clientOrderService.selectByPrimaryKey(orderApprovalVo.getClientOrderId());
//				ImportPackageElementVo elementVo=importpackageElementService.findimportpackageelement(orderApprovalVo.getImportPackageElementId().toString());
//				orderApprovalVo.setImportPrice(orderApprovalVo.getPrice()*elementVo.getExchangeRate());
//				}
//				Double storagePrice=orderApprovalVo.getPrice()*elementVo.getExchangeRate();
//				Double orderPrice=orderApprovalVo.getOrderPrice()*clientOrder.getExchangeRate();
//				Double fixedCost=orderApprovalVo.getFixedCost();
//				 if(fixedCost<1){
//					 fixedCost=fixedCost*orderPrice;
//				 }
//				
//				Double profitMargin=((orderPrice-fixedCost-storagePrice)/orderPrice)*100;
//				orderApprovalVo.setProfitMargin(profitMargin);
//				orderApprovalVo.setClientProfitMargin(18.0);
//				orderApprovalVo.setOrderPrice(orderPrice);
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
	 * 文件上传预订单
	 */
	@RequestMapping(value="/weatherOrderuploadExcel",method=RequestMethod.POST)
	public @ResponseBody String weatherOrderuploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		String clientOrderId=request.getParameter("clientOrderId");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		String taskdefname=request.getParameter("taskdefname");
		MessageVo messageVo = supplierWeatherOrderElementService.uploadExcel(multipartFile, clientOrderId, taskdefname);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/*
	 * 订单上传 
	 */
	@RequestMapping(value="/finaluploadExcel",method=RequestMethod.POST)
	public @ResponseBody String finaluploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = clientWeatherOrderElementService.finalUploadExcel(multipartFile,clientOrderId,new Integer(userVo.getUserId()));
		
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/*
	 * 订单上传取消合同
	 */
	@RequestMapping(value="/uploadCancellationOrder",method=RequestMethod.POST)
	public @ResponseBody String uploadCancellationOrder(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = clientWeatherOrderElementService.uploadCancellationOrder(multipartFile,clientOrderId,new Integer(userVo.getUserId()));
		
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}

	
	/**
	 * 插入审批意见
	 * **/
	@RequestMapping(value="/insertJbyj",  method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo insertJbyj(HttpServletRequest request,
			@ModelAttribute Jbpm4Jbyj record)
	{
		
		boolean success = true;
		String message = "更新完成！";
		try {
			Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findGyJbyjByTaskId(request.getParameter("taskId"));
			 if(null!=jbpm4Jbyj){
				 jbpm4Jbyj.setJbyj(record.getJbyj());
				 jbpm4JbyjService.modify(jbpm4Jbyj);
			 }else{
					record.setCreateTime(new Date());
					record.setTaskId(request.getParameter("taskId"));
					jbpm4JbyjService.add(record);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "更新失败";
		}
		EditRowResultVo result = new EditRowResultVo(success, message);
		return result;
	}
	
	/*
	 * 采购修改订单明细
	 */
	@RequestMapping(value="/updateSupplierPrice",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateSupplierPrice(HttpServletRequest request,@ModelAttribute SupplierWeatherOrderElement element,@ModelAttribute Jbpm4Jbyj record ) {
		boolean success = false;
		String message = "";
		String destination=request.getParameter("destinationValue");
		String shipWay=request.getParameter("shipWayValue");
		String cert=request.getParameter("certificationCode");
		String cond=request.getParameter("conditionCode");
		String location=request.getParameter("location");
//		String supplierStatus=request.getParameter("supplierStatus");
		SupplierQuoteElement supplierQuoteElement=new SupplierQuoteElement();
		supplierQuoteElement.setLocation(location);
		if(null!=destination&&!"".equals(destination)){
			element.setDestination(destination);
		}
		if(null!=shipWay&&!"".equals(shipWay)){
			element.setShipWayId(new Integer(shipWay));
		}
		if(null!=cert&&!"".equals(cert)){
			supplierQuoteElement.setCertificationId(Integer.parseInt(cert));
		}
		if(null!=cond&&!"".equals(cond)){
			supplierQuoteElement.setConditionId(Integer.parseInt(cond));
		}
		String supplierWeatherOrderElementId = getString(request, "supplierWeatherOrderElementId");
		if(supplierWeatherOrderElementId!=null && !"".equals(supplierWeatherOrderElementId)){
			
//			String destination=request.getParameter("destinationValue");
//			String shipWay=request.getParameter("shipWayValue");
//			if(null!=destination&&!"".equals(destination)){
//				element.setDestination(destination);
//			}
//			if(null!=shipWay&&!"".equals(shipWay)){
//				element.setShipWayId(new Integer(shipWay));
//			}
			if(null==element.getSupplierQuoteElementId()&&element.getSupplierStatus().equals(1)){
				supplierWeatherOrderElementService.addSupplierWeatherOrder(element,supplierQuoteElement);
			}else{
				element.setId(new Integer(supplierWeatherOrderElementId));
			supplierWeatherOrderElementService.updateByPrimaryKeySelective(element);
			}
			success = true;
			message = "修改成功！";
		}else if(null==supplierWeatherOrderElementId || "".equals(supplierWeatherOrderElementId)){
			
			supplierWeatherOrderElementService.addSupplierWeatherOrder(element,supplierQuoteElement);
			success = true;
			message = "修改成功！";
		}
		insertJbyj(request,record);
		return new EditRowResultVo(success, message);
	}
	
	/*
	 * 销售修改订单明细
	 */
	@RequestMapping(value="/updateElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateElement(HttpServletRequest request,@ModelAttribute Jbpm4Jbyj record) {
		boolean success = false;
		String message = "";
		String clientOrderElementId = getString(request, "clientOrderElementId");
		if(clientOrderElementId!=null && !"".equals(clientOrderElementId)){
			ClientWeatherOrderElement clientWeatherOrderElement = new ClientWeatherOrderElement();
			clientWeatherOrderElement.setId(new Integer(clientOrderElementId));
			clientWeatherOrderElement.setAmount(new Double(getString(request, "orderAmount")));
			clientWeatherOrderElement.setPrice(new Double(getString(request, "orderPrice")));
			String orderStatus=request.getParameter("orderStatusId");
			if(null!=orderStatus){
				if(orderStatus.equals("711")){
					clientWeatherOrderElement.setOrderStatusId(64);
					ClientWeatherOrderElement clientOrderElement2 = clientWeatherOrderElementService.selectByPrimaryKey(new Integer(clientOrderElementId));
	    			ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientOrderElement2.getClientQuoteElementId());
	    			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
	    				clientInquiryElement.setElementStatusId(711);
	    			clientInquiryElementService.updateByPrimaryKeySelective(clientInquiryElement);
				}
			}
			clientWeatherOrderElementService.updateByPrimaryKeySelective(clientWeatherOrderElement);
			insertJbyj(request,record);
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		return new EditRowResultVo(success, message);
	}
	
	/*
	 * 销售作废订单明细
	 */
	@RequestMapping(value="/cancellationOrderElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo cancellationOrderElement(HttpServletRequest request,@ModelAttribute Jbpm4Jbyj record) {
		boolean success = false;
		String message = "";
		String clientOrderElementId = getString(request, "clientOrderElementId");
		if(clientOrderElementId!=null && !"".equals(clientOrderElementId)){
			ClientOrderElementFinal clientOrderElementFinal = new ClientOrderElementFinal();
			clientOrderElementFinal.setClientOrderElementId(new Integer(clientOrderElementId));
			String orderStatus=request.getParameter("orderStatusId");
			if(null!=orderStatus){
				if(orderStatus.equals("711")){
					clientOrderElementFinal.setOrderStatusId(64);
					ClientWeatherOrderElement clientOrderElement2 = clientWeatherOrderElementService.selectByPrimaryKey(new Integer(clientOrderElementId));
	    			ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientOrderElement2.getClientQuoteElementId());
	    			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
	    				clientInquiryElement.setElementStatusId(711);
	    			clientInquiryElementService.updateByPrimaryKeySelective(clientInquiryElement);
				}else if(orderStatus.equals("60")){
					clientOrderElementFinal.setOrderStatusId(60);
				}
			}
			clientOrderElementFinalService.updateStatus(clientOrderElementFinal);
			insertJbyj(request,record);
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		return new EditRowResultVo(success, message);
	}
	
	
	/*
	 * 销售生成订单明细
	 */
	@RequestMapping(value="/insertElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo insertElement(HttpServletRequest request,@ModelAttribute ClientOrderElementFinal clientOrderElementFinal,@ModelAttribute Jbpm4Jbyj record) {
		boolean success = false;
		String message = "";
		String id = getString(request, "clientOrderElementId");
		String finalPrice = getString(request, "price");
		String finalLeadTime = getString(request, "leadTime");
//		String fixedCost = getString(request, "fixedCost");
//		String certificationId = getString(request, "certificationCode");
		String finalAmount = getString(request, "amount");
		
		if(id!=null && !"".equals(id)){
			ClientWeatherOrderElement clientWeatherOrderElement = clientWeatherOrderElementService.selectByPrimaryKey(new Integer(id));
			clientOrderElementFinal.setClientOrderElementId(new Integer(id));
			clientOrderElementFinal.setDescription(clientWeatherOrderElement.getDescription());
			clientOrderElementFinal.setAmount(new Double(finalAmount));
			clientOrderElementFinal.setPrice(new Double(finalPrice));
			clientOrderElementFinal.setOrderStatusId(Integer.parseInt(request.getParameter("orderStatusId")));
//			clientOrderElementFinal.setCertificationId(Integer.parseInt(request.getParameter("certificationValue")));
			clientOrderElementFinal.setFixedCost(clientWeatherOrderElement.getFixedCost());
			clientOrderElementFinal.setBankCharges(clientWeatherOrderElement.getBankCharges());
			clientOrderElementFinal.setLeadTime(finalLeadTime);
			clientOrderElementFinal.setId(null); //测试得到的错误，不知id是否与其他表关联
//			clientOrderElementFinal.setCertificationId(Integer.parseInt(certificationId));
				if(null!=finalLeadTime&&!"".equals(finalLeadTime)){
					 Pattern pattern = Pattern.compile("[0-9]*"); 
					   Matcher isNum = pattern.matcher(finalLeadTime);
					if(null!=finalLeadTime&&!"".equals(finalLeadTime)&&isNum.matches()){
					GregorianCalendar gc=new GregorianCalendar(); 
					gc.setTime(new Date()); 
					gc.add(5,Integer.parseInt(finalLeadTime));
					gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
					Date deadline=gc.getTime();
					clientOrderElementFinal.setDeadline(deadline);
					}
				}
			ClientOrderElementFinal clientOrderElementFinal2=clientOrderElementFinalService.selectByPrimaryKey(clientOrderElementFinal.getClientOrderElementId());
			if(null==clientOrderElementFinal2){
				clientOrderElementFinalService.insert(clientOrderElementFinal);
			}else{
				clientOrderElementFinal.setId(clientOrderElementFinal2.getId());
				clientOrderElementFinalService.updateByPrimaryKeySelective(clientOrderElementFinal);
			}
			insertJbyj(request,record);
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		return new EditRowResultVo(success, message);
	}
	
	
	/*
	 * 订单上传 
	 */
	@RequestMapping(value="/wetherorderuploadExcel",method=RequestMethod.POST)
	public @ResponseBody String wetherorderuploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = clientWeatherOrderElementService.wetherorderuploadExcel(multipartFile,clientOrderId,new Integer(userVo.getUserId()));
		
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * 修改描述后新增订单明细
	 */
	@RequestMapping(value="/insertDate",method=RequestMethod.POST)
	public @ResponseBody ResultVo insertDate(HttpServletRequest request,HttpServletResponse response) {
		UserVo userVo=getCurrentUser(request);
		 List<ClientOrderElement>  clientOrderElements=clientOrderElementNotmatchService.selectByUserId(new Integer(userVo.getUserId()));
		 MessageVo messageVo= clientWeatherOrderElementService.addClientOrder(clientOrderElements);
		 clientOrderElementNotmatchService.deleteByUserId(new Integer(userVo.getUserId()));
		 return new ResultVo(messageVo.getFlag(),messageVo.getMessage());
	}
	
	/*
	 * 货币信息
	 */
	@RequestMapping(value="/findCurrency",method=RequestMethod.POST)
	public @ResponseBody ResultVo findCurrency(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		Integer id =new Integer(getString(request, "id"));
		ClientOrderVo clientOrderVo = clientWeatherOrderService.findById(id);
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
		ClientOrderVo clientOrderVo = clientWeatherOrderService.findById(id);
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
	
	
	@RequestMapping(value="/toAddFee",method=RequestMethod.GET)
	public String toAddFee(HttpServletRequest request){
		PageModel<SupplierWeatherOrder> page = new PageModel<SupplierWeatherOrder>();
		String ids =request.getParameter("clientOrderElementIds");
		String taskdefname =request.getParameter("taskdefname");
		String[] id=ids.split(",");
		String oaId="0";
		for (String string : id) {
			OrderApproval approval=orderApprovalService.selectByPrimaryKey(Integer.parseInt(string));
			oaId=oaId+","+approval.getClientOrderElementId();
		}
		page.put("clientOrderElementId", oaId);
		if(taskdefname.equals("add")){
			taskdefname="采购生成供应商预订单";
		}else if(taskdefname.equals("update")){
			taskdefname="采购询问供应商能否降价";
		}
		page.put("taskdefname", taskdefname);
		List<SupplierWeatherOrder> list = supplierWeatherOrderElementService.getFeeInfo(page);
		request.setAttribute("list", list);
		return "/marketing/clientweatherorder/addFeeTable";
	}
	
	
	@RequestMapping(value="/saveFee",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveFee(HttpServletRequest request,@ModelAttribute SupplierWeatherOrder supplierWeatherOrder){
		try {
			boolean success = supplierWeatherOrderElementService.saveFee(supplierWeatherOrder.getList());
			if (success) {
				return new ResultVo(true, "保存成功！");
			}else {
				return new ResultVo(false, "保存失败！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "保存异常！");
		}
		
	}
	
	/**
	 * 跳转价格验证页面
	 * @return
	 */
	@RequestMapping(value="/toCheckTotal",method=RequestMethod.GET)
	public String toCheckTotal(){
		return "/marketing/clientweatherorder/checkTotalPrice";
	}
	
	/**
	 * 价格验证页面
	 * @return
	 */
	@RequestMapping(value="/checkTotal",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkTotal(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			Double tprice = new Double(getString(request, "total"));
			Double total = clientWeatherOrderElementService.getTotalPrice(id);
			if (tprice.doubleValue() == total.doubleValue()) {
				return new ResultVo(true, "验证通过！");
			}else {
				return new ResultVo(false, "填写总价与系统计算总价不相等,请核实订单价格与数量再发起审批！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "验证异常！");
		}
	}
	
	/**
	 * 紧急程度
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/urgentList",method=RequestMethod.POST)
	public @ResponseBody ResultVo urgentList(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		String id=getString(request, "id");
		List<SystemCode> list2=systemCodeService.findType("URGENT_LEVEL");
		List<SystemCode> list=new ArrayList<SystemCode>();
		if (id != null && !"".equals(id)) {
			for (SystemCode systemCode2 : list2) {
				if (systemCode2.getId().toString().equals(id)) {
					list.add(systemCode2);
					break;
				}
			}
			for (SystemCode systemCode2 : list2) {
				if (!systemCode2.getId().toString().equals(id)) {
					list.add(systemCode2);
				}
			}
		}else {
			list = list2;
		}
		
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	
	/**
	 * 发送库存转订单的通知邮件
	 * @return
	 */
	@RequestMapping(value="/storageToOrderEmail",method=RequestMethod.POST)
	public @ResponseBody ResultVo storageToOrderEmail(){
		synchronized(this){
			try {
				List<StorageToOrderEmail> users = clientWeatherOrderService.getUnfinishListUser();
				clientWeatherOrderService.sendStorageToOrderEmail(users);
				return new ResultVo(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultVo(true, "");
			}
		}
		
	}
	
	/**
	 * 修改出库使用的数量
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editUseStorageAmount",method=RequestMethod.POST)
	public @ResponseBody ResultVo editUseStorageAmount(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			Double amount = new Double(getString(request, "amount"));
			OrderApproval orderApproval = new OrderApproval();
			orderApproval.setAmount(amount);
			orderApproval.setId(id);
			orderApprovalService.updateByPrimaryKeySelective(orderApproval);
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改失败！");
		}
	}
	
	/**
	 * 跳转填写备注页面
	 * @return
	 */
	@RequestMapping(value="/toAddRemark",method=RequestMethod.GET)
	public String toAddRemark(){
		return "/marketing/clientweatherorder/addremark";
	}
	
	
	/**
	 * 批量取消供应商预订单
	 * @param request
	 * @param element
	 * @return
	 */
	@RequestMapping(value="/cancelSupplierOrder",method=RequestMethod.POST)
	public @ResponseBody ResultVo cancelSupplierOrder(HttpServletRequest request,@ModelAttribute SupplierWeatherOrderElement element){
		try {
			for (int i = 0; i < element.getList().size(); i++) {
				supplierWeatherOrderElementService.addSupplierWeatherOrder(element.getList().get(i),new SupplierQuoteElement());
			}
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
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
