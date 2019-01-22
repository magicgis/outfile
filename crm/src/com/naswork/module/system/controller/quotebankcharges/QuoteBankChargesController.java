package com.naswork.module.system.controller.quotebankcharges;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.OrderBankCharges;
import com.naswork.model.QuoteBankCharges;
import com.naswork.model.SystemCode;
import com.naswork.service.QuoteBankChargesService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/system/quoteBankCharges")
public class QuoteBankChargesController extends BaseController{
	
	@Resource
	private QuoteBankChargesService quoteBankChargesdService;
	/*
	 * 银行费用管理
	 */
	@RequestMapping(value="/toQuoteBankCharges",method=RequestMethod.GET)
	public String toQuoteBankCharges() {
		return "/system/quotebankcharges/list";
	}
	
	/*
	 * 银行费用管理页面
	 */
	@RequestMapping(value="/quoteBankChargesManage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo quoteBankChargesManage(HttpServletRequest request) {
		PageModel<QuoteBankCharges> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		quoteBankChargesdService.quoteBankChargesPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (QuoteBankCharges quoteBankCharges : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(quoteBankCharges);
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
	 * 新增银行费用
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd() {
		return "/system/quotebankcharges/addquotebankcharges";
	}
	
	/*
	 * 检查代码持否存在
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public @ResponseBody ResultVo check(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		String clientId = getString(request, "clientId");
		QuoteBankCharges bankCharges = quoteBankChargesdService.findByClientId(Integer.parseInt(clientId));
		if (null!=bankCharges) {
			success = true;
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 保存新增银行费用
	 */
	@RequestMapping(value="/savequoteBankCharges",method=RequestMethod.POST)
	public @ResponseBody ResultVo savequoteBankCharges(HttpServletRequest request,@ModelAttribute QuoteBankCharges record) {
		String message = "";
		boolean success = false;
		
		if (record.getClientId()!=null) {
			quoteBankChargesdService.insert(record);
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	

	/**
	 * 修改
	 * **/
	@RequestMapping(value="/update",  method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updatesupplierquoteelement(HttpServletRequest request,
			@ModelAttribute QuoteBankCharges record)
	{
		boolean success = true;
		String message = "更新完成！";
		try{
			quoteBankChargesdService.updateByPrimaryKey(record);
	
		}catch(Exception e){
		e.printStackTrace();
		success = false;
		message = "更新失败";
		}
		EditRowResultVo result = new EditRowResultVo(success, message);
		return result;
	}

}
