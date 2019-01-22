package com.naswork.module.storage.controller.exportpackage;

import java.awt.event.FocusEvent;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.filter.ContextHolder;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.Client;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.model.ClientReceipt;
import com.naswork.model.ClientShip;
import com.naswork.model.ExportPackage;
import com.naswork.model.ExportPackageElement;
import com.naswork.model.ExportPackageInstructions;
import com.naswork.model.ExportPackageInstructionsElement;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.Supplier;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.model.UnexportElement;
import com.naswork.module.purchase.controller.importpackage.ImportPackageElementVo;
import com.naswork.module.purchase.controller.importpackage.SupplierOrderElementVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.service.ClientContactService;
import com.naswork.service.ClientReceiptService;
import com.naswork.service.ClientService;
import com.naswork.service.ClientShipService;
import com.naswork.service.ExchangeRateService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.ExportPackageInstructionsElementService;
import com.naswork.service.ExportPackageInstructionsService;
import com.naswork.service.ExportPackageService;
import com.naswork.service.GyExcelService;
import com.naswork.service.ImportStorageLocationListService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.RoleService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.TPartService;
import com.naswork.service.UnexportElementService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.UuidUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/storage/exportpackage")
public class ExportPackageController extends BaseController{
	
	@Resource
	private ExportPackageService exportPackageService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ExportPackageElementService exportPackageElementService;
	@Resource
	private ExchangeRateService exchangeRateService;
	@Resource
	private ClientReceiptService clientReceiptService;
	@Resource
	private ClientShipService clientShipService;
	@Resource
	private ClientContactService clientContactService;
	@Resource
	private ExportPackageInstructionsService exportPackageInstructionsService;
	@Resource
	private ExportPackageInstructionsElementService exportPackageInstructionsElementService;
	@Resource
	private UserService userService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private ClientService clientService;
	@Resource
	private UnexportElementService unexportElementService;
	@Resource
	private ImportStorageLocationListService importStorageLocationListService;
	@Resource
	private TPartService tPartService;
	@Resource
	private GyExcelService gyExcelService;
	@Resource
	private RoleService roleService;
	
	/**
	 * 出库列表
	 */
	@RequestMapping(value="/toExportList",method=RequestMethod.GET)
	public String toExportList() {
		return "/storage/exportpackage/exportpackageList";
	}
	
	/**
	 * 出库列表数据
	 */
	@RequestMapping(value="/exportList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo exportList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ExportPackageVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String orderNumber = getString(request, "orderNumber");
		if (orderNumber != null && !"".equals(orderNumber)) {
			page.put("orderNumber", orderNumber);
			if (where != null && !"".equals(where)) {
				where = where + "and (co.ORDER_NUMBER like '%"+orderNumber+"%' or so.ORDER_NUMBER like '%"+orderNumber+"%')";
			}else {
				where = "co.ORDER_NUMBER like '%"+orderNumber+"%' or so.ORDER_NUMBER like '%"+orderNumber+"%'";
			}
		}
		
		exportPackageService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExportPackageVo exportPackageVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("部件资料", exportModel, mapList, response);
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
	 * 新增出库
	 */
	@RequestMapping(value="/toAddExport")
	public String toAddExport(HttpServletRequest request) {
		Date today = new Date();
		request.setAttribute("today", today);
		return "/storage/exportpackage/addMessage";
	}
	
	
	/**
	 * 保存新增
	 */
	@RequestMapping(value="/saveAddExport",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAddExport(HttpServletRequest request,@ModelAttribute ExportPackage exportPackage) {
		String message = "";
		boolean success = false;
		if (exportPackage.getClientId()!=null) {
			UserVo userVo = getCurrentUser(request);
			exportPackage.setCreateUser(new Integer(userVo.getUserId()));
			exportPackageService.insertSelective(exportPackage);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 修改出库单
	 */
	@RequestMapping(value="/toEditExport",method=RequestMethod.GET)
	public String toEditExport(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
		request.setAttribute("exportPackage", exportPackage);
		return "/storage/exportpackage/editMessage";
	}
	
	/**
	 * 保存修改
	 */
	@RequestMapping(value="/saveEditExport",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEditExport(HttpServletRequest request,@ModelAttribute ExportPackage exportPackage) {
		String message = "";
		boolean success = false;
		
		if (exportPackage.getId()!=null) {
			exportPackage.setUpdateTimestamp(new Date());
			UserVo userVo = getCurrentUser(request);
			exportPackage.setLastUpdateUser(new Integer(userVo.getUserId()));
			exportPackageService.updateByPrimaryKeySelective(exportPackage);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 货币信息
	 */
	@RequestMapping(value="/findCurrency",method=RequestMethod.POST)
	public @ResponseBody ResultVo findCurrency(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		Integer id =new Integer(getString(request, "id"));
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
		List<SystemCode> list2 = systemCodeService.findCurrency();
		List<SystemCode> list = new ArrayList<SystemCode>();
		for (SystemCode systemCode : list2) {
			if (systemCode.getId().equals(exportPackage.getCurrencyId())) {
				list.add(systemCode);
			}
		}
		for (SystemCode systemCode : list2) {
			if (!systemCode.getId().equals(exportPackage.getCurrencyId())) {
				list.add(systemCode);
			}
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 运输方式
	 */
	@RequestMapping(value="/findLogistics",method=RequestMethod.POST)
	public @ResponseBody ResultVo findLogistics(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		Integer id =new Integer(getString(request, "id"));
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
		List<SystemCode> list2=systemCodeService.findSupplierByType("LOGISTICS_WAY");
		List<SystemCode> list = new ArrayList<SystemCode>();
		for (SystemCode systemCode : list2) {
			if (systemCode.getId().equals(exportPackage.getLogisticsWay())) {
				list.add(systemCode);
			}
		}
		for (SystemCode systemCode : list2) {
			if (!systemCode.getId().equals(exportPackage.getLogisticsWay())) {
				list.add(systemCode);
			}
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 明细页面
	 */
	@RequestMapping(value="/toElement",method=RequestMethod.GET)
	public String toElement(HttpServletRequest request) {
		request.setAttribute("exportNumber", getString(request, "exportNumber"));
		request.setAttribute("id", getString(request, "id"));
		request.setAttribute("clientId", getString(request, "clientId"));
//		String token=get32UUID();
//		HttpSession session = request.getSession();
//		session.setAttribute("token", token);
//		request.setAttribute("token", token);
		return "/storage/exportpackage/elementList";
	}
	
	/**
	 * 明细数据
	 */
	@RequestMapping(value="/Element",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo Element(HttpServletRequest request) {
		PageModel<ExportPackageElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		Integer id = new Integer(getString(request, "id"));
		page.put("exportPackageId", id);
		
		exportPackageElementService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExportPackageElementVo exportPackageElementVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageElementVo);
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
	
	/**
	 * 新增明细
	 */
	@RequestMapping(value="/toAddElement",method=RequestMethod.GET)
	public String toAddElement(HttpServletRequest request) {
		request.setAttribute("clientId", getString(request, "clientId"));
		request.setAttribute("exportNumber", getString(request, "exportNumber"));
		String id =  getString(request, "id");
		request.setAttribute("id", getString(request, "id"));
		return "/storage/exportpackage/addelementList";
	}
	
	/**
	 * 新增列表
	 */
	@RequestMapping(value="/addElementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo addElementList(HttpServletRequest request) {
		PageModel<ExportPackageElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String where = request.getParameter("searchString");
		GridSort sort = getSort(request);
		Integer clientId = new Integer(getString(request, "clientId"));
		Integer id = new Integer(getString(request, "id"));
		page.put("clientId", clientId);
		if (where!=null && !where.equals("")) {
			if (where.startsWith("isll.id") || where.startsWith("e.id")) {
				ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
				if (exportPackage.getExportPackageInstructionsId()!=null && !"".equals(exportPackage.getExportPackageInstructionsId())) {
					page.put("id", exportPackage.getExportPackageInstructionsId());
					exportPackageElementService.exportPackageInstructionsPage(page, where, sort);
				}else {
					exportPackageElementService.addElementPage(page,where, sort);
				}
			}else {
				int location = where.indexOf("code");
				if (location>=0) {
					String string = where.substring(location, where.length());
					int l = string.indexOf("0");
					int last = string.indexOf(")");
					String code = string.substring(l, last).trim();
					where = where.substring(0, location);
					StringBuffer condition = new StringBuffer();
					if (code.startsWith("040")) {
						code = code.substring(18);
						condition.append(where).append("isll.id = ").append(code);
					}else if (code.startsWith("030")) {
						code = code.substring(13);
						condition.append(where).append("e.id = ").append(code);
					}
					ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
					if (exportPackage.getExportPackageInstructionsId()!=null && !"".equals(exportPackage.getExportPackageInstructionsId())) {
						page.put("id", exportPackage.getExportPackageInstructionsId());
						exportPackageElementService.exportPackageInstructionsPage(page, where, sort);
					}else {
						exportPackageElementService.addElementPage(page,condition.toString(), sort);
					}
					
				}else {
					ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
					if (exportPackage.getExportPackageInstructionsId()!=null && !"".equals(exportPackage.getExportPackageInstructionsId())) {
						page.put("id", exportPackage.getExportPackageInstructionsId());
						exportPackageElementService.exportPackageInstructionsPage(page, where, sort);
					}else {
						exportPackageElementService.addElementPage(page,where, sort);
					}
				}
				
			}
			
		}else {
			ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
			if (exportPackage.getExportPackageInstructionsId()!=null && !"".equals(exportPackage.getExportPackageInstructionsId())) {
				page.put("id", exportPackage.getExportPackageInstructionsId());
				exportPackageElementService.exportPackageInstructionsPage(page, where, sort);
			}else {
				exportPackageElementService.addElementPage(page,where, sort);
			}
			
		}
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExportPackageElementVo exportPackageElementVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageElementVo);
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
	
	
	/**
	 * 保存新增明细
	 */
	@RequestMapping(value="/saveAddElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAddElement(HttpServletRequest request,@ModelAttribute ExportPackageElement exportPackageElement) {
		String message = "";
		boolean success = false;
		if (exportPackageElement.getExportPackageId()!=null) {
			exportPackageElementService.insertSelective(exportPackageElement);
			exportPackageElementService.checkExportPackage(exportPackageElement);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 新增收款
	 */
	@RequestMapping(value="/toAddReceipt",method=RequestMethod.GET)
	public String toAddReceipt(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
		SystemCode systemCode = systemCodeService.findById(exportPackage.getCurrencyId());
		Date today = new Date();
		request.setAttribute("today", today);
		request.setAttribute("exportPackage", exportPackage);
		request.setAttribute("systemCode", systemCode);
		return "/storage/exportpackage/addreceipt";
	}
	
	/**
	 * 保存收款
	 */
	@RequestMapping(value="/saveReceipt",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveReceipt(HttpServletRequest request,@ModelAttribute ClientReceipt clientReceipt) {
		String message = "";
		boolean success = false;
		if (clientReceipt.getExportPackageId()!=null) {
			clientReceipt.setUpdateTimestamp(new Date());
			clientReceiptService.insertSelective(clientReceipt);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 新增客户箱单
	 */
	@RequestMapping(value="/toClientShip",method=RequestMethod.GET)
	public String toClientShip(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
		Date today = new Date();
		request.setAttribute("today", today);
		request.setAttribute("exportPackage", exportPackage);
		return "/storage/exportpackage/addclientship";
	}
	
	/**
	 * 保存客户箱单
	 */
	@RequestMapping(value="/saveClientShip",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveClientShip(HttpServletRequest request,@ModelAttribute ClientShip clientShip) {
		String message = "";
		boolean success = true;
		if (clientShip.getExportPackageId()!=null) {
			clientShip.setUpdateTimestamp(new Date());
			clientShipService.insertSelective(clientShip);
			ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(clientShip.getExportPackageId());
			Client client = clientService.selectByPrimaryKey(exportPackage.getClientId());
			if (client.getCode().startsWith("8") || client.getCode().startsWith("9")) {
				success = exportPackageService.createInvoice(clientShip);
				if (!success) {
					return new ResultVo(false, "生成发票异常!");
				}
			}
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 客户联系人
	 */
	@RequestMapping(value="/clientContacts",method=RequestMethod.POST)
	public @ResponseBody ResultVo clientContacts(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		Integer clientId = new Integer(getString(request, "clientId"));
		PageModel<ClientContact> page = new PageModel<ClientContact>();
		page.put("clientId", clientId);
		List<ClientContact> clientContacts = clientContactService.selectByclientId(page);
		JSONArray json = new JSONArray();
		json.add(clientContacts);
		message = json.toString();
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 检查功能
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public @ResponseBody ResultVo check(HttpServletRequest request){
		String message = "";
		boolean success = true;
		Integer exportPackageId = new Integer(getString(request, "id"));
		/*List<ExportPackageElementVo> locationList = exportPackageElementService.getCountWithLocation(exportPackageId);
		List<ImportPackageElement> list = new ArrayList<ImportPackageElement>();
		if (locationList.size()!=0) {
			for (ExportPackageElementVo exportPackageElementVo : locationList) {
				List<ImportPackageElement> importList = exportPackageElementService.getElementByLocation(exportPackageElementVo.getLocation());
				if (importList.size()!=0) {
					for (ImportPackageElement importPackageElement : importList) {
						if (importPackageElement.getLocation()!="地面") {
							list.add(importPackageElement);
						}
					}
				}
			}
		}*/
		PageModel<ImportPackageElement> page=getPage(request);
		page.put("exportPackageId", exportPackageId);
		List<ImportPackageElement> list = exportPackageElementService.getLackList(page);
		if (list.size()!=0) {
			message = Integer.toString(list.size());
			success = false;
		}else {
			message = "没有缺少的件！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 缺件页面
	 */
	@RequestMapping(value="/toLack",method=RequestMethod.GET)
	public String toLack(HttpServletRequest request) {
		Integer count = new Integer(getString(request, "count"));
		Integer exportPackageId = new Integer(getString(request, "id"));
		request.setAttribute("exportPackageId", exportPackageId);
		request.setAttribute("count", count);
		return "/storage/exportpackage/lackOfPartNumberList";
	}
	
	/**
	 * 缺件明细
	 */
	@RequestMapping(value="/lack",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo lack(HttpServletRequest request) {
		PageModel<ImportPackageElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		Integer exportPackageId = new Integer(getString(request, "exportPackageId"));
		
		exportPackageElementService.lackOfPartNumber(page, exportPackageId);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportPackageElement importPackageElement : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(importPackageElement);
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
	
	/**
	 * 新增出库发票页面
	 */
	@RequestMapping(value="/toExportPackInvoice",method=RequestMethod.GET)
	public String toExportPackInvoice(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		return "/storage/exportpackage/clientorderpage";
	}
	
	/**
	 * 客户订单列表
	 */
	@RequestMapping(value="/clientOrderList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo clientOrderList(HttpServletRequest request) {
		PageModel<ExportPackageElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		Integer exportPackageId = new Integer(getString(request, "exportPackageId"));
		page.put("exportPackageId", exportPackageId);
		exportPackageElementService.findClientOrderNumber(page);;
		for (int i=0 ;i< page.getEntities().size();i++) {
			ExportPackageElementVo exportPackageElementVo=exportPackageElementService.findEpeAmount(exportPackageId, page.getEntities().get(i).getId());
			page.getEntities().get(i).setRemark(exportPackageElementVo.getRemark());
		}
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExportPackageElementVo  exportPackageElementVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageElementVo);
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
	 * 新增出库发票
	 */
	@RequestMapping(value="/addExportpackageInvoice",method=RequestMethod.POST)
	public @ResponseBody ResultVo addExportpackageInvoice(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		
//		if (clientShip.getExportPackageId()!=null) {
			String exportPackageId=request.getParameter("exportPackageId");
			String clientOrderId=request.getParameter("id");
//			List<ExportPackageElementVo>  list=exportPackageElementService.findByEpidAndCoid(exportPackageId, clientOrderId);
			
//			clientShip.setUpdateTimestamp(new Date());
//			clientShipService.insertSelective(clientShip);
			message = exportPackageId+","+clientOrderId;
			if(null!=clientOrderId){
				success = true;
			}
			
//		}else {
//			message = "新增失败！";
//		}
		
		return new ResultVo(success, message);
	}
	
	
	/**
	 * 条形码新增出库单
	 */
	@RequestMapping(value="/BarCodeAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo BarCodeAddByLocation(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		PageModel<ExportPackageElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String where = request.getParameter("searchString");
		Integer exportPackageId = new Integer(getString(request, "id"));
		GridSort sort = getSort(request);
		Integer clientId = new Integer(getString(request, "clientId"));
		page.put("clientId", clientId);
		List<ExportPackageElementVo> list = new ArrayList<ExportPackageElementVo>();
		if (where!=null) {
			try {
				ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(exportPackageId);
				if (exportPackage.getExportPackageInstructionsId()!=null) {
					page.put("instructionsId", exportPackage.getExportPackageInstructionsId());
				}	
					page.put("where", where);
					list = exportPackageElementService.getImportElement(page);
					page.put("exportPackageId", exportPackageId);
					List<ImportPackageElement> importList = importpackageElementService.selectByExportPackageId(page);
					if (importList.size() != list.size() && exportPackage.getExportPackageInstructionsId()!=null) {
						importList = exportPackageElementService.unEqual(list, importList);
						for (ImportPackageElement importPackageElement : importList) {
							UnexportElement unexportElement = new UnexportElement();
							unexportElement.setImportPackageElementId(importPackageElement.getId());
							unexportElement.setUserId(new Integer(userVo.getUserId()));
							unexportElementService.insertSelective(unexportElement);
						}
						
						
					}else {
						if (list.size()>0) {
							for (int i = 0; i < list.size(); i++) {
								ExportPackageElementVo exportPackageElementVo = list.get(i);
								ExportPackageElement exportPackageElement = new ExportPackageElement();
								exportPackageElement.setExportPackageId(exportPackageId);
								if (exportPackage.getExportPackageInstructionsId() != null && !"".equals(exportPackage.getExportPackageInstructionsId())) {
									ExportPackageInstructionsElement exportPackageInstructionsElement = exportPackageInstructionsElementService.selectByImportElementId(exportPackageElementVo.getImportPackageElementId());
									exportPackageElement.setAmount(exportPackageInstructionsElement.getAmount());
								}else {
									exportPackageElement.setAmount(exportPackageElementVo.getStorageAmount());
								}
								exportPackageElement.setImportPackageElementId(exportPackageElementVo.getImportPackageElementId());
								exportPackageElement.setStatus(0);
								exportPackageElement.setUpdateTimestamp(new Date());
								exportPackageElementService.insertSelective(exportPackageElement);
								exportPackageElementService.checkExportPackage(exportPackageElement);
							}
						}
						success = true;
						message = "新增成功！";
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}else {
			message = "新增失败！";
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 件号条码新增出库
	 */
	@RequestMapping(value="/BarCodeAddByPartNumber",method=RequestMethod.POST)
	public @ResponseBody ResultVo BarCodeAddByPartNumber(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			PageModel<ExportPackageElement> page = getPage(request);
			Integer exportPackageId = new Integer(getString(request, "id"));
			String clientOrderElementId = getString(request, "clientOrderElementId");
			String importPackageId = getString(request, "importPackageId");
			String locationId = getString(request, "locationId");
			if(null!=getString(request, "sequence")&&!"".equals(getString(request, "sequence"))){
				Integer sequence = new Integer(getString(request, "sequence"));
				page.put("sequence", sequence);
			}
			
			char [] stringArr = importPackageId.toCharArray();
			for (int i = 0; i < stringArr.length; i++) {
				if (stringArr[i]!='0') {
					importPackageId = importPackageId.substring(i);
					break;
				}
			}
			page.put("importPackageId", importPackageId);
			page.put("clientOrderElementId", clientOrderElementId);
			ExportPackageElement exportPackageElementCheck = exportPackageElementService.getImportPackageElementId(page);
			if (locationId != null && "".equals(locationId)) {
				locationId = null;
			}
			page.put("locationId", locationId);
			
			ExportPackageElement exportPackageElement = exportPackageElementService.getImportPackageElementId(page);
			List<ExportPackageElement> exports = exportPackageElementService.getByExportElementByOrderId(new Integer(clientOrderElementId), new Integer(importPackageId));
			HttpSession session = request.getSession();
//			Object retoken=request.getParameter("token");
//			Object setoken=session.getAttribute("token");
//			if(null!=setoken&&setoken.equals(retoken)){
				if (exportPackageElement!=null) {
					exportPackageElement.setExportPackageId(exportPackageId);
					ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(exportPackageId);
					if (exportPackage.getExportPackageInstructionsId() != null && !"".equals(exportPackage.getExportPackageInstructionsId())) {
						ExportPackageInstructionsElement exportPackageInstructionsElement = exportPackageInstructionsElementService.selectByImportElementId(exportPackageElement.getImportPackageElementId());
						exportPackageElement.setAmount(exportPackageInstructionsElement.getAmount());
					}
					exportPackageElementService.insertSelective(exportPackageElement);
//					session.removeAttribute("token");
					exportPackageElementService.checkExportPackage(exportPackageElement);
					success = true;
					message = "新增成功！";
				}else{
					if (exportPackageElementCheck != null) {
						message = "该件不属于这个位置上！";
					}else if(exports.size() > 0){
						message = "该件已出库！";
					}else {
						message = "该件不在出库指令中！";
					}
				}
//			}else{
//				return null;
//			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 新增多条出库明细
	 */
	@RequestMapping(value="/addExportElements",method=RequestMethod.POST)
	public @ResponseBody ResultVo addExportElements(HttpServletRequest request,@ModelAttribute ClientInvoiceElement clientInvoiceElement){
		String message = "";
		boolean success = false;
		Integer exportPackageId = new Integer(getString(request, "id")); 
		if (clientInvoiceElement.getVoList()!=null) {
			exportPackageElementService.insertElements(clientInvoiceElement.getVoList(), exportPackageId);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 出库指令
	 */
	@RequestMapping(value="/toExportPackageInstructions",method=RequestMethod.GET)
	public String toExportPackageInstructions() {
		return "/storage/exportpackage/exportpackageinstructionslist";
	}
	
	/**
	 * 出库指令列表数据
	 */
	@RequestMapping(value="/exportPackageInstructionsList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo exportPackageInstructionsList(HttpServletRequest request) {
		PageModel<ExportPackageInstructions> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String clientCode = request.getParameter("clientCode");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") &&! roleVo.getRoleName().equals("行政") &&! roleVo.getRoleName().equals("财务")&&roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId",userId);
			if(null!=where&&!where.equals("")){
				where="ar.USER_ID ="+userId+" and "+where;
			}
			else{
				where="ar.USER_ID ="+userId;
			}
		}
		if (clientCode != null) {
			page.put(where, "epi.CODE = "+clientCode);
		}
		exportPackageInstructionsService.listDataPage(page, where, sort);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExportPackageInstructions exportPackageVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageVo);
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
	 * 出库流水
	 */
	@RequestMapping(value="/toExportPackageFlowList",method=RequestMethod.GET)
	public String toExportPackageFlowList(HttpServletRequest request) {
		request.setAttribute("flow", "yes");
		return "/storage/exportpackage/exportpackageflowlist";
	}
	
	/**
	 * 出库流水列表数据
	 */
	@RequestMapping(value="/exportPackageFlowList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo exportPackageFlowList(HttpServletRequest request) {
		PageModel<ExportPackageInstructions> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") &&! roleVo.getRoleName().equals("行政") &&! roleVo.getRoleName().equals("财务")&&roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId",userId);
			if(null!=where&&!where.equals("")){
				where="ar.USER_ID ="+userId+" and "+where;
			}
			else{
				where="ar.USER_ID ="+userId;
			}
		}
		exportPackageInstructionsService.flowLlistDataPage(page, where, sort);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExportPackageInstructions exportPackageVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageVo);
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
	 * 出库指令明细
	 */
	@RequestMapping(value="/toExportPackageInstructionsElement",method=RequestMethod.GET)
	public String toExportPackageInstructionsElement(HttpServletRequest request) {
		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("flow", request.getParameter("flow"));
		request.setAttribute("code", request.getParameter("code"));
		return "/storage/exportpackage/exportpackageinstructionselementlist";
	}
	
	/**
	 * 出库指令明细列表数据
	 */
	@RequestMapping(value="/exportPackageInstructionsElementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo exportPackageInstructionsElementList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ExportPackageElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String flow=request.getParameter("flow");
		if(flow.equals("yes")){
			page.put("flow", "yes");
		}
		page.put("id", request.getParameter("id"));
		ImportPackageElement element=new ImportPackageElement();
		element.setFlow(flow);
		if("".equals(where)){
			where=null;
		}
		element.setParame(where);
		element.setId(Integer.parseInt(request.getParameter("id")));
		Double boxWeight=exportPackageElementService.BoxWeight(element);
		exportPackageElementService.exportPackageInstructionsPage(page, where, sort);
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			 DecimalFormat df = new DecimalFormat("#.00"); 
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExportPackageElementVo exportPackageVo : page.getEntities()) {
				if(null!=boxWeight&&boxWeight!=0){
					exportPackageVo.setCountWeight(Double.parseDouble(df.format(boxWeight/1000)));
					}
				if(null==exportPackageVo.getStorageAmount()){
					exportPackageVo.setStorageAmount(exportPackageVo.getAmount());
				}
				if (exportPackageVo.getPrice() != null && exportPackageVo.getExportAmount() != null) {
					exportPackageVo.setSupplierOrderTotal(exportPackageVo.getPrice() * exportPackageVo.getExportAmount());
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
				if (roleVo.getRoleName().indexOf("管理员") >= 0 || roleVo.getRoleName().indexOf("销售") >= 0 || roleVo.getRoleName().indexOf("财务") >= 0) {
					exportModel = "[{\"name\":\"件号\",\"width\":120,\"align\":0,\"property\":\"partNumber\"},"+
									 "{\"name\":\"描述\",\"width\":150,\"align\":0,\"property\":\"description\"},"+
									 "{\"name\":\"状态\",\"width\":70,\"align\":0,\"property\":\"conditionCode\"},"+
									 "{\"name\":\"证书\",\"width\":70,\"align\":0,\"property\":\"certificationCode\"},"+
									 "{\"name\":\"单位\",\"width\":40,\"align\":0,\"property\":\"unit\"},"+
									 "{\"name\":\"数量\",\"width\":40,\"align\":0,\"property\":\"exportPackageInstructionsAmount\"},"+
									 "{\"name\":\"退税\",\"width\":40,\"align\":0,\"property\":\"taxValue\"},"+
									 "{\"name\":\"符合性证明\",\"width\":80,\"align\":0,\"property\":\"complianceCertificateValue\"},"+
									 "{\"name\":\"是否完成符合性证明\",\"width\":80,\"align\":0,\"property\":\"completeComplianceCertificateValue\"},"+
									 "{\"name\":\"供应商订单币种\",\"width\":60,\"align\":0,\"property\":\"supplierOrderCurrencyValue\"},"+
									 "{\"name\":\"供应商订单价格\",\"width\":80,\"align\":0,\"property\":\"price\"},"+
									 "{\"name\":\"供应商订单总价\",\"width\":100,\"align\":0,\"property\":\"supplierOrderTotal\"},"+
									 "{\"name\":\"客户订单币种\",\"width\":60,\"align\":0,\"property\":\"clientOrderCurrencyValue\"},"+
									 "{\"name\":\"客户订单价格\",\"width\":80,\"align\":0,\"property\":\"orderPrice\"},"+
									 "{\"name\":\"客户订单总价\",\"width\":100,\"align\":0,\"property\":\"orderTotal\"},"+
									 "{\"name\":\"位置\",\"width\":80,\"align\":0,\"property\":\"location\"},"+
									 "{\"name\":\"订单号\",\"width\":150,\"align\":0,\"property\":\"clientOrderNumber\"},"+
									 "{\"name\":\"客户订单号\",\"width\":150,\"align\":0,\"property\":\"sourceNumber\"},"+
									 "{\"name\":\"供应商订单号\",\"width\":100,\"align\":0,\"property\":\"orderNumber\"},"+
									 "{\"name\":\"入库日期\",\"width\":80,\"align\":0,\"property\":\"importDate\"},"+
									 "{\"name\":\"重量(g)\",\"width\":80,\"align\":0,\"property\":\"boxWeight\"},"+
									 "{\"name\":\"更新时间\",\"width\":120,\"align\":0,\"property\":\"updateTimestamp\"}]";
				}
				try {
					exportService.exportGridToXls("Exportpack", exportModel, mapList, response);
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
	 * 修改出库指令数量
	 */
	@RequestMapping(value="/updateExportpackageInstructionsElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo updateExportpackageInstructionsElement(HttpServletRequest request,@ModelAttribute ExportPackageInstructionsElement record){
		String message = "";
		boolean success = false;
		if (record.getId()!=null) {
			Double amount = new Double(request.getParameter("exportPackageInstructionsAmount"));
			record.setAmount(amount);
			exportPackageInstructionsElementService.updateByPrimaryKeySelective(record);
			message = "修改成功！";
			success = true;
		}else {
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
		
	}

	/**
	 * 不出库
	 */
	@RequestMapping(value="/noExportpackage",method=RequestMethod.POST)
	public @ResponseBody ResultVo noExportpackage(HttpServletRequest request){
		String message = "";
		boolean success = false;
		String Id = getString(request, "id"); 
		if (Id!=null) {
//			ExportPackageInstructionsElement record=new ExportPackageInstructionsElement();
//			record.setAmount(0.0);
			String[] ids=Id.split(",");
			for (int i = 0; i < ids.length; i++) {
//			record.setId(Integer.parseInt(ids[i]));
			exportPackageInstructionsElementService.deleteByPrimaryKey(Integer.parseInt(ids[i]));
			}
			message = "修改成功！";
			success = true;
		}else {
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 新增指令明细页面
	 */
	@RequestMapping(value="/toAddExportPackageInstructionsElement",method=RequestMethod.GET)
	public String toAddExportPackageInstructionsElement(HttpServletRequest request) {
		request.setAttribute("code", request.getParameter("code"));
		request.setAttribute("epiId", request.getParameter("epiId"));
		return "/storage/exportpackage/addexportpackageinstructionselement";
	}
	
	/**
	 * 新增出库指令明细
	 */
	@RequestMapping(value="/addExportPackageInstructionsElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo addExportPackageInstructionsElement(HttpServletRequest request){
		String message = "";
		boolean success = false;
		String Id = getString(request, "ids"); 
		if (Id!=null) {
			String[] ids=Id.split(",");
			ExportPackageInstructionsElement element=new ExportPackageInstructionsElement();
			 element.setExportPackageInstructionsId(Integer.parseInt(request.getParameter("epiId")));
			 for (int i = 0; i < ids.length; i++) {
				 String[] split=ids[i].split("-");
//				 ImportPackageElement data= importpackageElementService.selectByPrimaryKey(Integer.parseInt(ids[i]));
				 element.setAmount(new Double(split[1]));
				 element.setImportPackageElementId(Integer.parseInt(split[0]));
				 ExportPackageInstructionsElement element2=exportPackageInstructionsElementService.findElement(element);
				 if(null==element2||element2.getAmount()==0){
				 exportPackageInstructionsElementService.insert(element);
				 }
			}
				
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 验证code
	 */
	@RequestMapping(value="/testCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo testCode(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String code = getString(request, "code");
		ExportPackageInstructions data=exportPackageInstructionsService.findByNumber(code);
		if (data!=null) {
			success = true;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 根据出库指令获取客户
	 */
	@RequestMapping(value="/getClient",method=RequestMethod.POST)
	public @ResponseBody ResultVo getClient(HttpServletRequest request) {
		String exportPackageInstructionsNumber = getString(request, "number");
		Client client = exportPackageService.getByExportPackageInstructionsNumber(exportPackageInstructionsNumber);
		PageModel<Client> page = getPage(request);
		List<Client> list = new ArrayList<Client>();
		List<Client> clients = clientService.findAll(page);
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getId().equals(client.getId())) {
				list.add(clients.get(i));
				break;
			}
		}
		for (int i = 0; i < clients.size(); i++) {
			if (!clients.get(i).getId().equals(client.getId())) {
				list.add(clients.get(i));
			}
		}
		
		JSONArray json = new JSONArray();
		json.add(list);
		return new ResultVo(true, json.toString());
	}
	
	/**
	 * 根据出库指令获取货币
	 */
	@RequestMapping(value="/getCurrency",method=RequestMethod.POST)
	public @ResponseBody ResultVo getCurrency(HttpServletRequest request) {
		String exportPackageInstructionsNumber = getString(request, "number");
		Integer currencyId = exportPackageService.findByExportPackageInstructionsNumber(exportPackageInstructionsNumber);
		List<SystemCode> list = new ArrayList<SystemCode>();
		List<SystemCode> currency = systemCodeService.findType("CURRENCY");
		for (int i = 0; i < currency.size(); i++) {
			if (currency.get(i).getId().equals(currencyId)) {
				list.add(currency.get(i));
				break;
			}
		}
		for (int i = 0; i < currency.size(); i++) {
			if (!currency.get(i).getId().equals(currencyId)) {
				list.add(currency.get(i));
			}
		}
		
		JSONArray json = new JSONArray();
		json.add(list);
		return new ResultVo(true, json.toString());
	}
	
	/**
	 * 删除明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteElement(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		try {
			ExportPackageElement exportPackageElement = exportPackageElementService.selectByPrimaryKey(id);
			ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(exportPackageElement.getExportPackageId());
			exportPackageElementService.deleteByPrimaryKey(id);
			exportPackageInstructionsElementService.updateExportStatus(exportPackageElement.getImportPackageElementId(), exportPackage.getExportPackageInstructionsId());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "删除异常！");
		}
		
		
		return new ResultVo(true, "删除成功！");
	}
	
	/**
	 * 多出件页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toErrorTable",method=RequestMethod.GET)
	public String toErrorTable(HttpServletRequest request) {
		UserVo userVo = getCurrentUser(request);
		List<ImportPackageElement> list = importpackageElementService.selectByUserId(new Integer(userVo.getUserId()));
		request.setAttribute("importList", list);
		return "/storage/exportpackage/errorelementtable";
	}
	
	@RequestMapping(value="/deleteUnexportElement",method=RequestMethod.POST)
	public void deleteUnexportElement(HttpServletRequest request){
		UserVo userVo = getCurrentUser(request);
		unexportElementService.deleteByUserId(new Integer(userVo.getUserId()));
	}
	
	/*
	 * 查询所有客户代码
	 */
	@RequestMapping(value="/clientCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo ClientCode(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		PageModel<Client> page = getPage(request);
		List<Client> list=clientService.findAll(page);
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 出库明细箱子重量
	 */
	@RequestMapping(value="/toBoxWeight",method=RequestMethod.GET)
	public String toBoxWeight(HttpServletRequest request) {
		request.setAttribute("id", request.getParameter("id"));
		return "/storage/exportpackage/exportpackageboxweight";
	}
	
	/**
	 * 出库指令明细列表数据
	 */
	@RequestMapping(value="/boxWeightList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo boxWeightList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ExportPackageElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String id=request.getParameter("id");
		ImportPackageElement element=new ImportPackageElement();
		List<ExportPackageElementVo> elementVos=exportPackageElementService.findBoxWeight(Integer.parseInt(id));
		page.setEntities(elementVos);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			 DecimalFormat df = new DecimalFormat("#.00"); 
			for (ExportPackageElementVo exportPackageVo : page.getEntities()) {
				if(null!=exportPackageVo.getBoxWeight()){
					exportPackageVo.setBoxWeight(Double.parseDouble(df.format(exportPackageVo.getBoxWeight()/1000)));
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageVo);
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
	 * 根据位置查找件号页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toPartListByLocation",method=RequestMethod.GET)
	public String toPartListByLocation(HttpServletRequest request){
		request.setAttribute("id", getString(request, "id"));
		request.setAttribute("locationId", getString(request, "locationId"));
		return "/storage/exportpackage/partnumberinlocationList";
	}
	
	/**
	 * 位置内件号列表
	 */
	@RequestMapping(value="/partListByLocation",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo partListByLocation(HttpServletRequest request) {
		PageModel<ExportPackageElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String locationId = request.getParameter("locationId");
		Integer id = new Integer(getString(request, "id"));
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(id);
		GridSort sort = getSort(request);
		page.put("locationId", locationId);
		page.put("id", exportPackage.getExportPackageInstructionsId());
		page.put("export", "flow");
		exportPackageElementService.exportPackageInstructionsPage(page, "", sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExportPackageElementVo exportPackageElementVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(exportPackageElementVo);
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
	 * 根据出库指令查询入库信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getLabelList",method=RequestMethod.POST)
	public @ResponseBody ResultVo getLabelList(HttpServletRequest request){
		try {
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			String id = request.getParameter("id");
			List<ImportPackageElement> list = exportPackageInstructionsService.getByInstructionsId(new Integer(id));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getInspectionDate() != null) {
					list.get(i).setInspectionDateString(dateFormat.format(list.get(i).getInspectionDate()));
				}
				if (list.get(i).getManufactureDate() != null) {
					list.get(i).setManufactureDateString(dateFormat.format(list.get(i).getManufactureDate()));
				}
			}
			JSONArray json = new JSONArray();
			json.add(list);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "查询不到该位置的入库信息！");
		}
		
	}
	
	@RequestMapping(value="/partiframes",method=RequestMethod.GET)
	public String partiframes(HttpServletRequest request) throws UnsupportedEncodingException, ParseException{
	String elementId=request.getParameter("elementId");
	String sign="0";
	String haveResume="";
	String ipeId=request.getParameter("ipeId");
	String unit=request.getParameter("unit");
	String type=request.getParameter("type");
	String printAmount=request.getParameter("pamount");
	String amount=request.getParameter("amount");
	
	
	if(null!=request.getParameter("partNumber")){
	String partnumber=request.getParameter("partNumber").replace("%27", "'");
	}
	String printPartNumber="";
	if(null!=request.getParameter("ppart")){
	 printPartNumber=request.getParameter("ppart").replace("%27", "'");
	}
	String printDescription="";
	if(null!=request.getParameter("pdesc")){
		printDescription=request.getParameter("pdesc");
	}
	String id=request.getParameter("id").trim();
	String importpackid=request.getParameter("ipid").trim();
	String location=request.getParameter("sl");
	if(null!=location&&!"".equals(location)){
		location=	new String(location.getBytes("iso8859-1"),"UTF-8");
	}
	
	List<SupplierOrderElementVo> data=importpackageElementService.findsupplierorderDate(Integer.parseInt(id));
	String sn="";
	if (unit != null && !"".equals(unit)) {
		data.get(0).setQuoteUnit(unit);
	}
	if(null!=request.getParameter("sn")&&!"".equals(request.getParameter("sn"))&&!request.getParameter("sn").trim().equals("undefined")){
		sn=request.getParameter("sn");
		sn=	new String(sn.getBytes("iso8859-1"),"UTF-8");
	data.get(0).setSerialNumber(sn);
	}
	int l=sn.length();
	 data.get(0).setLocation(location);
		if(!id.equals("0")&&!id.equals("-1")&&null==type){
			String resume=request.getParameter("resume").trim();
			String complianceCertificate=request.getParameter("complianceCertificate").trim();
			if(complianceCertificate.equals("301")){
				haveResume="please refer to log book for details";
			}
			Integer importPackageSign=importpackageElementService.findImportPackageSign(Integer.parseInt(id));
			importPackageSign=importPackageSign+1;
				sign=importPackageSign.toString();
		}else if(type.trim().equals("update")&&null!=ipeId&&!"".equals(ipeId)){
			ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(ipeId.trim());
			if(importPackageElementVo.getComplianceCertificate().toString().equals("301")){
				haveResume="please refer to log book for details";
			}
			if(null!=importPackageElementVo){
				if(null!=importPackageElementVo.getImportPackageSign()){
					sign=importPackageElementVo.getImportPackageSign().toString();
				}
				if(null==location||"".equals(location)){
					 data.get(0).setLocation(importPackageElementVo.getLocation());
				}
				if(null!=importPackageElementVo.getSerialNumber()&&sn.equals("")){
					sn=importPackageElementVo.getSerialNumber();
					data.get(0).setSerialNumber(sn);
				}
			}
		}
		
		if(sn.length()>=30){
			data.get(0).setSerialNumber("See Page 2");
			request.setAttribute("hide", "onehide");
			request.setAttribute("sn", sn);
		}else if("".equals(sn)){
			request.setAttribute("hide", "allhide");
		}else if(!"".equals(sn)){
			request.setAttribute("hide", "onehide");
		}
		
	if(null!=printAmount&&!"0".equals(printAmount.trim())){
		 data.get(0).setQuoteAmount(Double.parseDouble(printAmount));
	}else{
		 data.get(0).setQuoteAmount(Double.parseDouble(amount));
	}
	if(data.get(0).getCsn().equals(0)){
		data.get(0).setCsn(data.get(0).getItem());
	}
	if(null!=printPartNumber&&!"".equals(printPartNumber)){
		printPartNumber=	new String(printPartNumber.getBytes("iso8859-1"),"UTF-8");
	data.get(0).setQuotePartNumber(printPartNumber);
	}
	if(null!=printDescription&&!"".equals(printDescription)&&!printDescription.trim().equals("undefined")){
		printDescription=	new String(printDescription.getBytes("iso8859-1"),"UTF-8");
	data.get(0).setQuoteDescription(printDescription);
	}
	data.get(0).setManufactureDate("N/A");
	String manDate=request.getParameter("mdate");
	String inDate=request.getParameter("idate");
	if(null!=inDate&&!"".equals(inDate)&&!"undefined".equals(inDate.trim())){
		data.get(0).setManufactureDate(inDate);
	}else if(null!=manDate&&!"".equals(manDate)&&!"undefined".equals(manDate.trim())){
		data.get(0).setManufactureDate(manDate);
	}

	String expiryDate="";
	if(null!=data.get(0).getBsn()){
		TPart tPart=tPartService.selectByPrimaryKey(data.get(0).getBsn());
		if(null!=tPart&&null!=tPart.getShelfLife()&&tPart.getShelfLife()>0){
			boolean man=false;
			boolean in=false;
			Date day=new Date();
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			if(null!=inDate&&!"".equals(inDate.trim())&&!"undefined".equals(inDate.trim())){
				in=true;
			}
			if(null!=manDate&&!"".equals(manDate.trim())&&!"undefined".equals(manDate.trim())){
				man=true;
			}
				if(man&&in){
					Date inday=dateFormat.parse(inDate);
					Date manday=dateFormat.parse(manDate);
					if(inday.getTime()>manday.getTime()){ 
						 day=dateFormat.parse(inDate);
					}else if(manday.getTime()>inday.getTime()){
						 day=dateFormat.parse(manDate);
					}else{
						 day=dateFormat.parse(manDate);
					}
				}else if(man){
					 day=dateFormat.parse(manDate);
				}else if(in){
					 day=dateFormat.parse(inDate);
				}
			
				if(man||in){
					Integer shelfLife=tPart.getShelfLife();
					GregorianCalendar gc=new GregorianCalendar(); 
					gc.setTime(day); 
					gc.add(5,shelfLife); 
					gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
					Date time=gc.getTime();
					expiryDate=dateFormat.format(time);
				}
		}
	}
	if(!"".equals(expiryDate) && !"934".equals(data.get(0).getClientCode())){
		data.get(0).setExpiryDate(expiryDate);
	}else{
		data.get(0).setExpiryDate("hide");
	}
	
	request.setAttribute("data", data.get(0));
	  String y=importpackid;  
	  String x= data.get(0).getClientOrderElementId().toString();
//	  Random rm = new Random();  
//		 double pross = (1 + rm.nextDouble()) * Math.pow(10, 5); 
//		 double pross2 = (1 + rm.nextDouble()) * Math.pow(10, 5); 
//		 String fixLenthString = String.valueOf(pross);  
//		
//		  fixLenthString = String.valueOf(pross2);  
//		  String z=fixLenthString.substring(1, 5 + 1);  
//	  String y= data.get(0).getCsn().toString();
   //  第一个判断件号或箱子，locationid,elementid;  
		  if(x.length()<7){
			  for (int i = 0; i < x.length(); i++) {
					 Integer xl=x.length();
					 if(!xl.equals(7)){
						 x="0"+x;
					 }else{
						 break;
					 }
				}
		  }
		  if(y.length()<7){
				 for (int i = 0; i < y.length(); i++) {
					 Integer yl=y.length();
					 if(!yl.equals(7)){
						 y="0"+y;
					 }else{
						 break;
					 }
				}
		  } 
     String barCode="03"+sign+y+x;
     	request.setAttribute("haveResume", haveResume);
		request.setAttribute("barCode",barCode);
		return "/purchase/importpackage/partiframe3";
	}
	
	/**
	 * 条状批量生成excel页面
	 * @return
	 */
	@RequestMapping(value="/toLotsExcel",method=RequestMethod.GET)
	public String toLotsExcel(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		return "/storage/exportpackage/locationListForCertification";
	}
	
	/**
	 * 出库指令内位置列表
	 */
	@RequestMapping(value="/locationList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo locationList(HttpServletRequest request) {
		PageModel<ImportStorageLocationList> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String id = getString(request, "id");
		page.put("id", id);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		importStorageLocationListService.getLocationByInstructionsId(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportStorageLocationList importStorageLocationList : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(importStorageLocationList);
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
	 * 生成多个excel
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/lotsExcel",method=RequestMethod.POST)
	public @ResponseBody ResultVo lotsExcel(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		try {
			String location = getString(request, "location");
			String id = getString(request, "id");
			List<Integer> list = importpackageElementService.getByLocation(location,id);
			List<Integer> set = new ArrayList<Integer>();
			StringBuffer ids = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				set.add(list.get(i));
				if (set.size() == 4) {
					ids.append(id).append(location).append("-");
					for (int j = 0; j < set.size(); j++) {
						ids.append(set.get(j)).append(",");
					}
					ids.deleteCharAt(ids.length()-1);
					StringBuffer businessKey = new StringBuffer();
					businessKey.append("export_package_instructions.id.").append(ids.toString()).append(".LotCertificationExcel");
					gyExcelService.generateExcel(businessKey.toString());
					set.removeAll(set);
					ids.delete( 0, ids.length() );
					Thread.sleep(1000);
				}
			}
			if (set.size() > 0) {
				ids.append(id).append(location).append("-");
				for (int j = 0; j < set.size(); j++) {
					ids.append(set.get(j)).append(",");
				}
				ids.deleteCharAt(ids.length()-1);
				StringBuffer businessKey = new StringBuffer();
				businessKey.append("export_package_instructions.id.").append(ids.toString()).append(".LotCertificationExcel");
				gyExcelService.generateExcel(businessKey.toString());
				set.removeAll(set);
				ids.delete( 0, ids.length() );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "生成失败！";
		}
		return new ResultVo(success, message);
		
	}
	
	
	/**
	 * 修改库位
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/changeLocation",method=RequestMethod.POST)
	public @ResponseBody ResultVo changeLocation(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			String location = getString(request, "location");
			ImportPackageElement importPackageElement = importpackageElementService.selectByPrimaryKey(id);
			importPackageElement.setLocation(location);
			importpackageElementService.updateByPrimaryKeySelective(importPackageElement);
			return new ResultVo(true,"修改成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常!");
		}
	}
	
	
	/**
	 * 前往查看不在出库指令的清单
	 * @return
	 */
	@RequestMapping(value="/toNotInInstruction",method=RequestMethod.GET)
	public String toNotInInstruction(HttpServletRequest request){
		request.setAttribute("id", getString(request, "id"));
		request.setAttribute("clientId", getString(request, "clientId"));
		return "/storage/exportpackage/notInInstructionList";
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
		List<SystemCode> list2 = systemCodeService.findCurrency();
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
