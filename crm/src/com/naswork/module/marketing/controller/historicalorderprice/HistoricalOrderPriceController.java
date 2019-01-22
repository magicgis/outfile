package com.naswork.module.marketing.controller.historicalorderprice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.HistoricalOrderPrice;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.HistoricalOrderPriceService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping(value="/order/historicalorderprice")
public class HistoricalOrderPriceController extends BaseController {

	@Resource
	private HistoricalOrderPriceService historicalOrderPriceService;
	@Resource
	private UserService userService;
	
	
	/**
	 * 跳转历史客户价格
	 * @return
	 */
	@RequestMapping(value="/toClientOrderList",method = RequestMethod.GET)
	public String toClientOrderList() {
		return "/marketing/clienthistoricalprice/historicalprice";
	}
	
	/**
	 * 跳转历史供应商价格
	 * @return
	 */
	@RequestMapping(value="/toSupplierOrderList",method = RequestMethod.GET)
	public String toSupplierOrderList() {
		return "/purchase/supplierhistoricalprice/historicalprice";
	}
	
	/**
	 * 历史客户价格列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/clientOrderList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo clientOrderList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<HistoricalOrderPrice> page=getPage(request);
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
		
		historicalOrderPriceService.clientPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (HistoricalOrderPrice historicalOrderPrice : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(historicalOrderPrice);
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
	 * 历史供应商价格列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/supplierOrderList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierOrderList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<HistoricalOrderPrice> page=getPage(request);
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
		
		historicalOrderPriceService.supplierPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (HistoricalOrderPrice historicalOrderPrice : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(historicalOrderPrice);
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
	 * 历史客户采购价excel上传
	 */
	@RequestMapping(value="/uploadExcelForClient",method=RequestMethod.POST)
	public @ResponseBody String uploadExcelForClient(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = historicalOrderPriceService.clientUploadExcel(multipartFile);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	
	/**
	 * 历史供应商采购价excel上传
	 */
	@RequestMapping(value="/uploadExcelForSupplier",method=RequestMethod.POST)
	public @ResponseBody String uploadExcelForSupplier(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = historicalOrderPriceService.supplierUploadExcel(multipartFile);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/*
	 * 文件管理
	 */
	@RequestMapping(value="/file",method=RequestMethod.GET)
	public String file(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "client_inquiry");
		return "/marketing/clientinquiry/fileUpload";
	}
	
}
