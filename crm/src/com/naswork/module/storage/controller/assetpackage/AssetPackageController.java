package com.naswork.module.storage.controller.assetpackage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.naswork.dao.ArPricePartMappingDao;
import com.naswork.model.*;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.service.*;
import net.sf.json.JSONArray;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.constants.FileConstant;
import com.naswork.common.controller.BaseController;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

/**
 * @Author: white
 * @Date: create in 2018-08-14 16:00
 * @Description: 采购10200----
 * @Modify_By:
 */
@Controller
@RequestMapping(value = "storage/assetpackage")
public class AssetPackageController extends BaseController{

	@Resource
	private SupplierCommissionForStockmarketService supplierCommissionForStockmarketService;
	@Resource
	private SupplierCommissionForStockmarketElementService supplierCommissionForStockmarketElementService;
	@Resource
	private UserService userService;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SupplierCommissionSaleElementService supplierCommissionSaleElementService;
	@Resource
	private ArPricePartMappingService arPricePartMappingService;

	/**
	 * @Author: Create by white
	 * @Description: 获取ar预测价列表
	 * @Date: 2018-08-21 11:50
	 * @Params: [request]
	 * @Return: com.naswork.vo.JQGridMapVo
	 * @Throws:
	 */
	@RequestMapping(value = "/getARPriceData",method = RequestMethod.POST)
	@ResponseBody
	public JQGridMapVo getARPriceData(HttpServletRequest request){
		PageModel<ArPricePartMappingVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = request.getParameter("searchString");
		String partNumber = getString(request, "partNumber");
		GridSort sort = getSort(request);
		UserVo userVo=getCurrentUser(request);
		if ("".equals(where)) {
			where = null;
		}else{
			page.put("where",where);
		}
		if (!"".equals(partNumber)) {
			page.put("partNumber", partNumber);
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", userVo.getUserId());
		}

		arPricePartMappingService.getARPriceDataPage(page,where,sort);
		if(page.getEntities().size()>0){
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
			for(ArPricePartMappingVo arPricePartMappingVo:page.getEntities()){
				Map<String, Object> map = EntityUtil.entityToTableMap(arPricePartMappingVo);
				maps.add(map);
			}
			jqgrid.setRows(maps);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
	}

	/**
	 * @Author: Create by white
	 * @Description: 跳转到AR预测报价页面
	 * @Date: 2018-08-21 11:26
	 * @Params: []
	 * @Return: java.lang.String
	 * @Throws:
	 */
	@RequestMapping(value="/partandarpricemappinglist",method = RequestMethod.GET)
	public String partandarpricemappinglist(){
		return "/storage/assetpackage/partandarpricemappinglist";
	}

    /**
     * @Author: Create by white
     * @Description: 返回主页面
     * @Date: 2018-08-14 16:15
     * @Params: []
     * @Return: java.lang.String
     * @Throws:
     */
    @RequestMapping(value = "toList",method = RequestMethod.GET)
    public String toList(HttpServletRequest request){
//		UserVo userVo = getCurrentUser(request);
		//只有用户id为 17 和26 只有Kris和Saidy进行操作
//		if("17".equals(userVo.getUserId())
//			|| "26".equals(userVo.getUserId())
//			|| "1".equals(userVo.getUserId())
//			|| "2".equals(userVo.getUserId())
//			|| "3".equals(userVo.getUserId())
//		){
			return "/storage/assetpackage/list";
//		}else{
//			return "/common/noPermission";
//		}
    }
    
    /**
     * @Author: Tanoy
     * @date:2018年8月16日 上午10:37:49
     * @Description: 资产包数据加载
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierCommissionForStockmarket> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String where = getString(request, "searchString");
		String partNumber = getString(request, "partNumber");
		if (where != null && !"".equals(where)) {
			page.put("where", where);
		}
		if (partNumber != null && !"".equals(partNumber)) {
			page.put("partNumber", partNumber);
		}
		supplierCommissionForStockmarketService.listPage(page, sort);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierCommissionForStockmarket supplierCommissionForStockmarket : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierCommissionForStockmarket);
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
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 上午10:53:11
	 * @Description:资产包明细数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/elementListData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementListData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierCommissionForStockmarketElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String id = getString(request, "id");
		GridSort sort = getSort(request);
		String where = getString(request, "searchString");
		if (where != null && !"".equals(where)) {
			page.put("where", where);
		}
		page.put("id", id);
		supplierCommissionForStockmarketElementService.listPage(page, sort);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierCommissionForStockmarketElement);
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
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 上午11:00:56
	 * @Description:修改资产包明细
	 * @param request
	 * @param supplierCommissionForStockmarketElement
	 * @return
	 */
	@RequestMapping(value="/editElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo editElement(HttpServletRequest request,@ModelAttribute SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement){
		try {
			supplierCommissionForStockmarketElement.setUpdateTimestamp(new Date());
			supplierCommissionForStockmarketElementService.updateByPrimaryKeySelective(supplierCommissionForStockmarketElement);
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午1:57:50
	 * @Description:跳转新增资产包页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addAssetPackage",method=RequestMethod.GET)
	public String addAssetPackage(HttpServletRequest request){
		return "/storage/assetpackage/addassetpackage";
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午2:07:19
	 * @Description:新增资产包
	 * @param request
	 * @param supplierCommissionForStockmarket
	 * @return
	 */
	@RequestMapping(value="/saveAddAssetPackage",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAddAssetPackage(HttpServletRequest request,@ModelAttribute SupplierCommissionForStockmarket supplierCommissionForStockmarket){
		try {
			supplierCommissionForStockmarket.setCreateDate(new Date());
			supplierCommissionForStockmarketService.insertSelective(supplierCommissionForStockmarket);
			return new ResultVo(true, supplierCommissionForStockmarket.getId().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午2:07:54
	 * @Description:修改资产包
	 * @param request
	 * @param supplierCommissionForStockmarket
	 * @return
	 */
	@RequestMapping(value="/saveEditAssetPackage",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEditAssetPackage(HttpServletRequest request,@ModelAttribute SupplierCommissionForStockmarket supplierCommissionForStockmarket){
		try {
			supplierCommissionForStockmarketService.updateByPrimaryKeySelective(supplierCommissionForStockmarket);
			return new ResultVo(true, "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午4:27:05
	 * @Description:跳转明细新增表格
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAfterAddTable",method=RequestMethod.GET)
	public String toAfterAddTable(HttpServletRequest request){
		request.setAttribute("id", getString(request, "id"));
		return "/storage/assetpackage/addElementTableAfterAdd";
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午4:33:01
	 * @Description:新增明细
	 * @param request
	 * @param supplierCommissionForStockmarketElement
	 * @return
	 */
	@RequestMapping(value="/saveElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveElement(HttpServletRequest request,@ModelAttribute SupplierCommissionForStockmarketElement supplierCommissionForStockmarketElement){
		try {
			Integer id = new Integer(getString(request, "id"));
			if (supplierCommissionForStockmarketElement.getList().size() > 0) {
				for (int i = 0; i < supplierCommissionForStockmarketElement.getList().size(); i++) {
					supplierCommissionForStockmarketElement.getList().get(i).setSupplierCommissionForStockmarketId(id);
					supplierCommissionForStockmarketElementService.insertSelective(supplierCommissionForStockmarketElement.getList().get(i));
				}
				return new ResultVo(true, "新增成功！");
			}else {
				return new ResultVo(false, "新增失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
		
	}
	
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午1:57:50
	 * @Description:跳转修改资产包页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editAssetPackage",method=RequestMethod.GET)
	public String editAssetPackage(HttpServletRequest request){
		Integer id = new Integer(getString(request, "id"));
		SupplierCommissionForStockmarket supplierCommissionForStockmarket = supplierCommissionForStockmarketService.selectByPrimaryKey(id);
		request.setAttribute("supplierCommissionForStockmarket", supplierCommissionForStockmarket);
		return "/storage/assetpackage/addassetpackage";
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午4:44:22
	 * @Description:获取供应商列表
	 * @param request
	 * @return
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
			SupplierCommissionForStockmarket supplierCommissionForStockmarket = supplierCommissionForStockmarketService.selectByPrimaryKey(id);
			Supplier supplier = supplierService.selectByPrimaryKey(supplierCommissionForStockmarket.getSupplierId());
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
		String airTypeId = getString(request, "airTypeId");
		List<SystemCode> list=systemCodeService.findType("AIR_TYPE_FOR_STOCK_MARKET");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		if (airTypeId != null && !"".equals(airTypeId)) {
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
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午6:18:30
	 * @Description:上传明细
	 * @param request
	 * @param response
	 * @param
	 * @return
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		Integer id =new Integer(getString(request, "id"));
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = supplierCommissionForStockmarketElementService.uploadExcel(multipartFile, id);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月16日 下午6:27:51
	 * @Description:发起报价爬虫
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/commitCrawl",method=RequestMethod.POST)
	public @ResponseBody ResultVo commitCrawl(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			Integer airType = new Integer(getString(request, "airType"));
			ResultVo resultVo = supplierCommissionForStockmarketElementService.AddInquiryAndCrawlElement(id,airType);
			SupplierCommissionForStockmarket supplierCommissionForStockmarket = supplierCommissionForStockmarketService.selectByPrimaryKey(id);
			clientInquiryElementService.searchSatair(new ArrayList<ClientInquiryElement>(), supplierCommissionForStockmarket.getClientInquiryId(), 0, 1,null);
			return resultVo;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "发起异常！");
		}
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月17日 下午3:37:35
	 * @Description:选择生成询价单的类型
	 * @return
	 */
	@RequestMapping(value="/toSelectAirType",method=RequestMethod.GET)
	public String toSelectAirType(){
		return "/storage/assetpackage/selectAirType";
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月17日 下午4:49:27
	 * @Description:获取明细数量
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getElementCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getElementCount(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			List<SupplierCommissionForStockmarketElement> list = supplierCommissionForStockmarketElementService.selectByForeign(id);
			return new ResultVo(true, list.size()+"");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
		}
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月18日 下午2:10:48
	 * @Description:跳转统计页面
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
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月18日 下午2:25:13
	 * @Description:跳转爬取供应商列表
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
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月18日 下午2:26:10
	 * @Description:需要爬取的供应商列表
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
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月18日 下午2:28:53
	 * @Description:下载明细上传模板
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadTemplate(HttpServletResponse response) {
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment",
					new String("资产包模板.xlsx".getBytes("UTF-8"),
							"ISO-8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return new ResponseEntity<byte[]>(
					FileUtils.readFileToByteArray(new File(
							FileConstant.UPLOAD_REALPATH
									+ "/mis/template/assetpackage.xlsx")), headers,
					HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @Author: Tanoy
	 * @date:2018年8月18日 下午2:45:57
	 * @Description:Stockmarket定时爬虫
	 */
	@RequestMapping(value="/checkStockMarketCrawl",method=RequestMethod.POST)
	public void checkStockMarketCrawl(){
		synchronized(this){
			try {
				List<SupplierCommissionForStockmarket> list = supplierCommissionForStockmarketService.getCrawlStockList();
				for (SupplierCommissionForStockmarket supplierCommissionForStockmarket : list) {
					Integer count = supplierCommissionSaleElementService.checkCrawlRecord(supplierCommissionForStockmarket.getId());
					Integer resordInteger = supplierCommissionSaleElementService.checkCrawlRecordById(supplierCommissionForStockmarket.getId());
					if (resordInteger == 0 || (count != null && count > 0)) {
						Date date = new Date();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						supplierCommissionSaleElementService.insertStockMarket(simpleDateFormat.format(date), supplierCommissionForStockmarket.getId().toString());
						Integer lastInsertId = supplierCommissionSaleElementService.getStockLastInsert();
						supplierCommissionSaleElementService.crawlStockMarket(lastInsertId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * @Author: Create by white
	 * @Datetime: 2018/10/11 14:33
	 * @Descrition: toAddArPriceTable 跳转到模板添加AR价格页面
	 * @Params: []
	 * @Return: java.lang.String
	 * @Throws:
	 */
    @RequestMapping(value = "/toAddArPriceTable",method = RequestMethod.GET)
	public String toAddArPriceTable(){
		return "/storage/assetpackage/addArPriceByTemplate";
	}

	/*
	 * @Author: Create by white
	 * @Datetime: 2018/10/13 18:54
	 * @Descrition: uploadArPriceExcel  通过模板上传arPrice
	 * @Params: [request, response]
	 * @Return: java.lang.String
	 * @Throws:
	 */
	@RequestMapping(value = "/uploadArPriceExcel",method = RequestMethod.POST)
	@ResponseBody
	public String uploadArPriceExcel(HttpServletRequest request,HttpServletResponse response){
		boolean success=false;
		String message="";
//		Integer id =new Integer(getString(request, "id"));
//		UserVo userVo=getCurrentUser(request);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = supplierCommissionForStockmarketElementService.uploadArPriceExcel(multipartFile);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		return message;
	}

	/*
	 * @Author: Create by white
	 * @Datetime: 2018/10/13 18:56
	 * @Descrition: getArPriceListById  通过id获取到某个件号下的预测价 用于曲线图显示
	 * @Params: [request]
	 * @Return: com.naswork.vo.ResultVo
	 * @Throws:
	 */
	@RequestMapping(value = "/getArPriceListById",method = RequestMethod.POST)
	@ResponseBody
	public ResultVo getArPriceListById(HttpServletRequest request){
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    	 int id = Integer.parseInt(request.getParameter("id"));
         Map<String,Object> resultMap = new HashedMap();
         List<String> dateList = new ArrayList<String>();
         List<String> valueList = new ArrayList<String>();
         try{
			List<ArPricePartMapping> arPricePartMappings = arPricePartMappingService.getArPriceListById(id);
			for(int i = 0;i<arPricePartMappings.size();i++){
				String date = sdf.format(arPricePartMappings.get(i).getUpdateTimestamp());
				dateList.add(date);
				valueList.add(arPricePartMappings.get(i).getArPrice());
			}
		 }catch (Exception e){
         	e.printStackTrace();
         	return new ResultVo(false,"获取失败");
		 }
		 resultMap.put("dateList",dateList);
         resultMap.put("valueList",valueList);
         return  new ResultVo(true,"获取成功",resultMap);
	}


	@RequestMapping(value = "/downloadArTemplate", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadArTemplate(HttpServletResponse response) {
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment",
					new String("Ar价格上传模板.xlsx".getBytes("UTF-8"),
							"ISO-8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return new ResponseEntity<byte[]>(
					FileUtils.readFileToByteArray(new File(
							FileConstant.UPLOAD_REALPATH
									+ "/mis/template/arPrice.xlsx")), headers,
					HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}



