package com.naswork.module.marketing.controller.partsinformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.controller.BaseController;
import com.naswork.module.marketing.controller.clientinquiry.ClientInquiryVo;
import com.naswork.service.ClientInquiryElementService;
import com.naswork.service.ClientInquiryService;
import com.naswork.service.StaticClientQuotePriceService;
import com.naswork.service.SupplierCageRelationService;
import com.naswork.service.SupplierPnRelationService;
import com.naswork.service.UserService;
import com.naswork.utils.EntityUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder.*;

@Controller
@RequestMapping(value="/market/partsinformation")
public class PartsInformationController extends BaseController{

	@Resource
	private ClientInquiryElementService clientInquiryElementService;
	@Resource
	private UserService userService;
	@Resource
	private ClientInquiryService clientInquiryService;
	@Resource
	private StaticClientQuotePriceService staticClientQuotePriceService;
	@Resource
	private SupplierPnRelationService supplierPnRelationService;
	@Resource
	private SupplierCageRelationService supplierCageRelationService;
	
	/*
	 * 销售部件资料页面
	 */
	@RequestMapping(value="/tomarketinformation",method=RequestMethod.GET)
	public String tomarketinformation(HttpServletRequest request) {
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			request.setAttribute("userId", getCurrentUser(request).getUserId());
		}else {
			request.setAttribute("userId", "null");
		}
		
		return "/marketing/partsinformation/marketPartsInformationList";
	}
	
	/*
	 * 采购部件资料
	 */
	@RequestMapping(value="/topurchaseinformation",method=RequestMethod.GET)
	public String topurchaseinformation(HttpServletRequest request) {
		UserVo userVo=getCurrentUser(request);
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			request.setAttribute("userId", getCurrentUser(request).getUserId());
		}else {
			request.setAttribute("userId", "null");
		}
		return "/purchase/partsinformation/purchasePartsInformationList";
	}
	
	/*
	 * 销售部件资料列表
	 */
	/*@RequestMapping(value="/marketinformation",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo marketinformation(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartsInformationVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
 		String[] wheres = where.split(" ");
		StringBuffer whereString = new StringBuffer();
		for (int i = 0; i < wheres.length; i++) {
			if (wheres[i].equals("e.PART_NUMBER_CODE")) {
				wheres[i] = "(e.PART_NUMBER_CODE";
				StringBuffer codeString = new StringBuffer();
				String partNumber = wheres[i+2];
				String code = clientInquiryService.getCodeFromPartNumber(partNumber);
				if (wheres[i+1].equals("=")) {
					codeString.append("'").append(code).append("'").append(" OR ciiee.PART_NUMBER_CODE = '").append(code).append("'").append(")");
				}else if (wheres[i+1].equals("like")) {
					codeString.append("'%").append(code).append("%'").append("OR ciiee.PART_NUMBER_CODE like '%").append(code).append("%'").append(")");
				}
				wheres[i+2] = codeString.toString();
			}
			whereString.append(wheres[i]).append(" ");
		}
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		clientInquiryElementService.marketpartPage(page, whereString.toString(), sort);
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartsInformationVo partsInformationVo : page.getEntities()) {
				List<Integer> checkList = new ArrayList<Integer>();
				if (partsInformationVo.getMainId()!=null && !checkList.contains(partsInformationVo.getMainId())) {
					checkList.add(partsInformationVo.getMainId());
					PageModel<PartsInformationVo> pageModel = new PageModel<PartsInformationVo>();
					pageModel.put("where", "cie.id = "+partsInformationVo.getMainId());
					List<PartsInformationVo> partList = clientInquiryElementService.marketPartInformation(pageModel);
					for (PartsInformationVo partsInformationVo2 : partList) {
						Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo2);
						if (!mapList.contains(map)) {
							mapList.add(map);
						}
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
				if (!mapList.contains(map)) {
					mapList.add(map);
				}
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("部件资料", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		

		return jqgrid;
	}*/
	
/*	
	 * 销售部件资料列表
	 
	@RequestMapping(value="/marketinformation",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo marketinformation(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartsInformationVo> page1=getPage(request);
		PageModel<PartsInformationVo> page2=getPage(request);
		PageModel<PartsInformationVo> page3=getPage(request);
		PageModel<PartsInformationVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
 		String[] wheres = where.split(" ");
		StringBuffer whereString = new StringBuffer();
		for (int i = 0; i < wheres.length; i++) {
			if (wheres[i].equals("e.PART_NUMBER_CODE")) {
				wheres[i] = "(e.PART_NUMBER_CODE";
				StringBuffer codeString = new StringBuffer();
				String partNumber = wheres[i+2];
				String code = clientInquiryService.getCodeFromPartNumber(partNumber);
				if (wheres[i+1].equals("=")) {
					codeString.append("'").append(code).append("'").append(" OR ciiee.PART_NUMBER_CODE = '").append(code).append("'").append(")");
				}else if (wheres[i+1].equals("like")) {
					codeString.append("'%").append(code).append("%'").append("OR ciiee.PART_NUMBER_CODE like '%").append(code).append("%'").append(")");
				}
				wheres[i+2] = codeString.toString();
			}
			whereString.append(wheres[i]).append(" ");
		}
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page1.put("userId", userVo.getUserId());
			page2.put("userId", userVo.getUserId());
			page3.put("userId", userVo.getUserId());
		}
		clientInquiryElementService.marketpartPage(page1, whereString.toString(), sort);
		clientInquiryElementService.marketpartCoeNullPage(page2, whereString.toString(), sort);
		clientInquiryElementService.marketpartAllNullPage(page3, whereString.toString(), sort);
		//拼装列表数据
		if (page1.getPageSize()>=page1.getPageCount()) {
			//有订单的数据但是不够20条的情况
			if (page1.getPageNo()==page1.getPageCount() && page1.getEntities().size()<20) {
				for (int i = 0; i < 20-page1.getEntities().size(); i++) {
					page1.getEntities().add(page2.getEntities().get(i));
				}
			//页数超过了有订单的数据以后拼接上有报价的数据
			}else if (page1.getPageNo()<page2.getPageCount()) {
				for (int i = page1.getPageNo()*20; i < wheres.length; i++) {
					
				}
			}
		}
		if (page1.getRecordCount() == 0) {
			page1.setPageCount(0);
		}
		if (page2.getRecordCount() == 0) {
			page2.setPageCount(0);
		}
		if (page3.getRecordCount() == 0) {
			page3.setPageCount(0);
		}
		if ((page1.getPageNo()>page1.getPageCount() && page1.getPageNo()<=page2.getPageCount()+page1.getPageCount() && page2.getRecordCount() > 0) ) {
				//|| (page1.getRecordCount() == 0 && page2.getRecordCount() > 0)) {
			page2.setPageNo(page1.getPageNo()-page1.getPageCount());
			clientInquiryElementService.marketpartCoeNullPage(page2, whereString.toString(), sort);
				page.setEntities(page2.getEntities());
		}else if ((page1.getPageNo()>page2.getPageCount()+page1.getPageCount() && page3.getRecordCount() > 0)
				|| (page1.getRecordCount() == 0 && page2.getRecordCount() == 0 && page3.getRecordCount() > 0)) {
			page3.setPageNo(page1.getPageNo()-page1.getPageCount()-page2.getPageCount());
			clientInquiryElementService.marketpartAllNullPage(page3, whereString.toString(), sort);
				page.setEntities(page3.getEntities());
		}else if (page1.getRecordCount() > 0){
			page.setEntities(page1.getEntities());
		}
		
		page.setRecordCount(page1.getRecordCount()+page2.getRecordCount()+page3.getRecordCount());
		page.setPageCount(page1.getPageCount()+page2.getPageCount()+page3.getPageCount());
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartsInformationVo partsInformationVo : page.getEntities()) {
				int count = staticClientQuotePriceService.findByClientId(partsInformationVo.getClientId());
				if (count != 0) {
					partsInformationVo.setSupplierBasePrice(null);
				}
				List<Integer> checkList = new ArrayList<Integer>();
				if (partsInformationVo.getMainId()!=null && !checkList.contains(partsInformationVo.getMainId())) {
					checkList.add(partsInformationVo.getMainId());
					PageModel<PartsInformationVo> pageModel = new PageModel<PartsInformationVo>();
					pageModel.put("where", "cie.id = "+partsInformationVo.getMainId());
					List<PartsInformationVo> partList = clientInquiryElementService.marketPartInformation(pageModel);
					for (PartsInformationVo partsInformationVo2 : partList) {
						Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo2);
						if (!mapList.contains(map)) {
							mapList.add(map);
						}
					}
				}
				Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
				if (!mapList.contains(map)) {
					mapList.add(map);
				}
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				List<Map<String, Object>> exportList = new ArrayList<Map<String, Object>>();
				for (PartsInformationVo partsInformationVo : page1.getEntities()) {
					Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
					exportList.add(map);
				}
				for (PartsInformationVo partsInformationVo : page2.getEntities()) {
					Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
					exportList.add(map);				
				}
				for (PartsInformationVo partsInformationVo : page3.getEntities()) {
					Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
					exportList.add(map);
				}
				try {
					exportService.exportGridToXls("部件资料", exportModel, exportList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		

		return jqgrid;
	}*/
	
	/*
	 * 销售部件资料列表
	 */
	@RequestMapping(value="/marketinformation",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo marketinformation(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartsInformationVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
 		String[] wheres = where.split(" ");
		StringBuffer whereString = new StringBuffer();
		for (int i = 0; i < wheres.length; i++) {
			if (wheres[i].equals("e.PART_NUMBER_CODE")) {
				wheres[i] = "(cie.SHORT_PART_NUMBER";
				StringBuffer codeString = new StringBuffer();
				String partNumber = wheres[i+2];
				String code = clientInquiryService.getCodeFromPartNumber(partNumber);
				if (wheres[i+1].equals("=")) {
					codeString.append("'").append(code).append("'").append(" OR ciie.SHORT_PART_NUMBER = '").append(code).append("'").append(")");
				}else if (wheres[i+1].equals("like")) {
					codeString.append("'").append(code).append("%'").append("OR ciie.SHORT_PART_NUMBER like ").append("'%").append(code).append("%'").append(")");
				}
				wheres[i+2] = codeString.toString();
			}
			whereString.append(wheres[i]).append(" ");
		}
		if ("".equals(where)) {
			where = null;
		}
		/*RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}*/
		clientInquiryElementService.marketpartPage(page, whereString.toString(), sort);
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			List<Integer> cieIds = new ArrayList<Integer>();
			/*for (PartsInformationVo partsInformationVo : page.getEntities()) {
				if (partsInformationVo.getMainId()!=null && !cieIds.contains(partsInformationVo.getMainId())) {
					cieIds.add(partsInformationVo.getId());
				}
			}
			page.setRecordCount(page.getRecordCount()+cieIds.size());
			for (int i = 0; i < cieIds.size(); i++) {
				PageModel<PartsInformationVo> pageModel = new PageModel<PartsInformationVo>();
				pageModel.put("where", "cie.id = "+cieIds.get(i));
				List<PartsInformationVo> partList = clientInquiryElementService.marketPartInformation(pageModel);
				for (PartsInformationVo partsInformationVo2 : partList) {
					page.getEntities().add(partsInformationVo2);
				}
			}*/
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			for (PartsInformationVo partsInformationVo : page.getEntities()) {
					Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
					mapList.add(map);
			}
			
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("部件资料", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		

		return jqgrid;
	}
	
	
	/*
	 * 采购部件资料列表
	 */
	@RequestMapping(value="/purchaseinformation",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo purchaseinformation(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartsInformationVo> page=getPage(request);
		PageModel<PartsInformationVo> page1=getPage(request);
		page1.setEntities(new ArrayList<PartsInformationVo>());
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String[] wheres = where.split(" ");
		StringBuffer whereString = new StringBuffer();
		/*for (int i = 0; i < wheres.length; i++) {
			if (wheres[i].equals("e.PART_NUMBER_CODE")) {
				wheres[i] = "(cie.SHORT_PART_NUMBER";
				StringBuffer codeString = new StringBuffer();
				String partNumber = wheres[i+2];
				String code = clientInquiryService.getCodeFromPartNumber(partNumber);
				if (wheres[i+1].equals("=")) {
					codeString.append("'").append(code).append("'").append(" OR sqe.SHORT_PART_NUMBER = '").append(code).append("'").append(")");
				}else if (wheres[i+1].equals("like")) {
					codeString.append("'%").append(code).append("%'").append("OR sqe.SHORT_PART_NUMBER like '%").append(code).append("%'").append(")");
				}
				wheres[i+2] = codeString.toString();
			}
			whereString.append(wheres[i]).append(" ");
		}*/
		Boolean withPartNumber = false;
		String partNumber = getString(request, "partNumber");
		String partNumber2 = "";
		String way = getString(request, "way");
		if (partNumber != null && !"".equals(partNumber)) {
			partNumber2 = partNumber.replaceAll("[^0-9a-zA-Z]","");
			withPartNumber = true;
			if ("like".equals(way)) {
				page.put("like", "like");
				page.put("partNumber", "'%"+partNumber+"%'");
				page.put("partNumber2", "'%"+partNumber2+"%'");
			}else {
				page.put("partNumber", "'"+partNumber+"'");
			}
		}
		
		if ("".equals(where)) {
			where = null;
//			withPartNumber = true;
		}
//		else{
//			withPartNumber = false;
//
//		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		if (withPartNumber) {
			//
			clientInquiryElementService.purchasepartPageWithPart(page, where, sort);
		}else {
			//
			clientInquiryElementService.purchasepartPage(page, where, sort);
		}
		String exportModel = getString(request, "exportModel");
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartsInformationVo partsInformationVo : page.getEntities()) {
				List<Integer> checkList = new ArrayList<Integer>();
				Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("部件资料", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		

		return jqgrid;
	}
	
	/*
	 * 采购部件资料列表
	 */
	/**
	@Author: Modify by white
	@DateTime: 2018/10/10 9:25
	@Description:修改模糊查询问题 带横杠问题：如LF45、LF-45
	*/
	@RequestMapping(value="/purchaseinformationOnce",method=RequestMethod.POST)
	public @ResponseBody ResultVo purchaseinformationOnce(HttpServletRequest request,HttpServletResponse response) {
		PageModel<PartsInformationVo> page=getPage(request);
		PageModel<PartsInformationVo> page1=getPage(request);
		page1.setEntities(new ArrayList<PartsInformationVo>());
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = request.getParameter("searchString");
		String[] wheres = where.split(" ");
		StringBuffer whereString = new StringBuffer();
		Boolean withPartNumber = false;
		String partNumber = getString(request, "partNumber");
		String way = getString(request, "way");
		if (partNumber != null && !"".equals(partNumber)) {
			String [] array = partNumber.split("");
//			String partNumber2 = "";
//			for(int i = 0;i<array.length;i++){
//				partNumber2 = partNumber2+array[i]+"-";
//			}
//			System.out.println("拼接到的字符串"+partNumber2);
			withPartNumber = true;
			if ("like".equals(way)) {
				page.put("like", "like");
				page.put("partNumber", "'%"+partNumber+"%'");
//				page.put("partNumber2","'%"+partNumber2+"%'");
			}else {
				page.put("partNumber", "'"+partNumber+"'");
			}
		}
		if ("".equals(where)) {
			where = null;
		}
		RoleVo roleVo = userService.getPower(new Integer(userVo.getUserId()));
		if (!roleVo.getRoleName().equals("管理员") && !roleVo.getRoleName().equals("行政") && !roleVo.getRoleName().equals("财务")) {
			page.put("userId", userVo.getUserId());
		}
		
		if (withPartNumber) {
			clientInquiryElementService.purchasepartPageWithPart(page, where, sort);
		}else {
			clientInquiryElementService.purchasepartPage(page, where, sort);
		}
		String exportModel = getString(request, "exportModel");
		JSONArray json = new JSONArray();
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartsInformationVo partsInformationVo : page.getEntities()) {
				List<Integer> checkList = new ArrayList<Integer>();
				Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
				mapList.add(map);
			}
			json.add(mapList);
			//jqgrid.setRows(mapList);
			if (StringUtils.isNotEmpty(exportModel)) {
				try {
					exportService.exportGridToXls("部件资料", exportModel, mapList, response);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("导出数据出错!", e);
				}
			}
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		

		return new ResultVo(true, json.toString());
	}
	
	
	
	/**
	 * 客户能力清单
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/toAbility",method=RequestMethod.GET)
	public String toAbility(HttpServletRequest request) throws UnsupportedEncodingException {
		String searchString = java.net.URLDecoder.decode(request.getParameter("searchString").replaceAll("%", "%25"),"UTF-8");
		request.setAttribute("searchString", searchString);
		return "/purchase/partsinformation/supplierAbilityList";
	}
	
	/**
	 * 客户能力清单列表
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/abilityList",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo abilityList(HttpServletRequest request) throws UnsupportedEncodingException {
		PageModel<PartsInformationVo> page=getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo=getCurrentUser(request);
		GridSort sort = getSort(request);
		String where = java.net.URLDecoder.decode(request.getParameter("searchString").replaceAll("%", "%25"),"UTF-8");
		String[] wheres = where.split(" ");
		StringBuffer whereString = new StringBuffer();
		for (int i = 0; i < wheres.length; i++) {
			if (wheres[i].equals("e.PART_NUMBER_CODE")) {
				wheres[i] = "(cie.SHORT_PART_NUMBER";
				StringBuffer codeString = new StringBuffer();
				String partNumber = wheres[i+2];
				String code = clientInquiryService.getCodeFromPartNumber(partNumber);
				if (wheres[i+1].equals("=")) {
					codeString.append("'").append(code).append("'").append(" OR ciie.SHORT_PART_NUMBER = '").append(code).append("'").append(")");
				}else if (wheres[i+1].equals("like")) {
					codeString.append("'%").append(code).append("%'").append("OR ciie.SHORT_PART_NUMBER like '%").append(code).append("%'").append(")");
				}
				wheres[i+2] = codeString.toString();
			}
			whereString.append(wheres[i]).append(" ");
		}
		clientInquiryElementService.supplierAbilityPage(page, whereString.toString(), sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (PartsInformationVo partsInformationVo : page.getEntities()) {
				
				Map<String, Object> map = EntityUtil.entityToTableMap(partsInformationVo);
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
	 * 采购部件资料
	 */
	@RequestMapping(value="/toAbilityList",method=RequestMethod.GET)
	public String toAbilityList(HttpServletRequest request) {
		return "/marketing/partsinformation/ability";
	}
	
	/**
	 * 供应商能力
	 * @return
	 */
	@RequestMapping(value="/toSupplierAbility",method=RequestMethod.GET)
	public String toSupplierAbility() {
		return "/purchase/supplierability/ability";
	}
	
	
	/**
	 * 根据cage查询能力清单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/supplierAbilityByCage",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierAbilityByCage(HttpServletRequest request) {
		PageModel<SupplierAbilityVo> page=getPage(request);
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
		
		supplierCageRelationService.listByAbilityPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierAbilityVo supplierAbilityVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierAbilityVo);
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
	 * 根据件号查询能力清单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/supplierAbilityByPartNumber",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierAbilityByPartNumber(HttpServletRequest request) {
		PageModel<SupplierAbilityVo> page=getPage(request);
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
		
		supplierPnRelationService.listPage(page, where, sort);
		
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierAbilityVo supplierAbilityVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierAbilityVo);
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
	 * 根据件号查询能力清单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/supplierAbilityBySupplier",method=RequestMethod.POST)
	public @ResponseBody JQGridMapVo supplierAbilityBySupplier(HttpServletRequest request) {
		PageModel<SupplierAbilityVo> page=getPage(request);
		PageModel<SupplierAbilityVo> page1=getPage(request);
		PageModel<SupplierAbilityVo> page2=getPage(request);
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
		supplierCageRelationService.listByAbilityPage(page2, where, sort);
		if (page2.getPageCount() < page2.getPageNo()) {
			page1.setPageNo(page2.getPageNo() - page2.getPageCount());
			supplierPnRelationService.listPage(page1, where, sort);
			page.setEntities(page1.getEntities());
		}else {
			supplierPnRelationService.listPage(page1, where, sort);
			page.setEntities(page2.getEntities());
		}
		
		if (page1.getRecordCount() == 0) {
			page1.setPageCount(0);
		}
		if (page2.getRecordCount() == 0) {
			page2.setPageCount(0);
		}
		page.setRecordCount(page1.getRecordCount()+page2.getRecordCount());
		page.setPageCount(page1.getPageCount()+page2.getPageCount());
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierAbilityVo supplierAbilityVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierAbilityVo);
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
}
