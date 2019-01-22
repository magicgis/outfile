package com.naswork.module.storage.controller.suppliercommissionsale;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.constants.FileConstant;
import com.naswork.common.controller.BaseController;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.HistoricalOrderPrice;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.StockMarketAddSupplier;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierAirRelationKey;
import com.naswork.model.SupplierCommissionForStockmarket;
import com.naswork.model.SupplierCommissionForStockmarketElement;
import com.naswork.model.SupplierCommissionSale;
import com.naswork.model.SupplierCommissionSaleElement;
import com.naswork.model.SystemCode;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.module.marketing.controller.clientinquiry.CompetitorVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.StockMarketAddSupplierService;
import com.naswork.service.SupplierCommissionForStockmarketElementService;
import com.naswork.service.SupplierCommissionForStockmarketService;
import com.naswork.service.SupplierCommissionSaleElementService;
import com.naswork.service.SupplierCommissionSaleService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.ColumnVo;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping(value="/storage/suppliercommissionsale")
public class SupplierCommissionSaleController extends BaseController {

	@Resource
	private SupplierCommissionSaleService supplierCommissionSaleService;
	@Resource
	private UserService userService;
	@Resource
	private SupplierCommissionSaleElementService supplierCommissionSaleElementService;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private StockMarketAddSupplierService stockMarketAddSupplierService;
	@Resource
	private SupplierCommissionForStockmarketElementService supplierCommissionForStockmarketElementService;
	@Resource
	private SupplierCommissionForStockmarketService supplierCommissionForStockmarketService;
	
	/**
	 * 跳转列表页面
	 * @return
	 */
	@RequestMapping(value="/toList",method=RequestMethod.GET)
	public String toList() {
		return "/storage/suppliercommissionsale/list";
	}
	
	/**
	 * 列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/List",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo List(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierCommissionSale> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		
		supplierCommissionSaleService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierCommissionSale supplierCommissionSale : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierCommissionSale);
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
	 * 明细列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/elementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierCommissionSaleElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		Integer supplierCommissionSaleId = new Integer(getString(request, "id"));
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		page.put("supplierCommissionSaleId", supplierCommissionSaleId);
		supplierCommissionSaleElementService.listPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierCommissionSaleElement supplierCommissionSaleElement : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierCommissionSaleElement);
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
	 * 跳转新增页面
	 * @return
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd(HttpServletRequest request) {
		SupplierCommissionSale supplierCommissionSale = new SupplierCommissionSale();
		Calendar cal = Calendar.getInstance();  
        cal.setTime(new Date());  
        cal.add(Calendar.DATE, +29);
        supplierCommissionSale.setValidity(cal.getTime());
        supplierCommissionSale.setCommissionDate(new Date());
        request.setAttribute("supplierCommissionSale", supplierCommissionSale);
		return "/storage/suppliercommissionsale/addsuppliercommissionsale";
	}
	
	/**
	 * 保存新增
	 * @param request
	 * @param supplierCommissionSale
	 * @return
	 */
	@RequestMapping(value="/saveAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute SupplierCommissionSale supplierCommissionSale) {
		boolean success = false;
		String message = "";
		try {
			if (supplierCommissionSale.getSupplierId() != null) {
				supplierCommissionSaleService.insertSelective(supplierCommissionSale);
				success = true;
				message = supplierCommissionSale.getId().toString();
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
	 * 跳转修改页面
	 * @return
	 */
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		SupplierCommissionSale supplierCommissionSale = supplierCommissionSaleService.selectByPrimaryKey(id);
		request.setAttribute("supplierCommissionSale", supplierCommissionSale);
		return "/storage/suppliercommissionsale/addsuppliercommissionsale";
	}
	
	/**
	 * 保存修改
	 * @return
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute SupplierCommissionSale supplierCommissionSale) {
		boolean success = false;
		String message = "";
		try {
			if (supplierCommissionSale.getId() != null) {
				supplierCommissionSaleService.updateByPrimaryKeySelective(supplierCommissionSale);
				success = true;
				message = "修改成功！";
			}else {
				message = "修改失败！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "保存异常！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 修改明细
	 * @return
	 */
	@RequestMapping(value="/saveElementEdit",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo saveElementEdit(HttpServletRequest request,@ModelAttribute SupplierCommissionSaleElement supplierCommissionSaleElement) {
		boolean success = false;
		String message = "";
		try {
			supplierCommissionSaleElementService.updateByPrimaryKeySelective(supplierCommissionSaleElement);
			success = true;
			message = "修改成功！";
			/*Integer id = new Integer(getString(request, "id"));
			String remark = getString(request, "remark");
			String partNumber = getString(request, "partNumber");
			Double amount = new Double(getString(request, "amount"));
			SupplierCommissionSaleElement supplierCommissionSaleElement = new SupplierCommissionSaleElement();
			supplierCommissionSaleElement.setId(id);
			supplierCommissionSaleElement.setPartNumber(partNumber);
			supplierCommissionSaleElement.setAmount(amount);
			supplierCommissionSaleElement.setRemark(remark);
			supplierCommissionSaleElement.setUpdateTimestamp(new Date());
			supplierCommissionSaleElementService.updateByPrimaryKeySelective(supplierCommissionSaleElement);
			success = true;
			message = "修改成功！";*/
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "保存异常！";
		}
		
		return new EditRowResultVo(success, message);
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
		MessageVo messageVo = supplierCommissionSaleElementService.uploadExcelSecond(multipartFile, id);
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
		return "/storage/suppliercommissionsale/addElementTable";
		
	}
	
	/*
	 * 页面新增明细
	 */
	@RequestMapping(value="/addElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo addElement(HttpServletRequest request,@ModelAttribute SupplierCommissionSaleElement supplicCommissionSaleElement) {
		boolean success = false;
		String message = "";
		Integer supplicCommissionSaleId = new Integer(getString(request, "id"));
		HttpSession session = request.getSession();
		try {
			if (supplicCommissionSaleElement.getList().size()>0) {
				supplierCommissionSaleElementService.addWithTable(supplicCommissionSaleElement.getList(), supplicCommissionSaleId);
				success = true;
				message = "新增成功！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 供应商信息
	 */
	@RequestMapping(value="/supplierList",method=RequestMethod.POST)
	public @ResponseBody ResultVo supplierList(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		PageModel<Supplier> page = getPage(request);
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}
		List<Supplier> list = supplierInquiryService.getSupplier(page);
		Integer edit = new Integer(getString(request, "edit"));
		List<Supplier> suppliers = new ArrayList<Supplier>();
		if (edit==1) {
			Integer id =new Integer(getString(request, "id"));
			SupplierCommissionSale supplierCommissionSale = supplierCommissionSaleService.selectByPrimaryKey(id);
			Supplier supplier = supplierService.selectByPrimaryKey(supplierCommissionSale.getSupplierId());
			for (Supplier supplier2 : list) {
				if (supplier2.getId().equals(supplier.getId())) {
					suppliers.add(supplier2);
					break;
				}
			}
			for (Supplier supplier2 : list) {
				if (!supplier2.getId().equals(supplier.getId())) {
					suppliers.add(supplier2);
				}
			}
		}else {
			suppliers = list;
		}
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(suppliers);
		message = jsonArray.toString();
		
		return new ResultVo(success, message);

	}
	
	/*
	 * 新增寄卖后跳转明细新增
	 */
	@RequestMapping(value="/toAddElementAfterAdd",method=RequestMethod.GET)
	public String toAddElementAfterAdd(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		return "/storage/suppliercommissionsale/addElement";
		
	}
	
	
	/**
	 * 发起爬虫
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/commitCrawl",method=RequestMethod.POST)
	public @ResponseBody ResultVo commitCrawl(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			Integer airType = new Integer(getString(request, "airType"));
			ResultVo resultVo = supplierCommissionSaleElementService.AddInquiryAndCrawlElement(id,airType);
			SupplierCommissionSale supplierCommissionSale = supplierCommissionSaleService.selectByPrimaryKey(id);
			clientInquiryElementService.searchSatair(new ArrayList<ClientInquiryElement>(), supplierCommissionSale.getCrawlClientInquiryId(), 0, 1,null);
			return resultVo;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "发起异常！");
		}
	}
	
	/**
	 * 获取件号条数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getCount(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			List<SupplierCommissionSaleElement> list = supplierCommissionSaleElementService.getDistinctWithSaleId(id);
			return new ResultVo(true, list.size()+"");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "获取总条数异常！");
		}
	}
	
	
	/**
	 * 下载模板
	 * 
	 * @param response
	 * @return
	 * @version v1.0
	 */
	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadTemplate(HttpServletResponse response) {
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment",
					new String("供应商寄卖模板.xlsx".getBytes("UTF-8"),
							"ISO-8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return new ResponseEntity<byte[]>(
					FileUtils.readFileToByteArray(new File(
							FileConstant.UPLOAD_REALPATH
									+ "/mis/template/最新寄卖模板.xlsx")), headers,
					HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 预报价页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toPriceList",method=RequestMethod.GET)
	public String toPriceList(HttpServletRequest request){
		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("type", "purchase");
		return "/storage/suppliercommissionsale/weatherquotelist";
	}
	
	/**
	 * 跳转选择机型页面
	 * @return
	 */
	@RequestMapping(value="/toSelectAirType",method=RequestMethod.GET)
	public String toSelectAirType(HttpServletRequest request){
		request.setAttribute("airTypeId", getString(request, "airTypeId"));
		return "/storage/suppliercommissionsale/selectAirType";
	}
	
	/**
	 * 获取类型列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/airType",method=RequestMethod.POST)
	public @ResponseBody ResultVo airType(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		UserVo userVo=getCurrentUser(request);
		String userName=userVo.getUserName();
		String id = getString(request, "id");
		String airTypeId = getString(request, "airTypeId");
		List<SystemCode> list=systemCodeService.findType("AIR_TYPE_FOR_STOCK_MARKET");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		if (id != null && !"".equals(id)) {
			SupplierCommissionSale supplierCommissionSale = supplierCommissionSaleService.selectByPrimaryKey(new Integer(id));
			if (supplierCommissionSale.getAirTypeId() != null && !"".equals(supplierCommissionSale.getAirTypeId())) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId().equals(supplierCommissionSale.getAirTypeId())) {
						arraylist.add(list.get(i));
					}
				}
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).getId().equals(supplierCommissionSale.getAirTypeId())) {
						arraylist.add(list.get(i));
					}
				}
			}else {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getValue().equals("其他机型")) {
						arraylist.add(list.get(i));
					}
				}
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).getValue().equals("其他机型")) {
						arraylist.add(list.get(i));
					}
				}
			}
		}else if (airTypeId != null && !"".equals(airTypeId)) {
			SystemCode systemCode = systemCodeService.selectByPrimaryKey(new Integer(airTypeId));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(systemCode.getId())) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().equals(systemCode.getId())) {
					arraylist.add(list.get(i));
				}
			}
		}else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getValue().equals("其他机型")) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getValue().equals("其他机型")) {
					arraylist.add(list.get(i));
				}
			}
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(arraylist);
		message =json.toString();
		return new ResultVo(success, message);
	}
	
	/**
	 * 需要爬取的供应商列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/crawlSupplierList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo crawlSupplierList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierCommissionSaleElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		Integer supplierCommissionSaleElementId = new Integer(getString(request, "id"));
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		page.put("id", supplierCommissionSaleElementId);
		supplierCommissionSaleElementService.getCrawlSupplierList(page, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierCommissionSaleElement supplierCommissionSaleElement : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierCommissionSaleElement);
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
	 * 增加爬虫的供应商
	 * @param request
	 * @param stockMarketAddSupplier
	 * @return
	 */
	@RequestMapping(value="/addSupplier",method=RequestMethod.POST)
	public @ResponseBody ResultVo addSupplier(HttpServletRequest request) {
  		String message = "";
		boolean success = false;
		String ids=request.getParameter("ids");
		Integer supplierCommissionSaleElementId = new Integer(getString(request, "supplierCommissionSaleElementId"));
		try {
			String[] id=ids.split(",");
			for (int i = 0; i < id.length; i++) {
				StockMarketAddSupplier stockMarketAddSupplier = new StockMarketAddSupplier();
				Integer count = supplierCommissionSaleElementService.checkRecord(supplierCommissionSaleElementId, new Integer(id[i]));
				if(count == null || count.equals(0)){
					stockMarketAddSupplier.setSupplierCommissionSaleElementId(supplierCommissionSaleElementId);
					stockMarketAddSupplier.setSupplierId(new Integer(id[i]));
					stockMarketAddSupplierService.insertSelective(stockMarketAddSupplier);
				}
			}
			 success = true;
			 message = "新增完成";
		} catch (Exception e) {
			e.printStackTrace();
			message = "保存失败！";
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 删除爬虫的供应商
	 * @param request
	 * @param stockMarketAddSupplier
	 * @return
	 */
	@RequestMapping(value="/deleteSupplier",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteSupplier(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		Integer id = new Integer(getString(request, "id"));
		try {
			stockMarketAddSupplierService.deleteByPrimaryKey(id);
			success = true;
			message = "删除完成";
		} catch (Exception e) {
			e.printStackTrace();
			message = "删除失败！";
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 跳转增加供应商页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAddSupplier",method=RequestMethod.GET)
	public String toAddSupplier(HttpServletRequest request){
		String id = getString(request, "id");
		request.setAttribute("id", id);
		return "/storage/suppliercommissionsale/supplierlist";
	}
	
	
	/**
	 * 统计页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toCountTable",method=RequestMethod.GET)
	public String toCountTable(HttpServletRequest request){
		String id = getString(request, "id");
		request.setAttribute("id", id);
		//id = "577843";
		List<String> suppliers = supplierCommissionSaleElementService.getSuppliers(new Integer(id));
		request.setAttribute("size", suppliers.size());
		JSONArray json = new JSONArray();
		json.add(suppliers);
		request.setAttribute("list", json.toString());
		return "/storage/stockmarketmessage/list";
	}
	
	/**
	 * 获取echart折线图显示的数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getEchartData",method=RequestMethod.POST)
	public @ResponseBody ResultVo getEchartData(HttpServletRequest request){
		try {
			PageModel<String> page = new PageModel<String>();
			String id = getString(request, "id");
			String supplier = getString(request, "supplier");
			//id = "577843";
			page.put("id", id);
			if (supplier != null && !"".equals(supplier) && !"Total".equals(supplier)) {
				page.put("supplier", supplier);
			}
			List<String> dataList = supplierCommissionSaleElementService.getDateAmount(page);
			List<Double> ars = new ArrayList<Double>();
			List<Double> svs = new ArrayList<Double>();
			List<Double> ohs = new ArrayList<Double>();
			List<Double> nes = new ArrayList<Double>();
			List<CountStockMarketVo> arList = supplierCommissionSaleElementService.getArAmount(page);
			for (int i = 0; i < dataList.size(); i++) {
				boolean addFlat = false;
				for (int j = 0; j < arList.size(); j++) {
					if (dataList.get(i).equals(arList.get(j).getCrawlDate())) {
						ars.add(arList.get(j).getAr());
						addFlat = true;
						break;
					}
				}
				if (!addFlat) {
					ars.add(0.0);
				}
			}
			List<CountStockMarketVo> svList = supplierCommissionSaleElementService.getSvAmount(page);
			for (int i = 0; i < dataList.size(); i++) {
				boolean addFlat = false;
				for (int j = 0; j < svList.size(); j++) {
					if (dataList.get(i).equals(svList.get(j).getCrawlDate())) {
						svs.add(svList.get(j).getSv());
						addFlat = true;
						break;
					}
				}
				if (!addFlat) {
					svs.add(0.0);
				}
			}
			List<CountStockMarketVo> ohList = supplierCommissionSaleElementService.getOhAmount(page);
			for (int i = 0; i < dataList.size(); i++) {
				boolean addFlat = false;
				for (int j = 0; j < ohList.size(); j++) {
					if (dataList.get(i).equals(ohList.get(j).getCrawlDate())) {
						ohs.add(ohList.get(j).getOh());
						addFlat = true;
						break;
					}
				}
				if (!addFlat) {
					ohs.add(0.0);
				}
			}
			List<CountStockMarketVo> neList = supplierCommissionSaleElementService.getNeAmount(page);
			for (int i = 0; i < dataList.size(); i++) {
				boolean addFlat = false;
				for (int j = 0; j < neList.size(); j++) {
					if (dataList.get(i).equals(neList.get(j).getCrawlDate())) {
						nes.add(neList.get(j).getNe());
						addFlat = true;
						break;
					}
				}
				if (!addFlat) {
					nes.add(0.0);
				}
			}
			JSONArray json = new JSONArray();
			json.add(ars);
			json.add(svs);
			json.add(ohs);
			json.add(nes);
			json.add(dataList);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "加载列表数据失败！");
		}
	}
	
	/**
	 * 获取供应商列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSupplierList",method=RequestMethod.POST)
	public @ResponseBody ResultVo getSupplierList(HttpServletRequest request){
		try {
			PageModel<String> page = new PageModel<String>();
			String id = getString(request, "id");
			//id = "577843";
			List<String> supplierList = supplierCommissionSaleElementService.getSupplierList(new Integer(id));
			JSONArray json = new JSONArray();
			json.add(supplierList);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "加载列表数据失败！");
		}
	}
	
	/**
	 * 获取时间列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getTimeList",method=RequestMethod.POST)
	public @ResponseBody ResultVo getTimeList(HttpServletRequest request){
		try {
			PageModel<String> page = new PageModel<String>();
			String id = getString(request, "id");
			//id = "577843";
			page.put("id", id);
			List<String> dataList = supplierCommissionSaleElementService.getDateAmount(page);
			JSONArray json = new JSONArray();
			json.add(dataList);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "加载列表数据失败！");
		}
	}
	
	
	/**
	 * 获取动态列
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCols",method=RequestMethod.POST)
	public @ResponseBody ColumnVo getCols(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		List<String> suppliers = supplierCommissionSaleElementService.getSuppliers(id);
		List<String> colModel = new ArrayList<String>();
		List<String> colNames = new ArrayList<String>();
		for (int i = 0; i < suppliers.size(); i++) {
			colNames.add("AR");
			colModel.add(suppliers.get(i)+"_AR");
			colNames.add("SV");
			colModel.add(suppliers.get(i)+"_SV");
			colNames.add("OH");
			colModel.add(suppliers.get(i)+"_OH");
			colNames.add("NE");
			colModel.add(suppliers.get(i)+"_NE");
		}
		ColumnVo result = new ColumnVo();
		result.setColumnDisplayNames(colNames);
		result.setColumnKeyNames(colModel);
		
		return result;
 	}
	
	/**
	 * 统计stockmarket爬虫列表
	 * @param request
	 * @param response
	 * @return
	 */
	/**
	@Author: Modify by white
	@DateTime: 2018/9/5 14:57
	@Description:  添加旧件，新件，维修平均价
	*/
	@RequestMapping(value="/countStockMarket",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo countStockMarket(HttpServletRequest request,HttpServletResponse response) {
		PageModel<String> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		Integer id = new Integer(getString(request, "id"));
		List<String> suppliers = supplierCommissionSaleElementService.getSuppliers(id);
		SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement = supplierCommissionForStockmarketElementService.selectByPrimaryKey(id);
		SupplierCommissionForStockmarket supplierCommissionForStockmarket = supplierCommissionForStockmarketService.selectByPrimaryKey(supplierCommissionForStockmarketElement.getSupplierCommissionForStockmarketId());
		StringBuffer string = new StringBuffer();
		for (int i = 0; i < suppliers.size(); i++) {
			//SV
			string.append("SUM(IF(smcm.SUPPLIER_CODE = '");
			string.append(suppliers.get(i).replace("'", "\\'"));
			string.append("' AND smcm.CONDITION_VALUE = 'SV',smcm.AMOUNT,0)) AS '");
			string.append(suppliers.get(i).replace("'", "\\'")).append("_SV',");
			//AR
			string.append("SUM(IF(smcm.SUPPLIER_CODE = '");
			string.append(suppliers.get(i).replace("'", "\\'"));
			string.append("' AND smcm.CONDITION_VALUE = 'AR',smcm.AMOUNT,0)) AS '");
			string.append(suppliers.get(i).replace("'", "\\'")).append("_AR',");
			//OH
			string.append("SUM(IF(smcm.SUPPLIER_CODE = '");
			string.append(suppliers.get(i).replace("'", "\\'"));
			string.append("' AND smcm.CONDITION_VALUE = 'OH',smcm.AMOUNT,0)) AS '");
			string.append(suppliers.get(i).replace("'", "\\'")).append("_OH',");
			//NE
			string.append("SUM(IF(smcm.SUPPLIER_CODE = '");
			string.append(suppliers.get(i).replace("'", "\\'"));
			string.append("' AND smcm.CONDITION_VALUE = 'NE',smcm.AMOUNT,0)) AS '");
			string.append(suppliers.get(i).replace("'", "\\'")).append("_NE',");
		}
		page.put("supplier", string.toString());
		page.put("id", id);
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		List<Map<String, Object>> mapList = supplierCommissionSaleElementService.stockMarketCountMessage(page);
		if (mapList.size() > 0) {
			System.out.println(mapList.get(0).get("partNumber").toString());
			String partNumer = mapList.get(0).get("partNumber").toString();
			Map<String,Object> resultMap = new HashMap<String,Object>();
			List<Double> oldPriceList = new ArrayList<Double>();
			List<String> oldPrices = supplierCommissionForStockmarketElementService.getOldPricesByPartNumber(partNumer);
			for (String prices : oldPrices) {
				if (prices != null) {
					String[] priceArra = prices.split(",");
					for (int i = 0; i < priceArra.length; i++) {
						if (priceArra[i] != null && !"".equals(priceArra[i])) {
							oldPriceList.add(new Double(priceArra[i]));
						}
					}
				}
				
			}
			if(oldPriceList.size()>5){
				Double total = 0.0;
				for(int oldIndex = 1;oldIndex<oldPriceList.size()-1;oldIndex++){
					if(oldPriceList.get(oldIndex) != null && !"".equals(oldPriceList.get(oldIndex))){
						total = total + new Double(oldPriceList.get(oldIndex));
					}
				}
				BigDecimal amount  = new BigDecimal(total);
				BigDecimal length = new BigDecimal(oldPriceList.size()-2);
				String avg = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
				resultMap.put("oldPrices",avg);
			}else if (oldPriceList.size() > 0) {
				Double total = 0.0;
				for(int oldIndex = 0;oldIndex<oldPriceList.size();oldIndex++){
					if(oldPriceList.get(oldIndex) != null && !"".equals(oldPriceList.get(oldIndex))){
						total = total + new Double(oldPriceList.get(oldIndex));
					}
				}
				BigDecimal amount  = new BigDecimal(total);
				BigDecimal length = new BigDecimal(oldPriceList.size());
				String avg = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
				resultMap.put("oldPrices",avg);
			}
			/*String  oldPrices = supplierCommissionForStockmarketElementService.getOldPricesByPartNumber(partNumer);
			if(oldPrices != null){
				String [] oldPricesArray = oldPrices.split(",");
				Double total = 0.0;
				if(oldPricesArray.length>5){
					for(int oldIndex = 1;oldIndex<oldPricesArray.length-1;oldIndex++){
						if(oldPricesArray[oldIndex] != null && !"".equals(oldPricesArray[oldIndex])){
							total = total + new Double(oldPricesArray[oldIndex]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(oldPricesArray.length-2);
					oldPrices = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("oldPrices",oldPrices);
				}else{
					for(int oldIndex = 0;oldIndex<oldPricesArray.length;oldIndex++){
						if(oldPricesArray[oldIndex] != null && !"".equals(oldPricesArray[oldIndex])){
							total = total + new Double(oldPricesArray[oldIndex]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(oldPricesArray.length);
					oldPrices = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("oldPrices",oldPrices);
				}
			}*/
			PageModel<String> wherePage = new PageModel<String>();
			wherePage.put("partNumer", partNumer);
			if (supplierCommissionForStockmarket.getClientInquiryId() != null) {
				wherePage.put("ciid", supplierCommissionForStockmarket.getClientInquiryId());
			}
			List<String>  newPrices = supplierCommissionForStockmarketElementService.getNewPricesByPartNumber(wherePage);
			List<Double> newPriceList = new ArrayList<Double>();
			for (String prices : newPrices) {
				if (prices != null) {
					String[] priceArra = prices.split(",");
					for (int j = 0; j < priceArra.length; j++) {
						if (priceArra[j] != null && !"".equals(priceArra[j])) {
							newPriceList.add(new Double(priceArra[j]));
						}
					}
				}
			}
			Collections.sort(newPriceList);
			if(newPriceList.size()>5){
				Double total = 0.0;
				for(int newIndex = 1;newIndex<newPriceList.size()-1;newIndex++){
					if(newPriceList.get(newIndex) != null && !"".equals(newPriceList.get(newIndex))){
						total = total + new Double(newPriceList.get(newIndex));
					}
				}
				BigDecimal amount  = new BigDecimal(total);
				BigDecimal length = new BigDecimal(newPriceList.size()-2);
				String avg = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
				resultMap.put("newPrices",avg);
			}else if (newPriceList.size() > 0) {
				Double total = 0.0;
				for(int newIndex = 0;newIndex<newPriceList.size();newIndex++){
					if(newPriceList.get(newIndex) != null && !"".equals(newPriceList.get(newIndex))){
						total = total + new Double(newPriceList.get(newIndex));
					}
				}
				BigDecimal amount  = new BigDecimal(total);
				BigDecimal length = new BigDecimal(newPriceList.size());
				String avg = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
				resultMap.put("newPrices",avg);
			}
			/*if(newPrices != null){
				String [] newPricesArray = newPrices.split(",");
				Double total = 0.0;
				if(newPricesArray.length>5){
					for(int newIndex = 1;newIndex<newPricesArray.length-1;newIndex++){
						if(newPricesArray[newIndex] != null && !"".equals(newPricesArray[newIndex])){
							total = total + new Double(newPricesArray[newIndex]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(newPricesArray.length-2);
					newPrices = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("newPrices",newPrices);
				}else{
					for(int newIndex = 0;newIndex<newPricesArray.length;newIndex++){
						if(newPricesArray[newIndex] != null && !"".equals(newPricesArray[newIndex])){
							total = total + new Double(newPricesArray[newIndex]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(newPricesArray.length);
					newPrices = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("newPrices",newPrices);
				}
			}*/
			String  mainPrices = supplierCommissionForStockmarketElementService.getMainPricesByPartNumber(partNumer);
			if(mainPrices != null){
				String [] mainPricesArray = mainPrices.split(",");
				Double total = 0.0;
				if(mainPricesArray.length>5){
					for(int mainIndex = 1;mainIndex<mainPricesArray.length-1;mainIndex++){
						if(mainPricesArray[mainIndex] != null && !"".equals(mainPricesArray[mainIndex])){
							total = total + new Double(mainPricesArray[mainIndex]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(mainPricesArray.length-2);
					mainPrices = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("maintenancePrices",mainPrices);
				}else{
					for(int mainIndex = 0;mainIndex<mainPricesArray.length;mainIndex++){
						if(mainPricesArray[mainIndex] != null && !"".equals(mainPricesArray[mainIndex])){
							total = total + new Double(mainPricesArray[mainIndex]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(mainPricesArray.length);
					mainPrices = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("maintenancePrices",mainPrices);
				}
			}
			String mroRepair = supplierCommissionForStockmarketElementService.getMroRepairByPartNumber(partNumer);
			if(mroRepair != null){
				String[] priceArra = mroRepair.split(",");
				if (priceArra.length > 5) {
					Double total = 0.0;
					for (int i = 1; i < priceArra.length-1; i++) {
						if (priceArra[i] != null && !"".equals(priceArra[i])) {
							total = total + new Double(priceArra[i]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(priceArra.length - 2);
					mroRepair = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("mroRepair",mroRepair);
				}else {
					Double total = 0.0;
					for (int i = 0; i < priceArra.length; i++) {
						if (priceArra[i] != null && !"".equals(priceArra[i])) {
							total = total + new Double(priceArra[i]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(priceArra.length);
					mroRepair = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("mroRepair",mroRepair);
				}
			}
			String mroOverhual = supplierCommissionForStockmarketElementService.getMroOverhaulByPartNumber(partNumer);
			if(mroOverhual != null){
				String[] priceArra = mroOverhual.split(",");
				if (priceArra.length > 5) {
					Double total = 0.0;
					for (int i = 1; i < priceArra.length-1; i++) {
						if (priceArra[i] != null && !"".equals(priceArra[i])) {
							total = total + new Double(priceArra[i]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(priceArra.length - 2);
					mroOverhual = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("mroOverhual",mroOverhual);
				}else {
					Double total = 0.0;
					for (int i = 0; i < priceArra.length; i++) {
						if (priceArra[i] != null && !"".equals(priceArra[i])) {
							total = total + new Double(priceArra[i]);
						}
					}
					BigDecimal amount  = new BigDecimal(total);
					BigDecimal length = new BigDecimal(priceArra.length);
					mroOverhual = amount.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
					resultMap.put("mroOverhual",mroOverhual);
				}
			}
			/*if(mroOverhual != null){
				resultMap.put("mroOverhual",mroOverhual);
			}*/
			for(int mapIndex = 0;mapIndex<mapList.size();mapIndex++){
				mapList.get(mapIndex).put("newPrices",resultMap.get("newPrices"));
				mapList.get(mapIndex).put("oldPrices",resultMap.get("oldPrices"));
				mapList.get(mapIndex).put("maintenancePrices",resultMap.get("maintenancePrices"));
				mapList.get(mapIndex).put("mroOverhual",resultMap.get("mroOverhual"));
				mapList.get(mapIndex).put("mroRepair",resultMap.get("mroRepair"));
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
	 * stockmarket爬虫
	 */
	@RequestMapping(value="/checkStockMarketCrawl",method=RequestMethod.POST)
	public void checkStockMarketCrawl(){
		synchronized(this){
			try {
				List<SupplierCommissionSale> list = supplierCommissionSaleService.getCrawlStockList();
				for (SupplierCommissionSale supplierCommissionSale : list) {
					Integer count = supplierCommissionSaleElementService.checkCrawlRecord(supplierCommissionSale.getId());
					Integer resordInteger = supplierCommissionSaleElementService.checkCrawlRecordById(supplierCommissionSale.getId());
					if (resordInteger == 0 || (count != null && count > 0)) {
						Date date = new Date();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						supplierCommissionSaleElementService.insertStockMarket(simpleDateFormat.format(date), supplierCommissionSale.getId().toString());
						Integer lastInsertId = supplierCommissionSaleElementService.getStockLastInsert();
						supplierCommissionSaleElementService.crawlStockMarket(lastInsertId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 跳转StockMarket页面
	 * @return
	 */
	@RequestMapping(value="/toStockCrawlMessage",method=RequestMethod.GET)
	public String toStockCrawlMessage(HttpServletRequest request){
//		UserVo userVo = getCurrentUser(request);
		//只有用户id为 17 和26 只有Kris和Saidy进行操作
//		if("17".equals(userVo.getUserId())
//			|| "26".equals(userVo.getUserId())
//		    || "1".equals(userVo.getUserId())
//			|| "2".equals(userVo.getUserId())
//			|| "3".equals(userVo.getUserId())
//		){
			return "/storage/stockmarketcrawlmessage/list";
//		}else{
//			return "/common/noPermission";
//		}
	}
	
	/**
	 * stockmarket爬虫列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/stockCrawlList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo stockCrawlList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<CountStockMarketVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		page.put("where", where);
		supplierCommissionSaleElementService.getStockCrawlListPage(page, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (CountStockMarketVo countStockMarketVo : page.getEntities()) {
				DecimalFormat df = new DecimalFormat("######0.00");  
				Double percent = supplierCommissionForStockmarketElementService.getCrawlPercent(countStockMarketVo.getId())*100;
				if (new Double(100).equals(percent) && countStockMarketVo.getComplete().equals(0)) {
					countStockMarketVo.setCrawlPercent(0+"%");
				}else {
					countStockMarketVo.setCrawlPercent(df.format(percent)+"%");
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(countStockMarketVo);
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
	 * stockmarket爬虫明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/stockCrawlElementList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo stockCrawlElementList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<CountStockMarketVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		Integer id = new Integer(getString(request, "id"));
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		page.put("stockMarketCrawlId", id);
		supplierCommissionSaleElementService.getStockCrawlElementPage(page, sort, where);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (CountStockMarketVo countStockMarketVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(countStockMarketVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXlsx("爬虫信息", exportModel, mapList, response);
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
	 * 获取饼状图数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getPieData",method=RequestMethod.POST)
	public @ResponseBody ResultVo getPieData(HttpServletRequest request){
		try {
			String supplier = getString(request, "supplier");
			String id = getString(request, "id");
			String time = getString(request, "time");
			PageModel<String> page = new PageModel<String>();
			if (supplier != null && !"".equals(supplier) && !supplier.equals("Total")) {
				page.put("supplierCode", supplier);
			}
			if (time != null && !"".equals(time)) {
				page.put("time", time);
			}
			page.put("id", id);
			
			//ar数据
			page.put("condition", "ar");
			List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
			Double arDouble = supplierCommissionSaleElementService.getDataForPie(page);
			Map<String, String> arMap = new HashMap<String, String>();
			arMap.put("name", "AR");
			if (arDouble != null) {
				arMap.put("value", arDouble.toString());
			}else {
				arMap.put("value", "0");
			}
			listMap.add(arMap);
			
			//sv数据
			page.put("condition", "sv");
			Double svDouble = supplierCommissionSaleElementService.getDataForPie(page);
			Map<String, String> svMap = new HashMap<String, String>();
			svMap.put("name", "SV");
			if (svDouble != null) {
				svMap.put("value", svDouble.toString());
			}else {
				svMap.put("value", "0");
			}
			listMap.add(svMap);
			
			//oh数据
			page.put("condition", "oh");
			Double ohDouble = supplierCommissionSaleElementService.getDataForPie(page);
			Map<String, String> ohMap = new HashMap<String, String>();
			ohMap.put("name", "OH");
			if (ohDouble != null) {
				ohMap.put("value", ohDouble.toString());
			}else {
				ohMap.put("value", "0");
			}
			listMap.add(ohMap);
			
			//ne数据
			page.put("condition", "ne");
			Double neDouble = supplierCommissionSaleElementService.getDataForPie(page);
			Map<String, String> neMap = new HashMap<String, String>();
			neMap.put("name", "NE");
			if (neDouble != null) {
				neMap.put("value", neDouble.toString());
			}else {
				neMap.put("value", "0");
			}
			listMap.add(neMap);
			JSONArray json = new JSONArray();
			json.add(listMap);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
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
