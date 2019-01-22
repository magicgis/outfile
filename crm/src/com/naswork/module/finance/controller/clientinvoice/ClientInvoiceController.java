package com.naswork.module.finance.controller.clientinvoice;

import java.math.BigDecimal;
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
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.model.ClientOrder;
import com.naswork.model.ExportPackageInstructions;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.service.ClientInvoiceElementService;
import com.naswork.service.ClientInvoiceService;
import com.naswork.service.ClientOrderElementService;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.ImportPackagePaymentElementService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;
@Controller
@RequestMapping("/finance/invoice")
public class ClientInvoiceController extends BaseController {

	@Resource
	private ClientInvoiceService clientinvoIceService;
	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private ClientOrderElementService clientOrderElementService;
	@Resource
	private ClientInvoiceElementService clientInvoiceElementService;
	@Resource
	private ExportPackageElementService exportPackageElementService;
	@Resource
	private UserService userService;
	@Resource
	private ImportPackagePaymentElementService importPackagePaymentElementService;
	
	/**
	 * 新增发票页面
	 * **/
	@RequestMapping(value="/toAddInvoice",method=RequestMethod.GET)
	private String toAddInvoice(HttpServletRequest request,HttpServletResponse response){
		String type=request.getParameter("type");
		String id=request.getParameter("id");
		ClientOrder clientOrder=new ClientOrder();
		ClientInvoice clientInvoice =new ClientInvoice();
		if(type.equals("2")){
			String[] ids=id.split(",");
			id=ids[1];
			clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(id));
			List<ExportPackageElementVo>  epelist=exportPackageElementService.findByEpidAndCoid(ids[0], ids[1]);
			ExportPackageElementVo exportPackageElementVo=exportPackageElementService.findEpeAmount(Integer.parseInt(ids[0]),Integer.parseInt( ids[1]));
			 clientInvoice.setInvoiceType(2);
			 String epe=exportPackageElementVo.getRemark().split("/")[0];
			 String coe=exportPackageElementVo.getRemark().split("/")[1];
			 if(coe.equals(epe)){
				clientOrder.setPrepayRate((1-clientOrder.getPrepayRate())*100);
			 }else{
				 clientOrder.setPrepayRate((clientOrder.getShipPayRate())*100);
			 }
				request.setAttribute("exportPackageId", ids[0]);
			}
		else if(type.equals("0")){
			clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(id));
			clientOrder.setPrepayRate(100.00);
			clientInvoice.setInvoiceType(0);
		}else if(type.equals("1")){
			clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(id));
			clientInvoice.setInvoiceType(1);
			clientOrder.setPrepayRate(clientOrder.getPrepayRate()*100);
		}
		clientInvoice.setInvoiceDate(new Date());
		request.setAttribute("clientInvoice", clientInvoice);
		request.setAttribute("clientOrder", clientOrder);
		return "/finance/invoice/addinvoice";
	}
	
	/**
	 * 新增发票
	 * @throws Exception 
	 * **/
	@RequestMapping("/addInvoice")
	private @ResponseBody ResultVo addInvoice(HttpServletRequest request,HttpServletResponse response,@ModelAttribute ClientInvoice record) throws Exception{
		boolean result=false;
		String message="";
		try {
			record.setExportPackageId(request.getParameter("exportPackageId"));
			if(record.getInvoiceType().equals(0)||record.getInvoiceType().equals(2)){
			clientinvoIceService.autoinsert(record);
			}else{
			clientinvoIceService.insert(record);
			}
			result=true;
			message="新增成功";
		} catch (Exception e) {
			message="新增失败";
		}
		
		return new ResultVo(result, message);
	}
	
	/**
	 * 修改发票页面
	 * **/
	@RequestMapping(value="/toUpdateInvoice",method=RequestMethod.GET)
	private String toUpdateInvoice(HttpServletRequest request,HttpServletResponse response){
		ListDataVo clientInvoice=clientinvoIceService.findById(request.getParameter("id"));
		ClientOrder clientOrder=new ClientOrder();
		clientOrder.setPrepayRate(clientInvoice.getInvoiceTerms().doubleValue());
		clientOrder.setOrderNumber(clientInvoice.getOrderNumber());
		clientOrder.setId(clientInvoice.getClientOrderId());
		request.setAttribute("clientOrder", clientOrder);
		request.setAttribute("clientInvoice", clientInvoice);
		return "/finance/invoice/addinvoice";
	}
	
	/**
	 * 修改发票
	 * **/
	@RequestMapping("/updateInvoice")
	private @ResponseBody ResultVo updateInvoice(HttpServletRequest request,HttpServletResponse response,@ModelAttribute ClientInvoice record){
		boolean result=false;
		String message="";
		try {
			clientinvoIceService.updateByPrimaryKey(record);
			result=true;
			message="修改成功";
		} catch (Exception e) {
			message="修改失败";
		}
		return new ResultVo(result, message);
	}
	
	
	/**
	 * 新增发票明细页面
	 * **/
	@RequestMapping(value="/toAddInvoiceElement",method=RequestMethod.GET)
	private String toAddInvoiceElement(HttpServletRequest request,HttpServletResponse response){
		ListDataVo clientInvoice=clientinvoIceService.findById(request.getParameter("id"));
		ClientOrderElementVo clientOrder=clientOrderElementService.findByclientOrderELementId(Integer.parseInt(request.getParameter("clientOrderElementId")));
		request.setAttribute("clientOrder", clientOrder);
		request.setAttribute("clientInvoice", clientInvoice);
		return "/finance/invoice/addinvoiceelement";
	}
	
	/**
	 * 新增发票明细
	 * **/
	@RequestMapping("/addInvoiceElement")
	private @ResponseBody ResultVo addInvoiceElement(HttpServletRequest request,HttpServletResponse response,@ModelAttribute ClientInvoiceElement record){
		boolean result=false;
		String message="";
		try {
			clientInvoiceElementService.insert(record);
			result=true;
			message="新增成功";
		} catch (Exception e) {
			message="新增失败";
		}
		
		return new ResultVo(result, message);
	}
	
	/**
	 * 修改发票明细页面
	 * **/
	@RequestMapping(value="/toUpdateInvoiceElement",method=RequestMethod.GET)
	private String toUpdateInvoiceElement(HttpServletRequest request,HttpServletResponse response){
		ListDataVo clientInvoice=clientinvoIceService.findById(request.getParameter("id"));
		ClientInvoiceElement clientInvoiceElement=clientInvoiceElementService.selectByPrimaryKey(Integer.parseInt(request.getParameter("clientInvoiceElementId")));
		ClientOrderElementVo clientOrder=clientOrderElementService.findByclientOrderELementId(Integer.parseInt(request.getParameter("clientOrderElementId")));
		clientInvoice.setInvoiceTerms(clientInvoiceElement.getTerms());
		request.setAttribute("clientInvoiceElement", clientInvoiceElement);
		request.setAttribute("clientOrder", clientOrder);
		request.setAttribute("clientInvoice", clientInvoice);
		return "/finance/invoice/addinvoiceelement";
	}
	
	/**
	 * 修改发票明细
	 * **/
	@RequestMapping("/updateInvoiceElement")
	private @ResponseBody ResultVo updateInvoiceElement(HttpServletRequest request,HttpServletResponse response,@ModelAttribute ClientInvoiceElement record){
		boolean result=false;
		String message="";
		try {
			clientInvoiceElementService.updateByPrimaryKey(record);
			result=true;
			message="修改成功";
		} catch (Exception e) {
			message="修改失败";
		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 形式发票单列表页面
	 * **/
	@RequestMapping("/proformainvoicelist")
	private String proformainvoicelist(){
		return "/finance/invoice/proformainvoicelist";
	}
	
	/**
	 * 形式发票列表数据分页
	 * **/
	@RequestMapping(value="/proformaInvoiceListData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo proformaInvoiceListData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<ListDataVo> page = getPage(request);
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
				searchString="inv.invoice_type =0 and ar.USER_ID ="+userId+" and "+searchString;
			}
			else{
				searchString=" inv.invoice_type =0 and ar.USER_ID ="+userId;
			}
		}else{
			if(null!=searchString&&!searchString.equals("")){
				searchString="inv.invoice_type =0"+" and "+searchString;
			}
			else{
				searchString=" inv.invoice_type =0";
			}
		}
		
		clientinvoIceService.listDataPage(page, searchString, getSort(request));
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ListDataVo listDataVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(listDataVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("clientinvoice", exportModel, mapList, response);
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
	 * 发货发票单列表页面
	 * **/
	@RequestMapping("/receivablesinvoicelist")
	private String receivablesinvoicelist(){
		return "/finance/invoice/receivablesinvoicelist";
	}
	
	/**
	 * 发货发票列表数据分页
	 * **/
	@RequestMapping(value="/receivablesInvoiceListData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo receivablesInvoiceListData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<ListDataVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		String reqexportPackageInstructionsNumber=request.getParameter("exportPackageInstructionsNumber");
		String reqexportNumber=request.getParameter("exportNumber");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		ListDataVo vo=new ListDataVo();
		boolean in =false;
		 if(null!=reqexportPackageInstructionsNumber&&!"".equals(reqexportPackageInstructionsNumber)){
			 vo.setExportPackageInstructionsNumber(reqexportPackageInstructionsNumber);
			 in=true;
		 }
		 if( null!=reqexportNumber&&!"".equals(reqexportNumber)){
			 vo.setExportNumber(reqexportNumber);
			 in=true;
		 }
		 
		 String clientInvoiceId="";
		 if(in){
		 List<ClientInvoice> clientInvoices=clientinvoIceService.findByexportMunber(vo);
			 if(clientInvoices.size()>0){
			  clientInvoiceId=clientInvoices.get(0).getId().toString();
					 for (ClientInvoice clientInvoice : clientInvoices) {
						 if(clientInvoiceId.indexOf(clientInvoice.getId().toString())<0){
						 clientInvoiceId+=","+clientInvoice.getId();
						 }
					}
			 }
		 }
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
			if(null!=searchString&&!searchString.equals("")){
				searchString="inv.invoice_type !=0 and  ar.USER_ID ="+userId+" and "+searchString;
			}
			else{
				searchString="inv.invoice_type !=0 and ar.USER_ID ="+userId;
			}
		}else{
			if(null!=searchString&&!searchString.equals("")){
				searchString="inv.invoice_type !=0"+" and "+searchString;
			}
			else{
				searchString=" inv.invoice_type !=0";
			}
		}
		if(!"".equals(clientInvoiceId)){
			page.put("clientInvoiceId", clientInvoiceId);
		}
		clientinvoIceService.listDataPage(page, searchString, getSort(request));
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ListDataVo listDataVo : page.getEntities()) {
				listDataVo.setUncollected(listDataVo.getClientInvoicePrice()-listDataVo.getTotalPrice());
				if(null!=listDataVo.getTotalPrice()&&null!=listDataVo.getClientInvoicePrice()){
					if(listDataVo.getTotalPrice().equals(listDataVo.getClientInvoicePrice())&&listDataVo.getTotalPrice()>0&&listDataVo.getClientInvoicePrice()>0){
					listDataVo.setInvoiceStatusId(1);
					}
				}
				
				 List<ListDataVo> list=	clientinvoIceService.findExportNumber(listDataVo.getId());
				 String exportNumber="";
				 String exportPackageInstructionsNumber="";
				 for (ListDataVo listDataVo2 : list) {
					 int a=exportNumber.indexOf(listDataVo2.getExportNumber());
					 if(null!=listDataVo2.getExportNumber()){
						if(exportNumber.indexOf(listDataVo2.getExportNumber())!=-1){
							continue;
						}else{
							exportNumber+=listDataVo2.getExportNumber()+";";
						}
					 }
					
					 if(null!=listDataVo2.getExportPackageInstructionsNumber()){
						if(exportPackageInstructionsNumber.indexOf(listDataVo2.getExportPackageInstructionsNumber())!=-1){
							continue;
						}else{
							exportPackageInstructionsNumber+=listDataVo2.getExportPackageInstructionsNumber()+";";
						}
					 }
				}
				 listDataVo.setExportNumber(exportNumber);
				 listDataVo.setExportPackageInstructionsNumber(exportPackageInstructionsNumber);
				 
				Map<String, Object> map = EntityUtil.entityToTableMap(listDataVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("clientinvoice", exportModel, mapList, response);
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
	 * 修改发票
	 * **/
	@RequestMapping("/updateInvoiceStatus")
	private @ResponseBody ResultVo updateInvoiceStatus(HttpServletRequest request,HttpServletResponse response,@ModelAttribute ClientInvoice record){
		boolean result=false;
		String message="";
//		try {
			record.setInvoiceStatusId(1);
			clientinvoIceService.updateByPrimaryKeySelective(record);
			result=true;
			message="修改成功";
//		} catch (Exception e) {
//			message="修改失败";
//		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 删除发票
	 * **/
	@RequestMapping("/deleteInvoice")
	private @ResponseBody ResultVo deleteInvoice(HttpServletRequest request,HttpServletResponse response,@ModelAttribute ClientInvoice record){
		boolean result=false;
		String message="";
//		try {
			clientinvoIceService.deleteByPrimaryKey(record.getId());
			result=true;
			message="删除成功";
//		} catch (Exception e) {
//			message="修改失败";
//		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 订单页面发票列表数据
	 * **/
	@RequestMapping(value="/orderInvoicelistData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo orderInvoicelistData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<ListDataVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		page.put("orderNumber", request.getParameter("orderNumber"));
		clientinvoIceService.findByOrderNumber(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ListDataVo listDataVo : page.getEntities()) {
				listDataVo.setUncollected(listDataVo.getClientInvoicePrice()-listDataVo.getTotalPrice());
				if(null!=listDataVo.getTotalPrice()&&null!=listDataVo.getClientInvoicePrice()){
					if(listDataVo.getTotalPrice().equals(listDataVo.getClientInvoicePrice())&&listDataVo.getTotalPrice()>0&&listDataVo.getClientInvoicePrice()>0){
					listDataVo.setInvoiceStatusId(1);
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(listDataVo);
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
	
	/***
	 * 发票单明细页面
	 * */
	@RequestMapping("/invoiceElementList")
	private String invoiceElementList(HttpServletRequest request){
		ListDataVo clientInvoice=clientinvoIceService.findById(request.getParameter("id"));
		request.setAttribute("clientInvoice", clientInvoice);
		return "/finance/invoice/elementlist";
	}
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/elementListData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementListData(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute SupplierQuoteVo supplierQuotevo){
		PageModel<ElementDataVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString =request.getParameter("searchString");
		page.put("clientInvoiceId", request.getParameter("id"));
		page.put("clientOrderId",request.getParameter("clientOrderId"));
		clientInvoiceElementService.elementDataPage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ElementDataVo listDataVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(listDataVo);
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
	 * 批量新增发票明细页面
	 * **/
	@RequestMapping(value="/toBatchAddInvoiceElement",method=RequestMethod.GET)
	private String toBatchAddInvoiceElement(HttpServletRequest request,HttpServletResponse response){
		String clientOrderId=request.getParameter("clientOrderId");
		String clientInvoiceId=request.getParameter("id");
		 List<ElementDataVo> list=clientInvoiceElementService.findByCoidAndCiid(clientOrderId, clientInvoiceId);
		 String invoiceTerms=request.getParameter("invoiceTerms");
		 for (ElementDataVo elementDataVo : list) {
			 elementDataVo.setInvoiceTerms(Integer.parseInt(invoiceTerms));
		}
		request.setAttribute("list", list);
		return "/finance/invoice/addElementTable";
	}
	
	/**
	 * 新增发票明细
	 * **/
	@RequestMapping(value="/batchAddInvoiceElement",method=RequestMethod.POST)
	private @ResponseBody ResultVo batchAddInvoiceElement(HttpServletRequest request,HttpServletResponse response,@ModelAttribute ClientInvoiceElement record){
		boolean result=false;
		String message="";
		try {
			for (ClientInvoiceElement clientInvoiceElement : record.getList()) {
				
				if(!clientInvoiceElement.getDataItem().equals("check")){
					ClientInvoiceElement data=clientInvoiceElementService.selectByCoeIdAndCiId(clientInvoiceElement);
					if(null==data){
					clientInvoiceElementService.insert(clientInvoiceElement);
					}
				}
				
			}
			result=true;
			message="修改成功";
		} catch (Exception e) {
			message="修改失败";
		}
		return new ResultVo(result, message);
	}
	
	/**
	 * 验证code
	 */
	@RequestMapping(value="/testCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo testCode(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String code = getString(request, "code");
		ClientInvoice invoice=clientinvoIceService.selectByCode(code);
		if (invoice!=null) {
			success = true;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 新增发货发票页面
	 * **/
	@RequestMapping(value="/toAddExportInvoice",method=RequestMethod.GET)
	private String toAddExportInvoice(HttpServletRequest request,HttpServletResponse response){
		String type=request.getParameter("type");
		String id=request.getParameter("id");
		ClientOrder clientOrder=new ClientOrder();
		ClientInvoice clientInvoice =new ClientInvoice();
		if(type.equals("2")){
			clientOrder=clientOrderService.selectByPrimaryKey(Integer.parseInt(id));
			 clientInvoice.setInvoiceType(2);
				 clientOrder.setPrepayRate((clientOrder.getShipPayRate())*100);
			}
	
		clientInvoice.setInvoiceDate(new Date());
		request.setAttribute("clientInvoice", clientInvoice);
		request.setAttribute("clientOrder", clientOrder);
		return "/finance/invoice/addinvoice";
	}
	
	/**
	 * 新增发票
	 * @throws Exception 
	 * **/
	@RequestMapping("/addExportInvoice")
	private @ResponseBody ResultVo addExportInvoice(HttpServletRequest request,HttpServletResponse response,@ModelAttribute ClientInvoice record) throws Exception{
		boolean result=false;
		String message="";
		try {
		
			clientinvoIceService.insert(record);
			
			result=true;
			message="新增成功";
		} catch (Exception e) {
			message="新增失败";
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
}
