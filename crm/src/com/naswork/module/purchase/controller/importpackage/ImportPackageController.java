package com.naswork.module.purchase.controller.importpackage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.net.aso.c;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.controller.BaseController;
import com.naswork.dao.Jbpm4JbyjDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientOrderElement;
import com.naswork.model.Element;
import com.naswork.model.ExportPackage;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.ImportPackagePaymentElementPrepare;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.MpiMessage;
import com.naswork.model.OnPassageStorage;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierFileRelation;
import com.naswork.model.SupplierImportElement;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierPayment;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.model.TPart;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.module.marketing.controller.clientorder.EmailVo;
import com.naswork.module.marketing.controller.clientquote.ProfitStatementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.service.AuthorityRelationService;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientQuoteElementService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ElementService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.ExportPackageService;
import com.naswork.service.FileService;
import com.naswork.service.GyExcelService;
import com.naswork.service.GyFjService;
import com.naswork.service.ImportPackagePaymentElementPrepareService;
import com.naswork.service.ImportPackagePaymentElementService;
import com.naswork.service.ImportPackagePaymentService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.ImportStorageLocationListService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.MpiService;
import com.naswork.service.OnPassageStroageService;
import com.naswork.service.OrderApprovalService;
import com.naswork.service.SupplierFileRelationService;
import com.naswork.service.SupplierImportElementService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SupplierPaymenService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.service.SupplierService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.TPartService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;
import com.thoughtworks.xstream.alias.ClassMapper.Null;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/importpackage")
public class ImportPackageController extends BaseController{
	@Resource
	private GyFjService gyFjService;
	@Resource
	private ImportPackageService importPackageService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private ImportPackagePaymentService importPackagePaymentService;
	@Resource
	private ElementService elementService;
	@Resource
	private SupplierImportElementService supplierImportElementService;
	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private ClientQuoteElementService clientQuoteElementService;
	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private ExportPackageService exportPackageService;
	@Resource
	private ExportPackageElementService exportPackageElementService;
	@Resource
	private ImportStorageLocationListService importStorageLocationListService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private SupplierPaymenService supplierPaymenService;
	@Resource
	private UserService userService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private ImportPackagePaymentElementPrepareService importPackagePaymentElementPrepareService;
	@Resource
	private ImportPackagePaymentElementService importPackagePaymentElementService;
	@Resource
	protected GyExcelService gyExcelService;
	@Resource
	private OnPassageStroageService onPassageStroageService;
	@Resource
	private AuthorityRelationService authorityRelationService;
	@Resource
	private FileService fileService;
	@Resource
	private OrderApprovalService orderApprovalService;
	@Resource
	private SupplierFileRelationService supplierFileRelationService;
	@Resource
	private TPartService tPartService;
	@Resource
	private Jbpm4JbyjDao jbyjDao;
	@Resource
	private MpiService mpiService;
	
	/**
	 * 列表页面
	 * **/
	@RequestMapping(value="/importpackagelist",method=RequestMethod.GET)
	public String supplierquotelist(){
		return "/purchase/importpackage/importpackagelist";
	}
	
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/importpackagedate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo importpackagedate(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ImportPackageListVo importPackageListVo){
		PageModel<ImportPackageListVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		String orderNumber = getString(request, "orderNumber");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")&& roleVo.getRoleName().indexOf("物流") < 0&& !roleVo.getRoleName().equals("销售")) {
			page.put("userId", user.getUserId());
			if(null!=searchString&&!searchString.equals("")){
				searchString="ar.USER_ID ="+userId+" and "+searchString;
			}
			else{
				searchString="ar.USER_ID ="+userId;
			}
		}
		if (orderNumber != null && !"".equals(orderNumber)) {
			page.put("orderNumber", orderNumber);
			if (searchString != null && !"".equals(searchString)) {
				searchString = searchString + "and (co.ORDER_NUMBER like '%"+orderNumber+"%' or so.ORDER_NUMBER like '%"+orderNumber+"%')";
			}else {
				searchString = "co.ORDER_NUMBER like '%"+orderNumber+"%' or so.ORDER_NUMBER like '%"+orderNumber+"%'";
			}
		}
		importPackageService.findListDatePage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportPackageListVo importPackageListvo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(importPackageListvo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		String exportModel = getString(request, "exportModel");
		List<StorageDetailVo> list = importpackageElementService.getHasLifeStorage();
		if (StringUtils.isNotEmpty(exportModel) && list.size() > 0) {
			exportModel ="[{\"name\":\"入库单号\",\"width\":120,\"align\":0,\"property\":\"importNumber\"},"
					  +"{\"name\":\"客户订单号\",\"width\":120,\"align\":0,\"property\":\"clientOrderNumber\"},"
					  +"{\"name\":\"供应商订单号\",\"width\":120,\"align\":0,\"property\":\"orderNumber\"},"
		              +"{\"name\":\"件号\",\"width\":120,\"align\":0,\"property\":\"partNumber\"},"
		              +"{\"name\":\"描述\",\"width\":120,\"align\":0,\"property\":\"description\"},"
		              +"{\"name\":\"状态\",\"width\":120,\"align\":0,\"property\":\"conditionCode\"},"
		              +"{\"name\":\"证书\",\"width\":120,\"align\":0,\"property\":\"certificationCode\"},"
		              +"{\"name\":\"位置\",\"width\":120,\"align\":0,\"property\":\"location\"},"
		              +"{\"name\":\"生产时间\",\"width\":120,\"align\":0,\"property\":\"manufactureDate\"},"
		              +"{\"name\":\"过期时间\",\"width\":120,\"align\":0,\"property\":\"expireDate\"},"
					  +"{\"name\":\"剩余寿命(%)\",\"width\":120,\"align\":0,\"property\":\"restLife\"}]";
			try {
				List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
				for (StorageDetailVo storageDetailVo : list) {
					Map<String, Object> map = EntityUtil.entityToTableMap(storageDetailVo);
					mapList.add(map);
				}
				exportService.exportGridToXls("库存内时寿件信息", exportModel, mapList, response);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("导出数据出错!", e);
			}
		}
		
		return jqgrid;
	}
	
	/**
	 * 明细列表页面
	 * **/
	@RequestMapping(value="/importpackageelement",method=RequestMethod.GET)
	public String importpackageelement(HttpServletRequest request){
		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("supplierCode", request.getParameter("supplierCode"));
		request.setAttribute("importNumber", request.getParameter("importNumber"));
		request.setAttribute("supplierId", request.getParameter("supplierId"));
		return "/purchase/importpackage/importpackagelistelement";
	}
	
	/**
	 * 选中件号生成合格证页面
	 * **/
	@RequestMapping(value="/partCertification",method=RequestMethod.GET)
	public String partCertification(HttpServletRequest request){
		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("supplierCode", request.getParameter("supplierCode"));
		request.setAttribute("importNumber", request.getParameter("importNumber"));
		request.setAttribute("supplierId", request.getParameter("supplierId"));
		return "/purchase/importpackage/partcertification";
	}
	
	/**
	 * 未换位置的件号生成合格证
	 * **/
	@RequestMapping(value="/unchangecertification",method=RequestMethod.GET)
	public String unchangecertification(HttpServletRequest request){
		return "/purchase/importpackage/unchangecertification";
	}
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/importpackageelementdate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo importpackageelementdate(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ImportPackageElementVo elementVo){
		PageModel<ImportPackageElementVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		String id=request.getParameter("id");
		page.put("id", id);
		importpackageElementService.findElementListDatePage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportPackageElementVo importPackageElementVo : page.getEntities()) {
				if(null!=importPackageElementVo.getIpSupplierOrderElementId()){
					SupplierOrder supplierOrder=supplierOrderService.selectBySupplierOrderElementId(importPackageElementVo.getIpSupplierOrderElementId());
					if(null!=supplierOrder){
						importPackageElementVo.setOrderNumber(supplierOrder.getOrderNumber());
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(importPackageElementVo);
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
	 * 修改入库单页面
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/updateimportpackage",method=RequestMethod.GET)
	public String updateimportpackage(HttpServletRequest request) throws UnsupportedEncodingException{
//		request.setAttribute("importNumber", request.getParameter("importNumber"));
//		request.setAttribute("importDate", request.getParameter("importDate"));
//		String remark= request.getParameter("remark");
//		remark = new String(remark.getBytes("iso8859-1"),"UTF-8");
//		request.setAttribute("remark",remark);
//		request.setAttribute("exchangeRate", request.getParameter("exchangeRate"));
//		request.setAttribute("currencyId", request.getParameter("currencyId"));
//		request.setAttribute("updateTimestamp", request.getParameter("updateTimestamp"));
//		String currencyValue= request.getParameter("currencyValue");
//		currencyValue = new String(currencyValue.getBytes("iso8859-1"),"UTF-8");
//		request.setAttribute("currencyValue",currencyValue);
		request.setAttribute("id", request.getParameter("id"));
		List<ImportPackageListVo> importPackage=importPackageService.findImportPackageDate(request.getParameter("id"));
		request.setAttribute("importPackage", importPackage.get(0));
//		request.setAttribute("supplierCode", request.getParameter("supplierCode"));
		return "/purchase/importpackage/updateimportpackage";
	}
	/**
	 * 修改入库单
	 * **/
	@RequestMapping(value="/updateimportpackagedate",  method=RequestMethod.POST)
	public @ResponseBody ResultVo updateimportpackagedate(HttpServletRequest request, @ModelAttribute ImportPackage record)
	{
		boolean result = true;
		String message = "修改完成！";
		ImportPackage importPackage=importPackageService.selectByPrimaryKey(record.getId());
//		String importDate=outputDate((Date) record.getImportDate(),
//				"yyyy-MM-dd");
		if(!record.getImportDate().equals(importPackage.getImportDate())){
			importPackage.setImportDate(record.getImportDate());
			Integer maxseq=importPackageService.findmaxseq(importPackage);
//			Integer seq = importPackageService.findmaxseq(importPackage);
			importPackage.setSeq(maxseq);
			
			String importNumber = generateImportNumber(importPackage);
			record.setImportNumber(importNumber);
		}
		UserVo userVo = getCurrentUser(request);
		record.setLastUpdateUser(new Integer(userVo.getUserId()));
		importPackageService.updateByPrimaryKey(record);
		return new ResultVo(result, message);
	}
	
	/**
	 *新增入库单页面
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/addimportpackage",method=RequestMethod.GET)
	public String addimportpackage(HttpServletRequest request) throws UnsupportedEncodingException{
		request.setAttribute("importDate",new Date());
		return "/purchase/importpackage/addimportpackage";
	}
	
	/**
	 * 新增入库单
	 * **/
	@RequestMapping(value="/addimportpackagedate",  method=RequestMethod.POST)
	public @ResponseBody ResultVo addimportpackagedate(HttpServletRequest request, @ModelAttribute ImportPackage importPackage)
	{
		Integer seq = importPackageService.findmaxseq(importPackage);
		importPackage.setSeq(seq);
		String importNumber = generateImportNumber(importPackage);
		UserVo userVo = getCurrentUser(request);
		importPackage.setImportNumber(importNumber);
		importPackage.setCreateUser(new Integer(userVo.getUserId()));
		if (importPackage.getImportStatus() == null && "".equals(importPackage.getImportStatus())) {
			importPackage.setImportStatus(0);
		}
		importPackageService.insert(importPackage);
		boolean result = true;
		String message = "新增完成！";
		return new ResultVo(result, message);
	}
	
	/**
	 *新增付款页面
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/addimportpackagepayment",method=RequestMethod.GET)
	public String addimportpackagepayment(HttpServletRequest request) throws UnsupportedEncodingException{
		request.setAttribute("importNumber",request.getParameter("importNumber"));
		request.setAttribute("id",request.getParameter("id"));
		return "/purchase/importpackage/addimportpackagepayment";
	}
	
	/**
	 *打印跳转
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
	  String clientOrderElementId=request.getParameter("clientOrderId");
//     第一个判断件号或箱子，locationid,elementid;        
   ImportStorageLocationList  list=importStorageLocationListService.selectByLocation(location);
   String sourceNumber="";
   List<ImportStorageLocationList> importStorageLocationLists=importStorageLocationListService.selectSourceNumber(location);
   if(null!=importStorageLocationLists&&importStorageLocationLists.size()>0){
	   for (ImportStorageLocationList importStorageLocationList : importStorageLocationLists) {
		   sourceNumber+=importStorageLocationList.getSourceNumber()+"<br>";
	}
   }else if(clientCode.trim().equals("313")||clientCode.trim().equals("320")){
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
	 *打印跳转
	 * @throws UnsupportedEncodingException 
	 * @throws ParseException 
	 * **/
	/*@RequestMapping(value="/partiframes",method=RequestMethod.GET)
	public String partiframes(HttpServletRequest request) throws UnsupportedEncodingException{
	String elementId=request.getParameter("elementId");
	String sign="0";
	String ipeId=request.getParameter("ipeId");
	String type=request.getParameter("type");
	String printAmount=request.getParameter("pamount");
	String amount=request.getParameter("amount");
	if(null!=request.getParameter("partNumber")){
	String partnumber=request.getParameter("partNumber").replace("%27", "'");
	}
	String printPartNumber="";
	if(null!=request.getParameter("ppart")){
	 printPartNumber=request.getParameter("ppart").replace("%27", "'");
	}
	String printDescription="";
	if(null!=request.getParameter("pdesc")){
		printDescription=request.getParameter("pdesc");
	}
	String id=request.getParameter("id").trim();
	String importpackid=request.getParameter("ipid").trim();
	String location=request.getParameter("sl");
	if(null!=location&&!"".equals(location)){
		location=	new String(location.getBytes("iso8859-1"),"UTF-8");
	}
	List<SupplierOrderElementVo> data=importpackageElementService.findsupplierorderDate(Integer.parseInt(id));
	 data.get(0).setLocation(location);
		if(!id.equals("0")&&!id.equals("-1")&&null==type){
			Integer importPackageSign=importpackageElementService.findImportPackageSign(Integer.parseInt(id));
			importPackageSign=importPackageSign+1;
				sign=importPackageSign.toString();
		}else if(type.trim().equals("update")&&null!=ipeId&&!"".equals(ipeId)){
			ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(ipeId.trim());
			if(null!=importPackageElementVo){
				if(null!=importPackageElementVo.getImportPackageSign()){
					sign=importPackageElementVo.getImportPackageSign().toString();
				}
				if(null==location||"".equals(location)){
					 data.get(0).setLocation(importPackageElementVo.getLocation());
				}
			}
		
		}
	if(null!=printAmount&&!"0".equals(printAmount.trim())){
		 data.get(0).setQuoteAmount(Double.parseDouble(printAmount));
	}else{
		 data.get(0).setQuoteAmount(Double.parseDouble(amount));
	}
	if(data.get(0).getCsn().equals(0)){
		data.get(0).setCsn(data.get(0).getItem());
	}
	if(null!=printPartNumber&&!"".equals(printPartNumber)){
		printPartNumber=	new String(printPartNumber.getBytes("iso8859-1"),"UTF-8");
	data.get(0).setQuotePartNumber(printPartNumber);
	}
	if(null!=printDescription&&!"".equals(printDescription)&&!printDescription.trim().equals("undefined")){
		printDescription=	new String(printDescription.getBytes("iso8859-1"),"UTF-8");
	data.get(0).setQuoteDescription(printDescription);
	}
	data.get(0).setManufactureDate("N/A");
	String manDate=request.getParameter("mdate");
	String inDate=request.getParameter("idate");
	if(null!=inDate&&!"".equals(inDate)&&!"undefined".equals(inDate.trim())){ 
		data.get(0).setManufactureDate(inDate);
	}else if(null!=manDate&&!"".equals(manDate)&&!"undefined".equals(manDate.trim())){
		data.get(0).setManufactureDate(manDate);
	}

	data.get(0).setExpiryDate("N/A");
	request.setAttribute("data", data.get(0));
	  String y=importpackid;  
	  String x= data.get(0).getClientOrderElementId().toString();
//	  Random rm = new Random();  
//		 double pross = (1 + rm.nextDouble()) * Math.pow(10, 5); 
//		 double pross2 = (1 + rm.nextDouble()) * Math.pow(10, 5); 
//		 String fixLenthString = String.valueOf(pross);  
//		
//		  fixLenthString = String.valueOf(pross2);  
//		  String z=fixLenthString.substring(1, 5 + 1);  
//	  String y= data.get(0).getCsn().toString();
   //  第一个判断件号或箱子，locationid,elementid;  
		  if(x.length()<7){
			  for (int i = 0; i < x.length(); i++) {
					 Integer xl=x.length();
					 if(!xl.equals(7)){
						 x="0"+x;
					 }else{
						 break;
					 }
				}
		  }
		  if(y.length()<7){
				 for (int i = 0; i < y.length(); i++) {
					 Integer yl=y.length();
					 if(!yl.equals(7)){
						 y="0"+y;
					 }else{
						 break;
					 }
				}
		  } 
     String barCode="03"+sign+y+x;
     
		request.setAttribute("barCode",barCode);
		return "/purchase/importpackage/partiframe";
	}*/
	
	@RequestMapping(value="/partiframes",method=RequestMethod.GET)
	public String partiframes(HttpServletRequest request) throws UnsupportedEncodingException, ParseException{
	String elementId=request.getParameter("elementId");
	String sign="0";
	String haveResume="";
	String ipeId=request.getParameter("ipeId");
	String unit=request.getParameter("unit");
	String type=request.getParameter("type");
	String printAmount=request.getParameter("pamount");
	String amount=request.getParameter("amount");
	String shelflife=request.getParameter("shelfLife");
	String sourceNumber=request.getParameter("sourceNumber");
	
	
	if(null!=request.getParameter("partNumber")){
	String partnumber=request.getParameter("partNumber").replace("%27", "'");
	}
	String printPartNumber="";
	if(null!=request.getParameter("ppart")){
	 printPartNumber=request.getParameter("ppart").replace("%27", "'");
	}
	String printDescription="";
	if(null!=request.getParameter("pdesc")){
		printDescription=request.getParameter("pdesc");
	}
	String id=request.getParameter("id").trim();
	String importpackid=request.getParameter("ipid");
	importpackid = importpackid.trim();
	String location=request.getParameter("sl");
	if(null!=location&&!"".equals(location)){
		location=	new String(location.getBytes("iso8859-1"),"UTF-8");
	}
	
	List<SupplierOrderElementVo> data=importpackageElementService.findsupplierorderDate(Integer.parseInt(id));
	String sn="";
	if (unit != null && !"".equals(unit)) {
		data.get(0).setQuoteUnit(unit);
	}
	if(null!=request.getParameter("sn")&&!"".equals(request.getParameter("sn"))&&!request.getParameter("sn").trim().equals("undefined")){
		sn=request.getParameter("sn");
		sn=	new String(sn.getBytes("iso8859-1"),"UTF-8");
	data.get(0).setSerialNumber(sn);
	}
	int l=sn.length();
	 data.get(0).setLocation(location);
		if(!id.equals("0")&&!id.equals("-1")&&null==type){
			String resume=request.getParameter("resume").trim();
			String complianceCertificate=request.getParameter("complianceCertificate").trim();
			if(complianceCertificate.equals("301")){
				haveResume="please refer to log book for details";
			}
			Integer importPackageSign=importpackageElementService.findImportPackageSign(Integer.parseInt(id));
			importPackageSign=importPackageSign+1;
				sign=importPackageSign.toString();
		}else if(type.trim().equals("update")&&null!=ipeId&&!"".equals(ipeId)){
			ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(ipeId.trim());
			if(importPackageElementVo.getComplianceCertificate().toString().equals("301")){
				haveResume="please refer to log book for details";
			}
			if(null!=importPackageElementVo){
				if(null!=importPackageElementVo.getImportPackageSign()){
					sign=importPackageElementVo.getImportPackageSign().toString();
				}
				if(null==location||"".equals(location)){
					 data.get(0).setLocation(importPackageElementVo.getLocation());
				}
				if(null!=importPackageElementVo.getSerialNumber()&&sn.equals("")){
					sn=importPackageElementVo.getSerialNumber();
					data.get(0).setSerialNumber(sn);
				}
			}
		}
		
		if(sn.length()>=30){
			data.get(0).setSerialNumber("See Page 2");
			request.setAttribute("hide", "onehide");
			request.setAttribute("sn", sn);
		}else if("".equals(sn)){
			request.setAttribute("hide", "allhide");
		}else if(!"".equals(sn)){
			request.setAttribute("hide", "onehide");
		}
		
	if(null!=printAmount&&!"0".equals(printAmount.trim())){
		 data.get(0).setQuoteAmount(Double.parseDouble(printAmount));
	}else{
		 data.get(0).setQuoteAmount(Double.parseDouble(amount));
	}
	if(data.get(0).getCsn().equals(0)){
		data.get(0).setCsn(data.get(0).getItem());
	}
	if(null!=printPartNumber&&!"".equals(printPartNumber)){
		printPartNumber=	new String(printPartNumber.getBytes("iso8859-1"),"UTF-8");
	data.get(0).setQuotePartNumber(printPartNumber);
	}
	if(null!=printDescription&&!"".equals(printDescription)&&!printDescription.trim().equals("undefined")){
		printDescription=	new String(printDescription.getBytes("iso8859-1"),"UTF-8");
	data.get(0).setQuoteDescription(printDescription);
	}
	data.get(0).setManufactureDate("N/A");
	String manDate=request.getParameter("mdate");
	String inDate=request.getParameter("idate");
	if(null!=inDate&&!"".equals(inDate)&&!"undefined".equals(inDate.trim())){
		data.get(0).setManufactureDate(inDate);
	}else if(null!=manDate&&!"".equals(manDate)&&!"undefined".equals(manDate.trim())){
		data.get(0).setManufactureDate(manDate);
	}

	String expiryDate="";
	if(null!=data.get(0).getBsn() && !"".equals(data.get(0).getBsn())){
		TPart tPart=tPartService.selectByPrimaryKey(data.get(0).getBsn());
		boolean man=false;
		boolean in=false;
		Date day=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		if(null!=inDate&&!"".equals(inDate)&&!"undefined".equals(inDate.trim())){
			in=true;
		}
		if(null!=manDate&&!"".equals(manDate)&&!"undefined".equals(manDate.trim())){
			man=true;
		}
		if(man&&in){
			Date inday=dateFormat.parse(inDate);
			Date manday=dateFormat.parse(manDate);
			if(inday.getTime()>manday.getTime()){ 
				 day=dateFormat.parse(inDate);
			}else if(manday.getTime()>inday.getTime()){
				 day=dateFormat.parse(manDate);
			}else{
				 day=dateFormat.parse(manDate);
			}
		}else if(man){
			 day=dateFormat.parse(manDate);
		}else if(in){
			 day=dateFormat.parse(inDate);
		}
	
		if(man||in){
			GregorianCalendar gc=new GregorianCalendar(); 
			gc.setTime(day);
			if(null!=tPart&&null!=tPart.getShelfLife()&&tPart.getShelfLife()>0){
				Integer shelfLife=tPart.getShelfLife();
				gc.add(5,shelfLife); 
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				Date time=gc.getTime();
				expiryDate=dateFormat.format(time);
			}else if (shelflife != null && !"undefined".equals(shelflife) && !"".equals(shelflife)) {
				if (new Integer(shelflife) > 0) {
					gc.add(5,new Integer(shelflife)); 
					gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
					Date time=gc.getTime();
					expiryDate=dateFormat.format(time);
				}
			}
		}
			
	}else if (ipeId != null) {
		List<TPart> tParts = tPartService.getByImportElementId(new Integer(ipeId));
		if (tParts.size() > 0) {
			boolean man=false;
			boolean in=false;
			Date day=new Date();
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			if(null!=inDate&&!"".equals(inDate)&&!"undefined".equals(inDate.trim())){
				in=true;
			}
			if(null!=manDate&&!"".equals(manDate)&&!"undefined".equals(manDate.trim())){
				man=true;
			}
			if(man&&in){
				Date inday=dateFormat.parse(inDate);
				Date manday=dateFormat.parse(manDate);
				if(inday.getTime()>manday.getTime()){ 
					 day=dateFormat.parse(inDate);
				}else if(manday.getTime()>inday.getTime()){
					 day=dateFormat.parse(manDate);
				}else{
					 day=dateFormat.parse(manDate);
				}
			}else if(man){
				 day=dateFormat.parse(manDate);
			}else if(in){
				 day=dateFormat.parse(inDate);
			}
		
			if(man||in){
				GregorianCalendar gc=new GregorianCalendar(); 
				gc.setTime(day);
				if(null!=tParts.get(0)&&null!=tParts.get(0).getShelfLife()&&tParts.get(0).getShelfLife()>0){
					Integer shelfLife=tParts.get(0).getShelfLife();
					gc.add(5,shelfLife); 
					gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
					Date time=gc.getTime();
					expiryDate=dateFormat.format(time);
				}else if (shelflife != null && !"undefined".equals(shelflife) && !"".equals(shelflife)) {
					if (new Integer(shelflife) > 0) {
						gc.add(5,new Integer(shelflife)); 
						gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
						Date time=gc.getTime();
						expiryDate=dateFormat.format(time);
					}
				}
			}	
		
		}
		
	}
	if(!"".equals(expiryDate) && !"934".equals(data.get(0).getClientCode())){
		data.get(0).setExpiryDate(expiryDate);
	}else{
		data.get(0).setExpiryDate("hide");
	}
	if (sourceNumber != null && !"".equals(sourceNumber) && !"undefined".equals(sourceNumber)) {
		data.get(0).setClientOrderSourceNumber(sourceNumber);
	}
	
	request.setAttribute("data", data.get(0));
	  String y=importpackid;  
	  String x= data.get(0).getClientOrderElementId().toString();
//	  Random rm = new Random();  
//		 double pross = (1 + rm.nextDouble()) * Math.pow(10, 5); 
//		 double pross2 = (1 + rm.nextDouble()) * Math.pow(10, 5); 
//		 String fixLenthString = String.valueOf(pross);  
//		
//		  fixLenthString = String.valueOf(pross2);  
//		  String z=fixLenthString.substring(1, 5 + 1);  
//	  String y= data.get(0).getCsn().toString();
   //  第一个判断件号或箱子，locationid,elementid;  
		  if(x.length()<7){
			  for (int i = 0; i < x.length(); i++) {
					 Integer xl=x.length();
					 if(!xl.equals(7)){
						 x="0"+x;
					 }else{
						 break;
					 }
				}
		  }
		  if(y.length()<7){
				 for (int i = 0; i < y.length(); i++) {
					 Integer yl=y.length();
					 if(!yl.equals(7)){
						 y="0"+y;
					 }else{
						 break;
					 }
				}
		  } 
     String barCode="03"+sign+y+x;
     	request.setAttribute("haveResume", haveResume);
		request.setAttribute("barCode",barCode);
		return "/purchase/importpackage/partiframe3";
	}
	
	/**
	 * 新增付款
	 * **/
	@RequestMapping(value="/addimportpackagepaymentdate",  method=RequestMethod.POST)
	public @ResponseBody ResultVo addimportpackagepaymentdate(HttpServletRequest request, @ModelAttribute SupplierPayment supplierPayment)
	{
		supplierPaymenService.insert(supplierPayment);
		boolean result = true;
		String message = "新增完成！";
		return new ResultVo(result, message);
	}
	
	/**
	 * 新增入库单明细列表
	 * **/
	@RequestMapping(value="/addimportpackageelement",method=RequestMethod.GET)
	public String addimportpackageelement(HttpServletRequest request) throws UnsupportedEncodingException{
//		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
//		int originalNumber =0;
//		Date importDate =null;
		ImportPackageElementVo importPackageElement=new ImportPackageElementVo();
		importPackageElement.setImportNumber(request.getParameter("importNumber"));
//		request.setAttribute("importNumber",request.getParameter("importNumber"));
		request.setAttribute("supplierId",request.getParameter("supplierId"));
		List<ImportPackageListVo> imortpackagedate=importPackageService.findImportPackageDate(request.getParameter("id"));
		for (ImportPackageListVo importPackageListVo : imortpackagedate) {
			 Date importDate  =importPackageListVo.getImportDate();
			 importPackageElement.setCertificationDate(importDate);
			 int originalNumber=importPackageService.getOriginalNumber(importDate);
			 importPackageElement.setOriginalNumber(originalNumber);
		}
//		request.setAttribute("originalNumber",originalNumber);
//		request.setAttribute("certificationDate",format.format(importDate));
		importPackageElement.setImportPackageId(Integer.parseInt(request.getParameter("id")));
		request.setAttribute("importPackageElement", importPackageElement);
		return "/purchase/importpackage/updateimportpackagelement";
	}
	
	/**
	 * 入库存
	 * **/
	@RequestMapping(value="/addStock",method=RequestMethod.GET)
	public String addStock(HttpServletRequest request) throws UnsupportedEncodingException{
		ImportPackageElementVo importPackageElementVo=new ImportPackageElementVo();
		importPackageElementVo.setClientId(Integer.parseInt(request.getParameter("clientId")));
		importPackageElementVo.setSupplierOrderElementId(Integer.parseInt(request.getParameter("supplierOrderElementId")));
		importPackageElementVo.setElementId(Integer.parseInt(request.getParameter("elementId")));
		importPackageElementVo.setImportNumber(request.getParameter("importNumber"));
		SupplierOrderElement supplierOrderElement=supplierOrderElementService.selectByPrimaryKey(Integer.parseInt(request.getParameter("supplierOrderElementId")));
		SupplierQuoteElement supplierQuoteElement=supplierQuoteElementService.selectByPrimaryKey(supplierOrderElement.getSupplierQuoteElementId());
		List<ImportPackageElementVo> importPackageElement=importpackageElementService.findImportpackageData(importPackageElementVo);
		importPackageElement.get(0).setDescription(supplierQuoteElement.getDescription());
		List<ImportPackageListVo> imortpackagedate=importPackageService.findImportPackageDate(request.getParameter("id"));
		for (ImportPackageListVo importPackageListVo : imortpackagedate) {
			 Date importDate  =importPackageListVo.getImportDate();
			 importPackageElement.get(0).setCertificationDate(importDate);
			 int originalNumber=importPackageService.getOriginalNumber(importDate);
			 importPackageElement.get(0).setOriginalNumber(originalNumber);
		}
		importPackageElement.get(0).setCheck("1");
		importPackageElement.get(0).getClientOrderAmount();
		String message=request.getParameter("message");
		importPackageElement.get(0).setAmount(Double.valueOf(message));
		request.setAttribute("importPackageElement", importPackageElement.get(0));
		request.setAttribute("clientId", importPackageElement.get(0).getClientId());
		request.setAttribute("clientOrderNumber", importPackageElement.get(0).getClientOrderNumber());
		request.setAttribute("supplierOrderElementId", request.getParameter("supplierOrderElementId"));
		return "/purchase/importpackage/updateimportpackagelement";
	}
	
	/**
	 * 供应商订单明细列表
	 * **/
	@RequestMapping(value="/supplierorderlement",method=RequestMethod.GET)
	public String supplierorderlement(HttpServletRequest request) throws UnsupportedEncodingException{
		request.setAttribute("supplierId",request.getParameter("supplierId"));
		return "/purchase/importpackage/supplierorderelementlist";
	}
	
	/**
	 * 供应商订单明细列表列表数据分页
	 * **/
	@RequestMapping(value="/supplierorderelementdate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierorderelementdate(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierOrderElementVo supplierOrderElementVo){
		PageModel<SupplierOrderElementVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		String Id=request.getParameter("Id");
		page.put("id", Id);
		importpackageElementService.findsupplierorderDatePage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			int id = 0;
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierOrderElementVo supplierOrderElementvo : page.getEntities()) {
				BigDecimal orderAmount =new BigDecimal(supplierOrderElementvo.getSupplierOrderAmount());
				BigDecimal importAmount =new BigDecimal(supplierOrderElementvo.getImportAmount());
				if (importAmount == null) {
					importAmount = BigDecimal.ZERO;
				}
				BigDecimal amount = BigDecimal.ZERO;
				if (orderAmount.compareTo(importAmount) > 0) {
					amount = orderAmount.subtract(importAmount);
				}
				if (amount.doubleValue() != 0) {
					supplierOrderElementvo.setRk("入库");
				}
				if (importAmount.doubleValue() != 0) {
					supplierOrderElementvo.setTh("退货");
				
				}
				List<SupplierOrderElementVo> list = importpackageElementService.selectELementBySoe(supplierOrderElementvo.getId());
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setOnPassageStatus("useOnPassage");
					//在途库存明细
					if (!supplierOrderElementvo.getClientOrderElementId().equals(list.get(i).getClientOrderElementId())) {
						BigDecimal supplierOrderAmount =new BigDecimal(Double.toString(supplierOrderElementvo.getSupplierOrderAmount()));
						BigDecimal onPassageAmount =new BigDecimal(Double.toString(list.get(i).getSupplierOrderAmount()));
						BigDecimal importedAmount =new BigDecimal(Double.toString(supplierOrderElementvo.getImportAmount()));
						supplierOrderElementvo.setSupplierOrderAmount(supplierOrderAmount.subtract(onPassageAmount).doubleValue());
						supplierOrderElementvo.setSupplierOrderTotalPrice(supplierOrderElementvo.getSupplierOrderAmount()*supplierOrderElementvo.getSupplierOrderPrice());
						/*Double Amount = importpackageElementService.selectImportBySoeIdAndCoeId(list.get(i).getId(), list.get(i).getClientOrderElementId());
						if (Amount == null) {
							Amount = new Double(0);
						}
						BigDecimal onPassageImport =new BigDecimal(Double.toString(Amount));
						if (!supplierOrderElementvo.getImportAmount().equals(0.0)) {
							supplierOrderElementvo.setImportAmount(importedAmount.subtract(onPassageImport).doubleValue());
						}*/
						
						//list.get(i).setImportAmount(Amount);
						list.get(i).setKey(++id);
						/*BigDecimal ordAmount =new BigDecimal(list.get(i).getSupplierOrderAmount());
						BigDecimal impAmount =new BigDecimal(list.get(i).getImportAmount());
						if (impAmount == null) {
							impAmount = BigDecimal.ZERO;
						}
						BigDecimal am = BigDecimal.ZERO;
						if (ordAmount.compareTo(importAmount) > 0) {
							am = ordAmount.subtract(importAmount);
						}
						if (am.doubleValue() != 0) {
							list.get(i).setRk("入库");
						}
						if (impAmount.doubleValue() != 0) {
							list.get(i).setTh("退货");
						
						}*/
						Map<String, Object> map = EntityUtil.entityToTableMap(list.get(i));
						mapList.add(map);
					}
					
				}
				if (list.size()>0) {
					supplierOrderElementvo.setOnPassageStatus("onPassageStorage");
				}
				supplierOrderElementvo.setKey(++id);
				Integer supplierOrderELementId=supplierOrderElementService.getSupplierOrderELementId(supplierOrderElementvo.getId());
			 if(null!=supplierOrderELementId){
					if(supplierOrderELementId.equals(supplierOrderElementvo.getId())){
						supplierOrderElementvo.setOrderType("是");
					}else if(!supplierOrderELementId.equals(supplierOrderElementvo.getId())&&!supplierOrderELementId.equals(0)){
						supplierOrderElementvo.setOrderType("否");
						SupplierOrder order=supplierOrderService.selectBySupplierOrderElementId(supplierOrderELementId);
						supplierOrderElementvo.setMainOrder(order.getOrderNumber());
					}
				}
			Double supplierOrderImportAmount= importpackageElementService.findsupplierorderImportAmount(supplierOrderElementvo.getId());
			supplierOrderElementvo.setSupplierOrderImportAmount(supplierOrderImportAmount);
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierOrderElementvo);
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
	 * 新增入库单明细
	 * @throws SQLException 
	 * **/
	@RequestMapping(value="/addimportpackageelementdate",  method=RequestMethod.POST)
	public @ResponseBody ResultVo addimportpackageelementdate(HttpServletRequest request, @ModelAttribute ImportPackageElement importPackageElement) throws SQLException
	{
//		importPackagePaymentService.insert(importPackageElement);
		List<EmailVo> emailVos=new ArrayList<EmailVo>();
		UserVo user = ContextHolder.getCurrentUser();
		boolean result = true;
		String message = "新增完成！";
		List<ImportPackageListVo> imortpackagedate=importPackageService.findImportPackageDate(request.getParameter("importPackageId"));
		String onPassageStatus = request.getParameter("onPassageStatus");
		Integer importPackageId =new Integer(request.getParameter("importPackageId"));
		importPackageElement.setImportPackageId(importPackageId);
		List<ImportPackageListVo> originalNumber=importPackageService.findOriginalNumber(importPackageElement.getOriginalNumber());
		SupplierImportElement supplierImportElement=new SupplierImportElement();
		supplierImportElement.setAmount(importPackageElement.getAmount());
				if(originalNumber.size()>0){
			 result = false;
			 message = "请输入正确的溯源号！";
			 return new ResultVo(result, message);
		}
		Integer supplierOrderElementId =new Integer(request.getParameter("supplierOrderElementId"));
		if(supplierOrderElementId>0){
		Integer clientOrderElementId =new Integer(request.getParameter("clientOrderElementId"));
		importPackageElement.setClientOrderElementId(clientOrderElementId);
		}
		String soeId =request.getParameter("soeid");
		supplierImportElement.setSupplierOrderElementId(supplierOrderElementId);
		Double amount =importPackageElement.getAmount();
		Double price =importPackageElement.getPrice();
		List<SupplierOrderElementVo> soe=importpackageElementService.findsupplierorderDate(supplierOrderElementId);
		Double coamount=0.0;
		Double soamount=0.0;
		if (supplierOrderElementId.intValue() == 0||supplierOrderElementId.intValue() ==-1) {
			String partNumber =importPackageElement.getPartNumber();
			Element element = elementService.findIdByPn(partNumber);
			if (element == null) {
				element =new Element();
				String partNumberCode =elementService.getCodeFromPartNumber(partNumber);
				byte[] p = partNumberCode.getBytes();
            	Byte[] pBytes = new Byte[p.length];
            	for(int j=0;j<p.length;j++){
            		pBytes[j] = Byte.valueOf(p[j]);
            	}
				element.setPartNumberCode(pBytes);
				element.setUpdateTimestamp(new Date());
				elementService.insert(element);
			}
				importPackageElement.setElementId(element.getId());
			
				if(supplierOrderElementId.intValue() ==-1){
					try {
						importpackageElementService.insert(importPackageElement);
						} catch (Exception e) {
							 result = false;
							 message = "新增失败！";
							e.printStackTrace();
						}
						return new ResultVo(result, message);
				}
		}
		else{
			coamount=soe.get(0).getClientOrderAmount();
			soamount=soe.get(0).getSupplierOrderAmount();
			 for (SupplierOrderElementVo supplierOrderElementVo : soe) {
				 importPackageElement.setElementId(supplierOrderElementVo.getElementId());
			}
		}
		
	
		
		Double count=0.0;
		Double stock= soamount-coamount;
		if(supplierOrderElementId.intValue() == 0){
			supplierOrderElementId=Integer.parseInt(soeId);
			try {
				importPackageElement.setImportPackageSign(1);
				importPackageElement.setSupplierOrderElementId(supplierOrderElementId);
				 importpackageElementService.Storage(importPackageElement, imortpackagedate, supplierImportElement);
			} catch (Exception e) {
				 result = false;
				 message = "新增失败！";
				e.printStackTrace();
			}
//			List<OrderApproval> orderApprovals=orderApprovalService.orderApprovalUseOnpassStorage(supplierOrderElementId);
//			if(orderApprovals.size()>0){
//				Double ipamount=amount;
//				for (OrderApproval orderApproval : orderApprovals) {
//					if(ipamount>=orderApproval.getAmount()){
//						ipamount=ipamount-orderApproval.getAmount();
//					}else{
//						orderApproval.setAmount(ipamount);
//					}
//					orderApproval.setSupplierQuoteElementId(importPackageElement.getSupplierQuoteElementId());
//					orderApproval.setImportPackageElementId(importPackageElement.getId());
//					orderApproval.setSupplierOrderElementId(null);
//					orderApprovalService.updateByPrimaryKey(orderApproval);
//				}
//			}
		}else{
			if ("useOnPassage".equals(onPassageStatus)) {
				supplierImportElement = importpackageElementService.onPassageImport(importPackageElement, supplierImportElement);
			}else{
				if(importPackageElement.getAmount()<0){
					List<ImportPackageElement> elements=importpackageElementService.findSign(supplierImportElement.getSupplierOrderElementId());
					Integer sign=0;
					boolean in=false;
					for (ImportPackageElement element : elements) {
					Double sum=	element.getAmount()+importPackageElement.getAmount();
						if(sum==0){
							importPackageElement.setImportPackageSign(element.getImportPackageSign());
							in=true;
							break;
						}else if(sum>0){
							sign=element.getImportPackageSign();
						}
					}
					if(!in){
						importPackageElement.setImportPackageSign(sign);
					}
				}else{
					Integer importPackageSign=importpackageElementService.findImportPackageSign(supplierImportElement.getSupplierOrderElementId());
					importPackageElement.setImportPackageSign(importPackageSign+1);
				}
				importPackageElement.setSupplierOrderElementId(supplierOrderElementId);
				importpackageElementService.insert(importPackageElement);
				supplierImportElement.setImportPackageElementId(importPackageElement.getId());
				supplierImportElementService.insert(supplierImportElement);
				
			}
			
			
			
		 List<ImportPackageElement> importamount=importpackageElementService.findCertStatus(importPackageId);
			if (importamount.size()==4) {
				String ipeId="";
				for (ImportPackageElement importPackageElement2 : importamount) {
					ipeId+=importPackageElement2.getId()+",";
				}
				StringBuffer businessKey = new StringBuffer();
				businessKey.append("import_package_element.id.").append(importPackageId+"-"+ipeId).append(".CertificationExcel");
				try {
					gyExcelService.generateExcel(businessKey.toString());
				} catch (Exception e1) {
					e1.printStackTrace();
					 message = "新增成功！自动生成合格证失败";
					result = false;
				} 
			}
			
//			if(importPackageElement.getAmount()<0){
//				List<ImportPackageElementVo> list=importpackageElementService.findStorageByCoeId(soe.get(0).getClientOrderElementId());
//				if(null!=list.get(0)&&list.size()>0){
//				EmailVo emailVo=new EmailVo();
//				emailVo.setClientOrderNumber(soe.get(0).getClientOrderNumber());
//				emailVo.setSupplierOrderNumber(soe.get(0).getSupplierOrderNumber());
//				emailVo.setPartNumber(importPackageElement.getPartNumber());
//				emailVo.setDescription(importPackageElement.getDescription());
//				emailVos.add(emailVo);
//				importpackageElementService.sendReturnGoodsEmail(emailVos, user.getUserId());
//				}
//			}
			
			if(soamount>coamount){
//			ImportPackageElementVo importPackageElementVo=new ImportPackageElementVo();
//			importPackageElementVo.setElementId(importPackageElement.getElementId());
//			importPackageElementVo.setSupplierOrderElementId(supplierImportElement.getSupplierOrderElementId());
//			 count=importpackageElementService.findStock(importPackageElementVo);
			List<ProfitStatementVo> list=clientQuoteService.findSupplierOrderAmount(soe.get(0).getClientOrderElementId());
			count=list.get(0).getImportAmount();
			}else if(soamount<coamount){
				List<ProfitStatementVo> list=clientQuoteService.findSupplierOrderAmount(soe.get(0).getClientOrderElementId());
				Double orderAmount=list.get(0).getOrderAmount();
				Double ipAmount=list.get(0).getImportAmount();
				Double surplusAmount=orderAmount-ipAmount;
				if(ipAmount.equals(coamount)&&surplusAmount>0){
					int stockamount=(int) (orderAmount-coamount);
					message=stockamount+"";
				}
			}
			
		}
		
		if(soamount>coamount&&count.equals(coamount)){
			if (onPassageStatus != null) {
				if ("onPassageStorage".equals(onPassageStatus)) {
					PageModel<OnPassageStorage> onPassagePage = new PageModel<OnPassageStorage>();
					onPassagePage.put("where", "soe.id = "+supplierOrderElementId);
					SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(supplierOrderElementId);
					List<OnPassageStorage> list = onPassageStroageService.selectOnPassageAmount(onPassagePage);
					if (list.get(0).getAmount()>0) {
						message=list.get(0).getAmount()+"";
					}
					
					onPassageStroageService.updateBySoeIdAndCoeId(supplierOrderElement.getId(), supplierOrderElement.getClientOrderElementId());
				}
			}else {
				stock=soamount-amount;
				Double stockamount= stock;
				message=stockamount+"";
			}
		
			
		}
		
//		importpackageElementService.checkLastPayment(importPackageElement, supplierOrderElementId);
		if (!new Double(0).equals(importPackageElement.getPrice()) && !"YC000".equals(importPackageElement.getLocation())) {
			ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare = new ImportPackagePaymentElementPrepare();
			importPackagePaymentElementPrepare.setAmount(importPackageElement.getAmount());
			//Integer supplierId = supplierOrderElementService.getSupplierId(supplierImportElement.getSupplierOrderElementId());
			Integer supplierId = supplierOrderElementService.getSupplierId(new Integer(soeId));
			importPackagePaymentElementPrepare.setSupplierId(supplierId);
			importPackagePaymentElementPrepare.setSupplierOrderElementId(supplierOrderElementId);
			importPackagePaymentElementPrepare.setImportPackageId(importPackageId);
			importPackagePaymentElementPrepareService.add(importPackagePaymentElementPrepare);
		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 修改入库单明细列表
	 * **/
	@RequestMapping(value="/editimportpackageelement",method=RequestMethod.GET)
	public String editimportpackageelement(HttpServletRequest request) throws UnsupportedEncodingException{
		String importpackageelementid=request.getParameter("id");
		ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(importpackageelementid);
		if (!importPackageElementVo.getClientCode().startsWith("3")) {
			importPackageElementVo.setOrderDescription(importPackageElementVo.getDescription());
		}
		request.setAttribute("importPackageElement", importPackageElementVo);
		request.setAttribute("supplierId",request.getParameter("supplierId"));
		request.setAttribute("type", "edit");
		request.setAttribute("clientOrderNumber", request.getParameter("clientOrderNumber"));
		request.setAttribute("clientId", request.getParameter("clientId"));
		return "/purchase/importpackage/updateimportpackagelement";
	}
	
	/**
	 * 修改入库单明细
	 * **/
	@RequestMapping(value="/editimportpackageelementdate",  method=RequestMethod.POST)
	public @ResponseBody ResultVo editimportpackageelementdate(HttpServletRequest request, @ModelAttribute ImportPackageElement importPackageElement)
	{
		boolean result = true;
		String message = "修改完成！";
		String importpackageelementid=request.getParameter("importPackageId");
		ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(importpackageelementid);
		List<ImportPackageListVo> originalNumber=importPackageService.findOriginalNumber(importPackageElement.getOriginalNumber());
		SupplierImportElement supplierImportElement=new SupplierImportElement();
		
		
		for (ImportPackageListVo importPackageListVo : originalNumber) {
			if(originalNumber.size()>0&&!importPackageElementVo.getId().equals(importPackageListVo.getId())){
				result = false;
				 message = "请输入正确的溯源号！";
				 return new ResultVo(result, message);
			}
		}
		SupplierImportElementVo sie=importpackageElementService.findSupplierimportElement(importPackageElementVo.getId());
		Integer soeId =null;
		if(null==request.getParameter("supplierOrderElementId")){
		soeId=sie.getSupplierOrderElementId();
		}
		else{
		 soeId =new Integer(request.getParameter("supplierOrderElementId"));
		}
		if (sie != null) {
			if(soeId.intValue()!=0){
				AddSupplierOrderElementVo soe=supplierOrderElementService.findByElementId(soeId);
				importPackageElement.setElementId(soe.getElementId());
			}
		}
		ImportPackageElement importPackageElement2 = importpackageElementService.selectByPrimaryKey(importPackageElement.getId());
		if (importPackageElement2.getHasLife() != null && importPackageElement.getHasLife() != null) {
			if ((importPackageElement2.getHasLife().equals(0) && importPackageElement.getHasLife().equals(1)) || 
					(importPackageElement2.getHasLife() == null && importPackageElement.getHasLife().equals(1))
					|| (importPackageElement2.getHasLife().equals(1) && (importPackageElement2.getExpireDate() != importPackageElement.getExpireDate() || importPackageElement2.getManufactureDate() != importPackageElement.getManufactureDate()))) {
				importPackageElement.setRestLifeEmail(0);
			}
		}
		importpackageElementService.updateByPrimaryKey(importPackageElement);
		supplierImportElementService.deleteByPrimaryKey(importPackageElement.getId());
		if(soeId.intValue()!=0){
		supplierImportElement.setImportPackageElementId(importPackageElement.getId());
		supplierImportElement.setSupplierOrderElementId(soeId);
		supplierImportElement.setAmount(importPackageElement.getAmount());
		supplierImportElementService.insert(supplierImportElement);
		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 生成入库单号
	 * **/
	public String generateImportNumber(ImportPackage importPackage) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(outputDate((Date) importPackage.getImportDate(),
				"yyyyMMdd"));
		List<SupplierVo> supplier=importPackageService.findsupplier(importPackage.getSupplierId());
		for (SupplierVo supplierVo : supplier) {
		buffer.append(supplierVo.getCode());
		buffer.append(StringUtils.leftPad(importPackage.getSeq()
				.toString(), 3, '0'));}
		return buffer.toString();
	}
	
	public static String outputDate(Date value, String pattern) {
		return value == null ? "" : FastDateFormat.getInstance(pattern).format(
				value);
	}
	
	/**
	 * 生成出库单号
	 * **/
	public String generateExportNumber(ExportPackage exportPackage) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(outputDate((Date) exportPackage.getExportDate(),
				"yyyyMMdd"));
		List<ClientContact> client=importPackageService.findclient(exportPackage.getClientId());
		for (ClientContact contact : client) {
		buffer.append(contact.getCode());
		buffer.append(StringUtils.leftPad(exportPackage.getSeq()
				.toString(), 3, '0'));}
		return buffer.toString();
	}
	
	
	
	/**
	 * 入库转库存
	 * **/
	@RequestMapping(value="/toalterstoragepage",method=RequestMethod.GET)
	public String toalterstoragepage(HttpServletRequest request) throws UnsupportedEncodingException{
		String importpackageelementid=request.getParameter("id");
		ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(importpackageelementid);
		String storageAmount=request.getParameter("storageAmount");
		if(null!=storageAmount&&!"".equals(storageAmount)){
			Double amount=new Double(storageAmount);
			importPackageElementVo.setAmount(amount);
		}
		request.setAttribute("importPackageElement", importPackageElementVo);
		request.setAttribute("supplierId",request.getParameter("supplierId"));
		return "/purchase/importpackage/alterstorage";
	}
	
	/**
	 * 入库转库存
	 * **/
	@RequestMapping(value="/alterstoragea",  method=RequestMethod.POST)
	public @ResponseBody ResultVo alterstoragea(HttpServletRequest request, @ModelAttribute ImportPackageElement importPackageElement)
	{
		boolean result = true;
		String message = "修改完成！";
		String importpackageelementid=request.getParameter("importPackageId");
		ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(importpackageelementid);
		Double price  =importPackageElementVo.getPrice();//1380.0
		Double originalAmount=importPackageElementVo.getAmount();//30
		Double exportAmount = importPackageElement.getAmount();//30
		int currencyId=importPackageElementVo.getCurrencyId();//10
		Double exchangeRate=importPackageElementVo.getExchangeRate();//1
		if ( exportAmount.compareTo(originalAmount) > 0) {
			 message = "数量不正确！";
			return new ResultVo(result, message);
		}else{
			importPackageElementVo.setAmount(exportAmount);
			try {
				importpackageElementService.alterstoragea(importPackageElementVo, importPackageElement);
			} catch (Exception e) {
				 result = false;
				 message = "修改失败！";
				e.printStackTrace();
			}
			
		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 退货页面
	 * **/
	@RequestMapping(value="/returnGoodsPage",method=RequestMethod.GET)
	public String returnGoodsPage(HttpServletRequest request) throws UnsupportedEncodingException{
		String importpackageelementid=request.getParameter("id");
		ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(importpackageelementid);
		importPackageElementVo.setAmount(-importPackageElementVo.getAmount());
		importPackageElementVo.setRemark("退货");
		request.setAttribute("importPackageElement", importPackageElementVo);
		request.setAttribute("supplierId",request.getParameter("supplierId"));
		return "/purchase/importpackage/alterstorage";
	}
	
	/**
	 * 退货
	 * **/
	@RequestMapping(value="/returnGoods",  method=RequestMethod.POST)
	public @ResponseBody ResultVo returnGoods(HttpServletRequest request, @ModelAttribute ImportPackageElement importPackageElement)
	{
		boolean result = true;
		String message = "退货完成！";
		String importpackageelementid=request.getParameter("importpackageelementid");
		String supplierOrderElementId=request.getParameter("supplierOrderElementId");
		ImportPackageElement element=importpackageElementService.selectByPrimaryKey(Integer.parseInt(importpackageelementid));
		SupplierImportElement supplierImportElement=new SupplierImportElement();
			try {
				List<ImportPackageListVo> imortpackagedate=importPackageService.findImportPackageDate(element.getImportPackageId().toString());
				for (ImportPackageListVo importPackageListVo : imortpackagedate) {
					 Date importDate  =importPackageListVo.getImportDate();
					 importPackageElement.setCertificationDate(importDate);
					 int originalNumber=importPackageService.getOriginalNumber(importDate);
					 importPackageElement.setOriginalNumber(originalNumber);
				}
				element.setOriginalNumber(importPackageElement.getOriginalNumber());
				element.setAmount(importPackageElement.getAmount());
				element.setLocation(importPackageElement.getLocation());
				element.setRemark(importPackageElement.getRemark());
				importpackageElementService.insert(element);
				supplierImportElement.setSupplierOrderElementId(Integer.parseInt(supplierOrderElementId));
				supplierImportElement.setImportPackageElementId(element.getId());
				supplierImportElement.setAmount(element.getAmount());
				supplierImportElementService.insert(supplierImportElement);
			} catch (Exception e) {
				 result = false;
				 message = "修改失败！";
				e.printStackTrace();
			}
			
		
		return new ResultVo(result, message);
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
	

	/**
	 * 位置
	 */
	@RequestMapping(value="/locationList",method=RequestMethod.POST)
	public @ResponseBody ResultVo locationList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String id1=request.getParameter("id");
		List<ImportStorageLocationList> list=importStorageLocationListService.selectAll();
		List<ImportStorageLocationList> arraylist=new ArrayList<ImportStorageLocationList>();
		
		for (int i = 0; i < list.size(); i++) {
			ImportStorageLocationList location=list.get(i);
			Integer id2=location.getId();
//			String value=clientQuoteVo.getCurrency_value();
			if(id1!=null&&id2!=null){
				if(id1.equals(id2+"")){
					
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
		if(null==id1||"".equals(id1)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 位置
	 
	@RequestMapping(value="/location",method=RequestMethod.POST)
	public @ResponseBody ResultVo location(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
//		int item=0;
		String clientId=request.getParameter("clientId");
		String orderNumber=request.getParameter("clientOrderNumber");
//		 List<ImportPackageElementVo> ipeList=importpackageElementService.findByClientIdAndOrdernum(clientId, orderNumber);
//		 if(ipeList.size()==0||null==ipeList){//按客户和订单查询为空时只按客户查
		 List<ImportPackageElementVo>	 ipeList=importpackageElementService.findByClientId(clientId);
//		 }
		 ImportPackageElementVo element=new ImportPackageElementVo();
		 List<ImportPackageElementVo> limList=new ArrayList<ImportPackageElementVo>();
		 List<ImportStorageLocationList> list=importStorageLocationListService.selectAll();
		 //如果没有此客户的或订单的记录，说明没有入过库的记录
		for (ImportStorageLocationList importStorageLocationList : list) {//所有location
			limList=importpackageElementService.findByLocationIm(importStorageLocationList.getLocation());
			if(limList.size()==0||null==limList){//记录为空，此位置可用
				  element=new ImportPackageElementVo();
				element.setLocation(importStorageLocationList.getLocation());
				ipeList.add( element);
				}
			}
		success = true;
		JSONArray json = new JSONArray();
			json.add(ipeList);
		msg =json.toString();
		return new ResultVo(success, msg);
	}*/
	
	/**
	 * 位置
	 */
	@RequestMapping(value="/location",method=RequestMethod.POST)
	public @ResponseBody ResultVo location(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String clientId=request.getParameter("clientId");
		List<ImportPackageElementVo>	 ipeList=importpackageElementService.findByClientId(clientId);
		List<ImportPackageElementVo>	limList=importpackageElementService.findByLocation();
		List<ImportPackageElementVo> restList = importpackageElementService.getRestLocation();
		ipeList.addAll( limList);
		ipeList.addAll(restList);
		success = true;
		JSONArray json = new JSONArray();
	    json.add(ipeList);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	
	/**
	 * 下拉列表数据-证书
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/findcert",method=RequestMethod.POST)
	public @ResponseBody ResultVo findsqstauts(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String supplierQuoteStatusId=request.getParameter("certificationId");
 		List<ListDateVo> list=supplierQuoteService.findcert();
		List<ListDateVo> arraylist=new ArrayList<ListDateVo>();
		
		for (int i = 0; i < list.size(); i++) {
			ListDateVo dateVo=list.get(i);
			Integer id=dateVo.getId();
			if(supplierQuoteStatusId!=null&&id!=null){
				if(supplierQuoteStatusId.equals(id+"")){
					arraylist.add(list.get(i));
					for (int j = 0; j < list.size(); j++) {
						if(j!=i){
						arraylist.add(list.get(j));
						}
					}
					
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==supplierQuoteStatusId){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 下拉列表数据-状态
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/findcond",method=RequestMethod.POST)
	public @ResponseBody ResultVo findcond(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String conditionId=request.getParameter("conditionId");
		List<ListDateVo> list=supplierQuoteService.findcond();
		List<ListDateVo> arraylist=new ArrayList<ListDateVo>();
		
		for (int i = 0; i < list.size(); i++) {
			ListDateVo dateVo=list.get(i);
			Integer id=dateVo.getId();
			if(conditionId!=null&&id!=null){
				if(conditionId.equals(id+"")){
					arraylist.add(list.get(i));
					for (int j = 0; j < list.size(); j++) {
						if(j!=i){
						arraylist.add(list.get(j));
					}
					}	
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==conditionId||"".equals(conditionId)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	
	/**
	 * 库存流水列表页面
	 * **/
	@RequestMapping(value="/storageflowlist",method=RequestMethod.GET)
	public String storageflowlist(){
		return "/purchase/importpackage/storageflowlist";
	}
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/storageflowlistdate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo storageflowlistdate(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ImportPackageListVo importPackageListVo){
		PageModel<StorageFlowVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		String partNumber =request.getParameter("part_number");
		String check =request.getParameter("check");
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal totalPrice = BigDecimal.ZERO;
		BigDecimal amount =BigDecimal.ZERO;
		BigDecimal price =  BigDecimal.ZERO;
		int item=0;
		page.put("partNumber", partNumber);
		page.put("check", check);
		StorageFlowVo total=importpackageElementService.findtotalList(page, searchString);
		importpackageElementService.findstorageFlowPage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StorageFlowVo flowVo : page.getEntities()) {
				
//				if(null!=flowVo.getAmount()){
//					 amount = new BigDecimal(flowVo.getAmount());
//				}
//				if(null!=flowVo.getTotalBasePrice()){
//				 price =  new BigDecimal(flowVo.getTotalBasePrice());
//				}
//				if ("import"
//						.equals(flowVo.getStorageType())) {
//					if (amount != null) {
//						totalAmount = totalAmount.add(amount);
//					}
//					if (price != null) {
//						totalPrice = totalPrice.add(price);
//					}
//				} else {
//					if (amount != null) {
//						totalAmount = totalAmount.subtract(amount);
//					}
//					if (price != null) {
//						totalPrice = totalPrice.subtract(price);
//					}
//				}
				if(null!=total){
				flowVo.setTotalAmount(total.getTotalAmount());
				flowVo.setTotalBasePrice(total.getTotalBasePrice());
				}else{
					flowVo.setTotalAmount(0.0);
					flowVo.setTotalBasePrice(0.0);
				}
				flowVo.setTotal(page.getEntities().size());
				flowVo.setId(item++);
				Map<String, Object> map = EntityUtil.entityToTableMap(flowVo);
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
	 * 物流方式
	 */
	@RequestMapping(value="/logisticsWay",method=RequestMethod.POST)
	public @ResponseBody ResultVo sex(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String phasesid=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findSupplierByType("LOGISTICS_WAY");
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
	 * 按供应商订单号查供应商
	 */
	@RequestMapping(value="/findSupplierByNumber",method=RequestMethod.POST)
	public @ResponseBody ResultVo findSupplierByNumber(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String supplierOrderNumber=request.getParameter("supplierOrderNumber").trim();
		int supplierId=importpackageElementService.findSupplierBySupplierOrderNumber(supplierOrderNumber);
		List<Supplier> list=supplierService.selectAll();
		List<Supplier> arraylist=new ArrayList<Supplier>();
		
		if(0!=supplierId){
		for (int i = 0; i < list.size(); i++) {
			Supplier vo=list.get(i);
			Integer id=vo.getId();
//			String value=clientQuoteVo.getCurrency_value();
				if(supplierId==id){
					Supplier su = new Supplier();
					su.setId(list.get(i).getId());
					su.setCode(list.get(i).getCode());
					arraylist.add(su);
					for (int j = 0; j < list.size(); j++) {
						if(i!=j){
							Supplier sup = new Supplier();
							sup.setId(list.get(i).getId());
							sup.setCode(list.get(i).getCode());
							arraylist.add(sup);
							arraylist.add(sup);
						}
					}
				}
		}
		success = true;
		}else{
			success = false;
		}
		JSONArray json = new JSONArray();
		json.add(arraylist);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 下拉列表数据-供应商
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/findsid",method=RequestMethod.POST)
	public @ResponseBody ResultVo findsid(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		
		List<Supplier> list=supplierService.selectAll();
		List<Supplier> arrayList = new ArrayList<Supplier>();
		for (Supplier supplier : list) {
			Supplier su = new Supplier();
			su.setId(supplier.getId());
			su.setCode(supplier.getCode());
			arrayList.add(su);
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(arrayList);
		msg =json.toString();
		return new ResultVo(success, msg);
		}
	
	/*
	 * 验证locationId
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public @ResponseBody ResultVo check(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		String checkId = getString(request, "checkId");
//		String location = getString(request, "location");
		String location = importStorageLocationListService.selectByPrimaryKey(Integer.parseInt(checkId)).getLocation();
//		if (checkId.equals(locationId)) {
//			message = "匹配!";
//			success = true;
//		}else {
//			message = "位置不匹配!";
//		}
		success = true;
		message=location;
		
		return new ResultVo(success, message);
	}
	
	/**
	'符合性证明
	 */
	@RequestMapping(value="/complianceCertificate",method=RequestMethod.POST)
	public @ResponseBody ResultVo complianceCertificate(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String pid=request.getParameter("id");
		List<SystemCode> list=systemCodeService.findType("COMPLIANCE_CERTIFICATE");
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
	 * 新增付款单页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAddPayment",method=RequestMethod.GET)
	public String toAddPayment(HttpServletRequest request) {
		request.setAttribute("today", new Date());
		return "/purchase/importpackage/addPayment";
	}
	
    /**
     * 新增付款单
     * @param request
     * @param importPackagePayment
     * @return
     */
	@RequestMapping(value="/addPayment",method=RequestMethod.POST)
	public @ResponseBody ResultVo addPayment(HttpServletRequest request,@ModelAttribute ImportPackagePayment importPackagePayment) {
		String message = "";
		boolean success = false;
		if (importPackagePayment.getImportPackageId()!=null) {
			importPackagePaymentService.add(importPackagePayment);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 修改付款单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editPayment",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editPayment(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		try {
		ImportPackagePayment importPackagePayment = new ImportPackagePayment();
		importPackagePayment.setPaymentNumber(getString(request, "paymentNumber"));
		importPackagePayment.setId(new Integer(getString(request, "id")));
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		importPackagePayment.setPaymentDate(sdf.parse(getString(request, "paymentDate")));
		importPackagePayment.setLeadTime(getString(request, "leadTime"));
		importPackagePayment.setRemark(getString(request, "remark"));
		importPackagePayment.setPaymentType(new Integer(getString(request, "paymentType")));
		importPackagePayment.setPaymentStatusId(new Integer(getString(request, "paymentStatusValue")));
		importPackagePaymentService.updateByPrimaryKeySelective(importPackagePayment);
		message ="修改成功！";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "新增失败！";
			success = false;
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 跳转付款页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAddPaymentList",method=RequestMethod.GET)
	public String toAddPaymentList(HttpServletRequest request) {
		request.setAttribute("supplierId", getString(request, "supplierId"));
		return "/purchase/importpackage/paymentList";
	}
	
	/**
	 * 付款列表
	 * **/
	@RequestMapping(value="/paymentList",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo paymentList(HttpServletRequest request){
		PageModel<ImportPackagePayment> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String supplierId =request.getParameter("supplierId");
		page.put("supplierId", supplierId);
		
		importPackagePaymentService.findByImportPackageIdPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportPackagePayment importPackagePayment : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(importPackagePayment);
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
	 * 付款明细页面
	 * **/
	@RequestMapping(value="/paymentElementList",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo paymentElementList(HttpServletRequest request,HttpServletResponse response){
		PageModel<ImportPackagePaymentElement> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String importPackagePaymentId =request.getParameter("importPackagePaymentId");
		page.put("importPackagePaymentId", importPackagePaymentId);
		
		importPackagePaymentElementService.listPage(page);
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportPackagePaymentElement importPackagePaymentElement : page.getEntities()) {
				SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(importPackagePaymentElement.getSupplierOrderElementId());
//				Double total = supplierOrderElement.getPrice()*importPackagePaymentElement.getAmount()*importPackagePaymentElement.getPaymentPercentage()/100;
//				BigDecimal b = new BigDecimal(total);  
//				total = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//				importPackagePaymentElement.setShouldPay(total);
				Jbpm4Jbyj jbpm4Jbyj=jbyjDao.findLastJbrByBusinessKey("IMPORT_PACKAGE_PAYMENT_ELEMENT.ID."+importPackagePaymentElement.getId());
				if(null!=jbpm4Jbyj){
					importPackagePaymentElement.setJbyj(jbpm4Jbyj.getJbyj());
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(importPackagePaymentElement);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("付款单", exportModel, mapList, response);
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
	 * 流程付款明细页面
	 * **/
	@RequestMapping(value="/paymentTaskElementList",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo paymentTaskElementList(HttpServletRequest request){
		PageModel<ImportPackagePaymentElement> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String importPackagePaymentElementId =request.getParameter("ipeeId");
		String taskId=request.getParameter("taskId");
		if(null!=taskId&&!"".equals(taskId)){
			page.put("taskId", taskId);
		}
		page.put("importPackagePaymentElementId", importPackagePaymentElementId);
		
		importPackagePaymentElementService.tasklistPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportPackagePaymentElement importPackagePaymentElement : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(importPackagePaymentElement);
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
	 * 修改付款单明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editPaymentElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editPaymentElement(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		try {
		ImportPackagePaymentElement importPackagePaymentElement = new ImportPackagePaymentElement();
		importPackagePaymentElement.setId(new Integer(getString(request, "id")));
		importPackagePaymentElement.setImportPackagePaymentId(new Integer(getString(request, "importPackagePaymentId")));
		if (getString(request, "paymentSum")!=null && !"".equals(getString(request, "paymentSum"))) {
			importPackagePaymentElement.setPaymentSum(new Double(getString(request, "paymentSum")));
		}
		importPackagePaymentElement.setSupplierOrderElementId(new Integer(getString(request, "supplierOrderElementId")));
		importPackagePaymentElement.setPaymentPercentage(new Double(getString(request, "paymentPercentage")));
		importPackagePaymentElement.setRemark(getString(request, "remark"));
		importPackagePaymentElementService.edit(importPackagePaymentElement);
		message ="修改成功！";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "新增失败！";
			success = false;
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 跳转新增货到付款明细付款页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAddPaymentElement",method=RequestMethod.GET)
	public String toAddPaymentElement(HttpServletRequest request) {
		//Integer supplierId = new Integer(getString(request, "supplierId"));
		Integer importPackageId = new Integer(getString(request, "importPackageId"));
		//ImportPackage importPackage = importPackageService.selectByPrimaryKey(importPackageId);
		//最初版本
		//List<ImportPackagePaymentElementPrepare> list = importPackagePaymentElementPrepareService.selectBySupplierId(importPackageId);
		List<ImportPackagePaymentElementPrepare> list = importpackageElementService.getPaymentList(importPackageId);
		for (int i = 0; i < list.size(); i++) {
			//list.get(i).setAmount(supplierOrderElementService.getImportAmount(list.get(i).getSupplierOrderElementId()));
			List<ImportPackagePaymentElement> paymentList = importPackagePaymentElementService.selectBySupplierOrderElementId(list.get(i).getSupplierOrderElementId());
			for (ImportPackagePaymentElement importPackagePaymentElement : paymentList) {
				BigDecimal a2 = new BigDecimal(Double.toString(100.0));
				BigDecimal b2 = new BigDecimal(Double.toString(importPackagePaymentElement.getPaymentPercentage()));
				Double paymentPercentage = a2.subtract(b2).doubleValue();
				if (importPackagePaymentElement.getAmount().equals(list.get(i).getAmount())) {
					if (paymentPercentage>=0) {
						list.get(i).setPaymentPercentage(paymentPercentage);
					}
					list.get(i).setTotalPrice(list.get(i).getAmount()*list.get(i).getPrice()*list.get(i).getPaymentPercentage()/100);
				}else if (importPackagePaymentElement.getAmount() < list.get(i).getAmount()) {
					BigDecimal a1 = new BigDecimal(Double.toString(list.get(i).getAmount()));
					BigDecimal b1 = new BigDecimal(Double.toString(importPackagePaymentElement.getAmount()));
					Double rest = a1.subtract(b1).doubleValue();
					if (paymentPercentage>0) {
						list.get(i).setPaymentPercentage(paymentPercentage);
					}
					list.get(i).setAmount(rest);
					list.get(i).setPaymentPercentage(new Double(100));
					list.get(i).setTotalPrice(list.get(i).getAmount()*list.get(i).getPrice()*list.get(i).getPaymentPercentage()/100);
					ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare = new ImportPackagePaymentElementPrepare();
					importPackagePaymentElementPrepare.setSupplierOrderElementId(list.get(i).getSupplierOrderElementId());;
					importPackagePaymentElementPrepare.setPrice(list.get(i).getPrice());
					importPackagePaymentElementPrepare.setAmount(importPackagePaymentElement.getAmount());
					importPackagePaymentElementPrepare.setPaymentPercentage(importPackagePaymentElement.getPaymentPercentage());
					importPackagePaymentElementPrepare.setPartNumber(list.get(i).getPartNumber());
					importPackagePaymentElementPrepare.setOrderNumber(list.get(i).getOrderNumber());
					//importPackagePaymentElementPrepare.setTotalPrice(importPackagePaymentElementPrepare.getAmount()*importPackagePaymentElementPrepare.getPrice());
					list.add(importPackagePaymentElementPrepare);
				}
			}
			if (paymentList.size()==0) {
				list.get(i).setPaymentPercentage(new Double(100));
				list.get(i).setTotalPrice(list.get(i).getAmount()*list.get(i).getPrice());
			}
//			SupplierOrder supplierOrder = supplierOrderService.selectBySupplierOrderElementId(list.get(i).getSupplierOrderElementId());
//			list.get(i).setPaymentPercentage(supplierOrder.getShipPayRate());
		}
		request.setAttribute("list", list);
		return "/purchase/importpackage/addPaymentElementTable";
	}
	
	
	/**
	 * 跳转新增预付款和尾款页面明细页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAddPaymentElementBySupplierOrderElementId",method=RequestMethod.GET)
	public String toAddPaymentElementBySupplierOrderElementId(HttpServletRequest request) {
		Integer supplierOrderId = new Integer(getString(request, "supplierOrderId"));
		Integer id = new Integer(getString(request, "id"));
		//ImportPackage importPackage = importPackageService.selectByPrimaryKey(importPackageId);
		List<SupplierOrderElement> list = supplierOrderElementService.selectBySupplierOrderId(supplierOrderId);
		ImportPackagePayment importPackagePayment = importPackagePaymentService.selectByPrimaryKey(id);
		for (int i = 0; i < list.size(); i++) {
			SupplierOrder supplierOrder = supplierOrderService.selectBySupplierOrderElementId(list.get(i).getId());
			if (importPackagePayment.getPaymentType().equals(0)) {
				list.get(i).setPaymentPercentage(supplierOrder.getPrepayRate());
			}else if (importPackagePayment.getPaymentType().equals(1)) {
				list.get(i).setPaymentPercentage(supplierOrder.getShipPayRate());
			}else if (importPackagePayment.getPaymentType().equals(2)) {
				list.get(i).setPaymentPercentage(supplierOrder.getReceivePayRate());
			}
			list.get(i).setTotalPrice(list.get(i).getAmount()*list.get(i).getPrice()*list.get(i).getPaymentPercentage()/100);
			list.get(i).setSupplierOrderElementId(list.get(i).getId());
		}
		request.setAttribute("list", list);
		return "/purchase/importpackage/addPaymentElementTable";
	}
	
	
	/**
	 * 选择付款明细列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toPaymentElement",method=RequestMethod.GET)
	public String toPaymentElement(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		List<ImportPackagePaymentElement> list = importPackagePaymentElementService.elementList(id);
		request.setAttribute("list", list);
		return "/finance/importpayment/addPaymentElementTotalTable";
	}
	
	/**
	 * 批量新增付款金额
	 * @param request
	 * @param importPackagePaymentElementPrepare
	 * @return
	 */
	@RequestMapping(value="/AddPaymentElementTotal",method=RequestMethod.POST)
	public @ResponseBody ResultVo AddPaymentElementTotal(HttpServletRequest request,
			@ModelAttribute ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare) {
		//Integer importPackagePaymentId = new Integer(getString(request, "id"));
		String message = "";
		boolean success = false;
		if (importPackagePaymentElementPrepare.getList().size()>0) {
			importPackagePaymentElementService.addPaymentElemntTotal(importPackagePaymentElementPrepare.getList());
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	
	
	/**
	 * 批量新增付款明细
	 * @param request
	 * @param importPackagePaymentElementPrepare
	 * @return
	 */
	@RequestMapping(value="/AddPaymentElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo AddPaymentElement(HttpServletRequest request,
			@ModelAttribute ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare) {
		Integer importPackagePaymentId = new Integer(getString(request, "id"));
		String message = "";
		boolean success = false;
		if (importPackagePaymentElementPrepare.getList().size()>0) {
			importPackagePaymentElementService.addPaymentElemnt(importPackagePaymentElementPrepare.getList(), importPackagePaymentId);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 检查预付比例是100%的到货情况
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkImportAmount",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkImportAmount(HttpServletRequest request) {
		Integer importPackageId = new Integer(getString(request, "id"));
		UserVo userVo = getCurrentUser(request);
		importPackagePaymentElementPrepareService.checkImportAmount(importPackageId, new Integer(userVo.getUserId()));
		return new ResultVo(true, "");
	}
	
	/**
	 * 查询到期未到货
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkImportLeadTime",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkImportLeadTime(HttpServletRequest request) {
		importPackagePaymentElementPrepareService.checkOverLeadTime();
		return new ResultVo(true, "");
	}
	
	/**
	 * 入库完成
	 * @param request
	 * @return
	 * @author Tanoy
	 */
	@RequestMapping(value="/importComplete",method=RequestMethod.POST)
	public @ResponseBody ResultVo importComplete(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		try {
			UserVo userVo = getCurrentUser(request);
			Integer id = new Integer(getString(request, "id"));
			String supplierId=request.getParameter("supplierId");
			ImportPackage importPackage = importPackageService.selectByPrimaryKey(id);
			importPackage.setImportStatus(1);
			importPackage.setLastUpdateUser(new Integer(userVo.getUserId()));
			importPackageService.updateStatusByPrimaryKey(importPackage);
			//检查异常件发起流程
			List<ImportPackageElement> elementVos=importpackageElementService.findAbnormalPart(importPackage.getId());
			if(elementVos.size()>0){
				List<AuthorityRelation> authorityRelations=authorityRelationService.selectBySupplierId(Integer.parseInt(supplierId));
				String ids=authorityRelations.get(0).getUserId().toString();
				for (int i = 1; i < authorityRelations.size(); i++) {
					ids+=";"+authorityRelations.get(i).getUserId();
				}
				
				importpackageElementService.abnormalPartRequest(elementVos, ids);
				importpackageElementService.abnormalEmail(elementVos, ids, importPackage);
			}
			
			//检查预付100%的入库情况
			importPackagePaymentElementPrepareService.checkImportAmount(id, new Integer(userVo.getUserId()));
			
			//检查这张入库单是不是退货
			List<ImportPackageElementVo> importPackageElementVos=importpackageElementService.findByIpid(id);
			for (ImportPackageElementVo importPackageElementVo : importPackageElementVos) {
				if(importPackageElementVo.getAmount()<0){
					importPackage.setImportStatus(-1);//标志位改为-1代表退货
					break;
				}
			}
			
			//发送入库情况
			boolean emailSuccess = importPackagePaymentElementPrepareService.importCondition(importPackage, new Integer(userVo.getUserId()));
			if (!emailSuccess) {
				return new ResultVo(false, "邮件发送失败!");
			}
			importpackageElementService.missionComplete(importPackage);
			success = true;
			message = "修改成功！";
			return new ResultVo(success, message);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "修改失败！";
			return new ResultVo(success, message);
		}
	}
	



	/**
	 * 异常件附件管理
	 */
	@RequestMapping(value="/partfile",method=RequestMethod.GET)
	public String partfile(HttpServletRequest request) {
		String id = getString(request, "id");
		request.setAttribute("id", id);
		request.setAttribute("tableName", "abnormal_part");
		request.setAttribute("type", request.getParameter("type"));
//		String type=request.getParameter("type");
		return "/purchase/importpackage/fileUpload";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String attachmentlist(HttpServletRequest request) {
		String data = request.getParameter("data");
		Assert.notNull(data);
		request.setAttribute("data", data);
		request.setAttribute("datajson", JsonUtils.urlParam2Json(data));
		PageData pd = new PageData(request);
		if (pd != null) {
			request.setAttribute("pd", JsonUtils.toJson(pd));
		}
		request.setAttribute("type", request.getParameter("type"));
//		String type=request.getParameter("type");
		return "/purchase/importpackage/list";
	}
	
	/**
	 * 异常件号附件数据
	 */
	@RequestMapping(value = "/data", method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo attachmentlistdata(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<GyFj> page = getPage(request);
		
		String businessKey = getString(request, "businessKey");
//		page.put("businessKey", businessKey);
		
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		String type=request.getParameter("type");
		String[] id=type.split("\\?");
		if(id[0].equals("importpackage")){
			String ywid = businessKey.substring(businessKey.lastIndexOf(".") + 1, businessKey.length());
			page.put("importPackageId", ywid);
		}else{
			String ywid = businessKey.substring(businessKey.lastIndexOf(".") + 1, businessKey.length());
			page.put("importPackageElementId",ywid);
		}
		
		JQGridMapVo jqgrid = new JQGridMapVo();
	
		gyFjService.searchPage(page, "", getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (GyFj fj : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(fj);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());
		}

		
		return jqgrid;
	}
	
	
	/**
	 * 异常件审核页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/elementlist",method=RequestMethod.GET)
	public String elementlist(HttpServletRequest request) {
		request.setAttribute("ipeIds", request.getParameter("id"));
		request.setAttribute("taskId", request.getParameter("taskId"));
		return "/purchase/importpackage/elementlist";
	}
	
	/**
	 * 异常件处理页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/abnormalpartelementlist",method=RequestMethod.GET)
	public String returngoodselementlist(HttpServletRequest request) {
		request.setAttribute("ipeIds", request.getParameter("id"));
		String type=request.getParameter("type");
		request.setAttribute("taskId", request.getParameter("taskId"));
		if(type.equals("wlzkc")){
		return "/purchase/importpackage/storageselementlist";
		}else if(type.equals("wlycjzalt")){
			return "/purchase/importpackage/altelementlist";
		}else if(type.equals("wlth")){
			return "/purchase/importpackage/returngoodselementlist";
		}
		return null;
	}
	
	/**
	 * 异常件转库存
	 * **/
	@RequestMapping(value="/importstoragepage",method=RequestMethod.GET)
	public String importstoragepage(HttpServletRequest request) throws UnsupportedEncodingException{
		String importpackageelementid=request.getParameter("id");
		ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(importpackageelementid);
		importPackageElementVo.setRemark("库存");
		request.setAttribute("importPackageElement", importPackageElementVo);
		return "/purchase/importpackage/importstorage";
	}
	
	/**
	 * 异常件转ALT
	 * **/
	@RequestMapping(value="/altimportstoragepage",method=RequestMethod.GET)
	public String altimportstoragepage(HttpServletRequest request) throws UnsupportedEncodingException{
		String importpackageelementid=request.getParameter("id");
		ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(importpackageelementid);
		importPackageElementVo.setRemark("ALT");
		request.setAttribute("supplierOrderNumber", request.getParameter("supplierOrderNumber"));
		request.setAttribute("quotePartNumber", request.getParameter("quotePartNumber"));
		request.setAttribute("clientId", request.getParameter("clientId"));
		request.setAttribute("importPackageElement", importPackageElementVo);
		return "/purchase/importpackage/altimportstorage";
	}
	
	
	/**
	 * 异常件入库存
	 * **/
	@RequestMapping(value="/importstorage",  method=RequestMethod.POST)
	public @ResponseBody ResultVo importstorage(HttpServletRequest request, @ModelAttribute ImportPackageElement importPackageElement)
	{
		boolean result = true;
		String message = "修改完成！";
		String id=request.getParameter("id");
		ImportPackageElementVo importPackageElementVo=importpackageElementService.findimportpackageelement(id);
		List<ImportPackageListVo> imortpackagedate=importPackageService.findImportPackageDate(importPackageElementVo.getImportPackageId().toString());
		SupplierImportElement supplierImportElement=new SupplierImportElement();
			try {
				importPackageElement.setImportPackageId(importPackageElementVo.getImportPackageId());
				String partNumber =importPackageElement.getPartNumber();
				Element element = elementService.findIdByPn(partNumber);
				if (element == null) {
					element =new Element();
					String partNumberCode =elementService.getCodeFromPartNumber(partNumber);
					byte[] p = partNumberCode.getBytes();
	            	Byte[] pBytes = new Byte[p.length];
	            	for(int j=0;j<p.length;j++){
	            		pBytes[j] = Byte.valueOf(p[j]);
	            	}
					element.setPartNumberCode(pBytes);
					element.setUpdateTimestamp(new Date());
					elementService.insert(element);
				}
				importPackageElement.setElementId(element.getId());
				supplierImportElement.setAmount(importPackageElement.getAmount());
				 importpackageElementService.Storage(importPackageElement, imortpackagedate, supplierImportElement);
			} catch (Exception e) {
				 result = false;
				 message = "修改失败！";
				e.printStackTrace();
			}
		return new ResultVo(result, message);
	}
	
	/**
	 * 异常件入ALT
	 * **/
	@RequestMapping(value="/altimportstorage",  method=RequestMethod.POST)
	public @ResponseBody ResultVo altimportstorage(HttpServletRequest request, @ModelAttribute ImportPackageElement importPackageElement)
	{
		boolean result = true;
		String message = "修改完成！";
		String id=request.getParameter("id");
		Integer supplierOrderELementId=Integer.parseInt(request.getParameter("supplierOrderELementId"));
		SupplierImportElement supplierImportElement=new SupplierImportElement();
			try {
				importPackageElement.setId(Integer.parseInt(id));
				importPackageElement.setSupplierOrderElementId(supplierOrderELementId);
				importpackageElementService.updateByPrimaryKey(importPackageElement);
				supplierImportElement.setSupplierOrderElementId(supplierOrderELementId);
				supplierImportElement.setImportPackageElementId(importPackageElement.getId());
				supplierImportElement.setAmount(importPackageElement.getAmount());
				supplierImportElementService.insert(supplierImportElement);
			} catch (Exception e) {
				 result = false;
				 message = "修改失败！";
				e.printStackTrace();
			}
		return new ResultVo(result, message);
	}
	
	/**
	 * 异常件退货
	 * **/
	@RequestMapping(value="/abnormalpartreturnGoods",  method=RequestMethod.POST)
	public @ResponseBody ResultVo abnormalpartreturnGoods(HttpServletRequest request, @ModelAttribute ImportPackageElement importPackageElement)
	{
		boolean result = true;
		String message = "退货完成！";
		String importpackageelementid=request.getParameter("importpackageelementid");
		ImportPackageElement element=importpackageElementService.selectByPrimaryKey(Integer.parseInt(importpackageelementid));
			try {
				element.setAmount(importPackageElement.getAmount());
				element.setLocation(importPackageElement.getLocation());
				element.setRemark(importPackageElement.getRemark());
				importpackageElementService.insert(element);
			} catch (Exception e) {
				 result = false;
				 message = "修改失败！";
				e.printStackTrace();
			}
		
		return new ResultVo(result, message);
	}
	
	/**
	 *  异常件审核页面数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request) {
		PageModel<ImportPackageElement> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String importPackageElementId =request.getParameter("id");
		String taskId=request.getParameter("taskId");
		if(null!=taskId&&!"".equals(taskId)){
			page.put("taskId", taskId);
		}
		page.put("importPackageElementId", importPackageElementId);
		
		importpackageElementService.tasklistPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportPackageElement element : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(element);
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
	 * 跳转库位修改页面
	 * @return
	 */
	@RequestMapping(value="/toChangeLocation",method=RequestMethod.GET)
	public String toChangeLocation(){
		return "/purchase/importpackage/changeLocation";
	}
	
	/**
	 * 修改库位
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveChangeLoaction",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveChangeLoaction(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			String input = getString(request, "inputList");
			String aim = getString(request, "aimLocation");
			if (!"".equals(input) && input != null && !"".equals(aim) && aim != null) {
				importpackageElementService.changeLocation(input, aim);
				message = "修改成功！";
				success = true;
			}else {
				message = "修改失败！";
				success = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "修改异常！";
			success = false;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 跳转未换库位提醒
	 * @return
	 */
	@RequestMapping(value="/toUnchangeLocationPage",method=RequestMethod.GET)
	public String toUnchangeLocationPage(){
		return "/purchase/importpackage/unchangelocationlist";
	}
	
	
	/**
	 *  未换库位提醒数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/unchangeLocationPage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo unchangeLocationPage(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StorageDetailVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = getString(request, "searchString");
		String exportModel = getString(request, "exportModel");
		if (where != null && !"".equals(where)) {
			page.put("where", where);
		}
		importpackageElementService.getUnchangeLocation(page,getSort(request));
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (StorageDetailVo element : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(element);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("未换库位", exportModel, mapList, response);
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
	 *  未换库位提醒数据数量
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/unchangeLocationCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo unchangeLocationCount(HttpServletRequest request,HttpServletResponse response) {
		PageModel<StorageDetailVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String where = getString(request, "searchString");
		String exportModel = getString(request, "exportModel");
		if (where != null && !"".equals(where)) {
			page.put("where", where);
		}
		importpackageElementService.getUnchangeLocation(page,getSort(request));
		String count = String.valueOf(page.getRecordCount());
		
		return new ResultVo(true, count);
	}
	
	/**
	 * 查询入库记录
	 * **/
	/*@RequestMapping(value="/importElementList",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo importElementList(HttpServletRequest request){
		PageModel<ImportPackagePaymentElementPrepare> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String importPackageId =request.getParameter("importPackageId");
		page.put("importPackageId", importPackageId);
		
		importPackagePaymentElementPrepareService.selectByImportIdPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ImportPackagePaymentElementPrepare importPackagePaymentElementPrepare : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(importPackagePaymentElementPrepare);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
	
		return jqgrid;
	}*/
	
	
	/*
	 * 查询库存
	 */
	@RequestMapping(value="/storage",method=RequestMethod.POST)
	public @ResponseBody ResultVo storage(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		String id=request.getParameter("supplierQuoteElementId");
//		Integer supplierQuoteElementId=	importpackageElementService.findSupplierQuoteElementId(Integer.parseInt(id));
		StorageFlowVo importPackageElement=	importpackageElementService.findImportPackageElementId(Integer.parseInt(id));
		if(null!=importPackageElement&&importPackageElement.getStorageAmount()>0){
			success = true;
			JSONObject jsonObject=JSONObject.fromObject(importPackageElement);
			JSONArray json = new JSONArray();
			json.add(jsonObject);
			msg=json.toString();
		}
		return new ResultVo(success, msg);
	}
	
	/**
	 * 文件管理
	 */
	@RequestMapping(value="/file",method=RequestMethod.GET)
	public String file(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "import_package");
		request.setAttribute("fileType", "fileType");
		UserVo userVo=ContextHolder.getCurrentUser();
		RoleVo roleVo=userService.getPower(Integer.parseInt(userVo.getUserId()));
		if(!roleVo.getRoleId().equals("0")&&!roleVo.getRoleId().equals("5")&&!roleVo.getRoleId().equals("6")){
			request.setAttribute("roleId", roleVo.getRoleId());
		}
		
		return "/purchase/importpackage/supplierfileupload";
	}
	
	
	/**
	 * 下拉列表数据-附件类型
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/fileType",method=RequestMethod.POST)
	public @ResponseBody ResultVo fileType(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		List<SupplierFileRelation> list=supplierFileRelationService.listData();
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 搜索询价单
	 * @return
	 */
	@RequestMapping(value="/toSearchImport",method=RequestMethod.GET)
	public String toSearchImport(){
		return "/purchase/importpackage/searchImportPackageList";
	}
	
	/*
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			ClientInquiryElement clientInquiryElement) {
		boolean success=false;
		String message="";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = importpackageElementService.uploadExcel(multipartFile);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	
	/**
	 * 监测可用寿命是否小于2/3
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkShelfLife",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkShelfLife(HttpServletRequest request){
		try {
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			String inspectionDate = request.getParameter("inspectionDate");
			String manufactureDate = request.getParameter("manufactureDate");
			String soeId = request.getParameter("soeid");
			Date date = new Date();
			Date aimDate = null;
			if (inspectionDate != "" && inspectionDate != null && manufactureDate != "" && manufactureDate != null) {
				Date inDate = dateFormat.parse(inspectionDate);
				Date manDate = dateFormat.parse(manufactureDate);
				if(inDate.getTime()>manDate.getTime()){ 
					aimDate=inDate;
				}else{
					aimDate=manDate;
				}
			}else if (inspectionDate != "" && inspectionDate != null) {
				Date inDate = dateFormat.parse(inspectionDate);
				aimDate=inDate;
			}else if (manufactureDate != "" && manufactureDate != null) {
				Date manDate = dateFormat.parse(manufactureDate);
				aimDate=manDate;
			}
			if (aimDate != null && soeId != "") {
				Integer shelfLife = supplierOrderElementService.getShelfLifeBySoeId(soeId);
				if (shelfLife != null && shelfLife > 0) {
					Double checkShelfLife = shelfLife.doubleValue()/3;
					Integer day=Integer.parseInt(String.valueOf((date.getTime()-aimDate.getTime())/(24*60*60*1000)));
					if (day >= (checkShelfLife)) {
						return new ResultVo(false, "寿命为"+shelfLife+"天,可使用寿命少于2/3！");
					}
				}
			}
			return new ResultVo(true, "");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(true, "");
		}
	}
	
	
	/**
	 * 跳转批量打印标签页面
	 * @return
	 */
	@RequestMapping(value="/toPrintLotLabel",method=RequestMethod.GET)
	public String toPrintLotLabel(){
		return "/purchase/importpackage/printlotlabel";
	}
	
	/**
	 * 到货附件管理
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/importFile",method=RequestMethod.GET)
	public String importFile(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "import_package_element");
		return "/marketing/clientinquiry/fileUpload";
	}
	
	/**
	 * 检测当前用户是否物流
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkUser",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkUser(HttpServletRequest request) {
		try {
			boolean success = false;
			UserVo userVo = getCurrentUser(request);
			RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
			if (roleVo.getRoleName().indexOf("物流") >= 0 || roleVo.getRoleName().indexOf("管理员") >= 0) {
				success = true;
				return new ResultVo(success, "");
			}else {
				return new ResultVo(success, "只有物流可以操作");
			}
			
		} catch (Exception e) {
			return new ResultVo(false, "监测用户异常");
		}
	}
	
	/**
	 * 根据该位置中的件号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getImportList",method=RequestMethod.POST)
	public @ResponseBody ResultVo getImportList(HttpServletRequest request){
		try {
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			String locationId = request.getParameter("locationId");
			List<ImportPackageElement> list = importpackageElementService.selectByLocationId(new Integer(locationId));
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getInspectionDate() != null) {
					list.get(i).setInspectionDateString(dateFormat.format(list.get(i).getInspectionDate()));
				}
				if (list.get(i).getManufactureDate() != null) {
					list.get(i).setManufactureDateString(dateFormat.format(list.get(i).getManufactureDate()));
				}
			}
			JSONArray json = new JSONArray();
			json.add(list);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "查询不到该位置的入库信息！");
		}
		
	}
	
	
	/**
	 * 获取供应商订单运输方式
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getShipWay",method=RequestMethod.POST)
	public @ResponseBody ResultVo getShipWay(HttpServletRequest request){
		try {
			String orderNumber = getString(request, "orderNumber");
			List<Integer> shipIds = supplierOrderElementService.getShipByOrderNumber(orderNumber);
			List<SystemCode> shipList = systemCodeService.findType("LOGISTICS_WAY");
			List<SystemCode> mpiList = mpiService.getList();
			List<SystemCode> list = new ArrayList<SystemCode>();
			if (shipIds.size() > 0) {
				for (SystemCode systemCode : mpiList) {
					if (systemCode.getId().equals(shipIds.get(0)) ) {
						list.add(systemCode);
					}
				}
				for (SystemCode systemCode : shipList) {
					if (systemCode.getId().equals(shipIds.get(0))) {
						list.add(systemCode);
					}
				}
				for (SystemCode systemCode : mpiList) {
					if (!systemCode.getId().equals(shipIds.get(0)) ) {
						list.add(systemCode);
					}
				}
				for (SystemCode systemCode : shipList) {
					if (!systemCode.getId().equals(shipIds.get(0))) {
						list.add(systemCode);
					}
				}
			}else {
				list.addAll(shipList);
				list.addAll(mpiList);
			}
			JSONArray json = new JSONArray();
			json.add(list);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
		}
	}
	
	/**
	 * 获取供应商订单币种
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCurrency",method=RequestMethod.POST)
	public @ResponseBody ResultVo getCurrency(HttpServletRequest request){
		try {
			String orderNumber = getString(request, "orderNumber");
			SupplierOrder supplierOrder = supplierOrderService.selectByOrderNumber(orderNumber);
			List<SystemCode> currencyList = systemCodeService.findType("CURRENCY");
			List<SystemCode> list = new ArrayList<SystemCode>();
			for (SystemCode systemCode : currencyList) {
				if (systemCode.getId().equals(supplierOrder.getCurrencyId())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : currencyList) {
				if (!systemCode.getId().equals(supplierOrder.getCurrencyId())) {
					list.add(systemCode);
				}
			}
			JSONArray json = new JSONArray();
			json.add(list);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
		}
	}
	
	
	/**
	 * 获取剩余寿命的百分比
	 * @param request
	 * @param importPackageElement
	 * @return
	 */
	@RequestMapping(value="/getRestLifePercent",method=RequestMethod.POST)
	public @ResponseBody ResultVo getRestLifePercent(HttpServletRequest request,@ModelAttribute ImportPackageElement importPackageElement){
		try {
			
			long begin = 0;
			Date beginDate = null;
			if (importPackageElement.getManufactureDate() != null) {
				begin = importPackageElement.getManufactureDate().getTime();
				beginDate = importPackageElement.getManufactureDate();
			}else if (importPackageElement.getInspectionDate() != null) {
				begin = importPackageElement.getInspectionDate().getTime();
				beginDate = importPackageElement.getInspectionDate();
			}
			long end = 0;
			Date expireDate = null;
			if (importPackageElement.getShelfLife() != null && !"".equals(importPackageElement.getShelfLife()) && beginDate != null) {
				GregorianCalendar gc=new GregorianCalendar(); 
				gc.setTime(beginDate); 
				gc.add(5,importPackageElement.getShelfLife()); 
				gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
				expireDate=gc.getTime();
				end = expireDate.getTime();
			}
					//importPackageElement.getExpireDate().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		    String dateString = formatter.format(new Date());  
		    Date currentTime = formatter.parse(dateString);
			long today = currentTime.getTime();
			long allLife = (long)((end - begin)/ (1000 * 60 * 60 *24));
			long restLife = (long)((end - today)/ (1000 * 60 * 60 *24));
			
			double a = (double)restLife/(double)allLife;
			Integer restPercent = (int)(a*100);
			if (restLife < 0 && allLife < 0) {
				restPercent = -restPercent;
			}
			String result = restPercent+","+formatter.format(expireDate);
			return new ResultVo(true, result);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "计算出错！");
		}
	}
	
	
	/**
	 * 下个月剩余寿命百分比低于80%的件号提醒
	 * @param request
	 * @param importPackageElement
	 * @return
	 */
	@RequestMapping(value="/shilfLifeDealLine",method=RequestMethod.POST)
	public @ResponseBody ResultVo shilfLifeDealLine(HttpServletRequest request){
		synchronized(this){
			try {
				List<StorageDetailVo> list = importpackageElementService.getHasLifeStorage();
				List<ImportPackageElement> importElements = new ArrayList<ImportPackageElement>(); 
				for (StorageDetailVo storageDetailVo : list) {
					ImportPackageElement importPackageElement = importpackageElementService.selectByPrimaryKey(storageDetailVo.getId());
					if (importPackageElement.getManufactureDate() != null && importPackageElement.getExpireDate() != null) {
						long begin = importPackageElement.getManufactureDate().getTime();
						long end = importPackageElement.getExpireDate().getTime();
						
						//现在的剩余寿命
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
					    String dateString = formatter.format(new Date());  
					    Date currentTime = formatter.parse(dateString);
						long today = currentTime.getTime();
						long allLife = (long)((end - begin)/ (1000 * 60 * 60 *24));
						long restLife = (long)((end - today)/ (1000 * 60 * 60 *24));
						double a = (double)restLife/(double)allLife;
						Integer restPercent = (int)(a*100);
						if (restLife < 0 && allLife < 0) {
							restPercent = -restPercent;
						}
						importPackageElement.setRestLife(restPercent);
						importpackageElementService.updateByPrimaryKey(importPackageElement);
						
						//一个月后的剩余寿命
						GregorianCalendar gc=new GregorianCalendar(); 
						gc.setTime(Calendar.getInstance().getTime()); 
						gc.add(5,30); //一个月后的日期
						gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
						Date monthLater=gc.getTime();
						long nextMonth = monthLater.getTime();
						long restLifeNextMonth = (long)((end - nextMonth)/ (1000 * 60 * 60 *24));
						double b = (double)restLifeNextMonth/(double)allLife;
						Integer restPercentNextMonth = (int)(b*100);
						if (restPercentNextMonth < 0 && allLife < 0) {
							restPercentNextMonth = -restPercentNextMonth;
						}
						if (restPercentNextMonth < 75 && storageDetailVo.getRestLifeEmail().equals(0)) {
							importPackageElement.setSupplierOrderNumber(storageDetailVo.getOrderNumber());
							importPackageElement.setClientOrderNumber(storageDetailVo.getClientOrderNumber());
							importPackageElement.setLocation(storageDetailVo.getLocation());
							importPackageElement.setRestLifeNextMonth(restPercentNextMonth);
							importElements.add(importPackageElement);
						}
					}
				}
				if (importElements.size() > 0) {
					importpackageElementService.sendEmail(importElements);
				}
				return new ResultVo(true, "");
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultVo(false, "异常！");
			}
		}
	}
	
	
	/**
	 * 检查是否有时寿件的记录可导出
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkRecord",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkRecord(HttpServletRequest request){
		try {
			List<StorageDetailVo> list = importpackageElementService.getHasLifeStorage();
			if (list.size() == 0) {
				return new ResultVo(true, "当前没有记录可以导出！");
			}else {
				return new ResultVo(false, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "当前没有记录可以导出！");
		}
	}
	
	/**
	 * 获取全部位置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAllLocation",method=RequestMethod.POST)
	public @ResponseBody ResultVo getAllLocation(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		 List<ImportPackageElementVo>	 ipeList=importpackageElementService.getLocationInUse();
		 List<ImportPackageElementVo>	limList=importpackageElementService.findByLocation();
				ipeList.addAll( limList);
		success = true;
		JSONArray json = new JSONArray();
			json.add(ipeList);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	
	/**
	 * 批量修改库位
	 * @return
	 */
	@RequestMapping(value="/toChangeLocationForLot",method=RequestMethod.GET)
	public String toChangeLocationForLot(){
		return "/storage/storagedetail/selectLocation";
	}
	
	/**
	 * 批量修改库位
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateLocationForLot",method=RequestMethod.POST)
	public @ResponseBody ResultVo updateLocationForLot(HttpServletRequest request){
		try {
			String ids = getString(request, "importPackageElementIds");
			String location = getString(request, "location");
			String[] elementIds = ids.split(",");
			ResultVo resultVo = importpackageElementService.updateLocation(elementIds, location);
			return resultVo;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
	}
}
