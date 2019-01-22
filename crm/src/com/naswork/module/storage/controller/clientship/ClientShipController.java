package com.naswork.module.storage.controller.clientship;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.module.storage.controller.exportpackage.ExportPackageVo;
import com.naswork.service.ClientContactService;
import com.naswork.service.ClientShipService;
import com.naswork.service.ExportPackageService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientShip;
import com.naswork.model.ExportPackage;

@Controller
@RequestMapping(value="/storage/clientship")
public class ClientShipController extends BaseController{

	@Resource
	private ClientShipService clientShipService;
	@Resource
	private ExportPackageService exportPackageService;
	@Resource
	private ClientContactService clientContactService;
	
	
	
	/**
	 * 客户箱单
	 */
	@RequestMapping(value="/toClientShip",method=RequestMethod.GET)
	public String toClientShip() {
		return "/storage/clientship/clientshipList";
	}
	
	/**
	 * 客户箱单列表
	 */
	@RequestMapping(value="/ClientShip",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo ClientShip(HttpServletRequest request) {
		PageModel<ClientShipVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		
		clientShipService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientShipVo clientShipVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(clientShipVo);
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
	 * 箱单修改
	 */
	@RequestMapping(value="/toEdit",method=RequestMethod.GET)
	public String toEdit(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ClientShip clientShip = clientShipService.selectByPrimaryKey(id);
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(clientShip.getExportPackageId());
		request.setAttribute("exportPackage", exportPackage);
		request.setAttribute("clientShip", clientShip);
		return "/storage/clientship/editclientship";
	}
	
	/**
	 * 保存修改
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute ClientShip clientShip) {
		String message = "";
		boolean success = false;
		
		if (clientShip.getId()!=null) {
			clientShip.setUpdateTimestamp(new Date());
			clientShipService.updateByPrimaryKeySelective(clientShip);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 查询客户联系人
	 */
	@RequestMapping(value="/clientContacts",method=RequestMethod.POST)
	public @ResponseBody ResultVo clientContacts(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		PageModel<ClientContact> page = new PageModel<ClientContact>();
		Integer id = new Integer(getString(request, "id"));
		ClientShip clientShip = clientShipService.selectByPrimaryKey(id);
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(clientShip.getExportPackageId());
		page.put("clientId", exportPackage.getClientId());
		List<ClientContact> clientContacts = clientContactService.selectByclientId(page);
		List<ClientContact> list = new ArrayList<ClientContact>();
		
		for (ClientContact clientContact : clientContacts) {
			if (clientContact.getId().equals(clientShip.getClientContactId())) {
				list.add(clientContact);
			}
		}
		for (ClientContact clientContact : clientContacts) {
			if (!clientContact.getId().equals(clientShip.getClientContactId())) {
				list.add(clientContact);
			}
		}
		
		JSONArray json = new JSONArray();
		json.add(list);
		message = json.toString();
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 查询收货联系人
	 */
	@RequestMapping(value="/shipContacts",method=RequestMethod.POST)
	public @ResponseBody ResultVo shipContacts(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		PageModel<ClientContact> page = new PageModel<ClientContact>();
		Integer id = new Integer(getString(request, "id"));
		ClientShip clientShip = clientShipService.selectByPrimaryKey(id);
		ExportPackage exportPackage = exportPackageService.selectByPrimaryKey(clientShip.getExportPackageId());
		page.put("clientId", exportPackage.getClientId());
		List<ClientContact> clientContacts = clientContactService.selectByclientId(page);
		List<ClientContact> list = new ArrayList<ClientContact>();
		
		for (ClientContact clientContact : clientContacts) {
			if (clientContact.getId().equals(clientShip.getShipContactId())) {
				list.add(clientContact);
			}
		}
		for (ClientContact clientContact : clientContacts) {
			if (!clientContact.getId().equals(clientShip.getShipContactId())) {
				list.add(clientContact);
			}
		}
		
		JSONArray json = new JSONArray();
		json.add(list);
		message = json.toString();
		
		return new ResultVo(success, message);
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
