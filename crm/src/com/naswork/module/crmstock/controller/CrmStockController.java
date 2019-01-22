package com.naswork.module.crmstock.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.CrmStock;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SystemCode;
import com.naswork.model.TManufactory;
import com.naswork.model.TPart;
import com.naswork.model.TPartUploadBackup;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.partsinformation.PartsInformationVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.CrmStockService;
import com.naswork.service.GyFjService;
import com.naswork.service.RoleService;
import com.naswork.service.StaticClientQuotePriceService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.TManufactoryService;
import com.naswork.service.TPartService;
import com.naswork.service.UserService;
import com.naswork.service.rfqstock.NsnCenterService;
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
@RequestMapping("/stock/search")
public class CrmStockController extends BaseController {

	@Resource
	private CrmStockService crmStockService;
	@Resource
	private NsnCenterService nsnCenterService;
	@Resource
	private TPartService tPartService;
	@Resource
	private TManufactoryService tManufactoryService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private UserService userService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private RoleService roleService;
	@Resource
	private StaticClientQuotePriceService staticClientQuotePriceService;
	@Resource
	private GyFjService gyFjService;
	
	
	@RequestMapping(value = "/listByCage", method = RequestMethod.GET)
	public String listByCage(HttpServletRequest request){
		request.setAttribute("cageCode", request.getParameter("cageCode"));
		return "/crmstock/listByCage";
	}

	@RequestMapping(value = "/listByNsnPart", method = RequestMethod.GET)
	public String listByNsnPart(HttpServletRequest request){
		UserVo userVo = getCurrentUser(request);
		List<Integer> roleId = roleService.getRoleIdByUserId(new Integer(userVo.getUserId()));
		String cols = "";
		String roleName = roleService.selectByRoleId(roleId.get(0)).getRoleName();
		int purchase = roleName.indexOf("采购");
		int market = roleName.indexOf("销售");
		if (market >= 0 && purchase < 0) {
			cols = "market";
		}else if (market < 0 && purchase >= 0) {
			cols = "purchase";
		}else if (market < 0 && purchase < 0) {
			cols = "other";
		}
		request.setAttribute("cageCode", request.getParameter("cageCode"));
		request.setAttribute("cols", cols);
		return "/crmstock/listByNsnPart";
	}
	
	@RequestMapping(value="/findStockPage", method=RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo findStockPage(HttpServletRequest request,
			HttpServletResponse response) {
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString=request.getParameter("searchString");
		UserVo userVo=getCurrentUser(request);
		StringBuffer searchBuffer = new StringBuffer();
		if (!"".equals(searchString) && searchString != null) {
			String[] search = searchString.split(" ");
			for (int i = 0; i < search.length; i++) {
				if (search[i].equals("regexp")) {
					if (search[i+1].indexOf("%") > 0) {
						search[i+1] = search[i+1].replaceAll("\\%","[0-9]");
					}else if (search[i+1].indexOf("#") > 0) {
						search[i+1] = search[i+1].replaceAll("\\#","[a-zA-Z]");
					}
					else if (search[i+1].indexOf("?") > 0) {
						search[i+1] = search[i+1].replaceAll("\\?","[a-zA-Z0-9]");
					}
					/*else if (search[i+1].indexOf("*") > 0) {
						tring regex = "[-_\\\~!@#\\\$%\\^&\\*\\.\\(\\)\\?\\/\\'\\{\\}<>±Ω\\=\\Ø\\#\\,]|[a-zA-Z0-9]";
						//search[i+1] = search[i+1].replaceAll("\\*",);
					}*/
				}
				searchBuffer.append(search[i]).append(" ");
			}
		
		}
		//String a = "\\\"";
		String cageCode=request.getParameter("cageCode");
		if(null!=cageCode&&!cageCode.equals("")){
			if(null==searchString){
			searchString="tm.CAGE_CODE like"+"'%"+cageCode+"%'";
			searchBuffer.append("tm.CAGE_CODE like '%").append(cageCode).append("%'");
			}
		}
		String partNumber =request.getParameter("partNum");
		String check =request.getParameter("check");
		if(null!=searchString||null!=partNumber){
			PageModel<CrmStock> page=getPage(request);
			page.put("partNumber", partNumber);
			page.put("check", check);
			crmStockService.findStockPage(page, searchBuffer.toString(), getSort(request));
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			for (CrmStock stock : page.getEntities()) {
				if (stock.getIsBlacklist().equals(0)) {
					stock.setIsBlacklistValue("否");
				}else if (stock.getIsBlacklist().equals(1)) {
					stock.setIsBlacklistValue("是");
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(stock);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("类型修改", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());
		}
		
		return jqgrid;
	}
	
	/**
	 * 一次性加载数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getDataOnce", method=RequestMethod.POST)
	public @ResponseBody
	ResultVo getDataOnce(HttpServletRequest request,
			HttpServletResponse response) {
		//JQGridMapVo jqgrid = new JQGridMapVo();
		
		String searchString=request.getParameter("searchString");
		String shelfLife = getString(request, "shelfLife");
		UserVo userVo=getCurrentUser(request);
		StringBuffer searchBuffer = new StringBuffer();
		if (!"".equals(searchString) && searchString != null) {
			String[] search = searchString.split(" ");
			for (int i = 0; i < search.length; i++) {
				if (search[i].equals("regexp")) {
					if (search[i+1].indexOf("%") > 0) {
						search[i+1] = search[i+1].replaceAll("\\%","[0-9]");
					}else if (search[i+1].indexOf("#") > 0) {
						search[i+1] = search[i+1].replaceAll("\\#","[a-zA-Z]");
					}
					else if (search[i+1].indexOf("?") > 0) {
						search[i+1] = search[i+1].replaceAll("\\?","[a-zA-Z0-9]");
					}
					/*else if (search[i+1].indexOf("*") > 0) {
						tring regex = "[-_\\\~!@#\\\$%\\^&\\*\\.\\(\\)\\?\\/\\'\\{\\}<>±Ω\\=\\Ø\\#\\,]|[a-zA-Z0-9]";
						//search[i+1] = search[i+1].replaceAll("\\*",);
					}*/
				}
				searchBuffer.append(search[i]).append(" ");
			}
		
		}
		//String a = "\\\"";
		String cageCode=request.getParameter("cageCode");
		if(null!=cageCode&&!cageCode.equals("")){
			if(null==searchString){
			searchString="tm.CAGE_CODE like"+"'%"+cageCode+"%'";
			searchBuffer.append("tm.CAGE_CODE like '%").append(cageCode).append("%'");
			}
		}
		if (shelfLife != null && !"".equals(shelfLife)) {
			if (searchBuffer.length() > 0) {
				if (shelfLife.equals("0")) {
					searchBuffer.append(" and (t.SHELF_LIFE is null or t.SHELF_LIFE = 0)");
				}else if (shelfLife.equals("1")) {
					searchBuffer.append(" and t.SHELF_LIFE > 0");
				}
			}else {
				if (shelfLife.equals("0")) {
					searchBuffer.append(" where t.SHELF_LIFE is null or t.SHELF_LIFE = 0");
				}else if (shelfLife.equals("1")) {
					searchBuffer.append(" where t.SHELF_LIFE > 0");
				}
			}
			
		}
		String partNumber =request.getParameter("partNum");
		String check =request.getParameter("check");
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if(null!=searchString||null!=partNumber){
			PageModel<CrmStock> page=getPage(request);
			page.put("partNumber", partNumber);
			page.put("check", check);
			crmStockService.getDataOnce(page, searchBuffer.toString(), getSort(request));
			/*jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());*/
			
			StringBuffer bsns = new StringBuffer();
			bsns.append("(");
			StringBuffer correlationMarks = new StringBuffer();
			correlationMarks.append("(");
			for (CrmStock crmStock : page.getEntities()) {
				bsns.append("'").append(crmStock.getBsn()).append("'").append(",");
				if (crmStock.getCorrelationMark() != null) {
					correlationMarks.append(crmStock.getCorrelationMark()).append(",");
				}
			}
			if (bsns.length() > 1 && correlationMarks.length() >1) {
				bsns.deleteCharAt(bsns.length()-1);
				correlationMarks.deleteCharAt(correlationMarks.length()-1);
				bsns.append(")");
				correlationMarks.append(")");
				page.put("bsns", bsns);
				page.put("correlationMarks", correlationMarks);
				List<CrmStock> stocks = tPartService.selectByMarkAndBsn(page);
				for (int i = 0; i < stocks.size(); i++) {
					page.getEntities().add(stocks.get(i));
				}
			}
			/*for (CrmStock stock : page.getEntities()) {
				List<CrmStock> list = tPartService.selectByMark(stock.getCorrelationMark());
				for (int j = 0; j < list.size(); j++) {
					stocks.add(list.get(j));
				}
			}*/
			for (CrmStock stock : page.getEntities()) {
				/*if (stock.getIsBlacklist().equals(0)) {
					stock.setIsBlacklistValue("否");
				}else if (stock.getIsBlacklist().equals(1)) {
					stock.setIsBlacklistValue("是");
				}
				List<GyFj> fjs = gyFjService.findByYwid(stock.getBsn());
				if (fjs.size() > 0) {
					stock.setHaveAttachment(1);
				}*/
				Map<String, Object> map = EntityUtil.entityToTableMap(stock);
				mapList.add(map);
			}
			//jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportModel ="[{\"name\":\"BSN\",\"width\":120,\"align\":0,\"property\":\"bsn\"},{\"name\":\"件号\",\"width\":120,\"align\":0,\"property\":\"partNum\"},{\"name\":\"描述\",\"width\":180,\"align\":0,\"property\":\"partName\"},"
								 +"{\"name\":\"黑\",\"width\":40,\"align\":0,\"property\":\"isBlacklistValue\"},{\"name\":\"MSN\",\"width\":80,\"align\":0,\"property\":\"msn\"},"
								 +"{\"name\":\"厂商\",\"width\":150,\"align\":0,\"property\":\"manName\"},{\"name\":\"类型\",\"width\":40,\"align\":0,\"property\":\"code\"},{\"name\":\"NSN编号\",\"width\":150,\"align\":0,\"property\":\"nsn\"},"
								 +"{\"name\":\"有效期\",\"width\":80,\"align\":0,\"property\":\"shelfLife\"},{\"name\":\"ata chapter section\",\"width\":80,\"align\":0,\"property\":\"ataChapterSection\"},"
								 +"{\"name\":\"weight\",\"width\":80,\"align\":0,\"property\":\"weight\"},{\"name\":\"dimentions\",\"width\":80,\"align\":0,\"property\":\"dimentions\"},"
								 +"{\"name\":\"country of origin\",\"width\":80,\"align\":0,\"property\":\"countryOfOrigin\"},{\"name\":\"eccn\",\"width\":80,\"align\":0,\"property\":\"eccn\"},"
								 +"{\"name\":\"scheduleBCode\",\"width\":80,\"align\":0,\"property\":\"scheduleBCode\"},{\"name\":\"category no\",\"width\":80,\"align\":0,\"property\":\"categoryNo\"},"
								 +"{\"name\":\"usml\",\"width\":80,\"align\":0,\"property\":\"usml\"},{\"name\":\"hazmat code\",\"width\":80,\"align\":0,\"property\":\"hazmatCode\"},{\"name\":\"HS Code\",\"width\":80,\"align\":0,\"property\":\"hsCode\"},"
								 +"{\"name\":\"remark\",\"width\":80,\"align\":0,\"property\":\"remark\"}]";
					exportService.exportGridToXls("类型修改", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		}else{
			/*jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());*/
			
		}
		JSONArray json = new JSONArray();
		json.add(mapList);
		return new ResultVo(true, json.toString());
	}

	@RequestMapping(value="/findCagePage", method=RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo findCagePage(HttpServletRequest request,
			HttpServletResponse response) {
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString=request.getParameter("searchString");
		String cageCode=request.getParameter("cageCode");
		if(null!=cageCode&&!cageCode.equals("")){
			if(null==searchString){
			searchString="tm.CAGE_CODE like"+"'%"+cageCode+"%'";
			}
		}
			PageModel<CrmStock> page=getPage(request);
			if(null!=searchString){
			crmStockService.findCagePage(page, searchString, getSort(request));
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			for (CrmStock stock : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(stock);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			}else{
				jqgrid.setRecords(0);
				jqgrid.setTotal(0);
				jqgrid.setRows(new ArrayList<Map<String, Object>>());
			}
		
		return jqgrid;
	}
	
	/*
	 * 新增
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd(HttpServletRequest request) {
		return "/crmstock/addpart";
	}
	
	/*
	 * 保存新增
	 */
	@RequestMapping(value="/saveAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute TPart tPart) {
		String message = "";
		boolean success = false;
		if (tPart.getPartNum()!=null) {
			tPartService.insertSelective(tPart);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 修改
	 */
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit(HttpServletRequest request) {
		String bsn = getString(request, "bsn");
		TPart tPart = tPartService.selectByPrimaryKey(bsn);
		request.setAttribute("tPart", tPart);
		return "/crmstock/editpart";
	}
	
	/*
	 * 保存修改
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute TPart tPart) {
		String message = "";
		boolean success = false;
		if (tPart.getBsn()!=null) {
			tPart.setMsn(tPart.getMsnFlag()+"-"+tPart.getCageCode());
			tPartService.updateByPrimaryKeySelective(tPart);
			message = "修改成功！";
			success = true;
		}else {
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * t_part excel上传
	 */
	@RequestMapping(value="/tpUploadExcel",method=RequestMethod.POST)
	public @ResponseBody String tpUploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = tPartService.excelUpload(multipartFile,new Integer(userVo.getUserId()));
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/*
	 * 错误列表
	 */
	@RequestMapping(value="/toUnknow",method=RequestMethod.GET)
	public String toUnknow() {
		return "/crmstock/errorlist";
	}
	
	/*
	 * 错误列表数据
	 */
	@RequestMapping(value="/Unknow",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo Unknow(HttpServletRequest request) {
		PageModel<TPartUploadBackup> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		page.put("userId", userVo.getUserId());
		
		tPartService.listPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (TPartUploadBackup tPartUploadBackup : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(tPartUploadBackup);
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
	 * 删除数据
	 */
	@RequestMapping(value="/deleteData",method=RequestMethod.POST)
	public @ResponseBody ResultVo delete(HttpServletRequest request) {
		boolean success=true;
		String message="";
		UserVo userVo=getCurrentUser(request);
		tPartService.delete(new Integer(userVo.getUserId()));
		return new ResultVo(success, message);
	}
	
	/*	
	 * 新增cagecode
	 */
	@RequestMapping(value="/toUpdateCageCode",method=RequestMethod.GET)
	public String toAddCageCode(HttpServletRequest request) {
		String msn=request.getParameter("msn");
		if (msn != null && !"".equals(msn)) {
			TManufactory tManufactory=tManufactoryService.selectByPrimaryKey(msn);
			String msnFlag=tManufactory.getMsn().split("-")[0];
			tManufactory.setMsnFlag(Integer.parseInt(msnFlag));
			request.setAttribute("tManufactory", tManufactory);
		}
		return "/crmstock/updateCageCode";
	}
	
	/*
	 * 保存新增
	 */
	@RequestMapping(value="/saveAddCageCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAddCageCode(HttpServletRequest request,@ModelAttribute TManufactory tManufactory) {
		String message = "";
		boolean success = false;
		if(null!=tManufactory.getCageCode()){
		String msn=tManufactory.getMsnFlag().toString().trim()+"-"+tManufactory.getCageCode();
		tManufactory.setMsn(msn);
		List<TManufactory> data=tManufactoryService.selectByMsn(msn);
		if(data.size() == 0){	
			tManufactoryService.insertSelective(tManufactory);
			message = "新增成功！";
			success = true;
		}else{
			message = "该记录已存在！";
		}
		
		}else{
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 保存修改
	 */
	@RequestMapping(value="/editCageCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo editCageCode(HttpServletRequest request,@ModelAttribute TManufactory tManufactory) {
		String message = "";
		boolean success = false;
		if(null!=tManufactory.getManName()){
			tManufactoryService.updateByPrimaryKeySelective(tManufactory);
			message = "修改成功！";
			success = true;
		}else{
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 查询cagecode
	 */
	@RequestMapping(value="/findCageCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo findCageCode(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		String cageCode=request.getParameter("code");
		List<TManufactory> tManufactory=tManufactoryService.selectByCageCode(cageCode);
		if(tManufactory.size() > 0){
			 success = true;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			SupplierQuote record) {
		boolean success=false;
		String message="";
		MessageVo messageVo =new MessageVo();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		try {
			 messageVo = tManufactoryService.uploadExcel(multipartFile);
		} catch (Exception e) {
			e.printStackTrace();
			messageVo.setFlag(success);
			messageVo.setMessage(e.getMessage());
		}
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	'件号类型
	 */
	@RequestMapping(value="/findparttype",method=RequestMethod.POST)
	public @ResponseBody ResultVo findcond(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String pid=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findType("PART_TYPE");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		
		for (int i = 0; i < list.size(); i++) {
			SystemCode dateVo=list.get(i);
			Integer id=dateVo.getId();
			if(pid!=null&&id!=null){
				if(pid.equals(id+"")){
					arraylist.add(list.get(i));
					for (int j = 0; j < list.size(); j++) {
						if(i!=j){
						arraylist.add(list.get(j));
					}
					}
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==pid||"".equals(pid)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 匹配页面新增商品库件号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addPartByMatch",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo addPartByMatch(HttpServletRequest request) {
		Boolean success = false;
		String message = "";
		try {
			Integer id = new Integer(getString(request, "id"));
			String msn = getString(request, "msn");
			List<TManufactory> tManufactories = tManufactoryService.selectByMsn(msn);
			if (tManufactories.size() > 0) {
				tPartService.add(id, msn);
				message = "新增成功！";
				success = true;
			}else {
				message = "MSN不存在！";
				success = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "新增失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * excel上传
	 */
	@RequestMapping(value="/updateType",method=RequestMethod.POST)
	public @ResponseBody String updateType(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			ClientInquiryElement clientInquiryElement) {
		boolean success=false;
		String message="";
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = tPartService.updateType(multipartFile);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * 根据bsn查询部件资料销售
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/systeminformationformarket",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo systeminformationformarket(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartsInformationVo> page1=getPage(request);
		PageModel<PartsInformationVo> page2=getPage(request);
		PageModel<PartsInformationVo> page3=getPage(request);
		PageModel<PartsInformationVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String bsn = getString(request, "bsn");
		String where = "cie.bsn = "+ "'"+bsn+"'";
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page1.put("userId", userVo.getUserId());
			page2.put("userId", userVo.getUserId());
			page3.put("userId", userVo.getUserId());
		}
		clientInquiryElementService.marketpartPage(page1, where, sort);
//		clientInquiryElementService.marketpartCoeNullPage(page2, where, sort);
//		clientInquiryElementService.marketpartAllNullPage(page3, where, sort);
		//拼装列表数据
		/*if (page1.getPageSize()>=page1.getPageCount()) {
			//有订单的数据但是不够20条的情况
			if (page1.getPageNo()==page1.getPageCount() && page1.getEntities().size()<20) {
				for (int i = 0; i < 20-page1.getEntities().size(); i++) {
					page1.getEntities().add(page2.getEntities().get(i));
				}
			//页数超过了有订单的数据以后拼接上有报价的数据
			}else if (page1.getPageNo()<page2.getPageCount()) {
				for (int i = page1.getPageNo()*20; i < wheres.length; i++) {
					
				}
			}
		}*/
		if (page1.getRecordCount() == 0) {
			page1.setPageCount(0);
		}
		if (page2.getRecordCount() == 0) {
			page2.setPageCount(0);
		}
		if (page3.getRecordCount() == 0) {
			page3.setPageCount(0);
		}
		if (page1.getPageNo()==0) {
			page1.setPageNo(1);
		}
		
		if ((page1.getPageNo()>page1.getPageCount() && page1.getPageNo()<=page2.getPageCount()+page1.getPageCount() && page2.getRecordCount() > 0) ) {
				//|| (page1.getRecordCount() == 0 && page2.getRecordCount() > 0)) {
			page2.setPageNo(page1.getPageNo()-page1.getPageCount());
			clientInquiryElementService.marketpartCoeNullPage(page2, where, sort);
				page.setEntities(page2.getEntities());
		}else if ((page1.getPageNo()>page2.getPageCount()+page1.getPageCount() && page3.getRecordCount() > 0)
				|| (page1.getRecordCount() == 0 && page2.getRecordCount() == 0 && page3.getRecordCount() > 0)) {
			page3.setPageNo(page1.getPageNo()-page1.getPageCount()-page2.getPageCount());
			clientInquiryElementService.marketpartAllNullPage(page3, where, sort);
				page.setEntities(page3.getEntities());
		}else if (page1.getRecordCount() > 0){
			page.setEntities(page1.getEntities());
		}
		
		page.setRecordCount(page1.getRecordCount()+page2.getRecordCount()+page3.getRecordCount());
		page.setPageCount(page1.getPageCount()+page2.getPageCount()+page3.getPageCount());
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartsInformationVo partsInformationVo : page.getEntities()) {
				int count = staticClientQuotePriceService.findByClientId(partsInformationVo.getClientId());
				if (count != 0) {
					partsInformationVo.setSupplierBasePrice(null);
				}
				List<Integer> checkList = new ArrayList<Integer>();
				if (partsInformationVo.getMainId()!=null && !checkList.contains(partsInformationVo.getMainId())) {
					checkList.add(partsInformationVo.getMainId());
					PageModel<PartsInformationVo> pageModel = new PageModel<PartsInformationVo>();
					pageModel.put("where", "cie.id = "+partsInformationVo.getMainId());
					List<PartsInformationVo> partList = clientInquiryElementService.marketPartInformation(pageModel);
					for (PartsInformationVo partsInformationVo2 : partList) {
						Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo2);
						if (!mapList.contains(map)) {
							mapList.add(map);
						}
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
				if (!mapList.contains(map)) {
					mapList.add(map);
				}
			}
			jqgrid.setRows(mapList);
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
	 * 根据bsn查询部件资料采购
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/systeminformationforpurchase",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo systeminformationforpurchase(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartsInformationVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String bsn = getString(request, "bsn");
		String where = "cie.bsn = "+ "'"+bsn+"'";
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		clientInquiryElementService.purchasepartPage(page, where, sort);
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartsInformationVo partsInformationVo : page.getEntities()) {
				List<Integer> checkList = new ArrayList<Integer>();
				if (partsInformationVo.getMainId()!=null && !checkList.contains(partsInformationVo.getMainId())) { 
					checkList.add(partsInformationVo.getMainId());
					PageModel<PartsInformationVo> pageModel = new PageModel<PartsInformationVo>();
					pageModel.put("where", "cie.id = "+partsInformationVo.getMainId());
					List<PartsInformationVo> partList = clientInquiryElementService.marketPartInformation(pageModel);
					for (PartsInformationVo partsInformationVo2 : partList) {
						Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo2);
						mapList.add(map);
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
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
	 * 重量单位
	 */
	@RequestMapping(value="/findWeightUnit",method=RequestMethod.POST)
	public @ResponseBody ResultVo findWeightUnit(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String id=request.getParameter("id");
		List<SystemCode> list=tPartService.getSystemByType("WEIGHT");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		if (id != null && !"".equals(id)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(new Integer(id))) {
					arraylist.add(list.get(i));
					break;
				}				
				
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().equals(new Integer(id))) {
					arraylist.add(list.get(i));
				}				
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
	
	/**
	 * 尺寸单位
	 */
	@RequestMapping(value="/findDimentionsUnit",method=RequestMethod.POST)
	public @ResponseBody ResultVo findDimentionsUnit(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String id=request.getParameter("id");
		List<SystemCode> list=tPartService.getSystemByType("DIMENTIONS");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		if (id != null && !"".equals(id)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(new Integer(id))) {
					arraylist.add(list.get(i));
					break;
				}				
				
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().equals(new Integer(id))) {
					arraylist.add(list.get(i));
				}				
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
	 * 文件管理
	 */
	@RequestMapping(value="/file",method=RequestMethod.GET)
	public String file(HttpServletRequest request) {
		String bsn = getString(request, "bsn");
		request.setAttribute("id", bsn);
		request.setAttribute("tableName", "t_part");
		return "/marketing/clientinquiry/fileUpload";
	}

}
