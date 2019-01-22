package com.naswork.module.purchase.controller.supplierinquirystatistic;

import java.text.DecimalFormat;
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

import com.naswork.common.controller.BaseController;
import com.naswork.filter.ContextHolder;
import com.naswork.model.SupplierImportElement;
import com.naswork.model.SupplierOrder;
import com.naswork.model.SupplierQuote;
import com.naswork.module.purchase.controller.supplierinquiry.ManageVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.service.SupplierImportElementService;
import com.naswork.service.SupplierInquiryService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/supplierstatistic")
public class SupplierInquiryStatisticController extends BaseController {

	@Resource
	private SupplierInquiryService supplierInquiryService;
	@Resource
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private SupplierImportElementService supplierImportElementService;
	@Resource
	private UserService userService;
	/**
	 * 列表页面
	 * **/
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(){
		return "/purchase/supplierInquiry/supplierinquirystatistic";
	}
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/listdate",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo listdate(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<SupplierInquiryStatistic> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String sid =request.getParameter("sid");
		String airId=request.getParameter("airId");
		String bizId=request.getParameter("bizId");
		String start=request.getParameter("start");
		String end=request.getParameter("end");
		UserVo user = ContextHolder.getCurrentUser();
		SupplierInquiryStatistic supplierInquiryStatistic=new SupplierInquiryStatistic();
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			supplierInquiryStatistic.setUserId(user.getUserId());
		}
		if(null!=sid&&!"".equals(sid)){
		supplierInquiryStatistic.setSupplierId(Integer.parseInt(sid));
		}
		if(null!=airId&&!"".equals(airId)){
		supplierInquiryStatistic.setAirTypeId(Integer.parseInt(airId));
		}
		if(null!=bizId&&!"".equals(bizId)){
		supplierInquiryStatistic.setBizTypeId(Integer.parseInt(bizId));
		}
		if(null!=start&&!"".equals(start)){
		supplierInquiryStatistic.setStartDate(start);
		}
		if(null!=end&&!"".equals(end)){
		supplierInquiryStatistic.setEndDate(end);
		}
		List<SupplierInquiryStatistic> siList=supplierInquiryService.supplierInquiryStat(supplierInquiryStatistic);
		List<SupplierInquiryStatistic> sqList=supplierQuoteService.supplierQuoteStat(supplierInquiryStatistic);
		List<SupplierInquiryStatistic> soList=supplierOrderService.supplierOrderStat(supplierInquiryStatistic);
		List<SupplierInquiryStatistic> simList=supplierImportElementService.supplierImportStat(supplierInquiryStatistic);
		List<SupplierInquiryStatistic> list = generateSupplierList(siList, sqList, soList, simList);
		page.setEntities(list);
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			int sic = 0;
			int sqc = 0;
			Double sqs = 0.0;
			int soc = 0;
			Double sos = 0.0;
			int simc = 0;
			Double sims = 0.0;
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierInquiryStatistic supplierQuoteVo : page.getEntities()) {
				if(null!=supplierQuoteVo.getSupplierInquiryCount()){
				 sic+=supplierQuoteVo.getSupplierInquiryCount();
				}
				if(null!=supplierQuoteVo.getSupplierQuoteCount()){
				 sqc+=supplierQuoteVo.getSupplierQuoteCount();
				Double rate= (double)supplierQuoteVo.getSupplierQuoteCount()/supplierQuoteVo.getSupplierInquiryCount();
				supplierQuoteVo.setQuotationRate(Double.parseDouble(new DecimalFormat("#.00").format(rate*100))+"%");
				}
				 if(null!=supplierQuoteVo.getSupplierQuoteSum()){
				 sqs+=supplierQuoteVo.getSupplierQuoteSum();
				 }
				 if(null!=supplierQuoteVo.getSupplierOrderCount()){
				 soc+=supplierQuoteVo.getSupplierOrderCount();
				 }
				 if(null!=supplierQuoteVo.getSupplierOrderSum()){
				 sos+=supplierQuoteVo.getSupplierOrderSum();
				 }
				 if(null!=supplierQuoteVo.getSupplierImportCount()){
				 simc+=supplierQuoteVo.getSupplierImportCount();
				 }
				 if(null!=supplierQuoteVo.getSupplierImportSum()){
				 sims+=supplierQuoteVo.getSupplierImportSum();
				 }
				 supplierQuoteVo.setAmount(1);
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteVo);
				mapList.add(map);
			}
			SupplierInquiryStatistic supplierQuoteVo=new SupplierInquiryStatistic();
			supplierQuoteVo.setBizTypeCode("总计");
			supplierQuoteVo.setSupplierInquiryCount(sic);
			supplierQuoteVo.setSupplierQuoteCount(sqc);
			supplierQuoteVo.setSupplierQuoteSum(Double.parseDouble(new DecimalFormat("#.0000").format(sqs)));
			supplierQuoteVo.setSupplierOrderCount(soc);
			supplierQuoteVo.setSupplierOrderSum(Double.parseDouble(new DecimalFormat("#.0000").format(sos)));
			supplierQuoteVo.setSupplierImportCount(simc);
			supplierQuoteVo.setSupplierImportSum(Double.parseDouble(new DecimalFormat("#.0000").format(sims)));
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierQuoteVo);
			mapList.add(map);
			jqgrid.setRows(mapList);
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					String[] array=exportModel.split("\\}\\,\\{");
					exportModel=array[0].substring(2);
					exportModel="[{\"name\":\"数量\",\"width\":50,\"align\":0,\"property\":\"amount\"},{"+exportModel+"},{";
					for (int i = 1; i < array.length; i++) {
						
						if(array[i].indexOf("报价单数量")>-1){
							exportModel+=array[i]+"},{\"name\":\"报价单率\",\"width\":100,\"align\":0,\"property\":\"quotationRate\"},{\"name\":\"报价项目数\",\"width\":100,\"align\":0,\"property\":\"quoteAmount\"},{";
						}else if(i==array.length-1){
							exportModel+=array[i];
						}
						else{
							exportModel+=array[i]+"},{";
						}
					}
					exportModel=exportModel.replaceAll("width\":112", "width\":60");
					exportModel=exportModel.replaceAll("width\":157", "width\":70");
					exportModel=exportModel.replaceAll("width\":179", "width\":80");
					exportService.exportGridToXls("供应商询价单统计", exportModel, mapList, response);
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
	
	
	private List<SupplierInquiryStatistic> generateSupplierList(List<SupplierInquiryStatistic> siList, List<SupplierInquiryStatistic> sqList, List<SupplierInquiryStatistic> soList,
			List<SupplierInquiryStatistic> simList) {
		return generateSupplierList(
				generateSupplierList(generateSupplierList(siList, sqList),
						soList), simList);
	}

	private List<SupplierInquiryStatistic> generateSupplierList(List<SupplierInquiryStatistic> list1, List<SupplierInquiryStatistic> list2) {
		if (list1 == null || list1.size() == 0) {
			return list2;
		} else if (list2 == null || list2.size() == 0) {
			return list1;
		} else {
			for (int i = 0; i < list1.size(); i++) {
				SupplierInquiryStatistic list3= list1.get(i);
				for (int j = 0; j < list2.size(); j++) {
					SupplierInquiryStatistic list4= list2.get(j);
					if (equalsSupplierMap(list3, list4)) {
						if(null==list3.getSupplierQuoteCount()&&null!=list4.getSupplierQuoteCount()){
							list3.setSupplierQuoteCount(list4.getSupplierQuoteCount());
						}
						if(null==list3.getSupplierQuoteSum()&&null!=list4.getSupplierQuoteSum()){
							list3.setSupplierQuoteSum(list4.getSupplierQuoteSum());
						}
						if(null==list3.getSupplierOrderCount()&&null!=list4.getSupplierOrderCount()){
							list3.setSupplierOrderCount(list4.getSupplierOrderCount());
						}
						if(null==list3.getSupplierOrderSum()&&null!=list4.getSupplierOrderSum()){
							list3.setSupplierOrderSum(list4.getSupplierOrderSum());
						}
						if(null==list3.getSupplierImportCount()&&null!=list4.getSupplierImportCount()){
							list3.setSupplierImportCount(list4.getSupplierImportCount());
						}
						if(null==list3.getSupplierImportSum()&&null!=list4.getSupplierImportSum()){
							list3.setSupplierImportSum(list4.getSupplierImportSum());
						}
						if(null==list3.getQuoteAmount()&&null!=list4.getQuoteAmount()){
							list3.setQuoteAmount(list4.getQuoteAmount());
						}
					}
				}
			}

			for (int j = 0; j < list2.size(); j++) {
				SupplierInquiryStatistic list3= list2.get(j);
				boolean exists = false;
				for (int i = 0; i < list1.size(); i++) {
					SupplierInquiryStatistic list4= list1.get(i);
					if (equalsSupplierMap(list3, list4)) {
						exists = true;
					}
				}
				if (exists) {
					break;
				} else {
					list1.add(list2.get(j));
				}
			}
		}
		return list1;
	}
	
	private boolean equalsSupplierMap(SupplierInquiryStatistic supplierInquiryStatistic, SupplierInquiryStatistic supplierInquiryStatistic2) {
		return equalsSupplier(supplierInquiryStatistic, supplierInquiryStatistic2) && equalsAirType(supplierInquiryStatistic, supplierInquiryStatistic2)
				&& equalsBizType(supplierInquiryStatistic, supplierInquiryStatistic2);
	}
	
	private boolean equalsSupplier(SupplierInquiryStatistic supplierInquiryStatistic, SupplierInquiryStatistic supplierInquiryStatistic2) {
		return equalsMapFieldSupplierId(supplierInquiryStatistic, supplierInquiryStatistic2);
	}
	
	private boolean equalsAirType(SupplierInquiryStatistic supplierInquiryStatistic, SupplierInquiryStatistic supplierInquiryStatistic2) {
		return equalsMapFieldAirTypeId(supplierInquiryStatistic, supplierInquiryStatistic2);
	}
	
	private boolean equalsBizType(SupplierInquiryStatistic supplierInquiryStatistic, SupplierInquiryStatistic supplierInquiryStatistic2) {
		return equalsMapFieldBizTypeId(supplierInquiryStatistic, supplierInquiryStatistic2);
	}
	private boolean equalsMapFieldSupplierId(SupplierInquiryStatistic supplierInquiryStatistic, SupplierInquiryStatistic supplierInquiryStatistic2) {
		return supplierInquiryStatistic.getSupplierId().equals(supplierInquiryStatistic2.getSupplierId());
	}
	private boolean equalsMapFieldAirTypeId(SupplierInquiryStatistic supplierInquiryStatistic, SupplierInquiryStatistic supplierInquiryStatistic2) {
		return supplierInquiryStatistic.getAirTypeId().equals(supplierInquiryStatistic2.getAirTypeId());
	}
	private boolean equalsMapFieldBizTypeId(SupplierInquiryStatistic supplierInquiryStatistic, SupplierInquiryStatistic supplierInquiryStatistic2) {
		return supplierInquiryStatistic.getBizTypeId().equals(supplierInquiryStatistic2.getBizTypeId());
	}
}
