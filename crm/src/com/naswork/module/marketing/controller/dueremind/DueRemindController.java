package com.naswork.module.marketing.controller.dueremind;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientInquiry;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.module.marketing.controller.clientorder.ClientOrderVo;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteElementVo;
import com.naswork.module.purchase.controller.supplierorder.AddSupplierOrderElementVo;
import com.naswork.module.purchase.controller.supplierorder.SupplierOrderManageVo;
import com.naswork.service.ClientOrderService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.DueRemindService;
import com.naswork.service.SupplierOrderService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/dueremind")
public class DueRemindController extends BaseController{
	@Resource
	private DueRemindService dueRemindService;
	@Resource
	private ClientOrderService clientOrderService;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private SupplierOrderService supplierOrderService;
	@Resource
	private UserService userService;
	
	/**
	 * 询价到期提醒页面
	 * **/
	@RequestMapping(value="/viewListByInquiry",method=RequestMethod.GET)
	public String viewListByInquiry(HttpServletRequest request){
		request.setAttribute("type", request.getParameter("type"));
		return "/marketing/dueremind/dueremindinquirylist";
	}
	
	/**
	 * 询价到期提醒列表数据
	 */
	@RequestMapping(value="/listDataByInquiry",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listDataByInquiry(HttpServletRequest request,HttpServletResponse response) {
		PageModel<DueRemindListVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String warningDate = request.getParameter("warningDate");
		String  searchString = request.getParameter("searchString");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
		}
		String type = request.getParameter("type");
		page.put("type", type);
		if(null!=warningDate&&!warningDate.equals("")){
		page.put("warningDate", warningDate);
		}
		dueRemindService.findDueRemind(page, searchString, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (DueRemindListVo dueRemindListVo : page.getEntities()) {
				if(dueRemindListVo.getInquiryStatusId()==35){
					int icount=dueRemindService.findInquiryCount(dueRemindListVo.getId());
					int qcount=dueRemindService.findQuoteCount(dueRemindListVo.getId());
//					int p=qcount/icount;
					BigDecimal i=new BigDecimal(icount);
					BigDecimal q=new BigDecimal(qcount);
					BigDecimal p=q.divide(i,10,BigDecimal.ROUND_HALF_DOWN);
					BigDecimal p2=p.multiply(new BigDecimal(100));
					String proportion=p2.setScale(0, BigDecimal.ROUND_HALF_UP)+"%"+"("+qcount+"/"+icount+")";
					dueRemindListVo.setProportion(proportion);
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(dueRemindListVo);
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
	 * 询价到期提醒列表数据
	 */
	@RequestMapping(value="/getInquiryWarnCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getInquiryWarnCount(HttpServletRequest request,HttpServletResponse response) {
		PageModel<DueRemindListVo> page=new PageModel<DueRemindListVo>();
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String  searchString = request.getParameter("searchString");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
		}
		String type = request.getParameter("type");
		String warningDate = request.getParameter("warningDate");
		page.put("type", type);
		if(null!=warningDate&&!warningDate.equals("")){
			page.put("warningDate", warningDate);
		}
		dueRemindService.findDueRemind(page, searchString, sort);
		String count = String.valueOf(page.getRecordCount());
		return new ResultVo(true, count);
	}
	
	
	/**
	 * 超时拒报
	 * **/
	@RequestMapping(value="/refuse",method=RequestMethod.POST)
	public @ResponseBody ResultVo refuse(HttpServletRequest request,@ModelAttribute ClientInquiry clientInquiry) {
		String id=getString(request, "id");
		clientInquiry.setId(Integer.parseInt(id));
		clientInquiry.setInquiryStatusId(32);
		boolean success = false;
		String msg = "";
		if (id!=null) {
			dueRemindService.refuse(clientInquiry);	
			success=true;
			msg="更新成功！";
		}else {
			msg="更新失败！";
		}
		return new ResultVo(success, msg);
	}
	
	/**
	 * 客户订单到期提醒
	 * **/
	@RequestMapping(value="/viewListByClientOrder",method=RequestMethod.GET)
	public String viewListByClientOrder(HttpServletRequest request){
		return "/marketing/dueremind/dueremindclientorderlist";
	}
	
	/**
	 * 列表页面数据
	 */
	@RequestMapping(value="/listDataByClientOrder",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listDataByClientOrder(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String  searchString = request.getParameter("searchString");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			page.put("userId", user.getUserId());
		}
		clientOrderService.dueReminlistPage(page, searchString, sort);
		
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientOrderVo clientOrderVo : page.getEntities()) {
				
				
				
				Map<String, Object> map = EntityUtil.entityToTableMap(clientOrderVo);
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
	 * 列表页面数据
	 */
	@RequestMapping(value="/getOrderWarnCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getOrderWarnCount(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientOrderVo> page=new PageModel<ClientOrderVo>();
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String  searchString = request.getParameter("searchString");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
		}
		clientOrderService.dueReminlistPage(page, searchString, sort);
		String count = String.valueOf(page.getRecordCount());
		return new ResultVo(true, count);
	}
	
	/**
	 * 供应商订单到期提醒
	 * **/
	@RequestMapping(value="/viewListBySupplierOrder",method=RequestMethod.GET)
	public String viewListBySupplierOrder(HttpServletRequest request){
		return "/marketing/dueremind/dueremindsupplierorderlist";
	}
	
	
	/**
	 * 供应商订单到期提醒列表页面数据
	 */
	@RequestMapping(value="/listDataBySupplierOrder",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listDataBySupplierOrder(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierOrderManageVo> page=getPage(request);
		PageModel<AddSupplierOrderElementVo> page2=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String  searchString = request.getParameter("searchString");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
		}
		supplierOrderService.dueRemindSupplierOrderManagePage(page, searchString, sort);
		
//		supplierOrderService.SupplierOrderElement(page2);
//		int a=0;
//		for (int i = 0; i < page.getEntities().size(); i++) {
//			page2.put("id", page.getEntities().get(i).getId());
//			supplierOrderService.SupplierOrderElement(page2);
//			for (int j = 0; j < page2.getEntities().size(); j++) {
//				if(page2.getEntities().get(j).getSupplierOrderAmount().equals(page2.getEntities().get(j).getImportAmount())
//						){
//					a++;
//				}
//			}
//			if(a==page2.getEntities().size()){
//				page.getEntities().remove(i);
//			}
//		}
		
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierOrderManageVo supplierOrderManageVo : page.getEntities()) {
//				clientOrderVo.setPrepayRate(clientOrderVo.getPrepayRate()*100);
//				clientOrderVo.setReceivePayRate(clientOrderVo.getReceivePayRate()*100);
//				clientOrderVo.setShipPayRate(clientOrderVo.getShipPayRate()*100);
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierOrderManageVo);
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
	 * 列表页面数据
	 */
	@RequestMapping(value="/getWarnSupplierOrderCount",method=RequestMethod.POST)
	public @ResponseBody ResultVo getWarnSupplierOrderCount(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SupplierOrderManageVo> page=new PageModel<SupplierOrderManageVo>();
		PageModel<AddSupplierOrderElementVo> page2=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		GridSort sort = getSort(request);
		String  searchString = request.getParameter("searchString");
		UserVo user = ContextHolder.getCurrentUser();
		String userId="";
		if(user!=null){
			userId= user.getUserId();
		}
		
		RoleVo roleVo = userService.getPower(new Integer(user.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", user.getUserId());
		}
		supplierOrderService.dueRemindSupplierOrderManagePage(page, searchString, sort);
		String count = String.valueOf(page.getRecordCount());
		return new ResultVo(true, count);
	}
}
