package com.naswork.module.purchase.controller.supplierquote;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.naswork.common.constants.FileConstant;
import com.naswork.common.controller.BaseController;
import com.naswork.dao.ClientInquiryElementDao;
import com.naswork.dao.ClientQuoteDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElementUpload;
import com.naswork.model.HighPriceCrawlQuote;
import com.naswork.model.SatairQuoteElement;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierQuoteElementUpload;
import com.naswork.model.SystemCode;
import com.naswork.model.VasStroge;
import com.naswork.model.WarnMessage;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.service.AviallQuoteService;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.CompetitorSupplierService;
import com.naswork.service.DasiService;
import com.naswork.service.GyFjService;
import com.naswork.service.HighPriceCrawlQuoteService;
import com.naswork.service.KapcoQuoteService;
import com.naswork.service.KlxQuoteElementService;
import com.naswork.service.SatairQuoteService;
import com.naswork.service.SupplierInquiryElementService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.SupplierQuoteElementUploadService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.service.SupplierService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.TxtavQuoteService;
import com.naswork.service.UserService;
import com.naswork.service.VasStrogeService;
import com.naswork.service.WarnMessageService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.ColumnVo;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONArray;
@Controller
@RequestMapping("/supplierquote")
public class SupplierQuoteController extends BaseController{

	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private ClientQuoteDao clientQuoteDao;
	@Resource
	private SupplierQuoteElementService supplierQuoteElemenetService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private SupplierQuoteElementUploadService supplierQuoteElementUploadService;
	@Resource
	private UserService userService;
	@Resource
	private WarnMessageService warnMessageService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private SatairQuoteService satairQuoteService;
	@Resource
	private GyFjService gyFjService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private SupplierInquiryElementService supplierInquiryElementService;
	@Resource
	private AviallQuoteService aviallQuoteService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private KlxQuoteElementService klxQuoteElementService;
	@Resource
	private KapcoQuoteService kapcoQuoteService;
	@Resource
	private TxtavQuoteService txtavQuoteService;
	@Resource
	private VasStrogeService vasStrogeService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	@Resource
	private HighPriceCrawlQuoteService highPriceCrawlQuoteService;
	@Resource
	private CompetitorSupplierService competitorSupplierService;
	@Resource
	private DasiService dasiService;
	
	
	/**
	 * 列表页面
	 * **/
	@RequestMapping(value="/supplierquotelist",method=RequestMethod.GET)
	public String supplierquotelist(){
		return "/purchase/supplierquote/supplierquotelist";
	}
	
	/**
	 * 供应商到货清单列表页面
	 * **/
	@RequestMapping(value="/supplierarrivallist",method=RequestMethod.GET)
	public String supplierarrivallist(){
		return "/purchase/supplierquote/supplierarrivallist";
	}
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/supplierquotedate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo querysupplierquote(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<SupplierQuoteVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
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
		
		supplierQuoteService.findSupplierQuotePage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierQuoteVo supplierQuoteVo : page.getEntities()) {
				List<GyFj> fjs = gyFjService.findByYwid(supplierQuoteVo.getId().toString());
				if (fjs.size() > 0) {
					supplierQuoteVo.setHaveAttachment(1);
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteVo);
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
	 * 明细列表页面
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/supplierquoteelementlist",method=RequestMethod.GET)
	public String supplierquoteelementlist(HttpServletRequest request) throws UnsupportedEncodingException{
		request.setAttribute("Id", request.getParameter("Id"));
		request.setAttribute("supplierInquiryQuoteNumber", request.getParameter("supplierInquiryQuoteNumber"));
		request.setAttribute("clientInquiryQuoteNumber", request.getParameter("clientInquiryQuoteNumber"));
//		String currencyValue= request.getParameter("currencyValue");
//		currencyValue = new String(currencyValue.getBytes("iso8859-1"),"UTF-8");
//		request.setAttribute("currencyValue", currencyValue);
		request.setAttribute("supplierInquiryId", request.getParameter("supplierInquiryId"));
		SupplierQuote supplierQuoteElement=supplierQuoteService.selectByPrimaryKey(Integer.parseInt(request.getParameter("Id")));
		request.setAttribute("supplierQuoteElement", supplierQuoteElement);
		request.setAttribute("supplierCode", request.getParameter("supplierCode"));
		return "/purchase/supplierquote/supplierquoteelementlist";
	}
	
	/**
	 * 明细列表数据分页
	 * **/
	@RequestMapping(value="/querysupplierquoteelement",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo querysupplierquoteelement(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<SupplierQuoteVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String Id =request.getParameter("Id");
		String searchString ="";
		page.put("Id", Id);
		supplierQuoteService.findSupplierQuoteElementPage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierQuoteVo supplierQuoteVo : page.getEntities()) {
				Double latestPrice = supplierQuoteElemenetService.getLatestPrice(supplierQuoteVo.getQuotePartNumber());
				supplierQuoteVo.setLatestPrice(latestPrice);
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteVo);
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
	 * 少量新增明细
	 */
	@RequestMapping(value="/toAddElement",method=RequestMethod.GET)
	public String toAddElement(HttpServletRequest request) {
		String supplierQuoteId = getString(request, "id");
		request.setAttribute("id", getString(request, "id"));
		String supplierInquiryId=request.getParameter("supplierInquiryId");
		SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(new Integer(supplierQuoteId));
		List<SupplierInquiryEmelentVo> list=supplierQuoteElemenetService.findsupplierinquiryelement(supplierInquiryId,request.getParameter("id").trim());
		List<SupplierQuoteVo> sqelist=supplierQuoteElemenetService.findSupplierQuoteElement(Integer.parseInt(request.getParameter("id").trim()));
		for (SupplierQuoteVo supplierQuoteVo : sqelist) {
		for (int i = 0; i < list.size(); i++) {
				if(supplierQuoteVo.getItem().equals(list.get(i).getItem())){
					list.remove(i);
					break;
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			if (supplierQuote.getBankCost() != null) {
				list.get(i).setBankCost(supplierQuote.getBankCost());
			}
			if (supplierQuote.getFeeForExchangeBill() != null) {
				list.get(i).setFeeForExchangeBill(supplierQuote.getFeeForExchangeBill().toString());
			}
		}
		request.setAttribute("list", list);
		return "/purchase/supplierquote/addElementTable";
		
	}
	
	/**
	 * 新增供应商询价明细
	 */
	@RequestMapping(value="/addElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo addElement(HttpServletRequest request,@ModelAttribute SupplierInquiryEmelentVo elementVo) {
		boolean success = false;
		String message = "";
		Integer id = new Integer(getString(request, "id"));
		ResultVo resultVo=null;
		if (elementVo.getList().size()>0) {
			try {
			 resultVo = supplierQuoteElemenetService.insertSelective(elementVo.getList(), id);
			} catch (Exception e) {
				e.printStackTrace();
				message=e.getMessage();
				return new ResultVo(success, message);
			}
		}
		
		return resultVo;
	}
	
	/**
	 * 修改供应商报价明细页面
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/editsupplierquoteelementdate",method=RequestMethod.GET)
	public String editsupplierquoteelement(HttpServletRequest request) throws UnsupportedEncodingException{
		request.setAttribute("clientInquiryQuoteNumber", request.getParameter("clientInquiryQuoteNumber"));
		request.setAttribute("supplierInquiryQuoteNumber", request.getParameter("supplierInquiryQuoteNumber"));
		Integer id=Integer.parseInt(request.getParameter("id"));
		SupplierQuoteElement supplierQuoteElement=supplierQuoteElemenetService.selectByPrimaryKey(id);
		if(50==supplierQuoteElement.getCertificationId()){
			supplierQuoteElement.setCertificationValue(supplierQuoteElement.getCertificationCode()+" - "+supplierQuoteElement.getCertificationValue());
		}
		request.setAttribute("supplierQuoteElement",supplierQuoteElement);
		return "/purchase/supplierquote/updatesupplierquoteelement";
	}
	
	/**
	 * 修改供应商报价明细
	 * **/
	@RequestMapping(value="/editsupplierquoteelement",  method=RequestMethod.POST)
	public @ResponseBody ResultVo editsupplierquoteelement(HttpServletRequest request,
			@ModelAttribute SupplierQuoteElement record)
	{
		boolean result = true;
		String message = "修改完成！";
		UserVo userVo = getCurrentUser(request);
		record.setUpdateUserId(new Integer(userVo.getUserId()));
		supplierQuoteElemenetService.updateByPrimaryKeySelective(record);
		return new ResultVo(result, message);
	}
	
	/**
	 * 修改供应商报价明细
	 * **/
	@RequestMapping(value="/updatesupplierquoteelement",  method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updatesupplierquoteelement(HttpServletRequest request,
			@ModelAttribute SupplierQuoteElement record)
	{
		String supplierQuoteElementId=request.getParameter("supplierQuoteElementId");
		if(null!=supplierQuoteElementId&&!"".equals(supplierQuoteElementId)){
		String quotePrice=request.getParameter("quotePrice");
		record.setPrice(Double.parseDouble(quotePrice));
		record.setId(Integer.parseInt(supplierQuoteElementId));
		}else{
			record.setDescription(request.getParameter("quoteDescription"));
			record.setAmount(Double.parseDouble(request.getParameter("quoteAmount")));
			String moq = request.getParameter("moq");
			if (moq != null && moq != "") {
				record.setMoq(Integer.parseInt(moq));
			}
			record.setUnit(request.getParameter("quoteUnit"));
			record.setConditionId(Integer.parseInt(request.getParameter("conditionCode")));
			record.setCertificationId(Integer.parseInt(request.getParameter("certificationCode")));
			record.setSupplierQuoteStatusId(Integer.parseInt(request.getParameter("supplierQuoteStatusValue")));
			record.setRemark(request.getParameter("quoteRemark"));
			if (record.getQuoteFeeForExchangeBill() != null && !"".equals(record.getQuoteFeeForExchangeBill())) {
				record.setFeeForExchangeBill(new Double(record.getQuoteFeeForExchangeBill().replace("$", "")));
			}
			if (record.getQuoteBankCost() != null && !"".equals(record.getQuoteBankCost())) {
				record.setBankCost(new Double(record.getQuoteBankCost().replace("$", "")));
			}
			if (record.getQuoteHazmatFee() != null && !"".equals(record.getQuoteHazmatFee())) {
				record.setHazmatFee(new Double(record.getQuoteHazmatFee().replace("$", "")));
			}
			if (record.getQuoteOtherFee() != null && !"".equals(record.getQuoteOtherFee())) {
				record.setOtherFee(new Double(record.getQuoteOtherFee().replace("$", "")));
			}
			
		}
		
		boolean success = true;
		String message = "更新完成！";
		try{
		supplierQuoteElemenetService.updateByPrimaryKeySelective(record);
	
		}catch(Exception e){
		e.printStackTrace();
		success = false;
		message = "更新失败";
		}
		EditRowResultVo result = new EditRowResultVo(success, message);
		return result;
	}
	
	/**
	 * 修改供应商报价页面
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/editsupplierquotedate",method=RequestMethod.GET)
	public String editsupplierquote(HttpServletRequest request) throws UnsupportedEncodingException{
		request.setAttribute("clientInquiryQuoteNumber", request.getParameter("clientInquiryQuoteNumber"));
		request.setAttribute("supplierInquiryQuoteNumber", request.getParameter("supplierInquiryQuoteNumber"));
		request.setAttribute("sourceNumber", request.getParameter("sourceNumber"));
//		request.setAttribute("currencyId", request.getParameter("currencyId"));
//		request.setAttribute("exchangeRate", request.getParameter("exchangeRate"));
//		request.setAttribute("quoteDate", request.getParameter("quoteDate"));
		request.setAttribute("updateTimestamp", request.getParameter("updateTimestamp"));
//		request.setAttribute("id", request.getParameter("id"));
		
//		String currencyValue= request.getParameter("currencyValue");
//		currencyValue = new String(currencyValue.getBytes("iso8859-1"),"UTF-8");
//		request.setAttribute("currencyValue",currencyValue);
		SupplierQuote supplierQuote=supplierQuoteService.selectByPrimaryKey(Integer.parseInt(request.getParameter("Id")));
		request.setAttribute("supplierQuote", supplierQuote);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("today is:" + format.format(Calendar.getInstance().getTime()));
		System.out.println("yesterday is:" + format.format(cal.getTime()));
		return "/purchase/supplierquote/updatesupplierquote";
	}
	
	/**
	 * 修改供应商报价
	 * **/
	@RequestMapping(value="/editsupplierquote",  method=RequestMethod.POST)
	public @ResponseBody ResultVo editsupplierquote(HttpServletRequest request, @ModelAttribute SupplierQuote record)
	{
		boolean result = true;
		String message = "修改完成！";
		SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(record.getId());
		Integer statusId = supplierQuote.getQuoteStatusId();
		UserVo userVo = getCurrentUser(request);
		record.setLastUpdateUser(new Integer(userVo.getUserId()));
		supplierQuoteService.updateByPrimaryKey(record);
		if(record.getQuoteStatusId().equals(91)){
			SupplierQuoteElement element=new SupplierQuoteElement();
			element.setSupplierQuoteStatusId(73);
			element.setSupplierQuoteId(record.getId());
			supplierQuoteElemenetService.updateBySupplierQuoteId(element);
		}else if(record.getQuoteStatusId().equals(92)){
			SupplierQuoteElement element=new SupplierQuoteElement();
			element.setSupplierQuoteStatusId(71);
			element.setSupplierQuoteId(record.getId());
			supplierQuoteElemenetService.updateBySupplierQuoteId(element);
		}else if (record.getQuoteStatusId().equals(90) && (statusId.equals(91) || statusId.equals(92))) {
			SupplierQuoteElement element=new SupplierQuoteElement();
			element.setSupplierQuoteStatusId(70);
			element.setSupplierQuoteId(record.getId());
			supplierQuoteElemenetService.updateBySupplierQuoteId(element);
		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 新增供应商明细上传
	 */
	@RequestMapping(value="/addelement",method=RequestMethod.GET)
	public String addelement(HttpServletRequest request) {
		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("supplierInquiryQuoteNumber", request.getParameter("supplierInquiryQuoteNumber"));
		request.setAttribute("clientInquiryQuoteNumber", request.getParameter("clientInquiryQuoteNumber"));
		return "/purchase/supplierquote/addElement";
	}
	
	/**
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			SupplierQuote record) {
		boolean success=false;
		String message="";
		Integer id =new Integer(request.getParameter("id"));
		MessageVo messageVo =new MessageVo();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		try {
			 messageVo = supplierQuoteElemenetService.uploadExcel(multipartFile, id);
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
	 * pdf上传
	 * @throws IOException 
	 */
	@RequestMapping(value="/uploadPdf",method=RequestMethod.POST)
	public @ResponseBody String uploadPdf(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			SupplierQuote record) throws IOException {
		boolean success=false;
		String message="";
//		Integer id =new Integer(request.getParameter("id"));
		MessageVo messageVo =new MessageVo();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("pdffile");
		try {
			 messageVo = supplierQuoteElemenetService.uploadPdf(multipartFile, 0);
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
	 * 不询价新增报价供应商列表
	 */
	@RequestMapping(value="/supplierList",method=RequestMethod.GET)
	public String supplierList(HttpServletRequest request) {
		return "/purchase/supplierquote/supplierlist";
	}
	
	/**
	 * excel上传
	 */
	@RequestMapping(value="/quoteUploadExcel",method=RequestMethod.POST)
	public @ResponseBody String quoteUploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			SupplierQuote record) {
		boolean success=false;
		String message="";
		String data =request.getParameter("data");
		MessageVo messageVo =new MessageVo();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		try {
			 messageVo = supplierQuoteElemenetService.quoteUploadExcel(multipartFile, data);
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
	 * 供应商件号附件管理
	 */
	@RequestMapping(value="/partfile",method=RequestMethod.GET)
	public String partfile(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "supplier_quote");
		request.setAttribute("type", request.getParameter("type"));
//		String type=request.getParameter("type");
		return "/marketing/clientquote/fileUpload";
	}
	
	/*
	 * 错误列表
	 */
	@RequestMapping(value="/toUnknow",method=RequestMethod.GET)
	public String toUnknow() {
		return "/purchase/supplierquote/errorlist";
	}
	
	/*
	 * 错误列表数据
	 */
	@RequestMapping(value="/Unknow",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo Unknow(HttpServletRequest request) {
		PageModel<SupplierQuoteElementUpload> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		page.put("userId", userVo.getUserId());
		supplierQuoteElementUploadService.selectByUserId(page);;
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierQuoteElementUpload supplierQuoteElementUpload : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElementUpload);
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
		supplierQuoteElementUploadService.deleteByPrimaryKey(new Integer(userVo.getUserId()));
		return new ResultVo(success, message);
	}
	
	
	/**
	 * 供应商预报价
	 */
	@RequestMapping(value="/weatherQuote",method=RequestMethod.GET)
	public String weatherQuote(HttpServletRequest request) {
		return "/purchase/supplierquote/airtype";
	}
	
	/**
	 * 供应商预报价机型
	 * **/
	@RequestMapping(value="/airtypedata",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo airtypedata(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<SystemCode> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
	
		supplierQuoteService.findAirType(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SystemCode listDateVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(listDateVo);
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
	 * 供应商预报价客户参考号
	 * **/
	@RequestMapping(value="/clientinquirydata",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo clientinquirydata(HttpServletRequest request,
			HttpServletResponse response){
		PageModel<ClientInquiry> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		String airTypeId=request.getParameter("airTypeId");
	
		if(null!=searchString){
			if(null!=airTypeId){	
				if("".equals(searchString)){
					searchString="AIR_TYPE_ID IN ("+airTypeId+")";
				}else{
					searchString="AIR_TYPE_ID IN ("+airTypeId+") and "+searchString;
				}
				
			}
		supplierQuoteService.findClientInquirypage(page, searchString, getSort(request));
		
		}
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientInquiry listDateVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(listDateVo);
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
	 * 预报价数据
	 * **/
	@RequestMapping(value="/weatherQuoteData",  method=RequestMethod.POST)
	public @ResponseBody ResultVo weatherQuoteData(HttpServletRequest request, @ModelAttribute SupplierQuote record)
	{
		boolean result = true;
		String message = "";
		String airTypeId=request.getParameter("airTypeId");
		String clientinquiryId=request.getParameter("clientinquiryId");
		String clientId=request.getParameter("clientId");
//		String[] b=a.split(",");
		
//		if(null!=clientinquiryId){
//			 clientinquiryId=clientinquiryId.replace(",", "b");
//		}
//		if(null!=airTypeId){
//			 airTypeId=airTypeId.replace(",", "a");
//		}
//		for (int i = 0; i < b.length; i++) {
//			airTypeId=b[i]+"a";
//		}
		 message=airTypeId+"/"+clientinquiryId+"//"+clientId;
		return new ResultVo(result, message);
	}
	
	/**
	 * 供应商预报价数据页面
	 */
	@RequestMapping(value="/weatherQuotePage",method=RequestMethod.GET)
	public String weatherQuotePage(HttpServletRequest request) {
		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("type", "purchase");
		return "/purchase/supplierquote/weatherquotelist";
	}
	
	/**
	 * 动态列
	 * **/
	@RequestMapping(value = "/list/dynamicColNames", method = RequestMethod.POST)
	public @ResponseBody
	ColumnVo excelListDynamicCol(HttpServletRequest request,
			HttpServletResponse response) {
		ListDateVo vo=new ListDateVo();
		String id= request.getParameter("id");
		String[] a=id.split("//");
		String clientId=a[1];
		String[] b=a[0].split("/");
		String airTypeId =b[0];
		String clientinquiryId=b[1];
//		String s=b[0];
//		   String airTypeId = s.replace("a",",");
//		String d=b[1];
//		String clientinquiryId=d.replace("b", ",");
		vo.setAirTypeId(airTypeId);
		vo.setClientinquiryId(clientinquiryId);
		vo.setClientId(clientId);
		List<ClientInquiry> list=supplierQuoteService.findClientInquiry(vo);
		List<String> supplierCode=new ArrayList<String>();
		for (ClientInquiry clientInquiry : list) {
			List<ClientInquiry> slist=supplierQuoteService.findSupplierQuote(clientInquiry.getId(),clientInquiry.getItem());
			for (ClientInquiry supplierQuoteVo : slist) {
				if(null!=supplierQuoteVo.getSupplierCode()&&!supplierCode.contains(supplierQuoteVo.getSupplierCode())){
					supplierCode.add(supplierQuoteVo.getSupplierCode());
				}
			}
		}
		List<String> displayNames = new ArrayList<String>();
		List<String> colNames = new ArrayList<String>();
		Collections.sort(supplierCode);
		for(int i=0;i<supplierCode.size();i++){
			displayNames.add(supplierCode.get(i));
			colNames.add("supplier"+supplierCode.get(i));			
		}
		ColumnVo result = new ColumnVo();
		result.setColumnDisplayNames(displayNames);
		result.setColumnKeyNames(colNames);
		
		return result;
	}
	
	/**
	 * 供应商预报价数据
	 * **/
	@RequestMapping(value="/weatherquotedata",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo weatherquotedata(HttpServletRequest request,
			HttpServletResponse response){
		PageModel<ClientInquiry> page = getPage(request);
		PageModel<ClientInquiry> page2 = getPage(request);
		List<ClientInquiry> arraylist=new ArrayList<ClientInquiry>();
		JQGridMapVo jqgrid = new JQGridMapVo();
		String partNumber =request.getParameter("part_number");
		String check =request.getParameter("check");
		String type=request.getParameter("type");
		ListDateVo vo=new ListDateVo();
		String id= request.getParameter("id");
		String[] a=id.split("//");
		String clientId=a[1];
		String[] b=a[0].split("/");
//		String s=b[0];
		String airTypeId =b[0];
		String clientinquiryId=b[1];
//		vo.setAirTypeId(airTypeId);
//		vo.setClientinquiryId(clientinquiryId);
//		vo.setClientId(clientId);
		page2.put("partNumber", partNumber);
		page2.put("airTypeId", airTypeId);
		page2.put("clientinquiryId", clientinquiryId);
		page2.put("clientId", clientId);
		page2.put("check", check);
		supplierQuoteService.findClientInquiryPage(page2, "", getSort(request));
		Map<String,Double> supplierPrice=new HashMap<String, Double>();
		List<String> supplierCode=new ArrayList<String>();
		for (ClientInquiry clientInquiry : page2.getEntities()) {
			page.put("id", clientInquiry.getId());
			page.put("item", clientInquiry.getItem());
			supplierQuoteService.findWeatherQuote(page, "", getSort(request));
			for (int j = 0; j < page.getEntities().size(); j++) {
				if(!supplierCode.contains(page.getEntities().get(j).getSupplierCode())){
					supplierCode.add(page.getEntities().get(j).getSupplierCode());
				}
				Double counterFee=0.0;
				Double cqex=1.0;
				Double sqex=1.0;
				if(type.equals("marketing")){
					ClientQuote clientQuoteVos=clientQuoteDao.findclientquote(clientInquiry.getId());
					cqex=clientQuoteVos.getExchangeRate();
					sqex=page.getEntities().get(j).getExchangeRate();
				}
				if(null!=page.getEntities().get(j).getCounterFee()&&0!=page.getEntities().get(j).getCounterFee()){
					counterFee=page.getEntities().get(j).getCounterFee();
					if(counterFee<1){
						counterFee=page.getEntities().get(j).getPrice()*counterFee;
					}
					 BigDecimal bg = new BigDecimal(counterFee);  
					 BigDecimal pg = new BigDecimal(page.getEntities().get(j).getPrice()); 
			         counterFee = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			         page.getEntities().get(j).setPrice(bg.add(pg).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				if(null!= page.getEntities().get(j).getPrice()){
					 BigDecimal price = new BigDecimal(page.getEntities().get(j).getPrice()*sqex/cqex); 
					supplierPrice.put(page.getEntities().get(j).getId()+"-"+page.getEntities().get(j).getSupplierCode(),price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() );

				}
							}
			arraylist.add(page.getEntities().get(0));
		}
		page.clear();
		page.setEntities(arraylist);
		if (page2.getEntities().size() > 0) {
			jqgrid.setPage(page2.getPageNo());
			jqgrid.setRecords(page2.getRecordCount());
			jqgrid.setTotal(page2.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientInquiry listDateVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(listDateVo);
				for (int i = 0; i < supplierCode.size(); i++) {
					map.put("supplier"+supplierCode.get(i), supplierPrice.get(listDateVo.getId()+"-"+supplierCode.get(i)));
				}
				
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
		String type=request.getParameter("type");
		String conditionId=request.getParameter("conditionId");
		List<ListDateVo> list=supplierQuoteService.findcond();
		List<ListDateVo> arraylist=new ArrayList<ListDateVo>();
		StringBuffer value = new StringBuffer();
		
		for (int i = 0; i < list.size(); i++) {
			ListDateVo dateVo=list.get(i);
			Integer id=dateVo.getId();
			if(conditionId!=null&&id!=null){
				if(conditionId.equals(id+"")){
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
			}				
			
		}
		if(arraylist.size()>0){
			list.clear();
			list.addAll(arraylist);
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCode().equals("SV") || list.get(i).getCode().equals("OH") || list.get(i).getCode().equals("RE") || list.get(i).getCode().equals("Exchange")) {
				list.remove(i);
				i= i-1;
			}
		}
		success = true;
		JSONArray json = new JSONArray();
//		if(null==conditionId||"".equals(conditionId)){
			json.add(list);
//		}else{
//			json.add(arraylist);
//		}
		if(null!=type&&type.equals("onlineedit")){
			for (ListDateVo listDateVo : list) {
				value.append(listDateVo.getId()).append(":").append(listDateVo.getCode()).append(";");
			}
			value.deleteCharAt(value.length()-1);
			msg=value.toString();
		}else{
			msg =json.toString();
		}
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
	public @ResponseBody ResultVo findcert(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String certificationId=request.getParameter("certificationId");
		String type=request.getParameter("type");
		List<ListDateVo> list=supplierQuoteService.findcert();
		List<ListDateVo> arraylist=new ArrayList<ListDateVo>();
		ListDateVo element=new ListDateVo();
		StringBuffer value = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			ListDateVo dateVo=list.get(i);
			Integer id=dateVo.getId();
			if(certificationId!=null&&id!=null){
				if(certificationId.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						if(50==list.get(j).getId()){
							element.setValue(list.get(j).getCode()+" - "+list.get(j).getValue());
							element.setId(50);
							list.set(j, element);
						}
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
		}
		if(arraylist.size()>0){
			list.clear();
			list.addAll(arraylist);
		}
		
		success = true;
		JSONArray json = new JSONArray();
//		if(null==certificationId||"".equals(certificationId)){
			json.add(list);
//		}else{
//			json.add(arraylist);
//		}
		
		if(null!=type&&type.equals("onlineedit")){
			for (ListDateVo listDateVo : list) {
				value.append(listDateVo.getId()).append(":").append(listDateVo.getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
			msg=value.toString();
		}else{
		msg =json.toString();
		}
		return new ResultVo(success, msg);
	}
	
	
	/**
	 * 算有效期
	 * @throws ParseException 
	 */
	@RequestMapping(value="/validity",method=RequestMethod.POST)
	public @ResponseBody ResultVo validity(HttpServletRequest request) throws ParseException {
		String message = "";
		boolean success = true;
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String validity=request.getParameter("validity");
		Date day=dateFormat.parse(validity);
		GregorianCalendar gc=new GregorianCalendar(); 
		gc.setTime(day); 
		gc.add(2,1); 
		gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
		Date time=gc.getTime();
		String deadline=dateFormat.format(time);
		return new ResultVo(success, deadline);
	}
	
	/**
	 * 报价单状态证书
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/findstatus",method=RequestMethod.POST)
	public @ResponseBody ResultVo findstatus(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String quoteStatusId=request.getParameter("quoteStatusId");
		List<SystemCode> list=systemCodeService.findSupplierByType("QUOTE_STATUS_ID");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		
		for (int i = 0; i < list.size(); i++) {
			SystemCode dateVo=list.get(i);
			Integer id=dateVo.getId();
			if(quoteStatusId!=null&&id!=null){
				if(quoteStatusId.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==quoteStatusId||"".equals(quoteStatusId)){
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
	@RequestMapping(value="/findsqstauts",method=RequestMethod.POST)
	public @ResponseBody ResultVo findsqstauts(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String supplierQuoteStatusId=request.getParameter("supplierQuoteStatusId");
		String type=request.getParameter("type");
		List<ListDateVo> list=supplierQuoteService.findsqstauts();
		List<ListDateVo> arraylist=new ArrayList<ListDateVo>();
		StringBuffer value = new StringBuffer();
		
		for (int i = 0; i < list.size(); i++) {
			ListDateVo dateVo=list.get(i);
			Integer id=dateVo.getId();
			if(supplierQuoteStatusId!=null&&id!=null){
				if(supplierQuoteStatusId.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}			
		}
		if(arraylist.size()>0){
			list.clear();
			list.addAll(arraylist);
		}
		
		success = true;
		JSONArray json = new JSONArray();
//		if(null==supplierQuoteStatusId){
			json.add(list);
//		}else{
//			json.add(arraylist);
//		}
		if(null!=type&&type.equals("onlineedit")){
			for (ListDateVo listDateVo : list) {
				value.append(listDateVo.getId()).append(":").append(listDateVo.getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
			msg=value.toString();
		}else{
		msg =json.toString();
		}
		return new ResultVo(success, msg);
	}
	
	/**
	 * 下拉列表数据-供应商
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
//	@RequestMapping(value="/finddata",method=RequestMethod.POST)
//	public @ResponseBody ResultVo finddata(HttpServletRequest request,HttpServletResponse response) throws IOException {
//		boolean success = false;
//		String msg = "";
//		String supplierinquiryid=request.getParameter("");
//		List<SupplierInquiryEmelentVo> list=supplierQuoteElemenetService.findsupplierinquiryelement(supplierinquiryid);
//		
//		success = true;
//		JSONArray json = new JSONArray();
//		json.add(list);
//		msg =json.toString();
//		return new ResultVo(success, msg);
//		}
	
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
		UserVo user = ContextHolder.getCurrentUser();
		AuthorityRelation authorityRelation=new AuthorityRelation();
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (roleVo.getRoleName().equals("管理员") || roleVo.getRoleName().equals("行政") || roleVo.getRoleName().equals("财务")||roleVo.getRoleName().indexOf("物流") >= 0||roleVo.getRoleName().equals("销售")) {
			authorityRelation.setUserId(null);
		}else{
			authorityRelation.setUserId(Integer.parseInt(user.getUserId()));
		}
		List<ListDateVo> list=supplierQuoteService.findsid(authorityRelation);
		
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
		}
	
	@RequestMapping(value="/findcid",method=RequestMethod.POST)
	public @ResponseBody ResultVo findcid(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		List<ListDateVo> list=supplierQuoteService.findcid();
		
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
		}
	
	@RequestMapping(value="/findbizid",method=RequestMethod.POST)
	public @ResponseBody ResultVo findbizid(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		List<ListDateVo> list=supplierQuoteService.findbizid();
		
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
		}
	
	@RequestMapping(value="/findairid",method=RequestMethod.POST)
	public @ResponseBody ResultVo findairid(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		List<ListDateVo> list=supplierQuoteService.findairid();
		
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
		}
	
	/**
	 * 提示信息
	 */
	@RequestMapping(value="/warnMessage",method=RequestMethod.POST)
	public @ResponseBody ResultVo warnMessage(HttpServletRequest request) {
		UserVo userVo = getCurrentUser(request);
		List<Integer> list = warnMessageService.selectByUserId(new Integer(userVo.getUserId()));
		List<WarnMessage> messages = new ArrayList<WarnMessage>();
		for (int i = 0; i < list.size(); i++) {
			List<WarnMessage> messageList = warnMessageService.getMessage(list.get(i));
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(list.get(i));
			StringBuffer message = new StringBuffer();
			message.append("询价单").append(clientInquiry.getQuoteNumber()).append("件号：");
			for (int j = 0; j < messageList.size(); j++) {
				message.append(messageList.get(j).getPartNumber()).append(",");
			}
			message.deleteCharAt(message.length()-1);  
			message.append("已报价!");
			WarnMessage warnMessage = new WarnMessage(list.get(i),message.toString());
			messages.add(warnMessage);
			
		}
		JSONArray json = new JSONArray();
		json.add(messages);
		return new ResultVo(true, json.toString());
	}
	
	/**
	 * 爬虫价格大于5000美元提示信息
	 */
	@RequestMapping(value="/highPriceMessage",method=RequestMethod.POST)
	public @ResponseBody ResultVo highPriceMessage(HttpServletRequest request) {
		synchronized(this) {
			UserVo userVo = getCurrentUser(request);
			List<HighPriceCrawlQuote> list = highPriceCrawlQuoteService.getUnFinishList();
			List<WarnMessage> messages = new ArrayList<WarnMessage>();
			List<UserVo> userList = userService.getByRole("国外采购");
			boolean isPurchase = false;
			for (UserVo userVo2 : userList) {
				if (userVo2.getUserId().equals(userVo.getUserId())) {
					isPurchase = true;
					break;
				}
			}
			if (isPurchase) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getPerson() == null) {
						SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(list.get(i).getSupplierQuoteId());
						StringBuffer message = new StringBuffer();
						message.append("报价单号:").append(supplierQuote.getQuoteNumber()).append("件号：");
						List<SupplierQuoteElement> lists = supplierQuoteElemenetService.getBySupplierQuoteId(supplierQuote.getId());
						for (int j = 0; j < lists.size(); j++) {
							if (lists.get(j).getPrice() > 5000) {
								message.append(lists.get(j).getPartNumber()).append(",");
							}
						}
						message.append("报价大于5000!");
						WarnMessage warnMessage = new WarnMessage(list.get(i).getSupplierQuoteId(),message.toString());
						messages.add(warnMessage);
					}else {
						String[] users = list.get(i).getPerson().split(",");
						int exist = 0;
						for (int j = 0; j < users.length; j++) {
							if (userVo.getUserId().equals(users[j])) {
								exist = 1;
								break;
							}
						}
						if (exist == 1) {
							continue;
						}else {
							SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(list.get(i).getSupplierQuoteId());
							StringBuffer message = new StringBuffer();
							message.append("报价单号:").append(supplierQuote.getQuoteNumber()).append("件号：");
							List<SupplierQuoteElement> lists = supplierQuoteElemenetService.getBySupplierQuoteId(supplierQuote.getId());
							for (int j = 0; j < lists.size(); j++) {
								if (lists.get(j).getPrice() > 5000) {
									message.append(lists.get(j).getPartNumber()).append(",");
								}
							}
							message.append("报价大于5000!");
							WarnMessage warnMessage = new WarnMessage(list.get(i).getSupplierQuoteId(),message.toString());
							messages.add(warnMessage);
						}
					}
				}
				
				JSONArray json = new JSONArray();
				json.add(messages);
				return new ResultVo(true, json.toString());
			}else {
				return null;
			}
		}
	}
	
	/**
	 * 更新报价大于5000信息状态
	 */
	@RequestMapping(value="/updateHighPriceStatus",method=RequestMethod.POST)
	public @ResponseBody ResultVo updateHighPriceStatus(HttpServletRequest request) {
		synchronized(this) {
			UserVo userVo = getCurrentUser(request);
			Integer supplieQuoteId = new Integer(getString(request, "id"));
			List<HighPriceCrawlQuote> list = highPriceCrawlQuoteService.getBySupplierQuoteId(supplieQuoteId);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getPerson() == null) {
					list.get(i).setPerson(userVo.getUserId());
				}else {
					String[] users = list.get(i).getPerson().split(",");
					int exist = 0;
					for (int j = 0; j < users.length; j++) {
						if (userVo.getUserId().equals(users[j])) {
							exist = 1;
							break;
						}
					}
					if (exist == 1) {
						break;
					}else {
						list.get(i).setPerson(list.get(i).getPerson()+","+userVo.getUserId());
					}
				}
				highPriceCrawlQuoteService.updateByPrimaryKeySelective(list.get(i));
				String[] users = list.get(i).getPerson().split(",");
				List<UserVo> userList = userService.getByRole("国外采购");
				if (users.length == userList.size()-1) {
					list.get(i).setIsSend(1);
					highPriceCrawlQuoteService.updateByPrimaryKeySelective(list.get(i));
				}
			}
			return new ResultVo(true, "");
		}
	}
	
	/**
	 * 监测爬虫信息并生成报价
	 */
	/**
	@Author: Modify by white
	@DateTime: 2018/10/9 16:27
	@Description: 添加注释
	*/
	@RequestMapping(value="/createSatairQuote",method=RequestMethod.POST)
	public @ResponseBody ResultVo createSatairQuote(HttpServletRequest request) {
		synchronized(this) {
			//'1077'供应商
			satairQuoteService.createQuote();
			//'1006'供应商
			aviallQuoteService.createQuote();
			//'1003'供应商
			klxQuoteElementService.createQuote();
			// '1005'供应商
			kapcoQuoteService.createQuote();
			//'6012'供应商
			txtavQuoteService.createQuote();
			//是否存在参数处理的异常
			clientInquiryService.onpostcheck();
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR); 
			int month = cal.get(Calendar.MONTH); 
			int date = cal.get(Calendar.DATE); 
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int min = cal.get(Calendar.MINUTE);
			String time = "%"+year+"-"+(month+1)+"-"+date+" "+hour+"%";
			if (hour== 10 || hour==15 || hour== 19) {
				int count = clientInquiryElementService.getCountByTime(time);
				if (count == 0) {
					clientInquiryElementService.sendByCrawEmail();
				}
			}
			//satairQuoteService.unSelectEmail();
			return new ResultVo(true, "");
		}
	}
	
	@RequestMapping(value="/test")
	public void iii(){
		clientInquiryService.onpostcheck();
	}
	
	/**
	 * 更新提醒信息状态
	 */
	@RequestMapping(value="/updateStatus",method=RequestMethod.POST)
	public @ResponseBody ResultVo updateStatus(HttpServletRequest request) {
		Integer clientInquiryId = new Integer(getString(request, "id"));
		warnMessageService.updateStatus(clientInquiryId);
		return new ResultVo(true, "");
	}
	
/*	*//**
	 * 提示信息
	 *//*
	@RequestMapping(value="/addQuote",method=RequestMethod.POST)
	public @ResponseBody ResultVo addQuote(HttpServletRequest request,@ModelAttribute SupplierQuoteElement supplierQuoteElement) {
		
		
		return new ResultVo(true, "");
	}*/
	
	/**
	 * 历史报价
	 * @param request
	 * @author Tanoy
	 */
	@RequestMapping(value="/historyQuote",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo historyQuote(HttpServletRequest request) {
		PageModel<SupplierQuoteElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		GridSort sort = getSort(request);
		
		String partNumber = getString(request, "partNumber");
		String id = getString(request, "id");
		String clientInquiryId = getString(request, "clientInquiryId");
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(new Integer(clientInquiryId));
		page.put("partNumberCode", clientInquiryService.getCodeFromPartNumber(partNumber));
		page.put("partNumber", partNumber);
		
		supplierQuoteElemenetService.getByShortPartPage(page, sort);
		List<SupplierQuoteElement> storageList = supplierQuoteElementService.getStorage(page);
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		List<SupplierQuoteElement> competitorPrice = supplierQuoteElemenetService.getCompetitorPrice(clientInquiry.getClientId().toString(), clientInquiryService.getCodeFromPartNumber(partNumber));
		page.getEntities().addAll(competitorPrice);
		List<SupplierQuoteElement> list = new ArrayList<SupplierQuoteElement>();
		List<SupplierQuoteElement> hisList = new ArrayList<SupplierQuoteElement>();
		for (SupplierQuoteElement supplierQuoteElement : page.getEntities()) {
			if (supplierQuoteElement.getClientInquiryId().equals(new Integer(clientInquiryId))) {
				list.add(supplierQuoteElement);
			}else {
				hisList.add(supplierQuoteElement);
			}
		}
		for (SupplierQuoteElement supplierQuoteElement : storageList) {
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
			mapList.add(map);
		}
		for (SupplierQuoteElement supplierQuoteElement : list) {
			//标记竞争对手供应商
			int supplierCount = competitorSupplierService.getCountByClientAndSupplier(clientInquiry.getClientId(), supplierQuoteElement.getSupplierId());
			if (supplierCount > 0) {
				supplierQuoteElement.setIsCompetitor(1);
			}
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
			mapList.add(map);
		}
		if (hisList.size() > 0) {
			SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
			supplierQuoteElement.setDescription("历史供应商报价");
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
			mapList.add(map);
		}
		for (SupplierQuoteElement supplierQuoteElement : hisList) {
			//标记竞争对手供应商
			int supplierCount = competitorSupplierService.getCountByClientAndSupplier(clientInquiry.getClientId(), supplierQuoteElement.getSupplierId());
			if (supplierCount > 0) {
				supplierQuoteElement.setIsCompetitor(1);
			}
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
			mapList.add(map);
		}
		jqgrid.setRows(mapList);

		return jqgrid;
	}
	
	/*
	 * 一次性读取历史报价
	 */
	@RequestMapping(value="/historyQuoteOnce",method=RequestMethod.POST)
	public @ResponseBody ResultVo historyQuoteOnce(HttpServletRequest request) {
		PageModel<SupplierQuoteElement> page=getPage(request);
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		GridSort sort = getSort(request);
		
		String partNumber = getString(request, "partNumber");
		String id = getString(request, "id");
		String clientInquiryId = getString(request, "clientInquiryId");
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(new Integer(clientInquiryId));
		page.put("partNumberCode", clientInquiryService.getCodeFromPartNumber(partNumber));
		
		supplierQuoteElemenetService.getByShortPart(page,sort);
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//		SupplierQuoteElement first = new SupplierQuoteElement();
//		first.setId(000001);
//		first.setClientInquiryElementId(new Integer(id));
//		first.setClientInquiryId(new Integer(clientInquiryId));
//		Map<String, Object> fmap = EntityUtil.entityToTableMap(first);
//		mapList.add(fmap);
		List<SupplierQuoteElement> competitorPrice = supplierQuoteElemenetService.getCompetitorPrice(clientInquiry.getClientId().toString(), clientInquiryService.getCodeFromPartNumber(partNumber));
		page.getEntities().addAll(competitorPrice);
		List<SupplierQuoteElement> list = new ArrayList<SupplierQuoteElement>();
		List<SupplierQuoteElement> hisList = new ArrayList<SupplierQuoteElement>();
		for (SupplierQuoteElement supplierQuoteElement : page.getEntities()) {
			if (supplierQuoteElement.getClientInquiryId().equals(new Integer(clientInquiryId))) {
				list.add(supplierQuoteElement);
			}else {
				hisList.add(supplierQuoteElement);
			}
		}
		for (SupplierQuoteElement supplierQuoteElement : list) {
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
			mapList.add(map);
		}
		if (hisList.size() > 0) {
			SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
			supplierQuoteElement.setDescription("历史供应商报价");
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
			mapList.add(map);
		}
		for (SupplierQuoteElement supplierQuoteElement : hisList) {
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
			mapList.add(map);
		}
		String exportModel = getString(request, "exportModel");
		JSONArray json = new JSONArray();
		json.add(mapList);
		return new ResultVo(true, json.toString());
	}
	
	/*
	 * 新增或修改报价
	 */
	@RequestMapping(value="/addOrEditQuote",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo addOrEditQuote(HttpServletRequest request){
		boolean success = false;
		String message = "";
		UserVo userVo = getCurrentUser(request);
		try {
			String price = getString(request, "price");
			if (price != null && !"".equals(price)) {
				SupplierQuoteElement supplierQuoteElement = new SupplierQuoteElement();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Integer id = new Integer(getString(request, "id"));
				supplierQuoteElement.setCreateUser(new Integer(userVo.getUserId()));
				String unit  = getString(request, "unit");
				if (unit != null && !"".equals(unit)) {
					supplierQuoteElement.setUnit(unit);
				}else {
					supplierQuoteElement.setUnit("EA");
				}
				String sourceNumber = getString(request, "sourceNumber");
				if (sourceNumber != null && !"".equals(sourceNumber)) {
					supplierQuoteElement.setSourceNumber(sourceNumber);
				}
				supplierQuoteElement.setCurrencyId(new Integer(getString(request, "currencyValue")));
				supplierQuoteElement.setCurrencyId(new Integer(getString(request, "currencyValue")));
				supplierQuoteElement.setAmount(new Double(getString(request, "amount")));
				supplierQuoteElement.setCertificationId(new Integer(getString(request, "certificationCode")));
				supplierQuoteElement.setConditionId(new Integer(getString(request, "conditionCode")));
				supplierQuoteElement.setPartNumber(getString(request, "partNumber"));
				supplierQuoteElement.setDescription(getString(request, "description"));
				supplierQuoteElement.setPrice(new Double(price));
				supplierQuoteElement.setLeadTime(getString(request, "leadTime"));
				supplierQuoteElement.setRemark(getString(request, "remark"));
				supplierQuoteElement.setAlterPartNumber(getString(request, "alterPartNumber"));
				supplierQuoteElement.setLocation(getString(request, "location"));
				if (getString(request,"moq") != "" && !"".equals(getString(request, "moq"))) {
					supplierQuoteElement.setMoq(new Integer(getString(request,"moq")));
				}
				if (getString(request,"freight") != "" && !"".equals(getString(request, "freight"))) {
					supplierQuoteElement.setFreight(new Double(getString(request,"freight")));
				}
				if (getString(request,"validity") != "" && !"".equals(getString(request, "validity"))) {
					supplierQuoteElement.setValidity(sdf.parse(getString(request,"validity")));
				}else {
					Calendar cal = Calendar.getInstance();  
			        cal.setTime(new Date());  
			        cal.add(Calendar.DATE, +29);
			        supplierQuoteElement.setValidity(cal.getTime());
				}
				supplierQuoteElement.setWarranty(getString(request, "warranty"));
				supplierQuoteElement.setSerialNumber(getString(request, "serialNumber"));
				supplierQuoteElement.setTagSrc(getString(request, "tagSrc"));
				supplierQuoteElement.setTagDate(getString(request, "tagDate"));
				supplierQuoteElement.setTrace(getString(request, "trace"));
				supplierQuoteElement.setRemark(getString(request, "remark"));
				if (id == 1 || id.equals(1)) {
					supplierQuoteElement.setClientInquiryElementId(new Integer(getString(request, "clientInquiryElementId")));
					supplierQuoteElement.setClientInquiryId(new Integer(getString(request, "clientInquiryId")));
					String supplierCode = getString(request, "supplierCode");
					if (supplierCode != null && !"".equals(supplierCode)) {
						Supplier supplier = supplierService.selectByCode(supplierCode.trim());
						if (supplier != null) {
							supplierQuoteElement.setSupplierId(supplier.getId());
						}else {
							return new EditRowResultVo(false, "供应商不存在！");
						}
					}else {
						return new EditRowResultVo(false, "供应商不存在！");
					}
					
					supplierQuoteElement.setSupplierCode((getString(request, "supplierCode")));
					supplierQuoteElemenetService.addQuote(supplierQuoteElement);
				}else {
					supplierQuoteElement.setId(id);
					supplierQuoteElemenetService.updateByPrimaryKeySelective(supplierQuoteElement);
				}
				success = true;
				message = "修改成功!";
			}else {
				Integer supplierId = null;
				String supplierCode = getString(request, "supplierCode");
				if (supplierCode != null && !"".equals(supplierCode)) {
					Supplier supplier = supplierService.selectByCode(supplierCode.trim());
					if (supplier != null) {
						supplierId = supplier.getId();
					}else {
						return new EditRowResultVo(false, "供应商不存在！");
					}
				}else {
					return new EditRowResultVo(false, "供应商不存在！");
				}
				Integer clientInquiryElementId = new Integer(getString(request, "clientInquiryElementId"));
				success = supplierInquiryElementService.insertInquiry(supplierId, clientInquiryElementId);
				if (success) {
					success = true;
					message = "修改成功!";
				}else {
					success = false;
					message = "修改失败!";
				}
			}
		} catch (Exception e) {
			success = false;
			message = "修改失败!";
			e.printStackTrace();
		}
		
		return new EditRowResultVo(success, message);
		
	}
	
	/*
	 * 新增或修改报价
	 */
	@RequestMapping(value="/addOrEditQuoteBtn",method=RequestMethod.POST)
	public @ResponseBody ResultVo addOrEditQuoteBtn(HttpServletRequest request,@ModelAttribute SupplierQuoteElement supplierQuoteElement){
		boolean success = false;
		String message = "";
		UserVo userVo = getCurrentUser(request);
		try {
			String price = getString(request, "price");
			if (price != null && !"".equals(price)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				supplierQuoteElement.setCreateUser(new Integer(userVo.getUserId()));
				String unit  = getString(request, "unit");
				String supplierCode = getString(request, "supplierId");
				if (supplierCode != null && !"".equals(supplierCode)) {
					Supplier supplier = supplierService.selectByCode(supplierCode.trim());
					if (supplier != null) {
						supplierQuoteElement.setSupplierId(supplier.getId());
					}else {
						return new EditRowResultVo(false, "供应商不存在！");
					}
				}else {
					return new EditRowResultVo(false, "供应商不存在！");
				}
				supplierQuoteElemenetService.addQuote(supplierQuoteElement);
				success = true;
				message = "新增成功!";
			}else {
				Integer supplierId = null;
				String supplierCode = getString(request, "supplierId");
				if (supplierCode != null && !"".equals(supplierCode)) {
					Supplier supplier = supplierService.selectByCode(supplierCode.trim());
					if (supplier != null) {
						supplierId = supplier.getId();
					}else {
						return new EditRowResultVo(false, "供应商不存在！");
					}
				}else {
					return new EditRowResultVo(false, "供应商不存在！");
				}
				Integer clientInquiryElementId = new Integer(getString(request, "clientInquiryElementId"));
				success = supplierInquiryElementService.insertInquiry(supplierId, clientInquiryElementId);
				if (success) {
					success = true;
					message = "新增成功!";
				}else {
					success = false;
					message = "新增失败!";
				}
			}
		} catch (Exception e) {
			success = false;
			message = "新增失败!";
			e.printStackTrace();
		}
		
		return new ResultVo(success, message);
		
	}
	
	
	
	/**
	 * 双击修改时获取供应商
	 * @param request
	 * @param response
	 * @author Tanoy 2017-7-21 上午15：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/getSuppliers",method=RequestMethod.POST)
	public @ResponseBody ResultVo getSuppliers(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String supplierId=request.getParameter("supplierId");
		List<Supplier> list = supplierService.selectAll();
		List<Supplier> arraylist=new ArrayList<Supplier>();
		SystemCode element=new SystemCode();
		StringBuffer value = new StringBuffer();
		if (supplierId != null && !"".equals(supplierId)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(supplierId)) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().equals(supplierId)) {
					arraylist.add(list.get(i));
				}
			}
		}else {
			arraylist = list;
		}
		
		success = true;
		
		for (Supplier supplier : arraylist) {
			value.append(supplier.getId()).append(":").append(supplier.getCode()).append(";");
		}
		value.deleteCharAt(value.length()-1);
		msg=value.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 双击修改时获取证书
	 * @param request
	 * @param response
	 * @author Tanoy 2017-7-21 上午15：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/getCertifications",method=RequestMethod.POST)
	public @ResponseBody ResultVo getCertifications(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String certificationId=request.getParameter("certificationId");
		List<SystemCode> list = systemCodeService.findType("CERT");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		SystemCode element=new SystemCode();
		StringBuffer value = new StringBuffer();
		if (certificationId != null && !"".equals(certificationId)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(certificationId)) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().equals(certificationId)) {
					arraylist.add(list.get(i));
				}
			}
		}else {
			arraylist = list;
		}
		
		success = true;
		
		for (SystemCode systemCode : arraylist) {
			value.append(systemCode.getId()).append(":").append(systemCode.getCode()).append(";");
		}
		value.deleteCharAt(value.length()-1);
		msg=value.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 双击修改时获取状态
	 * @param request
	 * @param response
	 * @author Tanoy 2017-7-21 上午15：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/getConditions",method=RequestMethod.POST)
	public @ResponseBody ResultVo getConditions(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String conditionId=request.getParameter("conditionId");
		List<SystemCode> list = systemCodeService.findType("COND");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		SystemCode element=new SystemCode();
		StringBuffer value = new StringBuffer();
		if (conditionId != null && !"".equals(conditionId)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(conditionId)) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().equals(conditionId)) {
					arraylist.add(list.get(i));
				}
			}
		}else {
			arraylist = list;
		}
		
		success = true;
		
		for (SystemCode systemCode : arraylist) {
			value.append(systemCode.getId()).append(":").append(systemCode.getCode()).append(";");
		}
		value.deleteCharAt(value.length()-1);
		msg=value.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 双击修改时获取币种
	 * @param request
	 * @param response
	 * @author Tanoy 2017-7-21 上午15：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/getCurrencys",method=RequestMethod.POST)
	public @ResponseBody ResultVo getCurrencys(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String currencyId=request.getParameter("currencyId");
		List<SystemCode> list = systemCodeService.findType("CURRENCY");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		SystemCode element=new SystemCode();
		StringBuffer value = new StringBuffer();
		if (currencyId != null && !"".equals(currencyId)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(currencyId)) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().equals(currencyId)) {
					arraylist.add(list.get(i));
				}
			}
		}else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCode().equals("USD")) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getCode().equals("USD")) {
					arraylist.add(list.get(i));
				}
			}
		}
		
		success = true;
		
		for (SystemCode systemCode : arraylist) {
			value.append(systemCode.getId()).append(":").append(systemCode.getCode()).append(";");
		}
		value.deleteCharAt(value.length()-1);
		msg=value.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 双击修改时获取币种
	 * @param request
	 * @param response
	 * @author Tanoy 2017-7-21 上午15：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/getCurrency",method=RequestMethod.POST)
	public @ResponseBody ResultVo getCurrency(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String currencyId=request.getParameter("currencyId");
		List<SystemCode> list = systemCodeService.findType("CURRENCY");
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		SystemCode element=new SystemCode();
		StringBuffer value = new StringBuffer();
		if (currencyId != null && !"".equals(currencyId)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().toString().equals(currencyId)) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getId().toString().equals(currencyId)) {
					arraylist.add(list.get(i));
				}
			}
		}else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCode().equals("USD")) {
					arraylist.add(list.get(i));
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).getCode().equals("USD")) {
					arraylist.add(list.get(i));
				}
			}
		}
		
		success = true;
		JSONArray json = new JSONArray();
		json.add(arraylist);
		return new ResultVo(success, json.toString());
	}
	
	/**
	 * 跳转爬虫结果页面
	 * @return
	 */
	@RequestMapping(value="/toCrawList",method=RequestMethod.GET)
	public String toCrawList(){
		return "/purchase/craw/list";
	}
	
	/**
	 * 爬虫结果
	 */
	@RequestMapping(value="/crawerList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo crawerList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SatairQuoteElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String quoteStatus = request.getParameter("quoteStatus");
		String where = request.getParameter("searchString");
		if (quoteStatus != null && !"".equals(quoteStatus)) {
			where = where+" and "+quoteStatus;
		}
		
		satairQuoteService.listPage(page, where, sort);		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SatairQuoteElement satairQuoteElement : page.getEntities()) {
				if ("$".equals(satairQuoteElement.getCurrency())) {
					satairQuoteElement.setCurrency("USD");
				}else if ("€".equals(satairQuoteElement.getCurrency())) {
					satairQuoteElement.setCurrency("EUR");
				}else if ("￡".equals(satairQuoteElement.getCurrency())) {
					satairQuoteElement.setCurrency("GBP");
				}else if ("HK$".equals(satairQuoteElement.getCurrency())) {
					satairQuoteElement.setCurrency("HK");
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(satairQuoteElement);
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
	 * 爬虫结果
	 */
	@RequestMapping(value="/crawerListOnce",method=RequestMethod.POST)
	public @ResponseBody ResultVo crawerListOnce(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SatairQuoteElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String quoteStatus = request.getParameter("quoteStatus");
		String where = request.getParameter("searchString");
		if (quoteStatus != null && !"".equals(quoteStatus)) {
			where = where+" and "+quoteStatus;
		}
		JSONArray json = new JSONArray();
		satairQuoteService.list(page, where, sort);		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SatairQuoteElement satairQuoteElement : page.getEntities()) {
				if ("$".equals(satairQuoteElement.getCurrency())) {
					satairQuoteElement.setCurrency("USD");
				}else if ("€".equals(satairQuoteElement.getCurrency())) {
					satairQuoteElement.setCurrency("EUR");
				}else if ("￡".equals(satairQuoteElement.getCurrency())) {
					satairQuoteElement.setCurrency("GBP");
				}else if ("HK$".equals(satairQuoteElement.getCurrency())) {
					satairQuoteElement.setCurrency("HK");
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(satairQuoteElement);
				mapList.add(map);
			}
			json.add(mapList);
			//jqgrid.setRows(mapList);
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		return new ResultVo(true, json.toString());
	}
	
	/**
	 * 跳转vas库存附件列表
	 * 
	 * @param request
	 * @return
	 * @author tanoy
	 * @version v1.0
	 */
	@RequestMapping(value="/toPdfList",method=RequestMethod.GET)
	public String toPdfList(HttpServletRequest request){
		String id = getString(request, "id");
		List<String> parts = vasStrogeService.getShortPart(new Integer(id));
		request.setAttribute("data", parts.get(0));
		return "/purchase/historyquote/pdflist";
	}
	
	/**
	 * vas库存附件列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @author tanoy
	 * @version v1.0
	 */
	@RequestMapping(value = "/list/data", method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo attachmentlistdata(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<VasStroge> page = getPage(request);
		GridSort sort = getSort(request);
		String partNumber = getString(request, "partNumber");
		page.put("partNumber", partNumber);
		
		JQGridMapVo jqgrid = new JQGridMapVo();
		vasStrogeService.getFileInformationPage(page, "", sort);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (VasStroge vasStroge : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(vasStroge);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());
		}

		// 导出
		if (StringUtils.isNotEmpty(request.getParameter("exportModel"))) {
			try {
				exportService.exportGridToXls("附件列表",
						request.getParameter("exportModel"), jqgrid.getRows(),
						response);
				return null;
			} catch (Exception e) {
				logger.warn("导出数据出错!", e);
			}
		}
		return jqgrid;
	}
	
	/**
	 * 下载方法
	 * 
	 * @param response
	 * @return
	 * @author tanoy
	 * @version v1.0
	 */
	@RequestMapping(value = "/download/{ids}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(HttpServletResponse response,
			@PathVariable("ids") String ids) {
		String[] idarr = ids.split(",");
		if (idarr == null || idarr.length == 0)
			return null;

		if (idarr.length == 1) {// 只有一个附件时直接下载
			String id = idarr[0];
			VasStroge vas = vasStrogeService.selectByPrimaryKey(new Integer(id));
			String fileName = vas.getFileName()+".pdf";
			if (vas != null) {
				HttpHeaders headers = new HttpHeaders();
				try {
					headers.setContentDispositionFormData("attachment",
							new String(fileName.getBytes("UTF-8"),
									"ISO-8859-1"));
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					//headers.setContentLength(fj.getFjLength().longValue());
					return new ResponseEntity<byte[]>(
							FileUtils.readFileToByteArray(new File(
									FileConstant.UPLOAD_REALPATH
											+ vas.getFilePath())), headers,
							HttpStatus.OK);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {// 多个附件，压缩后下载
			List<VasStroge> fjs = vasStrogeService.findByIds(idarr);
			ZipOutputStream zos;
			try {
				response.setContentType("application/zip");
				String fileName = "附件打包.zip";
				response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO-8859-1")); 
				if (fjs != null && fjs.size() > 0) {
					// 压缩下载
					byte[] buffer = new byte[1024];
						zos = new ZipOutputStream(response.getOutputStream());
						for (VasStroge vasStroge : fjs) {
							String subFileName = vasStroge.getFileName()+".pdf";
							FileInputStream fis = new FileInputStream(
									new File(FileConstant.UPLOAD_REALPATH
											+ vasStroge.getFilePath()));
							zos.putNextEntry(new ZipEntry(new String(subFileName)));
							int len;
							while ((len = fis.read(buffer)) > 0) {
								zos.write(buffer, 0, len);
							}
							zos.setEncoding("UTF-8");
							zos.closeEntry();
							fis.close();
						}
						zos.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 检测供应商是否存在
	 * @param request
	 * @return
	 */
	/**
	@Author: Modify by white
	@DateTime: 2018/9/27 10:49
	@Description: 修改供应商验证，用户没有改权限的时候返回false
	*/
	@RequestMapping(value = "/checkSupplier",method = RequestMethod.POST)
	public @ResponseBody ResultVo checkSupplier(HttpServletRequest request){
		try {
			String supplierCode = getString(request, "supplierCode").trim();
			Supplier supplier = supplierService.selectByCode(supplierCode);
			if (supplier != null) {
				//获取当前登陆用户id
				UserVo userVo = getCurrentUser(request);
				String userId = userVo.getUserId();
				//通过用户id和供应商code判断用户是否有权限
				List<Supplier> supplierList = supplierService.getSupplierByCodeAndUserId(userId,supplierCode);
				if(supplierList != null && supplierList.size()>0){
					Double counterFee = 0.00;
					if (supplier.getCounterFee() != null && !"".equals(supplier.getCounterFee())) {
						counterFee = supplier.getCounterFee();
					}
					JSONArray json = new JSONArray();
					json.add(supplier);
					return new ResultVo(true, json.toString());
				}else{
					return new ResultVo(false,"权限不足，请重新选择供应商");
				}

			}else {
				return new ResultVo(false, "供应商不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
		}
	}
	
	
	/*
	 * 货币信息
	 */
	@RequestMapping(value="/currencyType",method=RequestMethod.POST)
	public @ResponseBody ResultVo currencyType(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		String id = getString(request, "id");
		try {
			List<ClientQuoteVo> list2 = systemCodeService.findRate();
			List<ClientQuoteVo> list = new ArrayList<ClientQuoteVo>();
			for (ClientQuoteVo clientQuoteVo : list2) {
				if (clientQuoteVo.getCurrency_id().equals(new Integer(id))) {
					list.add(clientQuoteVo);
				}
			}
			for (ClientQuoteVo clientQuoteVo : list2) {
				if (!clientQuoteVo.getCurrency_id().equals(new Integer(id))) {
					list.add(clientQuoteVo);
				}
			}
			JSONArray json = new JSONArray();
			json.add(list);
			message = json.toString();
		} catch (Exception e) {
			// TODO: handle exception
			success=false;
		}

		return new ResultVo(success, message);
	}
	
	
	/**
	 * 校验总价
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkTotal",method = RequestMethod.POST)
	public @ResponseBody ResultVo checkTotal(HttpServletRequest request){
		try {
			Double price = new Double(getString(request, "price"));
			Double amount = new Double(getString(request, "amount"));
			Double total = new Double(getString(request, "total"));
			BigDecimal p = new BigDecimal(price);
			BigDecimal a = new BigDecimal(amount);
			BigDecimal t = p.multiply(a).setScale(4, BigDecimal.ROUND_HALF_UP);
			if (t.doubleValue() == total.doubleValue()) {
				return new ResultVo(true, "校验通过！");
			}else {
				return new ResultVo(false, "填写总价与录入总价不一致，请从新复核修改再录入！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "校验异常！");
		}
	}
	
	/**
	 * 根据报价单校验总价
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkTotalByQuote",method = RequestMethod.POST)
	public @ResponseBody ResultVo checkTotalByQuote(HttpServletRequest request){
		try {
			Integer supplierQuoteId = new Integer(getString(request, "id"));
			Double total = new Double(getString(request, "total"));
			Double pTotal = supplierQuoteElemenetService.getTotalByQuoteId(supplierQuoteId);
			if (pTotal.doubleValue() == total.doubleValue()) {
				return new ResultVo(true, "校验通过！");
			}else {
				SupplierQuote supplierQuote = supplierQuoteService.selectByPrimaryKey(supplierQuoteId);
				supplierQuote.setQuoteStatusId(91);
				supplierQuoteService.updateByPrimaryKeySelective(supplierQuote);
				return new ResultVo(false, "填写总价与录入总价不一致,请复核后修改,修改完成后修改状态为有效");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "校验异常！");
		}
	}
	
	/**
	 * 跳转到爬虫价格大于5000的报价信息页面
	 * @return
	 */
	@RequestMapping(value="/toHighPricePage",method=RequestMethod.GET)
	public String toHighPricePage(){
		return "/purchase/craw/highpricelist";
	}
	
	/**
	 * 爬虫价格大于5000的报价信息
	 *
	 */
	@RequestMapping(value="/highPrice",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo highPrice(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<SupplierQuoteElement> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		supplierQuoteElemenetService.getHighPriceCrawl(page,getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierQuoteElement supplierQuoteElement : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteElement);
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
	 * dasi邮件
	 * @return
	 */
	@RequestMapping(value="/checkDasiCrawlMessage",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkDasiCrawlMessage(){
		try {
			dasiService.sendDasiEmail();
			return new ResultVo(true, "");
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
