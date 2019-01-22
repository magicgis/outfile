package com.naswork.module.storage.controller.storagedetail;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

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

import com.naswork.common.controller.BaseController;
import com.naswork.filter.ContextHolder;
import com.naswork.model.CheckStorageByLocation;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.ExportPackage;
import com.naswork.model.ExportPackageElement;
import com.naswork.model.ExportPackageInstructions;
import com.naswork.model.ExportPackageInstructionsElement;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.StorageCorrelation;
import com.naswork.model.SystemCode;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.service.CheckStorageByLocationService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.ExportPackageInstructionsElementService;
import com.naswork.service.ExportPackageInstructionsService;
import com.naswork.service.ImportStorageLocationListService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.RoleService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;
import com.sun.star.i18n.reservedWords;

@Controller
@RequestMapping(value="/storage/detail")
public class StorageDetailController extends BaseController{

	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private ExportPackageInstructionsService exportPackageInstructionsService;
	@Resource
	private ExportPackageInstructionsElementService exportPackageInstructionsElementService;
	@Resource
	private UserService userService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ImportStorageLocationListService importStorageLocationListService;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private RoleService roleService;
	@Resource
	private CheckStorageByLocationService checkStorageByLocationService;
	@Resource
	private ExportPackageElementService exportPackageElementService;
	
	
	/**
	 * 库存明细
	 */
	@RequestMapping(value="/toStorageDetail",method=RequestMethod.GET)
	public String toStorageDetail(HttpServletRequest request) {
		StorageDetailVo storageDetailVo = importpackageElementService.getCountAndPrice();
		UserVo userVo=ContextHolder.getCurrentUser();
		RoleVo roleVo=userService.getPower(Integer.parseInt(userVo.getUserId()));
		request.setAttribute("role", roleVo.getRoleName());
		request.setAttribute("storageDetailVo", storageDetailVo);
		return "/storage/storagedetail/storagedetailList";
	}
	
	
	/**
	 * 库存列表
	 */
	@RequestMapping(value="/StorageDetail",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo StorageDetail(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StorageDetailVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		Double countWeight=0.0;
		Double totalPrice = 0.0;
		Double totalAmount = 0.0;
		Double clientOrderPrice = 0.0;
		String exportpackage=request.getParameter("exportpackage");
		if(!"".equals(exportpackage)){
			page.put("exportpackage", exportpackage);
		}
		if(null!=where&&!where.equals("")){
			ImportPackageElement element=new ImportPackageElement();
			if(null!=exportpackage&&!"".equals(exportpackage)){
				if(exportpackage.equals("0")){
					where=where+" and (a.location not like 'TS%' AND a.location not like '%地面%' AND not a.location like '%退税%')";
				}else{
				where=where+" and (a.location like 'TS%' OR a.location like '%地面%' OR a.location like '%退税%')";
				}
			}
			element.setParame(where);
			StorageDetailVo storageDetailVo=importpackageElementService.BoxWeight(element);
			countWeight = storageDetailVo.getBoxWeight();
			totalAmount = storageDetailVo.getStorageAmount();
			totalPrice = storageDetailVo.getTotalBasePrice();
			clientOrderPrice=storageDetailVo.getClientOrderPrice();
		}
		String correlation = getString(request, "correlation");
		String importId = getString(request, "importId");
		if (correlation != null && !"".equals(correlation)) {
			if (where != null && !"".equals(where)) {
				where = where + " and (a.client_code = '197' or a.client_code = '199') and a.id != "+importId;
			}else {
				where = "a.client_code = '197' or a.client_code = '199' and a.id != "+importId;
			}
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		page.put("epiId", request.getParameter("epiId"));
		importpackageElementService.StoragePage(page, where, sort);
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			 DecimalFormat df = new DecimalFormat("#.00"); 
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StorageDetailVo storageDetailVo : page.getEntities()) {
				if(null!=countWeight&&countWeight!=0){
					storageDetailVo.setCountWeight(Double.parseDouble(df.format(countWeight/1000)));
				}
				Double lockAmount = importpackageElementService.getLockStorageAmount(storageDetailVo.getSupplierQuoteElementId(), storageDetailVo.getId());
				storageDetailVo.setLockAmount(lockAmount);
				storageDetailVo.setTotalAmount(totalAmount);
				storageDetailVo.setTotalPrice(totalPrice);
				storageDetailVo.setClientOrderPrice(clientOrderPrice);
				Map<String, Object> map = EntityUtil.entityToTableMap(storageDetailVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("storagedetail", exportModel, mapList, response);
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
	 * 不在出库指令里面的件
	 */
	@RequestMapping(value="/notInInstructions",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo notInInstructions(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StorageDetailVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String exportPackageInstructionsId = request.getParameter("id");
		page.put("exportPackageInstructionsId", exportPackageInstructionsId);
		importpackageElementService.getNotInInstructionsPage(page, sort);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			 DecimalFormat df = new DecimalFormat("#.00"); 
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StorageDetailVo storageDetailVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(storageDetailVo);
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
	 * 修改入库单明细
	 * **/
	@RequestMapping(value="/editimportpackageelementdate",  method=RequestMethod.POST)
	public @ResponseBody ResultVo editimportpackageelementdate(HttpServletRequest request, @ModelAttribute ImportPackageElement importPackageElement)
	{
		boolean result = true;
		String message = "";
		String importPackageElementId=request.getParameter("importPackageElementId");
		importPackageElement.setId(Integer.parseInt(importPackageElementId));
		importPackageElement.setCompleteComplianceCertificate(520);
		try {
			importpackageElementService.updateByPrimaryKeySelective(importPackageElement);
		} catch (Exception e) {
			 result = false;
		}
		
		
		return new ResultVo(result, message);
	}
	
	/**
	 * 新增出库指令
	 */
	@RequestMapping(value="/toEcportpackageInstructions",method=RequestMethod.GET)
	public String toEcportpackageInstructions(HttpServletRequest request) {
		request.setAttribute("createDate", new Date());
		request .setAttribute("clientCode", request.getParameter("clientCode"));
		request .setAttribute("tax", request.getParameter("tax"));
		request.setAttribute("supplierCode", request.getParameter("supplierCode"));
		request.setAttribute("partNumber", request.getParameter("partNumber"));
		request.setAttribute("location", request.getParameter("location"));
		request.setAttribute("importDateStart", request.getParameter("importDateStart"));
		request.setAttribute("importDateEnd", request.getParameter("importDateEnd"));
		request.setAttribute("complianceCertificate", request.getParameter("complianceCertificate"));
		request.setAttribute("completeComplianceCertificate", request.getParameter("completeComplianceCertificate"));
		request.setAttribute("exportpackage", request.getParameter("exportpackage"));
		return "/storage/storagedetail/addEcportpackageInstructions";
	}
	
	/**
	 * 新增出库指令(选择)
	 */
	@RequestMapping(value="/toEcportpackageInstructionsWithSelect",method=RequestMethod.GET)
	public String toEcportpackageInstructionsWithSelect(HttpServletRequest request) {
		request.setAttribute("createDate", new Date());
		request .setAttribute("clientCode", request.getParameter("clientCode"));
		request .setAttribute("tax", request.getParameter("tax"));
		request.setAttribute("supplierCode", request.getParameter("supplierCode"));
		request.setAttribute("partNumber", request.getParameter("partNumber"));
		request.setAttribute("location", request.getParameter("location"));
		request.setAttribute("importDateStart", request.getParameter("importDateStart"));
		request.setAttribute("importDateEnd", request.getParameter("importDateEnd"));
		request.setAttribute("complianceCertificate", request.getParameter("complianceCertificate"));
		request.setAttribute("completeComplianceCertificate", request.getParameter("completeComplianceCertificate"));
		request.setAttribute("exportpackage", request.getParameter("exportpackage"));
		return "/storage/storagedetail/addEcportpackageInstructionsWithSelect";
	}
	
	/**
	 * 新增出库指令明细
	 * @throws ParseException 
	 * **/
	@RequestMapping(value="/addEcportpackageInstructions",  method=RequestMethod.POST)
	public @ResponseBody ResultVo addEcportpackageInstructions(HttpServletRequest request, @ModelAttribute StorageFlowVo vo) throws ParseException
	{
		boolean result = true;
		String message = "新增完成";
//		try {
		String ids = request.getParameter("ids");
		String exportPackageInstructionsIds = request.getParameter("exportPackageInstructionsIds");
		if (ids != null) {
			String[] importPackageElementIds = ids.split(",");
			
			ExportPackageInstructionsElement element=new ExportPackageInstructionsElement();
			if (exportPackageInstructionsIds != null) {
				String id = exportPackageInstructionsIds.split(",")[0];
				element.setExportPackageInstructionsId(new Integer(id));
			}else {
				ExportPackageInstructions record=new ExportPackageInstructions();
				String ecportpackageInstructionsNumber=request.getParameter("ecportpackageInstructionsNumber");
				String remark=request.getParameter("remark");
				record.setExportPackageInstructionsNumber(ecportpackageInstructionsNumber);
				record.setRemark(remark);
				record.setCode(vo.getClientCode());
				exportPackageInstructionsService.insert(record);
				element.setExportPackageInstructionsId(record.getId());
			}
			for (int i = 0; i < importPackageElementIds.length; i++) {
				vo.setImportPackageElementId(new Integer(importPackageElementIds[i]));
				List<StorageDetailVo> list=importpackageElementService.ecportpackageInstructionsData(vo);
				for (StorageDetailVo storageDetailVo : list) {
					element.setAmount(storageDetailVo.getStorageAmount());
					element.setImportPackageElementId(storageDetailVo.getId());
					exportPackageInstructionsElementService.insert(element);
				}
				
			}
			
		}else {
			vo.setImportDateStart(request.getParameter("importDateStart"));
			vo.setImportDateEnd(request.getParameter("importDateEnd"));
			if(null!=vo.getLocation()){
				try {
					vo.setLocation(new String(vo.getLocation().getBytes("iso8859-1"),"UTF-8"));	
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			List<StorageDetailVo> list=importpackageElementService.ecportpackageInstructionsData(vo);
			String ecportpackageInstructionsNumber=request.getParameter("ecportpackageInstructionsNumber");
			String remark=request.getParameter("remark");
			ExportPackageInstructions record=new ExportPackageInstructions();
			record.setExportPackageInstructionsNumber(ecportpackageInstructionsNumber);
			record.setRemark(remark);
			record.setCode(vo.getClientCode());
			exportPackageInstructionsService.insert(record);
			ExportPackageInstructionsElement element=new ExportPackageInstructionsElement();
			element.setExportPackageInstructionsId(record.getId());
			for (StorageDetailVo storageDetailVo : list) {
				 element.setAmount(storageDetailVo.getStorageAmount());
				 element.setImportPackageElementId(storageDetailVo.getId());
				 exportPackageInstructionsElementService.insert(element);
			}
		}
//		} catch (Exception e) {
//			 result = false;
//			 message = "新增失败";
//		}
		return new ResultVo(result, message);
	}
	
	/*
	 * 查询退税
	 */
	@RequestMapping(value="/tax",method=RequestMethod.POST)
	public @ResponseBody ResultVo bizType() {
		boolean success = false;
		String msg = "";
		List<SystemCode> list=systemCodeService.findType("SUPPLIER_TAXREIMBURSEMENT_ID");
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 *打印库位
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/locationiframes",method=RequestMethod.GET)
	public String locationiframes(HttpServletRequest request) throws UnsupportedEncodingException{
		
		String location=request.getParameter("location");
		String clientCode=request.getParameter("clientCode");
		Random rm = new Random();  
		double pross = (1 + rm.nextDouble()) * Math.pow(10, 5); 
		double pross2 = (1 + rm.nextDouble()) * Math.pow(10, 5); 
		double pross3 = (1 + rm.nextDouble()) * Math.pow(10, 5); 
		String fixLenthString = String.valueOf(pross);  
		String x=fixLenthString.substring(1, 5 + 1);  
		fixLenthString = String.valueOf(pross2);  
		String y=fixLenthString.substring(1, 5 + 1);  
		fixLenthString = String.valueOf(pross3);  
		String z=fixLenthString.substring(1, 5 + 1);  
		String clientOrderElementId=request.getParameter("clientOrderElementId");
		//第一个判断件号或箱子，locationid,elementid;        
	    ImportStorageLocationList  list=importStorageLocationListService.selectByLocation(location);
	    String sourceNumber="";
	    List<ImportStorageLocationList> importStorageLocationLists=importStorageLocationListService.selectSourceNumber(location);
	    if(null!=importStorageLocationLists&&importStorageLocationLists.size()>0){
		    for (ImportStorageLocationList importStorageLocationList : importStorageLocationLists) {
			    sourceNumber+=importStorageLocationList.getSourceNumber()+"<br>";
		    }
	    }else if(clientCode.trim().equals("13")||clientCode.trim().equals("20")){
		    if(null!=clientOrderElementId&&!"".equals(clientOrderElementId)&&!clientOrderElementId.trim().equals("undefined")){
			    ClientOrderElement coe=clientOrderElementService.selectByPrimaryKey(Integer.parseInt(clientOrderElementId.trim()));
			    if(null!=coe){
				    ClientOrder co=clientOrderService.selectByPrimaryKey(coe.getClientOrderId());
				    if(null!=co){
					    sourceNumber=co.getSourceNumber();
				    }
			    }
		    }
	    }
   		Integer locationid=list.getId();
     	String barCode="040"+x+y+z+locationid+"";
     	request.setAttribute("sourceNumber", sourceNumber);
     	request.setAttribute("location", location);
     	request.setAttribute("clientCode", clientCode);
		request.setAttribute("barCode",barCode);
		return "/purchase/importpackage/locationiframe";
	}
	
	/**
	 * 选择位置页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toChangeLocation",method=RequestMethod.GET)
	public String toChangeLocation(HttpServletRequest request){
		request .setAttribute("clientId", request.getParameter("clientId"));
		request .setAttribute("location", request.getParameter("location"));
		request .setAttribute("clientCode", request.getParameter("clientCode"));
		request .setAttribute("clientOrderElementId", request.getParameter("clientOrderElementId"));
		return "/storage/exportpackage/selectLocation";
	}
	
	
	/**
	 * 跳转库存关联页面
	 * @return
	 */
	@RequestMapping(value="/toStorageCorrelation",method=RequestMethod.GET)
	public String toStorageCorrelation(HttpServletRequest request){
		request.setAttribute("importPackageId", getString(request, "importPackageId"));
		request.setAttribute("partNumber", getString(request, "partNumber"));
		request.setAttribute("description", getString(request, "description"));
		/*try {
			request.setCharacterEncoding("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		return "/storage/storagedetail/addStorageCorrelation";
	}
	
	/**
	 * 绑定库存替换件
	 * @return
	 */
	@RequestMapping(value="/addStorageCorrelation",method=RequestMethod.POST)
	public @ResponseBody ResultVo addStorageCorrelation(HttpServletRequest request){
		try {
			Integer importId = new Integer(getString(request, "importPackageId"));
			String correlationId = getString(request, "correlationId");
			StorageCorrelation storageCorrelation = new StorageCorrelation();
			storageCorrelation.setImportPackageElementId(importId);
			storageCorrelation.setCorrelationImportPackageElementId(correlationId);
			boolean success = importpackageElementService.addStorageCorrelation(storageCorrelation);
			if (success) {
				return new ResultVo(true, "绑定成功！");
			}else {
				return new ResultVo(false, "绑定异常！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "绑定异常！");
		}
	}
	
	/**
	 * 跳转关系关系列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toCorrelationList",method=RequestMethod.GET)
	public String toCorrelationList(HttpServletRequest request){
		request.setAttribute("importPackageId", getString(request, "importPackageId"));
		request.setAttribute("partNumber", getString(request, "partNumber"));
		request.setAttribute("description", getString(request, "description"));
		/*try {
			request.setCharacterEncoding("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return "/storage/storagedetail/correlationList";
	}
	
	/**
	 * 关联列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/correlationList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo correlationList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StorageDetailVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String importId = request.getParameter("importId");
		page.put("importId", importId);
		page.put("importIdLike", "'%"+importId+"%'");
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		importpackageElementService.correlationPage(page);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			 DecimalFormat df = new DecimalFormat("#.00"); 
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StorageDetailVo storageDetailVo : page.getEntities()) {
				Double lockAmount = importpackageElementService.getLockStorageAmount(storageDetailVo.getSupplierQuoteElementId(), storageDetailVo.getId());
				storageDetailVo.setLockAmount(lockAmount);
				Map<String, Object> map = EntityUtil.entityToTableMap(storageDetailVo);
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
	 * 解除绑定
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteCorrelation",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteCorrelation(HttpServletRequest request){
		try {
			String id = getString(request, "ids");
			boolean success = importpackageElementService.deleteCorrelation(id);
			if (success) {
				return new ResultVo(true, "修改成功！");
			}else {
				return new ResultVo(false, "修改异常！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
	}
	
	/**
	 * 判断当前用户是否采购
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/isPurchase",method=RequestMethod.POST)
	public @ResponseBody ResultVo isPurchase(HttpServletRequest request){
		UserVo userVo = getCurrentUser(request);
		List<RoleVo> roleList = userService.searchRoleByUserId(userVo.getUserId());
		Boolean isPurchase = false;
		for (RoleVo roleVo : roleList) {
			if (roleVo.getRoleName().indexOf("采购") >= 0) {
				isPurchase = true;
				break;
			}
		}
		return new ResultVo(isPurchase,"");
		
	}
	
	
	/**
	 * 跳转位置内入库信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toPartInLoaction",method=RequestMethod.GET)
	public String toPartInLoaction(HttpServletRequest request){
		String location = getString(request, "location");
		checkStorageByLocationService.checkAndInsertRecord(location);
		request.setAttribute("location", location);
		return "/storage/storagedetail/partnumberinlocationList";
	}
	
	
	/**
	 * 位置内入库信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/partInLoaction",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo partInLoaction(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ImportPackageElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String location = request.getParameter("location");
		page.put("location", location);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		checkStorageByLocationService.listPage(page, sort);
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
		
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
	}
	
	
	/**
	 * 清点库存（清点完后删掉）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkAndRemoveRecord",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkAndRemoveRecord(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			PageModel<ImportPackageElement> page = getPage(request);
			String clientOrderElementId = getString(request, "clientOrderElementId");
			String importPackageId = getString(request, "importPackageId");
			String location = getString(request, "location");
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
			page.put("location", location);
			ImportPackageElement importPackageElement = importpackageElementService.getImportPackageElementByLocationAndCoeId(page);
			if (importPackageElement != null) {
				ResultVo resultVo = checkStorageByLocationService.checkStorage(importPackageElement);
				return resultVo;
			}else {
				return new ResultVo(false, "记录不存在！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "清点失败！");
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
