package com.naswork.module.system.controller.suppliermanage;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.naswork.filter.ContextHolder;
import com.naswork.model.Country;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAirRelationKey;
import com.naswork.model.SupplierAnnualOffer;
import com.naswork.model.SupplierAptitude;
import com.naswork.model.SupplierCageRelationKey;
import com.naswork.model.SupplierClassify;
import com.naswork.model.SupplierContact;
import com.naswork.model.SystemCode;
import com.naswork.model.TManufactory;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.service.CountryService;
import com.naswork.service.GyFjService;
import com.naswork.service.SupplierAirRelationService;
import com.naswork.service.SupplierAnnualOfferService;
import com.naswork.service.SupplierAptitudeService;
import com.naswork.service.SupplierCageRelationService;
import com.naswork.service.SupplierClassifyService;
import com.naswork.service.SupplierContactService;
import com.naswork.service.SupplierService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.TManufactoryService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONArray;
@Controller
@RequestMapping("/suppliermanage")
public class SupplierManageController extends BaseController {
	@Resource
	private SupplierService supplierService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private SupplierContactService supplierContactService;
	@Resource
	private SupplierClassifyService supplierClassifyService;
	@Resource
	private UserService userService;
	@Resource
	private SupplierCageRelationService supplierCageRelationService;
	@Resource
	private SupplierAirRelationService supplierAirRelationService;
	@Resource
	private TManufactoryService tManufactoryService;
	@Resource
	private SupplierAnnualOfferService supplierAnnualOfferService;
	@Resource
	private GyFjService gyFjService;
	@Resource
	private SupplierAptitudeService supplierAptitudeService;
	@Resource
	private CountryService countryService;
	
	/**
	 * 供应商管理列表
	 * **/
	@RequestMapping(value="/viewList",method=RequestMethod.GET)
	public String viewList(){
		return "/system/suppliermanage/list";
	}
	
	/**
	 * 供应商管理列表
	 * **/
	@RequestMapping(value="/approval",method=RequestMethod.GET)
	public String approval(){
		return "/demo/approval";
	}
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/listData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo queryclientquote(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<Supplier> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		String exportModel = getString(request, "exportModel");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		String cageCode = getString(request, "cageCode");
		if(user!=null){
			userId= user.getUserId();
		}
		
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
			if(null!=searchString&&!searchString.equals("")){
				searchString="ar.USER_ID ="+userId+" and "+searchString;
			}
			else{
				searchString="ar.USER_ID ="+userId;
			}
		}
		if (cageCode != null && !"".equals(cageCode)) {
			page.put("cageCode", cageCode);
			if (searchString != null && !"".equals(searchString)) {
				searchString = searchString + "and tm.CAGE_CODE = '"+cageCode+"'";
			}else {
				searchString = "tm.CAGE_CODE = '"+cageCode+"'";
			}
		}
		supplierService.listPage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Supplier supplier : page.getEntities()) {
				List<SupplierClassify> list=supplierClassifyService.selectByPrimaryKey(supplier.getId());
				String supplierClassifyValue=new String();
				for (SupplierClassify supplierClassify : list) {
					if(!"".equals(supplierClassify.getSupplierClassifyId())&&null!=supplierClassify.getSupplierClassifyId()){
						SystemCode systemCode=systemCodeService.findById(Integer.parseInt(supplierClassify.getSupplierClassifyId()));
						supplierClassifyValue+=systemCode.getValue()+"/";
						supplier.setSupplierClassifyValue(supplierClassifyValue);
					}
				}
				List<GyFj> fjs = gyFjService.findByYwid(supplier.getId().toString());
				if (fjs.size() > 0) {
					supplier.setHaveAttachment(1);
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(supplier);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("profitStatistics", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
	}
	
	/**
	 * 新增供应商页面
	 * **/
	@RequestMapping(value="/addSupplier",method=RequestMethod.GET)
	public String addSupplier(HttpServletRequest request){
		request.setAttribute("currencyValue", "请选择");
		request.setAttribute("edit", 0);
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (roleVo.getRoleName().equals("国内采购")||roleVo.getRoleName().equals("国外采购")) {
			UserVo userVo=userService.findById(Integer.parseInt(userId));
			request.setAttribute("userId", userId);
			request.setAttribute("userName", userVo.getUserName());
		}
		return "/system/suppliermanage/addsupplier";
	}
	
	/**
	 * 保存新增数据
	 */
	@RequestMapping(value="/save",  method=RequestMethod.POST)
	public @ResponseBody ResultVo save(HttpServletRequest request, @ModelAttribute Supplier supplier)
	{
		boolean result = true;
		String message = "新增成功！";
		UserVo userVo = getCurrentUser(request);
		RoleVo roleVo = userService.getPower(supplier.getOwner());
		List<UserVo> list = userService.getByRole(roleVo.getRoleName());
		Supplier data=supplierService.findByCode(supplier.getCode());
		if(null==data){
			supplierService.insertSelective(supplier,list);
		}else{
			result = false;
			message = "新增失败！代码已存在";
		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 修改供应商页面
	 * **/
	@RequestMapping(value="/editSupplier",method=RequestMethod.GET)
	public String editSupplier(HttpServletRequest request){
		String id=request.getParameter("id");
		Supplier supplier=supplierService.selectByPrimaryKey(Integer.parseInt(id));
		Supplier data=supplierService.findByCode(supplier.getCode());
		
		request.setAttribute("data", data);
		request.setAttribute("supplier", supplier);
		request.setAttribute("edit", 1);
		return "/system/suppliermanage/addsupplier";
	}
	
	/**
	 * 保存修改数据
	 */
	@RequestMapping(value="/update",  method=RequestMethod.POST)
	public @ResponseBody ResultVo update(HttpServletRequest request, @ModelAttribute Supplier supplier)
	{
		boolean result = true;
		String message = "修改完成！";
		supplierService.updateByPrimaryKeySelective(supplier);
		return new ResultVo(result, message);
	}
	
	
	/**
	 * 供应商联系人信息管理页面
	 * **/
	@RequestMapping(value="/supplierContact",method=RequestMethod.GET)
	public String supplierContact(HttpServletRequest request){
		request.setAttribute("supplierId", request.getParameter("id"));
		return "/system/suppliermanage/contactlist";
	}
	
	/**
	 * 供应商联系人信息
	 * **/
	@RequestMapping(value="/contactlistData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo contactlistData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<SupplierContact> page = getPage(request);
		String supplierId=request.getParameter("supplierId");
		page.put("supplierId", supplierId);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		supplierContactService.findSupplierContactPage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierContact supplierContact : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierContact);
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
	 * 供应商联系人信息新增页面
	 * **/
	@RequestMapping(value="/toAddSupplierContact",method=RequestMethod.GET)
	public String toAddSupplierContact(HttpServletRequest request){
		request.setAttribute("supplierId", request.getParameter("supplierId"));
		return "/system/suppliermanage/addcontact";
	}
	
	/**
	 * 保存新增数据
	 */
	@RequestMapping(value="/addSupplierContact",  method=RequestMethod.POST)
	public @ResponseBody ResultVo addSupplierContact(HttpServletRequest request, @ModelAttribute SupplierContact supplierContact)
	{
		boolean result = true;
		String message = "新增完成！";
		supplierContactService.insertSelective(supplierContact);
		return new ResultVo(result, message);
	}
	
	/**
	 * 供应商联系人信息修改页面
	 * **/
	@RequestMapping(value="/toEditSupplierContact",method=RequestMethod.GET)
	public String toEditSupplierContact(HttpServletRequest request){
		request.setAttribute("id", request.getParameter("id"));
		SupplierContact supplierContact=supplierContactService.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
		request.setAttribute("supplierId", supplierContact.getSupplierId());
		request.setAttribute("data", supplierContact);
		return "/system/suppliermanage/addcontact";
	}
	
	/**
	 * 保存修改数据
	 */
	@RequestMapping(value="/updateSupplierContact",  method=RequestMethod.POST)
	public @ResponseBody ResultVo updateSupplierContact(HttpServletRequest request, @ModelAttribute SupplierContact supplierContact)
	{
		boolean result = true;
		String message = "修改完成！";
		if(null!=supplierContact.getSexValue()&&null==supplierContact.getSexId()){
			supplierContact.setSexId(Integer.parseInt(supplierContact.getSexValue()));
		}
		supplierContactService.updateByPrimaryKeySelective(supplierContact);
		return new ResultVo(result, message);
	}
	
	/**
	 * 供应商机型管理页面
	 * **/
	@RequestMapping(value="/supplierAir",method=RequestMethod.GET)
	public String supplierAir(HttpServletRequest request){
		request.setAttribute("supplierId", request.getParameter("id"));
		return "/system/suppliermanage/airlist";
	}
	
	/**
	 * 供应商机型
	 * **/
	@RequestMapping(value="/airlistData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo airlistData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<SupplierAirRelationKey> page = getPage(request);
		String supplierId=request.getParameter("supplierId");
		page.put("supplierId", supplierId);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		supplierAirRelationService.selectBySupplierIdPage(page);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierAirRelationKey supplierAirRelationKey : page.getEntities()) {
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
	
	/**
	 * 供应商机型新增页面
	 * **/
	@RequestMapping(value="/toAddSupplierAir",method=RequestMethod.GET)
	public String toAddSupplierAir(HttpServletRequest request){
		request.setAttribute("supplierId", request.getParameter("supplierId"));
		return "/system/suppliermanage/addair";
	}
	
	
	/**
	 * 保存新增数据
	 */
	@RequestMapping(value="/addSupplierAir",  method=RequestMethod.POST)
	public @ResponseBody ResultVo addSupplierAir(HttpServletRequest request, @ModelAttribute SupplierAirRelationKey supplierAirRelationKey)
	{
		boolean result = true;
		String message = "新增完成！";
		String id=supplierAirRelationKey.getIds();
		String[] airId=id.split(",");
		for (int i = 0; i < airId.length; i++) {
			supplierAirRelationKey.setAirId(Integer.parseInt(airId[i]));
			SupplierAirRelationKey data=supplierAirRelationService.selectBySupplierIdAndAirId(supplierAirRelationKey);
			if(null==data){
				supplierAirRelationService.insertSelective(supplierAirRelationKey);
			}
		}
		
		return new ResultVo(result, message);
	}
	

	/**
	 * 删除数据
	 */
	@RequestMapping(value="/deleteSupplierAir",  method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteSupplierAir(HttpServletRequest request, @ModelAttribute SupplierAirRelationKey supplierAirRelationKey)
	{
		boolean result = true;
		String message = "删除完成！";
		supplierAirRelationService.deleteByPrimaryKey(supplierAirRelationKey);
		return new ResultVo(result, message);
	}
	
	/**
	 * 供应商状态
	 */
	@RequestMapping(value="/statusList",method=RequestMethod.POST)
	public @ResponseBody ResultVo statusList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String statusid=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findSupplierByType("SUPPLIER_STATUS_ID");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		
		for (int i = 0; i < list.size(); i++) {
			SystemCode systemCode=list.get(i);
			Integer id=systemCode.getId();
//			String value=clientQuoteVo.getCurrency_value();
			if(statusid!=null&&id!=null){
				if(statusid.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					if(arraylist.size()>1){
						arraylist.remove(i);
					}
					
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==statusid||"".equals(statusid)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 币种
	 */
	@RequestMapping(value="/currencyList",method=RequestMethod.POST)
	public @ResponseBody ResultVo currencyList(HttpServletRequest request) {
		String msg = "";
		String id=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findType("CURRENCY");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		if (id != null && !"".equals(id)) {
			for (SystemCode systemCode : list) {
				if (systemCode.getId().toString().equals(id)) {
					arraylist.add(systemCode);
					break;
				}
			}
			for (SystemCode systemCode : list) {
				if (!systemCode.getId().toString().equals(id)) {
					arraylist.add(systemCode);
				}
			}
		}else {
			arraylist = list;
		}
		JSONArray json = new JSONArray();
		json.add(arraylist);
		msg =json.toString();
		return new ResultVo(true, msg);
	}
	
	/**
	 * 供应商归类
	 */
	@RequestMapping(value="/classifyList",method=RequestMethod.POST)
	public @ResponseBody ResultVo classifyList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String supplierId=request.getParameter("id");
		List<SupplierClassify> list=new ArrayList<SupplierClassify>();
		List<SystemCode> codes=systemCodeService.findSupplierByType("SUPPLIER_CLASSIFY_ID");
		if(!"".equals(supplierId)&&null!=supplierId){
			list=supplierClassifyService.selectByPrimaryKey(Integer.parseInt(supplierId));
			
			for (SupplierClassify supplierClassify : list) {
				for (SystemCode systemCode : codes) {
						if(supplierClassify.getSupplierClassifyId().equals(systemCode.getId().toString())){
							systemCode.setCheck("checked");
						}
				}
			}
			 
		}
		
//		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		
//		for (int i = 0; i < list.size(); i++) {
//			SystemCode systemCode=list.get(i);
//			Integer id=systemCode.getId();
//			String value=clientQuoteVo.getCurrency_value();
//			if(classifyid!=null&&id!=null){
//				if(classifyid.equals(id+"")){
//					
//					for (int j = 0; j < list.size(); j++) {
//						arraylist.add(list.get(j));
//					}
//					arraylist.remove(i);
//				}
//				}				
//		}
		success = true;
		JSONArray json = new JSONArray();
//		if(null==classifyid||"".equals(classifyid)){
			json.add(codes);
//		}else{
//			json.add(arraylist);
//		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 供应商是否可退税
	 */
	@RequestMapping(value="/taxReimbursementList",method=RequestMethod.POST)
	public @ResponseBody ResultVo taxReimbursementList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String taxReimbursementid=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findSupplierByType("SUPPLIER_TAXREIMBURSEMENT_ID");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		
		for (int i = 0; i < list.size(); i++) {
			SystemCode systemCode=list.get(i);
			Integer id=systemCode.getId();
//			String value=clientQuoteVo.getCurrency_value();
			if(taxReimbursementid!=null&&id!=null){
				if(taxReimbursementid.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==taxReimbursementid||"".equals(taxReimbursementid)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 供应商等级
	 */
	@RequestMapping(value="/gradeList",method=RequestMethod.POST)
	public @ResponseBody ResultVo gradeList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String gradeid=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findSupplierByType("SUPPLIER_GRADE_ID");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		
		for (int i = 0; i < list.size(); i++) {
			SystemCode systemCode=list.get(i);
			Integer id=systemCode.getId();
//			String value=clientQuoteVo.getCurrency_value();
			if(gradeid!=null&&id!=null){
				if(gradeid.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==gradeid||"".equals(gradeid)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 供应商阶段
	 */
	@RequestMapping(value="/phasesList",method=RequestMethod.POST)
	public @ResponseBody ResultVo phasesList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String phasesid=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findSupplierByType("SUPPLIER_PHASES_ID");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		
		for (int i = 0; i < list.size(); i++) {
			SystemCode systemCode=list.get(i);
			Integer id=systemCode.getId();
//			String value=clientQuoteVo.getCurrency_value();
			if(phasesid!=null&&id!=null){
				if(phasesid.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==phasesid||"".equals(phasesid)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 代理
	 */
	@RequestMapping(value="/agentList",method=RequestMethod.POST)
	public @ResponseBody ResultVo agentList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String agentId=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findSupplierByType("IS_AGENT");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		if (agentId != null && !"".equals(agentId)) {
			for (SystemCode systemCode : list) {
				if (systemCode.getId().equals(new Integer(agentId))) {
					arraylist.add(systemCode);
				}
			}
			for (SystemCode systemCode : list) {
				if (!systemCode.getId().equals(new Integer(agentId))) {
					arraylist.add(systemCode);
				}
			}
			//java 8 特性
			/*arraylist = list.stream().filter(sys -> sys.getId().equals(new Integer(agentId))).collect(Collectors.toList());
			arraylist.addAll(list.stream().filter(sys -> !sys.getId().equals(new Integer(agentId))).collect(Collectors.toList()));*/
		}else {
			for (SystemCode systemCode : list) {
				if (systemCode.getValue().equals("否")) {
					arraylist.add(systemCode);
					break;
				}
			}
			for (SystemCode systemCode : list) {
				if (!systemCode.getValue().equals("是")) {
					arraylist.add(systemCode);
				}
			}
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(arraylist);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 代理
	 */
	@RequestMapping(value="/caacAbilityList",method=RequestMethod.POST)
	public @ResponseBody ResultVo caacAbilityList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String caacAbilityId=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findSupplierByType("HAS_CAAC_ABILITY");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		if (caacAbilityId != null && !"".equals(caacAbilityId)) {
			for (SystemCode systemCode : list) {
				if (systemCode.getId().equals(new Integer(caacAbilityId))) {
					arraylist.add(systemCode);
				}
			}
			for (SystemCode systemCode : list) {
				if (!systemCode.getId().equals(new Integer(caacAbilityId))) {
					arraylist.add(systemCode);
				}
			}
			//java 8 特性
			/*arraylist = list.stream().filter(sys -> sys.getId().equals(new Integer(agentId))).collect(Collectors.toList());
			arraylist.addAll(list.stream().filter(sys -> !sys.getId().equals(new Integer(agentId))).collect(Collectors.toList()));*/
		}else {
			arraylist.addAll(list);
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(arraylist);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 性别
	 */
	@RequestMapping(value="/sex",method=RequestMethod.POST)
	public @ResponseBody ResultVo sex(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String phasesid=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findSupplierByType("SEX_ID");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		
		for (int i = 0; i < list.size(); i++) {
			SystemCode systemCode=list.get(i);
			Integer id=systemCode.getId();
//			String value=clientQuoteVo.getCurrency_value();
			if(phasesid!=null&&id!=null){
				if(phasesid.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==phasesid||"".equals(phasesid)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 验证code
	 */
	@RequestMapping(value="/testCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo testCode(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String code = getString(request, "code");
		List<Integer> ids=supplierService.checkByCode(code);
		if (ids.size() > 0) {
			success = true;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 验证名称
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/testName",method=RequestMethod.POST)
	public @ResponseBody ResultVo testName(HttpServletRequest request) throws UnsupportedEncodingException {
		boolean success = false;
		String message = "";
		String name = getString(request, "name");
		name = new String(name.getBytes("iso8859-1"),"UTF-8");
		Supplier data=supplierService.findByName(name);
		if (data!=null) {
			success = true;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 验证简称
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/testShortName",method=RequestMethod.POST)
	public @ResponseBody ResultVo testShortName(HttpServletRequest request) throws UnsupportedEncodingException {
		boolean success = false;
		String message = "";
		String shortName = getString(request, "shortName");
		shortName = new String(shortName.getBytes("iso8859-1"),"UTF-8");
		Supplier data=supplierService.findByShortName(shortName);
		if (data!=null) {
			success = true;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 拥有者
	 */
	@RequestMapping(value="/Owners",method=RequestMethod.POST)
	public @ResponseBody ResultVo Owners(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<UserVo> list = new ArrayList<UserVo>();
//		Integer edit = new Integer(getString(request, "edit"));
//		if (edit.equals(1)) {
			String owner =getString(request, "id");
			String userid=request.getParameter("userId");
			if(null==owner||"".equals(owner)){
				owner=userid;
			}else if(null==userid||"".equals(userid)){
				userid=owner;
			}
			List<UserVo> arraylist=new ArrayList<UserVo>();
//			Supplier supplier = supplierService.selectByPrimaryKey(id);
			List<UserVo> list2 = userService.getOwner();
			for (int i = 0; i < list2.size(); i++) {
				UserVo userVo=list2.get(i);
				String userId=userVo.getUserId();
				if(userId!=null&&owner!=null){
					if(userId.equals(owner+"")){
						
						for (int j = 0; j < list2.size(); j++) {
							arraylist.add(list2.get(j));
						}
						arraylist.remove(i);
					}
				}
			}
			success = true;
			JSONArray json = new JSONArray();
			if(null==owner||"".equals(owner)||owner.equals("0")){
				json.add(list2);
			}else{
				json.add(arraylist);
			}
			msg =json.toString();
			return new ResultVo(success, msg);
	}
	
	/**
	 * 供应商--生产商页面
	 */
	@RequestMapping(value="/tofactoryList",method=RequestMethod.GET)
	public String tofactoryList(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		Supplier supplier = supplierService.selectByPrimaryKey(id);
		request.setAttribute("code", supplier.getCode());
		request.setAttribute("id", id);
		return "/system/suppliermanage/factoryList";
	}
	
	/**
	 * 供应商--生产商数据
	 */
	@RequestMapping(value="/factoryList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo factoryList(HttpServletRequest request) {
		PageModel<FactoryVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		Integer supplierId = new Integer(getString(request, "id"));
		page.put("supplierId", supplierId);
		
		supplierCageRelationService.listPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (FactoryVo factoryVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(factoryVo);
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
	 * 上传供应商--生产商关系
	 */
	@RequestMapping(value="/factoryExcel",method=RequestMethod.POST)
	public @ResponseBody String factoryExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		Integer id =new Integer(getString(request, "id"));
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = supplierCageRelationService.factoryExcel(multipartFile, id);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * 供应商列表
	 * **/
	@RequestMapping(value="/topartList",method=RequestMethod.GET)
	public String topartList(HttpServletRequest request){
		request.setAttribute("id", request.getParameter("id"));
		return "/system/suppliermanage/partList";
	}
	
	/**
	 * 校验网址
	 */
	@RequestMapping(value="/checkUrl",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkUrl(HttpServletRequest request) {
		boolean success=false;
		String message="";
		PageModel<Supplier> page = getPage(request);
		String url =getString(request, "url");
		if (url!=null && !"".equals(url)) {
			int index = url.lastIndexOf(".");
			String checkUrl = "";
			if (url.startsWith("www")) {
				checkUrl = url.substring(4, index+1);
			}else {
				checkUrl = url.substring(0, index+1);
			}
			List<Supplier> list = supplierService.Suppliers(page);
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getUrl()!=null && !"".equals(list.get(i))) {
						int check = list.get(i).getUrl().lastIndexOf(".");
						if (check>0) {
							String checkString = "";
							if (url.startsWith("www")) {
								checkString = list.get(i).getUrl().substring(4, check+1);
							}else {
								checkString = list.get(i).getUrl().substring(0, check+1);
							}
									
							if (checkString.equals(checkUrl)) {
								success = true;
								break;
							}
						}
					}
				}
			}
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 校验邮箱域名是否存在
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkEmail",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkEmail(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		PageModel<Supplier> page = getPage(request);
		String email = getString(request, "email");
		int index1 = email.indexOf("@");
		int index2 = email.lastIndexOf(".");
		String name = email.substring(index1, index2);
		List<Supplier> list = supplierService.Suppliers(page);
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getEmail()!=null) {
					int check1 = list.get(i).getEmail().indexOf("@");
					int check2 = list.get(i).getEmail().lastIndexOf(".");
					if (check2>0) {
						String checkString = list.get(i).getEmail().substring(check1,check2);
						if (checkString.equals(name)) {
							success = true;
							break;
						}
					}
				}
			}
		}
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 单条新增生成商
	 */
	@RequestMapping(value="/toAddManufactory",method=RequestMethod.GET)
	public String toAddManufactory(HttpServletRequest request){
		request.setAttribute("supplierId", getString(request, "supplierId"));
		return "/system/suppliermanage/addmanufactory";
	}
	
	/**
	 * 保存单条新增
	 */
	@RequestMapping(value="/addManufactory",method=RequestMethod.POST)
	public @ResponseBody ResultVo addManufactory(HttpServletRequest request){
		String message = "";
		boolean success = false;
		String cageCode = getString(request, "cageCode");
		String msn = getString(request, "msn");
		Integer supplierId = new Integer(getString(request, "supplierId"));
		if (!"".equals(msn)) {
			/*List<TManufactory> list = tManufactoryService.getMsnByCageCode(msn);
			if(list.size() > 0){
				supplierCageRelationService.add(msn, supplierId);
			}else{
				message="cagecode不存在";
				return new ResultVo(success, message);
			}*/
			supplierCageRelationService.add(msn, supplierId);
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 删除厂商
	 */
	@RequestMapping(value="/deleteManufactory",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteManufactory(HttpServletRequest request){
		String message = "";
		boolean success = false;
		String msn = getString(request, "msn");
		Integer supplierId = new Integer(getString(request, "supplierId"));
		if (!"".equals(msn)) {
			SupplierCageRelationKey supplierCageRelationKey=new SupplierCageRelationKey();
			supplierCageRelationKey.setMsn(msn);
			supplierCageRelationKey.setSupplierId(supplierId);
				supplierCageRelationService.deleteByPrimaryKey(supplierCageRelationKey);
			
			success = true;
			message = "删除成功！";
		}else {
			message = "删除失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 资质即将过期供应商
	 */
	/**
	@Author: Modify by white
	@DateTime: 2018/9/4 15:42
	@Description: 添加ResultVo 作为访问响应
	*/
	@RequestMapping(value="/checkSupplierAptitude",method = RequestMethod.POST)
	public @ResponseBody ResultVo checkSupplierAptitude() {
		try{
			supplierService.getOutTimeSupplier();
			return new ResultVo(true,"请求成功");
		}catch (Exception e){
			return new ResultVo(false,"请求失败");
		}
	}
	
	/**
	 * 删除供应商联系人
	 */
	@RequestMapping(value="/deleteSupplierContact",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteSupplierContact(HttpServletRequest request){
		String message = "";
		boolean success = false;
		Integer id = new Integer(getString(request, "id"));
		try {
			supplierContactService.deleteByPrimaryKey(id);
			success = true;
			message = "删除成功！";
		} catch (Exception e) {
			message = "删除失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	
	/**
	 * 供应商年度报价
	 */
	@RequestMapping(value="/toAnnualOffer",method=RequestMethod.GET)
	public String toAnnualOffer(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		return "/system/suppliermanage/annualoffer";
	}
	
	/**
	 * 供应商年度报价数据
	 */
	@RequestMapping(value="/annualOfferList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo annualOfferList(HttpServletRequest request) {
		PageModel<SupplierAnnualOffer> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		Integer supplierId = new Integer(getString(request, "id"));
		page.put("supplierId", supplierId);
		
		supplierAnnualOfferService.annualOfferListPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierAnnualOffer supplierAnnualOffer : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierAnnualOffer);
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
	 * 删除供应商年度报价
	 */
	@RequestMapping(value="/deleteSupplierAnnualOffer",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteSupplierAnnualOffer(HttpServletRequest request){
		String message = "";
		boolean success = false;
		Integer id = new Integer(getString(request, "id"));
		try {
			supplierAnnualOfferService.deleteByPrimaryKey(id);
			success = true;
			message = "删除成功！";
		} catch (Exception e) {
			message = "删除失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 *  excel上传能力清单
	 */
	@RequestMapping(value="/quoteUploadExcel",method=RequestMethod.POST)
	public @ResponseBody String quoteUploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		String id=request.getParameter("id");
		String supplierPnType=request.getParameter("supplierPnType");
		MessageVo messageVo = supplierAnnualOfferService.excelUpload(multipartFile,Integer.parseInt(id),supplierPnType);
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
		request.setAttribute("tableName", "supplier");
		return "/marketing/clientinquiry/fileUpload";
	}
	
	
	/**
	 * 设置接收邮件人
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/setEmailPerson",method = RequestMethod.POST)
	public @ResponseBody ResultVo setEmailPerson(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			SupplierContact supplierContact = supplierContactService.selectByPrimaryKey(id);
			supplierContactService.updateEmailPersonBySupplierId(supplierContact.getSupplierId());
			supplierContact.setEmailPerson(1);
			supplierContactService.updateByPrimaryKey(supplierContact);
			return new ResultVo(true, "设置成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "设置失败!");
		}
	}
	
	/**
	 * 获取供应商资质列表
	 * @return
	 */
	@RequestMapping(value="/getAptitudes",method=RequestMethod.POST)
	public @ResponseBody ResultVo getAptitudes(){
		try {
			List<SystemCode> ap = systemCodeService.findType("APTITUDE_CODE");
			JSONArray json = new JSONArray();
			json.add(ap);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
		}
	}
	
	/**
	 * 跳转供应商资质
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAptitude",method=RequestMethod.GET)
	public String toAptitude(HttpServletRequest request){
		request.setAttribute("supplierId", getString(request, "id"));
		return "/system/suppliermanage/aptitudelist";
	}
	
	/**
	 * 供应商资质列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/supplierAptitudeListData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierAptitudeListData(HttpServletRequest request,
			HttpServletResponse response){
		PageModel<SupplierAptitude> page = getPage(request);
		String supplierId=request.getParameter("supplierId");
		page.put("supplierId", supplierId);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		supplierAptitudeService.listPage(page, getSort(request), searchString);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierAptitude supplierAptitude : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierAptitude);
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
	 * 新增供应商资质
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAddAptitude",method=RequestMethod.GET)
	public String toAddAptitude(HttpServletRequest request){
		request.setAttribute("supplierId", getString(request, "supplierId"));
		return "/system/suppliermanage/addAptitude";
	}
	
	
	/**
	 * 保存新增资质
	 * @param request
	 * @param supplierAptitude
	 * @return
	 */
	@RequestMapping(value="/saveAddAptitude",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAddAptitude(HttpServletRequest request,@ModelAttribute SupplierAptitude supplierAptitude){
		try {
			Integer supplierId = new Integer(getString(request, "supplierId"));
			UserVo userVo = getCurrentUser(request);
			for (int i = 0; i < supplierAptitude.getList().size(); i++) {
				supplierAptitude.getList().get(i).setLastUpdateUser(new Integer(userVo.getUserId()));
				supplierAptitude.getList().get(i).setSupplierId(supplierId);
				supplierAptitudeService.insertSelective(supplierAptitude.getList().get(i));
			}
			return new ResultVo(true, "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
	}
	
	/**
	 * 删除供应商资质
	 * @param request
	 * @param supplierAptitude
	 * @return
	 */
	@RequestMapping(value="/deleteAptitude",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteAptitude(HttpServletRequest request,@ModelAttribute SupplierAptitude supplierAptitude){
		try {
			supplierAptitudeService.deleteByPrimaryKey(supplierAptitude.getId());
			return new ResultVo(true, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
	}
	
	
	/**
	 * 获取国家列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCountryList",method=RequestMethod.POST)
	public @ResponseBody ResultVo getCountryList(HttpServletRequest request){
		try {
			String id = getString(request, "id");
			List<Country> list = new ArrayList<Country>();
			List<Country> countryList = countryService.getList();
			if (id != null && !"".equals(id)) {
				Country country = countryService.selectByPrimaryKey(new Integer(id));
				list.add(country);
				for (Country cou : countryList) {
					if (!cou.getId().toString().equals(id)) {
						list.add(cou);
					}
				}
			}else {
				list = countryList;
			}
			JSONArray json = new JSONArray();
			json.add(list);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "获取数据失败！");
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
