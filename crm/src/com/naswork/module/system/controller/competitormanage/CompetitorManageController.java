package com.naswork.module.system.controller.competitormanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.Client;
import com.naswork.model.Competitor;
import com.naswork.model.CompetitorSupplier;
import com.naswork.model.Supplier;
import com.naswork.model.SystemCode;
import com.naswork.module.storage.controller.exportpackage.ExportPackageVo;
import com.naswork.service.CompetitorService;
import com.naswork.service.SupplierInquiryService;
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
@RequestMapping("/system/competitormanage")
public class CompetitorManageController extends BaseController{
	
	@Resource
	private CompetitorService competitorService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private UserService userService;
	@Resource
	private SupplierInquiryService supplierInquiryService;
	
	/*
	 * 机型管理
	 */
	@RequestMapping(value="/toCompetitorManage",method=RequestMethod.GET)
	public String toCompetitorManage() {
		return "/system/competitormanage/list";
	}
	
	/*
	 * 机型管理页面
	 */
	@RequestMapping(value="/competitorManage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo competitorManage(HttpServletRequest request) {
		PageModel<Competitor> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		competitorService.competitorPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Competitor competitor : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(competitor);
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
	 * 新增机型
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd() {
		return "/system/competitormanage/addcompetitor";
	}
	
	/*
	 * 检查代码持否存在
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public @ResponseBody ResultVo check(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		String code = getString(request, "code");
		List<Competitor> list = competitorService.selectByCode(code);
		if (list.size()>0) {
			success = true;
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 保存新增机型
	 */
	@RequestMapping(value="/saveCompetitor",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveCompetitor(HttpServletRequest request,@ModelAttribute Competitor competitor) {
		String message = "";
		boolean success = false;
		
		if (competitor.getCode()!=null) {
			competitor.setUpdateTimestamp(new Date());
			competitor.setId(competitorService.getMaxId()+1);
			competitorService.insertSelective(competitor);
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	/*		
	 * 修改机型
	 */
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		Competitor competitor = competitorService.selectByPrimaryKey(id);
		request.setAttribute("competitor", competitor);
		return "/system/competitormanage/editcompetitor";
	}
	
	/*
	 * 货币
	 */
	@RequestMapping(value="/currency",method=RequestMethod.POST)
	public @ResponseBody ResultVo currency(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit.equals(1)) {
			Integer id =new Integer(getString(request, "id"));
			Competitor competitor = competitorService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.findCurrency();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(competitor.getCurrencyId())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(competitor.getCurrencyId())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.findCurrency();
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*
	 * 保存修改
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute Competitor competitor) {
		String message = "";
		boolean success = false;
		
		if (competitor.getId()!=null) {
			competitor.setUpdateTimestamp(new Date());
			competitorService.updateByPrimaryKeySelective(competitor);
			success = true;
			message = "修改成功！";
		}else {
			message = "修改失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 跳转绑定供应商与竞争对手页面
	 * @return
	 */
	@RequestMapping(value="/toMapSupplierAndCompetitorList",method=RequestMethod.GET)
	public String toMapSupplierAndCompetitorList(HttpServletRequest request){
		request.setAttribute("competitorId", getString(request, "id"));
		return "/system/competitormanage/supplierAndCompetitorMapperlist";
	}
	
	/**
	 * 绑定供应商与竞争对手列表数据
	 */
	@RequestMapping(value="/mapSupplierAndCompetitorList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo mapSupplierAndCompetitorList(HttpServletRequest request) {
		PageModel<CompetitorSupplier> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo = getCurrentUser(request);
		String competitorId = getString(request, "id");
		page.put("competitorId", competitorId);
		
		competitorService.getMapSupplierAndCompetitorList(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (CompetitorSupplier competitorSupplier : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(competitorSupplier);
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
	 * 跳转绑定供应商与竞争对手页面
	 * @return
	 */
	@RequestMapping(value="/toAddMapSupplierAndCompetitor",method=RequestMethod.GET)
	public String toAddMapSupplierAndCompetitor(HttpServletRequest request){
		request.setAttribute("competitorId", getString(request, "id"));
		return "/system/competitormanage/addMapSupplierAndCompetitor";
	}
	
	/**
	 * 保存供应商与竞争对手的绑定
	 * @param request
	 * @param competitor
	 * @return
	 */
	@RequestMapping(value="/saveSupplierAndCompetitorMapper",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveSupplierAndCompetitorMapper(HttpServletRequest request,@ModelAttribute Competitor competitor){
		String message = "";
		boolean success = false;
		try {
			if (competitor.getId() != null) {
				competitorService.addMapSupplierAndCompetitor(competitor);
				message = "新增成功！";
				success = true;
			}else {
				message = "新增失败！";
				success = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "新增异常！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 保存供应商与竞争对手的绑定
	 * @param request
	 * @param competitor
	 * @return
	 */
	@RequestMapping(value="/delSupplierAndCompetitorMapper",method=RequestMethod.POST)
	public @ResponseBody ResultVo delSupplierAndCompetitorMapper(HttpServletRequest request){
		String message = "";
		boolean success = false;
		try {
			Integer id = new Integer(getString(request, "id"));
			competitorService.delMapSupplierAndCompetitor(id);
			message = "删除成功！";
			success = true;	
		} catch (Exception e) {
			e.printStackTrace();
			message = "删除异常！";
			success = false;
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 供应商
	 */
	@RequestMapping(value="/getSuppliers",method=RequestMethod.POST)
	public @ResponseBody ResultVo supplierList(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		PageModel<Supplier> page = getPage(request);
		List<Supplier> list = supplierInquiryService.getSupplier(page);
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(list);
		message = jsonArray.toString();
		
		return new ResultVo(success, message);
	}

}
