package com.naswork.module.storage.controller.sendbackmessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.model.SendBackMessage;
import com.naswork.model.SystemCode;
import com.naswork.model.UnknowStorageDetail;
import com.naswork.service.SendBackMessageService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping(value="/storage/sendbackmessage")
public class SendBackMessageController extends BaseController {

	@Resource
	private SendBackMessageService sendBackMessageService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private UserService userService;
	
	
	@RequestMapping(value="/toList")
	public String toList(){
		return "/storage/sendbackmessage/List";
	}
	
	/**
	 * 显示列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SendBackMessage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			if (roleVo.getRoleName().indexOf("销售") >= 0) {
				page.put("marketUserId", userVo.getUserId());
			}else if (roleVo.getRoleName().indexOf("采购") >= 0) {
				page.put("purchaseUserId", userVo.getUserId());
			}
		}
		sendBackMessageService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SendBackMessage sendBackMessage : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(sendBackMessage);
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
	 * 新增页面
	 * @return
	 */
	@RequestMapping(value="/toAdd",method=RequestMethod.GET)
	public String toAdd(){
		return "/storage/sendbackmessage/add";
	}
	
	/**
	 * 出库列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/exportList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo exportList(HttpServletRequest request,HttpServletResponse response) {
		PageModel<SendBackMessage> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		sendBackMessageService.getAddListPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SendBackMessage sendBackMessage : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(sendBackMessage);
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
	 * 保存新增
	 * @param request
	 * @param unknowStorageDetail
	 * @return
	 */
	@RequestMapping(value="/saveAdd",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveAdd(HttpServletRequest request,@ModelAttribute SendBackMessage sendBackMessage){
		try {
			sendBackMessageService.insertSelective(sendBackMessage);
			return new ResultVo(true, "新增成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
	}
	
	/**
	 * 修改页面
	 * @return
	 */
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit(HttpServletRequest request){
		Integer id = new Integer(getString(request, "id"));
		SendBackMessage sendBackMessage = sendBackMessageService.selectByPrimaryKey(id);
		request.setAttribute("sendBackMessage", sendBackMessage);
		return "/storage/sendbackmessage/edit";
	}
	
	/**
	 * 保存修改
	 * @param request
	 * @param unknowStorageDetail
	 * @return
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute SendBackMessage sendBackMessage){
		try {
			sendBackMessage.setUpdateTimestamp(new Date());
			sendBackMessageService.updateByPrimaryKeySelective(sendBackMessage);
			return new ResultVo(true, "修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改异常！");
		}
	}
	
	
	/**
	 * 获取状态列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/statusList",method=RequestMethod.POST)
	public @ResponseBody ResultVo statusList(HttpServletRequest request){
		try {
			String id = getString(request, "id");
			List<SystemCode> list = systemCodeService.findType("SEND_BACK_MANAGE_STATUS");
			List<SystemCode> arrayList = new ArrayList<SystemCode>();
			if (id != null && !"".equals(id)) {
				for (SystemCode systemCode : list) {
					if (systemCode.getId().toString().equals(id)) {
						arrayList.add(systemCode);
						break;
					}
				}
				for (SystemCode systemCode : list) {
					if (!systemCode.getId().toString().equals(id)) {
						arrayList.add(systemCode);
					}
				}
			}else {
				arrayList = list;
			}
			JSONArray json = new JSONArray();
			json.add(arrayList);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "获取状态异常！");
		}
	}
	
}
