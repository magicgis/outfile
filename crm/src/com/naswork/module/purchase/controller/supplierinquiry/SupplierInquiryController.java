package com.naswork.module.purchase.controller.supplierinquiry;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.dev.ReSave;
import org.bouncycastle.crypto.tls.HashAlgorithm;
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
import com.naswork.dao.SupplierDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.PartAndEmail;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierContact;
import com.naswork.model.SupplierInquiry;
import com.naswork.model.SupplierInquiryElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.model.gy.GyExcel;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierInquiryEmelentVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.GyExcelService;
import com.naswork.service.SupplierCageRelationService;
import com.naswork.service.SupplierContactService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierPnRelationService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.service.SupplierService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.TPartService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;
import com.sun.star.i18n.reservedWords;

@Controller
@RequestMapping(value="/purchase/supplierinquiry")
public class SupplierInquiryController extends BaseController{

	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private SupplierPnRelationService supplierPnRelationService;
	@Resource
	private UserService userService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private TPartService tPartService;
	@Resource
	private GyExcelService gyExcelService;
	@Resource
	private SupplierContactService supplierContactService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElemenetService;
	@Resource
	private SupplierCageRelationService supplierCageRelationService;
	
	
	/*
	 * 列表页面
	 */
	@RequestMapping(value="/inquiryList",method=RequestMethod.GET)
	public String inquiryList(HttpServletRequest request) {
		UserVo userVo = getCurrentUser(request);
		List<RoleVo> roleVos = userService.searchRoleByUserId(userVo.getUserId());
		request.setAttribute("roleName", roleVos.get(0).getRoleName());
		return "/purchase/supplierInquiry/List";
		//return "/purchase/historyquote/list";
	}
	
	/*
	 * 列表页面
	 */
	@RequestMapping(value="/historyquote",method=RequestMethod.GET)
	public String historyquote(HttpServletRequest request) {
		Calendar cal = Calendar.getInstance();  
        cal.setTime(new Date());  
        cal.add(Calendar.DATE, +29);
        request.setAttribute("validity", cal.getTime());
		return "/purchase/historyquote/list";
	}
	
	/*
	 * 新增供应商询价
	 */
	@RequestMapping(value="/addsupplierinquiry",method=RequestMethod.GET)
	public String addsupplierinquiry(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("count", supplierInquiryService.findCount2(id));
		request.setAttribute("id", id);
		request.setAttribute("count2", supplierInquiryService.findCount());
		return "/purchase/supplierInquiry/addSupplierInquiry";
	}
	
	
	/*
	 * 供应商列表
	 */
	@RequestMapping(value="/suppliers",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo suppliers(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = getString(request, "searchString");
		String ability = getString(request, "ability");
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		page.put("ability", ability);
		
		supplierInquiryService.findSupplier(page,where);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierVo supplierVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierVo);
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
	 * 询价列表数据
	 */
	@RequestMapping(value="/listinquiry",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listinquiry(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientInquiryVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		supplierInquiryService.inquiryPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientInquiryVo clientInquiryVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(clientInquiryVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");

			
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		

		return jqgrid;
	}
	
	
	/*
	 * 保存供应商询价信息
	 */
	@RequestMapping(value="/saveSupplierInquiry",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveSupplierInquiry(HttpServletRequest request,@ModelAttribute SaveVo saveVo){
		boolean success = false;
		String message = "";
		Integer clientInquiryId = null;
		String order = getString(request, "orderElementIds");
		String suppliers = getString(request, "supplierIds");
		String firstText = getString(request, "firstText");
		String secondText = getString(request, "secondText");
		Integer clientInquiryId2 = new Integer(getString(request, "id"));
		UserVo userVo = getCurrentUser(request);
		String[] orderElementIds = order.split(",");
		String[] supplierIds = suppliers.split(",");
		try {
			if (orderElementIds!=null && supplierIds!=null) {
				Integer ids = new Integer(getString(request, "id"));
				SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(clientInquiryId2);
				for (int i=0;i<supplierIds.length;i++) {
					if (supplierIds[i]!=null) {
						//拼接单号
						String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),new Integer(supplierIds[i]));
						supplierInquiry.setQuoteNumber(quoteNumber);
						
						clientInquiryId = supplierInquiry.getClientInquiryId();
						supplierInquiry.setUpdateTimestamp(new Date());
						supplierInquiry.setSupplierId(new Integer(supplierIds[i]));
						supplierInquiryService.insertSelective(supplierInquiry);
						for (int j=0;j<orderElementIds.length;j++) {
							if (orderElementIds[j]!=null) {
								SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
								supplierInquiryElement.setSupplierInquiryId(supplierInquiry.getId());
								supplierInquiryElement.setClientInquiryElementId(new Integer(orderElementIds[j]));
								supplierInquiryElement.setUpdateTimestamp(new Date());
								supplierInquiryElementService.insertSelective(supplierInquiryElement);
							}	
						}
						if (saveVo.getList() == null) {
							saveVo.setList(new ArrayList<SupplierContact>());
						}
						if (saveVo.getList().size() > 0) {
							supplierInquiry.setEmailStatus(1);
							supplierInquiryService.updateByPrimaryKeySelective(supplierInquiry);
						}
						supplierInquiryService.sendEmail(supplierInquiry, userVo.getUserId(), firstText, secondText,saveVo.getList(),0);
					}
					
				}
				//clientInquiryService.sendEmail(clientInquiryId2);
				ClientInquiry clientInquiry = clientInquiryService.findById(clientInquiryId2);
				if (clientInquiry.getInquiryStatusId().equals(30) || clientInquiry.getInquiryStatusId() == 30) {
					clientInquiry.setId(clientInquiryId2);
					clientInquiry.setInquiryStatusId(31);
					clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
				}
				success = true;
				message = "保存成功！";
			}else{
				message = "保存失败！";
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return new ResultVo(success, message);
	}
	
	
	/*
	 * 管理页面
	 */
	@RequestMapping(value="/SupplierInquiryManage",method=RequestMethod.GET)
	public String SupplierInquiryManage() {
		return "/purchase/supplierInquiry/SupplierInquiryManage";
	}
	
	/*
	 * 供应商询价管理数据
	 */
	@RequestMapping(value="/listManage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listManage(HttpServletRequest request) {
		PageModel<ManageVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		supplierInquiryService.listManagePage(page, request.getParameter("searchString"),sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ManageVo manageVo : page.getEntities()) {
				manageVo.setSupplierQuoteCount(supplierInquiryService.getQuoteCount(manageVo.getId()));
				manageVo.setProportion(supplierInquiryService.getEleCount(manageVo.getId()));
				Map<String, Object> map = EntityUtil.entityToTableMap(manageVo);
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
	 * 供应商列表
	 */
	@RequestMapping(value="/SupplierList",method=RequestMethod.POST)
	public @ResponseBody ResultVo SupplierList(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		PageModel<SupplierVo> page = getPage(request);
		
		List<SupplierVo> list = supplierInquiryService.suppliers(page);
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(list);
		message = jsonArray.toString();
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 供应商管理明细
	 */
	@RequestMapping(value="/toManageElement",method=RequestMethod.GET)
	public String toManageElement(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		String supplierInquiryQuoteNumber = getString(request, "supplierInquiryQuoteNumber");
		request.setAttribute("id",id);
		request.setAttribute("supplierInquiryQuoteNumber", supplierInquiryQuoteNumber);
		return "/purchase/supplierInquiry/SupplierInquiryManageElement";
	}
	
	/*
	 * 供应商管理明细
	 */
	@RequestMapping(value="/ManageElementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo ManageElementList(HttpServletRequest request) {
		PageModel<ManageElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		Integer id = new Integer(getString(request, "id"));
		page.put("supplierInquiryId", id);
		
		supplierInquiryService.ManageElementPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ManageElementVo manageElementVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(manageElementVo);
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
	 * 新增供应商报价页面
	 */
	@RequestMapping(value="/toAddQuote",method=RequestMethod.GET)
	public String toAddQuote(HttpServletRequest request) {
		PageModel<ManageVo> page = getPage(request);
		Integer id = new Integer(getString(request, "id"));
		ManageVo manageVo = supplierInquiryService.selectByPrimaryKey(page,id);
		SupplierInquiry supplierInquiry = supplierInquiryService.selectByPrimaryKey(id);
		Supplier supplier = supplierService.selectByPrimaryKey(supplierInquiry.getSupplierId());
		if (supplier.getCounterFee() != null) {
			request.setAttribute("fee", supplier.getCounterFee());
		}
		Date today = new Date();
		request.setAttribute("today", today);
		request.setAttribute("manageVo", manageVo);
		return "/purchase/supplierInquiry/addSupplierQuote";
	}
	
	/*
	 * 货币
	 */
	@RequestMapping(value="/Currency",method=RequestMethod.POST)
	public @ResponseBody ResultVo Currency(HttpServletRequest request) {
		PageModel<ManageVo> page = getPage(request);
		Integer id = new Integer(getString(request, "id"));
		ManageVo manageVo = supplierInquiryService.selectByPrimaryKey(page,id);
		boolean success = true;
		String message = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		List<SystemCode> currency = systemCodeService.findCurrency();
		for (SystemCode systemCode : currency) {
			if (systemCode.getId().equals(manageVo.getCurrencyId())) {
				list.add(systemCode);
			}
		}
		for (SystemCode systemCode : currency) {
			if (!systemCode.getId().equals(manageVo.getCurrencyId())) {
				list.add(systemCode);
			}
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(list);
		message = jsonArray.toString();
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 保存供应商报价
	 */
	@RequestMapping(value="/saveSupplierQuote",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveSupplierQuote(HttpServletRequest request,@ModelAttribute SupplierQuote supplierQuote) {
		boolean success = true;
		String message = "";
		
		if (supplierQuote.getSupplierInquiryId()!=null) {
			int count = supplierQuoteService.findBySupplierInquiryId(supplierQuote.getSupplierInquiryId());
			UserVo userVo = getCurrentUser(request);
			supplierQuote.setCreateUser(new Integer(userVo.getUserId()));
			supplierQuote.setUpdateTimestamp(new Date());
			supplierQuoteService.insertSelective(supplierQuote);
			/*SupplierInquiry supplierInquiry = supplierInquiryService.selectByPrimaryKey(supplierQuote.getSupplierInquiryId());
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(supplierInquiry.getClientInquiryId());
			clientInquiry.setInquiryStatusId(35);
			clientInquiryService.updateByPrimaryKeySelective(clientInquiry);*/
			success = true;
			message = supplierQuote.getId().toString();
		}else {
			message = "保存失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 增加明细页面
	 */
	@RequestMapping(value="/addelementafteradd",method=RequestMethod.GET)
	public String addelementafteradd(HttpServletRequest request) {
		String supplierQuoteId = getString(request, "id");
		request.setAttribute("id", getString(request, "id"));
		String supplierInquiryId=request.getParameter("supplierInquiryId");
		SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(new Integer(supplierQuoteId));
		List<SupplierInquiryEmelentVo> list=supplierQuoteElemenetService.findsupplierinquiryelement(supplierInquiryId,request.getParameter("id").trim());
		List<SupplierQuoteVo> sqelist=supplierQuoteElemenetService.findSupplierQuoteElement(Integer.parseInt(request.getParameter("id").trim()));
		for (SupplierQuoteVo supplierQuoteVo : sqelist) {
			for (int i = 0; i < list.size(); i++) {
				if(supplierQuoteVo.getItem().equals(list.get(i).getItem())){
					list.remove(i);
					break;
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			if (supplierQuote.getBankCost() != null) {
				list.get(i).setBankCost(supplierQuote.getBankCost());
			}
			if (supplierQuote.getFeeForExchangeBill() != null) {
				list.get(i).setFeeForExchangeBill(supplierQuote.getFeeForExchangeBill().toString());
			}
		}
		request.setAttribute("list", list);
		return "/purchase/supplierquote/addElementalterTable";
	}
	
	/*
	 * 查询订单是否已存在
	 */
	@RequestMapping(value="/searchOrder",method=RequestMethod.POST)
	public @ResponseBody ResultVo searchOrder(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		
		Integer supplierInquiryId = new Integer(getString(request, "id"));
		
		Integer count = supplierInquiryService.findBySupplierInquiryId(supplierInquiryId);
		
		if (count.equals(0)) {
			success = true;
		}else {
			message = "此询价单已新增报价单！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 明细页面
	 */
	@RequestMapping(value="/element",method=RequestMethod.GET)
	public String element(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ClientInquiry clientInquiry=clientInquiryService.findById(id);
		request.setAttribute("id", id);
		request.setAttribute("quoteNumber", clientInquiry.getQuoteNumber());
		return "/purchase/supplierInquiry/elementList";
	}
	
	/*
	 * 供应商
	 */
	@RequestMapping(value="/getSuppliers",method=RequestMethod.POST)
	public @ResponseBody ResultVo supplierList(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		PageModel<Supplier> page = getPage(request);
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		List<Supplier> list = supplierInquiryService.getSupplier(page);
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(list);
		message = jsonArray.toString();
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 修改询价
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo edit(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		try {
			Integer id = new Integer(getString(request, "id"));
			String remark = getString(request, "remark");
			SupplierInquiry supplierInquiry = new SupplierInquiry();
			supplierInquiry.setId(id);
			supplierInquiry.setRemark(remark);
			supplierInquiry.setUpdateTimestamp(new Date());
			supplierInquiryService.updateByPrimaryKeySelective(supplierInquiry);
			message = "修改成功！";
		} catch (Exception e) {
			message = "修改失败！";
			success = false;
		}
		
		return new EditRowResultVo(success, message);
		
	}
	
	/*
	 * 自动生成询价单
	 */
	@RequestMapping(value="/toAutoList",method=RequestMethod.GET)
	public String toInquiryList(HttpServletRequest request) {
		PageModel<FactoryVo> page=getPage(request);
		Integer clientInquiryId = new Integer(getString(request, "id"));
		int pageSize = supplierInquiryService.getInquiryList(page, clientInquiryId);
		request.setAttribute("size", pageSize);
		request.setAttribute("id", clientInquiryId);
		return "/purchase/supplierInquiry/checkList";
	}
	
	/*
	 * 选择列表
	 */
	@RequestMapping(value="/autoList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo autoList(HttpServletRequest request) {
		PageModel<FactoryVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		Integer clientInquiryId = new Integer(getString(request, "id"));
		String where = getString(request, "searchString");
		String msnFlag = getString(request, "msnFlag");
		if ("".equals(where)) {
			where = null;
		}
		if ("".equals(msnFlag)) {
			msnFlag = null;
		}
		page.put("where", where);
		page.put("msnFlag", msnFlag);
		
		supplierInquiryService.getInquiryList(page, clientInquiryId);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			int key = 0;
			for (FactoryVo factoryVo : page.getEntities()) {
				factoryVo.setKey(key);
				Map<String, Object> map = EntityUtil.entityToTableMap(factoryVo);
				mapList.add(map);
				key++;
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
	 * 确认询价单
	 */
	@RequestMapping(value="/saveAdd",method = RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute SaveVo saveVo) {
		boolean success = true;
		String message = "";
		UserVo userVo = getCurrentUser(request);
		String firstText = getString(request, "firstText");
		String secondText = getString(request, "secondText");
		Integer clientInquiryId =  new Integer(getString(request, "id"));
		String[] bsn= getString(request, "bsn").split(",");
		String[] msns= getString(request, "msn").split(",");
		String[] clientInquiryElementIds = getString(request, "clientInquiryElementIds").split(",");
		List<SupplierInquiry> list = new ArrayList<SupplierInquiry>();
		List<SupplierInquiryElement> eleList = new ArrayList<SupplierInquiryElement>();
		try {
			if (bsn!=null) {
				for (int i=0;i<msns.length;i++) {
					if (bsn[i]!=null) {
						boolean flag = false;
						for (int j = 0; j < list.size(); j++) {
							List<Integer> supplierList = supplierCageRelationService.selectByMsn(msns[i]);
							for (Integer integer : supplierList) {
								if (list.get(j).getSupplierId().equals(integer)) {
									flag = true;
								}
							}	
						}
						List<Integer> supplierIds = supplierCageRelationService.selectByMsn(msns[i]);
						if (flag==false) {
							for (int j = 0; j < supplierIds.size(); j++) {
								//拼接单号
								SupplierInquiry supplierInquiry = supplierInquiryService.findClientInquiryElement(clientInquiryId);
								String quoteNumber = supplierInquiryService.getQuoteNumberSeq(supplierInquiry.getInquiryDate(),supplierIds.get(j));
								supplierInquiry.setQuoteNumber(quoteNumber);
								clientInquiryId = supplierInquiry.getClientInquiryId();
								supplierInquiry.setUpdateTimestamp(new Date());
								supplierInquiry.setSupplierId(supplierIds.get(j));
								list.add(supplierInquiry);
							}
						}
					}
					
				}
				for (int j = 0; j < list.size(); j++) {
					supplierInquiryService.insertSelective(list.get(j));
					for (int j2 = 0; j2 < msns.length; j2++) {
							List<Integer> supplierList = supplierCageRelationService.selectByMsn(msns[j2]);
							for (Integer integer : supplierList) {
								if (list.get(j).getSupplierId().equals(integer)) {
									if (bsn[j2]!=null) {
										ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(new Integer(clientInquiryElementIds[j2]));
										SupplierInquiryElement supplierInquiryElement = new SupplierInquiryElement();
										supplierInquiryElement.setSupplierInquiryId(list.get(j).getId());
										supplierInquiryElement.setClientInquiryElementId(new Integer(clientInquiryElementIds[j2]));
										supplierInquiryElement.setUpdateTimestamp(new Date());
										supplierInquiryElementService.insertSelective(supplierInquiryElement);
										clientInquiryElement.setInquiryStatus(1);
										clientInquiryElement.setBsn(bsn[j2]);
										clientInquiryElementService.updateByPrimaryKeySelective(clientInquiryElement);
									}	
								}
							}
					}
				}
				ClientInquiry clientInquiry = clientInquiryService.findById(clientInquiryId);
				if (clientInquiry.getInquiryStatusId().equals(30) || clientInquiry.getInquiryStatusId() == 30) {
					clientInquiry.setId(clientInquiryId);
					clientInquiry.setInquiryStatusId(31);
					clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
				}
				if (saveVo.getList() == null) {
					saveVo.setList(new ArrayList<SupplierContact>());
				}
				for (SupplierInquiry supplierInquiry : list) {
					if (saveVo.getList() == null) {
						saveVo.setList(new ArrayList<SupplierContact>());
					}
					if (saveVo.getList().size() > 0) {
						supplierInquiry.setEmailStatus(1);
						supplierInquiryService.updateByPrimaryKeySelective(supplierInquiry);
					}
					supplierInquiryService.sendEmail(supplierInquiry,userVo.getUserId() , firstText, secondText, saveVo.getList(),1);
				}
			}
			message = "新增成功！";
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			message = "新增失败！";
			success = false;
		}
		for (int j = 0; j < clientInquiryElementIds.length; j++) {
			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(new Integer(clientInquiryElementIds[j]));
			TPart tPart = tPartService.selectByPrimaryKey(bsn[j]);
			String[] names = tPart.getPartName().split(",");
			int flag = 0;
			for (int k = 0; k < names.length; k++) {
				if (names[k].equals(clientInquiryElement.getDescription())) {
					flag = 1;
					break;
				}
			}
			if (flag==0) {
				StringBuffer name = new StringBuffer();
				name.append(tPart.getPartName()).append(",").append(clientInquiryElement.getDescription());
				tPart.setPartName(name.toString());
				tPartService.updateByPrimaryKeySelective(tPart);
			}
		}
		for (int i = 0; i < clientInquiryElementIds.length; i++) {
			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(new Integer(clientInquiryElementIds[i]));
			clientInquiryElement.setEmailSend(1);
			clientInquiryElementService.updateByPrimaryKeySelective(clientInquiryElement);
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 根据客户询价单查询供应商询价单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/selectClientQuoteNumber",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo selectClientQuoteNumber(HttpServletRequest request) {
		PageModel<SupplierInquiry> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = getString(request, "searchString");
		GridSort sort = getSort(request);
		supplierInquiryService.findByClientQuoteNumberPage(page,where,sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierInquiry supplierInquiry : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierInquiry);
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
	 * 条状批量生成excel页面
	 * @return
	 */
	@RequestMapping(value="/toLotsExcel",method=RequestMethod.GET)
	public String toLotsExcel() {
		return "/purchase/supplierInquiry/selectByClientQuoteNumberList";
	}
	
	/**
	 * 生成多个excel
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/lotsExcel",method=RequestMethod.POST)
	public @ResponseBody ResultVo lotsExcel(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		String ids = getString(request, "quoteNumbers");
		String[] inquiryIds = ids.split(",");
		try {
			for (int i = 0; i < inquiryIds.length; i++) {
				StringBuffer array = new StringBuffer();
				array.append(ids).append("-").append(i);
				StringBuffer businessKey = new StringBuffer();
				businessKey.append("supplier_inquiry.id.").append(array.toString()).append(".LotsSupplierInuquiryExcel");
				gyExcelService.generateExcel(businessKey.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "生成失败！";
		}
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 增加邮件内容页面
	 * @return
	 */
	@RequestMapping(value="/toAddEmailText",method=RequestMethod.GET)
	public String toAddEmailText() {
		return "/purchase/supplierInquiry/addEmailText";
	}
	
	/**
	 * 获取邮件列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getEmailList",method=RequestMethod.GET)
	public String getEmailList(HttpServletRequest request) {
		String suppliers = getString(request, "supplierIds");
		String[] supplierList = suppliers.split(",");
		List<SupplierContact> list = supplierInquiryService.getContacts(supplierList);
		request.setAttribute("list", list);
		return "/purchase/supplierInquiry/emailTable";
	}
	
	/**
	 * 根据BSN获取邮件列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getEmailListByMsn",method=RequestMethod.GET)
	public String getEmailListByBsn(HttpServletRequest request) {
		List<SupplierContact> list = new ArrayList<SupplierContact>();
		String[] msns= getString(request, "msn").split(",");
		for (int i = 0; i < msns.length; i++) {
			String msn = msns[i];
			List<Integer> supplierList = supplierCageRelationService.selectByMsn(msn);
			for (Integer integer : supplierList) {
				List<SupplierContact> supplierContacts = supplierContactService.findBySupplierId(integer);
				Supplier supplier = supplierService.selectByPrimaryKey(integer);
				for (int j = 0; j < supplierContacts.size(); j++){
					if (!list.contains(supplierContacts.get(j))) {
						supplierContacts.get(j).setSupplierCode(supplier.getCode());
						list.add(supplierContacts.get(j));
					}
				}
			}
		}
		request.setAttribute("list", list);
		return "/purchase/supplierInquiry/emailTable";
	}
	
	/**
	 * 客户询价明细与商品库挂钩
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveBsnInClientInuquiryElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveBsnInClientInuquiryElement(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String[] bsns = getString(request, "bsn").split(",");
		String[] clientInquiryElementIds = getString(request, "clientInquiryElementIds").split(",");
		success = clientInquiryElementService.updateBsn(bsns, clientInquiryElementIds);
		if (success) {
			message = "保存成功！";
		}else {
			message = "保存失败！";
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 搜索询价单
	 * @return
	 */
	@RequestMapping(value="/toSearchInquiry",method=RequestMethod.GET)
	public String toSearchInquiry(){
		return "/purchase/supplierInquiry/searchClientInquiryList";
	}
	
	/*
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			ClientInquiryElement clientInquiryElement) {
		boolean success=false;
		String message="";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = supplierInquiryElementService.uploadExcel(multipartFile);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	
	/**
	 * 新增询价-历史报价页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addInquiry",method=RequestMethod.POST)
	public @ResponseBody ResultVo  addInquiry(HttpServletRequest request){
		try {
			String ids = getString(request, "supplierQuoteElementIds");
			Integer clientInquiryElementId = new Integer(getString(request, "clientInquiryElementId"));
			if (ids.length() > 0) {
				String[] supplierQuoteElementIds = ids.split(",");
				boolean success = supplierInquiryElementService.addInquiry(supplierQuoteElementIds, clientInquiryElementId);
				if (!success) {
					return new ResultVo(success, "新增失败！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增失败！");
		}
		
		return new ResultVo(true, "新增成功！");
	}
	
	/*
	 * 供应商询价管理数据
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request) {
		PageModel<ManageVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String searchString = request.getParameter("searchString");
		if (searchString != null && !"".equals(searchString)) {
			supplierInquiryService.listManagePage(page, searchString,sort);
		}else {
			searchString = "si.EMAIL_STATUS = 0 AND si.INQUIRY_DATE = '"+format.format(now)+"'";
			supplierInquiryService.listManagePage(page, searchString,sort);
		}
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ManageVo manageVo : page.getEntities()) {
				manageVo.setSupplierQuoteCount(supplierInquiryService.getQuoteCount(manageVo.getId()));
				manageVo.setProportion(supplierInquiryService.getEleCount(manageVo.getId()));
				Map<String, Object> map = EntityUtil.entityToTableMap(manageVo);
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
	 * 跳转选择供应商询价单页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/chooseInquiry",method=RequestMethod.GET)
	public String chooseInquiry(HttpServletRequest request){
		UserVo userVo = getCurrentUser(request);
		List<RoleVo> roleVos = userService.searchRoleByUserId(userVo.getUserId());
		request.setAttribute("roleName", roleVos.get(0).getRoleName());
		return "/purchase/historyquote/SupplierInquiry";
	}
	
	
	/**
	 * 发送邮件
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendEmail",method=RequestMethod.POST)
	public @ResponseBody ResultVo sendEmail(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			UserVo userVo = getCurrentUser(request);
			String ids = getString(request, "supplierInquiryIds");
			String[] supplierInquiryIds = ids.split(",");
			String firstText = getString(request, "firstText");
			String secondText = getString(request, "secondText");
			for (int i = 0; i < supplierInquiryIds.length; i++) {
				SupplierInquiry supplierInquiry = supplierInquiryService.selectByPrimaryKey(new Integer(supplierInquiryIds[i]));
				supplierInquiry.setEmailStatus(1);
				supplierInquiryService.updateByPrimaryKeySelective(supplierInquiry);
				supplierInquiryService.sendEmail(supplierInquiry, userVo.getUserId(), firstText, secondText);
			}
			message = "发送成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "发送失败！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	
	/**
	 * 触发爬虫
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/crawEmail",method=RequestMethod.POST)
	public @ResponseBody ResultVo crawEmail(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			UserVo userVo = getCurrentUser(request);
			String id = getString(request, "id");
			List<ClientInquiryElement> list = clientInquiryElementService.findByclientInquiryId(new Integer(id));
			clientInquiryElementService.searchSatair(list,new Integer(id),1,0,null);
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(new Integer(id));
			clientInquiry.setCrawlEmail(1);
			clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
			message = "发起成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "发起失败！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 根据类型推送邮件
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendEmailBySelect",method=RequestMethod.POST)
	public @ResponseBody ResultVo sendEmailBySelect(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			UserVo userVo = getCurrentUser(request);
			String ids = getString(request, "ids");
			Integer id = new Integer(getString(request, "id"));
			success = supplierInquiryService.sendEmail(ids, id);
			if (success) {
				message = "发送成功！";
			}else {
				message = "发送失败！";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			message = "发送失败！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	@RequestMapping(value="/toCrawlEmailSupplierList",method=RequestMethod.GET)
	public String toCrawlEmailSupplierList(){
		return "/purchase/supplierInquiry/crawlEmailList";
	}
	
	/*
	 * 邮件爬虫结果
	 */
	@RequestMapping(value="/crawlEmailSupplierList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo crawlEmailSupplierList(HttpServletRequest request) {
		PageModel<PartAndEmail> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String searchString = request.getParameter("searchString");
		supplierInquiryService.getPrepareEmailList(page, searchString, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			int key = 1;
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartAndEmail partAndEmail : page.getEntities()) {
				partAndEmail.setId(key);
				Map<String, Object> map = EntityUtil.entityToTableMap(partAndEmail);
				mapList.add(map);
				key++;
			}
			jqgrid.setRows(mapList);
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return jqgrid;
	}
	
	
	@RequestMapping(value="/cancelEmailSend",method=RequestMethod.POST)
	public @ResponseBody ResultVo cancelEmailSend(HttpServletRequest request){
		String parts = getString(request, "partNumbers");
		String ids = getString(request, "supplierIds");
		try {
			if (parts != null && ids != null && !"".equals(parts) && !"".equals(ids)) {
				String[] partNumbers = parts.split(",");
				String[] supplierIds = ids.split(",");
				PartAndEmail partAndEmail = new PartAndEmail();
				for (int i = 0; i < supplierIds.length; i++) {
					partAndEmail.setPartNumber(partNumbers[i]);
					partAndEmail.setSupplierId(new Integer(supplierIds[i]));
					supplierInquiryService.cancelRecord(partAndEmail);
				}
				return new ResultVo(true, "修改成功！");
			}else {
				return new ResultVo(false, "修改失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
		
	}
	
	/**
	 * 选择爬虫的网站
	 * @return
	 */
	@RequestMapping(value="/selectSupplierToCrawl",method=RequestMethod.GET)
	public String selectSupplierToCrawl(){
		return "/purchase/supplierInquiry/selectSupplier";
	}
	
	/**
	 * 采购手动发起爬虫
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/commitCrawl",method=RequestMethod.POST)
	public @ResponseBody ResultVo commitCrawl(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			String supplierCodes = getString(request, "suppliers");
			String[] suppliers = supplierCodes.split(",");
			HashMap<String, Boolean> supplierMap = new HashMap<String, Boolean>();
			for (int i = 0; i < suppliers.length; i++) {
				supplierMap.put(suppliers[i], true);
			}
			List<ClientInquiryElement> list = clientInquiryElementService.findByclientInquiryId(id);
			clientInquiryElementService.searchSatair(list,id,0,1,supplierMap);
			return new ResultVo(true, "发起爬虫成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "发起爬虫异常！");
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
