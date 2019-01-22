package com.naswork.module.finance.controller.importpayment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ArrearsUse;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.SupplierDebt;
import com.naswork.model.SystemCode;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.service.ArrearsUseService;
import com.naswork.service.ImportPackagePaymentElementService;
import com.naswork.service.ImportPackagePaymentService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.Jbpm4JbyjService;
import com.naswork.service.SupplierDebtService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.SystemCodeService;
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
@RequestMapping(value="/finance/importpackagepayment")
public class ImportPackagePaymentController extends BaseController{

	@Resource
	private ImportPackagePaymentService importPackagePaymentService;
	@Resource
	private ImportPackageService importPackageService;
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private SupplierOrderElementService supplierOrderElementService;
	@Resource
	private UserService userService;
	@Resource
	private SupplierDebtService supplierDebtService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ArrearsUseService arrearsUseService;
	@Resource
	private ImportPackagePaymentElementService importPackagePaymentElementService;
	@Resource
	private Jbpm4JbyjService jbpm4JbyjService;
	/**
	 * 跳转付款管理页面
	 * @return
	 */
	@RequestMapping(value="/toList",method=RequestMethod.GET)
	public String toList() {
		return "/finance/importpayment/importpaymentList";
	}
	
	/**
	 * 付款列表数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/List",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo List(HttpServletRequest request) {
		PageModel<ImportPackagePayment> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		importPackagePaymentService.listPage(page, where, sort);
		
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
			String exportModel = getString(request, "exportModel");

			
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		

		return jqgrid;
	}
	

	
	/**
	 * 采购付款管理页面
	 * @return
	 */
	@RequestMapping(value="/toPaymenyList",method=RequestMethod.GET)
	public String toPaymenyList() {
		return "/finance/importpayment/importpaymentListForPurchase";
	}
	
	/**
	 * 搜索单号页面
	 * @return
	 */
	@RequestMapping(value="/toSearchNumber",method=RequestMethod.GET)
	public String toSearchNumber() {
		return "/finance/importpayment/searcheList";
	}
	
	/**
	 * 查询单号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/seach",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo seach(HttpServletRequest request) {
		PageModel<SearchVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		String[] wheres = where.split(" ");
		String number = "";
		for (int i = 0; i < wheres.length; i++) {
			if (wheres[i].equals("so.ORDER_NUMBER")) {
				number = "orderNumber";
				break;
			}
			if (wheres[i].equals("ip.IMPORT_NUMBER")) {
				number = "importNumber";
				break;
			}
		}
		if (number.equals("orderNumber")) {
			importPackagePaymentService.selectByOrderNumber(page, where);
		}
		if (number.equals("importNumber")) {
			importPackagePaymentService.selectByImportNumber(page, where);
		}
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SearchVo searchVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(searchVo);
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
	 * 保存付款单
	 * @param request
	 * @param importPackagePayment
	 * @return
	 */
	@RequestMapping(value="/savePayment",method=RequestMethod.POST)
	public @ResponseBody ResultVo savePayment(HttpServletRequest request,@ModelAttribute ImportPackagePayment payment) {
		String message = "";
		boolean success = false;
		if (payment.getId()!=null) {
			importPackagePaymentService.addBySearch(payment);
			message ="新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 新增付款单页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toAddPayment",method=RequestMethod.GET)
	public String toAddPayment(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		request.setAttribute("today", new Date());
		return "/finance/importpayment/addPayment";
	}
	
	/**
	 * 发起审核审批
	 * @param xm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/paymentRequest", method = RequestMethod.POST)
	public @ResponseBody ResultVo paymentRequest(@ModelAttribute ImportPackagePayment payment,@ModelAttribute(value="ids") String ids,HttpServletRequest request, HttpServletResponse response) {
		boolean success = true;
		String message = "提交成功！";
		try{
			importPackagePaymentService.paymentRequest(payment,ids);
		}catch(Exception e){
			success = false;
			message = "提交失败！";
		}
		
		return new ResultVo(success, message).setData(payment);
	}
	
	/**
	 * 插入审批意见
	 * **/
	@RequestMapping(value="/insertJbyj",  method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo insertJbyj(HttpServletRequest request,
			@ModelAttribute Jbpm4Jbyj record)
	{
		
		boolean success = true;
		String message = "更新完成！";
		String amount=request.getParameter("amount");
		String paymentPercentage=request.getParameter("paymentPercentage");
		String taskName=request.getParameter("taskName");
		String importPackagePaymentElementId=request.getParameter("importPackagePaymentElementId");
		try {
			Jbpm4Jbyj jbpm4Jbyj=jbpm4JbyjService.findGyJbyjByTaskId(request.getParameter("id"));
			 if(null!=jbpm4Jbyj){
				 jbpm4Jbyj.setJbyj(record.getJbyj());
				 jbpm4JbyjService.modify(jbpm4Jbyj);
			 }else{
					record.setCreateTime(new Date());
					record.setTaskId(request.getParameter("id"));
					jbpm4JbyjService.add(record);
			 }
			 
			 if(taskName.equals("cgzxsq")){
				 ImportPackagePaymentElement element=new ImportPackagePaymentElement();
				 element.setAmount(Double.parseDouble(amount));
				 element.setPaymentPercentage(Double.parseDouble(paymentPercentage));
				 element.setId(Integer.parseInt(importPackagePaymentElementId));
				 importPackagePaymentElementService.updateByPrimaryKeySelective(element);
			 }
		
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "更新失败";
		}
		EditRowResultVo result = new EditRowResultVo(success, message);
		return result;
		
	}
	
	/**
	 * 付款明细页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/elementlist",method=RequestMethod.GET)
	public String elementlist(HttpServletRequest request) {
		
		String ipeId=getString(request, "id");
		String[] ipeIds=ipeId.split(",");
		Integer importPackagePaymentId=0;
		Double shouldPay=0.0;
		Double arrearsUseTotal=0.0;
		AddSupplierOrderElementVo addSupplierOrderElementVo=new AddSupplierOrderElementVo();
		DecimalFormat xs = new DecimalFormat("#.##");
		for (int i = 0; i < ipeIds.length; i++) {
			ImportPackagePaymentElement importPackagePaymentElement=importPackagePaymentElementService.elementData(Integer.parseInt(ipeIds[i]));
			importPackagePaymentId=importPackagePaymentElement.getImportPackagePaymentId();
			shouldPay=shouldPay+importPackagePaymentElement.getShouldPay();
			ArrearsUse arrearsUse=arrearsUseService.selectByElementId(Integer.parseInt(ipeIds[i]));
			if(null!=arrearsUse){
				arrearsUseTotal=arrearsUseTotal+arrearsUse.getTotal();
			}
			 addSupplierOrderElementVo=supplierOrderElementService.findByElementId(importPackagePaymentElement.getSupplierOrderElementId());
		}
		request.setAttribute("supplierCode",addSupplierOrderElementVo.getSupplierCode());
		request.setAttribute("arrearsUseTotal", new Double(xs.format(arrearsUseTotal)));
		shouldPay=new Double(xs.format(shouldPay));
		ImportPackagePayment importPackagePayment=importPackagePaymentService.listById(importPackagePaymentId);
		SupplierDebt supplierDebt=supplierDebtService.totalArrears(importPackagePayment.getCode());
		request.setAttribute("taskName", request.getParameter("taskName"));
		request.setAttribute("id",importPackagePaymentId);
		request.setAttribute("importPackagePaymentElementId",ipeId);
		request.setAttribute("shouldPay", shouldPay);
		request.setAttribute("supplierCode", importPackagePayment.getCode());
		request.setAttribute("history", request.getParameter("history"));
		request.setAttribute("taskId", request.getParameter("taskId"));
		request.setAttribute("counterFee", importPackagePayment.getCounterFee());
		
		Double totalArrearsUse=arrearsUseService.selectTotalBySupplierCode(importPackagePayment.getCode());
		if(null!=supplierDebt){
			supplierDebt.setTotal(supplierDebt.getTotal()-totalArrearsUse);
		
			if(arrearsUseTotal==0.0&&supplierDebt.getTotal()>0&&request.getParameter("taskName").equals("cwsh")){
					if(supplierDebt.getTotal()>=shouldPay){
						request.setAttribute("arrearsUseTotal", new Double(xs.format(shouldPay)));
					}else if(supplierDebt.getTotal()<shouldPay){
						request.setAttribute("arrearsUseTotal", new Double(xs.format(supplierDebt.getTotal())));
					}
			}
			supplierDebt.setTotal(new Double(xs.format(supplierDebt.getTotal())));
		}
	
		request.setAttribute("supplierDebt", supplierDebt);
//		if(null!=arrearsUse){
//			request.setAttribute("optType", "edit");
//			request.setAttribute("arrearsUse", arrearsUse);
//		}else{
//			request.setAttribute("optType", "add");
//		}
		
		
		return "/finance/importpayment/elementlist";
	}
	
	/**
	 * 欠款手续费数据暂存
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/arrearsUse",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo arrearsUse(HttpServletRequest request,@ModelAttribute ArrearsUse arrearsUse) {
		String message = "";
		boolean success = true;
		try {
			Double total=arrearsUse.getTotal();
			String[] ipeIds=arrearsUse.getImportPackagePaymentElementId().split(",");
			arrearsUseService.deleteByElementId(arrearsUse);
			for (int j = 0; j < ipeIds.length; j++) {
				ImportPackagePaymentElement importPackagePaymentElement=importPackagePaymentElementService.elementData(Integer.parseInt(ipeIds[j]));
				Double shouldPay=importPackagePaymentElement.getShouldPay();
				if(total>shouldPay){
					arrearsUse.setTotal(shouldPay);
					arrearsUse.setImportPackagePaymentElementId(ipeIds[j]);
				}else if(total<=shouldPay){
					arrearsUse.setTotal(total);
					arrearsUse.setImportPackagePaymentElementId(ipeIds[j]);
				}
				total=total-arrearsUse.getTotal();
				arrearsUseService.insertSelective(arrearsUse);
			}
			if(null!=arrearsUse.getCounterFee()&&!"".equals(arrearsUse.getCounterFee())){
				ImportPackagePayment record=new ImportPackagePayment();
				record.setId(arrearsUse.getImportPackagePaymentId());
				record.setCounterFee(arrearsUse.getCounterFee());
				importPackagePaymentService.updateByPrimaryKeySelective(record);
			}
			
		message ="操作成功！";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "操作失败！";
			success = false;
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 欠款手续费数据暂存
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateArrearsUse",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateArrearsUse(HttpServletRequest request,@ModelAttribute ArrearsUse arrearsUse) {
		String message = "";
		boolean success = true;
		try {
			arrearsUseService.updateByPrimaryKey(arrearsUse);
		message ="修改成功！";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "修改失败！";
			success = false;
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 跳转欠款页面
	 * @return
	 */
	@RequestMapping(value="/toDebtList",method=RequestMethod.GET)
	public String toDebtList() {
		return "/finance/supplierdebt/supplierdebtlist";
	}
	
	/**
	 * 欠款数据列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/debtList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo debtList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierDebt> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		supplierDebtService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			double total = 0.0;
			double paid = 0.0;
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierDebt supplierDebt : page.getEntities()) {
				total = total + supplierDebt.getTotal();
				paid = paid + supplierDebt.getPaid();
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierDebt);
				mapList.add(map);
			}
			SupplierDebt supplierDebt = new SupplierDebt();
			supplierDebt.setSupplierCode("总计");
			supplierDebt.setTotal(total);
			supplierDebt.setPaid(paid);
			Map<String, Object> map = EntityUtil.entityToTableMap(supplierDebt);
			mapList.add(map);
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("欠款管理", exportModel, mapList, response);
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
	 * 付款状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getPaymentStatus",method=RequestMethod.POST)
	public @ResponseBody ResultVo getPaymentStatus(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		StringBuffer value = new StringBuffer();
		
		List<SystemCode> paymentStatus = new ArrayList<SystemCode>();
		List<SystemCode> list = systemCodeService.findType("PAYMENT_STATUS");
		Integer id = new Integer(getString(request, "id"));
		ImportPackagePayment importPackagePayment = importPackagePaymentService.selectByPrimaryKey(id);
			
			if (importPackagePayment.getPaymentStatusId()!=null && !"".equals(importPackagePayment.getPaymentStatusId())) {
				for (int i = 0; i < list.size(); i++) {
					if (importPackagePayment.getPaymentStatusId().equals(list.get(i).getId())) {
						paymentStatus.add(list.get(i));
					}
				}
				for (int i = 0; i < list.size(); i++) {
					if (!importPackagePayment.getPaymentStatusId().equals(list.get(i).getId())) {
						paymentStatus.add(list.get(i));
					}
				}
			}else {
				paymentStatus = list;
			}
			
			//拼接数据，页面双击修改使用
			for (int i = 0; i < paymentStatus.size(); i++) {
				value.append(paymentStatus.get(i).getId()).append(":").append(paymentStatus.get(i).getValue()).append(";");
			}
			value.deleteCharAt(value.length()-1);
		
			message = value.toString();
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 修改欠款
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editSupplierDebt",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editSupplierDebt(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		try {
		SupplierDebt supplierDebt = new SupplierDebt();
		supplierDebt.setId(new Integer(getString(request, "id")));
		supplierDebt.setTotal(new Double(getString(request, "total")));
		supplierDebt.setPaid(new Double(getString(request, "paid")));
		supplierDebtService.updateByPrimaryKeySelective(supplierDebt);
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
	 * 跳转付款提醒页面
	 * @return
	 */
	@RequestMapping(value="/toPaymentWarn",method=RequestMethod.GET)
	public String toPaymentWarn() {
		return "/purchase/paymentwarn/paymenWarnList";
	}
	
	/**
	 * 付款提醒页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/paymentWarnList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo paymentWarnList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SearchVo> page1=getPage(request);
		PageModel<SearchVo> page2=getPage(request);
		PageModel<SearchVo> page3=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String paymentType = request.getParameter("paymentType");
		String exportModel = getString(request, "exportModel");
		if ("".equals(where)) {
			where = null;
		}
		int ifOrder = 0;
		if (where != null) {
			String[] condition = where.split(" ");
			for (int i = 0; i < condition.length; i++) {
				if (condition[i].equals("so.order_number")) {
					ifOrder = 1;
				}
				if (condition[i].equals("ip.import_number")) {
					ifOrder = 2;
				}
				if (condition[i].equals("ip.import_date")) {
					ifOrder = 2;
				}
			}
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page1.put("userId", userVo.getUserId());
			page2.put("userId", userVo.getUserId());
			page3.put("userId", userVo.getUserId());
		}
		if (ifOrder==0 || ifOrder==1) {
			if (paymentType == null) {
				importPackagePaymentService.getShouldPrepaymentOrder(page1, where);
				importPackagePaymentService.getShouldShipPaymentOrderPage(page2, where);
			}else {
				if(paymentType.equals("0") || paymentType.equals("1")){
					importPackagePaymentService.getShouldPrepaymentOrder(page1, where);
				}
				if (paymentType.equals("0") || paymentType.equals("2")) {
					importPackagePaymentService.getShouldShipPaymentOrderPage(page2, where);
				}
			}
			
		}
		if (ifOrder==0 || ifOrder==2) {
			if (paymentType == null) {
				importPackagePaymentService.getShouldReceivePaymentOrderPage(page3, where);
			}else if (paymentType.equals("0") || paymentType.equals("3")) {
				importPackagePaymentService.getShouldReceivePaymentOrderPage(page3, where);
			}
		}
		
		
		if (page1.getEntities().size() > 0 || page2.getEntities().size() > 0 || page3.getEntities().size() > 0) {
			jqgrid.setPage(page1.getPageNo());
			jqgrid.setRecords(page1.getRecordCount());
			jqgrid.setTotal(page1.getPageCount());
			double total = 0.0;
			double paid = 0.0;
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SearchVo searchVo : page1.getEntities()) {
				searchVo.setPaymentType("预付");
				Map<String, Object> map = EntityUtil.entityToTableMap(searchVo);
				mapList.add(map);
			}
			for (SearchVo searchVo : page2.getEntities()) {
				searchVo.setPaymentType("发货付");
				Map<String, Object> map = EntityUtil.entityToTableMap(searchVo);
				mapList.add(map);
			}
			for (SearchVo searchVo : page3.getEntities()) {
				searchVo.setPaymentType("验收付");
				Map<String, Object> map = EntityUtil.entityToTableMap(searchVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("付款提醒", exportModel, mapList, response);
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
	 * 付款提醒页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getPaymentWarnCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getPaymentWarnCount(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SearchVo> page1=new PageModel<SearchVo>();
		PageModel<SearchVo> page2=new PageModel<SearchVo>();
		PageModel<SearchVo> page3=new PageModel<SearchVo>();
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		int ifOrder = 0;
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page1.put("userId", userVo.getUserId());
			page2.put("userId", userVo.getUserId());
			page3.put("userId", userVo.getUserId());
		}
		if (ifOrder==0 || ifOrder==1) {
			importPackagePaymentService.getShouldPrepaymentOrder(page1, where);
			importPackagePaymentService.getShouldShipPaymentOrderPage(page2, where);
		}
		if (ifOrder==0 || ifOrder==2) {
			importPackagePaymentService.getShouldReceivePaymentOrderPage(page3, where);
		}
		
		String count = String.valueOf(page1.getRecordCount() + page2.getRecordCount() + page3.getRecordCount());
		return new ResultVo(true, count);
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
