package com.naswork.module.system.controller.airmanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import oracle.net.aso.c;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.AirSupplierRelationKey;
import com.naswork.model.StockMarketSupplierMap;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAirRelationKey;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.service.AirSupplierRelationService;
import com.naswork.service.StockMarketSupplierMapService;
import com.naswork.service.SupplierAirRelationService;
import com.naswork.service.SupplierService;
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

/**
 * @Author: modify by white
 * @Description: 添加机型管理-----部分接口
 * @Date: 2018-08-21 15:54
*/

@Controller
@RequestMapping("/system/airmanage")
public class AirManageController extends BaseController{
	
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private UserService userService;
	@Resource
	private AirSupplierRelationService airSupplierRelationService;
	@Resource
	private SupplierAirRelationService supplierAirRelationService;
	@Resource
	private StockMarketSupplierMapService stockMarketSupplierMapService;

	/*
	 * 机型管理
	 */
	@RequestMapping(value="/toAirManage",method=RequestMethod.GET)
	public String toAirManage() {
		return "/system/airmanage/list";
	}
	
	/*
	 * 机型管理页面
	 */
	@RequestMapping(value="/airManage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo airManage(HttpServletRequest request) {
		PageModel<SystemCode> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		systemCodeService.airPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SystemCode systemCode : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(systemCode);
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
	 * StockMarket机型管理
	 */
	@RequestMapping(value="/toAirManageForStockMarket",method=RequestMethod.GET)
	public String toAirManageForStockMarket() {
		return "/system/airmanage/airlistforstockmarket";
	}
	
	/*
	 * stockmarket机型管理页面
	 */
	@RequestMapping(value="/airManageForStockMarket",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo airManageForStockMarket(HttpServletRequest request) {
		PageModel<SystemCode> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		systemCodeService.airForStockMarketPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SystemCode systemCode : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(systemCode);
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
	 * 新增机型
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd() {
		return "/system/airmanage/addair";
	}
	
	/*
	 * 检查代码持否存在
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public @ResponseBody ResultVo check(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		PageModel<String> page = new PageModel<String>();
		String code = getString(request, "code");
		page.put("code", code);
		page.put("type", "AIR_TYPE");
		List<SystemCode> list = systemCodeService.selectByCode(page);
		if (list.size()>0) {
			success = true;
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 保存新增机型
	 */
	@RequestMapping(value="/saveAir",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAir(HttpServletRequest request,@ModelAttribute SystemCode systemCode) {
		String message = "";
		boolean success = false;
		
		if (systemCode.getCode()!=null) {

			systemCodeService.saveAir(systemCode);
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	/*
	 * 新增机型
	 */
	@RequestMapping(value="/toAddAirForStockMarket",method=RequestMethod.GET)
	public String toAddAirForStockMarket() {
		return "/system/airmanage/addairforstockmarket";
	}
	
	/*
	 * 检查代码持否存在
	 */
	@RequestMapping(value="/checkAirForStockMarket",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkAirForStockMarket(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		String code = getString(request, "code");
		PageModel<String> page = new PageModel<String>();
		page.put("code", code);
		page.put("type", "AIR_TYPE_FOR_STOCK_MARKET");
		List<SystemCode> list = systemCodeService.selectByCode(page);
		if (list.size()>0) {
			success = true;
		}
		return new ResultVo(success, message);
	}
	
	/*
	 * 保存新增机型
	 */
	@RequestMapping(value="/saveAirForStockMarket",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAirForStockMarket(HttpServletRequest request,@ModelAttribute SystemCode systemCode) {
		String message = "";
		boolean success = false;
		if (systemCode.getCode()!=null) {
			UserVo userVo = getCurrentUser(request);
			String userId = userVo.getUserId();
			systemCodeService.saveAirForStockMarket(systemCode,userId);
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	
/*	
	 * 修改机型
	 
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit() {
		return "";
	}
*/	
	/*
	 * 保存修改
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo saveEdit(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		
		try {
			SystemCode systemCode = new SystemCode();
			systemCode.setId(new Integer(getString(request, "id")));
			systemCode.setCode(getString(request, "code"));
			systemCode.setValue(getString(request, "value"));
			systemCode.setRemark(getString(request, "remark"));
			systemCode.setUpdateTimestamp(new Date());
			systemCodeService.updateByPrimaryKeySelective(systemCode);
			message ="修改成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "保存失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/*
	 * 新增机型可询价供应商页面
	 */
	@RequestMapping(value="/airSupplier",method=RequestMethod.GET)
	public String airSupplier(HttpServletRequest request) {
		request.setAttribute("id", request.getParameter("id"));
		return "/system/airmanage/supplierlist";
	}
	
	/**
	 * 机型供应商关系
	 * **/
	@RequestMapping(value="/airSupplierlistData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo airSupplierlistData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<SupplierAirRelationKey> page = getPage(request);
		String id=request.getParameter("id");
		page.put("airId", id);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		supplierAirRelationService.selectByAirIdPage(page);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierAirRelationKey supplierAirRelationKey :page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierAirRelationKey);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
	}
	
	/*
	 * 新增机型可询价供应商页面
	 */
	@RequestMapping(value="/toAddSupplier",method=RequestMethod.GET)
	public String toAddSupplier() {
		return "/system/airmanage/addsupplier";
	}
	
	/**
	 * 机型供应商
	 * **/
	@RequestMapping(value="/airlistData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo airlistData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<Supplier> page = getPage(request);
		String supplierId=request.getParameter("supplierId");
		page.put("supplierId", supplierId);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		 List<Supplier> list=supplierService.Suppliers(page);
		if (list.size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Supplier supplier :list) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplier);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
	}
	
	/*
	 * 新增机型可询价供应商
	 */
	@RequestMapping(value="/addSupplier",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo addSupplier(HttpServletRequest request, @ModelAttribute SupplierAirRelationKey supplierAirRelationKey) {
		String message = "";
		boolean success = false;
		String ids=request.getParameter("ids");
//		String airId=request.getParameter("airId");
		try {
			String[] id=ids.split(",");
//			AirSupplierRelationKey record=new AirSupplierRelationKey();
//			record.setAirId(Integer.parseInt(airId));
			
			for (int i = 0; i < id.length; i++) {
				supplierAirRelationKey.setSupplierId(Integer.parseInt(id[i]));
				SupplierAirRelationKey data=supplierAirRelationService.selectBySupplierIdAndAirId(supplierAirRelationKey);
				if(null==data){
					supplierAirRelationService.insertSelective(supplierAirRelationKey);
				}
			}
			
//			for (int i = 0; i < id.length; i++) {
//				record.setSupplierId(Integer.parseInt(id[i]));
//				airSupplierRelationService.insert(record);
//			}
			 success = true;
			 message = "新增完成";
		} catch (Exception e) {
			e.printStackTrace();
			message = "保存失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/*
	 * 删除机型可询价供应商
	 */
	@RequestMapping(value="/deleteSupplier",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo deleteSupplier(HttpServletRequest request, @ModelAttribute SupplierAirRelationKey supplierAirRelationKey) {
		String message = "";
		boolean success = false;
//		String supplierId=request.getParameter("supplierId");
//		String airId=request.getParameter("airId");
		try {
//			AirSupplierRelationKey record=new AirSupplierRelationKey();
//			record.setAirId(Integer.parseInt(airId));
//			record.setSupplierId(Integer.parseInt(supplierId));
			supplierAirRelationService.deleteByPrimaryKey(supplierAirRelationKey);
			 success = true;
			 message = "删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "删除失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 类型与供应商的关联页面（stockmarmet）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/airSupplierForStockMarket",method=RequestMethod.GET)
	public String airSupplierForStockMarket(HttpServletRequest request) {
		request.setAttribute("id", request.getParameter("id"));
		return "/system/airmanage/supplierlistforstockmarket";
	}
	
	/**
	 * 新增类型与供应商的关联页面（stockmarmet）
	 * @return
	 */
	@RequestMapping(value="/toAddSupplierForStockMarket",method=RequestMethod.GET)
	public String toAddSupplierForStockMarket() {
		return "/system/airmanage/addsupplierforstockmarket";
	}
	
	/**
	 * 供应商列表（stockmarket）
	 * @param request
	 * @param response
	 * @param clientQuoteV
	 * @return
	 */
	@RequestMapping(value="/airSupplierListForStockMarket",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo airSupplierListForStockMarket(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<StockMarketSupplierMap> page = getPage(request);
		String id=request.getParameter("id");
		page.put("airId", id);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		stockMarketSupplierMapService.listPage(page, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StockMarketSupplierMap stockMarketSupplierMap :page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(stockMarketSupplierMap);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
	}
	
	/**
	 * 删除供应商（stockmarket）
	 * @param request
	 * @param supplierAirRelationKey
	 * @return
	 */
	@RequestMapping(value="/deleteSupplierForStockMarket",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo deleteSupplierForStockMarket(HttpServletRequest request, @ModelAttribute StockMarketSupplierMap stockMarketSupplierMap) {
		String message = "";
		boolean success = false;
		try {
			stockMarketSupplierMapService.deleteByPrimaryKey(stockMarketSupplierMap.getId());
			success = true;
			message = "删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "删除失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 新增供应商（stockmarket）
	 * @param request
	 * @param supplierAirRelationKey
	 * @return
	 */
	@RequestMapping(value="/addSupplierForStockMarket",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo addSupplierForStockMarket(HttpServletRequest request, @ModelAttribute StockMarketSupplierMap stockMarketSupplierMap) {
		String message = "";
		boolean success = false;
		String ids=request.getParameter("ids");
		UserVo userVo = getCurrentUser(request);
		try {
			String[] id=ids.split(",");
			for (int i = 0; i < id.length; i++) {
				stockMarketSupplierMap.setSupplierId(Integer.parseInt(id[i]));
				StockMarketSupplierMap check = stockMarketSupplierMapService.checkRecord(stockMarketSupplierMap);
				if(null==check){
					stockMarketSupplierMap.setCreateUser(new Integer(userVo.getUserId()));
					stockMarketSupplierMap.setUpdateTimestamp(new Date());
					stockMarketSupplierMapService.insertSelective(stockMarketSupplierMap);
				}
			}
			success = true;
			message = "新增完成";
		} catch (Exception e) {
			e.printStackTrace();
			message = "保存失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
	
}
