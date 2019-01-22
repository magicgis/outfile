package com.naswork.module.system.controller.clientmanage;

import java.text.DecimalFormat;
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
import com.naswork.model.Client;
import com.naswork.model.ClientBankMessage;
import com.naswork.model.ClientClassify;
import com.naswork.model.ClientContact;
import com.naswork.model.ClientProfitmargin;
import com.naswork.model.SupplierClassify;
import com.naswork.model.SystemCode;
import com.naswork.service.ClientClassifyService;
import com.naswork.service.ClientContactService;
import com.naswork.service.ClientProfitmarginService;
import com.naswork.service.ClientService;
import com.naswork.service.RoleService;
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

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/system/clientmanage")
public class ClientManageController extends BaseController{

	@Resource
	private ClientService clientService;
	
	@Resource
	private ClientContactService clientContactService;
	
	@Resource
	private SystemCodeService systemCodeService;
	
	@Resource
	private UserService userService;
	
	@Resource
	ClientClassifyService clientClassifyService;
	
	@Resource
	private ClientProfitmarginService clientProfitmarginService;
	
	@Resource
	private RoleService roleService;
	
	/**
	 * 跳转客户列表
	 */
	@RequestMapping(value="/toClientList",method=RequestMethod.GET)
	public String toClientList() {
		return "/system/clientmanage/list";
	}
	
	/**
	 * 客户列表数据
	 */
	@RequestMapping(value="/clientList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo clientList(HttpServletRequest request) {
		PageModel<Client> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		clientService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Client client : page.getEntities()) {
				client.setPrepayRate(client.getPrepayRate()*100);
				client.setShipPayRate(client.getShipPayRate()*100);
				client.setReceivePayRate(client.getReceivePayRate()*100);
				if(null!=client.getProfitMargin()){
					client.setProfitMargin(client.getProfitMargin()*100);
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(client);
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
	 * 跳转新增客户页面
	 */
	@RequestMapping(value="/toAddClient",method=RequestMethod.GET)
	public String toAddClient(HttpServletRequest request) {
		return "/system/clientmanage/addclient";
	}
	
	/**
	 * 保存新增客户
	 */
	@RequestMapping(value="/saveClient",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveClient(HttpServletRequest request,@ModelAttribute Client client) {
		String message = "";
		boolean success = false;
		UserVo userVo = getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		List<UserVo> list = userService.getByRole(roleVo.getRoleName());
		if (client.getCode()!=null) {
			client.setUpdateTimestamp(new Date());
			client.setCreateDate(new Date());
			client.setProfitMargin(client.getProfitMargin()/100);
			clientService.insertSelective(client,list);
			//没新增一个客户默认利润为18
			ClientProfitmargin clientProfitmargin=new ClientProfitmargin();
			clientProfitmargin.setClientId(client.getId().toString());
			/*if (client.getProfitMargin() != null) {
				clientProfitmargin.setProfitMargin(client.getProfitMargin());
			}else {
				clientProfitmargin.setProfitMargin(0.18);
			}*/
			clientProfitmargin.setProfitMargin(client.getProfitMargin());
			clientProfitmarginService.insert(clientProfitmargin);
			message = "新增成功！";
			success = true;
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 跳转修改客户信息
	 */
	@RequestMapping(value="/toClientEdit",method=RequestMethod.GET)
	public String toClientEdit(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		Client client = clientService.selectByPrimaryKey(id);
		client.setPrepayRate(client.getPrepayRate()*100);
		client.setShipPayRate(client.getShipPayRate()*100);
		client.setReceivePayRate(client.getReceivePayRate()*100);
		if(null!=client.getProfitMargin()){
			 DecimalFormat df = new DecimalFormat("#.00");   
			Double profitMargin=client.getProfitMargin()*100;
			client.setProfitMargin(new Double(df.format(profitMargin)));
		}
		request.setAttribute("client",client);
		return "/system/clientmanage/editclient";
	}
	
	/**
	 * 保存客户修改信息
	 */
	@RequestMapping(value="/saveEdit",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveEdit(HttpServletRequest request,@ModelAttribute Client client) {
		String message = "";
		boolean success = false;
		
		if (client.getId()!=null) {
			if(null!=client.getProfitMargin()){
			client.setProfitMargin(client.getProfitMargin()/100);
			}
			clientService.updateByPrimaryKeySelective(client);
			message = "修改成功！";
			success = true;
		}else {
			message = "修改失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	
	/**
	 * 跳转客户联系人列表
	 */
	@RequestMapping(value="/toContactsList",method=RequestMethod.GET)
	public String toContactsList(HttpServletRequest request) {
		Integer clientId = new Integer(getString(request, "id"));
		request.setAttribute("clientId", clientId);
		return "/system/clientmanage/contactlist";
	}
	
	/**
	 * 客户联系人数据
	 */
	@RequestMapping(value="/ContactsList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo ContactsList(HttpServletRequest request) {
		Integer clientId = new Integer(getString(request, "clientId"));
		PageModel<ClientContact> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		page.put("clientId", clientId);
		
		clientContactService.list(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientContact clientContact : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(clientContact);
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
	 * 货币信息
	 */
	@RequestMapping(value="/currencyList",method=RequestMethod.POST)
	public @ResponseBody ResultVo currencyList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit==1) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.findCurrency();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getCurrencyId())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getCurrencyId())) {
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
	
	/**
	 * 是否需要合格证
	 */
	@RequestMapping(value="/certificationList",method=RequestMethod.POST)
	public @ResponseBody ResultVo certificationList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit==1) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.findType("SUPPLIER_TAXREIMBURSEMENT_ID");
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getCertification())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getCertification())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.findType("SUPPLIER_TAXREIMBURSEMENT_ID");
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 拥有人
	 */
	@RequestMapping(value="/Owners",method=RequestMethod.POST)
	public @ResponseBody ResultVo Owners(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<UserVo> list = new ArrayList<UserVo>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit.equals(1)) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<UserVo> list2 = userService.getOwner();
			for (UserVo userVo : list2) {
				if (userVo.getUserId().equals(client.getOwner().toString())) {
					list.add(userVo);
				}
			}
			for (UserVo userVo : list2) {
				if (!userVo.getUserId().equals(client.getOwner().toString())) {
					list.add(userVo);
				}
			}
		}else {
			list = userService.getOwner();
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 用户列表
	 *//*
	@RequestMapping(value="/power",method=RequestMethod.POST)
	public @ResponseBody ResultVo power(HttpServletRequest request) {
		UserVo userVo = getCurrentUser(request);
		boolean success = true;
		String msg = "";
		List<RoleVo> list = userService.searchRoleByUserId(userVo.getUserId());
		List<UserVo> userList = userService.
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}*/
	
	/**
	 * 验证code
	 */
	@RequestMapping(value="/testCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo testCode(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		String code = getString(request, "code");
		Client client = clientService.findByCode(code);
		if (client!=null) {
			success = true;
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 跳转新增联系人
	 */
	@RequestMapping(value="/toAddContact",method=RequestMethod.GET)
	public String toAddContacts(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		return "/system/clientmanage/addcontact";
	}
	
	/**
	 * 保存新增联系人
	 */
	@RequestMapping(value="/saveContact",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveContacts(HttpServletRequest request,@ModelAttribute ClientContact clientContact) {
		String message = "";
		boolean success = false;
		if (clientContact.getClientId()!=null) {
			clientContact.setUpdateTimestamp(new Date());
			clientContactService.insertSelective(clientContact);
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 修改联系人
	 */
	@RequestMapping(value="/saveEditContact",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo saveEditContact(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		try {
			ClientContact clientContact = new ClientContact();
			clientContact.setId(new Integer(getString(request, "id")));
			clientContact.setName(getString(request, "name"));
			clientContact.setPostCode(getString(request, "postCode"));
			clientContact.setAddress(getString(request, "address"));
			clientContact.setShipAddress(getString(request, "shipAddress"));
			clientContact.setPhone(getString(request, "phone"));
			clientContact.setFax(getString(request, "fax"));
			clientContact.setEmail(getString(request, "email"));
			clientContact.setRemark(getString(request, "remark"));
			clientContact.setUpdateTimestamp(new Date());
			clientContactService.updateByPrimaryKeySelective(clientContact);
			success = true;
			message = "修改成功！";
			
		} catch (Exception e) {
			message = "修改失败！";
			e.printStackTrace();
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 客户状态
	 */
	@RequestMapping(value="/statusList",method=RequestMethod.POST)
	public @ResponseBody ResultVo statusList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit==1) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.getClientStatus();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getClientStatusId())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getClientStatusId())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.getClientStatus();
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	
	/**
	 * 客户等级
	 */
	@RequestMapping(value="/levelList",method=RequestMethod.POST)
	public @ResponseBody ResultVo levelList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit==1) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.getClientLevel();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getClientLevelId())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getClientLevelId())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.getClientLevel();
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 跳转银行信息页面
	 */
	@RequestMapping(value="/toBankList",method=RequestMethod.GET)
	public String toBankList(HttpServletRequest request) {
		request.setAttribute("clientId", getString(request, "id"));
		return "/system/clientmanage/banklist";
	}
	
	/**
	 * 银行信息
	 */
	@RequestMapping(value="/bankList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo bankList(HttpServletRequest request) {
		Integer clientId = new Integer(getString(request, "clientId"));
		PageModel<ClientBankMessage> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		page.put("clientId", clientId);
		
		clientService.bankList(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientBankMessage clientBankMessage : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(clientBankMessage);
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
	 * 跳转新增银行页面
	 */
	@RequestMapping(value="/toBankAdd",method=RequestMethod.GET)
	public String toBankAdd(HttpServletRequest request) {
		request.setAttribute("clientId", getString(request, "id"));
		return "/system/clientmanage/addbank";
	}
	
	/**
	 * 新增银行信息
	 */
	@RequestMapping(value="/saveBank",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveBank(HttpServletRequest request,@ModelAttribute ClientBankMessage clientBankMessage) {
		String message = "";
		boolean success = false;
		
		if (clientBankMessage.getClientId()!=null) {
			clientBankMessage.setUpdateTimestamp(new Date());
			clientService.insertBank(clientBankMessage);
			success = true;
			message = "新增成功！";
		}else {
			message = "新增失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 修改银行信息
	 */
	@RequestMapping(value="/editBank",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editBank(HttpServletRequest request) {
		boolean success = false;
		String message = "";
		try {
			ClientBankMessage clientBankMessage = new ClientBankMessage();
			clientBankMessage.setId(new Integer(getString(request, "id")));
			clientBankMessage.setBankAccountNumber(new Integer(getString(request, "bankAccountNumber")));
			clientBankMessage.setBankName(getString(request, "bankName"));
			clientBankMessage.setAccountName(getString(request, "accountName"));
			clientBankMessage.setEnglishName(getString(request, "englishName"));
			clientBankMessage.setBankAddress(getString(request, "bankAddress"));
			clientBankMessage.setBankPhoneNumber(getString(request, "bankPhoneNumber"));
			clientBankMessage.setReamrk(getString(request, "remark"));
			clientService.updateBank(clientBankMessage);
			success = true;
			message = "修改成功！";
		} catch (Exception e) {
			e.printStackTrace();
			message = "修改失败！";
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/**
	 * 发货方式
	 */
	@RequestMapping(value="/shipWay",method=RequestMethod.POST)
	public @ResponseBody ResultVo shipWay(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit==1) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.shipWay();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getClientShipWay())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getClientShipWay())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.shipWay();
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 交付方式
	 */
	@RequestMapping(value="/delivery",method=RequestMethod.POST)
	public @ResponseBody ResultVo delivery(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit.equals(1)) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.delivery();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getTermsOfDelivery())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getTermsOfDelivery())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.delivery();
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 客户类型
	 */
	@RequestMapping(value="/clientType",method=RequestMethod.POST)
	public @ResponseBody ResultVo clientType(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit.equals(1)) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.type();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getClientTemplateType())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getClientTemplateType())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.type();
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 发票模板
	 */
	@RequestMapping(value="/invoiceTemplet",method=RequestMethod.POST)
	public @ResponseBody ResultVo invoiceTemplet(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit.equals(1)) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.invoiceTemplet();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getInvoiceTemplet())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getInvoiceTemplet())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.invoiceTemplet();
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 箱单模板
	 */
	@RequestMapping(value="/shipTemplet",method=RequestMethod.POST)
	public @ResponseBody ResultVo shipTemplet(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = new ArrayList<SystemCode>();
		Integer edit = new Integer(getString(request, "edit"));
		if (edit.equals(1)) {
			Integer id =new Integer(getString(request, "id"));
			Client client = clientService.selectByPrimaryKey(id);
			List<SystemCode> list2 = systemCodeService.findType("SHIP_TEMPLET");
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(client.getShipTemplet())) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(client.getShipTemplet())) {
					list.add(systemCode);
				}
			}
		}else {
			list = systemCodeService.findType("SHIP_TEMPLET");
		}
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 校验网址
	 */
	@RequestMapping(value="/checkUrl",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkUrl(HttpServletRequest request) {
		String message = "";
		boolean success = false;
		String url = getString(request, "url");
		if (url!=null && !"".equals(url)) {
			int index = url.lastIndexOf(".");
			String checkUrl = "";
			if (url.startsWith("www")) {
				checkUrl = url.substring(4, index+1);
			}else {
				checkUrl = url.substring(0, index+1);
			}
			List<String> urls = clientService.getUrl();
			for (int i = 0; i < urls.size(); i++) {
				String getUrl = "";
				if (!"".equals(urls.get(i)) && urls.get(i) != null) {
					int lastIndex = urls.get(i).lastIndexOf(".");
					if (urls.get(i).startsWith("www")) {
						getUrl = urls.get(i).substring(4, lastIndex+1);
					}else {
						getUrl = urls.get(i).substring(0, lastIndex+1);
					}
					if (getUrl.equals(checkUrl)) {
						success = true;
						break;
					}
				}
			}
		}
		return new ResultVo(success, message);
	}
	
	/**
	 * 客户归类
	 */
	@RequestMapping(value="/classifyList",method=RequestMethod.POST)
	public @ResponseBody ResultVo classifyList(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		String clientId=request.getParameter("id");
		List<ClientClassify> list=new ArrayList<ClientClassify>();
		List<SystemCode> codes=systemCodeService.findSupplierByType("CLIENT_CLASSIFY_ID");
		if(!"".equals(clientId)&&null!=clientId){
			list=clientClassifyService.selectByPrimaryKey(Integer.parseInt(clientId));
			
			for (ClientClassify clientClassify : list) {
				for (SystemCode systemCode : codes) {
						if(clientClassify.getClientClassifyId().equals(systemCode.getId().toString())){
							systemCode.setCheck("checked");
						}
				}
			}
			 
		}
		
		success = true;
		JSONArray json = new JSONArray();
			json.add(codes);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/**
	 * 用户列表
	 */
	@RequestMapping(value="/userList",method=RequestMethod.POST)
	public @ResponseBody ResultVo userList(HttpServletRequest request) {
		try {
			UserVo userVo = getCurrentUser(request);
			List<Integer> roles = roleService.getRoleIdByUserId(new Integer(userVo.getUserId()));
			RoleVo roleVo = roleService.selectByRoleId(new Integer(roles.get(0).toString()));
			List<UserVo> allUserVos = userService.getUsers();
			List<UserVo> userVos = new ArrayList<UserVo>();
			if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
				userVos = userService.getUserIdByRoleId(roles.get(0).toString());
			}else {
				userVos = allUserVos;
			}
			JSONArray json = new JSONArray();
			json.add(userVos);
			return new ResultVo(true, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
		}
	}
	
}
