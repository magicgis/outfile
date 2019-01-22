package com.naswork.module.purchase.controller.supplierorder;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Primary;
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
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ClientWeatherOrderElement;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.PurchaseApplicationForm;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierOrderElementFj;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.service.AuthorityRelationService;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientQuoteElementService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientWeatherOrderElementService;
import com.naswork.service.ExchangeRateService;
import com.naswork.service.HistoricalQuotationService;
import com.naswork.service.ImportPackagePaymentElementService;
import com.naswork.service.ImportPackagePaymentService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.MpiService;
import com.naswork.service.OnPassageStroageService;
import com.naswork.service.PurchaseApplicationFormService;
import com.naswork.service.SupplierOrderElementFjService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SupplierQuoteElementService;
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
@RequestMapping(value="/purchase/supplierorder")
public class SupplierOrderController extends BaseController{

	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private UserService userService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private PurchaseApplicationFormService purchaseApplicationFormService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private ImportPackagePaymentService importPackagePaymentService;
	@Resource
	private ImportPackagePaymentElementService importPackagePaymentElementService;
	@Resource
	private OnPassageStroageService onPassageStroageService;
	@Resource
	private HistoricalQuotationService historicalQuotationService;
	@Resource
	private AuthorityRelationService authorityRelationService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	@Resource
	private ClientQuoteElementService clientQuoteElementService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SupplierWeatherOrderElementService supplierWeatherOrderElementService;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElemenetService;
	@Resource
	private ClientWeatherOrderElementService clientWeatherOrderElementService;
	@Resource
	private ImportPackageService importPackageService;
	@Resource
	private MpiService mpiService;
	@Resource
	private SupplierOrderElementFjService supplierOrderElementFjService;
	@Resource
	private ExchangeRateService exchangeRateService;
	
	
	public static final int AVERAGE_PRICE_COUNT = 3;
	
	/*
	 * 新增供应商订单列表
	 */
	@RequestMapping(value="/addSupplierOrderPage",method=RequestMethod.GET)
	public String addSupplierOrderPage() {
		return "/purchase/supplierorder/addSupplierOrderList";
	}
	
	
	/*
	 * 新增供应商订单列表数据
	 */
	@RequestMapping(value="/listSupplierOrderPage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listSupplierOrderPage(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		StringBuffer where = new StringBuffer();
		UserVo userVo=getCurrentUser(request);
		where.append("co.order_status_id!='64' and co.order_status_id!='62' and co.complete=1");
		if ("".equals(where)) {
			where = null;
		}
		if (request.getParameter("searchString")!=null&&!"".equals(request.getParameter("searchString"))) {
			where.append(" and ").append(request.getParameter("searchString").trim());
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		GridSort sort = getSort(request);
		
		clientOrderService.listPage(page, where.toString(), sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderVo clientOrderVo: page.getEntities()) {
				PurchaseApplicationForm purchaseApplicationForm = purchaseApplicationFormService.findByClientOrderId(clientOrderVo.getId());
				if (purchaseApplicationForm!=null) {
					clientOrderVo.setPurchaseApply(1);
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
	 * 新增供应商订单明细
	 */
	@RequestMapping(value="/addSupplierOrderElement",method=RequestMethod.GET)
	public String addSupplierOrderElement(HttpServletRequest request) {
		String orderNumber = getString(request, "orderNumber");
		Integer id = new Integer(getString(request, "id"));
		
		request.setAttribute("clientOrderId", id);
		request.setAttribute("orderNumber", orderNumber);
		String clientId = getString(request, "clientId");
		request.setAttribute("clientId", getString(request, "clientId"));
		return "/purchase/supplierorder/supplierOrderElementList";
	}
	
	/*
	 * 新增供应商订单明细-客户模块数据
	 */
	@RequestMapping(value="/ListClientOrderElement",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo ListClientOrderElement(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		StringBuffer where = new StringBuffer();
		Integer clientOrderId =new Integer(getString(request, "clientOrderId"));
		page.put("id", clientOrderId);
		page.put("where", "coe.amount != 0");
		supplierOrderService.ClientOrderPage(page);
		UserVo userVo = getCurrentUser(request);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElementVo clientOrderElementVo: page.getEntities()) {
				List<Map<String,Object>> rows = supplierOrderService.AddSupplierOrderPage(clientOrderElementVo.getId(),userVo.getUserId());
				clientOrderElementVo.setSupplierOrderCount(rows.size());
				clientOrderElementVo.setClientOrderLeadTime(clientOrderElementVo.getClientOrderLeadTime());
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElementVo);
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
	 * 新增供应商订单明细-供应商模块数据
	 */
	@RequestMapping(value="/ListSupplierOrderElement",method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> getSubListData(HttpServletRequest request){
		Map<String, Object> m = new HashMap<String, Object>();
		try{
			Integer clientOrderElementId =new Integer(getString(request, "id"));
			UserVo userVo = getCurrentUser(request);
			List<Map<String,Object>> rows = supplierOrderService.AddSupplierOrderPage(clientOrderElementId,userVo.getUserId());
			
			m.put("rows", rows);
			m.put("total", 1);
			m.put("page", 1);
			m.put("records", 1);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return m;
	}
	
	/*
	 * 新增订单页面
	 */
	@RequestMapping(value="/addSupplierOrder",method=RequestMethod.GET)
	public String addSupplierOrder(HttpServletRequest request) {
		Integer id =new Integer(getString(request, "id"));
		Integer clientQuoteElementId = new Integer(getString(request, "clientQuoteElementId"));
		String quotePartNumber = getString(request, "quotePartNumber");
		Double storageAmount = supplierOrderElementService.getStorageAmount(quotePartNumber);
		String orderNumber = getString(request, "orderNumber");
		String weather=request.getParameter("weather");
		ClientOrderElementVo clientOrderElementVo =new ClientOrderElementVo();
		if(null!=weather&&!"".equals(weather)){
			clientOrderElementVo=supplierWeatherOrderElementService.findById(id);
		}else{
			clientOrderElementVo = supplierOrderService.findById(id);
		}
		if (storageAmount==null) {
			clientOrderElementVo.setStorageAmount(0.0);
		}else {
			clientOrderElementVo.setStorageAmount(storageAmount);
		}
		ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientQuoteElementId);
		ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
		SupplierQuoteElement supplierQuoteElement = supplierQuoteElementService.selectByPrimaryKey(clientQuoteElement.getSupplierQuoteElementId());
		Integer cieElementId = supplierOrderService.getElementId(clientQuoteElementId);
		Integer sqeElementId = supplierOrderService.getSqeElementId(clientQuoteElementId);
		String quoteNumber = getString(request, "quoteNumber");
		
		clientOrderElementVo.setSupplierOrderAmount(supplierOrderElementService.getAmountByCLientElementId(clientOrderElementVo.getId()));
		PageModel<SupplierListVo> page=getPage(request);
		page.put("cieElementId", cieElementId);
		page.put("sqeElementId", sqeElementId);
		page.put("ciieElementId", cieElementId);
		page.put("quoteNumber", quoteNumber);
		page.put("shortPartNumber", supplierQuoteElement.getShortPartNumber());
		DecimalFormat df = new DecimalFormat("0.00");
		
		List<SupplierListVo> list = supplierOrderService.SupplierListMessage(page);
		//记录总价
		double price = 0;
		double p = supplierOrderService.getMinPrice(page);
		BigDecimal lowPrice = null;
		int count = 0;
		
		for (SupplierListVo supplierListVo : list) {
			price+=supplierListVo.getPrice();
			count++;
		}
		if (list.size() > 0) {
			lowPrice=new BigDecimal(p);
			//平均价格
			//BigDecimal averagePrice = new BigDecimal(price/count).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal atPrice = new BigDecimal(price);
			BigDecimal averagePrice = BigDecimal.ZERO;
			/*if (list.size() >= AVERAGE_PRICE_COUNT) {
				averagePrice = atPrice.divide(new BigDecimal(
						AVERAGE_PRICE_COUNT), 2, BigDecimal.ROUND_HALF_UP);
			} else {
				averagePrice = atPrice.divide(new BigDecimal(list.size()), 2,
						BigDecimal.ROUND_HALF_UP);
			}*/
			averagePrice = atPrice.divide(new BigDecimal(list.size()), 2,
					BigDecimal.ROUND_HALF_UP);
			
			BigDecimal compareRate;
			if (lowPrice == null || lowPrice == null) {
				compareRate = null;
			} else if (averagePrice.equals(BigDecimal.ZERO)
					|| lowPrice.equals(BigDecimal.ZERO)) {
				compareRate = null;
			} else {
				compareRate = lowPrice.divide(averagePrice, 2,
						BigDecimal.ROUND_HALF_UP);
			}
			if (compareRate!=null) {
				request.setAttribute("compareRate", compareRate.multiply(new BigDecimal(100)));
			}
			request.setAttribute("averagePrice", df.format(averagePrice));
			request.setAttribute("lowPrice", df.format(lowPrice));
		}
		
		Integer currencyId = new Integer(getString(request, "currencyId"));
		Double exchangeRate = new Double(getString(request, "exchangeRate"));
		
		
		request.setAttribute("currencyId", currencyId);
		request.setAttribute("exchangeRate", exchangeRate);
		
		
		request.setAttribute("clientOrderElementVo", clientOrderElementVo);
		request.setAttribute("id", id);
		request.setAttribute("clientQuoteElementId", clientQuoteElementId);
		request.setAttribute("orderNumber", orderNumber);
		request.setAttribute("quoteNumber", quoteNumber);
		request.setAttribute("weather", request.getParameter("weather"));
		request.setAttribute("type", request.getParameter("type"));
		request.setAttribute("supplierWeatherOrderElementId", request.getParameter("supplierWeatherOrderElementId"));
		return "/purchase/supplierorder/addSupplierOrder";
	}
	
	/**
	 * 订单页面新增供应商报价
	 * @return
	 */
	@RequestMapping(value="/toAddQuotePrice",method=RequestMethod.GET)
	public String toAddQuotePrice(HttpServletRequest request){
		String type = getString(request, "type");
		String clientOrderElementId = getString(request, "id");
		if ("weather".equals(type)) {
			ClientWeatherOrderElement clientWeatherOrderElement = clientWeatherOrderElementService.selectByPrimaryKey(new Integer(clientOrderElementId));
			ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			request.setAttribute("clientInquiryElement", clientInquiryElement);
		}else if("order".equals(type)){
			ClientOrderElement clientOrderElement = clientOrderElementService.selectByPrimaryKey(new Integer(clientOrderElementId));
			ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientOrderElement.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			request.setAttribute("clientInquiryElement", clientInquiryElement);
		}
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());  
        cal.add(Calendar.DATE, +29);
        request.setAttribute("validity", cal.getTime());
		return "/purchase/supplierorder/addQuotePrice";
	}
	
	
	/*
	 * 新增或修改报价
	 */
	@RequestMapping(value="/addQuotePrice",method=RequestMethod.POST)
	public @ResponseBody ResultVo addQuotePrice(HttpServletRequest request,@ModelAttribute SupplierQuoteElement supplierQuoteElement){
		UserVo userVo = getCurrentUser(request);
		try {
			String price = getString(request, "price");
			String clientOrderElementId = getString(request, "clientOrderElementId");
			String type = getString(request, "type");
			Integer clientQuoteElementId = null;
			if ("order".equals(type)) {
				ClientOrderElement clientOrderElement = clientOrderElementService.selectByPrimaryKey(new Integer(clientOrderElementId));
				ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientOrderElement.getClientQuoteElementId());
				clientQuoteElementId = clientQuoteElement.getId();
			}else if ("weather".equals(type)) {
				ClientWeatherOrderElement clientWeatherOrderElement = clientWeatherOrderElementService.selectByPrimaryKey(new Integer(clientOrderElementId));
				ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientWeatherOrderElement.getClientQuoteElementId());
				clientQuoteElementId = clientQuoteElement.getId();
			}
			
			boolean success = supplierQuoteElementService.addQuotePriceInOrder(supplierQuoteElement, clientQuoteElementId, price, userVo);
			if (success) {
				return new ResultVo(success, "新增成功！");
			}else{
				return new ResultVo(success, "新增失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
	}
	
	
	/**
	 * 算截止日期
	 */
	@RequestMapping(value="/deadline",method=RequestMethod.POST)
	public @ResponseBody ResultVo deadline(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		String leadTime=request.getParameter("leadTime");
		Integer day=Integer.parseInt(leadTime);
		GregorianCalendar gc=new GregorianCalendar(); 
		gc.setTime(Calendar.getInstance().getTime()); 
		gc.add(5,day); 
		gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
		Date time=gc.getTime();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String deadline=dateFormat.format(time);
		return new ResultVo(success, deadline);
	}
	
	/*
	 * 新增订单
	 */
	@RequestMapping(value="/saveSupplierOrder",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveSupplierOrder(HttpServletRequest request,@ModelAttribute SupplierOrderElement supplierOrderElement,
			@ModelAttribute SupplierOrder supplierOrder) {
		boolean success = false;
		String message = "";
		String add = getString(request, "add");
		String coeId=request.getParameter("coeId");
		UserVo userVo=getCurrentUser(request);
		List<EmailVo> emailVos=new ArrayList<EmailVo>();
		Double storageAmount =0.0;
		List<StorageFlowVo> supplierList =new ArrayList<StorageFlowVo>();
		List<StorageFlowVo> flowVos=importpackageElementService.findStorageBySupplierQuoteElementId(supplierOrderElement.getSupplierQuoteElementId());
	 	if(flowVos.size()>0){
	 		for (StorageFlowVo storageFlowVo : flowVos) {
				if(storageFlowVo.getStorageAmount()>0){
					storageAmount+=storageFlowVo.getStorageAmount();
					supplierList.add(storageFlowVo);
				}
				if(storageAmount==supplierOrderElement.getAmount()){
					break;
				}
			}
	 	}
		if (!"".equals(supplierOrderElement.getPrice())&&!"".equals(supplierOrder.getCurrencyId())) {
			if(storageAmount>0){
				ClientOrderElement clientOrderElement=new ClientOrderElement();
				clientOrderElement.setId(supplierOrderElement.getClientOrderElementId());
				clientOrderElement.setAmount(supplierOrderElement.getAmount());
				clientOrderElement.setClientOrderId(supplierOrder.getClientOrderId());
				ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(supplierOrder.getClientOrderId());
				clientOrder.setReplenishment(add);
				List<EmailVo> emailVo=new ArrayList<EmailVo>();
				 emailVo=clientOrderElementService.Storage(supplierList, clientOrderElement, clientOrder);
				 emailVos.addAll(emailVo);
				 clientOrderElementService.sendEmail(emailVos, userVo.getUserId());
		 	}else{
				SupplierOrder supplierOrder2 = supplierOrderService.findByClientOrderId(supplierOrder);
				ClientOrderElement clientOrderElement = clientOrderElementService.selectByPrimaryKey(supplierOrderElement.getClientOrderElementId());
				if (clientOrderElement.getAmount() > supplierOrderElement.getAmount()) {
					ClientOrderElement clientOrderElement2 = new ClientOrderElement(clientOrderElement.getOrderStatusId(), clientOrderElement.getClientOrderId(), clientOrderElement.getClientQuoteElementId()
							, clientOrderElement.getPrice(), clientOrderElement.getLeadTime(), clientOrderElement.getDeadline(), clientOrderElement.getDescription(), clientOrderElement.getRemark(),clientOrderElement.getElementStatusId());
					clientOrderElement2.setAmount(supplierOrderElement.getAmount());
					clientOrderElement2.setSplitReceive(1);
					clientOrderElement2.setOriginalAmount(clientOrderElement.getAmount());
					if (clientOrderElement.getUnit() != null) {
						clientOrderElement2.setUnit(clientOrderElement.getUnit());
					}
					if (clientOrderElement.getPartNumber() != null) {
						clientOrderElement2.setPartNumber(clientOrderElement.getPartNumber());
					}
					if (clientOrderElement.getBankCharges() != null) {
						clientOrderElement2.setBankCharges(clientOrderElement.getBankCharges());
					}
					if (clientOrderElement.getCertificationId() != null) {
						clientOrderElement2.setCertificationId(clientOrderElement.getCertificationId());
					}
					clientOrderElementService.insertSelective(clientOrderElement2);
					BigDecimal a = new BigDecimal(Double.toString(clientOrderElement2.getAmount()));   
					BigDecimal b = new BigDecimal(Double.toString(clientOrderElement.getAmount()));
					Double restAmount = b.subtract(a).doubleValue();
					clientOrderElement.setOriginalAmount(clientOrderElement.getAmount());
					clientOrderElement.setAmount(restAmount);
					clientOrderElement.setSplitReceive(1);
					clientOrderElementService.updateByPrimaryKeySelective(clientOrderElement);
					supplierOrderElement.setClientOrderElementId(clientOrderElement2.getId());
				}
				if (supplierOrder2!=null && "off".equals(add)&&null==supplierOrder2.getOrderType()) {
					supplierOrderElement.setSupplierOrderId(supplierOrder2.getId());
					if (supplierOrder2.getOrderType() != null && supplierOrder2.getOrderType().equals(1)) {
						supplierOrderElement.setElementStatusId(710);
					}else {
						supplierOrderElement.setElementStatusId(705);
					}
					supplierOrderElementService.insertSelective(supplierOrderElement);
				}else {
					supplierOrder.setOrderDate(new Date());
					ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(supplierOrder.getClientOrderId());
					Supplier supplier=supplierService.selectByPrimaryKey(supplierOrder.getSupplierId());
					supplierOrder.setPrepayRate(supplier.getPrepayRate());
					supplierOrder.setShipPayRate(supplier.getShipPayRate());
					supplierOrder.setReceivePayPeriod(supplier.getReceivePayPeriod());
					supplierOrder.setReceivePayRate(supplier.getReceivePayRate());
					supplierOrder.setCreateUserId(Integer.parseInt(userVo.getUserId()));
					supplierOrder.setUrgentLevelId(clientOrder.getUrgentLevelId());
					supplierOrderService.insertSelective(supplierOrder);
					supplierOrderElement.setSupplierOrderId(supplierOrder.getId());
					if (supplierOrder.getOrderType() != null && supplierOrder.getOrderType().equals(1)) {
						supplierOrderElement.setElementStatusId(710);
					}else {
						supplierOrderElement.setElementStatusId(705);
					}
					supplierOrderElementService.insertSelective(supplierOrderElement);
				}
		 	}
			//新增在途库存
			ClientOrderElement clientOrderElement = clientOrderElementService.selectByPrimaryKey(supplierOrderElement.getClientOrderElementId());
			Double otherSupplierAmount = supplierOrderElementService.getOtherOrderAmount(clientOrderElement.getId(), supplierOrderElement.getId());
			BigDecimal a = new BigDecimal(Double.toString(supplierOrderElement.getAmount()));   
			BigDecimal b = new BigDecimal(Double.toString(otherSupplierAmount));
			Double totalSupplierAmount = a.add(b).doubleValue();
			if (totalSupplierAmount > clientOrderElement.getAmount()) {
				OnPassageStorage onPassageStroage = new OnPassageStorage();
				onPassageStroage.setClientOrderElementId(clientOrderElement.getId());
				onPassageStroage.setSupplierOrderElementId(supplierOrderElement.getId());
				BigDecimal b1 = new BigDecimal(Double.toString(clientOrderElement.getAmount()));   
				BigDecimal b2 = new BigDecimal(Double.toString(otherSupplierAmount));
				double amount = b2.subtract(b1).doubleValue();
				if (amount >= 0) {
					onPassageStroage.setAmount(new Double(0));
				}else if(amount < 0){
					BigDecimal b3 = new BigDecimal(Double.toString(clientOrderElement.getAmount()));   
					BigDecimal b4 = new BigDecimal(Double.toString(otherSupplierAmount));
					double onpass = b3.subtract(b4).doubleValue();
					onPassageStroage.setAmount(onpass);
				}
				onPassageStroage.setImportStatus(0);
				onPassageStroageService.insertSelective(onPassageStroage);
			}
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	
	/*
	 * 供应商列表
	 */
	@RequestMapping(value="/SupplierList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo SupplierList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierListVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		StringBuffer where = new StringBuffer();
		Integer clientQuoteElementId = new Integer(getString(request, "clientQuoteElementId"));
		String orderNumber = getString(request, "orderNumber");
		String clientOrderId=request.getParameter("clientOrderId");
		String clientOrderElementId=request.getParameter("clientOrderElementId");
		Integer cieElementId = supplierOrderService.getElementId(clientQuoteElementId);
		Integer sqeElementId = supplierOrderService.getSqeElementId(clientQuoteElementId);
		String quoteNumber = getString(request, "quoteNumber");
		String clientOrderAmount=request.getParameter("clientOrderAmount");
		ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientQuoteElementId);
		ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
		SupplierQuoteElement supplierQuoteElement = supplierQuoteElementService.selectByPrimaryKey(clientQuoteElement.getSupplierQuoteElementId());
		page.put("cieElementId", cieElementId);
		page.put("sqeElementId", sqeElementId);
		page.put("ciieElementId", cieElementId);
		page.put("quoteNumber", quoteNumber);
		page.put("shortPartNumber", supplierQuoteElement.getShortPartNumber());
		
		supplierOrderService.SupplierListPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierListVo supplierListVo: page.getEntities()) {
				PageModel<StorageDetailVo> pageModel = new PageModel<StorageDetailVo>();
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("sqe.id = ").append(supplierListVo.getId()).append(" and c.name = 'KC'");
				pageModel.put("where", stringBuffer.toString());
				List<StorageDetailVo> storageDetailVos = importpackageElementService.getStorageAmountByQuote(pageModel);
				//库存
				Double storageAmount = 0.0;
				for (StorageDetailVo storageDetailVo2 : storageDetailVos) {
					if (storageDetailVo2.getStorageAmount() != null) {
						storageAmount += storageDetailVo2.getStorageAmount();
					}
				}
				supplierListVo.setStorageAmount(storageAmount);
				//在途库存
				Double onpass = 0.0;
				PageModel<OnPassageStorage> onPassagePage = new PageModel<OnPassageStorage>();
			    onPassagePage.put("where", "sqe.id = "+supplierListVo.getId());
				List<OnPassageStorage> onPassageStroages = onPassageStroageService.selectOnPassageAmount(onPassagePage);
				for (int i = 0; i < onPassageStroages.size(); i++) {
					onpass += onPassageStroages.get(i).getAmount();
				}
				supplierListVo.setOnPassageAmount(onpass);
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierListVo);
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
	 * 供应商订单管理
	 */
	@RequestMapping(value="/SupplierOrder",method=RequestMethod.GET)
	public String SupplierOrder(HttpServletRequest request) {
		request.setAttribute("userId", getCurrentUser(request).getUserId());
		return "/purchase/supplierorder/supplierOrderManageList";
	}
	
	/*
	 * 供应商订单管理数据
	 */
	@RequestMapping(value="/SupplierOrderData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo SupplierOrderData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierOrderManageVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = request.getParameter("searchString");
		String partNumber = getString(request, "partNumber");
		GridSort sort = getSort(request);
		UserVo userVo=getCurrentUser(request);
		if ("".equals(where)) {
			where = null;
		}
		if (!"".equals(partNumber)) {
			page.put("partNumber", partNumber);
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		
		supplierOrderService.SupplierOrderManagePage(page, where,sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierOrderManageVo supplierOrderManageVo: page.getEntities()) {
				SupplierOrderManageVo percent = supplierOrderService.getPaymentPercent(supplierOrderManageVo.getId());
				if (percent != null) {
					DecimalFormat df = new DecimalFormat("######0.00");
					supplierOrderManageVo.setAmountPercent(df.format(percent.getPaymentAmount()/percent.getOrderAmount()*100)+"%");
					supplierOrderManageVo.setTotalPercent(df.format(percent.getPaymentTotal()/percent.getOrderTotal()*100)+"%");
				}else {
					supplierOrderManageVo.setAmountPercent("0%");
					supplierOrderManageVo.setTotalPercent("0%");
				}
				supplierOrderManageVo.setTotal(supplierOrderElementService.getTotalByOrder(supplierOrderManageVo.getId()));
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierOrderManageVo);
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
	 * 供应商订单管理明细
	 */
	@RequestMapping(value="/SupplierOrderElement",method=RequestMethod.GET)
	public String SupplierOrderElement(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		SupplierOrderManageVo supplierOrderManageVo = supplierOrderService.findMessageById(id);
		request.setAttribute("supplierOrderManageVo", supplierOrderManageVo);
		return "/purchase/supplierorder/SupplierOrderManageElement";
	}
	
	/*
	 * 供应商订单明细数据
	 */
	@RequestMapping(value="/SupplierOrderElementData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo SupplierOrderElementData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<AddSupplierOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		Integer id = new Integer(getString(request, "id"));
		page.put("id", id);
		
		supplierOrderService.SupplierOrderElement(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (AddSupplierOrderElementVo addSupplierOrderElementVo: page.getEntities()) {
				DecimalFormat df = new DecimalFormat("######0.00");
				SupplierOrderElement supplierOrderElement = supplierOrderElementService.getPaymentMessage(addSupplierOrderElementVo.getId());
				AddSupplierOrderElementVo addSupplierOrderElementVo2 = supplierOrderElementService.getImportDateBySoeId(addSupplierOrderElementVo.getId());
				if (supplierOrderElement != null) {
					addSupplierOrderElementVo.setPaymentTotal(supplierOrderElement.getPrice());
					addSupplierOrderElementVo.setPaymentDate(supplierOrderElement.getPaymentDate());
				}else {
					addSupplierOrderElementVo.setPaymentTotal(0.00);
				}
				if (addSupplierOrderElementVo2.getImportDate() != null) {
					addSupplierOrderElementVo.setImportDate(addSupplierOrderElementVo2.getImportDate());
				}
				List<String> importNumbers = supplierOrderElementService.getImportNumberById(addSupplierOrderElementVo.getId());
				StringBuffer stringBuffer = new StringBuffer();
				for (String string : importNumbers) {
					stringBuffer.append(string).append(",");
				}
				if (stringBuffer.length() > 1) {
					stringBuffer.deleteCharAt(stringBuffer.length()-1);
					addSupplierOrderElementVo.setImportNumber(stringBuffer.toString());
				}
				Double importAmount = supplierOrderElementService.getImportAmount(addSupplierOrderElementVo.getId());
				if (importAmount != null) {
					addSupplierOrderElementVo.setImportAmount(importAmount);
				}
				
				//入库费用
				Double importFee = 0.00;
				Double importFreight = 0.00;
				List<ImportPackageElement> list = importpackageElementService.getBySoeId(addSupplierOrderElementVo.getId());
				for (int i = 0; i < list.size(); i++) {
					ImportPackage importPackage = importPackageService.getFeeMessage(list.get(i).getImportPackageId());
					if (importPackage.getImportFee() != null && importPackage.getTotalAmount() > 0 && importPackage.getImportFee() > 0 && importPackage.getExchangeRate() != null) {
						if (!addSupplierOrderElementVo.getCurrencyId().equals(importPackage.getCurrencyId())) {
							ExchangeRate exchangeRate = exchangeRateService.selectByPrimaryKey(addSupplierOrderElementVo.getCurrencyId());
							Double rate = importPackage.getExchangeRate()/exchangeRate.getRate();
							importFee = importFee + importPackage.getImportFee()*rate/importPackage.getTotalAmount()*list.get(i).getAmount();
						}else {
							importFee = importFee + importPackage.getImportFee()/importPackage.getTotalAmount()*list.get(i).getAmount();
						}
						
					}
					if (importPackage.getFreight() != null && importPackage.getTotalAmount() > 0 && importPackage.getFreight() > 0  && importPackage.getExchangeRate() != null) {
						if (!addSupplierOrderElementVo.getCurrencyId().equals(importPackage.getCurrencyId())) {
							ExchangeRate exchangeRate = exchangeRateService.selectByPrimaryKey(addSupplierOrderElementVo.getCurrencyId());
							Double rate = importPackage.getExchangeRate()/exchangeRate.getRate();
							importFreight = importFreight + importPackage.getFreight()*rate/importPackage.getTotalAmount()*list.get(i).getAmount();
						}else {
							importFreight = importFreight + importPackage.getFreight()/importPackage.getTotalAmount()*list.get(i).getAmount();
						}
					}
				}
				addSupplierOrderElementVo.setImportFee(new BigDecimal(importFee).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				addSupplierOrderElementVo.setImportFreight(new BigDecimal(importFreight).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				//是否有附件
				PageModel<String> pageModel = new PageModel<String>();
				pageModel.put("id", "'%"+addSupplierOrderElementVo.getId()+"%'");
				List<SupplierOrderElementFj> fjList = supplierOrderElementFjService.getByOrderId(pageModel);
				if (fjList != null && fjList.size() > 0) {
					addSupplierOrderElementVo.setHasAttach(1);
				}
//				Integer supplierOrderELementId=supplierOrderElementService.getSupplierOrderELementId(addSupplierOrderElementVo.getId());
//				if(null==supplierOrderELementId||supplierOrderELementId.equals(0)){
//					addSupplierOrderElementVo.setOrderType("合同订单");
//				}else{
//					if(!supplierOrderELementId.equals(addSupplierOrderElementVo.getId())){
//						addSupplierOrderElementVo.setOrderType("库存订单");
//					}else if(supplierOrderELementId.equals(addSupplierOrderElementVo.getId())){
//						addSupplierOrderElementVo.setOrderType("合同订单");
//					}
//				}
				Map<String, Object> map = EntityUtil.entityToTableMap(addSupplierOrderElementVo);
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
	 * 生成附件ID
	 */
	@RequestMapping(value="/createFjId",method=RequestMethod.POST)
	public @ResponseBody ResultVo createFjId(HttpServletRequest request) {
		try {
			String ids = getString(request, "ids");
			List<SupplierOrderElementFj> list = supplierOrderElementFjService.selectForeignKey(ids);
			if (list != null && list.size() > 0) {
				return new ResultVo(true, list.get(0).getId().toString());
			}else {
				SupplierOrderElementFj supplierOrderElementFj = new SupplierOrderElementFj();
				supplierOrderElementFj.setSupplierOrderElementIds(ids);
				supplierOrderElementFjService.insertSelective(supplierOrderElementFj);
				return new ResultVo(true, supplierOrderElementFj.getId().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "关联异常！");
		}
	}
	
	/*
	 * 文件管理
	 */
	@RequestMapping(value="/file",method=RequestMethod.GET)
	public String file(HttpServletRequest request) {
		String id = "";
		String ids = getString(request, "id");
		PageModel<String> page = new PageModel<String>();
		page.put("id", "'%"+ids+"%'");
		List<SupplierOrderElementFj> list = supplierOrderElementFjService.getByOrderId(page);
		if (list != null && list.size() > 0) {
			id = list.get(0).getId().toString();
		}
		request.setAttribute("id", id);
		request.setAttribute("tableName", "supplier_order_element_lot");
		return "/purchase/supplierorder/fileUpload";
	}
	
	/*
	 * 文件管理
	 */
	@RequestMapping(value="/fj",method=RequestMethod.GET)
	public String fj(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "supplier_order");
		return "/purchase/supplierorder/fileUpload";
	}
	
	/*
	 * 财务水单上传
	 */
	@RequestMapping(value="/paymentFile",method=RequestMethod.GET)
	public String paymentFile(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "payment_file");
		return "/purchase/supplierorder/fileUpload";
	}
	
	/**
	 * 跳转附件上传选择明细页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toSelectUploadElement",method=RequestMethod.GET)
	public String toSelectUploadElement(HttpServletRequest request){
		request.setAttribute("id", getString(request, "id"));
		return "/purchase/supplierorder/selectSupplierOrderManageElement";
	}
	
	/*
	 * 供应商订单明细数据
	 */
	@RequestMapping(value="/SupplierOrderElementForUpload",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo SupplierOrderElementForUpload(HttpServletRequest request,HttpServletResponse response) {
		PageModel<AddSupplierOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		Integer id = new Integer(getString(request, "id"));
		page.put("id", id);
		
		supplierOrderService.SupplierOrderElementForSelect(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (AddSupplierOrderElementVo addSupplierOrderElementVo: page.getEntities()) {
				DecimalFormat df = new DecimalFormat("######0.00");
				SupplierOrderElement supplierOrderElement = supplierOrderElementService.getPaymentMessage(addSupplierOrderElementVo.getId());
				AddSupplierOrderElementVo addSupplierOrderElementVo2 = supplierOrderElementService.getImportDateBySoeId(addSupplierOrderElementVo.getId());
				if (supplierOrderElement != null) {
					addSupplierOrderElementVo.setPaymentTotal(supplierOrderElement.getPrice());
					addSupplierOrderElementVo.setPaymentDate(supplierOrderElement.getPaymentDate());
				}else {
					addSupplierOrderElementVo.setPaymentTotal(0.00);
				}
				if (addSupplierOrderElementVo2.getImportDate() != null) {
					addSupplierOrderElementVo.setImportDate(addSupplierOrderElementVo2.getImportDate());
				}
				List<String> importNumbers = supplierOrderElementService.getImportNumberById(addSupplierOrderElementVo.getId());
				StringBuffer stringBuffer = new StringBuffer();
				for (String string : importNumbers) {
					stringBuffer.append(string).append(",");
				}
				if (stringBuffer.length() > 1) {
					stringBuffer.deleteCharAt(stringBuffer.length()-1);
					addSupplierOrderElementVo.setImportNumber(stringBuffer.toString());
				}
				Double importAmount = supplierOrderElementService.getImportAmount(addSupplierOrderElementVo.getId());
				if (importAmount != null) {
					addSupplierOrderElementVo.setImportAmount(importAmount);
				}
				
				//入库费用
				Double importFee = 0.00;
				Double importFreight = 0.00;
				List<ImportPackageElement> list = importpackageElementService.getBySoeId(addSupplierOrderElementVo.getId());
				for (int i = 0; i < list.size(); i++) {
					ImportPackage importPackage = importPackageService.getFeeMessage(list.get(i).getImportPackageId());
					if (importPackage.getImportFee() != null && importPackage.getTotalAmount() > 0) {
						if (!addSupplierOrderElementVo.getCurrencyId().equals(importPackage.getCurrencyId())) {
							ExchangeRate exchangeRate = exchangeRateService.selectByPrimaryKey(addSupplierOrderElementVo.getCurrencyId());
							Double rate = importPackage.getExchangeRate()/exchangeRate.getRate();
							importFee = importFee + importPackage.getImportFee()*rate/importPackage.getTotalAmount()*list.get(i).getAmount();
						}else {
							importFee = importFee + importPackage.getImportFee()/importPackage.getTotalAmount()*list.get(i).getAmount();
						}
						
					}
					if (importPackage.getFreight() != null && importPackage.getTotalAmount() > 0) {
						if (!addSupplierOrderElementVo.getCurrencyId().equals(importPackage.getCurrencyId())) {
							ExchangeRate exchangeRate = exchangeRateService.selectByPrimaryKey(addSupplierOrderElementVo.getCurrencyId());
							Double rate = importPackage.getExchangeRate()/exchangeRate.getRate();
							importFreight = importFreight + importPackage.getFreight()*rate/importPackage.getTotalAmount()*list.get(i).getAmount();
						}else {
							importFreight = importFreight + importPackage.getFreight()/importPackage.getTotalAmount()*list.get(i).getAmount();
						}
					}
				}
				addSupplierOrderElementVo.setImportFee(new BigDecimal(importFee).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				addSupplierOrderElementVo.setImportFreight(new BigDecimal(importFreight).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				
//				Integer supplierOrderELementId=supplierOrderElementService.getSupplierOrderELementId(addSupplierOrderElementVo.getId());
//				if(null==supplierOrderELementId||supplierOrderELementId.equals(0)){
//					addSupplierOrderElementVo.setOrderType("合同订单");
//				}else{
//					if(!supplierOrderELementId.equals(addSupplierOrderElementVo.getId())){
//						addSupplierOrderElementVo.setOrderType("库存订单");
//					}else if(supplierOrderELementId.equals(addSupplierOrderElementVo.getId())){
//						addSupplierOrderElementVo.setOrderType("合同订单");
//					}
//				}
				Map<String, Object> map = EntityUtil.entityToTableMap(addSupplierOrderElementVo);
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
	 * 修改明细
	 */
	@RequestMapping(value="/editELement",method=RequestMethod.GET)
	public String editELement(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		AddSupplierOrderElementVo addSupplierOrderElementVo = supplierOrderElementService.findByElementId(id);
		request.setAttribute("addSupplierOrderElementVo", addSupplierOrderElementVo);
		return "/purchase/supplierorder/editElementMessage";
	}
	
	/*
	 * 保存修改明细
	 */
	@RequestMapping(value="/saveEditElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo saveEditElement(HttpServletRequest request){
		boolean success = false;
		String message = "";
		try {
			SupplierOrderElement supplierOrderElement = new SupplierOrderElement();
			supplierOrderElement.setId(new Integer(getString(request, "id")));
			supplierOrderElement.setAmount(new Double(getString(request, "supplierOrderAmount")));
			supplierOrderElement.setAwb(getString(request, "awb"));
			supplierOrderElement.setPrice(new Double(getString(request, "supplierOrderPrice")));
			String leadTime = getString(request, "leadTime");
			if (leadTime != null && !"".equals(leadTime)) {
				supplierOrderElement.setLeadTime(leadTime);
			}
			String unit = getString(request, "quoteUnit");
			if (unit != null && !"".equals(unit)) {
				supplierOrderElement.setUnit(unit);
			}
			
			String shipWay = getString(request, "shipWayValue");
			if (!shipWay.isEmpty()) {
				supplierOrderElement.setShipWayId(new Integer(shipWay));
			}
			String destination = getString(request, "destination");
			if (!shipWay.isEmpty()) {
				supplierOrderElement.setDestination(destination);
			}
			
			String shipLeadTime = getString(request, "shipLeadTime");
			if (!"".equals(shipLeadTime) && shipLeadTime != null) {
				supplierOrderElement.setShipLeadTime(new Integer(getString(request, "shipLeadTime")));
			}
			String orderStatusValue = request.getParameter("orderStatusValue");
			SupplierOrderElement supplierOrderElement2 = supplierOrderElementService.selectByPrimaryKey(new Integer(getString(request, "id")));
			ClientOrderElement clientOrderElement2 = clientOrderElementService.selectByPrimaryKey(supplierOrderElement2.getClientOrderElementId());
			ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientOrderElement2.getClientQuoteElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(clientQuoteElement.getClientInquiryElementId());
			if (orderStatusValue != null && !"".equals(orderStatusValue)) {
				supplierOrderElement.setElementStatusId(new Integer(orderStatusValue));
			}
			clientOrderElementService.updateByPrimaryKeySelective(clientOrderElement2);
			if (orderStatusValue.equals("712")) {
				supplierOrderElement.setOrderStatusId(64);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String date = getString(request, "deadline");
			String invoiceDate = getString(request, "invoiceDate");
			if(!"".equals(date)){
				supplierOrderElement.setDeadline(sdf.parse(date));
			}
			if (!"".equals(invoiceDate)) {
				supplierOrderElement.setInvoiceDate(sdf.parse(invoiceDate));
			}
			supplierOrderElementService.updateByPrimaryKeySelective(supplierOrderElement);
			
			ImportPackagePaymentElement record=new ImportPackagePaymentElement();
			record.setAmount(supplierOrderElement.getAmount());
			record.setSupplierOrderElementId(supplierOrderElement.getId());
			importPackagePaymentElementService.updateBySupplierOrderElementId(record);
			
			success = true;
			message = "修改成功！";
		} catch (Exception e) {
			message = "修改失败！";
			e.printStackTrace();
		}
		
		return new EditRowResultVo(success, message);
	}
	/*public @ResponseBody ResultVo saveEditElement(HttpServletRequest request,@ModelAttribute SupplierOrderElement supplierOrderElement) {
		boolean success = false;
		String message = "";
		
		if (supplierOrderElement.getId()!=null) {
			supplierOrderElementService.updateByPrimaryKeySelective(supplierOrderElement);
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
	}*/
	
	/*
	 * 修改供应商订单
	 */
	@RequestMapping(value="/editSupplierOrder",method=RequestMethod.GET)
	public String editSupplierOrder(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		SupplierOrderManageVo supplierOrderManageVo = supplierOrderService.findByOrderId(id);
		request.setAttribute("supplierOrderManageVo", supplierOrderManageVo);
		return "/purchase/supplierorder/editSupplierOrder";
	}
	
	/*
	 * 保存修改后供应商订单
	 */
	@RequestMapping(value="/saveEditSupplierOrder",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEditSupplierOrder(HttpServletRequest request,@ModelAttribute SupplierOrder supplierOrder) {
		boolean success = false;
		String message = "";
		
		if (!"".equals(supplierOrder.getId())) {
			supplierOrderService.updateByPrimaryKeySelective(supplierOrder);
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 状态
	 */
	@RequestMapping(value="/status",method=RequestMethod.POST)
	public @ResponseBody ResultVo Status(HttpServletRequest request) {
		String msg = "";
		Integer id=new Integer(getString(request, "id"));
		SupplierOrderManageVo supplierOrderManageVo = supplierOrderService.findByOrderId(id);
		List<SystemCode> list2=systemCodeService.findOrderStatus();
		List<SystemCode> list=new ArrayList<SystemCode>();
		for (SystemCode systemCode2 : list2) {
			if (systemCode2.getValue().equals(supplierOrderManageVo.getOrderStatusValue())) {
				list.add(systemCode2);
			}
		}
		
		for (SystemCode systemCode2 : list2) {
			if (!systemCode2.getValue().equals(supplierOrderManageVo.getOrderStatusValue())) {
				list.add(systemCode2);
			}
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(true, msg);
	}
	
	
	/*
	 * 货币信息
	 */
	@RequestMapping(value="/findCurrency",method=RequestMethod.POST)
	public @ResponseBody ResultVo findCurrency(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		Integer id =new Integer(getString(request, "id"));
		SupplierOrderManageVo supplierOrderManageVo = supplierOrderService.findByOrderId(id);
		List<SystemCode> list2 = systemCodeService.findCurrency();
		List<SystemCode> list = new ArrayList<SystemCode>();
		for (SystemCode systemCode : list2) {
			if (systemCode.getId()==supplierOrderManageVo.getCurrencyId()) {
				list.add(systemCode);
			}
		}
		for (SystemCode systemCode : list2) {
			if (systemCode.getId()!=supplierOrderManageVo.getCurrencyId()) {
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
	 * 未完成工作页面
	 */
	@RequestMapping(value="/unFinishWork",method=RequestMethod.GET)
	public String unFinishWork() {
		return "/purchase/unfinishwork/unFinishWorkList";
	}
	
	/*
	 * 未完成工作列表
	 */
	@RequestMapping(value="/unFinish",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo unFinish(HttpServletRequest request,HttpServletResponse response) {
		PageModel<AddSupplierOrderElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = request.getParameter("searchString");

		GridSort sort = getSort(request);
		UserVo userVo=getCurrentUser(request);
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		//获取前端传过来的数据 partNumber
		String partNumber = getString(request,"partNumber").trim();
		partNumber = partNumber.replaceAll("[^0-9a-zA-Z]","");
		if(partNumber != null &&!"".equals(partNumber)){
			page.put("partNumber","'%"+partNumber+"%'");
		}

		supplierOrderService.unFinishWorkPage(page,where,sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (AddSupplierOrderElementVo addSupplierOrderElementVo: page.getEntities()) {
				Double b1 = addSupplierOrderElementVo.getSupplierOrderAmount();  
				Double b2 = addSupplierOrderElementVo.getImportAmount();  
				addSupplierOrderElementVo.setAmount(b1-b2);
				addSupplierOrderElementVo.setSupplierOrderTotalPrice(addSupplierOrderElementVo.getAmount()*addSupplierOrderElementVo.getSupplierOrderPrice());
				Map<String, Object> map = EntityUtil.entityToTableMap(addSupplierOrderElementVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("未交货清单", exportModel, mapList, response);
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
	
	/**
	 * 未订货清单
	 */
	@RequestMapping(value="/toNoOrder",method=RequestMethod.GET)
	public String toNoOrder() {
		return "/purchase/unfinishwork/noOrderList";
	}
	
	/**
	 * 未订货清单列表
	 */
	@RequestMapping(value="/NoOrder",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo NoOrder(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrder> page=getPage(request);
		PageModel<ClientOrderElement> elementpage=getPage(request);
		String clientOrderIds="";
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = getString(request, "searchString");
		GridSort sort = getSort(request);
		UserVo userVo=getCurrentUser(request);
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		supplierOrderService.NoOrderPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> elementmapList = new ArrayList<Map<String, Object>>();
			clientOrderIds=page.getEntities().get(0).getId().toString();
			for (ClientOrder clientOrder: page.getEntities()) {
				clientOrderIds+=","+clientOrder.getId();
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrder);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportModel ="[{\"name\":\"订单号\",\"width\":103,\"align\":0,\"property\":\"orderNumber\"},"
					              +"{\"name\":\"客户订单号\",\"width\":100,\"align\":0,\"property\":\"sourceNumber\"},"
					              +" {\"name\":\"客户\",\"width\":50,\"align\":0,\"property\":\"clientCode\"},"
					              +"{\"name\":\"机型\",\"width\":50,\"align\":0,\"property\":\"airCode\"},"
					              +"{\"name\":\"订单日期\",\"width\":80,\"align\":0,\"property\":\"orderDate\"},"
					              +"{\"name\":\"询价单号\",\"width\":100,\"align\":0,\"property\":\"quoteNumber\"},"
					              +"{\"name\":\"item\",\"width\":50,\"align\":0,\"property\":\"item\"},"
					              +"{\"name\":\"csn\",\"width\":50,\"align\":0,\"property\":\"csn\"},"
					              +"{\"name\":\"件号\",\"width\":180,\"align\":0,\"property\":\"partNumber\"},"
					              +"{\"name\":\"描述\",\"width\":217,\"align\":0,\"property\":\"description\"},"
					              +"{\"name\":\"单位\",\"width\":50,\"align\":0,\"property\":\"unit\"},"
					              +"{\"name\":\"客户订单数量\",\"width\":100,\"align\":0,\"property\":\"clientOrderAmount\"},"
					              +"{\"name\":\"数量\",\"width\":50,\"align\":0,\"property\":\"amount\"},"
					              +"{\"name\":\"周期\",\"width\":80,\"align\":0,\"property\":\"leadTime\"},"
					              +"{\"name\":\"订单日期\",\"width\":80,\"align\":0,\"property\":\"orderDate\"},"
					              +"{\"name\":\"截至日期\",\"width\":80,\"align\":0,\"property\":\"deadline\"}]";
					elementpage.put("clientOrderId", clientOrderIds);
					supplierOrderService.NoOrderELementDataPage(elementpage);
					for (ClientOrderElement clientOrderElement: elementpage.getEntities()) {
						Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElement);
						elementmapList.add(map);
					
					}
					exportService.exportGridToXls("未订货清单", exportModel, elementmapList, response);
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
	
	/**
	 * 未订货明细
	 */
	@RequestMapping(value="/NoOrderElement",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo noOrderElement(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		Integer clientOrderId = new Integer(getString(request, "id"));
		GridSort sort = getSort(request);
		page.put("clientOrderId", clientOrderId);
		
		supplierOrderService.NoOrderELementPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderElement clientOrderElement: page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderElement);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("未订货清单", exportModel, mapList, response);
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
	
	/**
	 * 跳转供应商发货明细页面
	 */
	@RequestMapping(value="/toImportPrepare",method=RequestMethod.GET)
	public String toImportPrepare(){
		return "/purchase/unfinishwork/importPrepareList";
	}
	
	
	/**
	 * 供应商发货明细
	 */
	@RequestMapping(value="/ImportPrepare",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo ImportPrepare(HttpServletRequest request,HttpServletResponse response){
		PageModel<SupplierOrderElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = getString(request, "searchString");
		GridSort sort = getSort(request);
		if ("".equals(where)) {
			where = null;
		}
		supplierOrderElementService.getImportPreparePage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierOrderElement supplierOrderElement: page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierOrderElement);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("供应商发货清单", exportModel, mapList, response);
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
	
	
	/**
	 * 新增库存订单
	 */
	@RequestMapping(value="/addStorageOrder",method=RequestMethod.POST)
	public @ResponseBody ResultVo addStorageOrder(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		UserVo userVo = getCurrentUser(request);
		Integer supplierId = new Integer(getString(request, "supplierId"));
		Integer repair = new Integer(getString(request, "repair"));
		try {
			supplierOrderService.addStorageOrder(new Integer(userVo.getUserId()),supplierId,repair);
			message = "新增成功！";
			success = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 搜索件号界面
	 */
	@RequestMapping(value="/toAddStorageElement",method=RequestMethod.GET)
	public String toAddStorageElement(HttpServletRequest request) {
		Integer clientOrderId = new Integer(getString(request, "clientOrderId"));
		StorageOrderVo storageOrderVo = supplierOrderService.getIds(clientOrderId);
		ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(clientOrderId);
		ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientOrder.getClientQuoteId());
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
		SystemCode biz = systemCodeService.selectByPrimaryKey(clientInquiry.getBizTypeId());
		request.setAttribute("clientOrderId", clientOrderId);
		request.setAttribute("supplierId", storageOrderVo.getSupplierId());
		request.setAttribute("biz", biz.getCode());
		return "/purchase/supplierorder/addStoragePartNumberList";
	}
	
	/**
	 * 搜索结果列表
	 */
	@RequestMapping(value="/addStorageElement",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo addStorageElement(HttpServletRequest request) {
		PageModel<SupplierQuoteElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = getString(request, "searchString");
		String part = getString(request, "part");
		String partCode = clientInquiryService.getCodeFromPartNumber(part);
		String biz = getString(request, "biz");
		String supplierId = getString(request, "supplierId");
		GridSort sort = getSort(request);
		page.put("partCode", partCode);
		page.put("supplierId", supplierId);
		if ("4".equals(biz)) {
			supplierOrderElementService.repairListByPartNumberPage(page, where, sort);
		}else {
			supplierOrderElementService.listByPartNumberPage(page, where, sort);
		}
		
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierQuoteElement supplierQuoteElement: page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
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
	 * 选择供应商
	 */
	@RequestMapping(value="/toSelectSupplier",method=RequestMethod.GET)
	public String toSelectSupplier() {
		return "/purchase/supplierorder/selectSupplier";
	}
	
	/**
	 * 增加库存订单明细
	 */
	@RequestMapping(value="/createStorageElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo createStorageElement(HttpServletRequest request,@ModelAttribute SupplierQuoteElement supplierQuoteElement) {
		String message = "";
		boolean success = false;
		Integer clientOrderId = new Integer(getString(request, "clientOrderId"));
		UserVo userVo = getCurrentUser(request);
		try {
			if (supplierQuoteElement.getPartNumber()!=null) {
				supplierOrderElementService.StorageOrderElement(supplierQuoteElement, clientOrderId);
				message = "新增成功！";
				success = true;
			}else {
				message = "新增失败！";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			success = false;
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 状态
	 */
	@RequestMapping(value="/Conditions",method=RequestMethod.POST)
	public @ResponseBody ResultVo Conditions(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		List<SystemCode> list = new ArrayList<SystemCode>();
		List<SystemCode> conditionList = systemCodeService.findType("COND");
		String where = getString(request, "conditionId");
		if (where!=null && !"".equals(where)) {
			for (int i = 0; i < conditionList.size(); i++) {
				if (conditionList.get(i).getId().equals(new Integer(where))) {
					list.add(conditionList.get(i));
					break;
				}
			}
			for (int i = 0; i < conditionList.size(); i++) {
				if (!conditionList.get(i).getId().equals(new Integer(where))) {
					list.add(conditionList.get(i));
				}
			}
		}else {
			list = conditionList;
		}
		
		JSONArray json = new JSONArray();
		json.add(list);
		message =json.toString();
		return new ResultVo(success, message);
	}
	
	/**
	 * 证书
	 */
	@RequestMapping(value="/Certifications",method=RequestMethod.POST)
	public @ResponseBody ResultVo Certifications(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		List<SystemCode> list = new ArrayList<SystemCode>();
		List<SystemCode> certificationList = systemCodeService.findType("CERT");
		String where = getString(request, "certificationId");
		if (where!=null && !"".equals(where)) {
			for (int i = 0; i < certificationList.size(); i++) {
				if (certificationList.get(i).getId().equals(new Integer(where))) {
					list.add(certificationList.get(i));
					break;
				}
			}
			for (int i = 0; i < certificationList.size(); i++) {
				if (!certificationList.get(i).getId().equals(new Integer(where))) {
					list.add(certificationList.get(i));
				}
			}
		}else {
			list = certificationList;
		}
		
		JSONArray json = new JSONArray();
		json.add(list);
		message =json.toString();
		return new ResultVo(success, message);
	}
	
	/**
	 * 获取运输方式
	 */
	@RequestMapping(value="/getShipWay",method=RequestMethod.POST)
	public @ResponseBody ResultVo getShipWay(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		StringBuffer value = new StringBuffer();
		String IfOrder = getString(request, "IfOrder");
		List<SystemCode> shipway = new ArrayList<SystemCode>();
		List<SystemCode> list = systemCodeService.findType("LOGISTICS_WAY");
		UserVo userVo = getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		List<SystemCode> mpiList = new ArrayList<SystemCode>();
		if (roleVo.getRoleName().indexOf("国外") >= 0) {
			mpiList = mpiService.getList();
		}
		if (IfOrder.equals("0")) {
			Integer id = new Integer(getString(request, "id"));
			SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(id);
			if (null!=supplierOrderElement&&null!=supplierOrderElement.getShipWayId() && !"".equals(supplierOrderElement.getShipWayId())) {
				/*for (int i = 0; i < list.size(); i++) {
					if (supplierOrderElement.getShipWayId().equals(list.get(i).getId())) {
						shipway.add(list.get(i));
					}
				}*/
				if (mpiList != null && mpiList.size() > 0) {
					/*for (SystemCode systemCode : mpiList) {
						if (supplierOrderElement.getShipWayId().equals(systemCode.getId())) {
							shipway.add(systemCode);
						}
					}*/
					for (SystemCode systemCode : mpiList) {
						if (!supplierOrderElement.getShipWayId().equals(systemCode.getId())) {
							shipway.add(systemCode);
						}
					}
				}
				for (int i = 0; i < list.size(); i++) {
					if (!supplierOrderElement.getShipWayId().equals(list.get(i).getId())) {
						shipway.add(list.get(i));
					}
				}
			}else {
				shipway = list;
				if (mpiList != null && mpiList.size() > 0) {
					shipway.addAll(mpiList);
				}
			}
			
			//拼接数据，页面双击修改使用
			for (int i = 0; i < shipway.size(); i++) {
				value.append(shipway.get(i).getId()).append(":").append(shipway.get(i).getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
		
			message = value.toString();
		}else {
			List<SystemCode> code = new ArrayList<SystemCode>();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getValue().equals("其他物流")) {
					code.add(list.get(i));
				}
			}
			
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getValue().equals("其他物流")) {
					code.add(list.get(i));
				}
			}
			//code.addAll(mpiList);
			JSONArray json = new JSONArray();
			json.add(code);
			message = json.toString();
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 目的地
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/destinationList",method=RequestMethod.POST)
	public @ResponseBody ResultVo destinationList(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		String IfOrder = getString(request, "IfOrder");
		List<SystemCode> destinations = new ArrayList<SystemCode>();
		List<SystemCode> list = systemCodeService.findType("STORE_LOCATION");
		StringBuffer value = new StringBuffer();
		UserVo userVo = getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		List<SystemCode> mpiList = new ArrayList<SystemCode>();
		if (roleVo.getRoleName().indexOf("国外") >= 0) {
			mpiList = mpiService.getList();
		}
		if (IfOrder.equals("0")) {
			Integer id = new Integer(getString(request, "id"));
			SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(id);
			
			if (null!=supplierOrderElement&&null!=supplierOrderElement.getDestination() && !"".equals(supplierOrderElement.getDestination())) {
				for (int i = 0; i < list.size(); i++) {
					if (supplierOrderElement.getDestination().equals(list.get(i).getId().toString())) {
						destinations.add(list.get(i));
					}
				}
				if (mpiList != null && mpiList.size() > 0) {
					/*for (SystemCode systemCode : mpiList) {
						if (supplierOrderElement.getShipWayId().equals(systemCode.getId())) {
							destinations.add(systemCode);
						}
					}*/
					for (SystemCode systemCode : mpiList) {
						if (!supplierOrderElement.getDestination().equals(systemCode.getId().toString())) {
							destinations.add(systemCode);
						}
					}
				}
				for (int i = 0; i < list.size(); i++) {
					if (!supplierOrderElement.getDestination().equals(list.get(i).getId().toString())) {
						destinations.add(list.get(i));
					}
				}
			}else {
				destinations = list;
				if (mpiList != null && mpiList.size() > 0) {
					destinations.addAll(mpiList);
				}
			}
			
			//拼接数据，页面双击修改使用
			for (int i = 0; i < destinations.size(); i++) {
				value.append(destinations.get(i).getId()).append(":").append(destinations.get(i).getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
		
			message = value.toString();
		}else {
			List<SystemCode> code = new ArrayList<SystemCode>();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getValue().equals("其他")) {
					code.add(list.get(i));
				}
			}
			
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getValue().equals("其他")) {
					code.add(list.get(i));
				}
			}
			code.addAll(mpiList);
			JSONArray json = new JSONArray();
			json.add(code);
			message = json.toString();
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 下拉列表数据-订单状态
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/findorderStatus",method=RequestMethod.POST)
	public @ResponseBody ResultVo findorderStatus(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String orderStatusId=request.getParameter("orderStatusId");
		List<SystemCode> list = systemCodeService.findType("ORDER_STATUS");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		SystemCode element=new SystemCode();
		StringBuffer value = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			SystemCode dateVo=list.get(i);
			Integer id=dateVo.getId();
			if(orderStatusId!=null&&id!=null){
				if(orderStatusId.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
			
		}
		if(arraylist.size()>0){
			list.clear();
			list.addAll(arraylist);
		}
		
		success = true;
		JSONArray json = new JSONArray();
//		if(null==certificationId||"".equals(certificationId)){
			json.add(list);
//		}else{
//			json.add(arraylist);
//		}
		
			for (SystemCode listDateVo : list) {
				value.append(listDateVo.getId()).append(":").append(listDateVo.getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
			msg=value.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			SupplierQuote record) {
		boolean success=false;
		String message="";
		String clientOrderId=request.getParameter("clientOrderId");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		MessageVo messageVo = supplierOrderElementService.uploadExcel(multipartFile, clientOrderId);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * 获取退税状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getTaxReimbursement",method=RequestMethod.POST)
	public @ResponseBody ResultVo getTaxReimbursement(HttpServletRequest request) {
		Integer supplierId = new Integer(getString(request, "supplierId"));
		Supplier supplier = supplierService.selectByPrimaryKey(supplierId);
		List<SystemCode> taxs = systemCodeService.findType("SUPPLIER_TAXREIMBURSEMENT_ID");
		List<SystemCode> list = new ArrayList<SystemCode>();
		if (supplier.getTaxReimbursementId() != null && !"".equals(supplier.getTaxReimbursementId())) {
			for (SystemCode systemCode : taxs) {
				if (systemCode.getId().toString().equals(supplier.getTaxReimbursementId())) {
					list.add(systemCode);
					break;
				}
			}
			for (SystemCode systemCode : taxs) {
				if (!systemCode.getId().toString().equals(supplier.getTaxReimbursementId())) {
					list.add(systemCode);
				}
			}
		}else {
			list = taxs;
		}
		JSONArray json = new JSONArray();
		json.add(list);
		return new ResultVo(true, json.toString());
	}
	
	/**
	 * 比较客户订单数量与供应商订单数量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSupplierOrderAmount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getSupplierOrderAmount(HttpServletRequest request){
		String message = "";
		boolean success = false;
		Integer clientOrderElementId = new Integer(getString(request, "clientOrderElementId"));
		SupplierOrderElement supplierOrderElement = supplierOrderElementService.getOrdersAmount(clientOrderElementId);
		if (supplierOrderElement.getClientOrderAmount() <= supplierOrderElement.getSupplierOrderAmount()) {
			success = true;
		}else {
			success = false;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 搜索询价单
	 * @return
	 */
	@RequestMapping(value="/toSearchOrder",method=RequestMethod.GET)
	public String toSearchInquiry(){
		return "/purchase/supplierorder/searchWeatherOrderList";
	}
	
	/**
	 * 监测当前下单供应商的资质是否过期
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkSupplierAptitude",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkSupplierAptitude(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			ResultVo resultVo = supplierOrderElementService.checkAptitude(id);
			return resultVo;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
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
