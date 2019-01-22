/**
 * 
 */
package com.naswork.module.xtgl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.Menu;
import com.naswork.service.MenuService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.LigerTreeVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;

/**
 * @since 2016年3月29日 上午10:56:01
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Controller
@RequestMapping("/xtgl/menu")
public class MenuController extends BaseController {
	@Resource
	private MenuService menuService;

	/**
	 * 模块管理(菜单管理)
	 * @return
	 * @since 2015年8月20日 下午4:10:10
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/menulist",method = RequestMethod.GET)
	public String menulist(){
		return "/xtgl/menu/menulist";
	}
	
	/**
	 * 菜单列表表格数据
	 * @param request
	 * @param response
	 * @return
	 * @since 2016年3月29日 上午11:59:29
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/data",method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo menulistData(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<Menu> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String parentMenuId = request.getParameter("parentMenuId");
		String searchString = request.getParameter("searchString");
		if(StringUtils.isNotBlank(searchString)){
			searchString += " and parent_menu_id='"+parentMenuId+"'";
		}else{
			searchString = " parent_menu_id='"+parentMenuId+"'";
		}
		
		menuService.findPage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Menu menu : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(menu);
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
	 * 全菜单树
	 * @return
	 * @since 2016年3月29日 上午11:59:47
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/fullmenu",method = RequestMethod.POST)
	public @ResponseBody List<LigerTreeVo> fullmenu(){		
		return menuService.searchFullTree();
	}
	
	/**
	 * 添加同级菜单的页面
	 * @return
	 * @since 2015年8月21日 下午2:22:39
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/addSibling/{pid}",method = RequestMethod.GET)
	public String addSibling(@PathVariable String pid,HttpServletRequest request){
		request.setAttribute("parentMenuId", pid);
		Menu parentMenu = menuService.findById(pid, null);
		if(parentMenu!=null){
			if(parentMenu.getRootMenuId().equals("0")){
				request.setAttribute("rootMenuId", pid);
			}else{
				request.setAttribute("rootMenuId", parentMenu.getRootMenuId());
			}
		}else{
			request.setAttribute("rootMenuId", "0");
		}
		
		return "/xtgl/menu/addSibling";
	}
	
	/**
	 * 添加子菜单页面
	 * @param id
	 * @return
	 * @since 2015年8月21日 下午2:26:03
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/addSub/{id}",method = RequestMethod.GET)
	public String addSub(@PathVariable String id,HttpServletRequest request){
		request.setAttribute("parentMenuId", id);
		Menu parentMenu = menuService.findById(id, null);
		if(parentMenu.getRootMenuId().equals("0")){
			request.setAttribute("rootMenuId", id);
		}else{
			request.setAttribute("rootMenuId", parentMenu.getRootMenuId());
		}
		
		return "/xtgl/menu/addSub";
	}
	
	/**
	 * 修改菜单页面
	 * @param id
	 * @return
	 * @since 2015年8月21日 下午2:26:32
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/modify/{id}",method = RequestMethod.GET)
	public String modify(@PathVariable String id,HttpServletRequest request){
		List<RoleVo> allRoles = (List<RoleVo>) this.getDmObj("ALL_ROLE", "allRoles");
		Menu menu = menuService.findById(id, allRoles);
		if(menu!=null){
			request.setAttribute("menu", menu);
		}
		Map<String, String> roleMap = new HashMap<String, String>();
		for(RoleVo role: allRoles){
			roleMap.put(role.getRoleId(), role.getRoleName());
		}
		request.setAttribute("allRoleMap", roleMap);
		return "/xtgl/menu/modify";
	}
	
	/**
	 * 添加菜单
	 * @return
	 * @since 2015年8月21日 下午5:24:08
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/addMenu",method = RequestMethod.POST)
	public @ResponseBody ResultVo addMenu(Menu menu,HttpServletRequest request){
		boolean success = false;
		String message = "";
		Menu menuEntity = new Menu();
		menuEntity.setIsLeaf(menu.getIsLeaf());
		//TODO use new user system
		//menuEntity.setLrrDm(getCurrentUser(request).getSwry().getSwryDm());
		menuEntity.setMenuName(menu.getMenuName());
		menuEntity.setMenuOrder(menu.getMenuOrder());
		menuEntity.setMenuUrl(menu.getMenuUrl());
		menuEntity.setParentMenuId(menu.getParentMenuId());
		menuEntity.setYxbz(menu.getYxbz());
		menuEntity.setRootMenuId(menu.getRootMenuId());
		
		try {
			menuService.add(menuEntity);
			success = true;
			message = "添加菜单成功";
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "添加菜单时发生异常";
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 修改菜单
	 * @param menu
	 * @return
	 * @since 2015年8月24日 下午3:30:02
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/modifyMenu",method = RequestMethod.POST)
	public @ResponseBody ResultVo modifyMenu(HttpServletRequest request, @ModelAttribute Menu menu){
		boolean success = false;
		String message = "";
		String roleListStr = this.getString(request, "roleList");
		String[] roleIdList = roleListStr.split(",");
		List<RoleVo> allRoles = (List<RoleVo>) this.getDmObj("ALL_ROLE", "allRoles");
		Menu menuEntity = menuService.findById(String.valueOf(menu.getMenuId()), allRoles);
		menuEntity.setMenuName(menu.getMenuName());
		menuEntity.setMenuOrder(menu.getMenuOrder());
		menuEntity.setMenuUrl(menu.getMenuUrl());
		menuEntity.setYxbz(menu.getYxbz());
		menuEntity.setIsLeaf(menu.getIsLeaf());
		menuEntity.setRootMenuId(menu.getRootMenuId());
		try {
			menuService.modify(menuEntity, roleIdList);
			success = true;
			message = "修改菜单成功";
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			message = "修改菜单时发生异常";
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 删除菜单
	 * @param request
	 * @param id
	 * @return
	 * @since 2016年3月31日 上午10:08:21
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/deleteMenu",method = RequestMethod.POST)
	public @ResponseBody ResultVo deleteMenu(HttpServletRequest request,@Param("id")String id){
		boolean success = false;
		String message = "";
		Assert.notNull(id, "菜单ID不能为空");
		try{
			menuService.remove(id);
			success = true;
			message = "删除菜单成功";
		}catch(Exception e){
			e.printStackTrace();
			success = false;
			message = "删除菜单失败,原因:"+e.getMessage();
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 岗位权限
	 * @param gwxh
	 * @return
	 * @since 2016年4月12日 上午10:09:55
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/gwmenu/{gwxh}",method = RequestMethod.POST)
	public @ResponseBody List<LigerTreeVo> gwmenu(@PathVariable String gwxh){
		List<LigerTreeVo> treeList = menuService.searchTreeByGwxh(gwxh);
		if (CollectionUtils.isEmpty(treeList)) {
			logger.error("--------------------The treeList is null!");
			if (StringUtils.isBlank(gwxh)) {
				logger.error("--------------------The gwxh is null!");
			}else {
				treeList = menuService.searchTreeByGwxh(gwxh);
			}
		}
		return treeList;
	}
}
