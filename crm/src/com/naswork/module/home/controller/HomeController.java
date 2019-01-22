package com.naswork.module.home.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.dao.ClientOrderDao;
import com.naswork.dao.ClientOrderElementDao;
import com.naswork.dao.ClientWeatherOrderDao;
import com.naswork.dao.ClientWeatherOrderElementDao;
import com.naswork.dao.ImportPackageDao;
import com.naswork.dao.ImportPackageElementDao;
import com.naswork.dao.ImportPackagePaymentDao;
import com.naswork.dao.ImportPackagePaymentElementDao;
import com.naswork.dao.Jbpm4TaskDao;
import com.naswork.filter.ContextHolder;
import com.naswork.model.ClientOrder;
import com.naswork.model.ClientWeatherOrder;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackageElement;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportPackagePaymentElement;
import com.naswork.model.Jbpm4Task;
import com.naswork.module.marketing.controller.clientorder.ClientOrderElementVo;
import com.naswork.service.FlowService;
import com.naswork.service.RoleService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController{

	@Autowired
	private FlowService flowService;
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private Jbpm4TaskDao jbpm4TaskDao;
	@Resource
	private ClientOrderDao clientOrderDao;
	@Resource
	private ClientOrderElementDao clientOrderElementDao;
	@Resource
	private ImportPackagePaymentDao importPackagePaymentDao;
	@Resource
	private ImportPackageDao importPackageDao;
	@Resource
	private ImportPackagePaymentElementDao importPackagePaymentElementDao;
	@Resource
	private ImportPackageElementDao importPackageElementDao;
	@Resource
	private ClientWeatherOrderDao clientWeatherOrderDao;
	@Resource
	private ClientWeatherOrderElementDao clientWeatherOrderElementDao;

	
	/**
	 * 我的主页
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request)
	{
		return "/home/index";
	}
	
	/**
	 * 个人任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/personalTask")
	public String personalTask(HttpServletRequest request)
	{
		UserVo userVo = ContextHolder.getCurrentUser();
		List<RoleVo> roles = userService.searchRoleByUserId(userVo.getUserId());
		if (roles.size() > 0) {
			request.setAttribute("role", roles.get(0).getRoleName());
		}
		request.setAttribute("userId", userVo.getUserName());
		
		List<Integer> leaders = userService.getLeadersByRole(new Integer(roles.get(0).getRoleId()));
		boolean isLeader = false;
		if (roles.get(0).getRoleName().equals("管理员")) {
			isLeader = true;
		}else {
			for (int i = 0; i < leaders.size(); i++) {
				if (leaders.get(i).equals(new Integer(userVo.getUserId()))) {
					isLeader = true;
				}
			}
		}
		request.setAttribute("isLeader", isLeader);
		return "/home/personalTask";
	}
	
	/**
	 * 组任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/groupTask")
	public String groupTask(HttpServletRequest request)
	{
		return "/home/groupTask";
	}
	
	/**
	 * 组任务数据
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/groupTaskData", method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo groupTaskData(HttpServletRequest request)
	{
		PageModel page = getPage(request);
		Map<String, String> map = new HashMap<String, String>();
		
		flowService.findGroupTaskListPage(page, map, getSort(request));
		
		JQGridMapVo vo = new JQGridMapVo();
		vo.setRows(page.getEntities());
		vo.setPage(page.getPageNo());
		vo.setTotal(page.getPageCount());
		vo.setRecords(page.getRecordCount());
		
		return vo;
	}
	
	/*
	 * 列表页面数据
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo listData(HttpServletRequest request,HttpServletResponse response) {
		PageModel<Jbpm4Task> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		String where = request.getParameter("searchString");
		page.put("userId", userVo.getUserId());
		 List<Jbpm4Task> entities=jbpm4TaskDao.groupTaskData(page);
	
			page.setEntities(entities);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Jbpm4Task jbpm4Task : page.getEntities()) {
				String processId=jbpm4Task.getExecutionId();
				String[] processIds=processId.split("\\.");
				String tableName=processIds[1];
				jbpm4Task.setTableName(tableName);
				if(tableName.equals("IMPORT_PACKAGE_PAYMENT_ELEMENT")){
					ImportPackagePayment importPackagePayment=importPackagePaymentDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
					jbpm4Task.setDeploymentName("付款申请");
					jbpm4Task.setTaskNumber(importPackagePayment.getPaymentNumber());
				}else if(tableName.equals("ORDER_APPROVAL")){
//					ClientOrder clientOrder=clientOrderDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
//					jbpm4Task.setDeploymentName("合同评审");
//					if(null==clientOrder){
						 ClientWeatherOrder clientWeatherOrder=clientWeatherOrderDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
							jbpm4Task.setTaskNumber(clientWeatherOrder.getOrderNumber());
							jbpm4Task.setDeploymentName("合同评审");
//					}else{
//						jbpm4Task.setTaskNumber(clientOrder.getSourceNumber());
//					}
				}else if(tableName.equals("IMPORT_PACKAGE_ELEMENT")){
					ImportPackage importPackage=importPackageDao.selectByPrimaryKey(jbpm4Task.getYwTableId());
					jbpm4Task.setDeploymentName("异常件审批");
					jbpm4Task.setTaskNumber(importPackage.getImportNumber());
				}
				 List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.groupSubTaskData(jbpm4Task.getYwTableId().toString(),userVo.getUserId());
				 
				  String ids=jbpm4Tasks.get(0).getId();
				  for (int i = 1; i < jbpm4Tasks.size(); i++) {
						ids+=","+jbpm4Tasks.get(i).getId();
				}
				 jbpm4Task.setIds(ids);
				Map<String, Object> map = EntityUtil.entityToTableMap(jbpm4Task);
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
	 * 我的任务子任务
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/subTask", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> subTask(HttpServletRequest request)
	{
		Map<String, Object> m = new HashMap<String, Object>();
		UserVo user = getCurrentUser(request);
		GridSort sort = getSort(request);
		
		Map<String, String> param = new HashMap<String, String>();
		String ywTableId=request.getParameter("ywTableId");
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		int number=1;
		 List<Jbpm4Task> jbpm4Tasks=jbpm4TaskDao.groupSubTaskData(ywTableId,user.getUserId());
		 for (Jbpm4Task jbpm4Task : jbpm4Tasks) {
				Map<String, Object> cellMap = new HashMap<String, Object>();
				String processId=jbpm4Task.getExecutionId();
				String[] processIds=processId.split("\\.");
				String tableName=processIds[1];
				if(tableName.equals("IMPORT_PACKAGE_PAYMENT_ELEMENT")){
					ImportPackagePaymentElement importPackagePaymentElement=importPackagePaymentElementDao.elementData(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
					String[] cell = {jbpm4Task.getId(),
					importPackagePaymentElement.getPartNumber(),
					importPackagePaymentElement.getShouldPay().toString(),
					importPackagePaymentElement.getAmount().toString(),
					importPackagePaymentElement.getPaymentPercentage().toString(),
					importPackagePaymentElement.getRemark()
					};
					cellMap.put("cell", cell);
				}else if(tableName.equals("ORDER_APPROVAL")){
//					ClientOrderElementVo clientOrderElement=clientOrderElementDao.findByclientOrderELementId(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
//					if(null==clientOrderElement){
					ClientOrderElementVo clientOrderElement=clientWeatherOrderElementDao.findByclientOrderELementId(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
//					}
					String[] cell = {jbpm4Task.getId(),
							number+"",
							clientOrderElement.getItem().toString(),
							clientOrderElement.getQuotePartNumber(),
					clientOrderElement.getQuoteDescription(),
					clientOrderElement.getQuoteUnit(),
					clientOrderElement.getClientOrderAmount().toString()
					};
					cellMap.put("cell", cell);
				}else if(tableName.equals("IMPORT_PACKAGE_ELEMENT")){
					ImportPackageElement importPackageElement=importPackageElementDao.selectByPrimaryKey(Integer.parseInt(jbpm4Task.getYwTableElementId().toString()));
					String[] cell = {jbpm4Task.getId(),
							importPackageElement.getPartNumber(),
							importPackageElement.getDescription(),
							importPackageElement.getUnit(),
					importPackageElement.getAmount().toString(),
					importPackageElement.getRemark()
					};
					cellMap.put("cell", cell);
				}
				number++;
//			cellMap.put("id", i);
			mapList.add(cellMap);
		}
		
		m.put("rows", mapList);
		m.put("total", 1);
		m.put("page", 1);
		m.put("records", 1);		
		return m;
	}
	
	/**
	 * 拾取任务
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/takeTask", method=RequestMethod.POST)
	public @ResponseBody ResultVo takeTask(HttpServletRequest request)
	{
		String taskIds = getString(request, "taskIds");
		String message = "";
		boolean result = false;
		try {
			flowService.claimTask(taskIds);
			result = true;
			message = "拾取任务成功！";
		} catch (Exception e) {
			e.printStackTrace();
			message = "拾取任务失败！";
		}
		return new ResultVo(result, message);
	}
}
