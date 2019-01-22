package com.naswork.module.storage.controller.exchangeimport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ExchangeExportPackage;
import com.naswork.model.ExchangeImportPackage;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.service.ExchangeExportPackageService;
import com.naswork.service.ExchangeImportPackageService;
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
@RequestMapping(value="/storage/exchangeimport")
public class ExchangeImportPackageController extends BaseController {
	@Resource
	private ExchangeImportPackageService exchangeImportPackageService;
	@Resource
	private UserService userService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ExchangeExportPackageService exchangeExportPackageService;
	

	/**
	 * 维修仓入库数据页面
	 * @return
	 */
	@RequestMapping(value="/toWarnList",method=RequestMethod.GET)
	public String toWarnList(){
		return "/marketing/exchangeimportpackage/warnList";
	}
	
	/**
	 * 维修仓入库数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/warnList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo warnList(HttpServletRequest request,HttpServletResponse response){
		PageModel<ExchangeImportPackage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		String exchange = getString(request, "exchange");
		if (exchange != null && !"".equals(exchange)) {
			page.put("exchange", 1);
		}else {
			page.put("notexchange", 1);
		}
		
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		
		exchangeImportPackageService.warnListPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExchangeImportPackage exchangeImportPackage : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(exchangeImportPackage);
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
	 * 入库页面
	 * @return
	 */
	@RequestMapping(value="/toList",method=RequestMethod.GET)
	public String toList(){
		return "/storage/exchangeimportpackage/importList";
	}
	
	
	/**
	 * 交换维修入库列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/importList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo importList(HttpServletRequest request,HttpServletResponse response){
		PageModel<ExchangeImportPackage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = getString(request, "searchString");
		if (where != null) {
			if (where.indexOf("eep") >= 0) {
				page.put("shipNumber", "1");
			}
		}
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		
		exchangeImportPackageService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExchangeImportPackage exchangeImportPackage : page.getEntities()) {
				Double sum = exchangeExportPackageService.sumByImportId(exchangeImportPackage.getId());
				if (sum != null) {
 					if (sum.equals(exchangeImportPackage.getAmount())) {
						exchangeImportPackage.setComplete(1);
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(exchangeImportPackage);
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
	 * 新增入库页面
	 * @return
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd(HttpServletRequest request){
		request.setAttribute("today", new Date());
		return "/storage/exchangeimportpackage/addimport";
	}
	
	/**
	 * 保存新增入库
	 * @param request
	 * @param exchangeImportPackage
	 * @return
	 */
	@RequestMapping(value="/saveAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute ExchangeImportPackage exchangeImportPackage){
		String message = "";
		boolean success = false;
		try {
			if (exchangeImportPackage.getClientOrderElementId() != null) {
				exchangeImportPackageService.insertSelective(exchangeImportPackage);
				message = "新增成功！";
				success = true;
			}else {
				message = "新增失败！";
				success = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增异常！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 交换出库列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/exportList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo exportList(HttpServletRequest request,HttpServletResponse response){
		PageModel<ExchangeExportPackage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String exchangeImportPackageId = request.getParameter("id");
		page.put("exchangeImportPackageId", exchangeImportPackageId);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		exchangeExportPackageService.listPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ExchangeExportPackage exchangeExportPackage : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(exchangeExportPackage);
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
	 * 新增出库页面
	 * @return
	 */
	@RequestMapping(value="/toAddExport",method=RequestMethod.GET)
	public String toAddExport(HttpServletRequest request){
		request.setAttribute("id", getString(request, "id"));
		request.setAttribute("today", new Date());
		return "/storage/exchangeimportpackage/addexport";
	}
	
	/**
	 * 保存新增出库
	 * @param request
	 * @param exchangeImportPackage
	 * @return
	 */
	@RequestMapping(value="/saveAddExport",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAddExport(HttpServletRequest request,@ModelAttribute ExchangeExportPackage exchangeExportPackage){
		String message = "";
		boolean success = false;
		try {
			if (exchangeExportPackage.getExchangeImportPackageId() != null) {
				exchangeExportPackageService.insertSelective(exchangeExportPackage);
				message = "新增成功！";
				success = true;
			}else {
				message = "新增失败！";
				success = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增异常！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 入库修改
	 */
	@RequestMapping(value="/editImport",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editImport(HttpServletRequest request){
		boolean success = false;
		String message = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			ExchangeImportPackage exchangeImportPackage = new ExchangeImportPackage();
			exchangeImportPackage.setId(new Integer(getString(request, "id")));
			exchangeImportPackage.setAmount(new Double(getString(request, "amount")));
			exchangeImportPackage.setDescription(getString(request, "description"));
			exchangeImportPackage.setLocation(getString(request, "location"));
			exchangeImportPackage.setImportDate(dateFormat.parse(getString(request, "importDate")));
			exchangeImportPackage.setUpdateTimestamp(new Date());
			exchangeImportPackage.setConditionId(new Integer(getString(request, "conValue")));
			exchangeImportPackage.setCertificationId(new Integer(getString(request, "cerValue")));
			exchangeImportPackage.setRemark(getString(request, "remark"));
			exchangeImportPackage.setShipNumber(getString(request, "shipNumber"));
			exchangeImportPackage.setSn(getString(request, "sn"));
			String repair = getString(request, "repairTypeValue");
			if (repair != null && !"".equals(repair)) {
				exchangeImportPackage.setRepairType(new Integer(repair));
			}
			exchangeImportPackageService.updateByPrimaryKeySelective(exchangeImportPackage);
			success = true;
			message = "修改成功!";
		} catch (Exception e) {
			message = "修改失败!";
			e.printStackTrace();
		}
		
		return new EditRowResultVo(success, message);
		
	}
	
	
	/*
	 * 出库修改
	 */
	@RequestMapping(value="/editExport",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editExport(HttpServletRequest request){
		boolean success = false;
		String message = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			ExchangeExportPackage exchangeExportPackage = new ExchangeExportPackage();
			exchangeExportPackage.setId(new Integer(getString(request, "id")));
			exchangeExportPackage.setAmount(new Double(getString(request, "amount")));
			exchangeExportPackage.setExportDate(dateFormat.parse(getString(request, "exportDate")));
			exchangeExportPackage.setRemark( getString(request, "remark"));
			exchangeExportPackage.setShipNumber(getString(request, "shipNumber"));
			exchangeExportPackage.setUpdateTimestamp(new Date());
			exchangeExportPackageService.updateByPrimaryKeySelective(exchangeExportPackage);
			success = true;
			message = "修改成功!";
		} catch (Exception e) {
			message = "修改失败!";
			e.printStackTrace();
		}
		
		return new EditRowResultVo(success, message);
		
	}
	
	
	/**
	 * 删除出库
	 * @param request
	 * @param exchangeImportPackage
	 * @return
	 */
	@RequestMapping(value="/delExport",method=RequestMethod.POST)
	public @ResponseBody ResultVo delExport(HttpServletRequest request,@ModelAttribute ExchangeExportPackage exchangeExportPackage){
		String message = "";
		boolean success = false;
		try {
			if (exchangeExportPackage.getId() != null) {
				exchangeExportPackageService.deleteByPrimaryKey(exchangeExportPackage.getId());
				message = "删除成功！";
				success = true;
			}else {
				message = "删除失败！";
				success = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "删除异常！";
			success = false;
		}
		
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
		String where = getString(request, "id");
		StringBuffer value = new StringBuffer();
		if (where!=null && !"".equals(where)) {
			ExchangeImportPackage exchangeImportPackage = exchangeImportPackageService.selectByPrimaryKey(new Integer(where));
			for (int i = 0; i < certificationList.size(); i++) {
				if (certificationList.get(i).getId().equals(exchangeImportPackage.getCertificationId())) {
					list.add(certificationList.get(i));
					break;
				}
			}
			for (int i = 0; i < certificationList.size(); i++) {
				if (!certificationList.get(i).getId().equals(exchangeImportPackage.getCertificationId())) {
					list.add(certificationList.get(i));
				}
			}
			//拼接数据，页面双击修改使用
			for (int i = 0; i < list.size(); i++) {
				value.append(list.get(i).getId()).append(":").append(list.get(i).getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
		
			message = value.toString();
		}else {
			list = certificationList;
			JSONArray json = new JSONArray();
			json.add(list);
			message =json.toString();
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
		String where = getString(request, "id");
		StringBuffer value = new StringBuffer();
		if (where!=null && !"".equals(where)) {
			ExchangeImportPackage exchangeImportPackage = exchangeImportPackageService.selectByPrimaryKey(new Integer(where));
			for (int i = 0; i < conditionList.size(); i++) {
				if (conditionList.get(i).getId().equals(exchangeImportPackage.getConditionId())) {
					list.add(conditionList.get(i));
					break;
				}
			}
			for (int i = 0; i < conditionList.size(); i++) {
				if (!conditionList.get(i).getId().equals(exchangeImportPackage.getConditionId())) {
					list.add(conditionList.get(i));
				}
			}
			//拼接数据，页面双击修改使用
			for (int i = 0; i < list.size(); i++) {
				value.append(list.get(i).getId()).append(":").append(list.get(i).getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
		
			message = value.toString();
		}else {
			Collections.reverse(conditionList);
			list = conditionList;
			JSONArray json = new JSONArray();
			json.add(list);
			message =json.toString();
		}
		
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 证书
	 */
	@RequestMapping(value="/repairType",method=RequestMethod.POST)
	public @ResponseBody ResultVo repairType(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		List<SystemCode> list = new ArrayList<SystemCode>();
		List<SystemCode> repairTypeList = systemCodeService.findType("REPAIR_TYPE");
		String where = getString(request, "id");
		StringBuffer value = new StringBuffer();
		if (where!=null && !"".equals(where)) {
			ExchangeImportPackage exchangeImportPackage = exchangeImportPackageService.selectByPrimaryKey(new Integer(where));
			for (int i = 0; i < repairTypeList.size(); i++) {
				if (repairTypeList.get(i).getId().equals(exchangeImportPackage.getRepairType())) {
					list.add(repairTypeList.get(i));
					break;
				}
			}
			for (int i = 0; i < repairTypeList.size(); i++) {
				if (!repairTypeList.get(i).getId().equals(exchangeImportPackage.getRepairType())) {
					list.add(repairTypeList.get(i));
				}
			}
			//拼接数据，页面双击修改使用
			for (int i = 0; i < list.size(); i++) {
				value.append(list.get(i).getId()).append(":").append(list.get(i).getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
		
			message = value.toString();
		}else {
			list = repairTypeList;
			JSONArray json = new JSONArray();
			json.add(list);
			message =json.toString();
		}
		return new ResultVo(success, message);
	}
	
	/*
	 * 文件上传
	 */
	@RequestMapping(value="/fileUpload",method=RequestMethod.GET)
	public String fileUpload(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "excahnge_import_package");
		return "/marketing/clientinquiry/fileUpload";
	}
	
	/**
	 * 状态
	 */
	@RequestMapping(value="/exchangeImportEmail",method=RequestMethod.POST)
	public @ResponseBody ResultVo exchangeImportEmail(HttpServletRequest request) {
		UserVo userVo=getCurrentUser(request);
		MessageVo messageVo = exchangeImportPackageService.email(userVo);
		return new ResultVo(messageVo.getFlag(), messageVo.getMessage());
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
