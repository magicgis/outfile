package com.naswork.module.system.controller.staticprice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.stereotype.Controller;

import com.naswork.common.controller.BaseController;
import com.naswork.dao.SystemCodeDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.StaticClientQuotePrice;
import com.naswork.model.StaticSupplierQuotePrice;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.service.StaticClientQuotePriceService;
import com.naswork.service.StaticSupplierQuotePriceService;
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

@Controller
@RequestMapping(value="/system/staticprice")
public class StaticQuotePriceController extends BaseController {

	@Resource
	private UserService userService;
	@Resource
	private StaticClientQuotePriceService staticClientQuotePriceService;
	@Resource
	private StaticSupplierQuotePriceService staticSupplierQuotePriceService;
	@Resource
	private SystemCodeService systemCodeService;
	
	
	/**
	 * 跳转客户页面
	 * @return
	 */
	@RequestMapping(value="/toClientList",method=RequestMethod.GET)
	public String toClientList() {
		return "/system/staticquoteprice/staticclientquotepriceList";
	}
	
	
	/**
	 * 客户列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/clientListData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo clientListData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StaticClientQuotePrice> page=getPage(request);
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
		
		staticClientQuotePriceService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StaticClientQuotePrice staticClientQuotePrice : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(staticClientQuotePrice);
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
	 * 跳转供应商页面
	 * @return
	 */
	@RequestMapping(value="/toSupplierList",method=RequestMethod.GET)
	public String toSupplierList() {
		return "/system/staticquoteprice/staticsupplierquotepriceList";
	}
	
	
	/**
	 * 供应商列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/supplierListData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierListData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StaticSupplierQuotePrice> page=getPage(request);
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
		
		staticSupplierQuotePriceService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StaticSupplierQuotePrice staticSupplierQuotePrice : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(staticSupplierQuotePrice);
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
	 * 供应商列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	/*@RequestMapping(value="/supplier",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierListData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StaticSupplierQuotePrice> page=getPage(request);
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
		
		staticSupplierQuotePriceService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StaticSupplierQuotePrice staticSupplierQuotePrice : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(staticSupplierQuotePrice);
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
	}*/
	
	/**
	 * 客户excel上传
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/uploadExcelForClient",method=RequestMethod.POST)
	public @ResponseBody String uploadExcelForClient(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = staticClientQuotePriceService.uploadExcel(multipartFile);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	
	/**
	 * 修改客户固定价目表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editClientQuote",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editClientQuote(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		try {
			Integer id = new Integer(getString(request, "id"));
			Double price = new Double(getString(request, "price"));
			Integer year = new Integer(getString(request, "year"));
			Integer currencyId = new Integer(getString(request, "currencyValue"));
			StaticClientQuotePrice staticClientQuotePrice = new StaticClientQuotePrice();
			staticClientQuotePrice.setId(id);
			staticClientQuotePrice.setPrice(price);
			staticClientQuotePrice.setYear(year);
			staticClientQuotePrice.setUpdateTimestamp(new Date());
			staticClientQuotePrice.setCurrencyId(currencyId);
			staticClientQuotePriceService.updateByPrimaryKeySelective(staticClientQuotePrice);
			message = "新增成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增失败！";
			success = false;
		}
		return new EditRowResultVo(success, message);
		
	}
	
	
	/**
	 * 页面新增
	 * @return
	 */
	@RequestMapping(value="/toAddForClient",method=RequestMethod.GET)
	public String toAddForClient() {
		return "/system/staticquoteprice/addclientTable";
	}
	
	/**
	 * 客户页面新增
	 * @param request
	 * @param staticClientQuotePrice
	 * @return
	 */
	@RequestMapping(value="/addWithPageForClient",method=RequestMethod.POST)
	public @ResponseBody ResultVo addWithPageForClient(HttpServletRequest request,@ModelAttribute StaticClientQuotePrice staticClientQuotePrice) {
		String message = "";
		boolean success = false;
		if (staticClientQuotePrice.getList().size()>0) {
			ResultVo resultVo = staticClientQuotePriceService.addWithPage(staticClientQuotePrice.getList());
			return resultVo;
		}else {
			message = "新增失败！";
			success = true;
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 修改供应商固定价目表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editSupplierQuote",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editSupplierQuote(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		try {
			Integer id = new Integer(getString(request, "id"));
			Double price = new Double(getString(request, "price"));
			Integer year = new Integer(getString(request, "year"));
			Integer currencyId = new Integer(getString(request, "currencyValue"));
			Integer conditionId = new Integer(getString(request, "conditionValue"));
			Integer certificationId = new Integer(getString(request, "certificationValue"));
			StaticSupplierQuotePrice staticSupplierQuotePrice = new StaticSupplierQuotePrice();
			staticSupplierQuotePrice.setId(id);
			staticSupplierQuotePrice.setPrice(price);
			staticSupplierQuotePrice.setYear(year);
			staticSupplierQuotePrice.setUpdateTimestamp(new Date());
			staticSupplierQuotePrice.setCurrencyId(currencyId);
			staticSupplierQuotePrice.setConditionId(conditionId);
			staticSupplierQuotePrice.setCertificationId(certificationId);
			staticSupplierQuotePriceService.edit(staticSupplierQuotePrice);
			message = "新增成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增失败！";
			success = false;
		}
		return new EditRowResultVo(success, message);
		
	}
	
	
	/**
	 * 供应商excel上传
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/uploadExcelForSupplier",method=RequestMethod.POST)
	public @ResponseBody String uploadExcelForSupplier(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = staticSupplierQuotePriceService.uploadExcel(multipartFile);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * 供应商页面新增
	 * @return
	 */
	@RequestMapping(value="/toAddForSupplier",method=RequestMethod.GET)
	public String toAddForSupplier() {
		return "/system/staticquoteprice/addsupplierTable";
	}
	
	/**
	 * 页面新增
	 * @param request
	 * @param staticClientQuotePrice
	 * @return
	 */
	@RequestMapping(value="/addWithPageForSupplier",method=RequestMethod.POST)
	public @ResponseBody ResultVo addWithPageForSupplier(HttpServletRequest request,@ModelAttribute StaticSupplierQuotePrice staticSupplierQuotePrice) {
		String message = "";
		boolean success = false;
		if (staticSupplierQuotePrice.getList().size()>0) {
			ResultVo resultVo = staticSupplierQuotePriceService.addWithPage(staticSupplierQuotePrice.getList());
			return resultVo;
		}else {
			message = "新增失败！";
			success = true;
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 客户货币信息
	 */
	@RequestMapping(value="/currencyTypeForClient",method=RequestMethod.POST)
	public @ResponseBody ResultVo currencyTypeForClient(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		try {
			List<ClientQuoteVo> list2 = systemCodeService.findRate();
			List<ClientQuoteVo> list = new ArrayList<ClientQuoteVo>();
			if (getString(request, "id") != null && !"".equals(getString(request, "id"))) {
				Integer id = new Integer(getString(request, "id"));
				StaticClientQuotePrice staticClientQuotePrice = staticClientQuotePriceService.selectByPrimaryKey(id);
				for (ClientQuoteVo clientQuoteVo : list2) {
					if (clientQuoteVo.getCurrency_id().equals(staticClientQuotePrice.getCurrencyId())) {
						list.add(clientQuoteVo);
						break;
					}
				}
				for (ClientQuoteVo clientQuoteVo : list2) {
					if (!clientQuoteVo.getCurrency_id().equals(staticClientQuotePrice.getCurrencyId())) {
						list.add(clientQuoteVo);
					}
				}
				StringBuffer value = new StringBuffer();
				for (ClientQuoteVo clientQuoteVo : list) {
					value.append(clientQuoteVo.getCurrency_id()).append(":").append(clientQuoteVo.getCurrency_value()).append(";");
				}
				value.deleteCharAt(value.length()-1);
				message = value.toString();
			}else {
				list = list2;
				JSONArray json = new JSONArray();
				json.add(list);
				message = json.toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
			success=false;
		}

		return new ResultVo(success, message);
	}
	
	/**
	 * 供应商货币信息
	 */
	@RequestMapping(value="/currencyTypeForSupplier",method=RequestMethod.POST)
	public @ResponseBody ResultVo currencyTypeForSupplier(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		try {
			List<ClientQuoteVo> list2 = systemCodeService.findRate();
			List<ClientQuoteVo> list = new ArrayList<ClientQuoteVo>();
			if (getString(request, "id") != null && !"".equals(getString(request, "id"))) {
				Integer id = new Integer(getString(request, "id"));
				StaticSupplierQuotePrice staticSupplierQuotePrice = staticSupplierQuotePriceService.selectByPrimaryKey(id);
				for (ClientQuoteVo clientQuoteVo : list2) {
					if (clientQuoteVo.getCurrency_id().equals(staticSupplierQuotePrice.getCurrencyId())) {
						list.add(clientQuoteVo);
						break;
					}
				}
				for (ClientQuoteVo clientQuoteVo : list2) {
					if (!clientQuoteVo.getCurrency_id().equals(staticSupplierQuotePrice.getCurrencyId())) {
						list.add(clientQuoteVo);
					}
				}
				StringBuffer value = new StringBuffer();
				for (ClientQuoteVo clientQuoteVo : list) {
					value.append(clientQuoteVo.getCurrency_id()).append(":").append(clientQuoteVo.getCurrency_value()).append(";");
				}
				value.deleteCharAt(value.length()-1);
				message = value.toString();
			}else {
				list = list2;
				JSONArray json = new JSONArray();
				json.add(list);
				message = json.toString();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			success=false;
		}

		return new ResultVo(success, message);
	}
	
	/**
	 * 状态信息
	 */
	@RequestMapping(value="/findConditionForSupplier",method=RequestMethod.POST)
	public @ResponseBody ResultVo findConditionForSupplier(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		try {
			List<SystemCode> list2 = systemCodeService.findTypeSortWithCode("COND");
			List<SystemCode> list = new ArrayList<SystemCode>();
			if (getString(request, "id") != null && !"".equals(getString(request, "id"))) {
				Integer id = new Integer(getString(request, "id"));
				StaticSupplierQuotePrice staticSupplierQuotePrice = staticSupplierQuotePriceService.selectByPrimaryKey(id);
				for (SystemCode systemCode : list2) {
					if (systemCode.getId().equals(staticSupplierQuotePrice.getConditionId())) {
						list.add(systemCode);
						break;
					}
				}
				for (SystemCode systemCode : list2) {
					if (!systemCode.getId().equals(staticSupplierQuotePrice.getConditionId())) {
						list.add(systemCode);
					}
				}
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getCode().equals("SV") || list.get(i).getCode().equals("OH") || list.get(i).getCode().equals("RE") || list.get(i).getCode().equals("Exchange")) {
						list.remove(i);
						i= i-1;
					}
				}
				StringBuffer value = new StringBuffer();
				for (SystemCode systemCode : list) {
					value.append(systemCode.getId()).append(":").append(systemCode.getCode()+"-"+systemCode.getValue()).append(";");
				}
				value.deleteCharAt(value.length()-1);
				message = value.toString();
			}else {
				for (int i = 0; i < list2.size(); i++) {
					if (list2.get(i).getCode().equals("FN")) {
						list.add(list2.get(i));
						break;
					}
				}
				for (int i = 0; i < list2.size(); i++) {
					if (list2.get(i).getCode().equals("NE")) {
						list.add(list2.get(i));
						break;
					}
				}
				for (int i = 0; i < list2.size(); i++) {
					if (list2.get(i).getCode().equals("NS")) {
						list.add(list2.get(i));
						break;
					}
				}
				for (int i = 0; i < list2.size(); i++) {
					if (list2.get(i).getCode().equals("OH-OUTRIGHT")) {
						list.add(list2.get(i));
						break;
					}
				}
				for (int i = 0; i < list2.size(); i++) {
					if (list2.get(i).getCode().equals("SV-OUTRIGHT")) {
						list.add(list2.get(i));
						break;
					}
				}
				for (int i = 0; i < list2.size(); i++) {
					if (list2.get(i).getCode().equals("Tested/Inspected-Outright")) {
						list.add(list2.get(i));
						break;
					}
				}
				for (int i = 0; i < list2.size(); i++) {
					if ((!list2.get(i).getCode().equals("FN") && !list2.get(i).getCode().equals("NE") && !list2.get(i).getCode().equals("NS") 
							&& !list2.get(i).getCode().equals("OH-OUTRIGHT") && !list2.get(i).getCode().equals("SV-OUTRIGHT") 
							&& !list2.get(i).getCode().equals("Inventory") && !list2.get(i).getCode().equals("MRO Capability") && !list2.get(i).getCode().equals("Tested/Inspected-Outright"))
							&& !(list2.get(i).getCode().equals("SV") || list2.get(i).getCode().equals("OH") || list2.get(i).getCode().equals("RE") || list2.get(i).getCode().equals("Exchange"))) {
						list.add(list2.get(i));
					}
				}
				for (int i = 0; i < list2.size(); i++) {
					if (list2.get(i).getCode().equals("Inventory") || list2.get(i).getCode().equals("MRO Capability")) {
						list.add(list2.get(i));
					}
				}
				/*for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getCode().equals("SV") || list.get(i).getCode().equals("OH") || list.get(i).getCode().equals("RE") || list.get(i).getCode().equals("Exchange")) {
						list.remove(i);
						i= i-1;
					}
				}*/
				JSONArray json = new JSONArray();
				json.add(list);
				message = json.toString();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			success=false;
		}

		return new ResultVo(success, message);
	}
	
	/**
	 * 状态信息
	 */
	@RequestMapping(value="/findCertificationForSupplier",method=RequestMethod.POST)
	public @ResponseBody ResultVo findCertificationForSupplier(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		try {
			List<SystemCode> list2 = systemCodeService.findType("CERT");
			List<SystemCode> list = new ArrayList<SystemCode>();
			if (getString(request, "id") != null && !"".equals(getString(request, "id"))) {
				Integer id = new Integer(getString(request, "id"));
				StaticSupplierQuotePrice staticSupplierQuotePrice = staticSupplierQuotePriceService.selectByPrimaryKey(id);
				for (SystemCode systemCode : list2) {
					if (systemCode.getId().equals(staticSupplierQuotePrice.getCertificationId())) {
						list.add(systemCode);
						break;
					}
				}
				for (SystemCode systemCode : list2) {
					if (!systemCode.getId().equals(staticSupplierQuotePrice.getCertificationId())) {
						list.add(systemCode);
					}
				}
				StringBuffer value = new StringBuffer();
				for (SystemCode systemCode : list) {
					value.append(systemCode.getId()).append(":").append(systemCode.getCode()+"-"+systemCode.getValue()).append(";");
				}
				value.deleteCharAt(value.length()-1);
				message = value.toString();
			}else {
				list = list2;
				JSONArray json = new JSONArray();
				json.add(list);
				message = json.toString();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			success=false;
		}

		return new ResultVo(success, message);
	}
	
	/**
	 * 客户删除
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/clientDelete",method=RequestMethod.POST)
	public @ResponseBody ResultVo clientDelete(HttpServletRequest request) {
		try {
			Integer id = new Integer(getString(request, "id"));
			staticClientQuotePriceService.deleteByPrimaryKey(id);
			return new ResultVo(true, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "删除失败！");
		}
		
	}
	
	/**
	 * 供应商删除
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/supplierDelete",method=RequestMethod.POST)
	public @ResponseBody ResultVo supplierDelete(HttpServletRequest request) {
		try {
			Integer id = new Integer(getString(request, "id"));
			staticSupplierQuotePriceService.deleteByPrimaryKey(id);
			return new ResultVo(true, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "删除失败！");
		}
		
	}
	
}
