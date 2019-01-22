package com.naswork.module.storage.controller.storehouseaddress;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.StorehouseAddress;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.ImportStorageLocationListService;
import com.naswork.service.StorehouseAddressService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/system/storehouseaddress")
public class StorehouseAddressController extends BaseController {

	@Resource
	private StorehouseAddressService storehouseAddressService;
	@Resource
	private ImportStorageLocationListService importStorageLocationListService;
	
	/**
	 * 仓库地址管理页面
	 * @return
	 */
	@RequestMapping(value="/toStorehouseAddressList",method=RequestMethod.GET)
	public String toStorehouseAddressList() {
		return "/system/storehouseaddress/list";
	}
	
	/**
	 * 数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/StorehouseAddressList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo StorehouseAddressList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StorehouseAddress> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		/*RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}*/
		
		storehouseAddressService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StorehouseAddress storehouseAddress : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(storehouseAddress);
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
	 * 跳转新增仓库地址管理页面
	 * @return
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd() {
		return "/system/storehouseaddress/addstorehouseaddress";
	}
	
	/**
	 * 跳转新增仓库地址管理页面
	 * @return
	 */
	@RequestMapping(value="/addStorehouseAddress",method=RequestMethod.POST)
	public @ResponseBody ResultVo addStorehouseAddress(HttpServletRequest request,@ModelAttribute StorehouseAddress storehouseAddress) {
		boolean success = false;
		String message = "";
		try {
			if (storehouseAddress.getName() != null) {
				storehouseAddressService.insertSelective(storehouseAddress);
				message = storehouseAddress.getId().toString();
				success = true;
			}else {
				message = "新增失败！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增异常！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 跳转修改仓库地址管理页面
	 * @return
	 */
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		StorehouseAddress storehouseAddress = storehouseAddressService.selectByPrimaryKey(id);
		request.setAttribute("storehouseAddress", storehouseAddress);
		return "/system/storehouseaddress/addstorehouseaddress";
	}
	
	/**
	 * 跳转新增仓库地址管理页面
	 * @return
	 */
	@RequestMapping(value="/editStorehouseAddress",method=RequestMethod.POST)
	public @ResponseBody ResultVo editStorehouseAddress(HttpServletRequest request,@ModelAttribute StorehouseAddress storehouseAddress) {
		boolean success = false;
		String message = "";
		try {
			if (storehouseAddress.getId() != null) {
				storehouseAddress.setUpdateTimestamp(new Date());
				storehouseAddressService.updateByPrimaryKeySelective(storehouseAddress);
				message = "修改成功！";
				success = true;
			}else {
				message = "修改失败！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "修改异常！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 获取所有仓库
	 * @return
	 */
	@RequestMapping(value="/getAll",method=RequestMethod.POST)
	public @ResponseBody ResultVo getAll() {
		boolean success = false;
		String msg = "";
		List<StorehouseAddress> list=storehouseAddressService.selectAll();
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 明细数据数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/elementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ImportStorageLocationList> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		Integer storehouseAddressId = new Integer(getString(request, "id"));
		/*RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}*/
		page.put("storehouseAddressId", storehouseAddressId);
		importStorageLocationListService.listPage(page);
		
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
	
	
	/*
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			ClientInquiryElement clientInquiryElement) {
		boolean success=false;
		String message="";
		Integer id =new Integer(getString(request, "id"));
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = storehouseAddressService.uploadExcel(multipartFile, id);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/*
	 * 少量新增明细
	 */
	@RequestMapping(value="/toAddElement",method=RequestMethod.GET)
	public String toAddElement(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		String token=get32UUID();
		request.setAttribute("token", token);
		return "/system/storehouseaddress/addElementTable";
		
	}
	
	/*
	 * 页面新增明细
	 */
	@RequestMapping(value="/addElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo addElement(HttpServletRequest request,@ModelAttribute ImportStorageLocationList importStorageLocationList) {
		boolean success = false;
		String message = "";
		Integer storehouseAddressId = new Integer(getString(request, "id"));
		HttpSession session = request.getSession();
		try {
			if (importStorageLocationList.getList().size()>0) {
				importStorageLocationListService.addWithTable(importStorageLocationList.getList(), storehouseAddressId);
				success = true;
				message = "新增成功！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 新增寄卖后跳转明细新增
	 */
	@RequestMapping(value="/toAddElementAfterAdd",method=RequestMethod.GET)
	public String toAddElementAfterAdd(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		return "/system/storehouseaddress/addElement";
		
	}
	
}
