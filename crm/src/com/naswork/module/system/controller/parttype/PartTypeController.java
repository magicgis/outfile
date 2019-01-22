package com.naswork.module.system.controller.parttype;

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
import com.naswork.model.PartTypeParent;
import com.naswork.model.PartTypeSubset;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAirRelationKey;
import com.naswork.model.SupplierPartTypeRelationKey;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.service.PartTypeParentService;
import com.naswork.service.PartTypeSubsetService;
import com.naswork.service.SupplierPartTypeRelationService;
import com.naswork.service.SupplierService;
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

@Controller
@RequestMapping(value="/system/parttype")
public class PartTypeController extends BaseController {

	@Resource
	private PartTypeParentService partTypeParentService;
	@Resource
	private PartTypeSubsetService partTypeSubsetService;
	@Resource
	private TPartService tPartService;
	@Resource
	private UserService userService;
	@Resource
	private SupplierPartTypeRelationService supplierPartTypeRelationService;
	@Resource
	private SupplierService supplierService;
	
	/**
	 * 跳转件号类型页面
	 * @return
	 */
	@RequestMapping(value="/toList",method=RequestMethod.GET)
	public String toList() {
		return "";
	}
	
	/**
	 * 类型数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartTypeParent> page=getPage(request);
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
		
		partTypeParentService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartTypeParent partTypeParent : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(partTypeParent);
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
	 * 跳转件号小类型页面
	 * @return
	 */
	@RequestMapping(value="/toElementList",method=RequestMethod.GET)
	public String toElementList() {
		return "/system/parttypemanage/list";
	}
	
	/**
	 * 类型明细数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/elementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartTypeSubset> page=getPage(request);
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
		
		partTypeSubsetService.listPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartTypeSubset partTypeSubset : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(partTypeSubset);
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
	 * 新增父类型
	 * @return
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd() {
		return "/system/parttype/addparttye";
	}
	
	/**
	 * 保存新增父类
	 * @param request
	 * @param partTypeParent
	 * @return
	 */
	@RequestMapping(value="/saveAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute PartTypeParent partTypeParent) {
		boolean success = false;
		String message = "";
		try {
			if (partTypeParent.getType() != null) {
				partTypeParentService.insertSelective(partTypeParent);
				success = true;
				message = "新增成功！";
			}else {
				message = "新增失败！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "新增异常！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	
	/**
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
		MessageVo messageVo = partTypeSubsetService.uploadExcel(multipartFile, id);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * table新增明细
	 * @return
	 */
	public String addElementTable() {
		return "";
	}
	
	/**
	 * 页面子类明细
	 */
	@RequestMapping(value="/addElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo addElement(HttpServletRequest request,@ModelAttribute PartTypeSubset partTypeSubset) {
		String message = "";
		boolean success = false;
		Integer id = new Integer(getString(request, "id"));
		try {
			if (partTypeSubset.getList().size()>0) {
				for (int i = 0; i < partTypeSubset.getList().size(); i++) {
					partTypeSubset.getList().get(i).setPartTypeParentId(id);
					partTypeSubsetService.insertSelective(partTypeSubset.getList().get(i));
				}
				message = "新增成功！";
				success = true;
			}else {
				message = "新增失败！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增异常！";
			success = false;
		}
		
		return new ResultVo(success,message);
	}
	
	/**
	 * 查询飞机类型
	 */
	@RequestMapping(value="/partType",method=RequestMethod.POST)
	public @ResponseBody ResultVo partType(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		List<PartTypeSubset> list=partTypeSubsetService.list();
		List<PartTypeSubset> arraylist=new ArrayList<PartTypeSubset>();
		if (!"".equals(getString(request, "bsn")) && getString(request, "bsn") != null) {
			TPart tPart = tPartService.selectByPrimaryKey(getString(request, "bsn"));
			PartTypeSubset partTypeSubset = partTypeSubsetService.selectByPrimaryKey(tPart.getPartType());
			UserVo userVo=getCurrentUser(request);
			String userName=userVo.getUserName();
			if (partTypeSubset != null) {
				for (int i = 0; i < list.size(); i++) {
					if(partTypeSubset.getId().equals(list.get(i).getId())){
						arraylist.add(list.get(i));
					}	
				}
				for (int i = 0; i < list.size(); i++) {
					if(!partTypeSubset.getId().equals(list.get(i).getId())){
						arraylist.add(list.get(i));
					}	
				}
			}else {
				arraylist = list;
			}
		}else {
			arraylist = list;
		}
		
		success = true;
		JSONArray json = new JSONArray();
		json.add(arraylist);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	
	/*
	 * 新增机型
	 */
	@RequestMapping(value="/toAddElement",method=RequestMethod.GET)
	public String toAddElement() {
		return "/system/parttypemanage/addair";
	}
	
	/*
	 * 检查代码持否存在
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public @ResponseBody ResultVo check(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		String code = getString(request, "code");
		List<PartTypeSubset> list = partTypeSubsetService.selectByCode(code);
		if (list.size()>0) {
			success = true;
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 保存新增机型
	 */
	@RequestMapping(value="/saveAir",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAir(HttpServletRequest request,@ModelAttribute PartTypeSubset partTypeSubset) {
		String message = "";
		boolean success = false;
		
		if (partTypeSubset.getCode()!=null) {
			partTypeSubset.setUpdateTimestamp(new Date());
			partTypeSubsetService.insertSelective(partTypeSubset);
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
			PartTypeSubset partTypeSubset = new PartTypeSubset();
			partTypeSubset.setId(new Integer(getString(request, "id")));
			partTypeSubset.setCode(getString(request, "code"));
			partTypeSubset.setValue(getString(request, "value"));
			partTypeSubset.setRemark(getString(request, "remark"));
			partTypeSubset.setUpdateTimestamp(new Date());
			partTypeSubsetService.updateByPrimaryKeySelective(partTypeSubset);
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
		return "/system/parttypemanage/supplierlist";
	}
	
	/**
	 * 机型供应商关系
	 * **/
	@RequestMapping(value="/airSupplierlistData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo airSupplierlistData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<SupplierPartTypeRelationKey> page = getPage(request);
		String id=request.getParameter("id");
		page.put("partTypeSubsetId", id);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		supplierPartTypeRelationService.selectByPartPage(page);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierPartTypeRelationKey supplierPartTypeRelationKey :page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierPartTypeRelationKey);
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
		return "/system/parttypemanage/addsupplier";
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
	public @ResponseBody EditRowResultVo addSupplier(HttpServletRequest request, @ModelAttribute SupplierPartTypeRelationKey supplierPartTypeRelationKey) {
		String message = "";
		boolean success = false;
		String ids=request.getParameter("ids");
//		String airId=request.getParameter("airId");
		try {
			String[] id=ids.split(",");
//			AirSupplierRelationKey record=new AirSupplierRelationKey();
//			record.setAirId(Integer.parseInt(airId));
			
			for (int i = 0; i < id.length; i++) {
				supplierPartTypeRelationKey.setSupplierId(Integer.parseInt(id[i]));
				SupplierPartTypeRelationKey data=supplierPartTypeRelationService.selectBySupplierIdAndAirId(supplierPartTypeRelationKey);
				if(null==data){
					supplierPartTypeRelationService.insertSelective(supplierPartTypeRelationKey);
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
	public @ResponseBody EditRowResultVo deleteSupplier(HttpServletRequest request, @ModelAttribute SupplierPartTypeRelationKey supplierPartTypeRelationKey) {
		String message = "";
		boolean success = false;
//		String supplierId=request.getParameter("supplierId");
//		String airId=request.getParameter("airId");
		try {
//			AirSupplierRelationKey record=new AirSupplierRelationKey();
//			record.setAirId(Integer.parseInt(airId));
//			record.setSupplierId(Integer.parseInt(supplierId));
			supplierPartTypeRelationService.deleteByPrimaryKey(supplierPartTypeRelationKey);
			 success = true;
			 message = "删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "删除失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
}
