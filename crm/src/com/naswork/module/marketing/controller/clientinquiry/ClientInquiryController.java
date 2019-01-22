package com.naswork.module.marketing.controller.clientinquiry;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.naswork.model.*;
import oracle.net.aso.o;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.druid.sql.visitor.functions.If;
import com.naswork.common.controller.BaseController;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.purchase.controller.supplierinquiry.SaveVo;
import com.naswork.module.purchase.controller.supplierquote.ListDateVo;
import com.naswork.service.ClientContactService;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.ClientQuoteElementService;
import com.naswork.service.ClientQuoteService;
import com.naswork.service.ClientService;
import com.naswork.service.CompetitorService;
import com.naswork.service.DueRemindService;
import com.naswork.service.ExchangeRateService;
import com.naswork.service.HistoricalOrderPriceService;
import com.naswork.service.SupplierQuoteService;
import com.naswork.service.SupplierService;
import com.naswork.service.SystemCodeService;
import com.naswork.service.TPartService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.ColumnVo;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONArray;


@Controller
@RequestMapping(value="/sales/clientinquiry")
public class ClientInquiryController extends BaseController{

	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private ClientService clientService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private ClientContactService clientContactService;
	@Resource
	private ClientQuoteService clientQuoteService;
	@Resource
	private CompetitorService competitorService;
	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private TPartService tPartService;
	@Resource
	private UserService userService;
	@Resource 
	private SupplierQuoteService supplierQuoteService;
	@Resource
	private DueRemindService dueRemindService;
	@Resource
	private HistoricalOrderPriceService historicalOrderPriceService;
	@Resource
	private ClientQuoteElementService clientQuoteElementService;
	@Resource
	private SupplierService supplierService;
	@Resource
	private ExchangeRateService exchangeRateService;

	private static Logger logger = LoggerFactory.getLogger(ClientInquiryController.class);

	/*
	 * 跳转列表页面
	 */
	@RequestMapping(value="/clientinquiryList",method = RequestMethod.GET)
	public String  clientinquiryList() {
		return "/marketing/clientinquiry/clientinquiryList";
		//return "/pages/mgr/kjAdmin/admin_demand";
	}
	
	/*
	 * 列表数据
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientInquiryVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		clientInquiryService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientInquiryVo clientInquiryVo : page.getEntities()) {
				if(clientInquiryVo.getInquiryStatusId()!=30 && clientInquiryVo.getInquiryStatusId()!=31 && clientInquiryVo.getInquiryStatusId()!=34){
					int icount=dueRemindService.findInquiryCount(clientInquiryVo.getId());
					int qcount=dueRemindService.findQuoteCount(clientInquiryVo.getId());
//					int p=qcount/icount;
					BigDecimal i=new BigDecimal(icount);
					BigDecimal q=new BigDecimal(qcount);
					if (!q.equals(BigDecimal.ZERO)){
						BigDecimal p=q.divide(i,10,BigDecimal.ROUND_HALF_DOWN);
						BigDecimal p2=p.multiply(new BigDecimal(100));
						String proportion=p2.setScale(0, BigDecimal.ROUND_HALF_UP)+"%"+"("+qcount+"/"+icount+")";
						clientInquiryVo.setProportion(proportion);
					}
					Double total = clientInquiryElementService.getTotalPrice(clientInquiryVo.getId());
					clientInquiryVo.setTotal(total);
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientInquiryVo);
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
	 * 查询所有客户代码
	 */
	@RequestMapping(value="/clientCode",method=RequestMethod.POST)
	public @ResponseBody ResultVo ClientCode(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		String all = request.getParameter("all");
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		PageModel<Client> page = getPage(request);
		if (all == null) {
			if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
				page.put("userId", userVo.getUserId());
			}
		}
		List<Client> list=clientService.findAll(page);
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*
	 * 查询飞机类型
	 */
	@RequestMapping(value="/airType",method=RequestMethod.POST)
	public @ResponseBody ResultVo airType(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		UserVo userVo=getCurrentUser(request);
		String userName=userVo.getUserName();
		List<SystemCode> list=systemCodeService.findairType();
		List<SystemCode> arraylist=new ArrayList<SystemCode>();
		for (int i = 0; i < list.size(); i++) {
			if(userName.equals("Tracy")){
				if(!"".equals(list.get(i).getValue())&&Integer.valueOf(list.get(i).getCode()).intValue()>28){
					arraylist.add(list.get(i));
				}
			}
		}
		if(userName.equals("Tracy")){
			list.clear();
			list.addAll(arraylist);
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*
	 * 查询商业类型
	 */
	@RequestMapping(value="/bizType",method=RequestMethod.POST)
	public @ResponseBody ResultVo bizType() {
		boolean success = false;
		String msg = "";
		List<SystemCode> list=systemCodeService.findbizType();
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*
	 * 状态
	 */
	@RequestMapping(value="/zt",method=RequestMethod.POST)
	public @ResponseBody ResultVo zt(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		Integer id=new Integer(getString(request, "id"));
		ClientInquiry clientInquiry=clientInquiryService.findById(id);
		List<SystemCode> list2=systemCodeService.findInquiryStatus();
		List<SystemCode> list=new ArrayList<SystemCode>();
		SystemCode systemCode=new SystemCode();
		for (SystemCode systemCode2 : list2) {
			if (systemCode2.getId()==clientInquiry.getInquiryStatusId()) {
				systemCode=systemCode2;
			}
		}
		list.add(systemCode);
		for (SystemCode systemCode2 : list2) {
			if (systemCode2.getId()!=clientInquiry.getInquiryStatusId()) {
				list.add(systemCode2);
			}
		}
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*
	 * 查询联系人
	 */
	@RequestMapping(value="/contacts",method=RequestMethod.POST)
	public @ResponseBody ResultVo Listcontacts(HttpServletRequest request) {
		PageModel<ClientContact> page = new PageModel<ClientContact>();
		boolean success = false;
		String msg = "";
		String flag = getString(request, "flag");
		List<ClientContact> list=new ArrayList<ClientContact>();
		UserVo userVo = getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		PageModel<String> pageUser = new PageModel<String>();
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && roleVo.getRoleName().indexOf("物流") < 0) {
			pageUser.put("userId", userVo.getUserId());
		}
		//判断是新增还是修改
		if("edit".equals(flag)){
			Integer clientContactId=new Integer(getString(request, "clientContactId"));
			Integer clientId=new Integer(getString(request, "clientId"));
			page.put("clientId", clientId);
			List<ClientContact> list2=clientContactService.selectByclientId(page);
			ClientContact clientContact=clientContactService.selectByPrimaryKey(clientContactId);
			
			list.add(clientContact);
			for (ClientContact a : list2) {	
				if (!a.getId().equals(clientContact.getId())) {
					list.add(a);
				}
			}
		}else if ("add".equals(flag)) {
			list=clientService.findContacts(pageUser);
		}
		
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*
	 * 新增页面
	 */
	@RequestMapping(value="/addMessage",method=RequestMethod.GET)
	public String addMessage(HttpServletRequest request) {
		Date today = new Date();
		request.setAttribute("today", today);
		String token=get32UUID();
		HttpSession session = request.getSession();
		session.setAttribute("token", token);
		request.setAttribute("token", token);
		return "/marketing/clientinquiry/addMessage";
	}
	
	/*
	 * 修改页面
	 */
	@RequestMapping(value="/editMessage",method=RequestMethod.GET)
	public String editMessage(HttpServletRequest request) { 
		Integer id = new Integer(getString(request, "id"));
		ClientInquiry clientInquiry=clientInquiryService.findById(id);
		request.setAttribute("clientInquiry", clientInquiry);
		return "/marketing/clientinquiry/editMessage";
	}
	
	
	
	/*
	 * 保存新增数据
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody ResultVo add(HttpServletRequest request,@ModelAttribute ClientInquiry clientInquiry) {
		String mixId=getString(request, "mixId");
		boolean success = false;
		String msg = "";
		HttpSession session = request.getSession();
		Object retoken=request.getParameter("token");
		Object setoken=session.getAttribute("token");
		UserVo userVo = getCurrentUser(request);
		if(null!=setoken&&setoken.equals(retoken)){
			if (clientInquiry.getAirTypeId()!=null) {
				clientInquiry.setUpdateTimestamp(new Date());
				String a[]=mixId.split(",");
				clientInquiry.setClientContactId(new Integer(a[0]));
				clientInquiry.setClientId(new Integer(a[1]));
				clientInquiry.setCreateUser(new Integer(userVo.getUserId()));
				clientInquiryService.add(clientInquiry);
				session.removeAttribute("token");
				success=true;
				msg=clientInquiry.getId().toString();
				String number = clientInquiry.getQuoteNumber();
				msg = msg+","+number;
			}else {
				msg="新增失败！";
			}
		}else{
			msg=null;
			return  new ResultVo(success, msg);
		}
/*		List<ClientInquiryElement> elements = clientInquiryElementService.getList();
		for (int i = 0; i < elements.size(); i++) {
			List<String> cagecodes = clientInquiryElementService.getCageCode(elements.get(i).getPartNumber());
			StringBuffer str = new StringBuffer();
			for (int j = 0; j < cagecodes.size(); j++) {
				str.append(cagecodes.get(j)).append(",");
			}
			if (str.length() > 0) {
				str.deleteCharAt(str.length()-1);
			}
			clientInquiryElementService.updateCageCode(str.toString(), elements.get(i).getItem().toString());
		}*/
		return new ResultVo(success, msg);
	}
	
	
	/*
	 * 保存修改后的数据
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public @ResponseBody ResultVo edit(HttpServletRequest request,@ModelAttribute ClientInquiry clientInquiry) {
		boolean success = false;
		String msg = "";
		if (clientInquiry.getId()!=null) {
			clientInquiry.setUpdateTimestamp(new Date());
			clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
			success=true;
			msg="修改成功！";
		}else {
			msg="修改失败！";
		}
		return new ResultVo(success, msg);
	}
	
	/*
	 * 明细页面
	 */
	@RequestMapping(value="/element",method=RequestMethod.GET)
	public String element(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ClientInquiry clientInquiry=clientInquiryService.findById(id);
		request.setAttribute("id", id);
		request.setAttribute("quoteNumber", clientInquiry.getQuoteNumber());
		return "/marketing/clientinquiry/elementList";
	}
	
	/*
	 * 增加明细页面
	 */
	@RequestMapping(value="/addelement",method=RequestMethod.GET)
	public String addelement(HttpServletRequest request) {
		String id = getString(request, "id");
		request.setAttribute("id", id);
		return "/marketing/clientinquiry/addElement";
	}
	
	
	/*
	 * 明细列表数据
	 */
	@RequestMapping(value="/elementData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String id = getString(request, "id");
		GridSort sort = getSort(request);
		page.put("id", id);
		clientInquiryService.ElePage(page,sort);		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ElementVo elementVo : page.getEntities()) {
				List<String> cages = tPartService.getCageCodeByShortCode(clientInquiryService.getCodeFromPartNumber(elementVo.getPartNumber()));
				if (cages.size() > 0) {
					StringBuffer cage = new StringBuffer();
					for (int j = 0; j < cages.size(); j++) {
						cage.append(cages.get(j)).append(",");
					}
					cage.deleteCharAt(cage.length()-1);
					elementVo.setCageCode(cage.toString());
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(elementVo);
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
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			ClientInquiryElement clientInquiryElement) {
		boolean success=false;
		String message="";
		Integer id =new Integer(getString(request, "id"));
		UserVo userVo=getCurrentUser(request);



		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = clientInquiryService.uploadExcel(multipartFile, id,new Integer(userVo.getUserId()),userVo);
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(id);
		List<ClientInquiryElement> list = clientInquiryElementService.findByclientInquiryId(id);
		Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
		//爬虫
		if (clientInquiry.getBizTypeId().equals(128) || clientInquiry.getBizTypeId() == 128 || client.getCode().startsWith("9") || client.getCode().startsWith("8") || client.getCode().startsWith("70") || client.getCode().startsWith("370")) {
			clientInquiryElementService.searchSatair(list,id,0,0,null);
		}
		if (messageVo.getFlag()) {
			clientInquiry.setEmailStatus(1);
			clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
			clientInquiryElementService.matchElement(list);
		}
		//发送QQ邮件
		if ((client.getCode().startsWith("9") || userVo.getLoginName().toLowerCase().equals("kent")) && clientInquiry.getQqMailSend().equals(0) && list.size() > 0) {
			logger.info("创建询价明细--开始发邮件");
			boolean flag = clientInquiryElementService.QQEmail(list, clientInquiry,userVo);
//			System.out.println("打印邮件发送标志："+flag);
			//邮箱发送成功生成一条记录
			if(flag){
				clientInquiry.setQqMailSend(1);
			}
			clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
		}else{
			logger.info("创建询价明细但是并不需要发邮件");
		}
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	
	/*
	 * 新增报价
	 */
	@RequestMapping(value="/addquote",method=RequestMethod.GET)
	public String addquote(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ClientInquiry clientInquiry = clientInquiryService.findById(id);
		Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
		if (client.getPrepayRate() != null) {
			client.setPrepayRate(client.getPrepayRate()*100);
		}
		if (client.getShipPayRate() != null) {
			client.setShipPayRate(client.getShipPayRate()*100);
		}
		if (client.getReceivePayRate() != null) {
			client.setReceivePayRate(client.getReceivePayRate()*100);
		}
		if (client.getProfitMargin() != null) {
			client.setProfitMargin(client.getProfitMargin()*100);
		}
		clientInquiry.setInquiryDate(new Date());
		request.setAttribute("id", id);
		request.setAttribute("client", client);
		request.setAttribute("clientInquiry", clientInquiry);
		return "/marketing/clientinquiry/addClientQuote";
	}
	
	/*
	 * 货币信息
	 */
	@RequestMapping(value="/currencyType",method=RequestMethod.POST)
	public @ResponseBody ResultVo currencyType(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		try {
			String clientId = request.getParameter("clientId");
			List<ClientQuoteVo> list2 = systemCodeService.findRate();
			List<ClientQuoteVo> list = new ArrayList<ClientQuoteVo>();
			if (clientId != null) {
				Client client = clientService.selectByPrimaryKey(new Integer(clientId));
				for (ClientQuoteVo clientQuoteVo : list2) {
					if (clientQuoteVo.getCurrency_id().toString().equals(client.getCurrencyId().toString())) {
						list.add(clientQuoteVo);
						break;
					}
				}
				for (ClientQuoteVo clientQuoteVo : list2) {
					if (!clientQuoteVo.getCurrency_id().toString().equals(client.getCurrencyId().toString())) {
						list.add(clientQuoteVo);
					}
				}
				JSONArray json = new JSONArray();
				json.add(list);
				message = json.toString();
			}else {
				for (ClientQuoteVo clientQuoteVo : list2) {
					if (clientQuoteVo.getCurrency_id()==11) {
						list.add(clientQuoteVo);
					}
				}
				for (ClientQuoteVo clientQuoteVo : list2) {
					if (clientQuoteVo.getCurrency_id()!=11) {
						list.add(clientQuoteVo);
					}
				}
				JSONArray json = new JSONArray();
				json.add(list);
				message = json.toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
			success=false;
		}

		return new ResultVo(success, message);
	}
	
	
	/*
	 * 修改商业模式
	 */
	@RequestMapping(value="/changeBiz",method=RequestMethod.POST)
	public @ResponseBody ResultVo changeBiz(HttpServletRequest request) {
		boolean success = true;
		String message = "";
		Integer id = new Integer(getString(request, "bizTypeId"));
		try {
			List<SystemCode> list2 = systemCodeService.findType("BIZ_TYPE");
			List<SystemCode> list = new ArrayList<SystemCode>();
			for (SystemCode systemCode : list2) {
				if (systemCode.getId().equals(id)) {
					list.add(systemCode);
				}
			}
			for (SystemCode systemCode : list2) {
				if (!systemCode.getId().equals(id)) {
					list.add(systemCode);
				}
			}
			JSONArray json = new JSONArray();
			json.add(list);
			message = json.toString();
		} catch (Exception e) {
			// TODO: handle exception
			success=false;
		}

		return new ResultVo(success, message);
	}
	
	/*
	 * 保存报价信息
	 */
	@RequestMapping(value="/saveQuote",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveQuote(HttpServletRequest request,@ModelAttribute ClientQuote clientQuote) {
		boolean success = false;
		String message = "";
		Integer status = 35;
		if (clientQuote.getCurrencyId()!=null) {
			ClientInquiry clientInquiry = clientInquiryService.findById(clientQuote.getClientInquiryId());
			//if (clientInquiry.getInquiryStatusId().equals(status)) {
			clientQuoteService.insertSelective(clientQuote);
			clientInquiryElementService.addStaticElement(clientInquiry, clientQuote);
			//}
			success=true;
			message="保存成功！";
		}else{
			message="保存失败！";
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 文件管理
	 */
	@RequestMapping(value="/file",method=RequestMethod.GET)
	public String file(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		request.setAttribute("id", id);
		request.setAttribute("tableName", "client_inquiry");
		return "/marketing/clientinquiry/fileUpload";
	}
	
	
	/*
	 * 竞争对手报价列表
	 */
	@RequestMapping(value="/toTender",method=RequestMethod.GET)
	public String toTender(HttpServletRequest request) {
		request.setAttribute("id", new Integer(getString(request, "id")));
		return "/marketing/clientinquiry/addTender";
	}
	
	/*
	 * 获取竞争对手的列数
	 */
	@RequestMapping(value="/getCols",method=RequestMethod.POST)
	public @ResponseBody ColumnVo getCols(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		List<CompetitorVo> list= competitorService.getCode(id);
		//List<Integer> ElementId = competitorService.getElementId(id);
		List<String> colModel = new ArrayList<String>();
		List<String> colNames = new ArrayList<String>();
		List<HistoricalOrderPrice> his = historicalOrderPriceService.selectByCLientInquiryId(id);
		for (int i = 0; i < list.size(); i++) {
			colNames.add(String.valueOf(list.get(i).getCode()));
			colModel.add("col"+String.valueOf(list.get(i).getId()));
		}
		for (int i = 0; i < his.size(); i++) {
			if (his.get(i) == null) {
				colNames.add(String.valueOf(" "));
				colModel.add("col"+String.valueOf("null"));
			}else {
				colNames.add(String.valueOf(his.get(i).getYear()));
				colModel.add("col"+String.valueOf(his.get(i).getYear()));
			}
			
		}
		ColumnVo result = new ColumnVo();
		result.setColumnDisplayNames(colNames);
		result.setColumnKeyNames(colModel);
		
		return result;
 	}
	
	/*
	 * 上传竞争对手
	 */
	@RequestMapping(value="/uploadCompetitor",method=RequestMethod.POST)
	public @ResponseBody String uploadCompetitor(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		Integer id =new Integer(getString(request, "id"));
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		MessageVo messageVo = clientInquiryService.uploadCompetitor(multipartFile, id);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	
	/*
	 * 竞争对手列表数据
	 */
	@RequestMapping(value="/competitorData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo competitorData(HttpServletRequest request) {
		PageModel<ElementVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String id = getString(request, "id");
		GridSort sort = getSort(request);
		page.put("id", id);
		
		clientInquiryService.ElePage(page,sort);	
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ElementVo elementVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(elementVo);
				ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
				clientInquiryElement.setClientInquiryId(elementVo.getClientInquiryId());
				clientInquiryElement.setId(elementVo.getId());
				List<CompetitorQuoteElement> list = competitorService.getPrice(clientInquiryElement);
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i)!=null) {
						if ("人民币".equals(list.get(i).getCurrencyValue())) {
							map.put("col"+String.valueOf(list.get(i).getId()), "CNY "+list.get(i).getPrice());
						}else if ("美元".equals(list.get(i).getCurrencyValue())) {
							map.put("col"+String.valueOf(list.get(i).getId()), "USD "+list.get(i).getPrice());
						}else if ("欧元".equals(list.get(i).getCurrencyValue())) {
							map.put("col"+String.valueOf(list.get(i).getId()), "EUR "+list.get(i).getPrice());
						}else if ("英镑".equals(list.get(i).getCurrencyValue())) {
							map.put("col"+String.valueOf(list.get(i).getId()), "GBP "+list.get(i).getPrice());
						}
						
					}
				}
				if (elementVo.getBsn() != null || !"".equals(elementVo.getBsn())) {
					List<HistoricalOrderPrice> his = historicalOrderPriceService.selectByBsn(elementVo.getBsn());
					for (int i = 0; i < his.size(); i++) {
						if (his.get(i).getYear() == null || "".equals(his.get(i).getYear())) {
							if ("人民币".equals(his.get(i).getCurrencyValue())) {
								map.put("col"+String.valueOf("null"), "CNY "+his.get(i).getPrice());
							}else if ("美元".equals(his.get(i).getCurrencyValue())) {
								map.put("col"+String.valueOf("null"), "USD "+his.get(i).getPrice());
							}else if ("欧元".equals(his.get(i).getCurrencyValue())) {
								map.put("col"+String.valueOf("null"), "EUR "+his.get(i).getPrice());
							}else if ("英镑".equals(his.get(i).getCurrencyValue())) {
								map.put("col"+String.valueOf("null"), "GBP "+his.get(i).getPrice());
							}
						}else {
							if ("人民币".equals(his.get(i).getCurrencyValue())) {
								map.put("col"+String.valueOf(his.get(i).getYear()), "CNY "+his.get(i).getPrice());
							}else if ("美元".equals(his.get(i).getCurrencyValue())) {
								map.put("col"+String.valueOf(his.get(i).getYear()), "USD "+his.get(i).getPrice());
							}else if ("欧元".equals(his.get(i).getCurrencyValue())) {
								map.put("col"+String.valueOf(his.get(i).getYear()), "EUR "+his.get(i).getPrice());
							}else if ("英镑".equals(his.get(i).getCurrencyValue())) {
								map.put("col"+String.valueOf(his.get(i).getYear()), "GBP "+his.get(i).getPrice());
							}
						}
					}
				}
				mapList.add(map);
			}
			ElementVo elementVo = new ElementVo();
			elementVo.setRemark("运费");
			Map<String, Object> map = EntityUtil.entityToTableMap(elementVo);
			List<CompetitorQuote> list = competitorService.getfreight(new Integer(id));
			for (int i = 0; i < list.size(); i++) {
				if ("人民币".equals(list.get(i).getCurrencyValue())) {
					map.put("col"+String.valueOf(list.get(i).getId()), "CNY "+list.get(i).getFreight());
				}else if ("美元".equals(list.get(i).getCurrencyValue())) {
					map.put("col"+String.valueOf(list.get(i).getId()), "USD "+list.get(i).getFreight());
				}else if ("欧元".equals(list.get(i).getCurrencyValue())) {
					map.put("col"+String.valueOf(list.get(i).getId()), "EUR "+list.get(i).getFreight());
				}else if ("英镑".equals(list.get(i).getCurrencyValue())) {
					map.put("col"+String.valueOf(list.get(i).getId()), "GBP "+list.get(i).getFreight());
				}
				
			}
			mapList.add(map);
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
	 * 少量新增明细
	 */
	@RequestMapping(value="/toAddElement",method=RequestMethod.GET)
	public String toAddElement(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(id);
		SystemCode systemCode = systemCodeService.selectByPrimaryKey(clientInquiry.getBizTypeId());
		request.setAttribute("id", id);
		request.setAttribute("exchange", systemCode.getCode());
		String token=get32UUID();
		request.setAttribute("token", token);
		return "/marketing/clientinquiry/addElementTable";
		
	}
	
	/*
	 * 明细修改
	 */
	@RequestMapping(value="/editElement",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editElement(HttpServletRequest request){
		boolean success = false;
		String message = "";
		try {
			String aimPrice = getString(request, "aimPrice");
			ClientInquiryElement clientInquiryElement = new ClientInquiryElement();
			clientInquiryElement.setId(new Integer(getString(request, "id")));
			clientInquiryElement.setPartNumber(getString(request, "partNumber"));
			clientInquiryElement.setAlterPartNumber(getString(request, "alterPartNumber"));
			clientInquiryElement.setDescription(getString(request, "description"));
			clientInquiryElement.setUnit(getString(request, "unit"));
			clientInquiryElement.setAmount(new Double(getString(request, "amount")));
			clientInquiryElement.setRemark(getString(request, "remark"));
			String conditionId = request.getParameter("conditionCode");
			if (conditionId != null) {
				clientInquiryElement.setConditionId(new Integer(conditionId));
			}
			clientInquiryElement.setUpdateTimestamp(new Date());
			if (!"".equals(aimPrice) && aimPrice != null) {
				clientInquiryElement.setAimPrice(aimPrice);
			}
			clientInquiryElementService.updateByPrimaryKeySelective(clientInquiryElement);
			success = true;
			message = "修改成功!";
		} catch (Exception e) {
			message = "修改失败!";
			e.printStackTrace();
		}
		
		return new EditRowResultVo(success, message);
		
	}
	
	/*
	 * 新增询价明细
	 */
	/**
	@Author: Modify by white
	@DateTime: 2018/9/17 16:16
	@Description: 修改询价为“7”开头的也进行爬虫
	*/
	@RequestMapping(value="/addElement",method=RequestMethod.POST)
	public @ResponseBody ResultVo addElement(HttpServletRequest request,@ModelAttribute ElementVo elementVo) {
		Integer clientInquiryId = new Integer(getString(request, "id"));
		HttpSession session = request.getSession();
		Object retoken=request.getParameter("token");
		Object setoken=session.getAttribute("token");
		String check = getString(request, "check");
		UserVo userVo = getCurrentUser(request);
		if(!check.equals("1")){
			if (elementVo.getList().size()>0) {
				session.setAttribute("token", retoken);
				ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryId);
				ResultVo resultVo = clientInquiryElementService.insertSelective(elementVo.getList(),clientInquiryId);
				Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
				List<ClientInquiryElement> list = clientInquiryElementService.findByclientInquiryId(clientInquiryId);
				//爬虫
				if (clientInquiry.getBizTypeId().equals(128) || clientInquiry.getBizTypeId() == 128 || client.getCode().startsWith("9") || client.getCode().startsWith("8") || client.getCode().startsWith("70") || client.getCode().startsWith("370") || client.getCode().startsWith("7")) {
					clientInquiryElementService.searchSatair(list,clientInquiryId,0,0,null);
					//clientInquiryElementService.getEmail(list,id);
				}
				session.removeAttribute("token");
				if (resultVo.isSuccess()) {
					clientInquiryElementService.matchElement(list);
				}
				//发送QQ邮件
				if ((client.getCode().startsWith("9") || userVo.getLoginName().toLowerCase().equals("kent")) && clientInquiry.getQqMailSend().equals(0) && list.size() > 0) {
					logger.info("创建询价明细--开始发邮件");
					boolean flag = clientInquiryElementService.QQEmail(list, clientInquiry,userVo);
//					System.out.println("打印邮件发送成功标志"+flag);
					if(flag){
						clientInquiry.setQqMailSend(1);
					}
					clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
				}else{
					logger.info("创建询价明细但是并不需要发邮件");
				}
				return resultVo;
			}
		}
		return new ResultVo(false, "请勿重复新增！");
	}
	
	/*
	 * 网页新增竞争对手
	 */
	@RequestMapping(value="/addTenderPrice",method=RequestMethod.GET)
	public String addTenderPrice(HttpServletRequest request) {
		Integer id = new Integer(getString(request, "id"));
		
		List<ElementVo> list = clientInquiryService.tenderElelment(id);
		request.setAttribute("list", list);
		return "/marketing/clientinquiry/addTenderTable";
	}
	
	/*
	 * 保存页面新增竞争对手
	 */
	@RequestMapping(value="/saveTender",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveTender(HttpServletRequest request,@ModelAttribute TenderVo tenderVo) {
		Integer clientInquiryId =  new Integer(getString(request, "id"));
		
		if (tenderVo.getCompetitorList().size()>0 && 
				tenderVo.getCompetitorQuoteElementList().size()>0 &&
				tenderVo.getCompetitorQuoteList().size()>0) {
			ResultVo resultVo = clientInquiryService.saveTender(tenderVo, clientInquiryId);
			return resultVo;
		}
		
		return new ResultVo(false, "新增失败！");
	}
	
	/**
	 * 交付方式
	 */
	@RequestMapping(value="/delivery",method=RequestMethod.POST)
	public @ResponseBody ResultVo delivery(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		Integer id = new Integer(getString(request, "id"));
		List<SystemCode> list = new ArrayList<SystemCode>();
		List<SystemCode> deList = systemCodeService.delivery();
		Client client = clientService.selectByPrimaryKey(id);
		if (client.getTermsOfDelivery() != null && !"".equals(client.getTermsOfDelivery())) {
			for (int i = 0; i < deList.size(); i++) {
				if (deList.get(i).getId().equals(client.getTermsOfDelivery())) {
					list.add(deList.get(i));
				}
			}
			for (int j = 0; j < deList.size(); j++) {
				if (!deList.get(j).getId().equals(client.getTermsOfDelivery())) {
					list.add(deList.get(j));
				}
			}
		}else {
			list = deList;
		}
		
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	/*@RequestMapping(value="/test",method=RequestMethod.POST)
	public @ResponseBody ResultVo test(HttpServletRequest request) {
		String message = "";
		boolean success = true;
		BarcodeConsumer barcodeConsumer = new BarcodeConsumer();
		barcodeConsumer.startConsume();
		return new ResultVo(success, message);
	}*/
	
	/*
	 * 黑名单列表
	 */
	@RequestMapping(value="/toisblacklist",method=RequestMethod.GET)
	public String toisblacklist(HttpServletRequest request) {
		request.setAttribute("partNumber", getString(request, "partNumber"));
		
		return "/purchase/supplierInquiry/isblackList";
	}
	
	/*
	 * 列表数据
	 */
	@RequestMapping(value="/isblacklistData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo isblacklistData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<BlackList> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		page.put("partNumberCode", getString(request, "partNumber"));
		 tPartService.blacklist(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (BlackList tPart : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(tPart);
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
	
	/*
	 * 明细修改
	 */
	@RequestMapping(value="/editisblacklist",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo editisblacklist(HttpServletRequest request,@ModelAttribute ClientInquiryElement clientInquiryElement){
		boolean success = false;
		String message = "";
		try {
			clientInquiryElement.setUpdateTimestamp(new Date());
			clientInquiryElementService.update(clientInquiryElement);
			success = true;
			message = "修改成功!";
		} catch (Exception e) {
			message = "修改失败!";
			e.printStackTrace();
		}
		
		return new EditRowResultVo(success, message);
		
	}
	
	/*
	 * 标注黑名单
	 */
	@RequestMapping(value="/editBlackList",method=RequestMethod.POST)
	public @ResponseBody ResultVo editBlackList(HttpServletRequest request){
		boolean success = false;
		String message = "";
	
		String blacklist = getString(request, "blacklist");
		String id=request.getParameter("id");
		TPart tPart=new TPart();
		String[] list = blacklist.split(",");
		try {
		for (String string : list) {
			tPart.setBsn(string);
			tPart.setIsBlacklist(1);
			tPartService.update(tPart);
		}
		ClientInquiryElement clientInquiryElement=clientInquiryElementService.selectByPrimaryKey(Integer.parseInt(id));
		if(clientInquiryElement.getIsBlacklist().equals(0)){
			clientInquiryElement.setIsBlacklist(1);
			clientInquiryElementService.update(clientInquiryElement);
		}
		success = true;
		message = "标注完成!";
		} catch (Exception e) {
			message = "标注失败!";
			e.printStackTrace();
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 预报价数据
	 * **/
	@RequestMapping(value="/weatherQuoteData",  method=RequestMethod.POST)
	public @ResponseBody ResultVo weatherQuoteData(HttpServletRequest request, @ModelAttribute SupplierQuote record)
	{
		boolean result = true;
		String message = "";
		String airTypeId=request.getParameter("airTypeId");
		String clientinquiryId=request.getParameter("clientinquiryId");
		String clientId=request.getParameter("clientId");
		message=airTypeId+"/"+clientinquiryId+"//"+clientId;
		return new ResultVo(result, message);
	}
	
	/**
	 * 客户预报价数据页面
	 */
	@RequestMapping(value="/weatherQuotePage",method=RequestMethod.GET)
	public String weatherQuotePage(HttpServletRequest request) {
		UserVo userVo = getCurrentUser(request);
		request.setAttribute("id", request.getParameter("id"));
		request.setAttribute("type", "marketing");
		request.setAttribute("userId", userVo.getUserId());
		return "/marketing/clientinquiry/weatherquotelist";
	}
	
	/**
	 * 客户预报价
	 */
	@RequestMapping(value="/weatherQuote",method=RequestMethod.GET)
	public String weatherQuote(HttpServletRequest request) {
		return "/purchase/supplierquote/airtype";
	}
	
	/**
	 * 动态列
	 * **/
	@RequestMapping(value = "/list/dynamicColNames", method = RequestMethod.POST)
	public @ResponseBody
	ColumnVo excelListDynamicCol(HttpServletRequest request,
			HttpServletResponse response) {
		ListDateVo vo=new ListDateVo();
		String id= request.getParameter("id");
		String[] a=id.split("//");
		String clientId=a[1];
		String[] b=a[0].split("/");
		String airTypeId =b[0];
		String clientinquiryId=b[1];
//		String s=b[0];
//		   String airTypeId = s.replace("a",",");
//		String d=b[1];
//		String clientinquiryId=d.replace("b", ",");
		vo.setAirTypeId(airTypeId);
		vo.setClientinquiryId(clientinquiryId);
		vo.setClientId(clientId);
		List<ClientInquiry> list=supplierQuoteService.findClientInquiry(vo);
		List<String> supplierCode=new ArrayList<String>();
		for (ClientInquiry clientInquiry : list) {
			List<ClientInquiry> slist=supplierQuoteService.findSupplierQuote(clientInquiry.getId(),clientInquiry.getItem());
			for (ClientInquiry supplierQuoteVo : slist) {
				if(null!=supplierQuoteVo.getSupplierCode()&&!supplierCode.contains(supplierQuoteVo.getSupplierCode())){
				supplierCode.add(supplierQuoteVo.getSupplierCode());
				}
			}
		}
		List<String> displayNames = new ArrayList<String>();
		List<String> colNames = new ArrayList<String>();
		Collections.sort(supplierCode);
		for(int i=0;i<supplierCode.size();i++){
			displayNames.add(supplierCode.get(i));
			colNames.add("supplier"+supplierCode.get(i));			
		}
		ColumnVo result = new ColumnVo();
		result.setColumnDisplayNames(displayNames);
		result.setColumnKeyNames(colNames);
		
		return result;
	}
	
	/**
	 * 错误列表
	 */
	@RequestMapping(value="/toErrorMessage",method=RequestMethod.GET)
	public String toErrorMessage(){
		return "/marketing/clientinquiry/errorlist";
	}
	
	/**
	 * 错误列表数据
	 */
	@RequestMapping(value="/errorMessage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo errorMessage(HttpServletRequest request) {
		PageModel<ClientInquiryElementUpload> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo = getCurrentUser(request);
		page.put("userId", userVo.getUserId());
		
		clientInquiryService.getErrorPage(page);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientInquiryElementUpload clientInquiryElementUpload : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(clientInquiryElementUpload);
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
	 * 删除错误信息
	 */
	@RequestMapping(value="/deleteMessage",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteMessage(HttpServletRequest request) {
		UserVo userVo = getCurrentUser(request);
		clientInquiryService.deleteError(new Integer(userVo.getUserId()));
		return new ResultVo(true, "删除成功！");
	}
	
	/**
	 * 检查是否已新增报价单
	 */
	@RequestMapping(value="/checkQuote",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkQuote(HttpServletRequest request) {
		String message = "需要选择的询价单都已生成报价单！";
		boolean success = false;
		String ywId = getString(request, "id");
		String[] c=ywId.split("//");
		String clientId=c[1];
		String[] d=c[0].split("/");
		String airTypeId =d[0];
		String clientinquiryIds=d[1];
		String[] clientinquiryId = clientinquiryIds.split(",");
		int ifQuote = 1;
		for (int i = 0; i < clientinquiryId.length; i++) {
			Integer clientCurrencyId = clientQuoteService.getCurrencyId(new Integer(clientinquiryId[i]));
			if (clientCurrencyId==null) {
				ifQuote = 0;
				break;
			}
		}
		if (ifQuote!=0) {
			success = true;
		}
		
		return new ResultVo(success, message);
	}
	
	/*
	 * 检查机型并新增询价单
	 */
	@RequestMapping(value="/checkAirType",method=RequestMethod.POST)
	public @ResponseBody ResultVo checkAirType(HttpServletRequest request){
		String message = "";
		UserVo userVo = getCurrentUser(request);
		Integer id = new Integer(getString(request, "id"));
//		boolean success = clientInquiryService.sendEmail(id) || clientInquiryService.supplierCommissionSale(id);
		
//		if (success) {
//			message = "邮件发送成功！";
//		}else {
//			message = "邮件发送失败！";
//		}
		
		return new ResultVo(true, message);
	}
	
	/*
	 * 增加明细页面
	 */
	@RequestMapping(value="/addelementafteradd",method=RequestMethod.GET)
	public String addelementafteradd(HttpServletRequest request) {
		String id = getString(request, "id");
		ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(new Integer(id));
		SystemCode systemCode = systemCodeService.selectByPrimaryKey(clientInquiry.getBizTypeId());
		request.setAttribute("id", id);
		request.setAttribute("exchange", systemCode.getCode());
		return "/marketing/clientinquiry/addelementafteradd";
	}
	
	/*
	 * 明细列表数据
	 */
	@RequestMapping(value="/elementlistData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementlistData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientInquiryElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String beginWith = request.getParameter("beginWith");
		if ("".equals(where)) {
			where = null;
		}
		if (!"".equals(beginWith) && beginWith != null) {
			beginWith = "^"+beginWith;
			page.put("beginWith", beginWith);
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		String manageStatus = getString(request, "manageStatus");
		if (manageStatus != null && !"".equals(manageStatus)) {
			if (manageStatus.equals("1")) {
				page.put("quoteStatus", "and cqe.id IS NOT NULL");
			}else {
				page.put("quoteStatus", "and cqe.id IS NULL");
			}
			
		}
		//根据代码查询
		String code = getString(request, "code");
		if (code != null && !"".equals(code)) {
			Supplier supplier = supplierService.selectByCode(code);
			if (supplier != null) {
				page.put("code", code);
				if (where != null && !"".equals(where)) {
					where = where + " and s.code = '"+code+"'";
				}else {
					where = "s.code = '"+code+"'";
				}
			}else {
				if (where != null && !"".equals(where)) {
					where = where + " and c.code = '"+code+"'";
				}else {
					where = "c.code = '"+code+"'";
				}
			}
		}
		clientInquiryElementService.elementPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientInquiryElement clientInquiryElement : page.getEntities()) {
				ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
				Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
				clientInquiryElement.setClientId(client.getId());
				Map<String, Object> map = EntityUtil.entityToTableMap(clientInquiryElement);
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
	 * 历史报价客户询价单明细数据
	 */
	@RequestMapping(value="/elementlistDataForMarketHis",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementlistDataForMarketHis(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientInquiryElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String beginWith = request.getParameter("beginWith");
		if ("".equals(where)) {
			where = null;
		}
		if (!"".equals(beginWith) && beginWith != null) {
			beginWith = "^"+beginWith;
			page.put("beginWith", beginWith);
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		String manageStatus = getString(request, "manageStatus");
		if (manageStatus != null && !"".equals(manageStatus)) {
			if (manageStatus.equals("1")) {
				page.put("quoteStatus", "and cqe.id IS NOT NULL");
			}else {
				page.put("quoteStatus", "and cqe.id IS NULL");
			}
			
		}
		//根据代码查询
		String code = getString(request, "code");
		if (code != null && !"".equals(code)) {
			Supplier supplier = supplierService.selectByCode(code);
			if (supplier != null) {
				page.put("code", code);
				if (where != null && !"".equals(where)) {
					where = where + " and s.code = '"+code+"'";
				}else {
					where = "s.code = '"+code+"'";
				}
			}else {
				if (where != null && !"".equals(where)) {
					where = where + " and c.code = '"+code+"'";
				}else {
					where = "c.code = '"+code+"'";
				}
			}
		}
		clientInquiryElementService.elementPageForMarketHis(page, where, sort);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			int row = 1;
			for (ClientInquiryElement clientInquiryElement : page.getEntities()) {
				ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
				Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
				clientInquiryElement.setClientId(client.getId());
				clientInquiryElement.setRow(row);
				if (clientInquiryElement.getCurrencyId() != null && clientInquiryElement.getSupplierCurrencyId() != null) {
					if (!clientInquiryElement.getCurrencyId().equals(clientInquiryElement.getSupplierCurrencyId())) {
						if (client.getCode().startsWith("7")) {
							ExchangeRate exchangeRate = exchangeRateService.selectByPrimaryKey(clientInquiryElement.getSupplierCurrencyId());
							ExchangeRate usRate = exchangeRateService.selectByPrimaryKey(11);
							Double rate = clientQuoteService.caculatePrice(new BigDecimal(exchangeRate.getRate()), new BigDecimal(usRate.getRate()), new BigDecimal(usRate.getRelativeRate())).doubleValue();
						}else {
							Double price = clientQuoteService.caculatePrice(new BigDecimal(clientInquiryElement.getSupplierQuotePrice()), new BigDecimal(clientInquiryElement.getSupplierExchangeRate()), new BigDecimal(clientInquiryElement.getExchangeRate())).doubleValue();
							clientInquiryElement.setSupplierAndPrice(clientInquiryElement.getSupplierCode()+" - "+price);
						}
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientInquiryElement);
				mapList.add(map);
				row++;
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
	 * 采购历史报价询价明细
	 */
	@RequestMapping(value="/elementlistDataForHis",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo elementlistDataForHis(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientInquiryElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String beginWith = request.getParameter("beginWith");
		String purchase = request.getParameter("purchase");
		if (purchase != null) {
			page.put("purchase", purchase);
		}
		if ("".equals(where)) {
			where = null;
		}
		if (!"".equals(beginWith) && beginWith != null) {
			beginWith = "^"+beginWith;
			page.put("beginWith", beginWith);
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		String manageStatus = getString(request, "manageStatus");
		if (manageStatus != null && !"".equals(manageStatus)) {
			if (manageStatus.equals("1")) {
				page.put("quoteStatus", "and cqe.id IS NOT NULL");
			}else {
				page.put("quoteStatus", "and cqe.id IS NULL");
			}
			
		}
		//根据代码查询
		String code = getString(request, "code");
		if (code != null && !"".equals(code)) {
			Supplier supplier = supplierService.selectByCode(code);
			if (supplier != null) {
				page.put("code", code);
				if (where != null && !"".equals(where)) {
					where = where + " and s.code = '"+code+"'";
				}else {
					where = "s.code = '"+code+"'";
				}
			}else {
				if (where != null && !"".equals(where)) {
					where = where + " and c.code = '"+code+"'";
				}else {
					where = "c.code = '"+code+"'";
				}
			}
		}
		
		clientInquiryElementService.elementPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ClientInquiryElement clientInquiryElement : page.getEntities()) {
				ClientInquiry clientInquiry = clientInquiryService.selectByPrimaryKey(clientInquiryElement.getClientInquiryId());
				Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
				clientInquiryElement.setClientId(client.getId());
				List<TPart> list = tPartService.selectByPart(clientInquiryElement.getPartNumber());
				if (list.size() > 0) {
					if (list.get(0) != null) {
						if (list.get(0).getAtaChapterSection() != null) {
							clientInquiryElement.setAta(list.get(0).getAtaChapterSection().toString());
						}
						if (list.get(0).getWeight() != null) {
							clientInquiryElement.setWeight(list.get(0).getWeight());
						}
						if (list.get(0).getDimentions() != null) {
							clientInquiryElement.setDimentions(list.get(0).getDimentions());
						}
						
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(clientInquiryElement);
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
	 * 列表数据
	 */
	@RequestMapping(value="/getDataOnce",method=RequestMethod.POST)
	public @ResponseBody ResultVo getDataOnce(HttpServletRequest request,HttpServletResponse response) {
		PageModel<ClientInquiryElement> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		clientInquiryElementService.getDataOnce(page, where, sort);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			for (ClientInquiryElement clientInquiryElement : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(clientInquiryElement);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			String exportModel = getString(request, "exportModel");

		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		JSONArray json = new JSONArray();
		json.add(mapList);

		return new ResultVo(true, json.toString());
	}
	
	
	
	/**
	 * 修改历史报价提交状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateStatus",method=RequestMethod.POST)
	public @ResponseBody ResultVo updateStatus(HttpServletRequest request){
		String where = request.getParameter("where");
		boolean success = false;
		PageModel<ClientInquiryElement> page = new PageModel<ClientInquiryElement>();
		if (where != null && !"".equals(where)) {
			page.put("where", where);
			success = clientInquiryElementService.updateStatus(page);
			if (success) {
				return new ResultVo(success, "更新成功！");
			}else {
				return new ResultVo(success, "更新失败！");
			}
		}else {
			return new ResultVo(success, "更新失败！");
		}
	}
	
	
	/**
	 * 增加目标价
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addAimPrice",method=RequestMethod.POST)
	public @ResponseBody ResultVo addAimPrice(HttpServletRequest request){
		try {
			String id = request.getParameter("id");
			String aimPrice = request.getParameter("aimPrice");
			if (id != null && aimPrice != null) {
				ClientInquiryElement clientInquiryElement = clientInquiryElementService.selectByPrimaryKey(new Integer(id));
				clientInquiryElement.setAimPrice(aimPrice);
				clientInquiryElementService.updateByPrimaryKeySelective(clientInquiryElement);
				return new ResultVo(true, "修改成功！");
			}else {
				return new ResultVo(true, "修改失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "修改失败！");
		}
	}
	
	/**
	 * 售前状态
	 */
	@RequestMapping(value="/elementStatus",method=RequestMethod.POST)
	public @ResponseBody ResultVo elementStatus(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> list = systemCodeService.findType("ELEMENT_STATUS");
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}
	
	
/*	
	 * 查询所有客户代码
	 
	@RequestMapping(value="/clientCodeByUser",method=RequestMethod.POST)
	public @ResponseBody ResultVo clientCodeByUser(HttpServletRequest request) {
		boolean success = false;
		String msg = "";
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		PageModel<Client> page = getPage(request);
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务") && !roleVo.getRoleName().equals("物流")) {
			page.put("userId", userVo.getUserId());
		}
		List<Client> list=clientService.findAll(page);
		success = true;
		JSONArray json = new JSONArray();
		json.add(list);
		msg =json.toString();
		return new ResultVo(success, msg);
	}*/
	
	@RequestMapping(value="/toSwf",method=RequestMethod.GET)
	public String toSwf(HttpServletRequest request){
		//request.setAttribute("swfpath", "D:\\CRM\\Files\\mis\\excel\\sampleoutput\\ORD-18001141_SupplierOrder_testuser_20180321_171442.xls");
		return "/marketing/clientinquiry/viewpdf";
	}
	
	/**
	 * test接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/testInterface",method=RequestMethod.GET)
	public @ResponseBody ResultVo testInterface(HttpServletRequest request){
		logger.error("接口1访问成功！");
		return new ResultVo(true, "接口1访问成功！");
	}
	
	/**
	 * test接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/testInterface2",method=RequestMethod.GET)
	public @ResponseBody ResultVo testInterface2(HttpServletRequest request){
		logger.error("接口2访问成功！");
		return new ResultVo(true, "接口2访问成功！");
	}
	
	/**
	 * 跳转copy excel功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pasteExcel",method=RequestMethod.GET)
	public String pasteExcel(HttpServletRequest request){
		return "/marketing/historyquote/pasteTable";
	}
	
	/**
	 * 整理数据
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value="/transData",method=RequestMethod.POST)
	public @ResponseBody ResultVo transData(HttpServletRequest request,@ModelAttribute List<ClientInquiryElement> list){
		try {
			for
		} catch (Exception e) {
			// TODO: handle exception
		}
	}*/
	
	@RequestMapping(value="/inquiryTable",method=RequestMethod.GET)
	public String inquiryTable(HttpServletRequest request){
		request.setAttribute("today", new Date());
		return "/marketing/historyquote/addInquiryTable";
	}
	
	/**
	 * 新增询价单以及询价明细
	 * @return
	 */
	@RequestMapping(value="/addInquiry",method=RequestMethod.POST)
	public @ResponseBody ResultVo addInquiry(HttpServletRequest request,@ModelAttribute ClientInquiry clientInquiry){
		try {
			UserVo userVo = getCurrentUser(request);
			clientInquiry.setUpdateTimestamp(new Date());
			String a[]=clientInquiry.getMixId().split(",");
			clientInquiry.setClientContactId(new Integer(a[0]));
			clientInquiry.setClientId(new Integer(a[1]));
			clientInquiry.setCreateUser(new Integer(userVo.getUserId()));
			clientInquiry.setQqMailSend(0);
			ClientInquiry afterInsert = clientInquiryService.add(clientInquiry);
			ResultVo resultVo = clientInquiryElementService.insertForCopyTable(clientInquiry.getList(),clientInquiry.getId());
			Client client = clientService.selectByPrimaryKey(clientInquiry.getClientId());
			List<ClientInquiryElement> list = clientInquiryElementService.findByclientInquiryId(clientInquiry.getId());
			//爬虫
			if (clientInquiry.getBizTypeId().equals(128) || clientInquiry.getBizTypeId() == 128 || client.getCode().startsWith("9") || client.getCode().startsWith("8") || client.getCode().startsWith("70") || client.getCode().startsWith("370")) {
				clientInquiryElementService.searchSatair(list,clientInquiry.getId(),0,0,null);
				//clientInquiryElementService.getEmail(list,id);
			}
			if (resultVo.isSuccess()) {
				clientInquiryElementService.matchElement(list);
			}
			//发送QQ邮件
			if ((client.getCode().startsWith("9") || userVo.getLoginName().toLowerCase().equals("kent")) && afterInsert.getQqMailSend().equals(0) && list.size() > 0) {
				logger.info("创建询价明细--开始发邮件");
				boolean flag = clientInquiryElementService.QQEmail(list, clientInquiry,userVo);
//				System.out.println("打印邮件发送成功标志"+flag);
				if(flag){
					clientInquiry.setQqMailSend(1);
				}
				clientInquiryService.updateByPrimaryKeySelective(clientInquiry);
			}else{
				logger.info("创建询价明细但是并不需要发邮件");
			}
			resultVo.setMessage(resultVo.getMessage()+","+clientInquiry.getQuoteNumber());
			return resultVo;
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "新增异常！");
		}
	}
	
	
	@RequestMapping(value="/getConditions",method=RequestMethod.POST)
	public @ResponseBody ResultVo getConditions(HttpServletRequest request){
		try {
			List<SystemCode> systemCodes = systemCodeService.findType("COND");
			/*String conditionCode = request.getParameter("conditionCode");
			List<SystemCode> list = new ArrayList<SystemCode>();
			for (int i = 0; i < systemCodes.size(); i++) {
				if (systemCodes.get(i).getCode().equals(conditionCode)) {
					list.add(systemCodes.get(i));
					break;
				}
			}
			for (int i = 0; i < systemCodes.size(); i++) {
				if (!systemCodes.get(i).getCode().equals(conditionCode)) {
					list.add(systemCodes.get(i));
				}
			}*/
			StringBuffer value = new StringBuffer();
			for (SystemCode listDateVo : systemCodes) {
				value.append(listDateVo.getId()).append(":").append(listDateVo.getCode()).append(";");
			}
			value.deleteCharAt(value.length()-1);
			return new ResultVo(true, value.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo(false, "");
		}
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
