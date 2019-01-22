package com.naswork.module.xtgl.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.naswork.model.Menu;
import com.naswork.model.RRoleUser;
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
@RequestMapping("/xtgl/user")
public class UserController extends BaseController{
	@Resource
	private UserService userService;
	
	@RequestMapping(value = "/userlist",method = RequestMethod.GET)
	public String userListPage(HttpServletRequest request){
		request.setAttribute("type", request.getParameter("type"));
		return "/xtgl/user/userList";
	}
	
	@RequestMapping(value="/toSaveUser", method=RequestMethod.GET)
	public String toSaveUser(HttpServletRequest request)
	{
		String userId = getString(request, "userId");
		List<RoleVo> allRoles = (List<RoleVo>) this.getDmObj("ALL_ROLE", "allRoles");
		List<String> unRoleIdList = new ArrayList<String>();
		Map<String, String> roleMap = new HashMap<String, String>();
		for(RoleVo role: allRoles){
			roleMap.put(role.getRoleId(), role.getRoleName());
			if(userId.equals("")){
				unRoleIdList.add(role.getRoleId());
			}
		}
		request.setAttribute("allRoleMap", roleMap);
		UserVo user =null;
		if(userId.equals("")){
			user = new UserVo();
			user.setUnRoleIdList(unRoleIdList);
			user.setRoleIdList(new ArrayList<String>());
		}
		else{
			
			user = userService.findUserByUserId(userId, allRoles);
		}

		request.setAttribute("xtglUserVo", user);
		return "/xtgl/user/editUser";
	}
	@RequestMapping(value="/saveUser",  method=RequestMethod.POST)
	public @ResponseBody ResultVo saveUser(HttpServletRequest request, @ModelAttribute UserVo userVo)
	{
		boolean result = true;
		String message = "用户保存成功！";
		UserVo currentUser = getCurrentUser(request);
		String roleListStr = this.getString(request, "roleList");
		String[] roleIdList = roleListStr.split(",");
		if(userVo.getUserId() ==null ||userVo.getUserId().equals("")){
			userService.add(userVo,roleIdList);
		}
		else{
			userService.modify(userVo,roleIdList);
		}
		return new ResultVo(result, message);
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public @ResponseBody ResultVo deleteUser(HttpServletRequest request) 
	{
		boolean success = false;
		String message = "";
		String userId = request.getParameter("userId");
		try
		{
			userService.remove(userId);
			success = true;
			message = "删除用户成功！";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			message = "删除失败！";
		}
		return new ResultVo(success, message); 
	}
	
	
	@RequestMapping(value = "/userdatalist",method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo queryUserDataList(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<UserVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo currentUser = getCurrentUser(request);
		String type=request.getParameter("type");
		String searchString = null;
		if(type.equals("personal")){
		 searchString = "USER_ID="+currentUser.getUserId();
		}
		List<RoleVo> allRoles = (List<RoleVo>) this.getDmObj("ALL_ROLE", "allRoles");
		userService.findPage(page, searchString, getSort(request), allRoles);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (UserVo UserVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(UserVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		
		// 导出
		if (StringUtils.isNotEmpty(request.getParameter("exportModel"))) {
			try {
				exportService.exportGridToXlsx("菜单列表",
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
	 * 增加用户管理
	 */
	@RequestMapping(value="/toAddUser",method=RequestMethod.GET)
	public String toAddUser() {
		return "/xtgl/user/addUser";
	}
	
	/**
	 * 保存用户
	 */
	@RequestMapping(value="/saveAddUser",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAddUser(HttpServletRequest request,@ModelAttribute UserVo userVo) {
		String message = "";
		boolean success = false;
		
		if (userVo.getUserName()!=null) {
			userService.insertSelective(userVo);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 权限页面
	 */
	@RequestMapping(value="/toPower",method=RequestMethod.GET)
	public String toPower(HttpServletRequest request) {
		request.setAttribute("id", getString(request, "id"));
		return "/xtgl/user/powerlist";
	}
	
	/**
	 * 权限页面数据
	 */
	@RequestMapping(value="/Power",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo Power(HttpServletRequest request) {
		PageModel<PowerVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		Integer id = new Integer(getString(request, "id"));
		page.put("id", id);
		
		userService.powerList(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PowerVo powerVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(powerVo);
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
	 * 修改用户
	 */
	@RequestMapping(value="/editUser",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editUser(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		try {
			UserVo userVo = new UserVo();
			userVo.setUserId(getString(request, "userId"));
			userVo.setUserName(getString(request, "userName"));
			userVo.setLoginName(getString(request, "loginName"));
			userVo.setInformation(getString(request, "information"));
			userVo.setEmail(getString(request, "email"));
			userVo.setFax(getString(request, "fax"));
			userVo.setPhone(getString(request, "phone"));
			userVo.setEmailPassword(getString(request, "emailPassword"));
			userService.updateById(userVo);
			message = "修改成功！";
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			message = "修改失败！";
		}
		return new EditRowResultVo(success, message);
	}
	
	
	
}
