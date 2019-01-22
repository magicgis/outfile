package com.naswork.module.xtgl.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.RRoleUser;
import com.naswork.model.Role;
import com.naswork.model.Supplier;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.RoleService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.LigerTreeVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

/**
 * 
 * @author eyaomai
 *
 */
@Controller
@RequestMapping("/xtgl/role")
public class RoleController  extends BaseController {
	@Resource
	private RoleService roleService;

	@RequestMapping(value = "/rolelist",method = RequestMethod.GET)
	public String menulist(){
		return "/xtgl/role/rolelist";
	}
	
	@RequestMapping(value = "/rolelistdata",method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo fullmenu(HttpServletRequest request,
			HttpServletResponse response){
		PageModel<RoleVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = null;
		roleService.findRolePage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (RoleVo roleVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(roleVo);
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

	@RequestMapping(value="/toUpdateRoleMenu", method=RequestMethod.GET)
	public String toUpdateRoleMenu(HttpServletRequest request)
	{
		String roleId = getString(request, "roleId");
		request.setAttribute("roleId",roleId);
		RoleVo role=(RoleVo)this.getDmObj("ROLE", roleId);
		if(null!=role){
		request.setAttribute("roleName", (role.getRoleName()));		
		}
		return "/xtgl/role/rolemenu";
	}
	
	@RequestMapping(value = "/displayRoleMenu",method = RequestMethod.POST)
	public @ResponseBody List<LigerTreeVo> displayRoleMenu(HttpServletRequest request){		
		String roleId = getString(request, "roleId");
		return roleService.searchFullTreeByRole(roleId);
	}
	

	@RequestMapping(value="/updateRoleMenu",  method=RequestMethod.POST)
	public @ResponseBody ResultVo updateRoleMenu(HttpServletRequest request)
	{
		boolean result = true;
		String message = "菜单权限保存成功！";
		UserVo currentUser = getCurrentUser(request);
		String menuIdListStr = this.getString(request, "menuIdList");
		String[] menuIdList = menuIdListStr.split(",");
		String roleId = this.getString(request, "roleId");
		ResultVo resultVo = new ResultVo(result, message);
		roleService.updateRoleMenu(roleId, menuIdList, resultVo);
		return resultVo;
	}	
	
	/**
	 * 添加人员
	 */
	@RequestMapping(value="/toPeople",method=RequestMethod.GET)
	public String toPeople(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		return "/xtgl/role/peoplelist";
	}
	
	/**
	 * 人员列表
	 */
	@RequestMapping(value="/People",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo People(HttpServletRequest request) {
		PageModel<UserVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		page.put("id", getString(request, "id"));
		
		roleService.PeopleList(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (UserVo userVo2 : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(userVo2);
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
	 * 新增用户
	 */
	@RequestMapping(value="/addPeople",method=RequestMethod.GET)
	public String addPeople(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		return "/xtgl/role/addpeople";
	}
	
	/**
	 * 保存用户
	 */
	@RequestMapping(value="/savePeople",method=RequestMethod.POST)
	public @ResponseBody ResultVo savePeople(HttpServletRequest request,@ModelAttribute RRoleUser rRoleUser) {
		String message = "";
		boolean success = false;
		if(rRoleUser.getRoleId()!=null){
			roleService.insertPeople(rRoleUser);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
		
	}
	
	/**
	 * 人员列表
	 */
	@RequestMapping(value="/getPeoples",method=RequestMethod.POST)
	public @ResponseBody ResultVo getPeoples() {
		boolean success = true;
		String message = "";
		List<UserVo> list = roleService.getUsers();
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(list);
		message = jsonArray.toString();
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 删除人员
	 */
	@RequestMapping(value="/deletePeople",method=RequestMethod.POST)
	public @ResponseBody ResultVo deletePeople(HttpServletRequest request,@ModelAttribute RRoleUser rRoleUser) {
		String message = "";
		boolean success = false;
		if(rRoleUser.getRoleId()!=null){
			roleService.deletePeople(rRoleUser);
			message = "删除成功！";
			success = true;
		}else {
			message = "删除失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 新增角色页面
	 * **/
	@RequestMapping(value = "/toAddRole",method = RequestMethod.GET)
	public String toAddRole(){
		return "/xtgl/role/addrole";
	}
	
	/**
	 * 新增角色
	 */
	@RequestMapping(value="/addRole",method=RequestMethod.POST)
	public @ResponseBody ResultVo addRole(HttpServletRequest request,@ModelAttribute Role role) {
		String message = "";
		boolean success = false;
		if(role.getRoleName()!=null){
			int maxRoleId=roleService.selectMaxRoleId();
			role.setRoleId(maxRoleId+1);
			roleService.insertRole(role);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
}
