package com.naswork.module.marketing.controller.clientquote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.naswork.filter.ContextHolder;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.Client;
import com.naswork.model.ClientInquiry;
import com.naswork.model.ClientInquiryElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientQuote;
import com.naswork.model.ClientQuoteElement;
import com.naswork.model.ClientQuoteElementUpload;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.Currency;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ExportPackage;
import com.naswork.model.HistoricalOrderPrice;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.QuoteBankCharges;
import com.naswork.model.SupplierOrderElement;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SystemCode;
import com.naswork.model.gy.GyFj;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientQuoteElementService;
import com.naswork.service.ClientQuoteElementUploadService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.CompetitorService;
import com.naswork.service.CompetitorSupplierService;
import com.naswork.service.ExchangeRateService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.ExportPackageService;
import com.naswork.service.GyFjService;
import com.naswork.service.HistoricalOrderPriceService;
import com.naswork.service.HistoricalQuotationService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.ImportpackageElementService;
import com.naswork.service.OrderApprovalService;
import com.naswork.service.QuoteBankChargesService;
import com.naswork.service.StaticClientQuotePriceService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierQuoteElementService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.ColumnVo;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/clientquote")
public class ClientQuoteController extends BaseController{
	@Resource
	private GyFjService gyFjService;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private ClientQuoteElementService clientQuoteElementService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElementService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private SupplierQuoteElementService supplierQuoteElemenetService;
	@Resource
	private ClientQuoteElementUploadService clientQuoteElementUploadService;
	@Resource
	private UserService userService;
	@Resource
	private OrderApprovalService orderApprovalService;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private StaticClientQuotePriceService staticClientQuotePriceService;
	@Resource
	private ClientService clientService;
	@Resource
	private CompetitorService competitorService;
	@Resource
	private QuoteBankChargesService quoteBankChargesdService;
	@Resource
	private ExchangeRateService exchangeRateService;
	@Resource
	private ImportPackageService importPackageService;
	@Resource
	private ImportpackageElementService importpackageElementService;
	@Resource
	private ExportPackageService exportPackageService;
	@Resource
	private ExportPackageElementService exportPackageElementService;
	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private CompetitorSupplierService competitorSupplierService;
	@Resource
	private HistoricalOrderPriceService historicalOrderPriceService;
	
	public static final int AVERAGE_PRICE_COUNT = 3;
	
	public static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat(
			"#.#%");
	/**
	 * 客户报价列表页面
	 * 
	 */
	@RequestMapping(value="/viewlist",method=RequestMethod.GET)
	public String viewlist(HttpServletRequest request){
		return "/marketing/clientquote/clientquotelist";
	}
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/clientquotedate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo queryclientquote(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<ClientQuoteVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
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
		clientQuoteService.findClientQuotePage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientQuoteVo clientQuoteVo : page.getEntities()) {
				Double ProfitMargin=clientQuoteVo.getProfitMargin();
				BigDecimal profitMargin=new BigDecimal(ProfitMargin);
				clientQuoteVo.setProfitMargin(clientQuoteService.caculateRevenueRate(profitMargin).doubleValue());//把double类型浮点数精确到两位
				Double total = clientQuoteElementService.getTotalById(clientQuoteVo.getId());
				if (total != null) {
					clientQuoteVo.setTotal(total);
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientQuoteVo);
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
	 * 明细列表
	 * **/
	@RequestMapping(value="clientquoteelementlist",method=RequestMethod.GET)
	public String clientquoteelementlist(HttpServletRequest request){
		request.setAttribute("client_inquiry_quote_number", request.getParameter("client_inquiry_quote_number"));
		request.setAttribute("clientInquiryId", request.getParameter("clientInquiryId"));
		request.setAttribute("clientquoteid", request.getParameter("clientquoteid"));//查询条件
	//	request.setAttribute("fixedCost", request.getParameter("fixedCost"));
		return "/marketing/clientquote/clientquoteelementlist";
	}
	
	/**
	 * 明细列表数据
	 * **/
	@RequestMapping(value="/clientquoteelementdate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo queryclientquoteelement(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteElementVo clientQuoteElementV){
		PageModel<ClientQuoteElementVo> page = getPage(request);
		PageModel<ClientQuoteElementVo> page2 = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = "";
		Integer id=0;
		List id2=new ArrayList();
		int k=0;
		List HAS_HISTORY_QUOTE=new ArrayList();//判断是否有历史供应商
		List HAS_SUPPLIER_QUOTE=new ArrayList();//判断是否有供应商报价
//		String clientinquiryquotenumber = request.getParameter("clientinquiryquotenumber");
//		String clientInquiryId = request.getParameter("clientInquiryId");
		String clientquoteid = request.getParameter("clientquoteid");
		ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(new Integer(clientquoteid));
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
		Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
		String supplier_quote = request.getParameter("supplier_quote");
		List<ClientQuoteVo> cqelist=clientQuoteService.findbyclientquoteid(clientquoteid);//客户报价信息
		List<ClientQuoteElementVo> searchlist=new ArrayList<ClientQuoteElementVo>();
		List<ClientQuoteElementVo> cielist=new ArrayList<ClientQuoteElementVo>();
		//BigDecimal fixedCost=new BigDecimal(cqelist.get(0).getFixedCost());
		List elist=new ArrayList();
		List searchelist=new ArrayList();
		for (ClientQuoteVo clientQuoteVo : cqelist) {
			id=clientQuoteVo.getId();
			if(null!=supplier_quote&&!"".equals(supplier_quote)){
				page.put("supplier_quote", supplier_quote);
			}
			page.put("clientInquiryId", clientQuoteVo.getClient_inquiry_id());
			cielist=clientQuoteService.findElementid(page,getSort(request));//客户询价信息
//			page.setEntities(cielist);//主要用于得到条数
			if(cielist!=null){
				for (ClientQuoteElementVo clientQuoteElementVo : cielist) {
					List<ClientQuoteElementVo> sqelist=clientQuoteService.findbyelementid(clientQuoteElementVo.getElementId());//根据elementid查是否有历史供应商
					elist.add(clientQuoteElementVo.getId());
					id2.add(clientQuoteElementVo.getId());
					if(sqelist==null||sqelist.isEmpty()){
						HAS_HISTORY_QUOTE.add("FALSE");//没有历史供应商
						HAS_SUPPLIER_QUOTE.add("FALSE");//没有供应商报价
					}else{
						HAS_HISTORY_QUOTE.add("TRUE");
						HAS_SUPPLIER_QUOTE.add("FALSE");
						for (ClientQuoteElementVo clientQuoteElementVo2 : sqelist) {
							if(clientQuoteElementVo2.getClientInquiryId().equals(clientQuoteVo.getClient_inquiry_id())){
								HAS_SUPPLIER_QUOTE.remove(HAS_SUPPLIER_QUOTE.size()-1);//去掉再新增
								HAS_SUPPLIER_QUOTE.add("TRUE");
								break;
							}
						}
					}
//				page.put("clientinquiryelementid", clientQuoteElementVo.getId());
				}
			}
			
			page2.put("clientquoteid", clientQuoteVo.getId());//查询条件
			
		}
			page.setEntities(cielist);//主要用于得到条数
		
		
		List<ClientQuoteElementVo> list=new ArrayList<ClientQuoteElementVo>();
		for (int i = 0; i < elist.size(); i++) {
			page2.put("clientinquiryelementid", elist.get(i));//查询条件
			clientQuoteService.findClientQuoteElementPage(page2, searchString, getSort(request));
			list.addAll(page2.getEntities());//循环查询，避免覆盖
		}
		page2.getEntities().clear();
		page2.getEntities().addAll(list);
//		if(page2.getEntities().size()>1&&page2.getEntities().size()>elist.size()){
//			page2.getEntities().remove(0);//去掉重复的第一条
//		}

		
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());//1
			jqgrid.setRecords(page.getRecordCount());//27
			jqgrid.setTotal(page.getPageCount());//3
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			
			
			for (ClientQuoteElementVo clientQuoteElementVo : page.getEntities()) {
				List<Integer> integers=staticClientQuotePriceService.getClients();
				
				clientQuoteElementVo.setClientQuoteId(id);
				Integer clientinquiryelementid=(Integer) id2.get(k);
				clientQuoteElementVo.setClientInquiryElementId(clientinquiryelementid);
				for (ClientQuoteElementVo clientQuoteElementVo2 : page2.getEntities()) {
					clientQuoteElementVo.setId(clientQuoteElementVo2.getId());
					if(clientQuoteElementVo.getClientInquiryElementId().equals(clientQuoteElementVo2.getClientInquiryElementId())){//把询价的list和供应商，客户报价的list组装起来
						
						clientQuoteElementVo.setWarranty(clientQuoteElementVo2.getWarranty());
						clientQuoteElementVo.setTagSrc(clientQuoteElementVo2.getTagSrc());
						clientQuoteElementVo.setTagDate(clientQuoteElementVo2.getTagDate());
						clientQuoteElementVo.setTrace(clientQuoteElementVo2.getTrace());
						clientQuoteElementVo.setSerialNumber(clientQuoteElementVo2.getSerialNumber());
						clientQuoteElementVo.setSupplierQuoteElementId(clientQuoteElementVo2.getSupplierQuoteElementId());
						clientQuoteElementVo.setClientInquiryElementId(clientQuoteElementVo2.getClientInquiryElementId());
						clientQuoteElementVo.setInquiryRemark(clientQuoteElementVo2.getInquiryRemark());
						clientQuoteElementVo.setClientQuoteAmount(clientQuoteElementVo2.getClientQuoteAmount());
						clientQuoteElementVo.setClientQuotePrice(clientQuoteElementVo2.getClientQuotePrice());
						clientQuoteElementVo.setPaymentRule(clientQuoteElementVo2.getPaymentRule());
						clientQuoteElementVo.setQuoteLocation(clientQuoteElementVo2.getQuoteLocation());
						clientQuoteElementVo.setCqeLeadTime(clientQuoteElementVo2.getCqeLeadTime());
						clientQuoteElementVo.setFixedCost(clientQuoteElementVo2.getFixedCost());
						clientQuoteElementVo.setBankCharges(clientQuoteElementVo2.getBankCharges());
						BigDecimal p1rice=new BigDecimal(clientQuoteElementVo2.getClientQuotePrice());
						BigDecimal amount=new BigDecimal(clientQuoteElementVo2.getClientQuoteAmount());
						BigDecimal countprice=( amount.multiply(p1rice));//数量乘单价
						clientQuoteElementVo.setCountprice(countprice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
						BigDecimal freight=new BigDecimal(clientQuoteElementVo2.getFreight());
						if(null!=clientQuoteElementVo2.getClientQuotePrice()){
							BigDecimal price=new BigDecimal(clientQuoteElementVo2.getClientQuotePrice());
						if(null!=clientQuoteElementVo2.getSupplierQuotePrice()){
							BigDecimal sqePrice=new BigDecimal(clientQuoteElementVo2.getSupplierQuotePrice());
						if(null!=clientQuoteElementVo2.getExchangeRate()){
							BigDecimal er=new BigDecimal(clientQuoteElementVo2.getExchangeRate());
						if(null!=clientQuoteElementVo2.getSupplierQuoteExchangeRate()){
							BigDecimal sqER=new BigDecimal(clientQuoteElementVo2.getSupplierQuoteExchangeRate());
							Integer sqcurrencyId=clientQuoteElementVo2.getSqcurrencyId();
							Integer cqcurrencyId=clientQuoteElementVo2.getCurrencyId();
							if(!sqcurrencyId.equals(cqcurrencyId)){
								//sqePrice = clientQuoteService.caculatePrice(sqePrice, sqER,er);//算出利润
								//判断是否7开头客户
								ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
								if (client.getCode().startsWith("7") && clientQuoteElementVo2.getRelativeRate() != null && clientQuoteElementVo2.getRelativeRate() > 0) {
									if (!sqcurrencyId.toString().equals("11")) {
										BigDecimal usBD = new BigDecimal(Double.toString(usRate.getRate()));   
										Double relativeRate = sqER.divide(usBD,4,BigDecimal.ROUND_HALF_UP).doubleValue();
										relativeRate = relativeRate*usRate.getRelativeRate();
										sqePrice = clientQuoteService.caculatePrice(sqePrice, new BigDecimal(relativeRate),er);
									}else {
										sqePrice = clientQuoteService.caculatePrice(sqePrice, new BigDecimal(usRate.getRelativeRate()),er);
									}
								}else {
									//正常的价格计算方法
									sqePrice = clientQuoteService.caculatePrice(sqePrice, sqER,er);
								}
							}
							BigDecimal fixedCost=new BigDecimal(clientQuoteElementVo2.getFixedCost());
							BigDecimal bankCharges=new BigDecimal(clientQuoteElementVo2.getBankCharges());
							DecimalFormat xs = new DecimalFormat("#.##");
							if (fixedCost.doubleValue() == new Double(0)) {
								fixedCost=BigDecimal.ZERO;
							}else if (fixedCost.doubleValue() < new Double(1)) {
								fixedCost = new BigDecimal(fixedCost.doubleValue() * clientQuoteElementVo2.getClientQuotePrice());
							}
							if (bankCharges.doubleValue() == new Double(0)) {
								bankCharges=BigDecimal.ZERO;
							}else if (bankCharges.doubleValue() < new Double(1)) {
								bankCharges = new BigDecimal(bankCharges.doubleValue() * clientQuoteElementVo2.getClientQuotePrice());
							}
							
							BigDecimal counterFee=new BigDecimal(0.0);
							if(null!=clientQuoteElementVo2.getCounterFee()&&0!=clientQuoteElementVo2.getCounterFee()){
								 counterFee=new BigDecimal(clientQuoteElementVo2.getCounterFee());
								if(counterFee.compareTo(BigDecimal.ONE)==-1){
									counterFee=sqePrice.multiply(counterFee);
//									sqePrice=sqePrice.add(counterFee);
								}
							}
							Double bankCost = 0.0;
							Double feeForExchangeBill = 0.0;
							Double hazmatFee = 0.0;
							Double otherFee = 0.0;
							ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
							SupplierQuoteElement supplierQuoteElement = supplierQuoteElementService.selectByPrimaryKey(clientQuoteElementVo2.getSupplierQuoteElementId());
							if (supplierQuoteElement.getHazmatFee() != null && !"".equals(supplierQuoteElement.getHazmatFee())) {
								if (client.getCode().startsWith("7")) {
									BigDecimal fee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getHazmatFee()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(clientQuoteElementVo2.getExchangeRate()));
									hazmatFee = fee.doubleValue();
								}else {
									BigDecimal fee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientQuoteElementVo2.getExchangeRate()));
									hazmatFee = fee.doubleValue();
								}
								
							}
							if (!client.getCode().startsWith("3")) {
								if (supplierQuoteElement.getFeeForExchangeBill() != null && !"".equals(supplierQuoteElement.getFeeForExchangeBill())) {
									BigDecimal fee = new BigDecimal(0);
									if (client.getCode().startsWith("7")) {
										fee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getFeeForExchangeBill()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(clientQuoteElementVo2.getExchangeRate()));
									}else {
										fee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(clientQuoteElementVo2.getExchangeRate()));
									}
									if (clientQuoteElementVo2.getClientMoq() != null) {
										if (clientQuoteElementVo2.getClientMoq() > clientQuoteElementVo2.getClientQuoteAmount()) {
											feeForExchangeBill = fee.doubleValue()/clientQuoteElementVo2.getClientMoq();
										}else {
											feeForExchangeBill = fee.doubleValue()/clientQuoteElementVo2.getClientQuoteAmount();
										}
									}else {
										feeForExchangeBill = fee.doubleValue()/clientQuoteElementVo2.getClientQuoteAmount();
									}
									
								}
							}
							if (supplierQuoteElement.getBankCost() != null && !"".equals(supplierQuoteElement.getBankCost())) {
								BigDecimal fee = new BigDecimal(0);
								if (client.getCode().startsWith("7")) {
									fee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getBankCost()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(clientQuoteElementVo2.getExchangeRate()));
								}else {
									fee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(clientQuoteElementVo2.getExchangeRate()));
								}
								if (fee.doubleValue() > 0) {
									if (clientQuoteElementVo2.getClientMoq() != null) {
										if (clientQuoteElementVo2.getClientMoq() > clientQuoteElementVo2.getClientQuoteAmount()) {
											bankCost = fee.doubleValue()/clientQuoteElementVo2.getClientMoq();
										}else {
											bankCost = fee.doubleValue()/clientQuoteElementVo2.getClientQuoteAmount();
										}
									}else {
										bankCost = fee.doubleValue()/clientQuoteElementVo2.getClientQuoteAmount();
									}
								}
							}
							if (supplierQuoteElement.getOtherFee() != null && !"".equals(supplierQuoteElement.getOtherFee())) {
								BigDecimal fee = new BigDecimal(0);
								if (client.getCode().startsWith("7")) {
									fee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getOtherFee()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(clientQuoteElementVo2.getExchangeRate()));
								}else {
									fee = clientQuoteService.caculatePrice(new BigDecimal(supplierQuoteElement.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientQuoteElementVo2.getExchangeRate()));
								}
								if (fee.doubleValue() > 0) {
									if (clientQuoteElementVo2.getClientMoq() != null) {
										if (clientQuoteElementVo2.getClientMoq() > clientQuoteElementVo2.getClientQuoteAmount()  && fee.doubleValue() > 0) {
											otherFee = fee.doubleValue()/clientQuoteElementVo2.getClientMoq();
										}else {
											otherFee = fee.doubleValue()/clientQuoteElementVo2.getClientQuoteAmount();
										}
									}else {
										otherFee = fee.doubleValue()/clientQuoteElementVo2.getClientQuoteAmount();
									}
								}
								
							}
//							BigDecimal profitMargit=price.doubleValue()==0?BigDecimal.ZERO:
//								new BigDecimal(1.00).subtract((sqePrice.add(freight).add(new BigDecimal(bankCost)).add(new BigDecimal(feeForExchangeBill)).add(new BigDecimal(hazmatFee))/*.add(counterFee)*/).divide((price).multiply(new BigDecimal(1.00).subtract(fixedCost).subtract(bankCharges)),2,BigDecimal.ROUND_HALF_UP));
							BigDecimal profitMargit = BigDecimal.ZERO;
							if (price.doubleValue() != 0) {
//								profitMargit = new BigDecimal((price.doubleValue() - bankCost - feeForExchangeBill - hazmatFee - bankCharges.doubleValue() - fixedCost.doubleValue() - sqePrice.doubleValue())/
//										(price.doubleValue() - bankCost - feeForExchangeBill - hazmatFee - bankCharges.doubleValue() - fixedCost.doubleValue()));
								BigDecimal pro = (price.subtract(new BigDecimal(bankCost)).subtract(new BigDecimal(feeForExchangeBill)).subtract(new BigDecimal(hazmatFee)).subtract(new BigDecimal(otherFee)).subtract(fixedCost).subtract(bankCharges).subtract(sqePrice));
								BigDecimal cost = (price.subtract(new BigDecimal(bankCost)).subtract(new BigDecimal(feeForExchangeBill)).subtract(new BigDecimal(hazmatFee)).subtract(new BigDecimal(otherFee)).subtract(fixedCost).subtract(bankCharges));
//								profitMargit = new BigDecimal((price.subtract(new BigDecimal(bankCost)).subtract(new BigDecimal(feeForExchangeBill)).subtract(new BigDecimal(hazmatFee)).subtract(new BigDecimal(otherFee)).subtract(fixedCost).subtract(bankCharges).subtract(sqePrice)).doubleValue()/
//										((price.subtract(new BigDecimal(bankCost)).subtract(new BigDecimal(feeForExchangeBill)).subtract(new BigDecimal(hazmatFee).subtract(new BigDecimal(otherFee))).subtract(fixedCost).subtract(bankCharges))).doubleValue());
								profitMargit = pro.divide(cost, 3, BigDecimal.ROUND_HALF_UP);
							}
							BigDecimal pm=profitMargit.multiply(new BigDecimal(100));//乘以100
							clientQuoteElementVo.setProfitMargin(pm.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
							clientQuoteElementVo.setSupplierQuotePrice(sqePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
						clientQuoteElementVo.setClientQuoteRemark(clientQuoteElementVo2.getClientQuoteRemark());
						clientQuoteElementVo.setQuoteupDatTimestamp(clientQuoteElementVo2.getQuoteupDatTimestamp());
						if(!integers.contains(clientInquiry.getClientId())){
							clientQuoteElementVo.setCounterFee(clientQuoteElementVo2.getCounterFee());
							clientQuoteElementVo.setSupplierCode(clientQuoteElementVo2.getSupplierCode());
							clientQuoteElementVo.setQuotePartNumber(clientQuoteElementVo2.getQuotePartNumber());
							clientQuoteElementVo.setQuoteRemark(clientQuoteElementVo2.getQuoteRemark());
							clientQuoteElementVo.setQuoteUnit(clientQuoteElementVo2.getQuoteUnit());
							clientQuoteElementVo.setSupplierQuotePrice(sqePrice.doubleValue());
							clientQuoteElementVo.setSupplierQuoteAmount(clientQuoteElementVo2.getSupplierQuoteAmount());
							clientQuoteElementVo.setCertificationCode(clientQuoteElementVo2.getCertificationCode());
							clientQuoteElementVo.setMoq(clientQuoteElementVo2.getMoq());
							clientQuoteElementVo.setConditionCode(clientQuoteElementVo2.getConditionCode());
							clientQuoteElementVo.setLocation(clientQuoteElementVo2.getLocation());
							clientQuoteElementVo.setFreight(clientQuoteElementVo2.getFreight());
							clientQuoteElementVo.setLeadTime(clientQuoteElementVo2.getLeadTime());
							clientQuoteElementVo.setMov(clientQuoteElementVo2.getMov());
						}else{
							clientQuoteElementVo.setSupplierCode(" ");
							clientQuoteElementVo.setSupplierQuotePrice(0.0);
						}
						break;
					}
				}
							}
						}
					
					}
					
				}
				
					if(null==clientQuoteElementVo.getSupplierCode()||"".equals(clientQuoteElementVo.getSupplierCode())){
						if(HAS_HISTORY_QUOTE.get(k).equals("FALSE")&&HAS_SUPPLIER_QUOTE.get(k).equals("FALSE")){
							clientQuoteElementVo.setSupplierCode("没有报价");
						}
						else if(HAS_HISTORY_QUOTE.get(k).equals("TRUE")&&HAS_SUPPLIER_QUOTE.get(k).equals("FALSE")){
							clientQuoteElementVo.setSupplierCode("有历史报价");
						}
						else{
							clientQuoteElementVo.setSupplierCode("有供应商报价");
						}
					}
				k++;
				Map<String, Object> map = EntityUtil.entityToTableMap(clientQuoteElementVo);
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
	 * 新增客户订单页面
	 * **/
	@RequestMapping(value="/addclientorder",method=RequestMethod.GET)
	public String addclientorder(HttpServletRequest request){
		request.setAttribute("client_quote_id", request.getParameter("client_quote_id"));
		request.setAttribute("client_inquiry_quote_number", request.getParameter("client_inquiry_quote_number"));
		String id=request.getParameter("client_quote_id");
		ClientQuote record=clientQuoteService.selectByPrimaryKey(Integer.valueOf(id));
		record.setPrepayRate(record.getPrepayRate()*100);
		record.setShipPayRate(record.getShipPayRate()*100);
		record.setReceivePayRate(record.getReceivePayRate()*100);
		request.setAttribute("record", record);
		return "/marketing/clientquote/addclientorder";
	}
	
	/**
	 * 保存新增数据
	 */
	@RequestMapping(value="/save",  method=RequestMethod.POST)
	public @ResponseBody ResultVo save(HttpServletRequest request, @ModelAttribute ClientOrder clientOrder)
	{
		boolean result = true;
		String message = "新增成功！";
		String clientinquiryquotenumber=request.getParameter("clientinquiryquotenumber"); 
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		int id=clientOrder.getClientQuoteId();
		Integer seq=clientQuoteService.findseq(id);
		int maxSeq;
		if(seq==null){
			maxSeq=0;
		}else{
			maxSeq=seq;
		}
		clientOrder.setSeq(++maxSeq);

		String orderNumber=clientinquiryquotenumber;
		if(maxSeq>1){
			orderNumber = orderNumber + "-" +maxSeq;
		}
		orderNumber = "ORD-" + orderNumber;//订单号组装
		clientOrder.setOrderNumber(orderNumber);
		clientOrder.setOrderStatusId(60);
		clientOrder.setPrepayRate(clientOrder.getPrepayRate()/100);
		clientOrder.setShipPayRate(clientOrder.getShipPayRate()/100);
		clientOrder.setReceivePayRate(clientOrder.getReceivePayRate()/100);
		clientOrder.setCreateUserId(Integer.parseInt(userId));
		clientOrderService.add(clientOrder);
		return new ResultVo(result, message);
	}

	/***
	 * 修改客户报价页面
	 * */
	@RequestMapping(value="/updateview",method=RequestMethod.GET)
	public String updateview(HttpServletRequest request){
		String id=request.getParameter("client_quote_id");
		request.setAttribute("client_quote_id", request.getParameter("client_quote_id"));
		request.setAttribute("client_inquiry_quote_number", request.getParameter("client_inquiry_quote_number"));
		ClientQuote record=clientQuoteService.selectByPrimaryKey(Integer.valueOf(id));
		List<ClientQuoteVo> list=clientQuoteService.searchcurrency();
		for (ClientQuoteVo clientQuoteVo : list) {
			if(clientQuoteVo.getCurrency_id().equals(record.getCurrencyId())){
				request.setAttribute("currencyvalue",clientQuoteVo.getCurrency_value());
			}
		}
		BigDecimal profitMargin=new BigDecimal(record.getProfitMargin());
		record.setPrepayRate(record.getPrepayRate()*100);
		record.setShipPayRate(record.getShipPayRate()*100);
		record.setReceivePayRate(record.getReceivePayRate()*100);
		request.setAttribute("record", record);
		request.setAttribute("profitmargin",clientQuoteService.caculateRevenueRate(profitMargin).doubleValue());
		return "/marketing/clientquote/updateclientquote";
	}
	
	/**
	 * 修改客户报价
	 */
	@RequestMapping(value="/updateclientquote",  method=RequestMethod.POST)
	public @ResponseBody ResultVo updateclientquote(HttpServletRequest request, @ModelAttribute ClientQuote record)
	{
		boolean result = true;
		String message = "修改完成！";
		
		clientQuoteService.updateByPrimaryKeySelective(record);
		return new ResultVo(result, message);
	}
	
	/**
	 * 新增客户报价明细页面
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/addquoteelement",method=RequestMethod.GET)
	public String addquoteelement(HttpServletRequest request) throws UnsupportedEncodingException{
		Integer clientInquiryElemenetId=Integer.parseInt(request.getParameter("clientInquiryElementId"));
		ClientInquiryElement clientInquiryElement=clientInquiryElementService.selectByPrimaryKey(clientInquiryElemenetId);
		Integer clientQuoteId = new Integer(request.getParameter("clientQuoteId"));
		ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientQuoteId);
		String type=request.getParameter("optType");
		
		request.setAttribute("clientInquiryElement", clientInquiryElement);
		request.setAttribute("clientInquiryQuoteNumber", request.getParameter("client_inquiry_quote_number"));
		request.setAttribute("clientQuoteId", request.getParameter("clientQuoteId"));
		request.setAttribute("optType", request.getParameter("optType"));
//		Double quoteTotalPrice=clientQuoteService.getQuotePrice(Integer.parseInt(request.getParameter("clientQuoteId")));
//		request.setAttribute("quoteTotalPrice", quoteTotalPrice);
//		request.setAttribute("fixedCost", request.getParameter("fixedCost"));
		request.setAttribute("id", request.getParameter("id"));
		String id = request.getParameter("id");
		if(null!=request.getParameter("id")){
			ClientQuoteElement clientQuoteElement=clientQuoteElementService.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
			request.setAttribute("clientQuoteElement", clientQuoteElement);
			request.setAttribute("fixedCost", clientQuoteElement.getFixedCost());
			request.setAttribute("bankCharges", clientQuoteElement.getBankCharges());
		}
		if(type.equals("add")){
			request.setAttribute("fixedCost", clientQuote.getFixedCost());
			ClientInquiry clientInquiry=clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
			QuoteBankCharges quoteBankCharges=quoteBankChargesdService.findByClientId(clientInquiry.getClientId());
			if(null!=quoteBankCharges){
				request.setAttribute("bankCharges", quoteBankCharges.getBankCharges());
			}
		}
		String sid = request.getParameter("supplierQuoteElementId");
		request.setAttribute("supplierQuoteElementId", request.getParameter("supplierQuoteElementId"));
		return "/marketing/clientquote/addquoteelement";
	}
	
	/**
	 * 新增供应商报价明细上传
	 */
	@RequestMapping(value="/addelement",method=RequestMethod.GET)
	public String addelement(HttpServletRequest request) {
//		String[] clientinquiryquotenumber=request.getParameter("clientinquiryquotenumber").split("-");
//		for (int i = 0; i < clientinquiryquotenumber.length; i++) {
//			request.setAttribute("clientinquiryquotenumber"+i, clientinquiryquotenumber[i]);
//		}
		request.setAttribute("clientinquiryquotenumber", request.getParameter("clientinquiryquotenumber"));
		return "/marketing/clientquote/addElement";
	}
	
	/**
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			SupplierQuote record) {
		boolean success=false;
		String message="";
//		String clientinquiryquotenumber0 =request.getParameter("clientinquiryquotenumber0");
//		String clientinquiryquotenumber1 =request.getParameter("clientinquiryquotenumber1");
//		String clientinquiryquotenumber2 =request.getParameter("clientinquiryquotenumber2");
		String clientinquiryquotenumber=request.getParameter("clientinquiryquotenumber");
//		if(null!=clientinquiryquotenumber2&&!"".equals(clientinquiryquotenumber2)){
//			clientinquiryquotenumber=clientinquiryquotenumber0+"-"+clientinquiryquotenumber1+"-"+clientinquiryquotenumber2;
//		}else{
//			clientinquiryquotenumber=clientinquiryquotenumber0+"-"+clientinquiryquotenumber1;
//		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		MessageVo messageVo = clientQuoteElementService.uploadExcel(multipartFile, clientinquiryquotenumber);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	
	/**
	 * 供应商客户报价数据
	 * **/
	@RequestMapping(value="/quotedate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo quotedate(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteElementVo clientQuoteElementVos){
		PageModel<ClientQuoteElementVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String clientQuoteId = request.getParameter("clientQuoteId");
		ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(new Integer(clientQuoteId));
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
		Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
		String clientInquiryElementId = request.getParameter("clientInquiryElementId");
		String searchString="";
		BigDecimal exchangeRate = BigDecimal.ZERO;
		BigDecimal averagePrice = BigDecimal.ZERO;
		BigDecimal lowestPrice = null;
		BigDecimal atPrice = BigDecimal.ZERO;
		BigDecimal compareRate = null;
		BigDecimal profitMargin = null;
		Integer cqCurrrncyId = null;
		Map competitormap = new HashMap();
		Integer clientinquiryelementid=null;
		List<ClientQuoteElementVo> list=new ArrayList<ClientQuoteElementVo>();
		List<ClientQuoteVo> cqelist=clientQuoteService.findbyclientquoteid(clientQuoteId);//客户报价信息
		List<ClientQuoteElementVo> cielist=clientQuoteService.findbycieid(clientInquiryElementId);//客户询价信息
		for (ClientQuoteVo clientQuoteVo : cqelist) {
			 cqCurrrncyId=clientQuoteVo.getCurrency_id();
			if(null!=clientQuoteVo.getExchangeRate()){
				exchangeRate=new BigDecimal(clientQuoteVo.getExchangeRate());
			}
			if(null!=clientQuoteVo.getProfitMargin()){
				profitMargin=new BigDecimal(clientQuoteVo.getProfitMargin());
			}
		}
		for (ClientQuoteElementVo clientQuoteElementVo : cielist) {
			page.put("elementid", clientQuoteElementVo.getElementId());
			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(new Integer(clientInquiryElementId));
			String partCode = clientInquiryService.getCodeFromPartNumber(clientInquiryElement.getPartNumber());
			page.put("partCode", partCode);
			clientQuoteService.findQuoteDatePage(page, searchString, getSort(request));
		}
		List<ClientQuoteElementVo> sqeList = new ArrayList<ClientQuoteElementVo>();
		List<ClientQuoteElementVo> sqeHistory = new ArrayList<ClientQuoteElementVo>();
		if (page.getEntities() != null && page.getEntities().size() > 0) {
			for (int i = 0; i < page.getEntities().size(); i++) {
				ClientQuoteElementVo map = (ClientQuoteElementVo) page.getEntities().get(i);
				BigDecimal sqER=null;
				if(null!=page.getEntities().get(i).getExchangeRate()){
					sqER=new BigDecimal(page.getEntities().get(i).getExchangeRate());
				}
				BigDecimal sqprice=null;
				if(null!=page.getEntities().get(i).getPrice()){
					sqprice=new BigDecimal(page.getEntities().get(i).getPrice());
				}
				
				BigDecimal price = sqprice;
				Integer sqCurrencyId=page.getEntities().get(i).getCurrencyId();
				
				if(!sqCurrencyId.equals(cqCurrrncyId)){
					//判断是否7开头客户
					ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
					if (client.getCode().startsWith("7") && usRate.getRelativeRate() != null && usRate.getRelativeRate() > 0) {
						if (!sqCurrencyId.toString().equals("11")) {
							BigDecimal usBD = new BigDecimal(Double.toString(usRate.getRate()));   
							Double relativeRate = sqER.divide(usBD,4,BigDecimal.ROUND_HALF_UP).doubleValue();
							relativeRate = relativeRate*usRate.getRelativeRate();
							price = clientQuoteService.caculatePrice(sqprice, new BigDecimal(relativeRate),exchangeRate);
						}else {
							price = clientQuoteService.caculatePrice(sqprice, new BigDecimal(usRate.getRelativeRate()),exchangeRate);
						}
						page.getEntities().get(i).setRelativeRate(usRate.getRelativeRate());
					}else {
						//正常的价格计算方法
						price = clientQuoteService.caculatePrice(sqprice, sqER,exchangeRate);
					}
					 
				}
				if (i < AVERAGE_PRICE_COUNT) {
					atPrice = atPrice.add(price);
				}
				page.getEntities().get(i).setPrice(price.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				BigDecimal amount =null;
				if(null!=page.getEntities().get(i).getQuoteAmount()){
					 amount =new BigDecimal(page.getEntities().get(i).getQuoteAmount());
				}
				page.getEntities().get(i).setCountprice(price.multiply(amount).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				BigDecimal cqER=null;
				if(null!=page.getEntities().get(i).getClientQuoteExchangeRate()){
					 cqER = new BigDecimal(page.getEntities().get(i).getClientQuoteExchangeRate());
				}
				BigDecimal cqPrice=null;
				if(null!=page.getEntities().get(i).getClientQuotePrice()){
					 cqPrice =new BigDecimal(page.getEntities().get(i).getClientQuotePrice());
				}
				if (cqPrice != null&&null!=cqER) {
					cqPrice = clientQuoteService.caculatePrice(cqPrice, cqER, exchangeRate);
					page.getEntities().get(i).setClientQuotePrice(cqPrice.doubleValue());
				}
				BigDecimal coER=null;
				if(null!=page.getEntities().get(i).getClientQuoteExchangeRate()){
					coER =new BigDecimal(page.getEntities().get(i).getClientQuoteExchangeRate());
				}
				if(null!=page.getEntities().get(i).getClientOrderPrice()){
					BigDecimal clientOrderPrice =new BigDecimal(page.getEntities().get(i).getClientOrderPrice());
				if (clientOrderPrice != null) {
					clientOrderPrice = clientQuoteService.caculatePrice(clientOrderPrice, coER,
							exchangeRate);
					page.getEntities().get(i).setClientOrderPrice(clientOrderPrice.doubleValue());
				}}
				//if(cqelist.get(0).getClient_inquiry_id().equals(page.getEntities().get(i).getClientInquiryId())){
				if(new Integer(clientInquiryElementId).equals(page.getEntities().get(i).getClientInquiryElementId())){
					sqeList.add(map);
					if (lowestPrice == null) {
						lowestPrice = price;
					} else if (lowestPrice.compareTo(price) > 0) {
						lowestPrice = price;
					}
				}else{
					clientinquiryelementid=page.getEntities().get(i).getClientInquiryElementId();
					List<CompetitorVo> competitorlist=clientQuoteService.findcompetitor(clientinquiryelementid);//竞争对手报价
					if(competitorlist!=null){
						StringBuffer competitor=new StringBuffer();
						for (int j = 0; j < competitorlist.size(); j++) {
							BigDecimal cqePrice =new BigDecimal(competitorlist.get(j).getPrice());
							BigDecimal cqRate =new BigDecimal(competitorlist.get(j).getExchangeRate());
							competitorlist.get(j).setPrice(clientQuoteService.caculatePrice(cqePrice, cqRate,
						exchangeRate).doubleValue());
							String competitorprice=competitorlist.get(j).getPrice()+"";
							String competitorcode=competitorlist.get(j).getCompetitorCode();
							competitor.append(competitorcode+":");
							competitor.append(competitorprice+" ");
							competitormap.put(clientinquiryelementid, competitor);
						}
					}
					sqeHistory.add(map);
				}
			}
					
			if(page.getEntities().size()>=AVERAGE_PRICE_COUNT){
				averagePrice = atPrice.divide(new BigDecimal(
						AVERAGE_PRICE_COUNT), 4, BigDecimal.ROUND_HALF_UP);
			}else{
				averagePrice = atPrice.divide(new BigDecimal(page.getEntities().size()), 4,
						BigDecimal.ROUND_HALF_UP);
			}
		
			if (lowestPrice == null || averagePrice == null) {
				compareRate = null;
			} else if (averagePrice.equals(BigDecimal.ZERO)
					|| lowestPrice.equals(BigDecimal.ZERO)
					|| averagePrice.floatValue() < 0.01
					|| lowestPrice.floatValue() < 0.01) {
				compareRate = null;
			} else {
				compareRate = lowestPrice.divide(averagePrice, 4,
						BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
//						compareRate=compareRate.multiply(new BigDecimal(100));
			}
		}
		list.addAll(sqeList);
		if(sqeHistory.size()>0){
			clientQuoteElementVos.setSupplierCode("历史供应商报价");
			list.add(clientQuoteElementVos);
		}
		list.addAll(sqeHistory);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientQuoteElementVo clientQuoteElementVo : list) {
				boolean isNum = false;
				Pattern pattern = Pattern.compile("[0-9]*"); 
/*				if (clientQuoteElementVo.getLeadTime() != null) {
					Matcher Num = pattern.matcher(clientQuoteElementVo.getLeadTime());
					if(Num.matches() && clientQuoteElementVo.getLeadTime() != null && !"".equals(clientQuoteElementVo.getLeadTime())){
						isNum = true;
					} 
					if (isNum) {
						Integer leadTime = new Integer(clientQuoteElementVo.getLeadTime());
						if (leadTime<10){
							leadTime = leadTime+1;
						}else if (leadTime>=10 && leadTime<30) {
							leadTime = leadTime+3;
						}else if (leadTime>=30 && leadTime<60) {
							leadTime = (int)(leadTime.doubleValue() * 1.1);
						}
						clientQuoteElementVo.setLeadTime(leadTime.toString());
					}
				}*/
				for (Object obj : competitormap.keySet()) {
					if(clientQuoteElementVo.getClientInquiryElementId().equals(obj)){
						clientQuoteElementVo.setCompetitor(competitormap.get(obj).toString());
					}
				}
				if(null!=clientQuoteElementVo.getPrice()){
					Double freight=0.0;
					Double counterFee=0.0;
					BigDecimal quotePrice = new BigDecimal(clientQuoteElementVo.getPrice()) 
							.multiply(profitMargin)
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					if(null!=clientQuoteElementVo.getFreight()&&0!=clientQuoteElementVo.getFreight()){
						freight=clientQuoteElementVo.getFreight();
					}
//					if(null!=clientQuoteElementVo.getCounterFee()&&0!=clientQuoteElementVo.getCounterFee()){
//						counterFee=clientQuoteElementVo.getCounterFee();
//						if(counterFee<1){
//							counterFee=clientQuoteElementVo.getPrice()*counterFee;
//							 BigDecimal bg = new BigDecimal(counterFee);  
//							 BigDecimal pg = new BigDecimal(clientQuoteElementVo.getPrice()); 
//					         counterFee = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
//							clientQuoteElementVo.setPrice(bg.add(pg).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//						}
//					}
					 quotePrice = new BigDecimal(clientQuoteElementVo.getPrice()+freight) 
							.multiply(profitMargin)
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					
					clientQuoteElementVo.setProfitmargin(profitMargin.doubleValue());
					Double profitmargin=null;
					Double sqPrice=clientQuoteElementVo.getPrice();
					Double price=quotePrice.doubleValue();
					clientQuoteElementVo.setQuotePrice(price);
					profitmargin=((price-(sqPrice+freight))/price*100);
					Double NAN = new Double(Double.NaN);
					if(null!=profitmargin&&!profitmargin.equals(NAN)){
						clientQuoteElementVo.setProfitMargin(new BigDecimal(profitmargin).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
					}
				}
				if(null!=averagePrice){
					clientQuoteElementVo.setAveragePrice(averagePrice.doubleValue());
				}
				if(null!=lowestPrice){
					clientQuoteElementVo.setLowestPrice(lowestPrice.doubleValue());
				}
				if(null!=compareRate){
					clientQuoteElementVo.setCompareRate(compareRate.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				int count = competitorService.getCountByClientAndSupplier(clientInquiry.getClientId(), clientQuoteElementVo.getSupplierId());
				if (count > 0) {
					clientQuoteElementVo.setIfCompetitor(1);
				}
				ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
				if (clientQuoteElementVo.getFeeForExchangeBill() != null && !"".equals(clientQuoteElementVo.getFeeForExchangeBill())) {
					if(!"11".equals(cqCurrrncyId.toString())){
						if (client.getCode().startsWith("7")) {
							BigDecimal feeForExchangeBill = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getFeeForExchangeBill()), new BigDecimal(usRate.getRelativeRate()),exchangeRate);
							clientQuoteElementVo.setFeeForExchangeBill(feeForExchangeBill.doubleValue());
						}else {
							BigDecimal feeForExchangeBill = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),exchangeRate);
							clientQuoteElementVo.setFeeForExchangeBill(feeForExchangeBill.doubleValue());
						}
						
					}
				}
				if (clientQuoteElementVo.getBankCost() != null && !"".equals(clientQuoteElementVo.getBankCost())) {
					if(!"11".equals(cqCurrrncyId.toString())){
						if (client.getCode().startsWith("7")) {
							BigDecimal counterFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getBankCost()), new BigDecimal(usRate.getRelativeRate()),exchangeRate);
							clientQuoteElementVo.setCounterFee(counterFee.doubleValue());
						}else {
							BigDecimal counterFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getBankCost()), new BigDecimal(usRate.getRate()),exchangeRate);
							clientQuoteElementVo.setCounterFee(counterFee.doubleValue());
						}
						
					}
				}
				if (clientQuoteElementVo.getHazmatFee() != null && !"".equals(clientQuoteElementVo.getHazmatFee())) {
					if(!"11".equals(cqCurrrncyId.toString())){
						if (client.getCode().startsWith("7")) {
							BigDecimal hazmatFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getHazmatFee()), new BigDecimal(usRate.getRelativeRate()),exchangeRate);
							clientQuoteElementVo.setHazmatFee(hazmatFee.doubleValue());
						}else {
							BigDecimal hazmatFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getHazmatFee()), new BigDecimal(usRate.getRate()),exchangeRate);
							clientQuoteElementVo.setHazmatFee(hazmatFee.doubleValue());
						}
					}
				}
				if (clientQuoteElementVo.getOtherFee() != null && !"".equals(clientQuoteElementVo.getOtherFee())) {
					if (client.getCode().startsWith("7")) {
						if(!"11".equals(cqCurrrncyId.toString())){
							BigDecimal otherFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getOtherFee()), new BigDecimal(usRate.getRelativeRate()),exchangeRate);
							clientQuoteElementVo.setOtherFee(otherFee.doubleValue());
						}
					}else {
						if(!"11".equals(cqCurrrncyId.toString())){
							BigDecimal otherFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getOtherFee()), new BigDecimal(usRate.getRate()),exchangeRate);
							clientQuoteElementVo.setOtherFee(otherFee.doubleValue());
						}
					}
				}
				//3字头客户不需要提货换单费
				if (client.getCode().startsWith("3")) {
					clientQuoteElementVo.setFeeForExchangeBill(new Double(0));
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientQuoteElementVo);
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
	 * 历史报价
	 * **/
	@RequestMapping(value="/quoteDateForHis",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo quoteDateForHis(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteElementVo clientQuoteElementVos){
		PageModel<ClientQuoteElementVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String clientId = request.getParameter("clientId");
		Client client = clientService.selectByPrimaryKey(new Integer(clientId));
		String clientInquiryElementId = request.getParameter("clientInquiryElementId");
		ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(new Integer(clientInquiryElementId));
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
		if (clientInquiryElement.getShortPartNumber() != null) {
			page.put("partCode", clientInquiryElement.getShortPartNumber());
		}
		page.put("partCode", clientInquiryElementService.getCodeFromPartNumber(clientInquiryElement.getPartNumber()));
		page.put("clientInquiryId", clientInquiryElement.getClientInquiryId());
		String searchString="";
		BigDecimal exchangeRate = BigDecimal.ZERO;
		BigDecimal averagePrice = BigDecimal.ZERO;
		BigDecimal lowestPrice = null;
		BigDecimal atPrice = BigDecimal.ZERO;
		BigDecimal compareRate = null;
		BigDecimal profitMargin = null;
		Integer cqCurrrncyId = null;
		Map competitormap = new HashMap();
		Integer clientinquiryelementid=null;
		List<ClientQuoteElementVo> list=new ArrayList<ClientQuoteElementVo>();
		List<ClientQuoteElementVo> cielist=clientQuoteService.findbycieid(clientInquiryElementId);//客户询价信息
		cqCurrrncyId=client.getCurrencyId();
		if(null!=client.getCurrencyId()){
			ExchangeRate currency = exchangeRateService.selectByPrimaryKey(client.getCurrencyId());
			exchangeRate=new BigDecimal(currency.getRate());
		}
		if(null!=client.getProfitMargin()){
			profitMargin=clientQuoteService.caculateProfitMargin(new BigDecimal(client.getProfitMargin()*100));
		}else {
			profitMargin=clientQuoteService.caculateProfitMargin(new BigDecimal(0.13*100));
		}
		for (ClientQuoteElementVo clientQuoteElementVo : cielist) {
			page.put("elementid", clientQuoteElementVo.getElementId());
			clientQuoteService.getElementForListPage(page, searchString, getSort(request));
		}
		
		List<ClientQuoteElementVo> sqeList = new ArrayList<ClientQuoteElementVo>();
		List<ClientQuoteElementVo> sqeHistory = new ArrayList<ClientQuoteElementVo>();
		if (page.getEntities() != null && page.getEntities().size() > 0) {
			for (int i = 0; i < page.getEntities().size(); i++) {
				PageModel<String> attachPage = new PageModel<String>();
				attachPage.put("ywId", page.getEntities().get(i).getCieclientInquiryId());
				attachPage.put("supplier", "'%"+page.getEntities().get(i).getSupplierCode()+"'");
				attachPage.put("partNumber", "'"+page.getEntities().get(i).getQuotePartNumber()+"%'");
				List<GyFj> fjList = gyFjService.getQuoteAttach(attachPage);
				if (fjList != null && fjList.size() > 0) {
					page.getEntities().get(i).setAttachId(fjList.get(0).getFjId());
					page.getEntities().get(i).setHasAttach(1);
				}
				ClientQuoteElementVo map = (ClientQuoteElementVo) page.getEntities().get(i);
				BigDecimal sqER=null;
				if(null!=page.getEntities().get(i).getExchangeRate()){
					sqER=new BigDecimal(page.getEntities().get(i).getExchangeRate());
				}
				BigDecimal sqprice=null;
				if(null!=page.getEntities().get(i).getPrice()){
					sqprice=new BigDecimal(page.getEntities().get(i).getPrice());
				}
				
				BigDecimal price = sqprice;
				Integer sqCurrencyId=page.getEntities().get(i).getCurrencyId();
				
				if(!sqCurrencyId.equals(cqCurrrncyId)){
					//判断是否7开头客户
					ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
					if (client.getCode().startsWith("7") && usRate.getRelativeRate() != null && usRate.getRelativeRate() > 0) {
						if (!sqCurrencyId.toString().equals("11")) {
							BigDecimal usBD = new BigDecimal(Double.toString(usRate.getRate()));   
							Double relativeRate = sqER.divide(usBD,4,BigDecimal.ROUND_HALF_UP).doubleValue();
							relativeRate = relativeRate*usRate.getRelativeRate();
							price = clientQuoteService.caculatePrice(sqprice, new BigDecimal(relativeRate),exchangeRate);
						}else {
							price = clientQuoteService.caculatePrice(sqprice, new BigDecimal(usRate.getRelativeRate()),exchangeRate);
						}
						page.getEntities().get(i).setRelativeRate(usRate.getRelativeRate());
					}else {
						//正常的价格计算方法
						price = clientQuoteService.caculatePrice(sqprice, sqER,exchangeRate);
					}
				}
				
				
				if (i < AVERAGE_PRICE_COUNT) {
					atPrice = atPrice.add(price);
				}
				page.getEntities().get(i).setPrice(price.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				BigDecimal amount =null;
				if(null!=page.getEntities().get(i).getQuoteAmount()){
					 amount =new BigDecimal(page.getEntities().get(i).getQuoteAmount());
				}
				page.getEntities().get(i).setCountprice(price.multiply(amount).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				BigDecimal cqER=null;
				if(null!=page.getEntities().get(i).getClientQuoteExchangeRate()){
					 cqER = new BigDecimal(page.getEntities().get(i).getClientQuoteExchangeRate());
				}
				BigDecimal cqPrice=null;
				if(null!=page.getEntities().get(i).getClientQuotePrice()){
					 cqPrice =new BigDecimal(page.getEntities().get(i).getClientQuotePrice());
				}
				if (cqPrice != null&&null!=cqER) {
					cqPrice = clientQuoteService.caculatePrice(cqPrice, cqER, exchangeRate);
					page.getEntities().get(i).setClientQuotePrice(cqPrice.doubleValue());
				}
				BigDecimal coER=null;
				if(null!=page.getEntities().get(i).getClientQuoteExchangeRate()){
					coER =new BigDecimal(page.getEntities().get(i).getClientQuoteExchangeRate());
				}
				if(null!=page.getEntities().get(i).getClientOrderPrice()){
					BigDecimal clientOrderPrice =new BigDecimal(page.getEntities().get(i).getClientOrderPrice());
				if (clientOrderPrice != null) {
					clientOrderPrice = clientQuoteService.caculatePrice(clientOrderPrice, coER,
							exchangeRate);
					page.getEntities().get(i).setClientOrderPrice(clientOrderPrice.doubleValue());
				}}
				if(new Integer(clientInquiryElementId).equals(page.getEntities().get(i).getClientInquiryElementId())){
					sqeList.add(map);
					if (price.doubleValue() > 0) {
						if (lowestPrice == null) {
							lowestPrice = price;
						} else if (lowestPrice.compareTo(price) > 0) {
							lowestPrice = price;
						}
					}
				}else{
					clientinquiryelementid=page.getEntities().get(i).getClientInquiryElementId();
					List<CompetitorVo> competitorlist=clientQuoteService.findcompetitor(clientinquiryelementid);//竞争对手报价
					if(competitorlist!=null){
						StringBuffer competitor=new StringBuffer();
						for (int j = 0; j < competitorlist.size(); j++) {
							BigDecimal cqePrice =new BigDecimal(competitorlist.get(j).getPrice());
							BigDecimal cqRate =new BigDecimal(competitorlist.get(j).getExchangeRate());
							competitorlist.get(j).setPrice(clientQuoteService.caculatePrice(cqePrice, cqRate,
						exchangeRate).doubleValue());
							String competitorprice=competitorlist.get(j).getPrice()+"";
							String competitorcode=competitorlist.get(j).getCompetitorCode();
							competitor.append(competitorcode+":");
							competitor.append(competitorprice+" ");
							competitormap.put(clientinquiryelementid, competitor);
						}
					}
					if (price.doubleValue() > 0) {
						if (lowestPrice == null) {
							lowestPrice = price;
						} else if (lowestPrice.compareTo(price) > 0) {
							lowestPrice = price;
						}
					}
					sqeHistory.add(map);
				}
			}
					
			if(page.getEntities().size()>=AVERAGE_PRICE_COUNT){
				averagePrice = atPrice.divide(new BigDecimal(
						AVERAGE_PRICE_COUNT), 2, BigDecimal.ROUND_HALF_UP);
			}else{
				averagePrice = atPrice.divide(new BigDecimal(page.getEntities().size()), 2,
						BigDecimal.ROUND_HALF_UP);
			}
		
			if (lowestPrice == null || averagePrice == null) {
				compareRate = null;
			} else if (averagePrice.equals(BigDecimal.ZERO)
					|| lowestPrice.equals(BigDecimal.ZERO)
					|| averagePrice.floatValue() < 0.01
					|| lowestPrice.floatValue() < 0.01) {
				compareRate = null;
			} else {
				compareRate = lowestPrice.divide(averagePrice, 2,
						BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
//				compareRate=compareRate.multiply(new BigDecimal(100));
			}
		}
		//历史订单记录
		List<HistoricalOrderPrice> orderList = historicalOrderPriceService.getByPart(clientInquiryElement.getPartNumber());
		List<ClientQuoteElementVo> addList = new ArrayList<ClientQuoteElementVo>();
		for (HistoricalOrderPrice historicalOrderPrice : orderList) {
			ClientQuoteElementVo clientQuoteElementVo = new ClientQuoteElementVo();
			clientQuoteElementVo.setSupplierCode(historicalOrderPrice.getClientCode());
			clientQuoteElementVo.setQuotePartNumber(historicalOrderPrice.getPartNum());
			clientQuoteElementVo.setQuoteDescription(historicalOrderPrice.getPartName());
			if (historicalOrderPrice.getPrice() != null) {
				clientQuoteElementVo.setPrice(historicalOrderPrice.getPrice());
			}
			if (historicalOrderPrice.getYear() != null) {
				clientQuoteElementVo.setQuoteRemark("年份："+historicalOrderPrice.getYear());
			}
			if (historicalOrderPrice.getAmount() != null) {
				clientQuoteElementVo.setQuoteAmount(historicalOrderPrice.getAmount());
			}
			clientQuoteElementVo.setCurrencyId(historicalOrderPrice.getCurrencyId());
			clientQuoteElementVo.setIsHisOrder(1);
			clientQuoteElementVo.setId(2);
			addList.add(clientQuoteElementVo);
		}
		if (addList.size() > 0) {
			list.addAll(addList);
		}
		list.addAll(sqeList);
		if(sqeHistory.size()>0){
			clientQuoteElementVos.setSupplierCode("历史供应商报价");
			list.add(clientQuoteElementVos);
		}
		list.addAll(sqeHistory);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientQuoteElementVo clientQuoteElementVo : list) {
				boolean isNum = false;
				Pattern pattern = Pattern.compile("[0-9]*"); 
				for (Object obj : competitormap.keySet()) {
					if (clientQuoteElementVo.getClientInquiryElementId() != null) {
						if(clientQuoteElementVo.getClientInquiryElementId().equals(obj)){
							clientQuoteElementVo.setCompetitor(competitormap.get(obj).toString());
						}
					}
				}
				if(null!=clientQuoteElementVo.getPrice()){
					Double freight=0.0;
					Double counterFee=0.0;
					BigDecimal quotePrice = new BigDecimal(clientQuoteElementVo.getPrice()) 
							.multiply(profitMargin)
							.setScale(4, BigDecimal.ROUND_HALF_UP);
					if(null!=clientQuoteElementVo.getFreight()&&0!=clientQuoteElementVo.getFreight()){
						freight=clientQuoteElementVo.getFreight();
					}
					quotePrice = new BigDecimal(clientQuoteElementVo.getPrice()+freight) 
						.multiply(profitMargin)
						.setScale(4, BigDecimal.ROUND_HALF_UP);
					
					clientQuoteElementVo.setProfitmargin(profitMargin.doubleValue());
					Double profitmargin=null;
					Double sqPrice=clientQuoteElementVo.getPrice();
					Double price=quotePrice.doubleValue();
					clientQuoteElementVo.setQuotePrice(price);
					profitmargin=((price-(sqPrice+freight))/price*100);
					Double NAN = new Double(Double.NaN);
					if(null!=profitmargin&&!profitmargin.equals(NAN)){
						clientQuoteElementVo.setProfitMargin(new BigDecimal(profitmargin).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
					}
				}
				if(null!=averagePrice){
					clientQuoteElementVo.setAveragePrice(averagePrice.doubleValue());
				}
				if(null!=lowestPrice){
					clientQuoteElementVo.setLowestPrice(lowestPrice.doubleValue());
				}
				if(null!=compareRate){
					clientQuoteElementVo.setCompareRate(compareRate.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				int count = competitorService.getCountByClientAndSupplier(clientInquiry.getClientId(), clientQuoteElementVo.getSupplierId());
				if (count > 0) {
					clientQuoteElementVo.setIfCompetitor(1);
				}
				ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
				if (clientQuoteElementVo.getFeeForExchangeBill() != null && !"".equals(clientQuoteElementVo.getFeeForExchangeBill())) {
					if(!"11".equals(cqCurrrncyId.toString())){
						if (client.getCode().startsWith("7")) {
							BigDecimal feeForExchangeBill = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getFeeForExchangeBill()), new BigDecimal(usRate.getRelativeRate()),exchangeRate);
							clientQuoteElementVo.setFeeForExchangeBill(feeForExchangeBill.doubleValue());
						}else {
							BigDecimal feeForExchangeBill = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),exchangeRate);
							clientQuoteElementVo.setFeeForExchangeBill(feeForExchangeBill.doubleValue());
						}
					}
				}
				if (clientQuoteElementVo.getCounterFee() != null && !"".equals(clientQuoteElementVo.getCounterFee())) {
					if(!"11".equals(cqCurrrncyId.toString())){
						if (client.getCode().startsWith("7")) {
							BigDecimal counterFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getCounterFee()), new BigDecimal(usRate.getRelativeRate()),exchangeRate);
							clientQuoteElementVo.setCounterFee(counterFee.doubleValue());
						}else {
							BigDecimal counterFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getCounterFee()), new BigDecimal(usRate.getRate()),exchangeRate);
							clientQuoteElementVo.setCounterFee(counterFee.doubleValue());
						}
					}
				}
				if (clientQuoteElementVo.getHazmatFee() != null && !"".equals(clientQuoteElementVo.getHazmatFee())) {
					if(!"11".equals(cqCurrrncyId.toString())){
						if (client.getCode().startsWith("7")) {
							BigDecimal hazmatFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getHazmatFee()), new BigDecimal(usRate.getRelativeRate()),exchangeRate);
							clientQuoteElementVo.setHazmatFee(hazmatFee.doubleValue());
						}else {
							BigDecimal hazmatFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getHazmatFee()), new BigDecimal(usRate.getRate()),exchangeRate);
							clientQuoteElementVo.setHazmatFee(hazmatFee.doubleValue());
						}
					}
				}
				if (clientQuoteElementVo.getOtherFee() != null && !"".equals(clientQuoteElementVo.getOtherFee())) {
					if(!"11".equals(cqCurrrncyId.toString())){
						if (client.getCode().startsWith("7")) {
							BigDecimal otherFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getOtherFee()), new BigDecimal(usRate.getRelativeRate()),exchangeRate);
							clientQuoteElementVo.setOtherFee(otherFee.doubleValue());
						}else {
							BigDecimal otherFee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteElementVo.getOtherFee()), new BigDecimal(usRate.getRate()),exchangeRate);
							clientQuoteElementVo.setOtherFee(otherFee.doubleValue());
						}
					}
				}
				
				//库存
				if (clientQuoteElementVo.getId() != null) {
					PageModel<StorageDetailVo> pageModel = new PageModel<StorageDetailVo>();
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("ipe.part_number = '").append(clientQuoteElementVo.getQuotePartNumber()).append("' and c.name = 'KC'").append(" and s.id = ").append(clientQuoteElementVo.getSupplierId());
					pageModel.put("where", stringBuffer.toString());
					List<StorageDetailVo> storageDetailVos = importpackageElementService.getStorageAmountByQuote(pageModel);
					//库存
					Double storageAmount = 0.0;
					for (StorageDetailVo storageDetailVo2 : storageDetailVos) {
						if (storageDetailVo2.getStorageAmount() != null) {
							storageAmount += storageDetailVo2.getStorageAmount();
						}
					}
					clientQuoteElementVo.setStorageAmount(storageAmount);
				}
				//3字头客户不需要提货换单费
				if (client.getCode().startsWith("3")) {
					clientQuoteElementVo.setFeeForExchangeBill(new Double(0));
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientQuoteElementVo);
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
	 * 新增客户报价
	 * **/
	@RequestMapping(value="/creatclientquote",  method=RequestMethod.POST)
	public @ResponseBody ResultVo creatclientquote(HttpServletRequest request, @ModelAttribute ClientQuoteElement record)
	{
		boolean result = true;
		String message = "新增完成！";
		
		clientQuoteElementService.insert(record);
//		String location=request.getParameter("location");
//		Integer id=record.getSupplierQuoteElementId();
//		SupplierQuoteElement supplierQuoteElement=supplierQuoteElementService.selectByPrimaryKey(id);
//		if(!location.equals(supplierQuoteElement.getLocation())){
//			supplierQuoteElement.setLocation(location);
//			supplierQuoteElementService.updateByPrimaryKey(supplierQuoteElement);
//		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 新增客户报价
	 * **/
	@RequestMapping(value="/creatClientQuoteByHis",  method=RequestMethod.POST)
	public @ResponseBody ResultVo creatClientQuoteByHis(HttpServletRequest request, @ModelAttribute ClientQuoteElement record)
	{
		
		try {
			boolean mistake = clientQuoteElementService.addInHistoryQuote(record);
			if (mistake) {
				return new ResultVo(true, "新增成功！");
			}else {
				return new ResultVo(false, "新增失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增失败！");
		}
	}
	
	/**
	 * 关闭询价明细
	 * **/
	@RequestMapping(value="/changeColseStatus",  method=RequestMethod.POST)
	public @ResponseBody ResultVo changeColseStatus(HttpServletRequest request, @ModelAttribute ClientQuoteElement record)
	{
		try {
			ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(record.getClientInquiryElementId());
			clientInquiryElement.setElementStatusId(710);
			clientInquiryElementService.updateByPrimaryKeySelective(clientInquiryElement);
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
	}
	
	/**
	 * 修改客户报价
	 * **/
	@RequestMapping(value="/editclientquoteelement",  method=RequestMethod.POST)
	public @ResponseBody ResultVo editclientquoteelement(HttpServletRequest request, @ModelAttribute ClientQuoteElement record)
	{
		boolean result = true;
		String message = "修改完成！";
		
		clientQuoteElementService.updateByPrimaryKey(record);
		
		
//		String location=request.getParameter("location");
//		Integer id=record.getSupplierQuoteElementId();
//		SupplierQuoteElement supplierQuoteElement=supplierQuoteElementService.selectByPrimaryKey(id);
//		if(!location.equals(supplierQuoteElement.getLocation())){
//			supplierQuoteElement.setLocation(location);
//			supplierQuoteElementService.updateByPrimaryKey(supplierQuoteElement);
//		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 修改供应商报价明细
	 * **/
	@RequestMapping(value="/updatesupplierquotefreight",  method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updatesupplierquoteelement(HttpServletRequest request,
			@ModelAttribute SupplierQuoteElement record)
	{
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
	 * 修改客户报价
	 * **/
	@RequestMapping(value="/updateclientquoteelement",  method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateclientquoteelement(HttpServletRequest request, @ModelAttribute ClientQuoteElement record)
	{
		boolean success = true;
		String message = "修改完成！";
		try{
		record.setAmount(Double.parseDouble(request.getParameter("clientQuoteAmount")));
		record.setPrice(Double.parseDouble(request.getParameter("clientQuotePrice")));
		record.setRemark(request.getParameter("clientQuoteRemark"));
		record.setLeadTime(Integer.parseInt(request.getParameter("cqeLeadTime")));
		record.setLocation(request.getParameter("quoteLocation"));
		clientQuoteElementService.updateByPrimaryKey(record);
		}catch(Exception e){
			e.printStackTrace();
			success = false;
			message = "更新失败";
			}
			EditRowResultVo result = new EditRowResultVo(success, message);
			return result;
	}
	
	/**
	 * 利润表
	 * **/
	@RequestMapping(value="/profitStatementByQuote",method=RequestMethod.GET)
	public String profitStatementByQuote(HttpServletRequest request){
		request.setAttribute("clientquoteid", request.getParameter("clientquoteid"));
		request.setAttribute("clientInquiryQuoteNumber", request.getParameter("clientInquiryQuoteNumber"));
		 List<ProfitStatementVo> cqelist=clientQuoteService.findQuoteProfitStatement(request.getParameter("clientquoteid"));
		request.setAttribute("rownum", cqelist.size()+2);
		return "/marketing/clientquote/profitstatement";
	}
	
	/**
	 * 利润表
	 * **/
	@RequestMapping(value="/weatherProfitStatementByOrder",method=RequestMethod.GET)
	public String weatherProfitStatementByOrder(HttpServletRequest request){
		request.setAttribute("clientquoteid", request.getParameter("clientquoteid"));
		request.setAttribute("clientOrderId", request.getParameter("id"));
		request.setAttribute("orderNumber", request.getParameter("orderNumber"));
		 List<ProfitStatementVo> cqelist=clientQuoteService.findQuoteProfitStatement(request.getParameter("clientquoteid"));
		request.setAttribute("rownum", cqelist.size()+2);
		return "/marketing/clientorder/weatherprofitstatement";
	}
	
	/**
	 * 利润表
	 * **/
	@RequestMapping(value="/profitStatementByOrder",method=RequestMethod.GET)
	public String profitStatementByOrder(HttpServletRequest request){
		request.setAttribute("clientquoteid", request.getParameter("clientquoteid"));
		request.setAttribute("clientOrderId", request.getParameter("id"));
		request.setAttribute("orderNumber", request.getParameter("orderNumber"));
		return "/marketing/clientorder/profitstatement";
	}
	

	/**
	 * 动态列
	 * **/
	@RequestMapping(value = "/list/dynamicColNames", method = RequestMethod.POST)
	public @ResponseBody
	ColumnVo excelListDynamicCol(HttpServletRequest request,
			HttpServletResponse response) {
		String id= request.getParameter("clientquoteid");//查询条件
		String clientOrderId= request.getParameter("clientOrderId");//查询条件
		String type=request.getParameter("type");
		 List<ProfitStatementVo> cqelist=clientQuoteService.findQuoteProfitStatement(id);
		 List<ClientQuoteElementVo> sqeList =new ArrayList<ClientQuoteElementVo>();
		 List<ClientQuoteElementVo> List  =new ArrayList<ClientQuoteElementVo>();
		 List<String> supplierCode = new ArrayList<String>();
		for (ProfitStatementVo profitStatementVo : cqelist) {
			if(type.equals("quote")){
				List= clientQuoteService.findQuoteDataByCieId(profitStatementVo.getClientInquiryElementId());
			}else if(type.equals("order"))
			{
				List= clientQuoteService.findOrderDataByCieId(profitStatementVo.getClientInquiryElementId());
			}else if(type.equals("weather")){
				List= clientQuoteService.findCodeByCoId(clientOrderId);
				for (ClientQuoteElementVo clientQuoteElementVo : List) {
					if(!supplierCode.contains(clientQuoteElementVo.getSupplierCode())){
						supplierCode.add(clientQuoteElementVo.getSupplierCode());
					}
				}
				break;
			}
			sqeList.addAll(List);
			for (ClientQuoteElementVo clientQuoteElementVo : sqeList) {
				if(!supplierCode.contains(clientQuoteElementVo.getSupplierCode())){
					supplierCode.add(clientQuoteElementVo.getSupplierCode());
				}
			}
		}
		if (cqelist != null) {
			 List<ClientOrderVo> clientOrderList =	clientQuoteService.findByCqId(id);
		}
//		String businessKey = getString(request, "max");
//		int dynamicColCount = Integer.valueOf(businessKey);
		List<String> displayNames = new ArrayList<String>();
		List<String> colNames = new ArrayList<String>();
		Collections.sort(supplierCode);
		for(int i=0;i<supplierCode.size();i++){
			displayNames.add(supplierCode.get(i));
			colNames.add("supplier"+supplierCode.get(i));
			displayNames.add("提货换单费");
			displayNames.add("银行费用");
			displayNames.add("危品费");
			displayNames.add("其他费用");
			colNames.add(supplierCode.get(i)+"feeForExchangeBill");	
			colNames.add(supplierCode.get(i)+"bankCost");
			colNames.add(supplierCode.get(i)+"hazmatFee");
			colNames.add(supplierCode.get(i)+"otherFee");
		}
		ColumnVo result = new ColumnVo();
		result.setColumnDisplayNames(displayNames);
		result.setColumnKeyNames(colNames);
		
		return result;
	}
	
	/**
	 * 利润表列表数据
	 * **/
	@RequestMapping(value = "/list/data", method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo excelListData(HttpServletRequest request,
			HttpServletResponse response) {
//		PageModel<GyExcel> page = getPage(request);
		String clientquoteid= request.getParameter("clientquoteid");//查询条件
		String clientCode = "";
		String clientOrderId= request.getParameter("clientOrderId");//查询条件
		String type=request.getParameter("type");
		PageModel<ProfitStatementVo> page= getPage(request);
		Double cqex=0.0;
		Double coex=0.0;
		Double sqex=0.0;
		String coCurrency="";
		Double soex=0.0;
		String soCurrency="";
		String sqCurrency="";
		String cqCurrency="";
		 List<ClientOrderVo> clientOrderList = null;
		if(type.equals("order")){
			page.put("clientOrderId", clientOrderId);
			ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(new Integer(clientOrderId));
			ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientOrder.getClientQuoteId());
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
			Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
			clientCode = client.getCode();
			clientQuoteService.findOrderWeatherProfitStatementpage(page);
			if(page.getEntities().size()>0){
			cqex=page.getEntities().get(0).getCqExchangeRate();
			coex=page.getEntities().get(0).getCoExchangeRate();
			 sqex=page.getEntities().get(0).getSqExchangeRate();
			 coCurrency=page.getEntities().get(0).getCoCurrencyValue();
			 sqCurrency=page.getEntities().get(0).getSqCurrencyValue();
			}
		}else if(type.equals("quote")){
			page.put("clientquoteid", clientquoteid);
			ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(new Integer(clientquoteid));
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
			Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
			clientCode = client.getCode();
			clientQuoteService.findQuoteProfitStatementpage(page);
			if(page.getEntities().size()>0){
			cqex=page.getEntities().get(0).getCqExchangeRate();
			 sqex=page.getEntities().get(0).getSqExchangeRate();
			 cqCurrency=page.getEntities().get(0).getCqCurrencyValue();
			 sqCurrency=page.getEntities().get(0).getSqCurrencyValue();
			}
		}else if(type.equals("weather")){
			page.put("clientOrderId", clientOrderId);
			ClientOrder clientOrder = clientOrderService.selectByPrimaryKey(new Integer(clientOrderId));
			ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientOrder.getClientQuoteId());
			ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
			Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
			clientCode = client.getCode();
			clientQuoteService.findOrderProfitStatementpage(page);
			if(page.getEntities().size()>0){
				cqex=page.getEntities().get(0).getCqExchangeRate();
				coex=page.getEntities().get(0).getCoExchangeRate();
				 soex=page.getEntities().get(0).getSoExchangeRate();
				 coCurrency=page.getEntities().get(0).getCoCurrencyValue();
				 cqCurrency=page.getEntities().get(0).getCqCurrencyValue();
				 soCurrency=page.getEntities().get(0).getSoCurrencyValue();
				}
		}
		
		if (page.getEntities() != null) {
			 clientOrderList =	clientQuoteService.findByCqId(clientquoteid);
		}
		 List<ClientQuoteElementVo> sqeList =new ArrayList<ClientQuoteElementVo>();
		 List<String> supplierCode = new ArrayList<String>();
		 List<ClientQuoteElementVo> List  =new ArrayList<ClientQuoteElementVo>();
		 Map<String,Double> supplierPrice=new HashMap<String, Double>();
		for (ProfitStatementVo profitStatementVo : page.getEntities()) {
			if(type.equals("quote")){
				List= clientQuoteService.findQuoteDataByCieId(profitStatementVo.getClientInquiryElementId());
			}else if(type.equals("order"))
			{
				List= clientQuoteService.findOrderDataByCieId(profitStatementVo.getClientInquiryElementId());
			}else if(type.equals("weather")){
				List= clientQuoteService.findCodeByCoId(clientOrderId);
			}
			sqeList.addAll(List);
			for (ClientQuoteElementVo clientQuoteElementVo : sqeList) {
				if (clientCode.startsWith("7") && clientQuoteElementVo.getRelativeRate() != null) {
					clientQuoteElementVo.setBasePrice(clientQuoteElementVo.getSupplierQuotePrice() * clientQuoteElementVo.getRelativeRate());
				}
				if(type.equals("quote")){
					 sqex=clientQuoteElementVo.getExchangeRate();
					 sqCurrency=clientQuoteElementVo.getCurrencyValue();
					if(cqCurrency.equals(sqCurrency)/*&&cqCurrency.equals("美元")&&sqCurrency.equals("美元")*/){
						clientQuoteElementVo.setBasePrice(clientQuoteElementVo.getBasePrice()/sqex);
					}
				}else if(type.equals("order")){
					 sqex=clientQuoteElementVo.getExchangeRate();
					 sqCurrency=clientQuoteElementVo.getCurrencyValue();
					if(coCurrency.equals(sqCurrency)/*&&coCurrency.equals("美元")&&sqCurrency.equals("美元")*/){
						clientQuoteElementVo.setBasePrice(clientQuoteElementVo.getBasePrice()/sqex);
					}
				}else if(type.equals("weather")){
					 soex=clientQuoteElementVo.getExchangeRate();
					 soCurrency=clientQuoteElementVo.getCurrencyValue();
					if(coCurrency.equals(soCurrency)/*&&coCurrency.equals("美元")&&soCurrency.equals("美元")*/){
						clientQuoteElementVo.setBasePrice(clientQuoteElementVo.getBasePrice()/soex);
					}
				}
				
				if(!supplierCode.contains(clientQuoteElementVo.getSupplierCode())){
					supplierCode.add(clientQuoteElementVo.getSupplierCode());
				}
				if(type.equals("weather")){
					supplierPrice.put(profitStatementVo.getId()+"-"+clientQuoteElementVo.getSupplierCode(), clientQuoteElementVo.getBasePrice());
				}else{
					if(null!=supplierPrice.get(profitStatementVo.getClientInquiryElementId()+"-"+clientQuoteElementVo.getSupplierCode())){
						if(clientQuoteElementVo.getId().equals(profitStatementVo.getSupplierQuoteElementId())){
							supplierPrice.put(profitStatementVo.getClientInquiryElementId()+"-"+clientQuoteElementVo.getSupplierCode(), clientQuoteElementVo.getBasePrice());
							break;
						}
					}else{
					supplierPrice.put(profitStatementVo.getClientInquiryElementId()+"-"+clientQuoteElementVo.getSupplierCode(), clientQuoteElementVo.getBasePrice());
					}
				}
			}
			sqeList.clear();
		}
		List<ProfitStatementVo> countlist=new ArrayList<ProfitStatementVo>();
		ProfitStatementVo element=new ProfitStatementVo();
		ProfitStatementVo element1=new ProfitStatementVo();
		if(type.equals("quote")){
		element.setInquiryDescription("总计");
		element.setId(0);
		countlist.add(0, element);
		}else if(type.equals("weather")){
			element1.setInquiryDescription("中标总计");
			element1.setId(-1);
			countlist.add(0, element1);
		}else{
			element.setInquiryDescription("总计");
			element.setId(0);
			countlist.add(0, element);
			element1.setInquiryDescription("中标总计");
			element1.setId(-1);
			countlist.add(1, element1);
		}
		
		page.getEntities().addAll(countlist);
		Double[] supplierPriceArr = new Double[supplierCode.size()];
		Double[] orderpriceArr = new Double[supplierCode.size()];
		for (int i = 0; i < supplierCode.size(); i++) {
			supplierPriceArr[i]=0.0;
			orderpriceArr[i]=0.0;
		}
		JQGridMapVo jqgrid = new JQGridMapVo();
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			 DecimalFormat df = new DecimalFormat("#.00");   
			 Double totalProfitMargin=0.0;

			 Double totalSale = 0.0;
			 Double totalPurchase = 0.0;
			 Double totalCharge = 0.0;

				for (ProfitStatementVo profitStatementVo :page.getEntities()) {
//					if(cqCurrency.equals(sqCurrency)||coCurrency.equals(sqCurrency)||coCurrency.equals(soCurrency)){
					if (clientCode.startsWith("7") && profitStatementVo.getRelativeRate() != null) {
						if (type.equals("weather")) {
							profitStatementVo.setQuoteBasePrice(profitStatementVo.getSupplierOrderPrice() * profitStatementVo.getRelativeRate());
						}else if (type.equals("order") ||type.equals("quote")) {
							profitStatementVo.setQuoteBasePrice(profitStatementVo.getSupplierQuotePrice() * profitStatementVo.getRelativeRate());
						}
						profitStatementVo.setQuoteTotalPrice(profitStatementVo.getQuoteBasePrice() * profitStatementVo.getSupplierOrderAmount());
					}
						if(!profitStatementVo.getInquiryDescription().equals("总计")&&!profitStatementVo.getInquiryDescription().equals("中标总计")){
							if(type.equals("quote")/*cqCurrency.equals("美元")&&sqCurrency.equals("美元")*/){
								 sqex=profitStatementVo.getSqExchangeRate();
								 sqCurrency=profitStatementVo.getSqCurrencyValue();
								if(cqCurrency.equals(sqCurrency)){
									profitStatementVo.setQuoteBasePrice(profitStatementVo.getQuoteBasePrice()/sqex);
									profitStatementVo.setQuoteTotalPrice(profitStatementVo.getQuoteTotalPrice()/sqex);
									if(null!=profitStatementVo.getBaseStoragePrice()){
									profitStatementVo.setBaseStoragePrice(profitStatementVo.getBaseStoragePrice()/sqex);
									}
									profitStatementVo.setClientQuoteBasePrice(profitStatementVo.getClientQuoteBasePrice()/cqex);
								}
//								profitStatementVo.setProfitMargin(((profitStatementVo.getClientQuoteBasePrice()-profitStatementVo.getQuoteBasePrice())/profitStatementVo.getClientQuoteBasePrice())*100);
							}else if(type.equals("order")/*coCurrency.equals("美元")&&sqCurrency.equals("美元")*/){
								 sqex=profitStatementVo.getSqExchangeRate();
								 sqCurrency=profitStatementVo.getSqCurrencyValue();
								if(coCurrency.equals(sqCurrency)){
									profitStatementVo.setQuoteBasePrice(profitStatementVo.getQuoteBasePrice()/sqex);
									profitStatementVo.setQuoteTotalPrice(profitStatementVo.getQuoteTotalPrice()/sqex);
									if(null!=profitStatementVo.getBaseStoragePrice()){
									profitStatementVo.setBaseStoragePrice(profitStatementVo.getBaseStoragePrice()/sqex);
									}
									profitStatementVo.setClientQuoteBasePrice(profitStatementVo.getClientQuoteBasePrice()/cqex);
									profitStatementVo.setOrderBasePrice(profitStatementVo.getOrderBasePrice()/coex);
									profitStatementVo.setOrderTotalPrice(profitStatementVo.getOrderTotalPrice()/coex);
								}
//								profitStatementVo.setProfitMargin(((profitStatementVo.getClientQuoteBasePrice()-profitStatementVo.getQuoteBasePrice())/profitStatementVo.getClientQuoteBasePrice())*100);
							}else if(type.equals("weather")/*coCurrency.equals("美元")&&soCurrency.equals("美元")*/){
								 soex=profitStatementVo.getSoExchangeRate();
								 soCurrency=profitStatementVo.getSoCurrencyValue();
								if(coCurrency.equals(soCurrency)){
									profitStatementVo.setQuoteBasePrice(profitStatementVo.getQuoteBasePrice()/soex);
									profitStatementVo.setQuoteTotalPrice(profitStatementVo.getQuoteTotalPrice()/soex);
									if(null!=profitStatementVo.getBaseStoragePrice()){
									profitStatementVo.setBaseStoragePrice(profitStatementVo.getBaseStoragePrice()/soex);
									}
									profitStatementVo.setClientQuoteBasePrice(profitStatementVo.getClientQuoteBasePrice()/cqex);
									profitStatementVo.setOrderBasePrice(profitStatementVo.getOrderBasePrice()/coex);
									profitStatementVo.setOrderTotalPrice(profitStatementVo.getOrderTotalPrice()/coex);
								}
//								profitStatementVo.setProfitMargin(((profitStatementVo.getOrderBasePrice()-profitStatementVo.getQuoteBasePrice())/profitStatementVo.getOrderBasePrice())*100);
							}
						}
//					}
					DecimalFormat xs = new DecimalFormat("#.##");
					
					//费用
					Double feeForExchangeBill = 0.0;
					Double bankCost = 0.0;
					Double otherFee = 0.0;
					Double hazmatFee = 0.0;
					ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
					Double clientRate = 0.0;
					Double amount = 0.0;
					if (type.equals("weather")) {
						clientRate = profitStatementVo.getCoExchangeRate();
						amount = profitStatementVo.getOrderAmount();
					}else if (type.equals("order") ||type.equals("quote")) {
						clientRate = profitStatementVo.getCqExchangeRate();
						amount = profitStatementVo.getQuoteAmount();
					}
					if (profitStatementVo.getFeeForExchangeBill() != null && !"".equals(profitStatementVo.getFeeForExchangeBill())) {
						BigDecimal fee = new BigDecimal(0);
						if (clientCode.startsWith("7")) {
							fee = clientQuoteService.caculatePrice(new BigDecimal(profitStatementVo.getFeeForExchangeBill()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(clientRate));
						}else {
							fee = clientQuoteService.caculatePrice(new BigDecimal(profitStatementVo.getFeeForExchangeBill()), new BigDecimal(usRate.getRate()),new BigDecimal(clientRate));
						}
						profitStatementVo.setFeeForExchangeBill(fee.doubleValue());
						if (profitStatementVo.getMoq() != null && !"".equals(profitStatementVo.getMoq())) {
							if (profitStatementVo.getMoq() > amount) {
								feeForExchangeBill = fee.doubleValue()/profitStatementVo.getMoq();
							}else {
								feeForExchangeBill = fee.doubleValue()/amount;
							}
						}else {
							feeForExchangeBill = fee.doubleValue()/amount;
						}
					}
					if (profitStatementVo.getBankCost() != null && !"".equals(profitStatementVo.getBankCost())) {
						BigDecimal fee = new BigDecimal(0);
						if (clientCode.startsWith("7")) {
							fee = clientQuoteService.caculatePrice(new BigDecimal(profitStatementVo.getBankCost()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(clientRate));
						}else {
							fee = clientQuoteService.caculatePrice(new BigDecimal(profitStatementVo.getBankCost()), new BigDecimal(usRate.getRate()),new BigDecimal(clientRate));
						}
						profitStatementVo.setBankCost(fee.doubleValue());
						if (profitStatementVo.getMoq() != null && !"".equals(profitStatementVo.getMoq())) {
							if (profitStatementVo.getMoq() > amount) {
								bankCost = fee.doubleValue()/profitStatementVo.getMoq();
							}else {
								bankCost = fee.doubleValue()/amount;
							}
						}else {
							bankCost = fee.doubleValue()/amount;
						}
					}
					if (profitStatementVo.getOtherFee() != null && !"".equals(profitStatementVo.getOtherFee())) {
						BigDecimal fee = new BigDecimal(0);
						if (clientCode.startsWith("7")) {
							fee = clientQuoteService.caculatePrice(new BigDecimal(profitStatementVo.getOtherFee()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(clientRate));
						}else {
							fee = clientQuoteService.caculatePrice(new BigDecimal(profitStatementVo.getOtherFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientRate));
						}
						profitStatementVo.setOtherFee(fee.doubleValue());
						if (profitStatementVo.getMoq() != null && !"".equals(profitStatementVo.getMoq())) {
							if (profitStatementVo.getMoq() > amount) {
								otherFee = fee.doubleValue()/profitStatementVo.getMoq();
							}else {
								otherFee = fee.doubleValue()/amount;
							}
						}else {
							otherFee = fee.doubleValue()/amount;
						}
					}
					if (profitStatementVo.getHazmatFee() != null && !"".equals(profitStatementVo.getHazmatFee())) {
						BigDecimal fee = new BigDecimal(0);
						if (clientCode.startsWith("7")) {
							fee = clientQuoteService.caculatePrice(new BigDecimal(profitStatementVo.getHazmatFee()), new BigDecimal(usRate.getRelativeRate()),new BigDecimal(clientRate));
						}else {
							fee = clientQuoteService.caculatePrice(new BigDecimal(profitStatementVo.getHazmatFee()), new BigDecimal(usRate.getRate()),new BigDecimal(clientRate));
						}
						profitStatementVo.setHazmatFee(fee.doubleValue());
						hazmatFee = fee.doubleValue();
					}
					
					
					if (profitStatementVo.getFixedCost() != null) {
						Double fixedCost = profitStatementVo.getFixedCost();
						Double profitMargin =profitStatementVo.getProfitMargin();
						if(type.equals("quote")||type.equals("order")){
							if (fixedCost < new Double(1)) {
								profitStatementVo.setFixedCost(new Double(xs.format(profitStatementVo.getClientQuoteBasePrice()*fixedCost)));
							}else if(fixedCost > new Double(1)){
//								if(cqCurrency.equals(sqCurrency)){
									profitStatementVo.setFixedCost(new Double(xs.format(fixedCost)));
//								}else{
//									profitStatementVo.setFixedCost(new Double(xs.format(fixedCost*cqex)));
//								}
							}
							
							Double counterFee=0.0;
							Double bankCharges=0.0;
							if(null!=profitStatementVo.getCounterFee()){
								if(profitStatementVo.getCounterFee()<1){
									counterFee=profitStatementVo.getCounterFee()*profitStatementVo.getQuoteBasePrice();
								}else{
									counterFee=0.0;
								}
							}
							if(null!=profitStatementVo.getBankCharges()){
								if(profitStatementVo.getBankCharges()<1){
									bankCharges=profitStatementVo.getBankCharges()*profitStatementVo.getClientQuoteBasePrice();
								}else{
									bankCharges=profitStatementVo.getBankCharges();
								}
							}
							
//							 profitMargin =((profitStatementVo.getClientQuoteBasePrice()-bankCharges-profitStatementVo.getFixedCost()-profitStatementVo.getQuoteBasePrice()-counterFee-profitStatementVo.getFreight()-feeForExchangeBill-bankCost-otherFee-hazmatFee)
//									 /(profitStatementVo.getClientQuoteBasePrice()-bankCharges-profitStatementVo.getFixedCost()-counterFee-profitStatementVo.getFreight()-feeForExchangeBill-bankCost-otherFee-hazmatFee))*100;
							profitMargin =((profitStatementVo.getClientQuoteBasePrice()-bankCharges-profitStatementVo.getFixedCost()-profitStatementVo.getQuoteBasePrice()-counterFee-profitStatementVo.getFreight()-feeForExchangeBill-bankCost-otherFee-hazmatFee)
									/(profitStatementVo.getClientQuoteBasePrice()))*100;
							totalCharge = totalCharge + profitStatementVo.getFixedCost()+counterFee+bankCharges+feeForExchangeBill+bankCost+otherFee+hazmatFee;

						}else{
							if (fixedCost < new Double(1)) {
								profitStatementVo.setFixedCost(new Double(xs.format(profitStatementVo.getOrderBasePrice()*fixedCost)));
							}else if(fixedCost > new Double(1)){
//								if(coCurrency.equals(soCurrency)){
									profitStatementVo.setFixedCost(new Double(xs.format(fixedCost)));
//								}else{
//									profitStatementVo.setFixedCost(new Double(xs.format(fixedCost*coex)));
//								}
							}
							Double counterFee=0.0;
							Double bankCharges=0.0;
							if(null!=profitStatementVo.getCounterFee()){
								if(profitStatementVo.getCounterFee()<1){
									counterFee=profitStatementVo.getCounterFee()*profitStatementVo.getQuoteBasePrice();
								}else{
									counterFee=0.0;
								}
							}
							if(null!=profitStatementVo.getBankCharges()){
								if(profitStatementVo.getBankCharges()<1){
									bankCharges=profitStatementVo.getBankCharges()*profitStatementVo.getOrderBasePrice();
								}else{
									bankCharges=profitStatementVo.getBankCharges();
								}
							}
//							 profitMargin = ((profitStatementVo.getOrderBasePrice()-profitStatementVo.getFixedCost()-counterFee-bankCharges-profitStatementVo.getQuoteBasePrice()-feeForExchangeBill-bankCost-otherFee-hazmatFee)
//									 /(profitStatementVo.getOrderBasePrice()-profitStatementVo.getFixedCost()-counterFee-bankCharges-feeForExchangeBill-bankCost-otherFee-hazmatFee))*100;
							profitMargin = ((profitStatementVo.getOrderTotalPrice()-profitStatementVo.getFixedCost()-counterFee-bankCharges-profitStatementVo.getQuoteTotalPrice()-feeForExchangeBill-bankCost-otherFee-hazmatFee))/(profitStatementVo.getOrderTotalPrice())*100;

							totalCharge = totalCharge + profitStatementVo.getFixedCost()+counterFee+bankCharges+feeForExchangeBill+bankCost+otherFee+hazmatFee;

						}

						profitStatementVo.setProfitMargin(new Double(xs.format(profitMargin)));
						totalProfitMargin+=profitMargin;

                        totalSale += profitStatementVo.getOrderTotalPrice();
                        totalPurchase += profitStatementVo.getQuoteTotalPrice();

					}
					
						Map<String, Object> map = EntityUtil.entityToTableMap(profitStatementVo);
						for (int i = 0; i < supplierCode.size(); i++) {
							if(!type.equals("weather")){
								map.put("supplier"+supplierCode.get(i), supplierPrice.get(profitStatementVo.getClientInquiryElementId()+"-"+supplierCode.get(i)));
							}else{
								map.put("supplier"+profitStatementVo.getSupplierCode(), profitStatementVo.getQuoteBasePrice());
							}
							map.put(supplierCode.get(i)+"feeForExchangeBill", (profitStatementVo.getFeeForExchangeBill() != null)?profitStatementVo.getFeeForExchangeBill():0.0);
							map.put(supplierCode.get(i)+"bankCost", (profitStatementVo.getBankCost() != null)?profitStatementVo.getBankCost():0.0);
							map.put(supplierCode.get(i)+"hazmatFee", (profitStatementVo.getHazmatFee() != null)?profitStatementVo.getHazmatFee():0.0);
							map.put(supplierCode.get(i)+"otherFee", (profitStatementVo.getOtherFee() != null)?profitStatementVo.getOtherFee():0.0);
							
							if(null!=supplierPrice.get(profitStatementVo.getClientInquiryElementId()+"-"+supplierCode.get(i))){
								if(type.equals("quote")){
									supplierPriceArr[i]+=supplierPrice.get(profitStatementVo.getClientInquiryElementId()+"-"+supplierCode.get(i))*profitStatementVo.getQuoteAmount();
								}else if(type.equals("order")){
									supplierPriceArr[i]+=supplierPrice.get(profitStatementVo.getClientInquiryElementId()+"-"+supplierCode.get(i))*profitStatementVo.getOrderAmount();
								}
							}
							
							if(null!=profitStatementVo.getSupplierCode()&&profitStatementVo.getSupplierCode().equals(supplierCode.get(i))&&null!=profitStatementVo.getOrderAmount()){
									if(!type.equals("weather")){
										if(null!=supplierPrice.get(profitStatementVo.getClientInquiryElementId()+"-"+supplierCode.get(i))){
								orderpriceArr[i]+=supplierPrice.get(profitStatementVo.getClientInquiryElementId()+"-"+supplierCode.get(i))*profitStatementVo.getOrderAmount();
										}
									}else{
								orderpriceArr[i]+=supplierPrice.get(profitStatementVo.getId()+"-"+supplierCode.get(i))*profitStatementVo.getOrderAmount();
								}
							}
							
							if(profitStatementVo.getInquiryDescription().equals("总计")){
								if(type.equals("quote")){
//								map.put("profitMargin", totalProfitMargin/(page.getEntities().size()-1));
                                map.put("profitMargin",(totalSale-totalPurchase-totalCharge)/(totalSale));
                                map.put("orderTotalPrice",totalSale);
                                map.put("quoteTotalPrice",totalPurchase);
                                map.put("totalCharge",totalCharge);
								}
								map.put("supplier"+supplierCode.get(i), df.format(supplierPriceArr[i]) );
							}
							if(profitStatementVo.getInquiryDescription().equals("中标总计")){
								if(type.equals("weather")){
								    map.put("profitMargin",(totalSale-totalPurchase-totalCharge)/(totalSale)*100);
                                    map.put("orderTotalPrice",totalSale);
                                    map.put("quoteTotalPrice",totalPurchase);
									map.put("totalCharge",totalCharge);
//									map.put("profitMargin", totalProfitMargin/(page.getEntities().size()-1));
								}
//								else{
//                                    map.put("profitMargin",(totalSale-totalPurchase)/(totalSale)*100);
//                                    map.put("orderTotalPrice",totalSale);
//                                    map.put("quoteTotalPrice",totalPurchase);
////									map.put("profitMargin", totalProfitMargin/(page.getEntities().size()-2));
//								}
								map.put("supplier"+supplierCode.get(i), df.format(orderpriceArr[i]) );
							}
						}
						if(type.equals("weather")){
						
						
						}
						mapList.add(map);
				}
				
			jqgrid.setRows(mapList);
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());
		}
		// 导出
		return jqgrid;
		
	}
	
	/**
	 * 大利润表统计
	 * **/
	@RequestMapping(value="/profitStatistics",method=RequestMethod.GET)
	public String profitStatistics(HttpServletRequest request) {
		return "/marketing/clientquote/profitstatisticslist";
	}
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/profitStatisticsData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo profitStatisticsData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<ProfitStatementVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		String partNumber=request.getParameter("partNumber");
		String check =request.getParameter("check");
		page.put("check", check);
		page.put("partNumber", partNumber);
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
		clientQuoteService.profitStatisticsPage(page, searchString, getSort(request));
		String exportModel = getString(request, "exportModel");
		ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ProfitStatementVo clientQuoteVo : page.getEntities()) {
				/*if(clientQuoteVo.getCoCurrencyValue().equals(clientQuoteVo.getSoCurrencyValue())){
					clientQuoteVo.setQuoteBasePrice(clientQuoteVo.getQuoteBasePrice()/clientQuoteVo.getSoExchangeRate());
					clientQuoteVo.setQuoteTotalPrice(clientQuoteVo.getQuoteTotalPrice()/clientQuoteVo.getSoExchangeRate());
					clientQuoteVo.setOrderBasePrice(clientQuoteVo.getOrderBasePrice()/clientQuoteVo.getCoExchangeRate());
					clientQuoteVo.setOrderTotalPrice(clientQuoteVo.getOrderTotalPrice()/clientQuoteVo.getCoExchangeRate());
				}*/
				DecimalFormat df = new DecimalFormat("#.##");
				//计算出库,入库费用,运费,危险品费用,还有银行手续费
				List<ImportPackageElement> list = importpackageElementService.getBySoeId(clientQuoteVo.getId());
				Double importFee = 0.00;
				Double importFreight = 0.00;
				Double exportFee = 0.00;
				Double exportFreight = 0.00;
				Double hazFee = 0.00;
				Double bankCost = 0.00;
				Double feeForExchangeBill = 0.00;
				Double otherFee = 0.00;
				Double profitOtherFee = 0.00;
				Double totalHaz = 0.00;
				Double totalFeeForExchangeBill = 0.00;
				Double totalOtherFee = 0.00;
				Double totalBankCost = 0.00;
				ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByPrimaryKey(clientQuoteVo.getClientQuoteElementId());
				for (int i = 0; i < list.size(); i++) {
					ImportPackage importPackage = importPackageService.getFeeMessage(list.get(i).getImportPackageId());
					ExchangeRate exchangeRate = exchangeRateService.selectByPrimaryKey(10);
					//入库费用
					if (importPackage.getImportFee() != null && importPackage.getTotalAmount() > 0 && importPackage.getImportFee() > 0 && importPackage.getCurrencyId() != null) {
						if (clientQuoteVo.getClientCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0 && importPackage.getImportFee() != null && importPackage.getCurrencyId() != null) {
							if (!importPackage.getCurrencyId().equals(usRate.getCurrencyId())) {
								ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(importPackage.getCurrencyId());
								//兑换美元的倍数
								Double relative = new BigDecimal(currentRate.getRate()).divide(new BigDecimal(usRate.getRate()), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
								relative = relative * usRate.getRelativeRate();
								Double realFee = clientQuoteService.caculatePrice(new BigDecimal(importPackage.getImportFee()), new BigDecimal(relative), new BigDecimal(exchangeRate.getRate())).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
								importFee = importFee + realFee/importPackage.getTotalAmount()*list.get(i).getAmount();
							}else {
								Double realFee = clientQuoteService.caculatePrice(new BigDecimal(importPackage.getImportFee()), new BigDecimal(clientQuoteElement.getRelativeRate()), new BigDecimal(1)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
								importFee = importFee + realFee/importPackage.getTotalAmount()*list.get(i).getAmount();
							}
						}else {
							Double currencyRate = 0.00;
							if (importPackage.getExchangeRate() != null) {
								currencyRate = importPackage.getExchangeRate();
							}else {
								ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(importPackage.getCurrencyId());
								currencyRate = currentRate.getRate();
							}
							Double rate = currencyRate/exchangeRate.getRate();
							importFee = importFee + importPackage.getImportFee()*rate/importPackage.getTotalAmount()*list.get(i).getAmount();
						}
					}
					//入库运费
					if (importPackage.getFreight() != null && importPackage.getTotalAmount() > 0 && importPackage.getFreight() > 0 && importPackage.getCurrencyId() != null) {
						if (clientQuoteVo.getClientCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0 && importPackage.getFreight() != null && importPackage.getCurrencyId() != null) {
							if (!importPackage.getCurrencyId().equals(usRate.getCurrencyId())) {
								ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(importPackage.getCurrencyId());
								//兑换美元的倍数
								Double relative = new BigDecimal(currentRate.getRate()).divide(new BigDecimal(usRate.getRate()), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
								relative = relative * usRate.getRelativeRate();
								Double realFee = clientQuoteService.caculatePrice(new BigDecimal(importPackage.getFreight()), new BigDecimal(relative), new BigDecimal(exchangeRate.getRate())).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
								importFreight = importFreight + realFee/importPackage.getTotalAmount()*list.get(i).getAmount();
							}else {
								Double realFee = clientQuoteService.caculatePrice(new BigDecimal(importPackage.getFreight()), new BigDecimal(clientQuoteElement.getRelativeRate()), new BigDecimal(1)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
								importFreight = importFreight + realFee/importPackage.getTotalAmount()*list.get(i).getAmount();
							}
						}else{
							Double currencyRate = 0.00;
							if (importPackage.getExchangeRate() != null) {
								currencyRate = importPackage.getExchangeRate();
							}else {
								ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(importPackage.getCurrencyId());
								currencyRate = currentRate.getRate();
							}
							Double rate = currencyRate/exchangeRate.getRate();
							importFreight = importFreight + importPackage.getFreight()*rate/importPackage.getTotalAmount()*list.get(i).getAmount();
						}
					}
					
					/*if (importPackage.getImportFee() != null && importPackage.getTotalAmount() > 0 && importPackage.getImportFee() > 0) {
						Double rate = importPackage.getExchangeRate()/exchangeRate.getRate();
						importFee = importFee + importPackage.getImportFee()*rate/importPackage.getTotalAmount()*list.get(i).getAmount();
						
					}
					if (importPackage.getFreight() != null && importPackage.getTotalAmount() > 0 && importPackage.getFreight() > 0) {
						Double rate = importPackage.getExchangeRate()/exchangeRate.getRate();
						importFreight = importFreight + importPackage.getFreight()*rate/importPackage.getTotalAmount()*list.get(i).getAmount();
					}*/
				}
				for (int i = 0; i < list.size(); i++) {
					List<ExportPackage> epList = exportPackageService.getByIpeId(list.get(i).getId());
					for (ExportPackage exportPackage : epList) {
						ExportPackage ep = exportPackageService.getToTalAmount(exportPackage.getId());
						ExchangeRate exchangeRate = exchangeRateService.selectByPrimaryKey(10);
						//出库费用
						if (exportPackage.getExportFee() != null && ep.getTotalAmount() > 0 && exportPackage.getExportFee() > 0 && exportPackage.getFeeCurrencyId() != null) {
							if (clientQuoteVo.getClientCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
								if (!exportPackage.getFeeCurrencyId().equals(usRate.getCurrencyId())) {
									ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(exportPackage.getFeeCurrencyId());
									//兑换美元的倍数
									Double relative = new BigDecimal(currentRate.getRate()).divide(new BigDecimal(usRate.getRate()), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
									relative = relative * usRate.getRelativeRate();
									Double realFee = clientQuoteService.caculatePrice(new BigDecimal(exportPackage.getExportFee()), new BigDecimal(relative), new BigDecimal(exchangeRate.getRate())).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
									exportFee = exportFee + realFee/ep.getTotalAmount()*list.get(i).getAmount();
								}else {
									Double realFee = clientQuoteService.caculatePrice(new BigDecimal(exportPackage.getExportFee()), new BigDecimal(clientQuoteElement.getRelativeRate()), new BigDecimal(1)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
									exportFee = exportFee + realFee/ep.getTotalAmount()*list.get(i).getAmount();
								}
							}else {
								Double currencyRate = 0.00;
								if (exportPackage.getFeeRate() != null) {
									currencyRate = exportPackage.getFeeRate();
								}else {
									ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(exportPackage.getFeeCurrencyId());
									currencyRate = currentRate.getRate();
								}
								Double rate = currencyRate/exchangeRate.getRate();
								exportFee = exportFee + exportPackage.getExportFee()*rate/ep.getTotalAmount()*list.get(i).getAmount();
							}
						}
						
						//出库运费
						if (exportPackage.getFreight() != null && ep.getTotalAmount() > 0 && exportPackage.getFreight() > 0 && exportPackage.getFeeCurrencyId() != null) {
							if (clientQuoteVo.getClientCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
								if (!exportPackage.getFeeCurrencyId().equals(usRate.getCurrencyId())) {
									ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(exportPackage.getFeeCurrencyId());
									//兑换美元的倍数
									Double relative = new BigDecimal(currentRate.getRate()).divide(new BigDecimal(usRate.getRate()), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
									relative = relative * usRate.getRelativeRate();
									Double realFee = clientQuoteService.caculatePrice(new BigDecimal(exportPackage.getFreight()), new BigDecimal(relative), new BigDecimal(exchangeRate.getRate())).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
									exportFreight = exportFreight + realFee/ep.getTotalAmount()*list.get(i).getAmount();
								}else {
									Double realFee = clientQuoteService.caculatePrice(new BigDecimal(exportPackage.getFreight()), new BigDecimal(clientQuoteElement.getRelativeRate()), new BigDecimal(1)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
									exportFreight = exportFreight + realFee/ep.getTotalAmount()*list.get(i).getAmount();
								}
							}else {
								Double currencyRate = 0.00;
								if (exportPackage.getFeeRate() != null) {
									currencyRate = exportPackage.getFeeRate();
								}else {
									ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(exportPackage.getFeeCurrencyId());
									currencyRate = currentRate.getRate();
								}
								Double rate = currencyRate/exchangeRate.getRate();
								exportFreight = exportFreight + exportPackage.getFreight()*rate/ep.getTotalAmount()*list.get(i).getAmount();
							}
						}
						
						/*if (exportPackage.getExportFee() != null && ep.getTotalAmount() > 0 && exportPackage.getExportFee() > 0) {
							Double rate = exportPackage.getFeeRate()/exchangeRate.getRate();
							exportFee = exportFee + exportPackage.getExportFee()*rate/ep.getTotalAmount()*list.get(i).getAmount();
						}
						if (exportPackage.getFreight() != null && ep.getTotalAmount() > 0 && exportPackage.getFreight() > 0) {
							Double rate = exportPackage.getFeeRate()/exchangeRate.getRate();
							exportFreight = exportFreight + exportPackage.getFreight()*rate/ep.getTotalAmount()*list.get(i).getAmount();
						}*/
					}
				}
				SupplierOrderElement supplierOrderElement = supplierOrderElementService.selectByPrimaryKey(clientQuoteVo.getId());
				SupplierQuoteElement supplierQuoteElement = supplierQuoteElemenetService.selectByPrimaryKey(supplierOrderElement.getSupplierQuoteElementId());
				if (clientQuoteVo.getHazmatFee() != null && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
					BigDecimal fee = new BigDecimal(clientQuoteVo.getHazmatFee());
					if (clientQuoteVo.getClientCode().startsWith("7")) {
						fee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getHazmatFee()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientQuoteVo.getCoExchangeRate()));
					}else {
						fee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getHazmatFee()), new BigDecimal(clientQuoteVo.getSoExchangeRate()),new BigDecimal(clientQuoteVo.getCoExchangeRate()));
					}
					totalBankCost = fee.doubleValue()*clientQuoteVo.getOrderAmount();
					hazFee = fee.doubleValue();
				}
				if (clientQuoteVo.getBankCost() != null && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
					BigDecimal fee = new BigDecimal(clientQuoteVo.getBankCost());
					if (clientQuoteVo.getClientCode().startsWith("7")) {
						fee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getBankCost()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientQuoteVo.getCoExchangeRate()));
					}else {
						fee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getBankCost()), new BigDecimal(clientQuoteVo.getSoExchangeRate()),new BigDecimal(clientQuoteVo.getCoExchangeRate()));
					}
					totalBankCost = fee.doubleValue();
					if (clientQuoteVo.getMoq() != null && !"".equals(clientQuoteVo.getMoq())) {
						if (clientQuoteVo.getMoq() > clientQuoteVo.getOrderAmount()) {
							bankCost = fee.doubleValue()/clientQuoteVo.getMoq();
						}else {
							bankCost = fee.doubleValue()/clientQuoteVo.getOrderAmount();
						}
					}else {
						bankCost = fee.doubleValue()/clientQuoteVo.getOrderAmount();
					}
				}
				if (clientQuoteVo.getFeeForExchangeBill() != null && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
					BigDecimal fee = new BigDecimal(clientQuoteVo.getFeeForExchangeBill());
					if (clientQuoteVo.getClientCode().startsWith("7")) {
						fee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getFeeForExchangeBill()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientQuoteVo.getCoExchangeRate()));
					}else {
						fee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getFeeForExchangeBill()), new BigDecimal(clientQuoteVo.getSoExchangeRate()),new BigDecimal(clientQuoteVo.getCoExchangeRate()));
					}
					totalFeeForExchangeBill = fee.doubleValue();
					if (clientQuoteVo.getMoq() != null && !"".equals(clientQuoteVo.getMoq())) {
						if (clientQuoteVo.getMoq() > clientQuoteVo.getOrderAmount()) {
							feeForExchangeBill = fee.doubleValue()/clientQuoteVo.getMoq();
						}else {
							feeForExchangeBill = fee.doubleValue()/clientQuoteVo.getOrderAmount();
						}
					}else {
						feeForExchangeBill = fee.doubleValue()/clientQuoteVo.getOrderAmount();
					}
				}
				if (clientQuoteVo.getOtherFee() != null && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
					BigDecimal fee = new BigDecimal(clientQuoteVo.getOtherFee());
					if (clientQuoteVo.getClientCode().startsWith("7")) {
						fee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getOtherFee()), new BigDecimal(clientQuoteElement.getRelativeRate()),new BigDecimal(clientQuoteVo.getCoExchangeRate()));
					}else {
						fee = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getOtherFee()), new BigDecimal(clientQuoteVo.getSoExchangeRate()),new BigDecimal(clientQuoteVo.getCoExchangeRate()));
					}
					totalOtherFee = fee.doubleValue();
					if (clientQuoteVo.getMoq() != null && !"".equals(clientQuoteVo.getMoq())) {
						if (clientQuoteVo.getMoq() > clientQuoteVo.getOrderAmount()) {
							otherFee = fee.doubleValue()/clientQuoteVo.getMoq();
						}else {
							otherFee = fee.doubleValue()/clientQuoteVo.getOrderAmount();
						}
					}else {
						otherFee = fee.doubleValue()/clientQuoteVo.getOrderAmount();
					}
				}
//				importFee = importFee * usRate.getRate()/clientQuoteVo.getSupplierOrderAmount();
//				importFreight = importFreight * usRate.getRate()/clientQuoteVo.getSupplierOrderAmount();
//				exportFee = exportFee * usRate.getRate()/clientQuoteVo.getSupplierOrderAmount();
//				exportFreight = exportFreight * usRate.getRate()/clientQuoteVo.getSupplierOrderAmount();
				//hazFee = hazFee * usRate.getRate()/clientQuoteVo.getSupplierOrderAmount();
				//bankCost = bankCost * usRate.getRate()/clientQuoteVo.getSupplierOrderAmount();
				clientQuoteVo.setImportFee(new BigDecimal(importFee).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				clientQuoteVo.setImportFreight(new BigDecimal(importFreight).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				clientQuoteVo.setExportFee(new BigDecimal(exportFee).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				clientQuoteVo.setExportFreight(new BigDecimal(exportFreight).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				clientQuoteVo.setHazFee(new BigDecimal(hazFee).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				clientQuoteVo.setBankCost(new BigDecimal(bankCost).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				clientQuoteVo.setFeeForExchangeBill(new BigDecimal(feeForExchangeBill).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				clientQuoteVo.setOtherFee(new BigDecimal(otherFee).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				//clientQuoteVo.setQuoteTotalPrice(new BigDecimal(clientQuoteVo.getQuoteTotalPrice()+importFee+importFreight+exportFee+exportFreight+totalBankCost+totalFeeForExchangeBill+totalHaz+totalOtherFee).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
				
				
				if (clientQuoteVo.getFixedCost() != null) {
					Double fixedCost = clientQuoteVo.getFixedCost();
					if (fixedCost < new Double(1)) {
						clientQuoteVo.setFixedCost(new Double(df.format(clientQuoteVo.getOrderBasePrice()*fixedCost)));
					}else if(fixedCost > new Double(1)){
						/*Double orderPrice=clientOrderService.getOrderPrice(clientQuoteVo.getClientOrderId());
						Double rate=fixedCost/orderPrice;
						fixedCost=clientQuoteVo.getOrderBasePrice()*rate;*/
						/*if(!clientQuoteVo.getCoCurrencyValue().equals(clientQuoteVo.getSoCurrencyValue())){
						clientQuoteVo.setFixedCost(new Double(df.format(fixedCost*clientQuoteVo.getCoExchangeRate())));
						}else{
							clientQuoteVo.setFixedCost(new Double(df.format(fixedCost)));
						}*/
						clientQuoteVo.setFixedCost(new Double(df.format(fixedCost)));
					}
					Double counterFee=0.0;
					Double bankCharges=0.0;
					if(null!=clientQuoteVo.getCounterFee()){
						if(clientQuoteVo.getCounterFee()<1){
							counterFee=clientQuoteVo.getCounterFee()*clientQuoteVo.getQuoteBasePrice();
						}else{
							counterFee=0.0;
						}
					}
					if(null!=clientQuoteVo.getBankCharges()){
						if(clientQuoteVo.getBankCharges()<1){
							bankCharges=clientQuoteVo.getBankCharges()*clientQuoteVo.getOrderBasePrice();
						}else{
							bankCharges=clientQuoteVo.getBankCharges();
						}
					}
					ClientQuote clientQuote = clientQuoteService.selectByPrimaryKey(clientQuoteElement.getClientQuoteId());
					ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientQuote.getClientInquiryId());
					Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
					if (!clientQuoteVo.getSupplierOrderCurrencyId().equals(10)) {
						if (client.getCode().startsWith("7") && clientQuoteElement.getRelativeRate() != null && clientQuoteElement.getRelativeRate() > 0) {
							if (!clientQuoteVo.getSupplierOrderCurrencyId().equals(usRate.getCurrencyId())) {
								ExchangeRate currentRate = exchangeRateService.selectByPrimaryKey(clientQuoteVo.getSupplierOrderCurrencyId());
								//兑换美元的倍数
								Double relative = new BigDecimal(currentRate.getRate()).divide(new BigDecimal(usRate.getRate()), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
								relative = relative * usRate.getRelativeRate();
								Double realPrice = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getSupplierOrderElementPrice()), new BigDecimal(relative), new BigDecimal(1)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
								clientQuoteVo.setQuoteBasePrice(realPrice);
								clientQuoteVo.setQuoteTotalPrice(realPrice * clientQuoteVo.getSupplierOrderAmount());
							}else {
								Double realPrice = clientQuoteService.caculatePrice(new BigDecimal(clientQuoteVo.getSupplierOrderElementPrice()), new BigDecimal(usRate.getRelativeRate()), new BigDecimal(1)).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
								clientQuoteVo.setQuoteBasePrice(realPrice);
								clientQuoteVo.setQuoteTotalPrice(realPrice * clientQuoteVo.getSupplierOrderAmount());
							}
						}
					}
//					Double profitMargin = ((clientQuoteVo.getOrderBasePrice()-clientQuoteVo.getFixedCost()-bankCharges-(importFee+importFreight+exportFee+exportFreight)/clientQuoteVo.getSupplierOrderAmount()-hazFee-bankCost-otherFee-feeForExchangeBill-clientQuoteVo.getProfitOtherFee()/clientQuoteVo.getOrderAmount()-(clientQuoteVo.getQuoteBasePrice()+counterFee/*+clientQuoteVo.getFreight()*/))/
//							(clientQuoteVo.getOrderBasePrice()-clientQuoteVo.getFixedCost()-bankCharges-(importFee+importFreight+exportFee+exportFreight)/clientQuoteVo.getSupplierOrderAmount()-hazFee-bankCost-otherFee-feeForExchangeBill-clientQuoteVo.getProfitOtherFee()/clientQuoteVo.getOrderAmount()-counterFee))*100;

					Double profitMargin = ((clientQuoteVo.getOrderBasePrice()-clientQuoteVo.getFixedCost()-bankCharges-(importFee+importFreight+exportFee+exportFreight)/clientQuoteVo.getSupplierOrderAmount()-hazFee-bankCost-otherFee-feeForExchangeBill-clientQuoteVo.getProfitOtherFee()/clientQuoteVo.getOrderAmount()-(clientQuoteVo.getQuoteBasePrice()+counterFee/*+clientQuoteVo.getFreight()*/))/
							(clientQuoteVo.getOrderBasePrice()))*100;

					double INFINITY = Double.POSITIVE_INFINITY;
					Double NAN = new Double(Double.NaN);
					if(null!=profitMargin&&!profitMargin.equals(NAN)&&!profitMargin.equals(-INFINITY)&&!profitMargin.equals(INFINITY)){
						clientQuoteVo.setProfitMargin(new Double(df.format(profitMargin)));
					}
				}

				Map<String, Object> map = EntityUtil.entityToTableMap(clientQuoteVo);
				map.put("profitMargin", map.get("profitMargin")+"%");
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
	 * 文件管理
	 */
	@RequestMapping(value="/file",method=RequestMethod.GET)
	public String file(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "client_quote");
		return "/marketing/clientinquiry/fileUpload";
	}
	
	/**
	 * 供应商件号附件管理
	 */
	@RequestMapping(value="/partfile",method=RequestMethod.GET)
	public String partfile(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "client_quote_view");
		request.setAttribute("type", request.getParameter("type"));
//		String type=request.getParameter("type");
		return "/marketing/clientquote/fileUpload";
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
		return "/marketing/clientquote/list";
	}
	
	/**
	 * 供应商件号附件数据
	 */
	@RequestMapping(value = "/data", method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo attachmentlistdata(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<GyFj> page = getPage(request);
		
		String businessKey = getString(request, "businessKey");
		page.put("businessKey", businessKey);
		
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		String type=request.getParameter("type");
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
			
		}
		String[] clietnInquiryId=type.split("\\?");
		if(clietnInquiryId[0].equals("marketing")){
			page.put("parm", "and YW_TABLE_NAME ='client_quote_view'"+"))");
		}else{
			String ywid = businessKey.substring(businessKey.lastIndexOf(".") + 1, businessKey.length());
			page.put("parm", "and YW_TABLE_NAME ='supplier_quote') or (fj.YW_ID ='"+ywid+"' and YW_TABLE_NAME ='supplier_quote_element' ) or  (YW_TABLE_NAME ='client_quote_view' and fj.YW_ID ="+clietnInquiryId[0]+"))");
		}
		
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		if (StringUtils.isNotBlank(searchString)) {

		} else {

		}
	
		gyFjService.searchPage(page, searchString, getSort(request));
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
	 * 供应商件号附件管理-历史报价
	 */
	@RequestMapping(value="/partfileHis",method=RequestMethod.GET)
	public String partfileHis(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		String clientInquiryElementId = getString(request, "clientInquiryElementId");
		request.setAttribute("clientInquiryElementId", getString(request, "clientInquiryElementId"));
		request.setAttribute("tableName", "client_quote_view");
		request.setAttribute("type", request.getParameter("type"));
//		String type=request.getParameter("type");
		return "/marketing/historyquote/fileUpload";
	}
	
	@RequestMapping(value = "/listForHisFile", method = RequestMethod.GET)
	public String listForHisFile(HttpServletRequest request) {
		String data = request.getParameter("data");
		Assert.notNull(data);
		request.setAttribute("data", data);
		String clientInquiryElementId = getString(request, "clientInquiryElementId");
		request.setAttribute("clientInquiryElementId", getString(request, "clientInquiryElementId"));
		request.setAttribute("datajson", JsonUtils.urlParam2Json(data));
		PageData pd = new PageData(request);
		if (pd != null) {
			request.setAttribute("pd", JsonUtils.toJson(pd));
		}
		request.setAttribute("type", request.getParameter("type"));
//		String type=request.getParameter("type");
		return "/marketing/historyquote/filelist";
	}
	
	/**
	 * 供应商件号附件数据-历史报价
	 */
	@RequestMapping(value = "/partData", method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo partData(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<GyFj> page = getPage(request);
		
		String businessKey = getString(request, "businessKey");
		String clientInquiryElementId = getString(request, "clientInquiryElementId");
		ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(new Integer(clientInquiryElementId));
		page.put("businessKey", businessKey);
		
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		String type=request.getParameter("type");
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
			
		}
		String[] clietnInquiryId=type.split("\\?");
		if(clietnInquiryId[0].equals("marketing")){
			page.put("append", "((YW_TABLE_NAME ='client_quote_view' and FJ_NAME like'%"+clientInquiryElement.getPartNumber()+"%' AND FJ_NAME LIKE '%FjBy%'))");
		}
		
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		if (StringUtils.isNotBlank(searchString)) {

		} else {

		}
	
		gyFjService.searchPageForHis(page, searchString, getSort(request));
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
	
	
	/*
	 * 错误列表
	 */
	@RequestMapping(value="/toUnknow",method=RequestMethod.GET)
	public String toUnknow() {
		return "/marketing/clientquote/errorlist";
	}
	
	/*
	 * 错误列表数据
	 */
	@RequestMapping(value="/Unknow",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo Unknow(HttpServletRequest request) {
		PageModel<ClientQuoteElementUpload> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		page.put("userId", userVo.getUserId());
		clientQuoteElementUploadService.selectByUserId(page);;
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientQuoteElementUpload clientQuoteElementUpload : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(clientQuoteElementUpload);
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
		clientQuoteElementUploadService.deleteByPrimaryKey(new Integer(userVo.getUserId()));
		return new ResultVo(success, message);
	}
	
	/*
	 * 多份报价单列表
	 */
	@RequestMapping(value="/quotesListPage",method=RequestMethod.GET)
	public String quotesListPage(HttpServletRequest request) {
		request.setAttribute("clientInquiryId", request.getParameter("client_inquiry_id"));
		request.setAttribute("clientTemplateType", request.getParameter("clientTemplateType"));
		request.setAttribute("bizTypeId", request.getParameter("bizTypeId"));
		request.setAttribute("currencyId", request.getParameter("currencyId"));
		return "/marketing/clientquote/quotelist";
	}
	
	/*
	 * 多份报价单列表数据
	 */
	@RequestMapping(value="/quotesList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo quotesList(HttpServletRequest request) {
		PageModel<ClientQuote> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String clientInquiryId=request.getParameter("clientInquiryId");
		page.put("clientInquiryId", clientInquiryId);
		clientQuoteService.selectByclientInquiryId(page);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientQuote clientQuote : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(clientQuote);
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
	 * 利润表
	 * *
	@RequestMapping(value="/excel",method=RequestMethod.GET)
	public String excel(HttpServletRequest request){
		return "/demo/excelMgmtDemo";
	}*/
	
	/**
	 * 下拉列表数据-客户
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/searchclient",method=RequestMethod.POST)
	public @ResponseBody ResultVo searchclient(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		UserVo user = ContextHolder.getCurrentUser();
		AuthorityRelation authorityRelation=new AuthorityRelation();
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (roleVo.getRoleName().equals("管理员") || roleVo.getRoleName().equals("行政") || roleVo.getRoleName().equals("财务")|| roleVo.getRoleName().indexOf("物流") >= 0) {
			authorityRelation.setUserId(null);
		}else{
			authorityRelation.setUserId(Integer.parseInt(user.getUserId()));
		}
		List<ClientQuoteVo> list1=clientQuoteService.searchclient(authorityRelation);
		success = true;
		JSONArray json = new JSONArray();
		json.add(list1);
		msg =json.toString();
		return new ResultVo(success, msg);
		
	}
	/**
	 * 下拉列表数据-机型
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/searchairType",method=RequestMethod.POST)
	public @ResponseBody ResultVo searchairType(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		
		List<ClientQuoteVo> list=clientQuoteService.searchairType();
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
		
	}
	/**
	 * 下拉列表数据-商业类型
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/searchbizType",method=RequestMethod.POST)
	public @ResponseBody ResultVo searchbizType(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		List<ClientQuoteVo> list=clientQuoteService.searchbizType();
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 *供应商订单数量 
	 * **/
	@RequestMapping(value="/supplierOrderAmount",method=RequestMethod.POST)
	public @ResponseBody ResultVo supplierOrderAmount(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		List<ProfitStatementVo> list=clientQuoteService.findSupplierOrderAmount(Integer.parseInt(request.getParameter("clientOrderElementId")));
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 下拉列表数据-币种
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/searchcurrency",method=RequestMethod.POST)
	public @ResponseBody ResultVo searchcurrency(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String currencyid=request.getParameter("id");
		List<ClientQuoteVo> list=clientQuoteService.searchcurrency();
		List<ClientQuoteVo> arraylist=new ArrayList();
		
		for (int i = 0; i < list.size(); i++) {
			ClientQuoteVo clientQuoteVo=list.get(i);
			Integer id=clientQuoteVo.getCurrency_id();
			String value=clientQuoteVo.getCurrency_value();
			if(currencyid!=null&&id!=null){
				if(currencyid.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==currencyid||"".equals(currencyid)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 下拉列表数据-交付方式
	 * @param request
	 * @param response
	 * @author Tanoy 2016-5-7 上午9：20：00
	 * @throws IOException 
	 */
	@RequestMapping(value="/searchDelivery",method=RequestMethod.POST)
	public @ResponseBody ResultVo searchDelivery(HttpServletRequest request,HttpServletResponse response) throws IOException {
		boolean success = false;
		String msg = "";
		String termsid=request.getParameter("id");
		List<SystemCode> list = new ArrayList<SystemCode>();
		list = systemCodeService.delivery();
		List<SystemCode> arraylist=new ArrayList();
		
		for (int i = 0; i < list.size(); i++) {
			SystemCode systemCode=list.get(i);
			Integer id=systemCode.getId();
			String value=systemCode.getValue();
			if(termsid!=null&&id!=null){
				if(termsid.equals(id+"")){
					
					for (int j = 0; j < list.size(); j++) {
						arraylist.add(list.get(j));
					}
					arraylist.remove(i);
				}
				}				
		}
		success = true;
		JSONArray json = new JSONArray();
		if(null==termsid||"".equals(termsid)){
			json.add(list);
		}else{
			json.add(arraylist);
		}
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 历史报价页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/historyQuote",method=RequestMethod.GET)
	public String historyQuote(HttpServletRequest request){
		return "/marketing/historyquote/list";
	}
	
	
	/**
	 * 获取客户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getClients",method=RequestMethod.POST)
	public @ResponseBody ResultVo getClients(HttpServletRequest request){
		String clientId = getString(request, "clientId");
		if (clientId != null && !"".equals(clientId)) {
			Client client = clientService.selectByPrimaryKey(new Integer(clientId));
			String message = client.getBankCost()+","+client.getFixedCost();
			return new ResultVo(true, message);
		}
		return new ResultVo(false, "");
	}
	
	/**
	 * 根据询价明细查询是否已报价
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkIfHasQuote",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkIfHasQuote(HttpServletRequest request){
		String clientInquiryElementId = getString(request, "id");
		if (clientInquiryElementId != null && !"".equals(clientInquiryElementId)) {
			ClientQuoteElement clientQuoteElement = clientQuoteElementService.selectByClientInquiryElementId(new Integer(clientInquiryElementId));
			if (clientQuoteElement != null) {
				return new ResultVo(true, clientQuoteElement.getSupplierQuoteElementId().toString());
			}
		}
		return new ResultVo(false, "");
	}
	
	
	@RequestMapping(value="/quoteListToday",method=RequestMethod.GET)
	public String quoteListToday(){
		return "/marketing/historyquote/clientQuoteListToday";
	}
	
	
	/**
	 * 监测是否已经生成报价单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkQuote",method = RequestMethod.POST)
	public @ResponseBody ResultVo checkQuote(HttpServletRequest request){
		Integer clientInquiryId = new Integer(getString(request, "clientInquiryId"));
		List<ClientQuote> list = clientQuoteService.getByClientInquiryId(clientInquiryId);
		if (list.size() > 0) {
			return new ResultVo(true, list.get(0).getCurrencyId().toString());
		}else {
			return new ResultVo(false, "");
		}
	}
	
	/**
	 * 财务添加杂费
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addOtherFeeInProfit",method = RequestMethod.POST)
	private @ResponseBody EditRowResultVo addOtherFeeInProfit(HttpServletRequest request){
		try {
			Integer id = new Integer(getString(request, "id"));
			String otherFee = getString(request, "profitOtherFee");
			if (otherFee != null && !"".equals(otherFee)) {
				SupplierOrderElement supplierOrderElement = new SupplierOrderElement();
				supplierOrderElement.setId(id);
				supplierOrderElement.setProfitOtherFee(new Double(otherFee));
				supplierOrderElementService.updateByPrimaryKeySelective(supplierOrderElement);
				return new EditRowResultVo(true, "修改成功！");
			}
			return new EditRowResultVo(false, "修改失败！");
		} catch (Exception e) {
			e.printStackTrace();
			return new EditRowResultVo(false, "修改异常！");
		}
	}
	
	/**
	 * 根据件号获取历史订单的数量
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getListCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getListCount(HttpServletRequest request){
		try {
			String partNumber = getString(request, "partNumber");
			if (partNumber != null) {
				List<HistoricalOrderPrice> list = historicalOrderPriceService.getByPart(partNumber);
				return new ResultVo(true, String.valueOf(list.size()));
			}else {
				return new ResultVo(true, "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(true, "0");
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
