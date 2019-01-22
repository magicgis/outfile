package com.naswork.module.system.controller.businessmanage;

import java.util.ArrayList;
import java.util.Date;
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
import com.naswork.model.SystemCode;
import com.naswork.module.storage.controller.exportpackage.ExportPackageVo;
import com.naswork.service.SystemCodeService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/system/businessmanage")
public class BusinessManageController extends BaseController{
	
	@Resource
	private SystemCodeService systemCodeService;
	
	/*
	 * 机型管理
	 */
	@RequestMapping(value="/toBusinessManage",method=RequestMethod.GET)
	public String toBusinessManage() {
		return "/system/bizmanage/list";
	}
	
	/*
	 * 机型管理页面
	 */
	@RequestMapping(value="/businessManage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo businessManage(HttpServletRequest request) {
		PageModel<SystemCode> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		systemCodeService.businessPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SystemCode systemCode : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(systemCode);
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
		return "/system/bizmanage/addbiz";
	}
	
	/*
	 * 检查代码持否存在
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public @ResponseBody ResultVo check(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		String code = getString(request, "code");
		List<SystemCode> list = systemCodeService.selectByBizCode(code);
		if (list.size()>0) {
			success = true;
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 保存新增机型
	 */
	@RequestMapping(value="/saveBusiness",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveBusiness(HttpServletRequest request,@ModelAttribute SystemCode systemCode) {
		String message = "";
		boolean success = false;
		
		if (systemCode.getCode()!=null) {
			systemCodeService.saveBiz(systemCode);
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
/*	
	 * 修改机型
	 
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit() {
		return "";
	}
*/	
	/*
	 * 保存修改
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo saveEdit(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		
		try {
			SystemCode systemCode = new SystemCode();
			systemCode.setId(new Integer(getString(request, "id")));
			systemCode.setCode(getString(request, "code"));
			systemCode.setValue(getString(request, "value"));
			systemCode.setRemark(getString(request, "remark"));
			systemCode.setUpdateTimestamp(new Date());
			systemCodeService.updateByPrimaryKeySelective(systemCode);
			message ="修改成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "保存失败！";
		}
		
		return new EditRowResultVo(success, message);
	}

}
